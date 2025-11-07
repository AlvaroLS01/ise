package com.comerzzia.bimbaylola.pos.persistence.ventas.tipooperacion;

import com.comerzzia.core.util.base.MantenimientoBean;

public class TipoOperacionVentaKey extends MantenimientoBean {

	private static final long serialVersionUID = -8364276306662816112L;

	private String uidActividad;

	private String uidTicket;

	private String uidDiarioCaja;

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

	public String getUidDiarioCaja() {
		return uidDiarioCaja;
	}

	public void setUidDiarioCaja(String uidDiarioCaja) {
		this.uidDiarioCaja = uidDiarioCaja == null ? null : uidDiarioCaja.trim();
	}

	@Override
	protected void initNuevoBean() {
		
	}
}