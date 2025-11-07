package com.comerzzia.pos.gui.ventas.apartados.detalle.pagos;

/**
 * LEEME : Esta clase se ha creado como nueva porque al extender la clase de PagosController tenía el programa dos
 * clases diferentes extendiendo de la misma y las dos con Component/Primary. Esto producía un error que hacía que el
 * programa entrara en la estandar en vez de las clases personalizadas. La solución ha sido crear esta clase como nueva
 * y que extienda de nuestra personalización de PagosController.
 * 
 * @author JAP
 */

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.gui.componentes.dialogos.ByLVentanaDialogoComponent;
import com.comerzzia.bimbaylola.pos.gui.pagos.ByLPagosController;
import com.comerzzia.bimbaylola.pos.gui.pagos.standalone.ReferenciaStandaloneController;
import com.comerzzia.bimbaylola.pos.gui.pagos.standalone.ReferenciaStandaloneView;
import com.comerzzia.bimbaylola.pos.persistence.reservas.ReservasPagoGiftCardBean;
import com.comerzzia.bimbaylola.pos.services.cajas.ByLCajasService;
import com.comerzzia.pos.core.dispositivos.Dispositivos;
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
import com.comerzzia.pos.dispositivo.visor.pantallasecundaria.gui.TicketVentaDocumentoVisorConverter;
import com.comerzzia.pos.gui.ventas.apartados.ApartadosManager;
import com.comerzzia.pos.gui.ventas.tickets.TicketManager;
import com.comerzzia.pos.gui.ventas.tickets.TicketManager.SalvarTicketCallback;
import com.comerzzia.pos.gui.ventas.tickets.articulos.FacturacionArticulosController;
import com.comerzzia.pos.gui.ventas.tickets.pagos.FormularioImportePagosBean;
import com.comerzzia.pos.gui.ventas.tickets.pagos.PagosController;
import com.comerzzia.pos.persistence.giftcard.GiftCardBean;
import com.comerzzia.pos.persistence.mediosPagos.MedioPagoBean;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.services.core.variables.VariablesServices;
import com.comerzzia.pos.services.mediospagos.MediosPagosService;
import com.comerzzia.pos.services.ticket.TicketVenta;
import com.comerzzia.pos.services.ticket.TicketsServiceException;
import com.comerzzia.pos.services.ticket.pagos.IPagoTicket;
import com.comerzzia.pos.services.ticket.pagos.PagoTicket;
import com.comerzzia.pos.services.ticket.pagos.PagoTicketException;
import com.comerzzia.pos.services.ticket.pagos.tarjeta.DatosRespuestaPagoTarjeta;
import com.comerzzia.pos.servicios.impresion.ServicioImpresion;
import com.comerzzia.pos.util.bigdecimal.BigDecimalUtil;
import com.comerzzia.pos.util.config.SpringContext;
import com.comerzzia.pos.util.format.FormatUtil;
import com.comerzzia.pos.util.i18n.I18N;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;

@Component
public class NuevoPagoApartadoController extends PagosController {

	public static final String GIFCARD_PAGO_RESERVA = "GIFCARD_PAGO_RESERVA";

	public static final String PARAMETRO_APARTADO_MANAGER = "MANAGER_APARTADO";
	
	public static final String DESMEDPAG_MSI = "ADYEN MSI";

	protected ApartadosManager apartadoManager;

	@Autowired
	protected VariablesServices variablesServices;
	@Autowired
	protected MediosPagosService mediosPagosService;
	@Autowired
	protected ByLCajasService cajaService;

	@Autowired
	private Sesion sesion;

	final IVisor visor = Dispositivos.getInstance().getVisor();
	@Autowired
	protected TicketVentaDocumentoVisorConverter visorConverter;

	public void initializeForm() throws InitializeGuiException {

		enProceso = false;
		tarjetaRegalo = null;

		apartadoManager = (ApartadosManager) getDatos().remove(PARAMETRO_APARTADO_MANAGER);
		ticketManager = (TicketManager) getDatos().get(FacturacionArticulosController.TICKET_KEY);

		cargarBotoneraDatosAdicionales();
		try {
			panelPagoContado.getChildren().clear();
			botoneraMediosPagoContado = new BotoneraComponent(3, 4, this, botoneraMediosPagoContado.getListaAcciones(), null, panelPagoContado.getPrefHeight(),
			        BotonBotoneraTextoComponent.class.getName());
			panelPagoContado.getChildren().add(botoneraMediosPagoContado);
		}
		catch (Exception e) {
		}

		lbDocActivo.setText(ticketManager.getDocumentoActivo().getDestipodocumento());

		lbTitulo.setText(I18N.getTexto("Pagos"));

		panelPagos.focusedProperty().addListener(new ChangeListener<Boolean>(){

			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
				if (t.booleanValue() == false && t1.booleanValue() == true) {
					medioPagoSeleccionado = MediosPagosService.medioPagoDefecto;
					lbMedioPago.setText(medioPagoSeleccionado.getDesMedioPago());
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

		panelEntregaCuenta.setVisible(false);

		/* Establecemos el medio de pago por defecto */
		medioPagoSeleccionado = MediosPagosService.medioPagoDefecto;
		lbMedioPago.setText(medioPagoSeleccionado.getDesMedioPago());

		panelPagos.getSelectionModel().select(panelPestanaPagoEfectivo);
		lbSaldo.setText("");

		ticketManager.getTicket().getTotales().recalcular();

		visor.modoPago(visorConverter.convert((TicketVenta<?, ?, ?>) ticketManager.getTicket()));
		escribirVisor();

		refrescarDatosPantalla();
	}

	/**
	 * Introduce un pago en un ticket.
	 * 
	 * @param importe
	 *            : Importe que se va a introducir.
	 * @throws PagoTicketException
	 */
	protected void incluirPagoTicket(BigDecimal importe) throws PagoTicketException {
		String desMedioPagoUsado = null;

		if (!((TicketVenta<?, ?, ?>) ticketManager.getTicket()).getPagos().isEmpty()) {
			desMedioPagoUsado = ((IPagoTicket) ((TicketVenta<?, ?, ?>) ticketManager.getTicket()).getPagos().get(0)).getDesMedioPago();
		}

		if (desMedioPagoUsado == null || desMedioPagoUsado.equals(medioPagoSeleccionado.getDesMedioPago())) {
			ticketManager.nuevaLineaPago(medioPagoSeleccionado.getCodMedioPago(), importe, true, true);
			apartadoManager.getTicketApartado().calcularTotales();
			refrescarDatosPantalla();
		}
		else {
			String mensajeAviso = "No se permite el uso de diferentes medios de pago";
			log.debug("incluirPagoTicket() - " + mensajeAviso);
			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto(mensajeAviso), getStage());
		}
	}

	/**
	 * Acción para borrar un registro seleccionado por el usuario de la tabla.
	 */
	@FXML
	protected void accionBorrarRegistroTabla() {
		log.debug("accionBorrarRegistroTabla() - Acción ejecutada");

		if (tbPagos.getItems() != null && tbPagos.getItems().size() > 0) {
			log.debug("accionBorrarRegistroTabla() - Usuario solicita eliminar pagos.");
			String mensajeConfirmacion = "¿Desea eliminar los registros?";
			if (VentanaDialogoComponent.crearVentanaConfirmacion(I18N.getTexto("Confirme operación"), I18N.getTexto(mensajeConfirmacion), getApplication().getStage())) {

				log.debug("accionBorrarRegistroTabla() - Se confirma la operación");
				/* Eliminamos el registro */
				ticketManager.eliminarPagos();

				if (tarjetaRegalo != null) {
					tarjetaRegalo = null;
					ticketManager.getTicket().getCabecera().setTarjetaRegalo(null);
					ticketManager.setEsOperacionTarjetaRegalo(false);
				}

				/* Refrescar datos pantalla recarga la lista de pagos en pantalla. */
				refrescarDatosPantalla();
			}
		}
		else {
			VentanaDialogoComponent.crearVentanaInfo(I18N.getTexto("Borrar pago"), I18N.getTexto("Sin pagos para eliminar."), getApplication().getStage());
		}

		tfImporte.requestFocus();
	}

	/**
	 * Realiza el borrado de pagos de un ticket y calcula el total de los tickets aparcados.
	 */
	@Override
	public void borrarDatosPago() {

		ticketManager.getTicket().getPagos().clear();
		apartadoManager.getTicketApartado().calcularTotales();

		getStage().close();

	}

	/**
	 * Acción aceptar
	 */
	@FXML
	public void aceptar() {
		if (cajaService.comprobarCajaMaster()) {
			if (!enProceso) {
				log.debug("aceptar() - Iniciamos el guardado de los Pagos realizado...");
				enProceso = true;
				try {
					if ((((TicketVenta<?, ?, ?>) ticketManager.getTicket()).isPagosCubiertos() && ticketManager.getDocumentoActivo().getRequiereCompletarPagos())
					        || !ticketManager.getDocumentoActivo().getRequiereCompletarPagos()) {

						comprobarMensajeVisa();

						/* Comprobacion de la operativa StandAlone */
						if (!comprobarStandAlone()) {
							return;
						}

						ticketManager.salvarTicketApartado(apartadoManager.getTicketApartado().getCabecera(), getStage(), new SalvarTicketCallback(){

							@Override
							public void onSucceeded() {
								try {
									Map<String, Object> mapaParametros = new HashMap<String, Object>();
									mapaParametros.put("ticket", ticketManager.getTicket());
									mapaParametros.put("apartado", apartadoManager.getTicketApartado().getCabecera());
									mapaParametros.put("fecha", FormatUtil.getInstance().formateaFechaHora(new Date()));
									mapaParametros.put("empresa", sesion.getAplicacion().getEmpresa());
									mapaParametros.put("cajero", sesion.getSesionUsuario().getUsuario().getDesusuario());
									mapaParametros.put("pagos", ((TicketVenta<?, ?, ?>) ticketManager.getTicket()).getPagos());
									mapaParametros.put("totales", ((TicketVenta<?, ?, ?>) ticketManager.getTicket()).getTotales());

									ServicioImpresion.imprimir(ticketManager.getTicket().getCabecera().getFormatoImpresion(), mapaParametros);

									if (mediosPagosService.isCodMedioPagoVale(ticketManager.getTicket().getTotales().getCambio().getCodMedioPago(),
									        ticketManager.getTicket().getCabecera().getTipoDocumento())
									        && !BigDecimalUtil.isIgualACero(ticketManager.getTicket().getTotales().getCambio().getImporte())) {
										Map<String, Object> mapaParametrosTicket = new HashMap<String, Object>();
										mapaParametrosTicket.put("ticket", ticketManager.getTicket());
										mapaParametrosTicket.put("urlQR", variablesServices.getVariableAsString("TPV.URL_VISOR_DOCUMENTOS"));
										mapaParametrosTicket.put("importeVale", ticketManager.getTicket().getTotales().getCambio().getImporteAsString());
										mapaParametrosTicket.put("esCopia", Boolean.FALSE);
										ServicioImpresion.imprimir(PLANTILLA_VALE, mapaParametrosTicket);
										mapaParametrosTicket.put("esCopia", Boolean.TRUE);
										ServicioImpresion.imprimir(PLANTILLA_VALE, mapaParametrosTicket);
									}

								}
								catch (Exception e) {
									String mensajeError = "Lo sentimos, ha ocurrido un error al imprimir";
									log.error("aceptar() - " + mensajeError + " - " + e.getMessage(), e);
									VentanaDialogoComponent.crearVentanaError(getApplication().getStage(), I18N.getTexto(mensajeError), e);
								}

								/* Mostramos la ventana con la información de importe pagado, cambio... */
								if (ticketManager.getTicket().getTotales().getCambio().getImporte().compareTo(BigDecimal.ZERO) != 0) {
									mostrarVentanaCambio();
								}

								ticketManager.finalizarTicket();
								getStage().close();
								enProceso = false;
							}

							@Override
							public void onFailure(Exception e) {
								String mensajeError = "Error al salvar el ticket";
								log.debug("onFailure() - " + mensajeError + " - " + e.getMessage(), e);
								VentanaDialogoComponent.crearVentanaError(getStage(), mensajeError, e);
								enProceso = false;
								getStage().close();
							}
						});
					}
					else {
						String mensajeAviso = I18N.getTexto("Los pagos han de cubrir el importe a pagar");
						log.debug("aceptar() - " + mensajeAviso);
						VentanaDialogoComponent.crearVentanaAviso(mensajeAviso, this.getStage());
						enProceso = false;
					}
				}
				catch (TicketsServiceException ex) {
					log.error("aceptar() - " + ex.getMessage(), ex);
					VentanaDialogoComponent.crearVentanaError(getApplication().getStage(), ex.getMessageI18N(), ex);
				}
				finally {
					enProceso = false;
				}
			}
			else {
				String mensajeAviso = "Pago en proceso";
				log.debug("aceptar() - " + mensajeAviso);
			}
		}
		else {
			cajaMasterCerrada();
		}
	}

	@Override
	protected void escribirVisor() {
		visor.escribir(I18N.getTexto("TOTAL:     ") + ticketManager.getTicket().getTotales().getTotalAPagarAsString(),
		        I18N.getTexto("PENDIENTE: ") + ticketManager.getTicket().getTotales().getPendienteAsString());
	}

	/**
	 * Envía un mensaje por pantalla, que indica que la caja no está abierta y te envía a la pantalla principal.
	 */
	private void cajaMasterCerrada() {
		String mensajeAviso = "La caja a la que estaba conectado se ha cerrado";
		log.debug("cajaMasterCerrada() - " + mensajeAviso);
		VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto(mensajeAviso), this.getStage());
		/* Vuelve a la pantalla principal. */
		POSApplication.getInstance().getMainView().close();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void anotarPago(BigDecimal importe) {
		log.debug("anotarPago() - Medio de Pago : " + medioPagoSeleccionado);
		log.debug("anotarPago() -Importe : " + importe);
		boolean esTarjetaRegalo = Dispositivos.getInstance().getGiftCard().isMedioPagoGiftCard(medioPagoSeleccionado.getCodMedioPago());
		if (medioPagoSeleccionado == null) {
			VentanaDialogoComponent.crearVentanaError(I18N.getTexto("No hay ninguna forma de pago seleccionada."), getStage());
			return;
		}
		if (importe.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}
		if ((esTarjetaRegalo || MediosPagosService.mediosPagoTarjetas.contains(medioPagoSeleccionado))
		        && BigDecimalUtil.isMayor(importe.abs(), ticketManager.getTicket().getTotales().getPendiente().abs())) {
			VentanaDialogoComponent.crearVentanaError(I18N.getTexto("El medio de pago seleccionado no permite introducir un importe superior al total del documento"), getStage());
			tfImporte.setText(FormatUtil.getInstance().formateaImporte(ticketManager.getTicket().getTotales().getPendiente()));
			return;
		}
		try {
			if (esTarjetaRegalo) {
				if (tarjetaRegalo != null) {
					boolean esVenta = ticketManager.getTicket().getCabecera().esVenta();
					BigDecimal saldoDisponibleTarjetaRegalo = calcularSaldoPendienteTarjetaRegalo(esVenta);

					if (esVenta && BigDecimalUtil.isMayor(importe, saldoDisponibleTarjetaRegalo)) {
						VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("El importe supera el saldo disponible"), this.getStage());
					}
					else {
						ticketManager.setEsOperacionTarjetaRegalo(true);
						incluirPagoTicket(importe, tarjetaRegalo);

						medioPagoSeleccionado = MediosPagosService.medioPagoDefecto;
						lbMedioPago.setText(medioPagoSeleccionado.getDesMedioPago());
						tfImporte.setText(FormatUtil.getInstance().formateaImporte(ticketManager.getTicket().getTotales().getPendiente()));
						panelPagos.getSelectionModel().select(panelPestanaPagoEfectivo);
						lbSaldo.setText("");
						tfImporte.requestFocus();

						/* Generamos el pago de la tarjeta de regalo en esta parte porque después se pierde */
						ReservasPagoGiftCardBean pagoGiftCardReserva = new ReservasPagoGiftCardBean();
						pagoGiftCardReserva.setSaldo(tarjetaRegalo.getSaldo());
						pagoGiftCardReserva.setSaldoProvisional(tarjetaRegalo.getSaldoProvisional());
						pagoGiftCardReserva.setImportePago(importe);
						pagoGiftCardReserva.setNumeroTarjeta(tarjetaRegalo.getNumTarjetaRegalo());
						pagoGiftCardReserva.setUidTransaccion(tarjetaRegalo.getUidTransaccion());

						getDatos().put(GIFCARD_PAGO_RESERVA, pagoGiftCardReserva);
					}
				}
			}
			else {
				incluirPagoTicket(importe, null);
			}
			visor.modoPago(visorConverter.convert((TicketVenta) ticketManager.getTicket()));
			escribirVisor();
		}
		catch (PagoTicketException ex) {
			VentanaDialogoComponent.crearVentanaError(getStage(), ex.getMessageI18N(), ex);
		}
		tfImporte.requestFocus();

		/* La ponemos a nulo para que no vuelva a ponerse el saldo */
		if (tarjetaRegalo != null) {
			tarjetaRegalo = null;
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

	private void comprobarMensajeVisa() {
		// Inicio ISSUES #66
		Boolean mostrarAviso = false;

		for (Object pago : ticketManager.getTicket().getPagos()) {
			if (pago instanceof PagoTicket && ((PagoTicket) pago).getDatosRespuestaPagoTarjeta() != null) {
				break;
			}

			if (Dispositivos.getInstance().getTarjeta() != null && Dispositivos.getInstance().getTarjeta().getConfiguracion() != null) {
				Map<String, String> mapaConfiguracion = Dispositivos.getInstance().getTarjeta().getConfiguracion().getParametrosConfiguracion();
				String medPagoConfigurado = ByLPagosController.COD_MP_PINPAD;
				if (mapaConfiguracion.get("PAYMENTS") != null) {
					medPagoConfigurado = mapaConfiguracion.get("PAYMENTS");
				}
				if (pago instanceof PagoTicket && ((PagoTicket) pago).isMedioPagoTarjeta() && !((PagoTicket) pago).getCodMedioPago().equals(medPagoConfigurado)
				        && !((PagoTicket) pago).getCodMedioPago().equals(ByLPagosController.COD_MP_PINPAD_ADYEN_STANDALONE)) {
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
			if (((PagoTicket) pago).getCodMedioPago().equals(ByLPagosController.COD_MP_PINPAD_ADYEN_STANDALONE)) {
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
	@Override
	public void realizarAccion(BotonBotoneraComponent botonAccionado) {
		log.debug("realizarAccion() - Realizando la acción : " + botonAccionado.getClave() + " de tipo : " + botonAccionado.getTipo());
		switch (botonAccionado.getClave()) {
			case "ACCION_SELECIONAR_MEDIO_PAGO":
				log.debug("Acción cambiar medio de pago en pantalla");
				BotonBotoneraTextoComponent boton = (BotonBotoneraTextoComponent) botonAccionado;
				MedioPagoBean mp = boton.getMedioPago();

				if (mp.getTarjetaCredito()) {
					medioPagoSeleccionado = mp;
					lbMedioPago.setText(mp.getDesMedioPago());
					lbSaldo.setText("");
					seleccionarMedioPagoTarjeta(mp);
				}
				else if (Dispositivos.getInstance().getGiftCard() != null && Dispositivos.getInstance().getGiftCard().isMedioPagoGiftCard(mp.getCodMedioPago())) {
					/*
					 * Si el medio de pago seleccionado es de tipo tarjeta regalo pedimos el número de tarjeta /* En los
					 * pagos de reservas, sólo permitiremos un pago de tarjeta regalo/abono
					 */
					List<PagoTicket> listaPagos = ticketManager.getTicket().getPagos();
					Boolean existePagoTTA = Boolean.FALSE;
					if (listaPagos != null && !listaPagos.isEmpty()) {
						for (PagoTicket pagoTicket : listaPagos) {
							if (Dispositivos.getInstance().getGiftCard().isMedioPagoGiftCard(pagoTicket.getCodMedioPago())) {
								existePagoTTA = Boolean.TRUE;
							}
						}
					}
					if (!existePagoTTA) {
						procesarPagoTarjetaRegalo(mp);
					}
					else {
						log.warn("Sólo se puede realizar el pago con una tarjeta regalo/abono a la vez");
						VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("No puede usarse más de una tarjeta regalo"), getStage());
					}

				}
				else {
					lbMedioPago.setText(mp.getDesMedioPago());
					medioPagoSeleccionado = mp;
					lbSaldo.setText("");
				}
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
			default:
				log.error("No se ha especificado acción en pantalla para la operación :" + botonAccionado.getClave());
				break;
		}

		restaurarFocoTFImporte();
	}

	@FXML
	public void accionCambiarCajero() {
		datos.put(ByLPagosController.PARAMETRO_MODO_PANTALLA_CAJERO, true);
		POSApplication.getInstance().getMainView().showModal(SeleccionUsuariosView.class, datos);

		ticketManager.getTicket().setCajero(sesion.getSesionUsuario().getUsuario());

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
			List<ConfiguracionBotonBean> listaAccionesTarjeta = new LinkedList<>();

			
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
	
				for(MedioPagoBean mpTarjeta: MediosPagosService.mediosPagoTarjetas){
					if (!mpTarjeta.getDesMedioPago().equals(DESMEDPAG_MSI)) {
						ConfiguracionBotonMedioPagoBean cfg = new ConfiguracionBotonMedioPagoBean(null, mpTarjeta.getDesMedioPago(), null, "ACCION_SELECIONAR_MEDIO_PAGO", "", mpTarjeta);
						listaAccionesTarjeta.add(cfg);
					}
					
				}
				botoneraMediosPagoTarjeta = new BotoneraComponent(3, 4, this, listaAccionesTarjeta, panelPagoTarjeta.getPrefWidth(), panelPagoTarjeta.getPrefHeight(), BotonBotoneraTextoComponent.class.getName());
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
	}

}
