package com.comerzzia.bricodepot.backoffice.services.usuarios.x.exception;

import com.comerzzia.core.servicios.usuarios.UsuarioException;

public class HorasMinimasException extends UsuarioException {

	private static final long serialVersionUID = -188527112112661043L;

	public HorasMinimasException() {
		super();
	}

	public HorasMinimasException(String msg, String msgKey, Throwable e) {
		super(msg, msgKey, e);
	}

	public HorasMinimasException(String msg, String msgKey) {
		super(msg, msgKey);
	}

	public HorasMinimasException(String msg, Throwable e) {
		super(msg, e);
	}

	public HorasMinimasException(String msg) {
		super(msg);
	}
	
	

}
