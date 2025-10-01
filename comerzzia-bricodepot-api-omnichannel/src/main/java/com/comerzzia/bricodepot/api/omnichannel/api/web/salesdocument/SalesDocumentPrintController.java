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

    public SalesDocumentPrintController(SalesDocumentPrintService servicioImpresionDocumento) {
        this.servicioImpresionDocumento = servicioImpresionDocumento;
    }

    @GetMapping("/{documentUid}/print")
    public ResponseEntity<SalesDocumentPrintResponse> imprimirDocumento(
            @PathVariable("documentUid") String identificadorDocumento,
            @RequestParam MultiValueMap<String, String> parametrosPeticion) {

        boolean esCopia = esParametroBooleanoActivo(parametrosPeticion.get("copy"));
        String plantillaSolicitada = extraerValor(parametrosPeticion, "printTemplate");
        String nombreSalida = extraerValor(parametrosPeticion, "outputDocumentName");
        String tipoContenidoSolicitado = normalizarTexto(extraerValor(parametrosPeticion, "mimeType"));
        boolean enviarInline = esParametroBooleanoActivo(parametrosPeticion.get("inline"));
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
        cabeceras.setContentDisposition((enviarInline ? ContentDisposition.inline() : ContentDisposition.attachment())
                .filename(documento.getFileName())
                .build());

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
            if ("copy".equalsIgnoreCase(clave) || "printTemplate".equalsIgnoreCase(clave)
                    || "outputDocumentName".equalsIgnoreCase(clave)) {
                return;
            }
            if (clave.startsWith("customParams.")) {
                String claveDestino = clave.substring("customParams.".length());
                if (!claveDestino.isEmpty() && valores != null && !valores.isEmpty()) {
                    parametrosPersonalizados.put(claveDestino, valores.get(0));
                }
                return;
            }
            if (clave.startsWith("customParams[") && clave.endsWith("]")) {
                String claveDestino = clave.substring("customParams[".length(), clave.length() - 1);
                if (!claveDestino.isEmpty() && valores != null && !valores.isEmpty()) {
                    parametrosPersonalizados.put(claveDestino, valores.get(0));
                }
                return;
            }
        });

        if (parametros.containsKey("customParams")) {
            String valorPlano = extraerValor(parametros, "customParams");
            if (valorPlano != null) {
                parametrosPersonalizados.put("customParams", valorPlano);
            }
        }

        return parametrosPersonalizados;
    }

    private MediaType resolverMediaType(String mimeType) {
        if (mimeType == null || mimeType.trim().isEmpty()) {
            return MediaType.APPLICATION_PDF;
        }
        try {
            return MediaType.parseMediaType(mimeType);
        } catch (IllegalArgumentException excepcion) {
            LOGGER.warn("Tipo MIME {} no válido. Se utilizará application/pdf", mimeType);
            return MediaType.APPLICATION_PDF;
        }
    }

    private String normalizarTexto(String valor) {
        return valor == null ? null : valor.trim();
    }
}
