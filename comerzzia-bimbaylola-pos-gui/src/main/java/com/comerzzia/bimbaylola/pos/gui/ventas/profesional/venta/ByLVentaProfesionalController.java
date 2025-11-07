package com.comerzzia.bimbaylola.pos.gui.ventas.profesional.venta;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.comerzzia.ByL.backoffice.persistencia.fidelizacion.fidelizados.FidelizadosBean;
import com.comerzzia.ByL.backoffice.persistencia.fidelizacion.firma.FidelizadoFirmaBean;
import com.comerzzia.ByL.backoffice.rest.client.fidelizados.ByLFidelizadoRequestRest;
import com.comerzzia.ByL.backoffice.rest.client.fidelizados.ByLFidelizadosRest;
import com.comerzzia.ByL.backoffice.rest.client.firma.FidelizadoFirmaRequestRest;
import com.comerzzia.ByL.backoffice.rest.client.firma.FidelizadoFirmaRest;
import com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.axis.AxisManager;
import com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.axis.bean.AxisConsentimientoBean;
import com.comerzzia.bimbaylola.pos.gui.ByLBackgroundTask;
import com.comerzzia.bimbaylola.pos.gui.componentes.ventanaCarga.ByLVentanaEspera;
import com.comerzzia.bimbaylola.pos.gui.mantenimientos.fidelizados.datosgenerales.ByLPaneDatosGeneralesController;
import com.comerzzia.bimbaylola.pos.gui.pagos.ByLPagosController;
import com.comerzzia.bimbaylola.pos.gui.pagos.profesional.ByLPagosProfesionalView;
import com.comerzzia.bimbaylola.pos.gui.ventas.devoluciones.ByLDevolucionesController;
import com.comerzzia.bimbaylola.pos.gui.ventas.tickets.ByLVentaProfesionalManager;
import com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.ByLFacturacionArticulosController;
import com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.agregarNotaInformativa.ByLAgregarNotaInformativaController;
import com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.agregarNotaInformativa.ByLAgregarNotaInformativaView;
import com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.cambiarVendedor.ByLCambiarVendedorView;
import com.comerzzia.bimbaylola.pos.persistence.fidelizacion.ByLFidelizacionBean;
import com.comerzzia.bimbaylola.pos.persistence.ticket.articulos.agregarnotainformativa.AvisoInformativoBean;
import com.comerzzia.bimbaylola.pos.services.dispositivofirma.ByLDispositivosFirma;
import com.comerzzia.bimbaylola.pos.services.dispositivofirma.IFirma;
import com.comerzzia.bimbaylola.pos.services.ticket.ByLLineaTicket;
import com.comerzzia.bimbaylola.pos.services.ticket.ByLTicketVentaAbono;
import com.comerzzia.bimbaylola.pos.services.ticket.aparcados.ByLTicketsAparcadosService;
import com.comerzzia.bimbaylola.pos.services.ticket.profesional.ByLLineaTicketProfesional;
import com.comerzzia.pos.core.dispositivos.Dispositivos;
import com.comerzzia.pos.core.dispositivos.dispositivo.DispositivoCallback;
import com.comerzzia.pos.core.dispositivos.dispositivo.balanza.BalanzaNoConfig;
import com.comerzzia.pos.core.gui.BackgroundTask;
import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.componentes.botonaccion.BotonBotoneraComponent;
import com.comerzzia.pos.core.gui.componentes.botonera.ConfiguracionBotonBean;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.permisos.exception.SinPermisosException;
import com.comerzzia.pos.core.gui.tablas.celdas.CellFactoryBuilder;
import com.comerzzia.pos.dispositivo.comun.tarjeta.CodigoTarjetaController;
import com.comerzzia.pos.dispositivo.comun.tarjeta.saldo.SaldoTarjetaRegaloController;
import com.comerzzia.pos.dispositivo.comun.tarjeta.saldo.SaldoTarjetaRegaloView;
import com.comerzzia.pos.gui.ventas.gestiontickets.ticketRegalo.TicketRegaloController;
import com.comerzzia.pos.gui.ventas.gestiontickets.ticketRegalo.TicketRegaloView;
import com.comerzzia.pos.gui.ventas.profesional.venta.LineaTicketProfesionalGui;
import com.comerzzia.pos.gui.ventas.profesional.venta.VentaProfesionalController;
import com.comerzzia.pos.gui.ventas.tickets.articulos.FacturacionArticulosController;
import com.comerzzia.pos.gui.ventas.tickets.articulos.LineaTicketGui;
import com.comerzzia.pos.gui.ventas.tickets.articulos.edicion.EdicionArticuloController;
import com.comerzzia.pos.persistence.clientes.ClienteBean;
import com.comerzzia.pos.persistence.core.documentos.propiedades.PropiedadDocumentoBean;
import com.comerzzia.pos.persistence.core.documentos.tipos.TipoDocumentoBean;
import com.comerzzia.pos.persistence.fidelizacion.FidelizacionBean;
import com.comerzzia.pos.persistence.giftcard.GiftCardBean;
import com.comerzzia.pos.persistence.tickets.TicketBean;
import com.comerzzia.pos.services.cajas.CajaEstadoException;
import com.comerzzia.pos.services.cajas.CajasServiceException;
import com.comerzzia.pos.services.core.documentos.DocumentoException;
import com.comerzzia.pos.services.core.documentos.Documentos;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.services.core.sesion.SesionImpuestos;
import com.comerzzia.pos.services.core.sesion.SesionInitException;
import com.comerzzia.pos.services.core.variables.VariablesServices;
import com.comerzzia.pos.services.promociones.PromocionesServiceException;
import com.comerzzia.pos.services.ticket.ITicket;
import com.comerzzia.pos.services.ticket.TicketVenta;
import com.comerzzia.pos.services.ticket.TicketVentaAbono;
import com.comerzzia.pos.services.ticket.TicketsService;
import com.comerzzia.pos.services.ticket.TicketsServiceException;
import com.comerzzia.pos.services.ticket.cabecera.CabeceraTicket;
import com.comerzzia.pos.services.ticket.cabecera.TotalesTicket;
import com.comerzzia.pos.services.ticket.copiaSeguridad.CopiaSeguridadTicketService;
import com.comerzzia.pos.services.ticket.cupones.CuponAplicadoTicket;
import com.comerzzia.pos.services.ticket.lineas.ILineaTicket;
import com.comerzzia.pos.services.ticket.lineas.LineaTicket;
import com.comerzzia.pos.services.ticket.profesional.TotalesTicketProfesional;
import com.comerzzia.pos.util.bigdecimal.BigDecimalUtil;
import com.comerzzia.pos.util.config.AppConfig;
import com.comerzzia.pos.util.config.SpringContext;
import com.comerzzia.pos.util.format.FormatUtil;
import com.comerzzia.pos.util.i18n.I18N;
import com.comerzzia.pos.util.xml.MarshallUtil;
import com.google.gson.Gson;
import com.ingenico.fr.jc3api.JC3ApiC3Rspn;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

@Primary
@Component
public class ByLVentaProfesionalController extends VentaProfesionalController{
	
	public static final String PERMISO_CAMBIAR_VENDEDOR_LINEA = "CAMBIAR VENDEDOR LINEA";
	public static final String PERMISO_AGREGAR_NOTA_INFORMATIVA = "AGREGAR NOTA INFORMATIVA";
	public static final String OCULTA_IMPUESTO_LINEA = "POS.OCULTA_IMPUESTO";
	public static final String COD_PUERTO_RICO = "PR";
	public static final String COD_USA = "US";
	
	@Autowired
	protected Sesion sesion;
	@Autowired
	protected Documentos documentos;
	@Autowired
	protected TicketsService ticketsService;
	@Autowired
	protected ByLTicketsAparcadosService ticketsAparcadosService;
	@Autowired
	private CopiaSeguridadTicketService copiaSeguridadTicketService;
	@Autowired
	protected VariablesServices variablesServices;
	String idVariableGestionCaja = "GESTION.CAJA";
	
	@FXML
	protected Label lbNombreFide, lbNombreFideDato, lbEmailCliente, lbEmailClienteDato, lbMovilCliente, lbMovilClienteDato, lbFirmaCliente, lbTituloIva, lbTituloRecargo;
	@FXML
	protected Button btFirmaClienteDato;
	
	protected String colorTrazabilidad;
	
	@Override
	public void initializeManager() throws SesionInitException {
		ticketManager = SpringContext.getBean(ByLVentaProfesionalManager.class);
		ticketManager.init();
	}
	
	@Override
	public void initializeFocus() {
		cliente = sesion.getAplicacion().getTienda().getCliente();
		super.initializeFocus();
	}
	
	@Override
	public void initializeForm() throws InitializeGuiException {
		log.debug("initializeForm() - Inicializando formulario...");
		lbTitulo.setText(I18N.getTexto("Venta Profesional"));
		try {

			/* Comprobamos que exista una balanza configurada antes de solicitar el peso */
			if (Dispositivos.getInstance().getBalanza() != null && Dispositivos.getInstance().getBalanza().getConfiguracion() != null) {
				if (Dispositivos.getInstance().getBalanza() instanceof BalanzaNoConfig) {
					log.debug("La balanza no está configurada");
				}
				else {
					if (scheduledThreadPoolExecutor == null && Dispositivos.getInstance().getBalanza().muestraPesoPantalla()) {
						lblPeso.setVisible(true);
						tfPesoIntro.setVisible(true);
						obtenerPeso();
					}
				}
			}

			/* Ocultamos los componentes relacionado con los Fidelizados */
			ocultarComponentesFidelizado(Boolean.FALSE);

			lbTitulo.setText(I18N.getTexto("Ventas Profesional"));

			/* Realizamos las comprobaciones de apertura automática de caja y de cierre de caja obligatorio. */
			comprobarAperturaPantalla();

			if (ticketManager.getTicket() != null) {
				if (ticketManager.getTicket().getLineas() != null && ticketManager.getTicket().getLineas().isEmpty()) {
					visor.escribirLineaArriba(I18N.getTexto("---NUEVO CLIENTE---"));
				}
				else {
					int ultimArticulo = ticketManager.getTicket().getLineas().size();
					LineaTicket linea = (LineaTicket) ticketManager.getTicket().getLineas().get(ultimArticulo - 1);
					escribirLineaEnVisor(linea);
				}
			}

			if (!ticketManager.isTicketAbierto()) {
				ticketManager.nuevoTicket();
				visor.escribirLineaArriba(I18N.getTexto("---NUEVO CLIENTE---"));
				visor.modoEspera();
			}
			else {
				if (sesion.getSesionCaja().getCajaAbierta() != null) {
					/* AÃ±adida comprobaciÃ³n para realizar un nuevo ticket o no. */
					if (variablesServices.getVariableAsString(idVariableGestionCaja).equals("N")) {
						if (!sesion.getSesionCaja().getCajaAbierta().getUidDiarioCaja().equals(ticketManager.getTicket().getCabecera().getUidDiarioCaja())) {
							ticketManager.nuevoTicket();
						}
					}
					else {
						if (!ticketManager.getTicket().getLineas().isEmpty()) {
							/*
							 * En caso de tener el ticket manager lineas y la variable de gestion caja tener una S,
							 * tendremos que ponerle al ticket manager el uidDiarioCaja de sesion. Esto evita que las
							 * lineas se pierdan.
							 */
							ticketManager.getTicket().getCabecera().setUidDiarioCaja(sesion.getSesionCaja().getCajaAbierta().getUidDiarioCaja());
						}
					}
				}
				visor.modoVenta(visorConverter.convert((TicketVentaAbono) ticketManager.getTicket()));
			}

			/*
			 * El septimo botÃ³n de la tabla es negar. Si el sistema tiene valores positivos o negativos para el
			 * documento, quitamos le botÃ³n y su acciÃ³n asociada
			 */
			botoneraAccionesTabla.setAccionVisible("ACCION_TABLA_NEGAR_REGISTRO", ticketManager.getDocumentoActivo().isSignoLibre());

			tfCodigoIntro.clear();
			if (ticketManager.getTicket().getLineas().isEmpty()) {
				ClienteBean cliente = ticketManager.getTicket().getCliente();
				FidelizacionBean tarjeta = ticketManager.getTicket().getCabecera().getDatosFidelizado();
				ticketManager.nuevoTicket();
				ticketManager.getTicket().setCliente(cliente);
				ticketManager.getTicket().getCabecera().setDatosFidelizado(tarjeta);
			}

			refrescarDatosPantalla();
			
			try {
				PropiedadDocumentoBean propiedadTrazabilidadColor;
				propiedadTrazabilidadColor = documentos.getDocumento(ticketManager.getTicket().getCabecera().getTipoDocumento()).getPropiedades().get(ByLFacturacionArticulosController.PROPIEDAD_ARTICULOS_TRAZABILIDAD_COLOR);
				if(propiedadTrazabilidadColor != null && StringUtils.isNotBlank(propiedadTrazabilidadColor.getValor())) {
					colorTrazabilidad = propiedadTrazabilidadColor.getValor();
				}
			}
			catch (DocumentoException e1) {
				log.error("initializeForm() - " + e1.getClass().getName() + " - " + e1.getLocalizedMessage(), e1);
			}
			
		}
		catch (CajaEstadoException | CajasServiceException | PromocionesServiceException | DocumentoException e) {
			log.error("initializeForm() - Error inicializando formulario :" + e.getMessageI18N(), e);
			throw new InitializeGuiException(e.getMessageI18N(), e);
		}
		catch (InitializeGuiException e) {
			throw e;
		}
		catch (Exception e) {
			log.error("initializeForm() - Error inesperado inicializando formulario. ", e);
			throw new InitializeGuiException(e);
		}

	}
	
	@SuppressWarnings("unchecked")
    @Override
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);
		
		tcPrecio.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbLineas", "tcPrecio", 2, CellFactoryBuilder.ESTILO_ALINEACION_DCHA));
		tcImporte.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbLineas", "tcImporte", 2, CellFactoryBuilder.ESTILO_ALINEACION_DCHA));
		tcLineasCantidad.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tvLineas", "tcLineasCantidad", 0, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));

		tcImporte.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaTicketProfesionalGui, BigDecimal>, ObservableValue<BigDecimal>>(){

			@Override
			public ObservableValue<BigDecimal> call(CellDataFeatures<LineaTicketProfesionalGui, BigDecimal> cdf) {
				return cdf.getValue().getImporteProperty();
			}
		});

		if(sesion.getAplicacion().getTienda().getCliente().getCodpais().equals(COD_PUERTO_RICO)) {
			lbTituloIva.setText(ByLPagosController.NOMBRE_IMPUESTO_PR_ST);
			lbTituloRecargo.setText(ByLPagosController.NOMBRE_IMPUESTO_PR_MU);
		}
		
		
		String codPais = sesion.getAplicacion().getTienda().getCliente().getCodpais();
		/* Para el menu profesional o para Usa se ocultaran campo iva y recargo */
		if (AppConfig.menu.equals(ByLDevolucionesController.MENU_POS_PROFESIONAL) && codPais.equals(COD_USA) ) {
			log.debug("initialize() - Ocultando labels de iva y recargo ");
			lbIva.setVisible(false);
			lbRecargo.setVisible(false);
			lbTituloIva.setVisible(false);
			lbTituloRecargo.setVisible(false);
		}
		
	}
	
	protected List<ConfiguracionBotonBean> cargarAccionesTabla() {

		List<ConfiguracionBotonBean> listaAcciones = new ArrayList<>();
		listaAcciones.add(new ConfiguracionBotonBean("iconos/navigate_up2.png", null, null, "ACCION_TABLA_PRIMER_REGISTRO", "REALIZAR_ACCION"));
		listaAcciones.add(new ConfiguracionBotonBean("iconos/navigate_up.png", null, null, "ACCION_TABLA_ANTERIOR_REGISTRO", "REALIZAR_ACCION"));
		listaAcciones.add(new ConfiguracionBotonBean("iconos/navigate_down.png", null, null, "ACCION_TABLA_SIGUIENTE_REGISTRO", "REALIZAR_ACCION"));
		listaAcciones.add(new ConfiguracionBotonBean("iconos/navigate_down2.png", null, null, "ACCION_TABLA_ULTIMO_REGISTRO", "REALIZAR_ACCION"));
		listaAcciones.add(new ConfiguracionBotonBean("iconos/row_delete.png", null, null, "ACCION_TABLA_BORRAR_REGISTRO", "REALIZAR_ACCION"));
		listaAcciones.add(new ConfiguracionBotonBean("iconos/row_edit.png", null, null, "ACCION_TABLA_EDITAR_REGISTRO", "REALIZAR_ACCION"));
		listaAcciones.add(new ConfiguracionBotonBean("iconos/row_neg.png", null, null, "ACCION_TABLA_NEGAR_REGISTRO", "REALIZAR_ACCION")); 
		listaAcciones.add(new ConfiguracionBotonBean("iconos/cambiar_cajero_linea.png", null, null, "ACCION_TABLA_CAMBIAR_VENDEDOR", "REALIZAR_ACCION"));
		listaAcciones.add(new ConfiguracionBotonBean("iconos/about_pressed.png", null, null, "ACCION_TABLA_AGREGAR_NOTA_INFORMATIVA", "REALIZAR_ACCION"));
		return listaAcciones;
	}

	@Override
	public void realizarAccion(BotonBotoneraComponent botonAccionado) {

		log.debug("realizarAccion() - Realizando la acción : " + botonAccionado.getClave() + " de tipo : " + botonAccionado.getTipo());
		switch (botonAccionado.getClave()) {
			/* BOTONERA TABLA MOVIMIENTOS */
			case "ACCION_TABLA_PRIMER_REGISTRO":
				accionTablaPrimerRegistro(tbLineas);
				break;
			case "ACCION_TABLA_ANTERIOR_REGISTRO":
				accionTablaIrAnteriorRegistro(tbLineas);
				break;
			case "ACCION_TABLA_SIGUIENTE_REGISTRO":
				accionTablaIrSiguienteRegistro(tbLineas);
				break;
			case "ACCION_TABLA_ULTIMO_REGISTRO":
				accionTablaUltimoRegistro(tbLineas);
				break;
			case "ACCION_TABLA_BORRAR_REGISTRO":
				accionTablaEliminarRegistro();
				break;
			case "ACCION_TABLA_EDITAR_REGISTRO":
				accionTablaEditarRegistro();
				break;
			case "ACCION_TABLA_NEGAR_REGISTRO":
				accionNegarRegistroTabla();
				break;
			case "ACCION_TABLA_CAMBIAR_VENDEDOR":
				accionTablaCambiarVendedor();
				break;
			case "ACCION_TABLA_AGREGAR_NOTA_INFORMATIVA":
				accionTablaAgregarNotaInformativa();

			default:
				log.error("No se ha especificado acción en pantalla para la operación :" + botonAccionado.getClave());
				break;
		}
	}

	@FXML
	protected void accionTablaCambiarVendedor() {
		try {
			log.debug("accionTablaEditarRegistro() - Acción ejecutada");
			super.compruebaPermisos(PERMISO_CAMBIAR_VENDEDOR_LINEA);
			if (tbLineas.getItems() != null && getLineaSeleccionada() != null) {
				LineaTicketGui selectedItem = getLineaSeleccionada();
				if (selectedItem.isCupon()) {
					VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("La línea seleccionada no se puede modificar."), this.getStage());
				}
				else {
					int linea = selectedItem.getIdLinea();
					if (linea > 0) {
						ILineaTicket lineaTicket = ticketManager.getTicket().getLinea(linea);
						if (lineaTicket != null) {
							if (lineaTicket.isEditable()) {
								/* Creamos la ventana de ediciÃ³n de artÃ­culos. */
								HashMap<String, Object> parametrosEdicionArticulo = new HashMap<>();
								parametrosEdicionArticulo.put(EdicionArticuloController.CLAVE_PARAMETRO_ARTICULO, lineaTicket);
								parametrosEdicionArticulo.put(FacturacionArticulosController.TICKET_KEY, ticketManager);
								abrirVentanaCambiarVendedor(parametrosEdicionArticulo);

								guardarCopiaSeguridad();
								escribirLineaEnVisor((LineaTicket) lineaTicket);
								refrescarDatosPantalla();
								visor.modoVenta(visorConverter.convert(((TicketVentaAbono) ticketManager.getTicket())));
							}
							else {
								VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("La línea seleccionada no se puede modificar."), this.getStage());
							}
						}
					}
				}
			}
		}
		catch (SinPermisosException ex) {
			log.debug("accionTablaEditarRegistro() - El usuario no tiene permisos para modificar línea");
			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("No tiene permisos para modificar una línea"), getStage());
		}
	}

	protected void abrirVentanaCambiarVendedor(HashMap<String, Object> parametrosEdicionArticulo) {
		getApplication().getMainView().showModalCentered(ByLCambiarVendedorView.class, parametrosEdicionArticulo, this.getStage());
	}

	@FXML
	public void accionTablaAgregarNotaInformativa() {
		try {
			log.debug("accionTablaAgregarNotaInformativa() - Acción ejecutada");
			super.compruebaPermisos(PERMISO_AGREGAR_NOTA_INFORMATIVA);
			if (tbLineas.getItems() != null && getLineaSeleccionada() != null) {
				LineaTicketGui selectedItem = getLineaSeleccionada();
				int linea = selectedItem.getIdLinea();
				if (linea > 0) {
					ILineaTicket lineaTicket = ticketManager.getTicket().getLinea(linea);
					if (lineaTicket != null) {
						if (lineaTicket.isEditable()) {
							/* Creamos la ventana de inserción de nota informativa */
							HashMap<String, Object> parametrosEdicionArticulo = new HashMap<>();
							parametrosEdicionArticulo.put(ByLAgregarNotaInformativaController.CLAVE_PARAMETRO_LINEA_TICKET, lineaTicket);
							abrirVentanaAgregarNotaInformativa(parametrosEdicionArticulo);
						}
						else {
							VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("La línea seleccionada no se puede modificar."), this.getStage());
						}
					}
				}

			}

		}
		catch (SinPermisosException ex) {
			log.debug("accionTablaAgregarNotaInformativa() - El usuario no tiene permisos para modificar línea");
			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("No tiene permisos para modificar una línea"), getStage());
		}
	}
	
	@SuppressWarnings("unused")
	protected void abrirVentanaAgregarNotaInformativa(HashMap<String, Object> parametrosEdicionArticulo) {
		getApplication().getMainView().showModalCentered(ByLAgregarNotaInformativaView.class, parametrosEdicionArticulo, this.getStage());
		AvisoInformativoBean avisoInformativo = (AvisoInformativoBean) parametrosEdicionArticulo.get(ByLAgregarNotaInformativaController.AVISO_INFORMATIVO_BEAN);
	}
	
	public void cancelarVenta() {
		log.debug("cancelarVenta()");
		try {
			boolean confirmacion = VentanaDialogoComponent.crearVentanaConfirmacion(I18N.getTexto("¿Está seguro de querer eliminar todas las líneas del ticket?"), getStage());
			if (!confirmacion) {
				return;
			}
			ticketManager.eliminarTicketCompleto();

			// Restauramos la cantidad en la pantalla
			tfCantidadIntro.setText(FormatUtil.getInstance().formateaNumero(BigDecimal.ONE, 0));

			refrescarDatosPantalla();
			initializeFocus();
			tbLineas.getSelectionModel().clearSelection();

			visor.escribirLineaArriba(I18N.getTexto("---NUEVO CLIENTE---"));
			visor.modoEspera();
		}
		catch (TicketsServiceException | PromocionesServiceException | DocumentoException ex) {
			log.error("accionAnularTicket() - Error inicializando nuevo ticket: " + ex.getMessage(), ex);
			VentanaDialogoComponent.crearVentanaError(getStage(), ex.getMessageI18N(), ex);
		}
	}
	
	/**
	 * Realiza la petición de la firma del Fidelizado al pulsar el botón de Firma que aparece en la pantalla al cargar
	 * un Fidelizado.
	 */
	@FXML
	public void realizarFirmaAxis() {
		log.debug("realizarFirmaAxis() - Iniciando la petición de la firma del cliente...");
		/* Realizamos la llamada al método de Axis que realiza la petición de firma */
		ByLFidelizacionBean datosFidelizado = (ByLFidelizacionBean) ticketManager.getTicket().getCabecera().getDatosFidelizado();
		if (datosFidelizado != null) {
			/* CREAMOS EL DIÁLOGO DE CARGA PERSONALIZADO, ESTO DEBERÍA HACERSE UNA SOLA VEZ EN EL POSAPPLICATION */
			ByLVentanaEspera.crearVentanaCargando(getStage());
			new RealizarFirmaTask(sesion.getAplicacion().getUidInstancia(), datosFidelizado.getIdFidelizado())
			.start(I18N.getTexto("En espera de que el cliente pulse los consentimientos y la firma en el dispositivo"));
		}
		else {
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
			
			if(resultado == null) {
				throw new Exception(I18N.getTexto("No hay dispositivo configurado para la firma"));
			}
			
			return resultado;
		}

		@Override
		protected void succeeded() {
				Map<String, Object> respuesta = getValue();
				/* Después de rescatar la respuesta realizamos un guardado del objeto de la firma */
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
							}
							else {
								Gson gson = new Gson();
								AxisConsentimientoBean axis = gson.fromJson(((JC3ApiC3Rspn) entry.getValue()).getJson(), AxisConsentimientoBean.class);
								if (axis.getOperationResult().getMainCondition()) {
									firma.setConsentimientoRecibenoti("S");
								}
								else {
									firma.setConsentimientoRecibenoti("N");
								}
								if (axis.getOperationResult().getSecondaryCondition()) {
									firma.setConsentimientoUsodatos("S");
								}
								else {
									firma.setConsentimientoUsodatos("N");
								}
							}
						}
						if (AxisManager.IMAGEN_FIRMA.equals(entry.getKey())) {
							if (entry.getValue() != null) {
								firma.setFirma(((byte[]) entry.getValue()));
							}
							else {
								String mensajeAviso = "El Cliente ha cancelado la Firma en el Dispositivo";
								log.info("RealizarFirmaTask/succeeded() - " + mensajeAviso);
								VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto(mensajeAviso), getStage());
								break;
							}
						}
					}
				}
				else {
					for (Map.Entry<String, Object> entry : respuesta.entrySet()) {
						if (entry.getKey().equals(IFirma.RESPUESTA_CONSENTIMIENTO_NOTIFICACIONES)) {
							if ((Boolean) entry.getValue()) {
								firma.setConsentimientoRecibenoti("S");
							}
							else {
								firma.setConsentimientoRecibenoti("N");
							}
						}
						if (entry.getKey().equals(IFirma.RESPUESTA_CONSENTIMIENTO_USO_DATOS)) {
							if ((Boolean) entry.getValue()) {
								firma.setConsentimientoUsodatos("S");
							}
							else {
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
				}
				else {
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
	
	/**
	 * Realiza la acción de Salvar de una firma.
	 * 
	 * @param firma
	 *            : Objeto para salvar.
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

			/* Pasamos los datos de la Firma y el Consentimiento al Fidelizado para poder cargar el cuadro de color. */
			ByLFidelizacionBean fidelizadoDatos = (ByLFidelizacionBean) ticketManager.getTicket().getCabecera().getDatosFidelizado();
			fidelizadoDatos.setConsentimientoUsodatos(firma.getConsentimientoUsodatos());
			fidelizadoDatos.setConsentimientoRecibenoti(firma.getConsentimientoRecibenoti());
			fidelizadoDatos.setFirma(firma.getFirma());
			ticketManager.getTicket().getCabecera().setDatosFidelizado(fidelizadoDatos);

			refrescarDatosPantalla();
		}
		catch (Exception e) {
			activarFirma(null);
			log.error("RealizarFirmaTask() - " + e.getMessage(), e);
			VentanaDialogoComponent.crearVentanaError(getStage(), I18N.getTexto(e.getMessage()), e);
		}
	}
	
	/**
	 * Realiza comprobaciones para saber que color poner al recuadro de la Firma.
	 * 
	 * @param firma
	 *            : Objeto que contiene los datos de la Firma del Fidelizado.
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
				btFirmaClienteDato.setStyle("-fx-background-color: #" + ByLPaneDatosGeneralesController.CONSENTIMIENTO_COLOR_VERDE + ";");
			}
			/* Si ninguno es correcto, lo ponemos en rojo */
			else if (!usoDatos && !recibeNoti && !firma) {
				btFirmaClienteDato.setStyle("-fx-background-color: #" + ByLPaneDatosGeneralesController.CONSENTIMIENTO_COLOR_ROJO + ";");
			}
			/* Para todas las demás combinaciones naranja */
			else {
				btFirmaClienteDato.setStyle("-fx-background-color: #" + ByLPaneDatosGeneralesController.CONSENTIMIENTO_COLOR_NARANJA + ";");
			}
			/* Si el objeto no se ha encontrado, ponemos el color rojo */
		}
		else {
			btFirmaClienteDato.setStyle("-fx-background-color: #" + ByLPaneDatosGeneralesController.CONSENTIMIENTO_COLOR_ROJO + ";");
		}
	}

	protected void accionEditarFidelizado() {
		if (ticketManager.getTicket().getCabecera().getDatosFidelizado() != null) {

			Long idFidelizado = ticketManager.getTicket().getCabecera().getDatosFidelizado().getIdFidelizado();
			if (idFidelizado != null) {
				Map<String, Object> datos = new HashMap<String, Object>();
				datos.put(CodigoTarjetaController.PARAMETRO_ID_FIDELIZADO, idFidelizado);
				datos.put(CodigoTarjetaController.PARAMETRO_MODO, "EDICION");
				getApplication().getMainView().showActionView(AppConfig.accionFidelizado, (HashMap<String, Object>) datos);
				return;
			}
		}
	}

	/**
	 * Oculta los componentes de la pantalla referentes a un Fidelizado.
	 * 
	 * @param activar
	 *            : Controla si activar o no los campos.
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
	 * Realiza un refrescado de los datos de la tabla donde se muestran los articulos.
	 */
	public void refrescarDatosPantalla() {

		log.debug("refrescarDatosPantalla() - Refrescando datos de pantalla...");

		/*
		 * EL cálculo de tickets aparcados se realiza de otra manera para el label que de forma normal.
		 */
		int tiquesLabel = ticketsAparcadosService.countTicketsAparcadosParaLabel(ticketManager.getTicket().getCabecera().getTipoDocumento());

		/* Comprobamos si es fidelizado o no. */
		if (ticketManager.getTicket().getCabecera().getDatosFidelizado() instanceof ByLFidelizacionBean || ticketManager.getTicket().getCabecera().getDatosFidelizado() == null) {
			ByLFidelizacionBean datosFidelizado = (ByLFidelizacionBean) ticketManager.getTicket().getCabecera().getDatosFidelizado();
			if (datosFidelizado == null || !StringUtils.isNotBlank(datosFidelizado.getNumTarjetaFidelizado())) {
				ocultarComponentesFidelizado(Boolean.FALSE);
			}
			else {
				limpiarDatosFidelizados();

				// Si el consentimientoRecibeNoti es null significa que no se han podido cargar correctamente los datos
				// firma y de consentimientos
				if (((ByLFidelizacionBean) ticketManager.getTicket().getCabecera().getDatosFidelizado()).getConsentimientoRecibenoti() == null) {
					// Consultamos los consentimientos y la firma actualizados del fidelizado para poderlo cargarlos
					// correctamente en el "semaforo"
					consultarFirmaConsentimientos(ticketManager.getTicket().getCabecera().getDatosFidelizado().getIdFidelizado());
				}
				else {
					cargarDatosFidelizado(datosFidelizado);
				}
			}
		}

		/* Comprobamos los tickets aparcados. */
		if (tiquesLabel > 0) {
			lbStatusTicketsAparcados.setText(I18N.getTexto("Hay tickets aparcados") + " (" + tiquesLabel + ")");
			lbimagenTicketsAparcados.setVisible(true);
		}
		else {
			lbStatusTicketsAparcados.setText(I18N.getTexto("No hay tickets aparcados"));
			lbimagenTicketsAparcados.setVisible(false);
		}

		/* Realizamos el calculo del total de precio y lo cargamos en pantalla. */
		String totalFormateado = FormatUtil.getInstance().formateaImporte(ticketManager.getTicket().getTotales().getTotal());
		resetAutosizeLabelTotalFont();
		lbTotal.setText(totalFormateado);
		autosizeLabelTotalFont();

		/* Insertamos los datos del cliente al que se está realizando la venta. */
		lbCodCliente.setText(((TicketVentaAbono) ticketManager.getTicket()).getCliente().getCodCliente());
		lbDesCliente.setText(((TicketVentaAbono) ticketManager.getTicket()).getCliente().getDesCliente());

		/* Cargamos la linea seleccionada y a continuacion limpiamos la tabla de articulos. */
		LineaTicketGui selectedItem = getLineaSeleccionada();
		lineas.clear();

		/* Aniadimos las lineas de nuevo, se pueden aniadir tambien cupones. */
		for (LineaTicket lineaTicket : ((TicketVentaAbono) ticketManager.getTicket()).getLineas()) {
			if (lineaTicket instanceof ByLLineaTicketProfesional) {
				addDescuentoReferencia((ByLLineaTicketProfesional) lineaTicket);
			}
			else {
				addDescuentoReferencia((ByLLineaTicket) lineaTicket);
			}
			lineas.add(createLineaGui(lineaTicket));
		}
		for (CuponAplicadoTicket cupon : ((TicketVentaAbono) ticketManager.getTicket()).getCuponesAplicados()) {
			lineas.add(createLineaGui(cupon));
		}

		/* Le damos la vuelta a las lineas. */
		Collections.reverse(lineas);

		if (selectedItem != null) {
			tbLineas.getSelectionModel().select(lineas.indexOf(searchIdLinea(selectedItem)));
		}
		if (getLineaSeleccionada() == null) {
			tfCodigoIntro.requestFocus();
		}
		tbLineas.scrollTo(0);

		if (imagenArticulo != null) {
			imagenArticulo.setImage(null);
		}

		obtenerCantidadTotal();
		
		lbBase.setText(FormatUtil.getInstance().formateaImporte(ticketManager.getTicket().getTotales().getBase()));
		lbIva.setText(FormatUtil.getInstance().formateaImporte(((TotalesTicketProfesional) ticketManager.getTicket().getTotales()).getIvaTotal()));
		lbRecargo.setText(FormatUtil.getInstance().formateaImporte(((TotalesTicketProfesional) ticketManager.getTicket().getTotales()).getRecargoTotal()));
		
		personalizacionUSA();
	}
	
	private void addDescuentoReferencia(ByLLineaTicket linea) {
		log.debug("addDescuentoReferencia() - Iniciamos el proceso para añadir el descuento sobre la tarifa inicial y los precios de venta de referencia de las tarifas a la línea");
		
		/* Cuando no venga tarifa inicial le setearemos su precio de tarifa original */
		BigDecimal precioVentaRef = linea.getPrecioTarifaOrigen();
		BigDecimal precioVentaRefTotal = linea.getPrecioTotalTarifaOrigen();		
		if(linea.getTarifa().getPrecioVentaRefTotal() != null && !BigDecimalUtil.isIgualACero(linea.getTarifa().getPrecioVentaRefTotal())){
			precioVentaRef = linea.getTarifa().getPrecioVentaRef();
			precioVentaRefTotal = linea.getTarifa().getPrecioVentaRefTotal();
		}
		
		BigDecimal descuentoSobreInicial = calculoDescuentoSobreInicial(precioVentaRefTotal, linea.getPrecioTotalConDto());
		log.debug("addDescuentoReferencia() - Porcentaje de descuento obtenido [" + descuentoSobreInicial + "]");
		linea.setDescuentoSobreInicial(descuentoSobreInicial);
		linea.setPrecioVentaRefTotal(precioVentaRefTotal);
		linea.setPrecioVentaRef(precioVentaRef);
	}
	
	private void addDescuentoReferencia(ByLLineaTicketProfesional linea) {
		log.debug("addDescuentoReferencia() - Iniciamos el proceso para añadir el descuento sobre la tarifa inicial y los precios de venta de referencia de las tarifas a la línea");
		
		/* Cuando no venga tarifa inicial le setearemos su precio de tarifa original */
		BigDecimal precioVentaRef = linea.getPrecioTarifaOrigen();
		BigDecimal precioVentaRefTotal = linea.getPrecioTotalTarifaOrigen();		
		if(linea.getTarifa().getPrecioVentaRefTotal() != null && !BigDecimalUtil.isIgualACero(linea.getTarifa().getPrecioVentaRefTotal())){
			precioVentaRef = linea.getTarifa().getPrecioVentaRef();
			precioVentaRefTotal = linea.getTarifa().getPrecioVentaRefTotal();
		}
		
		BigDecimal descuentoSobreInicial = calculoDescuentoSobreInicial(precioVentaRefTotal, linea.getPrecioTotalConDto());
		log.debug("addDescuentoReferencia() - Porcentaje de descuento obtenido [" + descuentoSobreInicial + "]");
		linea.setDescuentoSobreInicial(descuentoSobreInicial);
		linea.setPrecioVentaRefTotal(precioVentaRefTotal);
		linea.setPrecioVentaRef(precioVentaRef);
	}
	
	private BigDecimal calculoDescuentoSobreInicial(BigDecimal pvpInicial, BigDecimal pvpActual) {
		log.debug("calculoDescuentoSobreInicial() - Inicio del cálculo del descuento de la tarifa actual con promociones con respecto a la tarifa de referencia");
		log.debug("Tarifa Inicial [" + pvpInicial + "] - Tarifa Actual [" + pvpActual + "]");
		String maxDtoVisible = variablesServices.getVariableAsString(ByLFacturacionArticulosController.MAX_DTO_VISIBLE_PVP_INICIAL);
		BigDecimal porcentaje = BigDecimal.ZERO;
		if (StringUtils.isNotBlank(maxDtoVisible)) {
			if (pvpInicial != null && pvpInicial.compareTo(new BigDecimal(0)) > 0) {
				porcentaje = pvpInicial.subtract(pvpActual).multiply(new BigDecimal(100)).divide(pvpInicial, RoundingMode.HALF_DOWN);
				if (porcentaje.compareTo(new BigDecimal(5)) >= 0) {

					if (porcentaje.compareTo(new BigDecimal(maxDtoVisible)) >= 0) {
						porcentaje = new BigDecimal(maxDtoVisible);
					}
					else {
						BigDecimal resto = porcentaje.remainder(new BigDecimal(5));
						porcentaje = porcentaje.subtract(resto);
					}
				}
				else {
					porcentaje = BigDecimal.ZERO;
				}
			}

		}
		else {
			log.debug("calculoDescuentoSobreInicial() - No se ha encontrado la variable TPV.MAX_DTO_VISIBLE_PVP_INICIAL");
		}
		return porcentaje;
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

			if (fidelizadosBean.getConsentimientosFirma() != null) {
				String consentimientoNoti = fidelizadosBean.getConsentimientosFirma().getConsentimientoRecibenoti();
				String consentimientoUsoDatos = fidelizadosBean.getConsentimientosFirma().getConsentimientoUsodatos();
				byte[] firma = fidelizadosBean.getConsentimientosFirma().getFirma();

				((ByLFidelizacionBean) ticketManager.getTicket().getCabecera().getDatosFidelizado()).setConsentimientoRecibenoti(consentimientoNoti);
				((ByLFidelizacionBean) ticketManager.getTicket().getCabecera().getDatosFidelizado()).setConsentimientoUsodatos(consentimientoUsoDatos);
				((ByLFidelizacionBean) ticketManager.getTicket().getCabecera().getDatosFidelizado()).setFirma(firma);

				cargarDatosFidelizado((ByLFidelizacionBean) ticketManager.getTicket().getCabecera().getDatosFidelizado());
			}
			log.debug("ConsultarFidelizadoPorIdTask/succeeded() - Finalizada la carga de los datos del Fidelizado desde la pantalla de ByLFacturacionArticulosController");
			super.succeeded();
		}

		@Override
		public void failed() {
			super.failed();
			Exception e = (Exception) getException();
			log.error("ConsultarFidelizadoPorIdTask/failed() - " + e.getMessage(), e);
			VentanaDialogoComponent.crearVentanaError(I18N.getTexto(e.getMessage()), getStage());
		}
	}
	
	/**
	 * Rellena los campos que tienen que ver con un Fidelizado con los datos de este.
	 * 
	 * @param datosFidelizado
	 *            : Contiene los datos del Fidelizado.
	 */
	public void cargarDatosFidelizado(ByLFidelizacionBean datosFidelizado) {
		/* Lo primero es activar los componentes porque sino no sirve de nada cargarlos */
		ocultarComponentesFidelizado(Boolean.TRUE);
		if (datosFidelizado != null) {
			if (datosFidelizado.getNombre() != null && !datosFidelizado.getNombre().isEmpty()) {
				if (datosFidelizado.getApellido() != null && !datosFidelizado.getApellido().isEmpty()) {
					lbNombreFideDato.setText(datosFidelizado.getNombre() + " " + datosFidelizado.getApellido());
				}
				else {
					lbNombreFideDato.setText(datosFidelizado.getNombre());
				}
			}
			if (datosFidelizado.getEmail() != null && !datosFidelizado.getEmail().isEmpty()) {
				lbEmailClienteDato.setText(datosFidelizado.getEmail());
			}
			if (datosFidelizado.getTelefono() != null && !datosFidelizado.getTelefono().isEmpty()) {
				lbMovilClienteDato.setText(datosFidelizado.getTelefono());
			}
			if (datosFidelizado.getConsentimientoUsodatos() != null && datosFidelizado.getConsentimientoRecibenoti() != null && datosFidelizado.getFirma() != null) {
				activarFirma(datosFidelizado);
			}
			else {
				activarFirma(null);
			}
		}
	}

	public void limpiarDatosFidelizados() {
		lbNombreFideDato.setText("");
		lbMovilClienteDato.setText("");
		lbEmailClienteDato.setText("");
		activarFirma(null);
	}
	
	@Override
	protected void cerrarPantallaPagos() {
		getView().resetSubViews();
	}
	
	/**
	 * Inicia la pantalla necesaria para administrar la impresiÃ³n de tickets.
	 */
	@SuppressWarnings("rawtypes")
	@FXML
	public void imprimirTicketRegalo() {

		String formatoTicketRegalo;
		try {

			List<TipoDocumentoBean> tiposDocs = new ArrayList<TipoDocumentoBean>(sesion.getAplicacion().getDocumentos().getDocumentos().values());
			List<String> tiposDocValidos = new ArrayList<String>();
			List<Long> idTiposDocValidos = new ArrayList<>();
			for (TipoDocumentoBean doc : tiposDocs) {
				if (!doc.getFormatoImpresion().equals(TipoDocumentoBean.PROPIEDAD_FORMATO_IMPRESION_NO_CONFIGURADO)) {
					PropiedadDocumentoBean propiedad = doc.getPropiedades().get("TIPO_DOCUMENTO_ABONO");
					if (propiedad != null) {
						tiposDocValidos.add(doc.getCodtipodocumento());
						idTiposDocValidos.add(doc.getIdTipoDocumento());
					}
				}
			}

			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			List<TicketBean> ticketsConsultados = ticketsService.consultarTickets(sesion.getAplicacion().getCodCaja(), null, cal.getTime(), null, idTiposDocValidos);

			if (ticketsConsultados == null || ticketsConsultados.isEmpty()) {
				VentanaDialogoComponent.crearVentanaError("No existe ticket para la caja actual y el día de hoy.", this.getStage());
				return;
			}

			TicketBean ticketConsultado = ticketsConsultados.get(0);
			for (TicketBean ticket : ticketsConsultados) {
				if (ticket.getFecha().after(ticketConsultado.getFecha())) {
					ticketConsultado = ticket;
				}
			}

			ticketConsultado = ticketsService.consultarTicket(ticketConsultado.getUidTicket(), sesion.getAplicacion().getUidActividad());
			byte[] ticketXML = ticketConsultado.getTicket();
			TipoDocumentoBean documento = sesion.getAplicacion().getDocumentos().getDocumento(ticketConsultado.getIdTipoDocumento());

			ITicket ticketOperacion = (TicketVenta) MarshallUtil.leerXML(ticketXML, getTicketClasses(documento).toArray(new Class[] {}));

			formatoTicketRegalo = sesion.getAplicacion().getDocumentos().getDocumento(ticketOperacion.getCabecera().getCodTipoDocumento()).getFormatoImpresionTicketRegalo();
			ticketOperacion.getCabecera().setFormatoImpresionTicketRegalo(formatoTicketRegalo);

			if (formatoTicketRegalo != null) {
				HashMap<String, Object> datos = new HashMap<String, Object>();
				datos.put(TicketRegaloController.PARAMETRO_TICKET_REGALO, ticketOperacion);
				getApplication().getMainView().showModalCentered(TicketRegaloView.class, datos, this.getStage());
			}
			else {
				VentanaDialogoComponent.crearVentanaError("No existe un formato de ticket regalo para el tipo de documento del ticket original.", this.getStage());
			}

		}
		catch (DocumentoException e) {
			log.error("accionTicketRegalo() - Fallo al imprimir ticket regalo. Error : " + e.getMessage(), e);
			VentanaDialogoComponent.crearVentanaError(I18N.getTexto("Fallo al imprimir ticket."), getStage());
		}
		catch (TicketsServiceException e) {
			log.error("accionTicketRegalo() - Fallo al imprimir ticket regalo. Error : " + e.getMessage(), e);
			VentanaDialogoComponent.crearVentanaError(I18N.getTexto("Fallo al imprimir ticket regalo."), getStage());
		}
	}
	
	/**
	 * Devuelve la lista de clases que el Unmarshaller debe conocer. Además de la clase root, hay que pasarle la lista
	 * de superClasses de la root en orden descendente.
	 * 
	 * @param tipoDocumento
	 * @return classes
	 */
	public List<Class<?>> getTicketClasses(TipoDocumentoBean tipoDocumento) {

		List<Class<?>> classes = new LinkedList<>();
		/* Obtenemos la clase root */
		Class<?> clazz = SpringContext.getBean(getTicketClass(tipoDocumento)).getClass();

		/* Generamos lista de clases "ancestras" de la principal */
		Class<?> superClass = clazz.getSuperclass();
		while (!superClass.equals(Object.class)) {
			classes.add(superClass);
			superClass = superClass.getSuperclass();
		}
		/* Las ordenamos descendentemente */
		Collections.reverse(classes);

		/* AÃ±adimos la clase principal y otras necesarias */
		classes.add(clazz);
		classes.add(SpringContext.getBean(LineaTicket.class).getClass());
		classes.add(SpringContext.getBean(CabeceraTicket.class).getClass());
		classes.add(SpringContext.getBean(TotalesTicket.class).getClass());

		return classes;
	}
	
	/**
	 * Consulta la clase de un ticket.
	 * 
	 * @param tipoDocumento
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Class<? extends ITicket> getTicketClass(TipoDocumentoBean tipoDocumento) {

		String claseDocumento = tipoDocumento.getClaseDocumento();
		if (claseDocumento != null) {
			try {

				return (Class<? extends ITicket>) Class.forName(claseDocumento);

			}
			catch (ClassNotFoundException e) {
				log.error(String.format("getTicketClass() - " + "Clase %s no encontrada, devolveremos TicketVentaAbono", claseDocumento));
			}
		}
		return TicketVentaAbono.class;
	}
	
	@Override
	public void aparcarTicket() {
		log.debug("aparcarTicket()");
		if (!ticketManager.isTicketVacio()) { // Si el ticket no es vacío se puede aparcar
			try {
				log.debug("accionAparcarTicket()");
				
				// Se borra la copia de seguridad para que no de fallos de violación de claves al guardar el ticket aparcado en la misma tabla
		        copiaSeguridadTicketService.guardarBackupTicketActivo(new TicketVentaAbono());
				
				// Comprobamos que el ticket tiene almenos un artículo
				ticketManager.aparcarTicket();
				
				VentanaDialogoComponent.crearVentanaInfo(I18N.getTexto("El ticket ha sido aparcado."), getStage());

				try {
					getView().loadAndInitialize();
	                guardarCopiaSeguridad();

					visor.escribirLineaArriba(I18N.getTexto("---NUEVO CLIENTE---"));
					visor.modoEspera();
				}
				catch (InitializeGuiException e) {
					VentanaDialogoComponent.crearVentanaError(getStage(), e);
				}
			}
			catch (TicketsServiceException ex) {
				log.error("accionAparcarTicket()");
				VentanaDialogoComponent.crearVentanaError(this.getScene().getWindow(), ex.getMessageDefault(), ex);
			}
		}
		else {
			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("El ticket no contiene líneas de artículo."), this.getScene().getWindow());
		}
	}
	
	@Override
	protected void abrirVentanaPagos() {
		getApplication().getMainView().showModal(ByLPagosProfesionalView.class, getDatos());
	}
	
	@Override
	public void recargarGiftcard() {
		log.info("recargarGiftcard()");

		final LineaTicket lineaTicket = (LineaTicket) ticketManager.getTicket().getLineas().get(0);
		Dispositivos.getInstance().getGiftCard().pedirTarjetaRegalo(getStage(), new DispositivoCallback<GiftCardBean>(){

			@Override
			public void onSuccess(GiftCardBean tarjeta) {
				HashMap<String, Object> parametros = new HashMap<>();

				parametros.clear();
				parametros.put(SaldoTarjetaRegaloController.PARAMETRO_ENTRADA_TARJETA, tarjeta);
				parametros.put(SaldoTarjetaRegaloController.PARAMETRO_ENTRADA_RECARGA, lineaTicket.getImporteTotalConDto());
				parametros.put(SaldoTarjetaRegaloController.PARAMETRO_ENTRADA_RECARGA_VARIABLE, lineaTicket.getArticulo().getGenerico());
				getApplication().getMainView().showModalCentered(SaldoTarjetaRegaloView.class, parametros, getStage());
				BigDecimal saldoRecarga = (BigDecimal) parametros.get(SaldoTarjetaRegaloController.PARAMETRO_SALIDA_RECARGA);
				if (saldoRecarga != null && BigDecimalUtil.isMayorACero(saldoRecarga)) {
					tarjeta.setImporteRecarga(saldoRecarga);
					SesionImpuestos sesionImpuestos = sesion.getImpuestos();
					BigDecimal precioSinDto = sesionImpuestos.getPrecioSinImpuestos(lineaTicket.getCodImpuesto(), saldoRecarga, lineaTicket.getCabecera().getCliente().getIdTratImpuestos());
					lineaTicket.setPrecioSinDto(precioSinDto);
					lineaTicket.setPrecioTotalSinDto(saldoRecarga);
					lineaTicket.recalcularImporteFinal();
					ticketManager.getTicket().getCabecera().agnadirTarjetaRegalo(tarjeta);
					ticketManager.setEsRecargaTarjetaRegalo(true);
					ticketManager.getTicket().getTotales().recalcular();
					try {
						ticketManager.setDocumentoActivo(documentos.getDocumento(ByLFacturacionArticulosController.TIPO_DOCUMENTO_TARJETA_REGALO));
					}
					catch (DocumentoException e) {
						VentanaDialogoComponent.crearVentanaError(e.getMessage(), getStage());
						onFailure(new Exception());
					}
					abrirPagos();
				}
				else {
					if (saldoRecarga != null && !BigDecimalUtil.isMayorACero(saldoRecarga)) {
						VentanaDialogoComponent.crearVentanaError(I18N.getTexto("El importe de recarga debe ser mayor que 0"), getStage());
					}
					try {
						ticketManager.eliminarTicketCompleto();
					}
					catch (TicketsServiceException e) {
						e.printStackTrace();
					}
					catch (PromocionesServiceException e) {
						e.printStackTrace();
					}
					catch (DocumentoException e) {
						e.printStackTrace();
					}
					refrescarDatosPantalla();
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				// Los errores se muestran desde el cÃ³digo del dispositivo
				try {
					ticketManager.eliminarTicketCompleto();
					refrescarDatosPantalla();
					initializeFocus();
					tbLineas.getSelectionModel().clearSelection();

					visor.escribirLineaArriba(I18N.getTexto("---NUEVO CLIENTE---"));
					visor.modoEspera();
				}
				catch (Exception e) {
					log.error("accionAnularTicket() - Error inicializando nuevo ticket: " + e.getMessage(), e);
					VentanaDialogoComponent.crearVentanaError(getStage(), I18N.getTexto("Lo sentimos, ha ocurrido un error."), e);
				}
			}
		});

	}

	private void personalizacionUSA() {
		String codPais = sesion.getAplicacion().getTienda().getCliente().getCodpais();
		if (codPais.equals(COD_USA)) {
			log.debug("personalizacionUSA() - Inicio de las personalizaciones para USA");
			lbTotal.setText(FormatUtil.getInstance().formateaImporte(ticketManager.getTicket().getTotales().getBase()));
			log.debug("personalizacionUSA() - Fin de las personalizaciones para USA");
		}
	}
	
	@Override
	public void nuevoCodigoArticulo() {
		// Validamos los datos
		if (!tfCodigoIntro.getText().trim().isEmpty()) {
			log.debug("nuevoCodigoArticulo() - Creando línea de artículo");

			frValidacion.setCantidad(tfCantidadIntro.getText().trim());
			frValidacion.setCodArticulo(tfCodigoIntro.getText().trim().toUpperCase());
			BigDecimal cantidad = frValidacion.getCantidadAsBigDecimal();
			tfCodigoIntro.clear();

			if (accionValidarFormulario() && cantidad != null && !BigDecimalUtil.isIgualACero(cantidad)) {
				log.debug("nuevoCodigoArticulo()- Formulario validado");

				// El primer parámetro por ser una clase anidada
				NuevoCodigoArticuloTask taskArticulo = SpringContext.getBean(NuevoCodigoArticuloTask.class, this, frValidacion.getCodArticulo(), cantidad);
				taskArticulo.start();
			}
		}
	}
}
