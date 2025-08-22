package com.comerzzia.bricodepot.instoreengine.rest.cliente.cajas;

import javax.ws.rs.QueryParam;

public class ApunteRequestRest {
	
	private String apiKey;
	
	private String uidActividad;
	
	private String codcaja;
	
	private String codConcepto;
	
	private String importe;
	
	private String usuario;
	
	private String documento;

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getUidActividad() {
		return uidActividad;
	}

	public void setUidActividad(String uidActividad) {
		this.uidActividad = uidActividad;
	}

	public String getCodcaja() {
		return codcaja;
	}

	public void setCodcaja(String codcaja) {
		this.codcaja = codcaja;
	}

	public String getCodConcepto() {
		return codConcepto;
	}

	public void setCodConcepto(String codConcepto) {
		this.codConcepto = codConcepto;
	}

	public String getImporte() {
		return importe;
	}

	public void setImporte(String importe) {
		this.importe = importe;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

}
