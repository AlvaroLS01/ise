package com.comerzzia.bimbaylola.pos.gui.ventas.devoluciones.articulos;

import java.math.BigDecimal;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.services.ticket.ByLLineaTicket;
import com.comerzzia.bimbaylola.pos.services.ticket.profesional.ByLLineaTicketProfesional;
import com.comerzzia.pos.gui.ventas.devoluciones.articulos.LineaTicketAbonoGui;
import com.comerzzia.pos.services.ticket.lineas.LineaTicketAbstract;

import javafx.beans.value.ObservableValue;

@Component
@Primary
public class ByLLineaTicketAbonoGui extends LineaTicketAbonoGui {

	protected Boolean aptoTrazabilidad;
	protected Boolean tieneCadenaTrazabilidad;

	public ByLLineaTicketAbonoGui(LineaTicketAbstract linea) {
		super(linea);

		if(linea instanceof ByLLineaTicketProfesional) {
			aptoTrazabilidad = ((ByLLineaTicketProfesional) linea).getTrazabilidad() == null || ((ByLLineaTicketProfesional) linea).getTrazabilidad().getTieneTrazabilidad() == null ? false
					: ((ByLLineaTicketProfesional) linea).getTrazabilidad().getTieneTrazabilidad();
			
			tieneCadenaTrazabilidad = ((ByLLineaTicketProfesional) linea).getTrazabilidad() == null || ((ByLLineaTicketProfesional) linea).getTrazabilidad().getCadenasTrazabilidad() == null ? false
					: !((ByLLineaTicketProfesional) linea).getTrazabilidad().getCadenasTrazabilidad().isEmpty();			
		}
		else {
			aptoTrazabilidad = ((ByLLineaTicket) linea).getTrazabilidad() == null || ((ByLLineaTicket) linea).getTrazabilidad().getTieneTrazabilidad() == null ? false
					: ((ByLLineaTicket) linea).getTrazabilidad().getTieneTrazabilidad();
			
			tieneCadenaTrazabilidad = ((ByLLineaTicket) linea).getTrazabilidad() == null || ((ByLLineaTicket) linea).getTrazabilidad().getCadenasTrazabilidad() == null ? false
					: !((ByLLineaTicket) linea).getTrazabilidad().getCadenasTrazabilidad().isEmpty();			
		}
	}

	public Boolean getAptoTrazabilidad() {
		return aptoTrazabilidad;
	}

	public void setAptoTrazabilidad(Boolean aptoTrazabilidad) {
		this.aptoTrazabilidad = aptoTrazabilidad;
	}

	public Boolean getTieneCadenaTrazabilidad() {
		return tieneCadenaTrazabilidad;
	}

	public void setTieneCadenaTrazabilidad(Boolean tieneCadenaTrazabilidad) {
		this.tieneCadenaTrazabilidad = tieneCadenaTrazabilidad;
	}

}
