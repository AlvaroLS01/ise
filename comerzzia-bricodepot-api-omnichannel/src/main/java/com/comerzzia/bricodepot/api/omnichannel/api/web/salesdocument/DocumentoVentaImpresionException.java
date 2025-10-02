package com.comerzzia.bricodepot.api.omnichannel.api.web.salesdocument;

public class DocumentoVentaImpresionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DocumentoVentaImpresionException(String mensaje) {
        super(mensaje);
    }

    public DocumentoVentaImpresionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
