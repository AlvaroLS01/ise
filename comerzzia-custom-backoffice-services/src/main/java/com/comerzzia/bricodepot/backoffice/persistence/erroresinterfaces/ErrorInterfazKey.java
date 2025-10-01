package com.comerzzia.bricodepot.backoffice.persistence.erroresinterfaces;

import com.comerzzia.core.util.base.MantenimientoBean;

public class ErrorInterfazKey extends MantenimientoBean {
    /**
     * 
     */
    private static final long serialVersionUID = -7039493843910722997L;

	private String uidActividad;

    private String uidError;

    public String getUidActividad() {
        return uidActividad;
    }

    public void setUidActividad(String uidActividad) {
        this.uidActividad = uidActividad;
    }

    public String getUidError() {
        return uidError;
    }

    public void setUidError(String uidError) {
        this.uidError = uidError;
    }

	@Override
    protected void initNuevoBean() {
	    // TODO Auto-generated method stub
	    
    }
}