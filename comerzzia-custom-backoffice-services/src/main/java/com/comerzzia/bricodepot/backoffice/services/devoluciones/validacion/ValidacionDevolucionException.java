package com.comerzzia.bricodepot.backoffice.services.devoluciones.validacion;


public class ValidacionDevolucionException extends Exception {

	private static final long serialVersionUID = -6655897599282690119L;

	public ValidacionDevolucionException(String mensaje, Exception excepcion) {
		super(mensaje,excepcion);
	}
	
	public ValidacionDevolucionException(String mensaje) {
		super(mensaje);
	}

}
