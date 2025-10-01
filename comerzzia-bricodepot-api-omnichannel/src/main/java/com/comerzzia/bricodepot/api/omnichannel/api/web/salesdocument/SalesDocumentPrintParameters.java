package com.comerzzia.bricodepot.api.omnichannel.api.web.salesdocument;

import java.util.Map;

class SalesDocumentPrintParameters {

    private final boolean copia;
    private final String plantillaSolicitada;
    private final String nombreSalida;
    private final String tipoContenido;
    private final boolean inline;
    private final Map<String, Object> parametrosPersonalizados;

    SalesDocumentPrintParameters(boolean copia,
            String plantillaSolicitada,
            String nombreSalida,
            String tipoContenido,
            boolean inline,
            Map<String, Object> parametrosPersonalizados) {
        this.copia = copia;
        this.plantillaSolicitada = plantillaSolicitada;
        this.nombreSalida = nombreSalida;
        this.tipoContenido = tipoContenido;
        this.inline = inline;
        this.parametrosPersonalizados = parametrosPersonalizados;
    }

    boolean esCopia() {
        return copia;
    }

    String getPlantillaSolicitada() {
        return plantillaSolicitada;
    }

    String getNombreSalida() {
        return nombreSalida;
    }

    String getTipoContenido() {
        return tipoContenido;
    }

    boolean esInline() {
        return inline;
    }

    Map<String, Object> getParametrosPersonalizados() {
        return parametrosPersonalizados;
    }
}
