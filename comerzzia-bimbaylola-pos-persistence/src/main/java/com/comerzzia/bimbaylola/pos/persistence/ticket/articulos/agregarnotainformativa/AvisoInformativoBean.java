package com.comerzzia.bimbaylola.pos.persistence.ticket.articulos.agregarnotainformativa;

public class AvisoInformativoBean extends AvisoInformativoKey{

	private static final long serialVersionUID = 1L;

	private String descripcion;

	private String fecha;

	private String texto;

	private String docuIndepe;

	private Long copias;

	private String codigo;

	public AvisoInformativoBean(){

	}

	public AvisoInformativoBean(String codigo){
		this.codigo = codigo;
	}

	public String getCodigo(){
		return codigo;
	}

	public void setCodigo(String codigo){
		this.codigo = codigo;
	}

	public String getDescripcion(){
		return descripcion;
	}

	public void setDescripcion(String descripcion){
		this.descripcion = descripcion == null ? null : descripcion.trim();
	}

	public String getFecha(){
		return fecha;
	}

	public void setFecha(String fecha){
		this.fecha = fecha == null ? null : fecha.trim();
	}

	public String getTexto(){
		return texto;
	}

	public void setTexto(String texto){
		this.texto = texto == null ? null : texto.trim();
	}

	public String getDocuIndepe(){
		return docuIndepe;
	}

	public void setDocuIndepe(String docuIndepe){
		this.docuIndepe = docuIndepe == null ? null : docuIndepe.trim();
	}

	public Long getCopias(){
		return copias;
	}

	public void setCopias(Long copias){
		this.copias = copias;
	}

}