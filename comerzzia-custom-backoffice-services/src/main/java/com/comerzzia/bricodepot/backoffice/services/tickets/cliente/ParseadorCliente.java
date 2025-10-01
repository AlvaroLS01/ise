package com.comerzzia.bricodepot.backoffice.services.tickets.cliente;

import java.util.Base64;

import com.comerzzia.core.util.xml.XMLDocumentNodeNotFoundException;
import com.comerzzia.core.util.xml.XMLDocumentUtils;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

public class ParseadorCliente {

	protected static final Logger log = Logger.getLogger(ParseadorCliente.class);

	public static final String PDF_FIDELIZADO = "pdf_cliente";
	public static final String CIF_CLIENTE = "cif_cliente";
	public static final String FECHA_ALTA = "fecha_alta";
	public static final String OPERACION = "operacion";

	public static TicketClienteCaptacion parse(Element root) throws CaptacionClienteException {
		log.debug("parse() - Iniciando la transformación de un XML con la informacion del fichero de facturacion de un cliente");
		try {

			TicketClienteCaptacion ticketCliente = new TicketClienteCaptacion();

			String pdfCliente = XMLDocumentUtils.getTagValueAsString(root, PDF_FIDELIZADO, false);
			String cif = XMLDocumentUtils.getTagValueAsString(root, CIF_CLIENTE, false);
			String fechaAlta = XMLDocumentUtils.getTagValueAsString(root, FECHA_ALTA, false);
			String operacion = XMLDocumentUtils.getTagValueAsString(root, OPERACION, false);

			byte[] decoded = Base64.getDecoder().decode(pdfCliente);

			ticketCliente.setPdfCliente(decoded);
			ticketCliente.setCif(cif);
			ticketCliente.setFechaAlta(fechaAlta);
			ticketCliente.setOperacion(operacion); //ALTA o MOD(ificación)

			return ticketCliente;
		}
		catch (XMLDocumentNodeNotFoundException e) {
			String msg = "Error en el tratamiento del documento XML: " + e.getMessage();
			log.error("parse() - " + msg);

			throw new CaptacionClienteException(msg, e);
		}
		catch (Exception e) {
			String msg = "Error en el tratamiento del documento XML: " + e.getMessage();
			log.error("parse() - " + msg, e);

			throw new CaptacionClienteException(msg, e);
		}
	}
}
