package com.comerzzia.bimbaylola.pos.gui.ventas.devoluciones.fechaorigen;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.componentes.datepicker.DatePicker;
import com.comerzzia.pos.core.gui.controllers.WindowController;
import com.comerzzia.pos.util.i18n.I18N;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

@Controller
public class RequestFechaOrigenController extends WindowController {

	public static final Logger log = Logger.getLogger(RequestFechaOrigenController.class);

	public static final String FECHA_RELLENADA = "FECHA_RELLENADA";
	public static final String FECHA_ACCION = "FECHA_ACCION";

	@FXML
	protected Button btAceptar;

	@FXML
	protected Button btCancelar;

	@FXML
	protected DatePicker dpFechaOrigen;

	@FXML
	protected Label lbError;

	@FXML
	protected Label lbInformacion;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		log.debug("initialize()");
	}

	@Override
	public void initializeComponents() throws InitializeGuiException {
		log.debug("initializeComponents()");
		lbError.setText("");
		lbInformacion.setText("");
		btCancelar.setDisable(false);

	}

	@Override
	public void initializeFocus() {
		log.debug("initializeFocus()");
		dpFechaOrigen.requestFocus();
	}

	@Override
	public void initializeForm() throws InitializeGuiException {
		log.debug("initializeForm()");
		dpFechaOrigen.setSelectedDate(new Date());
		lbError.setText("");
		initializeFocus();

	}

	@FXML
	void accionAceptar() {
		log.debug("accionAceptar");
		if (dpFechaOrigen.getSelectedDate() == null) {
			lbError.setText(I18N.getTexto("Debe escoger una fecha con el formato dd/mm/yyyy"));
			return;
		}
		else if (dpFechaOrigen.getSelectedDate().after(new Date())) {
			lbError.setText(I18N.getTexto("La fecha del documento origen no puede ser posterior al día de hoy"));
			return;
		}
		getDatos().put(FECHA_RELLENADA, dpFechaOrigen.getSelectedDate());
		getDatos().put(FECHA_ACCION, Boolean.TRUE);
		getStage().close();
	}

	@FXML
	void accionAceptarIntro(KeyEvent event) {
		log.debug("accionAceptarIntro()");
		if (event.getCode() == KeyCode.ENTER) {
			accionAceptar();
			event.consume();
		}
	}

	@FXML
	void accionCancelar(ActionEvent event) {
		log.debug("accionCancelar");
		getDatos().put(FECHA_ACCION, Boolean.FALSE);
		super.accionCancelar();
	}

	@FXML
	void accionCancelarEsc(KeyEvent event) {
		log.debug("accionCancelarEsc()");
		if (event.getCode() == KeyCode.ESCAPE) {
			accionCancelar();
			event.consume();
		}
	}

}
