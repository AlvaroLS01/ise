package com.comerzzia.bricodepot.backoffice.persistence.ventas.cajas.movimientos;


public class MovimientoJasperBean {

	private String cargo;
	private String documento;
	private String fecha;
	private String usuario;
	
	public MovimientoJasperBean() {
		
	}
	
	public MovimientoJasperBean(String cargo, String documento, String fecha, String usuario) {
		super();
		this.cargo = cargo;
		this.documento = documento;
		this.fecha = fecha;
		this.usuario = usuario;
	}


	public String getCargo() {
		return cargo;
	}

	
	public String getDocumento() {
		return documento;
	}

	
	public String getFecha() {
		return fecha;
	}

	
	public String getUsuario() {
		return usuario;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
		
	}

	public void setDocumento(String documento) {
		this.documento = documento;
		
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
		
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
		
	}

}
