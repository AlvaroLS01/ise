package com.comerzzia.bimbaylola.pos.persistence.pais.provincias;

public class ProvinciasKey{

	private String uidInstancia;

	private String codprovincia;

	private String codpais;

	public String getUidInstancia(){
		return uidInstancia;
	}

	public void setUidInstancia(String uidInstancia){
		this.uidInstancia = uidInstancia == null ? null : uidInstancia.trim();
	}

	public String getCodprovincia(){
		return codprovincia;
	}

	public void setCodprovincia(String codprovincia){
		this.codprovincia = codprovincia == null ? null : codprovincia.trim();
	}

	public String getCodpais(){
		return codpais;
	}

	public void setCodpais(String codpais){
		this.codpais = codpais == null ? null : codpais.trim();
	}
}