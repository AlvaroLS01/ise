package com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos;

import java.math.BigDecimal;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.services.ticket.ByLLineaTicket;
import com.comerzzia.pos.gui.ventas.tickets.articulos.LineaTicketGui;
import com.comerzzia.pos.services.ticket.lineas.LineaTicketAbstract;

import javafx.beans.property.SimpleObjectProperty;

@Primary
@Component
public class ByLLineaTicketGui extends LineaTicketGui {

	protected SimpleObjectProperty<BigDecimal> pvpInicial;
	protected SimpleObjectProperty<BigDecimal> descuentoTarifa;

	protected Boolean tieneTrazabilidad;

	public ByLLineaTicketGui() {
		super();
	}

	public ByLLineaTicketGui(LineaTicketAbstract linea) {
		super(linea);

		pvpInicial = new SimpleObjectProperty<BigDecimal>(((ByLLineaTicket) linea).getPrecioVentaRefTotal() != null ? ((ByLLineaTicket) linea).getPrecioVentaRefTotal() : new BigDecimal(0));
		descuentoTarifa = new SimpleObjectProperty<BigDecimal>(((ByLLineaTicket) linea).getDescuentoSobreInicial());
		tieneTrazabilidad = ((ByLLineaTicket) linea).getTrazabilidad() == null || ((ByLLineaTicket) linea).getTrazabilidad().getTieneTrazabilidad() == null ? false
		        : ((ByLLineaTicket) linea).getTrazabilidad().getTieneTrazabilidad();
	}

	public SimpleObjectProperty<BigDecimal> getPvpInicial() {
		return pvpInicial;
	}

	public void setPvpInicial(SimpleObjectProperty<BigDecimal> pvpInicial) {
		this.pvpInicial = pvpInicial;
	}

	public SimpleObjectProperty<BigDecimal> getDescuentoTarifa() {
		return descuentoTarifa;
	}

	public void setDescuentoTarifa(SimpleObjectProperty<BigDecimal> descuentoTarifa) {
		this.descuentoTarifa = descuentoTarifa;
	}

	public Boolean getTieneTrazabilidad() {
		return tieneTrazabilidad;
	}

	public void setTieneTrazabilidad(Boolean tieneTrazabilidad) {
		this.tieneTrazabilidad = tieneTrazabilidad;
	}

}
