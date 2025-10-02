package com.comerzzia.bricodepot.api.omnichannel.api.web.salesdocument;

public final class ResultadoFacturaPdf {

    private final byte[] contenidoPdf;
    private final String nombreFichero;

    public ResultadoFacturaPdf(byte[] contenidoPdf, String nombreFichero) {
        this.contenidoPdf = contenidoPdf;
        this.nombreFichero = nombreFichero;
    }

    public byte[] getContenidoPdf() {
        return contenidoPdf;
    }

    public String getNombreFichero() {
        return nombreFichero;
    }
}
