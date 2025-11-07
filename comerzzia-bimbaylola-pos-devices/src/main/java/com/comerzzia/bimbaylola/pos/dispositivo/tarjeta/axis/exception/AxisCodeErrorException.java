package com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.axis.exception;

import com.comerzzia.core.util.base.Exception;

@SuppressWarnings("serial")
public class AxisCodeErrorException extends Exception{

	public AxisCodeErrorException() {
		super();
	}
	public AxisCodeErrorException(String msg) {
		super(msg);
	}
	
	public AxisCodeErrorException(String msg, Throwable e) {
        super(msg, e);
    }

	public AxisCodeErrorException(String msg, String msgKey, Throwable e) {
		super(msg, msgKey, e);
	}

	public AxisCodeErrorException(String msg, String msgKey) {
		super(msg, msgKey);
	}
	
}