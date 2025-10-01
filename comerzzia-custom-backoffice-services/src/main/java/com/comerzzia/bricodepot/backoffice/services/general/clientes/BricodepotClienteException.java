package com.comerzzia.bricodepot.backoffice.services.general.clientes;

import com.comerzzia.core.util.base.Exception;

public class BricodepotClienteException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5053665302018959637L;

	public BricodepotClienteException() {
	}

	public BricodepotClienteException(String msg) {
		super(msg);
	}

	public BricodepotClienteException(String msg, Throwable e) {
		super(msg, e);
	}

	public BricodepotClienteException(String msg, String msgKey, Throwable e) {
		super(msg, msgKey, e);
	}

	public BricodepotClienteException(String msg, String msgKey) {
		super(msg, msgKey);
	}

}
