package com.comerzzia.bricodepot.backoffice.persistence.devoluciones.informe;

public class InformeDevolucionKey {

	private String uidActividad;

	private Long idClieAlbaran;

	private Integer linea;

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

	public Integer getLinea() {
		return linea;
	}

	public void setLinea(Integer linea) {
		this.linea = linea;
	}
}