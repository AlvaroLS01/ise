package com.comerzzia.bimbaylola.pos.persistence.reservas;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.springframework.stereotype.Component;

@Component
@XmlAccessorType(XmlAccessType.FIELD)
public class ReservasPagoGiftCardBean extends ReservasPagoGiftCardKey{
	
	@XmlElement(name="numero_tarjeta")
	private String numeroTarjeta;
	@XmlElement(name="saldo")
	private BigDecimal saldo;
	@XmlElement(name="saldo_provisional")
	private BigDecimal saldoProvisional;
	@XmlElement(name="uid_transaccion")
	private String uidTransaccion;
	@XmlElement(name="importe_pago")
	private BigDecimal importePago;

	public String getNumeroTarjeta(){
		return numeroTarjeta;
	}

	public void setNumeroTarjeta(String numeroTarjeta){
		this.numeroTarjeta = numeroTarjeta == null ? null : numeroTarjeta.trim();
	}

	public BigDecimal getSaldo(){
		return saldo;
	}

	public void setSaldo(BigDecimal saldo){
		this.saldo = saldo;
	}

	public BigDecimal getSaldoProvisional(){
		return saldoProvisional;
	}

	public void setSaldoProvisional(BigDecimal saldoProvisional){
		this.saldoProvisional = saldoProvisional;
	}

	public String getUidTransaccion(){
		return uidTransaccion;
	}

	public void setUidTransaccion(String uidTransaccion){
		this.uidTransaccion = uidTransaccion == null ? null : uidTransaccion.trim();
	}

	public BigDecimal getImportePago(){
		return importePago;
	}

	public void setImportePago(BigDecimal importePago){
		this.importePago = importePago;
	}

}