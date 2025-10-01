package com.comerzzia.bricodepot.backoffice.persistence.tickets.auditoria;

public class EventoAuditoriaBeanKey {
    private String uidInstancia;

    private String uidActividad;

    private String uidTicketAuditoria;

    public String getUidInstancia() {
        return uidInstancia;
    }

    public void setUidInstancia(String uidInstancia) {
        this.uidInstancia = uidInstancia == null ? null : uidInstancia.trim();
    }

    public String getUidActividad() {
        return uidActividad;
    }

    public void setUidActividad(String uidActividad) {
        this.uidActividad = uidActividad == null ? null : uidActividad.trim();
    }

    public String getUidTicketAuditoria() {
        return uidTicketAuditoria;
    }

    public void setUidTicketAuditoria(String uidTicketAuditoria) {
        this.uidTicketAuditoria = uidTicketAuditoria == null ? null : uidTicketAuditoria.trim();
    }
}