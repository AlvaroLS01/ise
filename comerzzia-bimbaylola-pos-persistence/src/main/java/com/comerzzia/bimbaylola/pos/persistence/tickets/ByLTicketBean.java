package com.comerzzia.bimbaylola.pos.persistence.tickets;

import java.math.BigDecimal;

import com.comerzzia.pos.persistence.tickets.TicketBean;

public class ByLTicketBean extends TicketBean {

	private String desDoc;
	private String vendedor;
	private BigDecimal total;
	private String codMedPag;
	private String desMedPag;

	public String getDesDoc() {
		return desDoc;
	}

	public void setDesDoc(String desDoc) {
		this.desDoc = desDoc;
	}

	public String getVendedor() {
		return vendedor;
	}

	public void setVendedor(String vendedor) {
		this.vendedor = vendedor;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getCodMedPag() {
		return codMedPag;
	}

	public void setCodMedPag(String codMedPag) {
		this.codMedPag = codMedPag;
	}

	public String getDesMedPag() {
		return desMedPag;
	}

	public void setDesMedPag(String desMedPag) {
		this.desMedPag = desMedPag;
	}

}
