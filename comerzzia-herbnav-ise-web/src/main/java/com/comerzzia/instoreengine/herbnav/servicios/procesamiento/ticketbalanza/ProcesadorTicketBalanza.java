package com.comerzzia.instoreengine.herbnav.servicios.procesamiento.ticketbalanza;

import java.net.InetAddress;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.comerzzia.core.model.empresas.ConfigEmpresaBean;
import com.comerzzia.core.model.ventas.tickets.TicketBean;
import com.comerzzia.core.servicios.empresas.Empresa;
import com.comerzzia.core.servicios.procesamiento.IProcesadorDocumento;
import com.comerzzia.core.servicios.procesamiento.ProcesadorDocumentoException;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.servicios.variables.ServicioVariablesImpl;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.core.util.xml.XMLDocumentException;
import com.comerzzia.core.util.xml.XMLDocumentUtils;
import com.comerzzia.instoreengine.herbnav.util.HerbNavUtils;
import com.comerzzia.instoreengine.util.AppISE;

public class ProcesadorTicketBalanza implements IProcesadorDocumento {

	private Logger log = Logger.getLogger(ProcesadorTicketBalanza.class);

	protected DatosSesionBean datosSesion;
	
	public boolean procesaDocumento(Connection conn, DatosSesionBean datosSesion, TicketBean ticket, SqlSession sqlSession) throws ProcesadorDocumentoException {
		log.debug("procesaDocumento() - Procesando documento de Ticket Balanza con UID: " + ticket.getUidTicket());
		
		boolean procesado = false;

		try {
			String idTicketBalanza = obtenerIdTicketBalanza(ticket);
			procesado = procesarFicheroFTP(idTicketBalanza);
		}
		catch (Exception e) {
			log.error("procesaDocumento() - Se ha producido un error al procesar el ticket de balanza: " + e.getMessage(), e);
			throw new RuntimeException("Error al procesar el ticket de balanza: " + e.getMessage(), e);
		}

		return procesado;
	}

	private String obtenerIdTicketBalanza(TicketBean ticket) {
		log.debug("obtenerIdTicketBalanza() - Obteniendo el ID del ticket balanza");

		String idTicketBalanza = "";
		try {
			Element root;
			root = ticket.getXml().getDocumentElement();
			idTicketBalanza = XMLDocumentUtils.getTagValueAsString(root, "idTicketBalanza", false);
		}
		catch (XMLDocumentException e) {
			log.error("obtenerIdTicketBalanza() - Error obteniendo ID del ticket balanza: " + e.getMessage());
		}

		return idTicketBalanza;
	}

	@SuppressWarnings("deprecation")
	private boolean procesarFicheroFTP(String idTicketBalanza) throws Exception {
		log.debug("procesarFicheroFTP() - Procesando ticket balanza con ID: " + idTicketBalanza + " en el FTP");
		boolean procesado = false;

		FTPClient ftpClient = new FTPClient();
		String path = "";
		try {
			/* Conexión con FTP */
			datosSesion = new DatosSesionBean();
			datosSesion.setUidActividad(AppISE.getPosConfig().getPosConfigDatos().getUidActividad());
			ConfigEmpresaBean config = new ConfigEmpresaBean();
			config.setUidActividad(datosSesion.getUidActividad());
			Empresa empresa = new Empresa(null, config);
			datosSesion.setEmpresa(empresa);

			String host = ServicioVariablesImpl.get().consultarValor(datosSesion, HerbNavUtils.VARIABLE_X_BALANZA_FTP_HOST);
			String port = ServicioVariablesImpl.get().consultarValor(datosSesion, HerbNavUtils.VARIABLE_X_BALANZA_FTP_PORT);
			String user = ServicioVariablesImpl.get().consultarValor(datosSesion, HerbNavUtils.VARIABLE_X_BALANZA_FTP_USER);
			String password = ServicioVariablesImpl.get().consultarValor(datosSesion, HerbNavUtils.VARIABLE_X_BALANZA_FTP_PASS);
			path = ServicioVariablesImpl.get().consultarValor(datosSesion, HerbNavUtils.VARIABLE_X_BALANZA_FTP_RUTA_EXPORTACION);
			String directorioProcesados = path + "/" + HerbNavUtils.DIRECTORIO_PROCESADOS;

			log.debug("procesarFicheroFTP() - Datos de conexion del FTP");
			log.debug("procesarFicheroFTP() - Url: " + host);
			log.debug("procesarFicheroFTP() - Puerto: " + port);
			log.debug("procesarFicheroFTP() - Usuario: " + user);
			log.debug("procesarFicheroFTP() - Contraseña: " + password);
			log.debug("procesarFicheroFTP() - Ruta Exportacion: " + path);
			log.debug("procesarFicheroFTP() - Directorio Procesados: " + directorioProcesados);

			ftpClient.connect(InetAddress.getByName(host), new Integer(port));
			ftpClient.enterLocalPassiveMode();
			ftpClient.login(user, password);

			/* Operaciones */
			String ficheroViejo = path + "/" + idTicketBalanza + ".xml";
			String ficheroNuevo = directorioProcesados + "/" + idTicketBalanza + ".xml";
			boolean archivoMovido = ftpClient.rename(ficheroViejo, ficheroNuevo);
			if (archivoMovido) {
				log.debug("procesarFicheroFTP() - Fichero movido con éxito: " + idTicketBalanza + ".xml");
				procesado = true;
			}
			else {
				log.error("procesarFicheroFTP() - Error moviendo el fichero " + idTicketBalanza + ".xml");
			}
		}
		catch (Exception e) {
			log.error("procesarFicheroFTP() - Ha ocurrido un error al mover el fichero: " + idTicketBalanza + " en el FTP: " + e.getMessage(), e);
			throw e;
		}
		finally {
			try {
				caducarFicherosFTP(ftpClient, path);
				ftpClient.disconnect();
			}
			catch (Exception e) {
				log.error("procesarFicheroFTP() - Error al hacer desconexión de FTP: " + e.getMessage(), e);
			}

		}

		return procesado;
	}

	@SuppressWarnings("deprecation")
	private void caducarFicherosFTP(FTPClient ftpClient, String path) {
		log.debug("caducarFicherosFTP() - Moviendo ficheros caducados en el FTP");

		try {
			Calendar calendar = Calendar.getInstance();
			String diasCaducidad = ServicioVariablesImpl.get().consultarValor(datosSesion, HerbNavUtils.VARIABLE_X_BALANZA_DIAS_CADUCIDAD_TICKETS);
			calendar.add(Calendar.DATE, -Integer.parseInt(diasCaducidad));

			calendar = DateUtils.truncate(calendar, Calendar.DATE);
			Date fechaCaducidad = calendar.getTime();

			log.debug("caducarFicherosFTP() - Fecha de caducidad: " + fechaCaducidad);

			FTPFileFilter filter = ftpFile -> (ftpFile.isFile() && !DateUtils.truncate(ftpFile.getTimestamp(), Calendar.DATE).getTime().after(fechaCaducidad));
			FTPFile[] files = ftpClient.listFiles(path, filter);

			for (FTPFile file : files) {
				String fileName = file.getName();
				String directorioCaducados = path + "/" + HerbNavUtils.DIRECTORIO_CADUCADOS;

				ftpClient.changeWorkingDirectory(directorioCaducados);

				boolean archivoMovido = ftpClient.rename(path + "/" + fileName, fileName);
				if (archivoMovido) {
					log.debug("caducarFicherosFTP() - Archivo " + fileName + " con fecha " + file.getTimestamp().getTime() + " movido correctamente.");
				}
				else {
					log.error("caducarFicherosFTP() - No se pudo mover el archivo " + fileName + ".");
				}
			}
		}
		catch (Exception e) {
			log.error("caducarFicherosFTP() - Error caducando los ficheros en el FTP.");
		}
	}
}
