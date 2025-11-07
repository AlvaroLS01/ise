package com.comerzzia.bimbaylola.pos.persistence.mediosPago;

import java.util.Date;

public class MediosPagoBIN extends MediosPagoBINKey {
    private Date fechaInicio;

    private Date fechaFin;

    private String idD365;

    private String idD365Abono;

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getIdD365() {
        return idD365;
    }

    public void setIdD365(String idD365) {
        this.idD365 = idD365 == null ? null : idD365.trim();
    }

    public String getIdD365Abono() {
        return idD365Abono;
    }

    public void setIdD365Abono(String idD365Abono) {
        this.idD365Abono = idD365Abono == null ? null : idD365Abono.trim();
    }
}