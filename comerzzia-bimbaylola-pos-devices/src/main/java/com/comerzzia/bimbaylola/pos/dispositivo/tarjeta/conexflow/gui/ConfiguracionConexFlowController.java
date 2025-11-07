package com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.conexflow.gui;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.conexflow.TefConexFlow;
import com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.conexflow.gui.mediosPago.AyudaMediosPagoConexFlowController;
import com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.conexflow.gui.mediosPago.AyudaMediosPagoConexFlowView;
import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.controllers.WindowController;
import com.comerzzia.pos.persistence.mediosPagos.MedioPagoBean;
import com.comerzzia.pos.services.mediospagos.MediosPagosService;
import com.comerzzia.pos.util.i18n.I18N;

@Component
public class ConfiguracionConexFlowController extends WindowController implements Initializable {

	@FXML
	private TextField tfComercio, tfTienda, tfNumTPV, tfServidorTCP, tfPuertoTCP, tfTimeout, tfCodFormaPago, tfDesFormaPago;

	@Autowired
	private MediosPagosService mediosPagosService;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}

	@Override
	public void initializeComponents() throws InitializeGuiException {
		tfCodFormaPago.focusedProperty().addListener(new ChangeListener<Boolean>(){

			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) {
				if (newValue == false) {
					MedioPagoBean medioPago = mediosPagosService.getMedioPago(tfCodFormaPago.getText());
					if (medioPago != null) {
						tfDesFormaPago.setText(medioPago.getDesMedioPago());
					}
				}
			}
		});
	}

	@Override
	public void initializeForm() throws InitializeGuiException {
		if(getDatos().containsKey(TefConexFlow.PARAMETRO_SALIDA_CONFIGURACION)) {
			// Cargamos datos actuales
			@SuppressWarnings("unchecked")
            Map<String,String> parametros = ((Map<String,String>) getDatos().get(TefConexFlow.PARAMETRO_SALIDA_CONFIGURACION));
			for(String parametro : parametros.keySet()) {
				if(parametro.equals(TefConexFlow.COMERCIO)) {
					tfComercio.setText(parametros.get(parametro));
				}
				else if(parametro.equals(TefConexFlow.TIENDA)) {
					tfTienda.setText(parametros.get(parametro));
				}
				else if(parametro.equals(TefConexFlow.NUM_TPV)) {
					tfNumTPV.setText(parametros.get(parametro));
				}
				else if(parametro.equals(TefConexFlow.SERVIDOR_TCP)) {
					tfServidorTCP.setText(parametros.get(parametro));
				}
				else if(parametro.equals(TefConexFlow.PUERTO_TCP)) {
					tfPuertoTCP.setText(parametros.get(parametro));
				}
				else if(parametro.equals(TefConexFlow.TIMEOUT)) {
					tfTimeout.setText(parametros.get(parametro));
				}
				else if(parametro.equals(TefConexFlow.FORMA_PAGO)) {
					tfCodFormaPago.setText(parametros.get(parametro));
					MedioPagoBean medioPago = mediosPagosService.getMedioPago(tfCodFormaPago.getText());
					if (medioPago != null) {
						tfDesFormaPago.setText(medioPago.getDesMedioPago());
					}
				}
			}
		}
		else {
			// Limpiamos los campos al entrar en el formulario
			tfComercio.clear();
			tfTienda.clear();
			tfNumTPV.clear();
			tfServidorTCP.clear();
			tfPuertoTCP.clear();
			tfTimeout.clear();
			tfCodFormaPago.clear();
			tfDesFormaPago.clear();
		}
	}

	@Override
	public void initializeFocus() {
		tfComercio.requestFocus();
	}

	public void abrirAyudaMediosPago() {
		getApplication().getMainView().showModalCentered(AyudaMediosPagoConexFlowView.class, getDatos(), getStage());

		if (getDatos().containsKey(AyudaMediosPagoConexFlowController.PARAMETRO_SALIDA_MEDIO_PAGO)) {
			MedioPagoBean medioPago = (MedioPagoBean) getDatos().get(AyudaMediosPagoConexFlowController.PARAMETRO_SALIDA_MEDIO_PAGO);
			if (medioPago != null) {
				tfCodFormaPago.setText(medioPago.getCodMedioPago());
				tfDesFormaPago.setText(medioPago.getDesMedioPago());
			}
		}
	}

	public void accionAceptar() {
		if (!StringUtils.isBlank(tfComercio.getText()) && !StringUtils.isBlank(tfTienda.getText()) && !StringUtils.isBlank(tfNumTPV.getText()) && !StringUtils.isBlank(tfServidorTCP.getText())
		        && !StringUtils.isBlank(tfPuertoTCP.getText()) && !StringUtils.isBlank(tfTimeout.getText()) && !StringUtils.isBlank(tfCodFormaPago.getText())) {
			if(StringUtils.isNumeric(tfTimeout.getText())) {
				Map<String, String> parametrosConfiguracion = new HashMap<String, String>();
				parametrosConfiguracion.put(TefConexFlow.COMERCIO, tfComercio.getText());
				parametrosConfiguracion.put(TefConexFlow.TIENDA, tfTienda.getText());
				parametrosConfiguracion.put(TefConexFlow.NUM_TPV, tfNumTPV.getText());
				parametrosConfiguracion.put(TefConexFlow.SERVIDOR_TCP, tfServidorTCP.getText());
				parametrosConfiguracion.put(TefConexFlow.PUERTO_TCP, tfPuertoTCP.getText());
				parametrosConfiguracion.put(TefConexFlow.TIMEOUT, tfTimeout.getText());
				parametrosConfiguracion.put(TefConexFlow.FORMA_PAGO, tfCodFormaPago.getText());
				
				getDatos().put(TefConexFlow.PARAMETRO_SALIDA_CONFIGURACION, parametrosConfiguracion);
				getStage().close();
			}
			else {
				VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("El timeout debe ser un valor numérico."), this.getStage());
			}
		}
		else {
			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Debe rellenar todos los campos."), this.getStage());
		}
	}
}
