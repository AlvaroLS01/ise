package com.comerzzia.bricodepot.api.omnichannel.api.web.salesdocument;

final class ResultadoFacturaPdf {

    private final byte[] contenidoPdf;
    private final String nombreFichero;

    ResultadoFacturaPdf(byte[] contenidoPdf, String nombreFichero) {
        this.contenidoPdf = contenidoPdf;
        this.nombreFichero = nombreFichero;
    }

    byte[] getContenidoPdf() {
        return contenidoPdf;
    }

    String getNombreFichero() {
        return nombreFichero;
    }
}
