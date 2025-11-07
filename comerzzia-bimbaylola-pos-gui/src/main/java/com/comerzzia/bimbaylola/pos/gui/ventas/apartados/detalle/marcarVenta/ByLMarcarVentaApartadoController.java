package com.comerzzia.bimbaylola.pos.gui.ventas.apartados.detalle.marcarVenta;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.ByL.backoffice.persistencia.fidelizacion.fidelizados.FidelizadosBean;
import com.comerzzia.ByL.backoffice.rest.client.fidelizados.ByLFidelizadosRest;
import com.comerzzia.bimbaylola.pos.devices.impresoras.fiscal.IFiscalPrinter;
import com.comerzzia.bimbaylola.pos.dispositivo.impresora.epsontm30.EpsonTM30;
import com.comerzzia.bimbaylola.pos.gui.pagos.ByLPagosController;
import com.comerzzia.bimbaylola.pos.gui.pagos.email.ByLEmailView;
import com.comerzzia.bimbaylola.pos.gui.ventas.apartados.detalle.ByLDetalleApartadosController;
import com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.ByLFacturacionArticulosController;
import com.comerzzia.bimbaylola.pos.services.cajas.ByLCajasService;
import com.comerzzia.bimbaylola.pos.services.epsontse.EposOutput;
import com.comerzzia.bimbaylola.pos.services.epsontse.EpsonTSEService;
import com.comerzzia.bimbaylola.pos.services.reservas.exception.TicketReservaException;
import com.comerzzia.bimbaylola.pos.services.ticket.ByLLineaTicket;
import com.comerzzia.bimbaylola.pos.services.ticket.aparcados.reservas.LineaReservaTicket;
import com.comerzzia.bimbaylola.pos.services.ticket.aparcados.reservas.TicketReserva;
import com.comerzzia.bimbaylola.pos.services.ticket.cabecera.ByLCabeceraTicket;
import com.comerzzia.pos.core.dispositivos.Dispositivos;
import com.comerzzia.pos.core.dispositivos.dispositivo.impresora.IPrinter;
import com.comerzzia.pos.core.gui.POSApplication;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.gui.ventas.apartados.detalle.marcarVenta.MarcarVentaApartadoController;
import com.comerzzia.pos.gui.ventas.tickets.TicketManager;
import com.comerzzia.pos.gui.ventas.tickets.TicketManager.SalvarTicketCallback;
import com.comerzzia.pos.persistence.apartados.detalle.ApartadosDetalleBean;
import com.comerzzia.pos.persistence.articulos.ArticuloBean;
import com.comerzzia.pos.persistence.core.documentos.propiedades.PropiedadDocumentoBean;
import com.comerzzia.pos.persistence.core.documentos.tipos.TipoDocumentoBean;
import com.comerzzia.pos.persistence.fidelizacion.FidelizacionBean;
import com.comerzzia.pos.services.articulos.ArticulosService;
import com.comerzzia.pos.services.core.documentos.DocumentoException;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.services.core.variables.VariablesServices;
import com.comerzzia.pos.services.ticket.TicketVenta;
import com.comerzzia.pos.services.ticket.cabecera.DatosDocumentoOrigenTicket;
import com.comerzzia.pos.services.ticket.lineas.DocumentoOrigen;
import com.comerzzia.pos.services.ticket.lineas.LineaTicket;
import com.comerzzia.pos.services.ticket.lineas.LineaTicketException;
import com.comerzzia.pos.services.ticket.pagos.PagoTicket;
import com.comerzzia.pos.servicios.impresion.ServicioImpresion;
import com.comerzzia.pos.util.bigdecimal.BigDecimalUtil;
import com.comerzzia.pos.util.config.SpringContext;
import com.comerzzia.pos.util.i18n.I18N;
import com.comerzzia.rest.client.exceptions.RestException;
import com.comerzzia.rest.client.exceptions.RestHttpException;
import com.comerzzia.rest.client.fidelizados.ConsultarFidelizadoRequestRest;

@Component
@Primary
public class ByLMarcarVentaApartadoController extends MarcarVentaApartadoController{

	@Autowired
	protected ByLCajasService cajasService;
	@Autowired
	private ArticulosService articulosService;
	@Autowired
	private VariablesServices variablesServices;
	@Autowired
	protected EpsonTSEService epsonTSEService;
	
	@Autowired
	private Sesion sesion;
	
	private final String ACCION_EMAIL = "ACCION_EMAIL";
	private final String CORREO = "CORREO";
	private final String AMBOS = "AMBOS";
	private static String accionEmail;
	
	@SuppressWarnings("unchecked")
	public void accionAceptar() {
		if (cajasService.comprobarCajaMaster()) {
			List<ApartadosDetalleBean> articulosVenta = new ArrayList<ApartadosDetalleBean>();
			BigDecimal importeVenta = BigDecimal.ZERO;
			TicketReserva ticketReservaGenerado = null;
			try {
				/* Generamos el Ticket de la Reserva a partir de los datos de TicketManager y de ApartadosManager */
				ByLDetalleApartadosController detalleApartado = SpringContext.getBean(ByLDetalleApartadosController.class);
				ticketReservaGenerado = detalleApartado.generarTicketReserva(null, null, null, false, false);
			}
			catch (TicketReservaException e) {
				log.error("vender() - " + e.getMessage());
				VentanaDialogoComponent.crearVentanaError(I18N.getTexto(e.getMessage()), getStage());
				return;
			}
			for (ApartadosDetalleBean linea : articulosApartado) {
				if (linea.isLineaSeleccionadaVenta()) {
					try {
						ArticuloBean articulo = articulosService.consultarArticulo(linea.getCodart());
						if (articulo.getNumerosSerie()) {
							ticketManager.nuevoTicket();
							ticketManager.getTicket().setCliente(apartadosManager.getCliente());

							if (StringUtils.isNotBlank(apartadosManager.getTicketApartado().getCabecera().getTarjetaFidelizacion())) {
								FidelizacionBean fidelizadoReserva = new FidelizacionBean();
								fidelizadoReserva.setNumTarjetaFidelizado(apartadosManager.getTicketApartado().getCabecera().getTarjetaFidelizacion());
								fidelizadoReserva.setNombre(apartadosManager.getCliente().getDesCliente());

								ticketManager.getTicket().getCabecera().setDatosFidelizado(fidelizadoReserva);
							}
							/* Generamos los datos del Documento Origen de la Reserva */
							DatosDocumentoOrigenTicket datosOrigenReserva = new DatosDocumentoOrigenTicket();
							datosOrigenReserva.setUidTicket(ticketReservaGenerado.getCabecera().getUidApartado());
							datosOrigenReserva.setSerie(ticketReservaGenerado.getCabecera().getSerieTicket());
							datosOrigenReserva.setCodTicket(ticketReservaGenerado.getCabecera().getIdReserva());
							/* Valor del contador de ID_Reserva antes de insertar delante la serie */
							String idReserva = datosOrigenReserva.getCodTicket();
							idReserva = idReserva.substring((idReserva.length() - 6), idReserva.length());
							int cadenaResultadoInt = Integer.parseInt(idReserva);
							datosOrigenReserva.setNumFactura(Long.valueOf(cadenaResultadoInt));
							datosOrigenReserva.setCaja(null);
							datosOrigenReserva.setTienda(ticketReservaGenerado.getCabecera().getTienda().getCodAlmacen());
							datosOrigenReserva.setIdTipoDoc(Long.valueOf(ticketReservaGenerado.getCabecera().getTipoDocumento()));
							datosOrigenReserva.setCodTipoDoc(ticketReservaGenerado.getCabecera().getCodigoTipoDocumento());
							datosOrigenReserva.setDesTipoDoc(ticketReservaGenerado.getCabecera().getDescripcionTipoDocumento());
							datosOrigenReserva.setFecha(ticketReservaGenerado.getCabecera().getFecha());
							ticketManager.getTicketOrigen().getCabecera().setDatosDocOrigen(datosOrigenReserva);

							for (LineaTicket lineaTicket : (List<LineaTicket>) ticketManager.getTicket().getLineas()) {
								for (LineaReservaTicket lineaReserva : ticketReservaGenerado.getLineas()) {
									if (lineaTicket.getIdLinea().equals(lineaReserva.getIdLinea())) {
										DocumentoOrigen docOriDetalle = new DocumentoOrigen();
										docOriDetalle.setUidDocumentoOrigen(ticketReservaGenerado.getCabecera().getUidApartado());
										docOriDetalle.setIdLineaDocumentoOrigen(lineaReserva.getIdLinea());
										lineaTicket.setDocumentoOrigen(docOriDetalle);
									}
								}
							}

							LineaTicket lineaTicket = ticketManager.nuevaLineaArticulo(linea.getCodart(), linea.getDesglose1(), linea.getDesglose2(), linea.getCantidad(), linea.getLinea());
							asignarNumerosSerie(lineaTicket, linea);

							if (lineaTicket.getNumerosSerie() == null || (lineaTicket.getNumerosSerie() != null && lineaTicket.getNumerosSerie().isEmpty())
							        || (lineaTicket.getNumerosSerie() != null && lineaTicket.getNumerosSerie().size() < lineaTicket.getCantidad().setScale(0, RoundingMode.HALF_UP).abs().intValue())) {
								String mensajeAviso = I18N.getTexto("Debe indicar los números de serie para continuar");
								log.debug("accionAceptar() - " + mensajeAviso);
								VentanaDialogoComponent.crearVentanaAviso(mensajeAviso, getStage());
								return;
							}
						}
					}
					catch (Exception e) {
						String mensajeError = I18N.getTexto("Ha ocurrido un error al procesar los números de serie");
						log.error("accionAceptar() - " + mensajeError + " - " + e.getMessage(), e);
						VentanaDialogoComponent.crearVentanaError(getStage(), mensajeError, e);
					}
					articulosVenta.add(linea);
					importeVenta = importeVenta.add(linea.getImporteTotal());
				}
			}
			if (articulosVenta.isEmpty()) {
				String mensajeAviso = I18N.getTexto("Debe seleccionar al menos un artículo para la venta");
				log.debug("accionAceptar() - " + mensajeAviso);
				VentanaDialogoComponent.crearVentanaAviso(mensajeAviso, getStage());
			}
			else {
				if (importeVenta.compareTo(apartadosManager.getTicketApartado().getCabecera().getSaldoCliente()) > 0) {
					String mensajeAviso = I18N.getTexto("El saldo disponible del cliente es inferior al total de la venta");
					log.debug("accionAceptar() - " + mensajeAviso);
					VentanaDialogoComponent.crearVentanaAviso(mensajeAviso, getStage());
				}
				else {
					try {
						/* Funcionalidad Epson TM-m30 TSE */
						if (Dispositivos.getInstance().getImpresora1() instanceof EpsonTM30) {
							if (Dispositivos.getInstance().getImpresora1().getConfiguracion().getNombreConexion().equals(EpsonTSEService.NOMBRE_CONEXION_TSE)) {
								Boolean correcto = tseStartTransaction(ticketManager);
								if (!correcto) {
									Boolean resultado = VentanaDialogoComponent.crearVentanaConfirmacion(I18N.getTexto("No se ha podido realizar la conexión con el TSE, ¿Desea continuar sin TSE?"),
									        getStage());
									if (!resultado) {
										return;
									}
								}
							}
						}

						ticketManager.generarVentaDeApartados(articulosVenta, apartadosManager.getCliente(), apartadosManager.getTicketApartado().getCabecera());
						if (StringUtils.isNotBlank(apartadosManager.getTicketApartado().getCabecera().getTarjetaFidelizacion())) {
							FidelizacionBean fidelizadoReserva = new FidelizacionBean();
							fidelizadoReserva.setNumTarjetaFidelizado(apartadosManager.getTicketApartado().getCabecera().getTarjetaFidelizacion());
							fidelizadoReserva.setNombre(apartadosManager.getCliente().getDesCliente());

							ticketManager.getTicket().getCabecera().setDatosFidelizado(fidelizadoReserva);
						}

						/* Generamos los datos del Documento Origen de la Reserva */
						DatosDocumentoOrigenTicket datosOrigenReserva = new DatosDocumentoOrigenTicket();
						datosOrigenReserva.setUidTicket(ticketReservaGenerado.getCabecera().getUidApartado());
						datosOrigenReserva.setSerie(ticketReservaGenerado.getCabecera().getSerieTicket());
						datosOrigenReserva.setCodTicket(ticketReservaGenerado.getCabecera().getIdReserva());
						/* Valor del contador de ID_Reserva antes de insertar delante la serie */
						String idReserva = datosOrigenReserva.getCodTicket();
						idReserva = idReserva.substring((idReserva.length() - 6), idReserva.length());
						int cadenaResultadoInt = Integer.parseInt(idReserva);
						datosOrigenReserva.setNumFactura(Long.valueOf(cadenaResultadoInt));
						datosOrigenReserva.setCaja(null);
						datosOrigenReserva.setTienda(ticketReservaGenerado.getCabecera().getTienda().getCodAlmacen());
						datosOrigenReserva.setIdTipoDoc(Long.valueOf(ticketReservaGenerado.getCabecera().getTipoDocumento()));
						datosOrigenReserva.setCodTipoDoc(ticketReservaGenerado.getCabecera().getCodigoTipoDocumento());
						datosOrigenReserva.setDesTipoDoc(ticketReservaGenerado.getCabecera().getDescripcionTipoDocumento());
						datosOrigenReserva.setFecha(ticketReservaGenerado.getCabecera().getFecha());
						ticketManager.getTicket().getCabecera().setDatosDocOrigen(datosOrigenReserva);

						for (LineaTicket linea : (List<LineaTicket>) ticketManager.getTicket().getLineas()) {
							for (LineaReservaTicket lineaReserva : ticketReservaGenerado.getLineas()) {
								if (linea.getArticulo().getCodArticulo().equals(lineaReserva.getCodigoArticulo()) && linea.getDesglose1().equals(lineaReserva.getDesglose1())
								        && linea.getDesglose2().equals(lineaReserva.getDesglose2()) && linea.getCantidad().compareTo(lineaReserva.getCantidad()) == 0) {
									DocumentoOrigen docOriDetalle = new DocumentoOrigen();
									docOriDetalle.setUidDocumentoOrigen(ticketReservaGenerado.getCabecera().getUidApartado());
									docOriDetalle.setIdLineaDocumentoOrigen(lineaReserva.getIdLinea());
									linea.setDocumentoOrigen(docOriDetalle);
								}
							}
														
							addDescuentoReferencia((ByLLineaTicket) linea);
						}

						apartadosManager.actualizarEstadoLineasVendidas(ticketManager.getTicket().getCabecera().getUidTicket(), articulosVenta);
						apartadosManager.registrarVentaApartado(importeVenta);

						if (apartadosManager.getCliente() != null) {
							String tarjetaFidelizacion = StringUtils.isNotBlank(apartadosManager.getTicketApartado().getCabecera().getTarjetaFidelizacion())
							        ? apartadosManager.getTicketApartado().getCabecera().getTarjetaFidelizacion()
							        : null;
							String email = StringUtils.isNotBlank(apartadosManager.getCliente().getEmail()) ? apartadosManager.getCliente().getEmail() : null;
							/* Realizamos la consulta del Fidelizado por su Número Tarjeta de Fidelizado */
							/*
							 * Incidencia #69 : Reservas. Al realizar una venta identificada no está preguntando si
							 * quiere enviar correo
							 */
							ConsultarFidelizadoRequestRest req = null;
							if (StringUtils.isNotBlank(tarjetaFidelizacion)) {
								req = new ConsultarFidelizadoRequestRest(variablesServices.getVariableAsString(VariablesServices.WEBSERVICES_APIKEY), sesion.getAplicacion().getUidActividad(),
								        tarjetaFidelizacion);
							}
							else if (StringUtils.isNotBlank(email)) {
								req = new ConsultarFidelizadoRequestRest(variablesServices.getVariableAsString(VariablesServices.WEBSERVICES_APIKEY), sesion.getAplicacion().getUidActividad());
								req.setEmail(email);
							}
							/* Fin Incidencia #69 */
							try {
								List<FidelizadosBean> fidelizados = ByLFidelizadosRest.getFidelizadosDatos(req);
								if (fidelizados != null && !fidelizados.isEmpty()) {
									FidelizadosBean fidelizado = fidelizados.get(0);
									String consentimientoRecibe = fidelizado.getConsentimientosFirma().getConsentimientoRecibenoti();
									
									TipoDocumentoBean tipoDocumento;
									String permiteTicketE = null;
									try {
										tipoDocumento = sesion.getAplicacion().getDocumentos().getDocumento(ticketManager.getTicket().getCabecera().getTipoDocumento());
										PropiedadDocumentoBean propiedadClaseProcesamiento = tipoDocumento.getPropiedades().get(ByLPagosController.POS_PERMITE_TICKET_ELECTRONICO);
									
										if (propiedadClaseProcesamiento != null) {
											permiteTicketE = propiedadClaseProcesamiento.getValor();
										}										
									}
									catch (DocumentoException e1) {
										log.error("aceptar() - Ha ocurrido un error al obtener el tipo de documento", e1);
									}
									/* 
									 * 0 -> No permite ticket electronico (Se salta pantalla de email)
									 * 1 -> Se trata de impresora fiscal, no se permite "Sólo email"
									 * 2/null -> Se permite todo
									 */
									if (permiteTicketE == null || permiteTicketE.equals("1") || permiteTicketE.equals("2")) {
										if (consentimientoRecibe != null && "S".equals(consentimientoRecibe) && email != null && !email.isEmpty()) {
											HashMap<String, Object> mapaEmail = new HashMap<String, Object>();
											mapaEmail.put(ByLPagosController.POS_PERMITE_TICKET_ELECTRONICO, permiteTicketE);
											getApplication().getMainView().showModalCentered(ByLEmailView.class, mapaEmail, this.getStage());
											accionEmail = (String) mapaEmail.get(ACCION_EMAIL);
											if (accionEmail != null && (accionEmail.equals(CORREO) || accionEmail.equals(AMBOS))) {
												((ByLCabeceraTicket) ticketManager.getTicket().getCabecera()).setEmail(email);
											}
										}
									}
								}
							}
							catch (RestException | RestHttpException e1) {
								String mensajeError = "Se ha producido un error al consultar los datos del Fidelizado";
								log.error("aceptar() - " + mensajeError + " - " + e1.getMessage(), e1);
							}
						}

						ticketManager.salvarTicket(getStage(), new SalvarTicketCallback(){

							@Override
							public void onSucceeded() {
								
								if (!isImpresoraFiscalPolonia()) {
									Map<String, Object> mapaParametros = new HashMap<String, Object>();
									mapaParametros.put("ticket", ticketManager.getTicket());
									mapaParametros.put("urlQR", variablesServices.getVariableAsString("TPV.URL_VISOR_DOCUMENTOS"));
									mapaParametros.put("apartado", apartadosManager.getTicketApartado().getCabecera().getIdApartado().toString());
									try {
										ServicioImpresion.imprimir(ticketManager.getTicket().getCabecera().getFormatoImpresion(), mapaParametros);
									}
									catch (Exception e) {
										String mensajeError = I18N.getTexto("Lo sentimos, ha ocurrido un error al imprimir");
										log.error("accionAceptar/onSucceeded() - " + mensajeError + " - " + e.getMessage(), e);
										VentanaDialogoComponent.crearVentanaError(getStage(), mensajeError, e);
									}
								}
								ticketManager.finalizarTicket();
								getStage().close();
							}

							@Override
							public void onFailure(Exception e) {
								String mensajeError = I18N.getTexto("Se ha producido un error al salvar el ticket");
								log.error("accionAceptar/onFailure() - " + mensajeError + " - " + e.getMessage(), e);
								VentanaDialogoComponent.crearVentanaError(getStage(), mensajeError, e);
								getStage().close();
							}
						});
					}
					catch (LineaTicketException e) {
						log.error(e.getMessage(), e);
						VentanaDialogoComponent.crearVentanaError(getStage(), e.getMessage(), e);
					}
				}
			}
		}
		else {
			cajaMasterCerrada();
		}

	}
	
	/**
	 * Envía un mensaje por pantalla, que indica que la caja no está abierta
	 * y te envía a la pantalla principal.
	 */
	private void cajaMasterCerrada(){
		VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("La caja a la que estaba conectado se ha cerrado.")
				, this.getStage());
		/* Vuelve a la pantalla principal. */
		POSApplication.getInstance().getMainView().close();
	}
	
	@Override
	public void accionCancelar(){
	    super.accionCancelar();
	    /* Para no generar el Ticket en caso de cancelar */
	    getDatos().put(ByLPagosController.ACCION_CANCELAR, Boolean.TRUE);
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

	private void addDescuentoReferencia(ByLLineaTicket linea) {
		log.debug("addDescuentoReferencia() - Iniciamos el proceso para añadir el descuento sobre la tarifa inicial y los precios de venta de referencia de las tarifas a la línea");

		/* Cuando no venga tarifa inicial le setearemos su precio de tarifa original */
		BigDecimal precioVentaRef = linea.getPrecioTarifaOrigen();
		BigDecimal precioVentaRefTotal = linea.getPrecioTotalTarifaOrigen();
		if (linea.getTarifa().getPrecioVentaRefTotal() != null && !BigDecimalUtil.isIgualACero(linea.getTarifa().getPrecioVentaRefTotal())) {
			precioVentaRef = linea.getTarifa().getPrecioVentaRef();
			precioVentaRefTotal = linea.getTarifa().getPrecioVentaRefTotal();
		}

		BigDecimal descuentoSobreInicial = calculoDescuentoSobreInicial(precioVentaRefTotal, linea.getPrecioTotalConDto());
		log.debug("addDescuentoReferencia() - Porcentaje de descuento obtenido [" + descuentoSobreInicial + "]");
		linea.setDescuentoSobreInicial(descuentoSobreInicial);
		linea.setPrecioVentaRefTotal(precioVentaRefTotal);
		linea.setPrecioVentaRef(precioVentaRef);
	}
	
	private boolean isImpresoraFiscalPolonia() {
		IPrinter printer = Dispositivos.getInstance().getImpresora1();
		if (printer != null && printer instanceof IFiscalPrinter) {
			return true;
		}
		else {
			return false;
		}

	}
}
