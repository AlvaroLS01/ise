package com.comerzzia.bimbaylola.pos.persistence.impuestos.porcentajes;

import java.math.BigDecimal;
import java.util.Date;

public class ByLPorcentajeImpuesto {
    private String uidActividad;

    private Integer idGrupoImpuestos;

    private Date vigenciaDesde;

    private Long idTratImpuestos;

    private String codtratimp;

    private String destratimp;

    private String aplicaRecargo;

    private String codimp;

    private String desimp;

    private BigDecimal porcentaje;

    private BigDecimal porcentajeRecargo;

    private String codimpFiscal;

    private String codpais;

    private BigDecimal porcentajeRecargo2;

    private BigDecimal porcentajeRecargo3;

    private BigDecimal porcentajeRecargo4;

    private BigDecimal porcentajeRecargo5;

    public String getUidActividad() {
        return uidActividad;
    }

    public void setUidActividad(String uidActividad) {
        this.uidActividad = uidActividad == null ? null : uidActividad.trim();
    }

    public Integer getIdGrupoImpuestos() {
        return idGrupoImpuestos;
    }

    public void setIdGrupoImpuestos(Integer idGrupoImpuestos) {
        this.idGrupoImpuestos = idGrupoImpuestos;
    }

    public Date getVigenciaDesde() {
        return vigenciaDesde;
    }

    public void setVigenciaDesde(Date vigenciaDesde) {
        this.vigenciaDesde = vigenciaDesde;
    }

    public Long getIdTratImpuestos() {
        return idTratImpuestos;
    }

    public void setIdTratImpuestos(Long idTratImpuestos) {
        this.idTratImpuestos = idTratImpuestos;
    }

    public String getCodtratimp() {
        return codtratimp;
    }

    public void setCodtratimp(String codtratimp) {
        this.codtratimp = codtratimp == null ? null : codtratimp.trim();
    }

    public String getDestratimp() {
        return destratimp;
    }

    public void setDestratimp(String destratimp) {
        this.destratimp = destratimp == null ? null : destratimp.trim();
    }

    public String getAplicaRecargo() {
        return aplicaRecargo;
    }

    public void setAplicaRecargo(String aplicaRecargo) {
        this.aplicaRecargo = aplicaRecargo == null ? null : aplicaRecargo.trim();
    }

    public String getCodimp() {
        return codimp;
    }

    public void setCodimp(String codimp) {
        this.codimp = codimp == null ? null : codimp.trim();
    }

    public String getDesimp() {
        return desimp;
    }

    public void setDesimp(String desimp) {
        this.desimp = desimp == null ? null : desimp.trim();
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public BigDecimal getPorcentajeRecargo() {
        return porcentajeRecargo;
    }

    public void setPorcentajeRecargo(BigDecimal porcentajeRecargo) {
        this.porcentajeRecargo = porcentajeRecargo;
    }

    public String getCodimpFiscal() {
        return codimpFiscal;
    }

    public void setCodimpFiscal(String codimpFiscal) {
        this.codimpFiscal = codimpFiscal == null ? null : codimpFiscal.trim();
    }

    public String getCodpais() {
        return codpais;
    }

    public void setCodpais(String codpais) {
        this.codpais = codpais == null ? null : codpais.trim();
    }

    public BigDecimal getPorcentajeRecargo2() {
        return porcentajeRecargo2;
    }

    public void setPorcentajeRecargo2(BigDecimal porcentajeRecargo2) {
        this.porcentajeRecargo2 = porcentajeRecargo2;
    }

    public BigDecimal getPorcentajeRecargo3() {
        return porcentajeRecargo3;
    }

    public void setPorcentajeRecargo3(BigDecimal porcentajeRecargo3) {
        this.porcentajeRecargo3 = porcentajeRecargo3;
    }

    public BigDecimal getPorcentajeRecargo4() {
        return porcentajeRecargo4;
    }

    public void setPorcentajeRecargo4(BigDecimal porcentajeRecargo4) {
        this.porcentajeRecargo4 = porcentajeRecargo4;
    }

    public BigDecimal getPorcentajeRecargo5() {
        return porcentajeRecargo5;
    }

    public void setPorcentajeRecargo5(BigDecimal porcentajeRecargo5) {
        this.porcentajeRecargo5 = porcentajeRecargo5;
    }
}