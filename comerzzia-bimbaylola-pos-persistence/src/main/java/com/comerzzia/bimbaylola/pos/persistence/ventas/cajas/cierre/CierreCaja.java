package com.comerzzia.bimbaylola.pos.persistence.ventas.cajas.cierre;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cierre_caja")
@XmlAccessorType(XmlAccessType.FIELD)
public class CierreCaja {

	@XmlElement(name = "uidActividad")
	private String uidActividad;

	@XmlElement(name = "codTicket")
	private String codTicket;

	@XmlElement(name = "codEmp")
	private String codEmp;

	@XmlElement(name = "uid_ticket")
	private String uidTicket;

	@XmlElement(name = "uidDiarioCaja")
	private String uidDiarioCaja;

	@XmlElement(name = "codAlmacen")
	private String codAlmacen;

	@XmlElement(name = "codCaja")
	private String codCaja;

	@XmlElement(name = "fechaApertura")
	private Date fechaApertura;
	
	@XmlElement(name = "fecha")
	private Date fecha;

	@XmlElement(name = "turno")
	private Integer turno;

	@XmlElement(name = "usuario")
	private String usuario;

	@XmlElement(name = "moneda")
	private String moneda;

	@XmlElement(name = "transaction_total")
	private Integer transactionTotal;

	@XmlElement(name = "customer_sales_count")
	private Integer customerSalesCount;

	@XmlElement(name = "sales_count")
	private Integer salesCount;

	@XmlElement(name = "giftcards_totals")
	private BigDecimal giftcardsTotals;

	@XmlElement(name = "giftcards_out_total")
	private BigDecimal giftcardsOutTotal;

	@XmlElement(name = "returns_totals")
	private BigDecimal returnsTotales;

	@XmlElement(name = "sales_totals")
	private BigDecimal salesTotals;

	@XmlElement(name = "tax_total")
	private BigDecimal taxTotal;

	@XmlElement(name = "empresa_datos_adicionales")
	private EmpresaDatosAdicionales empresaDatosAdicionales;

	public CierreCaja() {
	}

	public CierreCaja(String uidActividad, String codTicket, String codEmp, String uidTicket, String uidDiarioCaja, String codAlmacen, String codCaja, Date fechaApertura, Date fecha, Integer turno, String usuario,
	        String moneda, Integer transactionTotal, Integer customerSalesCount, Integer salesCount, BigDecimal giftcardsTotals, BigDecimal giftcardsOutTotal, BigDecimal returnsTotales, BigDecimal salesTotals,
	        BigDecimal taxTotal, EmpresaDatosAdicionales empresaDatosAdicionales) {
		super();
		this.uidActividad = uidActividad;
		this.codTicket = codTicket;
		this.codEmp = codEmp;
		this.uidTicket = uidTicket;
		this.uidDiarioCaja = uidDiarioCaja;
		this.codAlmacen = codAlmacen;
		this.codCaja = codCaja;
		this.fechaApertura = fechaApertura;
		this.fecha = fecha;
		this.turno = turno;
		this.usuario = usuario;
		this.moneda = moneda;
		this.transactionTotal = transactionTotal;
		this.customerSalesCount = customerSalesCount;
		this.salesCount = salesCount;
		this.giftcardsTotals = giftcardsTotals;
		this.giftcardsOutTotal = giftcardsOutTotal;
		this.returnsTotales = returnsTotales;
		this.salesTotals = salesTotals;
		this.taxTotal = taxTotal;
		this.empresaDatosAdicionales = empresaDatosAdicionales;
	}

	public String getUidActividad() {
		return uidActividad;
	}

	public void setUidActividad(String uidActividad) {
		this.uidActividad = uidActividad;
	}

	public String getCodTicket() {
		return codTicket;
	}

	public void setCodTicket(String codTicket) {
		this.codTicket = codTicket;
	}

	public String getCodEmp() {
		return codEmp;
	}

	public void setCodEmp(String codEmp) {
		this.codEmp = codEmp;
	}

	public String getUidTicket() {
		return uidTicket;
	}

	public void setUidTicket(String uidTicket) {
		this.uidTicket = uidTicket;
	}

	public String getUidDiarioCaja() {
		return uidDiarioCaja;
	}

	public void setUidDiarioCaja(String uidDiarioCaja) {
		this.uidDiarioCaja = uidDiarioCaja;
	}

	public String getCodAlmacen() {
		return codAlmacen;
	}

	public void setCodAlmacen(String codAlmacen) {
		this.codAlmacen = codAlmacen;
	}

	public String getCodCaja() {
		return codCaja;
	}

	public void setCodCaja(String codCaja) {
		this.codCaja = codCaja;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Integer getTurno() {
		return turno;
	}

	public void setTurno(Integer turno) {
		this.turno = turno;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public Integer getTransactionTotal() {
		return transactionTotal;
	}

	public void setTransactionTotal(Integer transactionTotal) {
		this.transactionTotal = transactionTotal;
	}

	public Integer getCustomerSalesCount() {
		return customerSalesCount;
	}

	public void setCustomerSalesCount(Integer customerSalesCount) {
		this.customerSalesCount = customerSalesCount;
	}

	public Integer getSalesCount() {
		return salesCount;
	}

	public void setSalesCount(Integer salesCount) {
		this.salesCount = salesCount;
	}

	public BigDecimal getGiftcardsTotals() {
		return giftcardsTotals;
	}

	public void setGiftcardsTotals(BigDecimal giftcardsTotals) {
		this.giftcardsTotals = giftcardsTotals;
	}

	public BigDecimal getGiftcardsOutTotal() {
		return giftcardsOutTotal;
	}

	public void setGiftcardsOutTotal(BigDecimal giftcardsOutTotal) {
		this.giftcardsOutTotal = giftcardsOutTotal;
	}

	public BigDecimal getReturnsTotales() {
		return returnsTotales;
	}

	public void setReturnsTotales(BigDecimal returnsTotales) {
		this.returnsTotales = returnsTotales;
	}

	public BigDecimal getSalesTotals() {
		return salesTotals;
	}

	public void setSalesTotals(BigDecimal salesTotals) {
		this.salesTotals = salesTotals;
	}

	public BigDecimal getTaxTotal() {
		return taxTotal;
	}

	public void setTaxTotal(BigDecimal taxTotal) {
		this.taxTotal = taxTotal;
	}

	public EmpresaDatosAdicionales getEmpresaDatosAdicionales() {
		return empresaDatosAdicionales;
	}

	public void setEmpresaDatosAdicionales(EmpresaDatosAdicionales empresaDatosAdicionales) {
		this.empresaDatosAdicionales = empresaDatosAdicionales;
	}

	public Date getFechaApertura() {
		return fechaApertura;
	}

	public void setFechaApertura(Date fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	@Override
	public String toString() {
		return "CierreCaja [uidActividad=" + uidActividad + ", codTicket=" + codTicket + ", codEmp=" + codEmp + ", uidTicket=" + uidTicket + ", uidDiarioCaja=" + uidDiarioCaja + ", codAlmacen="
		        + codAlmacen + ", codCaja=" + codCaja + ", fechaApertura=" + fechaApertura + ", fecha=" + fecha + ", turno=" + turno + ", usuario=" + usuario + ", moneda=" + moneda + ", transactionTotal=" + transactionTotal
		        + ", customerSalesCount=" + customerSalesCount + ", salesCount=" + salesCount + ", giftcardsTotals=" + giftcardsTotals + ", giftcardsOutTotal=" + giftcardsOutTotal
		        + ", returnsTotales=" + returnsTotales + ", salesTotals=" + salesTotals + ", taxTotal=" + taxTotal + ", empresaDatosAdicionales=" + empresaDatosAdicionales + "]";
	}

}
