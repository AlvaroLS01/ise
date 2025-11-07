package com.comerzzia.bimbaylola.pos.persistence.ventas.cajas.movimientos;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "movimiento")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "uidActividad", "uidDiarioCaja", "codTicket", "uidTicket", "codEmp", "codAlmacen", "codCaja", "codConceptoMov", "fechaMovimiento", "usuario", "turno", "cargo", "abono", "moneda", "documento" })
public class MovimientosCajaBean {

	@XmlElement(name = "uidActividad", required = true)
	private String uidActividad;

	@XmlElement(name = "uidDiarioCaja", required = true)
	private String uidDiarioCaja;

	@XmlElement(name = "codTicket", required = true)
	private String codTicket;

	@XmlElement(name = "uidTicket", required = true)
	private String uidTicket;

	@XmlElement(name = "codEmp", required = true)
	private String codEmp;

	@XmlElement(name = "codAlmacen", required = true)
	private String codAlmacen;

	@XmlElement(name = "codCaja", required = true)
	private String codCaja;

	@XmlElement(name = "codConceptoMov", required = true)
	private String codConceptoMov;

	@XmlElement(name = "fechaMovimiento", required = true)
	private Date fechaMovimiento;

	@XmlElement(name = "usuario", required = true)
	private String usuario;

	@XmlElement(name = "turno", required = true)
	private Integer turno;

	@XmlElement(name = "cargo", required = true)
	private BigDecimal cargo;

	@XmlElement(name = "abono", required = true)
	private BigDecimal abono;

	@XmlElement(name = "moneda", required = true)
	private String moneda;
	
	@XmlElement(name = "documento", required = true)
	private String documento;

	public MovimientosCajaBean() {
		cargo = BigDecimal.ZERO;
		abono = BigDecimal.ZERO;
	}

	public MovimientosCajaBean(String uidActividad, String uidDiarioCaja, String codTicket, String uidTicket, String codEmp, String codAlmacen, String codCaja, String codConceptoMov,
	        Date fechaMovimiento, String usuario, Integer turno, BigDecimal cargo, BigDecimal abono, String moneda, String documento) {
		super();
		this.uidActividad = uidActividad;
		this.uidDiarioCaja = uidDiarioCaja;
		this.codTicket = codTicket;
		this.uidTicket = uidTicket;
		this.codEmp = codEmp;
		this.codAlmacen = codAlmacen;
		this.codCaja = codCaja;
		this.codConceptoMov = codConceptoMov;
		this.fechaMovimiento = fechaMovimiento;
		this.usuario = usuario;
		this.turno = turno;
		this.cargo = cargo;
		this.abono = abono;
		this.moneda = moneda;
		this.documento = documento;
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

	public String getCodTicket() {
		return codTicket;
	}

	public void setCodTicket(String codTicket) {
		this.codTicket = codTicket;
	}

	public String getUidTicket() {
		return uidTicket;
	}

	public void setUidTicket(String uidTicket) {
		this.uidTicket = uidTicket;
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

	public String getCodConceptoMov() {
		return codConceptoMov;
	}

	public void setCodConceptoMov(String codConceptoMov) {
		this.codConceptoMov = codConceptoMov;
	}

	public Date getFechaMovimiento() {
		return fechaMovimiento;
	}

	public void setFechaMovimiento(Date fechaMovimiento) {
		this.fechaMovimiento = fechaMovimiento;
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

	public BigDecimal getCargo() {
		return cargo;
	}

	public void setCargo(BigDecimal cargo) {
		this.cargo = cargo;
	}

	public BigDecimal getAbono() {
		return abono;
	}

	public void setAbono(BigDecimal abono) {
		this.abono = abono;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	
	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	@Override
	public String toString() {
		return "MovimientosCajaBean [uidActividad=" + uidActividad + ", uidDiarioCaja=" + uidDiarioCaja + ", uidTicket=" + uidTicket + ", codEmp=" + codEmp + ", codAlmacen=" + codAlmacen
		        + ", codCaja=" + codCaja + ", codConceptoMov=" + codConceptoMov + ", fechaMovimiento=" + fechaMovimiento + ", usuario=" + usuario + ", turno=" + turno + ", cargo=" + cargo + ", abono="
		        + abono + ", moneda=" + moneda +  ", documento=" + documento + "]";
	}

}
