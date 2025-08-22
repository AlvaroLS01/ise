package com.comerzzia.bricodepot.inStoreEngine.rest;

import com.comerzzia.instoreengine.rest.cajas.CajaRequestRest;

public class BricodepotCajaRequestRest extends CajaRequestRest {
	
	private String codConcepto;
	
	private String importe;
	
	private String usuario;
	
	private String documento;

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
