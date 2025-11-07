package com.comerzzia.bimbaylola.pos.gui.ventas.cajas.recuentos;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.pos.gui.ventas.cajas.recuentos.RecuentoCajaController;

@Primary
@Component
public class ByLRecuentoCajaController extends RecuentoCajaController {

    private static final Logger log = Logger.getLogger(ByLRecuentoCajaController.class.getName());

	@Override
	public void refrescarDatosPantalla() {
		super.refrescarDatosPantalla();
		autosizeLabelTotalFont();
	}

	protected void autosizeLabelTotalFont() {
		try {
			lbTotal.setStyle("");
			String text = lbTotal.getText();
			if(text.length()>=15) {
				lbTotal.setStyle("-fx-font-size: 30px;");
			}
			else if(text.length()>=12) {
				lbTotal.setStyle("-fx-font-size: 35px;");
			}
			else if(text.length()>=10) {
				lbTotal.setStyle("-fx-font-size: 45px;");
			}
			else if(text.length()>=8) {
				lbTotal.getStyleClass().add("total-reduced");
			}
		} catch (Exception e) {
			log.debug("autosizeLabelTotalFont() - " + e.getClass().getName() + " - " + e.getLocalizedMessage(), e);
		}

	}
}
