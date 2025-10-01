package com.comerzzia.bricodepot.backoffice.persistence.pagos.mediospago;


public class DatosRespuestaPagoTarjetaDto {

	String tarjeta;
	String comercio;
	String terminal;
	String AID;
	String tipoLectura;
	String codAutorizacion;
	String ARC;
	String applicationLabel;
	String marcaTarjeta;
	String fechaTransaccion;
	
	public DatosRespuestaPagoTarjetaDto() {
		tarjeta = "";
		comercio = "";
		terminal = "";
		AID = "";
		tipoLectura = "";
		codAutorizacion = "";
		ARC = "";
		applicationLabel = "";
		marcaTarjeta = "";
		fechaTransaccion = "";
	}

	
	public String getTarjeta() {
		return tarjeta;
	}

	
	public void setTarjeta(String tarjeta) {
		this.tarjeta = tarjeta;
	}

	
	public String getComercio() {
		return comercio;
	}

	
	public void setComercio(String comercio) {
		this.comercio = comercio;
	}

	
	public String getTerminal() {
		return terminal;
	}

	
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	
	public String getAID() {
		return AID;
	}

	
	public void setAID(String aID) {
		AID = aID;
	}

	
	public String getTipoLectura() {
		return tipoLectura;
	}

	
	public void setTipoLectura(String tipoLectura) {
		this.tipoLectura = tipoLectura;
	}

	
	public String getCodAutorizacion() {
		return codAutorizacion;
	}

	
	public void setCodAutorizacion(String codAutorizacion) {
		this.codAutorizacion = codAutorizacion;
	}

	
	public String getARC() {
		return ARC;
	}

	
	public void setARC(String aRC) {
		ARC = aRC;
	}

	
	public String getApplicationLabel() {
		return applicationLabel;
	}

	
	public void setApplicationLabel(String applicationLabel) {
		this.applicationLabel = applicationLabel;
	}

	
	public String getMarcaTarjeta() {
		return marcaTarjeta;
	}

	
	public void setMarcaTarjeta(String marcaTarjeta) {
		this.marcaTarjeta = marcaTarjeta;
	}

	
	public String getFechaTransaccion() {
		return fechaTransaccion;
	}

	
	public void setFechaTransaccion(String fechaTransaccion) {
		this.fechaTransaccion = fechaTransaccion;
	}
	
}
