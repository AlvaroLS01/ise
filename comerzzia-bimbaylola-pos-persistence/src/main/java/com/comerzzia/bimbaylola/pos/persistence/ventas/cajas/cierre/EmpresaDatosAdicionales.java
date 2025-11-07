package com.comerzzia.bimbaylola.pos.persistence.ventas.cajas.cierre;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class EmpresaDatosAdicionales {

	@XmlElement(name = "modelo_empresa")
	private String modeloEmpresa;

	@XmlElement(name = "tipo_impuesto")
	private String tipoImpuesto;

	@XmlElement(name = "cod_tipo_empresa_filial")
	private String codTipoEmpresaFilial;

	@XmlElement(name = "des_tipo_empresa_filial")
	private String desTipoEmpresaFilial;

	public EmpresaDatosAdicionales() {
	}

	public EmpresaDatosAdicionales(String modeloEmpresa, String tipoImpuesto, String codTipoEmpresaFilial, String desTipoEmpresaFilial) {
		super();
		this.modeloEmpresa = modeloEmpresa;
		this.tipoImpuesto = tipoImpuesto;
		this.codTipoEmpresaFilial = codTipoEmpresaFilial;
		this.desTipoEmpresaFilial = desTipoEmpresaFilial;
	}

	public String getModeloEmpresa() {
		return modeloEmpresa;
	}

	public void setModeloEmpresa(String modeloEmpresa) {
		this.modeloEmpresa = modeloEmpresa;
	}

	public String getTipoImpuesto() {
		return tipoImpuesto;
	}

	public void setTipoImpuesto(String tipoImpuesto) {
		this.tipoImpuesto = tipoImpuesto;
	}

	public String getCodTipoEmpresaFilial() {
		return codTipoEmpresaFilial;
	}

	public void setCodTipoEmpresaFilial(String codTipoEmpresaFilial) {
		this.codTipoEmpresaFilial = codTipoEmpresaFilial;
	}

	public String getDesTipoEmpresaFilial() {
		return desTipoEmpresaFilial;
	}

	public void setDesTipoEmpresaFilial(String desTipoEmpresaFilial) {
		this.desTipoEmpresaFilial = desTipoEmpresaFilial;
	}

	@Override
	public String toString() {
		return "EmpresaDatosAdicionales [modeloEmpresa=" + modeloEmpresa + ", tipoImpuesto=" + tipoImpuesto + ", codTipoEmpresaFilial=" + codTipoEmpresaFilial + ", desTipoEmpresaFilial="
		        + desTipoEmpresaFilial + "]";
	}

}