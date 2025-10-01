package com.comerzzia.bricodepot.backoffice.services.usuarios.x.exception;

import com.comerzzia.core.servicios.usuarios.UsuarioException;

public class DuracionMinimaException extends UsuarioException {

	private static final long serialVersionUID = 1224302372927752715L;

	public DuracionMinimaException() {
		super();
	}

	public DuracionMinimaException(String msg, String msgKey, Throwable e) {
		super(msg, msgKey, e);
	}

	public DuracionMinimaException(String msg, String msgKey) {
		super(msg, msgKey);
	}

	public DuracionMinimaException(String msg, Throwable e) {
		super(msg, e);
	}

	public DuracionMinimaException(String msg) {
		super(msg);
	}

}
