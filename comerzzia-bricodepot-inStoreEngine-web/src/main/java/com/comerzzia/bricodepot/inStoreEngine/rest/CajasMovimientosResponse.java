package com.comerzzia.bricodepot.inStoreEngine.rest;

import java.util.List;

import com.comerzzia.bricodepot.inStoreEngine.persistence.cajas.movimientos.CajasMovimientosBean;

public class CajasMovimientosResponse {

	List<CajasMovimientosBean> listaCajasMovimientos;

	public List<CajasMovimientosBean> getListaCajasMovimientos() {
		return listaCajasMovimientos;
	}

	public void setListaCajasMovimientos(List<CajasMovimientosBean> listaCajasMovimientos) {
		this.listaCajasMovimientos = listaCajasMovimientos;
	}

}
