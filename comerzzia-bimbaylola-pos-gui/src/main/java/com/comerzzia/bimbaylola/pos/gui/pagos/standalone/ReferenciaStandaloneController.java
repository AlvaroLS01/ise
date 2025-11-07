package com.comerzzia.bimbaylola.pos.gui.pagos.standalone;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.controllers.WindowController;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

@Component
public class ReferenciaStandaloneController extends WindowController {

	protected static final Logger log = Logger.getLogger(ReferenciaStandaloneController.class);

	public static final String PARAMETRO_NUM_REFERENCIA = "NUM_REFERENCIA";
	public static final String ACCION_CANCELAR = "ACCION_CANCELAR";

	@FXML
	protected TextField tfReferencia;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}

	@Override
	public void initializeComponents() {
	}

	@Override
	public void initializeForm() throws InitializeGuiException {
		tfReferencia.setText("");
	}

	@Override
	public void initializeFocus() {
		tfReferencia.requestFocus();
	}

	@FXML
	public void accionAceptarIntro(KeyEvent e) {
		if (e.getCode() == KeyCode.ENTER) {
			accionAceptar();
		}
	}

	@FXML
	public void accionAceptar() {
		if (StringUtils.isNotBlank(tfReferencia.getText().trim())) {
			getDatos().put(PARAMETRO_NUM_REFERENCIA, tfReferencia.getText());
			getStage().close();
		}
	}

	@FXML
	public void accionCancelar() {
		getDatos().put(ACCION_CANCELAR, Boolean.TRUE);
		getStage().close();
	}

}
