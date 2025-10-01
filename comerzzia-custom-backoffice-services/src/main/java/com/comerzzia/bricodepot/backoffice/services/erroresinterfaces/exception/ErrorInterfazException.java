package com.comerzzia.bricodepot.backoffice.services.erroresinterfaces.exception;

public class ErrorInterfazException extends Exception {


	/**
     * 
     */
    private static final long serialVersionUID = 7463103973182003082L;

	public ErrorInterfazException() {
	}

	public ErrorInterfazException(String msg) {
		super(msg);
	}

	public ErrorInterfazException(String msg, Throwable e) {
		super(msg, e);
	}

}
