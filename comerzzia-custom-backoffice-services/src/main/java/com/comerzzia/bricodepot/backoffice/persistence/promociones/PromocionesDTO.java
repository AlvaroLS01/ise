package com.comerzzia.bricodepot.backoffice.persistence.promociones;

import java.math.BigDecimal;

public class PromocionesDTO {

	long idPromocion;
	long idTipoPromocion;
	String acceso;
	String codCupon;
	String codArticulo;
	String precioSinCupon;
	String precioConCupon;
	int idLinea;
	BigDecimal importeCupon;
	
	
	public int getIdLinea() {
		return idLinea;
	}

	
	public void setIdLinea(int idLinea) {
		this.idLinea = idLinea;
	}

	public long getIdPromocion() {
		return idPromocion;
	}
	
	public void setIdPromocion(long idPromocion) {
		this.idPromocion = idPromocion;
	}
	
	public long getIdTipoPromocion() {
		return idTipoPromocion;
	}
	
	public void setIdTipoPromocion(long idTipoPromocion) {
		this.idTipoPromocion = idTipoPromocion;
	}
	
	public String getAcceso() {
		return acceso;
	}
	
	public void setAcceso(String acceso) {
		this.acceso = acceso;
	}
	
	public String getCodCupon() {
		return codCupon;
	}
	
	public void setCodCupon(String codCupon) {
		this.codCupon = codCupon;
	}
	
	public String getCodArticulo() {
		return codArticulo;
	}
	
	public void setCodArticulo(String codArticulo) {
		this.codArticulo = codArticulo;
	}
	
	public String getPrecioSinCupon() {
		return precioSinCupon;
	}
	
	public void setPrecioSinCupon(String precioSinCupon) {
		this.precioSinCupon = precioSinCupon;
	}
	
	public String getPrecioConCupon() {
		return precioConCupon;
	}
	
	public void setPrecioConCupon(String precioConCupon) {
		this.precioConCupon = precioConCupon;
	}


	
	public BigDecimal getImporteCupon() {
		return importeCupon;
	}


	
	public void setImporteCupon(BigDecimal importeCupon) {
		this.importeCupon = importeCupon;
	}
	
	
	
}
