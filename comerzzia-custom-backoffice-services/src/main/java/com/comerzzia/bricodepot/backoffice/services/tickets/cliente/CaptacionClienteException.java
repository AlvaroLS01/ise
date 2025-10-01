package com.comerzzia.bricodepot.backoffice.services.tickets.cliente;

import com.comerzzia.core.util.base.Exception;

public class CaptacionClienteException extends Exception {


	/**
	 * 
	 */
	private static final long serialVersionUID = -1198527944945714096L;

	public CaptacionClienteException() {
	}

	public CaptacionClienteException(String msg) {
		super(msg);
	}

	public CaptacionClienteException(String msg, Throwable e) {
		super(msg, e);
	}

	public CaptacionClienteException(String msg, String msgKey) {
		super(msg, msgKey);
	}

	public CaptacionClienteException(String msg, String msgKey, Throwable e) {
		super(msg, msgKey, e);
	}
}
