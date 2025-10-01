package com.comerzzia.bricodepot.backoffice.services.procesamiento.fidelizados;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.comerzzia.bricodepot.backoffice.services.tickets.fidelizado.ParseadorFidelizado;
import com.comerzzia.bricodepot.backoffice.services.tickets.fidelizado.TicketFidelizadoCaptacion;
import com.comerzzia.core.model.ventas.tickets.TicketBean;
import com.comerzzia.core.servicios.procesamiento.IProcesadorDocumento;
import com.comerzzia.core.servicios.procesamiento.ProcesadorDocumentoException;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.servicios.variables.ServicioVariablesImpl;
import com.comerzzia.core.servicios.variables.VariableException;
import com.comerzzia.core.servicios.variables.VariableNotFoundException;
import com.comerzzia.core.util.db.Connection;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

@SuppressWarnings("deprecation")
public class ProcesadorCaptacionFidelizados implements IProcesadorDocumento {

	protected static Logger log = Logger.getLogger(ProcesadorCaptacionFidelizados.class);

	public static final String SFTP_PASS = "SFTP.PASS";
	public static final String SFTP_USER = "SFTP.USER";
	public static final String SFTP_IP = "SFTP.IP";
	public static final String SFTP_PORT = "SFTP.PORT";
	public static final String SFTP_PATH_CONSENTIMIENTOS = "SFTP.PATH_CONSENTIMIENTOS";

	@Override
	public boolean procesaDocumento(Connection conn, DatosSesionBean datosSesion, TicketBean ticket, SqlSession sqlSession) throws ProcesadorDocumentoException {
		boolean procesado = false;
		try {

			TicketFidelizadoCaptacion ticketFidelizado = ParseadorFidelizado.parse(ticket.getXml().getDocumentElement());

			InputStream is = new ByteArrayInputStream(ticketFidelizado.getPdfFidelizado());
			uploadFile(is, ticketFidelizado.getIdFidelizado(), ticketFidelizado.getFechaAlta());

			procesado = true;

		}
		catch (Exception e) {
			throw new ProcesadorDocumentoException("Error procesando el documento: " + e.getMessage(), e);
		}
		return procesado;
	}

	public boolean uploadFile(InputStream is, Long idFidelizado, String fechaAlta) {
		log.debug("uploadFile() - Inicio del proceso de subida de pdf");
		ChannelSftp channelSftp = createChannelSftp();
		try {
			String path = ServicioVariablesImpl.get().consultarDefinicion(SFTP_PATH_CONSENTIMIENTOS);
			String pathCompleto = path + "/" + idFidelizado + "_" + fechaAlta + ".pdf";

			channelSftp.cd(path);

			log.debug("uploadFile() - Ruta donde se realizara la subida del pdf [" + pathCompleto + "]");
			channelSftp.put(is, pathCompleto);
			return true;
		}
		catch (SftpException ex) {
			log.error("uploadFile() - Error realizando la subida del fichero al sftp", ex);
		}
		catch (VariableException | VariableNotFoundException e) {
			log.error("uploadFile() - No se ha encontrado la variable.", e);
		}
		finally {
			disconnectChannelSftp(channelSftp);
		}

		return false;
	}

	private ChannelSftp createChannelSftp() {
		log.debug("createChannelSftp() - Inicio de conexion al servidor sftp");
		try {
			String ip = ServicioVariablesImpl.get().consultarDefinicion(SFTP_IP);
			String port = ServicioVariablesImpl.get().consultarDefinicion(SFTP_PORT);
			String user = ServicioVariablesImpl.get().consultarDefinicion(SFTP_USER);
			String pass = ServicioVariablesImpl.get().consultarDefinicion(SFTP_PASS);

			JSch jSch = new JSch();
			Session session = jSch.getSession(user, ip, Integer.parseInt(port));
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(pass);
			session.connect();
			Channel channel = session.openChannel("sftp");
			channel.connect();
			return (ChannelSftp) channel;
		}
		catch (JSchException ex) {
			log.error("createChannelSftp() - Error al conectarse con el servidor sftp", ex);
		}
		catch (VariableException | VariableNotFoundException e) {
			log.error("createChannelSftp() - No se ha encontrado la variable. ", e);
		}

		return null;
	}

	private void disconnectChannelSftp(ChannelSftp channelSftp) {
		log.debug("disconnectChannelSftp() - Se procede a desconectarse el servidor sftp");
		try {
			if (channelSftp == null)
				return;

			if (channelSftp.isConnected())
				channelSftp.disconnect();

			if (channelSftp.getSession() != null)
				channelSftp.getSession().disconnect();

		}
		catch (Exception ex) {
			log.error("disconnectChannelSftp() - Ha ocurrido un error al realizar la desconexion con el servidor sftp", ex);
		}
	}
}
