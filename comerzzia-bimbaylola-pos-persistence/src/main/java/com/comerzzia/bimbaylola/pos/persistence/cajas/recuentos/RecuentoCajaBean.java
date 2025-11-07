package com.comerzzia.bimbaylola.pos.persistence.cajas.recuentos;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "recuento")
@XmlAccessorType(XmlAccessType.FIELD)
public class RecuentoCajaBean {

	@XmlElement
	private String uidActividad;
	@XmlElement(name = "codTicket", required = true)
	private String codTicket;
	@XmlElement(name = "codEmp", required = true)
	private String codEmp;
	@XmlElement(name = "uid_ticket")
	private String uidtTicket;
	@XmlElement
	private String uidDiarioCaja;
	@XmlElement
	private String codAlmacen;
	@XmlElement
	private String codCaja;
	@XmlElement(name = "fechaApertura")
	private Date fechaApertura;
	@XmlElement
	private Date fecha;
	@XmlElement
	private Integer turno;
	@XmlElement
	private String usuario;
	@XmlElement
	private String moneda;
	@XmlElementWrapper(name = "recuentos")
	@XmlElement(name = "recuento")
	private List<RecuentoBean> recuentos;

	public String getUidActividad() {
		return uidActividad;
	}

	public void setUidActividad(String uidActividad) {
		this.uidActividad = uidActividad;
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

	public String getUidtTicket() {
		return uidtTicket;
	}

	public void setUidtTicket(String uidtTicket) {
		this.uidtTicket = uidtTicket;
	}

	public String getUidDiarioCaja() {
		return uidDiarioCaja;
	}

	public void setUidDiarioCaja(String uidDiarioCaja) {
		this.uidDiarioCaja = uidDiarioCaja;
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

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Integer getTurno() {
		return turno;
	}

	public void setTurno(Integer turno) {
		this.turno = turno;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public List<RecuentoBean> getRecuentos() {
		return recuentos;
	}

	public void setRecuentos(List<RecuentoBean> recuentos) {
		this.recuentos = recuentos;
	}

	public Date getFechaApertura() {
		return fechaApertura;
	}

	public void setFechaApertura(Date fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

}
