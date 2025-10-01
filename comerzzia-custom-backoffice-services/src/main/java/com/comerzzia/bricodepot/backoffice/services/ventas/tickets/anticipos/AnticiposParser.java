package com.comerzzia.bricodepot.backoffice.services.ventas.tickets.anticipos;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.comerzzia.bricodepot.backoffice.persistence.albaranes.venta.pagos.anticipos.Anticipos;
import com.comerzzia.bricodepot.backoffice.services.tickets.auditoria.EventoAuditoriaException;
import com.comerzzia.bricodepot.backoffice.util.anticipos.AnticiposConstants;
import com.comerzzia.core.util.xml.XMLDocumentNodeNotFoundException;
import com.comerzzia.core.util.xml.XMLDocumentUtils;

public class AnticiposParser {

	protected static final Logger log = Logger.getLogger(AnticiposParser.class);

	protected static final String UID_ACTIVIDAD_TAG = "uidActividad";
	protected static final String UID_ANTICIPO_TAG = "uidAnticipo";
	protected static final String NUM_ANTICIPO_TAG = "numAnticipo";
	protected static final String IMPORTE_ANTICIPO_TAG = "importeAnticipo";
	protected static final String ID_CLIE_ALBARAN_TAG = "idClieAlbaran";
	protected static final String OPERACION_ANTICIPO_TAG = "operacionAnticipo";

	public static Anticipos parse(Element root) throws EventoAuditoriaException {
		try {
			log.debug("parse() - Iniciando la transformación de un XML con los datos del cierre de caja a su representación en objeto");

			Anticipos anticipo = new Anticipos();

			anticipo.setUidActividad(XMLDocumentUtils.getTagValueAsString(root, UID_ACTIVIDAD_TAG, false));
			anticipo.setNumAnticipo(XMLDocumentUtils.getTagValueAsString(root, NUM_ANTICIPO_TAG, true));

			String importeAnticipo = XMLDocumentUtils.getTagValueAsString(root, IMPORTE_ANTICIPO_TAG, false);
			Long idClieAlbaran = null;
			try {
				idClieAlbaran = XMLDocumentUtils.getTagValueAsLong(root, ID_CLIE_ALBARAN_TAG, false);
			}catch(Exception e){
				
			}
			String operacionAlbaran = XMLDocumentUtils.getTagValueAsString(root, OPERACION_ANTICIPO_TAG, false);

			if (operacionAlbaran.equals(AnticiposConstants.PARAMETRO_PAGAR_ANTICIPO)) {
				anticipo.setEstado(AnticiposConstants.PARAMETRO_ESTADO_LIQUIDADO);
			}else if(operacionAlbaran.equals(AnticiposConstants.PARAMETRO_ESTADO_DEVUELTO)) {
				anticipo.setEstado(AnticiposConstants.PARAMETRO_ESTADO_DEVUELTO);
			}

			anticipo.setIdClieAlbaran(idClieAlbaran);
			anticipo.setImporte(importeAnticipo);

			return anticipo;
		}
		catch (XMLDocumentNodeNotFoundException e) {
			String msg = "Error en el tratamiento del documento XML: " + e.getMessage();
			log.error("parse() - " + msg);

			throw new EventoAuditoriaException(msg, e);
		}
		catch (Exception e) {
			String msg = "Error en el tratamiento del documento XML: " + e.getMessage();
			log.error("parse() - " + msg, e);

			throw new EventoAuditoriaException(msg, e);
		}
	}

}
