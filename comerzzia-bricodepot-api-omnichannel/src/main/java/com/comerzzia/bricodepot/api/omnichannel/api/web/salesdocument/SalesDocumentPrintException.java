package com.comerzzia.bricodepot.api.omnichannel.api.web.salesdocument;

public class SalesDocumentPrintException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SalesDocumentPrintException(String message) {
        super(message);
    }

    public SalesDocumentPrintException(String message, Throwable cause) {
        super(message, cause);
    }
}
