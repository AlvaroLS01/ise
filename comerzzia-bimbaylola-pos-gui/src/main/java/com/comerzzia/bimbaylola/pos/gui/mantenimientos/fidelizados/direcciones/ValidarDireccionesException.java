package com.comerzzia.bimbaylola.pos.gui.mantenimientos.fidelizados.direcciones;

import com.comerzzia.core.util.base.Exception;

@SuppressWarnings("serial")
public class ValidarDireccionesException extends Exception{

	public ValidarDireccionesException(){
		super();
	}
	
	public ValidarDireccionesException(String msg){
		super(msg);
	}
	
	public ValidarDireccionesException(String msg, Throwable e){
        super(msg, e);
    }

	public ValidarDireccionesException(String msg, String msgKey, Throwable e){
		super(msg, msgKey, e);
	}

	public ValidarDireccionesException(String msg, String msgKey){
		super(msg, msgKey);
	}
	
}

