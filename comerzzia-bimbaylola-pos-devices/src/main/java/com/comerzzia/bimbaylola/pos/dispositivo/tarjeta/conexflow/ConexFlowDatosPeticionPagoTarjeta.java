package com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.conexflow;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.comerzzia.pos.services.ticket.pagos.tarjeta.DatosPeticionPagoTarjeta;

public class ConexFlowDatosPeticionPagoTarjeta extends DatosPeticionPagoTarjeta {

	private Map<String, String> adicionales;

	private String codTipoDocumento;
	
	private Boolean esDevolucion;
	
	private Boolean esDevolucionFallida;
	
	private String idDocumentoString;
	
	public ConexFlowDatosPeticionPagoTarjeta(String codTicket, Long idTicket, BigDecimal importe) {
		super(codTicket, idTicket, importe);
    }
	
	public ConexFlowDatosPeticionPagoTarjeta(String codTicket, Long idTicket, BigDecimal importe, String codTipoDocumento) {
		super(codTicket, idTicket, importe);
		this.esDevolucion = false;
		this.esDevolucionFallida = false;
		this.codTipoDocumento = codTipoDocumento;
    }
	
	public ConexFlowDatosPeticionPagoTarjeta(String idTransaccion, String idDocumento, BigDecimal importe, String codTipoDocumento) {
		this.importe = importe;
		this.idTransaccion = idTransaccion;
		this.idDocumentoString = idDocumento;
		this.esDevolucion = false;
		this.esDevolucionFallida = false;
		this.codTipoDocumento = codTipoDocumento;
    }

	public Map<String, String> getAdicionales() {
		return adicionales;
	}

	public void setAdicionales(Map<String, String> adicionales) {
		this.adicionales = adicionales;
	}
	
	public void addAdiccional(String clave, String valor) {
		if(this.adicionales == null) {
			adicionales = new HashMap<String, String>();
		}
		adicionales.put(clave, valor);
	}

	public Boolean getEsDevolucion() {
		return esDevolucion;
	}

	public void setEsDevolucion(Boolean esDevolucion) {
		this.esDevolucion = esDevolucion;
	}

	public Boolean getEsDevolucionFallida() {
		return esDevolucionFallida;
	}

	public void setEsDevolucionFallida(Boolean esDevolucionFallida) {
		this.esDevolucionFallida = esDevolucionFallida;
	}

	public String getCodTipoDocumento() {
		return codTipoDocumento;
	}

	public void setCodTipoDocumento(String codTipoDocumento) {
		this.codTipoDocumento = codTipoDocumento;
	}

	public String getIdDocumentoString() {
		return idDocumentoString;
	}

	public void setIdDocumentoString(String idDocumentoString) {
		this.idDocumentoString = idDocumentoString;
	}
}
