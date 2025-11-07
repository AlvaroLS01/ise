package com.comerzzia.bimbaylola.pos.persistence.articulosNoAptos;

import com.comerzzia.core.util.base.MantenimientoBean;

public class ArticuloNoAptoKey extends MantenimientoBean{
    
	private static final long serialVersionUID = 4408442514186488415L;

	private String uidActividad;

    private String codart;

    public String getUidActividad(){
        return uidActividad;
    }

    public void setUidActividad(String uidActividad){
        this.uidActividad = uidActividad == null ? null : uidActividad.trim();
    }

    public String getCodart(){
        return codart;
    }

    public void setCodart(String codart){
        this.codart = codart == null ? null : codart.trim();
    }

	@Override
	protected void initNuevoBean(){
		
	}
	
}