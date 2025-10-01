package com.comerzzia.bricodepot.api.omnichannel.api.web.salesdocument;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

@Component
class SalesDocumentPrintRequestMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(SalesDocumentPrintRequestMapper.class);

    SalesDocumentPrintParameters map(MultiValueMap<String, String> parametros) {
        boolean esCopia = esParametroBooleanoActivo(parametros.get("copy"));
        String plantillaSolicitada = normalizarNombrePlantilla(extraerValor(parametros, "printTemplate"));
        String nombreSalida = extraerValor(parametros, "outputDocumentName");
        String tipoContenidoSolicitado = normalizarTexto(extraerValor(parametros, "mimeType"));
        boolean enviarInline = esParametroBooleanoActivo(parametros.get("inline"));
        Map<String, Object> parametrosPersonalizados = extraerParametrosPersonalizados(parametros);

        return new SalesDocumentPrintParameters(esCopia,
                plantillaSolicitada,
                nombreSalida,
                tipoContenidoSolicitado,
                enviarInline,
                parametrosPersonalizados);
    }

    MediaType resolverMediaType(String mimeType) {
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
                    || "outputDocumentName".equalsIgnoreCase(clave)
                    || "mimeType".equalsIgnoreCase(clave)
                    || "inline".equalsIgnoreCase(clave)) {
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

    private String normalizarNombrePlantilla(String plantillaSolicitada) {
        if (plantillaSolicitada == null) {
            return null;
        }
        String texto = plantillaSolicitada.trim();
        if (texto.isEmpty()) {
            return null;
        }
        String textoMinusculas = texto.toLowerCase(Locale.ROOT);
        if (textoMinusculas.endsWith(".jasper")) {
            texto = texto.substring(0, texto.length() - ".jasper".length());
        } else if (textoMinusculas.endsWith(".jrxml")) {
            texto = texto.substring(0, texto.length() - ".jrxml".length());
        }
        return texto;
    }

    private String normalizarTexto(String valor) {
        return valor == null ? null : valor.trim();
    }
}
