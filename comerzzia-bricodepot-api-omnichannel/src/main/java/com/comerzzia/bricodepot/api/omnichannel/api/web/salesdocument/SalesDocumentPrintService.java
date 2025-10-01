package com.comerzzia.bricodepot.api.omnichannel.api.web.salesdocument;

import java.util.Base64;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SalesDocumentPrintService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SalesDocumentPrintService.class);
    private static final String MIME_TYPE_PDF = "application/pdf";

    private final FacturaA4ReportManager gestorFactura;

    public SalesDocumentPrintService(FacturaA4ReportManager gestorFactura) {
        this.gestorFactura = gestorFactura;
    }

    public Optional<SalesDocumentPrintResponse> imprimirDocumento(String identificadorDocumento,
            boolean esCopia,
            String plantillaSolicitada,
            String nombreSalida,
            Map<String, Object> parametrosPersonalizados) {
        try {
            Optional<FacturaPdfResultado> posibleFactura = gestorFactura.generarFactura(
                    identificadorDocumento,
                    esCopia,
                    plantillaSolicitada,
                    nombreSalida,
                    parametrosPersonalizados);

            if (!posibleFactura.isPresent()) {
                return Optional.empty();
            }

            FacturaPdfResultado factura = posibleFactura.get();
            String contenidoCodificado = Base64.getEncoder().encodeToString(factura.getContenidoPdf());
            SalesDocumentPrintResponse respuesta = new SalesDocumentPrintResponse(
                    MIME_TYPE_PDF,
                    factura.getNombreFichero(),
                    contenidoCodificado);
            return Optional.of(respuesta);
        } catch (SalesDocumentPrintException excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            LOGGER.error("Error generando factura para el documento {}", identificadorDocumento, excepcion);
            throw new SalesDocumentPrintException("Error generando la factura", excepcion);
        }
    }
}
