package com.comerzzia.bimbaylola.pos.gui.configuracion.tpv;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.axis.AxisManager;
import com.comerzzia.bimbaylola.pos.services.core.variables.ByLVariablesServices;
import com.comerzzia.bimbaylola.pos.services.dispositivofirma.ByLConfiguracionModelo;
import com.comerzzia.bimbaylola.pos.services.dispositivofirma.ByLDispositivosFirma;
import com.comerzzia.bimbaylola.pos.services.dispositivofirma.DispositivoFirma;
import com.comerzzia.bimbaylola.pos.services.dispositivofirma.FirmaNoConfig;
import com.comerzzia.bimbaylola.pos.services.dispositivofirma.IFirma;
import com.comerzzia.core.util.xml.XMLDocumentException;
import com.comerzzia.core.util.xml.XMLDocumentUtils;
import com.comerzzia.pos.core.dispositivos.Dispositivos;
import com.comerzzia.pos.core.dispositivos.TipoDispositivo;
import com.comerzzia.pos.core.dispositivos.configuracion.ConfiguracionDispositivo;
import com.comerzzia.pos.core.dispositivos.configuracion.ConfiguracionModelo;
import com.comerzzia.pos.core.dispositivos.dispositivo.DispositivoException;
import com.comerzzia.pos.core.dispositivos.dispositivo.IDispositivo;
import com.comerzzia.pos.core.gui.BackgroundTask;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.gui.configuracion.tpv.ConfiguracionTPVController;
import com.comerzzia.pos.persistence.core.tiendas.cajas.TiendaCajaBean;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.services.core.tiendas.cajas.TiendaCajaService;
import com.comerzzia.pos.services.core.tiendas.cajas.TiendaCajaServiceException;
import com.comerzzia.pos.services.core.variables.VariablesServices;
import com.comerzzia.pos.util.i18n.I18N;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

@Primary
@Component
public class ByLConfiguracionTPVController extends ConfiguracionTPVController {

	private static final Logger log = Logger.getLogger(ByLConfiguracionTPVController.class);

	public static final String TAG_FIRMAS = "firmas";
	public static final String TAG_METODOS_CONEX = "metodosconexion";
	public static final String TAG_MODELO = "modelo";
	public static final String TAG_NOMBRE_CONEXION = "nombreconexion";
	public static final String TAG_PARAMETROS_CONFIGURACION = "parametrosConfiguracion";

	public static final String ATT_MODELO = "modelo";
	public static final String ATT_NOMBRE_CONEX = "nombreconexion";
	public static final String ATT_TIPO_CONEX = "tipoconexion";
	public static final String ATT_MANEJADOR = "manejador";
	public static final String OPCION_FIRMA = "FIRMA";

	public static final String IMPRESORA_MODELO_TM30 = "TM M30";
	public static final String NOMBRE_CONEXION_TSE = "TSE";

	private static final String TEF_AXIS = "TEF Axis";

	private IFirma dispositivoFirmaInicial;

	@FXML
	protected Button btTestConexion, btAutoTest;
	@FXML
	protected Label lbTextoEjemplo;

	@Autowired
	protected VariablesServices variablesService;
	@Autowired
	protected TiendaCajaService tiendaCajaService;
	@Autowired
	protected Sesion sesion;

	// @Autowired
	// private ApplicationContext context;

	// TODO
	protected ChangeListener<String> cbDesConfigListener = new ChangeListener<String>(){

		@Override
		public void changed(ObservableValue<? extends String> paramObservableValue, String oldValue, String newValue) {
			mostrarOpcionesCBoxMetConexiones();
			cbMetConex.getSelectionModel().selectFirst();
		}
	};
	protected ChangeListener<String> cbMetConexListener = new ChangeListener<String>(){

		@Override
		public void changed(ObservableValue<? extends String> paramObservableValue, String oldValue, String newValue) {
			mostrarConfigMetConex();
		}
	};
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);
	}

	@Override
	public void initializeForm() {
		super.initializeForm();
		mostrarBotonesAxis(Boolean.FALSE);
	}

	@Override
	public void initializeFocus() {
		super.initializeFocus();
	}

	@Override
	public void initializeComponents() {
		log.debug("inicializarComponentes() - Inicializando componentes");

		cargarDatos();

		cbDesConfig.setItems(FXCollections.<String> observableArrayList());

		// TODO
		cbDesConfig.getSelectionModel().selectedItemProperty().addListener(cbDesConfigListener);
        cbMetConex.getSelectionModel().selectedItemProperty().addListener(cbMetConexListener);

//		cbDesConfig.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
//
//			@Override
//			public void changed(ObservableValue<? extends String> paramObservableValue, String oldValue, String newValue) {
//
//				if (opcionSeleccionada.equals(OPCION_FIRMA)) {
//					if (ByLDispositivosFirma.getInstance().getDispositivoFirmaActual() == null) {
//						cbMetConex.getSelectionModel().selectFirst();
//					}
//				}
//				else {
//					cbMetConex.getSelectionModel().selectFirst();
//				}
//			}
//		});
		configBalanzaProvisional = new ConfiguracionDispositivo(TipoDispositivo.BALANZA);
		configCajonProvisional = new ConfiguracionDispositivo(TipoDispositivo.CAJON);
		configFidelizProvisional = new ConfiguracionDispositivo(TipoDispositivo.FIDELIZACION);
		configGeneralProvisional = new ConfiguracionDispositivo();
		configVisorProvisional = new ConfiguracionDispositivo(TipoDispositivo.VISOR);
		configTarjRegaloProvisional = new ConfiguracionDispositivo(TipoDispositivo.GIFTCARD);
		configImpresoraProvisional = new ConfiguracionDispositivo(TipoDispositivo.IMPRESORA1);
		configImpresoraProvisional2 = new ConfiguracionDispositivo(TipoDispositivo.IMPRESORA2);
		configTarjetaProvisional = new ConfiguracionDispositivo(TipoDispositivo.TARJETA);
		configRecargaMovilProvisional = new ConfiguracionDispositivo(TipoDispositivo.RECARGA_MOVIL);
	}

	@FXML
	public void accionBtGenerales() {
		log.debug("accionBtGenerales()");

		btEditarConfiguracion.setVisible(false);

		lbMetodoConex.setText(I18N.getTexto("Caja configurada"));
		lbDesConfig.setText(I18N.getTexto("Tienda"));
		taConfig.setVisible(false);
		mostrarComboBox(false);

		opcionSeleccionada = OPCION_GENERAL;
		tfTienda.setText(sesion.getAplicacion().getTienda().getDesAlmacen());
		tfCaja.setText(sesion.getAplicacion().getCodCaja());
		taConfig.setText("");
		
		mostrarBotonesAxis(Boolean.FALSE);
	}

	@FXML
	public void accionBtImpresora() {
		log.debug("accionBtImpresora()");

		lbMetodoConex.setText(I18N.getTexto("Método de conexión"));
		lbDesConfig.setText(I18N.getTexto("Tipo de impresora"));
		taConfig.setVisible(true);
		mostrarComboBox(true);

		opcionSeleccionada = OPCION_IMPRESORA1;
		limpiarComboBox();

		ConfiguracionDispositivo impresora;

		if (configImpresoraProvisional.getModelo().isEmpty()) {
			impresora = Dispositivos.getInstance().getImpresora1().getConfiguracion();
		}
		else {
			impresora = configImpresoraProvisional;
		}
		if (impresora != null && !impresora.getModelo().equals(variableNoUsa)) {
			establecerConfigDispositivoInicial(impresora);
		}
		else {
			cbDesConfig.getSelectionModel().select(variableNoUsa);
			taConfig.setText("");
		}

		cbDesConfig.requestFocus();
		mostrarBotonesAxis(Boolean.FALSE);

		if (impresora != null && StringUtils.isNotBlank(impresora.getModelo()) && impresora.getModelo().equals(IMPRESORA_MODELO_TM30) && impresora.getNombreConexion().equals(NOMBRE_CONEXION_TSE)) {
			btEditarConfiguracion.setVisible(true);
		}
		else {
			btEditarConfiguracion.setVisible(false);
		}
	}

	@FXML
	public void accionBtImpresora2() {
		log.debug("accionBtImpresora2()");

		btEditarConfiguracion.setVisible(false);

		lbMetodoConex.setText(I18N.getTexto("Método de conexión"));
		lbDesConfig.setText(I18N.getTexto("Tipo de impresora"));
		taConfig.setVisible(true);
		mostrarComboBox(true);

		opcionSeleccionada = OPCION_IMPRESORA2;
		limpiarComboBox();

		ConfiguracionDispositivo impresora2;

		if (configImpresoraProvisional2.getModelo().isEmpty()) {
			impresora2 = Dispositivos.getInstance().getImpresora2().getConfiguracion();
		}
		else {
			impresora2 = configImpresoraProvisional2;
		}
		if (impresora2 != null && !impresora2.getModelo().equals(variableNoUsa)) {
			establecerConfigDispositivoInicial(impresora2);
		}
		else {
			cbDesConfig.getSelectionModel().select(variableNoUsa);
			taConfig.setText("");
		}

		cbDesConfig.requestFocus();
		mostrarBotonesAxis(Boolean.FALSE);
	}

	@FXML
	public void accionBtFidelizacion() {
		log.debug("accionBtFidelizacion()");

		btEditarConfiguracion.setVisible(false);

		lbMetodoConex.setText(I18N.getTexto("Método de conexión"));
		lbDesConfig.setText(I18N.getTexto("Tipo de tarjeta de fidelización"));
		taConfig.setVisible(true);
		mostrarComboBox(true);

		opcionSeleccionada = OPCION_FIDELIZACION;
		limpiarComboBox();

		ConfiguracionDispositivo fidelizacion;

		if (configFidelizProvisional.getModelo().isEmpty()) {
			fidelizacion = Dispositivos.getInstance().getFidelizacion().getConfiguracion();
		}
		else {
			fidelizacion = configFidelizProvisional;
		}

		if (fidelizacion != null && !fidelizacion.getModelo().equals(variableNoUsa)) {
			establecerConfigDispositivoInicial(fidelizacion);
		}
		else {
			cbDesConfig.getSelectionModel().select(variableNoUsa);
			taConfig.setText("");
		}

		cbDesConfig.requestFocus();
		mostrarBotonesAxis(Boolean.FALSE);
	}

	@FXML
	public void accionBtGiftCard() {
		log.debug("accionBtGiftCard()");

		btEditarConfiguracion.setVisible(false);

		lbMetodoConex.setText(I18N.getTexto("Método de conexión"));
		lbDesConfig.setText(I18N.getTexto("Tipo de visor"));
		taConfig.setVisible(true);
		mostrarComboBox(true);

		opcionSeleccionada = OPCION_GIFTCARD;
		limpiarComboBox();

		ConfiguracionDispositivo tarjRegalo;

		if (configTarjRegaloProvisional.getModelo().isEmpty()) {
            tarjRegalo = Dispositivos.getInstance().getGiftCard().getConfiguracion();
		}
		else {
			tarjRegalo = configTarjRegaloProvisional;
		}

		if (tarjRegalo != null && !tarjRegalo.getModelo().equals(variableNoUsa)) {
			establecerConfigDispositivoInicial(tarjRegalo);
		}
		else {
			cbDesConfig.getSelectionModel().select(variableNoUsa);
			taConfig.setText("");
		}

		cbDesConfig.requestFocus();
		mostrarBotonesAxis(Boolean.FALSE);

	}

	@FXML
	public void accionBtVisor() {
		log.debug("accionBtVisor()");

		btEditarConfiguracion.setVisible(false);

		lbMetodoConex.setText(I18N.getTexto("Método de conexión"));
		lbDesConfig.setText(I18N.getTexto("Tipo de visor"));
		taConfig.setVisible(true);
		mostrarComboBox(true);

		opcionSeleccionada = OPCION_VISOR;
		limpiarComboBox();

		ConfiguracionDispositivo visor;

		if (configVisorProvisional.getModelo().isEmpty()) {
			visor = Dispositivos.getInstance().getVisor().getConfiguracion();
		}
		else {
			visor = configVisorProvisional;
		}

		if (visor != null && !visor.getModelo().equals(variableNoUsa)) {
			establecerConfigDispositivoInicial(visor);
		}
		else {
			cbDesConfig.getSelectionModel().select(variableNoUsa);
			taConfig.setText("");
		}

		cbDesConfig.requestFocus();
		mostrarBotonesAxis(Boolean.FALSE);
	}

	@FXML
	public void accionBtCajon() {
		log.debug("accionBtCajon()");

		btEditarConfiguracion.setVisible(false);

		lbMetodoConex.setText(I18N.getTexto("Método de conexión"));
		lbDesConfig.setText(I18N.getTexto("Tipo de cajón"));
		taConfig.setVisible(true);
		mostrarComboBox(true);

		opcionSeleccionada = OPCION_CAJON;
		limpiarComboBox();

		ConfiguracionDispositivo cajon;

		if (configCajonProvisional.getModelo().isEmpty()) {
			cajon = Dispositivos.getInstance().getCajon().getConfiguracion();
		}
		else {
			cajon = configCajonProvisional;
		}

		if (cajon != null && !cajon.getModelo().equals(variableNoUsa)) {

			establecerConfigDispositivoInicial(cajon);
		}
		else {
			cbDesConfig.getSelectionModel().select(variableNoUsa);
			taConfig.setText("");
		}

		cbDesConfig.requestFocus();
		mostrarBotonesAxis(Boolean.FALSE);
	}

	@FXML
	public void accionBtBalanza() {
		log.debug("accionBtBalanza()");

		btEditarConfiguracion.setVisible(false);

		lbMetodoConex.setText(I18N.getTexto("Método de conexión"));
		lbDesConfig.setText(I18N.getTexto("Tipo de balanza"));
		taConfig.setVisible(true);
		mostrarComboBox(true);

		opcionSeleccionada = OPCION_BALANZA;
		limpiarComboBox();

		ConfiguracionDispositivo balanza;

		if (configBalanzaProvisional.getModelo().isEmpty()) {
			balanza = Dispositivos.getInstance().getBalanza().getConfiguracion();
		}
		else {
			balanza = configBalanzaProvisional;
		}

		if (balanza != null && !balanza.getModelo().equals(variableNoUsa)) {
			establecerConfigDispositivoInicial(balanza);
		}
		else {
			cbDesConfig.getSelectionModel().select(variableNoUsa);
			taConfig.setText("");
		}

		cbDesConfig.requestFocus();
		mostrarBotonesAxis(Boolean.FALSE);
	}

	@FXML
	public void accionBtRecargaMovil() {
		log.debug("accionBtRecargaMovil()");

		btEditarConfiguracion.setVisible(false);

		lbMetodoConex.setText(I18N.getTexto("Método de conexión"));
		lbDesConfig.setText(I18N.getTexto("Tipo de recarga móvil"));
		taConfig.setVisible(true);
		mostrarComboBox(true);

		opcionSeleccionada = OPCION_RECARGA_MOVIL;
		limpiarComboBox();

		ConfiguracionDispositivo recargaMovil;

		if (configRecargaMovilProvisional.getModelo().isEmpty()) {
			recargaMovil = Dispositivos.getInstance().getRecargaMovil().getConfiguracion();
		}
		else {
			recargaMovil = configRecargaMovilProvisional;
		}

		if (recargaMovil != null && !recargaMovil.getModelo().equals(variableNoUsa)) {

			establecerConfigDispositivoInicial(recargaMovil);
		}
		else {
			cbDesConfig.getSelectionModel().select(variableNoUsa);
			taConfig.setText("");
		}

		cbDesConfig.requestFocus();
		mostrarBotonesAxis(Boolean.FALSE);
	}

	/**
	 * Muestra o oculta los botones relacionados con los test de Axis.
	 * 
	 * @param mostrar
	 *            : Indica si mostrarlo o ocultarlo.
	 */
	private void mostrarBotonesAxis(Boolean mostrar) {
		btTestConexion.setVisible(mostrar);
		btAutoTest.setVisible(mostrar);
	}

	@FXML
	public void mostrarConfigMetConex() {
		if (!opcionSeleccionada.equals(OPCION_GENERAL)) {
			HashMap<String, HashMap<String, ConfiguracionModelo>> opcionesBtSeleccion = configuracionesDispositivos.get(opcionSeleccionada);

			String opcionSeleccionadaDesConfig = (String) cbDesConfig.getSelectionModel().getSelectedItem();
			String opcionSeleccionadaMetodoConfig = (String) cbMetConex.getSelectionModel().getSelectedItem();

			if (opcionSeleccionadaDesConfig != null && !opcionSeleccionada.equals(OPCION_FIRMA)) {
				ConfiguracionModelo metodoConex = null;
				HashMap<String, ConfiguracionModelo> metodosConex = opcionesBtSeleccion.get(opcionSeleccionadaDesConfig);
				if (metodosConex != null) {
					metodoConex = metodosConex.get((String) cbMetConex.getSelectionModel().getSelectedItem());
				}

				if (metodoConex != null) {
					taConfig.setText(metodoConex.getConfigConexion().toString());
					/* Debemos mostrar las operaciones en caso de tenerlas */
					if (metodoConex.getConfigOperaciones() != null) {
						taConfig.setText(taConfig.getText() + "\n" + metodoConex.getConfigOperaciones().toString());
					}
					establecerConfigProvisional(opcionSeleccionadaDesConfig, metodoConex);

					IDispositivo dispositivo = getDispositivoActual();
					if (dispositivo != null && dispositivo.isConfigurable()) {
						btEditarConfiguracion.setVisible(true);
					}
					else {
						btEditarConfiguracion.setVisible(false);
					}
				}
				else {
					taConfig.setText("");
				}

				/* En caso de ser Axis la opción seleccionada, deberemos mostrar los botones de Test */
				if (TEF_AXIS.equals(opcionSeleccionadaDesConfig)) {
					mostrarBotonesAxis(Boolean.TRUE);
				}
				else {
					mostrarBotonesAxis(Boolean.FALSE);
				}
			}
			else if (opcionSeleccionada.equals(OPCION_FIRMA)) {
				// TODO De momento no tendremos configuracion
				// HashMap<String, ByLConfiguracionModelo> metodosConexFirmas =
				// ByLDispositivos.getInstance().getDispositivosFirma().get(opcionSeleccionadaDesConfig).getListaConfiguracion();
				// ByLConfiguracionModelo metodoConexFirmas = metodosConexFirmas.get((String)
				// cbMetConex.getSelectionModel().getSelectedItem());
				// establecerConfigProvisional(opcionSeleccionadaDesConfig, metodoConexFirmas);

				if (opcionSeleccionadaDesConfig != null && opcionSeleccionadaMetodoConfig != null) {

					String manejador = ByLDispositivosFirma.getInstance().getDispositivosFirma().get(opcionSeleccionadaDesConfig).getListaConfiguracion()
					        .get((String) cbMetConex.getSelectionModel().getSelectedItem()).getManejador();

					IFirma firmaActual = ByLDispositivosFirma.getInstance().getDispositivoIFirma(manejador);
					ByLConfiguracionModelo configuracion = ByLDispositivosFirma.getInstance().getDispositivosFirma().get(opcionSeleccionadaDesConfig).getListaConfiguracion()
					        .get((String) cbMetConex.getSelectionModel().getSelectedItem());

					configuracion.setNombreConexion(cbMetConex.getSelectionModel().getSelectedItem());
					configuracion.setTipoConexion(cbMetConex.getSelectionModel().getSelectedItem());
					firmaActual.setModelo(cbDesConfig.getSelectionModel().getSelectedItem());
					firmaActual.setConfiguracionActual(configuracion);

					ByLDispositivosFirma.getInstance().setDispositivoFirmaActual(firmaActual);

				}
				else if (opcionSeleccionadaDesConfig != null && opcionSeleccionadaDesConfig.equals(I18N.getTexto("NO USA"))) {
					IFirma firmaNoConfig = new FirmaNoConfig();
					firmaNoConfig.setModelo(opcionSeleccionadaDesConfig);
					ByLDispositivosFirma.getInstance().setDispositivoFirmaActual(firmaNoConfig);
				}
			}
		}
	}

	/**
	 * Realiza un test de conexión con el Pinpad de Axis.
	 */
	@FXML
	public void testConexion() {
		try {

			new ConexionTest().start();

		}
		catch (Exception e) {
			log.error("testConexion() - " + e.getMessage());
			VentanaDialogoComponent.crearVentanaError(I18N.getTexto(e.getMessage()), getStage());
		}
	}

	protected class ConexionTest extends BackgroundTask<String> {

		public ConexionTest() {
			super();
		}

		@Override
		protected String call() throws Exception {
			/* Cargamos una nueva instancia de Axis con los datos de configuración y realizamos el test */
			ConfiguracionDispositivo configuracionAxis = ((AxisManager) Dispositivos.getInstance().getTarjeta()).getConfiguracion();
			AxisManager axisManager = new AxisManager();
			axisManager.setConfiguracion(configuracionAxis);

			/* Cargamos los archivos a partir de la ruta proporcionada de las variables de BD */
			String rutaProperties = variablesService.getVariableAsString(ByLVariablesServices.PATH_AXIS_CONFIGURACION);
			if (StringUtils.isNotBlank(rutaProperties)) {
				axisManager.loadParamsFromFile(rutaProperties);
			}
			return axisManager.conexionTest();
		}

		@Override
		protected void succeeded() {
			String mensajeTest = getValue();
			/* Realizamos el Test de conexión y comprobamos que no se han producido errores. */
			if (StringUtils.isBlank(mensajeTest)) {
				String mensajeInfo = "Se ha completado el Test sin errores";
				VentanaDialogoComponent.crearVentanaInfo(I18N.getTexto(mensajeInfo), getStage());
				/* Si el String de respuesta esta lleno, procedemos a mostrar el mensaje por pantalla. */
			}
			else {
				log.error("testConexion() - " + mensajeTest);
				VentanaDialogoComponent.crearVentanaError(I18N.getTexto(mensajeTest), getStage());
			}
			super.succeeded();
		}

		@Override
		protected void failed() {
			super.failed();
			Exception e = (Exception) getException();
			log.error("autoTest() - " + e.getMessage());
			VentanaDialogoComponent.crearVentanaError(I18N.getTexto(e.getMessage()), getStage());
		}
	}

	/**
	 * Realiza un test de funcionalidad en el Pinpad de Axis.
	 */
	@FXML
	public void autoTest() {
		try {

			new AutoTest().start();

		}
		catch (Exception e) {
			log.error("autoTest() - " + e.getMessage());
			VentanaDialogoComponent.crearVentanaError(I18N.getTexto(e.getMessage()), getStage());
		}
	}

	protected class AutoTest extends BackgroundTask<String> {

		public AutoTest() {
			super();
		}

		@Override
		protected String call() throws Exception {
			/* Cargamos una nueva instancia de Axis con los datos de configuración y realizamos el test */
			ConfiguracionDispositivo configuracionAxis = ((AxisManager) Dispositivos.getInstance().getTarjeta()).getConfiguracion();
			AxisManager axisManager = new AxisManager();
			axisManager.setConfiguracion(configuracionAxis);

			/* Cargamos los archivos a partir de la ruta proporcionada de las variables de BD */
			String rutaProperties = variablesService.getVariableAsString(ByLVariablesServices.PATH_AXIS_CONFIGURACION);
			if (StringUtils.isNotBlank(rutaProperties)) {
				axisManager.loadParamsFromFile(rutaProperties);
			}
			return axisManager.autoTest();
		}

		@Override
		protected void succeeded() {
			String mensajeTest = getValue();
			/* Realizamos el auto Test y comprobamos que no se han producido errores. */
			if (StringUtils.isBlank(mensajeTest)) {
				String mensajeInfo = "Se ha completado el Test sin errores";
				VentanaDialogoComponent.crearVentanaInfo(I18N.getTexto(mensajeInfo), getStage());
				/* Si el String de respuesta esta lleno, procedemos a mostrar el mensaje por pantalla. */
			}
			else {
				log.error("testConexion() - " + mensajeTest);
				VentanaDialogoComponent.crearVentanaError(I18N.getTexto(mensajeTest), getStage());
			}
			super.succeeded();
		}

		@Override
		protected void failed() {
			super.failed();
			Exception e = (Exception) getException();
			log.error("autoTest() - " + e.getMessage());
			VentanaDialogoComponent.crearVentanaError(I18N.getTexto(e.getMessage()), getStage());
		}
	}

	@Override
	public void mostrarOpcionesCBoxMetConexiones() {
		if (opcionSeleccionada.equals(OPCION_FIRMA)) {
			String opcionSeleccionadaDesConfig = (String) cbDesConfig.getSelectionModel().getSelectedItem();
			if (opcionSeleccionadaDesConfig != null) {
				DispositivoFirma dispositivo = ByLDispositivosFirma.getInstance().getDispositivosFirma().get(opcionSeleccionadaDesConfig);
				if (dispositivo != null) {
					HashMap<String, ByLConfiguracionModelo> metodosConex = dispositivo.getListaConfiguracion();
					if (metodosConex != null) {
						cbMetConex.setItems(FXCollections.observableList(new ArrayList<String>(metodosConex.keySet())));
					}
					else {
						cbMetConex.setItems(FXCollections.<String> emptyObservableList());
						// establecerConfigProvisional(variableNoUsa, null);
					}
					taConfig.setText("");
				}
				else {
					cbMetConex.setItems(FXCollections.<String> emptyObservableList());

					if (opcionSeleccionadaDesConfig.equals(I18N.getTexto("NO USA"))) {
						IFirma firmaNoConfig = new FirmaNoConfig();
						firmaNoConfig.setModelo(I18N.getTexto("NO USA"));
						ByLDispositivosFirma.getInstance().setDispositivoFirmaActual(firmaNoConfig);
					}
					// establecerConfigProvisional(variableNoUsa, null);
				}
			}
		}
		else {
			super.mostrarOpcionesCBoxMetConexiones();
		}
	}

	@FXML
	protected void accionBtFirma() {
		opcionSeleccionada = OPCION_FIRMA;

		dispositivoFirmaInicial = ByLDispositivosFirma.getInstance().getDispositivoFirmaActual();

		lbMetodoConex.setText(I18N.getTexto("Método de conexión"));
		lbDesConfig.setText(I18N.getTexto("Tipo de dispositivo de firma"));
		taConfig.setVisible(true);
		mostrarComboBox(true);
		limpiarComboBox();
		taConfig.setText("");

		LinkedList<String> listaFirmas = new LinkedList<>(ByLDispositivosFirma.getInstance().getDispositivosFirma().keySet());
		cbDesConfig.setItems(FXCollections.observableList(listaFirmas));
		cbDesConfig.requestFocus();

		IFirma firmaActual = ByLDispositivosFirma.getInstance().getDispositivoFirmaActual();
		if (firmaActual instanceof FirmaNoConfig) {
			if (listaFirmas.removeFirstOccurrence(I18N.getTexto("NO USA"))) {
				listaFirmas.addFirst(I18N.getTexto("NO USA"));
			}
			cbDesConfig.getSelectionModel().select(firmaActual.getModelo());
		}
		else if (firmaActual != null) {
			cbDesConfig.getSelectionModel().select(firmaActual.getModelo());
			cbMetConex.getSelectionModel().select(firmaActual.getConfiguracionActual().getTipoConexion());
		}

		mostrarBotonesAxis(Boolean.FALSE);
		btEditarConfiguracion.setVisible(false);
	}

	protected byte[] aniadirXMLFirma(byte[] xml) {
		Document doc = null;

		String modelo = ByLDispositivosFirma.getInstance().getDispositivoFirmaActual().getModelo();
		String nombreConexion;
		if (ByLDispositivosFirma.getInstance().getDispositivoFirmaActual() instanceof FirmaNoConfig) {
			nombreConexion = "";
			modelo = I18N.getTexto("NO USA");
		}
		else {
			nombreConexion = ByLDispositivosFirma.getInstance().getDispositivoFirmaActual().getConfiguracionActual().getTipoConexion();
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();

			doc = builder.parse(new ByteArrayInputStream(xml));

			Element dispositivosNode = XMLDocumentUtils.getElement(doc.getDocumentElement(), "dispositivos", true);

			dispositivosNode.appendChild(crearFirma("firma", modelo, nombreConexion, null, doc));

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);

			StreamResult result = new StreamResult(bos);
			transformer.transform(source, result);

		}
		catch (ParserConfigurationException | SAXException | IOException | TransformerException | XMLDocumentException e) {
			e.printStackTrace();
		}

		return bos.toByteArray();
	}

	protected Element crearFirma(String nDispositivo, String modelo, String nombreConexion, Map<String, String> parametrosConfiguracion, Document doc) {
		Element dispositivo = doc.createElement(nDispositivo);
		Element modeloNode = doc.createElement(TAG_MODELO);

		modeloNode.appendChild(doc.createTextNode(modelo));

		Element nombreConexNode = doc.createElement(TAG_NOMBRE_CONEXION);
		nombreConexNode.appendChild(doc.createTextNode(nombreConexion));

		dispositivo.appendChild(modeloNode);
		dispositivo.appendChild(nombreConexNode);

		if (parametrosConfiguracion != null) {
			Element parametrosConfiguracionNode = doc.createElement(TAG_PARAMETROS_CONFIGURACION);
			for (String parametro : parametrosConfiguracion.keySet()) {
				Element parametroConfiguracioNode = doc.createElement(parametro);
				parametroConfiguracioNode.appendChild(doc.createTextNode(parametrosConfiguracion.get(parametro)));
				parametrosConfiguracionNode.appendChild(parametroConfiguracioNode);
			}
			dispositivo.appendChild(parametrosConfiguracionNode);
		}

		return dispositivo;
	}

	@Override
	public void accionAceptar() {
		try {
			if (!configTarjetaProvisional.getModelo().equals("")) {
				Dispositivos.getInstance().getTarjeta().setConfiguracion(configTarjetaProvisional);
			}
			if (!configCajonProvisional.getModelo().equals("")) {
				Dispositivos.getInstance().getCajon().setConfiguracion(configCajonProvisional);
			}
			if (!configFidelizProvisional.getModelo().equals("")) {
				Dispositivos.getInstance().getFidelizacion().setConfiguracion(configFidelizProvisional);
			}
			if (!configImpresoraProvisional.getModelo().equals("")) {
				Dispositivos.getInstance().getImpresora1().setConfiguracion(configImpresoraProvisional);
			}
			if (!configImpresoraProvisional2.getModelo().equals("")) {
				Dispositivos.getInstance().getImpresora2().setConfiguracion(configImpresoraProvisional2);
			}
			if (!configTarjRegaloProvisional.getModelo().equals("")) {
				Dispositivos.getInstance().getGiftCard().setConfiguracion(configTarjRegaloProvisional);
			}
			if (!configVisorProvisional.getModelo().equals("")) {
				Dispositivos.getInstance().getVisor().setConfiguracion(configVisorProvisional);
			}
			if (!configBalanzaProvisional.getModelo().equals("")) {
				Dispositivos.getInstance().getBalanza().setConfiguracion(configBalanzaProvisional);
			}
			if (!configRecargaMovilProvisional.getModelo().equals("")) {
				Dispositivos.getInstance().getRecargaMovil().setConfiguracion(configRecargaMovilProvisional);
			}

			byte[] confDispositivos = Dispositivos.getInstance().crearConfiguracionDispositivos();
			if (ByLDispositivosFirma.getInstance().getDispositivoFirmaActual() != null) {
				confDispositivos = aniadirXMLFirma(confDispositivos);
			}

			if (confDispositivos != null) {
				TiendaCajaBean confCaja = sesion.getAplicacion().getTiendaCaja();
				confCaja.setConfiguracion(confDispositivos);
				try {
					tiendaCajaService.grabarConfiguracionDispositivos(confCaja);
					VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Deberá reiniciar la aplicación para establecer los cambios."), getStage());
					getApplication().getMainView().close();
				}
				catch (TiendaCajaServiceException ex) {
					VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Error guardando información de dispositivos"), getStage());
				}

				accionBtGenerales();
				btEditarConfiguracion.setVisible(false);
			}
		}
		catch (DispositivoException e) {
			log.error("accionAceptar() - " + e.getClass().getName() + " - " + e.getLocalizedMessage(), e);
			VentanaDialogoComponent.crearVentanaError(I18N.getTexto("Error guardando información de dispositivos"), getStage());
		}
	}

	@Override
	protected void establecerConfigProvisional(String modelo, ConfiguracionModelo metodoConex) {
		if (!opcionSeleccionada.equals(OPCION_FIRMA)) {
			super.establecerConfigProvisional(modelo, metodoConex);
		}
	}

	@Override
	public void accionCancelar() {
		ByLDispositivosFirma.getInstance().setDispositivoFirmaActual(dispositivoFirmaInicial);
		super.accionCancelar();
	}
	
	protected void establecerConfigDispositivoInicial(ConfiguracionDispositivo dispositivo) {
		HashMap<String, HashMap<String, ConfiguracionModelo>> configuracionDispositivo = configuracionesDispositivos.get(opcionSeleccionada);

		LinkedList<String> listaDispositivos = new LinkedList<>(configuracionDispositivo.keySet());

		// Fix para hacer que NO USA o la cadena equivalente sea la primera que aparezca
		if (listaDispositivos.removeFirstOccurrence(I18N.getTexto("NO USA"))) {
			listaDispositivos.addFirst(I18N.getTexto("NO USA"));
		}

		ObservableList<String> listDesConfig = FXCollections.observableList(listaDispositivos);
		cbDesConfig.getSelectionModel().selectedItemProperty().removeListener(cbDesConfigListener);
		cbDesConfig.setItems(listDesConfig);

		String modelo = dispositivo.getModelo();
		if (StringUtils.isBlank(modelo)) {
			modelo = "NO USA";
		}

		cbDesConfig.getSelectionModel().select(modelo);
		cbDesConfig.getSelectionModel().selectedItemProperty().addListener(cbDesConfigListener);
		HashMap<String, ConfiguracionModelo> configuracion = configuracionDispositivo.get(modelo);
		if (configuracion != null) {
			Set<String> metodosConexion = configuracion.keySet();
			cbMetConex.setItems(FXCollections.observableList(new ArrayList<>(metodosConexion)));
			cbMetConex.getSelectionModel().select(dispositivo.getNombreConexion());
			if (dispositivo.getConfiguracionModelo().getConfigConexion() != null) {
				taConfig.setText(dispositivo.getConfiguracionModelo().getConfigConexion().toString());
			}
			// Debemos mostrar las operaciones en caso de tenerlas
			if (dispositivo.getConfiguracionModelo().getConfigOperaciones() != null) {
				taConfig.setText(taConfig.getText() + "\n" + dispositivo.getConfiguracionModelo().getConfigOperaciones().toString());
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void limpiarComboBox() {
		HashMap<String, HashMap<String, ConfiguracionModelo>> listaOpcionesTienda = configuracionesDispositivos.get(opcionSeleccionada);
		cbDesConfig.getSelectionModel().selectedItemProperty().removeListener(cbDesConfigListener);
		if (listaOpcionesTienda != null) {
			LinkedList listaDispositivos = new LinkedList(listaOpcionesTienda.keySet());
			// Fix para hacer que NO USA o la cadena equivalente sea la primera que aparezca
			if (listaDispositivos.removeFirstOccurrence(I18N.getTexto("NO USA"))) {
				listaDispositivos.addFirst(I18N.getTexto("NO USA"));
			}
			cbDesConfig.setItems(FXCollections.observableList(listaDispositivos));
		}
		else {
			cbDesConfig.setItems(FXCollections.<String> observableArrayList());
		}
		cbDesConfig.getSelectionModel().selectedItemProperty().addListener(cbDesConfigListener);
		cbMetConex.setItems(FXCollections.<String> observableArrayList());
	}
}
