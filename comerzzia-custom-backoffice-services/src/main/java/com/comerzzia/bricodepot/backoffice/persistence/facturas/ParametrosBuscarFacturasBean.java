package com.comerzzia.bricodepot.backoffice.persistence.facturas;

import java.util.Date;

import com.comerzzia.core.util.base.ParametrosBuscarBean;

public class ParametrosBuscarFacturasBean extends ParametrosBuscarBean {

	private static final long serialVersionUID = 2545517460587204397L;

	protected String codalm;
	protected String desalm;
	protected String codcaja;
	protected String usuario;
	protected Date fechaDesde;
	protected Date fechaHasta;
	protected Long idTipoDoc;
	protected String codTipoDoc;
	protected String desTipoDoc;
	protected String referenciaCliente;
	protected String cif;
	protected String codart;
	protected String desart;
	protected String codcli;
	protected String descli;
	protected Double importeTotal;
	protected String uidActividad;
	protected String uidTicket;

	public ParametrosBuscarFacturasBean() {
		super();
		this.setOrden("FECHA DESC");
	}

	public String getCodalm() {
		return codalm;
	}

	public void setCodalm(String codalm) {
		this.codalm = codalm;
	}

	public String getDesalm() {
		return desalm;
	}

	public void setDesalm(String desalm) {
		this.desalm = desalm;
	}

	public String getCodcaja() {
		return codcaja;
	}

	public void setCodcaja(String codcaja) {
		this.codcaja = codcaja;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
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

	public Long getIdTipoDoc() {
		return idTipoDoc;
	}

	public void setIdTipoDoc(Long idTipoDoc) {
		this.idTipoDoc = idTipoDoc;
	}
	
	public String getCodTipoDoc() {
		return codTipoDoc;
	}

	public void setCodTipoDoc(String codTipoDoc) {
		this.codTipoDoc = codTipoDoc;
	}

	public String getDesTipoDoc() {
		return desTipoDoc;
	}

	public void setDesTipoDoc(String desTipoDoc) {
		this.desTipoDoc = desTipoDoc;
	}

	public String getReferenciaCliente() {
		return referenciaCliente;
	}

	public void setReferenciaCliente(String referenciaCliente) {
		this.referenciaCliente = referenciaCliente;
	}

	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public String getCodart() {
		return codart;
	}

	public void setCodart(String codart) {
		this.codart = codart;
	}

	public String getDesart() {
		return desart;
	}

	public void setDesart(String desart) {
		this.desart = desart;
	}
	
	public String getCodcli() {
		return codcli;
	}
	
	public void setCodcli(String codcli) {
		this.codcli = codcli;
	}

	public String getDescli() {
		return descli;
	}

	public void setDescli(String descli) {
		this.descli = descli;
	}

	public Double getImporteTotal() {
		return importeTotal;
	}

	public void setImporteTotal(Double importeTotal) {
		this.importeTotal = importeTotal;
	}

	public String getUidActividad() {
		return uidActividad;
	}

	public void setUidActividad(String uidActividad) {
		this.uidActividad = uidActividad;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	public String getUidTicket() {
		return uidTicket;
	}

	
	public void setUidTicket(String uidTicket) {
		this.uidTicket = uidTicket;
	}
	
	

}
