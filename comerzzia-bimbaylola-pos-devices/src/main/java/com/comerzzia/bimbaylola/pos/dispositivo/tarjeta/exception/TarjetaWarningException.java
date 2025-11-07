package com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.exception;

import com.comerzzia.pos.core.dispositivos.dispositivo.tarjeta.TarjetaException;
import com.comerzzia.pos.services.ticket.pagos.tarjeta.DatosRespuestaPagoTarjeta;

public class TarjetaWarningException extends TarjetaException{

	private static final long serialVersionUID = 1L;
	
	public TarjetaWarningException(String msg, DatosRespuestaPagoTarjeta datRespuesta) {
		super(msg, datRespuesta);
	}

}
