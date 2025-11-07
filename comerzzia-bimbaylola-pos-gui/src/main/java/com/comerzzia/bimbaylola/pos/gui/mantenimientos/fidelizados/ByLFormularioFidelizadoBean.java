package com.comerzzia.bimbaylola.pos.gui.mantenimientos.fidelizados;

import java.util.Date;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.comerzzia.pos.core.gui.validation.FormularioGui;

@Component
@Scope("prototype")
public class ByLFormularioFidelizadoBean extends FormularioGui{
	
	
	@Size(max=30, message="La longitud del campo Código no puede superar los {max} caracteres.")
	private String codigo;
	
	private String numeroTarjeta;
	
	@NotEmpty(message="Debe rellenar el campo nombre")
	@Size(max=45, message="La longitud del campo Nombre no puede superar los {max} caracteres.")
	private String nombre;
	
	@NotEmpty(message="Debe rellenar el campo apellidos")
	@Size(max=45, message="La longitud del campo Apellidos no puede superar los {max} caracteres.")
	private String apellidos;
	
	private String tipoDocumento;
	
	@Size(max=20, message="La longitud del campo Documento no puede superar los {max} caracteres.")
	private String documento;
	
	private String sexo;
	
	private String estadoCivil;
	
	private Date fechaNacimiento;
	
	@Size(max=255, message="La longitud del campo Email no puede superar los {max} caracteres.")
	private String email;
	
	@Size(max=255, message="La longitud del campo Móvil no puede superar los {max} caracteres.")
	private String movil;
	
	
	@Size(max=4, message="La longitud del campo Código de Pais no puede superar los {max} caracteres.")
	private String codPais;
	private String desPais;
	
	@Size(max=8, message="La longitud del campo Código Postal no puede superar los {max} caracteres.")
	private String cp;
	
	@Size(max=50, message="La longitud del campo Provincia no puede superar los {max} caracteres.")
	private String provincia;
	
	@Size(max=50, message="La longitud del campo Población no puede superar los {max} caracteres.")
	private String poblacion;
	
	@Size(max=50, message="La Longitud del campo Localidad no puede superar los {max} caracteres.")
	private String localidad;
	
	@Size(max=50, message="La longitud del campo Domicilio no puede superar los {max} caracteres.")
	private String domicilio;
	
	@Size(max=255, message = "La longitud del campo Observaciones no puede superar los {max} caracteres.")
	private String observaciones;
	
	private String codAlmFav;
	private String desAlmFav;
	
	private String codColectivo;
	private String desColectivo;

	@Override
	public void limpiarFormulario() {
		codigo = "";
		numeroTarjeta = "";
		nombre = "";
		apellidos = "";
		tipoDocumento = null;
		documento = "";
		fechaNacimiento = null;
		sexo = null;
		estadoCivil = null;
		email = "";
		movil = "";
		codPais = "";
		desPais = "";
		cp = "";
		provincia = "";
		poblacion = "";
		localidad = "";
		domicilio = "";
		codAlmFav = "";
		desAlmFav = "";
		codColectivo = "";
		desColectivo = "";
		observaciones = "";
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNumeroTarjeta() {
		return numeroTarjeta;
	}

	public void setNumeroTarjeta(String numeroTarjeta) {
		this.numeroTarjeta = numeroTarjeta;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMovil() {
		return movil;
	}

	public void setMovil(String movil) {
		this.movil = movil;
	}

	public String getCodPais() {
		return codPais;
	}

	public void setCodPais(String codPais) {
		this.codPais = codPais;
	}

	public String getDesPais() {
		return desPais;
	}

	public void setDesPais(String desPais) {
		this.desPais = desPais;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getPoblacion() {
		return poblacion;
	}

	public void setPoblacion(String poblacion) {
		this.poblacion = poblacion;
	}
	
	public String getLocalidad(){
		return localidad;
	}
	
	public void setLocalidad(String localidad){
		this.localidad = localidad;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getCodAlmFav() {
		return codAlmFav;
	}

	public void setCodAlmFav(String codAlmFav) {
		this.codAlmFav = codAlmFav;
	}

	public String getDesAlmFav() {
		return desAlmFav;
	}

	public void setDesAlmFav(String desAlmFav) {
		this.desAlmFav = desAlmFav;
	}

	public String getCodColectivo() {
		return codColectivo;
	}

	public void setCodColectivo(String codColectivo) {
		this.codColectivo = codColectivo;
	}

	public String getDesColectivo() {
		return desColectivo;
	}

	public void setDesColectivo(String desColectivo) {
		this.desColectivo = desColectivo;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

}
