package com.comerzzia.bricodepot.backoffice.services.login;

public class UsuarioInactivoException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public UsuarioInactivoException() {
		super();
	}

	public UsuarioInactivoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UsuarioInactivoException(String message, Throwable cause) {
		super(message, cause);
	}

	public UsuarioInactivoException(String message) {
		super(message);
	}

	public UsuarioInactivoException(Throwable cause) {
		super(cause);
	}

}
