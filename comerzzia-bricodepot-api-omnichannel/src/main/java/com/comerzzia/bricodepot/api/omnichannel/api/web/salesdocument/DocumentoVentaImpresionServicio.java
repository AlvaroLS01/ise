package com.comerzzia.bricodepot.api.omnichannel.api.web.salesdocument;

import java.util.Base64;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentoVentaImpresionServicio {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentoVentaImpresionServicio.class);
    private static final String MIMETYPE_POR_DEFECTO = "application/pdf";

    private final GeneradorFacturaA4 generadorFactura;

    @Autowired
    public DocumentoVentaImpresionServicio(GeneradorFacturaA4 generadorFactura) {
        this.generadorFactura = generadorFactura;
    }

    public Optional<DocumentoVentaImpresionRespuesta> imprimir(String uidDocumento,
            OpcionesImpresionDocumentoVenta opciones) {

        if (StringUtils.isBlank(uidDocumento)) {
            throw new DocumentoVentaImpresionException("El identificador del documento es obligatorio");
        }

        try {
            Optional<ResultadoFacturaPdf> posibleFactura = generadorFactura.generarFactura(uidDocumento, opciones);
            if (!posibleFactura.isPresent()) {
                LOGGER.debug("imprimir() - No se encontró el documento de venta con uid '{}'", uidDocumento);
                return Optional.empty();
            }

            ResultadoFacturaPdf factura = posibleFactura.get();
            DocumentoVentaImpresionRespuesta respuesta = construirRespuesta(uidDocumento, opciones, factura);
            return Optional.of(respuesta);
        }
        catch (DocumentoVentaImpresionException excepcion) {
            throw excepcion;
        }
        catch (Exception excepcion) {
            throw new DocumentoVentaImpresionException("No fue posible generar el PDF del documento de venta", excepcion);
        }
    }

    private DocumentoVentaImpresionRespuesta construirRespuesta(String uidDocumento,
                                                                 OpcionesImpresionDocumentoVenta opciones,
                                                                 ResultadoFacturaPdf factura) {
        DocumentoVentaImpresionRespuesta respuesta = new DocumentoVentaImpresionRespuesta();
        respuesta.setUidDocumento(uidDocumento);
        respuesta.setCopia(opciones.esCopia());
        respuesta.setEnLinea(opciones.esEnLinea());
        respuesta.setNombreArchivo(factura.getNombreFichero());
        respuesta.setTipoMime(resolverMimeType(opciones.getTipoMime()));
        respuesta.setDocumento(Base64.getEncoder().encodeToString(factura.getContenidoPdf()));
        return respuesta;
    }

    private String resolverMimeType(String tipoSolicitado) {
        if (tipoSolicitado == null) {
            return MIMETYPE_POR_DEFECTO;
        }
        String normalizado = tipoSolicitado.trim();
        if (normalizado.isEmpty()) {
            return MIMETYPE_POR_DEFECTO;
        }
        return normalizado;
    }
}
