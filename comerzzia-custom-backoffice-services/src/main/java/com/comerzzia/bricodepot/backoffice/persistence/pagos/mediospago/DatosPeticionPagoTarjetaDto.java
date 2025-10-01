package com.comerzzia.bricodepot.backoffice.persistence.pagos.mediospago;


public class DatosPeticionPagoTarjetaDto {

	Long idDocumento;
	String idTransaccion;
	String fechaDocumentoOrigen;
	String idDocumentoOrigen;
	
	public DatosPeticionPagoTarjetaDto() {
		idDocumento = 0L;
		idTransaccion = "";
		fechaDocumentoOrigen = "";
	}

	
	public Long getIdDocumento() {
		return idDocumento;
	}

	
	public void setIdDocumento(Long idDocumento) {
		this.idDocumento = idDocumento;
	}

	
	public String getIdTransaccion() {
		return idTransaccion;
	}

	
	public void setIdTransaccion(String idTransaccion) {
		this.idTransaccion = idTransaccion;
	}

	
	public String getFechaDocumentoOrigen() {
		return fechaDocumentoOrigen;
	}

	
	public void setFechaDocumentoOrigen(String fechaDocumentoOrigen) {
		this.fechaDocumentoOrigen = fechaDocumentoOrigen;
	}


	
	public String getIdDocumentoOrigen() {
		return idDocumentoOrigen;
	}


	
	public void setIdDocumentoOrigen(String idDocumentoOrigen) {
		this.idDocumentoOrigen = idDocumentoOrigen;
	}
	
	
}
