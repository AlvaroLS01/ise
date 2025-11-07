package com.comerzzia.bimbaylola.pos.gui.ventas.profesional.devoluciones;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.gui.pagos.ByLPagosController;
import com.comerzzia.bimbaylola.pos.gui.pagos.profesional.ByLPagosProfesionalView;
import com.comerzzia.bimbaylola.pos.gui.ventas.articulos.devoluciones.ByLIntroduccionArticulosController;
import com.comerzzia.bimbaylola.pos.gui.ventas.devoluciones.ByLDevolucionesController;
import com.comerzzia.bimbaylola.pos.gui.ventas.devoluciones.fechaorigen.RequestFechaOrigenController;
import com.comerzzia.bimbaylola.pos.gui.ventas.profesional.venta.ByLVentaProfesionalController;
import com.comerzzia.bimbaylola.pos.gui.ventas.tickets.ByLVentaProfesionalManager;
import com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.ByLFacturacionArticulosController;
import com.comerzzia.bimbaylola.pos.services.ticket.profesional.ByLLineaTicketProfesional;
import com.comerzzia.pos.core.dispositivos.Dispositivos;
import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.tablas.celdas.CellFactoryBuilder;
import com.comerzzia.pos.gui.ventas.profesional.venta.LineaTicketProfesionalGui;
import com.comerzzia.pos.gui.ventas.tickets.articulos.LineaTicketGui;
import com.comerzzia.pos.gui.ventas.tickets.pagos.PagosController;
import com.comerzzia.pos.persistence.articulos.ArticuloBean;
import com.comerzzia.pos.persistence.core.documentos.propiedades.PropiedadDocumentoBean;
import com.comerzzia.pos.persistence.core.documentos.tipos.TipoDocumentoBean;
import com.comerzzia.pos.persistence.giftcard.GiftCardBean;
import com.comerzzia.pos.services.cajas.CajaEstadoException;
import com.comerzzia.pos.services.cajas.CajasServiceException;
import com.comerzzia.pos.services.core.documentos.DocumentoException;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.services.core.sesion.SesionInitException;
import com.comerzzia.pos.services.core.variables.VariablesServices;
import com.comerzzia.pos.services.promociones.PromocionesServiceException;
import com.comerzzia.pos.services.ticket.TicketVenta;
import com.comerzzia.pos.services.ticket.TicketVentaAbono;
import com.comerzzia.pos.services.ticket.lineas.LineaTicket;
import com.comerzzia.pos.services.ticket.lineas.LineaTicketAbstract;
import com.comerzzia.pos.services.ticket.profesional.TotalesTicketProfesional;
import com.comerzzia.pos.util.config.AppConfig;
import com.comerzzia.pos.util.config.SpringContext;
import com.comerzzia.pos.util.config.Variables;
import com.comerzzia.pos.util.format.FormatUtil;
import com.comerzzia.pos.util.i18n.I18N;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

@Component
public class ByLIntroduccionArticulosProfesionalController extends ByLIntroduccionArticulosController {
	
	private static final Logger log = Logger.getLogger(ByLIntroduccionArticulosProfesionalController.class.getName());
	
	@Autowired
	private Sesion sesion;
	@Autowired
	private VariablesServices variablesServices;
	
	@FXML
	protected Label lbBase, lbIva, lbRecargo, lbTituloIva, lbTituloRecargo;
	@FXML
	protected TableColumn<LineaTicketProfesionalGui, BigDecimal> tcLineasPrecio;
	@FXML
	protected AnchorPane anPaIva,anPaRecargo;
	
	public static final String COD_USA="US";
	
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		log.debug("initialize() - Inicializando pantalla...");

        // Mensaje sin contenido para tabla. los establecemos a vacio
        //tbLineas.setPlaceholder(new Label(""));

        lineas = FXCollections.observableList(new ArrayList<LineaTicketGui>());

        // CENTRADO CON ESTILO A LA DERECHA
        tcLineasArticulo.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbLineas", "tcLineasArticulo", null, CellFactoryBuilder.ESTILO_ALINEACION_DCHA));
        tcLineasDescripcion.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbLineas", "tcLineasDescripcion", null, CellFactoryBuilder.ESTILO_ALINEACION_DCHA));
        tcLineasDesglose1.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbLineas", "tcLineasDesglose1", null, CellFactoryBuilder.ESTILO_ALINEACION_DCHA));
        tcLineasDesglose2.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbLineas", "tcLineasDesglose2", null, CellFactoryBuilder.ESTILO_ALINEACION_DCHA));
        tcLineasImporte.setCellFactory(CellFactoryBuilder.createCellRendererCeldaImporte("tbLineas", "tcLineasImporte", CellFactoryBuilder.ESTILO_ALINEACION_DCHA));
        tcLineasCantidad.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbLineas", "tcLineasCantidad", 0, CellFactoryBuilder.ESTILO_ALINEACION_DCHA));
		tcVendedor.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbLineas", "tcVendedor", 2, CellFactoryBuilder.ESTILO_ALINEACION_DCHA));
		tcLineasPrecio.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbLineas", "tcLineasPrecio", 2, CellFactoryBuilder.ESTILO_ALINEACION_DCHA));

		Boolean vendedorVisible = variablesServices.getVariableAsBoolean(VariablesServices.TPV_COLUMNA_VENDEDOR_VISIBLE, false);
		if (!vendedorVisible) {
			tcVendedor.setVisible(false);
		}
		
        // Asignamos las lineas a la tabla
        tbLineas.setItems(lineas);

        // Definimos un factory para cada celda para aumentar el rendimiento
        tcLineasArticulo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaTicketGui, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<LineaTicketGui, String> cdf) {
                return cdf.getValue().getArtProperty();
            }
        });
        tcLineasDescripcion.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaTicketGui, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<LineaTicketGui, String> cdf) {
                return cdf.getValue().getDescripcionProperty();
            }
        });
        tcLineasCantidad.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaTicketGui, BigDecimal>, ObservableValue<BigDecimal>>() {
            @Override
            public ObservableValue<BigDecimal> call(TableColumn.CellDataFeatures<LineaTicketGui, BigDecimal> cdf) {
                return cdf.getValue().getCantidadProperty();
            }
        });
        tcLineasDesglose1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaTicketGui, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<LineaTicketGui, String> cdf) {
                return cdf.getValue().getDesglose1Property();
            }
        });
        tcLineasDesglose2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaTicketGui, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<LineaTicketGui, String> cdf) {
                return cdf.getValue().getDesglose2Property();
            }
        });
        tcLineasPrecio.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaTicketProfesionalGui, BigDecimal>, ObservableValue<BigDecimal>>(){
        	
        	@Override
        	public ObservableValue<BigDecimal> call(CellDataFeatures<LineaTicketProfesionalGui, BigDecimal> cdf) {
        		return cdf.getValue().getPrecioProperty();
        	}
        });
        tcLineasImporte.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaTicketGui, BigDecimal>, ObservableValue<BigDecimal>>() {
            @Override
            public ObservableValue<BigDecimal> call(TableColumn.CellDataFeatures<LineaTicketGui, BigDecimal> cdf) {
                return cdf.getValue().getImporteProperty();
            }
        });
        tcVendedor.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaTicketGui, String>, ObservableValue<String>>(){

			@Override
			public ObservableValue<String> call(CellDataFeatures<LineaTicketGui, String> cdf) {
				return cdf.getValue().getVendedorProperty();
			}
		});
        
        //Ocultamos el Pad numérico si es necesario
        log.debug("initialize() - Comprobando configuración para panel numérico");
        if (!Variables.MODO_PAD_NUMERICO) {
            log.debug("initialize() - PAD Numerico off");
            panelNumberPad.setMaxWidth(0);
            panelNumberPad.setMinWidth(0);
            panelNumberPad.setPrefWidth(0);
            panelNumberPad.getChildren().clear();
		}
        
        String codPais = sesion.getAplicacion().getTienda().getCliente().getCodpais();
		/* Para el menu profesional o para Usa se ocultaran campo iva y recargo */
		if (AppConfig.menu.equals(ByLDevolucionesController.MENU_POS_PROFESIONAL) && codPais.equals(COD_USA) ) {
			log.debug("initialize() - Ocultando labels de iva y recargo ");
			lbIva.setVisible(false);
			lbRecargo.setVisible(false);
			lbTituloIva.setVisible(false);
			lbTituloRecargo.setVisible(false);
			anPaIva.setVisible(false);
			anPaRecargo.setVisible(false);
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

			if (cajaAbierta) {
				if(!esDevolucionSinTicket){
					HashMap<String, Object> datos = getView().getParentView().getController().getDatos();
					
					if (datos.containsKey(TICKET_KEY)) {
						ticketManager = (ByLVentaProfesionalManager) datos.get(TICKET_KEY);
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
						
						if(tipoDocumentoInicial == null) {
							tipoDocumentoInicial = documentos.getDocumento(((TicketVentaAbono) ticketManager.getTicket()).getCabecera().getTipoDocumento());
						}
					}
					
					if (ticketManager == null) {
						log.error("initializeForm() -----No se ha inicializado el ticket manager-----");
						throw new InitializeGuiException();
					}
	
						// Comprobamos si la operación es una devolución de tarjeta regalo
						if (ticketManager.esDevolucionRecargaTarjetaRegalo()) {
							GiftCardBean tarjeta = Dispositivos.getInstance().getGiftCard()
							        .consultarTarjetaRegalo(ticketManager.getTicketOrigen().getCabecera().getTarjetaRegalo().getNumTarjetaRegalo(), sesion.getAplicacion().getUidActividad());
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
					
				}else{
					if(ticketManager.getTicket() == null || ticketManager.getTicketOrigen() != null){
						ticketManager.eliminarTicketCompleto();
						ticketManager.nuevoTicket();
					}
					
					ticketManager.setEsDevolucion(true);
					((ByLVentaProfesionalManager) ticketManager).setEsTicketRegalo(Boolean.FALSE);

					TipoDocumentoBean documentoAbono = documentos.getDocumentoAbono(ticketManager.getDocumentoActivo().getCodtipodocumento());
					
//					ticketManager.setDocumentoActivo(sesion.getAplicacion().getDocumentos().getDocumento("SR"));
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
			throw new InitializeGuiException(e);
		}
	}
	
	@Override
	public void initializeComponents() throws InitializeGuiException {
		super.initializeComponents();
		try {
			ticketManager.init();
			ticketManager = SpringContext.getBean(ByLVentaProfesionalManager.class);
		}
		catch (SesionInitException e) {
			log.error("inicializarComponentes() - Error inicializando pantalla de venta de artículos");
			VentanaDialogoComponent.crearVentanaError("Error cargando pantalla. Para mas información consulte el log.", getStage());
		}
		
		if(sesion.getAplicacion().getTienda().getCliente().getCodpais().equals(ByLVentaProfesionalController.COD_PUERTO_RICO)) {
			lbTituloIva.setText(ByLPagosController.NOMBRE_IMPUESTO_PR_ST);
			lbTituloRecargo.setText(ByLPagosController.NOMBRE_IMPUESTO_PR_MU);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void refrescarDatosPantalla() {

		log.debug("refrescarDatosPantalla() - Refrescando datos de pantalla...");

		tfCantidadIntro.setText(FormatUtil.getInstance().formateaNumero(BigDecimal.ONE, 0));
		tfCodigoIntro.setText("");

		lbCodCliente.setText(((TicketVenta<?, ?, ?>) ticketManager.getTicket()).getCliente().getCodCliente());
		lbDesCliente.setText(((TicketVenta<?, ?, ?>) ticketManager.getTicket()).getCliente().getDesCliente());
		lbTotal.setText(FormatUtil.getInstance().formateaImporte(ticketManager.getTicket().getTotales().getTotal()));

		LineaTicketGui selectedItem = getLineaSeleccionada();
		lineas.clear();

		for (LineaTicketAbstract lineaTicket : (List<ByLLineaTicketProfesional>) (ticketManager.getTicket()).getLineas()) {
			lineas.add(new LineaTicketProfesionalGui(lineaTicket));
		}

		Collections.reverse(lineas);

		if (selectedItem != null) {
			tbLineas.getSelectionModel().select(lineas.indexOf(searchIdLinea(selectedItem.getIdLinea())));
		}
		else {
			tbLineas.getSelectionModel().selectFirst();
		}

		if (getLineaSeleccionada() == null) {
			tfCodigoIntro.requestFocus();
		}

		tbLineas.scrollTo(0);

		if (!esDevolucionSinTicket) {
			actualizarVisibilidadBtnEditar();
		}
		
		lbBase.setText(FormatUtil.getInstance().formateaImporte(ticketManager.getTicket().getTotales().getBase()));
		lbIva.setText(FormatUtil.getInstance().formateaImporte(((TotalesTicketProfesional) ticketManager.getTicket().getTotales()).getIvaTotal()));
		lbRecargo.setText(FormatUtil.getInstance().formateaImporte(((TotalesTicketProfesional) ticketManager.getTicket().getTotales()).getRecargoTotal()));
		
		obtenerCantidadTotal();
	}
	
	@SuppressWarnings("unchecked")
    @Override
    protected boolean validarTicket() {
		boolean valido = true;
		boolean lineasNegativasOrigen = false;

		for (LineaTicket linea : (List<ByLLineaTicketProfesional>) ticketManager.getTicket().getLineas()) {
			ArticuloBean articulo = linea.getArticulo();
			
			//Se comprueba si alguna de als lineas del ticket pertenece al ticket origen
			if(linea.getLineaDocumentoOrigen()!=null){
				lineasNegativasOrigen = true;
			}

			// Si no es válido no hacemos las comprobaciones para los números de serie
			// Si quedan números de serie por asignar, mostramos la pantalla de números de serie
			if (quedanNumerosSeriePorAsignar(linea, articulo.getNumerosSerie())) {
				VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Quedan números de serie por asignar. Por favor, asígnelos antes de seguir."), getStage());

				asignarNumerosSerie(linea);

				/*
				 * if (quedanNumerosSeriePorAsignar(linea,articulo.getNumerosSerie())) { return false; }
				 */
				valido = false;
			}
		}
		if(!lineasNegativasOrigen && !esDevolucionSinTicket){			
			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Debe añadir alguna linea del documento origen para realizar la devolución."), getStage());
		}
		
		if(esDevolucionSinTicket){
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

				getApplication().getMainView().showModal(ByLPagosProfesionalView.class, getDatos());
				try {
					getView().resetSubViews();
					if (getDatos().containsKey(PagosController.ACCION_CANCELAR)) {
						initializeForm();
					}
					else {
						try {
							ticketManager.nuevoTicket();
							ticketManager.setEsDevolucion(true);
							if(!esDevolucionSinTicket){
								ticketManager.setDocumentoActivo(documentos.getDocumentoAbono(ticketManager.getTicketOrigen().getCabecera().getCodTipoDocumento()));
							}
						}
						catch (Exception e) {
							log.error("abrirPagos() - Ha habido un error al inicializar un nuevo ticket para borrar la copia de seguridad: " + e.getMessage(), e);
						}
						getView().getParentView().loadAndInitialize();
					}
				}
				catch (InitializeGuiException e) {
					VentanaDialogoComponent.crearVentanaError(getStage(), e);
				}
			}
		}
		else {
			log.warn("abrirPagos() - Ticket vacio");
			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("El ticket no contiene líneas de artículo."), this.getStage());
		}
	}
	
}
