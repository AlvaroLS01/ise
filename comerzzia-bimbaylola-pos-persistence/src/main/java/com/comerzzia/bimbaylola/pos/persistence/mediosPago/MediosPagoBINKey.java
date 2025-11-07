package com.comerzzia.bimbaylola.pos.persistence.mediosPago;

public class MediosPagoBINKey {
    private String uidActividad;

    private String codmedpag;

    private String orden;

    public String getUidActividad() {
        return uidActividad;
    }

    public void setUidActividad(String uidActividad) {
        this.uidActividad = uidActividad == null ? null : uidActividad.trim();
    }

    public String getCodmedpag() {
        return codmedpag;
    }

    public void setCodmedpag(String codmedpag) {
        this.codmedpag = codmedpag == null ? null : codmedpag.trim();
    }

    public String getOrden() {
        return orden;
    }

    public void setOrden(String orden) {
        this.orden = orden == null ? null : orden.trim();
    }
}