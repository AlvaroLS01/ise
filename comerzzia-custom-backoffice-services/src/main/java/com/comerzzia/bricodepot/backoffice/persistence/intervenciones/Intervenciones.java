package com.comerzzia.bricodepot.backoffice.persistence.intervenciones;

import java.util.Date;

public class Intervenciones extends IntervencionesKey {
    private String codalm;

    private String codcaja;

    private Date fecha;

    private Long idUsuario;

    public String getCodalm() {
        return codalm;
    }

    public void setCodalm(String codalm) {
        this.codalm = codalm == null ? null : codalm.trim();
    }

    public String getCodcaja() {
        return codcaja;
    }

    public void setCodcaja(String codcaja) {
        this.codcaja = codcaja == null ? null : codcaja.trim();
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
}