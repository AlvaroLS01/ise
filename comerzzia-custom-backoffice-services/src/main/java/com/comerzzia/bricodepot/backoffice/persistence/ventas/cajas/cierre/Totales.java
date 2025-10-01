package com.comerzzia.bricodepot.backoffice.persistence.ventas.cajas.cierre;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Totales implements Serializable {

	private static final long serialVersionUID = -2887340416982890914L;
	protected BigDecimal totalVentasEntrada = BigDecimal.ZERO;
	protected BigDecimal totalVentasSalida = BigDecimal.ZERO;
	protected BigDecimal totalApuntesEntrada = BigDecimal.ZERO;
	protected BigDecimal totalApuntesSalida = BigDecimal.ZERO;
	protected BigDecimal totalRecuento = BigDecimal.ZERO;

	public BigDecimal getTotalVentasEntrada() {
		return totalVentasEntrada;
	}

	public void setTotalVentasEntrada(BigDecimal totalVentasEntrada) {
		this.totalVentasEntrada = totalVentasEntrada;
	}

	public BigDecimal getTotalVentasSalida() {
		return totalVentasSalida;
	}

	public void setTotalVentasSalida(BigDecimal totalVentasSalida) {
		this.totalVentasSalida = totalVentasSalida;
	}

	public BigDecimal getTotalApuntesEntrada() {
		return totalApuntesEntrada;
	}

	public void setTotalApuntesEntrada(BigDecimal totalApuntesEntrada) {
		this.totalApuntesEntrada = totalApuntesEntrada;
	}

	public BigDecimal getTotalApuntesSalida() {
		return totalApuntesSalida;
	}

	public void setTotalApuntesSalida(BigDecimal totalApuntesSalida) {
		this.totalApuntesSalida = totalApuntesSalida;
	}

	public BigDecimal getTotalRecuento() {
		return totalRecuento;
	}

	public void setTotalRecuento(BigDecimal totalRecuento) {
		this.totalRecuento = totalRecuento;
	}
}
