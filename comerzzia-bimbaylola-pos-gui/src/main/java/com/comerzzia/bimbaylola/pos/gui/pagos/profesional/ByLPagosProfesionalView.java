package com.comerzzia.bimbaylola.pos.gui.pagos.profesional;

import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.gui.pagos.ByLPagosController;
import com.comerzzia.pos.core.gui.view.ModalView;
import com.comerzzia.pos.gui.ventas.tickets.pagos.PagosView;

@Component
public class ByLPagosProfesionalView extends ModalView {
	
	@Override
	protected String getFXMLName() {
		return getFXMLName(PagosView.class);
	}
	
	@Override
	public Object loadCustomController() {
		return new ByLPagosController();
	}

}
