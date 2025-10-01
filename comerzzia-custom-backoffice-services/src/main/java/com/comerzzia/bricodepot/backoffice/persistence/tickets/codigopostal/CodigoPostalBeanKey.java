package com.comerzzia.bricodepot.backoffice.persistence.tickets.codigopostal;

public class CodigoPostalBeanKey {
    private String uidActividad;

    private Long idClieAlbaran;

    private String codigoPostal;

    public String getUidActividad() {
        return uidActividad;
    }

    public void setUidActividad(String uidActividad) {
        this.uidActividad = uidActividad == null ? null : uidActividad.trim();
    }

    public Long getIdClieAlbaran() {
        return idClieAlbaran;
    }

    public void setIdClieAlbaran(Long idClieAlbaran) {
        this.idClieAlbaran = idClieAlbaran;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal == null ? null : codigoPostal.trim();
    }
}