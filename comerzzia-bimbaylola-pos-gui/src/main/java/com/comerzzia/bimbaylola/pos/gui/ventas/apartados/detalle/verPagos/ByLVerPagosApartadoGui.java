package com.comerzzia.bimbaylola.pos.gui.ventas.apartados.detalle.verPagos;

import java.math.BigDecimal;

import javafx.beans.property.SimpleObjectProperty;

import com.comerzzia.pos.gui.ventas.apartados.detalle.verPagos.VerPagosApartadoGui;
import com.comerzzia.pos.persistence.cajas.movimientos.CajaMovimientoBean;

public class ByLVerPagosApartadoGui extends VerPagosApartadoGui{

	private SimpleObjectProperty<BigDecimal> importe;

	public ByLVerPagosApartadoGui(CajaMovimientoBean movimiento){
		super(movimiento);

		this.importe = new SimpleObjectProperty<BigDecimal>();
		BigDecimal total = movimiento.getCargo().subtract(movimiento.getAbono());
		importe.setValue(total);
	}

	public BigDecimal getImporte(){
		return importe.getValue();
	}

}
