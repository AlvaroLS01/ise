package com.comerzzia.bimbaylola.pos.dispositivo.impresora.spark130f.gui;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.dispositivo.impresora.spark130f.Spark130F;
import com.comerzzia.bimbaylola.pos.services.spark130f.Spark130FService;
import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.controllers.WindowController;
import com.comerzzia.pos.util.i18n.I18N;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

@Component
public class ConfiguracionSpark130FController extends WindowController implements Initializable {

	@Autowired
	protected Spark130FService spark130FService;

	@FXML
	private TextField tfIP;
	@FXML
	private Button btPrintXReport;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}

	@Override
	public void initializeComponents() throws InitializeGuiException {
	}

	@Override
	public void initializeFocus() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initializeForm() throws InitializeGuiException {
		if (getDatos().containsKey(Spark130F.PARAMETRO_SALIDA_CONFIGURACION)) {
			/* Realizamos la carga de los datos actuales */
			Map<String, String> parametros = ((Map<String, String>) getDatos().get(Spark130F.PARAMETRO_SALIDA_CONFIGURACION));
			for (String parametro : parametros.keySet()) {
				if (parametro.equals(Spark130F.IP)) {
					tfIP.setText(parametros.get(parametro));
				}
			}
		}
		else {
			/* Realizamos un limpiado de los campos al entrar en el formulario */
			tfIP.clear();
		}
	}

	public void accionAceptar() {
		if (!StringUtils.isBlank(tfIP.getText())) {
			Map<String, String> parametrosConfiguracion = new HashMap<String, String>();
			parametrosConfiguracion.put(Spark130F.IP, tfIP.getText());

			getDatos().put(Spark130F.PARAMETRO_SALIDA_CONFIGURACION, parametrosConfiguracion);
			getStage().close();
		}
		else {
			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Debe rellenar todos los campos."), this.getStage());
		}
	}

}
