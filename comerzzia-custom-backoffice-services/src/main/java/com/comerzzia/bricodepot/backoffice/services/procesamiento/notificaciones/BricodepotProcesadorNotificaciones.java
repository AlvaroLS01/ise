package com.comerzzia.bricodepot.backoffice.services.procesamiento.notificaciones;

import java.io.ByteArrayInputStream;

import com.comerzzia.core.model.notificaciones.NotificacionModel;
import com.comerzzia.core.model.ventas.tickets.TicketBean;
import com.comerzzia.core.servicios.ContextHolder;
import com.comerzzia.core.servicios.notificaciones.GeneradorMensajesNotificacion;
import com.comerzzia.core.servicios.notificaciones.NotificacionesFactory;
import com.comerzzia.core.servicios.procesamiento.IProcesadorDocumento;
import com.comerzzia.core.servicios.procesamiento.ProcesadorDocumentoException;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.servicios.procesamiento.notificaciones.GeneradorMensajesNotificacionImpl;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class BricodepotProcesadorNotificaciones implements IProcesadorDocumento {

	private static final Logger log = Logger.getLogger(BricodepotProcesadorNotificaciones.class);

	public static final String TIPO_NOTIFICACION_ENVIO_TICKET = "ENVIO_TICKET_EMAIL";

	@Autowired
	protected GeneradorMensajesNotificacion generadorMensajesNotificacion;

	@Override
	public boolean procesaDocumento(Connection conn, DatosSesionBean datosSesion, TicketBean ticket, SqlSession sqlSession) throws ProcesadorDocumentoException {
		log.debug(String.format("procesaDocumento() - Obteniendo el modelo de la notificación a través del ticket con uid [%s] ", ticket.getUidTicket()));

		boolean procesado = false;
		try {
			NotificacionModel notificacion = null;
			try {
				notificacion = NotificacionesFactory.createNotificacionModelFromXml(new ByteArrayInputStream(ticket.getTicket()));
				if (notificacion == null) {
					String msgError = "No se ha podido generar la notificación ya que el XML no tiene el formato o modelo correcto.";
					throw new Exception(msgError);
				}
			}
			catch (Exception e) {
				String msgError = "Error al generar la notificación : " + e.getMessage();
				throw new Exception(msgError, e);
			}

			log.debug(String.format("procesaDocumento() - Procesando notificación de tipo [%s] para la actividad [%s] ", notificacion.getTipoNotificacion(), notificacion.getUidActividad()));

			// Notificaciones personalizadas para Bricodepot
			if (TIPO_NOTIFICACION_ENVIO_TICKET.equals(notificacion.getTipoNotificacion())) {

				BricodepotGeneradorMensajesNotificacionImpl generadorMensajesNotificacion = new BricodepotGeneradorMensajesNotificacionImpl();
				generadorMensajesNotificacion.setTicketNotificacion(ticket);
				generadorMensajesNotificacion.generarMensajes(conn, notificacion, datosSesion);

			}
			// Resto de notificaciones.
			else {
				GeneradorMensajesNotificacion generadorMensajesNotificacion = null;
				try {
					// Traemos del contexto el servicio estándar, no la interfaz, ya que en ese caso se volvería a
					// mapear el servicio personalizado debido a la anotacion Primary
					generadorMensajesNotificacion = (GeneradorMensajesNotificacion) ContextHolder.get().getBean("generadorMensajesNotificacionImpl");
				}
				catch (Exception e) {
					generadorMensajesNotificacion = new GeneradorMensajesNotificacionImpl();
				}
				generadorMensajesNotificacion.generarMensajes(conn, notificacion, datosSesion);
			}

			procesado = true;
		}
		catch (Exception e) {
			log.error("procesaDocumento() - " + e.getMessage(), e);
			throw new ProcesadorDocumentoException(e.getMessage(), e);
		}

		return procesado;
	}
}
