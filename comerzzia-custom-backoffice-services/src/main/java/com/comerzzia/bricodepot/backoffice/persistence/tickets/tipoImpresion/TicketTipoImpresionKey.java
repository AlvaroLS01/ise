package com.comerzzia.bricodepot.backoffice.persistence.tickets.tipoImpresion;

import com.comerzzia.core.util.base.MantenimientoBean;

public class TicketTipoImpresionKey extends MantenimientoBean {

	private static final long serialVersionUID = 2871967059901984954L;

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

	@Override
	protected void initNuevoBean() {
	}
}