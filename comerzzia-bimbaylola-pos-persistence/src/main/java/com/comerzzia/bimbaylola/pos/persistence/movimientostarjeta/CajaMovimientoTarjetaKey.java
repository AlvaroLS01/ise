package com.comerzzia.bimbaylola.pos.persistence.movimientostarjeta;

import com.comerzzia.core.util.base.MantenimientoBean;

public class CajaMovimientoTarjetaKey extends MantenimientoBean {

	private static final long serialVersionUID = 8047261339722818019L;

	private String uidActividad;

	private String uidDiarioCaja;

	private Integer linea;

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

	public Integer getLinea() {
		return linea;
	}

	public void setLinea(Integer linea) {
		this.linea = linea;
	}

	@Override
	protected void initNuevoBean() {

	}
}