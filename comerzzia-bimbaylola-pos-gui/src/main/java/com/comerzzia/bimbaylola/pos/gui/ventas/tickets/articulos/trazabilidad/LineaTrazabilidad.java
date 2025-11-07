package com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.trazabilidad;

import java.math.BigDecimal;
import java.util.List;

public interface LineaTrazabilidad {

	BigDecimal getCantidad();

	String getDesArticulo();

	List<String> getCadenasTrazabilidad();
}
