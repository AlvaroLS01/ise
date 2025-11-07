package com.comerzzia.bimbaylola.pos.dispositivo.comun.tarjeta;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javax.validation.ConstraintViolation;

import net.sf.jni4net.Bridge;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import cbylsmartcard.ISmartCard;
import cbylsmartcard.cBYLSmartCard;

import com.comerzzia.bimbaylola.pos.dispositivo.giftcard.ByLGiftCard;
import com.comerzzia.pos.core.dispositivos.Dispositivos;
import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.validation.ValidationUI;
import com.comerzzia.pos.dispositivo.comun.tarjeta.CodigoTarjetaController;
import com.comerzzia.pos.dispositivo.comun.tarjeta.FormularioCodigoTarjetaBean;
import com.comerzzia.pos.util.config.AppConfig;
import com.comerzzia.pos.util.config.SpringContext;
import com.comerzzia.pos.util.config.TPVConfig;
import com.comerzzia.pos.util.i18n.I18N;

@Component
@Primary
public class ByLCodigoTarjetaController extends CodigoTarjetaController{

	protected static final Logger log = Logger.getLogger(ByLCodigoTarjetaController.class);

	public static final String PARAMETRO_NUM_TARJETA = "NUM_TARJETA";
	public static final String PARAMETRO_MODO = "MODO";
	public static final String PARAMETRO_IN_TEXTOCABECERA = "TEXTO_CABECERA";
	public static final String PARAMETRO_ID_FIDELIZADO = "ID_FIDELIZADO";
	public static final String PARAMETRO_TIPO_TARJETA = "TIPO_TARJETA";

	public static final String ACCION_CANCELAR = "ACCION_CANCELAR";

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb){
		frCodTarjeta = SpringContext.getBean(FormularioCodigoTarjetaBean.class);
		frCodTarjeta.setFormField("codTarjRegalo", tfNumero);
	}

	@Override
	public void initializeComponents(){
		registrarAccionCerrarVentanaEscape();
		try{
			URL resource = Thread.currentThread().getContextClassLoader().getResource(TPVConfig.POS_CONFIG_NAME);
			if(resource != null){
				String path = resource.toURI().toString();
				String separator = path.contains("/") ? "/" : "\\";
				File configFile = new File(new URL(path.substring(0, path.lastIndexOf(separator))).toURI());
				String configLevelPath = configFile.getAbsolutePath().substring(0, configFile.getAbsolutePath().lastIndexOf(File.separator));
				String libExtPath = configLevelPath + File.separator + "lib" + File.separator + "ext" + File.separator;

				Bridge.setVerbose(true);
				Bridge.init(new File(libExtPath));
				Bridge.LoadAndRegisterAssemblyFrom(new File(libExtPath + "cBYLSmartCard.j4n.dll"));
			}
		}
		catch(Exception e){
			log.error("initializeComponents() - Ha ocurrido un error al iniciar la comunicación con el lector de tarjetas: " + e.getMessage(), e);
		}
	}

	@Override
	public void initializeForm() throws InitializeGuiException{
		log.debug("initializeForm() - Iniciamos la lectura de la Tarjeta Regalo...");
		String nombreTarjeta = ((ByLGiftCard) Dispositivos.getInstance().getGiftCard()).getNombreTarjeta();
		ISmartCard tarjeta = new cBYLSmartCard();
		tarjeta.setNombreReader(nombreTarjeta);
		tfNumero.setText(tarjeta.GetCodigo());
		String texto = (String) getDatos().get(PARAMETRO_IN_TEXTOCABECERA);
		if(texto == null){
			texto = I18N.getTexto("Lea o escriba el código de barras de la tarjeta");
		}
		lbTitulo.setText(texto);
		String tipoTarjeta = (String) getDatos().get(PARAMETRO_TIPO_TARJETA);
		if(tipoTarjeta != null){
			if("GIFTCARD".equals(tipoTarjeta)){
				btnAltaFidelizado.setVisible(false);
			}
			else if("FIDELIZADO".equals(tipoTarjeta)){
				btnAltaFidelizado.setVisible(true);
			}
		}
	}

	@Override
	public void initializeFocus(){
		tfNumero.requestFocus();
	}

	@FXML
	public void accionAceptarIntro(KeyEvent e){
		if(e.getCode() == KeyCode.ENTER){
			accionAceptar();
		}
	}

	@FXML
	public void accionAceptar(){

		frCodTarjeta.setCodTarjRegalo(tfNumero.getText());

		// Validamos el formulario
		Set<ConstraintViolation<FormularioCodigoTarjetaBean>> constraintViolations = ValidationUI.getInstance().getValidator().validate(frCodTarjeta);
		if(constraintViolations.size() >= 1){
			ConstraintViolation<FormularioCodigoTarjetaBean> next = constraintViolations.iterator().next();
			frCodTarjeta.setErrorStyle(next.getPropertyPath(), true);
			frCodTarjeta.setFocus(next.getPropertyPath());
			VentanaDialogoComponent.crearVentanaError(next.getMessage(), this.getStage());
		}
		else{
			getDatos().put(PARAMETRO_NUM_TARJETA, tfNumero.getText());
			getStage().close();
		}
	}

	@FXML
	public void accionAltaFidelizado(){
		getDatos().put(PARAMETRO_ID_FIDELIZADO, null);
		getDatos().put(PARAMETRO_MODO, "INSERCION");
		getStage().close();
		getApplication().getMainView().showActionView(AppConfig.accionFidelizado, datos);
	}

	@FXML
	public void accionLeerTarjeta(){
		String numTarjeta = null;
		String nombreTarjeta = ((ByLGiftCard) Dispositivos.getInstance().getGiftCard()).getNombreTarjeta();
		ISmartCard tarjeta = new cBYLSmartCard();
		tarjeta.setNombreReader(nombreTarjeta);
		if(tarjeta != null){
			try{
				numTarjeta = tarjeta.GetCodigo();
			}
			catch(Exception e1){
				log.error("accionLeerTarjeta() - Ha ocurrido un error al leer la tarjeta regalo/abono: " + e1.getMessage(), e1);
			}

			if(numTarjeta != null && !numTarjeta.isEmpty()){
				tfNumero.setText(numTarjeta);
				accionAceptar();
			}
			else{
				VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("No se ha podido leer la tarjeta."), getStage());
			}
		}
	}

	@FXML
	public void accionCancelar(){
		getDatos().put(ACCION_CANCELAR, Boolean.TRUE);
		getStage().close();
	}

}
