package com.comerzzia.bricodepot.backoffice.persistence.ventas.cajas.cierre;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import com.comerzzia.core.omnichannel.engine.persistence.cajas.CajaKey;


@XmlAccessorType(XmlAccessType.FIELD)
public class CajaBean extends CajaKey {
    private String codAlmacen;
 
    private String codCaja;

    private Date fechaApertura;

    private Date fechaCierre;

    private Date fechaEnvio;

    private String usuario;

    private String usuarioCierre;
    
    private Date fechaContable;
    
    //INICIO ATRIBUTOS PERSONALIZADOS--------------------------------------------
    
    //FIN ATRIBUTOS PERSONALIZADOS-----------------------------------------------


    public String getCodAlmacen() {
        return codAlmacen;
    }

    public void setCodAlmacen(String codAlmacen) {
        this.codAlmacen = codAlmacen == null ? null : codAlmacen.trim();
    }

    public String getCodCaja() {
        return codCaja;
    }

    public void setCodCaja(String codcaja) {
        this.codCaja = codcaja == null ? null : codcaja.trim();
    }

    public Date getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(Date fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public Date getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario == null ? null : usuario.trim();
    }

    public String getUsuarioCierre() {
        return usuarioCierre;
    }

    public void setUsuarioCierre(String usuarioCierre) {
        this.usuarioCierre = usuarioCierre == null ? null : usuarioCierre.trim();
    }

	public Date getFechaContable() {
		return fechaContable;
	}

	public void setFechaContable(Date fechaContable) {
		this.fechaContable = fechaContable;
	}
    
    //INICIO MÉTODOS PERSONALIZADOS--------------------------------------------
    
    //FIN MÉTODOS PERSONALIZADOS-----------------------------------------------

}