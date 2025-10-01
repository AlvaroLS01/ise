package com.comerzzia.bricodepot.api.omnichannel.api.web.salesdocument;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.InvalidMimeTypeException;
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

    private static final String PARAM_COPY = "copy";
    private static final String PARAM_INLINE = "inline";
    private static final String PARAM_TEMPLATE = "printTemplate";
    private static final String PARAM_OUTPUT_NAME = "outputDocumentName";
    private static final String PARAM_MIME = "mimeType";
    private static final String PARAM_CUSTOM = "customParams";

    private final SalesDocumentPrintService servicioImpresionDocumento;

    public SalesDocumentPrintController(SalesDocumentPrintService servicioImpresionDocumento) {
        this.servicioImpresionDocumento = servicioImpresionDocumento;
    }

    @GetMapping("/{documentUid}/print")
    public ResponseEntity<SalesDocumentPrintResponse> imprimirDocumento(
            @PathVariable("documentUid") String identificadorDocumento,
            @RequestParam MultiValueMap<String, String> parametrosPeticion) {

        boolean esCopia = esParametroBooleanoActivo(parametrosPeticion.get(PARAM_COPY));
        boolean esInline = esParametroBooleanoActivo(parametrosPeticion.get(PARAM_INLINE));
        String plantillaSolicitada = extraerValor(parametrosPeticion, PARAM_TEMPLATE);
        String nombreSalida = extraerValor(parametrosPeticion, PARAM_OUTPUT_NAME);
        String tipoContenidoSolicitado = extraerValor(parametrosPeticion, PARAM_MIME);
        Map<String, Object> parametrosPersonalizados = extraerParametrosPersonalizados(parametrosPeticion);

        Optional<SalesDocumentPrintResponse> respuesta = servicioImpresionDocumento.imprimirDocumento(
                identificadorDocumento,
                esCopia,
                plantillaSolicitada,
                nombreSalida,
                parametrosPersonalizados,
                tipoContenidoSolicitado);

        if (!respuesta.isPresent()) {
            LOGGER.debug("No se encontró documento de venta con UID {}", identificadorDocumento);
            return ResponseEntity.ok().body(null);
        }

        SalesDocumentPrintResponse documento = respuesta.get();
        HttpHeaders cabeceras = new HttpHeaders();
        cabeceras.setContentType(resolverMediaType(documento.getMimeType()));
        ContentDisposition disposicion = ContentDisposition
                .builder(esInline ? "inline" : "attachment")
                .filename(documento.getFileName())
                .build();
        cabeceras.setContentDisposition(disposicion);

        return new ResponseEntity<>(documento, cabeceras, HttpStatus.OK);
    }

    private boolean esParametroBooleanoActivo(List<String> valores) {
        if (valores == null || valores.isEmpty()) {
            return false;
        }
        String valor = valores.get(0);
        return valor != null && valor.trim().toLowerCase(Locale.ROOT).equals("true");
    }

    private String extraerValor(MultiValueMap<String, String> parametros, String clave) {
        List<String> valores = parametros.get(clave);
        if (valores == null || valores.isEmpty()) {
            return null;
        }
        return valores.get(0);
    }

    private Map<String, Object> extraerParametrosPersonalizados(MultiValueMap<String, String> parametros) {
        Map<String, Object> parametrosPersonalizados = new HashMap<>();

        parametros.forEach((clave, valores) -> {
            if (clave == null) {
                return;
            }

            if (PARAM_COPY.equalsIgnoreCase(clave)
                    || PARAM_TEMPLATE.equalsIgnoreCase(clave)
                    || PARAM_OUTPUT_NAME.equalsIgnoreCase(clave)
                    || PARAM_INLINE.equalsIgnoreCase(clave)
                    || PARAM_MIME.equalsIgnoreCase(clave)) {
                return;
            }

            if (clave.startsWith(PARAM_CUSTOM + ".")) {
                String claveDestino = clave.substring((PARAM_CUSTOM + ".").length());
                if (!claveDestino.isEmpty() && valores != null && !valores.isEmpty()) {
                    parametrosPersonalizados.put(claveDestino, valores.get(0));
                }
                return;
            }

            if (clave.startsWith(PARAM_CUSTOM + "[") && clave.endsWith("]")) {
                String claveDestino = clave.substring((PARAM_CUSTOM + "[").length(), clave.length() - 1);
                if (!claveDestino.isEmpty() && valores != null && !valores.isEmpty()) {
                    parametrosPersonalizados.put(claveDestino, valores.get(0));
                }
            }
        });

        if (parametros.containsKey(PARAM_CUSTOM)) {
            String valorPlano = extraerValor(parametros, PARAM_CUSTOM);
            if (valorPlano != null) {
                parametrosPersonalizados.put(PARAM_CUSTOM, valorPlano);
            }
        }

        return parametrosPersonalizados;
    }

    private MediaType resolverMediaType(String tipoContenidoSolicitado) {
        if (tipoContenidoSolicitado == null || tipoContenidoSolicitado.trim().isEmpty()) {
            return MediaType.APPLICATION_PDF;
        }
        try {
            return MediaType.parseMediaType(tipoContenidoSolicitado);
        } catch (InvalidMimeTypeException ex) {
            LOGGER.warn("Tipo MIME {} no válido. Se utilizará application/pdf", tipoContenidoSolicitado);
            return MediaType.APPLICATION_PDF;
        }
    }
}
