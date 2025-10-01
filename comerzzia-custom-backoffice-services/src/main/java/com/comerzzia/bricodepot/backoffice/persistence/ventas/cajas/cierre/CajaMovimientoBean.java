package com.comerzzia.bricodepot.backoffice.persistence.ventas.cajas.cierre;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.comerzzia.core.omnichannel.engine.persistence.cajas.movimientos.CajaMovimientoKey;
import com.comerzzia.core.omnichannel.engine.util.FormatUtil;


@XmlRootElement( name="movimiento" )
@XmlAccessorType(XmlAccessType.FIELD)
public class CajaMovimientoBean extends CajaMovimientoKey {
    private Date fecha;

    private BigDecimal cargo;

    private BigDecimal abono;

    private String concepto;

    private String documento;

    private String codMedioPago;

    private String idDocumento;
    
    @XmlElement(name = "codconcepto_mov")
    private String codConceptoMovimiento;

    private Long idTipoDocumento;

    private String uidTransaccionDet;

    private String coddivisa;

    private BigDecimal tipoDeCambio;

    private String usuario;
    
    //INICIO ATRIBUTOS PERSONALIZADOS--------------------------------------------
    
    @XmlTransient
    private String desMedioPago;
    
    //FIN ATRIBUTOS PERSONALIZADOS-----------------------------------------------


    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getCargo() {
    	if (cargo == null){
    		return BigDecimal.ZERO;
    	}
        return cargo;
    }

    public void setCargo(BigDecimal cargo) {
        this.cargo = cargo;
    }

    public BigDecimal getAbono() {
    	if (abono == null){
    		return BigDecimal.ZERO;
    	}
        return abono;
    }

    public void setAbono(BigDecimal abono) {
        this.abono = abono;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto == null ? null : concepto.trim();
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento == null ? null : documento.trim();
    }

    public String getCodMedioPago() {
        return codMedioPago;
    }

    public void setCodMedioPago(String codMedioPago) {
        this.codMedioPago = codMedioPago == null ? null : codMedioPago.trim();
    }

    public String getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(String idDocumento) {
        this.idDocumento = idDocumento == null ? null : idDocumento.trim();
    }

    public String getCodConceptoMovimiento() {
        return codConceptoMovimiento;
    }

    public void setCodConceptoMovimiento(String codConceptoMovimiento) {
        this.codConceptoMovimiento = codConceptoMovimiento == null ? null : codConceptoMovimiento.trim();
    }

    public Long getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(Long idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public String getUidTransaccionDet() {
        return uidTransaccionDet;
    }

    public void setUidTransaccionDet(String uidTransaccionDet) {
        this.uidTransaccionDet = uidTransaccionDet == null ? null : uidTransaccionDet.trim();
    }

    public String getCoddivisa() {
        return coddivisa;
    }

    public void setCoddivisa(String coddivisa) {
        this.coddivisa = coddivisa == null ? null : coddivisa.trim();
    }

    public BigDecimal getTipoDeCambio() {
        return tipoDeCambio;
    }

    public void setTipoDeCambio(BigDecimal tipoDeCambio) {
        this.tipoDeCambio = tipoDeCambio;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario == null ? null : usuario.trim();
    }
    
    //INICIO MÉTODOS PERSONALIZADOS--------------------------------------------
    
    public String getAbonoTicket() {
        if (abono == null || abono.compareTo(BigDecimal.ZERO)== 0){
            return null;
        }
        return FormatUtil.getInstance().formateaImporte(abono);
    }
    
    public String getCargoTicket() {
        if (cargo == null || cargo.compareTo(BigDecimal.ZERO)== 0){
            return null;
        }
        return FormatUtil.getInstance().formateaImporte(cargo);
    }
    
    public String getFechaTicket() {

    	String fechaTicket = FormatUtil.getInstance().formateaFechaCorta(fecha);
    	String horaTicket = FormatUtil.getInstance().formateaHora(fecha);

    	return fechaTicket +" "+ horaTicket;
    }

    public String getDesMedioPago() {       
        return desMedioPago;
    }

    public void setDesMedioPago(String desMedioPago) {
        this.desMedioPago = desMedioPago;
    }
    
    //FIN MÉTODOS PERSONALIZADOS-----------------------------------------------

}