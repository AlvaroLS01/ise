package com.comerzzia.bricodepot.backoffice.persistence.facturas;

import java.util.Date;

public class BricodepotFacturasBean {

	private String codalm;
	private String codcaja;
	private String usuario;
	private Date fecha;
	private Integer idTipoDoc;
	private String referenciaCliente;
	private String cif;
	private String codart;
	private String desart;
	private String descli;
	private Double importeTotal;
	private String uidActividad;
	private String uidTicket;

	public String getCodalm() {
		return codalm;
	}

	public void setCodalm(String codalm) {
		this.codalm = codalm;
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

	public Date getFecha() {
	    return fecha;

	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Integer getIdTipoDoc() {
		return idTipoDoc;
	}

	public void setIdTipoDoc(Integer idTipoDoc) {
		this.idTipoDoc = idTipoDoc;
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

	
	public String getUidTicket() {
		return uidTicket;
	}

	
	public void setUidTicket(String uidTicket) {
		this.uidTicket = uidTicket;
	}
	
}
