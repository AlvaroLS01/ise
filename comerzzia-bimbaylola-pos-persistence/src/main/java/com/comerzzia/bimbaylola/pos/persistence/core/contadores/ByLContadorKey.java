package com.comerzzia.bimbaylola.pos.persistence.core.contadores;

import com.comerzzia.core.util.base.MantenimientoBean;

public class ByLContadorKey extends MantenimientoBean {
    /**
     * 
     */
    private static final long serialVersionUID = 5231120322614654400L;

	private String uidActividad;

    private String idContador;

    private String divisor1;

    private String divisor2;

    private String divisor3;

    public String getUidActividad() {
        return uidActividad;
    }

    public void setUidActividad(String uidActividad) {
        this.uidActividad = uidActividad == null ? null : uidActividad.trim();
    }

    public String getIdContador() {
        return idContador;
    }

    public void setIdContador(String idContador) {
        this.idContador = idContador == null ? null : idContador.trim();
    }

    public String getDivisor1() {
        return divisor1;
    }

    public void setDivisor1(String divisor1) {
        this.divisor1 = divisor1 == null ? null : divisor1.trim();
    }

    public String getDivisor2() {
        return divisor2;
    }

    public void setDivisor2(String divisor2) {
        this.divisor2 = divisor2 == null ? null : divisor2.trim();
    }

    public String getDivisor3() {
        return divisor3;
    }

    public void setDivisor3(String divisor3) {
        this.divisor3 = divisor3 == null ? null : divisor3.trim();
    }

	@Override
    protected void initNuevoBean() {
	    // TODO Auto-generated method stub
	    
    }
}