package com.comerzzia.bricodepot.backoffice.services.ventas.tickets;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

import com.comerzzia.bricodepot.backoffice.services.fidelizacion.fidelizados.BricodepotServicioFidelizadosImpl;
import com.comerzzia.core.model.ventas.tickets.TicketBean;
import com.comerzzia.core.omnichannel.engine.model.fidelizacion.contactos.FidelizadoContactoBean;
import com.comerzzia.core.servicios.notificaciones.NotificacionesFactory;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.servicios.ventas.tickets.ServicioTicketsImpl;
import com.comerzzia.core.util.base.Estado;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.core.util.xml.XMLDocumentUtils;
import com.comerzzia.model.fidelizacion.fidelizados.FidelizadoBean;
import com.comerzzia.model.fidelizacion.fidelizados.contactos.TiposContactoFidelizadoBean;
import com.comerzzia.model.fidelizacion.tarjetas.TarjetaBean;
import com.comerzzia.model.general.servicios.ServicioBean;
import com.comerzzia.model.general.servicios.contactos.ServicioContactosBean;
import com.comerzzia.servicios.fidelizacion.fidelizados.ServicioFidelizadosImpl;
import com.comerzzia.servicios.fidelizacion.tarjetas.ServicioTarjetasImpl;
import com.comerzzia.servicios.notificaciones.servicios.cambioestado.DestinatarioServicioCambioEstado;
import com.comerzzia.servicios.notificaciones.servicios.cambioestado.NotificacionServicioCambioEstado;
import com.comerzzia.servicios.ventas.albaranes.AlbaranVenta;

@Service
@Primary
public class BricodepotServicioTicketsImpl extends ServicioTicketsImpl {

	public static final String UID_DEVOLUCION_FLEXPOINT = "UID_DEVOLUCION_FLEXPOINT";

	public static final String SOLICITO_TICKET_POR_MAIL = "Solicito ticket por mail";

	private static final String UID_CONVERSION_FLEXPOINT = "FLEXPOINT";
	
	@Autowired
	private BricodepotServicioFidelizadosImpl fidelizadosService;

	@SuppressWarnings("deprecation")
	@Override
	protected boolean existeDocumentoOrigen(Connection arg0, TicketBean arg1) {
		Boolean esFlexPoint = compruebaDevFlexPoint(arg0, arg1);
		Boolean existeDocumento = Boolean.FALSE;
		if (!esFlexPoint) {
			existeDocumento = super.existeDocumentoOrigen(arg0, arg1);
		}
		log.debug("existeDocumentoOrigen " + existeDocumento + " ||||| " + esFlexPoint);
		return existeDocumento || esFlexPoint;
	}

	private boolean compruebaDevFlexPoint(Connection conn, TicketBean ticket) {
		Element root = null;
		Boolean resultado = Boolean.FALSE;
		try {
			// Parseamos el XML
			root = ticket.getXml().getDocumentElement();
		}
		catch (Exception e) {
			// devolvemos true porque el xml no esta correcto y el error lo registrara el
			// procesador
			log.error("compruebaDevFlexPoint - El documento " + ticket.getUidTicket() + " no esta bien formado", e);
			return false;
		}

		try {
			// Extraemos los datos de la cabecera
			Element cabecera = XMLDocumentUtils.getElement(root, "cabecera", true); // es opcional porque tenemos que
			if (cabecera != null) {
				// Obtenemos del documento origen
				Element datosDocumentoOrigen = XMLDocumentUtils.getElement(cabecera, "datos_documento_origen", true);
				if (datosDocumentoOrigen != null) {
					String uidTicket = XMLDocumentUtils.getTagValueAsString(datosDocumentoOrigen, "uidTicket", true);
					String codTipoDocumento = XMLDocumentUtils.getTagValueAsString(datosDocumentoOrigen, "cod_tipo_documento", true);
					if (StringUtils.isNotBlank(uidTicket) && uidTicket.equals(UID_DEVOLUCION_FLEXPOINT)) {
						log.info("existeDocumentoOrigen() - Se trata de una devolución de flexPoint");
						resultado = true;
					}else if(codTipoDocumento.equals("FLEX")) {
						log.info("existeDocumentoOrigen() - Se trata de una conversión flexpoint");
						resultado = true;
					}
				}
			}

			return resultado;
		}
		catch (Exception e) {
			log.error("compruebaDevFlexPoint - Error al extraer los datos del documento origen del documento " + ticket.getUidTicket(), e);
			return false;
		}
	}

	public void procesoNotificacionEmail(TicketBean ticket, DatosSesionBean datosSesion, AlbaranVenta albaran, Connection conn, SqlSession sqlSession) throws Exception {
		log.debug("procesoNotificacionEmail() - Inicio del proceso de generacion de notificacion por email del ticket de venta");
		Element root = null;
		try {
			// Parseamos el XML
			root = ticket.getXml().getDocumentElement();
		}
		catch (Exception e) {
			log.error("procesoNotificacionEmail - El documento " + ticket.getUidTicket() + " no esta bien formado", e);
		}
		
		Element cabecera = XMLDocumentUtils.getElement(root, "cabecera", true);

		String email = XMLDocumentUtils.getTagValueAsString(cabecera, "email_envio_ticket", true);
		if (StringUtils.isNotBlank(email)) {
			tratamientoEmailEnvioTicket(email, datosSesion, albaran, conn, sqlSession);
		}
	}

	public String generateEmailNotification(DatosSesionBean sessionData, String tipoNotificacion, TicketBean ticket, SqlSession sqlSession) throws Exception {
		log.debug("generateEmailNotification() - Iniciando proceso de envio de correo con el ticket de compra");
		String email = null;
		Element root = null;
		try {
			// Parseamos el XML
			root = ticket.getXml().getDocumentElement();
		}
		catch (Exception e) {
			log.error("procesoNotificacionEmail - El documento " + ticket.getUidTicket() + " no esta bien formado", e);
		}
		Element cabecera = XMLDocumentUtils.getElement(root, "cabecera", true);

		email = XMLDocumentUtils.getTagValueAsString(cabecera, "email_envio_ticket", true);
		if (StringUtils.isNotBlank(email)) {
			log.debug("generateEmailNotification() - email obtenido: " + email);

			try {
				Element cliente = XMLDocumentUtils.getElement(cabecera, "cliente", true);
				String paisCliente = null;

				if (cliente != null) {
					paisCliente = XMLDocumentUtils.getTagValueAsString(cliente, "pais", true);
				}
				
				Element empresa = XMLDocumentUtils.getElement(cabecera, "empresa", true);
				String codEmp = null;

				if (empresa != null) {
					codEmp = XMLDocumentUtils.getTagValueAsString(empresa, "codigo", true);
				}
				
				sendNotificacionTicketEmail(generarServicio(ticket), sessionData, sqlSession, email, paisCliente, codEmp);
			}
			catch (Exception e) {
				String msgError = sessionData.getTranslation()
				        .getText("Error al generar la notificacion para el email: " + email + ", tipo notificacion:" + tipoNotificacion + ", error:" + e.getMessage());
				log.error("generateEmailNotification() - " + msgError);
				return null;
			}
		}

		return email;
	}

	@SuppressWarnings("deprecation")
	public void tratamientoEmailEnvioTicket(String email, DatosSesionBean datosSesion, AlbaranVenta albaran, Connection conn, SqlSession sqlSession) throws Exception {
		log.debug("tratamientoEmailEnvioTicket() - Comprobacion del email: " + email);

		List<FidelizadoContactoBean> listaFidelizados = fidelizadosService.buscaFidelizadosPorEmailCaseInSensitive(datosSesion, email);
		if (listaFidelizados != null && !listaFidelizados.isEmpty()) {
			log.debug("tratamientoEmailEnvioTicket() - Ya existe un fidelizado con ese correo. Se añade la tarjeta de fidelizado al albaran");
			List<TarjetaBean> listaTarjetas = ServicioTarjetasImpl.get().consultarTarjetasCliente(conn, datosSesion, listaFidelizados.get(0).getIdFidelizado());
			if (listaTarjetas != null && !listaTarjetas.isEmpty()) {
				String numeroTarjetaFidelizacion = listaTarjetas.get(0).getNumeroTarjeta();
				albaran.setTarjetaFidelizacion(numeroTarjetaFidelizacion);
				
				log.debug("tratamientoEmailEnvioTicket() - Se añade al albaran el numero de tarjeta fidelizacion " + numeroTarjetaFidelizacion);
			}
		}
		else {
			// Se rellena la empresa del datosSesion para el procesamiento de la notificación 'NUEVO_USUARIO_FIDELIZADO'
			// que se creará al final de este
			datosSesion.setEmpresaSeleccionada(albaran.getCodEmpresa());
			
			FidelizadoBean fidelizado = new FidelizadoBean();
			fidelizado.setNombre(SOLICITO_TICKET_POR_MAIL);
			fidelizado.setApellidos(SOLICITO_TICKET_POR_MAIL);
			fidelizado.setEstadoBean(Estado.NUEVO); // Llama a initNuevoBean();

			TiposContactoFidelizadoBean tiposContactoFidelizado = new TiposContactoFidelizadoBean();
			tiposContactoFidelizado.setEstadoBean(Estado.NUEVO);
			tiposContactoFidelizado.setCodTipoCon("EMAIL");
			tiposContactoFidelizado.setRecibeNotificaciones(true);
			tiposContactoFidelizado.setRecibeNotificacionesCom(false);
			tiposContactoFidelizado.setValor(email);
			fidelizado.getTiposContacto().add(tiposContactoFidelizado);

			fidelizado.getEtiquetas().add(SOLICITO_TICKET_POR_MAIL);

			fidelizadosService.crear(fidelizado, datosSesion, conn);

			List<TarjetaBean> listaTarjetas = ServicioTarjetasImpl.get().consultarTarjetasCliente(conn, datosSesion, fidelizado.getIdFidelizado());
			if (listaTarjetas != null && !listaTarjetas.isEmpty()) {
				String numeroTarjetaFidelizacion = listaTarjetas.get(0).getNumeroTarjeta();
				albaran.setTarjetaFidelizacion(numeroTarjetaFidelizacion);

				log.debug("tratamientoEmailEnvioTicket() - Se añade al albaran el numero de tarjeta fidelizacion " + numeroTarjetaFidelizacion);
			}
			else {
				log.warn("tratamientoEmailEnvioTicket() - No se ha podido recuperar la tarjeta del fidelizado " + fidelizado.getIdFidelizado());
			}
		}
	}
	
	protected void sendNotificacionTicketEmail(ServicioBean servicio, DatosSesionBean datosSesion, org.apache.ibatis.session.SqlSession sqlSession, String email, String codPais, String codEmp) {
		log.debug("sendNotificacionTicketEmail() - Generando notificación de ticket por email...");
		List<ServicioContactosBean> contactos = new ArrayList<ServicioContactosBean>();
		ServicioContactosBean emailContacto = new ServicioContactosBean();
		emailContacto.setUidActividad(datosSesion.getUidActividad());
		emailContacto.setUidServicio(servicio.getUidServicio());
		emailContacto.setCodtipocon("EMAIL");
		emailContacto.setDestipocon("EMAIL");
		emailContacto.setVisible("S");
		emailContacto.setActivo(true);
		emailContacto.setValor(email);
		contactos.add(emailContacto);

		servicio.setLstContactos(contactos);
		servicio.setContactosCargados(true);

		NotificacionServicioCambioEstado notificacion = NotificacionesFactory.createNotificacion("ENVIO_TICKET_EMAIL", codEmp, servicio.getCodalmOrigen(), datosSesion);

		DestinatarioServicioCambioEstado destinatario = notificacion.createDestinatario();
		for (ServicioContactosBean contacto : contactos) {
			destinatario.addContacto(contacto.getCodtipocon(), contacto.getValor());
		}

		destinatario.setServicio(servicio);
		notificacion.addDestinatario(destinatario);
		notificacion.setCodlengua(codPais);
		notificacion.guardar(sqlSession);
		
		log.debug("sendNotificacionTicketEmail() - Notificación generada correctamente");
	}
	
	private ServicioBean generarServicio(TicketBean ticket){
		ServicioBean servicio = new ServicioBean();
		servicio.setUidServicio(UUID.randomUUID().toString());
		servicio.setCodDocumentoOrigen(ticket.getCodTicket());
		servicio.setUidDocumentoOrigen(ticket.getUidTicket());
		servicio.setCodalmOrigen(ticket.getCodAlmacen());
		return servicio;
	}

}
