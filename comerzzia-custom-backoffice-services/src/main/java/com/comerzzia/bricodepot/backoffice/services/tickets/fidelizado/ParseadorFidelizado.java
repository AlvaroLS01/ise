package com.comerzzia.bricodepot.backoffice.services.tickets.fidelizado;

import java.util.Base64;

import com.comerzzia.core.util.xml.XMLDocumentNodeNotFoundException;
import com.comerzzia.core.util.xml.XMLDocumentUtils;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

public class ParseadorFidelizado {

	protected static final Logger log = Logger.getLogger(ParseadorFidelizado.class);

	public static final String PDF_FIDELIZADO = "pdf_fidelizado";
	public static final String ID_FIDELIZADO = "id_fidelizado";
	public static final String FECHA_ALTA = "fecha_alta";

	public static TicketFidelizadoCaptacion parse(Element root) throws CaptacionFidelizadoException {
		log.debug("parse() - Iniciando la transformación de un XML con la informacion del fichero de consentimiento de un fidelizado");
		try {

			TicketFidelizadoCaptacion ticketFidelizado = new TicketFidelizadoCaptacion();

			String pdfFidelizado = XMLDocumentUtils.getTagValueAsString(root, PDF_FIDELIZADO, false);
			Long idFidelizado = XMLDocumentUtils.getTagValueAsLong(root, ID_FIDELIZADO, false);
			String fechaAlta = XMLDocumentUtils.getTagValueAsString(root, FECHA_ALTA, false);

			byte[] decoded = Base64.getDecoder().decode(pdfFidelizado);

			ticketFidelizado.setPdfFidelizado(decoded);
			ticketFidelizado.setIdFidelizado(idFidelizado);
			ticketFidelizado.setFechaAlta(fechaAlta);

			return ticketFidelizado;
		}
		catch (XMLDocumentNodeNotFoundException e) {
			String msg = "Error en el tratamiento del documento XML: " + e.getMessage();
			log.error("parse() - " + msg);

			throw new CaptacionFidelizadoException(msg, e);
		}
		catch (Exception e) {
			String msg = "Error en el tratamiento del documento XML: " + e.getMessage();
			log.error("parse() - " + msg, e);

			throw new CaptacionFidelizadoException(msg, e);
		}
	}
}
