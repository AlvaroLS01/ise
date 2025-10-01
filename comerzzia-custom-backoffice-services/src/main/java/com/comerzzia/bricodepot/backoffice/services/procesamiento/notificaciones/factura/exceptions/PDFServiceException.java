package com.comerzzia.bricodepot.backoffice.services.procesamiento.notificaciones.factura.exceptions;

@SuppressWarnings("serial")
public class PDFServiceException extends Exception {

	public PDFServiceException() {
		super();
	}

	public PDFServiceException(String msg) {
		super(msg);
	}

	public PDFServiceException(String msg, Throwable e) {
		super(msg, e);
	}

}
