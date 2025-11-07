package com.comerzzia.bimbaylola.pos.gui.mantenimientos.fidelizados.datosgenerales;

import com.comerzzia.core.util.base.Exception;

@SuppressWarnings("serial")
public class ValidarDatosGeneralesException extends Exception{

	public ValidarDatosGeneralesException(){
		super();
	}
	
	public ValidarDatosGeneralesException(String msg){
		super(msg);
	}
	
	public ValidarDatosGeneralesException(String msg, Throwable e){
        super(msg, e);
    }

	public ValidarDatosGeneralesException(String msg, String msgKey, Throwable e){
		super(msg, msgKey, e);
	}

	public ValidarDatosGeneralesException(String msg, String msgKey){
		super(msg, msgKey);
	}
	
}

