package com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.conexflow.gui.mediosPago;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.componentes.botonaccion.BotonBotoneraComponent;
import com.comerzzia.pos.core.gui.componentes.botonaccion.simple.BotonBotoneraSimpleComponent;
import com.comerzzia.pos.core.gui.componentes.botonera.BotoneraComponent;
import com.comerzzia.pos.core.gui.componentes.botonera.ConfiguracionBotonBean;
import com.comerzzia.pos.core.gui.componentes.botonera.IContenedorBotonera;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.controllers.WindowController;
import com.comerzzia.pos.core.gui.exception.CargarPantallaException;
import com.comerzzia.pos.core.gui.tablas.celdas.CellFactoryBuilder;
import com.comerzzia.pos.persistence.mediosPagos.MedioPagoBean;
import com.comerzzia.pos.services.mediospagos.MediosPagosService;
import com.comerzzia.pos.util.i18n.I18N;

@Controller
public class AyudaMediosPagoConexFlowController extends WindowController implements Initializable, IContenedorBotonera {

	private Logger log = Logger.getLogger(getClass());

	public static final String PARAMETRO_SALIDA_MEDIO_PAGO = "MEDIOPAGO";

	@SuppressWarnings("rawtypes")
	@FXML
	protected TableView tbMedioPago;

	@FXML
	protected AnchorPane panelBotoneraTabla;

	@SuppressWarnings("rawtypes")
	@FXML
	protected TableColumn tcCod, tcDescripcion;

	@FXML
	protected Button btAceptar, btCancelar;

	private BotoneraComponent botoneraAccionesTabla;

	@Autowired
	private MediosPagosService mediosPagosService;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		tcCod.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbMedioPago", "tcCod", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
		tcDescripcion.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbMedioPago", "tcDes", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));

		tcCod.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MedioPagoConexFlowGui, String>, ObservableValue<String>>(){

			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<MedioPagoConexFlowGui, String> cdf) {
				return cdf.getValue().getCodMedioPago();
			}
		});
		tcDescripcion.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MedioPagoConexFlowGui, String>, ObservableValue<String>>(){

			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<MedioPagoConexFlowGui, String> cdf) {
				return cdf.getValue().getDesMedioPago();
			}
		});
	}

	@Override
	public void initializeComponents() throws InitializeGuiException {
		registrarAccionCerrarVentanaEscape();
		crearEventoEnterTabla(tbMedioPago);

		try {
			List<ConfiguracionBotonBean> listaAccionesAccionesTabla = cargarAccionesTabla();
			botoneraAccionesTabla = new BotoneraComponent(4, 1, this, listaAccionesAccionesTabla, panelBotoneraTabla.getPrefWidth(), panelBotoneraTabla.getPrefHeight(),
			        BotonBotoneraSimpleComponent.class.getName());
			panelBotoneraTabla.getChildren().add(botoneraAccionesTabla);
		}
		catch (CargarPantallaException ex) {
			VentanaDialogoComponent.crearVentanaError(I18N.getTexto("Error cargando pantalla. Para mas información consulte el log."), getStage());
		}
	}

	private List<ConfiguracionBotonBean> cargarAccionesTabla() {
		List<ConfiguracionBotonBean> listaAcciones = new ArrayList<>();
		listaAcciones.add(new ConfiguracionBotonBean("iconos/navigate_up2.png", null, null, "ACCION_TABLA_PRIMER_REGISTRO", "REALIZAR_ACCION")); // "Home"
		listaAcciones.add(new ConfiguracionBotonBean("iconos/navigate_up.png", null, null, "ACCION_TABLA_ANTERIOR_REGISTRO", "REALIZAR_ACCION")); // "Page Up"
		listaAcciones.add(new ConfiguracionBotonBean("iconos/navigate_down.png", null, null, "ACCION_TABLA_SIGUIENTE_REGISTRO", "REALIZAR_ACCION")); // "Page Down"
		listaAcciones.add(new ConfiguracionBotonBean("iconos/navigate_down2.png", null, null, "ACCION_TABLA_ULTIMO_REGISTRO", "REALIZAR_ACCION")); // "End"
		return listaAcciones;
	}

	@SuppressWarnings({ "unchecked", "static-access" })
	@Override
	public void initializeForm() throws InitializeGuiException {
		List<MedioPagoBean> mediosPago = new ArrayList<MedioPagoBean>();
		mediosPago.addAll(mediosPagosService.mediosPagoTarjetas);

		ObservableList<MedioPagoConexFlowGui> guis = FXCollections.observableList(new ArrayList<MedioPagoConexFlowGui>());
		for (MedioPagoBean medioPago : mediosPago) {
			MedioPagoConexFlowGui medioPagoGui = new MedioPagoConexFlowGui(medioPago);
			guis.add(medioPagoGui);
		}

		tbMedioPago.getSelectionModel().clearSelection();
		tbMedioPago.setItems(guis);
	}

	@Override
	public void initializeFocus() {
		btAceptar.requestFocus();
	}

	public void accionAceptar() {
		if (tbMedioPago.getSelectionModel().getSelectedItem() != null) {
			getDatos().put(PARAMETRO_SALIDA_MEDIO_PAGO, ((MedioPagoConexFlowGui) tbMedioPago.getSelectionModel().getSelectedItem()).getMedioPago());
			getStage().close();
		}
		else {
			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Debe seleccionar un medio de pago."), this.getStage());
		}
	}

	public void aceptarDobleClick(MouseEvent event) {
		log.trace("aceptarDobleClick() - Acción aceptar");
		if (event.getButton().equals(MouseButton.PRIMARY)) {
			if (event.getClickCount() == 2) {
				accionAceptar();
			}
		}
	}

	@Override
	public void realizarAccion(BotonBotoneraComponent botonAccionado) {
		switch (botonAccionado.getClave()) {
		// BOTONERA TABLA MOVIMIENTOS
			case "ACCION_TABLA_PRIMER_REGISTRO":
				accionTablaPrimerRegistro();
				break;
			case "ACCION_TABLA_ANTERIOR_REGISTRO":
				accionTablaAnteriorRegistro();
				break;
			case "ACCION_TABLA_SIGUIENTE_REGISTRO":
				accionTablaSiguienteRegistro();
				break;
			case "ACCION_TABLA_ULTIMO_REGISTRO":
				accionTablaUltimoRegistro();
				break;
		}
	}

	/**
	 * Acción mover a primer registro de la tabla
	 *
	 * @param event
	 */
	@FXML
	private void accionTablaPrimerRegistro() {
		log.debug("accionTablaPrimerRegistro() - Acción ejecutada");
		if (tbMedioPago.getItems() != null && !tbMedioPago.getItems().isEmpty()) {
			tbMedioPago.getSelectionModel().select(0);
			tbMedioPago.scrollTo(0); // Mueve el scroll para que se vea el registro
		}
	}

	/**
	 * Acción mover a anterior registro de la tabla
	 *
	 * @param event
	 */
	@FXML
	private void accionTablaAnteriorRegistro() {
		log.debug("accionTablaAnteriorRegistro() - Acción ejecutada");
		if (tbMedioPago.getItems() != null && !tbMedioPago.getItems().isEmpty()) {
			int indice = tbMedioPago.getSelectionModel().getSelectedIndex();
			if (indice > 0) {
				tbMedioPago.getSelectionModel().select(indice - 1);
				tbMedioPago.scrollTo(indice - 1); // Mueve el scroll para que se vea el registro
			}
		}
	}

	/**
	 * Acción mover a siguiente registro de la tabla
	 *
	 * @param event
	 */
	@FXML
	private void accionTablaSiguienteRegistro() {
		log.debug("accionIrSiguienteRegistroTabla() - Acción ejecutada");
		if (tbMedioPago.getItems() != null && !tbMedioPago.getItems().isEmpty()) {
			int indice = tbMedioPago.getSelectionModel().getSelectedIndex();
			if (indice < tbMedioPago.getItems().size()) {
				tbMedioPago.getSelectionModel().select(indice + 1);
				tbMedioPago.scrollTo(indice + 1); // Mueve el scroll para que se vea el registro
			}
		}
	}

	/**
	 * Acción mover a último registro de la tabla
	 *
	 * @param event
	 */
	@FXML
	private void accionTablaUltimoRegistro() {
		log.debug("accionTablaUltimoRegistro() - Acción ejecutada");
		if (tbMedioPago.getItems() != null && !tbMedioPago.getItems().isEmpty()) {
			tbMedioPago.getSelectionModel().select(tbMedioPago.getItems().size() - 1);
			tbMedioPago.scrollTo(tbMedioPago.getItems().size() - 1); // Mueve el scroll para que se vea el registro
		}
	}

}
