package com.comerzzia.bricodepot.backoffice.services.tickets.auditoria;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.comerzzia.bricodepot.backoffice.persistence.tickets.auditoria.EventoAuditoriaBean;
import com.comerzzia.core.util.xml.XMLDocumentNodeNotFoundException;
import com.comerzzia.core.util.xml.XMLDocumentUtils;

public class ParseadorEventoAuditoria {

	protected static final Logger log = Logger.getLogger(ParseadorEventoAuditoria.class);

	// Raíz del XML
	protected static final String ROOT_TAG = "evento";
	// Cabecera
	protected static final String UID_ACTIVIDAD_TAG = "uid_actividad";
	protected static final String UID_TICKET_VENTA_TAG = "uid_ticket_venta";
	protected static final String COD_ALMACEN_TAG = "cod_almacen";
	protected static final String FECHA_TAG = "fecha";
	protected static final String TIPO_TAG = "tipo";
	protected static final String DESCRIPCION_TAG = "des_evento";
	protected static final String PROCEDE_TAG = "procede";
	protected static final String ID_USUARIO_TAG = "id_usuario";
	protected static final String NOMBRE_USUARIO_TAG = "des_usuario";
	protected static final String ID_USUARIO_SUPERVISOR_TAG = "id_supervisor";
	protected static final String NOMBRE_USUARIO_SUPERVISOR_TAG = "des_supervisor";
	protected static final String COD_ARTICULO_TAG = "cod_articulo";
	protected static final String DES_ARTICULO_TAG = "des_articulo";
	protected static final String CANTIDAD_ARTICULO_TAG = "can_articulo";
	protected static final String PRECIO_ORIGINAL_TAG = "precio_articulo_original";
	protected static final String PRECIO_APLICADO_TAG = "precio_articulo_aplicado";
	protected static final String LINEA_REFERENCIA_TAG = "linea_referencia";

	public static EventoAuditoriaBean parse(Element root) throws EventoAuditoriaException {
		try {
			log.debug("parse() - Iniciando la transformación de un XML con los datos del cierre de caja a su representación en objeto");

			EventoAuditoriaBean eventoAuditoria = new EventoAuditoriaBean();

			eventoAuditoria.setUidActividad(XMLDocumentUtils.getTagValueAsString(root, UID_ACTIVIDAD_TAG, false));
			eventoAuditoria.setUidTicketVenta(XMLDocumentUtils.getTagValueAsString(root, UID_TICKET_VENTA_TAG, true));
			// fecha
			OffsetDateTime odtEvento = OffsetDateTime
					.parse(XMLDocumentUtils.getTagValueAsString(root, FECHA_TAG, false));
			Instant instantEvento = odtEvento.toInstant();
			eventoAuditoria.setFecha(Date.from(instantEvento));
			// eventoAuditoria.setFecha(root.getNodo(FECHA_TAG).getValue());

			String codAlmacen = XMLDocumentUtils.getTagValueAsString(root, COD_ALMACEN_TAG, false);
			String tipoEvent = XMLDocumentUtils.getTagValueAsString(root, TIPO_TAG, false);
			String descripcionEvento = XMLDocumentUtils.getTagValueAsString(root, DESCRIPCION_TAG, false);
			Boolean procede = Boolean.valueOf(XMLDocumentUtils.getTagValueAsString(root, PROCEDE_TAG, false));
			Long idUsuario = Long.valueOf(XMLDocumentUtils.getTagValueAsString(root, ID_USUARIO_TAG, false));
			String nombreUsuario = XMLDocumentUtils.getTagValueAsString(root, NOMBRE_USUARIO_TAG, false);
			// para los tickets descartados no hay supervisor
			if (!XMLDocumentUtils.getTagValueAsString(root, ID_USUARIO_SUPERVISOR_TAG, true).isEmpty()) {
				eventoAuditoria.setIdUsuarioSupervisor(
						Long.valueOf(XMLDocumentUtils.getTagValueAsString(root, ID_USUARIO_SUPERVISOR_TAG, true)));

				eventoAuditoria.setNombreUsuarioSupervisor(
						XMLDocumentUtils.getTagValueAsString(root, NOMBRE_USUARIO_SUPERVISOR_TAG, true));
			}

			String codArt = XMLDocumentUtils.getTagValueAsString(root, COD_ARTICULO_TAG, true);
			String desArt = XMLDocumentUtils.getTagValueAsString(root, DES_ARTICULO_TAG, true);

			if (!XMLDocumentUtils.getTagValueAsString(root, CANTIDAD_ARTICULO_TAG, true).isEmpty()) {
				eventoAuditoria.setCantidadArticulo(
						new BigDecimal(XMLDocumentUtils.getTagValueAsString(root, CANTIDAD_ARTICULO_TAG, true)));
			}
			if (!XMLDocumentUtils.getTagValueAsString(root, PRECIO_ORIGINAL_TAG, true).isEmpty()) {
				eventoAuditoria.setPrecioOriginal(
						new BigDecimal(XMLDocumentUtils.getTagValueAsString(root, PRECIO_ORIGINAL_TAG, true)));
			}
			if (!XMLDocumentUtils.getTagValueAsString(root, PRECIO_APLICADO_TAG, true).isEmpty()) {
				eventoAuditoria.setPrecioAplicado(
						new BigDecimal(XMLDocumentUtils.getTagValueAsString(root, PRECIO_APLICADO_TAG, true)));
			}

			/* Se añade la linea que ha generado ese evento auditoria */
			String lineaReferencia = XMLDocumentUtils.getTagValueAsString(root, LINEA_REFERENCIA_TAG, true);
			if (StringUtils.isNotBlank(lineaReferencia)) {
				eventoAuditoria.setLineaReferencia(Integer.parseInt(lineaReferencia));
			}
			
			eventoAuditoria.setCodigoAlmacen(codAlmacen);
			eventoAuditoria.setTipoEvento(tipoEvent);
			eventoAuditoria.setDescripcionEvento(descripcionEvento);
			eventoAuditoria.setProcede(procede);
			eventoAuditoria.setIdUsuario(idUsuario);
			eventoAuditoria.setNombreUsuario(nombreUsuario);

			eventoAuditoria.setCodigoArticulo(codArt);
			eventoAuditoria.setDescripcionArticulo(desArt);

			return eventoAuditoria;
		} catch (XMLDocumentNodeNotFoundException e) {
			String msg = "Error en el tratamiento del documento XML: " + e.getMessage();
			log.error("parse() - " + msg);

			throw new EventoAuditoriaException(msg, e);
		} catch (Exception e) {
			String msg = "Error en el tratamiento del documento XML: " + e.getMessage();
			log.error("parse() - " + msg, e);

			throw new EventoAuditoriaException(msg, e);
		}
	}
}
