package com.comerzzia.bricodepot.backoffice.services.tickets.fidelizado;

public class TicketFidelizadoCaptacion {

	protected byte[] pdfFidelizado;

	protected long idFidelizado;

	protected String fechaAlta;

	public byte[] getPdfFidelizado() {
		return pdfFidelizado;
	}

	public void setPdfFidelizado(byte[] pdfFidelizado) {
		this.pdfFidelizado = pdfFidelizado;
	}

	public long getIdFidelizado() {
		return idFidelizado;
	}

	public void setIdFidelizado(long idFidelizado) {
		this.idFidelizado = idFidelizado;
	}

	public String getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(String fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

}
