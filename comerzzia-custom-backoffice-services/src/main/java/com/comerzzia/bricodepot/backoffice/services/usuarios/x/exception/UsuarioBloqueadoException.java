package com.comerzzia.bricodepot.backoffice.services.usuarios.x.exception;

public class UsuarioBloqueadoException extends Exception {

	private static final long serialVersionUID = 9139643818250611580L;

	public UsuarioBloqueadoException() {
		super();
	}

	public UsuarioBloqueadoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UsuarioBloqueadoException(String message, Throwable cause) {
		super(message, cause);
	}

	public UsuarioBloqueadoException(String message) {
	}

	public UsuarioBloqueadoException(Throwable cause) {
		super(cause);
	}

}
