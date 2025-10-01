package com.comerzzia.bricodepot.backoffice.persistence.ventas.cajas.cierre;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "caja")
@XmlAccessorType(XmlAccessType.NONE)
public class Caja {
	
	@XmlElement(name = "cabecera")
	protected CajaBean cajaBean;
	@XmlElement(name = "totales")
	protected Totales totales;
	@XmlElement(name = "recuentos")
	public String recuentos = "";
	@XmlElementWrapper(name = "movimientos")
	@XmlElement(name = "movimiento")
	public List<CajaMovimientoBean> movimientos;

	
	public Caja() {
		this(new CajaBean());
	}


	public Caja(CajaBean caja) {
		cajaBean = caja;
		totales = new Totales();
		movimientos  = new ArrayList<CajaMovimientoBean>();
	}


	
	public Totales getTotales() {
		return totales;
	}


	
	public void setTotales(Totales totales) {
		this.totales = totales;
	}


	
	public String getRecuentos() {
		return recuentos;
	}


	
	public void setRecuentos(String recuentos) {
		this.recuentos = recuentos;
	}


	
	public List<CajaMovimientoBean> getMovimientos() {
		return movimientos;
	}


	
	
	public void setCajaBean(CajaBean cajaBean) {
		this.cajaBean = cajaBean;
	}


	public void setMovimientos(List<CajaMovimientoBean> movimientos) {
		this.movimientos = movimientos;
	}


	
	public CajaBean getCajaBean() {
		return cajaBean;
	}
	

}
