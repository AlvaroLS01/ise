package com.comerzzia.bimbaylola.pos.persistence.core.config.configcontadores.rangos;

import com.comerzzia.core.util.base.MantenimientoBean;

public class ConfigContadorRangoKey extends MantenimientoBean{
    /**
	 *
	 */
	private static final long serialVersionUID = -7764619865252116138L;

	private String idContador;

    private String idRango;

    public String getIdContador() {
        return idContador;
    }

    public void setIdContador(String idContador) {
        this.idContador = idContador == null ? null : idContador.trim();
    }

    public String getIdRango() {
        return idRango;
    }

    public void setIdRango(String idRango) {
        this.idRango = idRango == null ? null : idRango.trim();
    }
    
    @Override
	protected void initNuevoBean() {		
	}
}