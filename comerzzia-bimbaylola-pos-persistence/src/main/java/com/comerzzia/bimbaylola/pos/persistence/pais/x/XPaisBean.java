package com.comerzzia.bimbaylola.pos.persistence.pais.x;

public class XPaisBean extends XPaisKey {
    private Integer prefijoTelefono;

    private String isoLenguaje;

    private Boolean filtrarClientesPais;
    
    //INICIO ATRIBUTOS PERSONALIZADOS--------------------------------------------
    
    //FIN ATRIBUTOS PERSONALIZADOS-----------------------------------------------


    public Integer getPrefijoTelefono() {
        return prefijoTelefono;
    }

    public void setPrefijoTelefono(Integer prefijoTelefono) {
        this.prefijoTelefono = prefijoTelefono;
    }

    public String getIsoLenguaje() {
        return isoLenguaje;
    }

    public void setIsoLenguaje(String isoLenguaje) {
        this.isoLenguaje = isoLenguaje == null ? null : isoLenguaje.trim();
    }

    public Boolean getFiltrarClientesPais() {
        return filtrarClientesPais;
    }

    public void setFiltrarClientesPais(Boolean filtrarClientesPais) {
        this.filtrarClientesPais = filtrarClientesPais;
    }
    
    //INICIO MÉTODOS PERSONALIZADOS--------------------------------------------
    
    //FIN MÉTODOS PERSONALIZADOS-----------------------------------------------

}