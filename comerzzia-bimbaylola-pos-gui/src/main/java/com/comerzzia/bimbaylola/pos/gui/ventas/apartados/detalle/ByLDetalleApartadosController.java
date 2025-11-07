package com.comerzzia.bimbaylola.pos.gui.ventas.apartados.detalle;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.dispositivo.fidelizacion.busqueda.ByLBusquedaFidelizadoController;
import com.comerzzia.bimbaylola.pos.gui.pagos.ByLPagosController;
import com.comerzzia.bimbaylola.pos.gui.ventas.apartados.detalle.verPagos.ByLVerPagosApartadoGui;
import com.comerzzia.bimbaylola.pos.gui.ventas.devoluciones.ByLDevolucionesController;
import com.comerzzia.bimbaylola.pos.gui.ventas.tickets.ByLTicketManager;
import com.comerzzia.bimbaylola.pos.persistence.apartados.ByLApartadosCabecera;
import com.comerzzia.bimbaylola.pos.persistence.fidelizacion.ByLFidelizacionBean;
import com.comerzzia.bimbaylola.pos.persistence.reservas.ReservasPagoGiftCardBean;
import com.comerzzia.bimbaylola.pos.services.apartados.ByLApartadosService;
import com.comerzzia.bimbaylola.pos.services.cajas.ByLCajasService;
import com.comerzzia.bimbaylola.pos.services.core.documentos.tipos.ByLTipoDocumentoService;
import com.comerzzia.bimbaylola.pos.services.movimientos.CajaMovimientoTarjetaService;
import com.comerzzia.bimbaylola.pos.services.reservas.exception.TicketReservaException;
import com.comerzzia.bimbaylola.pos.services.reservas.pagogift.ReservasPagoGiftCardService;
import com.comerzzia.bimbaylola.pos.services.reservas.pagogift.exception.ReservasPagoGiftCardNotFoundException;
import com.comerzzia.bimbaylola.pos.services.ticket.ByLLineaTicket;
import com.comerzzia.bimbaylola.pos.services.ticket.aparcados.reservas.CabeceraEspecialesReservaTicket;
import com.comerzzia.bimbaylola.pos.services.ticket.aparcados.reservas.CabeceraReservaTicket;
import com.comerzzia.bimbaylola.pos.services.ticket.aparcados.reservas.DatosFidelizadoReservaTicket;
import com.comerzzia.bimbaylola.pos.services.ticket.aparcados.reservas.DatosRespuestaTarjetaReservaTicket;
import com.comerzzia.bimbaylola.pos.services.ticket.aparcados.reservas.LineaReservaTicket;
import com.comerzzia.bimbaylola.pos.services.ticket.aparcados.reservas.PagoReservaTicket;
import com.comerzzia.bimbaylola.pos.services.ticket.aparcados.reservas.TicketReserva;
import com.comerzzia.bimbaylola.pos.services.ticket.aparcados.reservas.TotalesReservaTicket;
import com.comerzzia.core.util.fechas.Fecha;
import com.comerzzia.core.util.mybatis.session.SqlSession;
import com.comerzzia.pos.core.dispositivos.Dispositivos;
import com.comerzzia.pos.core.dispositivos.dispositivo.DispositivoCallback;
import com.comerzzia.pos.core.dispositivos.dispositivo.fidelizacion.ConsultaTarjetaFidelizadoException;
import com.comerzzia.pos.core.dispositivos.dispositivo.fidelizacion.FidelizacionNoConfig;
import com.comerzzia.pos.core.dispositivos.dispositivo.visor.IVisor;
import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.POSApplication;
import com.comerzzia.pos.core.gui.componentes.botonaccion.BotonBotoneraComponent;
import com.comerzzia.pos.core.gui.componentes.botonera.ConfiguracionBotonBean;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.login.seleccionUsuarios.SeleccionUsuarioController;
import com.comerzzia.pos.core.gui.tablas.celdas.CellFactoryBuilder;
import com.comerzzia.pos.dispositivo.tarjeta.adyen.cloud.TefAdyenCloud;
import com.comerzzia.pos.gui.ventas.apartados.ApartadosManager;
import com.comerzzia.pos.gui.ventas.apartados.detalle.DetalleApartadosController;
import com.comerzzia.pos.gui.ventas.apartados.detalle.FormularioDetalleApartadoBean;
import com.comerzzia.pos.gui.ventas.apartados.detalle.LineaArticuloApartadoGui;
import com.comerzzia.pos.gui.ventas.apartados.detalle.datosCliente.CambiarDatosClienteApartadoController;
import com.comerzzia.pos.gui.ventas.apartados.detalle.datosCliente.CambiarDatosClienteApartadoView;
import com.comerzzia.pos.gui.ventas.apartados.detalle.marcarVenta.MarcarVentaApartadoController;
import com.comerzzia.pos.gui.ventas.apartados.detalle.marcarVenta.MarcarVentaApartadoView;
import com.comerzzia.pos.gui.ventas.apartados.detalle.pagos.NuevoPagoApartadoController;
import com.comerzzia.pos.gui.ventas.apartados.detalle.pagos.NuevoPagoApartadoView;
import com.comerzzia.pos.gui.ventas.apartados.detalle.verPagos.VerPagosApartadoGui;
import com.comerzzia.pos.gui.ventas.apartados.pagoApartado.PagoApartadoController;
import com.comerzzia.pos.gui.ventas.apartados.pagoApartado.PagoApartadoView;
import com.comerzzia.pos.gui.ventas.profesional.articulos.busqueda.BuscarArticulosProfesionalView;
import com.comerzzia.pos.gui.ventas.tickets.TicketManager;
import com.comerzzia.pos.gui.ventas.tickets.articulos.FacturacionArticulosController;
import com.comerzzia.pos.gui.ventas.tickets.articulos.FacturacionArticulosController.LineaInsertadaNoPermitidaException;
import com.comerzzia.pos.gui.ventas.tickets.articulos.FormularioLineaArticuloBean;
import com.comerzzia.pos.gui.ventas.tickets.articulos.busquedas.BuscarArticulosController;
import com.comerzzia.pos.gui.ventas.tickets.articulos.busquedas.BuscarArticulosView;
import com.comerzzia.pos.gui.ventas.tickets.articulos.edicion.EdicionArticuloController;
import com.comerzzia.pos.gui.ventas.tickets.articulos.edicion.EdicionArticuloView;
import com.comerzzia.pos.gui.ventas.tickets.pagos.PagosController;
import com.comerzzia.pos.persistence.apartados.ApartadosCabeceraBean;
import com.comerzzia.pos.persistence.apartados.detalle.ApartadosDetalleBean;
import com.comerzzia.pos.persistence.apartados.pagos.ApartadosPagoBean;
import com.comerzzia.pos.persistence.cajas.movimientos.CajaMovimientoBean;
import com.comerzzia.pos.persistence.clientes.ClienteBean;
import com.comerzzia.pos.persistence.core.contadores.ContadorBean;
import com.comerzzia.pos.persistence.core.documentos.tipos.TipoDocumentoBean;
import com.comerzzia.pos.persistence.fidelizacion.FidelizacionBean;
import com.comerzzia.pos.persistence.mediosPagos.MedioPagoBean;
import com.comerzzia.pos.persistence.mybatis.SessionFactory;
import com.comerzzia.pos.persistence.tickets.TicketBean;
import com.comerzzia.pos.services.apartados.ApartadosService;
import com.comerzzia.pos.services.articulos.ArticulosService;
import com.comerzzia.pos.services.core.contadores.ContadorServiceException;
import com.comerzzia.pos.services.core.contadores.ServicioContadores;
import com.comerzzia.pos.services.core.documentos.DocumentoException;
import com.comerzzia.pos.services.core.documentos.Documentos;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.services.core.variables.VariablesServices;
import com.comerzzia.pos.services.mediospagos.MediosPagosService;
import com.comerzzia.pos.services.promociones.PromocionesServiceException;
import com.comerzzia.pos.services.ticket.TicketsService;
import com.comerzzia.pos.services.ticket.lineas.LineaTicket;
import com.comerzzia.pos.services.ticket.lineas.LineaTicketException;
import com.comerzzia.pos.services.ticket.tarjetaRegalo.TarjetaRegaloException;
import com.comerzzia.pos.util.bigdecimal.BigDecimalUtil;
import com.comerzzia.pos.util.config.AppConfig;
import com.comerzzia.pos.util.config.SpringContext;
import com.comerzzia.pos.util.format.FormatUtil;
import com.comerzzia.pos.util.i18n.I18N;
import com.comerzzia.pos.util.xml.MarshallUtil;
import com.comerzzia.pos.util.xml.MarshallUtilException;
import com.sun.javafx.scene.control.skin.TableViewSkin;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;

@Component
@Primary
public class ByLDetalleApartadosController extends DetalleApartadosController {

	@Autowired
	protected ByLCajasService cajasService;
	
	@Autowired
	protected ArticulosService articuloService;
	
	@Autowired
	private ApartadosService apartadosService;
	
    @Autowired
    protected ServicioContadores contadoresService;
	
    @Autowired
    protected ByLTipoDocumentoService tipoDocumentoService;
    
	@Autowired
	protected TicketsService ticketService;
	
	@Autowired
	private MediosPagosService mediosPagosService;
	
	@Autowired
	private CajaMovimientoTarjetaService movimientoTarjetaService;
	
	@Autowired
	private ReservasPagoGiftCardService pagoGiftCardService;
	
    @Autowired
    private VariablesServices variablesServices;
	
	@Autowired
	private Sesion sesion;
	
	@Autowired
	private ByLApartadosService bylApartadosService;
	
	/* Para guardar el apartado, para poder usarlo en el recalculo. */
	ApartadosCabeceraBean apartado = new ApartadosCabeceraBean();
	
	private boolean preciosRecalculados;
	
	public static final String CODIGO_DOCUMENTO_RESERVA = "RE";
	public static final String CONTADOR_RESERVAS = "ID_RESERVA";
	
	final IVisor visor = Dispositivos.getInstance().getVisor();
	
	private boolean creacionApartado;
	
	protected Logger log = Logger.getLogger(getClass());
	
	@SuppressWarnings("unchecked")
    @Override
	public void initialize(URL url, ResourceBundle rb){
		super.initialize(url, rb);
		frDatosApartado = SpringContext.getBean(FormularioDetalleApartadoBean.class);
		frArticulo = SpringContext.getBean(FormularioLineaArticuloBean.class);

		frDatosApartado.setFormField("fechaRecogida", tfFechaRecogida);
		frArticulo.setFormField("codArticulo", tfArticulo);

		articulos = FXCollections.observableArrayList();
		tbArticulos.setPlaceholder(new Label(""));

		/* BYL-176 ajustar columnas para importes altos */
		 tbArticulos.getItems().addListener(new ListChangeListener<Object>() {
			private Method columnToFitMethod;
		    private void loadMethod() {
		    	try {
		            columnToFitMethod = TableViewSkin.class.getDeclaredMethod("resizeColumnToFitContent", TableColumn.class, int.class);
		            columnToFitMethod.setAccessible(true);
		        } catch (NoSuchMethodException e) {
		            e.printStackTrace();
		        }
		    }
            @Override
            public void onChanged(Change<?> c) {
            	if(columnToFitMethod == null) {
            		loadMethod();
            	}
            	try {
            		columnToFitMethod.invoke(tbArticulos.getSkin(), tcImporte, -1);
            		columnToFitMethod.invoke(tbArticulos.getSkin(), tcPrecio, -1);
            	} catch (Exception e) {
            		log.warn("initialize() - No se pueden ajustar columnas a contenido: " + e.getMessage(), e);
            	}
            }
        });
		 
		tcArticulo.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbLineas", "tcArticulo", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
		tcDescripcion.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbLineas", "tcDescripcion", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
		tcDesglose1.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbLineas", "tcDesglose1", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
		tcDesglose2.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbLineas", "tcDesglose2", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
		tcPrecio.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbLineas", "tcPrecio", 2, CellFactoryBuilder.ESTILO_ALINEACION_DCHA));
		tcImporte.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbLineas", "tcImporte", 2, CellFactoryBuilder.ESTILO_ALINEACION_DCHA));
		tcDescuento.setCellFactory(CellFactoryBuilder.createCellRendererCeldaPorcentaje("tcDescuento", "tcDescuento", 2, CellFactoryBuilder.ESTILO_ALINEACION_DCHA));
		tcCantidad.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbLineas", "tcCantidad", 3, CellFactoryBuilder.ESTILO_ALINEACION_DCHA));

		Boolean usaDescuentoEnLinea = variablesServices.getVariableAsBoolean(VariablesServices.TICKETS_USA_DESCUENTO_EN_LINEA);
		if(!usaDescuentoEnLinea){
			tcDescuento.setVisible(false);
		}
		if(sesion.getAplicacion().isDesglose1Activo()){
			tcDesglose1.setText(I18N.getTexto(variablesServices.getVariableAsString(VariablesServices.ARTICULO_DESGLOSE1_TITULO)));
		}
		else{ 
			tcDesglose1.setVisible(false);
		}
		if(sesion.getAplicacion().isDesglose2Activo()){
			tcDesglose2.setText(I18N.getTexto(variablesServices.getVariableAsString(VariablesServices.ARTICULO_DESGLOSE2_TITULO)));
		}
		else{
			tcDesglose2.setVisible(false);
		}

		tbArticulos.setItems(articulos);
		
		tcArticulo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaArticuloApartadoGui, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<LineaArticuloApartadoGui, String> cdf){
				return cdf.getValue().getArticuloProperty();
			}
		});
		tcDescripcion.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaArticuloApartadoGui, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<LineaArticuloApartadoGui, String> cdf){
				return cdf.getValue().getDescripcionProperty();
			}
		});
		tcCantidad.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaArticuloApartadoGui, BigDecimal>, ObservableValue<BigDecimal>>(){
			@Override
			public ObservableValue<BigDecimal> call(CellDataFeatures<LineaArticuloApartadoGui, BigDecimal> cdf){
				return cdf.getValue().getCantidadProperty();
			}
		});
		tcDesglose1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaArticuloApartadoGui, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<LineaArticuloApartadoGui, String> cdf){
				return cdf.getValue().getDesglose1Property();
			}
		});
		tcDesglose2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaArticuloApartadoGui, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<LineaArticuloApartadoGui, String> cdf){
				return cdf.getValue().getDesglose2Property();
			}
		});
		tcPrecio.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaArticuloApartadoGui, BigDecimal>, ObservableValue<BigDecimal>>(){
			@Override
			public ObservableValue<BigDecimal> call(CellDataFeatures<LineaArticuloApartadoGui, BigDecimal> cdf){
				return cdf.getValue().getPvpProperty();
			}
		});
		tcImporte.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaArticuloApartadoGui, BigDecimal>, ObservableValue<BigDecimal>>(){
			@Override
			public ObservableValue<BigDecimal> call(CellDataFeatures<LineaArticuloApartadoGui, BigDecimal> cdf){
				return cdf.getValue().getImporteTotalProperty();
			}
		});
		tcDescuento.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaArticuloApartadoGui, BigDecimal>, ObservableValue<BigDecimal>>(){
			@Override
			public ObservableValue<BigDecimal> call(CellDataFeatures<LineaArticuloApartadoGui, BigDecimal> cdf){
				return cdf.getValue().getDescuentoProperty();
			}
		});
		
		tbArticulos.setRowFactory(new Callback<TableView<LineaArticuloApartadoGui>, TableRow<LineaArticuloApartadoGui>>(){
			@Override
			public TableRow<LineaArticuloApartadoGui> call(TableView<LineaArticuloApartadoGui> p){
				final TableRow<LineaArticuloApartadoGui> row = new TableRow<LineaArticuloApartadoGui>(){
					@Override
					protected void updateItem(LineaArticuloApartadoGui linea, boolean empty){
						super.updateItem(linea, empty);
						if(linea != null){
							if(linea.isVendido()){
								getStyleClass().removeAll(Collections.singleton("cell-renderer-apartadoCancelado"));
								if(!getStyleClass().contains("cell-renderer-lineaDocAjeno")){
									getStyleClass().add("cell-renderer-lineaDocAjeno");
								}
							}
							else if(linea.getDetalle().getEstadoLineaApartado() == ApartadosCabeceraBean.ESTADO_CANCELADO){
								getStyleClass().removeAll(Collections.singleton("cell-renderer-cupon"));
								if(!getStyleClass().contains("cell-renderer-apartadoCancelado")){
									getStyleClass().add("cell-renderer-apartadoCancelado");
								}
							}
							else{
								getStyleClass().removeAll(Collections.singleton("cell-renderer-lineaDocAjeno"));
								getStyleClass().removeAll(Collections.singleton("cell-renderer-apartadoCancelado"));
								getStyleClass().removeAll(Collections.singleton("cell-renderer-cupon"));
							}
						}
						else{
							getStyleClass().removeAll(Collections.singleton("cell-renderer-lineaDocAjeno"));
							getStyleClass().removeAll(Collections.singleton("cell-renderer-apartadoCancelado"));
							getStyleClass().removeAll(Collections.singleton("cell-renderer-cupon"));

						}
					}
				};
				return row;
			}
		});
		
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initializeForm() throws InitializeGuiException{
		creacionApartado = false;
		
		apartado = new ApartadosCabeceraBean();
		
		/* Si no tenemos que inicializar la pantalla nos salimos. */
		if(!getDatos().containsKey(INICIALIZAR_PANTALLA)){
			return;
		}
		else{
			getDatos().remove(INICIALIZAR_PANTALLA);
		}
		if(!getDatos().isEmpty()){
			nuevosArticulos = 0;
			actionHandlerBuscar = null;
			articulos.clear();
			frArticulo.clearErrorStyle();

			if(getDatos().containsKey(PARAMETRO_APARTADO_MGR)){
				apartadoManager = (ApartadosManager) getDatos().remove(PARAMETRO_APARTADO_MGR);
			}
			if(getDatos().containsKey(FacturacionArticulosController.TICKET_KEY)){
				ticketManager = (TicketManager) getDatos().remove(FacturacionArticulosController.TICKET_KEY);
			}
			if(getDatos().containsKey(PARAMETRO_APARTADO)){
				/* Guardamos el apartado. */
				apartado = (ApartadosCabeceraBean) getDatos().remove(PARAMETRO_APARTADO);
				apartadoManager.cargarApartado(apartado);
				
				List<ApartadosDetalleBean> apartados = apartadosService.consultarArticulosApartados(apartado.getUidApartado());
				if(apartados.get(0) != null) {
					tfFechaRecogida.setSelectedDate(apartados.get(0).getFechaPrevistaRecogida());
				}
				
				lbCabecera.setText(I18N.getTexto("Detalle reserva {0}", apartado.getIdApartado()));
				refrescarDatosPantalla();
			}
			else{
				creacionApartado = true;
				apartadoManager.nuevoTicketApartado();
				ClienteBean clienteInicial = apartadoManager.getCliente();
				tfCodCliente.setText(clienteInicial.getCodCliente());
				tfCP.setText(clienteInicial.getCp());
				tfDesCliente.setText(clienteInicial.getDesCliente());
				tfDomicilio.setText(clienteInicial.getDomicilio());
				tfNumDoc.setText(clienteInicial.getCif());
				tfPoblacion.setText(clienteInicial.getPoblacion());
				tfProvincia.setText(clienteInicial.getProvincia());
				tfTelefono.setText(clienteInicial.getTelefono1());
				tfFechaRecogida.setSelectedDate(new Date());

				lbSaldo.setText(FormatUtil.getInstance().formateaImporte(BigDecimal.ZERO));
				lbPendiente.setText(FormatUtil.getInstance().formateaImporte(BigDecimal.ZERO));
				lbServido.setText(FormatUtil.getInstance().formateaImporte(BigDecimal.ZERO));
				lbAbonado.setText(FormatUtil.getInstance().formateaImporte(BigDecimal.ZERO));
				lbCabecera.setText(I18N.getTexto("Nueva reserva"));
			}
			if(getDatos().containsKey(PARAMETRO_EDITAR_APARTADO)){
				modoEdicion = true;
				tfArticulo.setDisable(false);
				tfFechaRecogida.setDisable(false);
				btBuscarArticulos.setDisable(false);
				if(getDatos().containsKey(PARAMETRO_NUEVO_APARTADO)){
					actionHandlerBuscar = (EventHandler) getDatos().get(PARAMETRO_EVENTO_BUSQUEDA);
					apartadoNuevo = true;
				}
				else{
					apartadoNuevo = false;
				}

				try{
					ticketManager.nuevoTicket();
					ticketManager.setEsApartado(true);
					
					if(apartadoManager.getTicketApartado().getArticulos() != null && !apartadoManager.getTicketApartado().getArticulos().isEmpty()){
						for (ApartadosDetalleBean apartado : apartadoManager.getTicketApartado().getArticulos()) {
							try {
								ticketManager.nuevaLineaArticulo(apartado.getCodart(), apartado.getDesglose1(), apartado.getDesglose2(), apartado.getCantidad(), apartado.getLinea());
							} catch (LineaTicketException e) {
								String mensajeError = I18N.getTexto("Se produjo un error en la carga de lineas de ticket");
								log.error("setDatosFidelizado() - " + mensajeError + " - " + e.getMessage());
								VentanaDialogoComponent.crearVentanaAviso(mensajeError, getStage());
							}
						}
						
						if(ticketManager.getTicket().getLineas() != null && !ticketManager.getTicket().getLineas().isEmpty()){
							for (ByLLineaTicket linea  :  (List<ByLLineaTicket>) ticketManager.getTicket().getLineas()) {
								linea.setNuevo(Boolean.FALSE);
							}
						}
					}
				}
				catch(PromocionesServiceException | DocumentoException e){
					log.error("Error creando el nuevo ticket.", e);
				}
			}
			else{
				modoEdicion = false;
				tfArticulo.setDisable(true);
				tfFechaRecogida.setDisable(true);
				btBuscarArticulos.setDisable(true);
				apartadoNuevo = false;
			}
			refrescarDatosPantalla();
			escribirEntradaEnVisor();
		}
	}

	protected List<ConfiguracionBotonBean> cargarAccionesTabla(){
		List<ConfiguracionBotonBean> listaAcciones = new ArrayList<>();
		if(!modoEdicion){
			/* ============================= VOLVER ============================= */
			listaAcciones.add(new ConfiguracionBotonBean("iconos/back.png", null, null, "ACCION_TABLA_VOLVER_A_APARTADOS", "REALIZAR_ACCION"));
		}
		/* ============================= PRIMER REGISTRO ============================= */
		listaAcciones.add(new ConfiguracionBotonBean("iconos/navigate_up2.png", null, null, "ACCION_TABLA_PRIMER_REGISTRO", "REALIZAR_ACCION"));
		/* ============================= ANTERIOR REGISTRO ============================= */
		listaAcciones.add(new ConfiguracionBotonBean("iconos/navigate_up.png", null, null, "ACCION_TABLA_ANTERIOR_REGISTRO", "REALIZAR_ACCION"));
		/* ============================= SIGUIENTE REGISTRO ============================= */
		listaAcciones.add(new ConfiguracionBotonBean("iconos/navigate_down.png", null, null, "ACCION_TABLA_SIGUIENTE_REGISTRO", "REALIZAR_ACCION"));
		/* ============================= ÚLTIMO REGISTRO ============================= */
		listaAcciones.add(new ConfiguracionBotonBean("iconos/navigate_down2.png", null, null, "ACCION_TABLA_ULTIMO_REGISTRO", "REALIZAR_ACCION"));
		if(modoEdicion){
			/* ============================= CONFIRMAR ============================= */
			listaAcciones.add(new ConfiguracionBotonBean("iconos/aceptar.png", null, null, "ACCION_TABLA_CONFIRMAR_CAMBIOS", "REALIZAR_ACCION"));
			/* ============================= RECHAZAR ============================= */
			listaAcciones.add(new ConfiguracionBotonBean("iconos/cancelar.png", null, null, "ACCION_TABLA_RECHAZAR_CAMBIOS", "REALIZAR_ACCION"));
			if(!apartadoNuevo){
				if(!apartado.getEstadoApartado().toString().equals("1") && !apartado.getEstadoApartado().toString().equals("2")){
					/* ============================= RECALCULAR PRECIOS ============================= */
					listaAcciones.add(new ConfiguracionBotonBean("iconos/recalcularPrecios.png", null, null, "ACCION_RECALCULAR_PRECIOS", "REALIZAR_ACCION"));
				}
			}
		}
		else{
			/* ============================= BORRAR ============================= */
			listaAcciones.add(new ConfiguracionBotonBean("iconos/row_delete.png", null, null, "ACCION_TABLA_BORRAR_REGISTRO", "REALIZAR_ACCION"));
		}
		return listaAcciones;
	}
	
	@Override
	public void realizarAccion(BotonBotoneraComponent botonAccionado){
		log.debug("realizarAccion() - Realizando la acción : " + 
		botonAccionado.getClave() + " de tipo : " + botonAccionado.getTipo());
		
    	switch (botonAccionado.getClave()){
    	case "ACCION_TABLA_PRIMER_REGISTRO":
    		accionTablaPrimerRegistro(tbArticulos);
    		initializeFocus();
    		break;
    	case "ACCION_TABLA_ANTERIOR_REGISTRO":
    		accionTablaIrAnteriorRegistro(tbArticulos);
    		initializeFocus();
    		break;
    	case "ACCION_TABLA_SIGUIENTE_REGISTRO":
    		accionTablaIrSiguienteRegistro(tbArticulos);
    		initializeFocus();
    		break;
    	case "ACCION_TABLA_ULTIMO_REGISTRO":
    		accionTablaUltimoRegistro(tbArticulos);
    		initializeFocus();
    		break;
    	case "ACCION_TABLA_BORRAR_REGISTRO":
    		accionEliminarArticulo();
    		initializeFocus();
    		break;
    	case "ACCION_TABLA_CONFIRMAR_CAMBIOS":
    		confirmarCambios();
    		initializeFocus();
    		break;
    	case "ACCION_TABLA_RECHAZAR_CAMBIOS":
    		rechazarCambios();
    		break;
    	case "ACCION_TABLA_VOLVER_A_APARTADOS":
    		close();
    		break;
    	case "ACCION_RECALCULAR_PRECIOS":
    		/* Preguntamos primero si quiere recalcular precios. */
    		 if(VentanaDialogoComponent.crearVentanaConfirmacion(
    				 I18N.getTexto("Se recalcularán los precios, se perderán "
    				 		+ "los anteriores ¿Desea continuar?"), getStage())){
    			 recalcularPrecios();
             }else{
            	 return; 
             }
    		break;
    	default:
    		log.error("No se ha especificado acción en pantalla para la operación :" + botonAccionado.getClave());
    		break;
    	}
	}
	
	/**
	 * Comprueba linea por linea para aplicar el precio de la tarifa
	 * o el precio de una promo, y recalcular los precios. 
	 */
	public void recalcularPrecios(){
		Map<String, LineaTicket> mapaPrecios = new HashMap<String, LineaTicket>();
		preciosRecalculados = true;
		for(ApartadosDetalleBean articulo : apartadoManager.getTicketApartado().getArticulos()){
			try{
				/* Bloqueamos el recalculo de las lineas que tengan estado Cancelada(1) o que tengan estado
				 * Finalizada(2). */
				if(!articulo.getEstadoLineaApartado().toString().equals("1") && !articulo.getEstadoLineaApartado().toString().equals("2")){
					/* Creamos un nuevo ticket donde vamos a introducir los datos para calcular las promociones
					 * correctamente. */
					ByLTicketManager ticketCalculoPromo = SpringContext.getBean(ByLTicketManager.class);
					ticketCalculoPromo.nuevoTicket();
					ticketCalculoPromo.setDocumentoActivo(sesion.getAplicacion().getDocumentos().getDocumento(Documentos.FACTURA_SIMPLIFICADA));
					ticketCalculoPromo.getTicket().setCliente(apartadoManager.getCliente());
					FidelizacionBean fidelizado = new FidelizacionBean();
					if(!(Dispositivos.getInstance().getFidelizacion() instanceof FidelizacionNoConfig)) {
						fidelizado = Dispositivos.getInstance().getFidelizacion().consultarTarjetaFidelizado(apartado.getTarjetaFidelizacion(), sesion.getAplicacion().getUidActividad());
					}
					if(fidelizado != null){
						ticketCalculoPromo.getTicket().getCabecera().setDatosFidelizado(fidelizado);
					} else {
						ticketCalculoPromo.getTicket().getCabecera().setDatosFidelizado(apartado.getTarjetaFidelizacion());
					}
					/* Solo añadimos de cantidad 1, porque en los apartados no ahi mas de uno. */
					ticketCalculoPromo.nuevaLineaArticulo(articulo.getCodart(), articulo.getDesglose1(), articulo.getDesglose2(), articulo.getCantidad(), articulo.getLinea());
					ticketCalculoPromo.recalcularConPromociones();
					/* Añadimos el precio que hemos comprobado para el articulo indicado en el mapa. */
					LineaTicket lineaCalculo = (LineaTicket) ticketCalculoPromo.getTicket().getLineas().get(0);
					mapaPrecios.put(articulo.getCodart() + " " + articulo.getDesglose1() + " " + articulo.getDesglose2(), lineaCalculo);
				}

			}
			catch(PromocionesServiceException | DocumentoException | LineaTicketException | ConsultaTarjetaFidelizadoException e){
				log.error("recalcularPrecios() - " + e.getMessage());
				VentanaDialogoComponent.crearVentanaError(I18N.getTexto("Se produjo error al obtener información del fidelizado. Los precios no se han actualizado. Inténtelo más tarde o comunique la incidencia"), getStage());
			}
		}
		/* Ahora que tenemos el mapa lleno con los articulos y los precios finales para cada uno, se los añadimos. */
		for(ApartadosDetalleBean linea : apartadoManager.getTicketApartado().getArticulos()){
			for(Map.Entry<String, LineaTicket> entry : mapaPrecios.entrySet()){
				String[] split = entry.getKey().split(" ");
				String codart = split[0];
				String desglose1 = split[1];
				String desglose2 = split[2];
				if(codart.equals(linea.getCodart()) && desglose1.equals(linea.getDesglose1()) && desglose2.equals(linea.getDesglose2())){
					/* El valor a modificar es el Precio Total. */
					linea.setPrecio(entry.getValue().getPrecioTotalSinDto());
					/* EL importe lo calculamos multiplicando el precio por cantidad. */
					linea.setImporteTotal(entry.getValue().getImporteTotalConDto());
					
					linea.setDescuento(entry.getValue().getDescuento());
				}
			}
		}
		/* Recalculamos totales, ya que la parte de anotar pagos tiene que calcularse con los nuevos precios. */
		apartadoManager.getTicketApartado().calcularTotales();
		/* Refrescamos la pantalla para aplicar los cambios de precio. */
		refrescarDatosPantalla();
	}
	
	/**
	 * Realiza la acción de confirmar en la pantalla de detalles
	 * para el botón con forma de V.
	 */
	@SuppressWarnings("unchecked")
	public void confirmarCambios(){
		log.trace("confirmarCambios() - Iniciamos el guardado del Apartado...");
		if(cajasService.comprobarCajaMaster()){
			if(tfFechaRecogida.getTexto() != null && !tfFechaRecogida.getTexto().isEmpty()){
				frDatosApartado.setFechaRecogida(tfFechaRecogida.getTexto());
							
				Fecha f = Fecha.getFecha(tfFechaRecogida.getSelectedDate());
				Fecha fMinima = new Fecha("01/01/1753");
				
				if(f.despuesOrEquals(fMinima)){
					/* Realizamos una validación de los datos del apartado, en caso de ser correcto procedemos a guardarlo */
					if(validarDatosApartado()){
						/* En caso de intentar realizar el apartado vacío se lo impedimos */
						if(apartadoManager.getTicketApartado().getArticulos().isEmpty() && nuevosArticulos == 0){
							String mensajeInfo = I18N.getTexto("No se puede crear un apartado vacío");
							log.info("confirmarCambios() - " + mensajeInfo);
							VentanaDialogoComponent.crearVentanaAviso(mensajeInfo, this.getStage());
							return;
						}
						else{
							/* ========================= GUARDAR EN BD ========================= */
							/* Iniciamos el apartado en caso de venir como nuevo */
							if(apartadoNuevo){
								apartadoManager.nuevoApartado();
								apartadoNuevo = false;
								
								bylApartadosService.insertarNuevoXApartadoCabecera(apartadoManager.getTicketApartado().getCabecera(), sesion.getAplicacion().getCodCaja());
							}
							/* Insertamos los artículos en el apartado */
							for(ApartadosDetalleBean linea : apartadoManager.getTicketApartado().getArticulos()){
								if(linea.isArticuloNuevo()){
									linea.setUidApartado(apartadoManager.getTicketApartado().getCabecera().getUidApartado());
									linea.setFechaPrevistaRecogida(tfFechaRecogida.getSelectedDate());
									apartadoManager.nuevoArticuloApartado(linea);
									linea.setArticuloNuevo(false);
									nuevosArticulos--;
								}
							}
							/* Después de haber insertado todos los artículo realizamos un recalculo de los totales */
							apartadoManager.getTicketApartado().calcularTotales();
							if(apartadoManager.getTicketApartado().getCabecera().getFechaApartado() != null){
								apartadoManager.getTicketApartado().getCabecera().setFechaApartado(apartadoManager.getTicketApartado().getCabecera().getFechaApartado());
							}
							else{
								apartadoManager.getTicketApartado().getCabecera().setFechaApartado(new Date());
							}
							apartadoManager.getTicketApartado().getCabecera().setImporteTotalApartado(apartadoManager.getTicketApartado().getImporteTotal());
		
							/* Realiza un UPDATE o CREATE a la tabla "D_CLIE_APARTADOS_CAB_TBL" */
							apartadoManager.actualizarCabecera();
		
							if(preciosRecalculados){
								for(ApartadosDetalleBean detalle : apartadoManager.getTicketApartado().getArticulos()){
									/* Realiza un UPDATE o CREATE a la tabla "D_CLIE_APARTADOS_ART_TBL" */
									apartadosService.actualizarDetalleApartado(detalle);
								}
							}
		
							String mensajeInfo = I18N.getTexto("Cambios guardados correctamente");
							log.info("confirmarCambios() - " + mensajeInfo);
							VentanaDialogoComponent.crearVentanaInfo(mensajeInfo, getStage());
		
							modoEdicion = false;
							int rowSelected = tbArticulos.getSelectionModel().getSelectedIndex();
							if(rowSelected < 0){
								tbArticulos.getSelectionModel().select(0);
							}
		
							/* Generamos el Ticket de la Reserva a partir de los datos de TicketManager y de
							 * ApartadosManager */
							try{
								generarTicketReserva(null, null, null, false, false);
							}
							catch(TicketReservaException e){
								log.error("confirmarCambios() - " + e.getMessage());
								VentanaDialogoComponent.crearVentanaError(I18N.getTexto(e.getMessage()), getStage());
								return;
							}
		
							ticketManager.finalizarTicket();
		
							if(actionHandlerBuscar != null){
								actionHandlerBuscar.handle(null);
								close();
							}
							else{
								refrescarDatosPantalla();
							}
						}
					}
					
				}
				else {
					VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Fecha incorrecta. Revise valores"), getStage());
					tfFechaRecogida.requestFocus();
				}
			}else{
				VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("La fecha de recogida es obligatoria"), getStage());
				tfFechaRecogida.requestFocus();
			}
		}
		else {
			cajaMasterCerrada();
		}
	}

	public void fidelizacion(){
		log.debug("fidelizacion() - Iniciamos el proceso para agregar un Fidelizado a la Reserva...");
		if(modoEdicion && creacionApartado){
			Dispositivos.getInstance().getFidelizacion().pedirTarjetaFidelizado(getStage(), new DispositivoCallback<FidelizacionBean>(){
				@Override
				public void onSuccess(FidelizacionBean fidelizado){
					if(fidelizado.getIdFidelizado() != null){
						if(fidelizado.getIdFidelizado() == 0L){
							String mensajeConfirmacion = I18N.getTexto("No se ha encontrado ningún Fidelizado con los datos introducidos" 
									+ "\n¿Desea realizar el Alta del Cliente?");
							if(VentanaDialogoComponent.crearVentanaConfirmacion(mensajeConfirmacion, getStage())){
								ByLBusquedaFidelizadoController busqueda = SpringContext.getBean(ByLBusquedaFidelizadoController.class);
								busqueda.accionAltaFidelizado();
							}
						}
						else{
							if(fidelizado.isBaja()){
								String mensajeError = I18N.getTexto("La Tarjeta de Fidelización " + fidelizado.getNumTarjetaFidelizado() + " no está activa");
								VentanaDialogoComponent.crearVentanaError(mensajeError, getStage());
								fidelizado = null;
								setDatosFidelizado(fidelizado);
								apartadoManager.actualizarCabecera();
							}else{
								/* Tarjeta válida, la seteamos en el Ticket */
								setDatosFidelizado(fidelizado);
								apartadoManager.getTicketApartado().getCabecera().setTarjetaFidelizacion(fidelizado.getNumTarjetaFidelizado());

								log.debug("fidelizacion() - Datos del Fidelizado : ");
								log.debug("fidelizacion() - ID del Fidelizado - " + fidelizado.getIdFidelizado());
								log.debug("fidelizacion() - Número de Tarjeta - " + fidelizado.getNumTarjetaFidelizado());
								log.debug("fidelizacion() - Nombre : " + fidelizado.getNombre());

								apartadoManager.actualizarCabecera();
							}
						}
					}
					else{
						if(fidelizado.isBaja()){
							String mensajeError = I18N.getTexto("La Tarjeta de Fidelización " + fidelizado.getNumTarjetaFidelizado() + " no está activa");
							VentanaDialogoComponent.crearVentanaError(mensajeError, getStage());
							fidelizado = null;
							setDatosFidelizado(fidelizado);
							apartadoManager.actualizarCabecera();
						}else{
							/* Tarjeta válida, la seteamos en el Ticket */
							setDatosFidelizado(fidelizado);
							apartadoManager.getTicketApartado().getCabecera().setTarjetaFidelizacion(fidelizado.getNumTarjetaFidelizado());

							log.debug("fidelizacion() - Datos del Fidelizado : ");
							log.debug("fidelizacion() - ID del Fidelizado - " + fidelizado.getIdFidelizado());
							log.debug("fidelizacion() - Número de Tarjeta - " + fidelizado.getNumTarjetaFidelizado());
							log.debug("fidelizacion() - Nombre : " + fidelizado.getNombre());

							apartadoManager.actualizarCabecera();
						}
					}
					refrescarDatosPantalla();
				}

				@Override
				public void onFailure(Throwable e){
					refrescarDatosPantalla();
				}
			});
		}
		else{
			String mensajeAviso = I18N.getTexto("No es posible editar el cliente en una reserva ya iniciada");
			log.debug("fidelizacion() - " + mensajeAviso);
			VentanaDialogoComponent.crearVentanaAviso(mensajeAviso, getStage());
		}
		log.debug("fidelizacion() - Finalizado el proceso para agregar un Fidelizado a la Reserva");
	}

	@SuppressWarnings("unchecked")
    public void setDatosFidelizado(FidelizacionBean tarjeta){
		apartado.setCif(tarjeta.getDocumento() != null ? tarjeta.getDocumento() : "-");
		apartado.setPoblacion(tarjeta.getPoblacion());
		apartado.setProvincia(tarjeta.getProvincia());
		apartado.setTelefono1(((ByLFidelizacionBean) tarjeta).getTelefono());
		apartado.setCodtar(null);
		apartado.setIdTratImpuestos(null);
		apartado.setCodpais(tarjeta.getCodPais());
		apartado.setEmail(((ByLFidelizacionBean) tarjeta).getEmail());
		apartado.setFax(null);
		apartado.setDomicilio(tarjeta.getDomicilio());
		apartado.setCodCliente(tarjeta.getDocumento());
		apartado.setCodPostal(tarjeta.getCp());
		apartado.setDesCliente(tarjeta.getNombre() + " " + tarjeta.getApellido());
		apartado.setTarjetaFidelizacion(tarjeta.getNumTarjetaFidelizado());

		ClienteBean clienteApartado = apartadoManager.getCliente();
		clienteApartado.setCodCliente(sesion.getAplicacion().getCodAlmacen());
		clienteApartado.setCp(tarjeta.getCp());
		String nombre = StringUtils.isNotBlank(tarjeta.getNombre()) ? tarjeta.getNombre() : "Cliente";
		String apellido = StringUtils.isNotBlank(tarjeta.getApellido()) ? tarjeta.getApellido() : "Genérico";
		clienteApartado.setDesCliente(nombre + " " + apellido);
		
		clienteApartado.setDomicilio(tarjeta.getDomicilio());
		clienteApartado.setPoblacion(tarjeta.getPoblacion());
		clienteApartado.setProvincia(tarjeta.getProvincia());
		
		clienteApartado.setCif(tarjeta.getDocumento());
		clienteApartado.setTelefono1(((ByLFidelizacionBean) tarjeta).getTelefono());
		clienteApartado.setEmail(((ByLFidelizacionBean) tarjeta).getEmail());
		apartadoManager.setCliente(clienteApartado);
				
		ticketManager.getTicket().getCabecera().setDatosFidelizado(tarjeta);
		ticketManager.recalcularConPromociones();
		
		apartadoManager.getTicketApartado().getArticulos().clear();
		for(ByLLineaTicket linea : (List<ByLLineaTicket>) ticketManager.getTicket().getLineas()) {
			introducirDetalleApartado(linea, linea.getNuevo() == null ? true : linea.getNuevo());
		}
		apartadoManager.actualizarCabecera();
		
		refrescarDatosPantalla();
		
//		confirmarCambios();
	}
	
	/**
	 * Realiza la acción de eliminar un articulo cuando entras en 
	 * la pantalla de detalles una segunda vez.
	 */
	public void accionEliminarArticulo(){
		if(cajasService.comprobarCajaMaster()){
			super.accionEliminarArticulo();
			try{
				generarTicketReserva(null, null, null, false, false);
			}
			catch(TicketReservaException e){
				log.error("accionEliminarArticulo() - " + e.getMessage());
				VentanaDialogoComponent.crearVentanaError(I18N.getTexto(e.getMessage()), getStage());
				return;
			}
		}
		else{
			cajaMasterCerrada();
		}
	}

	/**
	 * Realiza la acción de rechazar los cambios para el botón
	 * de X de la pantalla de detalles.
	 */
	public void rechazarCambios(){
		if(cajasService.comprobarCajaMaster()){
			super.rechazarCambios();
		}
		else{
			cajaMasterCerrada();
		}
	}
	
	@Override
	public boolean canClose(){
		log.debug("canClose() - Iniciamos comprobación de cierre...");
		if(modoEdicion){
			if(cajasService.comprobarCajaMaster()){
				String mensajeAviso = I18N.getTexto("Debe confirmar o rechazar los cambios para poder cerrar la pantalla de detalle de apartado");
				log.info("canClose() - " + mensajeAviso);
				VentanaDialogoComponent.crearVentanaAviso(mensajeAviso, getStage());
				return false;
			}
			else{
				return true;
			}
		}
		return true;
	}

	/**
	 * Envía un mensaje por pantalla, que indica que la caja no está abierta
	 * y te envía a la pantalla principal.
	 */
	private void cajaMasterCerrada(){
		String mensajeInfo = I18N.getTexto("La caja a la que estaba conectado se ha cerrado");
		log.info("cajaMasterCerrada() - " + mensajeInfo);
		VentanaDialogoComponent.crearVentanaAviso(mensajeInfo, this.getStage());
		POSApplication.getInstance().getMainView().close();
	}
	
	/**
	 * Abrimos la pantalla de pagos para registrarlo.
	 */
	public void abrirPagos(){
		/* Primero debemos comprobar si debemos editar o añadir */
		if(modoEdicion){
			String mensajeAviso = I18N.getTexto("Debe confirmar o rechazar antes los cambios");
			log.info("abrirPagos() - " + mensajeAviso);
			VentanaDialogoComponent.crearVentanaAviso(mensajeAviso, getStage());
			initializeFocus();
		}
		else{
			if(apartadoManager.getTicketApartado().getCabecera().getEstadoApartado() != ApartadosCabeceraBean.ESTADO_DISPONIBLE){
				String mensajeAviso = I18N.getTexto("No se pueden añadir pagos al apartado");
				log.info("abrirPagos() - " + mensajeAviso);
				VentanaDialogoComponent.crearVentanaAviso(mensajeAviso, getStage());
			}
			else{
				if(apartadoManager.getTicketApartado().getTotalPendiente().compareTo(BigDecimal.ZERO) > 0){
					log.debug("abrirPagos() - El ticket tiene líneas");
					try{
						ticketManager.nuevoTicketPagosApartado();
						ticketManager.getTicket().getTotales().setTotalAPagar(apartadoManager.getTicketApartado().getTotalPendiente());

						getDatos().put(PARAMETRO_IMPORTE_MAXIMO_PAGO, ticketManager.getTicket().getTotales().getTotalAPagar());
						getApplication().getMainView().showModalCentered(PagoApartadoView.class, getDatos(), getStage());

						if(getDatos().containsKey(PagoApartadoController.PARAMETRO_IMPORTE_PAGO)){
							BigDecimal importePago = (BigDecimal) getDatos().remove(PagoApartadoController.PARAMETRO_IMPORTE_PAGO);
							ticketManager.getTicket().getTotales().setTotalAPagar(importePago);

							getDatos().put(FacturacionArticulosController.TICKET_KEY, ticketManager);
							getDatos().put(NuevoPagoApartadoController.PARAMETRO_APARTADO_MANAGER, apartadoManager);
							getApplication().getMainView().showModal(NuevoPagoApartadoView.class, getDatos());

							escribirEntradaEnVisor();
							apartadoManager.actualizarPagos();

							/* En caso de haber cancelado el pago no generamos nada */
							if(!getDatos().containsKey(PagosController.ACCION_CANCELAR)){
								/* Generamos de nuevo el Ticket de Reserva en el momento que se modifica */
								try{
									generarTicketReserva(null, null, null, true, false);
								}
								catch(TicketReservaException e){
									log.error("abrirPagos() - " + e.getMessage());
									VentanaDialogoComponent.crearVentanaError(I18N.getTexto(e.getMessage()), getStage());
									return;
								}
							}

							refrescarDatosPantalla();
							getView().resetSubViews();
						}
					}
					catch(PromocionesServiceException | DocumentoException e){
						String mensajeError = I18N.getTexto("Se produjo un error en la carga del documento de pagos");
						log.error("abrirPagos() - " + mensajeError + " - " + e.getMessage());
						VentanaDialogoComponent.crearVentanaAviso(mensajeError, getStage());
					}
				}
				else{
					String mensajeInfo = I18N.getTexto("El importe pendiente de abonar es 0");
					log.info("abrirPagos() - " + mensajeInfo);
					VentanaDialogoComponent.crearVentanaAviso(mensajeInfo, this.getApplication().getStage());
				}
			}
		}
		initializeFocus();
	}

	/**
	 * Realiza el cambio del cliente en un Apartado.
	 */
	public void cambiarDatosCliente(){
		if(apartadoManager.getTicketApartado().getCabecera().getEstadoApartado() != null
		        && apartadoManager.getTicketApartado().getCabecera().getEstadoApartado() != ApartadosCabeceraBean.ESTADO_DISPONIBLE){
			String mensajeInfo = I18N.getTexto("No se pueden editar datos de un apartado finalizado o cancelado");
			log.info("cambiarDatosCliente() - " + mensajeInfo);
			VentanaDialogoComponent.crearVentanaAviso(mensajeInfo, getStage());
		}
		else{
			getDatos().put(CambiarDatosClienteApartadoController.PARAMETRO_APARTADOS_MANAGER, apartadoManager);
			getApplication().getMainView().showModalCentered(CambiarDatosClienteApartadoView.class, getDatos(), this.getStage());
			/* Generamos de nuevo el Ticket de Reserva en el momento que se modifica */
			try{
				generarTicketReserva(null, null, null, false, false);
			}
			catch(TicketReservaException e){
				log.error("cambiarDatosCliente() - " + e.getMessage());
				VentanaDialogoComponent.crearVentanaError(I18N.getTexto(e.getMessage()), getStage());
				return;
			}
			refrescarDatosPantalla();
			log.debug("cambiarDatosCliente() - Se ha actualizado el cliente : " + apartadoManager.getCliente().getDesCliente());
		}
	}
	
	@Override
	public void vender(){
		log.trace("vender()");
		if(modoEdicion){
			String mensajeAviso = I18N.getTexto("Debe confirmar antes los cambios");
			log.debug("vender() - " + mensajeAviso);
			VentanaDialogoComponent.crearVentanaAviso(mensajeAviso, getStage());
			initializeFocus();
		}
		else{
			List<ApartadosDetalleBean> articulos = apartadoManager.getTicketApartado().getArticulos();
			List<ApartadosDetalleBean> articulosDisponiblesVenta = new ArrayList<ApartadosDetalleBean>();

			for(ApartadosDetalleBean detalleApartado : articulos){
				if(detalleApartado.getEstadoLineaApartado() == ApartadosCabeceraBean.ESTADO_DISPONIBLE && !detalleApartado.isArticuloNuevo()){
					articulosDisponiblesVenta.add(detalleApartado);
				}
			}

			if(articulosDisponiblesVenta.isEmpty()){
				String mensajeAviso = I18N.getTexto("No hay artículos disponibles para su venta");
				log.debug("vender() - " + mensajeAviso);
				VentanaDialogoComponent.crearVentanaAviso(mensajeAviso, this.getApplication().getStage());
			}
			else{
				getDatos().put(MarcarVentaApartadoController.PARAMETRO_APARTADO_MGR, apartadoManager);
				getDatos().put(MarcarVentaApartadoController.PARAMETRO_ARTICULOS, articulosDisponiblesVenta);
				getApplication().getMainView().showModalCentered(MarcarVentaApartadoView.class, getDatos(), this.getStage());
			}

			apartadoManager.getTicketApartado().calcularTotales();
			
			if(!getDatos().containsKey(ByLPagosController.ACCION_CANCELAR)){
				try{
					/* Generamos el Ticket de la Reserva a partir de los datos de TicketManager y de ApartadosManager */
					generarTicketReserva(null, null, null, false, false);
				}
				catch(TicketReservaException e){
					log.error("vender() - " + e.getMessage());
					VentanaDialogoComponent.crearVentanaError(I18N.getTexto(e.getMessage()), getStage());
					return;
				}
			}
			refrescarDatosPantalla();
		}
	}
	
	/**
	 * Genera el XML del Ticket y realiza un guardado del Ticket en BD.
	 * @param esant 
	 * @throws TicketReservaException 
	 */
	public TicketReserva generarTicketReserva(ApartadosManager apartadosManager, ContadorBean contadorTicket, DatosRespuestaTarjetaReservaTicket datosRespuestaTarjeta, Boolean esPagoAnticipo, Boolean esCancelacion) throws TicketReservaException{
		log.debug("generarTicketReserva() - Iniciando la creación del Ticket de Reserva...");
		TicketReserva reserva = new TicketReserva();
		try{
			/* Creamos y insertamos la cabecera en el Ticket */
			reserva.setCabecera(crearCabeceraTicketReserva(apartadosManager, contadorTicket, esPagoAnticipo, esCancelacion));
			/* Si no viene el codCaja, le seteamos el de la caja actual */
			if(reserva.getCabecera().getCodCaja() == null) {
				reserva.getCabecera().setCodCaja(sesion.getSesionCaja().getCajaAbierta().getCodCaja());
			}
			/* A partir de las lineas del TicketManager, generamos las de nuestro Ticket */
			reserva.setLineas(crearLineasTicketReserva(apartadosManager));

			/* Para los pagos creamos un objeto vacio, a no ser que venga algún pago, porque en las reservas no tiene
			 * porque venir un pago. */
			if(apartadoManager != null){
				if(!apartadoManager.getTicketApartado().getMovimientos().isEmpty()){
					reserva.setPagos(crearPagosTicketReserva(apartadosManager));
				}
				else{
					esCancelacion = false;
					reserva.setPagos(new ArrayList<>());
				}
			}
			else{
				if(!apartadosManager.getTicketApartado().getMovimientos().isEmpty()){
					reserva.setPagos(crearPagosTicketReserva(apartadosManager));
				}
				else{
					esCancelacion = false;
					reserva.setPagos(new ArrayList<>());
				}
			}

			/*
			 * Si datosRespuestaTarjeta viene relleno, significa que venimos de una cancelación mediante tarjeta pinpad
			 * de adyen o por standalone, por lo que tendremos que añadirle manualmente a ese medio de pago los datosRespuestaTarjeta
			 */
			if (datosRespuestaTarjeta != null) {
				List<PagoReservaTicket> pagosTarjeta = reserva.getPagos();
				for (PagoReservaTicket pago : pagosTarjeta) {

					/*
					 * Comprobamos que el medio de pago sea el configurado como pinpad y que sea un importe negativo
					 * (cancelación/devolución)
					 */
					if (compruebaPagoTarjetaAdyen(pago.getCodigoMedioPago()) && BigDecimalUtil.isMenorACero(pago.getImporte())) {

						/* Esto se realiza porque si es null al realizar el marshall falla */
						if (datosRespuestaTarjeta.getAdicionales() == null) {
							datosRespuestaTarjeta.setAdicionales(new HashMap<String, String>());
						}

						pago.setDatosRespuestaTarjeta(datosRespuestaTarjeta);
					}
				}
			}
			
			if(esPagoAnticipo || esCancelacion) {
				cajasService.crearDocumentoAnticipoReserva(reserva);
			}
		}
		catch(Exception e){
			log.error("generarTicketReserva() - " + e.getMessage());
			throw new TicketReservaException(e.getMessage(), e);
		}

		/* Generamos el XML del Ticket de Reserva para guardarlo en BD */
		byte[] result;
		try{
			result = MarshallUtil.crearXML(reserva);
			reserva.setXmlTicket(result);
			salvarDocumentoReserva(reserva);
		}
		catch(MarshallUtilException e){
			String mensajeError = "Error al generar el XML del Ticket de Reserva";
			log.error("generarTicketReserva() - " + mensajeError + " - " + e.getMessage());
			throw new TicketReservaException(mensajeError, e);
		}
		log.debug("generarTicketReserva() - Finalizada la creación del Ticket de Reserva");
		return reserva;
	}
	
	/**
	 * Genera la cabecera de un Ticket de Reserva.
	 * @param esCancelacion 
	 * @param esPagoAnticipo2 
	 * @return TicketCabeceraReserva
	 * @throws TicketReservaException 
	 */
	public CabeceraReservaTicket crearCabeceraTicketReserva(ApartadosManager reserva, ContadorBean contadorTicket, Boolean esPagoAnticipo, Boolean esCancelacion) throws TicketReservaException{
		CabeceraReservaTicket cabecera = new CabeceraReservaTicket();
		ApartadosCabeceraBean cabeceraApartado = null;
		if(reserva != null){
			cabeceraApartado = reserva.getTicketApartado().getCabecera();
		}
		else{
			cabeceraApartado = apartadoManager.getTicketApartado().getCabecera();
		}
		
		//TODO BUG RESERVAS
		ByLApartadosCabecera bylApartadosCabecera = bylApartadosService.consultarXApartadoCabecera(cabeceraApartado.getUidActividad(), cabeceraApartado.getUidApartado());
		if(bylApartadosCabecera != null) {
			cabecera.setCodCaja(bylApartadosCabecera.getCodCaja());
		}
		
		if (esPagoAnticipo != null && esPagoAnticipo) {
			cabecera.setEsPagoAnticipo("S");
			cabecera.setEsCancelacion("N");
		}
		
		if (esCancelacion != null && esCancelacion) {
			cabecera.setEsPagoAnticipo("N");
			cabecera.setEsCancelacion("S");
		}
			
		cabecera.setUidApartado(cabeceraApartado.getUidApartado());
		log.debug("crearCabeceraTicketReserva() - UidApartado generado : " + cabecera.getUidApartado());
		cabecera.setUidActividad(sesion.getAplicacion().getUidActividad());
		cabecera.setUidDiarioCaja(sesion.getSesionCaja().getUidDiarioCaja());
		/* Sacamos el estado del apartado de el objeto "ApartadoManager" */
		cabecera.setEstadoApartado(cabeceraApartado.getEstadoApartado());

		/* Recorremos el listado de documentos activos para encontrar el documento de Reserva */
		TipoDocumentoBean tipoDocumentoReserva = null;
		Map<Long, TipoDocumentoBean> mapaDocumentos = sesion.getAplicacion().getDocumentos().getDocumentos();
		for(Map.Entry<Long, TipoDocumentoBean> entry : mapaDocumentos.entrySet()){
			TipoDocumentoBean documento = entry.getValue();
			if(CODIGO_DOCUMENTO_RESERVA.equals(documento.getCodtipodocumento())){
				tipoDocumentoReserva = documento;
			}
		}
		/* En caso de haber encontrado el documento no debemos continuar creando el Ticket */
		if(tipoDocumentoReserva != null){
			cabecera.setTipoDocumento(tipoDocumentoReserva.getIdTipoDocumento().toString());
			cabecera.setCodigoTipoDocumento(tipoDocumentoReserva.getCodtipodocumento());
			cabecera.setDescripcionTipoDocumento(tipoDocumentoReserva.getDestipodocumento());
			cabecera.setFormatoImpresion(tipoDocumentoReserva.getFormatoImpresion());
		}
		else{
			String mensajeError = "Error al localizar el tipo de documento de Reserva";
			log.error("crearCabeceraTicketReserva() - " + mensajeError);
			throw new TicketReservaException(mensajeError);
		}

		/* Calculamos la serie del Ticket a partir de los siguientes datos */
		Map<String, String> parametrosContador = new HashMap<>();
		parametrosContador.put("CODEMP", sesion.getAplicacion().getEmpresa().getCodEmpresa());
		parametrosContador.put("CODTIPODOCUMENTO", cabecera.getCodigoTipoDocumento());
		parametrosContador.put("CODSERIE", sesion.getAplicacion().getTienda().getAlmacenBean().getCodAlmacen());
		parametrosContador.put("CODCAJA", sesion.getAplicacion().getCodCaja());
		parametrosContador.put("PERIODO", ((new Fecha()).getAño().toString()));
		try{
			/* Generamos el ID del apartado a partir del contador */
			ContadorBean ticketContador = null;
			if(contadorTicket != null){
				ticketContador = contadorTicket;
			}
			else{
				ticketContador = contadoresService.obtenerContador(tipoDocumentoReserva.getIdContador(), parametrosContador, cabecera.getUidActividad());
			}
			cabecera.setIdTicket(ticketContador.getValor());
			/* Generamos la serie del Ticket */
			String codTicket = contadoresService.obtenerValorTotalConSeparador(ticketContador.getConfigContador().getValorDivisor3(), ticketContador.getValorFormateado());
			cabecera.setSerieTicket(ticketContador.getConfigContador().getValorDivisor3());
			/* Rellenamos el IdReserva con el serie Ticket delante */
			String idReserva = StringUtils.leftPad(cabeceraApartado.getIdApartado().toString(), 6, "0");
			String serieTicket = cabecera.getSerieTicket().substring(0, cabecera.getSerieTicket().length() - 6) + cabecera.getCodCaja() + Fecha.getFecha(cabeceraApartado.getFechaApartado()).getAño();
			idReserva = serieTicket + idReserva;
			cabecera.setIdReserva(idReserva);
			cabecera.setCodigoApartado(codTicket);

			log.debug("crearCabeceraTicketReserva() - El Contador del Documento Reserva generado es : " + cabecera.getIdTicket());
			log.debug("crearCabeceraTicketReserva() - El Código del Documento Reserva generado es : " + cabecera.getCodigoApartado());
		}
		catch(ContadorServiceException e){
			String mensajeError = "Error al consultar el contador para el Ticket de Reserva";
			log.error("crearCabeceraTicketReserva() - " + mensajeError + " - " + e.getMessage());
			throw new TicketReservaException(mensajeError, e);
		}

		/* Formateamos la fecha actual al formato que se requiere para el Ticket */
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
		cabecera.setFecha(simpleDateFormat.format(cabeceraApartado.getFechaApartado()));
		cabecera.setFechaActualizacion(simpleDateFormat.format(new Date()));

		/* Insertamos los datos de caja, usuario, empresa, cliente, etc.. */
		cabecera.setCajero(sesion.getSesionUsuario().getUsuario());
		cabecera.setTienda(sesion.getAplicacion().getTienda());
		cabecera.setCliente(apartadoManager == null ? reserva.getCliente() : apartadoManager.getCliente());
		cabecera.setEmpresa(sesion.getAplicacion().getEmpresa());

		/* Recalculamos antes los totales del Ticket Manager y luego generamos nuestros totales */
		TotalesReservaTicket totales = new TotalesReservaTicket();
		totales.setTotal(apartadoManager == null ? reserva.getTicketApartado().getTotalAbonado().add(reserva.getTicketApartado().getTotalPendiente()) 
				: apartadoManager.getTicketApartado().getTotalAbonado().add(apartadoManager.getTicketApartado().getTotalPendiente()));
		cabecera.setTotales(totales);
		
		if(apartadoManager != null){
			if(StringUtils.isNotBlank(apartadoManager.getTicketApartado().getCabecera().getTarjetaFidelizacion())){
				DatosFidelizadoReservaTicket datosFidelizado = new DatosFidelizadoReservaTicket();
				datosFidelizado.setNumeroTarjeta(apartadoManager.getTicketApartado().getCabecera().getTarjetaFidelizacion());
				cabecera.setDatosFidelizado(datosFidelizado);
			}
		}
		else{
			if(StringUtils.isNotBlank(reserva.getTicketApartado().getCabecera().getTarjetaFidelizacion())){
				DatosFidelizadoReservaTicket datosFidelizado = new DatosFidelizadoReservaTicket();
				datosFidelizado.setNumeroTarjeta(reserva.getTicketApartado().getCabecera().getTarjetaFidelizacion());
				cabecera.setDatosFidelizado(datosFidelizado);
			}
		}

		/* Ahora rellenemos los datos "especiales" de la cabecera */
		CabeceraEspecialesReservaTicket especiales = new CabeceraEspecialesReservaTicket();
		especiales.setUsuario(cabeceraApartado.getUsuario());
		especiales.setSaldoCliente(cabeceraApartado.getSaldoCliente());
		especiales.setReferenciaCliente(cabeceraApartado.getReferenciaCliente());
		especiales.setObservaciones(cabeceraApartado.getObservaciones());
		cabecera.setEspecialesApartado(especiales);
		
		log.debug("crearCabeceraTicketReserva() - El total del Ticket Reserva es : " + cabecera.getTotales().getTotal());
		return cabecera;
	}
	
	/**
	 * Realiza una copia de los datos de una linea de TicketManager para crear
	 * una linea para un TicketRegalo.
	 * @return List<TicketLineaReserva>
	 */
	public List<LineaReservaTicket> crearLineasTicketReserva(ApartadosManager reserva){
		List<LineaReservaTicket> lineasReserva = new ArrayList<LineaReservaTicket>();
		List<ApartadosDetalleBean> listadoTicket = null;
		if(reserva != null){
			listadoTicket = reserva.getTicketApartado().getArticulos();
		}
		else{
			listadoTicket = apartadoManager.getTicketApartado().getArticulos();
		}
		log.debug("crearLineasTicketReserva() - Total de lineas para crear : " + listadoTicket.size());

		int contadorLineas = 0;
		for(ApartadosDetalleBean linea : listadoTicket){
			LineaReservaTicket lineaReserva = new LineaReservaTicket();
			/* Datos del producto de la linea */
			lineaReserva.setLinea(linea.getLinea());
			lineaReserva.setCodigoArticulo(linea.getCodart());
			lineaReserva.setDescripcionArticulo(linea.getDesart());
			lineaReserva.setDescripcionArticuloAmpliada(linea.getDescripcionAmpliada());
			lineaReserva.setCantidad(linea.getCantidad());
			lineaReserva.setCantidadMedida(linea.getCantidadMedida());
			lineaReserva.setUnidadMedida(linea.getUnidadMedida());
			lineaReserva.setDesglose1(linea.getDesglose1());
			lineaReserva.setDesglose2(linea.getDesglose2());
			/* Tratamos las fechas que puede contener la linea */
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
			lineaReserva.setFechaActualizacion(linea.getFechaActualizacion() != null ? simpleDateFormat.format(linea.getFechaActualizacion()) : null);
			lineaReserva.setFechaApartadoArticulo(linea.getFechaApartadoArticulo() != null ? simpleDateFormat.format(linea.getFechaApartadoArticulo()) : null);
			lineaReserva.setFechaPrevistaRecogida(linea.getFechaPrevistaRecogida() != null ? simpleDateFormat.format(linea.getFechaPrevistaRecogida()) : null);

			/* Tratamos los datos que tienen que ver con los precios */
			lineaReserva.setCodigoImpuesto(linea.getCodimp());
			lineaReserva.setPrecio(linea.getPrecio());
			lineaReserva.setPrecioTotal(linea.getPrecioTotal());
			lineaReserva.setPrecioTotalSinDto(linea.getPrecioTotalSinDto());
			lineaReserva.setPrecioCosto(linea.getPrecioCosto());
			lineaReserva.setImporte(linea.getImporte());
			lineaReserva.setImporteTotal(linea.getImporteTotal());
			lineaReserva.setDescuento(linea.getDescuento());
			/* Para ponerlo en el Tag de la linea, indicará el orden en que se crearon */
			lineaReserva.setIdLinea(linea.getLinea());

			lineaReserva.setNombreUsuario(linea.getUsuario());
			lineaReserva.setUidTicket(linea.getUidTicket());

			/* Necesitamos sacar el estado de ApartadosManager */
			ApartadosDetalleBean lineaApartado = apartadoManager == null ? reserva.getTicketApartado().getArticulos().get(contadorLineas) 
					: apartadoManager.getTicketApartado().getArticulos().get(contadorLineas) ;
			contadorLineas++;
			lineaReserva.setEstadoLineaApartado(lineaApartado.getEstadoLineaApartado());

			log.debug("crearLineasTicketReserva() - Linea " + lineaReserva.getIdLinea() + " creada, del artículo " + lineaReserva.getCodigoArticulo() + ", con estado "
			        + lineaReserva.getEstadoLineaApartado());

			/* Según el estado de la linea del apartado, se rellenará de cierta manera estos campos */
			if(lineaReserva.getEstadoLineaApartado().equals(ApartadosCabeceraBean.ESTADO_CANCELADO)){
				lineaReserva.setCantidadEntregada(0);
				lineaReserva.setCantidadCancelada(1);
			}
			else if(lineaReserva.getEstadoLineaApartado().equals(ApartadosCabeceraBean.ESTADO_FINALIZADO)){
				lineaReserva.setCantidadEntregada(1);
				lineaReserva.setCantidadCancelada(0);
			}
			else{
				lineaReserva.setCantidadEntregada(0);
				lineaReserva.setCantidadCancelada(0);
			}

			lineasReserva.add(lineaReserva);
		}
		return lineasReserva;
	}
	
	/**
	 * Realiza la copia de los datos de los pagos de TicketManager para crear los datos
	 * de pagos de un TicketReserva.
	 * @param pagosRealizados : Pagos realizados en la pantalla de pagos de Reservas
	 * @return
	 * @throws TicketReservaException 
	 */
	@SuppressWarnings("static-access")
	public List<PagoReservaTicket> crearPagosTicketReserva(ApartadosManager reserva) throws TicketReservaException{
		List<VerPagosApartadoGui> listadoPagosRealizados = new ArrayList<VerPagosApartadoGui>();
		List<PagoReservaTicket> listadoPagos = new ArrayList<PagoReservaTicket>();
		List<ApartadosPagoBean> movimientosPagoReserva = null;
		try{
			if(reserva != null){
				movimientosPagoReserva = reserva.getTicketApartado().getMovimientos();
			}
			else{
				movimientosPagoReserva = apartadoManager.getTicketApartado().getMovimientos();
			}
			
			for(ApartadosPagoBean pago : movimientosPagoReserva){
				CajaMovimientoBean movimientoBean = cajasService.consultarMovimientoApartado(pago.getUidDiarioCaja(),
						pago.getLinea(), sesion.getAplicacion().getUidActividad());
				movimientoBean.setDesMedioPago(mediosPagosService.getMedioPago(movimientoBean.getCodMedioPago()).getDesMedioPago());
				listadoPagosRealizados.add(new ByLVerPagosApartadoGui(movimientoBean));
			}

			/* Recorremos los pagos del TicketManager y copiamos los datos en nuestro objeto */
			int contadorOrden = 1;
			for(int indice = 0; indice < listadoPagosRealizados.size(); indice++){
				PagoReservaTicket pagoReserva = new PagoReservaTicket();
				pagoReserva.setOrden(contadorOrden);
				pagoReserva.setLinea(String.valueOf(listadoPagosRealizados.get(indice).getMovimiento().getLinea()));
				pagoReserva.setUsuario(listadoPagosRealizados.get(indice).getMovimiento().getUsuario());
				pagoReserva.setUidDiarioCaja(listadoPagosRealizados.get(indice).getMovimiento().getUidDiarioCaja());

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
				pagoReserva.setFechaActualizacion(listadoPagosRealizados.get(indice).getMovimiento().getFecha()	!= null 
						? simpleDateFormat.format(listadoPagosRealizados.get(indice).getMovimiento().getFecha()) : null);

				pagoReserva.setCodigoMedioPago(listadoPagosRealizados.get(indice).getMovimiento().getCodMedioPago());
				pagoReserva.setDescripcionMedioPago(listadoPagosRealizados.get(indice).getMovimiento().getDesMedioPago());
				pagoReserva.setImporte(listadoPagosRealizados.get(indice).getImporte());

				/* Comprobamos que el pago se realizo con una Tarjeta para buscar y seguidamente añadir los datos del
				 * pago realizado con la tarjeta */
				DatosRespuestaTarjetaReservaTicket datosPagoTarjeta = null;
				for(MedioPagoBean medioPago : mediosPagosService.mediosPagoTarjetas){
					if(pagoReserva.getCodigoMedioPago().equals(medioPago.getCodMedioPago())){
						byte[] xmlPagoTarjeta = movimientoTarjetaService.consultarTicket(listadoPagosRealizados.get(indice).getMovimiento());
						if(xmlPagoTarjeta != null && xmlPagoTarjeta.length > 0){
							datosPagoTarjeta = (DatosRespuestaTarjetaReservaTicket) MarshallUtil.leerXML(xmlPagoTarjeta, DatosRespuestaTarjetaReservaTicket.class);
						}
						break;
					}
				}
				/* En caso de no ser un pago con tarjeta, ponemos los datos vacíos */
				if (datosPagoTarjeta != null) {
					pagoReserva.setDatosRespuestaTarjeta(datosPagoTarjeta);
				}
				else {
					pagoReserva.setDatosRespuestaTarjeta(null);
				}

				contadorOrden++;

				listadoPagos.add(pagoReserva);
				log.debug("crearPagosTicketReserva() - Pago realizado con " + pagoReserva.getDescripcionMedioPago() + " con importe " + pagoReserva.getImporte() + " creado");

				/* Buscar medio de pago de giftCard para comparar */
				MedioPagoBean medioPagoTarjetaRegalo = null;
				for(MedioPagoBean medioPago : mediosPagosService.mediosPagoContado){
					if(medioPago.getDesMedioPago().equals(listadoPagosRealizados.get(indice).getMedioPagoProperty().getValue())){
						medioPagoTarjetaRegalo = medioPago;
					}
				}
				if(medioPagoTarjetaRegalo != null){
					try{
						/* Consultamos primero si existe un pago previo de GiftCard */
						ReservasPagoGiftCardBean pagoGift = null;
						if(apartadoManager != null){
							pagoGift = pagoGiftCardService.consultar(apartadoManager.getTicketApartado().getCabecera().getUidActividad(),
									apartadoManager.getTicketApartado().getCabecera().getIdApartado(), listadoPagosRealizados.get(indice).getMovimiento().getLinea());
						}
						else{
							pagoGift = pagoGiftCardService.consultar(reserva.getTicketApartado().getCabecera().getUidActividad(),
									reserva.getTicketApartado().getCabecera().getIdApartado(), listadoPagosRealizados.get(indice).getMovimiento().getLinea());
						}
						List<ReservasPagoGiftCardBean> listadoGiftCards = new ArrayList<ReservasPagoGiftCardBean>();
						listadoGiftCards.add(pagoGift);
						pagoReserva.setGiftcards(listadoGiftCards);
					}
					catch (ReservasPagoGiftCardNotFoundException e) {
						if (getDatos().containsKey(NuevoPagoApartadoController.GIFCARD_PAGO_RESERVA)) {
							List<ReservasPagoGiftCardBean> listadoGiftCards = new ArrayList<ReservasPagoGiftCardBean>();

							ReservasPagoGiftCardBean tarjetaUsada = (ReservasPagoGiftCardBean) getDatos().get(NuevoPagoApartadoController.GIFCARD_PAGO_RESERVA);
							getDatos().remove(NuevoPagoApartadoController.GIFCARD_PAGO_RESERVA);

							tarjetaUsada.setUidActividad(apartadoManager == null ? reserva.getTicketApartado().getCabecera().getUidActividad() : apartadoManager.getTicketApartado().getCabecera()
							        .getUidActividad());
							tarjetaUsada.setIdClieAlbaran(apartadoManager == null ? reserva.getTicketApartado().getCabecera().getIdApartado() : apartadoManager.getTicketApartado().getCabecera()
							        .getIdApartado());
							tarjetaUsada.setLinea(listadoPagosRealizados.get(indice).getMovimiento().getLinea());
							pagoGiftCardService.crear(tarjetaUsada);

							listadoGiftCards.add(tarjetaUsada);
							pagoReserva.setGiftcards(listadoGiftCards);
						}
						else {
							pagoReserva.setGiftcards(null);
						}

					}
				}
			}
		}
		catch(Exception e){
			log.error("crearPagosTicketReserva() - " + e.getMessage());
			throw new TicketReservaException(e.getMessage(), e);
		}
		ordenarPagos(listadoPagos);
		return listadoPagos;
	}
	
	/**
	 * Guarda en BD el documento y el ticket de reserva.
	 * @param reserva : Objeto con los datos que completar en el Ticket.
	 * @throws TicketReservaException 
	 * @throws RutasServiciosException 
	 */
	public void salvarDocumentoReserva(TicketReserva reserva) throws TicketReservaException{
		SqlSession sqlSession = new SqlSession();
		try{
			sqlSession.openSession(SessionFactory.openSession());
			TicketBean ticketXML = new TicketBean();

			ticketXML.setUidActividad(sesion.getAplicacion().getUidActividad());
			ticketXML.setUidTicket(UUID.randomUUID().toString());
			ticketXML.setCodAlmacen(sesion.getAplicacion().getCodAlmacen());
			ticketXML.setIdTicket(reserva.getCabecera().getIdTicket());
			ticketXML.setFecha(new Date());
			/* Insertamos el objeto que vamos a transformar en XML como Ticket */
			ticketXML.setTicket(reserva.getXmlTicket());

			ticketXML.setCodcaja(sesion.getAplicacion().getCodCaja());
			ticketXML.setIdTipoDocumento(new Long(reserva.getCabecera().getTipoDocumento()));
			ticketXML.setCodTicket(reserva.getCabecera().getCodigoApartado());
			ticketXML.setSerieTicket(reserva.getCabecera().getSerieTicket());
			ticketXML.setFirma("*");

			ticketService.insertarTicket(sqlSession, ticketXML, false);
			sqlSession.commit();
		}
		catch(Exception e){
			sqlSession.rollback();
			String mensajeError = I18N.getTexto("Se ha producido un error al guardar el Ticket de Reserva");
			log.error("salvarDocumentoReserva() - " + mensajeError + " - " + e.getMessage(), e);
			throw new TicketReservaException(mensajeError, e);
		}
		finally{
			sqlSession.close();
		}
	}

	@FXML
	public void cargarFechaRecogida(){
		if(tbArticulos.getSelectionModel().getSelectedItem() != null){
			LineaArticuloApartadoGui linea = (LineaArticuloApartadoGui) tbArticulos.getSelectionModel().getSelectedItem();
			tfFechaRecogida.setSelectedDate(linea.getDetalle().getFechaPrevistaRecogida());
		}
	}
	
	@Override
	public LineaTicket insertarLineaVenta(String sCodart, String sDesglose1, String sDesglose2, BigDecimal nCantidad) throws LineaTicketException, PromocionesServiceException, DocumentoException {
		FidelizacionBean tarjeta = ticketManager.getTicket().getCabecera().getDatosFidelizado();
		if(ticketManager.getTicket().getLineas().isEmpty()) {
			ticketManager.nuevoTicket();
			ticketManager.getTicket().getCabecera().setDatosFidelizado(tarjeta);
		}
		
		LineaTicket linea = ticketManager.nuevaLineaArticulo(sCodart, sDesglose1, sDesglose2, frArticulo.getCantidadAsBigDecimal(), null);
		
		if (AppConfig.menu.equals(ByLDevolucionesController.MENU_POS_PROFESIONAL) && sesion.getAplicacion().getTienda().getCliente().getCodpais().equals(ByLPagosController.CODIGO_PAIS_USA)) {
			linea.setPrecioTotalSinDto(linea.getPrecioSinDto());
			linea.recalcularImporteFinal();
		}

		visor.escribir(linea.getDesArticulo(),FormatUtil.getInstance().formateaNumero(linea.getCantidad()) + " X " + FormatUtil.getInstance().formateaImporte(linea.getPrecioTotalConDto()));
		return linea;
	}
	
	/*
	 * Este método se encargará de ordenar la lista y setear el campo "orden" para asegurarnos de que están ordenados por fecha
	 */
	private void ordenarPagos(List<PagoReservaTicket> listaPagos) {
		
		Collections.sort(listaPagos, new Comparator<PagoReservaTicket>(){

			@Override
			public int compare(PagoReservaTicket o1, PagoReservaTicket o2) {
				// Se ordena for fecha de manera ascendente, para descendente cambiar el orden de los objetos a comparar
				return o1.getFechaActualizacion().compareTo(o2.getFechaActualizacion());
			}
		});
		
		int orden = 1;
		for (PagoReservaTicket pago : listaPagos) {
			pago.setOrden(orden);
			orden++;
		}
	}
	
	public ApartadosDetalleBean introducirDetalleApartado(LineaTicket lineaArticulo, boolean isNuevo){

		ApartadosDetalleBean detalle = new ApartadosDetalleBean();

		detalle.setCantidad(lineaArticulo.getCantidad());
		detalle.setCodart(lineaArticulo.getCodArticulo());
		detalle.setCodimp(lineaArticulo.getCodImpuesto());
		detalle.setDescuento(lineaArticulo.getDescuento());
		detalle.setDesart(lineaArticulo.getDesArticulo());
		detalle.setDesglose1(lineaArticulo.getDesglose1());
		detalle.setDesglose2(lineaArticulo.getDesglose2());
		detalle.setEstadoLineaApartado((short) 0);
		detalle.setImporteTotal(lineaArticulo.getImporteTotalConDto());
		detalle.setPrecio(lineaArticulo.getPrecioTotalSinDto());
		detalle.setLinea(lineaArticulo.getIdLinea());
		detalle.setUidApartado(apartadoManager.getTicketApartado().getCabecera().getUidApartado());
		detalle.setLinea(lineaArticulo.getIdLinea());
		detalle.setUidActividad(sesion.getAplicacion().getUidActividad());
		detalle.setFechaApartadoArticulo(new Date());
		detalle.setEstadoLineaApartado(ApartadosCabeceraBean.ESTADO_DISPONIBLE);
		detalle.setPrecioTotalSinDto(lineaArticulo.getPrecioTotalSinDto());
		detalle.setImporte(lineaArticulo.getImporteTotalSinDto());
		detalle.setPrecioCosto(lineaArticulo.getPrecioSinDto());
		detalle.setArticuloNuevo(isNuevo);
		detalle.setUsuario(sesion.getSesionUsuario().getUsuario().getUsuario());
		
		apartadoManager.getTicketApartado().getArticulos().add(detalle);
						
		return detalle;
	}

	@Override
	public void abrirBusquedaArticulos() {
		log.trace("abrirBusquedaArticulos()");
		
		if(!modoEdicion){
			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Para insertar artículos debe estar en modo edición."), this.getStage());
			return;
		}

		getDatos().put(BuscarArticulosController.PARAMETRO_ENTRADA_CLIENTE, apartadoManager.getCliente());
		getDatos().put(BuscarArticulosController.PARAMETRO_ENTRADA_CODTARIFA, apartadoManager.getTarifaDefault());
		getDatos().put(BuscarArticulosController.PARAM_MODAL, Boolean.TRUE);
		getDatos().put(SeleccionUsuarioController.PARAMETRO_ES_STOCK, Boolean.FALSE);
		
		if (AppConfig.menu.equals(ByLDevolucionesController.MENU_POS_PROFESIONAL)) {
			getApplication().getMainView().showModal(BuscarArticulosProfesionalView.class, getDatos());
		}
		else {
			getApplication().getMainView().showModal(BuscarArticulosView.class, getDatos());
		}
		initializeFocus();
		
		if (datos.containsKey(BuscarArticulosController.PARAMETRO_SALIDA_CODART)) {
            log.debug("abrirBusquedaArticulos() - ACCION BUSQUEDA ARTICULOS - Error registrando línea de ticket");
            String codArt = (String) getDatos().get(BuscarArticulosController.PARAMETRO_SALIDA_CODART);
            String desglose1 = (String) getDatos().get(BuscarArticulosController.PARAMETRO_SALIDA_DESGLOSE1);
            String desglose2 = (String) getDatos().get(BuscarArticulosController.PARAMETRO_SALIDA_DESGLOSE2);

            frArticulo.setCantidad("1");
            try {
            	if (accionValidarFormulario()) {
            		if(ticketManager.comprobarTarjetaRegalo(codArt)){
            			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("No están permitidas recargas de tarjeta regalo en apartados."), this.getStage());
            		}
            		else{
            			LineaTicket lineaNueva = insertarLineaVenta(codArt, desglose1, desglose2, frArticulo.getCantidadAsBigDecimal());
            			
            			comprobarArticuloGenerico(lineaNueva);
            			
            			if(lineaNueva.getGenerico()){
                    		HashMap<String, Object> parametrosEdicionArticulo = new HashMap<>();
							parametrosEdicionArticulo.put(EdicionArticuloController.CLAVE_PARAMETRO_ARTICULO, lineaNueva);
							parametrosEdicionArticulo.put(FacturacionArticulosController.TICKET_KEY, ticketManager);
							getApplication().getMainView().showModalCentered(EdicionArticuloView.class, parametrosEdicionArticulo, this.getStage());
							
							if(parametrosEdicionArticulo.containsKey(EdicionArticuloController.CLAVE_CANCELADO)){
								throw new LineaInsertadaNoPermitidaException(lineaNueva);
							}
            			}
            			
            			comprobarLineaPrecioCero(lineaNueva);
            			
            			FidelizacionBean fidelizado = new FidelizacionBean();
    					if(!(Dispositivos.getInstance().getFidelizacion() instanceof FidelizacionNoConfig)) {
    						if(apartado.getTarjetaFidelizacion() != null){
	    						fidelizado = Dispositivos.getInstance().getFidelizacion().consultarTarjetaFidelizado(apartado.getTarjetaFidelizacion(), sesion.getAplicacion().getUidActividad());
	    						ticketManager.getTicket().getCabecera().setDatosFidelizado(fidelizado);
    						}
    					}
            			
            			ticketManager.recalcularConPromociones();
            			apartadoManager.introducirDetalleApartado(lineaNueva);
    					
            			nuevosArticulos++;
            			refrescarDatosPantalla();
            		}            		
            	}
            }
            catch (LineaTicketException ex) {
                log.error("abrirBusquedaArticulos() - ACCION BUSQUEDA ARTICULOS - Error registrando línea de ticket");
                VentanaDialogoComponent.crearVentanaInfo(ex.getLocalizedMessage(), this.getScene().getWindow());
            } catch (LineaInsertadaNoPermitidaException e) {
				ticketManager.getTicket().getLineas().remove(e.getLinea());
				if(e.getMessage() != null){
					VentanaDialogoComponent.crearVentanaError(e.getMessage(), getStage());
				}
            } catch (TarjetaRegaloException e) {
            	log.error(e);
            	VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("No están permitidas recargas de tarjeta regalo en apartados."), this.getStage());
            	//VentanaDialogoComponent.crearVentanaInfo(e.getMessageI18N(), getApplication().getStage());
            } catch (Exception e) {
            	log.error("abrirBusquedaArticulos() - " + e.getClass().getName() + " - " + e.getLocalizedMessage(), e);
            	VentanaDialogoComponent.crearVentanaError(getStage(), I18N.getTexto("No se ha podido insertar la línea"), e);
			}
        }
		initializeFocus();
	}
	
	
	private Boolean compruebaPagoTarjetaAdyen(String codMedioPago) {
		Boolean esTarjetaAdyen = false;

		if (Dispositivos.getInstance().getTarjeta() instanceof TefAdyenCloud) {
			if (Dispositivos.getInstance().getTarjeta() != null && Dispositivos.getInstance().getTarjeta().getConfiguracion() != null) {
				Map<String, String> mapaConfiguracion = Dispositivos.getInstance().getTarjeta().getConfiguracion().getParametrosConfiguracion();
				String medPagoConfigurado = ByLPagosController.COD_MP_PINPAD;
				if (mapaConfiguracion.get("PAYMENTS") != null) {
					medPagoConfigurado = mapaConfiguracion.get("PAYMENTS");
				}

				if (codMedioPago.equals(medPagoConfigurado) || codMedioPago.equals(ByLPagosController.COD_MP_PINPAD_ADYEN_STANDALONE)) {
					esTarjetaAdyen = true;
				}
			}
		}
		return esTarjetaAdyen;
	}
}
