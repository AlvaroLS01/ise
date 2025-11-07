package com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.agregarNotaInformativa.tablaNotas;

import org.springframework.stereotype.Component;

import com.comerzzia.pos.core.gui.view.ModalView;

@Component
public class ByLTablaNotasView extends ModalView {

	@Override
	protected String getFXMLName() {
		return getFXMLName(ByLTablaNotasView.class);
	}

}
