package com.comerzzia.bricodepot.backoffice.persistence.usuarios.x;

import java.util.Date;

public class UsuarioX extends UsuarioXKey {
    private Date ultimoCambio;

    private Long intentosLoginFallidos;

    private Long cambiosClave;

    private String bloqueado;

    public Date getUltimoCambio() {
        return ultimoCambio;
    }

    public void setUltimoCambio(Date ultimoCambio) {
        this.ultimoCambio = ultimoCambio;
    }

    public Long getIntentosLoginFallidos() {
        return intentosLoginFallidos;
    }

    public void setIntentosLoginFallidos(Long intentosLoginFallidos) {
        this.intentosLoginFallidos = intentosLoginFallidos;
    }

    public Long getCambiosClave() {
        return cambiosClave;
    }

    public void setCambiosClave(Long cambiosClave) {
        this.cambiosClave = cambiosClave;
    }

    public String getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(String bloqueado) {
        this.bloqueado = bloqueado == null ? null : bloqueado.trim();
    }
}