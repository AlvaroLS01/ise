package com.comerzzia.bricodepot.backoffice.util.marshall;

public class MarshallUtilException extends Exception {

	private static final long serialVersionUID = 1L;

	public MarshallUtilException() {
	}

	public MarshallUtilException(String message) {
		super(message);
	}

	public MarshallUtilException(String message, Throwable cause) {
		super(message, cause);
	}

	public MarshallUtilException(Throwable cause) {
		super(cause);
	}

	public MarshallUtilException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
