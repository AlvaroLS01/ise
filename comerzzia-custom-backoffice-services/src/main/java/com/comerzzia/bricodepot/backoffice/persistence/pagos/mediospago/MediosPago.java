package com.comerzzia.bricodepot.backoffice.persistence.pagos.mediospago;

import java.math.BigDecimal;

public class MediosPago {

	private String uidActividad;
	private BigDecimal importeTotal;
	private String codMedioPago;
	private String desMedioPago;
	
	public String getUidActividad() {
		return uidActividad;
	}
	
	public void setUidActividad(String uidActividad) {
		this.uidActividad = uidActividad;
	}
	
	public BigDecimal getImporteTotal() {
		return importeTotal;
	}
	
	public void setImporteTotal(BigDecimal importeTotal) {
		this.importeTotal = importeTotal;
	}
	
	public String getCodMedioPago() {
		return codMedioPago;
	}
	
	public void setCodMedioPago(String codMedioPago) {
		this.codMedioPago = codMedioPago;
	}
	
	public String getDesMedioPago() {
		return desMedioPago;
	}
	
	public void setDesMedioPago(String desMedioPago) {
		this.desMedioPago = desMedioPago;
	}
	
}
