package com.comerzzia.bricodepot.backoffice.services.intervenciones;

@SuppressWarnings("serial")
public class ServicioIntervencionesException extends Exception{
	
	public ServicioIntervencionesException() {
		super();
	}

	public ServicioIntervencionesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ServicioIntervencionesException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServicioIntervencionesException(String message) {
		super(message);
	}

	public ServicioIntervencionesException(Throwable cause) {
		super(cause);
	}
}
