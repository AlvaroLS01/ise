package com.comerzzia.bricodepot.backoffice.persistence.ventas.cajas;


public class CajaEstadoException extends Exception{
	protected static final long serialVersionUID = 1L;

    public CajaEstadoException() {
    }

    public CajaEstadoException(String message) {
        super(message);
    }

    public CajaEstadoException(String message, Throwable cause) {
        super(message, cause);
    }

    public CajaEstadoException(Throwable cause) {
        super(cause);
    }

    public CajaEstadoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
