package com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.adyen.cloud.configuracion;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.pos.dispositivo.tarjeta.adyen.cloud.configuracion.ConfAdyenCloudController;
import com.comerzzia.pos.dispositivo.tarjeta.adyen.constans.AdyenConstans;
import com.comerzzia.pos.dispositivo.tarjeta.mediosPago.AyudaMediosPagoController;
import com.comerzzia.pos.dispositivo.tarjeta.mediosPago.AyudaMediosPagoView;
import com.comerzzia.pos.persistence.mediosPagos.MedioPagoBean;
import com.comerzzia.pos.services.mediospagos.MediosPagosService;
import com.comerzzia.pos.util.i18n.I18N;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

@Component
@Primary
public class ByLConfAdyenCloudController extends ConfAdyenCloudController{
	
	private Logger log = Logger.getLogger(ByLConfAdyenCloudController.class);
	

	@FXML
	protected TextField tfCodFormaPago2, tfDesFormaPago2;
	@FXML
	private Label lbTitle, lbError;
	@FXML
	private CheckBox cbClientLive, cbClientTest;
	@FXML
        private TextField tfPOIID, tfProtocol, tfMerchantAccount, tfCurrency, tfApiKey, tfTerminalApiUrl, tfCodFormaPago, tfDesFormaPago;
	@FXML
	private TextField tfMerchantApplicationName, tfMerchantApplicationVersion;
	@FXML
	private TextField tfMerchantDeviceSystem, tfMerchantDeviceVersion, tfMerchantDeviceReference;
	
	protected static String PAYMETS2 = "PAYMENTS2";
        protected static final String TERMINAL_API_URL = "TERMINAL_API_URL";
	
	@Autowired
	private MediosPagosService mediosPagosService;
	
	@FXML
	public void openPaymentMethod2() {
		getApplication().getMainView().showModalCentered(AyudaMediosPagoView.class, getDatos(), getStage());
		if (getDatos().containsKey(AyudaMediosPagoController.PARAMETRO_SALIDA_MEDIO_PAGO)) {
			MedioPagoBean medioPago = (MedioPagoBean) getDatos().get(AyudaMediosPagoController.PARAMETRO_SALIDA_MEDIO_PAGO);
			if (medioPago != null) {
				tfCodFormaPago2.setText(medioPago.getCodMedioPago());
				tfDesFormaPago2.setText(medioPago.getDesMedioPago());
				log.debug("openPaymentMethod() - " + I18N.getTexto("Seleccionado el medio de pago: ") + medioPago.getCodMedioPago() + " - " + medioPago.getDesMedioPago());
			}
		}
	}
	
	@Override
	public void saveConfiguration() {
		Map<String, String> parametrosConfiguracion = new HashMap<String, String>();
		parametrosConfiguracion.put(AdyenConstans.CLIENT, cbClientLive.isSelected() ? AdyenConstans.CLIENT_LIVE : AdyenConstans.CLIENT_TEST);
		parametrosConfiguracion.put(AdyenConstans.POIID, tfPOIID.getText().trim());
		parametrosConfiguracion.put(AdyenConstans.PROTOCOLO, tfProtocol.getText().trim());
		parametrosConfiguracion.put(AdyenConstans.MERCHANT_ACCOUNT, tfMerchantAccount.getText().trim());
		parametrosConfiguracion.put(AdyenConstans.CURRENCY, tfCurrency.getText().trim());
		parametrosConfiguracion.put(AdyenConstans.PAYMENTS, tfCodFormaPago.getText().trim());
		parametrosConfiguracion.put(PAYMETS2, tfCodFormaPago2.getText().trim());
                parametrosConfiguracion.put(AdyenConstans.API_KEY, tfApiKey.getText().trim());
                parametrosConfiguracion.put(TERMINAL_API_URL, tfTerminalApiUrl.getText().trim());
		parametrosConfiguracion.put(AdyenConstans.MERCHANT_APPLICATION_NAME, tfMerchantApplicationName.getText().trim());
		parametrosConfiguracion.put(AdyenConstans.MERCHANT_APPLICATION_VERSION, tfMerchantApplicationVersion.getText().trim());
		parametrosConfiguracion.put(AdyenConstans.MERCHANT_DEVICE_SYSTEM, tfMerchantDeviceSystem.getText().trim());
		parametrosConfiguracion.put(AdyenConstans.MERCHANT_DEVICE_VERSION, tfMerchantDeviceVersion.getText().trim());
		parametrosConfiguracion.put(AdyenConstans.MERCHANT_DEVICE_REFERENCE, tfMerchantDeviceReference.getText().trim());
		getDatos().put(AdyenConstans.CONFIGURATION_PARAM, parametrosConfiguracion);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void readConfiguration() {
		Map<String, String> paramConfiguration = ((Map<String, String>) getDatos().get(AdyenConstans.CONFIGURATION_PARAM));
		for (Map.Entry<String, String> parametro : paramConfiguration.entrySet()) {
			if (parametro.getKey().equals(AdyenConstans.CLIENT)) {
				cbClientLive.setSelected(parametro.getValue().equals(AdyenConstans.CLIENT_LIVE));
				cbClientTest.setSelected(parametro.getValue().equals(AdyenConstans.CLIENT_TEST));
			}
			else if (parametro.getKey().equals(AdyenConstans.POIID)) {
				tfPOIID.setText(parametro.getValue());
			}
			else if (parametro.getKey().equals(AdyenConstans.PROTOCOLO)) {
				tfProtocol.setText(parametro.getValue());
			}
			else if (parametro.getKey().equals(AdyenConstans.MERCHANT_ACCOUNT)) {
				tfMerchantAccount.setText(parametro.getValue());
			}
			else if (parametro.getKey().equals(AdyenConstans.CURRENCY)) {
				tfCurrency.setText(parametro.getValue());
			}
			else if (parametro.getKey().equals(AdyenConstans.API_KEY)) {
				tfApiKey.setText(parametro.getValue());
			}
                        else if (parametro.getKey().equals(TERMINAL_API_URL)) {
                                tfTerminalApiUrl.setText(parametro.getValue());
                        }
			else if (parametro.getKey().equals(AdyenConstans.PAYMENTS)) {
				MedioPagoBean medioPago = mediosPagosService.getMedioPago(parametro.getValue());
				if (medioPago != null) {
					tfCodFormaPago.setText(medioPago.getCodMedioPago());
					tfDesFormaPago.setText(medioPago.getDesMedioPago());
					log.debug("leerConfiguracion() - El método de pago seleccionado es: " + tfDesFormaPago.getText());
				}
			}
			else if(parametro.getKey().equals(PAYMETS2)) {
				if (StringUtils.isNotBlank(parametro.getValue())) {
					MedioPagoBean medioPago = mediosPagosService.getMedioPago(parametro.getValue());
					tfCodFormaPago2.setText(medioPago.getCodMedioPago());
					tfDesFormaPago2.setText(medioPago.getDesMedioPago());
					log.debug("leerConfiguracion() - El método de pago 2 seleccionado es: " + tfDesFormaPago2.getText());
				}
			}
			else if (parametro.getKey().equals(AdyenConstans.MERCHANT_APPLICATION_NAME)) {
				tfMerchantApplicationName.setText(parametro.getValue());
			}
			else if (parametro.getKey().equals(AdyenConstans.MERCHANT_APPLICATION_VERSION)) {
				tfMerchantApplicationVersion.setText(parametro.getValue());
			}
			else if (parametro.getKey().equals(AdyenConstans.MERCHANT_DEVICE_SYSTEM)) {
				tfMerchantDeviceSystem.setText(parametro.getValue());
			}
			else if (parametro.getKey().equals(AdyenConstans.MERCHANT_DEVICE_VERSION)) {
				tfMerchantDeviceVersion.setText(parametro.getValue());
			}
			else if (parametro.getKey().equals(AdyenConstans.MERCHANT_DEVICE_REFERENCE)) {
				tfMerchantDeviceReference.setText(parametro.getValue());
			}
		}
	}
}
