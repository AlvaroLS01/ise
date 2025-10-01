package com.comerzzia.bricodepot.backoffice.persistence.tickets.tipoImpresion;

import java.util.Date;

public class TicketTipoImpresion extends TicketTipoImpresionKey {

	private static final long serialVersionUID = -2258461135300729015L;

	private String tipoImpresion;

	private Date fecha;

	public String getTipoImpresion() {
		return tipoImpresion;
	}

	public void setTipoImpresion(String tipoImpresion) {
		this.tipoImpresion = tipoImpresion == null ? null : tipoImpresion.trim();
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
}