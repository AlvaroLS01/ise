package com.comerzzia.bricodepot.backoffice.persistence.ventas.cajas.fuerte;

import java.math.BigDecimal;

public class Caja90Dto {

	public BigDecimal importe ; 
	public String uidDiarioCaja;
	
	public BigDecimal getImporte() {
		return importe;
	}
	
	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}
	
	public String getUidDiarioCaja() {
		return uidDiarioCaja;
	}
	
	public void setUidDiarioCaja(String uidDiarioCaja) {
		this.uidDiarioCaja = uidDiarioCaja;
	}
	
	
}
