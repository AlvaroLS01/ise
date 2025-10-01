package com.comerzzia.bricodepot.backoffice.services.motivos;


public class MotivoException extends Exception {

	
	public MotivoException(String mensaje, Exception excepcion) {
		super(mensaje,excepcion);
	}
	
	public MotivoException(String mensaje) {
		super(mensaje);
	}

}
