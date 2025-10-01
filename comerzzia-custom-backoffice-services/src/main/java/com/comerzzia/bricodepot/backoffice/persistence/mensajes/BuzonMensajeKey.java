package com.comerzzia.bricodepot.backoffice.persistence.mensajes;

public class BuzonMensajeKey {
    private String uidActividad;

    private String uidServicio;

    public String getUidActividad() {
        return uidActividad;
    }

    public void setUidActividad(String uidActividad) {
        this.uidActividad = uidActividad == null ? null : uidActividad.trim();
    }

    public String getUidServicio() {
        return uidServicio;
    }

    public void setUidServicio(String uidServicio) {
        this.uidServicio = uidServicio == null ? null : uidServicio.trim();
    }
}