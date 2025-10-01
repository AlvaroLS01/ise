package com.comerzzia.bricodepot.backoffice.services.tickets.fidelizado;

import com.comerzzia.core.util.base.Exception;

public class CaptacionFidelizadoException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = -7045617213585371401L;

	public CaptacionFidelizadoException() {
	}

	public CaptacionFidelizadoException(String msg) {
		super(msg);
	}

	public CaptacionFidelizadoException(String msg, Throwable e) {
		super(msg, e);
	}

	public CaptacionFidelizadoException(String msg, String msgKey) {
		super(msg, msgKey);
	}

	public CaptacionFidelizadoException(String msg, String msgKey, Throwable e) {
		super(msg, msgKey, e);
	}
}
