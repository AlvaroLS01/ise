package com.comerzzia.bimbaylola.pos.persistence.articulosNoAptos;

public class ArticuloNoAptoBean extends ArticuloNoAptoKey{

	private static final long serialVersionUID = -6498935401735770982L;
	
	private String apto;

    public String getApto(){
        return apto;
    }

    public void setApto(String apto){
        this.apto = apto == null ? null : apto.trim();
    }

}