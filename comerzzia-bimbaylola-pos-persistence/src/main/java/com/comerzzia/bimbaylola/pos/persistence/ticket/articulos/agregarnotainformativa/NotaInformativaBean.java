package com.comerzzia.bimbaylola.pos.persistence.ticket.articulos.agregarnotainformativa;

public class NotaInformativaBean {
	
	private String codigo;
	private String descripcion;
	private String texto;
	private String fecha;
	
	public NotaInformativaBean() {
		super();
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

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
}
