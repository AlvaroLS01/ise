package com.comerzzia.bricodepot.backoffice.services.usuarios.x.exception;

public class ClaveExpiradaException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = -4470844215458871642L;

	public ClaveExpiradaException() {
		super();
	}

	public ClaveExpiradaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ClaveExpiradaException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClaveExpiradaException(String message) {
	}

	public ClaveExpiradaException(Throwable cause) {
		super(cause);
	}

}
