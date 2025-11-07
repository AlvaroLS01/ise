package com.comerzzia.bimbaylola.pos.persistence.ventas.cajas.apertura;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "caja")
@XmlAccessorType(XmlAccessType.FIELD)
public class AperturaCajaBean {

	@XmlElement(required = true)
	private AperturaCajaCabecera cabecera;

	public AperturaCajaBean() {
	}

	public AperturaCajaBean(AperturaCajaCabecera cabecera) {
		super();
		this.cabecera = cabecera;
	}

	public AperturaCajaCabecera getCabecera() {
		return cabecera;
	}

	public void setCabecera(AperturaCajaCabecera cabecera) {
		this.cabecera = cabecera;
	}

	@Override
	public String toString() {
		return "AperturaCajaBean [cabecera=" + cabecera + "]";
	}

}
