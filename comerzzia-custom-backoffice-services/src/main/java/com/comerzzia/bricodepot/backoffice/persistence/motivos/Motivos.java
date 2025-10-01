package com.comerzzia.bricodepot.backoffice.persistence.motivos;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.comerzzia.core.model.i18n.I18NBean;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "codigo_tipo", "descripcion", "comentario" })
public class Motivos extends MotivosKey {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String CLASE_DESCRIPCION = "X_MOTIVOS_TBL.DESCRIPCION";

	@XmlTransient
	private boolean isTraduccionesCargadas;

	@XmlElement(name = "codigo_tipo")
	private String codigoTipo;

	@XmlElement(name = "descripcion")
	private String descripcion;

	@XmlElement(name = "comentario")
	private String comentario;

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion == null ? null : descripcion.trim();
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario == null ? null : comentario.trim();
	}

	public boolean isTraduccionesCargadas() {
		return isTraduccionesCargadas;
	}

	public void setTraduccionesCargadas(boolean isTraduccionesCargadas) {
		this.isTraduccionesCargadas = isTraduccionesCargadas;
	}

	public String getCodigoTipo() {
		return codigoTipo;
	}

	public void setCodigoTipo(String codigoTipo) {
		this.codigoTipo = codigoTipo;
	}

	public List<I18NBean> getTraduccionesDescripcion() {
		return getMapaTraducciones().get(CLASE_DESCRIPCION);
	}

	public void setTraduccionesDescripcion(List<I18NBean> traducciones) {
		this.getMapaTraducciones().put(CLASE_DESCRIPCION, traducciones);
	}
}
