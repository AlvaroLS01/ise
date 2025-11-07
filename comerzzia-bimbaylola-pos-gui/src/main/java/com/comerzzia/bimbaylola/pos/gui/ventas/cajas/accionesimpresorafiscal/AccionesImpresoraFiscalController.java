package com.comerzzia.bimbaylola.pos.gui.ventas.cajas.accionesimpresorafiscal;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.dispositivo.impresora.spark130f.Spark130F;
import com.comerzzia.bimbaylola.pos.services.spark130f.Spark130FService;
import com.comerzzia.bimbaylola.pos.services.spark130f.exception.Spark130FException;
import com.comerzzia.pos.core.dispositivos.Dispositivos;
import com.comerzzia.pos.core.gui.BackgroundTask;
import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.componentes.botonaccion.BotonBotoneraComponent;
import com.comerzzia.pos.core.gui.componentes.botonaccion.normal.BotonBotoneraNormalComponent;
import com.comerzzia.pos.core.gui.componentes.botonera.BotoneraComponent;
import com.comerzzia.pos.core.gui.componentes.botonera.IContenedorBotonera;
import com.comerzzia.pos.core.gui.componentes.botonera.PanelBotoneraBean;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.controllers.Controller;
import com.comerzzia.pos.core.gui.controllers.WindowController;
import com.comerzzia.pos.services.cajas.CajasServiceException;
import com.comerzzia.pos.util.i18n.I18N;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

@Component
public class AccionesImpresoraFiscalController extends WindowController implements IContenedorBotonera {

	private static final Logger log = Logger.getLogger(Controller.class.getName());

	@FXML
	protected AnchorPane panelAcciones;

	protected BotoneraComponent botoneraAcciones;

	@Autowired
	protected Spark130FService spark130FService;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}

	@Override
	public void initializeComponents() throws InitializeGuiException {

	}

	@Override
	public void initializeForm() throws InitializeGuiException {
		try {
			PanelBotoneraBean panelBotoneraBean = getView().loadBotonera();
			botoneraAcciones = new BotoneraComponent(panelBotoneraBean, panelAcciones.getPrefWidth(), panelAcciones.getPrefHeight(), this, BotonBotoneraNormalComponent.class);
			panelAcciones.getChildren().add(botoneraAcciones);

		}
		catch (Exception e) {
			VentanaDialogoComponent.crearVentanaAviso(
			        I18N.getTexto("No existe panel de funciones para la impresora configurada: " + Dispositivos.getInstance().getImpresora1().getConfiguracion().getModelo()), getStage());
		}
	}

	@Override
	public void initializeFocus() {
	}

	@Override
	public void realizarAccion(BotonBotoneraComponent botonAccionado) throws CajasServiceException {
	}

	@FXML
	public void accionCerrar() {
		getStage().close();
	}

	/*
	 * METODOS PARA RUSIA
	 */
	public void RUprintXReport() {
		if (Dispositivos.getInstance().getImpresora1() instanceof Spark130F) {
			new PrintXReport().start();
		}
	}

	protected class PrintXReport extends BackgroundTask<HashMap<String, String>> {

		public PrintXReport() {
			super();
		}

		@Override
		protected HashMap<String, String> call() throws Spark130FException {
			HashMap<String, String> resultado = null;

			List<String> listaCampos = new ArrayList<String>();
			listaCampos.add("RC");
			resultado = spark130FService.getCamposRespuesta(spark130FService.realizarLlamada(spark130FService.printXReport()), listaCampos);

			return resultado;
		}

		@Override
		protected void succeeded() {
			// HashMap<String, String> resultado = getValue();
			// String rc = resultado.get("RC");

			super.succeeded();
		}

		@Override
		protected void failed() {
			super.failed();
			Exception e = (Exception) getException();
			VentanaDialogoComponent.crearVentanaError(getStage(), I18N.getTexto("Se ha producido un error realizando el PrintXReport."), e);
			log.error("PrintXReport() - Se ha producido un error realizando el PrintXReport.", e);
		}
	}

}
