package com.comerzzia.bricodepot.backoffice.util.movimientos.cajasficticias;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.comerzzia.core.omnichannel.engine.persistence.cajas.movimientos.CajaMovimientoBean;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CajaFicticiaDTO {
	@XmlElement
	private CajaMovimientoBean movimiento;
	@XmlElement(name = "uid_diario_caja")
	protected String uidDiarioCaja ; 
	@XmlElement(name = "codigo_caja")
	protected String codCaja;
	@XmlElement(name = "codigo_almacen")
	protected String codAlm;

	public void init() {
		movimiento = new CajaMovimientoBean();
	}

	public CajaMovimientoBean getMovimiento() {
		return movimiento;
	}

	public void setMovimiento(CajaMovimientoBean movimiento) {
		this.movimiento = movimiento;
	}

	
	public String getUidDiarioCaja() {
		return uidDiarioCaja;
	}

	
	public void setUidDiarioCaja(String uidDiarioCaja) {
		this.uidDiarioCaja = uidDiarioCaja;
	}

	
	public String getCodCaja() {
		return codCaja;
	}

	
	public void setCodCaja(String codCaja) {
		this.codCaja = codCaja;
	}

	
	public String getCodAlm() {
		return codAlm;
	}

	
	public void setCodAlm(String codAlm) {
		this.codAlm = codAlm;
	}
	
	
}
