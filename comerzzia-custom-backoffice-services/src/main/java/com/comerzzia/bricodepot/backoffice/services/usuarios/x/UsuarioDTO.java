package com.comerzzia.bricodepot.backoffice.services.usuarios.x;

public class UsuarioDTO {

	protected String uidInstancia;
	protected Long idUsuario;
	protected String usuario;
	protected String desUsuario;
	protected String clave;
	protected Boolean activo;
	protected String uidMenuPorDefecto;
	protected Boolean puedeCambiarMenu;

	public String getUidInstancia() {
		return uidInstancia;
	}

	public void setUidInstancia(String uidInstancia) {
		this.uidInstancia = uidInstancia;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getDesUsuario() {
		return desUsuario;
	}

	public void setDesUsuario(String desUsuario) {
		this.desUsuario = desUsuario;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
	
	public void setActivo(String activo) {
		if(activo != null && !activo.isEmpty() && activo.equalsIgnoreCase("S")) {
			this.activo = true;
		}
		else {
			this.activo = false;
		}
	}

	public String getUidMenuPorDefecto() {
		return uidMenuPorDefecto;
	}

	public void setUidMenuPorDefecto(String uidMenuPorDefecto) {
		this.uidMenuPorDefecto = uidMenuPorDefecto;
	}

	public Boolean getPuedeCambiarMenu() {
		return puedeCambiarMenu;
	}

	public void setPuedeCambiarMenu(Boolean puedeCambiarMenu) {
		this.puedeCambiarMenu = puedeCambiarMenu;
	}
	
	public String getPuedeCambiarMenuAsString() {
		return puedeCambiarMenu ? "S" : "N"; 
	}

}
