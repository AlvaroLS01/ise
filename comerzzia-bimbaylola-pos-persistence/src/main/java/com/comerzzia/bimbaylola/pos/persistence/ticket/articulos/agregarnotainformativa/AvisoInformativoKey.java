package com.comerzzia.bimbaylola.pos.persistence.ticket.articulos.agregarnotainformativa;

import com.comerzzia.core.util.base.MantenimientoBean;

@SuppressWarnings("serial")
public class AvisoInformativoKey extends MantenimientoBean{

	private String uidActividad;

	private String codpais;

	private String codigo;

	private String descripcion;

	public String getDescripcion(){
		return descripcion;
	}

	public void setDescripcion(String descripcion){
		this.descripcion = descripcion;
	}

	public String getUidActividad(){
		return uidActividad;
	}

	public void setUidActividad(String uidActividad){
		this.uidActividad = uidActividad == null ? null : uidActividad.trim();
	}

	public String getCodpais(){
		return codpais;
	}

	public void setCodpais(String codpais){
		this.codpais = codpais == null ? null : codpais.trim();
	}

	public String getCodigo(){
		return codigo;
	}

	public void setCodigo(String codigo){
		this.codigo = codigo == null ? null : codigo.trim();
	}

	@Override
	protected void initNuevoBean(){}

}