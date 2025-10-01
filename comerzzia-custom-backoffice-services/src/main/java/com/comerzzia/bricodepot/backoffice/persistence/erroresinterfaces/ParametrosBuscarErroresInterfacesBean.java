package com.comerzzia.bricodepot.backoffice.persistence.erroresinterfaces;

import com.comerzzia.core.util.base.ParametrosBuscarBean;


public class ParametrosBuscarErroresInterfacesBean extends ParametrosBuscarBean{
	
	
	/**
     * 
     */
    private static final long serialVersionUID = -7772583555305105145L;
	private String idClase;
	
	public ParametrosBuscarErroresInterfacesBean(){
		super();
		setOrden("FECHA_INICIO DESC");
	}
	
    public String getIdClase() {
    	return idClase;
    }

	
    public void setIdClase(String idClase) {
    	this.idClase = idClase;
    }
	
	


}