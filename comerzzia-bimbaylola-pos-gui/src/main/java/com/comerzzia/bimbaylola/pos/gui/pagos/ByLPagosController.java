package com.comerzzia.bimbaylola.pos.gui.pagos;

import java.io.File;
import java.io.FilenameFilter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.dispositivo.impresora.epsontm30.EpsonTM30;
import com.comerzzia.bimbaylola.pos.dispositivo.impresora.fiscal.polonia.PoloniaFiscalPrinter;
import com.comerzzia.bimbaylola.pos.dispositivo.impresora.spark130f.Spark130F;
import com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.adyen.AdyenDatosPeticionPagoTarjeta;
import com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.exception.TarjetaWarningException;
import com.comerzzia.bimbaylola.pos.gui.componentes.botonaccion.medioPago.ByLBotonBotoneraTextoComponent;
import com.comerzzia.bimbaylola.pos.gui.componentes.botonera.medioPago.ByLConfiguracionBotonMedioPagoOriginalBean;
import com.comerzzia.bimbaylola.pos.gui.componentes.dialogos.ByLVentanaDialogoComponent;
import com.comerzzia.bimbaylola.pos.gui.pagos.datoscliente.ByLCambiarDatosClienteCOView;
import com.comerzzia.bimbaylola.pos.gui.pagos.datoscliente.ByLCambiarDatosClienteECView;
import com.comerzzia.bimbaylola.pos.gui.pagos.datoscliente.ByLCambiarDatosClientePAView;
import com.comerzzia.bimbaylola.pos.gui.pagos.datoscliente.ByLCambiarDatosClientePTView;
import com.comerzzia.bimbaylola.pos.gui.ventas.tickets.ByLTicketRegalo;
import com.comerzzia.bimbaylola.pos.gui.pagos.email.ByLEmailView;
import com.comerzzia.bimbaylola.pos.gui.pagos.msi.MsiView;
import com.comerzzia.bimbaylola.pos.gui.pagos.standalone.ReferenciaStandaloneController;
import com.comerzzia.bimbaylola.pos.gui.pagos.standalone.ReferenciaStandaloneView;
import com.comerzzia.bimbaylola.pos.gui.ventas.devoluciones.ByLDevolucionesController;
import com.comerzzia.bimbaylola.pos.gui.ventas.devoluciones.fechaorigen.RequestFechaOrigenController;
import com.comerzzia.bimbaylola.pos.gui.ventas.profesional.venta.ByLVentaProfesionalController;
import com.comerzzia.bimbaylola.pos.gui.ventas.tickets.ByLTicketManager;
import com.comerzzia.bimbaylola.pos.gui.ventas.tickets.ByLVentaProfesionalManager;
import com.comerzzia.bimbaylola.pos.persistence.fidelizacion.ByLFidelizacionBean;
import com.comerzzia.bimbaylola.pos.persistence.giftcard.ByLGiftCardBean;
import com.comerzzia.bimbaylola.pos.persistence.mediosPago.MediosPagoBIN;
import com.comerzzia.bimbaylola.pos.persistence.ticket.articulos.agregarnotainformativa.AvisoInformativoBean;
import com.comerzzia.bimbaylola.pos.services.cajas.ByLCajasService;
import com.comerzzia.bimbaylola.pos.services.clientes.ByLClientesService;
import com.comerzzia.bimbaylola.pos.services.core.procesadorIdFiscal.ProcesadorIdFiscalCO;
import com.comerzzia.bimbaylola.pos.services.core.procesadorIdFiscal.ProcesadorIdFiscalException;
import com.comerzzia.bimbaylola.pos.services.core.procesadorIdFiscal.ProcesarDocumentoFiscalPAException;
import com.comerzzia.bimbaylola.pos.services.core.variables.ByLVariablesServices;
import com.comerzzia.bimbaylola.pos.services.epsontse.EposOutput;
import com.comerzzia.bimbaylola.pos.services.epsontse.EpsonTSEService;
import com.comerzzia.bimbaylola.pos.services.taxFree.TaxFreeLeerXML;
import com.comerzzia.bimbaylola.pos.services.taxFree.TaxFreeProcesador;
import com.comerzzia.bimbaylola.pos.services.ticket.ByLLineaTicket;
import com.comerzzia.bimbaylola.pos.services.ticket.ByLTicketVentaAbono;
import com.comerzzia.bimbaylola.pos.services.ticket.ByLTicketsService;
import com.comerzzia.bimbaylola.pos.services.ticket.articulos.agregarnotainformativa.ByLAgregarNotaInformativaService;
import com.comerzzia.bimbaylola.pos.services.ticket.cabecera.ByLCabeceraTicket;
import com.comerzzia.bimbaylola.pos.services.ticket.pagos.tarjeta.msi.MSIService;
import com.comerzzia.bimbaylola.pos.services.ticket.pagos.tarjeta.msi.MediosPagoBINException;
import com.comerzzia.bimbaylola.pos.services.ticket.pagos.tarjeta.msi.MediosPagoBINNotFoundException;
import com.comerzzia.bimbaylola.pos.services.ticket.profesional.ByLLineaTicketProfesional;
import com.comerzzia.bimbaylola.pos.services.vertex.LineaDetailVertex;
import com.comerzzia.bimbaylola.pos.services.vertex.VertexService;
import com.comerzzia.core.util.base.Estado;
import com.comerzzia.core.util.mybatis.session.SqlSession;
import com.comerzzia.pos.core.dispositivos.Dispositivos;
import com.comerzzia.pos.core.dispositivos.dispositivo.DispositivoCallback;
import com.comerzzia.pos.core.dispositivos.dispositivo.impresora.IPrinter;
import com.comerzzia.pos.core.dispositivos.dispositivo.tarjeta.TarjetaException;
import com.comerzzia.pos.core.dispositivos.dispositivo.visor.IVisor;
import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.POSApplication;
import com.comerzzia.pos.core.gui.componentes.botonaccion.BotonBotoneraComponent;
import com.comerzzia.pos.core.gui.componentes.botonaccion.importe.BotonBotoneraImagenValorComponent;
import com.comerzzia.pos.core.gui.componentes.botonaccion.medioPago.BotonBotoneraTextoComponent;
import com.comerzzia.pos.core.gui.componentes.botonaccion.normal.BotonBotoneraNormalComponent;
import com.comerzzia.pos.core.gui.componentes.botonaccion.simple.BotonBotoneraSimpleComponent;
import com.comerzzia.pos.core.gui.componentes.botonera.BotoneraComponent;
import com.comerzzia.pos.core.gui.componentes.botonera.ConfiguracionBotonBean;
import com.comerzzia.pos.core.gui.componentes.botonera.PanelBotoneraBean;
import com.comerzzia.pos.core.gui.componentes.botonera.medioPago.ConfiguracionBotonMedioPagoBean;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.exception.CargarPantallaException;
import com.comerzzia.pos.core.gui.login.seleccionUsuarios.SeleccionUsuariosView;
import com.comerzzia.pos.core.gui.permisos.exception.SinPermisosException;
import com.comerzzia.pos.gui.ventas.tickets.TicketManager;
import com.comerzzia.pos.gui.ventas.tickets.TicketManager.SalvarTicketCallback;
import com.comerzzia.pos.gui.ventas.tickets.articulos.FacturacionArticulosController;
import com.comerzzia.pos.gui.ventas.tickets.pagos.FormularioImportePagosBean;
import com.comerzzia.pos.gui.ventas.tickets.pagos.PagosController;
import com.comerzzia.pos.persistence.clientes.ClienteBean;
import com.comerzzia.pos.persistence.core.documentos.propiedades.PropiedadDocumentoBean;
import com.comerzzia.pos.persistence.core.documentos.tipos.TipoDocumentoBean;
import com.comerzzia.pos.persistence.fidelizacion.FidelizacionBean;
import com.comerzzia.pos.persistence.giftcard.GiftCardBean;
import com.comerzzia.pos.persistence.mediosPagos.MedioPagoBean;
import com.comerzzia.pos.persistence.mybatis.SessionFactory;
import com.comerzzia.pos.services.cajas.Caja;
import com.comerzzia.pos.services.cajas.CajaEstadoException;
import com.comerzzia.pos.services.cajas.CajasServiceException;
import com.comerzzia.pos.services.clientes.ClienteConstraintViolationException;
import com.comerzzia.pos.services.clientes.ClientesServiceException;
import com.comerzzia.pos.services.core.documentos.DocumentoException;
import com.comerzzia.pos.services.core.documentos.DocumentoNotFoundException;
import com.comerzzia.pos.services.core.documentos.Documentos;
import com.comerzzia.pos.services.core.documentos.tipos.TipoDocumentoService;
import com.comerzzia.pos.services.core.variables.VariablesServices;
import com.comerzzia.pos.services.fiscaldata.FiscalData;
import com.comerzzia.pos.services.mediospagos.MediosPagosService;
import com.comerzzia.pos.services.promociones.PromocionesServiceException;
import com.comerzzia.pos.services.ticket.Ticket;
import com.comerzzia.pos.services.ticket.TicketVenta;
import com.comerzzia.pos.services.ticket.TicketVentaAbono;
import com.comerzzia.pos.services.ticket.TicketsService;
import com.comerzzia.pos.services.ticket.TicketsServiceException;
import com.comerzzia.pos.services.ticket.cabecera.DatosDocumentoOrigenTicket;
import com.comerzzia.pos.services.ticket.cupones.CuponEmitidoTicket;
import com.comerzzia.pos.services.ticket.lineas.LineaTicket;
import com.comerzzia.pos.services.ticket.lineas.LineaTicketException;
import com.comerzzia.pos.services.ticket.pagos.PagoTicket;
import com.comerzzia.pos.services.ticket.pagos.PagoTicketException;
import com.comerzzia.pos.services.ticket.pagos.tarjeta.DatosRespuestaPagoTarjeta;
import com.comerzzia.pos.services.ticket.profesional.TicketVentaProfesional;
import com.comerzzia.pos.services.ticket.profesional.TotalesTicketProfesional;
import com.comerzzia.pos.services.ticket.tarjetaRegalo.TarjetaRegaloException;
import com.comerzzia.pos.servicios.impresion.ServicioImpresion;
import com.comerzzia.pos.util.bigdecimal.BigDecimalUtil;
import com.comerzzia.pos.util.config.AppConfig;
import com.comerzzia.pos.util.config.SpringContext;
import com.comerzzia.pos.util.format.FormatUtil;
import com.comerzzia.pos.util.i18n.I18N;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


@Primary
@Component
public class ByLPagosController extends PagosController {

	private static final String IMPORTE_MAXIMO_EFECTIVO_EX = "IMPORTE_MAXIMO_EFECTIVO_EX";
	private static final String LIMITE_EFECTIVO_CIF = "LIMITE_EFECTIVO_CIF";
	private static final String LIMITE_EFECTIVO_NIF = "LIMITE_EFECTIVO_NIF";
	public static final String PARAMETRO_MODO_PANTALLA_CAJERO = "MODO_PANTALLA_CAJERO";
	private static final String PERMISO_TVP_FACTURAR = "PERMITIR FACTURAR";
	private static final String URL_VISOR_DOCUMENTOS = "TPV.URL_VISOR_DOCUMENTOS";
	private static final String DOC_FACTURA_SIMPLIFICADA = "FS";
	private static final String DOC_FACTURA_COMPLETA = "FT";
	private static final String DOC_NOTA_CREDITO = "NC";
	public static final String DOC_TARJETA_REGALO = "TR";
	public static final String CODIGO_PAIS_ES = "ES";
	public static final String CODIGO_PAIS_PT = "PT";
	public static final String CODIGO_PAIS_USA = "US";
	public static final String CODIGO_PAIS_CO = "CO";
	public static final String CODIGO_PAIS_EC = "EC";
	public static final String CODIGO_PAIS_PANAMA = "PA";

	/* Permiso para controlar la forma de pago. */
	public static final String PERMISO_USAR_PAGOS = "USAR TODOS LOS PAGOS";
	public static final String COD_MP_EFECTIVO = "0000";
	public static final String COD_MP_ABONO = "1000";
	public static final String COD_MP_DEV_CAMBIO = "1001";
	public static final String COD_MP_PINPAD = "0010";
	public static final String COD_TIPO_DOC_NC = DOC_NOTA_CREDITO;
	public static final String DESMEDPAG_MSI = "ADYEN MSI";

	public static final String NOMBRE_IMPUESTO_PR_ST = "ST";
	public static final String NOMBRE_IMPUESTO_PR_MU = "MU";
	
	public static final String PLANTILLA_NOTA_INFORMATIVA = "nota_informativa";

	public static final String COD_MP_PINPAD_ADYEN_STANDALONE = "0016";

	public static final String POS_PERMITE_TICKET_ELECTRONICO = "POS.PERMITE_TICKET_ELECTRONICO";
	
	public static final String PERMISO_NO_AGREGAR_PAGO_ORIGEN = "NO AGREGAR PAGO ORIGEN"; // BYL-161
	
	/* BYL-177 - CLIENTE POR DEFECTO EN LAS TRANSACCIONES DE COLOMBIA */
	public static final String CO_DEFAULT_NOMBRE_RAZON_SOCIAL = "CONSUMIDOR FINAL";
	public static final String CO_DEFAULT_NUMERO_INDENTIFICACION = "222222222222";
	public static final String CO_DEFAULT_PAIS = "CO";
	public static final String CO_DEFAULT_CORREO_ELECTRONICO = "";
	public static final String CO_DEFAULT_CODTIPO_DOCUMENTO = "CC";	
	
	/* BYL-209 - CLIENTE POR DEFECTO EN LAS TRANSACCIONES DE ECUADOR */
	public static final String EC_DEFAULT_NOMBRE_RAZON_SOCIAL = "CONSUMIDOR FINAL";
	public static final String EC_DEFAULT_NUMERO_INDENTIFICACION = "9999999999999";
	public static final String EC_DEFAULT_PAIS = "EC";
	public static final String EC_DEFAULT_CODTIPO_DOCUMENTO = "07";	
	public static final String IMPORTE_MAXIMO_EC = "IMPORTE_MAX_ANONIMO";
	public static final String MOSTRAR_AVISO_CANCELAR = "mostrarAvisoCancelar";
	
	/* BYL-253 - CLIENTE POR DEFECTO EN LAS TRANSACCIONES DE PANAMÁ */
	public static final String PA_DEFAULT_NOMBRE_RAZON_SOCIAL = "CONSUMIDOR FINAL";
	public static final String PA_DEFAULT_NUMERO_INDENTIFICACION = "9999999999999";
	public static final String PA_DEFAULT_PAIS = "PA";
	public static final String PA_DEFAULT_CORREO_ELECTRONICO = "";
	public static final String PA_DEFAULT_CODTIPO_DOCUMENTO = "BO";	
	
	/* BYL-354 - AVISO BOTÓN FACTURA PARA PANAMÁ */
	private static final String PA_TEXTO_AVISO_BOTON_FACTURA = "avisoBotonFactura";
	
	
	
	private final String ACCION_EMAIL = "ACCION_EMAIL";
	private final String CORREO = "CORREO";
	private final String AMBOS = "AMBOS";
	private static String accionEmail;
	

	private boolean pedirTarjetaRegalo = false;
	private boolean medioPagoOriginal = false;
	private BigDecimal importePagoTarjeta = null;

	private BigDecimal totalDevo = null;

	private Boolean esDevolucionSinTicket = Boolean.FALSE;

	final IVisor visor = Dispositivos.getInstance().getVisor();

	@Autowired
	protected ByLCajasService cajasService;
	@Autowired
	protected MediosPagosService mediosPagosService;
	@Autowired
	protected TipoDocumentoService tipoDocuService;
	@Autowired
	private VariablesServices variablesServices;
	@Autowired
	private MediosPagosService mediosPagoServices;
	@Autowired
	private TicketsService ticketsService;
	@Autowired
	private ByLClientesService clientesService;
	@Autowired
	protected EpsonTSEService epsonTSEService;
	@Autowired
	protected Documentos documentos;
	@Autowired
	protected VertexService vertexservice;
	@Autowired
	protected MSIService msiService;

	@FXML
	protected Tab panelPestanaPagoTarjeta;
	@FXML
	protected Label lbPagosOrigen;
	@FXML
	protected HBox hBoxPagosOriginales;
	@FXML
	protected Button btCambiarCajero;
	@FXML
	protected AnchorPane pnBase, pnIva, pnRecargo, panelBotoneraNif;
	@FXML
	protected Label lbBase, lbIva, lbRecargo, lbTituloIva, lbTituloRecargo;

	private PropiedadDocumentoBean importeMaxEfectivo;
	protected BotoneraComponent botoneraMediosOrigen;

	/* Listados para dividir los tickets de devoluciones. */
	List<LineaTicket> listTicketNegativo = new ArrayList<LineaTicket>();
	List<LineaTicket> listTicketPositivo = new ArrayList<LineaTicket>();
	/* Ticket que contendrá las lineas en positivo. */
	// ByLTicketManager ticketPositivo = null;
	/* Ticket que contendrá las lineas en positivo. */
	ByLTicketManager ticketNegativo = null;

	ByLTicketManager ticketAux = null;
	ByLTicketManager ticketPrincipal = null;

	ByLVentaProfesionalManager ticketNegativoProfesional;
	ByLVentaProfesionalManager ticketPrincipalProfesional;
	ByLVentaProfesionalManager ticketAuxProfesional;

	private Boolean esPagoInicial = Boolean.FALSE;

	protected BotoneraComponent botoneraDatosNif;
	
	private List<ConfiguracionBotonBean> listaAccionesTarjeta = new LinkedList<>();
	
	protected Date fechaOrigenSinTicketReferenciar;

	@Override
	public void initializeForm() throws InitializeGuiException {
		ticketManager = (TicketManager) getDatos().get(FacturacionArticulosController.TICKET_KEY);
		ocultaInfoProfesional();

		esDevolucionSinTicket = Boolean.FALSE;
		// Recuperamos esta variable que nos indicará si se trata de una devolución sin ticket original
		if (getDatos().get(ByLDevolucionesController.DEVOLUCION_SIN_TICKET) != null) {
			esDevolucionSinTicket = (Boolean) getDatos().get(ByLDevolucionesController.DEVOLUCION_SIN_TICKET);
			getDatos().remove(ByLDevolucionesController.DEVOLUCION_SIN_TICKET);
		}
		
		fechaOrigenSinTicketReferenciar = null;
		if(ticketManager.isEsDevolucion()) {
			if (getDatos().containsKey(RequestFechaOrigenController.FECHA_RELLENADA)) {
				fechaOrigenSinTicketReferenciar = ((Date) getDatos().get(RequestFechaOrigenController.FECHA_RELLENADA));
				getDatos().remove(RequestFechaOrigenController.FECHA_RELLENADA);
			}
			((ByLTicketsService) ticketsService).setFechaOrigenSinTicketReferenciar(fechaOrigenSinTicketReferenciar);
		}

		tarjetaRegalo = null;
		enProceso = false;

		/*
		 * Refrescamos la variable cada vez que entramos en pantalla para que no se muestre el Popup, ya que si
		 * anteriormente se ha entrado en una devolución de pago con tarjeta regalo, se muestra siempre.
		 */
		pedirTarjetaRegalo = false;

		/*
		 * Solo se carga la botonera adicional (botón para hacer una factura) si el cliente que realiza la compra es
		 * fidelizado y tiene los campos de DNI y email rellenos
		 */
		if (comprobarPermisoFacturar() && !ticketManager.isEsDevolucion()) {
			// INICIO Incidencia #92
			if (ticketManager.getTicketOrigen() != null || comprobarBotonFactura()) {
				panelBotoneraDatosAdicionales.setVisible(true);
			}
			else {
				panelBotoneraDatosAdicionales.setVisible(false);
			}
			// FIN Incidencia #92
		}
		else {
			panelBotoneraDatosAdicionales.setVisible(false);
		}

		// Si entramos desde devoluciones y se trata de un ticket regalo, no se cargará la botoneraDatosAdicionales con
		// los métodos de pagos origen
		if (ticketManager.isEsDevolucion()) {
			if (AppConfig.menu.equals(ByLDevolucionesController.MENU_POS_PROFESIONAL)) {
				if (!((ByLVentaProfesionalManager) ticketManager).getEsTicketRegalo()) {
					cargarBotoneraDatosAdicionales();
				}
			}
			else {
				if (!((ByLTicketManager) ticketManager).getEsTicketRegalo()) {
					cargarBotoneraDatosAdicionales();
				}
			}
		}
		else {
			cargarBotoneraDatosAdicionales();
		}
		cargarBotoneraNif();
		lbDocActivo.setText(ticketManager.getDocumentoActivo().getDestipodocumento());
		if (!ticketManager.getDocumentoActivo().getRequiereCompletarPagos()) {
			lbTitulo.setText(I18N.getTexto("Entrega a cuenta"));
		}
		else {
			lbTitulo.setText(!AppConfig.menu.equals(ByLDevolucionesController.MENU_POS_PROFESIONAL) ? I18N.getTexto("Pagos") : I18N.getTexto("Pagos Profesionales"));
		}

		if (ticketManager.isEsFacturacionVentaCredito()) {
			panelEntregaCuenta.setVisible(true);
			panelEntregaCuenta.setManaged(true);
		}
		else {
			panelEntregaCuenta.setVisible(false);
			panelEntregaCuenta.setManaged(false);
		}

		/* Añadimos pagos de descuentos promocionales */
		String codMedioPagoPromocion = sesion.getAplicacion().getTienda().getTiendaBean().getCodMedioPagoPromocion();
		((TicketVenta<?, ?, ?>) ticketManager.getTicket()).removePago(codMedioPagoPromocion);
		BigDecimal importeDescPromocional = ticketManager.getTicket().getCabecera().getTotales().getTotalPromocionesCabecera();
		if (BigDecimalUtil.isMayorACero(importeDescPromocional)) {
			ticketManager.nuevaLineaPago(codMedioPagoPromocion, importeDescPromocional, true, false);
		}
		visor.modoPago(visorConverter.convert((TicketVentaAbono) ticketManager.getTicket()));
		escribirVisor();
		refrescarDatosPantalla();
		
		integracionVertex();
	}
	
	@Override
	public void refrescarDatosPantalla() {
		super.refrescarDatosPantalla();
		
		boolean esTicketRegalo = AppConfig.menu.equals(ByLDevolucionesController.MENU_POS_PROFESIONAL) ? ((ByLVentaProfesionalManager) ticketManager).getEsTicketRegalo()
		        : ((ByLTicketManager) ticketManager).getEsTicketRegalo();
		
		if ((comprobarPermisosPagos() && !esTicketRegalo) || BigDecimalUtil.isMayorACero(ticketManager.getTicket().getTotales().getTotalAPagar())) {
			if (comprobarLimitePagoEfectivo()) {
				/* Desactivamos el tab de efectivo, sus botones y la pestaña otros. */
				panelPestanaPagoEfectivo.setDisable(true);
				panelPagoEfectivo.setDisable(true);
				/*
				 * Insertamos en el Listener una comprobacion de más, para que solo lo lanze cuando si tiene permisos.
				 * Sino los Listener se lanzan siempre.
				 */
				panelPagos.focusedProperty().addListener(new ChangeListener<Boolean>(){

					@Override
					public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
						if (t.booleanValue() == false && t1.booleanValue() == true && comprobarPermisosPagos() && comprobarLimitePagoEfectivo()) {
							medioPagoSeleccionado = MediosPagosService.mediosPagoTarjetas.get(0);
							lbMedioPago.setText(I18N.getTexto(medioPagoSeleccionado.getDesMedioPago()));
							lbSaldo.setText("");
							Platform.runLater(new Runnable(){

								@Override
								public void run() {
									tfImporte.requestFocus();
								}
							});
						}
					}
				});

				/* Establecemos el medio de pago por defecto. */
				medioPagoSeleccionado = MediosPagosService.mediosPagoTarjetas.get(0);
				lbMedioPago.setText(I18N.getTexto(medioPagoSeleccionado.getDesMedioPago()));
				panelPagos.getSelectionModel().select(panelPestanaPagoTarjeta);
			}
			else {
				/*
				 * Debemos activar los componentes, ya que previamente se podria haber desactivado al entrar alguien sin
				 * permisos.
				 */
				panelPestanaPagoEfectivo.setDisable(false);
				panelPagoEfectivo.setDisable(false);
				panelPagoTarjeta.setDisable(false);
				panelPestanaPagoTarjeta.setDisable(false);

				BotoneraComponent botoneraContado = (BotoneraComponent) panelPagoContado.getChildren().get(0);
				for (Button configButton : botoneraContado.getListaBotones()) {
					configButton.setDisable(false);
				}

				/*
				 * Insertamos en el Listener una comprobacion de más, para que solo lo lanze cuando si tiene permisos.
				 * Sino los Listener se lanzan siempre.
				 */
				panelPagos.focusedProperty().addListener(new ChangeListener<Boolean>(){

					@Override
					public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
						if (t.booleanValue() == false && t1.booleanValue() == true && comprobarPermisosPagos()) {
							boolean esTicketRegalo = AppConfig.menu.equals(ByLDevolucionesController.MENU_POS_PROFESIONAL) ? ((ByLVentaProfesionalManager) ticketManager).getEsTicketRegalo()
							        : ((ByLTicketManager) ticketManager).getEsTicketRegalo();
							if (!esTicketRegalo) {
								reiniciaDatosGiftCard();
								medioPagoSeleccionado = MediosPagosService.medioPagoDefecto;
								lbMedioPago.setText(I18N.getTexto(medioPagoSeleccionado.getDesMedioPago()));
								lbSaldo.setText("");
							}
							Platform.runLater(new Runnable(){

								@Override
								public void run() {
									tfImporte.requestFocus();
								}
							});
						}
					}
				});
				/* Establecemos el medio de pago por defecto. */
				medioPagoSeleccionado = MediosPagosService.medioPagoDefecto;
				lbMedioPago.setText(I18N.getTexto(medioPagoSeleccionado.getDesMedioPago()));
				panelPagos.getSelectionModel().select(panelPestanaPagoEfectivo);
			}
			lbSaldo.setText("");
		}
		else {
			/* Desactivamos el tab de efectivo, sus botones y la pestaña tarjetas. */
			panelPestanaPagoEfectivo.setDisable(true);
			panelPagoEfectivo.setDisable(true);
			panelPagoTarjeta.setDisable(true);
			panelPestanaPagoTarjeta.setDisable(true);

			/*
			 * Ponemos por defecto "Tarjeta Regalo" porque el anterior por defecto era "Efectivo" y no esta permitido
			 * con estos permisos.
			 */
			for (MedioPagoBean mediosPagoRestantes : MediosPagosService.mediosPagoContado) {
				if (mediosPagoRestantes.getCodMedioPago().equals(COD_MP_ABONO)) {
					medioPagoSeleccionado = mediosPagoRestantes;
				}
			}
			BotoneraComponent botoneraContado = (BotoneraComponent) panelPagoContado.getChildren().get(0);
			MedioPagoBean medioPagoRegalo = mediosPagoServices.getMedioPago(COD_MP_ABONO);
			for (Button configButton : botoneraContado.getListaBotones()) {
				if (!configButton.getText().equals(medioPagoRegalo.getDesMedioPago())) {
					configButton.setDisable(true);
				}
			}
			/* Establecemos el modo de pago por defecto. */
			lbMedioPago.setText(I18N.getTexto(medioPagoSeleccionado.getDesMedioPago()));
			/* Establecemos el focus en la pestania de pago con tarjetas, ya que es la unica que queda. */
			panelPagos.getSelectionModel().select(panelPestanaPagoContado);
		}
		
		
		if (AppConfig.menu.equals(ByLDevolucionesController.MENU_POS_PROFESIONAL)) {
			ticketManager.getTicket().getTotales().recalcular();
			lbBase.setText(FormatUtil.getInstance().formateaImporte(ticketManager.getTicket().getTotales().getBase()));

			if (sesion.getAplicacion().getTienda().getCliente().getCodpais().equals(CODIGO_PAIS_USA)) {
				lbIva.setText(FormatUtil.getInstance().formateaImporte(((TotalesTicketProfesional) ticketManager.getTicket().getTotales()).getImpuestos()));
			}
			else {
				lbIva.setText(FormatUtil.getInstance().formateaImporte(((TotalesTicketProfesional) ticketManager.getTicket().getTotales()).getIvaTotal()));
				lbRecargo.setText(FormatUtil.getInstance().formateaImporte(((TotalesTicketProfesional) ticketManager.getTicket().getTotales()).getRecargoTotal()));
			}
		}
		
		autosizeLabelTotalFont();
	}

	@SuppressWarnings("unchecked")
	protected void cargarBotoneraDatosAdicionales() {
		try {
			panelBotoneraDatosAdicionales.getChildren().clear();
			PanelBotoneraBean panelBotoneraBean = null;
			String tipoDocumento = ticketManager.getDocumentoActivo().getCodtipodocumento();

			/*
			 * Si tenemos datos de documento origen estamos en una devolución controlada y por tanto cargamos la
			 * botonera de nota de crédito independientemente del tipo de documento para que no salgan los botones de
			 * datos adicionales salvo personalización.
			 */
			if (ticketManager.getTicketOrigen() != null) {
				tipoDocumento = DOC_NOTA_CREDITO;
			}
			else {
				if (tipoDocumento.equals(DOC_FACTURA_COMPLETA)) {
					tipoDocumento = DOC_FACTURA_SIMPLIFICADA;
				}
			}
			if (botoneraDatosAdicionales != null) {
				botoneraDatosAdicionales.eliminaComponentes();
			}

			try {
				log.debug("inicializarComponentes() - Cargando panel de Datos Adicionales");
				panelBotoneraBean = getView().loadBotonera("_adic_" + tipoDocumento + "_" + ticketManager.getDocumentoActivo().getCodpais() + ".xml");
				botoneraDatosAdicionales = new BotoneraComponent(panelBotoneraBean, null, panelBotoneraDatosAdicionales.getPrefHeight(), this, BotonBotoneraNormalComponent.class);
				panelBotoneraDatosAdicionales.getChildren().add(botoneraDatosAdicionales);
			}
			catch (InitializeGuiException e) {
				String mensajeError = "No existe Botonera por el Tipo de Documento y País";
				log.debug("initializeComponents() - " + mensajeError + " - " + e.getMessage());
				panelBotoneraDatosAdicionales.getChildren().clear();
			}
			catch (CargarPantallaException e) {
				String mensajeError = "Error cargando la Botonera asociada al Tipo de Documento";
				log.debug("initializeComponents() - " + mensajeError + " - " + e.getMessage(), e);
				panelBotoneraDatosAdicionales.getChildren().clear();
			}

			try {
				if (panelBotoneraBean == null) {
					panelBotoneraBean = getView().loadBotonera("_adic_" + tipoDocumento + ".xml");
					botoneraDatosAdicionales = new BotoneraComponent(panelBotoneraBean, null, panelBotoneraDatosAdicionales.getPrefHeight(), this, BotonBotoneraNormalComponent.class);
					panelBotoneraDatosAdicionales.getChildren().add(botoneraDatosAdicionales);
				}
			}
			catch (InitializeGuiException e) {
				String mensajeError = "Error al crear la Botonera";
				log.debug("initializeComponents() - " + mensajeError + " - " + e.getMessage());
				panelBotoneraDatosAdicionales.getChildren().clear();
			}
			catch (CargarPantallaException e) {
				String mensajeError = "Error cargando la Botonera asociada al Tipo de Documento";
				log.debug("initializeComponents() - " + mensajeError, e);
				panelBotoneraDatosAdicionales.getChildren().clear();
			}

			/*
			 * Limpiamos los estilos de los componentes para que en otras pantallas no afecten. Ponemos los colores en
			 * blanco y ponemos las cifras a 0. Todo es porque si accedes una sola vez a Devoluciones ya se quedan los
			 * estilos guardados, entonces tenemos que realizar esta limpieza.
			 */
			lbPagosOrigen.setText("");
			panelBotoneraDatosAdicionales.setStyle("-fx-padding: 0 0 0 0;");
			HBox.setMargin(panelBotoneraDatosAdicionales, new Insets(0, 0, 0, 0));
			lbPagosOrigen.setStyle("-fx-padding: 0 0 0 0; ");

			if (ticketManager.getTicketOrigen() != null) {
				lbPagosOrigen.setText(I18N.getTexto("Medios de pago origen"));
				/* Se añaden los botones de los medios de pago originales. */
				List<ConfiguracionBotonBean> listaAccionesMP = new LinkedList<ConfiguracionBotonBean>();

				// Solo se cargarán los pagos origen en el caso de que sea una devolución y no sea un ticket regalo
				Boolean esTicketRegalo = ticketManager instanceof ByLVentaProfesionalManager ? ((ByLVentaProfesionalManager) ticketManager).getEsTicketRegalo()
				        : ((ByLTicketManager) ticketManager).getEsTicketRegalo();

				if (!esTicketRegalo) {
					List<PagoTicket> pagos = ticketManager.getTicketOrigen().getPagos();
					Map<String, BigDecimal> mediosPagosMostrados = new HashMap<String, BigDecimal>();
					for (PagoTicket pago : pagos) {
						MedioPagoBean medioPago = mediosPagosService.getMedioPago(pago.getMedioPago().getCodMedioPago());
						if (medioPago != null) {
							/* No deberán aparecer los no manuales. */
							if (medioPago.getManual()) {

								if (pago.getImporte().compareTo(new BigDecimal(0)) == 1) {
									mediosPagosMostrados.put(medioPago.getCodMedioPago(), pago.getImporte());
									pago.setMedioPago(medioPago);
									/*
									 * Añadir en la descripción los 4 últimos dígitos del ID de transacción del pago con
									 * tarjeta.
									 */
									String descripcionMP = pago.getDesMedioPago();
									ByLConfiguracionBotonMedioPagoOriginalBean cfg = new ByLConfiguracionBotonMedioPagoOriginalBean(null, descripcionMP, null, "ACCION_SELECIONAR_MEDIO_PAGO_ORIGINAL",
									        "", pago);
									listaAccionesMP.add(cfg);
								}
							}
						}
					}
					if (!listaAccionesMP.isEmpty()) {
						botoneraMediosOrigen = new BotoneraComponent(1, 4, this, listaAccionesMP, null, null, ByLBotonBotoneraTextoComponent.class.getName());
						panelBotoneraDatosAdicionales.getChildren().add(botoneraMediosOrigen);
						panelBotoneraDatosAdicionales.setVisible(Boolean.TRUE);

						/* Añadimos los estilos ya que en otras pantallas no los tienen que tener */
						panelBotoneraDatosAdicionales.setStyle("-fx-padding: 7 2 2 2;");
						HBox.setMargin(panelBotoneraDatosAdicionales, new Insets(4, 0, 0, 10));
						lbPagosOrigen.setStyle("-fx-padding: 0 0 0 3; ");
					}

					/* Se comprueba si se debe anotar el pago directamente. */
					BigDecimal importeDevolucion = ticketManager.getTicket().getTotales().getPendiente();
					BigDecimal importeTotalVentaOrigen = ticketManager.getTicketOrigen().getTotales().getTotalAPagar();

					if (!BigDecimalUtil.isMayorACero(ticketManager.getTicket().getTotales().getTotalAPagar())) {
						/*
						 * Si solo existe un medio de pago y el total a pagar es menor o igual a ese medio de pago se
						 * anota el pago.
						 */
						if (mediosPagosMostrados.size() == 1) {
							String codMedioPago = (String) mediosPagosMostrados.keySet().toArray()[0];
							MedioPagoBean medioPago = mediosPagosService.getMedioPago(codMedioPago);
							BigDecimal importeAnotar = null;
							if (importeDevolucion.compareTo(mediosPagosMostrados.get(codMedioPago)) < 0 || importeDevolucion.compareTo(mediosPagosMostrados.get(codMedioPago)) == 0) {
								importeAnotar = importeDevolucion;
							}
							else {
								importeAnotar = mediosPagosMostrados.get(codMedioPago);
							}

							esPagoInicial = Boolean.TRUE;
							if (!medioPago.getCodMedioPago().equals(COD_MP_ABONO) && !medioPago.getTarjetaCredito()) {
								medioPagoSeleccionado = medioPago;
								if (!BigDecimalUtil.isMenorACero(importeAnotar) && puedeAgregarPagoOrigen()) {
									anotarPago(importeAnotar);
								}
							}
							else if (Dispositivos.getInstance().getGiftCard() != null && medioPago.getCodMedioPago().equals(COD_MP_ABONO)) {
								pedirTarjetaRegalo = true;
							}
							else if (medioPago.getTarjetaCredito()) {
								medioPagoSeleccionado = medioPago;
								if (!BigDecimalUtil.isMenorACero(importeAnotar) && puedeAgregarPagoOrigen()) {
									anotarPago(importeAnotar);
								}
							}
						}
						else {
							boolean pagoCompleto = importeDevolucion.compareTo(importeTotalVentaOrigen) == 0;
							if (pagoCompleto) {
								for (String codMedioPago : mediosPagosMostrados.keySet()) {
									MedioPagoBean medioPago = mediosPagosService.getMedioPago(codMedioPago);
									if (Dispositivos.getInstance().getGiftCard() != null && Dispositivos.getInstance().getGiftCard().isMedioPagoGiftCard(codMedioPago)) {
										pedirTarjetaRegalo = true;
										medioPagoOriginal = true;
										importePagoTarjeta = mediosPagosMostrados.get(codMedioPago);
									}
									else {
										medioPagoSeleccionado = medioPago;
										if (!BigDecimalUtil.isMenorACero(importeDevolucion)) {
											anotarPago(mediosPagosMostrados.get(codMedioPago));
										}
										if (medioPago.getTarjetaCredito()) {
											importePagoTarjeta = mediosPagosMostrados.get(codMedioPago);
										}
									}
								}
							}

						}
					}
				}
			}
		}
		catch (CargarPantallaException e) {
			String mensajeError = I18N.getTexto("Error al crear la botonera para los medios de pago del Ticket Original");
			log.error("inicializarComponentes() - " + mensajeError + " - " + e.getMessage(), e);
			VentanaDialogoComponent.crearVentanaError(mensajeError, getStage());
		}
		
		SqlSession sqlSession = new SqlSession();
		sqlSession.openSession(SessionFactory.openSession());

		MedioPagoBean medioPago = new MedioPagoBean();
		for (MedioPagoBean mpTarjeta : MediosPagosService.mediosPagoTarjetas) {
			if (mpTarjeta.getDesMedioPago().equals(DESMEDPAG_MSI)) {
				medioPago = mpTarjeta;
				break;
			}
		}

		ConfiguracionBotonMedioPagoBean botonCopia = new ConfiguracionBotonMedioPagoBean(null, DESMEDPAG_MSI, null, "ACCION_SELECIONAR_MEDIO_PAGO", "", medioPago);
		boolean contieneBotonCopia = false;
		for (ConfiguracionBotonBean boton : listaAccionesTarjeta) {
		    if (boton.getTexto().equals(botonCopia.getTexto())) {
		        contieneBotonCopia = true;
		        break;
		    }
		}
		if (contieneBotonCopia) {
			for (ConfiguracionBotonBean boton : listaAccionesTarjeta) {
				if (boton.getTexto().equals(DESMEDPAG_MSI)) {
					TipoDocumentoBean tipoDocumento = ticketManager.getDocumentoActivo();
					if (tipoDocumento.getCodtipodocumento().equals("TR")) {
						listaAccionesTarjeta.remove(boton);
						try {
							botoneraMediosPagoTarjeta = new BotoneraComponent(4, 4, this, listaAccionesTarjeta, panelPagoTarjeta.getPrefWidth(), panelPagoTarjeta.getPrefHeight(),
							        BotonBotoneraTextoComponent.class.getName());
							panelPagoTarjeta.getChildren().add(botoneraMediosPagoTarjeta);
							break;
						}
						catch (CargarPantallaException e) {
							log.error("cargarBotoneraDatosAdicionales() - Ha ocurrido un error: ", e);
						}

					}
				}
			}
		}
		else {
			if (sesion.getAplicacion().getTienda().getCliente().getCodpais().equals("MX")) {
				try {
					log.debug("initializeComponents() - Buscando si el medio de pago: " + medioPago.getDesMedioPago() + " está activo según el rango de fecha.");
					List<MediosPagoBIN> lstFechas = msiService.consultar(sesion.getAplicacion().getUidActividad(), medioPago.getCodMedioPago());
					if (lstFechas != null && lstFechas.size() == 1) {
						for (MediosPagoBIN fecha : lstFechas) {
							Date fechaInicio = fecha.getFechaInicio();
							Date fechaFin = fecha.getFechaFin();
							Date hoy = new Date();

							if (hoy.after(fechaInicio) && hoy.before(fechaFin)) {
								listaAccionesTarjeta.add(botonCopia);
							}
						}
					}
					try {
						botoneraMediosPagoTarjeta = new BotoneraComponent(4, 4, this, listaAccionesTarjeta, panelPagoTarjeta.getPrefWidth(), panelPagoTarjeta.getPrefHeight(),
						        BotonBotoneraTextoComponent.class.getName());
						panelPagoTarjeta.getChildren().add(botoneraMediosPagoTarjeta);
					}
					catch (CargarPantallaException e) {
						log.error("cargarBotoneraDatosAdicionales() - Ha ocurrido un error: ", e);
					}
				}
				catch (MediosPagoBINNotFoundException | MediosPagoBINException e) {
					log.error("inicializarComponentes() - Ha ocurrido un error: ", e);
					sqlSession.rollback();
				}
				finally {
					sqlSession.close();
				}
			}
		}

	}

	@Override
	public void initializeFocus() {
		restaurarFocoTFImporte();
		/* Añadimos la condición de que debe tener permisos para poder pagar con tarjeta regalo. */
		if (pedirTarjetaRegalo && comprobarPermisosPagos()) {
			procesarPagoTarjetaRegalo(mediosPagosService.getMedioPago(COD_MP_ABONO));
		}
	}
	
	@Override
	public void initializeComponents() {
		log.debug("inicializarComponentes()");
		try {
			initTecladoNumerico(tecladoNumerico);
			log.debug("inicializarComponentes() - Cargando medios de pago");
			frImportePago = SpringContext.getBean(FormularioImportePagosBean.class);

			//Registramos el evento de navegacion entre pestañas en el panel TabPane
			registrarEventosNavegacionPestanha(panelPagos, this.getStage());

			List<MedioPagoBean> mediosPago = MediosPagosService.mediosPagoContado;
			List<ConfiguracionBotonBean> listaAccionesAccionesTabla = new LinkedList<ConfiguracionBotonBean>();
			List<ConfiguracionBotonBean> listaAccionesMP = new LinkedList<ConfiguracionBotonBean>();
			
			log.debug("inicializarComponentes() - Registrando eventos de acceso rápido por teclado...");
			crearEventoEliminarTabla(tbPagos);
			
			log.debug("inicializarComponentes() - Configurando botonera");
			listaAccionesAccionesTabla.add(new ConfiguracionBotonBean("iconos/navigate_up.png", null, null, "ACCION_TABLA_ANTERIOR_REGISTRO", ""));
			listaAccionesAccionesTabla.add(new ConfiguracionBotonBean("iconos/navigate_down.png", null, null, "ACCION_TABLA_SIGUIENTE_REGISTRO", ""));
			listaAccionesAccionesTabla.add(new ConfiguracionBotonBean("iconos/row_delete.png", null, null, "ACCION_TABLA_ULTIMO_REGISTRO", ""));

			botoneraAccionesTabla = new BotoneraComponent(4, 1, this, listaAccionesAccionesTabla, panelMenuTabla.getPrefWidth(), panelMenuTabla.getPrefHeight(), BotonBotoneraSimpleComponent.class.getName());
			panelMenuTabla.getChildren().add(botoneraAccionesTabla);

			PanelBotoneraBean botoneraMediosPagos = null;
			try {
				botoneraMediosPagos = getView().loadBotonera("_mediospago_panel.xml");
			} 
			catch (InitializeGuiException ex) {
				log.info("inicializarComponentes() - No cargando botonera personalizada de mediospago \"xxx_mediospago_panel.xml\": " + ex.getMessage());
	        }
			
			if(botoneraMediosPagos == null) {
				log.debug("inicializarComponentes() - Creando acciones para botonera de pago contado"); // En pruebas: Metemos en contado cualquier medio de pago
				for (MedioPagoBean pag : mediosPago) {
					ConfiguracionBotonMedioPagoBean cfg = new ConfiguracionBotonMedioPagoBean(null, pag.getDesMedioPago(), null, "ACCION_SELECIONAR_MEDIO_PAGO", "", pag);
					listaAccionesMP.add(cfg);
				}
				botoneraMediosPagoContado = new BotoneraComponent(3, 4, this, listaAccionesMP, null, panelPagoContado.getPrefHeight(), BotonBotoneraTextoComponent.class.getName());
				panelPagoContado.getChildren().add(botoneraMediosPagoContado);
				
				SqlSession sqlSession = new SqlSession();
				sqlSession.openSession(SessionFactory.openSession());

				for (MedioPagoBean mpTarjeta : MediosPagosService.mediosPagoTarjetas) {
					if (mpTarjeta.getDesMedioPago().equals(DESMEDPAG_MSI)) {
						if (sesion.getAplicacion().getTienda().getCliente().getCodpais().equals("MX")) {
							try {
								log.debug("initializeComponents() - Buscando si el medio de pago: " + mpTarjeta.getDesMedioPago() + " está activo según el rango de fecha.");
								List<MediosPagoBIN> lstFechas = msiService.consultar(sesion.getAplicacion().getUidActividad(), mpTarjeta.getCodMedioPago());
								if (lstFechas != null && lstFechas.size() == 1) {
									for (MediosPagoBIN fecha : lstFechas) {
										if (fecha.getFechaInicio() != null && fecha.getFechaFin() != null) {
											Date fechaInicio = fecha.getFechaInicio();
											Date fechaFin = fecha.getFechaFin();
											Date hoy = new Date();

											if (hoy.after(fechaInicio) && hoy.before(fechaFin)) {
												ConfiguracionBotonMedioPagoBean cfg = new ConfiguracionBotonMedioPagoBean(null, mpTarjeta.getDesMedioPago(), null, "ACCION_SELECIONAR_MEDIO_PAGO", "",
												        mpTarjeta);
												listaAccionesTarjeta.add(cfg);
											}
										}
									}
								}
							}
							catch (MediosPagoBINNotFoundException | MediosPagoBINException e) {
								log.error("inicializarComponentes() - Ha ocurrido un error: ", e);
								sqlSession.rollback();
							}
							finally {
								sqlSession.close();
							}
						}
					}
					else {
						ConfiguracionBotonMedioPagoBean cfg = new ConfiguracionBotonMedioPagoBean(null, mpTarjeta.getDesMedioPago(), null, "ACCION_SELECIONAR_MEDIO_PAGO", "", mpTarjeta);
						listaAccionesTarjeta.add(cfg);
					}
				}
				botoneraMediosPagoTarjeta = new BotoneraComponent(4, 4, this, listaAccionesTarjeta, panelPagoTarjeta.getPrefWidth(), panelPagoTarjeta.getPrefHeight(), BotonBotoneraTextoComponent.class.getName());
				panelPagoTarjeta.getChildren().add(botoneraMediosPagoTarjeta);
				
	
				try{
					log.debug("inicializarComponentes() - Cargando panel de importes");
					PanelBotoneraBean panelBotoneraBean = getView().loadBotonera();
					botoneraImportes = new BotoneraComponent(panelBotoneraBean, null, panelPagoEfectivo.getPrefHeight(), this, BotonBotoneraImagenValorComponent.class);
					panelPagoEfectivo.getChildren().add(botoneraImportes);
				} catch (InitializeGuiException e) {
					log.error("initializeComponents() - Error al crear botonera: " + e.getMessage(), e);
				}
			} else {
				panelMediosPago.getChildren().clear();
				BotoneraComponent botonera = new BotoneraComponent(botoneraMediosPagos, panelMediosPago.getPrefWidth(), panelMediosPago.getPrefHeight(), this, BotonBotoneraNormalComponent.class);
				panelMediosPago.getChildren().add(botonera);
			}
			

			// inicializa Ciclo de focos
			inicializarFocos();
			
			addSeleccionarTodoCampos();

			registrarAccionCerrarVentanaEscape();
		}
		catch (CargarPantallaException ex) {
			log.error("inicializarComponentes() - Error creando botonera para medio de pago. error : " + ex.getMessage(), ex);
			VentanaDialogoComponent.crearVentanaError(I18N.getTexto("Error cargando pantalla pagos."), getStage());
		}
		
		String codPais = sesion.getAplicacion().getTienda().getCliente().getCodpais();
		if (codPais.equals(ByLVentaProfesionalController.COD_PUERTO_RICO)) {
			lbTituloIva.setText(NOMBRE_IMPUESTO_PR_ST);
			lbTituloRecargo.setText(NOMBRE_IMPUESTO_PR_MU);
		}

		if (AppConfig.menu.equals(ByLDevolucionesController.MENU_POS_PROFESIONAL) && codPais.equals(CODIGO_PAIS_USA)) {
			this.lbRecargo.setVisible(false);
			lbTituloRecargo.setVisible(false);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void realizarAccion(BotonBotoneraComponent botonAccionado) {
		log.debug("realizarAccion() - Realizando la acción : " + botonAccionado.getClave() + " de tipo : " + botonAccionado.getTipo());
		switch (botonAccionado.getClave()) {

			case "ACCION_SELECIONAR_MEDIO_PAGO":
				log.debug("Acción cambiar medio de pago en pantalla");
				BotonBotoneraTextoComponent boton = (BotonBotoneraTextoComponent) botonAccionado;
				MedioPagoBean mp = boton.getMedioPago();

				// Cada vez que pulsemos sobre un medio de pago, pondremos a null la tarjetaRegalo para que no la
				// mantenga y pinte su saldo cuando corresponda.
				tarjetaRegalo = null;

				if (mp.getDesMedioPago().equals(DESMEDPAG_MSI)) {
					log.debug("realizarAccion() - Abriendo pantalla de los pagos fraccionados.");
					getApplication().getMainView().showModalCentered(MsiView.class, getDatos(), this.getStage());
					if (StringUtils.isNotBlank((String) getDatos().get("meses"))) {
						((ByLCabeceraTicket) ticketManager.getTicket().getCabecera()).setTotalMeses(Integer.parseInt(getDatos().get("meses").toString()));
					}
				}
				
				if (getDatos().isEmpty()) {
					if (mp.getTarjetaCredito()) {
						medioPagoSeleccionado = mp;
						lbMedioPago.setText(mp.getDesMedioPago());
						lbSaldo.setText("");
						seleccionarMedioPagoTarjeta(mp);
						((ByLCabeceraTicket) ticketManager.getTicket().getCabecera()).setTotalMeses(null);
					}
					else if (Dispositivos.getInstance().getGiftCard() != null && Dispositivos.getInstance().getGiftCard().isMedioPagoGiftCard(mp.getCodMedioPago())) {
						/* Si el medio de pago seleccionado es de tipo tarjeta regalo pedimos el número de tarjeta. */
						procesarPagoTarjetaRegalo(mp);
						
						((ByLCabeceraTicket) ticketManager.getTicket().getCabecera()).setTotalMeses(null);
					}
					else {
						lbMedioPago.setText(mp.getDesMedioPago());
						medioPagoSeleccionado = mp;
						lbSaldo.setText("");
						
						((ByLCabeceraTicket) ticketManager.getTicket().getCabecera()).setTotalMeses(null);
					}
				}
				else if (StringUtils.isNotBlank((String) getDatos().get("meses"))) {
					lbMedioPago.setText(mp.getDesMedioPago());
					medioPagoSeleccionado = mp;
					lbSaldo.setText("");
					seleccionarMedioPagoTarjeta(mp);
				}
				else if (StringUtils.isNotBlank((String) getDatos().get("cancelar"))) {
					log.debug("Se ha cancelado la introducción de los meses.");
					
					((ByLCabeceraTicket) ticketManager.getTicket().getCabecera()).setTotalMeses(null);
					break;
				}
				 getDatos().clear();
				break;

			case "ACCION_TABLA_ANTERIOR_REGISTRO":
				log.debug("Acción seleccionar registro anterior de la tabla");
				accionIrAnteriorRegistroTabla();
				break;

			case "ACCION_TABLA_SIGUIENTE_REGISTRO":
				log.debug("Acción seleccionar siguiente registro de la tabla");
				accionIrSiguienteRegistroTabla();
				break;

			case "ACCION_TABLA_ULTIMO_REGISTRO":
				log.debug("Acción seleccionar último registro de la tabla");
				accionBorrarRegistroTabla();
				break;

			case "ACCION_SELECIONAR_MEDIO_PAGO_ORIGINAL":
				log.debug("Acción cambiar medio de pago en pantalla");
				ByLBotonBotoneraTextoComponent botonOriginal = (ByLBotonBotoneraTextoComponent) botonAccionado;
				PagoTicket pago = botonOriginal.getPago();

				/* Eliminamos el pago anterior al pulsar otra vez sobre el mismo botón. */
				Boolean eliminarPagoAnterior = false;
				PagoTicket pagoEliminar = new PagoTicket();
				for (PagoTicket pagos : (List<PagoTicket>) ticketManager.getTicket().getPagos()) {
					if (pago.getCodMedioPago().equals(pagos.getCodMedioPago())) {
						eliminarPagoAnterior = true;
						pagoEliminar = pagos;
					}
				}
				if (eliminarPagoAnterior) {
					ticketManager.getTicket().getPagos().remove(pagoEliminar);
					ticketManager.getTicket().getTotales().recalcular();
					refrescarDatosPantalla();
				}

				if (Dispositivos.getInstance().getGiftCard() != null && Dispositivos.getInstance().getGiftCard().isMedioPagoGiftCard(pago.getMedioPago().getCodMedioPago())) {
					/* Si el medio de pago seleccionado es de tipo tarjeta regalo pedimos el número de tarjeta. */
					importePagoTarjeta = pago.getImporte();
					medioPagoOriginal = true;
					procesarPagoTarjetaRegalo(pago.getMedioPago());
				}
				else {
					lbMedioPago.setText(pago.getMedioPago().getDesMedioPago());
					medioPagoSeleccionado = pago.getMedioPago();
					lbSaldo.setText("");
					if (ticketManager.getTicket().getTotales().getPendiente().compareTo(BigDecimal.ZERO) > 0) {
						BigDecimal importeAnotar = null;
						if (ticketManager.getTicket().getTotales().getPendiente().compareTo(pago.getImporte()) > 0) {
							importeAnotar = pago.getImporte();
						}
						else {
							importeAnotar = ticketManager.getTicket().getTotales().getPendiente();
						}
						if (BigDecimalUtil.isMenorACero(importeAnotar)) {
							importeAnotar = importeAnotar.negate();
						}
						anotarPago(importeAnotar);
					}
				}
				break;

			default:
				String mensajeError = "No se ha especificado ninguna acción en pantalla para la Operación : " + botonAccionado.getClave();
				log.error("realizarAccion() - " + mensajeError);
				break;
		}
		restaurarFocoTFImporte();
	}

	/**
	 * Realiza una comprobacion del permiso de "Operaciones Efectivo".
	 * 
	 * @return permisos : Boolean que indica "true" si tiene permisos, y "false" en caso de no tener permisos.
	 */
	public Boolean comprobarPermisosPagos() {
		boolean permisos = true;
		try {
			if (ticketManager.getTicketOrigen() != null || esDevolucionSinTicket) {
				compruebaPermisos(PERMISO_USAR_PAGOS);
			}
		}
		catch (SinPermisosException e) {
			permisos = false;
		}
		return permisos;
	}

	public Boolean comprobarPermisoFacturar() {
		boolean permisos = true;
		try {
			compruebaPermisos(PERMISO_TVP_FACTURAR);
		}

		catch (SinPermisosException e) {
			permisos = false;
		}
		return permisos;
	}

	public Boolean comprobarLimitePagoEfectivo() {
		boolean limitePagoEfectivo = false;
		obtenerImporteMaxEfectivo();
		if (importeMaxEfectivo != null) {
			if (StringUtils.isNotBlank(importeMaxEfectivo.getValor())) {
				Long importeMaxEfecLong = Long.valueOf(importeMaxEfectivo.getValor());
				BigDecimal importeMaxEfecBd = BigDecimal.valueOf(importeMaxEfecLong);
				if (BigDecimalUtil.isMayorOrIgual(ticketManager.getTicket().getCabecera().getTotales().getTotalAPagar(), importeMaxEfecBd)) {
					limitePagoEfectivo = true;
					comprobarLimitePagosAnotados();
				}
			}
		}
		return limitePagoEfectivo;
	}

	protected void procesarPagoTarjetaRegalo(final MedioPagoBean medioPagoBean) {
		log.info("procesarPagoTarjetaRegalo() - Iniciamos el procesamiento del Pago con Tarjeta Regalo...");
		try {
			if (ticketManager.getTicket().getCabecera().getTarjetaRegalo() == null) {
				Stage stage = null;
				
				/* BYL-147 Fallo en devoluciones con TA
				 * 
				 * Jerarquía de herencia:
				 * TicketManager
				 *  - ByLTicketManager
				 *  - VentaProfesionalManager
				 *     - ByLVentaProfesionalManager
				 * 
				 * ByLVentaProfesionalManager no hereda de ByLTicketManager, sino del TicketManager
				 * estándar, por lo que no siempre se puede castear una subclase de TicketManager
				 * al personalizado, cuidado al personalizar otras subclases de TicketManager
				 */
				boolean esTicketRegalo = AppConfig.menu.equals(ByLDevolucionesController.MENU_POS_PROFESIONAL) ?
							((ByLVentaProfesionalManager) ticketManager).getEsTicketRegalo() :
							((ByLTicketManager) ticketManager).getEsTicketRegalo();
				
				if(esTicketRegalo && esPagoInicial) {
					stage = getApplication().getMainView().getStage();
				}
				else {
					stage = getStage();
				}
				/* === FIN BYL-147 Fallo en devoluciones con TA === */
				
				Dispositivos.getInstance().getGiftCard().pedirTarjetaRegalo(stage, new DispositivoCallback<GiftCardBean>(){

					@Override
					public void onSuccess(GiftCardBean tarjeta) {
						if (tarjeta != null) {
							boolean esOperacionPositiva = BigDecimalUtil.isMayorACero(ticketManager.getTicket().getTotales().getTotal());
							boolean esTarjetaAbono = ((ByLGiftCardBean) tarjeta).isTarjetaAbono();
							Integer estadoTarjeta = ((ByLGiftCardBean) tarjeta).getEstado();
							String error = ((ByLGiftCardBean) tarjeta).getError();
							if (error != null && !error.isEmpty()) {
								VentanaDialogoComponent.crearVentanaAviso(error, getStage());
							}
							else if (!esOperacionPositiva && !esTarjetaAbono) {
								String mensajeAviso = I18N.getTexto("No se puede utilizar una tarjeta de regalo como medio de devolución." + " \nTome una tarjeta de tipo Abono e inténtelo de nuevo");
								log.debug("procesarPagoTarjetaRegalo() - " + mensajeAviso);
								VentanaDialogoComponent.crearVentanaAviso(mensajeAviso, getStage());
							}
							else if (tarjeta.isBaja() || (ticketManager.getTicketOrigen() == null && !tarjeta.isActiva() && !esDevolucionSinTicket)) {
								log.debug("procesarPagoTarjetaRegalo() - La tarjeta introducida está " + obtenerEstadoTarjeta(estadoTarjeta));
								VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("La tarjeta introducida está " + obtenerEstadoTarjeta(estadoTarjeta)), getStage());
							}
							else {
								boolean esMedioPagoCorrectoTipoTarj = Dispositivos.getInstance().getGiftCard().esMedioPago(tarjeta.getCodTipoTarjeta(), medioPagoBean);
								if (!esMedioPagoCorrectoTipoTarj) {
									String mensajeAviso = I18N.getTexto("La tarjeta introducida no es del tipo permitido para este medio de pago");
									log.debug("procesarPagoTarjetaRegalo() - " + mensajeAviso);
									VentanaDialogoComponent.crearVentanaAviso(mensajeAviso, getStage());
									return;
								}
								tarjetaRegalo = tarjeta;

								String mensajeSaldo = "Saldo" + " : (" + tarjeta.getSaldo() + ")";
								lbSaldo.setText(I18N.getTexto(mensajeSaldo));
								MedioPagoBean mp = medioPagoBean;
								lbMedioPago.setText(mp.getDesMedioPago());
								medioPagoSeleccionado = mp;

								boolean venta = (!ticketManager.getTicket().getCabecera().getCodTipoDocumento().equals(DOC_NOTA_CREDITO))
								        && BigDecimalUtil.isMayorOrIgualACero(ticketManager.getTicket().getTotales().getTotal());
								GiftCardBean tarjetaRegaloPago = obtenerPagoTarjetaRegalo(tarjetaRegalo);
								if (tarjetaRegaloPago != null) {
									asociarPagoTarjetaRegalo(venta, tarjetaRegaloPago);
								}
								else {
									String mensaje = "Saldo" + " : (" + tarjetaRegalo.getSaldo() + ")";
									lbSaldo.setText(I18N.getTexto(mensaje));
									if (venta) {
										if (BigDecimalUtil.isMayor(ticketManager.getTicket().getTotales().getPendiente(), tarjeta.getSaldoTotal())) {
											tfImporte.setText(FormatUtil.getInstance().formateaImporte(tarjeta.getSaldoTotal()));
										}
									}
									if (medioPagoOriginal) {
										BigDecimal importeAPagar = ticketManager.getTicket().getTotales().getPendiente().compareTo(importePagoTarjeta) > 0 ? importePagoTarjeta
										        : ticketManager.getTicket().getTotales().getPendiente();
										anotarPago(importeAPagar);
										medioPagoOriginal = false;
										importePagoTarjeta = null;
									}
								}
							}
						}
						else {
							String mensajeAviso = I18N.getTexto("No se ha encontrado la tarjeta en el sistema");
							log.debug("procesarPagoTarjetaRegalo() - " + mensajeAviso);
							VentanaDialogoComponent.crearVentanaAviso(mensajeAviso, getStage());
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						String mensajeError = "Ha ocurrido un error al pedir la Tarjeta Regalo";
						log.error("procesarPagoTarjetaRegalo() - " + mensajeError + " - " + caught.getMessage(), caught);
						medioPagoOriginal = false;
						importePagoTarjeta = null;

						reiniciaDatosGiftCard();
					}
				});
			}
			else {
				if (!ticketManager.isEsOperacionTarjetaRegalo()) {
					String mensajeAviso = I18N.getTexto("No puede usarse una tarjeta regalo en la operación");
					VentanaDialogoComponent.crearVentanaAviso(mensajeAviso, getStage());
				}
				else {
					String mensajeAviso = I18N.getTexto("No puede usarse más de una tarjeta regalo");
					VentanaDialogoComponent.crearVentanaAviso(mensajeAviso, getStage());
				}
			}
			log.info("procesarPagoTarjetaRegalo() - Finalizado el procesamiento del Pago con Tarjeta Regalo");
		}
		catch (Exception e) {
			String mensajeError = "Error procesando la tarjeta regalo para el pago";
			log.error("procesarPagoTarjetaRegalo() - " + mensajeError + " - " + e.getMessage(), e);
			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto(mensajeError), getStage());
		}
	}

	@SuppressWarnings("unchecked")
	protected GiftCardBean obtenerPagoTarjetaRegalo(GiftCardBean tarjetaRegalo) {
		if (tarjetaRegalo != null) {
			for (PagoTicket pago : (List<PagoTicket>) ticketManager.getTicket().getPagos()) {
				if (pago.getGiftcards() != null) {
					for (GiftCardBean tarjPago : pago.getGiftcards()) {
						if (tarjetaRegalo.getNumTarjetaRegalo().equals(tarjPago.getNumTarjetaRegalo())) {
							return tarjPago;
						}
					}
					break;
				}
			}
		}
		return null;
	}

	/**
	 * Acción que acepta los pagos realizados.
	 * 
	 * @throws DocumentoException
	 */
	@FXML
	public void aceptar() throws DocumentoException {
		Boolean operacionCero = false;
		if (cajasService.comprobarCajaMaster()) {

			/* Se añade estas 2 lineas para el caso en el que varios tpv's comparten una misma caja */
			try {
				Caja cajaAbierta = cajasService.consultarCajaAbierta();
				cajasService.meterCajaSesion(cajaAbierta);
				ticketManager.getTicket().getCabecera().setUidDiarioCaja(cajaAbierta.getUidDiarioCaja());
			}
			catch (CajasServiceException | CajaEstadoException e1) {
				log.error("aceptar() - Ha ocurrido un error al comprobar la caja abierta + " + e1.getMessage());
				VentanaDialogoComponent.crearVentanaError(getStage(), e1);
				enProceso = false;
				return;
			}

			if (!enProceso) {
				enProceso = true;
				/* Comprobamos que se hayan cubierto los pagos */
				if (((((TicketVenta<?, ?, ?>) ticketManager.getTicket()).isPagosCubiertos() && ticketManager.getDocumentoActivo().getRequiereCompletarPagos())
				        || !ticketManager.getDocumentoActivo().getRequiereCompletarPagos())) {
					log.debug("aceptar() - Pagos cubiertos");

					if (!ticketManager.comprobarImporteMaximoOperacion(getStage())) {
						enProceso = false;
						return;
					}
					if (!ticketManager.comprobarCierreCajaDiarioObligatorio()) {
						String fechaCaja = FormatUtil.getInstance().formateaFecha(sesion.getSesionCaja().getCajaAbierta().getFechaApertura());
						String fechaActual = FormatUtil.getInstance().formateaFecha(new Date());
						String mensajeError = I18N.getTexto("No se puede realizar la venta. El día de apertura de la caja " + fechaCaja + " no coincide con el del sistema " + fechaActual);
						VentanaDialogoComponent.crearVentanaError(mensajeError, getStage());
						enProceso = false;
						return;
					}

					/* Comprobamos que el total a pagar es 0. */
					if (ticketManager.getTicket().getTotales().getTotalAPagar().compareTo(BigDecimal.ZERO) == 0) {
						operacionCero = true;
					}
					accionEmail = null;

					/* ======================= VENTA ======================= */
					if (!ticketManager.isEsDevolucion()) {
						if (!operacionCero) {
							if (Dispositivos.getInstance().getImpresora1() instanceof EpsonTM30) {
								if (Dispositivos.getInstance().getImpresora1().getConfiguracion().getNombreConexion().equals(EpsonTSEService.NOMBRE_CONEXION_TSE)) {
									Boolean correcto = tseStartTransaction(ticketManager);
									if (!correcto) {
										Boolean resultado = VentanaDialogoComponent
										        .crearVentanaConfirmacion(I18N.getTexto("No se ha podido realizar la conexión con el TSE, ¿Desea continuar sin TSE?"), getStage());
										if (!resultado) {
											return;
										}
									}
								}
							}

							comprobarMensajeVisa();

							/* Comprobacion de la operativa StandAlone */
							if (!comprobarStandAlone()) {
								enProceso = false;
								return;
							}
							/*Comprobacion de si es obligatorio identificar al cliente en la venta*/							
							if(tratamientoClientePorPais()) {
								enProceso = false;
								return;
							}
							

							/*
							 * Se muestra la pantalla para decidir si imprimir el ticket y/o enviarlo por correo solo en
							 * caso de que exista un fidelizado para la venta y tenga consentimiento y email
							 */

							/*
							 * Ya que puede ser que entre por aqui al ser una devolucion con signo positivo, controlamos
							 * que solo entre cuando no sea devolución
							 */
							if (ticketManager.getTicket().getCabecera().getDatosFidelizado() != null && !ticketManager.isEsDevolucion()) {
								String consentimiento2 = ((ByLFidelizacionBean) ticketManager.getTicket().getCabecera().getDatosFidelizado()).getConsentimientoRecibenoti();
								String email = ((ByLFidelizacionBean) ticketManager.getTicket().getCabecera().getDatosFidelizado()).getEmail();

								TipoDocumentoBean tipoDocumento;
								String permiteTicketE = null;
								try {
									tipoDocumento = sesion.getAplicacion().getDocumentos().getDocumento(ticketManager.getTicket().getCabecera().getTipoDocumento());
									PropiedadDocumentoBean propiedadClaseProcesamiento = tipoDocumento.getPropiedades().get(POS_PERMITE_TICKET_ELECTRONICO);

									if (propiedadClaseProcesamiento != null) {
										permiteTicketE = propiedadClaseProcesamiento.getValor();
									}
								}
								catch (DocumentoException e1) {
									log.error("aceptar() - Ha ocurrido un error al obtener el tipo de documento", e1);
								}

								/*
								 * 0 -> No permite ticket electronico (Se salta pantalla de email) 1 -> Se trata de
								 * impresora fiscal, no se permite "Sólo email" 2/null -> Se permite todo
								 */
								
								/* Comprobacion para colombia */
								boolean permiteTicketElectronico = permitirTicketElectronicoCO();
								if (!permiteTicketElectronico) {
									permiteTicketE = "0";
								}
								
								if (permiteTicketE == null || permiteTicketE.equals("1") || permiteTicketE.equals("2")) {
									if (consentimiento2 != null && "S".equals(consentimiento2) && email != null && !email.isEmpty()) {
										HashMap<String, Object> mapaEmail = new HashMap<String, Object>();
										
										mapaEmail.put(POS_PERMITE_TICKET_ELECTRONICO, permiteTicketE);
										getApplication().getMainView().showModalCentered(ByLEmailView.class, mapaEmail, this.getStage());
										accionEmail = (String) mapaEmail.get(ACCION_EMAIL);
										if (accionEmail != null && (accionEmail.equals(CORREO) || accionEmail.equals(AMBOS))) {
											((ByLCabeceraTicket) ticketManager.getTicket().getCabecera()).setEmail(email);
										}
									}
								}
								
							}
							
							ticketManager.salvarTicketSeguridad(getStage(), new SalvarTicketCallback(){

								@Override
								public void onSucceeded() {
									accionSalvarTicketSucceeded(false);
									
									enProceso = false;
								}

								@Override
								public void onFailure(Exception e) {
									accionSalvarTicketOnFailure(e);
									reiniciarDivisionTickets();
									accionBorrarRegistroTabla();
									enProceso = false;
								}
							});
						}
						else {
							VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("No se puede realizar una venta a 0. "), this.getStage());
							enProceso = false;
						}
						/* ======================= DEVOLUCIÓN ======================= */
					}
					else {
						if (ticketManager.getTicketOrigen() != null) {

							Boolean anulacionCorrecta = Boolean.FALSE;
							if (((ByLTicketVentaAbono) ticketManager.getTicketOrigen()).getTaxFree() != null) {
								VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Operación sujeta a Taxfree. La operación inicial debe abonarse al completo."), this.getStage());

								anulacionCorrecta = realizarVOIDTaxFree(((ByLTicketVentaAbono) ticketManager.getTicketOrigen()).getTaxFree().getNumeroFormulario());
							}

							if (((ByLTicketVentaAbono) ticketManager.getTicketOrigen()).getTaxFree() != null && !anulacionCorrecta) {
								// Significa que el ticket tiene taxfree pero ha ocurrido algun problema con la
								// anulacion del voucher
								VentanaDialogoComponent.crearVentanaError(I18N.getTexto("No se ha podido realizar la anulación del voucher de TAXFREE"), this.getStage());
								enProceso = false;
							}
							else {
								if (anulacionCorrecta) {
									VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Se ha anulado correctamente el TAXFREE. Téngalo en cuenta para futuras operaciones."), this.getStage());
								}
								// Inicio INCIDENCIA #102
								comprobarMensajeVisa();
								// Fin INCIDENCIA #102

								/* Comprobacion de la operativa StandAlone */
								if (!comprobarStandAlone()) {
									enProceso = false;
									return;
								}

								reiniciarDivisionTickets();
								divisionTicketDevolucion();
							}
						}
						else {
							// Significa que estamos en una devolución sin ticket (no referenciada) por lo que obviamos
							// las comprobaciones de taxfree
							comprobarMensajeVisa();

							/* Comprobacion de la operativa StandAlone */
							if (!comprobarStandAlone()) {
								enProceso = false;
								return;
							}

							reiniciarDivisionTickets();
							divisionTicketDevolucion();
						}
					}
				}
				else {
					log.debug("aceptar() - Pagos no cubiertos");
					VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Los pagos han de cubrir el importe a pagar."), this.getStage());
					enProceso = false;
				}
			}
			else {
				log.warn("aceptar() - Pago en proceso");
			}
		}
		else {
			cajaMasterCerrada();
		}
	}

	/**
	 * Vacía de datos los listados de lineas y los tickets para que no haya errores a la hora de imprimir. Sino
	 * realizamos estas acciones de vaciado al realizar cualquier otra operación de la pantalla de pagos, se muestra los
	 * anteriores.
	 */
	public void reiniciarDivisionTickets() {
		/* Vaciamos los listados de lineas. */
		listTicketNegativo = new ArrayList<LineaTicket>();
		listTicketPositivo = new ArrayList<LineaTicket>();
		/* Vaciamos los tickets. */
		ticketNegativo = SpringContext.getBean(ByLTicketManager.class);
		ticketPrincipal = SpringContext.getBean(ByLTicketManager.class);
		ticketAux = null;

		ticketNegativoProfesional = SpringContext.getBean(ByLVentaProfesionalManager.class);
		ticketPrincipalProfesional = SpringContext.getBean(ByLVentaProfesionalManager.class);
		ticketAuxProfesional = null;
	}

	/**
	 * Realiza una división en dos tickets a partir del ticketManager de una devolución. Se divide en lineas negativas y
	 * lineas positivas.
	 */
	@SuppressWarnings("unchecked")
	public void divisionTicketDevolucion() {

		log.debug("divisionTicketDevolucion() - Iniciamos la división de Tickets entre Positivo y Negativo...");

		/* Rellenamos el listado positivo y negativo. */
		for (LineaTicket linea : (List<LineaTicket>) ticketManager.getTicket().getLineas()) {
			int resultado = linea.getCantidad().compareTo(new BigDecimal("0"));
			if (resultado == -1) {
				listTicketNegativo.add(linea);
				log.debug("divisionTicketDevolucion() - Linea insertada en el Ticket Negativo : ");
				log.debug("divisionTicketDevolucion() - Id Linea : " + linea.getIdLinea());
				log.debug("divisionTicketDevolucion() - Artículo : " + linea.getDesArticulo());
				log.debug("divisionTicketDevolucion() - Precio : " + linea.getImporteTotalConDto());
				log.debug("divisionTicketDevolucion() - Cantidad : " + linea.getCantidadAsString());
			}
			else {
				listTicketPositivo.add(linea);
				log.debug("divisionTicketDevolucion() - Linea insertada en el Ticket Positivo : ");
				log.debug("divisionTicketDevolucion() - Id Linea : " + linea.getIdLinea());
				log.debug("divisionTicketDevolucion() - Artículo : " + linea.getDesArticulo());
				log.debug("divisionTicketDevolucion() - Precio : " + linea.getImporteTotalConDto());
				log.debug("divisionTicketDevolucion() - Cantidad : " + linea.getCantidadAsString());
			}
		}

		totalDevo = BigDecimal.ZERO;
		/* Nuevo medio de pago para las devoluciones. */
		final MedioPagoBean medioPagoDevo = mediosPagoServices.getMedioPago(COD_MP_DEV_CAMBIO);
		try {
			if (AppConfig.menu.equals(ByLDevolucionesController.MENU_POS_PROFESIONAL)) {
				tratarTicketNegativoProfesional(medioPagoDevo);
				tratarTicketPositivoProfesional(medioPagoDevo);

				if (ticketAuxProfesional != null) {
					ticketPrincipalProfesional = ticketAuxProfesional;
					ticketPrincipalProfesional.setTicketAuxiliar(ticketNegativoProfesional);
				}
				else {
					ticketPrincipalProfesional = ticketNegativoProfesional;
				}
				
				
				ticketPrincipalProfesional.salvarTicketSeguridad(getStage(), new SalvarTicketCallback(){

					@Override
					public void onSucceeded() {		
						accionSalvarTicketSucceededDivision();
						enProceso = false;

					}

					@Override
					public void onFailure(Exception e) {
						accionSalvarTicketOnFailure(e);
						reiniciarDivisionTickets();
						enProceso = false;
					}
				});
			}
			else {
				tratarTicketNegativo(medioPagoDevo);
				tratarTicketPositivo(medioPagoDevo);

				if (ticketAux != null) {
					ticketPrincipal = ticketAux;
					ticketPrincipal.setTicketAuxiliar(ticketNegativo);
				}
				else {
					ticketPrincipal = ticketNegativo;
				}
				/*
				 * COLOMBIA --> RELLENAMOS EL CLIENTE CONSUMIDOR FINAL EN LAS DEVOLUCIONES SI ES EL MISMO QUE
				 * EL GENÉRICO
				 */
				if (CODIGO_PAIS_CO.equals(ticketManager.getDocumentoActivo().getCodpais())) {
					tratarClienteConsumidorFinalCO(ticketPrincipal.getTicket().getCliente());
				}
				else if (CODIGO_PAIS_EC.equals(ticketManager.getDocumentoActivo().getCodpais())) {
					if (tratarDevolucionEC()) {
						VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Se deben añadir los datos del cliente que realiza la devolución"), getStage());
						enProceso = false;
						return;
					}
				}
				else if (CODIGO_PAIS_PANAMA.equals(ticketManager.getDocumentoActivo().getCodpais())) {
					if (tratarDevolucionPA()) {
						VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Se deben añadir los datos del cliente que realiza la devolución"), getStage());
						enProceso = false;
						return;
					}
				}
				
				ticketPrincipal.salvarTicketSeguridad(getStage(), new SalvarTicketCallback(){

					@Override
					public void onSucceeded() {
						accionSalvarTicketSucceededDivision();
						enProceso = false;

					}

					@Override
					public void onFailure(Exception e) {
						accionSalvarTicketOnFailure(e);
						reiniciarDivisionTickets();
						enProceso = false;
					}
				});
			}
		}
		catch (PromocionesServiceException | DocumentoException | LineaTicketException | TicketsServiceException e1) {
			accionSalvarTicketOnFailure(e1);
			enProceso = false;
		}

		log.debug("divisionTicketDevolucion() - Finalizada la creación del Ticket Positivo");
	}
	
	/** Realiza acciones después de guardar los tickets. */
	protected void accionSalvarTicketSucceededDivision() {
		imprimir();
		mostrarVentanaCambio();

		if (AppConfig.menu.equals(ByLDevolucionesController.MENU_POS_PROFESIONAL)) {
			((ByLVentaProfesionalManager) ticketManager).notificarContadores();
		}
		else {
			((ByLTicketManager) ticketManager).notificarContadores();
		}
		ticketManager.finalizarTicket();
		getStage().close();
	}

	@Override
	protected void accionSalvarTicketSucceeded(boolean repiteOperacion) {
		imprimir();

		// Mostramos la ventana con la información de importe pagado, cambio...
		if (!BigDecimalUtil.isIgualACero(ticketManager.getTicket().getTotales().getCambio().getImporte())) {
			mostrarVentanaCambio();
		}

		comprobarAptoRealizarTaxFree();
		
		comprobarEnvioEdicom();

		if (repiteOperacion) {
			enProceso = false;
			initTicketManager(false);
			aceptarPagos(false);
		}
		else {
			if (AppConfig.menu.equals(ByLDevolucionesController.MENU_POS_PROFESIONAL)) {
				((ByLVentaProfesionalManager) ticketManager).notificarContadores();
			}
			else {
				((ByLTicketManager) ticketManager).notificarContadores();
			}
			ticketManager.finalizarTicket();
			getStage().close();
		}

	}

	/** Realiza la impresión de los datos de un ticket. */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void imprimir() {
		try {

			Boolean isImpresoraWebPosPanama = sesion.getAplicacion().getTienda().getCliente().getCodpais().equals(CODIGO_PAIS_PANAMA) && !"TR".equals(ticketManager.getTicket().getCabecera().getCodTipoDocumento());
			
			/* Si se trata de la impresora fiscal de polonia o rusia, sólo se imprime el ticket fiscal */
			Boolean isImpresoraFiscal = isImpresoraFiscalPolonia() || isImpresoraFiscalRusia() || isImpresoraWebPosPanama;

			// --- WEBPOS PANAMÁ INICIO ---
//			FiscalData fiscalData = ticketManager.getTicket().getCabecera().getFiscalData();
//			if (sesion.getAplicacion().getTienda().getCliente().getCodpais().equals(CODIGO_PAIS_PANAMA)) {
//				log.debug("comprobarEnvioWebPos() - Comprobando si se ha realizado el envio del documento a WebPos");
//				if (fiscalData != null && fiscalData.getProperty(ByLVariablesServices.FICHERO_FISCAL_PROCESADO) != null
//						&& fiscalData.getProperty(ByLVariablesServices.FICHERO_FISCAL_PROCESADO).getValue().equals("N")) {
//					ByLVentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("No se ha podido establecer la conexión con WEBPOS. Inténtelo de nuevo. Gracias"),getStage());
//					return;
//				}
//			}
			// --- WEBPOS PANAMÁ FIN ---
			
			while (true) {
				String formatoImpresion = ticketManager.getTicket().getCabecera().getFormatoImpresion();
				if (formatoImpresion.equals(TipoDocumentoBean.PROPIEDAD_FORMATO_IMPRESION_NO_CONFIGURADO)) {
					String mensajeInfo = "El Formato de impresión no está configurado correctamente, se procede a cancelar la impresión";
					log.info("imprimir() - " + mensajeInfo);
					return;
				}

				// boolean mostrarAviso = false;
				boolean hayPagosTarjeta = false;
				for (Object pago : ticketManager.getTicket().getPagos()) {
					if (pago instanceof PagoTicket && ((PagoTicket) pago).getDatosRespuestaPagoTarjeta() != null) {
						hayPagosTarjeta = true;
						break;
					}

				}

				/* Comprobamos que los listados están vacios, significa que no es una devolución. */
				if (listTicketNegativo.isEmpty() && listTicketPositivo.isEmpty()) {
					boolean imprimir = true;
					if (accionEmail != null && accionEmail.equals(CORREO)) {
						imprimir = false;

						/* Comprobamos si en los pagos hay algún pago con efectivo para proceder a abrir el cajón */
						Boolean abreCajon = Boolean.FALSE;
						for (Object pago : ticketManager.getTicket().getPagos()) {
							if (pago instanceof PagoTicket && ((PagoTicket) pago).getCodMedioPago().equals(COD_MP_EFECTIVO)) {
								abreCajon = Boolean.TRUE;
							}
						}

						if (abreCajon) {
							Dispositivos.abrirCajon();
						}
						/* ------------------------------------------------------- */
					}

					if (imprimir) {
						if (!isImpresoraFiscal || (isImpresoraFiscalPolonia() && ticketManager.getTicket().getCabecera().getCodTipoDocumento().equals(DOC_TARJETA_REGALO))) {
						Map<String, Object> mapaParametrosManager = new HashMap<String, Object>();
						mapaParametrosManager.put("activacionTR", ticketManager.isEsOperacionTarjetaRegalo());
						mapaParametrosManager.put("ticket", ticketManager.getTicket());
						mapaParametrosManager.put("urlQR", variablesServices.getVariableAsString(URL_VISOR_DOCUMENTOS));

						if (ticketManager.getTicket().getCabecera().getCodTipoDocumento()
						        .equals(sesion.getAplicacion().getDocumentos().getDocumento(Documentos.FACTURA_COMPLETA).getCodtipodocumento())) {
							mapaParametrosManager.put("empresa", sesion.getAplicacion().getEmpresa());
						}

						ServicioImpresion.imprimir(formatoImpresion, mapaParametrosManager);
					}
					// Imprimimos, si existen, las notas informativas del ticket de venta
					imprimeNotaInformativa(ticketManager);
					}
				}
				else {
					/* Se trata de una devolucion */
					if (!isImpresoraFiscalRusia()) {
						if (ticketPrincipal.getTicket() != null || ticketPrincipalProfesional != null) {
							Map<String, Object> mapaParametrosNegativo = new HashMap<String, Object>();
							mapaParametrosNegativo.put("ticket",
							        AppConfig.menu.equals(ByLDevolucionesController.MENU_POS_PROFESIONAL) ? ticketPrincipalProfesional.getTicket() : ticketPrincipal.getTicket());
							mapaParametrosNegativo.put("urlQR", variablesServices.getVariableAsString(URL_VISOR_DOCUMENTOS));

							ServicioImpresion.imprimir(formatoImpresion, mapaParametrosNegativo);
						}
					}

					if (ticketPrincipal.getTicketAuxiliar() != null || ticketPrincipalProfesional.getTicketAuxiliar() != null) {
						if (!isImpresoraFiscalRusia()) {
							Map<String, Object> mapaParametrosPositivo = new HashMap<String, Object>();
							mapaParametrosPositivo.put("ticket", AppConfig.menu.equals(ByLDevolucionesController.MENU_POS_PROFESIONAL) ? ticketPrincipalProfesional.getTicketAuxiliar().getTicket()
							        : ticketPrincipal.getTicketAuxiliar().getTicket());
							mapaParametrosPositivo.put("urlQR", variablesServices.getVariableAsString(URL_VISOR_DOCUMENTOS));

							if (ticketPrincipal.getTicketAuxiliar() != null) {
								if (ticketPrincipal.getTicketAuxiliar().getTicket().getCabecera().getCodTipoDocumento()
								        .equals(sesion.getAplicacion().getDocumentos().getDocumento(Documentos.FACTURA_COMPLETA).getCodtipodocumento())) {
									mapaParametrosPositivo.put("empresa", sesion.getAplicacion().getEmpresa());
								}
							}
							else {
								if (ticketPrincipalProfesional.getTicketAuxiliar().getTicket().getCabecera().getCodTipoDocumento()
								        .equals(sesion.getAplicacion().getDocumentos().getDocumento(Documentos.FACTURA_COMPLETA).getCodtipodocumento())) {
									mapaParametrosPositivo.put("empresa", sesion.getAplicacion().getEmpresa());
								}
							}

							ServicioImpresion.imprimir(formatoImpresion, mapaParametrosPositivo);
						}
						// Imprimimos, si existen, las notas informativas del ticket positivo
						imprimeNotaInformativa(
						        AppConfig.menu.equals(ByLDevolucionesController.MENU_POS_PROFESIONAL) ? ticketPrincipalProfesional.getTicketAuxiliar() : ticketPrincipal.getTicketAuxiliar());
					}
					/* Limpiamos los listados y tickets antes de terminar. */
					reiniciarDivisionTickets();
				}

				if (hayPagosTarjeta) {
					String mensajeConfirmacion = I18N.getTexto("¿Es correcta la impresión del recibo del pago con tarjeta?");
					if (VentanaDialogoComponent.crearVentanaConfirmacion(mensajeConfirmacion, getStage())) {
						break;
					}
				}
				else {
					break;
				}
			}

			/* Cupones */
			if (BigDecimalUtil.isMayorACero(ticketManager.getTicket().getTotales().getTotal())) {
				List<CuponEmitidoTicket> cupones = ((TicketVentaAbono) ticketManager.getTicket()).getCuponesEmitidos();
				if (cupones.size() > 0) {
					Map<String, Object> mapaParametrosCupon = new HashMap<String, Object>();
					mapaParametrosCupon.put("ticket", ticketManager.getTicket());
					for (CuponEmitidoTicket cupon : cupones) {
						mapaParametrosCupon.put("cupon", cupon);
						SimpleDateFormat df = new SimpleDateFormat();
						mapaParametrosCupon.put("fechaEmision", df.format(ticketManager.getTicket().getCabecera().getFecha()));
						ServicioImpresion.imprimir(PLANTILLA_CUPON, mapaParametrosCupon);
					}
				}
				if (!ticketManager.isEsDevolucion()) {
					/* Imprimimos vale para cambio */
					if (mediosPagosService.isCodMedioPagoVale(ticketManager.getTicket().getTotales().getCambio().getCodMedioPago(), ticketManager.getTicket().getCabecera().getTipoDocumento())
					        && !BigDecimalUtil.isIgualACero(ticketManager.getTicket().getTotales().getCambio().getImporte())) {
						printVale(ticketManager.getTicket().getTotales().getCambio());
					}
				}
				else {
					if (ticketManager.getTicket().getCabecera().getCodTipoDocumento().equals(DOC_NOTA_CREDITO)) {
						/*
						 * Es una devolución donde el signo del tipo de documento es positivo, imprimimos vales de
						 * pagos.
						 */
						List<PagoTicket> pagos = ((TicketVenta) ticketManager.getTicket()).getPagos();
						for (PagoTicket pago : pagos) {
							if (mediosPagosService.isCodMedioPagoVale(pago.getCodMedioPago(), ticketManager.getTicket().getCabecera().getTipoDocumento())
							        && BigDecimalUtil.isMenorACero(pago.getImporte())) {
								printVale(pago);
							}
						}
					}
				}
			}
			else {
				/*
				 * Imprimimos vales para pagos si estamos en devolución pero no si es de cambio (pago positivo en una
				 * devolucion donde los pagos son negativos)
				 */
				List<PagoTicket> pagos = ((TicketVenta) ticketManager.getTicket()).getPagos();
				for (PagoTicket pago : pagos) {
					if (mediosPagosService.isCodMedioPagoVale(pago.getCodMedioPago(), ticketManager.getTicket().getCabecera().getTipoDocumento()) && !BigDecimalUtil.isMayorACero(pago.getImporte())) {
						printVale(pago);
					}
				}
			}
		}
		catch (Exception e) {
			String mensajeError = I18N.getTexto("Se ha producido un error al imprimir");
			log.error("imprimir() - " + mensajeError + " - " + e.getClass().getName() + " - " + e.getLocalizedMessage(), e);
			VentanaDialogoComponent.crearVentanaError(getStage(), mensajeError, e);
		}
	}

	/** Acción que cancela el pago. */
	@SuppressWarnings("rawtypes")
	@Override
	public void accionCancelar() {
		getDatos().put(PagosController.ACCION_CANCELAR, true);
		getDatos().put("origen", true);
		lbSaldo.setText("");

		// Se puede dar el caso que se quiera salir de la pantalla pagos despues
		// de haber realizado un pago (Ej: PinPad) por lo que tendría relleno
		// IdTicket y los contadores. En este caso setearemos a null el IdTicket
		// para que al volver a darle al boton aceptar de pagos vuelva a pedir
		// contador en bbdd
		String mensajeConfirmacion = I18N.getTexto("Se perderán los datos de pagos. ¿Desea continuar?");
		if (((TicketVenta<?, ?, ?>) ticketManager.getTicket()).getPagos().isEmpty()) {
			((Ticket) ticketManager.getTicket()).setIdTicket(null);
			if (vertexservice.integracionImpuestosVertexActiva(ticketManager.getTicket().getCabecera().getTipoDocumento())) {
				borrarDatosImpuestos();
			}
			borrarDatosPago();
		}
		else if (VentanaDialogoComponent.crearVentanaConfirmacion(mensajeConfirmacion, getStage())) {
			((Ticket) ticketManager.getTicket()).setIdTicket(null);
			if (vertexservice.integracionImpuestosVertexActiva(ticketManager.getTicket().getCabecera().getTipoDocumento())) {
				borrarDatosImpuestos();
			}
			borrarDatosPago();
		}
		if(ticketManager.getDocumentoActivo().getCodpais().equals(CODIGO_PAIS_CO)) {
			setearDatosClienteGenerico();
		}
	}

	/** Envía un mensaje por pantalla, que indica que la caja no está abierta y te envía a la pantalla principal. */
	private void cajaMasterCerrada() {
		VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("La caja a la que estaba conectado se ha cerrado."), this.getStage());
		/*
		 * Enviamos este dato para evitar excepciones innecesarias. Enviamos siempre a true en caso de estar la caja
		 * cerrada.
		 */
		getDatos().put("origen", true);
		this.getStage().close();
	}

	@Override
	protected void seleccionarMedioPagoTarjeta(MedioPagoBean mp) {
		if (mp.getTarjetaCredito() && ticketManager.getTicket().getTotales().getPendiente().compareTo(BigDecimal.ZERO) > 0) {
			anotarPago(FormatUtil.getInstance().desformateaBigDecimal(tfImporte.getText()));
		}
	}

	/**
	 * Accion anotarPagos. Crea linea de pago con el importe indicado y el medio de pago seleccionado en pantalla
	 * 
	 * @param importe
	 */

	public void anotarPago(BigDecimal importe) {
		log.debug("anotarPago() - Anotando pago: \nMedio de pago : " + medioPagoSeleccionado + "\nImporte : " + importe);
		if (importe != null) {
			boolean esTarjetaRegalo = Dispositivos.getInstance().getGiftCard().isMedioPagoGiftCard(medioPagoSeleccionado.getCodMedioPago());

			if (medioPagoSeleccionado == null) {
				String mensajeError = "No hay ninguna forma de pago seleccionada";
				log.debug("anotarPago() - " + mensajeError);
				VentanaDialogoComponent.crearVentanaError(I18N.getTexto("No hay ninguna forma de pago seleccionada"), getStage());
				return;
			}
			if (importe.compareTo(BigDecimal.ZERO) == 0) {
				return;
			}
			/* Si medio de pago es giftcard o medio de pago es tarjeta */
			if ((esTarjetaRegalo || MediosPagosService.mediosPagoTarjetas.contains(medioPagoSeleccionado))
			        && BigDecimalUtil.isMayor(importe.abs(), ticketManager.getTicket().getTotales().getPendiente().abs())) {
				String mensajeError = I18N.getTexto("El medio de pago seleccionado no permite introducir un importe superior al total del documento");
				log.debug("anotarPago() - " + mensajeError);
				VentanaDialogoComponent.crearVentanaError(mensajeError, getStage());
				tfImporte.setText(FormatUtil.getInstance().formateaImporte(ticketManager.getTicket().getTotales().getPendiente()));
				return;
			}
			try {
				if (esTarjetaRegalo) {
					if (tarjetaRegalo != null) {
						boolean esVenta = ticketManager.getTicket().getCabecera().esVenta();
						BigDecimal saldoDisponibleTarjetaRegalo = calcularSaldoPendienteTarjetaRegalo(esVenta);

						if (esVenta && BigDecimalUtil.isMayor(importe, saldoDisponibleTarjetaRegalo)) {
							String mensajeAviso = I18N.getTexto("El importe supera el saldo disponible");
							log.debug("anotarPagos() - " + mensajeAviso);
							VentanaDialogoComponent.crearVentanaAviso(mensajeAviso, this.getStage());
						}
						else {

							ticketManager.setEsOperacionTarjetaRegalo(true);
							incluirPagoTicket(importe, tarjetaRegalo);

							if (ticketManager instanceof ByLTicketRegalo) {
							    ByLTicketRegalo ticketRegalo = (ByLTicketRegalo) ticketManager;
							    if (!ticketRegalo.getEsTicketRegalo() && comprobarPermisosPagos()) {
							        medioPagoSeleccionado = MediosPagosService.medioPagoDefecto;
							        lbMedioPago.setText(I18N.getTexto(medioPagoSeleccionado.getDesMedioPago()));
							        panelPagos.getSelectionModel().select(panelPestanaPagoEfectivo);
							    }
							}

							tfImporte.setText(FormatUtil.getInstance().formateaImporte(ticketManager.getTicket().getTotales().getPendiente()));
							lbSaldo.setText("");
							tfImporte.requestFocus();

							tarjetaRegalo = null;
						}
					}
					else {
						VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Debe indicar el número de tarjeta. Haga click en el botón correspondiente."), this.getStage());
					}
				}
				else {
					/*
					 * Realizamos la comprobación de que es una devolución para que no se tenga en cuenta en los pagos
					 */
					if (ticketManager.isEsDevolucion()) {
						/* Comprobamos que el ticket es un ticket regalo */
						boolean esTicketRegalo = false;
						if (ticketManager instanceof ByLTicketRegalo) {
						    esTicketRegalo = ((ByLTicketRegalo) ticketManager).getEsTicketRegalo();
						}
						if (esTicketRegalo && "1000".equals(medioPagoSeleccionado.getCodMedioPago())) {
							if (BigDecimalUtil.isMenorOrIgualACero(ticketManager.getTicket().getTotales().getTotal())) {
								for (MedioPagoBean medioPago : MediosPagosService.mediosPagoContado) {
									if ("1000".equals(medioPago.getCodMedioPago())) {
										medioPagoSeleccionado = medioPago;
									}
								}
								/*
								 * Procesamos el pago de la tarjeta de regalo porque también debemos pedir el número de
								 * la tarjeta que vamos a entregar
								 */
								try {
									procesarPagoTarjetaRegalo(medioPagoSeleccionado);
								}
								catch (Exception e) {
									String mensajeError = I18N.getTexto("Se ha producido un error al procesar el pago con Tarjeta Regalo");
									log.error("anotarPago() - " + mensajeError + " - " + e.getMessage());
									VentanaDialogoComponent.crearVentanaError(mensajeError, getStage());
								}
								/* En caso de haber encontrado la tarjeta de regalo, insertamos el pago con ella */
								if (tarjetaRegalo != null) {
									incluirPagoTicket(importe, tarjetaRegalo);
									tarjetaRegalo = null;
								}
							}
							else {
								incluirPagoTicket(importe, null);
							}
						}
						else {
							incluirPagoTicket(importe, null);
						}
					}
					else {
						incluirPagoTicket(importe, null);
					}
				}
				visor.modoPago(visorConverter.convert((TicketVenta) ticketManager.getTicket()));
				escribirVisor();
			}
			catch (PagoTicketException ex) {
				VentanaDialogoComponent.crearVentanaError(getStage(), ex.getMessageI18N(), ex);
			}
		}
		else {
			VentanaDialogoComponent.crearVentanaAviso("El importe introducido no es válido", getStage());
		}
		tfImporte.requestFocus();

	}

	@SuppressWarnings("rawtypes")
	private Boolean comprobarBotonFactura() {
		FidelizacionBean fidelizado = ((TicketVenta) ticketManager.getTicket()).getCabecera().getDatosFidelizado();

		if (fidelizado != null && (fidelizado.getDocumento() != null && !fidelizado.getDocumento().isEmpty()) && (fidelizado.getDomicilio() != null && !fidelizado.getDomicilio().isEmpty())) {
			return true;
		}

		return false;
	}

	private void comprobarMensajeVisa() {
		// Inicio ISSUES #66
		Boolean mostrarAviso = false;

		for (Object pago : ticketManager.getTicket().getPagos()) {
			if (pago instanceof PagoTicket && ((PagoTicket) pago).getDatosRespuestaPagoTarjeta() != null) {
				break;
			}

			if (Dispositivos.getInstance().getTarjeta() != null && Dispositivos.getInstance().getTarjeta().getConfiguracion() != null) {
				Map<String, String> mapaConfiguracion = Dispositivos.getInstance().getTarjeta().getConfiguracion().getParametrosConfiguracion();
				String medPagoConfigurado = COD_MP_PINPAD;
				String medPagoConfigurado2 = COD_MP_PINPAD;
				if (mapaConfiguracion.get("PAYMENTS") != null || mapaConfiguracion.get("PAYMENTS2") != null) {
					if (mapaConfiguracion.get("PAYMENTS") != null) {
						medPagoConfigurado = mapaConfiguracion.get("PAYMENTS");
					}
					if (mapaConfiguracion.get("PAYMENTS2") != null) {
						medPagoConfigurado2 = mapaConfiguracion.get("PAYMENTS2");
					}
				}
				if (pago instanceof PagoTicket && ((PagoTicket) pago).isMedioPagoTarjeta()
				        && (!((PagoTicket) pago).getCodMedioPago().equals(medPagoConfigurado) && !((PagoTicket) pago).getCodMedioPago().equals(medPagoConfigurado2))
				        && !((PagoTicket) pago).getCodMedioPago().equals(COD_MP_PINPAD_ADYEN_STANDALONE)) {
					mostrarAviso = true;
				}
			}
			else if (pago instanceof PagoTicket && ((PagoTicket) pago).isMedioPagoTarjeta()) {
				mostrarAviso = true;
			}
		}
		if (mostrarAviso) {
			String mensajeAviso = I18N.getTexto("ATENCIÓN, recuerda que tienes que pasar la tarjeta por el INALÁMBRICO");
			log.debug("imprimir() - " + mensajeAviso);
			ByLVentanaDialogoComponent.crearVentanaAviso(mensajeAviso, this.getStage());
		}
		// Fin ISSUES #66
	}

	@SuppressWarnings("unchecked")
	private void tratarTicketPositivo(MedioPagoBean medioPagoDevo) throws TicketsServiceException {
		if (!listTicketPositivo.isEmpty()) {
			log.debug("divisionTicketDevolucion() - Iniciamos la creación del Ticket Positivo...");

			ticketAux = SpringContext.getBean(ByLTicketManager.class);

			try {
				/* Creamos el Ticket, le indicamos el tipo de documento y los datos del Ticket */
				ticketAux.nuevoTicket();

				ticketAux.setDevolucionSinTicket(esDevolucionSinTicket);

				if (!esDevolucionSinTicket) {
					List<TipoDocumentoBean> listadoTipoDocu = tipoDocuService.consultarTiposDocumentos(sesion.getAplicacion().getUidActividad(),
					        sesion.getAplicacion().getTienda().getCliente().getCodpais());
					for (TipoDocumentoBean tipoDocu : listadoTipoDocu) {
						if (tipoDocu.getCodtipodocumento().equals(ticketManager.getTicket().getCabecera().getCodTipoDocumento())) {
							ticketAux.setDocumentoActivo(tipoDocu);
						}
					}

					/* Indicamos el Ticket Origen de la Devolución */
					ticketAux.setTicketOrigen(ticketNegativo.getTicketOrigen());
					/* Indicamos los datos del Cliente y los datos del Fidelizado */
//					ticketAux.getTicket().setCliente(ticketManager.getTicket().getCliente());
					ticketAux.getTicket().setCliente(sesion.getAplicacion().getTienda().getCliente());
				}

				if (ticketManager.isEsOperacionTarjetaRegalo()) {
					ticketAux.setEsOperacionTarjetaRegalo(true);
				}
				else {
					ticketAux.setEsOperacionTarjetaRegalo(false);
				}

				/*
				 * Ahora insertamos los Medios de Pago y las Lineas del Ticket pero sino tiene lineas en negativo,
				 * realizamos la operación como si fuera un ticket de venta, ya que los pagos, lineas, y origen serán
				 * los mismos.
				 */
				if (!listTicketNegativo.isEmpty()) {
					/* Indicamos los datos de Origen del Ticket */
					DatosDocumentoOrigenTicket datosOrigen = new DatosDocumentoOrigenTicket();
					datosOrigen.setCaja(ticketNegativo.getTicket().getCabecera().getCodCaja());
					datosOrigen.setCodTipoDoc(ticketNegativo.getTicket().getCabecera().getCodTipoDocumento());
					datosOrigen.setIdTipoDoc(ticketNegativo.getTicket().getCabecera().getTipoDocumento());
					datosOrigen.setNumFactura(ticketNegativo.getTicket().getIdTicket());
					datosOrigen.setSerie(ticketNegativo.getTicket().getCabecera().getTienda().getCodAlmacen());
					datosOrigen.setFecha(ticketNegativo.getTicket().getCabecera().getFechaAsLocale());
					datosOrigen.setDesTipoDoc(ticketNegativo.getTicket().getCabecera().getDesTipoDocumento());
					datosOrigen.setUidTicket(ticketNegativo.getTicket().getUidTicket());
					datosOrigen.setCodTicket(ticketNegativo.getTicket().getCabecera().getCodTicket());
					datosOrigen.setTienda(ticketNegativo.getTicket().getTienda().getCodAlmacen());

					/* Seteamos las referencias internas de Cabecera y totales hacia el ticket. */
					ticketAux.getTicket().getCabecera().inicializarCabecera(ticketNegativo.getTicket());
					ticketAux.getTicket().getCabecera().setUidTicketEnlace(ticketNegativo.getTicket().getUidTicket());
					ticketAux.getTicket().setCajero(sesion.getSesionUsuario().getUsuario());
					ticketAux.getTicket().getCabecera().getTotales().setCambio(SpringContext.getBean(PagoTicket.class, MediosPagosService.medioPagoDefecto));

					ticketAux.getTicket().getCabecera().setDatosDocOrigen(datosOrigen);

					ticketAux.getTicket().getCabecera().setDatosFidelizado(ticketManager.getTicket().getCabecera().getDatosFidelizado());
					ticketAux.getTicket().getCabecera().setIdTicket(ticketManager.getTicket().getCabecera().getIdTicket());
					ticketAux.getTicket().getCabecera().setCodTicket(ticketManager.getTicket().getCabecera().getCodTicket());
					ticketAux.getTicket().getCabecera().setSerieTicket(ticketManager.getTicket().getCabecera().getSerieTicket());
					ticketAux.getTicket().getCabecera().setUidTicket(ticketManager.getTicket().getCabecera().getUidTicket());

					/* Calculamos el total del medio de pago y añadimos las lineas. */
					for (LineaTicket lineaPositivo : listTicketPositivo) {
						/* Añadimos las lineas al ticket nuevo. */
						LineaTicket linea = ticketAux.nuevaLineaArticulo(lineaPositivo.getCodArticulo(), lineaPositivo.getDesglose1(), lineaPositivo.getDesglose2(), lineaPositivo.getCantidad(), null);
						linea.setPrecioTotalSinDto(lineaPositivo.getPrecioTotalSinDto());
						linea.setPrecioConDto(lineaPositivo.getPrecioConDto());
						linea.setPrecioTotalConDto(lineaPositivo.getPrecioTotalConDto());
						linea.setImporteConDto(lineaPositivo.getImporteConDto());
						linea.setImporteTotalConDto(lineaPositivo.getImporteTotalConDto());
						linea.setDescuentoManual(lineaPositivo.getDescuentoManual());
						linea.setLineaDocumentoOrigen(lineaPositivo.getLineaDocumentoOrigen());
						linea.setPromociones(lineaPositivo.getPromociones());
						linea.setVendedor(lineaPositivo.getVendedor());

						// Inicio GAP 1585
						if (lineaPositivo instanceof ByLLineaTicket) { /*
						                                                * Si es un ticket de Navision, no viene con el
						                                                * xsi:type=bylLineaTicket, por lo que fallaría
						                                                * al realizar el casting
						                                                */
							if (((ByLLineaTicket) lineaPositivo).getNotaInformativa() != null) {
								((ByLLineaTicket) linea).setNotaInformativa(((ByLLineaTicket) lineaPositivo).getNotaInformativa());
							}
						}
						// Fin GAP 1585
					}
					List<PagoTicket> listadoPagosManager = new ArrayList<PagoTicket>();
					PagoTicket pagoDevoPositivo = SpringContext.getBean(PagoTicket.class);
					pagoDevoPositivo.setMedioPago(medioPagoDevo);
					pagoDevoPositivo.setImporte(totalDevo.negate());
					listadoPagosManager.add(pagoDevoPositivo);
					listadoPagosManager.addAll(((ArrayList<PagoTicket>) ticketManager.getTicket().getPagos()));

					// Si el importe que tenemos que entregar al cliente es mayor del total a devolver, añadiremos el
					// cambio como linea de pago para evitar descuadres en caja.
					if (ticketManager.getTicket().getCabecera().getTotales().getCambio() != null
					        && BigDecimalUtil.isMenor(ticketManager.getTicket().getCabecera().getTotales().getCambio().getImporte(), new BigDecimal(0))) {
						PagoTicket pagoCambio = SpringContext.getBean(PagoTicket.class);
						pagoCambio.setMedioPago(ticketManager.getTicket().getCabecera().getTotales().getCambio().getMedioPago());
						pagoCambio.setImporte(ticketManager.getTicket().getCabecera().getTotales().getCambio().getImporte().negate());
						listadoPagosManager.add(pagoCambio);
					}
					ticketAux.getTicket().setPagos(listadoPagosManager);
				}
				else {
					/* Insertamos las lineas en negativo, y los medios de pago del ticketManager. */
					for (LineaTicket lineaPositiva : listTicketPositivo) {
						/* Añadimos el importe al total. */
						ticketAux.nuevaLineaArticulo(lineaPositiva.getCodArticulo(), lineaPositiva.getDesglose1(), lineaPositiva.getDesglose2(), lineaPositiva.getCantidad(), null);
					}
					ticketAux.getTicket().setPagos(ticketManager.getTicket().getPagos());

					if (!esDevolucionSinTicket) {
						/* Indicamos los datos de Origen del Ticket */
						ticketAux.getTicket().getCabecera().setDatosDocOrigen(ticketManager.getTicket().getCabecera().getDatosDocOrigen());
						ticketAux.getTicket().getCabecera().setUidTicketEnlace(ticketManager.getTicketOrigen().getUidTicket());
					}

					ticketAux.getTicket().getCabecera().setDatosFidelizado(ticketManager.getTicket().getCabecera().getDatosFidelizado());

					ticketAux.getTicket().getCabecera().setIdTicket(ticketManager.getTicket().getCabecera().getIdTicket());
					ticketAux.getTicket().getCabecera().setCodTicket(ticketManager.getTicket().getCabecera().getCodTicket());
					ticketAux.getTicket().getCabecera().setSerieTicket(ticketManager.getTicket().getCabecera().getSerieTicket());
					ticketAux.getTicket().getCabecera().setUidTicket(ticketManager.getTicket().getCabecera().getUidTicket());
				}

				/* Si existe pago con tarjeta regalo sin UidTransacción se crea */
				for (Object obj : ticketAux.getTicket().getPagos()) {
					PagoTicket pago = (PagoTicket) obj;
					if (pago.getGiftcards() != null) {
						for (GiftCardBean giftcard : pago.getGiftcards()) {
							if (giftcard.getUidTransaccion() == null || giftcard.getUidTransaccion().isEmpty()) {
								giftcard.setUidTransaccion(UUID.randomUUID().toString());
							}
						}
					}
				}
				/* Realizamos el recalcular para los totales entregados */
				ticketAux.getTicket().getCabecera().getTotales().recalcular();

				log.debug("divisionTicketDevolucion() - Datos del Ticket Positivo : ");
				log.debug("divisionTicketDevolucion() - IdTicket : " + ticketAux.getTicket().getCabecera().getIdTicket());
				log.debug("divisionTicketDevolucion() - Código Ticket : " + ticketAux.getTicket().getCabecera().getCodTicket());
				log.debug("divisionTicketDevolucion() - Documento Activo : " + ticketAux.getDocumentoActivo());
				if (ticketAux.getTicket().getCabecera().getDatosFidelizado() != null) {
					log.debug("divisionTicketDevolucion() - Fidelizado Ticket Negativo : " + ticketAux.getTicket().getCabecera().getDatosFidelizado().getIdFidelizado());
				}

				/* Ponemos la fecha actual y guardamos el Ticket Positivo */
				ticketAux.getTicket().getCabecera().setFormatoImpresion(ticketManager.getTicket().getCabecera().getFormatoImpresion());
				ticketAux.getTicket().getCabecera().setFecha(new Date());

//				/* Para los casos en los que se recuperen devoluciones de ventas previas a comerzzia */
//				if (ticketAux.getTicket().getCliente().getIdGrupoImpuestos() == null) {
//					ticketAux.getTicket().getCliente().setIdGrupoImpuestos(sesion.getAplicacion().getTienda().getCliente().getIdGrupoImpuestos());
//				}
				if (StringUtils.isBlank(ticketAux.getTicket().getCliente().getCif())) {
					ticketAux.getTicket().getCliente().setCif(sesion.getAplicacion().getTienda().getCliente().getCif());
				}

			}
			catch (PromocionesServiceException | DocumentoException | LineaTicketException | DocumentoNotFoundException e1) {
				accionSalvarTicketOnFailure(e1);
				enProceso = false;
			}

			if (Dispositivos.getInstance().getImpresora1() instanceof EpsonTM30) {
				if (Dispositivos.getInstance().getImpresora1().getConfiguracion().getNombreConexion().equals(EpsonTSEService.NOMBRE_CONEXION_TSE)) {
					Boolean correcto = tseStartTransaction(ticketAux);
					if (!correcto) {
						VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("No se ha podido realizar la conexión con el TSE, ¿Desea continuar sin TSE?"), getStage());
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void tratarTicketPositivoProfesional(MedioPagoBean medioPagoDevo) {
		TicketVentaProfesional ticketPrincipal = (TicketVentaProfesional) ticketManager.getTicket();
		if (!listTicketPositivo.isEmpty()) {
			log.debug("divisionTicketDevolucion() - Iniciamos la creación del Ticket Positivo...");
			ticketAuxProfesional = SpringContext.getBean(ByLVentaProfesionalManager.class);

			try {
				/* Creamos el Ticket, le indicamos el tipo de documento y los datos del Ticket */
				ticketAuxProfesional.nuevoTicket();

				ticketAuxProfesional.setDevolucionSinTicket(esDevolucionSinTicket);

				if (!esDevolucionSinTicket) {
					List<TipoDocumentoBean> listadoTipoDocu = tipoDocuService.consultarTiposDocumentos(sesion.getAplicacion().getUidActividad(),
					        sesion.getAplicacion().getTienda().getCliente().getCodpais());
					for (TipoDocumentoBean tipoDocu : listadoTipoDocu) {
						if (tipoDocu.getCodtipodocumento().equals(ticketManager.getTicket().getCabecera().getCodTipoDocumento())) {
							ticketAuxProfesional.setDocumentoActivo(tipoDocu);
						}
					}

					/* Indicamos el Ticket Origen de la Devolución */
					ticketAuxProfesional.setTicketOrigen(ticketNegativoProfesional.getTicketOrigen());
					/* Indicamos los datos del Cliente y los datos del Fidelizado */
					ticketAuxProfesional.getTicket().setCliente(ticketManager.getTicketOrigen().getCliente());
				}

				if (ticketManager.isEsOperacionTarjetaRegalo()) {
					ticketAuxProfesional.setEsOperacionTarjetaRegalo(true);
				}
				else {
					ticketAuxProfesional.setEsOperacionTarjetaRegalo(false);
				}

				/*
				 * Ahora insertamos los Medios de Pago y las Lineas del Ticket pero sino tiene lineas en negativo,
				 * realizamos la operación como si fuera un ticket de venta, ya que los pagos, lineas, y origen serán
				 * los mismos.
				 */
				if (!listTicketNegativo.isEmpty()) {
					/* Indicamos los datos de Origen del Ticket */
					DatosDocumentoOrigenTicket datosOrigen = new DatosDocumentoOrigenTicket();
					datosOrigen.setCaja(ticketNegativoProfesional.getTicket().getCabecera().getCodCaja());
					datosOrigen.setCodTipoDoc(ticketNegativoProfesional.getTicket().getCabecera().getCodTipoDocumento());
					datosOrigen.setIdTipoDoc(ticketNegativoProfesional.getTicket().getCabecera().getTipoDocumento());
					datosOrigen.setNumFactura(ticketNegativoProfesional.getTicket().getIdTicket());
					datosOrigen.setSerie(ticketNegativoProfesional.getTicket().getCabecera().getTienda().getCodAlmacen());
					datosOrigen.setFecha(ticketNegativoProfesional.getTicket().getCabecera().getFechaAsLocale());
					datosOrigen.setDesTipoDoc(ticketNegativoProfesional.getTicket().getCabecera().getDesTipoDocumento());
					datosOrigen.setUidTicket(ticketNegativoProfesional.getTicket().getUidTicket());
					datosOrigen.setCodTicket(ticketNegativoProfesional.getTicket().getCabecera().getCodTicket());
					datosOrigen.setTienda(ticketNegativoProfesional.getTicket().getTienda().getCodAlmacen());

					/* Seteamos las referencias internas de Cabecera y totales hacia el ticket. */
					ticketAuxProfesional.getTicket().getCabecera().inicializarCabecera(ticketNegativoProfesional.getTicket());
					ticketAuxProfesional.getTicket().getCabecera().setUidTicketEnlace(ticketNegativoProfesional.getTicket().getUidTicket());
					ticketAuxProfesional.getTicket().setCajero(sesion.getSesionUsuario().getUsuario());
					ticketAuxProfesional.getTicket().getCabecera().getTotales().setCambio(SpringContext.getBean(PagoTicket.class, MediosPagosService.medioPagoDefecto));

					ticketAuxProfesional.getTicket().getCabecera().setDatosDocOrigen(datosOrigen);

					ticketAuxProfesional.getTicket().getCabecera().setDatosFidelizado(ticketManager.getTicket().getCabecera().getDatosFidelizado());
					ticketAuxProfesional.getTicket().getCabecera().setIdTicket(ticketManager.getTicket().getCabecera().getIdTicket());
					ticketAuxProfesional.getTicket().getCabecera().setCodTicket(ticketManager.getTicket().getCabecera().getCodTicket());
					ticketAuxProfesional.getTicket().getCabecera().setSerieTicket(ticketManager.getTicket().getCabecera().getSerieTicket());
					ticketAuxProfesional.getTicket().getCabecera().setUidTicket(ticketManager.getTicket().getCabecera().getUidTicket());

					/* Calculamos el total del medio de pago y añadimos las lineas. */
					for (LineaTicket lineaPositivo : listTicketPositivo) {
						/* Añadimos las lineas al ticket nuevo. */
						ByLLineaTicketProfesional linea = (ByLLineaTicketProfesional) ticketAuxProfesional.nuevaLineaArticulo(lineaPositivo.getCodArticulo(), lineaPositivo.getDesglose1(),
						        lineaPositivo.getDesglose2(), lineaPositivo.getCantidad(), null);
						linea.setPrecioSinDto(lineaPositivo.getPrecioSinDto());
						linea.setPrecioTotalSinDto(lineaPositivo.getPrecioTotalSinDto());
						linea.setPrecioConDto(lineaPositivo.getPrecioConDto());
						linea.setPrecioTotalConDto(lineaPositivo.getPrecioTotalConDto());
						linea.setImporteConDto(lineaPositivo.getImporteConDto());
						linea.setImporteTotalConDto(lineaPositivo.getImporteTotalConDto());
						linea.setDescuentoManual(lineaPositivo.getDescuentoManual());
						linea.setLineaDocumentoOrigen(lineaPositivo.getLineaDocumentoOrigen());
						linea.setPromociones(lineaPositivo.getPromociones());
						linea.setVendedor(lineaPositivo.getVendedor());
						
						if(vertexservice.integracionImpuestosVertexActiva(ticketManager.getTicket().getCabecera().getTipoDocumento())) {
							
							linea.setListaImpuestosVertex(((ByLLineaTicketProfesional)lineaPositivo).getListaImpuestosVertex());
							linea.setLineaVertex(((ByLLineaTicketProfesional)lineaPositivo).getLineaVertex());
							 
						 }

						// Inicio GAP 1585
						if (lineaPositivo instanceof ByLLineaTicketProfesional) { /*
						                                                           * Si es un ticket de Navision, no
						                                                           * viene con el
						                                                           * xsi:type=bylLineaTicket, por lo que
						                                                           * fallaría al realizar el casting
						                                                           */
							if (((ByLLineaTicketProfesional) lineaPositivo).getNotaInformativa() != null) {
								((ByLLineaTicketProfesional) linea).setNotaInformativa(((ByLLineaTicketProfesional) lineaPositivo).getNotaInformativa());
							}
						}
						// Fin GAP 1585
						
					 
					}
					List<PagoTicket> listadoPagosManager = new ArrayList<PagoTicket>();
					PagoTicket pagoDevoPositivo = SpringContext.getBean(PagoTicket.class);
					pagoDevoPositivo.setMedioPago(medioPagoDevo);
					pagoDevoPositivo.setImporte(totalDevo.negate());
					listadoPagosManager.add(pagoDevoPositivo);
					listadoPagosManager.addAll(((ArrayList<PagoTicket>) ticketManager.getTicket().getPagos()));

					// Si el importe que tenemos que entregar al cliente es mayor del total a devolver, añadiremos el
					// cambio como linea de pago para evitar descuadres en caja.
					if (ticketManager.getTicket().getCabecera().getTotales().getCambio() != null
					        && BigDecimalUtil.isMenor(ticketManager.getTicket().getCabecera().getTotales().getCambio().getImporte(), new BigDecimal(0))) {
						PagoTicket pagoCambio = SpringContext.getBean(PagoTicket.class);
						pagoCambio.setMedioPago(ticketManager.getTicket().getCabecera().getTotales().getCambio().getMedioPago());
						pagoCambio.setImporte(ticketManager.getTicket().getCabecera().getTotales().getCambio().getImporte().negate());
						listadoPagosManager.add(pagoCambio);
					}
					ticketAuxProfesional.getTicket().setPagos(listadoPagosManager);
				}
				else {
					/* Insertamos las lineas en negativo, y los medios de pago del ticketManager. */
					for (LineaTicket lineaPositiva : listTicketPositivo) {
						/* Añadimos el importe al total. */
						ticketAuxProfesional.nuevaLineaArticulo(lineaPositiva.getCodArticulo(), lineaPositiva.getDesglose1(), lineaPositiva.getDesglose2(), lineaPositiva.getCantidad(), null);
					}
					ticketAuxProfesional.getTicket().setPagos(ticketManager.getTicket().getPagos());

					if (!esDevolucionSinTicket) {
						/* Indicamos los datos de Origen del Ticket */
						ticketAuxProfesional.getTicket().getCabecera().setDatosDocOrigen(ticketManager.getTicket().getCabecera().getDatosDocOrigen());
						ticketAuxProfesional.getTicket().getCabecera().setUidTicketEnlace(ticketManager.getTicketOrigen().getUidTicket());
					}

					ticketAuxProfesional.getTicket().getCabecera().setDatosFidelizado(ticketManager.getTicket().getCabecera().getDatosFidelizado());

					ticketAuxProfesional.getTicket().getCabecera().setIdTicket(ticketManager.getTicket().getCabecera().getIdTicket());
					ticketAuxProfesional.getTicket().getCabecera().setCodTicket(ticketManager.getTicket().getCabecera().getCodTicket());
					ticketAuxProfesional.getTicket().getCabecera().setSerieTicket(ticketManager.getTicket().getCabecera().getSerieTicket());
					ticketAuxProfesional.getTicket().getCabecera().setUidTicket(ticketManager.getTicket().getCabecera().getUidTicket());
				}

				/* Si existe pago con tarjeta regalo sin UidTransacción se crea */
				for (Object obj : ticketAuxProfesional.getTicket().getPagos()) {
					PagoTicket pago = (PagoTicket) obj;
					if (pago.getGiftcards() != null) {
						for (GiftCardBean giftcard : pago.getGiftcards()) {
							if (giftcard.getUidTransaccion() == null || giftcard.getUidTransaccion().isEmpty()) {
								giftcard.setUidTransaccion(UUID.randomUUID().toString());
							}
						}
					}
				}
				/* Realizamos el recalcular para los totales entregados */
				ticketAuxProfesional.getTicket().getCabecera().getTotales().recalcular();

				log.debug("divisionTicketDevolucion() - Datos del Ticket Positivo : ");
				log.debug("divisionTicketDevolucion() - IdTicket : " + ticketAuxProfesional.getTicket().getCabecera().getIdTicket());
				log.debug("divisionTicketDevolucion() - Código Ticket : " + ticketAuxProfesional.getTicket().getCabecera().getCodTicket());
				log.debug("divisionTicketDevolucion() - Documento Activo : " + ticketAuxProfesional.getDocumentoActivo());
				if (ticketAuxProfesional.getTicket().getCabecera().getDatosFidelizado() != null) {
					log.debug("divisionTicketDevolucion() - Fidelizado Ticket Negativo : " + ticketAuxProfesional.getTicket().getCabecera().getDatosFidelizado().getIdFidelizado());
				}

				/* Ponemos la fecha actual y guardamos el Ticket Positivo */
				ticketAuxProfesional.getTicket().getCabecera().setFormatoImpresion(ticketManager.getTicket().getCabecera().getFormatoImpresion());
				ticketAuxProfesional.getTicket().getCabecera().setFecha(new Date());
			}
			catch (PromocionesServiceException | DocumentoException | LineaTicketException | DocumentoNotFoundException e1) {
				accionSalvarTicketOnFailure(e1);
				enProceso = false;
			}

			if (Dispositivos.getInstance().getImpresora1() instanceof EpsonTM30) {
				if (Dispositivos.getInstance().getImpresora1().getConfiguracion().getNombreConexion().equals(EpsonTSEService.NOMBRE_CONEXION_TSE)) {
					Boolean correcto = tseStartTransaction(ticketAuxProfesional);
					if (!correcto) {
						VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("No se ha podido realizar la conexión con el TSE, ¿Desea continuar sin TSE?"), getStage());
					}
				}
			}
		}
		ticketManager.setTicket(ticketPrincipal);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void tratarTicketNegativo(MedioPagoBean medioPagoDevo) throws PromocionesServiceException, DocumentoException, LineaTicketException, TicketsServiceException {
		if (!listTicketNegativo.isEmpty()) {
			log.debug("divisionTicketDevolucion() - Iniciamos la creación del Ticket Negativo...");
			/* Creamos el Ticket, le indicamos el tipo de documento y los datos del Fidelizado */
			ticketNegativo.nuevoTicket();
			ticketNegativo.setEsDevolucion(true);

			ticketNegativo.setDevolucionSinTicket(esDevolucionSinTicket);
			if (!esDevolucionSinTicket) {
				ticketNegativo.setDocumentoActivo(documentos.getDocumentoAbono(ticketManager.getTicketOrigen().getCabecera().getCodTipoDocumento()));
			}
			else {
				ticketNegativo.setDocumentoActivo(documentos.getDocumentoAbono(sesion.getAplicacion().getDocumentos().getDocumento(Documentos.FACTURA_SIMPLIFICADA).getCodtipodocumento()));
			}
			ticketNegativo.getTicket().getCabecera().setDatosFidelizado(ticketManager.getTicket().getCabecera().getDatosFidelizado());

			if (!esDevolucionSinTicket) {
				/* Indicamos los datos del Cliente y los datos relaciones con devoluciones de tarjeta regalo */
				ticketNegativo.getTicket().setCliente(ticketManager.getTicketOrigen().getCliente());
			}
			boolean esDevTarj = ticketManager.isDevolucionTarjetaRegalo();
			ticketNegativo.setEsDevolucionTarjetaRegalo(esDevTarj);
			ticketNegativo.getTicket().getCabecera().setTarjetaRegalo(ticketManager.getTicket().getCabecera().getTarjetaRegalo());
			((ByLCabeceraTicket) ticketNegativo.getTicket().getCabecera()).setTarjeta(((ByLCabeceraTicket) ticketManager.getTicket().getCabecera()).getTarjeta());
			/* Todos los datos relaciones con el Ticket del que lo estamos generando */
			ticketNegativo.getTicket().getCabecera().setIdTicket(ticketManager.getTicket().getCabecera().getIdTicket());

			/* CO - Para la integracion con EDICOM */
			tratamientoIntegracionEdicom();
			
			// Generamos el idTicket, codTicket, serieTicket y uidTicket del
			// ticketNegativo antes del salvarTicket para poderlo insertar en el
			// documentoOrigen del ticketPositivo (en caso de tenerlo)
			ticketsService.setContadorIdTicket((Ticket) ticketNegativo.getTicket());

			/* Ahora insertamos los Medios de Pago y las Lineas del Ticket */
			if (!listTicketPositivo.isEmpty()) {
				PagoTicket pagoDevoNegativo = SpringContext.getBean(PagoTicket.class);
				pagoDevoNegativo.setMedioPago(medioPagoDevo);
				for (LineaTicket lineaNegativa : listTicketNegativo) {
					/* Añadimos el importe al total. */
					totalDevo = totalDevo.add(lineaNegativa.getImporteTotalConDto());
					LineaTicket linea = ticketNegativo.nuevaLineaArticulo(lineaNegativa.getCodArticulo(), lineaNegativa.getDesglose1(), lineaNegativa.getDesglose2(), lineaNegativa.getCantidad(),
					        null);
					linea.setPrecioTotalSinDto(lineaNegativa.getPrecioTotalSinDto());
					linea.setPrecioConDto(lineaNegativa.getPrecioConDto());
					linea.setPrecioTotalConDto(lineaNegativa.getPrecioTotalConDto());
					linea.setImporteConDto(lineaNegativa.getImporteConDto());
					linea.setImporteTotalConDto(lineaNegativa.getImporteConDto());
					linea.setDescuentoManual(lineaNegativa.getDescuentoManual());
					linea.setLineaDocumentoOrigen(lineaNegativa.getLineaDocumentoOrigen());
					linea.setVendedor(lineaNegativa.getVendedor());
				}
				pagoDevoNegativo.setImporte(totalDevo);
				List<PagoTicket> listadoPagos = new ArrayList<PagoTicket>();
				listadoPagos.add(pagoDevoNegativo);
				ticketNegativo.getTicket().setPagos(listadoPagos);
			}
			else {
				/* Insertamos las lineas en negativo, y los medios de pago del ticketManager. */
				for (LineaTicket lineaNegativa : listTicketNegativo) {
					/* Añadimos el importe al total. */
					LineaTicket linea = ticketNegativo.nuevaLineaArticulo(lineaNegativa.getCodArticulo(), lineaNegativa.getDesglose1(), lineaNegativa.getDesglose2(), lineaNegativa.getCantidad(),
					        null);
					linea.setPrecioSinDto(lineaNegativa.getPrecioSinDto());
					linea.setPrecioTotalSinDto(lineaNegativa.getPrecioTotalSinDto());
					linea.setPrecioConDto(lineaNegativa.getPrecioConDto());
					linea.setPrecioTotalConDto(lineaNegativa.getPrecioTotalConDto());
					linea.setImporteConDto(lineaNegativa.getImporteConDto());
					linea.setImporteTotalConDto(lineaNegativa.getImporteTotalConDto());
					linea.setDescuentoManual(lineaNegativa.getDescuentoManual());
					linea.setLineaDocumentoOrigen(lineaNegativa.getLineaDocumentoOrigen());
					linea.setVendedor(lineaNegativa.getVendedor());

					/* Trazabilidad */
					((ByLLineaTicket) linea).setTrazabilidad(((ByLLineaTicket) lineaNegativa).getTrazabilidad());
				}
				ticketNegativo.getTicket().setPagos(ticketManager.getTicket().getPagos());
			}

			/* Si existe pago con tarjeta regalo sin uidTransacción se crea */
			if (ticketNegativo.getTicket().getPagos() != null) {
				for (Object obj : ticketNegativo.getTicket().getPagos()) {
					PagoTicket pago = (PagoTicket) obj;
					if (pago.getGiftcards() != null) {
						for (GiftCardBean giftcard : pago.getGiftcards()) {
							if (giftcard.getUidTransaccion() == null || giftcard.getUidTransaccion().isEmpty()) {
								giftcard.setUidTransaccion(UUID.randomUUID().toString());
							}
						}
					}
				}
			}
			/* Realizamos el recalcular para los totales entregados */
			ticketNegativo.getTicket().getCabecera().getTotales().recalcular();

			if (!esDevolucionSinTicket) {
				/* Indicamos los datos de origen de la devolución */
				ticketNegativo.getTicket().getCabecera().setDatosDocOrigen(ticketManager.getTicket().getCabecera().getDatosDocOrigen());
				ticketNegativo.getTicket().getCabecera().setUidTicketEnlace(ticketManager.getTicketOrigen().getUidTicket());
				ticketNegativo.setTicketOrigen(ticketManager.getTicketOrigen());
			}
			if (ticketManager.isEsOperacionTarjetaRegalo()) {
				ticketNegativo.setEsOperacionTarjetaRegalo(true);
			}
			else {
				ticketNegativo.setEsOperacionTarjetaRegalo(false);
			}

			log.debug("divisionTicketDevolucion() - Datos del Ticket Negativo : ");
			log.debug("divisionTicketDevolucion() - IdTicket : " + ticketNegativo.getTicket().getCabecera().getIdTicket());
			log.debug("divisionTicketDevolucion() - Código Ticket : " + ticketNegativo.getTicket().getCabecera().getCodTicket());
			log.debug("divisionTicketDevolucion() - Documento Activo : " + ticketNegativo.getDocumentoActivo());
			if (ticketNegativo.getTicket().getCabecera().getDatosFidelizado() != null) {
				log.debug("divisionTicketDevolucion() - Fidelizado Ticket Negativo : " + ticketNegativo.getTicket().getCabecera().getDatosFidelizado().getIdFidelizado());
			}

			/* Ponemos la fecha actual y guardamos el Ticket Negativo */
			ticketNegativo.getTicket().getCabecera().setFecha(new Date());

			/* Para los casos en los que se recuperen devoluciones de ventas previas a comerzzia */
			if (ticketNegativo.getTicket().getCabecera().getCliente().getIdGrupoImpuestos() == null) {
				ticketNegativo.getTicket().getCabecera().getCliente().setIdGrupoImpuestos(0);
			}
			
			if (StringUtils.isBlank(ticketNegativo.getTicket().getCliente().getCif())) {
				ticketNegativo.getTicket().getCliente().setCif(sesion.getAplicacion().getTienda().getCliente().getCif());
			}

			TipoDocumentoBean tipoDocumento = new TipoDocumentoBean();
			try {
				tipoDocumento = documentos.getDocumento(ticketManager.getTicket().getCabecera().getTipoDocumento());
			}
			catch (DocumentoException e1) {
				log.error("aceptar() - Ha ocurrido un error al buscar el tipo de documento. " + e1.getMessage());
			}

			if (tipoDocumento.isSignoPositivo()) {
				// Si el signo del documento está forzado en positivo (Ej: Chile), pondremos las cantidades y los pagos
				// en positivo
				for (int i = 0; i < ticketNegativo.getTicket().getLineas().size(); i++) {

					ByLLineaTicket lineaTicket = (ByLLineaTicket) ticketNegativo.getTicket().getLineas().get(i);

					lineaTicket.setCantidad(lineaTicket.getCantidad().abs());
					// lineaTicket.recalcularPreciosImportes();
					lineaTicket.recalcularImporteFinal();
				}

				for (Object obj : ticketNegativo.getTicket().getPagos()) {
					PagoTicket pago = (PagoTicket) obj;

					pago.setImporte(pago.getImporte().abs());
				}
			}

			if (Dispositivos.getInstance().getImpresora1() instanceof EpsonTM30) {
				if (Dispositivos.getInstance().getImpresora1().getConfiguracion().getNombreConexion().equals(EpsonTSEService.NOMBRE_CONEXION_TSE)) {
					Boolean correcto = tseStartTransaction(ticketNegativo);
					if (!correcto) {
						Boolean resultado = VentanaDialogoComponent.crearVentanaConfirmacion(I18N.getTexto("No se ha podido realizar la conexión con el TSE, ¿Desea continuar sin TSE?"), getStage());
						if (!resultado) {
							return;
						}
					}
				}
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void tratarTicketNegativoProfesional(MedioPagoBean medioPagoDevo) throws PromocionesServiceException, DocumentoException, LineaTicketException, TicketsServiceException {
		ByLTicketVentaAbono ticketOrigen = (ByLTicketVentaAbono) ticketManager.getTicketOrigen();
		ticketNegativoProfesional.setTicketOrigen(null);
		if (!listTicketNegativo.isEmpty()) {
			log.debug("divisionTicketDevolucion() - Iniciamos la creación del Ticket Negativo...");
			/* Creamos el Ticket, le indicamos el tipo de documento y los datos del Fidelizado */
			TicketVentaProfesional ticketPrincipal = (TicketVentaProfesional) ticketManager.getTicket();
			ticketNegativoProfesional.nuevoTicket();
			ticketNegativoProfesional.setEsDevolucion(true);

			ticketNegativoProfesional.setDevolucionSinTicket(esDevolucionSinTicket);
			if (!esDevolucionSinTicket) {
				ticketNegativoProfesional.setDocumentoActivo(documentos.getDocumentoAbono(ticketOrigen.getCabecera().getCodTipoDocumento()));
			}
			else {
				ticketNegativoProfesional.setDocumentoActivo(documentos.getDocumentoAbono(sesion.getAplicacion().getDocumentos().getDocumento(Documentos.FACTURA_SIMPLIFICADA).getCodtipodocumento()));
			}
			ticketNegativoProfesional.getTicket().getCabecera().setDatosFidelizado(ticketManager.getTicket().getCabecera().getDatosFidelizado());

			if (!esDevolucionSinTicket) {
				/* Indicamos los datos del Cliente y los datos relaciones con devoluciones de tarjeta regalo */
				ticketNegativoProfesional.getTicket().setCliente(ticketOrigen.getCliente());
			}
			boolean esDevTarj = ticketManager.isDevolucionTarjetaRegalo();
			ticketNegativoProfesional.setEsDevolucionTarjetaRegalo(esDevTarj);
			ticketNegativoProfesional.getTicket().getCabecera().setTarjetaRegalo(ticketManager.getTicket().getCabecera().getTarjetaRegalo());
			((ByLCabeceraTicket) ticketNegativoProfesional.getTicket().getCabecera()).setTarjeta(((ByLCabeceraTicket) ticketManager.getTicket().getCabecera()).getTarjeta());
			/* Todos los datos relaciones con el Ticket del que lo estamos generando */
			ticketNegativoProfesional.getTicket().getCabecera().setIdTicket(ticketManager.getTicket().getCabecera().getIdTicket());

			/* Cabecera Vertex*/
			((ByLCabeceraTicket) ticketNegativoProfesional.getTicket().getCabecera()).setCabeceraVertex(((ByLCabeceraTicket) ticketPrincipal.getCabecera()).getCabeceraVertex());
			
			/* Ahora insertamos los Medios de Pago y las Lineas del Ticket */
			if (!listTicketPositivo.isEmpty()) {
				PagoTicket pagoDevoNegativo = SpringContext.getBean(PagoTicket.class);
				pagoDevoNegativo.setMedioPago(medioPagoDevo);
				for (LineaTicket lineaNegativa : listTicketNegativo) {
					/* Añadimos el importe al total. */
					totalDevo = totalDevo.add(lineaNegativa.getImporteTotalConDto());
					ByLLineaTicketProfesional linea = (ByLLineaTicketProfesional) ticketNegativoProfesional.nuevaLineaArticulo(lineaNegativa.getCodArticulo(), lineaNegativa.getDesglose1(),
					        lineaNegativa.getDesglose2(), lineaNegativa.getCantidad(), null);
					linea.setPrecioSinDto(lineaNegativa.getPrecioSinDto());
					linea.setPrecioTotalSinDto(lineaNegativa.getPrecioTotalSinDto());
					linea.setPrecioConDto(lineaNegativa.getPrecioConDto());
					linea.setPrecioTotalConDto(lineaNegativa.getPrecioTotalConDto());
					linea.setImporteConDto(lineaNegativa.getImporteConDto());
					linea.setImporteTotalConDto(lineaNegativa.getImporteConDto());
					linea.setDescuentoManual(lineaNegativa.getDescuentoManual());
					linea.setLineaDocumentoOrigen(lineaNegativa.getLineaDocumentoOrigen());
					linea.setVendedor(lineaNegativa.getVendedor());
										
				}
				pagoDevoNegativo.setImporte(totalDevo);
				List<PagoTicket> listadoPagos = new ArrayList<PagoTicket>();
				listadoPagos.add(pagoDevoNegativo);
				ticketNegativoProfesional.getTicket().setPagos(listadoPagos);
			}
			else {
				/* Insertamos las lineas en negativo, y los medios de pago del ticketManager. */
				for (LineaTicket lineaNegativa : listTicketNegativo) {
					/* Añadimos el importe al total. */
					ByLLineaTicketProfesional linea = (ByLLineaTicketProfesional) ticketNegativoProfesional.nuevaLineaArticulo(lineaNegativa.getCodArticulo(), lineaNegativa.getDesglose1(),
					        lineaNegativa.getDesglose2(), lineaNegativa.getCantidad(), null);
					linea.setPrecioSinDto(lineaNegativa.getPrecioSinDto());
					linea.setPrecioTotalSinDto(lineaNegativa.getPrecioTotalSinDto());
					linea.setPrecioConDto(lineaNegativa.getPrecioConDto());
					linea.setPrecioTotalConDto(lineaNegativa.getPrecioTotalConDto());
					linea.setImporteConDto(lineaNegativa.getImporteConDto());
					linea.setImporteTotalConDto(lineaNegativa.getImporteTotalConDto());
					linea.setDescuentoManual(lineaNegativa.getDescuentoManual());
					linea.setLineaDocumentoOrigen(lineaNegativa.getLineaDocumentoOrigen());
					linea.setVendedor(lineaNegativa.getVendedor());
					
					if (((ByLLineaTicketProfesional) lineaNegativa).getLineaVertex() != null) {
						linea.setLineaVertex(((ByLLineaTicketProfesional) lineaNegativa).getLineaVertex());
						linea.setListaImpuestosVertex(((ByLLineaTicketProfesional) lineaNegativa).getListaImpuestosVertex());
						
						linea.recalcularImporteFinal();
					}
					/* Trazabilidad */
					// ((ByLLineaTicket) linea).setTrazabilidad(((ByLLineaTicket) lineaNegativa).getTrazabilidad());
				}
				ticketNegativoProfesional.getTicket().setPagos(ticketManager.getTicket().getPagos());
				ticketNegativoProfesional.getTicket().getCabecera().getTotales().recalcular();
			}

			/* Si existe pago con tarjeta regalo sin uidTransacción se crea */
			if (ticketNegativoProfesional.getTicket().getPagos() != null) {
				for (Object obj : ticketNegativoProfesional.getTicket().getPagos()) {
					PagoTicket pago = (PagoTicket) obj;
					if (pago.getGiftcards() != null) {
						for (GiftCardBean giftcard : pago.getGiftcards()) {
							if (giftcard.getUidTransaccion() == null || giftcard.getUidTransaccion().isEmpty()) {
								giftcard.setUidTransaccion(UUID.randomUUID().toString());
							}
						}
					}
				}
			}
			/* Realizamos el recalcular para los totales entregados */
			ticketNegativoProfesional.getTicket().getCabecera().getTotales().recalcular();

			if (!esDevolucionSinTicket) {
				/* Indicamos los datos de origen de la devolución */
				ticketNegativoProfesional.getTicket().getCabecera().setDatosDocOrigen(ticketManager.getTicket().getCabecera().getDatosDocOrigen());
				ticketNegativoProfesional.getTicket().getCabecera().setUidTicketEnlace(ticketOrigen.getUidTicket());
				ticketNegativoProfesional.setTicketOrigen(ticketOrigen);
			}
			if (ticketManager.isEsOperacionTarjetaRegalo()) {
				ticketNegativoProfesional.setEsOperacionTarjetaRegalo(true);
			}
			else {
				ticketNegativoProfesional.setEsOperacionTarjetaRegalo(false);
			}

			log.debug("divisionTicketDevolucion() - Datos del Ticket Negativo : ");
			log.debug("divisionTicketDevolucion() - IdTicket : " + ticketNegativoProfesional.getTicket().getCabecera().getIdTicket());
			log.debug("divisionTicketDevolucion() - Código Ticket : " + ticketNegativoProfesional.getTicket().getCabecera().getCodTicket());
			log.debug("divisionTicketDevolucion() - Documento Activo : " + ticketNegativoProfesional.getDocumentoActivo());
			if (ticketNegativoProfesional.getTicket().getCabecera().getDatosFidelizado() != null) {
				log.debug("divisionTicketDevolucion() - Fidelizado Ticket Negativo : " + ticketNegativoProfesional.getTicket().getCabecera().getDatosFidelizado().getIdFidelizado());
			}

			/* Ponemos la fecha actual y guardamos el Ticket Negativo */
			ticketNegativoProfesional.getTicket().getCabecera().setFecha(new Date());

			// Generamos el idTicket, codTicket, serieTicket y uidTicket del
			// ticketNegativo antes del salvarTicket para poderlo insertar en el
			// documentoOrigen del ticketPositivo (en caso de tenerlo)
			ticketsService.setContadorIdTicket((Ticket) ticketNegativoProfesional.getTicket());
			
			TipoDocumentoBean tipoDocumento = new TipoDocumentoBean();
			try {
				tipoDocumento = documentos.getDocumento(ticketManager.getTicket().getCabecera().getTipoDocumento());
			}
			catch (DocumentoException e1) {
				log.error("aceptar() - Ha ocurrido un error al buscar el tipo de documento. " + e1.getMessage());
			}

			if (tipoDocumento.isSignoPositivo()) {
				// Si el signo del documento está forzado en positivo (Ej: Chile), pondremos las cantidades y los pagos
				// en positivo
				for (int i = 0; i < ticketNegativoProfesional.getTicket().getLineas().size(); i++) {

					ByLLineaTicketProfesional lineaTicket = (ByLLineaTicketProfesional) ticketNegativoProfesional.getTicket().getLineas().get(i);

					lineaTicket.setCantidad(lineaTicket.getCantidad().abs());
					// lineaTicket.recalcularPreciosImportes();
					lineaTicket.recalcularImporteFinal();
				}

				for (Object obj : ticketNegativoProfesional.getTicket().getPagos()) {
					PagoTicket pago = (PagoTicket) obj;

					pago.setImporte(pago.getImporte().abs());
				}
			}

			if (Dispositivos.getInstance().getImpresora1() instanceof EpsonTM30) {
				if (Dispositivos.getInstance().getImpresora1().getConfiguracion().getNombreConexion().equals(EpsonTSEService.NOMBRE_CONEXION_TSE)) {
					Boolean correcto = tseStartTransaction(ticketNegativo);
					if (!correcto) {
						Boolean resultado = VentanaDialogoComponent.crearVentanaConfirmacion(I18N.getTexto("No se ha podido realizar la conexión con el TSE, ¿Desea continuar sin TSE?"), getStage());
						if (!resultado) {
							return;
						}
					}
				}
			}

			ticketManager.setTicket(ticketPrincipal);
		}
	}

	@Override
	protected void accionSalvarTicketOnFailure(Exception e) {
		String msg = I18N.getTexto("Error al salvar el ticket.");

		if (e instanceof TarjetaException) {
			msg = e.getMessage();
		}

		if (e.getCause() instanceof ProcesadorIdFiscalException || e.getCause() instanceof ProcesarDocumentoFiscalPAException) {
			msg = ((com.comerzzia.pos.util.exception.Exception) e.getCause()).getMessageI18N();
		}

		if (!(e instanceof TarjetaRegaloException)) { // Ya se muestra desde el dispositivo
			if (e instanceof TarjetaWarningException) {
				VentanaDialogoComponent.crearVentanaAviso(msg, getStage());
			}
			else {
				VentanaDialogoComponent.crearVentanaError(getStage(), msg, e);
			}
		}
	}

	private void reiniciaDatosGiftCard() {
		tarjetaRegalo = null;
		lbSaldo.setText("");
	}

	private Boolean realizarVOIDTaxFree(final String numeroFormulario) {
		String rutaCarpeta = variablesServices.getVariableAsString(TaxFreeLeerXML.TAXFREE_RUTA_CARPETA);
		String rutaEjecutable = rutaCarpeta + "\\" + TaxFreeLeerXML.NOMBRE_EJECUTABLE;
		String rutaVoucher = rutaCarpeta + "\\" + TaxFreeLeerXML.NOMBRE_CARPETA_VOUCHER;

		Boolean anulacionCorrecta = Boolean.FALSE;

		TaxFreeProcesador p = new TaxFreeProcesador(rutaCarpeta, rutaEjecutable);
		p.run("-Void:" + numeroFormulario);

		log.debug("realizarVOIDTaxFree() - Finalizada peticion de anulación de taxFree");
		log.debug("realizarVOIDTaxFree() - Comprobamos si la aplicación ha dejado el fichero de anulación");

		File directorioVoucher = new File(rutaVoucher);
		File[] matchingFiles = directorioVoucher.listFiles(new FilenameFilter(){

			@Override
			public boolean accept(File dir, String name) {
				return name.contains(numeroFormulario) && name.endsWith("VOIDED.xml");
			}
		});

		if (matchingFiles != null && matchingFiles.length > 0) {
			anulacionCorrecta = Boolean.TRUE;

			File ficheroEncontrado = matchingFiles[0];
			File ficheroRenombrado = new File(ficheroEncontrado.getAbsolutePath() + ".CZZ");
			ficheroEncontrado.renameTo(ficheroRenombrado);

			log.debug("realizarVOIDTaxFree() - Se ha encontrado el fichero de anulación.");
		}

		log.debug("realizarVOIDTaxFree() - Finalizada la comprobación de ficheros. Se han encontrado " + matchingFiles.length + " ficheros.");

		return anulacionCorrecta;
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
	private void comprobarAptoRealizarTaxFree() {
		List<PagoTicket> pagos = ((TicketVenta) ticketManager.getTicket()).getPagos();
		String codIso = null;
		boolean taxFreeCorrecto = false;

		if (pagos != null && !pagos.isEmpty()) {
			for (PagoTicket pagoTicket : pagos) {

				if (pagoTicket.getDatosRespuestaPagoTarjeta() != null) {
					if (pagoTicket.getDatosRespuestaPagoTarjeta().getDatosPeticion() instanceof AdyenDatosPeticionPagoTarjeta) {

						Map<String, String> adicionales = pagoTicket.getDatosRespuestaPagoTarjeta().getAdicionales();

						if (adicionales != null && pagoTicket.getDatosRespuestaPagoTarjeta().getDatosPeticion() instanceof AdyenDatosPeticionPagoTarjeta) {

							JsonElement root = new JsonParser().parse(adicionales.get("additionalData"));
							codIso = root.getAsJsonObject().get("additionalData").getAsJsonObject().get("cardIssuerCountryId") != null
							        ? root.getAsJsonObject().get("additionalData").getAsJsonObject().get("cardIssuerCountryId").getAsString()
							        : null;
						}

					}
				}
			}

			log.debug("comprobarAptoRealizarTaxFree() - codigo ISO recibido " + codIso);

			if (codIso != null && !codIso.isEmpty()) {
				String rutaCarpeta = variablesServices.getVariableAsString(TaxFreeLeerXML.TAXFREE_RUTA_CARPETA);
				String rutaEjecutable = rutaCarpeta + "\\" + TaxFreeLeerXML.NOMBRE_EJECUTABLE;

				TaxFreeProcesador p = new TaxFreeProcesador(rutaCarpeta, rutaEjecutable);
				String respuesta = p.run("-Command:PerformCountrySearch -Iso:" + codIso);

				log.debug("comprobarAptoRealizarTaxFree() - Respuesta de PII sobre el codigo ISO [" + codIso + "] - " + respuesta);

				if (respuesta != null && respuesta.equalsIgnoreCase("true")) {
					if (VentanaDialogoComponent.crearVentanaConfirmacion(I18N.getTexto("La venta es apta para TAXFREE, ¿desea realizar taxfree?"), getStage())) {
						try {
							taxFreeCorrecto = ((ByLTicketsService) ticketsService).accionTaxFree(ticketManager.getTicket());
						}
						catch (Exception e) {
							String msg = "Se ha producido un error al generar el taxfree con voucher " + respuesta + " : " + e.getMessage();
							log.error("comprobarAptoRealizarTaxFree() - " + msg, e);
							VentanaDialogoComponent.crearVentanaError(I18N.getTexto("Se ha generado el Voucher pero no se ha podido guardar en BBDD"), this.getStage());
						}
					}
				}
			}

			if (taxFreeCorrecto) {
				VentanaDialogoComponent.crearVentanaInfo(I18N.getTexto("Se ha generado correctamente el taxfree"), this.getStage());
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Boolean tseStartTransaction(TicketManager ticketManager) {
		try {
			List<PagoTicket> pagos = ((TicketVenta) ticketManager.getTicket()).getPagos();
			BigDecimal pagosEfectivo = null;
			BigDecimal pagosNoEfectivo = null;
			for (PagoTicket pagoTicket : pagos) {
				if (pagoTicket.getCodMedioPago().equals("0000")) {
					pagosEfectivo = new BigDecimal(0);
					pagosEfectivo = pagosEfectivo.add(pagoTicket.getImporte());
				}
				else {
					pagosNoEfectivo = new BigDecimal(0);
					pagosNoEfectivo.add(pagoTicket.getImporte());
				}
			}

			String peticionStartTransaction = epsonTSEService.startTransaction(EpsonTSEService.PROCESSTYPE_KASSENBELEG_V1, EpsonTSEService.TIPO_TRANSACCION_BELEG,
			        ticketManager.getTicket().getTotales().getTotal(), pagosEfectivo, pagosNoEfectivo, false);
			epsonTSEService.enviarPeticion(peticionStartTransaction);
			String respuestaStartTransaction = epsonTSEService.lecturaSocket();

			List<String> listaCampos = new ArrayList<String>();
			listaCampos.add("logTime");
			listaCampos.add("transactionNumber");
			listaCampos.add("serialNumber");
			listaCampos.add("signature");
			listaCampos.add("signatureCounter");
			HashMap<String, String> mapaCampos = epsonTSEService.tratamientoRespuesta(respuestaStartTransaction, listaCampos);

			String result = epsonTSEService.tratamientoRespuestaResult(respuestaStartTransaction);
			if (result.equals(EpsonTSEService.EXECUTION_OK)) {
				String logTimeStart = mapaCampos.get("logTime");
				String transactionNumber = mapaCampos.get("transactionNumber");
				String serialNumber = mapaCampos.get("serialNumber");
				String signature = mapaCampos.get("signature");
				String signatureCounter = mapaCampos.get("signatureCounter");

				EposOutput eposOutput = new EposOutput();
				eposOutput.setLogTimeStart(logTimeStart);
				eposOutput.setTransactionNumber(transactionNumber);
				eposOutput.setSerialNumber(serialNumber);
				eposOutput.setSignature(signature);
				eposOutput.setSignatureCounter(signatureCounter);

				((ByLCabeceraTicket) ticketManager.getTicket().getCabecera()).setTse(eposOutput);
			}
			else {
				return false;
			}

			return true;
		}
		catch (Exception e) {
			log.warn("TSE() - Error al realizar el proceso de TSE -" + e.getMessage());
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	private Boolean comprobarStandAlone() {
		log.debug("comprobarStandAlone() - Inicio comprobación de stand alone");

		Boolean correcto = false;
		Boolean esStandAlone = false;
		PagoTicket pagoStandAlone = null;
		for (Object pago : ticketManager.getTicket().getPagos()) {
			if (pago instanceof PagoTicket && ((PagoTicket) pago).getDatosRespuestaPagoTarjeta() != null) {
				break;
			}
			if (((PagoTicket) pago).getCodMedioPago().equals(COD_MP_PINPAD_ADYEN_STANDALONE)) {
				pagoStandAlone = (PagoTicket) pago;
				esStandAlone = true;
				log.debug("comprobarStandAlone() - Es un pago standAlone");
			}
		}
		if (esStandAlone) {
			HashMap<String, Object> parametrosReferencia = new HashMap<String, Object>();
			getApplication().getMainView().showModalCentered(ReferenciaStandaloneView.class, parametrosReferencia, this.getStage());

			if (parametrosReferencia.get(ReferenciaStandaloneController.PARAMETRO_NUM_REFERENCIA) != null) {
				String numReferencia = (String) parametrosReferencia.get(ReferenciaStandaloneController.PARAMETRO_NUM_REFERENCIA);
				log.debug("comprobarStandAlone() - QR leido " + numReferencia);
				String numTransaccion = "";
				String timeStamp = "";
				try {
					JsonElement root = new JsonParser().parse(numReferencia);

					numTransaccion = root.getAsJsonObject().get("TransactionID").getAsString();
					timeStamp = root.getAsJsonObject().get("TimeStamp").getAsString();
				}
				catch (Exception e) {
					log.error("comprobarStandAlone() - Se ha recibido una cadena incorrecta - " + numReferencia);
					VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Error en la lectura de la referencia, por favor vuelva a leer la referencia."), getStage());
					return false;
				}

				DatosRespuestaPagoTarjeta datosRespuesta = new DatosRespuestaPagoTarjeta();
				datosRespuesta.setFechaTransaccion(timeStamp);
				datosRespuesta.setNumTransaccion(numTransaccion);

				pagoStandAlone.setDatosRespuestaPagoTarjeta(datosRespuesta);
				correcto = true;
			}
		}
		else {
			correcto = true;
		}
		return correcto;
	}

	@SuppressWarnings("unchecked")
	private void imprimeNotaInformativa(TicketManager ticketManager) {
		log.debug("imprimeNotaInformativa() - Inicio impresion nota informativa");

		if (AppConfig.menu.equals(ByLDevolucionesController.MENU_POS_PROFESIONAL)) {
			for (ByLLineaTicketProfesional linea : (List<ByLLineaTicketProfesional>) ticketManager.getTicket().getLineas()) {
				if (linea.getNotaInformativa() != null) {
					try {
						AvisoInformativoBean aviso = ByLAgregarNotaInformativaService.get().consultarAvisoInformativo(sesion.getAplicacion().getUidActividad(),
						        sesion.getAplicacion().getTienda().getCliente().getCodpais(), linea.getNotaInformativa().getCodigo());
						if (aviso.getDocuIndepe().equals("S")) {
							Long numeroCopias = 1L;
							try {
								numeroCopias = aviso.getCopias();
							}
							catch (Exception e) {
								log.debug("imprimeNotaInformativa() - " + e.getMessage());
							}

							Map<String, Object> mapaParametrosManager = new HashMap<String, Object>();
							mapaParametrosManager.put("ticket", ticketManager.getTicket());
							mapaParametrosManager.put("linea", linea);
							for (int i = 0; i < numeroCopias; i++) {
								ServicioImpresion.imprimir(PLANTILLA_NOTA_INFORMATIVA, mapaParametrosManager);
							}
						}
					}
					catch (Exception e) {
						String mensajeError = I18N.getTexto("Se ha producido un error al imprimir la nota informativa");
						log.error("imprimeNotaInformativa() - " + mensajeError + " - " + e.getClass().getName() + " - " + e.getLocalizedMessage(), e);
						VentanaDialogoComponent.crearVentanaError(getStage(), mensajeError, e);
					}
				}
			}
		}
		else {
			for (ByLLineaTicket linea : (List<ByLLineaTicket>) ticketManager.getTicket().getLineas()) {
				if (linea.getNotaInformativa() != null) {
					try {
						AvisoInformativoBean aviso = ByLAgregarNotaInformativaService.get().consultarAvisoInformativo(sesion.getAplicacion().getUidActividad(),
						        sesion.getAplicacion().getTienda().getCliente().getCodpais(), linea.getNotaInformativa().getCodigo());
						if (aviso.getDocuIndepe().equals("S")) {
							Long numeroCopias = 1L;
							try {
								numeroCopias = aviso.getCopias();
							}
							catch (Exception e) {
								log.debug("imprimeNotaInformativa() - " + e.getMessage());
							}

							Map<String, Object> mapaParametrosManager = new HashMap<String, Object>();
							mapaParametrosManager.put("ticket", ticketManager.getTicket());
							mapaParametrosManager.put("linea", linea);
							for (int i = 0; i < numeroCopias; i++) {
								ServicioImpresion.imprimir(PLANTILLA_NOTA_INFORMATIVA, mapaParametrosManager);
							}
						}
					}
					catch (Exception e) {
						String mensajeError = I18N.getTexto("Se ha producido un error al imprimir la nota informativa");
						log.error("imprimeNotaInformativa() - " + mensajeError + " - " + e.getClass().getName() + " - " + e.getLocalizedMessage(), e);
						VentanaDialogoComponent.crearVentanaError(getStage(), mensajeError, e);
					}
				}
			}
		}
	}

	private boolean isImpresoraFiscalPolonia() {
		IPrinter printer = Dispositivos.getInstance().getImpresora1();
		if (printer != null && printer instanceof PoloniaFiscalPrinter) {
			return true;
		}
		else {
			return false;
		}
	}

	private boolean isImpresoraFiscalRusia() {
		IPrinter printer = Dispositivos.getInstance().getImpresora1();
		if (printer != null && printer instanceof Spark130F) {
			return true;
		}
		else {
			return false;
		}
	}

	private String obtenerEstadoTarjeta(Integer stateCode) {
		switch (stateCode) {
			case 0:
				return I18N.getTexto("Inactiva");
			case 1:
				return I18N.getTexto("Activa");
			case 2:
				return I18N.getTexto("Desactivada");
			case 3:
				return I18N.getTexto("Consumida");
			case 4:
				return I18N.getTexto("Cancelada");

			default:
				return null;
		}
	}

	@FXML
	public void accionCambiarCajero() {
		datos.put(PARAMETRO_MODO_PANTALLA_CAJERO, true);
		POSApplication.getInstance().getMainView().showModal(SeleccionUsuariosView.class, datos);

		ticketManager.getTicket().setCajero(sesion.getSesionUsuario().getUsuario());

		refrescarDatosPantalla();
	}

	private void ocultaInfoProfesional() {
		if (AppConfig.menu.equals(ByLDevolucionesController.MENU_POS_PROFESIONAL)) {
			pnBase.setVisible(true);
			pnBase.setManaged(true);
			pnIva.setVisible(true);
			pnIva.setManaged(true);
			pnRecargo.setVisible(true);
			pnRecargo.setManaged(true);
		}
		else {
			pnBase.setVisible(false);
			pnBase.setManaged(false);
			pnIva.setVisible(false);
			pnIva.setManaged(false);
			pnRecargo.setVisible(false);
			pnRecargo.setManaged(false);
		}
	}

	protected void cargarBotoneraNif() {
		try {
			panelBotoneraNif.getChildren().clear();
			PanelBotoneraBean panelBotoneraBean = null;
			String tipoDocumento = ticketManager.getDocumentoActivo().getCodtipodocumento();

			/*
			 * Si tenemos datos de documento origen estamos en una devolución controlada y por tanto cargamos la
			 * botonera de nota de crédito independientemente del tipo de documento para que no salga el botón de añadir
			 * el NIF.
			 */
			if (ticketManager.getTicketOrigen() != null) {
				tipoDocumento = DOC_NOTA_CREDITO;
			}
			else {
				if (tipoDocumento.equals(DOC_FACTURA_COMPLETA)) {
					tipoDocumento = DOC_FACTURA_SIMPLIFICADA;
				}
			}
			if (botoneraDatosNif != null) {
				botoneraDatosNif.eliminaComponentes();
			}

			try {
				log.debug("inicializarComponentes() - Cargando panel de Datos Adicionales 2");
				panelBotoneraBean = getView().loadBotonera("_nif_" + tipoDocumento + "_" + ticketManager.getDocumentoActivo().getCodpais() + ".xml");
				botoneraDatosNif = new BotoneraComponent(panelBotoneraBean, null, panelBotoneraNif.getPrefHeight(), this, BotonBotoneraNormalComponent.class);
				panelBotoneraNif.getChildren().add(botoneraDatosNif);
			}
			catch (InitializeGuiException e) {
				String mensajeError = "No existe Botonera por el Tipo de Documento y País";
				log.debug("initializeComponents() - " + mensajeError + " - " + e.getMessage());
				panelBotoneraNif.getChildren().clear();
			}
			catch (CargarPantallaException e) {
				String mensajeError = "Error cargando la Botonera asociada al Tipo de Documento";
				log.debug("initializeComponents() - " + mensajeError + " - " + e.getMessage(), e);
				panelBotoneraNif.getChildren().clear();
			}

			try {
				if (panelBotoneraBean == null) {
					panelBotoneraBean = getView().loadBotonera("_nif_" + tipoDocumento + ".xml");
					botoneraDatosAdicionales = new BotoneraComponent(panelBotoneraBean, null, panelBotoneraNif.getPrefHeight(), this, BotonBotoneraNormalComponent.class);
					panelBotoneraNif.getChildren().add(botoneraDatosNif);
				}
			}
			catch (InitializeGuiException e) {
				String mensajeError = "Error al crear la Botonera";
				log.debug("initializeComponents() - " + mensajeError + " - " + e.getMessage());
				panelBotoneraNif.getChildren().clear();
			}
			catch (CargarPantallaException e) {
				String mensajeError = "Error cargando la Botonera asociada al Tipo de Documento";
				log.debug("initializeComponents() - " + mensajeError, e);
				panelBotoneraNif.getChildren().clear();
			}
		}
		catch (Exception e) {
			String mensajeError = I18N.getTexto("Error al crear la botonera para los medios de pago del Ticket Original");
			log.error("inicializarComponentes() - " + mensajeError + " - " + e.getMessage(), e);
			VentanaDialogoComponent.crearVentanaError(mensajeError, getStage());
		}
	}

	@FXML
	private void accionNIFNumContribuyente() {
		getDatos().put(FacturacionArticulosController.TICKET_KEY, ticketManager);
		getApplication().getMainView().showModalCentered(ByLCambiarDatosClientePTView.class, getDatos(), this.getStage());
		refrescarDatosPantalla();
	}

	private void tratarClienteSAFT(FidelizacionBean datosFidelizado) {
		log.debug("tratarClienteSAFT() - Tratando cliente según los datos del fidelizado " + datosFidelizado.getIdFidelizado());
		if (datosFidelizado.getDocumento() != null && !datosFidelizado.getDocumento().isEmpty()) {
			ClienteBean cliente = new ClienteBean();
			try {
				/*
				 * COMPROBAMOS SI YA EXISTE UN CLIENTE CON EL DOCUMENTO QUE TRAE EL FIDELIZADO, SI EXISTE LO TRAEMOS Y LO
				 * SETEAMOS, SI NO EXISTE LO CREAMOS
				 */
				List<ClienteBean> clientes = clientesService.consultarClientesCIF(datosFidelizado.getDocumento());
				cliente = clientes.isEmpty() ? null : clientes.get(0);
				if(cliente == null) {
					cliente = new ClienteBean();
					cliente.setActivo(Boolean.TRUE);
					cliente.setEstadoBean(Estado.NUEVO);
					cliente.setCodCliente(datosFidelizado.getIdFidelizado().toString());
					cliente.setDesCliente(datosFidelizado.getNombre() + " " + datosFidelizado.getApellido());
					cliente.setDomicilio(datosFidelizado.getDomicilio());
					cliente.setProvincia(datosFidelizado.getProvincia());
					cliente.setCp(datosFidelizado.getCp());
					cliente.setPais(datosFidelizado.getDesPais());
					cliente.setCodpais(datosFidelizado.getCodPais());
					cliente.setLocalidad(datosFidelizado.getLocalidad());
					cliente.setPoblacion(datosFidelizado.getPoblacion());
					cliente.setCif(datosFidelizado.getDocumento());
					cliente.setIdTratImpuestos(ticketManager.getTicket().getIdTratImpuestos());
					cliente.setCodtar(ticketManager.getTicket().getCodTarifa());
					cliente.setTipoIdentificacion(datosFidelizado.getCodTipoIden());
					cliente.setNombreComercial(datosFidelizado.getNombre() + " " + datosFidelizado.getApellido());
					clientesService.salvar(cliente);
				}
				
				if (cliente.getIdGrupoImpuestos() == null) {
					cliente.setIdGrupoImpuestos(sesion.getAplicacion().getTienda().getCliente().getIdGrupoImpuestos());
				}
				
				ticketManager.getTicket().setCliente(cliente);
			}
			catch (ClientesServiceException e) {
				log.error("tratarClienteSAFT() - Error creando al cliente " + cliente.getCodCliente() + " " + e.getMessage());
			}
			catch (ClienteConstraintViolationException e) {
				log.error("tratarClienteSAFT() - Error creando al cliente " + cliente.getCodCliente() + " " + e.getMessage());
			}
		}
	}
	
	protected void integracionVertex() {
		log.debug("integracionVertex()");
		if (vertexservice.integracionImpuestosVertexActiva(ticketManager.getTicket().getCabecera().getTipoDocumento()) && !ticketManager.getTicket().isEsDevolucion()) {
			try {
				vertexservice.peticionVertex(VertexService.VERTEX_TIPO_QUOTATION, ticketManager.getTicket(), ticketManager.getDocumentoActivo().getCodtipodocumento(), null, false);
				ticketManager.getTicket().getTotales().recalcular();
				refrescarDatosPantalla();
				cambiarBaseIva();
			}
			catch (Exception e) {
				log.warn("Se ha producido un error en la peticion a vertex desde ByLPagosController : " + e.getMessage());
				log.debug("Se procedera a realizar el modo offline");
				vertexservice.modoOffline(ticketManager.getTicket());
				ticketManager.getTicket().getTotales().recalcular();
				refrescarDatosPantalla();
				cambiarBaseIva();
			}
		}
	}
	@SuppressWarnings("unchecked")
	protected void cambiarBaseIva() {

		BigDecimal base = new BigDecimal(0);
		BigDecimal iva = new BigDecimal(0);

		List<ByLLineaTicketProfesional> lineas = ticketManager.getTicket().getLineas();

		for (ByLLineaTicketProfesional byLLineaTicketProfesional : lineas) {
			if (BigDecimalUtil.isMayorACero(byLLineaTicketProfesional.getCantidad())) {
				base = base.add(new BigDecimal(byLLineaTicketProfesional.getLineaVertex().getAmount()));
				List<LineaDetailVertex> listaImpuestosVertex = byLLineaTicketProfesional.getListaImpuestosVertex();
				for (LineaDetailVertex impuesto : listaImpuestosVertex) {
					iva = iva.add(new BigDecimal(impuesto.getTaxAmount()));
				}
			}
		}

		lbBase.setText(FormatUtil.getInstance().formateaImporte(base));
		lbIva.setText(FormatUtil.getInstance().formateaImporte(iva));
	}
	
	@SuppressWarnings("unchecked")
	private void borrarDatosImpuestos() {
		log.debug("borrarDatosImpuestos() - Se procede a eliminar los impuestos añadidos a cada linea desde vertex");

		if (!ticketManager.getTicket().isEsDevolucion()) {

			((ByLCabeceraTicket) ticketManager.getTicket().getCabecera()).setCabeceraVertex(null);
			
			ticketManager.getTicket().getTotales().recalcular();
			List<ByLLineaTicketProfesional> lineasTicket = ticketManager.getTicket().getLineas();
			for (ByLLineaTicketProfesional lineaTicket : lineasTicket) {
				lineaTicket.setPrecioSinDto(lineaTicket.getPrecioTarifaOrigen());
				limpiarLineasVertex(lineaTicket);
			}
		}
	}
	
	private void obtenerImporteMaxEfectivo() {
		log.debug("obtenerImporteMaxEfectivo()");
		if (CODIGO_PAIS_ES.equals(sesion.getAplicacion().getTienda().getCliente().getCodpais())) {
			importeMaxEfectivo = ticketManager.getDocumentoActivo().getPropiedades().get("IMPORTE_MAXIMO_EFECTIVO");
		}
		else if (!ticketManager.isEsDevolucion() && CODIGO_PAIS_PT.equals(sesion.getAplicacion().getTienda().getCliente().getCodpais())) {
			String tipoIdentificacion = "";
			if (ticketManager.getTicket().getCabecera().getDatosFidelizado() != null && ticketManager.getTicket().getCabecera().getDatosFidelizado().getCodTipoIden() != null) {
				tipoIdentificacion = ticketManager.getTicket().getCabecera().getDatosFidelizado().getCodTipoIden();
			}
			else if (ticketManager.getTicket().getCliente().getTipoIdentificacion() != null) {
				tipoIdentificacion = ticketManager.getTicket().getCliente().getTipoIdentificacion();
			}

			if (tipoIdentificacion.equals("NIF")) {
				importeMaxEfectivo = ticketManager.getDocumentoActivo().getPropiedades().get(LIMITE_EFECTIVO_NIF);
			}
			else if (tipoIdentificacion.equals("NIPC")) {
				importeMaxEfectivo = ticketManager.getDocumentoActivo().getPropiedades().get(LIMITE_EFECTIVO_CIF);
			}
			else {
				importeMaxEfectivo = ticketManager.getDocumentoActivo().getPropiedades().get(IMPORTE_MAXIMO_EFECTIVO_EX);
			}
		}
		else {
			importeMaxEfectivo = ticketManager.getDocumentoActivo().getPropiedades().get(IMPORTE_MAXIMO_EFECTIVO_EX);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void comprobarLimitePagosAnotados() {
		log.debug("comprobarLimitePagosAnotados()");
		List<PagoTicket> pagosAnotados = ticketManager.getTicket().getPagos();
		Boolean encontrado = Boolean.FALSE;
		if (pagosAnotados != null && !pagosAnotados.isEmpty()) {
			int i = 0;
			while (!encontrado && pagosAnotados.size() >= i+1) {
				PagoTicket pago = pagosAnotados.get(i);
				if (pago.getCodMedioPago().equals(COD_MP_EFECTIVO) && BigDecimalUtil.isMayor(pago.getImporte(), new BigDecimal(importeMaxEfectivo.getValor()))) {
					ticketManager.getTicket().getPagos().remove(pago);
					encontrado = Boolean.TRUE;
				}
				i++;
			}
		}
		if(encontrado) {
			ticketManager.recalcularConPromociones();
			super.refrescarDatosPantalla();
		}
	}
	
	private void limpiarLineasVertex(ByLLineaTicketProfesional linea) {
		linea.setLineaVertex(null);
		linea.setListaImpuestosVertex(null);
	}
	
	private boolean puedeAgregarPagoOrigen() {
		// Se hace así para no tener que enviar el permiso a todas las tiendas.
		// Si no existe permiso de NO agregar pago origen, se agregará el pago origen.
		try {
			compruebaPermisos(PERMISO_NO_AGREGAR_PAGO_ORIGEN);
			log.debug("agregarPagoOrigen() - Hay permiso :" + PERMISO_NO_AGREGAR_PAGO_ORIGEN);
			return false;
		}
		catch (SinPermisosException e) {
			log.debug("agregarPagoOrigen() - Sin permiso :" + PERMISO_NO_AGREGAR_PAGO_ORIGEN);
			return true;
		}
	}
	
	@FXML
	private void accionCambiarDatosCO() {
		getDatos().put(FacturacionArticulosController.TICKET_KEY, ticketManager);
		getApplication().getMainView().showModalCentered(ByLCambiarDatosClienteCOView.class, getDatos(), this.getStage());
		refrescarDatosPantalla();
	}
	
	/**
	 * BYL-177 COLOMBIA
	 * Setea los valores del cliente del ticket con los valores del cliente de la sesión si se ha modificado el ticket
	 */
	private void setearDatosClienteGenerico() {
		ClienteBean clienteSesion = sesion.getAplicacion().getTienda().getCliente();
		ClienteBean clienteTicket = ticketManager.getTicket().getCliente();
		if(!ObjectUtils.equals(clienteTicket.getCodCliente(), clienteSesion.getCodCliente())
				|| !clienteTicket.getDesCliente().equals(clienteSesion.getDesCliente())) {
			try {
				ticketManager.getTicket().setCliente((ClienteBean) BeanUtils.cloneBean(clienteSesion));
			}
			catch (Exception e) {
				log.error("setearDatosClienteGenerico()- Ha habido un error seteando los valores del cliente genérico: " + e.getMessage());
			}
		}
	}
	
	
	/**
	 * BYL-209 ECUADOR Comprueba el cliente de la sesión con el cliente del ticket, si no se ha modificado, se setea
	 * con los valores por defecto
	 */
	private boolean tratarClienteConsumidorFinalEC(ClienteBean clienteTicket) {
		log.debug("tratarClienteConsumidorFinalEC() - Tratamiento de cliente ECUADOR");
		boolean esObligatorioCliente = false;
		PropiedadDocumentoBean propiedadImporteMaximo = ticketManager.getDocumentoActivo().getPropiedades().get(IMPORTE_MAXIMO_EC);
		if (propiedadImporteMaximo == null) {
			log.error("tratarClienteConsumidorFinalEC()- No existe la propiedad del documento  " + ticketManager.getDocumentoActivo().getDestipodocumento() + ": " + IMPORTE_MAXIMO_EC);
			ByLVentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("No existe la propiedad del documento " + ticketManager.getDocumentoActivo().getDestipodocumento() + ": " + IMPORTE_MAXIMO_EC),
			        getStage());
		}
		else if (propiedadImporteMaximo.getValor() != null) {
			ClienteBean clienteSesion = sesion.getAplicacion().getTienda().getCliente();
			BigDecimal importeMaximoPropiedad = new BigDecimal(propiedadImporteMaximo.getValor());
			if (BigDecimalUtil.isMayor(ticketManager.getTicket().getCabecera().getTotales().getTotalAPagar(), importeMaximoPropiedad)
			        && ObjectUtils.equals(clienteTicket.getCodCliente(), clienteSesion.getCodCliente()) && clienteTicket.getDesCliente().equals(clienteSesion.getDesCliente())) {

				ClienteBean clienteFidelizado = rellenarClienteFidelizado();
				getDatos().put("clienteFidelizado", clienteFidelizado);
				getDatos().put(FacturacionArticulosController.TICKET_KEY, ticketManager);
				
				getApplication().getMainView().showModalCentered(ByLCambiarDatosClienteECView.class, getDatos(), getStage());

				if (ticketManager.getTicket().getCliente() != null) {
					esObligatorioCliente = true;
				}
			}
			else {
				if (ObjectUtils.equals(clienteTicket.getCodCliente(), clienteSesion.getCodCliente()) && clienteTicket.getDesCliente().equals(clienteSesion.getDesCliente())) {
					clienteTicket.setDesCliente(EC_DEFAULT_NOMBRE_RAZON_SOCIAL);
					clienteTicket.setCif(EC_DEFAULT_NUMERO_INDENTIFICACION);
					clienteTicket.setCodpais(EC_DEFAULT_PAIS);
					clienteTicket.setTipoIdentificacion(EC_DEFAULT_CODTIPO_DOCUMENTO);
				}

			}
		}

		return esObligatorioCliente;
	}

	/**
	 * BYL-320 ECUADOR Cambiar mensaje final de venta cuando se identifica el fidelizado
	 * 
	 * @return
	 */
	private ClienteBean rellenarClienteFidelizado() {

		ClienteBean clienteFidelizado = new ClienteBean();
		FidelizacionBean fidelizado = ticketManager.getTicket().getCabecera().getDatosFidelizado();
		
		if (fidelizado != null) {
			clienteFidelizado.setNombreComercial(fidelizado.getNombre());
			clienteFidelizado.setTipoIdentificacion(fidelizado.getCodTipoIden());
			clienteFidelizado.setCif(fidelizado.getDocumento());
			clienteFidelizado.setDomicilio(fidelizado.getDomicilio());
			clienteFidelizado.setPoblacion(fidelizado.getPoblacion());
			clienteFidelizado.setProvincia(fidelizado.getProvincia());
			clienteFidelizado.setCp(fidelizado.getCp());
			clienteFidelizado.setCodpais(fidelizado.getCodPais());
		}
		return clienteFidelizado;
	}

	/**
	 * BYL-177 COLOMBIA Comprueba el cliente de la sesión con el cliente del ticket, si no se ha modificado, se setea
	 * con los valores por defecto
	 */
	private void tratarClienteConsumidorFinalCO(ClienteBean clienteTicket) {
		log.debug("tratarClienteConsumidorFinalCO() - Tratamiento de cliente COLOMBIA");

		if (ticketPrincipal != null && ticketPrincipal.getTicketAuxiliar() != null && ticketPrincipal.getTicketAuxiliar().getTicket() != null) {
			log.debug("tratarClienteConsumidorFinal() - Se trata de una devolucion con cambio. Se modifica el cliente del ticket de venta para que sea el mismo que el de la devolucion");
			ClienteBean clienteDevolucion = ticketPrincipal.getTicketAuxiliar().getTicket().getCabecera().getCliente();
			ticketPrincipal.getTicket().getCabecera().setCliente(clienteDevolucion);
		}		
		else {
			ClienteBean clienteSesion = sesion.getAplicacion().getTienda().getCliente();
			if (ObjectUtils.equals(clienteTicket.getCodCliente(), clienteSesion.getCodCliente()) && clienteTicket.getDesCliente().equals(clienteSesion.getDesCliente())) {
				clienteTicket.setDesCliente(CO_DEFAULT_NOMBRE_RAZON_SOCIAL);
				clienteTicket.setCif(CO_DEFAULT_NUMERO_INDENTIFICACION);
				clienteTicket.setCodpais(CO_DEFAULT_PAIS);
				clienteTicket.setEmail(CO_DEFAULT_CORREO_ELECTRONICO);
				clienteTicket.setTipoIdentificacion(CO_DEFAULT_CODTIPO_DOCUMENTO);
			}
		}
	}
	
	
	/**
	 * BYL-263 PANAMÁ Comprueba el cliente de la sesión con el cliente del ticket, si no se ha modificado, se setea
	 * con los valores por defecto
	 */
	private void tratarClienteConsumidorFinalPA(ClienteBean clienteTicket) {
		log.debug("tratarClienteConsumidorFinalPA() - Tratamiento de cliente PANAMÁ");

		if (ticketPrincipal != null && ticketPrincipal.getTicketAuxiliar() != null && ticketPrincipal.getTicketAuxiliar().getTicket() != null) {
			log.debug("tratarClienteConsumidorFinalPA() - Se trata de una devolucion con cambio. Se modifica el cliente del ticket de venta para que sea el mismo que el de la devolución");
			ClienteBean clienteDevolucion = ticketPrincipal.getTicketAuxiliar().getTicket().getCabecera().getCliente();
			ticketPrincipal.getTicket().getCabecera().setCliente(clienteDevolucion);
		}		
		else {
			ClienteBean clienteSesion = sesion.getAplicacion().getTienda().getCliente();
			if (ObjectUtils.equals(clienteTicket.getCodCliente(), clienteSesion.getCodCliente()) && clienteTicket.getDesCliente().equals(clienteSesion.getDesCliente())) {
				clienteTicket.setDesCliente(PA_DEFAULT_NOMBRE_RAZON_SOCIAL);
				clienteTicket.setCif(PA_DEFAULT_NUMERO_INDENTIFICACION);
				clienteTicket.setCodpais(PA_DEFAULT_PAIS);
				clienteTicket.setEmail(PA_DEFAULT_CORREO_ELECTRONICO);
				clienteTicket.setTipoIdentificacion(PA_DEFAULT_CODTIPO_DOCUMENTO);
			}
		}
	}
	
	protected void autosizeLabelTotalFont() {
		try {
			lbTotal.setStyle("");
			String text = lbTotal.getText();
			if(text.length()>=15) {
				lbTotal.setStyle("-fx-font-size: 25px;");
			}
			else if(text.length()>=12) {
				lbTotal.setStyle("-fx-font-size: 30px;");
			}
			else if(text.length()>=10) {
				lbTotal.setStyle("-fx-font-size: 40px;");
			}
			else if(text.length()>=8) {
				lbTotal.getStyleClass().add("total-reduced");
			}
		} catch (Exception e) {
			log.debug("autosizeLabelTotalFont() - " + e.getClass().getName() + " - " + e.getLocalizedMessage(), e);
		}

	}
	
	private boolean tratamientoClientePorPais() {
		log.debug("tratamientoClientePorPais()");
		/*
		 * COLOMBIA --> RELLENAMOS EL CLIENTE CONSUMIDOR FINAL EN CASO DE QUE EL FIDELIZADO NO QUIERA FACTURA
		 */
		if (ticketManager.getDocumentoActivo().getCodpais().equals(CODIGO_PAIS_CO)){
			tratarClienteConsumidorFinalCO(ticketManager.getTicket().getCliente());
		}

		/*
		 * PORTUGAL --> RELLENAMOS EL CLIENTE PARA EL SAFT-PT EN CASO DE QUE EL FIDELIZADO NO QUIERA FACTURA
		 */
		if (ticketManager.getDocumentoActivo().getCodtipodocumento().equals(DOC_FACTURA_SIMPLIFICADA) && ticketManager.getDocumentoActivo().getCodpais().equals(CODIGO_PAIS_PT)) {
			if (ticketManager.getTicket().getCabecera().getDatosFidelizado() != null && !ticketManager.isEsDevolucion()) {
				tratarClienteSAFT(ticketManager.getTicket().getCabecera().getDatosFidelizado());
			}
		}
		
		/*
		 * ECUADOR --> RELLENAMOS EL CLIENTE CONSUMIDOR FINAL Y SE HACE LA COMPROBACION DE SI ES NECESARIO IDENTIFICAR AL CLIENTE
		 */
		if (ticketManager.getDocumentoActivo().getCodpais().equals(CODIGO_PAIS_EC)) {
			boolean obligatorioCliente = tratarClienteConsumidorFinalEC(ticketManager.getTicket().getCliente());
			return obligatorioCliente;
		}
		
		/*
		 * PANAMÁ --> RELLENAMOS EL CLIENTE CONSUMIDOR FINAL EN CASO DE QUE EL FIDELIZADO NO QUIERA FACTURA
		 */
		if (ticketManager.getDocumentoActivo().getCodpais().equals(CODIGO_PAIS_PANAMA)) {
			tratarClienteConsumidorFinalPA(ticketManager.getTicket().getCliente());
		}
		
		return false;
	}
	
	private void tratamientoIntegracionEdicom() {
		if (sesion.getAplicacion().getTienda().getCliente().getCodpais().equals(CODIGO_PAIS_CO)) {
			log.debug("tratamientoIntegracionEdicom()");
			if (ticketManager.getTicketOrigen() != null && ticketManager.getTicketOrigen().getCabecera().getFiscalData() != null) {
				FiscalData fiscalData = new FiscalData();
				if (ticketManager.getTicketOrigen().getCabecera().getFiscalData().getProperty(ProcesadorIdFiscalCO.CUFE) != null) {
					fiscalData.addProperty(ProcesadorIdFiscalCO.ORIGEN_CUFE, ticketManager.getTicketOrigen().getCabecera().getFiscalData().getProperty(ProcesadorIdFiscalCO.CUFE).getValue());
				}
				if (StringUtils.isNotBlank(((ByLCabeceraTicket) ticketManager.getTicketOrigen().getCabecera()).getIdentificadorFiscal())) {
					fiscalData.addProperty(ProcesadorIdFiscalCO.ORIGEN_IDENTIFICADOR_FISCAL, ticketManager.getTicketOrigen().getCabecera().getFiscalData().getProperty(ProcesadorIdFiscalCO.IDENTIFICADOR_FISCAL_EDICOM).getValue());
				}
				
				if (ticketManager.getTicketOrigen().getCabecera().getFecha() != null) {
					fiscalData.addProperty(ProcesadorIdFiscalCO.FECHA_ORIGEN_DOCUMENTO_FISCAL, ticketManager.getTicketOrigen().getCabecera().getFechaAsLocale());
				}
				
				ticketNegativo.getTicket().getCabecera().setFiscalData(fiscalData);

			}
		}
	}
	
	private void comprobarEnvioEdicom() {
		if (sesion.getAplicacion().getTienda().getCliente().getCodpais().equals(CODIGO_PAIS_CO)) {
			log.debug("comprobarEnvioEdicom() - Comprobando si se ha realizado el envio del documento a Edicom");
			if (ticketManager.getTicket().getCabecera().getFiscalData() != null
			        && ticketManager.getTicket().getCabecera().getFiscalData().getProperty(ByLVariablesServices.FICHERO_FISCAL_PROCESADO) != null
			        && ticketManager.getTicket().getCabecera().getFiscalData().getProperty(ByLVariablesServices.FICHERO_FISCAL_PROCESADO).getValue().equals("N")) {

				ByLVentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("No se ha podido establecer la conexión con EDICOM. Se volvera a intentar desde la Central. Gracias"), getStage());
			}
		}
	}
	
	private boolean permitirTicketElectronicoCO() {
		if (sesion.getAplicacion().getTienda().getCliente().getCodpais().equals(CODIGO_PAIS_CO)) {
			log.debug("comprobacionTicketElectronicoCO() - Comprobando si tenemos que permitir el envío de ticket electrónico");

			if (!ticketManager.getTicket().getCliente().getCif().equals(CO_DEFAULT_NUMERO_INDENTIFICACION)) {
				return false;
			}
		}

		return true;
	}
	
	@FXML
	private void accionCambiarDatosEC() {
		log.debug("accionCambiarDatosEC()");
		getDatos().put(FacturacionArticulosController.TICKET_KEY, ticketManager);

		PropiedadDocumentoBean propiedadImporteMaximo = ticketManager.getDocumentoActivo().getPropiedades().get(IMPORTE_MAXIMO_EC);
		if (propiedadImporteMaximo != null && StringUtils.isNotBlank(propiedadImporteMaximo.getValor())) {
			BigDecimal importeMaximoPropiedad = new BigDecimal(propiedadImporteMaximo.getValor());
			if (BigDecimalUtil.isMenor(ticketManager.getTicket().getCabecera().getTotales().getTotalAPagar(), importeMaximoPropiedad)
			        && ticketManager.getTicket().getCabecera().getCliente().getDatosFactura() == null) {
				getDatos().put(MOSTRAR_AVISO_CANCELAR, true);
			}
		}

		getApplication().getMainView().showModalCentered(ByLCambiarDatosClienteECView.class, getDatos(), this.getStage());
		refrescarDatosPantalla();
	}
	
	@FXML
	private void accionCambiarDatosPA() {
		log.debug("accionCambiarDatosPA()");
		getDatos().put(FacturacionArticulosController.TICKET_KEY, ticketManager);
		VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto(PA_TEXTO_AVISO_BOTON_FACTURA), getStage());
		getApplication().getMainView().showModalCentered(ByLCambiarDatosClientePAView.class, getDatos(), this.getStage());
		refrescarDatosPantalla();
	}
	
	private boolean tratarDevolucionEC() {
		log.debug("tratarDevolucionEC()");
		boolean esObligatorioCliente = false;
		if (ticketManager.isEsDevolucion() && ticketManager.getTicketOrigen() != null) {
			if (ticketManager.getTicketOrigen().getCabecera().getCliente().getDesCliente().equals(ByLPagosController.EC_DEFAULT_NOMBRE_RAZON_SOCIAL)) {
				accionCambiarDatosEC();
				boolean clienteTraspasado = (boolean) getDatos().get("clienteTraspasado");
				if (ticketManager.getTicket().getCabecera().getCliente().getDesCliente().equals(EC_DEFAULT_NOMBRE_RAZON_SOCIAL) || !clienteTraspasado) {
					esObligatorioCliente = true;
				}
				else {
					ticketPrincipal.getTicket().getCabecera().setCliente(ticketManager.getTicket().getCabecera().getCliente());
				}
			}
		}
		else {
			if (ticketManager.getTicket().getCabecera().getCliente().getDesCliente().equals(sesion.getAplicacion().getTienda().getCliente().getDesCliente())) {
				accionCambiarDatosEC();
				boolean clienteTraspasado = (boolean) getDatos().get("clienteTraspasado");
				if (ticketManager.getTicket().getCabecera().getCliente().getCif().equals(sesion.getAplicacion().getTienda().getCliente().getCif())
				        || ticketManager.getTicket().getCabecera().getCliente().getDesCliente().equals(sesion.getAplicacion().getTienda().getCliente().getDesCliente()) 
				        || !clienteTraspasado) {
					esObligatorioCliente = true;
				}else {
					ticketPrincipal.getTicket().getCabecera().setCliente(ticketManager.getTicket().getCabecera().getCliente());
				}
			}
			else {
				ticketPrincipal.getTicket().getCabecera().setCliente(ticketManager.getTicket().getCabecera().getCliente());
			}
		}
		return esObligatorioCliente;
	}
	
	private boolean tratarDevolucionPA() {
		log.debug("tratarDevolucionPA()");
		boolean esObligatorioCliente = false;
		if (ticketManager.isEsDevolucion() && ticketManager.getTicketOrigen() != null) {
			if (ticketManager.getTicketOrigen().getCabecera().getCliente().getDesCliente().equals(PA_DEFAULT_NOMBRE_RAZON_SOCIAL)) {
				accionCambiarDatosPA();
				boolean clienteTraspasado = (boolean) getDatos().get("clienteTraspasado");
				if (ticketManager.getTicket().getCabecera().getCliente().getDesCliente().equals(PA_DEFAULT_NOMBRE_RAZON_SOCIAL) || !clienteTraspasado) {
					esObligatorioCliente = true;
				}
				else {
					ticketPrincipal.getTicket().getCabecera().setCliente(ticketManager.getTicket().getCabecera().getCliente());
				}
			}
		}
		else {
			if (ticketManager.getTicket().getCabecera().getCliente().getDesCliente().equals(sesion.getAplicacion().getTienda().getCliente().getDesCliente())) {
				accionCambiarDatosPA();
				boolean clienteTraspasado = (boolean) getDatos().get("clienteTraspasado");
				if (ticketManager.getTicket().getCabecera().getCliente().getCif().equals(sesion.getAplicacion().getTienda().getCliente().getCif())
				        || ticketManager.getTicket().getCabecera().getCliente().getDesCliente().equals(sesion.getAplicacion().getTienda().getCliente().getDesCliente()) 
				        || !clienteTraspasado) {
					esObligatorioCliente = true;
				}
			}
			else {
				ticketPrincipal.getTicket().getCabecera().setCliente(ticketManager.getTicket().getCabecera().getCliente());
			}
		}
		return esObligatorioCliente;
	}
}
