package com.comerzzia.bimbaylola.pos.dispositivo.impresora.epsontm30.gui;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.dispositivo.impresora.epsontm30.EpsonTM30;
import com.comerzzia.bimbaylola.pos.services.epsontse.EpsonTSEService;
import com.comerzzia.bimbaylola.pos.services.epsontse.exception.EpsonTseException;
import com.comerzzia.pos.core.gui.BackgroundTask;
import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.controllers.WindowController;
import com.comerzzia.pos.util.i18n.I18N;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

@Component
public class ConfiguracionTM30Controller extends WindowController implements Initializable {

	private static final Logger log = Logger.getLogger(ConfiguracionTM30Controller.class);

	@Autowired
	protected EpsonTSEService epsonTSEService;

	@FXML
	private TextField tfIP;
	@FXML
	private Button btSetUp;
	@FXML
	private Button btExportData;
	@FXML
	private TextArea taStorageInfo;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}

	@Override
	public void initializeComponents() throws InitializeGuiException {
		taStorageInfo.setEditable(false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initializeForm() throws InitializeGuiException {
		if (getDatos().containsKey(EpsonTM30.PARAMETRO_SALIDA_CONFIGURACION)) {
			/* Realizamos la carga de los datos actuales */
			Map<String, String> parametros = ((Map<String, String>) getDatos().get(EpsonTM30.PARAMETRO_SALIDA_CONFIGURACION));
			for (String parametro : parametros.keySet()) {
				if (parametro.equals(EpsonTM30.EPSON_IP)) {
					tfIP.setText(parametros.get(parametro));
				}
			}
		}
		else {
			/* Realizamos un limpiado de los campos al entrar en el formulario */
			tfIP.clear();
		}

		try {
			if (epsonTSEService.socketConectado()) {
				epsonTSEService.enviarPeticion(epsonTSEService.getGetStorageInfo());
				String respuestaGetStorageInfo = epsonTSEService.lecturaSocket();
				taStorageInfo.setText(respuestaGetStorageInfo);
			}
		}
		catch (IOException e) {
			log.error("initializeForm() - Se ha producido un error al realizar el getStorageInfo.", e);
		}

	}

	@Override
	public void initializeFocus() {
	}

	public void accionAceptar() {
		if (!StringUtils.isBlank(tfIP.getText())) {
			Map<String, String> parametrosConfiguracion = new HashMap<String, String>();
			parametrosConfiguracion.put(EpsonTM30.EPSON_IP, tfIP.getText());

			getDatos().put(EpsonTM30.PARAMETRO_SALIDA_CONFIGURACION, parametrosConfiguracion);
			getStage().close();
		}
		else {
			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Debe rellenar todos los campos."), this.getStage());
		}
	}

	@FXML
	public void accionSetUp() {
		if (StringUtils.isNotBlank(tfIP.getText())) {
			try {
				new SetUp().start();
			}
			catch (Exception e) {
				VentanaDialogoComponent.crearVentanaError(I18N.getTexto("Se ha producido un error realizando el SetUp."), getStage());
				log.error("accionSetUp() - Se ha producido un error realizando el SetUp.", e);
			}
		}
	}

	protected class SetUp extends BackgroundTask<String> {

		public SetUp() {
			super();
		}

		@Override
		protected String call() throws EpsonTseException {
			String resultado = epsonTSEService.setUpInicialCompleto(tfIP.getText());

			return resultado;
		}

		@Override
		protected void succeeded() {
			String resultado = getValue();
			if (resultado != null && resultado.equals("OK")) {
				VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Se ha realizado el SetUp correctamente. Por favor, acepte los cambios y reinicie la aplicación."), getStage());
				accionAceptar();
			}
			else {
				VentanaDialogoComponent.crearVentanaError(I18N.getTexto("No se ha podido realizar el SetUp."), getStage());
				log.error("accionSetUp() - Se ha producido un error realizando el SetUp.");
			}

			super.succeeded();
		}

		@Override
		protected void failed() {
			super.failed();
			Exception e = (Exception) getException();
			VentanaDialogoComponent.crearVentanaError(getStage(), I18N.getTexto("Se ha producido un error realizando el SetUp."), e);
			log.error("accionSetUp() - Se ha producido un error realizando el SetUp.", e);
		}
	}

	@FXML
	public void accionExportData() {
		if (VentanaDialogoComponent.crearVentanaConfirmacion(I18N.getTexto("Ha seleccionado la opción Exportación, ¿desea continuar?"), getStage())) {
			try {
				new ExportData().start();
			}
			catch (Exception e) {
				VentanaDialogoComponent.crearVentanaError(I18N.getTexto("Se ha producido un error realizando el ExportData."), getStage());
				log.error("accionExportData() - Se ha producido un error realizando el ExportData.", e);
			}

		}
	}

	protected class ExportData extends BackgroundTask<Boolean> {

		public ExportData() {
			super();
		}

		@Override
		protected Boolean call() throws EpsonTseException {
			Boolean resultado = null;
			try {
				resultado = epsonTSEService.accionExportData();
			}
			catch (IOException e) {
				resultado = false;
			}

			return resultado;
		}

		@Override
		protected void succeeded() {
			Boolean resultado = getValue();
			if (resultado) {
				VentanaDialogoComponent.crearVentanaInfo(I18N.getTexto("Se ha realizado el ExportData correctamente"), getStage());
			}
			else {
				VentanaDialogoComponent.crearVentanaError(I18N.getTexto("No se ha podido realizar el ExportData."), getStage());
				log.error("accionExportData() - Se ha producido un error realizando el ExportData.");
			}

			super.succeeded();
		}

		@Override
		protected void failed() {
			super.failed();
			Exception e = (Exception) getException();
			VentanaDialogoComponent.crearVentanaError(getStage(), I18N.getTexto("Se ha producido un error realizando el ExportData."), e);
			log.error("accionExportData() - Se ha producido un error realizando el ExportData.", e);
		}
	}

	@FXML
	public void accionCopiarPortaPapeles() {
		ClipboardContent content = new ClipboardContent();
		content.putString(taStorageInfo.getText());
		Clipboard.getSystemClipboard().setContent(content);

		VentanaDialogoComponent.crearVentanaInfo(I18N.getTexto("StorageInfo copiado al portapapeles"), getStage());
	}

	@FXML
	public void accionAutoTest() {
		new AutoTest().start();
	}
	
	
	protected class AutoTest extends BackgroundTask<String> {

		public AutoTest() {
			super();
		}

		@Override
		protected String call() throws EpsonTseException {
			String resultado = null;
			try {
				resultado = epsonTSEService.autoTest();
			}
			catch (Exception e) {
				resultado = null;
			}

			return resultado;
		}

		@Override
		protected void succeeded() {
			String resultado = getValue();
			if (resultado != null) {
				log.debug("AutoTest() - resultado: " + resultado);
				VentanaDialogoComponent.crearVentanaInfo(I18N.getTexto("Se ha realizado el AutoTest correctamente"), getStage());
			}
			else {
				VentanaDialogoComponent.crearVentanaError(I18N.getTexto("Ha ocurrido un error al realizar el AutoTest."), getStage());
				log.error("accionExportData() - Se ha producido un error realizando el AutoTest.");
			}

			super.succeeded();
		}

		@Override
		protected void failed() {
			super.failed();
			Exception e = (Exception) getException();
			VentanaDialogoComponent.crearVentanaError(getStage(), I18N.getTexto("Se ha producido un error realizando el AutoTest."), e);
			log.error("accionExportData() - Se ha producido un error realizando el AutoTest.", e);
		}
	}
}
