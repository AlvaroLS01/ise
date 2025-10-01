package com.comerzzia.bricodepot.backoffice.persistence.tickets.auditoria;

import java.math.BigDecimal;
import java.util.Date;

import com.comerzzia.core.model.ventas.tickets.TicketBean;
import com.comerzzia.model.general.almacenes.AlmacenBean;

public class EventoAuditoriaBean extends EventoAuditoriaBeanKey {
    private Long idTicketAuditoria;

    private String codigoAlmacen;

    private Date fecha;

    private String tipoEvento;

    private String descripcionEvento;

    private Long idUsuario;

    private String nombreUsuario;

    private Boolean procede;

    private Long idUsuarioSupervisor;

    private String nombreUsuarioSupervisor;

    private String uidTicketVenta;

    private Long idTicketVenta;

    private String codigoArticulo;

    private String descripcionArticulo;

    private BigDecimal cantidadArticulo;

    private BigDecimal precioOriginal;

    private BigDecimal precioAplicado;

    private Integer lineaReferencia;

    private AlmacenBean almacen;
    
    private TicketBean ticketVenta;
    
    public Long getIdTicketAuditoria() {
        return idTicketAuditoria;
    }

    public void setIdTicketAuditoria(Long idTicketAuditoria) {
        this.idTicketAuditoria = idTicketAuditoria;
    }

    public String getCodigoAlmacen() {
        return codigoAlmacen;
    }

    public void setCodigoAlmacen(String codigoAlmacen) {
        this.codigoAlmacen = codigoAlmacen == null ? null : codigoAlmacen.trim();
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento == null ? null : tipoEvento.trim();
    }

    public String getDescripcionEvento() {
        return descripcionEvento;
    }

    public void setDescripcionEvento(String descripcionEvento) {
        this.descripcionEvento = descripcionEvento == null ? null : descripcionEvento.trim();
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario == null ? null : nombreUsuario.trim();
    }

    public Boolean getProcede() {
        return procede;
    }

    public void setProcede(Boolean procede) {
        this.procede = procede;
    }

    public Long getIdUsuarioSupervisor() {
        return idUsuarioSupervisor;
    }

    public void setIdUsuarioSupervisor(Long idUsuarioSupervisor) {
        this.idUsuarioSupervisor = idUsuarioSupervisor;
    }

    public String getNombreUsuarioSupervisor() {
        return nombreUsuarioSupervisor;
    }

    public void setNombreUsuarioSupervisor(String nombreUsuarioSupervisor) {
        this.nombreUsuarioSupervisor = nombreUsuarioSupervisor == null ? null : nombreUsuarioSupervisor.trim();
    }

    public String getUidTicketVenta() {
        return uidTicketVenta;
    }

    public void setUidTicketVenta(String uidTicketVenta) {
        this.uidTicketVenta = uidTicketVenta == null ? null : uidTicketVenta.trim();
    }

    public Long getIdTicketVenta() {
        return idTicketVenta;
    }

    public void setIdTicketVenta(Long idTicketVenta) {
        this.idTicketVenta = idTicketVenta;
    }

    public String getCodigoArticulo() {
        return codigoArticulo;
    }

    public void setCodigoArticulo(String codigoArticulo) {
        this.codigoArticulo = codigoArticulo == null ? null : codigoArticulo.trim();
    }

    public String getDescripcionArticulo() {
        return descripcionArticulo;
    }

    public void setDescripcionArticulo(String descripcionArticulo) {
        this.descripcionArticulo = descripcionArticulo == null ? null : descripcionArticulo.trim();
    }

    public BigDecimal getCantidadArticulo() {
        return cantidadArticulo;
    }

    public void setCantidadArticulo(BigDecimal cantidadArticulo) {
        this.cantidadArticulo = cantidadArticulo;
    }

    public BigDecimal getPrecioOriginal() {
        return precioOriginal;
    }

    public void setPrecioOriginal(BigDecimal precioOriginal) {
        this.precioOriginal = precioOriginal;
    }

    public BigDecimal getPrecioAplicado() {
        return precioAplicado;
    }

    public void setPrecioAplicado(BigDecimal precioAplicado) {
        this.precioAplicado = precioAplicado;
    }

    public Integer getLineaReferencia() {
        return lineaReferencia;
    }

	public void setLineaReferencia(Integer lineaReferencia) {
		this.lineaReferencia = lineaReferencia;
	}

	public AlmacenBean getAlmacen() {
		return almacen;
	}

	public void setAlmacen(AlmacenBean almacen) {
		this.almacen = almacen;
	}

	public TicketBean getTicketVenta() {
		return ticketVenta;
	}

	public void setTicketVenta(TicketBean ticketVenta) {
		this.ticketVenta = ticketVenta;
	}

}