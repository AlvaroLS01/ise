package com.comerzzia.bricodepot.backoffice.persistence.datosMovimiento;

public class DatosMovimientoKey {

	private String uidActividad;

	private String uidDiarioCaja;

	public String getUidActividad() {
		return uidActividad;
	}

	public void setUidActividad(String uidActividad) {
		this.uidActividad = uidActividad == null ? null : uidActividad.trim();
	}

	public String getUidDiarioCaja() {
		return uidDiarioCaja;
	}

	public void setUidDiarioCaja(String uidDiarioCaja) {
		this.uidDiarioCaja = uidDiarioCaja == null ? null : uidDiarioCaja.trim();
	}
}