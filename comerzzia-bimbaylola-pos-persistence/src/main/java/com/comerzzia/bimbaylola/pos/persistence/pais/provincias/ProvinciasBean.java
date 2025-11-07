package com.comerzzia.bimbaylola.pos.persistence.pais.provincias;

public class ProvinciasBean extends ProvinciasKey{

	private String desprovincia;

	public String getDesprovincia(){
		return desprovincia;
	}

	public void setDesprovincia(String desprovincia){
		this.desprovincia = desprovincia == null ? null : desprovincia.trim();
	}

}