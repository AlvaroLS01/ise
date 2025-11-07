package com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.axis.gui;

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

import com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.axis.AxisManager;
import com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.conexflow.gui.mediosPago.AyudaMediosPagoConexFlowController;
import com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.conexflow.gui.mediosPago.AyudaMediosPagoConexFlowView;
import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.controllers.WindowController;
import com.comerzzia.pos.persistence.mediosPagos.MedioPagoBean;
import com.comerzzia.pos.services.mediospagos.MediosPagosService;
import com.comerzzia.pos.util.i18n.I18N;

@Component
public class ConfiguracionAxisController extends WindowController implements Initializable{

	public static final String PARAMETRO_SALIDA_CONFIGURACION = "salida_configuracion";
	
	@FXML
	private TextField tfCodigoMoneda, tfCodFormaPago, tfDesFormaPago;

	@Autowired
	private MediosPagosService mediosPagosService;

	@Override
	public void initialize(URL url, ResourceBundle rb){}

	@Override
	public void initializeComponents() throws InitializeGuiException{
		tfCodFormaPago.focusedProperty().addListener(new ChangeListener<Boolean>(){
			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue){
				if(newValue == false){
					MedioPagoBean medioPago = mediosPagosService.getMedioPago(tfCodFormaPago.getText());
					if(medioPago != null){
						tfDesFormaPago.setText(medioPago.getDesMedioPago());
					}
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initializeForm() throws InitializeGuiException {
		if(getDatos().containsKey(PARAMETRO_SALIDA_CONFIGURACION)){
			/* Realizamos la carga de los datos actuales */
            Map<String,String> parametros = ((Map<String,String>) getDatos().get(PARAMETRO_SALIDA_CONFIGURACION));
			for(String parametro : parametros.keySet()){
				if(parametro.equals(AxisManager.COD_MONEDA_AXIS)){
					tfCodigoMoneda.setText(parametros.get(parametro));
				}else if(parametro.equals(AxisManager.FORMA_PAGO_AXIS)){
					tfCodFormaPago.setText(parametros.get(parametro));
					MedioPagoBean medioPago = mediosPagosService.getMedioPago(tfCodFormaPago.getText());
					if(medioPago != null){
						tfDesFormaPago.setText(medioPago.getDesMedioPago());
					}
				}
			}
		}else{
			/* Realizamos un limpiado de los campos al entrar en el formulario */
			tfCodigoMoneda.clear();
			tfCodFormaPago.clear();
			tfDesFormaPago.clear();
		}
	}

	@Override
	public void initializeFocus(){
		tfCodigoMoneda.requestFocus();
	}

	/**
	 * Abrimos la ventana de ayuda para seleccionar el medio de pago al que queremos
	 * hacer referencia.
	 */
	public void abrirAyudaMediosPago(){
		getApplication().getMainView().showModalCentered(AyudaMediosPagoConexFlowView.class, getDatos(), getStage());
		if(getDatos().containsKey(AyudaMediosPagoConexFlowController.PARAMETRO_SALIDA_MEDIO_PAGO)){
			MedioPagoBean medioPago = (MedioPagoBean) getDatos().get(AyudaMediosPagoConexFlowController.PARAMETRO_SALIDA_MEDIO_PAGO);
			if(medioPago != null){
				tfCodFormaPago.setText(medioPago.getCodMedioPago());
				tfDesFormaPago.setText(medioPago.getDesMedioPago());
			}
		}
	}

	public void accionAceptar(){
		if(!StringUtils.isBlank(tfCodigoMoneda.getText()) && !StringUtils.isBlank(tfCodFormaPago.getText())){
			Map<String, String> parametrosConfiguracion = new HashMap<String, String>();
			parametrosConfiguracion.put(AxisManager.COD_MONEDA_AXIS, tfCodigoMoneda.getText());
			parametrosConfiguracion.put(AxisManager.FORMA_PAGO_AXIS, tfCodFormaPago.getText());
			
			getDatos().put(PARAMETRO_SALIDA_CONFIGURACION, parametrosConfiguracion);
			getStage().close();
		}else{
			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Debe rellenar todos los campos."), this.getStage());
		}
	}
}
