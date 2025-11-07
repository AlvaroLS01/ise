package com.comerzzia.bimbaylola.pos.persistence.core.config.configcontadores;

import com.comerzzia.core.persistencia.config.configContadores.ParametrosBuscarConfigContadoresBean;

public class ByLParametrosBuscarConfigContadoresBean extends ParametrosBuscarConfigContadoresBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5461551140871530449L;

	private String codTipoDocumento;
	private String desTipoDocumento;
	private String codPais;
	private String desPais;


	public String getCodTipoDocumento() {
		return codTipoDocumento;
	}

	public void setCodTipoDocumento(String codTipoDocumento) {
		this.codTipoDocumento = codTipoDocumento;
	}

	public String getCodPais() {
		return codPais;
	}

	public void setCodPais(String codPais) {
		this.codPais = codPais;
	}

	public String getDesTipoDocumento() {
		return desTipoDocumento;
	}

	public void setDesTipoDocumento(String desTipoDocumento) {
		this.desTipoDocumento = desTipoDocumento;
	}

	public String getDesPais() {
		return desPais;
	}

	public void setDesPais(String desPais) {
		this.desPais = desPais;
	}
	
}
