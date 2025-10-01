package com.comerzzia.bricodepot.backoffice.services.motivos;

import com.comerzzia.core.util.base.ParametrosBuscarBean;

public class ParametrosBuscarMotivosBean extends ParametrosBuscarBean {

	private static final long serialVersionUID = -5473378946868254030L;

	protected String codigo;
	protected String descripcion;

	public ParametrosBuscarMotivosBean() {
		super();
		setOrden("DESCRIPCION");
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
