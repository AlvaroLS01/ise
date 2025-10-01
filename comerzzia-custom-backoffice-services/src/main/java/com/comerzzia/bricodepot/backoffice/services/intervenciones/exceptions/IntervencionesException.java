package com.comerzzia.bricodepot.backoffice.services.intervenciones.exceptions;

@SuppressWarnings("serial")
public class IntervencionesException extends Exception{

	public IntervencionesException(){
		super();
	}

	public IntervencionesException(String msg){
		super(msg);
	}

	public IntervencionesException(String msg, Throwable e){
		super(msg, e);
	}
	
}
