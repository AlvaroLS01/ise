package com.comerzzia.bricodepot.api.omnichannel.api.web.salesdocument;

class FacturaPdfResultado {

    private final byte[] contenidoPdf;
    private final String nombreFichero;

    FacturaPdfResultado(byte[] contenidoPdf, String nombreFichero) {
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
