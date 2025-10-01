package com.comerzzia.bricodepot.backoffice.persistence.devoluciones.validacion;

import java.util.Date;

import com.comerzzia.core.util.base.ParametrosBuscarBean;


public class ParametrosBuscarValidacionesDevolucionAlbaran extends ParametrosBuscarBean{
	
	private static final long serialVersionUID = 4902103045263926028L;
	
	protected String uidActividad;
	protected Date fechaDesde;
	protected Date fechaHasta;
	protected String codAlm;
	protected String desAlm;
	protected String idUsuarioSupervisor;
	protected String desUsuarioSupervisor;
	protected String codArt;
	protected String desArt;
	protected String referenciaCliente;
	protected String validado;
	
	//Como la fecha de albaranes está separada en fecha y hora, se descompone para el sql
	protected String fechaDesdeTime;
	protected String fechaHastaTime;
	
	public ParametrosBuscarValidacionesDevolucionAlbaran(){
		super();
		this.setOrden("alb_cab.fecha DESC");
	}

	public String getUidActividad() {
		return uidActividad;
	}

	public void setUidActividad(String uidActividad) {
		this.uidActividad = uidActividad;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public String getCodAlm() {
		return codAlm;
	}

	public void setCodAlm(String codAlm) {
		this.codAlm = codAlm;
	}

	public String getDesAlm() {
		return desAlm;
	}

	public void setDesAlm(String desAlm) {
		this.desAlm = desAlm;
	}

	public String getIdUsuarioSupervisor() {
		return idUsuarioSupervisor;
	}

	public void setIdUsuarioSupervisor(String idUsuarioSupervisor) {
		this.idUsuarioSupervisor = idUsuarioSupervisor;
	}

	public String getDesUsuarioSupervisor() {
		return desUsuarioSupervisor;
	}

	public void setDesUsuarioSupervisor(String desUsuarioSupervisor) {
		this.desUsuarioSupervisor = desUsuarioSupervisor;
	}

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

	public String getReferenciaCliente() {
		return referenciaCliente;
	}

	public void setReferenciaCliente(String referenciaCliente) {
		this.referenciaCliente = referenciaCliente;
	}

	public String getFechaDesdeTime() {
		return fechaDesdeTime;
	}

	public void setFechaDesdeTime(String fechaDesdeTime) {
		this.fechaDesdeTime = fechaDesdeTime;
	}

	public String getFechaHastaTime() {
		return fechaHastaTime;
	}

	public void setFechaHastaTime(String fechaHastaTime) {
		this.fechaHastaTime = fechaHastaTime;
	}

	public String getValidado() {
		return validado;
	}

	public void setValidado(String validado) {
		this.validado = validado;
	}





}