package com.comerzzia.bimbaylola.pos.persistence.ventas.cajas.apertura;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "cabecera")
public class AperturaCajaCabecera {

	@XmlElement(name = "uidActividad", required = true)
	private String uidActividad;

	@XmlElement(name = "uidDiarioCaja", required = true)
	private String uidDiarioCaja;

	@XmlElement(name = "uidTicket", required = true)
	private String uidTicket;

	@XmlElement(name = "codTicket", required = true)
	private String codTicket;

	@XmlElement(name = "codEmp", required = true)
	private String codEmp;

	@XmlElement(name = "codAlmacen", required = true)
	private String codAlmacen;

	@XmlElement(name = "codCaja", required = true)
	private String codCaja;

	@XmlElement(name = "fechaApertura", required = true)
	private Date fechaApertura;

	@XmlElement(name = "usuario", required = true)
	private String usuario;

	@XmlElement(name = "turno", required = true)
	private Integer turno;

	@XmlElement(name = "importeApertura", required = true)
	private BigDecimal importeApertura;

	@XmlElement(name = "moneda", required = true)
	private String moneda;

	public AperturaCajaCabecera() {
	}

	public AperturaCajaCabecera(String uidActividad, String uidDiarioCaja, String uidTicket, String codTicket, String codEmp, String codAlmacen, String codCaja, Date fechaApertura, String usuario,
	        Integer turno, BigDecimal importeApertura, String moneda) {
		super();
		this.uidActividad = uidActividad;
		this.uidDiarioCaja = uidDiarioCaja;
		this.uidTicket = uidTicket;
		this.codTicket = codTicket;
		this.codEmp = codEmp;
		this.codAlmacen = codAlmacen;
		this.codCaja = codCaja;
		this.fechaApertura = fechaApertura;
		this.usuario = usuario;
		this.turno = turno;
		this.importeApertura = importeApertura;
		this.moneda = moneda;
	}

	public String getUidActividad() {
		return uidActividad;
	}

	public void setUidActividad(String uidActividad) {
		this.uidActividad = uidActividad;
	}

	public String getUidDiarioCaja() {
		return uidDiarioCaja;
	}

	public void setUidDiarioCaja(String uidDiarioCaja) {
		this.uidDiarioCaja = uidDiarioCaja;
	}

	public String getUidTicket() {
		return uidTicket;
	}

	public void setUidTicket(String uidTicket) {
		this.uidTicket = uidTicket;
	}

	public String getCodTicket() {
		return codTicket;
	}

	public void setCodTicket(String codTicket) {
		this.codTicket = codTicket;
	}

	public String getCodEmp() {
		return codEmp;
	}

	public void setCodEmp(String codEmp) {
		this.codEmp = codEmp;
	}

	public String getCodAlmacen() {
		return codAlmacen;
	}

	public void setCodAlmacen(String codAlmacen) {
		this.codAlmacen = codAlmacen;
	}

	public String getCodCaja() {
		return codCaja;
	}

	public void setCodCaja(String codCaja) {
		this.codCaja = codCaja;
	}

	public Date getFechaApertura() {
		return fechaApertura;
	}

	public void setFechaApertura(Date fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Integer getTurno() {
		return turno;
	}

	public void setTurno(Integer turno) {
		this.turno = turno;
	}

	public BigDecimal getImporteApertura() {
		return importeApertura;
	}

	public void setImporteApertura(BigDecimal importeApertura) {
		this.importeApertura = importeApertura;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	@Override
	public String toString() {
		return "Cabecera [uidActividad=" + uidActividad + ", uidDiarioCaja=" + uidDiarioCaja + ", uidTicket=" + uidTicket + ", codAlmacen=" + codAlmacen + ", codCaja=" + codCaja + ", fechaApertura="
		        + fechaApertura + ", usuario=" + usuario + ", turno=" + turno + ", importeApertura=" + importeApertura + ", moneda=" + moneda + "]";
	}

}
