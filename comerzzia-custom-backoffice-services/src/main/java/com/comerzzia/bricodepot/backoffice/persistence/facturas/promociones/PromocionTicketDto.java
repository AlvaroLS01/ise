package com.comerzzia.bricodepot.backoffice.persistence.facturas.promociones;

import java.math.BigDecimal;

public class PromocionTicketDto {
	Long idPromocion;
	String textoPromocion;
	BigDecimal importeTotalAhorro;
	
	
	public PromocionTicketDto() {
		idPromocion = 0L;
		textoPromocion = "";
		importeTotalAhorro = BigDecimal.ZERO;
	}

	public Long getIdPromocion() {
		return idPromocion;
	}
	
	public void setIdPromocion(Long idPromocion) {
		this.idPromocion = idPromocion;
	}
	
	public String getTextoPromocion() {
		return textoPromocion;
	}
	
	public void setTextoPromocion(String textoPromocion) {
		this.textoPromocion = textoPromocion;
	}
	
	public BigDecimal getImporteTotalAhorro() {
		return importeTotalAhorro;
	}
	
	public void setImporteTotalAhorro(BigDecimal importeTotalAhorro) {
		this.importeTotalAhorro = importeTotalAhorro;
	}
	
	
}
