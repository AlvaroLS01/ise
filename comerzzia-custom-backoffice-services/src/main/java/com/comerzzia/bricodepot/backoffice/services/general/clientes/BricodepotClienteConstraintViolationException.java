package com.comerzzia.bricodepot.backoffice.services.general.clientes;

import com.comerzzia.core.util.base.Exception;

public class BricodepotClienteConstraintViolationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -420434507626388821L;

	public BricodepotClienteConstraintViolationException() {
	}

	public BricodepotClienteConstraintViolationException(String msg) {
		super(msg);
	}

	public BricodepotClienteConstraintViolationException(String msg, Throwable e) {
		super(msg, e);
	}

	public BricodepotClienteConstraintViolationException(String msg, String msgKey, Throwable e) {
		super(msg, msgKey, e);
	}

	public BricodepotClienteConstraintViolationException(String msg, String msgKey) {
		super(msg, msgKey);
	}

}
