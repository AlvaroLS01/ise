package com.comerzzia.bricodepot.backoffice.persistence.erroresinterfaces;

import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;

public class ErrorInterfaz extends ErrorInterfazKey {

	private static final long serialVersionUID = -4689227412416001236L;

	private String idClase;

	private String idObjeto;

	private Date fechaInicio;

	private Date ultimoError;

	private String ultimoDocumento;

	private String ultimoMensaje;

	private Date fechaFin;

	private Short tiendasImplicadas;

	private String ultimoMensajeTraza;

	public String getIdClase() {
		return idClase;
	}

	public void setIdClase(String idClase) {
		this.idClase = idClase;
	}

	public String getIdObjeto() {
		return idObjeto;
	}

	public void setIdObjeto(String idObjeto) {
		this.idObjeto = idObjeto;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getUltimoError() {
		return ultimoError;
	}

	public void setUltimoError(Date ultimoError) {
		this.ultimoError = ultimoError;
	}

	public String getUltimoDocumento() {
		return ultimoDocumento;
	}

	public void setUltimoDocumento(String ultimoDocumento) {
		this.ultimoDocumento = ultimoDocumento;
	}

	public String getUltimoMensaje() {
		return ultimoMensaje;
	}

	public void setUltimoMensaje(String ultimoMensaje) {
		this.ultimoMensaje = ultimoMensaje;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Short getTiendasImplicadas() {
		return tiendasImplicadas;
	}

	public void setTiendasImplicadas(Short tiendasImplicadas) {
		this.tiendasImplicadas = tiendasImplicadas;
	}

	public String getUltimoMensajeTraza() {
		return ultimoMensajeTraza;
	}

	public void setUltimoMensajeTraza(String ultimoMensajeTraza) {
		this.ultimoMensajeTraza = ultimoMensajeTraza;
	}

	// INICIO MÉTODOS PERSONALIZADOS--------------------------------------------

	public String getUltimoMensajeTrazaEscape() {
		return StringEscapeUtils.escapeJavaScript(ultimoMensajeTraza);
	}

	// FIN MÉTODOS PERSONALIZADOS-----------------------------------------------

}