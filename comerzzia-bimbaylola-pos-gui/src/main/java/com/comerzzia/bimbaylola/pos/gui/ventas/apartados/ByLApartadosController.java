package com.comerzzia.bimbaylola.pos.gui.ventas.apartados;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.dispositivo.giftcard.ByLGiftCard;
import com.comerzzia.bimbaylola.pos.dispositivo.giftcard.EnviarMovimientoGiftCardBean;
import com.comerzzia.bimbaylola.pos.gui.pagos.ByLPagosController;
import com.comerzzia.bimbaylola.pos.gui.pagos.standalone.ReferenciaStandaloneController;
import com.comerzzia.bimbaylola.pos.gui.pagos.standalone.ReferenciaStandaloneView;
import com.comerzzia.bimbaylola.pos.gui.ventas.apartados.detalle.ByLDetalleApartadosController;
import com.comerzzia.bimbaylola.pos.gui.ventas.tickets.ByLTicketManager;
import com.comerzzia.bimbaylola.pos.persistence.giftcard.ByLGiftCardBean;
import com.comerzzia.bimbaylola.pos.persistence.reservas.ReservasPagoGiftCardBean;
import com.comerzzia.bimbaylola.pos.services.apartados.ByLApartadosService;
import com.comerzzia.bimbaylola.pos.services.apartados.exception.ReservaDevolucionCancelacionException;
import com.comerzzia.bimbaylola.pos.services.cajas.ByLCajasService;
import com.comerzzia.bimbaylola.pos.services.reservas.exception.TicketReservaException;
import com.comerzzia.bimbaylola.pos.services.ticket.ByLTicketVentaAbono;
import com.comerzzia.bimbaylola.pos.services.ticket.aparcados.reservas.DatosRespuestaTarjetaReservaTicket;
import com.comerzzia.core.util.fechas.Fecha;
import com.comerzzia.pos.core.dispositivos.Dispositivos;
import com.comerzzia.pos.core.dispositivos.dispositivo.DispositivoCallback;
import com.comerzzia.pos.core.gui.POSApplication;
import com.comerzzia.pos.core.gui.componentes.botonaccion.BotonBotoneraComponent;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.dispositivo.tarjeta.adyen.cloud.TefAdyenCloud;
import com.comerzzia.pos.gui.ventas.apartados.ApartadosController;
import com.comerzzia.pos.gui.ventas.apartados.LineaApartadoGui;
import com.comerzzia.pos.gui.ventas.apartados.detalle.pagos.NuevoPagoApartadoController;
import com.comerzzia.pos.gui.ventas.tickets.TicketManager;
import com.comerzzia.pos.gui.ventas.tickets.TicketManager.SalvarTicketCallback;
import com.comerzzia.pos.gui.ventas.tickets.pagos.vuelta.VueltaController;
import com.comerzzia.pos.gui.ventas.tickets.pagos.vuelta.VueltaView;
import com.comerzzia.pos.persistence.apartados.ApartadosCabeceraBean;
import com.comerzzia.pos.persistence.apartados.detalle.ApartadosDetalleBean;
import com.comerzzia.pos.persistence.apartados.pagos.ApartadosPagoBean;
import com.comerzzia.pos.persistence.articulos.ArticuloBean;
import com.comerzzia.pos.persistence.cajas.movimientos.CajaMovimientoBean;
import com.comerzzia.pos.persistence.core.contadores.ContadorBean;
import com.comerzzia.pos.persistence.core.documentos.tipos.TipoDocumentoBean;
import com.comerzzia.pos.persistence.fidelizacion.FidelizacionBean;
import com.comerzzia.pos.persistence.giftcard.GiftCardBean;
import com.comerzzia.pos.persistence.mediosPagos.MedioPagoBean;
import com.comerzzia.pos.services.articulos.ArticulosService;
import com.comerzzia.pos.services.core.contadores.ContadorServiceException;
import com.comerzzia.pos.services.core.contadores.ServicioContadores;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.services.core.variables.VariablesServices;
import com.comerzzia.pos.services.devices.DeviceException;
import com.comerzzia.pos.services.mediospagos.MediosPagosService;
import com.comerzzia.pos.services.ticket.Ticket;
import com.comerzzia.pos.services.ticket.TicketsServiceException;
import com.comerzzia.pos.services.ticket.lineas.LineaTicket;
import com.comerzzia.pos.services.ticket.pagos.tarjeta.DatosRespuestaPagoTarjeta;
import com.comerzzia.pos.services.ticket.tarjetaRegalo.TarjetaRegaloException;
import com.comerzzia.pos.servicios.impresion.ServicioImpresion;
import com.comerzzia.pos.util.config.SpringContext;
import com.comerzzia.pos.util.format.FormatUtil;
import com.comerzzia.pos.util.i18n.I18N;
import com.comerzzia.rest.client.exceptions.RestException;
import com.comerzzia.rest.client.exceptions.RestHttpException;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

@Component
@Primary
public class ByLApartadosController extends ApartadosController {

	private static final Logger log = Logger.getLogger(ByLApartadosController.class.getName());

	@Autowired
	protected ByLCajasService cajasService;
	@Autowired
	private ByLApartadosService apartadosService;
	@Autowired
	private ArticulosService articulosService;
	@Autowired
	private VariablesServices variablesServices;
	@Autowired
	protected ServicioContadores contadoresService;
	@Autowired
	private MediosPagosService mediosPagosService;

	@Autowired
	private Sesion sesion;

	public static final String CODIGO_DOCUMENTO_RESERVA = "RE";
	public static final String CANCELACION_RESERVA = "esCancelacion";

	@Override
	public void realizarAccion(BotonBotoneraComponent botonAccionado) {

		log.debug("realizarAccion() - Realizando la acción : " + botonAccionado.getClave() + " de tipo : " + botonAccionado.getTipo());

		switch (botonAccionado.getClave()) {

			case "ACCION_TABLA_PRIMER_REGISTRO":
				accionTablaPrimerRegistro(tbApartados);
				reasignarFocoBtDesplazamiento();
				break;
			case "ACCION_TABLA_ANTERIOR_REGISTRO":
				accionTablaIrAnteriorRegistro(tbApartados);
				reasignarFocoBtDesplazamiento();
				break;
			case "ACCION_TABLA_SIGUIENTE_REGISTRO":
				accionTablaIrSiguienteRegistro(tbApartados);
				reasignarFocoBtDesplazamiento();
				break;
			case "ACCION_TABLA_ULTIMO_REGISTRO":
				accionTablaUltimoRegistro(tbApartados);
				reasignarFocoBtDesplazamiento();
				break;
			case "ACCION_TABLA_BORRAR_REGISTRO":
				if (cajasService.comprobarCajaMaster()) {
					cancelarApartado();
				}
				else {
					cajaMasterCerrada();
				}
				break;
			case "ACCION_TABLA_VER_REGISTRO":
				verApartado();
				initializeFocus();
				break;
			case "ACCION_TABLA_NUEVO_APARTADO":
				if (cajasService.comprobarCajaMaster()) {
					crearApartado();
				}
				else {
					cajaMasterCerrada();
				}
				break;
			case "ACCION_TABLA_FINALIZAR_APARTADO":
				if (cajasService.comprobarCajaMaster()) {
					finalizarApartado();
				}
				else {
					cajaMasterCerrada();
				}
				break;
			case "ACCION_TABLA_EDITAR_APARTADO":
				if (cajasService.comprobarCajaMaster()) {
					editarApartado();
					initializeFocus();
				}
				else {
					cajaMasterCerrada();
				}
				break;
			default:
				log.error("No se ha especificado acción en pantalla para la operación :" + botonAccionado.getClave());
				break;
		}

	}

	/**
	 * Envía un mensaje por pantalla, que indica que la caja no está abierta y te envía a la pantalla principal.
	 */
	private void cajaMasterCerrada() {
		VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("La caja a la que estaba conectado se ha cerrado."), this.getStage());
		/* Vuelve a la pantalla principal. */
		POSApplication.getInstance().getMainView().close();
	}

	public void cancelarApartado() {
		log.trace("cancelarApartado() - Iniciamos la cancelación de la Reserva...");
		LineaApartadoGui lineaApartado = getLineaSeleccionada();
		Boolean usaTarjetaRegalo = Boolean.FALSE;
		if (lineaApartado != null) {
			ApartadosCabeceraBean apartado = lineaApartado.getApartado();
			if (apartado.getEstadoApartado() != ApartadosCabeceraBean.ESTADO_DISPONIBLE) {
				String mensajeAviso = I18N.getTexto("Esta Reserva no se puede cancelar");
				log.debug("cancelarApartado() - " + mensajeAviso);
				VentanaDialogoComponent.crearVentanaAviso(mensajeAviso, getStage());
			}
			else {
				String mensajeConfirmacion = I18N.getTexto("Se cancelará la Reserva. ¿Está seguro?");
				if (VentanaDialogoComponent.crearVentanaConfirmacion(mensajeConfirmacion, this.getStage())) {
					if (apartado.getSaldoCliente().compareTo(BigDecimal.ZERO) > 0) {
						getDatos().put(VueltaController.CLAVE_OCULTAR_BOTONES, true);
						getDatos().put(CANCELACION_RESERVA, true);
						getApplication().getMainView().showModalCentered(VueltaView.class, getDatos(), getStage());
						getDatos().remove(VueltaController.CLAVE_OCULTAR_BOTONES);
						getDatos().remove(CANCELACION_RESERVA);
					}
					if (!getDatos().containsKey(VueltaController.CLAVE_CANCELADO)) {

						// INICIO incidencia #70
						if (apartado.getSaldoCliente().compareTo(BigDecimal.ZERO) > 0) {
							
							apartadosManager.cargarApartado(apartado);
							
							String codMedioPagoSeleccionado = (String) getDatos().remove(VueltaController.CLAVE_PARAMETRO_SALIDA_VUELTA);
							MedioPagoBean mp = SpringContext.getBean(MediosPagosService.class).getMedioPago(codMedioPagoSeleccionado);
							if (mp == null) {
								mp = MediosPagosService.medioPagoDefecto;
							}
							/* Comprobamos que el medio de pago es GiftCard */
							if (Dispositivos.getInstance().getGiftCard().isMedioPagoGiftCard(codMedioPagoSeleccionado)) {
								usaTarjetaRegalo = Boolean.TRUE;
								procesarPagoTarjetaRegalo(mp, apartado);
							}
							if (!usaTarjetaRegalo) {
								try {
									String medPagoConfigurado = ByLPagosController.COD_MP_PINPAD;
									if (Dispositivos.getInstance().getTarjeta() != null && Dispositivos.getInstance().getTarjeta().getConfiguracion() != null) {
										Map<String, String> mapaConfiguracion = Dispositivos.getInstance().getTarjeta().getConfiguracion().getParametrosConfiguracion();
										if (mapaConfiguracion.get("PAYMENTS") != null) {
											medPagoConfigurado = mapaConfiguracion.get("PAYMENTS");
										}
									}
									if (mp.getCodMedioPago().equals(medPagoConfigurado)) {
										if (Dispositivos.getInstance().getTarjeta() instanceof TefAdyenCloud) {
											((ByLTicketManager) ticketManager).solicitarDevTarjetaApartado(apartado, getStage(), mp, new SalvarTicketCallback(){

												@Override
												public void onSucceeded() {
													/* Primero grabamos el movimiento de forma normal */
													try {

														ByLTicketVentaAbono ticketVenta = (ByLTicketVentaAbono) ticketManager.getTicket();
														DatosRespuestaPagoTarjeta datosRespuestaPagoTarjeta = null;
														if (!ticketVenta.getPagos().isEmpty()) {
															datosRespuestaPagoTarjeta = ticketVenta.getPagos().get(0).getDatosRespuestaPagoTarjeta();
														}

														MedioPagoBean mp = SpringContext.getBean(MediosPagosService.class).getMedioPago(codMedioPagoSeleccionado);
														if (mp == null) {
															mp = MediosPagosService.medioPagoDefecto;
														}
														((ByLApartadosManager) apartadosManager).grabarMovimientoDevolucionTarjeta(apartado, apartado.getSaldoCliente(), mp, datosRespuestaPagoTarjeta);

														/*
														 * Luego generamos el nuevo pago para la Reserva en su tabla
														 * correspondiente
														 */
														ApartadosPagoBean pagoDevolucionCancelacion = apartadosService.crearPagoReservaCancelacion(apartado);
														/*
														 * Lo insertamos también el manager para que en la creación del
														 * XML lo tenga en cuenta
														 */
														apartadosManager.getTicketApartado().getMovimientos().add(pagoDevolucionCancelacion);

														finalizaProcesoCancelacionApartado(apartado, datosRespuestaPagoTarjeta);
													}
													catch (Exception e) {
														String mensajeError = "";
														if (e instanceof ReservaDevolucionCancelacionException) {
															mensajeError = e.getMessage();
														}
														else if (e instanceof TicketsServiceException) {
															mensajeError = I18N.getTexto("Error al realizar el pago con tarjeta pinpad");
														}
														else {
															mensajeError = e.getMessage();
														}
														log.error("cancelarApartado() - " + mensajeError, e);
														VentanaDialogoComponent.crearVentanaAviso(mensajeError, getStage());
													}

												}

												@Override
												public void onFailure(Exception e) {
													log.error("cancelarApartado() - Ha ocurrido un error en la devolución por tarjeta pinpad - " + e.getMessage());
													VentanaDialogoComponent.crearVentanaError(getStage(), I18N.getTexto("Ha ocurrido un error en la devolución por tarjeta pinpad"), e);
												}
											});

										}
									}
									else {
										
										/* Comprobamos si el medio de pago es standalone para abrir la pantalla de añadir la referencia */
										DatosRespuestaPagoTarjeta datosRespuestaStandAlone = comprobarStandAlone(mp.getCodMedioPago());
										/* Si el medio de pago es STANDALONE y no tenemos datos del QR, no podemos seguir con el proceso */
										if (mp.getCodMedioPago().equals(ByLPagosController.COD_MP_PINPAD_ADYEN_STANDALONE) && datosRespuestaStandAlone == null ) {
											VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Se ha cancelado el proceso de cancelación de reserva mediante STANDALONE."), getStage());
											return;
										}
										
										/* Primero grabamos el movimiento de forma normal */
										((ByLApartadosManager) apartadosManager).grabarMovimientoDevolucionTarjeta(apartado, apartado.getSaldoCliente(), mp, datosRespuestaStandAlone);
										
										/* Luego generamos el nuevo pago para la Reserva en su tabla correspondiente */
										ApartadosPagoBean pagoDevolucionCancelacion = apartadosService.crearPagoReservaCancelacion(apartado);
										/*
										 * Lo insertamos también el manager para que en la creación del XML lo tenga en
										 * cuenta
										 */
										apartadosManager.getTicketApartado().getMovimientos().add(pagoDevolucionCancelacion);
										
										finalizaProcesoCancelacionApartado(apartado, datosRespuestaStandAlone);
									}

								}
								catch (Exception e) {
									String mensajeError = "";
									if (e instanceof ReservaDevolucionCancelacionException) {
										mensajeError = e.getMessage();
									}
									else if (e instanceof TicketsServiceException) {
										mensajeError = I18N.getTexto("Error al realizar el grabado del Movimiento de Caja");
									}
									log.error("cancelarApartado() - " + mensajeError, e);
									VentanaDialogoComponent.crearVentanaAviso(mensajeError, getStage());
								}
							}
						}else {
							/* Al ser una cancelación con saldo 0 sólo hay que cancelar las lineas y cambiar el estado del apartado */
							apartadosManager.cargarApartado(apartado);
							finalizaProcesoCancelacionApartado(apartado, null);
						}
					}
					else {
						getDatos().remove(VueltaController.CLAVE_CANCELADO);
					}

				}
			}
		}
	}
	
	protected void finalizaProcesoCancelacionApartado(ApartadosCabeceraBean apartado, DatosRespuestaPagoTarjeta datosRespuestaPagoTarjeta) {
		cancelarLineas(apartado);
		
		DatosRespuestaTarjetaReservaTicket datosRespuestaTarjetaReservaTicket = null;
		if (datosRespuestaPagoTarjeta != null) {
			datosRespuestaTarjetaReservaTicket = parseTarjetaToTarjetaReserva(datosRespuestaPagoTarjeta);
		}

		/*
		 * Ahora pasamos a generar el Ticket con el nuevo pago de la devolución de la cancelación insertado
		 */
		ByLDetalleApartadosController detalle = SpringContext.getBean(ByLDetalleApartadosController.class);
		try {
			detalle.generarTicketReserva(apartadosManager, null, datosRespuestaTarjetaReservaTicket, false, true);
		}
		catch (TicketReservaException e) {
			log.error("finalizaProcesoCancelacionApartado() - " + e.getMessage());
			VentanaDialogoComponent.crearVentanaError(I18N.getTexto(e.getMessage()), getStage());
			return;
		}

		seleccionarSiguienteLinea();
		accionBtBuscar();
	}

	protected void cancelarLineas(ApartadosCabeceraBean apartado) {
		boolean hayLineasVendidas = apartadosManager.cancelarLineas(apartado);
		if (hayLineasVendidas) {
			apartado.setEstadoApartado(ApartadosCabeceraBean.ESTADO_FINALIZADO);
		}
		else {
			apartado.setEstadoApartado(ApartadosCabeceraBean.ESTADO_CANCELADO);
		}
		apartadosManager.cargarApartado(apartado);
		apartadosManager.getTicketApartado().calcularTotales();
		apartadosManager.getTicketApartado().getCabecera().setImporteTotalApartado(apartadosManager.getTicketApartado().getTotalServido());
		apartadosService.actualizarCabeceraApartado(apartado);

	}

	protected void procesarPagoTarjetaRegalo(final MedioPagoBean medioPagoBean, final ApartadosCabeceraBean apartado) {
		log.info("procesarPagoTarjetaRegalo() - Iniciamos el procesamiento de la Tarjeta...");
		try {
			((ByLGiftCard) Dispositivos.getInstance().getGiftCard()).pedirTarjetaRegaloReservas(getStage(), new DispositivoCallback<GiftCardBean>(){

				@Override
				public void onSuccess(GiftCardBean tarjeta) {
					/* Primero consultamos y realizamos comprobaciones de que la Tarjeta es correcta */
					if (tarjeta != null) {
						if (tarjeta.isBaja()) {
							String mensajeAviso = I18N.getTexto("La Tarjeta introducida está dada de Baja");
							log.debug("procesarPagoTarjetaRegalo() - " + mensajeAviso);
							VentanaDialogoComponent.crearVentanaAviso(mensajeAviso, getStage());
						}
						else {
							boolean esMedioPagoCorrectoTipoTarj = Dispositivos.getInstance().getGiftCard().esMedioPago(tarjeta.getCodTipoTarjeta(), medioPagoBean);
							if (!esMedioPagoCorrectoTipoTarj) {
								String mensajeAviso = I18N.getTexto("La Tarjeta introducida no es del tipo permitido para este Medio de Pago");
								log.debug("procesarPagoTarjetaRegalo() - " + mensajeAviso);
								VentanaDialogoComponent.crearVentanaAviso(mensajeAviso, getStage());
								apartado.setEstadoApartado(ApartadosCabeceraBean.ESTADO_DISPONIBLE);
								return;
							}

							ByLGiftCardBean tarjetaRegalo = (ByLGiftCardBean) tarjeta;

							/* Creamos el movimiento para la devolución de la Tarjeta */
							TipoDocumentoBean tipoDocumentoReserva = null;
							Map<Long, TipoDocumentoBean> mapaDocumentos = sesion.getAplicacion().getDocumentos().getDocumentos();
							for (Map.Entry<Long, TipoDocumentoBean> entry : mapaDocumentos.entrySet()) {
								TipoDocumentoBean documento = entry.getValue();
								if (CODIGO_DOCUMENTO_RESERVA.equals(documento.getCodtipodocumento())) {
									tipoDocumentoReserva = documento;
								}
							}

							/* Calculamos la serie del Ticket a partir de los siguientes datos */
							Map<String, String> parametrosContador = new HashMap<>();
							parametrosContador.put("CODEMP", sesion.getAplicacion().getEmpresa().getCodEmpresa());
							parametrosContador.put("CODTIPODOCUMENTO", tipoDocumentoReserva.getCodtipodocumento());
							parametrosContador.put("CODSERIE", sesion.getAplicacion().getTienda().getAlmacenBean().getCodAlmacen());
							parametrosContador.put("CODCAJA", sesion.getSesionCaja().getCajaAbierta().getCodCaja());
							parametrosContador.put("PERIODO", ((new Fecha()).getAño().toString()));
							ContadorBean ticketContador = null;
							try {
								/* Generamos el ID del apartado a partir del contador */
								ticketContador = contadoresService.obtenerContador(tipoDocumentoReserva.getIdContador(), parametrosContador, apartado.getUidActividad());
								/* Generamos la serie del Ticket */
							}
							catch (ContadorServiceException e) {
								String mensajeError = I18N.getTexto("Error al consultar el contador para el Ticket de Reserva");
								log.error("crearCabeceraTicketReserva() - " + mensajeError + " - " + e.getMessage());
								VentanaDialogoComponent.crearVentanaError(mensajeError, getStage());
								apartado.setEstadoApartado(ApartadosCabeceraBean.ESTADO_DISPONIBLE);
								return;
							}

							if(apartadosManager.getTicketApartado() == null){
								apartadosManager.cargarApartado(apartado);
							}
							
							Integer estado = tarjetaRegalo.getEstado();
							EnviarMovimientoGiftCardBean enviarMovimiento = new EnviarMovimientoGiftCardBean();
							enviarMovimiento.setSalida(0D);
							
							// INICIO Incidencia #82
							BigDecimal totalAbonado = apartadosManager.getTicketApartado().getTotalAbonado();
							BigDecimal totalServido = apartadosManager.getTicketApartado().getTotalServido();
							BigDecimal saldoCliente = totalAbonado.subtract(totalServido);
							enviarMovimiento.setEntrada(saldoCliente.doubleValue());
							// FIN Incidencia #82
							
							enviarMovimiento.setNumeroTarjeta(tarjetaRegalo.getNumTarjetaRegalo());
							enviarMovimiento.setTypeCode(estado);
							
							// INICIO Incidencia #71
							String idReserva = StringUtils.leftPad(apartado.getIdApartado().toString(), 6, "0");
							idReserva = ticketContador.getConfigContador().getValorDivisor3() + idReserva;
							enviarMovimiento.setDocumentNumber(idReserva);
							enviarMovimiento.setSourceDocumentNumber(idReserva);
							// FIN Incidencia #71
							
							try {
								/* Realizamos la operación para la Devolución */
								SpringContext.getBean(ByLGiftCard.class).realizarDevolucionTarjetaRestReservas(enviarMovimiento);
							}
							catch (RestException | RestHttpException e) {
								String mensajeError = I18N.getTexto("Se ha producido un error al realizar la operación con la Tarjeta");
								log.error("procesarPagoTarjetaRegalo() - " + mensajeError + " - " + e.getMessage(), e);
								VentanaDialogoComponent.crearVentanaError(mensajeError, getStage());
								apartado.setEstadoApartado(ApartadosCabeceraBean.ESTADO_DISPONIBLE);
								return;
							}

							try {
								/* Primero grabamos el movimiento de forma normal */
								grabarMovimientoDevolucionReservas(apartado, apartado.getSaldoCliente(), medioPagoBean, tarjeta);
								/* Luego generamos el nuevo pago para la Reserva en su tabla correspondiente */
								ApartadosPagoBean pagoDevolucionCancelacion = apartadosService.crearPagoReservaCancelacion(apartado);
								/* Lo insertamos también el manager para que en la creación del XML lo tenga en cuenta */
								apartadosManager.getTicketApartado().getMovimientos().add(pagoDevolucionCancelacion);
							}
							catch (Exception e) {
								String mensajeError = "";
								if (e instanceof ReservaDevolucionCancelacionException) {
									mensajeError = e.getMessage();
								}
								else if (e instanceof TicketsServiceException) {
									mensajeError = I18N.getTexto("Error al realizar el grabado del Movimiento de Caja");
								}
								log.error("cancelarApartado() - " + mensajeError);
								VentanaDialogoComponent.crearVentanaAviso(mensajeError, getStage());
								apartado.setEstadoApartado(ApartadosCabeceraBean.ESTADO_DISPONIBLE);
								return;
							}

							/*
							 * Ahora pasamos a generar el Ticket con el nuevo pago de la devolución de la cancelación
							 * insertado
							 */
							ByLDetalleApartadosController detalle = SpringContext.getBean(ByLDetalleApartadosController.class);
							try {
								
								// INICIO Incidencia #88
								ReservasPagoGiftCardBean pagoGiftCardReserva = new ReservasPagoGiftCardBean();
								pagoGiftCardReserva.setSaldo(tarjetaRegalo.getSaldo());
								pagoGiftCardReserva.setSaldoProvisional(tarjetaRegalo.getSaldoProvisional());
								pagoGiftCardReserva.setImportePago(saldoCliente.negate());
								pagoGiftCardReserva.setNumeroTarjeta(tarjetaRegalo.getNumTarjetaRegalo());
								pagoGiftCardReserva.setUidTransaccion(tarjetaRegalo.getUidTransaccion());

								getDatos().put(NuevoPagoApartadoController.GIFCARD_PAGO_RESERVA, pagoGiftCardReserva);
								// FIN Incidencia #88

								detalle.generarTicketReserva(apartadosManager, ticketContador, null, true, false);
							}
							catch (TicketReservaException e) {
								log.error("cancelarApartado() - " + e.getMessage());
								VentanaDialogoComponent.crearVentanaError(I18N.getTexto(e.getMessage()), getStage());
								apartado.setEstadoApartado(ApartadosCabeceraBean.ESTADO_DISPONIBLE);
								return;
							}

							// INICIO incidencia #70
							cancelarLineas(apartado);
							// FIN incidencia #70

							seleccionarSiguienteLinea();
							accionBtBuscar();
						}
					}
				}

				@Override
				public void onFailure(Throwable e) {
					String mensajeError = null;
					if (e instanceof TarjetaRegaloException) {
						mensajeError = I18N.getTexto("Se ha cancelado la consulta de la Tarjeta");
						log.info("procesarPagoTarjetaRegalo() - " + mensajeError);
						VentanaDialogoComponent.crearVentanaAviso(mensajeError, getStage());
					}
					else {
						mensajeError = "Se ha producido un error al pedir la Tarjeta";
						log.error("procesarPagoTarjetaRegalo() - " + mensajeError + " - " + e.getMessage(), e);
					}
					apartado.setEstadoApartado(ApartadosCabeceraBean.ESTADO_DISPONIBLE);
				}
			});
			log.info("procesarPagoTarjetaRegalo() - Finalizado el procesamiento de la Tarjeta");
		}
		catch (Exception e) {
			String mensajeError = I18N.getTexto("Se ha producido un error al procesar la Tarjeta para la Devolución");
			log.error("procesarPagoTarjetaRegalo() - " + mensajeError, e);
			VentanaDialogoComponent.crearVentanaAviso(mensajeError, getStage());
			apartado.setEstadoApartado(ApartadosCabeceraBean.ESTADO_DISPONIBLE);
		}
	}

	@SuppressWarnings("rawtypes")
	public void grabarMovimientoDevolucionReservas(ApartadosCabeceraBean cabeceraApartado, BigDecimal importe, MedioPagoBean medioPago, GiftCardBean tarjeta) throws TicketsServiceException {
		CajaMovimientoBean detalleCaja = new CajaMovimientoBean();

		detalleCaja.setFecha(new Date());
		detalleCaja.setAbono(importe);
		detalleCaja.setCargo(BigDecimal.ZERO);
		detalleCaja.setCodConceptoMovimiento(ByLApartadosService.CODCONCEPTO_MOV_DEVOLUCION_RESERVA);
		detalleCaja.setConcepto("Apartado nº " + cabeceraApartado.getIdApartado());
		detalleCaja.setCodMedioPago(medioPago.getCodMedioPago());
		detalleCaja.setDesMedioPago(medioPago.getDesMedioPago());

		apartadosService.crearMovimientoDevolucion(cabeceraApartado, medioPago, detalleCaja);

		Long idTipoDocumento = null;

		Map<String, Object> mapaParametros = new HashMap<String, Object>();
		try {
			TicketManager ticketManager = SpringContext.getBean(TicketManager.class);
			ticketManager.nuevoTicket();
			Ticket ticketPrincipal = (Ticket) ticketManager.getTicket();
			((Ticket) ticketPrincipal).getCabecera().setTienda(sesion.getAplicacion().getTienda());
			((Ticket) ticketPrincipal).getCabecera().setEmpresa(sesion.getAplicacion().getEmpresa());
			idTipoDocumento = ticketPrincipal.getCabecera().getTipoDocumento();
			mapaParametros.put("ticket", ticketPrincipal);
		}
		catch (Exception e) {
			log.error("grabarMovimientoDevolucionReservas() - Ha habido un error al crear el ticket para la impresión: " + e.getMessage());
		}

		mapaParametros.put("apartado", cabeceraApartado);
		mapaParametros.put("importe", FormatUtil.getInstance().formateaImporte(importe));
		mapaParametros.put("fecha", FormatUtil.getInstance().formateaFechaHora(detalleCaja.getFecha()));
		mapaParametros.put("empresa", sesion.getAplicacion().getEmpresa());
		mapaParametros.put("cajero", sesion.getSesionUsuario().getUsuario().getDesusuario());
		mapaParametros.put("pago", detalleCaja);
		mapaParametros.put("tarjeta", tarjeta.getNumTarjetaRegalo());
		if (mediosPagosService.isCodMedioPagoVale(medioPago.getCodMedioPago(), idTipoDocumento)) {
			mapaParametros.put("importeVale", importe);
		}
		try {
			ServicioImpresion.imprimir("informe_apartado", mapaParametros);
		}
		catch (DeviceException e) {
			log.error("grabarMovimientoDevolucionReservas() - " + e.getClass().getName() + " - " + e.getLocalizedMessage(), e);
		}
	}

	public void finalizarApartado() {
		log.trace("finalizarApartado() - Iniciamos la acción para finalizar una Reserva...");
		boolean bLineasFinalizadas = false;

		final LineaApartadoGui lineaApartado = getLineaSeleccionada();
		if (lineaApartado != null) {
			final ApartadosCabeceraBean detalleApartado = lineaApartado.getApartado();
			if (detalleApartado.getEstadoApartado() != ApartadosCabeceraBean.ESTADO_DISPONIBLE) {
				String mensajeAviso = I18N.getTexto("La Reserva no está disponible para su venta");
				log.info("finalizarApartado() - " + mensajeAviso);
				VentanaDialogoComponent.crearVentanaAviso(mensajeAviso, getStage());
			}
			else {
				apartadosManager.cargarApartado(detalleApartado);

				if (apartadosManager.getTicketApartado().getTotalPendiente().compareTo(BigDecimal.ZERO) > 0) {
					String mensajeAviso = I18N.getTexto("El coste de la Reserva no está no está cubierto");
					log.info("finalizarApartado() - " + mensajeAviso);
					VentanaDialogoComponent.crearVentanaAviso(mensajeAviso, getStage());
					return;
				}
				String mensajeConfirmacion = I18N.getTexto("Se va a proceder a finalizar el apartado. ¿Desea continuar?");
				if (VentanaDialogoComponent.crearVentanaConfirmacion(mensajeConfirmacion, getStage())) {

					apartadosManager.cargarApartado(detalleApartado);

					final List<ApartadosDetalleBean> articulos = new ArrayList<>();

					for (ApartadosDetalleBean articulo : apartadosManager.getTicketApartado().getArticulos()) {
						if (articulo.getEstadoLineaApartado() == ApartadosCabeceraBean.ESTADO_DISPONIBLE) {
							articulos.add(articulo);
						}
						else {
							if (articulo.getEstadoLineaApartado() == ApartadosCabeceraBean.ESTADO_FINALIZADO) {
								bLineasFinalizadas = true;
							}
						}
					}

					apartadosManager.getTicketApartado().calcularTotales();
					final BigDecimal importeTotal = apartadosManager.getTicketApartado().getTotalServido().add(apartadosManager.getTicketApartado().getImporteTotal());

					for (ApartadosDetalleBean detalleApartadoBean : apartadosManager.getTicketApartado().getArticulos()) {
						if (detalleApartadoBean.getEstadoLineaApartado() == ApartadosCabeceraBean.ESTADO_DISPONIBLE) {
							try {
								ArticuloBean articuloBean = articulosService.consultarArticulo(detalleApartadoBean.getCodart());
								if (articuloBean.getNumerosSerie()) {
									ticketManager.nuevoTicket();
									ticketManager.getTicket().setCliente(apartadosManager.getCliente());
									LineaTicket lineaTicket = ticketManager.nuevaLineaArticulo(detalleApartadoBean.getCodart(), detalleApartadoBean.getDesglose1(), detalleApartadoBean.getDesglose2(),
									        detalleApartadoBean.getCantidad(), null);

									asignarNumerosSerie(lineaTicket, detalleApartadoBean);

									if (detalleApartadoBean.getNumerosSerie() == null
									        || (detalleApartadoBean.getNumerosSerie() != null && detalleApartadoBean.getNumerosSerie().isEmpty())
									        || (detalleApartadoBean.getNumerosSerie() != null && detalleApartadoBean.getNumerosSerie().size() < detalleApartadoBean.getCantidad()
									                .setScale(0, RoundingMode.HALF_UP).abs().intValue())) {
										String mensajeAviso = I18N.getTexto("Debe indicar todos los números de serie para continuar");
										log.debug("finalizarApartado() - " + mensajeAviso);
										VentanaDialogoComponent.crearVentanaAviso(mensajeAviso, getStage());
										return;
									}
								}
							}
							catch (Exception e) {
								String mensajeError = I18N.getTexto("Se ha producido un error al procesar los números de serie");
								log.error("finalizarApartado() - " + mensajeError + " - " + e.getMessage());
								VentanaDialogoComponent.crearVentanaError(getStage(), mensajeError, e);
								return;
							}
						}
					}
					try {
						ticketManager.generarVentaDeApartados(apartadosManager.getTicketApartado().getArticulos(), apartadosManager.getCliente(), detalleApartado);

						FidelizacionBean tarjetaFidelizado = new FidelizacionBean();
						tarjetaFidelizado.setNumTarjetaFidelizado(apartadosManager.getTicketApartado().getCabecera().getTarjetaFidelizacion());
						ticketManager.getTicket().getCabecera().setDatosFidelizado(tarjetaFidelizado);

						if (articulos.isEmpty()) {
							/*
							 * Si no hay lineas para servir y no hay ninguna servida el estado del apartado pasará a
							 * cancelado
							 */
							if (bLineasFinalizadas) {
								apartadosManager.getTicketApartado().getCabecera().setEstadoApartado(ApartadosCabeceraBean.ESTADO_FINALIZADO);
							}
							else {
								apartadosManager.getTicketApartado().getCabecera().setEstadoApartado(ApartadosCabeceraBean.ESTADO_CANCELADO);
							}

							apartadosManager.registrarVentaApartado(ticketManager.getTicket().getTotales().getTotalAPagar());

							ticketManager.finalizarTicket();

							detalleApartado.setImporteTotalApartado(importeTotal);
							apartadosService.actualizarCabeceraApartado(detalleApartado);

							if (detalleApartado.getSaldoCliente().compareTo(BigDecimal.ZERO) > 0) {
								getApplication().getMainView().showModalCentered(VueltaView.class, getDatos(), getStage());
								String codMedioPagoSeleccionado = (String) getDatos().remove(VueltaController.CLAVE_PARAMETRO_SALIDA_VUELTA);
								MedioPagoBean mp = SpringContext.getBean(MediosPagosService.class).getMedioPago(codMedioPagoSeleccionado);
								if (mp == null) {
									mp = MediosPagosService.medioPagoDefecto;
								}
								try {
									apartadosManager.grabarMovimientoDevolucion(detalleApartado, detalleApartado.getSaldoCliente(), mp);
								}
								catch (TicketsServiceException e) {
									String mensajeError = I18N.getTexto("Se ha producido un error al grabar el Movimiento de Caja");
									log.error("finalizarApartado() - " + mensajeError + " - " + e.getMessage(), e);
									VentanaDialogoComponent.crearVentanaAviso(mensajeError, getStage());
								}
							}
							seleccionarSiguienteLinea();
							accionBtBuscar();
						}
						else {
							ticketManager.salvarTicket(getStage(), new SalvarTicketCallback(){

								@Override
								public void onSucceeded() {
									apartadosManager.getTicketApartado().getCabecera().setEstadoApartado(ApartadosCabeceraBean.ESTADO_FINALIZADO);
									apartadosManager.registrarVentaApartado(ticketManager.getTicket().getTotales().getTotalAPagar());
									apartadosManager.actualizarEstadoLineasVendidas(ticketManager.getTicket().getCabecera().getUidTicket(), articulos);

									if (ticketManager.getTicket().getTotales().getTotalAPagar().compareTo(BigDecimal.ZERO) > 0) {
										asignarDatosFacturacion();

										Map<String, Object> mapaParametros = new HashMap<String, Object>();
										mapaParametros.put("ticket", ticketManager.getTicket());
										mapaParametros.put("urlQR", variablesServices.getVariableAsString("TPV.URL_VISOR_DOCUMENTOS"));
										mapaParametros.put("apartado", lineaApartado.getApartado().getIdApartado().toString());

										try {
											ServicioImpresion.imprimir(ticketManager.getTicket().getCabecera().getFormatoImpresion(), mapaParametros);
										}
										catch (DeviceException e) {
											String mensajeError = "Se ha producido un error al imprimir el Documento de Venta";
											log.error("finalizarApartado() - " + mensajeError + " - " + e.getMessage(), e);
										}
									}
									ticketManager.finalizarTicket();

									detalleApartado.setImporteTotalApartado(importeTotal);
									apartadosService.actualizarCabeceraApartado(detalleApartado);

									if (detalleApartado.getSaldoCliente().compareTo(BigDecimal.ZERO) > 0) {
										getApplication().getMainView().showModalCentered(VueltaView.class, getDatos(), getStage());
										String codMedioPagoSeleccionado = (String) getDatos().remove(VueltaController.CLAVE_PARAMETRO_SALIDA_VUELTA);
										MedioPagoBean mp = SpringContext.getBean(MediosPagosService.class).getMedioPago(codMedioPagoSeleccionado);
										if (mp == null) {
											mp = MediosPagosService.medioPagoDefecto;
										}
										try {
											apartadosManager.grabarMovimientoDevolucion(detalleApartado, detalleApartado.getSaldoCliente(), mp);
										}
										catch (TicketsServiceException e) {
											String mensajeError = I18N.getTexto("Se ha producido un error al grabar el Movimiento de Caja");
											log.error("finalizarApartado() - " + mensajeError + " - " + e.getMessage(), e);
											VentanaDialogoComponent.crearVentanaAviso(mensajeError, getStage());
										}
									}

									seleccionarSiguienteLinea();
									accionBtBuscar();
								}

								@Override
								public void onFailure(Exception e) {
									String mensajeError = I18N.getTexto("Se ha producido un error al salvar el Ticket");
									log.error("finalizarApartado() - " + mensajeError + " - " + e.getMessage());
									VentanaDialogoComponent.crearVentanaError(getStage(), mensajeError, e);
								}
							});
						}
					}
					catch (Exception e) {
						String mensajeError = I18N.getTexto("Se ha producido un error al procesar la Venta");
						log.error("finalizarApartado() - " + mensajeError + " - " + e.getMessage());
						VentanaDialogoComponent.crearVentanaError(getStage(), mensajeError, e);
					}
				}
			}
		}
		else {
			String mensajeInfo = I18N.getTexto("Debe seleccionar una Reserva para poder Finalizarla");
			log.info("finalizarApartado() -" + mensajeInfo);
			VentanaDialogoComponent.crearVentanaAviso(mensajeInfo, getStage());
		}
	}
	
	
	private DatosRespuestaTarjetaReservaTicket parseTarjetaToTarjetaReserva(DatosRespuestaPagoTarjeta datosRespuestaPagoTarjeta) {
		DatosRespuestaTarjetaReservaTicket datosRespuestaTarjetaReservaTicket = new DatosRespuestaTarjetaReservaTicket();

		datosRespuestaTarjetaReservaTicket.setCodResult(datosRespuestaPagoTarjeta.getCodResult());
		datosRespuestaTarjetaReservaTicket.setMsgRespuesta(datosRespuestaPagoTarjeta.getMsgRespuesta());
		datosRespuestaTarjetaReservaTicket.setTicket(datosRespuestaPagoTarjeta.getTicket());
		datosRespuestaTarjetaReservaTicket.setTipoOp(datosRespuestaPagoTarjeta.getTipoOp());
		datosRespuestaTarjetaReservaTicket.setImporte(datosRespuestaPagoTarjeta.getImporte());
		datosRespuestaTarjetaReservaTicket.setAID(datosRespuestaPagoTarjeta.getAID());
		datosRespuestaTarjetaReservaTicket.setTarjeta(datosRespuestaPagoTarjeta.getTarjeta());
		datosRespuestaTarjetaReservaTicket.setMarcaTarjeta(datosRespuestaPagoTarjeta.getMarcaTarjeta());
		datosRespuestaTarjetaReservaTicket.setDescripcionMarcaTarjeta(datosRespuestaPagoTarjeta.getDescripcionMarcaTarjeta());
		datosRespuestaTarjetaReservaTicket.setTitular(datosRespuestaPagoTarjeta.getTitular());
		datosRespuestaTarjetaReservaTicket.setComercio(datosRespuestaPagoTarjeta.getComercio());
		datosRespuestaTarjetaReservaTicket.setTerminal(datosRespuestaPagoTarjeta.getTerminal());
		datosRespuestaTarjetaReservaTicket.setDescBanco(datosRespuestaPagoTarjeta.getDescBanco());
		datosRespuestaTarjetaReservaTicket.setARC(datosRespuestaPagoTarjeta.getARC());
		datosRespuestaTarjetaReservaTicket.setNumOperacionBanco(datosRespuestaPagoTarjeta.getNumOperacionBanco());
		datosRespuestaTarjetaReservaTicket.setCodAutorizacion(datosRespuestaPagoTarjeta.getCodAutorizacion());
		datosRespuestaTarjetaReservaTicket.setMoneda(datosRespuestaPagoTarjeta.getMoneda());
		datosRespuestaTarjetaReservaTicket.setNumOperacion(datosRespuestaPagoTarjeta.getNumOperacion());
		datosRespuestaTarjetaReservaTicket.setTipoLectura(datosRespuestaPagoTarjeta.getTipoLectura());
		datosRespuestaTarjetaReservaTicket.setVerificacion(datosRespuestaPagoTarjeta.getVerificacion());
		datosRespuestaTarjetaReservaTicket.setNumTransaccion(datosRespuestaPagoTarjeta.getNumTransaccion());

		datosRespuestaTarjetaReservaTicket.setPos(datosRespuestaPagoTarjeta.getPos());
		datosRespuestaTarjetaReservaTicket.setCodigoCentro(datosRespuestaPagoTarjeta.getCodigoCentro());
		datosRespuestaTarjetaReservaTicket.setCodigoTienda(datosRespuestaPagoTarjeta.getCodigoTienda());
		datosRespuestaTarjetaReservaTicket.setCodigoCajera(datosRespuestaPagoTarjeta.getCodigoCajera());
		datosRespuestaTarjetaReservaTicket.setTipoTransaccion(datosRespuestaPagoTarjeta.getTipoTransaccion());
		datosRespuestaTarjetaReservaTicket.setFuc(datosRespuestaPagoTarjeta.getFuc());
		datosRespuestaTarjetaReservaTicket.setNombreEntidad(datosRespuestaPagoTarjeta.getNombreEntidad());
		datosRespuestaTarjetaReservaTicket.setPAN(datosRespuestaPagoTarjeta.getPAN());
		datosRespuestaTarjetaReservaTicket.setP23(datosRespuestaPagoTarjeta.getP23());
		datosRespuestaTarjetaReservaTicket.setApplicationLabel(datosRespuestaPagoTarjeta.getApplicationLabel());
		datosRespuestaTarjetaReservaTicket.setFechaTransaccion(datosRespuestaPagoTarjeta.getFechaTransaccion());
		datosRespuestaTarjetaReservaTicket.setEstablecimiento(datosRespuestaPagoTarjeta.getEstablecimiento());
		datosRespuestaTarjetaReservaTicket.setDireccionEstablecimiento(datosRespuestaPagoTarjeta.getDireccionEstablecimiento());
		datosRespuestaTarjetaReservaTicket.setTerminalId(datosRespuestaPagoTarjeta.getTerminalId());
		datosRespuestaTarjetaReservaTicket.setAuthMode(datosRespuestaPagoTarjeta.getAuthMode());
		datosRespuestaTarjetaReservaTicket.setContactLess(datosRespuestaPagoTarjeta.getContactLess());
		datosRespuestaTarjetaReservaTicket.setNombredf(datosRespuestaPagoTarjeta.getNombredf());

		datosRespuestaTarjetaReservaTicket.setPedirFirma(datosRespuestaPagoTarjeta.getPedirFirma());
		datosRespuestaTarjetaReservaTicket.setAdicionales(datosRespuestaPagoTarjeta.getAdicionales());

		datosRespuestaTarjetaReservaTicket.setImporteDivisa(datosRespuestaPagoTarjeta.getImporteDivisa());
		datosRespuestaTarjetaReservaTicket.setCodigoDivisa(datosRespuestaPagoTarjeta.getCodigoDivisa());
		datosRespuestaTarjetaReservaTicket.setExchangeRate(datosRespuestaPagoTarjeta.getExchangeRate());
		datosRespuestaTarjetaReservaTicket.setComision(datosRespuestaPagoTarjeta.getComision());
		datosRespuestaTarjetaReservaTicket.setDCC(datosRespuestaPagoTarjeta.isDCC());

		return datosRespuestaTarjetaReservaTicket;
	}

	@SuppressWarnings("deprecation")
	private DatosRespuestaPagoTarjeta comprobarStandAlone(String codMedPago) {
		DatosRespuestaPagoTarjeta datosRespuesta = null;

		if (codMedPago.equals(ByLPagosController.COD_MP_PINPAD_ADYEN_STANDALONE)) {
			HashMap<String, Object> parametrosReferencia = new HashMap<String, Object>();
			getApplication().getMainView().showModalCentered(ReferenciaStandaloneView.class, parametrosReferencia, this.getStage());

			if (parametrosReferencia.get(ReferenciaStandaloneController.PARAMETRO_NUM_REFERENCIA) != null) {
				String numReferencia = (String) parametrosReferencia.get(ReferenciaStandaloneController.PARAMETRO_NUM_REFERENCIA);
				JsonElement root = new JsonParser().parse(numReferencia);
				String numTransaccion = root.getAsJsonObject().get("TransactionID").getAsString();
				String timeStamp = root.getAsJsonObject().get("TimeStamp").getAsString();

				datosRespuesta = new DatosRespuestaPagoTarjeta();
				datosRespuesta.setFechaTransaccion(timeStamp);
				datosRespuesta.setNumTransaccion(numTransaccion);
			}
		}

		return datosRespuesta;
	}

}
