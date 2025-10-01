package com.comerzzia.bricodepot.backoffice.services.login;


public class UsuarioNoExisteException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 8935800176477030126L;

	public UsuarioNoExisteException() {
		super();
	}

	public UsuarioNoExisteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UsuarioNoExisteException(String message, Throwable cause) {
		super(message, cause);
	}

	public UsuarioNoExisteException(String message) {
		super(message);
	}

	public UsuarioNoExisteException(Throwable cause) {
		super(cause);
	}
	
	

}
