package com.comerzzia.bricodepot.backoffice.persistence.mensajeticket;

public class MensajeTicketKey {
    private String uidActividad;

    private String uidTicket;

    private String uidMensaje;

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

    public String getUidMensaje() {
        return uidMensaje;
    }

    public void setUidMensaje(String uidMensaje) {
        this.uidMensaje = uidMensaje == null ? null : uidMensaje.trim();
    }
}