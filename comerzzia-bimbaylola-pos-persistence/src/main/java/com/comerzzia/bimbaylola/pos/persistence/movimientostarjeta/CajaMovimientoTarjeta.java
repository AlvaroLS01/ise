package com.comerzzia.bimbaylola.pos.persistence.movimientostarjeta;

import java.math.BigDecimal;

public class CajaMovimientoTarjeta extends CajaMovimientoTarjetaKey {

	private static final long serialVersionUID = -6730298219174903356L;

	private BigDecimal base;

	private BigDecimal impuestos;

	private byte[] respuestaTarjeta;

	public BigDecimal getBase() {
		return base;
	}

	public void setBase(BigDecimal base) {
		this.base = base;
	}

	public BigDecimal getImpuestos() {
		return impuestos;
	}

	public void setImpuestos(BigDecimal impuestos) {
		this.impuestos = impuestos;
	}

	public byte[] getRespuestaTarjeta() {
		return respuestaTarjeta;
	}

	public void setRespuestaTarjeta(byte[] respuestaTarjeta) {
		this.respuestaTarjeta = respuestaTarjeta;
	}
}