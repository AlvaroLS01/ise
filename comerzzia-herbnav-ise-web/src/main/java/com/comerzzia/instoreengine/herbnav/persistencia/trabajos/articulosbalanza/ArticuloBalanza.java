package com.comerzzia.instoreengine.herbnav.persistencia.trabajos.articulosbalanza;

import java.math.BigDecimal;

public class ArticuloBalanza {

	private String codArt;
	
	private String desArt;
	
	private String tipo;
	
	private BigDecimal precioTotal;
	
	private String codSeccion;
	
	private String observaciones;
	
	private String diasCaducidad;
	
	private String extraObservaciones;

	public String getCodArt() {
		return codArt;
	}

	public void setCodArt(String codArt) {
		this.codArt = codArt;
	}

	public String getDesArt() {
		return desArt;
	}

	public void setDesArt(String desArt) {
		this.desArt = desArt;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public BigDecimal getPrecioTotal() {
		return precioTotal;
	}

	public void setPrecioTotal(BigDecimal precioTotal) {
		this.precioTotal = precioTotal;
	}

	public String getCodSeccion() {
		return codSeccion;
	}

	public void setCodSeccion(String codSeccion) {
		this.codSeccion = codSeccion;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getDiasCaducidad() {
		return diasCaducidad;
	}

	public void setDiasCaducidad(String diasCaducidad) {
		this.diasCaducidad = diasCaducidad;
	}

	public String getExtraObservaciones() {
		return extraObservaciones;
	}

	public void setExtraObservaciones(String extraObservaciones) {
		this.extraObservaciones = extraObservaciones;
	}
	
}
