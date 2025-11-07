package com.comerzzia.bimbaylola.pos.gui.configuracion.ranges.modal;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.services.core.config.configContadores.rangos.CounterRangeDto;
import com.comerzzia.bimbaylola.pos.services.core.config.configContadores.rangos.CounterRangeManager;
import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.controllers.Controller;
import com.comerzzia.pos.core.gui.validation.ValidationUI;
import com.comerzzia.pos.util.i18n.I18N;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

@Component
public class NewRangeController extends Controller {

	protected Logger log = Logger.getLogger(getClass());

	@FXML
	protected Button btCancel, btSaveNewRange;

	@FXML
	protected Label lbCounterId, lbDivider1, lbDivider2, lbDivider3;

	@FXML
	protected Label lbTitle, lbError, lbRange;

	@FXML
	protected TextField tfCounterId, tfDivider1, tfDivider2, tfDivider3, tfRange;

	protected NewRangeForm newRangeForm;

	@Autowired
	protected CounterRangeManager counterRangeManager;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initNewRangeForm();
	}

	@Override
	public void initializeComponents() throws InitializeGuiException {
		registraEventoTeclado(new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {
					acceptAction();
				}
				else if (event.getCode().equals(KeyCode.ESCAPE)) {
					cancelAction();
				}
				event.consume();
			}

		}, KeyEvent.KEY_RELEASED);

	}

	@Override
	public void initializeForm() throws InitializeGuiException {
		clearScreen();
		newRangeForm.limpiarFormulario();
	}

	@Override
	public void initializeFocus() {
		tfCounterId.requestFocus();
	}

	protected void initNewRangeForm() {
		log.debug("initNewRangeForm()");

		newRangeForm = new NewRangeForm();
		newRangeForm.setFormField("counterId", tfCounterId);
		newRangeForm.setFormField("divider1", tfDivider1);
		newRangeForm.setFormField("divider2", tfDivider2);
		newRangeForm.setFormField("divider3", tfDivider3);
		newRangeForm.setFormField("range", tfRange);
	}

	protected void clearScreen() {
		tfCounterId.clear();
		tfDivider1.clear();
		tfDivider2.clear();
		tfDivider3.clear();
		tfRange.clear();

		newRangeForm.clearErrorStyle();
		lbError.setText("");
	}

	protected CounterRangeDto createCounterRangeDTO() {
		log.debug("createCounterRangeDTO()");

		CounterRangeDto crDTO = new CounterRangeDto();
		crDTO.setCounterId(newRangeForm.getCounterId());
		crDTO.setDivisor1(newRangeForm.getDivider1());
		crDTO.setDivisor2(newRangeForm.getDivider2());
		crDTO.setDivisor3(newRangeForm.getDivider3());
		crDTO.setRangeId(newRangeForm.getRange());

		return crDTO;
	}

	protected boolean validateForm() {
		log.debug("validateForm()");

		newRangeForm.clearErrorStyle();
		lbError.setText("");

		newRangeForm.setCounterId(tfCounterId.getText());
		newRangeForm.setDivider1(tfDivider1.getText());
		newRangeForm.setDivider2(tfDivider2.getText());
		newRangeForm.setDivider3(tfDivider3.getText());
		newRangeForm.setRange(tfRange.getText());

		Set<ConstraintViolation<NewRangeForm>> constraintViolations = ValidationUI.getInstance().getValidator().validate(newRangeForm);
		if (!constraintViolations.isEmpty()) {
			ConstraintViolation<NewRangeForm> next = constraintViolations.iterator().next();
			newRangeForm.setErrorStyle(next.getPropertyPath(), true);
			newRangeForm.setFocus(next.getPropertyPath());
			lbError.setText(next.getMessage());
			return false;
		}

		return true;
	}

	@FXML
	public void acceptAction() {
		log.debug("acceptAction()");

		if (validateForm()) {
			try {
				CounterRangeDto counterRangeDto = createCounterRangeDTO();
				counterRangeManager.saveRange(counterRangeDto, counterRangeDto.getRangeId());
				getStage().close();
			}
			catch (Exception e) {
				VentanaDialogoComponent.crearVentanaError(getStage(), I18N.getTexto("Error guardando rango. Consulte administrador."), e);
			}
		}
	}

	@FXML
	public void cancelAction() {
		log.debug("cancelAction()");

		getStage().close();
	}

}
