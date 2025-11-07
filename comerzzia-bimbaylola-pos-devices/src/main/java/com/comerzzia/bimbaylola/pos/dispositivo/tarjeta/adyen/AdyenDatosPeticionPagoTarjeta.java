package com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.adyen;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.comerzzia.pos.services.ticket.pagos.tarjeta.DatosPeticionPagoTarjeta;

public class AdyenDatosPeticionPagoTarjeta extends DatosPeticionPagoTarjeta {

	private Map<String, String> adicionales;

	private String idDocumentoString;
	private String codTipoDocumento;
	private Boolean esDevolucion;
	private Boolean esDevolucionFallida;
	
	private String terminalOrigen;
	private String numTransaccion; // Incidencia 106269
	private Integer totalMeses;

	public AdyenDatosPeticionPagoTarjeta(String idTransaccion, Long idDocumento, BigDecimal importe) {
		this.importe = importe;
		this.idTransaccion = idTransaccion;
		this.idDocumento = idDocumento;
	}

	public AdyenDatosPeticionPagoTarjeta(String codTicket, Long idTicket, BigDecimal importe, String codTipoDocumento) {
		super(codTicket, idTicket, importe);
		this.esDevolucion = false;
		this.esDevolucionFallida = false;
		this.codTipoDocumento = codTipoDocumento;
	}

	public AdyenDatosPeticionPagoTarjeta(String idTransaccion, String idDocumento, BigDecimal importe, String codTipoDocumento) {
		this.importe = importe;
		this.idTransaccion = idTransaccion;
		this.idDocumentoString = idDocumento;
		this.esDevolucion = false;
		this.esDevolucionFallida = false;
		this.codTipoDocumento = codTipoDocumento;
	}

	public AdyenDatosPeticionPagoTarjeta(String idTransaccion, String idDocumento, BigDecimal importe, String codTipoDocumento, Integer totalMeses) {
		this.importe = importe;
		this.idTransaccion = idTransaccion;
		this.idDocumentoString = idDocumento;
		this.esDevolucion = false;
		this.esDevolucionFallida = false;
		this.codTipoDocumento = codTipoDocumento;
		this.totalMeses = totalMeses;
	}

	public String getIdDocumentoString() {
		return idDocumentoString;
	}

	public void setIdDocumentoString(String idDocumentoString) {
		this.idDocumentoString = idDocumentoString;
	}

	public String getCodTipoDocumento() {
		return codTipoDocumento;
	}

	public void setCodTipoDocumento(String codTipoDocumento) {
		this.codTipoDocumento = codTipoDocumento;
	}

	public Map<String, String> getAdicionales() {
		return adicionales;
	}

	public void setAdicionales(Map<String, String> adicionales) {
		this.adicionales = adicionales;
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

	public void addAdiccional(String clave, String valor) {
		if (this.adicionales == null) {
			adicionales = new HashMap<String, String>();
		}
		adicionales.put(clave, valor);
	}

	public String getTerminalOrigen() {
		return terminalOrigen;
	}

	
	public Integer getTotalMeses() {
		return totalMeses;
	}

	
	public void setTotalMeses(Integer totalMeses) {
		this.totalMeses = totalMeses;
	}

	public void setTerminalOrigen(String terminalOrigen) {
		this.terminalOrigen = terminalOrigen;
	}

	public String getNumTransaccion() { // Incidencia 106269
		return numTransaccion;
	}

	public void setNumTransaccion(String numTransaccion) { // Incidencia 106269
		this.numTransaccion = numTransaccion;
	}
	
	
}
