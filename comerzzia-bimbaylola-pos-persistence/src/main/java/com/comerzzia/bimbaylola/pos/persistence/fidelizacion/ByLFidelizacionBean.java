package com.comerzzia.bimbaylola.pos.persistence.fidelizacion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.comerzzia.pos.persistence.fidelizacion.FidelizacionBean;

@XmlAccessorType(XmlAccessType.FIELD)
public class ByLFidelizacionBean extends FidelizacionBean {

	private String consentimientoUsodatos;
	private String consentimientoRecibenoti;
	private byte[] firma;
	private String email;
	private String telefono;

	@XmlElement(name = "tipo_identificacion")
	protected String tipoIdentificacion;

	public ByLFidelizacionBean() {
		super();
	}

	public String getConsentimientoUsodatos() {
		return consentimientoUsodatos;
	}

	public void setConsentimientoUsodatos(String consentimientoUsodatos) {
		this.consentimientoUsodatos = consentimientoUsodatos;
	}

	public String getConsentimientoRecibenoti() {
		return consentimientoRecibenoti;
	}

	public void setConsentimientoRecibenoti(String consentimientoRecibenoti) {
		this.consentimientoRecibenoti = consentimientoRecibenoti;
	}

	public byte[] getFirma() {
		return firma;
	}

	public void setFirma(byte[] firma) {
		this.firma = firma;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

}
