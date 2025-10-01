package com.comerzzia.bricodepot.backoffice.services.intervenciones.exceptions;

@SuppressWarnings("serial")
public class IntervencionesConstraintViolationException extends Exception{

	public IntervencionesConstraintViolationException(){
		super();
	}

	public IntervencionesConstraintViolationException(String msg){
		super(msg);
	}

	public IntervencionesConstraintViolationException(String msg, Throwable e){
		super(msg, e);
	}
	
}
