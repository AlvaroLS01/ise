package com.comerzzia.bimbaylola.pos.gui.pagos.msi;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.gui.componentes.dialogos.ByLVentanaDialogoComponent;
import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.controllers.WindowController;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;

@Component
public class MsiController extends WindowController {

	@FXML
	protected CheckBox cbTres;
	@FXML
	protected CheckBox cbSeis;
	@FXML
	protected CheckBox cbNueve;
	@FXML
	protected CheckBox cbDoce;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initializeComponents() throws InitializeGuiException {
		cbTres.setOnAction(event -> {
			if (cbTres.isSelected()) {
				cbSeis.setSelected(false);
				cbNueve.setSelected(false);
				cbDoce.setSelected(false);
			}
		});

		cbSeis.setOnAction(event -> {
			if (cbSeis.isSelected()) {
				cbTres.setSelected(false);
				cbNueve.setSelected(false);
				cbDoce.setSelected(false);
			}
		});

		cbNueve.setOnAction(event -> {
			if (cbNueve.isSelected()) {
				cbTres.setSelected(false);
				cbSeis.setSelected(false);
				cbDoce.setSelected(false);
			}
		});

		cbDoce.setOnAction(event -> {
			if (cbDoce.isSelected()) {
				cbTres.setSelected(false);
				cbSeis.setSelected(false);
				cbNueve.setSelected(false);
			}
		});
	}

	@Override
	public void initializeForm() throws InitializeGuiException {
		// TODO Auto-generated method stub
		cbTres.setSelected(false);
		cbSeis.setSelected(false);
		cbNueve.setSelected(false);
		cbDoce.setSelected(false);

	}

	@Override
	public void initializeFocus() {
		cbTres.requestFocus();

	}

	public void AccionAceptar() {
		if (cbTres.isSelected()) {
			getDatos().put("meses", "3");
		}

		if (cbSeis.isSelected()) {
			getDatos().put("meses", "6");
		}

		if (cbNueve.isSelected()) {
			getDatos().put("meses", "9");
		}

		if (cbDoce.isSelected()) {
			getDatos().put("meses", "12");
		}
		
		if (!cbTres.isSelected() && !cbSeis.isSelected() && !cbNueve.isSelected() && !cbDoce.isSelected()) {
			ByLVentanaDialogoComponent.crearVentanaAviso("No se ha seleccionado ninguna opción. Por favor, seleccione una opción para continuar.", getStage());
		}
		
		if (StringUtils.isNotBlank((String) getDatos().get("meses"))) {
			getStage().close();
		}
		
	}

	public void AccionCancelar() {
		getDatos().put("cancelar", "cancelar");
		getStage().close();
	}

}
