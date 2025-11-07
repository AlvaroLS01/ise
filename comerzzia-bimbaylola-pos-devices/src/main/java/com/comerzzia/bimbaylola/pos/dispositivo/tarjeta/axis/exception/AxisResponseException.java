package com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.axis.exception;

import com.comerzzia.core.util.base.Exception;

@SuppressWarnings("serial")
public class AxisResponseException extends Exception{

	public AxisResponseException() {
		super();
	}
	public AxisResponseException(String msg) {
		super(msg);
	}
	
	public AxisResponseException(String msg, Throwable e) {
        super(msg, e);
    }

	public AxisResponseException(String msg, String msgKey, Throwable e) {
		super(msg, msgKey, e);
	}

	public AxisResponseException(String msg, String msgKey) {
		super(msg, msgKey);
	}
	
}