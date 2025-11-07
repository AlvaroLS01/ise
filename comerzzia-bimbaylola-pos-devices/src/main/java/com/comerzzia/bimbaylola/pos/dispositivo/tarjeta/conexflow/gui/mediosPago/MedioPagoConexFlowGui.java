package com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.conexflow.gui.mediosPago;

import com.comerzzia.pos.persistence.mediosPagos.MedioPagoBean;

import javafx.beans.property.SimpleStringProperty;

public class MedioPagoConexFlowGui {

	private SimpleStringProperty codMedioPago;
	private SimpleStringProperty desMedioPago;

	private MedioPagoBean medioPago;

	public MedioPagoConexFlowGui(MedioPagoBean medioPago) {
		this.medioPago = medioPago;
		this.codMedioPago = new SimpleStringProperty(medioPago.getCodMedioPago());
		this.desMedioPago = new SimpleStringProperty(medioPago.getDesMedioPago());
	}

	public SimpleStringProperty getCodMedioPago() {
		return codMedioPago;
	}

	public void setCodMedioPago(SimpleStringProperty codMedioPago) {
		this.codMedioPago = codMedioPago;
	}

	public SimpleStringProperty getDesMedioPago() {
		return desMedioPago;
	}

	public void setDesMedioPago(SimpleStringProperty desMedioPago) {
		this.desMedioPago = desMedioPago;
	}

	public MedioPagoBean getMedioPago() {
		return medioPago;
	}

	public void setMedioPago(MedioPagoBean medioPago) {
		this.medioPago = medioPago;
	}

}
