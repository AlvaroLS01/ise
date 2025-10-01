package com.comerzzia.bricodepot.api.omnichannel.api.web.salesdocument;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/salesdocument")
public class SalesDocumentPrintController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SalesDocumentPrintController.class);

    private final SalesDocumentPrintService servicioImpresionDocumento;
    private final SalesDocumentPrintRequestMapper mapeadorPeticion;

    public SalesDocumentPrintController(SalesDocumentPrintService servicioImpresionDocumento,
            SalesDocumentPrintRequestMapper mapeadorPeticion) {
        this.servicioImpresionDocumento = servicioImpresionDocumento;
        this.mapeadorPeticion = mapeadorPeticion;
    }

    @GetMapping("/{documentUid}/print")
    public ResponseEntity<SalesDocumentPrintResponse> imprimirDocumento(
            @PathVariable("documentUid") String identificadorDocumento,
            @RequestParam MultiValueMap<String, String> parametrosPeticion) {

        SalesDocumentPrintParameters parametros = mapeadorPeticion.map(parametrosPeticion);
        Optional<SalesDocumentPrintResponse> respuesta = servicioImpresionDocumento.imprimirDocumento(
                identificadorDocumento,
                parametros.esCopia(),
                parametros.getPlantillaSolicitada(),
                parametros.getNombreSalida(),
                parametros.getParametrosPersonalizados(),
                parametros.getTipoContenido());

        if (!respuesta.isPresent()) {
            LOGGER.debug("No se encontró documento de venta con UID {}", identificadorDocumento);
            return ResponseEntity.ok().body(null);
        }

        SalesDocumentPrintResponse documento = respuesta.get();
        HttpHeaders cabeceras = new HttpHeaders();
        cabeceras.setContentType(mapeadorPeticion.resolverMediaType(documento.getMimeType()));
        ContentDisposition disposicionContenido = ContentDisposition
                .builder(parametros.esInline() ? "inline" : "attachment")
                .filename(documento.getFileName())
                .build();
        cabeceras.setContentDisposition(disposicionContenido);

        return new ResponseEntity<>(documento, cabeceras, HttpStatus.OK);
    }
}
