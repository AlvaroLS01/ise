package com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.trazabilidad;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.services.spark130f.Spark130FService;
import com.comerzzia.bimbaylola.pos.services.spark130f.exception.Spark130FException;
import com.comerzzia.bimbaylola.pos.services.ticket.ByLLineaTicket;
import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.componentes.botonaccion.BotonBotoneraComponent;
import com.comerzzia.pos.core.gui.componentes.botonaccion.simple.BotonBotoneraSimpleComponent;
import com.comerzzia.pos.core.gui.componentes.botonera.BotoneraComponent;
import com.comerzzia.pos.core.gui.componentes.botonera.ConfiguracionBotonBean;
import com.comerzzia.pos.core.gui.componentes.botonera.IContenedorBotonera;
import com.comerzzia.pos.core.gui.componentes.botonera.KeyCodeCombinationParser;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.componentes.tecladonumerico.TecladoNumerico;
import com.comerzzia.pos.core.gui.controllers.WindowController;
import com.comerzzia.pos.services.ticket.lineas.LineaTicket;
import com.comerzzia.pos.util.config.AppConfig;
import com.comerzzia.pos.util.format.FormatUtil;
import com.comerzzia.pos.util.i18n.I18N;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

@Component
public class TrazabilidadController extends WindowController implements IContenedorBotonera {

	private Logger log = Logger.getLogger(getClass());

	public static final String PARAMETRO_CADENAS_TRAZABILIDAD_DOCUMENTO_ORIGEN = "cadenasTrazabilidad";
	public static final String PARAMETRO_LINEA_DOCUMENTO_ACTIVO = "linea";
	public static final String PARAMETRO_LISTA_CADENAS_TRAZABILIDAD_ASIGNADOS = "listaCadenasTrazabilidad";

	@FXML
	protected Label lbArticulo, lbCantidad, lbPendiente;
	@FXML
	protected TextField tfCadenaTrazabilidad;
	@FXML
	protected ListView<String> lvCadenasTrazabilidad;
	@FXML
	protected TecladoNumerico tecladoNumerico;
	@FXML
	protected AnchorPane panelBotonera;
	@FXML
	protected Button btCancelar;
	@FXML
	protected Button btAceptar;

	@Autowired
	protected Spark130FService spark130FService;

	protected LineaTrazabilidad linea;
	protected LineaTicket lineaTicket;
	protected List<String> cadenasTrazabilidadDocumentoOrigen;
	protected BigDecimal cantidadLinea;

	public String cadenaDM;

	private List<Integer> listaCharControl;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}

	@Override
	public void initializeComponents() throws InitializeGuiException {
		try {
			initTecladoNumerico(tecladoNumerico);

			List<ConfiguracionBotonBean> listaAcciones = new ArrayList<>();
			listaAcciones.add(new ConfiguracionBotonBean("iconos/navigate_up.png", null, null, "ANTERIOR_REGISTRO", "REALIZAR_ACCION"));
			listaAcciones.add(new ConfiguracionBotonBean("iconos/navigate_down.png", null, null, "SIGUIENTE_REGISTRO", "REALIZAR_ACCION"));
			listaAcciones.add(new ConfiguracionBotonBean("iconos/cancelar.png", null, null, "BORRAR_REGISTRO", "REALIZAR_ACCION"));
			listaAcciones.add(new ConfiguracionBotonBean("iconos/delete.png", null, null, "BORRAR_TODOS", "REALIZAR_ACCION"));

			BotoneraComponent botoneraAccionesTabla = new BotoneraComponent(4, 1, this, listaAcciones, panelBotonera.getPrefWidth(), panelBotonera.getPrefHeight(),
			        BotonBotoneraSimpleComponent.class.getName());
			panelBotonera.getChildren().add(botoneraAccionesTabla);

			registrarEventosTeclado();
			registrarAccionCerrarVentanaEscape();

			// tfCadenaTrazabilidad.textProperty().addListener(new ChangeListener<String>() {
			// @Override
			// public void changed(ObservableValue<? extends String> paramObservableValue, String oldValue, String
			// newValue) {
			// if(!oldValue.isEmpty() && !newValue.isEmpty()){
			// String text = Clipboard.getSystemClipboard().getString();
			// tfCadenaTrazabilidad.setText(makeControlCharactersVisible(text));
			// }
			// }
			// });

		}
		catch (Exception e) {
			log.error("initialize() - Ha habido un error al inicializar la pantalla: " + e.getMessage());
			VentanaDialogoComponent.crearVentanaError(getStage(), "Ha habido un error al inicializar la pantalla", e);
		}
	}

	protected void registrarEventosTeclado() {
		log.trace("registrarEventosTeclado()");

		KeyCodeCombinationParser parser = new KeyCodeCombinationParser();
		final KeyCodeCombination keyCodeEliminarGeneral = parser.parse(AppConfig.keyCodesInfo.getKeyCodeTablaEliminar(), new KeyCodeCombination(KeyCode.DELETE, KeyCombination.CONTROL_DOWN));

		EventHandler<KeyEvent> evhGeneral = new EventHandler<KeyEvent>(){

			@Override
			public void handle(javafx.scene.input.KeyEvent event) {
				if (keyCodeEliminarGeneral.match(event)) {
					borrarRegistro();
				}
			}
		};

		registraEventoTeclado(evhGeneral, KeyEvent.KEY_RELEASED);
	}

	@Override
	public void initializeFocus() {
		tfCadenaTrazabilidad.requestFocus();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initializeForm() throws InitializeGuiException {
		cadenaDM = "";
		listaCharControl = new ArrayList<Integer>();

		lineaTicket = (LineaTicket) getDatos().get(PARAMETRO_LINEA_DOCUMENTO_ACTIVO);
		linea = convertLineaTrazabilidad(lineaTicket);

		cadenasTrazabilidadDocumentoOrigen = null;
		cadenasTrazabilidadDocumentoOrigen = (List<String>) getDatos().get(PARAMETRO_CADENAS_TRAZABILIDAD_DOCUMENTO_ORIGEN);

		if (linea != null) {
			// Redondeamos la cantidad por si tuviese decimales, que no debería
			cantidadLinea = linea.getCantidad().setScale(0, RoundingMode.HALF_UP).abs();

			List<String> numerosOriginales = linea.getCadenasTrazabilidad();
			if (numerosOriginales == null) {
				lvCadenasTrazabilidad.setItems(FXCollections.observableArrayList(new ArrayList<String>()));
			}
			else {
				lvCadenasTrazabilidad.setItems(FXCollections.observableArrayList(numerosOriginales));
			}
			tfCadenaTrazabilidad.clear();

			lbArticulo.setText(linea.getDesArticulo());
			lbCantidad.setText(FormatUtil.getInstance().formateaNumero(cantidadLinea));
			if (linea.getCadenasTrazabilidad() != null) {
				lbPendiente.setText(FormatUtil.getInstance().formateaNumero(cantidadLinea.subtract(new BigDecimal((linea.getCadenasTrazabilidad().size())))));
			}
			else {
				lbPendiente.setText(FormatUtil.getInstance().formateaNumero(cantidadLinea));
			}

			if (lvCadenasTrazabilidad.getItems().size() > 0) {
				lvCadenasTrazabilidad.getSelectionModel().selectFirst();
			}
		}
		else {
			throw new InitializeGuiException("No se ha encontrado línea para editar");
		}
	}

	protected LineaTrazabilidad convertLineaTrazabilidad(final Object o) {
		if (o instanceof LineaTicket) {
			return new LineaTrazabilidad(){

				@Override
				public BigDecimal getCantidad() {
					return ((LineaTicket) o).getCantidad();
				}

				@Override
				public String getDesArticulo() {
					return ((LineaTicket) o).getDesArticulo();
				}

				@Override
				public List<String> getCadenasTrazabilidad() {
					return ((LineaTicket) o).getNumerosSerie();
				}
			};
		}
		else {
			return (LineaTrazabilidad) o;
		}
	}

	public void addCadenaTrazabilidad() {
		String cadenaTrazabilidad = tfCadenaTrazabilidad.getText().trim();
		if (StringUtils.isNotBlank(cadenaTrazabilidad)) {
			if (!hayRepetidos(cadenaTrazabilidad)) {
				if (quedanCadenasPorAsignar()) {
					try {
						if (cadenaTrazabilidadValida(cadenaTrazabilidad)) {
							lvCadenasTrazabilidad.getItems().add(cadenaTrazabilidad);
							lvCadenasTrazabilidad.getSelectionModel().selectLast();
							tfCadenaTrazabilidad.clear();

							lbPendiente.setText(FormatUtil.getInstance().formateaNumero(cantidadLinea.subtract(new BigDecimal(lvCadenasTrazabilidad.getItems().size()))));

							if (!quedanCadenasPorAsignar()) {
								btAceptar.requestFocus();
							}
							else {
								tfCadenaTrazabilidad.requestFocus();
							}
						}
						else {
							VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("La cadena de trazabilidad no coincide con la cadena de la línea origen."), getStage());
							tfCadenaTrazabilidad.clear();
						}
					}
					catch (Spark130FException e) {
						VentanaDialogoComponent.crearVentanaError(I18N.getTexto("Ha ocurrido un error con la impresora fiscal: ") + e.getMessage(), getStage());
						tfCadenaTrazabilidad.clear();
					}
				}
				else {
					VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("La cadena de trazabilidad debe ser única."), getStage());
					tfCadenaTrazabilidad.clear();
				}
			}
			else {
				VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Imposible insertar la cadena de trazabilidad del artículo, ya están todos asignados."), getStage());
				btAceptar.requestFocus();
			}
		}
		else {
			if (quedanCadenasPorAsignar()) {
				tfCadenaTrazabilidad.requestFocus();
			}
			else {
				btAceptar.requestFocus();
			}
		}
	}

	protected boolean quedanCadenasPorAsignar() {
		return lvCadenasTrazabilidad.getItems().size() < cantidadLinea.intValue();
	}

	protected boolean cadenaTrazabilidadValida(String cadenaTrazabilidad) throws Spark130FException {
		boolean res = true;

		if (cadenasTrazabilidadDocumentoOrigen != null && cadenasTrazabilidadDocumentoOrigen.size() > 0) {
			res = cadenasTrazabilidadDocumentoOrigen.contains(spark130FService.tratamientoHexDataMatrix(cadenaTrazabilidad, listaCharControl));
		}
		else if (cadenasTrazabilidadDocumentoOrigen == null) {
			res = checkDataMatrix();
		}
		return res;
	}

	protected boolean hayRepetidos(String cadenaTrazabilidad) {
		boolean res = false;
		for (String num : lvCadenasTrazabilidad.getItems()) {
			if (num.equals(cadenaTrazabilidad)) {
				res = true;
				break;
			}
		}
		return res;
	}

	@Override
	public void accionCancelar() {
		if (VentanaDialogoComponent.crearVentanaConfirmacion(I18N.getTexto("¿Seguro que quiere cancelar la inserción de cadenas de trazabilidad?"), getStage())) {
			getStage().close();
		}
	}

	@FXML
	public void accionAceptar() {
		if (quedanCadenasPorAsignar()) {
			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Hay cadenas de trazabilidad sin asignar"), getStage());
		}
		else {
			List<String> cadenasTrazabilidad = new LinkedList<String>();

			for (String item : lvCadenasTrazabilidad.getItems()) {
				cadenasTrazabilidad.add(spark130FService.tratamientoHexDataMatrix(item, listaCharControl));
			}
			
			getDatos().put(PARAMETRO_LISTA_CADENAS_TRAZABILIDAD_ASIGNADOS, cadenasTrazabilidad);
			getStage().close();

			limpiaCampos();
		}
	}

	@Override
	public void realizarAccion(BotonBotoneraComponent botonAccionado) {
		switch (botonAccionado.getClave()) {
			case "ANTERIOR_REGISTRO":
				irAnteriorRegistro();
				break;
			case "SIGUIENTE_REGISTRO":
				irSiguienteRegistro();
				break;
			case "BORRAR_REGISTRO":
				borrarRegistro();
				break;
			case "BORRAR_TODOS":
				borrarTodos();
				break;
			default:
				break;
		}
	}

	protected void borrarTodos() {
		if (VentanaDialogoComponent.crearVentanaConfirmacion(I18N.getTexto("Las cadenas de trazabilidad asociados a la línea de venta serán borrados, ¿Está seguro?"), getStage())) {
			lvCadenasTrazabilidad.getItems().clear();
			lbPendiente.setText(FormatUtil.getInstance().formateaNumero(cantidadLinea.subtract(new BigDecimal(lvCadenasTrazabilidad.getItems().size()))));
			tfCadenaTrazabilidad.requestFocus();
		}
	}

	protected void borrarRegistro() {
		if (lvCadenasTrazabilidad.getSelectionModel().getSelectedItem() != null
		        && VentanaDialogoComponent.crearVentanaConfirmacion(I18N.getTexto("La cadena de trazabilidad indicada será borrada, ¿Está seguro?"), getStage())) {
			int index = lvCadenasTrazabilidad.getSelectionModel().getSelectedIndex();
			lvCadenasTrazabilidad.getItems().remove(index);

			if (index > lvCadenasTrazabilidad.getItems().size() - 1) {
				index = lvCadenasTrazabilidad.getItems().size() - 1;
			}
			lvCadenasTrazabilidad.getSelectionModel().select(index);
			lbPendiente.setText(FormatUtil.getInstance().formateaNumero(cantidadLinea.subtract(new BigDecimal(lvCadenasTrazabilidad.getItems().size()))));
		}
	}

	public void irAnteriorRegistro() {
		log.trace("accionIrAnteriorRegistroTabla() - Acción ejecutada");
		if (lvCadenasTrazabilidad.getItems() != null && lvCadenasTrazabilidad.getItems() != null) {
			int indice = lvCadenasTrazabilidad.getSelectionModel().getSelectedIndex();
			if (indice > 0) {
				lvCadenasTrazabilidad.getSelectionModel().select(indice - 1);
				lvCadenasTrazabilidad.scrollTo(indice - 1);
			}
		}
	}

	public void irSiguienteRegistro() {
		log.trace("accionIrSiguienteRegistroTabla() - Acción ejecutada");
		if (lvCadenasTrazabilidad.getItems() != null && lvCadenasTrazabilidad.getItems() != null) {
			int indice = lvCadenasTrazabilidad.getSelectionModel().getSelectedIndex();
			if (indice < lvCadenasTrazabilidad.getItems().size()) {
				lvCadenasTrazabilidad.getSelectionModel().select(indice + 1);
				lvCadenasTrazabilidad.scrollTo(indice + 1);
			}
		}
	}

	@FXML
	public void actionTfCodigoIntro(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.MULTIPLY) {
			addCadenaTrazabilidad();
		}
	}

	private boolean checkDataMatrix() throws Spark130FException {
		try {
		
			String cadena = spark130FService.tratamientoHexDataMatrix(tfCadenaTrazabilidad.getText(), listaCharControl);

			Boolean resultado = spark130FService.checkDataMatrix((ByLLineaTicket) lineaTicket, cadena, false);
			if (!resultado) {
				if (VentanaDialogoComponent.crearVentanaConfirmacion(I18N.getTexto("El DataMatrix es incorrecto, ¿Desea continuar?"), getStage())) {
					spark130FService.confirmacionCheckDataMatrix();
				}
				else {
					limpiaCampos();
					throw new Spark130FException(I18N.getTexto("El DataMatrix insertado no es correcto y no se ha seguido con la venta."));
				}
			}

			return true;
		}
		catch (Spark130FException e) {
			limpiaCampos();
			throw new Spark130FException(e.getMessage());
		}
	}

//	private String makeControlCharactersVisible(String s) {
//		if (s == null) {
//			return s;
//		}
//
//		int len = s.length();
//		StringBuilder visible = new StringBuilder(len);
//
//		for (int i = 0; i < len; i++) {
//			char c = s.charAt(i);
//			visible.append(c >= 32 || c == '\n' ? c : (char) (c + 0x2400));
//		}
//
//		return visible.toString();
//	}
	

	private void limpiaCampos() {
		cadenaDM = "";
		listaCharControl.clear();
	}

	@FXML
	public void actionKeyPress(KeyEvent event) {
		if (event.getCode().equals(KeyCode.CONTROL)) {
			listaCharControl.add(cadenaDM.length());
		}
		else {
			cadenaDM += event.getText();
		}
	}
}
