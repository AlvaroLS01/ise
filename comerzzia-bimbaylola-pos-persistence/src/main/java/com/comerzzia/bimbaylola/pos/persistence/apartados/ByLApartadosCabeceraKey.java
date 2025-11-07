package com.comerzzia.bimbaylola.pos.persistence.apartados;

public class ByLApartadosCabeceraKey {
    private String uidActividad;

    private String uidApartado;

    public String getUidActividad() {
        return uidActividad;
    }

    public void setUidActividad(String uidActividad) {
        this.uidActividad = uidActividad == null ? null : uidActividad.trim();
    }

    public String getUidApartado() {
        return uidApartado;
    }

    public void setUidApartado(String uidApartado) {
        this.uidApartado = uidApartado == null ? null : uidApartado.trim();
    }
}