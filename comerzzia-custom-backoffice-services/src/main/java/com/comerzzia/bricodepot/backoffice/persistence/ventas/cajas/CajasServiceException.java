package com.comerzzia.bricodepot.backoffice.persistence.ventas.cajas;

public class CajasServiceException extends Exception {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	public CajasServiceException() {
	}

	public CajasServiceException(String message) {
		super(message);
	}

	public CajasServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public CajasServiceException(Throwable cause) {
		super(cause);
	}

	public CajasServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
