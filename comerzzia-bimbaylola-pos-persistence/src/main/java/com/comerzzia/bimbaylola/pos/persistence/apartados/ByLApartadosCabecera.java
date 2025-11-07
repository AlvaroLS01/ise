package com.comerzzia.bimbaylola.pos.persistence.apartados;

public class ByLApartadosCabecera extends ByLApartadosCabeceraKey {
    private String codCaja;

    public String getCodCaja() {
        return codCaja;
    }

    public void setCodCaja(String codCaja) {
        this.codCaja = codCaja == null ? null : codCaja.trim();
    }
}