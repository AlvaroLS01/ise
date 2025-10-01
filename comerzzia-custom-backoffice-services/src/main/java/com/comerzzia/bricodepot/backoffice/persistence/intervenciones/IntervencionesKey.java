package com.comerzzia.bricodepot.backoffice.persistence.intervenciones;

public class IntervencionesKey {
    private String uidActividad;

    private String uidTicket;

    public String getUidActividad() {
        return uidActividad;
    }

    public void setUidActividad(String uidActividad) {
        this.uidActividad = uidActividad == null ? null : uidActividad.trim();
    }

    public String getUidTicket() {
        return uidTicket;
    }

    public void setUidTicket(String uidTicket) {
        this.uidTicket = uidTicket == null ? null : uidTicket.trim();
    }
}