package com.comerzzia.bricodepot.backoffice.persistence.devoluciones.informe;

public class InformeDevolucion extends InformeDevolucionKey {

	private String codigo;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo == null ? null : codigo.trim();
	}
}