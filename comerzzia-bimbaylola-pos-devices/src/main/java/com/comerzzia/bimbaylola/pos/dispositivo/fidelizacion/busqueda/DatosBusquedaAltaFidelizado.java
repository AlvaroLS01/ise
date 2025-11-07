package com.comerzzia.bimbaylola.pos.dispositivo.fidelizacion.busqueda;

import org.apache.commons.lang.StringUtils;

/**
 * Objeto para almacenar los datos del formulario de búsqueda 
 * para pasarlos al alta de Fidelizado.
 */
public class DatosBusquedaAltaFidelizado{
	
	private String codigoCliente;
	private String documentoFiscal;
	private String nombre;
	private String apellidos;
	private String telefono;
	private String email;
	
	public String getCodigoCliente(){
		return codigoCliente;
	}
	public void setCodigoCliente(String codigoCliente){
		this.codigoCliente = codigoCliente;
	}
	public String getDocumentoFiscal(){
		return documentoFiscal;
	}
	public void setDocumentoFiscal(String documentoFiscal){
		this.documentoFiscal = documentoFiscal;
	}
	public String getNombre(){
		return nombre;
	}
	public void setNombre(String nombre){
		this.nombre = nombre;
	}
	public String getApellidos(){
		return apellidos;
	}
	public void setApellidos(String apellidos){
		this.apellidos = apellidos;
	}
	public String getTelefono(){
		return telefono;
	}
	public void setTelefono(String telefono){
		this.telefono = telefono;
	}
	public String getEmail(){
		return email;
	}
	public void setEmail(String email){
		this.email = email;
	}
	public String toString(){
		String respuesta = "";
		if(StringUtils.isNotBlank(codigoCliente)){
			respuesta += "Código del Cliente : " + codigoCliente; 
		}
		if(StringUtils.isNotBlank(documentoFiscal)){
			respuesta += "- Documento Fiscal : " + documentoFiscal; 			
		}
		if(StringUtils.isNotBlank(nombre)){
			respuesta += "- Nombre : " + nombre; 
		}
		if(StringUtils.isNotBlank(apellidos)){
			respuesta += "- Apellidos : " + apellidos; 
		}
		if(StringUtils.isNotBlank(telefono)){
			respuesta += "- Teléfono : " + telefono; 
		}
		if(StringUtils.isNotBlank(email)){
			respuesta += "- Email : " + email; 
		}
		return respuesta;
	}
	
}
