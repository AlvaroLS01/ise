package com.comerzzia.bimbaylola.pos.gui.ventas.gestiontickets.ticketRegalo;

import com.comerzzia.bimbaylola.pos.persistence.tickets.ByLTicketBean;
import com.comerzzia.core.util.numeros.Numero;
import com.comerzzia.pos.gui.ventas.gestiontickets.GestionTicketGui;
import com.comerzzia.pos.util.i18n.I18N;

public class ByLGestionTicketGui extends GestionTicketGui {
	
	private String uidActividad;
	private String tipoDocumento;
	private String vendedor;
	private String total;
	private String desMedPag;

	public ByLGestionTicketGui(ByLTicketBean line) {
		super(line);
		this.uidActividad = line.getUidActividad() == null ? "" : line.getUidActividad();
		this.desDoc = line.getDesDoc() == null ? "" : line.getDesDoc();
		this.tipoDocumento = line.getIdTipoDocumento() == null ? "" : line.getIdTipoDocumento().toString();
		this.vendedor = line.getVendedor() == null ? "" : line.getVendedor();
		this.total = line.getTotal() == null ? "" : Numero.formatea(line.getTotal());
		
		/*
		 * Se realiza la traducción de esta manera ya que la descripción "MULTIPAGO" la sacamos directamente desde la
		 * consulta de bbdd
		 */
		this.desMedPag = line.getDesMedPag() != null && line.getDesMedPag().equalsIgnoreCase("MULTIPAGO") ? I18N.getTexto("MULTIPAGO") : line.getDesMedPag();
	}

	public String getUidActividad() {
		return uidActividad;
	}

	public void setUidActividad(String uidActividad) {
		this.uidActividad = uidActividad;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getVendedor() {
		return vendedor;
	}

	public void setVendedor(String vendedor) {
		this.vendedor = vendedor;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getDesMedPag() {
		return desMedPag;
	}

	public void setDesMedPag(String desMedPag) {
		this.desMedPag = desMedPag;
	}

}