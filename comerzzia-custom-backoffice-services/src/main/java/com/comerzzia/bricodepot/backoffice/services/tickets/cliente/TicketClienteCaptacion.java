package com.comerzzia.bricodepot.backoffice.services.tickets.cliente;

public class TicketClienteCaptacion {

	protected byte[] pdfCliente;

	protected String cif;

	protected String fechaAlta;
	
	protected String operacion;

	public byte[] getPdfCliente() {
		return pdfCliente;
	}

	public void setPdfCliente(byte[] pdfCliente) {
		this.pdfCliente = pdfCliente;
	}

	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public String getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(String fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public String getOperacion() {
		return operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}

}
