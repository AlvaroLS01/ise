package com.comerzzia.bimbaylola.pos.persistence.cajas.recuentos;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class RecuentoBean {
	
	@XmlAttribute
	private String linea;
	@XmlElement
	private String uidActividad;
	@XmlElement
	private String codMedioPago;
	@XmlElement
	private Integer cantidad;
	@XmlElement
	private BigDecimal valor;
	@XmlElement
	private BigDecimal valorAbono;
	@XmlElement
	private String idD365;
	@XmlElement
	private String idD365Abono;
	
	public String getLinea() {
		return linea;
	}
	
	public void setLinea(String linea) {
		this.linea = linea;
	}
	
	public String getUidActividad() {
		return uidActividad;
	}
	
	public void setUidActividad(String uidActividad) {
		this.uidActividad = uidActividad;
	}
	
	public String getCodMedioPago() {
		return codMedioPago;
	}
	
	public void setCodMedioPago(String codMedioPago) {
		this.codMedioPago = codMedioPago;
	}
	
	public Integer getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	
	public BigDecimal getValor() {
		return valor;
	}
	
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	public BigDecimal getValorAbono() {
		return valorAbono;
	}
	
	public void setValorAbono(BigDecimal valorAbono) {
		this.valorAbono = valorAbono;
	}
	
	public String getIdD365() {
		return idD365;
	}
	
	public void setIdD365(String idD365) {
		this.idD365 = idD365;
	}

	public String getIdD365Abono() {
		return idD365Abono;
	}

	public void setIdD365Abono(String idD365Abono) {
		this.idD365Abono = idD365Abono;
	}

}
