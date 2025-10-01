package com.comerzzia.bricodepot.backoffice.persistence.tickets.auditoria;

import java.util.Date;

import com.comerzzia.core.util.base.ParametrosBuscarBean;

public class ParametrosBuscarEventoAuditoria extends ParametrosBuscarBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6057260001588702161L;
	
	protected String usuario;
	protected String usuarioSupervisor;
	protected Date fechaDesde;
	protected Date fechaHasta;
	
	public ParametrosBuscarEventoAuditoria() {
		super();
		this.setOrden("FECHA DESC");
	}
	
	public String getUsuario() {
		return usuario;
	}
	public String getUsuarioSupervisor() {
		return usuarioSupervisor;
	}
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public void setUsuarioSupervisor(String usuarioSupervisor) {
		this.usuarioSupervisor = usuarioSupervisor;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	
	

}
