package com.comerzzia.bricodepot.backoffice.persistence.mensajes;

public class BuzonMensaje extends BuzonMensajeKey {
    private Long idBuzon;

    private String uidMensaje;

    private String uidTicketNotificacion;

    public Long getIdBuzon() {
        return idBuzon;
    }

    public void setIdBuzon(Long idBuzon) {
        this.idBuzon = idBuzon;
    }

    public String getUidMensaje() {
        return uidMensaje;
    }

    public void setUidMensaje(String uidMensaje) {
        this.uidMensaje = uidMensaje == null ? null : uidMensaje.trim();
    }

    public String getUidTicketNotificacion() {
        return uidTicketNotificacion;
    }

    public void setUidTicketNotificacion(String uidTicketNotificacion) {
        this.uidTicketNotificacion = uidTicketNotificacion == null ? null : uidTicketNotificacion.trim();
    }
}