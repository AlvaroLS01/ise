package com.comerzzia.bricodepot.backoffice.persistence.motivos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.comerzzia.core.model.i18n.InternacionalizableBean;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "motivo")
@XmlType(propOrder = { "uidActividad", "codigo" })
public class MotivosKey extends InternacionalizableBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "uidActividad")
	private String uidActividad;

	@XmlElement(name = "codigo")
	private String codigo;

	public String getUidActividad() {
		return uidActividad;
	}

	public void setUidActividad(String uidActividad) {
		this.uidActividad = uidActividad == null ? null : uidActividad.trim();
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo == null ? null : codigo.trim();
	}

}