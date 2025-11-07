package com.comerzzia.bimbaylola.pos.gui.ventas.articulos.devoluciones;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.comerzzia.ByL.backoffice.persistencia.fidelizacion.fidelizados.FidelizadosBean;
import com.comerzzia.ByL.backoffice.persistencia.fidelizacion.firma.FidelizadoFirmaBean;
import com.comerzzia.ByL.backoffice.rest.client.fidelizados.ByLFidelizadoRequestRest;
import com.comerzzia.ByL.backoffice.rest.client.fidelizados.ByLFidelizadosRest;
import com.comerzzia.ByL.backoffice.rest.client.firma.FidelizadoFirmaRequestRest;
import com.comerzzia.ByL.backoffice.rest.client.firma.FidelizadoFirmaResponseGet;
import com.comerzzia.ByL.backoffice.rest.client.firma.FidelizadoFirmaRest;
import com.comerzzia.bimbaylola.pos.dispositivo.fidelizacion.ByLFidelizacion.ByLFidelizacionTask;
import com.comerzzia.bimbaylola.pos.dispositivo.fidelizacion.busqueda.ByLBusquedaFidelizadoController;
import com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.axis.AxisManager;
import com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.axis.bean.AxisConsentimientoBean;
import com.comerzzia.bimbaylola.pos.gui.ByLBackgroundTask;
import com.comerzzia.bimbaylola.pos.gui.componentes.ventanaCarga.ByLVentanaEspera;
import com.comerzzia.bimbaylola.pos.gui.mantenimientos.fidelizados.datosgenerales.ByLPaneDatosGeneralesController;
import com.comerzzia.bimbaylola.pos.gui.pagos.ByLPagosController;
import com.comerzzia.bimbaylola.pos.gui.ventas.devoluciones.ByLDevolucionesController;
import com.comerzzia.bimbaylola.pos.gui.ventas.devoluciones.articulos.ByLLineaTicketAbonoGui;
import com.comerzzia.bimbaylola.pos.gui.ventas.devoluciones.fechaorigen.RequestFechaOrigenController;
import com.comerzzia.bimbaylola.pos.gui.ventas.tickets.ByLTicketManager;
import com.comerzzia.bimbaylola.pos.gui.ventas.tickets.ByLVentaProfesionalManager;
import com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.ByLFacturacionArticulosController;
import com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.agregarNotaInformativa.ByLAgregarNotaInformativaController;
import com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.agregarNotaInformativa.ByLAgregarNotaInformativaView;
import com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.cambiarVendedor.ByLCambiarVendedorView;
import com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.trazabilidad.TrazabilidadController;
import com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.trazabilidad.TrazabilidadView;
import com.comerzzia.bimbaylola.pos.persistence.fidelizacion.ByLFidelizacionBean;
import com.comerzzia.bimbaylola.pos.persistence.ticket.articulos.agregarnotainformativa.AvisoInformativoBean;
import com.comerzzia.bimbaylola.pos.services.core.impuestos.grupos.ByLGrupoImpuestosService;
import com.comerzzia.bimbaylola.pos.services.dispositivofirma.ByLDispositivosFirma;
import com.comerzzia.bimbaylola.pos.services.dispositivofirma.IFirma;
import com.comerzzia.bimbaylola.pos.services.ticket.ByLLineaTicket;
import com.comerzzia.bimbaylola.pos.services.ticket.ByLTicketVentaAbono;
import com.comerzzia.bimbaylola.pos.services.ticket.LineaTicketTrazabilidad;
import com.comerzzia.bimbaylola.pos.services.ticket.profesional.ByLLineaTicketProfesional;
import com.comerzzia.core.omnichannel.engine.documentos.DocumentoNotFoundException;
import com.comerzzia.model.fidelizacion.fidelizados.contactos.TiposContactoFidelizadoBean;
import com.comerzzia.pos.core.dispositivos.Dispositivos;
import com.comerzzia.pos.core.dispositivos.dispositivo.DispositivoCallback;
import com.comerzzia.pos.core.dispositivos.dispositivo.visor.IVisor;
import com.comerzzia.pos.core.gui.BackgroundTask;
import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.RestBackgroundTask;
import com.comerzzia.pos.core.gui.componentes.botonaccion.BotonBotoneraComponent;
import com.comerzzia.pos.core.gui.componentes.botonaccion.normal.BotonBotoneraNormalComponent;
import com.comerzzia.pos.core.gui.componentes.botonaccion.simple.BotonBotoneraSimpleComponent;
import com.comerzzia.pos.core.gui.componentes.botonera.BotoneraComponent;
import com.comerzzia.pos.core.gui.componentes.botonera.ConfiguracionBotonBean;
import com.comerzzia.pos.core.gui.componentes.botonera.PanelBotoneraBean;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.exception.CargarPantallaException;
import com.comerzzia.pos.core.gui.login.seleccionUsuarios.SeleccionUsuarioController;
import com.comerzzia.pos.core.gui.permisos.exception.SinPermisosException;
import com.comerzzia.pos.core.gui.tablas.celdas.CellFactoryBuilder;
import com.comerzzia.pos.dispositivo.comun.tarjeta.CodigoTarjetaController;
import com.comerzzia.pos.gui.ventas.devoluciones.IntroduccionArticulosController;
import com.comerzzia.pos.gui.ventas.devoluciones.articulos.DevolucionArticulosView;
import com.comerzzia.pos.gui.ventas.tickets.TicketManager;
import com.comerzzia.pos.gui.ventas.tickets.articulos.ConsultarIdFidelizadoTask;
import com.comerzzia.pos.gui.ventas.tickets.articulos.FacturacionArticulosController;
import com.comerzzia.pos.gui.ventas.tickets.articulos.FacturacionArticulosController.LineaInsertadaNoPermitidaException;
import com.comerzzia.pos.gui.ventas.tickets.articulos.FormularioLineaArticuloBean;
import com.comerzzia.pos.gui.ventas.tickets.articulos.LineaTicketGui;
import com.comerzzia.pos.gui.ventas.tickets.articulos.busquedas.BuscarArticulosController;
import com.comerzzia.pos.gui.ventas.tickets.articulos.busquedas.BuscarArticulosView;
import com.comerzzia.pos.gui.ventas.tickets.articulos.edicion.EdicionArticuloController;
import com.comerzzia.pos.gui.ventas.tickets.articulos.edicion.EdicionArticuloView;
import com.comerzzia.pos.gui.ventas.tickets.pagos.PagosController;
import com.comerzzia.pos.gui.ventas.tickets.pagos.PagosView;
import com.comerzzia.pos.persistence.articulos.ArticuloBean;
import com.comerzzia.pos.persistence.articulos.etiquetas.EtiquetaArticuloBean;
import com.comerzzia.pos.persistence.articulos.etiquetas.EtiquetaArticuloKey;
import com.comerzzia.pos.persistence.articulos.etiquetas.EtiquetaArticuloMapper;
import com.comerzzia.pos.persistence.core.documentos.propiedades.PropiedadDocumentoBean;
import com.comerzzia.pos.persistence.core.documentos.tipos.TipoDocumentoBean;
import com.comerzzia.pos.persistence.fidelizacion.FidelizacionBean;
import com.comerzzia.pos.persistence.giftcard.GiftCardBean;
import com.comerzzia.pos.services.articulos.ArticuloNotFoundException;
import com.comerzzia.pos.services.articulos.ArticulosService;
import com.comerzzia.pos.services.articulos.ArticulosServiceException;
import com.comerzzia.pos.services.cajas.CajaEstadoException;
import com.comerzzia.pos.services.cajas.CajasServiceException;
import com.comerzzia.pos.services.core.documentos.DocumentoException;
import com.comerzzia.pos.services.core.impuestos.grupos.GrupoImpuestosNotFoundException;
import com.comerzzia.pos.services.core.impuestos.grupos.GrupoImpuestosServiceException;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.services.core.sesion.SesionInitException;
import com.comerzzia.pos.services.core.variables.VariablesServices;
import com.comerzzia.pos.services.promociones.PromocionesServiceException;
import com.comerzzia.pos.services.ticket.TicketVenta;
import com.comerzzia.pos.services.ticket.TicketVentaAbono;
import com.comerzzia.pos.services.ticket.TicketsServiceException;
import com.comerzzia.pos.services.ticket.lineas.ILineaTicket;
import com.comerzzia.pos.services.ticket.lineas.LineaDevolucionCambioException;
import com.comerzzia.pos.services.ticket.lineas.LineaDevolucionNuevoArticuloException;
import com.comerzzia.pos.services.ticket.lineas.LineaTicket;
import com.comerzzia.pos.services.ticket.lineas.LineaTicketAbstract;
import com.comerzzia.pos.services.ticket.lineas.LineaTicketException;
import com.comerzzia.pos.util.bigdecimal.BigDecimalUtil;
import com.comerzzia.pos.util.config.AppConfig;
import com.comerzzia.pos.util.config.SpringContext;
import com.comerzzia.pos.util.config.Variables;
import com.comerzzia.pos.util.format.FormatUtil;
import com.comerzzia.pos.util.i18n.I18N;
import com.comerzzia.rest.client.fidelizados.ConsultarFidelizadoRequestRest;
import com.google.gson.Gson;
import com.ingenico.fr.jc3api.JC3ApiC3Rspn;
import com.sun.javafx.scene.control.skin.TableViewSkin;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

@Component
@Primary
public class ByLIntroduccionArticulosController extends IntroduccionArticulosController {

	private static final Logger log = Logger.getLogger(ByLIntroduccionArticulosController.class.getName());

	public static final String PROPIEDAD_ARTICULOS_TRAZABILIDAD_ETIQUETA = "ARTICULOS_TRAZABILIDAD.ETIQUETA";

	public static final String PAISES_OBLIGATORIO_FIDELIZADO = "DEVOLUCION.ES_OBLIGATORIO_FID";

	protected final IVisor visor = Dispositivos.getInstance().getVisor();

	@FXML
	protected TableColumn<LineaTicketGui, String> tcVendedor;

	@FXML
	protected Label lbNombreFide, lbNombreFideDato, lbEmailCliente, lbEmailClienteDato, lbMovilCliente,
			lbMovilClienteDato, lbFirmaCliente;
	@FXML
	protected Button btFirmaClienteDato;

	@Autowired
	private Sesion sesion;
	@Autowired
	private VariablesServices variablesServices;
	@Autowired
	protected ArticulosService articulosService;
	@Autowired
	protected EtiquetaArticuloMapper etiquetaArticuloMapper;
	@Autowired
	protected ByLGrupoImpuestosService grupoImpuestosService;
	
	public static Boolean esDevolucionSinTicket = Boolean.FALSE;

	protected ByLFidelizacionTask currentTask;

	protected String colorTrazabilidad;
	protected String colorTrazabilidadNoInsertada;
	protected Date fechaOrigenSinTicketReferenciar;

	public static final String PERMISO_DEVOLVER_NO_APTOS = "DEVOLVER NO APTOS";
	public static final String PERMISO_MODIFICAR_LINEA_NEGATIVA = "MODIFICAR LINEA NEGATIVA";
	public static final String PERMISO_CAMBIAR_VENDEDOR_LINEA = "CAMBIAR VENDEDOR LINEA";
	
	private static final String CODIGO_PAIS_ECUADOR ="EC";
	
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		log.debug("initialize() - Inicializando pantalla...");

		// Mensaje sin contenido para tabla. los establecemos a vacio
		// tbLineas.setPlaceholder(new Label(""));

		lineas = FXCollections.observableList(new ArrayList<LineaTicketGui>());

		// CENTRADO CON ESTILO A LA DERECHA
		tcLineasArticulo.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbLineas", "tcLineasArticulo", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
		tcLineasDescripcion.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbLineas", "tcLineasDescripcion", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
		tcLineasDesglose1.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbLineas", "tcLineasDesglose1", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
		tcLineasDesglose2.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbLineas", "tcLineasDesglose2", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
		tcLineasPVP.setCellFactory(CellFactoryBuilder.createCellRendererCeldaImporte("tbLineas", "tcLineasPVP", CellFactoryBuilder.ESTILO_ALINEACION_DCHA));
		tcLineasImporte.setCellFactory(CellFactoryBuilder.createCellRendererCeldaImporte("tbLineas", "tcLineasImporte", CellFactoryBuilder.ESTILO_ALINEACION_DCHA));
		tcLineasCantidad.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbLineas", "tcLineasCantidad", 0, CellFactoryBuilder.ESTILO_ALINEACION_DCHA));
		tcVendedor.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbLineas", "tcVendedor", 2, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));

		Boolean vendedorVisible = variablesServices.getVariableAsBoolean(VariablesServices.TPV_COLUMNA_VENDEDOR_VISIBLE, false);
		if (!vendedorVisible) {
			tcVendedor.setVisible(false);
		}

		// Asignamos las lineas a la tabla
		tbLineas.setItems(lineas);

		/* BYL-176 ajustar columnas para importes altos */
		tbLineas.getItems().addListener(new ListChangeListener<Object>(){

			private Method columnToFitMethod;

			private void loadMethod() {
				try {
					columnToFitMethod = TableViewSkin.class.getDeclaredMethod("resizeColumnToFitContent", TableColumn.class, int.class);
					columnToFitMethod.setAccessible(true);
				}
				catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onChanged(Change<?> c) {
				if (columnToFitMethod == null) {
					loadMethod();
				}
				try {
					columnToFitMethod.invoke(tbLineas.getSkin(), tcLineasPVP, -1);
					columnToFitMethod.invoke(tbLineas.getSkin(), tcLineasImporte, -1);
				}
				catch (Exception e) {
					log.warn("initialize() - No se pueden ajustar columnas a contenido: " + e.getMessage(), e);
				}
			}
		});

		// Definimos un factory para cada celda para aumentar el rendimiento
		tcLineasArticulo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaTicketGui, String>, ObservableValue<String>>(){

			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<LineaTicketGui, String> cdf) {
				return cdf.getValue().getArtProperty();
			}
		});
		tcLineasDescripcion.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaTicketGui, String>, ObservableValue<String>>(){

			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<LineaTicketGui, String> cdf) {
				return cdf.getValue().getDescripcionProperty();
			}
		});
		tcLineasCantidad.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaTicketGui, BigDecimal>, ObservableValue<BigDecimal>>(){

			@Override
			public ObservableValue<BigDecimal> call(TableColumn.CellDataFeatures<LineaTicketGui, BigDecimal> cdf) {
				return cdf.getValue().getCantidadProperty();
			}
		});
		tcLineasDesglose1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaTicketGui, String>, ObservableValue<String>>(){

			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<LineaTicketGui, String> cdf) {
				return cdf.getValue().getDesglose1Property();
			}
		});
		tcLineasDesglose2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaTicketGui, String>, ObservableValue<String>>(){

			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<LineaTicketGui, String> cdf) {
				return cdf.getValue().getDesglose2Property();
			}
		});
		tcLineasPVP.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaTicketGui, BigDecimal>, ObservableValue<BigDecimal>>(){

			@Override
			public ObservableValue<BigDecimal> call(TableColumn.CellDataFeatures<LineaTicketGui, BigDecimal> cdf) {
				return cdf.getValue().getPvpProperty();
			}
		});
		tcLineasImporte.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaTicketGui, BigDecimal>, ObservableValue<BigDecimal>>(){

			@Override
			public ObservableValue<BigDecimal> call(TableColumn.CellDataFeatures<LineaTicketGui, BigDecimal> cdf) {
				return cdf.getValue().getImporteTotalFinalProperty();
			}
		});
		tcVendedor.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaTicketGui, String>, ObservableValue<String>>(){

			@Override
			public ObservableValue<String> call(CellDataFeatures<LineaTicketGui, String> cdf) {
				return cdf.getValue().getVendedorProperty();
			}
		});

		// Ocultamos el Pad numérico si es necesario
		log.debug("initialize() - Comprobando configuración para panel numérico");
		if (!Variables.MODO_PAD_NUMERICO) {
			log.debug("initialize() - PAD Numerico off");
			panelNumberPad.setMaxWidth(0);
			panelNumberPad.setMinWidth(0);
			panelNumberPad.setPrefWidth(0);
			panelNumberPad.getChildren().clear();
		}

		Callback<TableView<LineaTicketGui>, TableRow<LineaTicketGui>> callback = new Callback<TableView<LineaTicketGui>, TableRow<LineaTicketGui>>(){

			@Override
			public TableRow<LineaTicketGui> call(TableView<LineaTicketGui> p) {
				return new TableRow<LineaTicketGui>(){

					@Override
					protected void updateItem(LineaTicketGui linea, boolean empty) {
						super.updateItem(linea, empty);
						if (linea != null) {
							if (((ByLLineaTicketAbonoGui) linea).getAptoTrazabilidad()) {
								if (((ByLLineaTicketAbonoGui) linea).getTieneCadenaTrazabilidad()) {
									setStyle("-fx-background-color: #" + colorTrazabilidad + ";");
								}
								else {
									setStyle("-fx-background-color: #" + colorTrazabilidadNoInsertada + ";");
								}
							}
							else {
								setStyle("");
							}
						}
						else {
							setStyle("");
						}
					}
				};
			}
		};

		tbLineas.setRowFactory(callback);

	}

	@Override
	public void initializeComponents() throws InitializeGuiException {

		try {

			ticketManager = SpringContext.getBean(TicketManager.class);
			ticketManager.init();
			initTecladoNumerico(tecladoNumerico);

			log.debug("inicializarComponentes() - Inicialización de componentes...");

			log.debug("inicializarComponentes() - Carga de acciones de botonera inferior");

			PanelBotoneraBean panelBotoneraBean = getView().loadBotonera();
			botonera = new BotoneraComponent(panelBotoneraBean, panelBotonera.getPrefWidth(),
					panelBotonera.getPrefHeight(), this, BotonBotoneraNormalComponent.class);
			panelBotonera.getChildren().add(botonera);

			// Botonera de Tabla
			log.debug("inicializarComponentes() - Carga de acciones de botonera de tabla de ventas");
			List<ConfiguracionBotonBean> listaAccionesAccionesTabla = cargarAccionesTabla();
			botoneraAccionesTabla = new BotoneraComponent(1, listaAccionesAccionesTabla.size(), this,
					listaAccionesAccionesTabla, panelMenuTabla.getPrefWidth(), panelMenuTabla.getPrefHeight(),
					BotonBotoneraSimpleComponent.class.getName());
			panelMenuTabla.getChildren().add(botoneraAccionesTabla);

			log.debug("inicializarComponentes() - registrando acciones de la tabla principal");
			crearEventoEnterTabla(tbLineas);
			crearEventoNegarTabla(tbLineas);
			crearEventoEliminarTabla(tbLineas);
			crearEventoNavegacionTabla(tbLineas);

			log.debug("inicializarComponentes() - Configuración de la tabla");
			if (sesion.getAplicacion().isDesglose1Activo()) { // Si hay desglose 1, establecemos el texto
				tcLineasDesglose1.setText(I18N
						.getTexto(variablesServices.getVariableAsString(VariablesServices.ARTICULO_DESGLOSE1_TITULO)));
			} else { // si no hay desgloses, compactamos la línea
				tcLineasDesglose1.setVisible(false);
			}
			if (sesion.getAplicacion().isDesglose2Activo()) { // Si hay desglose 1, establecemos el texto
				tcLineasDesglose2.setText(I18N
						.getTexto(variablesServices.getVariableAsString(VariablesServices.ARTICULO_DESGLOSE2_TITULO)));
			} else { // si no hay desgloses, compactamos la línea
				tcLineasDesglose2.setVisible(false);
			}

			frValidacionBusqueda = SpringContext.getBean(FormularioLineaArticuloBean.class);
			frValidacionBusqueda.setFormField("cantidad", tfCantidadIntro);
			// Inicializamos los formularios

			frValidacion = new FormularioLineaArticuloBean();
			frValidacion.setFormField("codArticulo", tfCodigoIntro);
			frValidacion.setFormField("cantidad", tfCantidadIntro);

			registraEventoTeclado(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent event) {
					if (event.getCode() == KeyCode.MULTIPLY) {
						cambiarCantidad();
					}
				}
			}, KeyEvent.KEY_RELEASED);

			addSeleccionarTodoCampos();

		} catch (CargarPantallaException | SesionInitException ex) {
			log.error("inicializarComponentes() - Error inicializando pantalla de venta de artículos");
			VentanaDialogoComponent.crearVentanaError("Error cargando pantalla. Para mas información consulte el log.",
					getStage());
		}
	}

	@Override
	public void initializeForm() throws InitializeGuiException {
		log.debug("initializeForm() - Inicializando formulario...");
		try {
			tfCodigoIntro.setText("");
			tbLineas.getSelectionModel().clearSelection();

			if (getView().getParentView().getController().getDatos().get(ByLDevolucionesController.DEVOLUCION_SIN_TICKET) != null) {
				esDevolucionSinTicket = (Boolean) getView().getParentView().getController().getDatos().get(ByLDevolucionesController.DEVOLUCION_SIN_TICKET);
			}
			getView().getParentView().getController().getDatos().remove(ByLDevolucionesController.DEVOLUCION_SIN_TICKET);

			fechaOrigenSinTicketReferenciar = null;
			if (getView().getParentView().getController().getDatos().containsKey(RequestFechaOrigenController.FECHA_RELLENADA)) {
				fechaOrigenSinTicketReferenciar = ((Date) getView().getParentView().getController().getDatos().get(RequestFechaOrigenController.FECHA_RELLENADA));
				getView().getParentView().getController().getDatos().remove(RequestFechaOrigenController.FECHA_RELLENADA);
			}

			boolean cajaAbierta = sesion.getSesionCaja().isCajaAbierta();
			comprobarAperturaPantalla();

			/* Ocultamos los componentes relacionado con los Fidelizados */
			ocultarComponentesFidelizado(Boolean.FALSE);

			if (cajaAbierta) {
				if (!esDevolucionSinTicket) {
					HashMap<String, Object> datos = getView().getParentView().getController().getDatos();

					if (datos.containsKey(TICKET_KEY)) {
						ticketManager = (TicketManager) datos.get(TICKET_KEY);
						if (!ticketManager.isTicketAbierto()) {
							try {
								ticketManager.nuevoTicket();
								ticketManager.setEsDevolucion(true);
								ticketManager.setDocumentoActivo(documentos.getDocumentoAbono(ticketManager.getTicketOrigen().getCabecera().getCodTipoDocumento()));

							}
							catch (PromocionesServiceException | DocumentoException ex) {
								log.error("initializeForm() - Error inicializando ticket", ex);
								VentanaDialogoComponent.crearVentanaError(getStage(), ex.getMessageI18N(), ex);
							}
						}
						else {
							if (!sesion.getSesionCaja().getCajaAbierta().getUidDiarioCaja().equals(ticketManager.getTicket().getCabecera().getUidDiarioCaja())) {
								try {
									ticketManager.nuevoTicket();
									ticketManager.setEsDevolucion(true);
									ticketManager.setDocumentoActivo(documentos.getDocumentoAbono(ticketManager.getTicketOrigen().getCabecera().getCodTipoDocumento()));
								}
								catch (PromocionesServiceException | DocumentoException ex) {
									log.error("initializeForm() - Error inicializando ticket", ex);
									VentanaDialogoComponent.crearVentanaError(getStage(), ex.getMessageI18N(), ex);
								}
							}
							ticketManager.setEsDevolucion(true);
						}

						if (tipoDocumentoInicial == null) {
							tipoDocumentoInicial = documentos.getDocumento(((TicketVentaAbono) ticketManager.getTicket()).getCabecera().getTipoDocumento());
						}
					}

					if (ticketManager == null) {
						log.error("initializeForm() -----No se ha inicializado el ticket manager-----");
						throw new InitializeGuiException();
					}

					// Comprobamos si la operación es una devolución de tarjeta regalo
					if (ticketManager.esDevolucionRecargaTarjetaRegalo()) {
						GiftCardBean tarjeta = Dispositivos.getInstance().getGiftCard().consultarTarjetaRegalo(ticketManager.getTicketOrigen().getCabecera().getTarjetaRegalo().getNumTarjetaRegalo(),
						        sesion.getAplicacion().getUidActividad());
						// Comprobamos si la tarjeta en cuestión está dada de baja
						if (tarjeta != null) {
							if (tarjeta.isBaja()) {
								VentanaDialogoComponent.crearVentanaConfirmacion(I18N.getTexto("La tarjeta introducida está dada de baja."), getStage());
							}
						}
						else {
							VentanaDialogoComponent.crearVentanaConfirmacion(I18N.getTexto("El número de tarjeta no es válido."), getStage());
						}
					}

					if (ticketManager.getTicket() != null && ticketManager.getTicket().getLineas() != null && !ticketManager.getTicket().getLineas().isEmpty()) {
						escribirUltimaLineaEnVisor();
					}

				}
				else {
					if (ticketManager.getTicket() == null || ticketManager.getTicketOrigen() != null) {
						ticketManager.eliminarTicketCompleto();
						ticketManager.nuevoTicket();
					}

					ticketManager.setEsDevolucion(true);
					((ByLTicketManager) ticketManager).setEsTicketRegalo(Boolean.FALSE);

					TipoDocumentoBean documentoAbono = documentos.getDocumentoAbono(ticketManager.getDocumentoActivo().getCodtipodocumento());

					ticketManager.setDocumentoActivo(documentoAbono);
				}

				try {
					TipoDocumentoBean tipoDocumento = documentos.getDocumento(ticketManager.getTicket().getCabecera().getTipoDocumento());

					PropiedadDocumentoBean propiedadTrazabilidadColor = tipoDocumento.getPropiedades().get(ByLFacturacionArticulosController.PROPIEDAD_ARTICULOS_TRAZABILIDAD_COLOR);
					if (propiedadTrazabilidadColor != null && StringUtils.isNotBlank(propiedadTrazabilidadColor.getValor())) {
						colorTrazabilidad = propiedadTrazabilidadColor.getValor();
					}

					PropiedadDocumentoBean propiedadTrazabilidadColorNoInsertada = tipoDocumento.getPropiedades()
					        .get(ByLFacturacionArticulosController.PROPIEDAD_ARTICULOS_TRAZABILIDAD_NO_INSERTADA_COLOR);
					if (propiedadTrazabilidadColorNoInsertada != null && StringUtils.isNotBlank(propiedadTrazabilidadColorNoInsertada.getValor())) {
						colorTrazabilidadNoInsertada = propiedadTrazabilidadColorNoInsertada.getValor();
					}

				}
				catch (DocumentoException e1) {
					log.error("initializeForm() - " + e1.getClass().getName() + " - " + e1.getLocalizedMessage(), e1);
				}
				catch (DocumentoNotFoundException e2) {
					log.error("initializeForm() - No se puede realizar la devolución: " + e2.getMessage());
					throw new Exception();
				}

				refrescarDatosPantalla();
			}
		}
		catch (CajaEstadoException | CajasServiceException e) {
			log.error("initializeForm() - Error de caja : " + e.getMessageI18N());
			throw new InitializeGuiException(e.getMessageI18N(), e);
		}
		catch (InitializeGuiException e) {
			throw e;
		}
		catch (Exception e) {
			log.error("initializeForm() - Error inesperado inicializando formulario. " + e.getMessage(), e);
			throw new InitializeGuiException("");
		}
	}

	/**
	 * Refresca los datos de la pantalla.
	 */
	@SuppressWarnings("unchecked")
	public void refrescarDatosPantalla() {

		log.debug("refrescarDatosPantalla() - Refrescando datos de pantalla...");

		tfCantidadIntro.setText(FormatUtil.getInstance().formateaNumero(BigDecimal.ONE, 0));
		tfCodigoIntro.setText("");

		lbCodCliente.setText(((TicketVenta<?, ?, ?>) ticketManager.getTicket()).getCliente().getCodCliente());
		lbDesCliente.setText(((TicketVenta<?, ?, ?>) ticketManager.getTicket()).getCliente().getDesCliente());
		lbTotal.setText(FormatUtil.getInstance().formateaImporte(ticketManager.getTicket().getTotales().getTotal()));

		LineaTicketGui selectedItem = getLineaSeleccionada();
		lineas.clear();

		for (LineaTicketAbstract lineaTicket : (List<LineaTicketAbstract>) (ticketManager.getTicket()).getLineas()) {
			lineas.add(new ByLLineaTicketAbonoGui(lineaTicket));
		}

		Collections.reverse(lineas);

		if (selectedItem != null) {
			tbLineas.getSelectionModel().select(lineas.indexOf(searchIdLinea(selectedItem.getIdLinea())));
		} else {
			tbLineas.getSelectionModel().selectFirst();
		}

		if (getLineaSeleccionada() == null) {
			tfCodigoIntro.requestFocus();
		}

		tbLineas.scrollTo(0);

		if (!esDevolucionSinTicket) {
			actualizarVisibilidadBtnEditar();
		}

		obtenerCantidadTotal();

		autosizeLabelTotalFont();

		if (ticketManager.getTicket().getCabecera().getCliente().getCodpais()
				.equalsIgnoreCase(ByLPagosController.EC_DEFAULT_PAIS)) {

			refrescarDatosFidelizado();

			if (ticketManager.getTicketOrigen() != null) {
				comprobarFidelizadoCargado();
			}
		}
	}

	public void comprobarFidelizadoCargado() {
		/*
		 * En el caso de que la devolucion traiga un fidelizado se precarga en pantalla
		 */

		if (ticketManager.getTicketOrigen().getCabecera().getDatosFidelizado() != null) {
			FidelizacionBean datosFidelizado = ticketManager.getTicketOrigen().getCabecera().getDatosFidelizado();
			ticketManager.getTicket().getCabecera().setDatosFidelizado(new ByLFidelizacionBean());
			new ConsultarFirmaTask(datosFidelizado.getIdFidelizado()).start();
		}
	}

	public void refrescarDatosFidelizado() {
		/* Comprobamos si es fidelizado o no. */
		if (ticketManager.getTicket().getCabecera().getDatosFidelizado() instanceof ByLFidelizacionBean
				|| ticketManager.getTicket().getCabecera().getDatosFidelizado() == null) {
			ByLFidelizacionBean datosFidelizado = (ByLFidelizacionBean) ticketManager.getTicket().getCabecera()
					.getDatosFidelizado();
			if (datosFidelizado == null || !StringUtils.isNotBlank(datosFidelizado.getNumTarjetaFidelizado())) {
				ocultarComponentesFidelizado(Boolean.FALSE);
			} else {
				limpiarDatosFidelizados();

				// Si el consentimientoRecibeNoti es null significa que no se han podido cargar
				// correctamente los datos
				// firma y de consentimientos
				if (((ByLFidelizacionBean) ticketManager.getTicket().getCabecera().getDatosFidelizado())
						.getConsentimientoRecibenoti() == null) {
					// Consultamos los consentimientos y la firma actualizados del fidelizado para
					// poderlo cargarlos
					// correctamente en el "semaforo"
					consultarFirmaConsentimientos(
							ticketManager.getTicket().getCabecera().getDatosFidelizado().getIdFidelizado());
				} else {
					cargarDatosFidelizado(datosFidelizado);
				}
			}
		}

	}

	public void limpiarDatosFidelizados() {
		lbNombreFideDato.setText("");
		lbMovilClienteDato.setText("");
		lbEmailClienteDato.setText("");
		activarFirma(null);
	}

	/**
	 * Realiza comprobaciones para saber que color poner al recuadro de la Firma.
	 * 
	 * @param firma : Objeto que contiene los datos de la Firma del Fidelizado.
	 */
	public void activarFirma(ByLFidelizacionBean datosFidelizado) {
		if (datosFidelizado != null) {
			/* Comprobamos que tiene hecho los dos consentimientos */
			Boolean usoDatos = Boolean.TRUE, recibeNoti = Boolean.TRUE, firma = Boolean.TRUE;
			if ("N".equals(datosFidelizado.getConsentimientoUsodatos())) {
				usoDatos = Boolean.FALSE;
			}
			if ("N".equals(datosFidelizado.getConsentimientoRecibenoti())) {
				recibeNoti = Boolean.FALSE;
			}
			if (datosFidelizado.getFirma() == null) {
				firma = Boolean.FALSE;
			}
			/* Si todo es correcto debemos ponerle el color verde */
			if (usoDatos && recibeNoti && firma) {
				btFirmaClienteDato.setStyle(
						"-fx-background-color: #" + ByLPaneDatosGeneralesController.CONSENTIMIENTO_COLOR_VERDE + ";");
			}
			/* Si ninguno es correcto, lo ponemos en rojo */
			else if (!usoDatos && !recibeNoti && !firma) {
				btFirmaClienteDato.setStyle(
						"-fx-background-color: #" + ByLPaneDatosGeneralesController.CONSENTIMIENTO_COLOR_ROJO + ";");
			}
			/* Para todas las demás combinaciones naranja */
			else {
				btFirmaClienteDato.setStyle(
						"-fx-background-color: #" + ByLPaneDatosGeneralesController.CONSENTIMIENTO_COLOR_NARANJA + ";");
			}
			/* Si el objeto no se ha encontrado, ponemos el color rojo */
		} else {
			btFirmaClienteDato.setStyle(
					"-fx-background-color: #" + ByLPaneDatosGeneralesController.CONSENTIMIENTO_COLOR_ROJO + ";");
		}
	}

	public void activarFirmaDevolucion(ByLFidelizacionBean datosFidelizado) {
		if (datosFidelizado != null) {
			/* Comprobamos que tiene hecho los dos consentimientos */
			Boolean usoDatos = Boolean.TRUE, recibeNoti = Boolean.TRUE, firma = Boolean.TRUE;
			if ("N".equals(datosFidelizado.getConsentimientoUsodatos())) {
				usoDatos = Boolean.FALSE;
			}
			if ("N".equals(datosFidelizado.getConsentimientoRecibenoti())) {
				recibeNoti = Boolean.FALSE;
			}
			if (datosFidelizado.getFirma() == null) {
				firma = Boolean.FALSE;
			}
			/* Si todo es correcto debemos ponerle el color verde */
			if (usoDatos && recibeNoti && firma) {
				btFirmaClienteDato.setStyle(
						"-fx-background-color: #" + ByLPaneDatosGeneralesController.CONSENTIMIENTO_COLOR_VERDE + ";");
			}
			/* Si ninguno es correcto, lo ponemos en rojo */
			else if (!usoDatos && !recibeNoti && !firma) {
				btFirmaClienteDato.setStyle(
						"-fx-background-color: #" + ByLPaneDatosGeneralesController.CONSENTIMIENTO_COLOR_ROJO + ";");
			}
			/* Para todas las demás combinaciones naranja */
			else {
				btFirmaClienteDato.setStyle(
						"-fx-background-color: #" + ByLPaneDatosGeneralesController.CONSENTIMIENTO_COLOR_NARANJA + ";");
			}
			/* Si el objeto no se ha encontrado, ponemos el color rojo */
		} else {
			btFirmaClienteDato.setStyle(
					"-fx-background-color: #" + ByLPaneDatosGeneralesController.CONSENTIMIENTO_COLOR_ROJO + ";");
		}
	}

	/**
	 * Inserta en el ticket manager una nueva linea de articulo, se ha añadido una
	 * comprobación de no aptos antes de añadir la linea.
	 * 
	 * @param codart    : Código del articulo seleccionado para devolver.
	 * @param desglose1 : Primer desglose del articulo seleccionado para devolver.
	 * @param desglose2 : Segundo desglose del articulo seleccionado para devolver.
	 * @param cantidad  : Cantidad del articulo seleccionado para devolver.
	 * @return LineaTicket : Si cumple correctamente los terminos devuelve una nueva
	 *         linea.
	 */
	protected LineaTicket nuevaLineaArticulo(String codart, String desglose1, String desglose2, BigDecimal cantidad)
			throws LineaTicketException {
		LineaTicket linea = ticketManager.nuevaLineaArticulo(codart, desglose1, desglose2, cantidad, null);
		if (comprobarNoAptos(linea.getCodArticulo())) {
			return linea;
		} else {
			ticketManager.eliminarLineaArticulo(linea.getIdLinea());
			return null;
		}
	}

	/**
	 * Añade un nuevo artículo.
	 */
	public void nuevoCodigoArticulo() {

		log.debug("nuevoCodigoArticulo() - Creando línea de artículo");

		/* Validamos los datos */
		if (!tfCodigoIntro.getText().trim().isEmpty()) {
			frValidacion.setCantidad(tfCantidadIntro.getText().trim());
			frValidacion.setCodArticulo(tfCodigoIntro.getText().trim().toUpperCase());
			tfCodigoIntro.clear();
			tfCantidadIntro.setText(FormatUtil.getInstance().formateaNumero(BigDecimal.ONE, 0));
			if (accionValidarFormulario()) {
				log.debug("nuevoCodigoArticulo()- Formulario validado");
				new ByLNuevoCodigoArticuloTask(frValidacion.getCodArticulo(), frValidacion.getCantidadAsBigDecimal())
						.start();
			}
		}
	}

	/**
	 * Se lanza al buscar por la barra superior. Se ha tenido que cambiar el nombre
	 * respecto al estandar, porque sino siempre hace el estandar.
	 */
	protected class ByLNuevoCodigoArticuloTask extends BackgroundTask<LineaTicket> {

		private String codArticulo;
		private final BigDecimal cantidad;
		private LineaTicket linea;

		public ByLNuevoCodigoArticuloTask(String codArticulo, BigDecimal cantidad) {
			this.codArticulo = codArticulo;
			this.cantidad = cantidad;
		}

		@Override
		protected LineaTicket call() throws Exception {
			linea = nuevaLineaArticulo(codArticulo, null, null, cantidad);
			/*
			 * Se necesita controlar que la linea no sea null porque ahora puede llegar como
			 * null.
			 */

			if (linea != null) {
				compruebaNotaInformativa(linea);
				linea.setAdmitePromociones(false);
				visor.escribir(linea.getDesArticulo(), FormatUtil.getInstance().formateaNumero(cantidad) + " X "
						+ FormatUtil.getInstance().formateaImporte(linea.getPrecioTotalConDto()));
			}
			return linea;
		}

		@Override
		protected void succeeded() {
			try {
				refrescarDatosPantalla();
				super.succeeded();

				boolean esTarjetaRegalo = ticketManager.comprobarTarjetaRegaloLineaYaInsertada(getValue());
				if (esTarjetaRegalo) {
					if (!consultarGiftCard()) {
						try {
							ticketManager.eliminarTicketCompleto();
							refrescarDatosPantalla();
						} catch (TicketsServiceException | PromocionesServiceException | DocumentoException e) {
							log.error("succeeded() - Excepción capturada" + e.getMessage(), e);
						}
					}
				}
				/*
				 * Se necesita controlar que la linea no sea null porque ahora puede llegar como
				 * null.
				 */
				if (linea != null) {
					asignarNumerosSerie(linea);
					tbLineas.getSelectionModel().select(0);
				}
			} catch (LineaInsertadaNoPermitidaException e) {
				log.error("Error en el proceso de tarjeta regalo", e);
				VentanaDialogoComponent.crearVentanaError(ByLIntroduccionArticulosController.this.getStage(),
						"Error en el proceso de tarjeta regalo.", e);
			}
		}

		@Override
		protected void failed() {
			super.failed();

			if (getCMZException() instanceof LineaDevolucionCambioException && tienePermisosCambioArticulo()) {
				introducirArticuloCambio(codArticulo, null, null, cantidad);
			} else if (getCMZException() instanceof LineaDevolucionNuevoArticuloException
					&& tienePermisosIntroducirNuevoArticulo()) {
				introducirNuevoArticulo(codArticulo, null, null, cantidad);
			} else {
				VentanaDialogoComponent.crearVentanaError(getCMZException().getMessageI18N(), getStage());
			}
		}
	}

	/**
	 * Abre la búsqueda de articulos y realiza acciones con los resultados
	 * seleccionados de la búsqueda. Se ha personalizado para poder controlar un
	 * null.
	 */
	public void abrirBusquedaArticulos() {

		log.debug("abrirBusquedaArticulos()");

		/*
		 * Validamos el campo cantidad antes de iniciar la búsqueda. Si el campo es
		 * vacío lo seteamos a 1 sin devolver error
		 */
		if (tfCantidadIntro.getText().trim().equals("")) {
			tfCantidadIntro.setText(FormatUtil.getInstance().formateaNumero(BigDecimal.ONE, 0));
		}
		/*
		 * Validamos que hay introducida una cantidad válida de artículos . Nota :
		 * También valida el campo código introducido. Podemos crear otro metodo de
		 * validación para que no lo haga.
		 */
		frValidacionBusqueda.setCantidad(tfCantidadIntro.getText());
		if (!accionValidarFormularioBusqueda()) {
			return; /*
					 * Si la validación de la cantidad no es satisfactoria, no realizamos la
					 * búsqueda.
					 */
		}

		datos = new HashMap<>();
		getDatos().put(BuscarArticulosController.PARAMETRO_ENTRADA_CLIENTE, ticketManager.getTicket().getCliente());
		getDatos().put(BuscarArticulosController.PARAMETRO_ENTRADA_CODTARIFA, ticketManager.getTarifaDefault());
		getDatos().put(BuscarArticulosController.PARAM_MODAL, Boolean.TRUE);
		getDatos().put(SeleccionUsuarioController.PARAMETRO_ES_STOCK, Boolean.FALSE);
		getApplication().getMainView().showModal(BuscarArticulosView.class, datos);
		initializeFocus();

		if (datos.containsKey(BuscarArticulosController.PARAMETRO_SALIDA_CODART)) {
			log.debug("realizarAccion() - ACCION BUSQUEDA ARTICULOS - Error registrando línea de ticket");
			String codArt = (String) datos.get(BuscarArticulosController.PARAMETRO_SALIDA_CODART);
			String desglose1 = (String) datos.get(BuscarArticulosController.PARAMETRO_SALIDA_DESGLOSE1);
			String desglose2 = (String) datos.get(BuscarArticulosController.PARAMETRO_SALIDA_DESGLOSE2);

			frValidacionBusqueda.setCantidad(tfCantidadIntro.getText());
			try {

				if (accionValidarFormularioBusqueda()) {
					LineaTicket lineaTicket = nuevaLineaArticulo(codArt, desglose1, desglose2,
							frValidacionBusqueda.getCantidadAsBigDecimal());
					/*
					 * Se necesita controlar que la linea no sea null porque ahora puede llegar como
					 * null.
					 */
					if (lineaTicket != null) {
						ticketManager.getTicket().getTotales().recalcular();
						refrescarDatosPantalla();
						visor.modoVenta(visorConverter.convert((TicketVentaAbono) ticketManager.getTicket()));

						asignarNumerosSerie(lineaTicket);

						visor.escribir(lineaTicket.getDesArticulo(), lineaTicket.getCantidadAsString() + " X "
								+ FormatUtil.getInstance().formateaImporte(lineaTicket.getPrecioTotalConDto()));
					}
				}

			} catch (LineaTicketException ex) {
				if (ex instanceof LineaDevolucionCambioException && tienePermisosCambioArticulo()) {
					introducirArticuloCambio(codArt, desglose1, desglose2,
							frValidacionBusqueda.getCantidadAsBigDecimal());
				} else if (ex instanceof LineaDevolucionNuevoArticuloException
						&& tienePermisosIntroducirNuevoArticulo()) {
					introducirNuevoArticulo(codArt, desglose1, desglose2,
							frValidacionBusqueda.getCantidadAsBigDecimal());
				} else {
					log.error("realizarAccion() - ACCION BUSQUEDA ARTICULOS - Error registrando línea de ticket");
					VentanaDialogoComponent.crearVentanaError(ex.getLocalizedMessage(), this.getScene().getWindow());
				}
			}
		}
	}

	/**
	 * Comprueba que los articulos que se intentan añadir no sean no aptos, y en
	 * caso de ser, se deberá comprobar los permisos.
	 * 
	 * @param todos : Indica si es añadir todos.
	 * @return
	 */
	public Boolean comprobarNoAptos(String codart) {

		Boolean continuarDevolucion = true;
		List<String> listadoNoAptos;
		/* Rescatamos el listado de no aptos. */
		if (AppConfig.menu.equals(ByLDevolucionesController.MENU_POS_PROFESIONAL)) {
			listadoNoAptos = ((ByLVentaProfesionalManager) ticketManager).getListadoNoAptos();
		} else {
			listadoNoAptos = ((ByLTicketManager) ticketManager).getListadoNoAptos();
		}

		ArticuloBean articuloComprobar;
		try {

			articuloComprobar = articulosService.consultarArticulo(codart);
			if (listadoNoAptos != null) {
				for (String apto : listadoNoAptos) {
					if (articuloComprobar.getCodArticulo().equals(apto)) {
						if (!comprobarPermisoNoAptos()) {
							continuarDevolucion = false;
							VentanaDialogoComponent.crearVentanaAviso(
									I18N.getTexto("El artículo " + articuloComprobar.getCodArticulo() + " , "
											+ articuloComprobar.getDesArticulo()
											+ " no es apto para devolución. Avise al responsable de tienda."),
									getStage());
							break;
						}
					}
				}
			}

		} catch (ArticuloNotFoundException e) {
			e.printStackTrace();
		} catch (ArticulosServiceException e) {
			e.printStackTrace();
		}

		return continuarDevolucion;
	}

	/**
	 * Comprueba que se tiene el permiso de "Devolver No Aptos", para las
	 * operaciones que son 0 de precio final.
	 * 
	 * @return permisos : Boolean que indica "true" si tiene permisos, y "false" en
	 *         caso de no tener permisos.
	 */
	public Boolean comprobarPermisoNoAptos() {

		boolean permisos = true;

		try {
			compruebaPermisos(PERMISO_DEVOLVER_NO_APTOS);
		} catch (SinPermisosException e) {
			permisos = false;
		}

		return permisos;

	}

	@Override
	protected List<ConfiguracionBotonBean> cargarAccionesTabla() {
		List<ConfiguracionBotonBean> listaAcciones = new ArrayList<>();
		listaAcciones = super.cargarAccionesTabla();
		listaAcciones.add(new ConfiguracionBotonBean("iconos/row_neg.png", null, null, "ACCION_TABLA_NEGAR_REGISTRO",
				"REALIZAR_ACCION")); // "Num Pad -"
		listaAcciones.add(new ConfiguracionBotonBean("iconos/about_pressed.png", null, null,
				"ACCION_TABLA_AGREGAR_NOTA_INFORMATIVA", "REALIZAR_ACCION")); // Botón INFO tal como el de Ventas GAP
																				// 1585
		listaAcciones.add(new ConfiguracionBotonBean("iconos/cambiar_cajero_linea.png", null, null,
				"ACCION_TABLA_CAMBIAR_VENDEDOR", "REALIZAR_ACCION")); // Cambiar vendedor

		return listaAcciones;

	}

	@Override
	public void realizarAccion(BotonBotoneraComponent botonAccionado) {
		log.debug("realizarAccion() - Realizando la acción : " + botonAccionado.getClave() + " de tipo : "
				+ botonAccionado.getTipo());
		switch (botonAccionado.getClave()) {
		// BOTONERA TABLA TICKET DEVOLUCIÓN
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
		case "ACCION_TABLA_BORRAR_REGISTRO":
			accionTablaEliminarRegistro();
			break;
		case "ACCION_TABLA_EDITAR_REGISTRO":
			accionTablaEditarRegistro();
			break;
		case "CANCELAR_VENTA":
			accionCancelarDevolucion();
			break;
		case "ACCION_TABLA_NEGAR_REGISTRO":
			accionNegarRegistroTabla();
			break;
		case "ACCION_TABLA_AGREGAR_NOTA_INFORMATIVA": // Inicio GAP #1585
			accionTablaAgregarNotaInformativa();
			break; // Fin GAP 1585
		case "ACCION_TABLA_CAMBIAR_VENDEDOR":
			accionTablaCambiarVendedor();
			break;
		default:
			log.error("No se ha especificado acción en pantalla para la operación :" + botonAccionado.getClave());
			break;
		}
	}

	@FXML
	protected void accionTablaCambiarVendedor() {

		try {
			log.debug("accionTablaCambiarVendedor() - Acción ejecutada");
			super.compruebaPermisos(PERMISO_CAMBIAR_VENDEDOR_LINEA);
			if (tbLineas.getItems() != null && getLineaSeleccionada() != null) {
				LineaTicketGui selectedItem = getLineaSeleccionada();
				if (selectedItem.isCupon()) {
					VentanaDialogoComponent.crearVentanaAviso(
							I18N.getTexto("La línea seleccionada no se puede modificar."), this.getStage());
				} else {
					int linea = selectedItem.getIdLinea();
					if (linea > 0) {
						ILineaTicket lineaTicket = ticketManager.getTicket().getLinea(linea);
						if (lineaTicket != null) {
							if (lineaTicket.isEditable()) {
								/* Creamos la ventana de ediciÃ³n de artÃ­culos. */
								HashMap<String, Object> parametrosEdicionArticulo = new HashMap<>();
								parametrosEdicionArticulo.put(EdicionArticuloController.CLAVE_PARAMETRO_ARTICULO,
										lineaTicket);
								parametrosEdicionArticulo.put(FacturacionArticulosController.TICKET_KEY, ticketManager);
								abrirVentanaCambiarVendedor(parametrosEdicionArticulo);

								guardarCopiaSeguridad();
								escribirLineaEnVisor((LineaTicket) lineaTicket);
								refrescarDatosPantalla();
								visor.modoVenta(visorConverter.convert(((TicketVentaAbono) ticketManager.getTicket())));
							} else {
								VentanaDialogoComponent.crearVentanaAviso(
										I18N.getTexto("La línea seleccionada no se puede modificar."), this.getStage());
							}
						}
					}
				}
			}

		} catch (SinPermisosException ex) {
			log.debug("accionTablaCambiarVendedor() - El usuario no tiene permisos para cambiar de vendedor");
			VentanaDialogoComponent.crearVentanaAviso(
					I18N.getTexto("El usuario no tiene permisos para cambiar de vendedor"), getStage());
		}
	}

	protected void abrirVentanaCambiarVendedor(HashMap<String, Object> parametrosEdicionArticulo) {
		getApplication().getMainView().showModalCentered(ByLCambiarVendedorView.class, parametrosEdicionArticulo,
				this.getStage());
	}

	protected void accionNegarRegistroTabla() {
		try {
			log.debug("accionNegarRegistroTabla() - ");
			super.compruebaPermisos(PERMISO_DEVOLUCIONES);
			LineaTicketGui lineaSeleccionada = getLineaSeleccionada();
			if (lineaSeleccionada != null) {
				if (ticketManager.getTicket().getCabecera().getDatosDocOrigen() == null && esDevolucionSinTicket) {
					if (lineaSeleccionada.isCupon()) {
						VentanaDialogoComponent.crearVentanaAviso(
								I18N.getTexto("La línea seleccionada no se puede modificar."), this.getStage());
					} else {
						int idLinea = lineaSeleccionada.getIdLinea();
						ILineaTicket linea = ticketManager.getTicket().getLinea(idLinea);
						if (linea.isEditable()) {
							ticketManager.negarLineaArticulo(idLinea);
							escribirLineaEnVisor((LineaTicket) linea);
							if (!esDevolucionSinTicket) {
								guardarCopiaSeguridad();
							}
							refrescarDatosPantalla();
							visor.modoVenta(visorConverter.convert(((TicketVentaAbono) ticketManager.getTicket())));
						} else {
							VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("La línea no se puede modificar."),
									this.getStage());
						}
					}
				}
			}
		} catch (LineaTicketException e) {
			log.error("accionNegarRegistroTabla() - Error recalculando importe de línea: " + e.getMessage(), e);
			VentanaDialogoComponent.crearVentanaError(getStage(), e.getMessageI18N(), e);
		} catch (SinPermisosException ex) {
			log.debug("accionNegarRegistroTabla() - El usuario no tiene permisos para realizar devolución");
			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("No tiene permisos para realizar una devolución"),
					getStage());
		}
	}

	protected void escribirLineaEnVisor(LineaTicket linea) {
		String desc = linea.getArticulo().getDesArticulo();
		visor.escribir(desc, linea.getCantidadAsString() + " X "
				+ FormatUtil.getInstance().formateaImporte(linea.getPrecioTotalConDto()));
	}

	protected void guardarCopiaSeguridad() {
		this.ticketManager.guardarCopiaSeguridadTicket();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void verDocumentoOrigen() {
		if (!esDevolucionSinTicket) {
			getView().getController().getDatos().put(TICKET_KEY, ticketManager);
			getApplication().getMainView().showModal(DevolucionArticulosView.class,
					getView().getController().getDatos());

			List<LineaTicket> listaLineas = ticketManager.getTicket().getLineas();
			if (listaLineas != null && !listaLineas.isEmpty()) {
				for (LineaTicket lineaTicket : listaLineas) {
					consultarTrazabilidad(lineaTicket);
				}
			}

			refrescarDatosPantalla();
			visor.modoVenta(visorConverter.convert((TicketVentaAbono) ticketManager.getTicket()));
			escribirUltimaLineaEnVisor();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected boolean validarTicket() {
		boolean valido = true;
		boolean lineasNegativasOrigen = false;

		for (LineaTicket linea : (List<LineaTicket>) ticketManager.getTicket().getLineas()) {
			ArticuloBean articulo = linea.getArticulo();

			// Se comprueba si alguna de als lineas del ticket pertenece al ticket origen
			if (linea.getLineaDocumentoOrigen() != null) {
				lineasNegativasOrigen = true;
			}

			// Si no es válido no hacemos las comprobaciones para los números de serie
			// Si quedan números de serie por asignar, mostramos la pantalla de números de
			// serie
			if (quedanNumerosSeriePorAsignar(linea, articulo.getNumerosSerie())) {
				VentanaDialogoComponent.crearVentanaAviso(
						I18N.getTexto("Quedan números de serie por asignar. Por favor, asígnelos antes de seguir."),
						getStage());

				asignarNumerosSerie(linea);

				/*
				 * if (quedanNumerosSeriePorAsignar(linea,articulo.getNumerosSerie())) { return
				 * false; }
				 */
				valido = false;
			}
		}
		if (!lineasNegativasOrigen && !esDevolucionSinTicket) {
			VentanaDialogoComponent.crearVentanaAviso(
					I18N.getTexto("Debe añadir alguna linea del documento origen para realizar la devolución."),
					getStage());
		}

		if (esDevolucionSinTicket) {
			getDatos().put(ByLDevolucionesController.DEVOLUCION_SIN_TICKET, Boolean.TRUE);
		}

		return valido && (lineasNegativasOrigen || esDevolucionSinTicket);
	}

	@Override
	public void abrirPagos() {
		log.trace("abrirPagos()");
		if (!ticketManager.isTicketVacio()) { // Si el ticket no es vacío se puede aparcar
			if (validarTicket()) {
				log.debug("abrirPagos() - El ticket tiene líneas");
				getDatos().put(TICKET_KEY, ticketManager);
				getDatos().put(PagosController.TIPO_DOC_INICIAL, tipoDocumentoInicial);
				getDatos().put(RequestFechaOrigenController.FECHA_RELLENADA, fechaOrigenSinTicketReferenciar);

				try {
					if (esDevolucionSinTicket && sesion.getAplicacion().getTienda().getCliente().getCodpais().equals(CODIGO_PAIS_ECUADOR)) {
						int idGrupoImpuesto = grupoImpuestosService.consultarGrupoImpuestosFecha(fechaOrigenSinTicketReferenciar).getIdGrupoImpuestos();
						ticketManager.getTicket().getCabecera().getCliente().setIdGrupoImpuestos(idGrupoImpuesto);
					}
				}
				catch (GrupoImpuestosServiceException | GrupoImpuestosNotFoundException e) {
				}
				
				getApplication().getMainView().showModal(PagosView.class, getDatos());
				try {
					getView().resetSubViews();
					if (getDatos().containsKey(PagosController.ACCION_CANCELAR)) {
						initializeForm();
					} else {
						try {
							ticketManager.nuevoTicket();
							ticketManager.setEsDevolucion(true);
							if (!esDevolucionSinTicket) {
								ticketManager.setDocumentoActivo(documentos.getDocumentoAbono(
										ticketManager.getTicketOrigen().getCabecera().getCodTipoDocumento()));
							}
						} catch (Exception e) {
							log.error(
									"abrirPagos() - Ha habido un error al inicializar un nuevo ticket para borrar la copia de seguridad: "
											+ e.getMessage(),
									e);
						}
						getView().getParentView().loadAndInitialize();
					}
				} catch (InitializeGuiException e) {
					VentanaDialogoComponent.crearVentanaError(getStage(), e);
				}
			}
		} else {
			log.warn("abrirPagos() - Ticket vacio");
			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("El ticket no contiene líneas de artículo."),
					this.getStage());
		}
	}

	@Override
	protected boolean tienePermisosCambioArticulo() {
		boolean tienePermisosCambio = false;
		try {
			compruebaPermisos(PERMISO_CAMBIAR_ARTICULOS);
			tienePermisosCambio = true;
		} catch (SinPermisosException e) {
		}
		return tienePermisosCambio;
	}

	@Override
	protected boolean tienePermisosIntroducirNuevoArticulo() {
		boolean tienePermisosCambio = false;
		try {
			compruebaPermisos(PERMISO_INTRODUCIR_ARTICULOS_NUEVOS);
			tienePermisosCambio = true;
		} catch (SinPermisosException e) {
		}
		return tienePermisosCambio;
	}

	@Override
	protected void accionTablaEditarRegistro() {
		try {
			log.debug("accionTablaEditarRegistro() - Acción ejecutada");
			super.compruebaPermisos(PERMISO_MODIFICAR_LINEA);
			if (tbLineas.getItems() != null && getLineaSeleccionada() != null) {
				int linea = getLineaSeleccionada().getIdLinea();
				if (linea > 0) {
					ILineaTicket lineaTicket = ticketManager.getTicket().getLinea(linea);

					if (BigDecimalUtil.isMayorACero(lineaTicket.getCantidad())
							|| cumpleCondicionesParaEditarLineaNegativa()) {

						BigDecimal cantidadInicial = lineaTicket.getCantidad();

						// Creamos la ventana de edición de artículos
						HashMap<String, Object> parametrosEdicionArticulo = new HashMap<>();
						parametrosEdicionArticulo.put(EdicionArticuloController.CLAVE_PARAMETRO_ARTICULO, lineaTicket);
						parametrosEdicionArticulo.put(EdicionArticuloController.CLAVE_APLICAR_PROMOCIONES, false);
						parametrosEdicionArticulo.put(FacturacionArticulosController.TICKET_KEY, ticketManager);
						getApplication().getMainView().showModalCentered(EdicionArticuloView.class,
								parametrosEdicionArticulo, this.getStage());

						if (BigDecimalUtil.isMayorACero(cantidadInicial)) {
							((LineaTicket) lineaTicket).setCantidad(lineaTicket.getCantidad().abs());
						}

						((LineaTicket) lineaTicket).recalcularImporteFinal();
						ticketManager.recalcularConPromociones();

						refrescarDatosPantalla();
					}
				}
			}
		} catch (SinPermisosException ex) {
			log.debug("accionTablaEditarRegistro() - El usuario no tiene permisos para modificar línea");
			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("No tiene permisos para modificar una línea."),
					getStage());
		}

	}

	private Boolean cumpleCondicionesParaEditarLineaNegativa() {
		log.debug("cumpleCondicionesParaEditarLineaNegativa()");

		if (!esDevolucionSinTicket) {
			log.debug(
					"accionTablaEditarRegistro() - El usuario no puede modificar una línea en una devolución con ticket original.");
			VentanaDialogoComponent.crearVentanaAviso(
					I18N.getTexto("No es posible modificar una línea en una devolución con ticket original."),
					getStage());
			return false;
		} else {
			try {
				compruebaPermisos(PERMISO_MODIFICAR_LINEA_NEGATIVA);
				return true;
			} catch (SinPermisosException e) {
				log.debug("accionTablaEditarRegistro() - El usuario no puede tiene permisos para modificar una línea.");
				VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("No tiene permisos para modificar una línea."),
						getStage());
				return false;
			}
		}
	}

	protected boolean tienePermisosModificarLineaNegativa() {
		boolean tienePermisosCambio = false;
		try {
			compruebaPermisos(PERMISO_MODIFICAR_LINEA_NEGATIVA);
			tienePermisosCambio = true;
		} catch (SinPermisosException e) {
		}
		return tienePermisosCambio;
	}

//	@Override
//	public void comprobarPermisosUI() {
//		super.comprobarPermisosUI();
//		botonera.comprobarPermisosOperaciones();
//		try {
//			super.compruebaPermisos(PERMISO_CAMBIAR_VENDEDOR_LINEA);
//			botoneraAccionesTabla.setAccionDisabled("ACCION_TABLA_CAMBIAR_VENDEDOR", false);
//		}
//		catch (SinPermisosException ex) {
//			botoneraAccionesTabla.setAccionDisabled("ACCION_TABLA_CAMBIAR_VENDEDOR", true);
//		}
//	}

	// Inicio GAP #1585
	@FXML
	public void accionTablaAgregarNotaInformativa() {
		try {
			log.debug("accionTablaAgregarNotaInformativa() - Acción ejecutada");
			super.compruebaPermisos(ByLFacturacionArticulosController.PERMISO_AGREGAR_NOTA_INFORMATIVA);
			if (tbLineas.getItems() != null && getLineaSeleccionada() != null) {
				LineaTicketGui selectedItem = getLineaSeleccionada();
				if (selectedItem.getCantidadProperty().getValue().intValue() < 0) {
					log.debug("accionTablaAgregarNotaInformativa() - Cantidad negativa");
				} else {
					int linea = selectedItem.getIdLinea();
					if (linea > 0) {
						ILineaTicket lineaTicket = ticketManager.getTicket().getLinea(linea);
						if (lineaTicket != null) {
							if (lineaTicket.isEditable()) {
								/* Creamos la ventana de inserción de nota informativa */
								HashMap<String, Object> parametrosEdicionArticulo = new HashMap<>();
								parametrosEdicionArticulo.put(
										ByLAgregarNotaInformativaController.CLAVE_PARAMETRO_LINEA_TICKET, lineaTicket);
								abrirVentanaAgregarNotaInformativa(parametrosEdicionArticulo);
							} else {
								VentanaDialogoComponent.crearVentanaAviso(
										I18N.getTexto("La línea seleccionada no se puede modificar."), this.getStage());
							}
						}
					}

				}
			}
		} catch (SinPermisosException ex) {
			log.debug("accionTablaAgregarNotaInformativa() - El usuario no tiene permisos para agregar nota +INFO");
			VentanaDialogoComponent.crearVentanaAviso(
					I18N.getTexto("El usuario no tiene permisos para agregar nota +INFO"), getStage());
		}
	}

	@SuppressWarnings("unused")
	protected void abrirVentanaAgregarNotaInformativa(HashMap<String, Object> parametrosEdicionArticulo) {
		getApplication().getMainView().showModalCentered(ByLAgregarNotaInformativaView.class, parametrosEdicionArticulo,
				this.getStage());
		AvisoInformativoBean avisoInformativo = (AvisoInformativoBean) parametrosEdicionArticulo
				.get(ByLAgregarNotaInformativaController.AVISO_INFORMATIVO_BEAN);
	}
	// Fin GAP #1585

	private void compruebaNotaInformativa(LineaTicket linea) {
		log.debug("compruebaNotaInformativa() - Comprobando si la linea tiene nota informativa");

		if ((linea instanceof ByLLineaTicket && ((ByLLineaTicket) linea).getNotaInformativa() != null)
				|| (linea instanceof ByLLineaTicketProfesional
						&& ((ByLLineaTicketProfesional) linea).getNotaInformativa() != null)) {
			VentanaDialogoComponent.crearVentanaAviso(
					I18N.getTexto(
							"EL ARTÍCULO SELECCIONADO SE HA VENDIDO CON +INFO. REVISE EL TICKET POR FAVOR. GRACIAS"),
					getStage());
		}
	}

	@Override
	protected void asignarNumerosSerie(LineaTicket linea) {
		super.asignarNumerosSerie(linea);

		consultarTrazabilidad(linea);
		refrescarDatosPantalla();
	}

	protected void consultarTrazabilidad(LineaTicket linea) {
		PropiedadDocumentoBean propiedadTrazabilidadEtiqueta;
		try {
			propiedadTrazabilidadEtiqueta = documentos
					.getDocumento(ticketManager.getTicket().getCabecera().getTipoDocumento()).getPropiedades()
					.get(PROPIEDAD_ARTICULOS_TRAZABILIDAD_ETIQUETA);
			if (propiedadTrazabilidadEtiqueta != null) {
				String valor = propiedadTrazabilidadEtiqueta.getValor();
				if (StringUtils.isNotBlank(valor)) {
					EtiquetaArticuloKey example = new EtiquetaArticuloKey();
					example.setUidActividad(sesion.getAplicacion().getUidActividad());
					example.setIdClase("D_ARTICULOS_TBL.CODART");
					example.setIdObjeto(linea.getCodArticulo());
					List<EtiquetaArticuloBean> listaEtiquetas = etiquetaArticuloMapper.selectWithDesc(example);

					Boolean tieneTrazabilidad = Boolean.FALSE;
					if (listaEtiquetas != null && !listaEtiquetas.isEmpty()) {
						for (EtiquetaArticuloBean etiquetaArticuloBean : listaEtiquetas) {
							if (etiquetaArticuloBean.getEtiqueta().equals(valor)) {
								tieneTrazabilidad = Boolean.TRUE;
							}
						}

						if (tieneTrazabilidad) {
							LineaTicketTrazabilidad lineaTrazabilidad = new LineaTicketTrazabilidad();
							lineaTrazabilidad.setTieneTrazabilidad(true);
							((ByLLineaTicket) linea).setTrazabilidad(lineaTrazabilidad);
							asignarCadenasTrazabilidad(linea);
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("consultarTrazabilidad() - " + e.getClass().getName() + " - " + e.getLocalizedMessage(), e);
			VentanaDialogoComponent.crearVentanaError(getStage(),
					I18N.getTexto("Ha ocurrido un error al obtener la propiedad del documento activo"), e);
		}
	}

	@SuppressWarnings("unchecked")
	protected void asignarCadenasTrazabilidad(LineaTicket linea) {
		if (linea != null) {
			List<String> cadenasTrazabilidadOrigen = null;
			LineaTicketTrazabilidad lineaTicketTrazabilidad = ((ByLLineaTicket) ticketManager.getTicketOrigen()
					.getLinea(linea.getLineaDocumentoOrigen())).getTrazabilidad();
			if (lineaTicketTrazabilidad != null && lineaTicketTrazabilidad.getCadenasTrazabilidad() != null
					&& !lineaTicketTrazabilidad.getCadenasTrazabilidad().isEmpty()) {
				cadenasTrazabilidadOrigen = ((ByLLineaTicket) ticketManager.getTicketOrigen()
						.getLinea(linea.getLineaDocumentoOrigen())).getTrazabilidad().getCadenasTrazabilidad();
			}

			getDatos().put(TrazabilidadController.PARAMETRO_CADENAS_TRAZABILIDAD_DOCUMENTO_ORIGEN,
					cadenasTrazabilidadOrigen);
			getDatos().put(TrazabilidadController.PARAMETRO_LINEA_DOCUMENTO_ACTIVO, linea);
			getApplication().getMainView().showModalCentered(TrazabilidadView.class, getDatos(), getStage());
			List<String> cadenasTrazabilidad = (List<String>) getDatos()
					.get(TrazabilidadController.PARAMETRO_LISTA_CADENAS_TRAZABILIDAD_ASIGNADOS);

			if (cadenasTrazabilidad != null && !cadenasTrazabilidad.isEmpty()) {
				((ByLLineaTicket) linea).getTrazabilidad().setCadenasTrazabilidad(cadenasTrazabilidad);
				ticketManager.guardarCopiaSeguridadTicket();
			}
		}
	}

	@Override
	protected void introducirLineaPositiva(String codArticulo, String desglose1, String desglose2, BigDecimal cantidad,
			boolean esCambio) {
		try {
			cambiarTipoDocumento();
			LineaTicket linea = ticketManager.nuevaLineaArticulo(codArticulo, desglose1, desglose2, cantidad, false,
					null, true);
			ticketManager.recalcularConPromociones();

			consultarTrazabilidad(linea);

			refrescarDatosPantalla();
		} catch (Exception e) {
			log.error("introducirArticuloCambio() - Ha habido un error al introducir la línea: " + e.getMessage(), e);
			VentanaDialogoComponent.crearVentanaError(getStage(),
					I18N.getTexto("Ha habido un error al introducir la línea"), e);
		}
	}

	protected void autosizeLabelTotalFont() {

		try {
			lbTotal.setStyle("");
			String text = lbTotal.getText();
			if (text.length() >= 15) {
				lbTotal.setStyle("-fx-font-size: 25px;");
			} else if (text.length() >= 12) {
				lbTotal.setStyle("-fx-font-size: 30px;");
			} else if (text.length() >= 10) {
				lbTotal.setStyle("-fx-font-size: 40px;");
			} else if (text.length() >= 8) {
				lbTotal.getStyleClass().add("total-reduced");
			}
		} catch (Exception e) {
			log.debug("autosizeLabelTotalFont() - " + e.getClass().getName() + " - " + e.getLocalizedMessage(), e);
		}

	}

	public void fidelizacion() {
		log.debug("fidelizacion()");
		tratarfidelizadoEC();
	}

	protected void tratarfidelizadoEC() {
		log.debug("tratarfidelizadoEC()");
		if (ticketManager.getTicketOrigen() == null
				|| ticketManager.getTicketOrigen().getCabecera().getDatosFidelizado() == null) {
			Dispositivos.getInstance().getFidelizacion().pedirTarjetaFidelizado(getStage(),
					new DispositivoCallback<FidelizacionBean>() {

						@Override
						public void onSuccess(FidelizacionBean tarjeta) {
							if (tarjeta.getIdFidelizado() != null) {
								if (tarjeta.getIdFidelizado() == 0L) {
									String mensajeConfirmacion = "No se ha encontrado ningún Fidelizado con los datos introducidos"
											+ "\n¿Desea realizar el Alta del Cliente?";
									if (VentanaDialogoComponent
											.crearVentanaConfirmacion(I18N.getTexto(mensajeConfirmacion), getStage())) {
										ByLBusquedaFidelizadoController busqueda = SpringContext
												.getBean(ByLBusquedaFidelizadoController.class);
										busqueda.accionAltaFidelizado();
									}
								} else {
									if (tarjeta.isBaja()) {
										VentanaDialogoComponent.crearVentanaError(
												I18N.getTexto("La tarjeta de fidelización {0} no está activa",
														tarjeta.getNumTarjetaFidelizado()),
												getStage());
										tarjeta = null;
										ticketManager.getTicket().getCabecera().setDatosFidelizado(tarjeta);

										ticketManager.recalcularConPromociones();
										guardarCopiaSeguridad();
										refrescarDatosPantalla();
									} else {
										/* Tarjeta válida */
										ticketManager.getTicket().getCabecera().setDatosFidelizado(tarjeta);
										/* Realizamos la consulta para traer la Firma */
										new ConsultarFirmaTask(tarjeta.getIdFidelizado()).start();
									}
								}
							} else {
								if (tarjeta.isBaja()) {
									VentanaDialogoComponent.crearVentanaError(
											I18N.getTexto("La tarjeta de fidelización {0} no está activa",
													tarjeta.getNumTarjetaFidelizado()),
											getStage());
									tarjeta = null;
									ticketManager.getTicket().getCabecera().setDatosFidelizado(tarjeta);

									ticketManager.recalcularConPromociones();
									guardarCopiaSeguridad();
									refrescarDatosPantalla();
								} else {
									/* Tarjeta válida */
									String fidelizadoGenerico = variablesServices
											.getVariableAsString(ByLFacturacionArticulosController.FIDELIZADO_GENERICO);
									if (fidelizadoGenerico.equals(tarjeta.getNumTarjetaFidelizado())) {
										ticketManager.getTicket().getCabecera().setDatosFidelizado(tarjeta);
										ticketManager.recalcularConPromociones();
										guardarCopiaSeguridad();
										refrescarDatosPantalla();
									} else {
										if (tarjeta.getIdFidelizado() != null) {
											/* Realizamos la consulta para traer la Firma */
											new ConsultarFirmaTask(tarjeta.getIdFidelizado()).start();
										} else {
											ticketManager.getTicket().getCabecera()
													.setDatosFidelizado(new ByLFidelizacionBean());
											ticketManager.recalcularConPromociones();
											guardarCopiaSeguridad();
											refrescarDatosPantalla();
										}
									}
								}
							}
						}

						@Override
						public void onFailure(Throwable e) {
							/*
							 * Los errores se muestran desde el código del dispositivo, quitamos los datos
							 * de fidelizado
							 */
							FidelizacionBean tarjeta = null;
							ticketManager.getTicket().getCabecera().setDatosFidelizado(tarjeta);
							guardarCopiaSeguridad();
							ticketManager.recalcularConPromociones();
							refrescarDatosPantalla();
						}
					});
		} else {
			VentanaDialogoComponent.crearVentanaAviso("No se puede cambiar el fidelizado en la devolucion", getStage());
		}
	}

	protected class ConsultarFirmaTask extends BackgroundTask<FidelizadoFirmaResponseGet> {

		private Long idFidelizado;

		public ConsultarFirmaTask(Long idFidelizadoDato) {
			super();
			idFidelizado = idFidelizadoDato;
		}

		@Override
		protected FidelizadoFirmaResponseGet call() throws Exception {
			String apiKey = variablesServices.getVariableAsString(VariablesServices.WEBSERVICES_APIKEY);
			String uidInstancia = sesion.getAplicacion().getUidInstancia();
			String uidActividad = sesion.getAplicacion().getUidActividad();
			return FidelizadoFirmaRest.consultar(idFidelizado, apiKey, uidInstancia, uidActividad);
		}

		@Override
		protected void succeeded() {
			FidelizadoFirmaResponseGet respuesta = getValue();
			/* Lo cargamos en el TicketAbono */
			((ByLTicketVentaAbono) ticketManager.getTicket()).setFirmaFidelizado(respuesta.getFirmaFidelizado());
			/* Cargamos los datos del Fidelziado */

			ByLFidelizacionBean fidelizado = (ByLFidelizacionBean) ticketManager.getTicket().getCabecera()
					.getDatosFidelizado();
			fidelizado.setConsentimientoUsodatos(respuesta.getFirmaFidelizado().getConsentimientoUsodatos());
			fidelizado.setConsentimientoRecibenoti(respuesta.getFirmaFidelizado().getConsentimientoRecibenoti());
			fidelizado.setFirma(respuesta.getFirmaFidelizado().getFirma());
			ticketManager.getTicket().getCabecera().setDatosFidelizado(fidelizado);

			ticketManager.recalcularConPromociones();
			guardarCopiaSeguridad();
			if (ticketManager.getTicketOrigen() != null
					&& ticketManager.getTicketOrigen().getCabecera().getDatosFidelizado() != null) {
				consultarFirmaConsentimientos(idFidelizado);
			} else {
				refrescarDatosPantalla();
			}

			super.succeeded();
		}

		@Override
		protected void failed() {
			super.failed();
			activarFirma(null);
			Exception e = (Exception) getException();
			log.error("ConsultarFirmaTask() - " + e.getMessage());
			VentanaDialogoComponent.crearVentanaError(getStage(), I18N.getTexto(e.getMessage()), e);
		}
	}

	/**
	 * Oculta los componentes de la pantalla referentes a un Fidelizado.
	 * 
	 * @param activar : Controla si activar o no los campos.
	 */
	public void ocultarComponentesFidelizado(Boolean activar) {
		lbNombreFide.setVisible(activar);
		lbNombreFideDato.setVisible(activar);
		lbEmailCliente.setVisible(activar);
		lbEmailClienteDato.setVisible(activar);
		lbMovilCliente.setVisible(activar);
		lbMovilClienteDato.setVisible(activar);
		lbFirmaCliente.setVisible(activar);
		btFirmaClienteDato.setVisible(activar);
	}

	/**
	 * Rellena los campos que tienen que ver con un Fidelizado con los datos de
	 * este.
	 * 
	 * @param datosFidelizado : Contiene los datos del Fidelizado.
	 */
	public void cargarDatosFidelizado(ByLFidelizacionBean datosFidelizado) {
		/*
		 * Lo primero es activar los componentes porque sino no sirve de nada cargarlos
		 */
		ocultarComponentesFidelizado(Boolean.TRUE);
		if (datosFidelizado != null) {
			if (datosFidelizado.getNombre() != null && !datosFidelizado.getNombre().isEmpty()) {
				if (datosFidelizado.getApellido() != null && !datosFidelizado.getApellido().isEmpty()) {
					lbNombreFideDato.setText(datosFidelizado.getNombre() + " " + datosFidelizado.getApellido());
				} else {
					lbNombreFideDato.setText(datosFidelizado.getNombre());
				}
			}
			if (datosFidelizado.getEmail() != null && !datosFidelizado.getEmail().isEmpty()) {
				lbEmailClienteDato.setText(datosFidelizado.getEmail());
			}
			if (datosFidelizado.getTelefono() != null && !datosFidelizado.getTelefono().isEmpty()) {
				lbMovilClienteDato.setText(datosFidelizado.getTelefono());
			}
			if (datosFidelizado.getConsentimientoUsodatos() != null
					&& datosFidelizado.getConsentimientoRecibenoti() != null && datosFidelizado.getFirma() != null) {
				activarFirma(datosFidelizado);
			} else {
				activarFirma(null);
			}
		}
	}

	public void cargarDatosFidelizadoDevolucion(ByLFidelizacionBean datosFidelizado, String consentimientoNoti,
			String consentimientoUsoDatos, byte[] firma) {
		/*
		 * Lo primero es activar los componentes porque sino no sirve de nada cargarlos
		 */
		ocultarComponentesFidelizado(Boolean.TRUE);
		if (datosFidelizado != null) {
			if (datosFidelizado.getNombre() != null && !datosFidelizado.getNombre().isEmpty()) {
				if (datosFidelizado.getApellido() != null && !datosFidelizado.getApellido().isEmpty()) {
					lbNombreFideDato.setText(datosFidelizado.getNombre() + " " + datosFidelizado.getApellido());
				} else {
					lbNombreFideDato.setText(datosFidelizado.getNombre());
				}
			}
			if (datosFidelizado.getEmail() != null && !datosFidelizado.getEmail().isEmpty()) {
				lbEmailClienteDato.setText(datosFidelizado.getEmail());
			}
			if (datosFidelizado.getTelefono() != null && !datosFidelizado.getTelefono().isEmpty()) {
				lbMovilClienteDato.setText(datosFidelizado.getTelefono());
			}
			if (consentimientoUsoDatos != null && consentimientoNoti != null && firma != null) {
				activarFirma(datosFidelizado);
			} else {
				activarFirma(null);
			}
		}
	}

	private void consultarFirmaConsentimientos(Long idFidelizado) {
		String apiKey = variablesServices.getVariableAsString(VariablesServices.WEBSERVICES_APIKEY);
		String uidActividad = sesion.getAplicacion().getUidActividad();
		ByLFidelizadoRequestRest fidelizado = new ByLFidelizadoRequestRest(apiKey, uidActividad, idFidelizado);

		new ConsultarFidelizadoPorIdTask(fidelizado).start();
	}

	public class ConsultarFidelizadoPorIdTask extends BackgroundTask<FidelizadosBean> {

		ByLFidelizadoRequestRest fidelizadoRequest;

		public ConsultarFidelizadoPorIdTask(ByLFidelizadoRequestRest fidelizadoRequest) {
			super();
			this.fidelizadoRequest = fidelizadoRequest;
		}

		@Override
		public FidelizadosBean call() throws Exception {
			return ByLFidelizadosRest.getFidelizadoPorId(fidelizadoRequest);
		}

		@Override
		public void succeeded() {
			FidelizadosBean fidelizadosBean = getValue();
			if (ticketManager.getTicketOrigen() != null
					&& ticketManager.getTicketOrigen().getCabecera().getDatosFidelizado() != null) {
				copiarDatosFidelizadoDevolucion(fidelizadosBean);
			}
			if (fidelizadosBean.getConsentimientosFirma() != null) {
				String consentimientoNoti = fidelizadosBean.getConsentimientosFirma().getConsentimientoRecibenoti();
				String consentimientoUsoDatos = fidelizadosBean.getConsentimientosFirma().getConsentimientoUsodatos();
				byte[] firma = fidelizadosBean.getConsentimientosFirma().getFirma();

				((ByLFidelizacionBean) ticketManager.getTicket().getCabecera().getDatosFidelizado())
						.setConsentimientoRecibenoti(consentimientoNoti);
				((ByLFidelizacionBean) ticketManager.getTicket().getCabecera().getDatosFidelizado())
						.setConsentimientoUsodatos(consentimientoUsoDatos);
				((ByLFidelizacionBean) ticketManager.getTicket().getCabecera().getDatosFidelizado()).setFirma(firma);

				cargarDatosFidelizado(
						(ByLFidelizacionBean) ticketManager.getTicket().getCabecera().getDatosFidelizado());
			}
			log.debug(
					"ConsultarFidelizadoPorIdTask/succeeded() - Finalizada la carga de los datos del Fidelizado desde la pantalla de ByLFacturacionArticulosController");
			super.succeeded();
		}

		private void copiarDatosFidelizadoDevolucion(FidelizadosBean fb) {
			ByLFidelizacionBean fidelizado = new ByLFidelizacionBean();
			fidelizado.setNombre(fb.getNombre());
			fidelizado.setApellido(fb.getApellidos());
			fidelizado.setIdFidelizado(fb.getIdFidelizado());
			fidelizado.setNumTarjetaFidelizado(fb.getNumeroTarjeta());
			String mail = "";
			String tel = "";
			for (TiposContactoFidelizadoBean contacto : fb.getTiposContacto()) {
				if (contacto.getCodTipoCon().equals("EMAIL")) {
					mail = contacto.getValor();
				}
				if (contacto.getCodTipoCon().equals("MOVIL")) {
					tel = contacto.getValor();
				}
				if (StringUtils.isBlank(tel) && contacto.getCodTipoCon().equals("TELEFONO1")) {
					tel = contacto.getValor();
				}
			}
			fidelizado.setEmail(mail);
			fidelizado.setTelefono(tel);
			fidelizado.setDocumento(fb.getDocumento());
			fidelizado.setProvincia(fb.getProvincia());
			fidelizado.setCodPais(fb.getCodPais());
			fidelizado.setDesPais(fb.getDesPais());
			fidelizado.setPoblacion(fb.getPoblacion());
			fidelizado.setCp(fb.getCp());
			fidelizado.setDomicilio(fb.getDomicilio());
			fidelizado.setLocalidad(fb.getLocalidad());
			ticketManager.getTicket().getCabecera().setDatosFidelizado(fidelizado);

		}

		@Override
		public void failed() {
			super.failed();
			Exception e = (Exception) getException();
			log.error("ConsultarFidelizadoPorIdTask/failed() - " + e.getMessage(), e);
			VentanaDialogoComponent.crearVentanaError(I18N.getTexto(e.getMessage()), getStage());
		}
	}

	@FXML
	public void accionVerFidelizado() {
		if (ticketManager.getTicket().getCabecera().getDatosFidelizado() != null) {
			String apiKey = variablesServices.getVariableAsString(VariablesServices.WEBSERVICES_APIKEY);
			String uidActividad = sesion.getAplicacion().getUidActividad();

			Long idFidelizado = ticketManager.getTicket().getCabecera().getDatosFidelizado().getIdFidelizado();
			if (idFidelizado != null) {
				Map<String, Object> datos = new HashMap<String, Object>();
				datos.put(CodigoTarjetaController.PARAMETRO_ID_FIDELIZADO, idFidelizado);
				datos.put(CodigoTarjetaController.PARAMETRO_MODO, "CONSULTA");
				getApplication().getMainView().showActionView(AppConfig.accionFidelizado,
						(HashMap<String, Object>) datos);
				return;
			}

			final String numeroTarjeta = ticketManager.getTicket().getCabecera().getDatosFidelizado()
					.getNumTarjetaFidelizado();
			ConsultarFidelizadoRequestRest consulta = new ConsultarFidelizadoRequestRest(apiKey, uidActividad,
					numeroTarjeta);

			ConsultarIdFidelizadoTask consultarFidelizadoTask = SpringContext.getBean(ConsultarIdFidelizadoTask.class,
					consulta, new RestBackgroundTask.FailedCallback<Long>() {

						@Override
						public void succeeded(Long result) {
							Map<String, Object> datos = new HashMap<String, Object>();
							datos.put(CodigoTarjetaController.PARAMETRO_ID_FIDELIZADO, result);
							datos.put(CodigoTarjetaController.PARAMETRO_MODO, "CONSULTA");
							datos.put(FacturacionArticulosController.PARAMETRO_NUMERO_TARJETA, numeroTarjeta);
							getApplication().getMainView().showActionView(AppConfig.accionFidelizado,
									(HashMap<String, Object>) datos);
						}

						@Override
						public void failed(Throwable throwable) {
						}
					}, getStage());

			consultarFidelizadoTask.start();
		}
	}

	/**
	 * Realiza la petición de la firma del Fidelizado al pulsar el botón de Firma
	 * que aparece en la pantalla al cargar un Fidelizado.
	 */
	@FXML
	public void realizarFirmaAxis() {
		log.debug("realizarFirmaAxis() - Iniciando la petición de la firma del cliente...");
		/* Realizamos la llamada al método de Axis que realiza la petición de firma */
		ByLFidelizacionBean datosFidelizado = (ByLFidelizacionBean) ticketManager.getTicket().getCabecera()
				.getDatosFidelizado();
		if (datosFidelizado != null) {
			/*
			 * CREAMOS EL DIÁLOGO DE CARGA PERSONALIZADO, ESTO DEBERÍA HACERSE UNA SOLA VEZ
			 * EN EL POSAPPLICATION
			 */
			ByLVentanaEspera.crearVentanaCargando(getStage());
			new RealizarFirmaTask(sesion.getAplicacion().getUidInstancia(), datosFidelizado.getIdFidelizado())
					.start(I18N.getTexto(
							"En espera de que el cliente pulse los consentimientos y la firma en el dispositivo"));
		} else {
			String mensajeAviso = "Los datos del Fidelizado no se han cargado correctamente";
			log.info("realizarFirmaAxis() - " + mensajeAviso);
			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto(mensajeAviso), getStage());
		}
		log.debug("realizarFirmaAxis() - Finalizada la petición de la firma del cliente");
	}

	protected class RealizarFirmaTask extends ByLBackgroundTask<Map<String, Object>> {

		private String uidInstancia;
		private Long idFidelizado;

		public RealizarFirmaTask(String uidInstanciaDato, Long idFidelizadoDato) {
			super();
			uidInstancia = uidInstanciaDato;
			idFidelizado = idFidelizadoDato;
		}

		@Override
		protected Map<String, Object> call() throws Exception {

			Map<String, Object> resultado = new HashMap<String, Object>();
			IFirma dispositivoFirma = ByLDispositivosFirma.getInstance().getDispositivoFirmaActual();

			resultado = dispositivoFirma.firmar();

			if (resultado == null) {
				throw new Exception(I18N.getTexto("No hay dispositivo configurado para la firma"));
			}

			return resultado;
		}

		@Override
		protected void succeeded() {
			Map<String, Object> respuesta = getValue();
			/*
			 * Después de rescatar la respuesta realizamos un guardado del objeto de la
			 * firma
			 */
			FidelizadoFirmaBean firma = new FidelizadoFirmaBean();
			firma.setUidInstancia(uidInstancia);
			firma.setIdFidelizado(idFidelizado);
			firma.setFecha(new Date());

			IFirma dispositivoFirma = ByLDispositivosFirma.getInstance().getDispositivoFirmaActual();
			if (dispositivoFirma instanceof AxisManager) {
				for (Map.Entry<String, Object> entry : respuesta.entrySet()) {
					if (AxisManager.RESPUESTA_CONSENTIMIENTO.equals(entry.getKey())) {
						if (((JC3ApiC3Rspn) entry.getValue()).getJson().contains("System Cancel")) {
							firma.setConsentimientoRecibenoti("N");
							firma.setConsentimientoUsodatos("N");
						} else {
							Gson gson = new Gson();
							AxisConsentimientoBean axis = gson.fromJson(((JC3ApiC3Rspn) entry.getValue()).getJson(),
									AxisConsentimientoBean.class);
							if (axis.getOperationResult().getMainCondition()) {
								firma.setConsentimientoRecibenoti("S");
							} else {
								firma.setConsentimientoRecibenoti("N");
							}
							if (axis.getOperationResult().getSecondaryCondition()) {
								firma.setConsentimientoUsodatos("S");
							} else {
								firma.setConsentimientoUsodatos("N");
							}
						}
					}
					if (AxisManager.IMAGEN_FIRMA.equals(entry.getKey())) {
						if (entry.getValue() != null) {
							firma.setFirma(((byte[]) entry.getValue()));
						} else {
							String mensajeAviso = "El Cliente ha cancelado la Firma en el Dispositivo";
							log.info("RealizarFirmaTask/succeeded() - " + mensajeAviso);
							VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto(mensajeAviso), getStage());
							break;
						}
					}
				}
			} else {
				for (Map.Entry<String, Object> entry : respuesta.entrySet()) {
					if (entry.getKey().equals(IFirma.RESPUESTA_CONSENTIMIENTO_NOTIFICACIONES)) {
						if ((Boolean) entry.getValue()) {
							firma.setConsentimientoRecibenoti("S");
						} else {
							firma.setConsentimientoRecibenoti("N");
						}
					}
					if (entry.getKey().equals(IFirma.RESPUESTA_CONSENTIMIENTO_USO_DATOS)) {
						if ((Boolean) entry.getValue()) {
							firma.setConsentimientoUsodatos("S");
						} else {
							firma.setConsentimientoUsodatos("N");
						}
					}

					if (entry.getKey().equals(IFirma.IMAGEN_FIRMA)) {
						if (entry.getValue() != null) {
							firma.setFirma((byte[]) entry.getValue());
						}
					}
				}
			}
			/* Primero cargamos en el Ticket la Firma */
			((ByLTicketVentaAbono) ticketManager.getTicket()).setFirmaFidelizado(firma);
			if (firma.getFirma() != null) {
				/* Comprobamos el color que debería tener el cuadrado */
				salvarFirma(firma);
			} else {
				refrescarDatosPantalla();
			}
			super.succeeded();
		}

		@Override
		protected void failed() {
			super.failed();
			Exception e = (Exception) getException();
			log.error("RealizarFirmaTask() - " + e.getMessage());
			VentanaDialogoComponent.crearVentanaError(I18N.getTexto(e.getMessage()), getStage());
			accionEditarFidelizado();

		}
	}

	protected void accionEditarFidelizado() {

		if (ticketManager.getTicket().getCabecera().getDatosFidelizado() != null) {

			Long idFidelizado = ticketManager.getTicket().getCabecera().getDatosFidelizado().getIdFidelizado();
			if (idFidelizado != null) {
				Map<String, Object> datos = new HashMap<String, Object>();
				datos.put(CodigoTarjetaController.PARAMETRO_ID_FIDELIZADO, idFidelizado);
				datos.put(CodigoTarjetaController.PARAMETRO_MODO, "EDICION");
				getApplication().getMainView().showActionView(AppConfig.accionFidelizado,
						(HashMap<String, Object>) datos);
				return;
			}
		}
	}

	/**
	 * Realiza la acción de Salvar de una firma.
	 * 
	 * @param firma : Objeto para salvar.
	 */
	public void salvarFirma(FidelizadoFirmaBean firma) {
		try {
			FidelizadoFirmaRequestRest request = new FidelizadoFirmaRequestRest(firma);
			request.setApiKey(variablesServices.getVariableAsString(VariablesServices.WEBSERVICES_APIKEY));
			request.setUidActividad(sesion.getAplicacion().getUidActividad());
			request.setUidInstancia(sesion.getAplicacion().getUidInstancia());
			request.setCodTienda(sesion.getAplicacion().getTienda().getCodAlmacen());

			/* Sino existe la creamos */
			FidelizadoFirmaRest.salvar(request);

			/* Mostramos un mensaje de que se ha completado bien el envío de la firma */
			String mensajeOk = "Se ha completado el proceso de Consentimientos y Firma correctamente";
			log.debug("salvarFirma() - " + mensajeOk);
			VentanaDialogoComponent.crearVentanaInfo(I18N.getTexto(mensajeOk), getStage());

			/*
			 * Pasamos los datos de la Firma y el Consentimiento al Fidelizado para poder
			 * cargar el cuadro de color.
			 */
			ByLFidelizacionBean fidelizadoDatos = (ByLFidelizacionBean) ticketManager.getTicket().getCabecera()
					.getDatosFidelizado();
			fidelizadoDatos.setConsentimientoUsodatos(firma.getConsentimientoUsodatos());
			fidelizadoDatos.setConsentimientoRecibenoti(firma.getConsentimientoRecibenoti());
			fidelizadoDatos.setFirma(firma.getFirma());
			ticketManager.getTicket().getCabecera().setDatosFidelizado(fidelizadoDatos);

			refrescarDatosPantalla();
		} catch (Exception e) {
			activarFirma(null);
			log.error("RealizarFirmaTask() - " + e.getMessage(), e);
			VentanaDialogoComponent.crearVentanaError(getStage(), I18N.getTexto(e.getMessage()), e);
		}
	}

}
