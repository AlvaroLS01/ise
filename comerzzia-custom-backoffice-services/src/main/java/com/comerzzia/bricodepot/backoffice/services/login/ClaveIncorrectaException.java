package com.comerzzia.bricodepot.backoffice.services.login;

public class ClaveIncorrectaException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 3653717363874798704L;

	public ClaveIncorrectaException() {
		super();
	}

	public ClaveIncorrectaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ClaveIncorrectaException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClaveIncorrectaException(String message) {
		super(message);
	}

	public ClaveIncorrectaException(Throwable cause) {
		super(cause);
	}

	
}
