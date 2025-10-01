package com.comerzzia.bricodepot.backoffice.util.movimientos.retirada;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.comerzzia.bricodepot.backoffice.persistence.movimientos.retirada.DetMovRetiradaKey;
import com.comerzzia.core.omnichannel.engine.persistence.cajas.movimientos.CajaMovimientoBean;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RetiradaABancoDTO {

	private String uidActividad;

	@XmlElement(name = "movimiento91")
	private CajaMovimientoBean movimiento91;
	
	@XmlElementWrapper(name = "movimientos_90")
	@XmlElement(name = "movimiento_90")
	private List<DetMovRetiradaKey> movimientos90;

	public String getUidActividad() {
		return uidActividad;
	}

	public void setUidActividad(String uidActividad) {
		this.uidActividad = uidActividad;
	}

	public CajaMovimientoBean getMovimiento91() {
		return movimiento91;
	}

	public void setMovimiento91(CajaMovimientoBean movimiento91) {
		this.movimiento91 = movimiento91;
	}

	public List<DetMovRetiradaKey> getMovimientos90() {
		return movimientos90;
	}

	public void setMovimientos90(List<DetMovRetiradaKey> movimientos90) {
		this.movimientos90 = movimientos90;
	}

	
}
