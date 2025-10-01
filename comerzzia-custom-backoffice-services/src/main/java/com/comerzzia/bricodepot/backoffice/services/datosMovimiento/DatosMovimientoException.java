package com.comerzzia.bricodepot.backoffice.services.datosMovimiento;

import com.comerzzia.servicios.ventas.cajas.cabecera.CabeceraCajaException;

public class DatosMovimientoException extends CabeceraCajaException {

	private static final long serialVersionUID = 5913941058080063291L;

	public DatosMovimientoException() {
	}

	public DatosMovimientoException(String msg) {
		super(msg);
	}

	public DatosMovimientoException(String msg, Throwable e) {
		super(msg, e);
	}

	public DatosMovimientoException(String msg, String msgKey, Throwable e) {
		super(msg, msgKey, e);
	}

	public DatosMovimientoException(String msg, String msgKey) {
		super(msg, msgKey);
	}

}
