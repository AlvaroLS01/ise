package com.comerzzia.bimbaylola.pos.gui.ventas.tickets;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.UUID;

import com.comerzzia.ByL.backoffice.rest.client.tickets.ByLTicketsRest;
import com.comerzzia.ByL.backoffice.rest.client.tickets.TicketDevolucionResponse;
import com.comerzzia.bimbaylola.pos.dispositivo.giftcard.ByLGiftCard;
import com.comerzzia.bimbaylola.pos.dispositivo.giftcard.EnviarMovimientoGiftCardBean;
import com.comerzzia.bimbaylola.pos.dispositivo.impresora.epsontm30.EpsonTM30;
import com.comerzzia.bimbaylola.pos.dispositivo.impresora.spark130f.Spark130F;
import com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.adyen.AdyenDatosPeticionPagoTarjeta;
import com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.axis.AxisDatosPeticionPagoTarjeta;
import com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.axis.AxisManager;
import com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.conexflow.ConexFlowDatosPeticionPagoTarjeta;
import com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.conexflow.TefConexFlow;
import com.comerzzia.bimbaylola.pos.gui.ventas.apartados.detalle.ByLDetalleApartadosController;
import com.comerzzia.bimbaylola.pos.persistence.core.config.configcontadores.ByLConfigContadorBean;
import com.comerzzia.bimbaylola.pos.persistence.core.config.configcontadores.rangos.ConfigContadorRango;
import com.comerzzia.bimbaylola.pos.persistence.core.config.configcontadores.rangos.ConfigContadorRangoExample;
import com.comerzzia.bimbaylola.pos.persistence.core.contadores.ByLContadorBean;
import com.comerzzia.bimbaylola.pos.persistence.giftcard.ByLGiftCardBean;
import com.comerzzia.bimbaylola.pos.services.articulos.articulosNoAptos.ArticuloNoAptoException;
import com.comerzzia.bimbaylola.pos.services.articulos.articulosNoAptos.ArticuloNoAptoService;
import com.comerzzia.bimbaylola.pos.services.core.ByLServicioContadores;
import com.comerzzia.bimbaylola.pos.services.core.config.configContadores.ByLServicioConfigContadoresImpl;
import com.comerzzia.bimbaylola.pos.services.core.config.configContadores.rangos.ConfigContadoresRangosConstraintViolationException;
import com.comerzzia.bimbaylola.pos.services.core.config.configContadores.rangos.ConfigContadoresRangosException;
import com.comerzzia.bimbaylola.pos.services.core.config.configContadores.rangos.ServicioConfigContadoresRangos;
import com.comerzzia.bimbaylola.pos.services.epsontse.EposOutput;
import com.comerzzia.bimbaylola.pos.services.epsontse.EpsonTSEService;
import com.comerzzia.bimbaylola.pos.services.reservas.exception.TicketReservaException;
import com.comerzzia.bimbaylola.pos.services.spark130f.Spark130FConstants;
import com.comerzzia.bimbaylola.pos.services.spark130f.Spark130FOutput;
import com.comerzzia.bimbaylola.pos.services.spark130f.Spark130FService;
import com.comerzzia.bimbaylola.pos.services.spark130f.exception.Spark130FException;
import com.comerzzia.bimbaylola.pos.services.ticket.ByLLineaTicket;
import com.comerzzia.bimbaylola.pos.services.ticket.ByLTicketPagosApartado;
import com.comerzzia.bimbaylola.pos.services.ticket.ByLTicketVentaAbono;
import com.comerzzia.bimbaylola.pos.services.ticket.ByLTicketsService;
import com.comerzzia.bimbaylola.pos.services.ticket.aparcados.reservas.CabeceraReservaTicket;
import com.comerzzia.bimbaylola.pos.services.ticket.cabecera.ByLCabeceraTicket;
import com.comerzzia.bimbaylola.pos.services.ticket.profesional.ByLLineaTicketProfesional;
import com.comerzzia.bimbaylola.pos.services.ticket.profesional.ByLTicketVentaProfesional;
import com.comerzzia.bimbaylola.pos.services.vertex.VertexService;
import com.comerzzia.core.model.notificaciones.Notificacion;
import com.comerzzia.core.model.notificaciones.Notificacion.Tipo;
import com.comerzzia.core.util.documentos.LocalizadorDocumento;
import com.comerzzia.core.util.documentos.LocalizadorDocumentoParseException;
import com.comerzzia.core.util.fechas.Fecha;
import com.comerzzia.core.util.mybatis.session.SqlSession;
import com.comerzzia.model.ventas.albaranes.articulos.ArticulosDevueltosBean;
import com.comerzzia.pos.core.dispositivos.Dispositivos;
import com.comerzzia.pos.core.dispositivos.dispositivo.DispositivoCallback;
import com.comerzzia.pos.core.dispositivos.dispositivo.balanza.BalanzaNoConfig;
import com.comerzzia.pos.core.dispositivos.dispositivo.balanza.IBalanza;
import com.comerzzia.pos.core.dispositivos.dispositivo.fidelizacion.ConsultaTarjetaFidelizadoException;
import com.comerzzia.pos.core.dispositivos.dispositivo.giftcard.GiftCardNoConfig;
import com.comerzzia.pos.core.dispositivos.dispositivo.tarjeta.TarjetaCallback;
import com.comerzzia.pos.core.dispositivos.dispositivo.tarjeta.TarjetaException;
import com.comerzzia.pos.core.gui.BackgroundTask;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.dispositivo.tarjeta.adyen.cloud.TefAdyenCloud;
import com.comerzzia.pos.gui.ventas.tickets.VentaProfesionalManager;
import com.comerzzia.pos.persistence.apartados.ApartadosCabeceraBean;
import com.comerzzia.pos.persistence.apartados.detalle.ApartadosDetalleBean;
import com.comerzzia.pos.persistence.clientes.ClienteBean;
import com.comerzzia.pos.persistence.codBarras.CodigoBarrasBean;
import com.comerzzia.pos.persistence.core.contadores.ContadorBean;
import com.comerzzia.pos.persistence.core.documentos.propiedades.PropiedadDocumentoBean;
import com.comerzzia.pos.persistence.core.documentos.tipos.TipoDocumentoBean;
import com.comerzzia.pos.persistence.fidelizacion.FidelizacionBean;
import com.comerzzia.pos.persistence.giftcard.GiftCardBean;
import com.comerzzia.pos.persistence.mediosPagos.MedioPagoBean;
import com.comerzzia.pos.persistence.mybatis.SessionFactory;
import com.comerzzia.pos.persistence.mybatis.SpringTransactionSqlSession;
import com.comerzzia.pos.persistence.tickets.TicketBean;
import com.comerzzia.pos.persistence.tickets.aparcados.TicketAparcadoBean;
import com.comerzzia.pos.services.apartados.ApartadosService;
import com.comerzzia.pos.services.articulos.ArticuloNotFoundException;
import com.comerzzia.pos.services.codBarrasEsp.CodBarrasEspecialesServices;
import com.comerzzia.pos.services.core.config.configContadores.parametros.ConfigContadoresParametrosException;
import com.comerzzia.pos.services.core.contadores.ContadorServiceException;
import com.comerzzia.pos.services.core.contadores.ServicioContadores;
import com.comerzzia.pos.services.core.documentos.DocumentoException;
import com.comerzzia.pos.services.core.documentos.Documentos;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.services.core.sesion.SesionImpuestos;
import com.comerzzia.pos.services.core.sesion.SesionPromociones;
import com.comerzzia.pos.services.core.usuarios.UsuariosService;
import com.comerzzia.pos.services.core.variables.VariablesServices;
import com.comerzzia.pos.services.cupones.CuponAplicationException;
import com.comerzzia.pos.services.cupones.CuponUseException;
import com.comerzzia.pos.services.cupones.CuponesServiceException;
import com.comerzzia.pos.services.cupones.CuponesServices;
import com.comerzzia.pos.services.mediospagos.MediosPagosService;
import com.comerzzia.pos.services.notificaciones.Notificaciones;
import com.comerzzia.pos.services.promociones.PromocionesServiceException;
import com.comerzzia.pos.services.ticket.ITicket;
import com.comerzzia.pos.services.ticket.Ticket;
import com.comerzzia.pos.services.ticket.TicketPagosApartado;
import com.comerzzia.pos.services.ticket.TicketVenta;
import com.comerzzia.pos.services.ticket.TicketVentaAbono;
import com.comerzzia.pos.services.ticket.TicketsServiceException;
import com.comerzzia.pos.services.ticket.aparcados.TicketsAparcadosService;
import com.comerzzia.pos.services.ticket.cabecera.CabeceraTicket;
import com.comerzzia.pos.services.ticket.cabecera.TarjetaRegaloTicket;
import com.comerzzia.pos.services.ticket.cabecera.TotalesTicket;
import com.comerzzia.pos.services.ticket.lineas.LineaTicket;
import com.comerzzia.pos.services.ticket.lineas.LineaTicketAbstract;
import com.comerzzia.pos.services.ticket.lineas.LineaTicketException;
import com.comerzzia.pos.services.ticket.lineas.LineasTicketServices;
import com.comerzzia.pos.services.ticket.pagos.IPagoTicket;
import com.comerzzia.pos.services.ticket.pagos.PagoTicket;
import com.comerzzia.pos.services.ticket.pagos.tarjeta.DatosPeticionPagoTarjeta;
import com.comerzzia.pos.services.ticket.pagos.tarjeta.DatosRespuestaPagoTarjeta;
import com.comerzzia.pos.services.ticket.promociones.PromocionLineaTicket;
import com.comerzzia.pos.services.ticket.promociones.PromocionTicket;
import com.comerzzia.pos.services.ticket.tarjetaRegalo.TarjetaRegaloException;
import com.comerzzia.pos.util.bigdecimal.BigDecimalUtil;
import com.comerzzia.pos.util.config.SpringContext;
import com.comerzzia.pos.util.format.FormatUtil;
import com.comerzzia.pos.util.i18n.I18N;
import com.comerzzia.pos.util.xml.MarshallUtil;
import com.comerzzia.rest.client.exceptions.HttpServiceRestException;
import com.comerzzia.rest.client.exceptions.RestConnectException;
import com.comerzzia.rest.client.exceptions.RestException;
import com.comerzzia.rest.client.exceptions.RestHttpException;
import com.comerzzia.rest.client.exceptions.RestTimeoutException;
import com.comerzzia.rest.client.exceptions.ValidationDataRestException;
import com.comerzzia.rest.client.exceptions.ValidationRequestRestException;
import com.comerzzia.rest.client.movimientos.MovimientoRequestRest;
import com.comerzzia.rest.client.tickets.ConsultarTicketRequestRest;
import com.comerzzia.rest.client.tickets.ResponseGetTicketDev;
import com.comerzzia.rest.client.tickets.TicketLocalizadorRequestRest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.stage.Stage;

@SuppressWarnings("rawtypes")
@Component
@Scope("prototype")
public class ByLVentaProfesionalManager extends VentaProfesionalManager implements ByLTicketRegalo {

	protected Logger log = Logger.getLogger(getClass());

	@Autowired
    private ByLTicketsService ticketsService;
	@Autowired
    private VariablesServices variablesServices;
	@Autowired
	private ArticuloNoAptoService noAptoService;
	@Autowired
	private SesionPromociones sesionPromociones;
	@Autowired
	private CuponesServices cuponesServices;
	@Autowired
	private ApartadosService apartadosService;
    @Autowired
    protected ServicioContadores contadoresService;
    @Autowired
	private ByLServicioContadores byLServicioContadores;
    @Autowired
    private CodBarrasEspecialesServices codBarrasEspecialesServices;
    @Autowired
    private LineasTicketServices lineasTicketServices;
    @Autowired
    private TicketsAparcadosService ticketsAparcadosService;
    @Autowired
	protected UsuariosService usuariosService;
    @Autowired
    private Sesion sesion;
    @Autowired
	protected EpsonTSEService epsonTSEService;
	@Autowired
	protected Spark130FService spark130FService;
	@Autowired
	protected VertexService vertexservice;
	
	public int intentosDevolucionFallido;

	public Boolean devolucionSinTicket;
	
	private List<String> listadoNoAptos = new ArrayList<String>();

	private static String ERROR_CANAL = "ERROR_CANAL";
	private static String ERROR_FECHA = "ERROR_FECHA";
	
	public static String SIN_TICKET = "sin_ticket";
		
	public static final String PARAMETRO_CODEMP = "CODEMP";
	public static final String PARAMETRO_CODALM = "CODALM";
	public static final String PARAMETRO_CODSERIE = "CODSERIE";
	public static final String PARAMETRO_CODCAJA = "CODCAJA";
	public static final String PARAMETRO_PERIODO = "PERIODO";
	public static final String PARAMETRO_CODDOC = "CODTIPODOCUMENTO";
	public static final String PARAMETRO_RANGO = "ID_RANGO";
		
	protected ByLContadorBean contador;
	 
	/* Para tratar los Ticket Regalos al realizar una devolucion */
	private Boolean esTicketRegalo = Boolean.FALSE;
	
	public static String RESERVAS = "RE";
	
	private ByLVentaProfesionalManager ticketAuxiliar;
	
	public void inicializarTicket() throws DocumentoException, PromocionesServiceException {
	    log.debug("inicializarTicket() - Creando nuevo ticket con valores iniciales...");
        documentoActivo = getNuevoDocumentoActivo();
        ticketPrincipal = SpringContext.getBean(getTicketClass(documentoActivo));
        crearTicket();

        devolucionTarjetaRegalo = false;
        esRecargaTarjetaRegalo = false;
        esOperacionTarjetaRegalo = false;
        esDevolucion = false;
        esFacturacionVentaCredito = false;
        ticketPrincipal.setEsDevolucion(false);

        intentosDevolucionFallido = 0;
    }

	@SuppressWarnings("unchecked")
	public void nuevoTicketPagosApartado() throws PromocionesServiceException, DocumentoException{
    	log.debug("nuevoTicketApartado() - Creando nuevo ticket con valores iniciales...");
        documentoActivo = sesion.getAplicacion().getDocumentos().getDocumento(RESERVAS);
        ticketPrincipal = SpringContext.getBean(TicketPagosApartado.class);
        ticketPrincipal.getCabecera().inicializarCabecera(ticketPrincipal);
        ((TicketPagosApartado)ticketPrincipal).inicializarTotales();
        ticketPrincipal.setCliente(sesion.getAplicacion().getTienda().getCliente().clone());
        ticketPrincipal.setCajero(sesion.getSesionUsuario().getUsuario());
        ticketPrincipal.getCabecera().getTotales().setCambio(SpringContext.getBean(PagoTicket.class , MediosPagosService.medioPagoDefecto));
        ticketPrincipal.getCabecera().setDocumento(documentoActivo);

        devolucionTarjetaRegalo = false;
        esRecargaTarjetaRegalo = false;
        esOperacionTarjetaRegalo = false;
        esDevolucion = false;
        ticketPrincipal.setEsDevolucion(false);
    }

	public List<String> getListadoNoAptos(){
		return listadoNoAptos;
	}

	public void setListadoNoAptos(List<String> listadoNoAptos){
		this.listadoNoAptos = listadoNoAptos;
	}

	public void salvarTicketSeguridad(Stage stage, SalvarTicketCallback callback) {
		new ByLSalvarTicketSeguridadTask(stage, callback).start();
	}

	public class ByLSalvarTicketSeguridadTask extends BackgroundTask<Void> {

    	protected Stage stage;
		protected SalvarTicketCallback callback;

		public ByLSalvarTicketSeguridadTask(Stage stage, SalvarTicketCallback callback) {
			this.stage = stage;
			this.callback = callback;
        }

		@Override
        protected Void call() throws Exception {
            sesionPromociones.aplicarPromocionesFinales((TicketVentaAbono) ticketPrincipal);

            // Registrar cupones utilizados. Si hay algún error, se ignorará y se registrarán al procesar el ticket en central
            cuponesServices.registraUsoCupones((TicketVentaAbono) ticketPrincipal);

            ticketPrincipal.getTotales().recalcular();
            ((TicketVentaAbono)ticketPrincipal).recalcularSubtotalesIva();
            
            if(ticketAuxiliar != null) {
            	ticketAuxiliar.getTicket().getTotales().recalcular();
            	((TicketVentaAbono)ticketAuxiliar.getTicket()).recalcularSubtotalesIva();
            }
            

            ((ByLTicketsService)ticketsService).asignarIdTransaccion((Ticket<?, ?, ?>) ticketPrincipal);
            if(ticketPrincipal.getIdTicket() == null) {
                ticketsService.setContadorIdTicket((Ticket<?, ?, ?>) ticketPrincipal);
            }
            log.debug("ByLSalvarTicketSeguridadTask() - Contador obtenido: idTicket = " + ticketPrincipal.getIdTicket());

            return null;
        }

		@SuppressWarnings("unchecked")
		@Override
		protected void succeeded(){
			super.succeeded();
			
			/* Generamos los DatosPeticionTarjeta */
			List<DatosPeticionPagoTarjeta> datosPeticionTarjeta = new LinkedList<>();

			BigDecimal totalPagos = BigDecimal.ZERO;
			//Añadimos todos los pagos
			final List<IPagoTicket> pagosTarjeta = new LinkedList<>(ticketPrincipal.getPagos());
			for(ListIterator<IPagoTicket> iterator = pagosTarjeta.listIterator(); iterator.hasNext();){
				IPagoTicket pagoTicket = iterator.next();
				/* Quitamos los pagos que no acepta el dispositivo */
				if(!Dispositivos.getInstance().getTarjeta().isCodMedPagoAceptado(pagoTicket.getCodMedioPago())){
					iterator.remove();
					continue;
				}

				totalPagos = totalPagos.add(pagoTicket.getImporte());				
				if(Dispositivos.getInstance().getTarjeta() instanceof TefConexFlow){
					
					String codTicketConexFlow = crearCodTicketPagoTarjeta(ticketPrincipal.getCabecera().getCodTicket());
					ConexFlowDatosPeticionPagoTarjeta datoPeticion = new ConexFlowDatosPeticionPagoTarjeta(((ByLCabeceraTicket)ticketPrincipal.getCabecera()).getIdTransaccion(),
					codTicketConexFlow, pagoTicket.getImporte(), ticketPrincipal.getCabecera().getCodTipoDocumento());
					if(getIntentosDevolucionFallido() == 1){
						datoPeticion.setEsDevolucionFallida(true);
					}
					datosPeticionTarjeta.add(datoPeticion);					
					if(ticketOrigen != null) {
						//Asociamos los datos de respuesta de los pagos origen
						List<IPagoTicket> pagosTarjetaOrig = new LinkedList<>(ticketOrigen.getPagos());
						for (IPagoTicket pagoTicketOrigen : pagosTarjetaOrig) {
							if(pagoTicket.getCodMedioPago().equals(pagoTicketOrigen.getCodMedioPago()) && BigDecimalUtil.isIgual(pagoTicket.getImporte().abs(), pagoTicketOrigen.getImporte().abs())) {
								// Se setean los valores del pago de tarjeta original al nuevo pago de tarjeta
								DatosRespuestaPagoTarjeta datosRespuestaPagoTarjeta = pagoTicketOrigen.getDatosRespuestaPagoTarjeta();
				            	if(datosRespuestaPagoTarjeta != null){
				            		datoPeticion.setCodAutorizacion(datosRespuestaPagoTarjeta.getCodAutorizacion());
				            		datoPeticion.setNumOpBanco(datosRespuestaPagoTarjeta.getNumOperacionBanco());
				            		datoPeticion.setNumOperacion(datosRespuestaPagoTarjeta.getNumOperacion());
									datoPeticion.setIdDocumentoOrigen(datosRespuestaPagoTarjeta.getNumTransaccion());
									datoPeticion.setFechaDocumentoOrigen(datosRespuestaPagoTarjeta.getFechaTransaccion());
	
				            		for(String clave : datosRespuestaPagoTarjeta.getAdicionales().keySet()) {
				            			datoPeticion.addAdiccional(clave, datosRespuestaPagoTarjeta.getAdicionales().get(clave));
				            		}
	
				            		// Parche para pasar los datos necesarios para devoluciones referenciadas en ConexFlow
				            		datoPeticion.setEmpleado(datosRespuestaPagoTarjeta.getCodigoCentro());
				            		datoPeticion.setNumOpBanco(datosRespuestaPagoTarjeta.getPos());
				            		// Parche para pasar los datos necesarios para devoluciones referenciadas en ConexFlow
				            	}							
				            }						
						}
					}
					
				}else if(Dispositivos.getInstance().getTarjeta() instanceof AxisManager){
					/* Cargamos el código del Ticket y creaamos el objeto de datos peticion */
					String codTicket = crearCodTicketPagoTarjeta(ticketPrincipal.getCabecera().getCodTicket());
					AxisDatosPeticionPagoTarjeta datoPeticion = new AxisDatosPeticionPagoTarjeta(((ByLCabeceraTicket)ticketPrincipal.getCabecera()).getIdTransaccion(),
							codTicket, pagoTicket.getImporte(), ticketPrincipal.getCabecera().getCodTipoDocumento());
					if(getIntentosDevolucionFallido() == 1){
						datoPeticion.setEsDevolucionFallida(true);
					}
					datosPeticionTarjeta.add(datoPeticion);

					if(ticketOrigen != null){
						/* Asociamos los datos respuesta de los pagos origen */
						List<IPagoTicket> pagosTarjetaOrig = new LinkedList<>(ticketOrigen.getPagos());
						for(IPagoTicket pagoTicketOrigen : pagosTarjetaOrig){
							if(pagoTicket.getCodMedioPago().equals(pagoTicketOrigen.getCodMedioPago())){
								DatosRespuestaPagoTarjeta datosRespuestaPagoTarjeta = pagoTicketOrigen.getDatosRespuestaPagoTarjeta();
				            	if(datosRespuestaPagoTarjeta != null){
				            		datoPeticion.setCodAutorizacion(datosRespuestaPagoTarjeta.getCodAutorizacion());
				            		datoPeticion.setNumOpBanco(datosRespuestaPagoTarjeta.getNumOperacionBanco());
				            		datoPeticion.setNumOperacion(datosRespuestaPagoTarjeta.getNumOperacion());
									datoPeticion.setIdDocumentoOrigen(datosRespuestaPagoTarjeta.getNumTransaccion());
									datoPeticion.setFechaDocumentoOrigen(datosRespuestaPagoTarjeta.getFechaTransaccion());

				            		for(String clave : datosRespuestaPagoTarjeta.getAdicionales().keySet()){
				            			datoPeticion.addAdiccional(clave, datosRespuestaPagoTarjeta.getAdicionales().get(clave));
				            		}
				            	}
							}
						}
					}
				}
				else if(Dispositivos.getInstance().getTarjeta() instanceof TefAdyenCloud){
					
					String codTicket = ticketPrincipal.getCabecera().getCodTicket();

					AdyenDatosPeticionPagoTarjeta datoPeticion = new AdyenDatosPeticionPagoTarjeta(((ByLCabeceraTicket)ticketPrincipal.getCabecera()).getIdTransaccion(),
							codTicket, pagoTicket.getImporte(), ticketPrincipal.getCabecera().getCodTipoDocumento());
					datosPeticionTarjeta.add(datoPeticion);
					if (ticketOrigen != null) {
						// Asociamos los datos respuesta de los pagos origen
						List<IPagoTicket> pagosTarjetaOrig = new LinkedList<>(ticketOrigen.getPagos());
						
						/*
						 * Debido al GAP1846, SÓLO se hara una devolución referenciada cuando el documento origen
						 * tenga UN SÓLO método de pago --> ADYEN 
						 * 
						 * En el caso de que el documento origen se haya
						 * pagado con más de un método de pago, se realizará una devolución no referenciada
						 */

						if (pagosTarjetaOrig.size() == 1 && pagosTarjetaOrig.get(0).getCodMedioPago().equals(pagoTicket.getCodMedioPago())) {
							DatosRespuestaPagoTarjeta datosRespuestaPagoTarjeta = pagosTarjetaOrig.get(0).getDatosRespuestaPagoTarjeta();
							if (datosRespuestaPagoTarjeta != null) {
								datoPeticion.setCodAutorizacion(datosRespuestaPagoTarjeta.getCodAutorizacion());
								datoPeticion.setNumOpBanco(datosRespuestaPagoTarjeta.getNumOperacionBanco());
								datoPeticion.setNumOperacion(datosRespuestaPagoTarjeta.getNumOperacion());

								datoPeticion.setIdDocumentoOrigen(datosRespuestaPagoTarjeta.getNumTransaccion());
								datoPeticion.setFechaDocumentoOrigen(datosRespuestaPagoTarjeta.getFechaTransaccion());

								for (String clave : datosRespuestaPagoTarjeta.getAdicionales().keySet()) {
									datoPeticion.addAdiccional(clave, datosRespuestaPagoTarjeta.getAdicionales().get(clave));
								}

								if (StringUtils.isNotBlank(datosRespuestaPagoTarjeta.getTerminal())) {
									String terminal = datosRespuestaPagoTarjeta.getTerminal().split("/")[1].trim();
									datoPeticion.setTerminalOrigen(terminal);
								}

							}
						}
					}
				}else {
					// Utilizaremos estandar
					DatosPeticionPagoTarjeta datoPeticion = new DatosPeticionPagoTarjeta(ticketPrincipal.getCabecera().getCodTicket(), ticketPrincipal.getCabecera().getIdTicket(),
					        pagoTicket.getImporte());
					datosPeticionTarjeta.add(datoPeticion);
					if (ticketOrigen != null) {
						// Asociamos los datos respuesta de los pagos origen
						List<IPagoTicket> pagosTarjetaOrig = new LinkedList<>(ticketOrigen.getPagos());
						for (IPagoTicket pagoTicketOrigen : pagosTarjetaOrig) {
							if (pagoTicket.getCodMedioPago().equals(pagoTicketOrigen.getCodMedioPago())) {
								DatosRespuestaPagoTarjeta datosRespuestaPagoTarjeta = pagoTicketOrigen.getDatosRespuestaPagoTarjeta();
								if (datosRespuestaPagoTarjeta != null) {
									datoPeticion.setCodAutorizacion(datosRespuestaPagoTarjeta.getCodAutorizacion());
									datoPeticion.setNumOpBanco(datosRespuestaPagoTarjeta.getNumOperacionBanco());
									datoPeticion.setNumOperacion(datosRespuestaPagoTarjeta.getNumOperacion());

									datoPeticion.setIdDocumentoOrigen(datosRespuestaPagoTarjeta.getNumTransaccion());
									datoPeticion.setFechaDocumentoOrigen(datosRespuestaPagoTarjeta.getFechaTransaccion());
								}
							}
						}
					}
				}
			}

            TarjetaCallback<List<DatosRespuestaPagoTarjeta>> dispositivoCallback = new TarjetaCallback<List<DatosRespuestaPagoTarjeta>>(){

				@Override
				public void onSuccess(List<DatosRespuestaPagoTarjeta> datosRespuesta){
					/* Asignamos los datos respuesta a los pagos */
					for(DatosRespuestaPagoTarjeta datoRespuesta : datosRespuesta){
						pagosTarjeta.get(datosRespuesta.indexOf(datoRespuesta)).setDatosRespuestaPagoTarjeta(datoRespuesta);
					}
					crearClaseRegistrarTicketTask(stage, callback, datosRespuesta).start();
				}

				@Override
				public void onFailure(List<DatosRespuestaPagoTarjeta> datosRespuesta, Throwable caught){
		            anularPagos(datosRespuesta, stage);
		            anularMovimientoTarjetaRegalo(stage);
		            callback.onFailure((Exception) caught);

		            /* Para comprobar los intentos fallidos para devolver */
		            if(Dispositivos.getInstance().getTarjeta() instanceof TefConexFlow){
		            	if(datosRespuesta != null && !datosRespuesta.isEmpty() && ((ConexFlowDatosPeticionPagoTarjeta) datosRespuesta.get(0).getDatosPeticion()) != null &&
								((ConexFlowDatosPeticionPagoTarjeta) datosRespuesta.get(0).getDatosPeticion()).getEsDevolucion()){

							setIntentosDevolucionFallido(getIntentosDevolucionFallido() + 1);
						}
		            }else if(Dispositivos.getInstance().getTarjeta() instanceof AxisManager){
		            	if(datosRespuesta != null && !datosRespuesta.isEmpty() && ((AxisDatosPeticionPagoTarjeta) datosRespuesta.get(0).getDatosPeticion()) != null &&
								((AxisDatosPeticionPagoTarjeta) datosRespuesta.get(0).getDatosPeticion()).getEsDevolucion()){

							setIntentosDevolucionFallido(getIntentosDevolucionFallido() + 1);
							VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("No se ha podido devolver automáticamente."
									+ " Debe introducir la tarjeta."), stage);

							if(getIntentosDevolucionFallido() == 1) {
								salvarTicketSeguridad(stage, callback);
							}
						}
		            }
				}
			};

			boolean totalMayorACero = BigDecimalUtil.isMayorOrIgualACero(totalPagos);
			boolean venta = totalMayorACero;

	        if(ticketPrincipal.getCabecera().getTicket() != null && !datosPeticionTarjeta.isEmpty()) {
	        	if(venta){
	        		datosPeticionTarjeta.get(0).setImporte(totalPagos);
				}
			}

			if (venta) {
				Dispositivos.getInstance().getTarjeta().solicitarPagosTarjeta(datosPeticionTarjeta, stage, dispositivoCallback);
			}
			else {
				Dispositivos.getInstance().getTarjeta().solicitarDevolucion(datosPeticionTarjeta, stage, dispositivoCallback);
				/*
				 * Inicializamos el valor esDevolucion = true para controlar en caso de fallo y volver a enviar petición
				 * como devolución no referenciada
				 */
				if (datosPeticionTarjeta != null && !datosPeticionTarjeta.isEmpty()) {
					if (Dispositivos.getInstance().getTarjeta() instanceof TefConexFlow) {
						((ConexFlowDatosPeticionPagoTarjeta) datosPeticionTarjeta.get(0)).setEsDevolucion(true);
					}
					else if (Dispositivos.getInstance().getTarjeta() instanceof AxisManager) {
						((AxisDatosPeticionPagoTarjeta) datosPeticionTarjeta.get(0)).setEsDevolucion(true);
					}
				}
			}
		}

		@Override
		protected void failed(){
			super.failed();
			Throwable ex = getException();
            callback.onFailure((Exception) ex);
		}

	}

	@SuppressWarnings("unchecked")
	public TicketDevolucionResponse recuperarTicketDevolucionByL(String codigo, String codAlmacen,
			String codCaja, Long idTipoDoc) throws ArticuloNoAptoException{

		TicketDevolucionResponse respuesta = new TicketDevolucionResponse();

    	try{
	    	log.debug("recuperarTicketDevolucion() - Recuperando ticket...");
	    	byte[] xmlTicketOrigen = null;
	    	ResponseGetTicketDev datosDevolucion = null;

	    	LocalizadorDocumento localizadorDocumento = null;
			try {
				localizadorDocumento = LocalizadorDocumento.parse(codigo);
			} catch (LocalizadorDocumentoParseException e) {
			}
	    	//Si es localizador
			if(localizadorDocumento != null){
				//Obtenemos por localizador desde central
				try {
					respuesta = obtenerTicketDevolucionCentralLocalizadorByL(localizadorDocumento.getLocalizador(), false);
					if(respuesta != null && respuesta.getTicket() != null) {
						xmlTicketOrigen = respuesta.getTicket().getBytes("UTF-8");
						if(!respuesta.isPermiteDevolucion()){
							respuesta.setError(ERROR_CANAL);
						}
						/* Comprobamos que la fecha este correcta a través de la variable boolean. */
						if(!respuesta.getFechaOk()){
							if(respuesta.getError() == null){
								respuesta.setError(ERROR_FECHA);
							}
						}
					}
				} catch (LineaTicketException e) {
					log.warn("recuperarTicketDevolucion() - Error al obtener ticket devolución desde central - " + e.getClass().getName() + " - " + e.getLocalizedMessage(), e);
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}

				if(xmlTicketOrigen != null){
					//Si no null, buscamos datos devolucion
					tratarTicketRecuperado(xmlTicketOrigen);
					if(respuesta.getError() == null || !respuesta.getError().equals(ERROR_CANAL)) {
						datosDevolucion = obtenerDatosDevolucion(ticketOrigen.getUidTicket());
					}
				}else{
					//Si null, obtenemos por localizador desde local
					List<TicketBean> tickets  = ticketsService.consultarTicketLocalizador(localizadorDocumento, null);
	        		if(!tickets.isEmpty()){
	        			xmlTicketOrigen = tickets.get(0).getTicket();
	        			if(respuesta == null) {
	        				respuesta = new TicketDevolucionResponse();
	        			}
	        			respuesta.setTicket(new String(xmlTicketOrigen, "UTF-8"));
	        			tratarTicketRecuperado(xmlTicketOrigen);
	        			/* Cargamos los datos de los articulos no aptos. */
				    	List<LineaTicket> lista = ticketOrigen.getLineas();
				    	List<String> listadoTicketNoAptos = new ArrayList<String>();
				    	for(LineaTicket linea : lista){
				    		listadoTicketNoAptos.add(linea.getCodArticulo());
				    	}

				    	respuesta.setListadoArticulosNoDevolver(noAptoService
				    			.consultarNoAptosLista(listadoTicketNoAptos, ticketOrigen.getUidActividad()));
	        		}else{
	        			throw new TicketsServiceException("No se ha encontrado ticket con el localizador: "
	        					+ localizadorDocumento.getLocalizador());
	        		}
				}
			}

			//Si no tenemos ticket, consultamos como id de documento en lugar de como localizador
			if(xmlTicketOrigen == null){
				//por codigo desde central
				try{
					respuesta = obtenerTicketDevolucionCentralByL(codCaja, codAlmacen, codigo, idTipoDoc);
					if(respuesta.getTicket() != null) {
						xmlTicketOrigen = respuesta.getTicket().getBytes("UTF-8");
						if(!respuesta.isPermiteDevolucion()){
							respuesta.setError(ERROR_CANAL);
						}
						/* Comprobamos que la fecha este correcta a través de la variable boolean. */
						if(!respuesta.getFechaOk()){
							if(respuesta.getError() == null){
								respuesta.setError(ERROR_FECHA);
							}
						}
					}
				}catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}catch(Exception e){
					log.warn("recuperarTicketDevolucion() - Error al obtener ticket devolución desde central - "
							+ e.getClass().getName() + " - " + e.getLocalizedMessage(), e);
				}

				if(xmlTicketOrigen != null){
					//Si no null, buscamos datos devolucion
					tratarTicketRecuperado(xmlTicketOrigen);
					if(respuesta.getError() == null || !respuesta.getError().equals(ERROR_CANAL)) {
						datosDevolucion = obtenerDatosDevolucion(ticketOrigen.getUidTicket());
					}
				}else{
					//Si null, obtenemos por codigo desde local
					TicketBean ticketA  = ticketsService.consultarTicketAbono(codAlmacen, codCaja, codigo, idTipoDoc);
				    if(ticketA!=null){
				    	xmlTicketOrigen = ticketA.getTicket();
				    	respuesta.setTicket(new String(xmlTicketOrigen, "UTF-8"));
				    	tratarTicketRecuperado(xmlTicketOrigen);
				    	/* Cargamos los datos de los articulos no aptos. */
				    	List<LineaTicket> lista = ticketOrigen.getLineas();
				    	List<String> listadoTicketNoAptos = new ArrayList<String>();
				    	for(LineaTicket linea : lista){
				    		listadoTicketNoAptos.add(linea.getCodArticulo());
				    	}

				    	respuesta.setListadoArticulosNoDevolver(noAptoService
				    			.consultarNoAptosLista(listadoTicketNoAptos, ticketOrigen.getUidActividad()));

				    }else{
				    	throw new TicketsServiceException("No se ha encontrado ticket con codigo: " + codigo);
				    }
				}
			}

			if (respuesta.getTaxFree() != null) {
				((ByLTicketVentaAbono) ticketOrigen).setTaxFree(respuesta.getTaxFree());
			}
			asignarLineasDevueltas(datosDevolucion);

	        descontarLineasNegativasTicketOrigen();

	        if(respuesta.getError() == null){
	        	respuesta.setError("");
	        }

	        return respuesta;

    	}catch(TicketsServiceException e){
    		log.error("recuperarTicketDevolucion() - " + e.getClass().getName() + " - "
    				+ e.getLocalizedMessage(), e);
    		return null;
		} catch (UnsupportedEncodingException e) {
			log.error("recuperarTicketDevolucion() - " + e.getClass().getName() + " - "
    				+ e.getLocalizedMessage(), e);
    		return null;
		}

    }

	protected TicketDevolucionResponse obtenerTicketDevolucionCentralLocalizadorByL(String localizador, boolean throwRestExceptions) throws LineaTicketException{
    	log.trace("consultarTicketLocalizador() - localizador: "+localizador);

    	TicketDevolucionResponse ticketRecuperado = null;

    	TicketLocalizadorRequestRest request = new TicketLocalizadorRequestRest(sesion.getAplicacion().getUidActividad(), variablesServices.getVariableAsString(VariablesServices.WEBSERVICES_APIKEY), localizador);
    	try {
    		ticketRecuperado = ByLTicketsRest.recuperarTicketLocalizador(request, sesion.getAplicacion().getCodAlmacen());

    	}catch (RestException | RestHttpException e){
    		if(throwRestExceptions){
    			if(e instanceof ValidationRequestRestException ||
    					e instanceof ValidationDataRestException ||
    					e instanceof HttpServiceRestException){
    				throw new LineaTicketException(e.getMessage(), e);
    			}else if(e instanceof RestHttpException){
    				throw new LineaTicketException(I18N.getTexto("Lo sentimos, ha ocurrido un error en la petición"), e);
    			}else if(e instanceof RestConnectException){
    				throw new LineaTicketException(I18N.getTexto("No se ha podido conectar con el servidor"), e);
    			}else if(e instanceof RestTimeoutException){
    				throw new LineaTicketException(I18N.getTexto("El servidor ha tardado demasiado tiempo en responder"), e);
    			}else if(e instanceof RestException){
    				throw new LineaTicketException(I18N.getTexto("Lo sentimos, ha ocurrido un error en la petición"), e);
    			}else{
    				throw new LineaTicketException(I18N.getTexto("Lo sentimos, ha ocurrido un error."), e);
    			}
    		}
    	}
    	return ticketRecuperado;
    }

	protected TicketDevolucionResponse obtenerTicketDevolucionCentralByL( String codCaja, String codTienda, String codOperacion, Long idTipoDocumento) throws RestHttpException, RestException{
        ConsultarTicketRequestRest request = new ConsultarTicketRequestRest(sesion.getAplicacion().getUidActividad(), variablesServices.getVariableAsString(VariablesServices.WEBSERVICES_APIKEY), codTienda, codOperacion, codCaja, idTipoDocumento);

        TicketDevolucionResponse ticketDevolucion;
        ticketDevolucion = ByLTicketsRest.getTicket(request, sesion.getAplicacion().getCodAlmacen());

        return ticketDevolucion;
    }

	@SuppressWarnings("unchecked")
	public void redondearImportesTicket() {
    	for (LineaTicket linea : (List<LineaTicket>)ticketPrincipal.getLineas()) {
			linea.setImporteConDto(BigDecimalUtil.redondear(linea.getImporteConDto()));
			linea.setImporteTotalConDto(BigDecimalUtil.redondear(linea.getImporteTotalConDto()));
			linea.setImporteTotalPromociones(BigDecimalUtil.redondear(linea.getImporteTotalPromociones()));
			linea.setImporteTotalPromocionesMenosIngreso(BigDecimalUtil.redondear(linea.getImporteTotalPromocionesMenosIngreso()));
			for (PromocionLineaTicket promocionLinea : linea.getPromociones()) {
				promocionLinea.setImporteTotalDtoMenosMargen(BigDecimalUtil.redondear(promocionLinea.getImporteTotalDtoMenosMargen()));
				promocionLinea.setImporteTotalDtoMenosIngreso(BigDecimalUtil.redondear(promocionLinea.getImporteTotalDtoMenosIngreso()));
			}
		}
    	for (PromocionTicket promocion : (List<PromocionTicket>)ticketPrincipal.getPromociones()) {
			promocion.setImporteTotalAhorro(BigDecimalUtil.redondear(promocion.getImporteTotalAhorro()));
		}
	}

	@SuppressWarnings("unchecked")
	public void llamarPinpad(final Stage stage, final SalvarTicketCallback callback){
		/* Generamos los DatosPeticionTarjeta */
		List<DatosPeticionPagoTarjeta> datosPeticionTarjeta = new LinkedList<>();
		/* Añadimos todos los pagos */
		final List<IPagoTicket> pagosTarjeta = new LinkedList<>(ticketPrincipal.getPagos());
		for(ListIterator<IPagoTicket> iterator = pagosTarjeta.listIterator(); iterator.hasNext();){
			IPagoTicket pagoTicket = iterator.next();
			/* Quitamos los pagos que no acepta el dispositivo */
			if(!Dispositivos.getInstance().getTarjeta().isCodMedPagoAceptado(pagoTicket.getCodMedioPago())){
				iterator.remove();
				continue;
			}

			if(Dispositivos.getInstance().getTarjeta() instanceof TefConexFlow){
				String codTicketConexFlow = crearCodTicketPagoTarjeta(ticketPrincipal.getCabecera().getCodTicket());
				ConexFlowDatosPeticionPagoTarjeta datoPeticion = new ConexFlowDatosPeticionPagoTarjeta(((ByLCabeceraTicket)ticketPrincipal.getCabecera()).getIdTransaccion(),
						codTicketConexFlow, pagoTicket.getImporte(), ticketPrincipal.getCabecera().getCodTipoDocumento());
				datosPeticionTarjeta.add(datoPeticion);

				if(ticketOrigen != null){
					/* Asociamos los datos de respuesta de los pagos origen */
					List<IPagoTicket> pagosTarjetaOrig = new LinkedList<>(ticketOrigen.getPagos());
					for(IPagoTicket pagoTicketOrigen : pagosTarjetaOrig){
						if(pagoTicket.getCodMedioPago().equals(pagoTicketOrigen.getCodMedioPago())){
							DatosRespuestaPagoTarjeta datosRespuestaPagoTarjeta = pagoTicketOrigen.getDatosRespuestaPagoTarjeta();
			            	if(datosRespuestaPagoTarjeta != null){
			            		datoPeticion.setCodAutorizacion(datosRespuestaPagoTarjeta.getCodAutorizacion());
			            		datoPeticion.setNumOpBanco(datosRespuestaPagoTarjeta.getNumOperacionBanco());
			            		datoPeticion.setNumOperacion(datosRespuestaPagoTarjeta.getNumOperacion());
								datoPeticion.setIdDocumentoOrigen(datosRespuestaPagoTarjeta.getNumTransaccion());
								datoPeticion.setFechaDocumentoOrigen(datosRespuestaPagoTarjeta.getFechaTransaccion());

			            		for(String clave : datosRespuestaPagoTarjeta.getAdicionales().keySet()){
			            			datoPeticion.addAdiccional(clave, datosRespuestaPagoTarjeta.getAdicionales().get(clave));
			            		}

			            		/* Parche para pasar los datos necesarios para devoluciones referenciadas en ConexFlow */
			            		datoPeticion.setEmpleado(datosRespuestaPagoTarjeta.getCodigoCentro());
			            		datoPeticion.setNumOpBanco(datosRespuestaPagoTarjeta.getPos());
			            	}
						}
					}
				}
			}else if(Dispositivos.getInstance().getTarjeta() instanceof AxisManager){
				/* Cargamos el código del Ticket y creaamos el objeto de datos peticion */
				String codTicket = crearCodTicketPagoTarjeta(ticketPrincipal.getCabecera().getCodTicket());
				AxisDatosPeticionPagoTarjeta datoPeticion = new AxisDatosPeticionPagoTarjeta(((ByLCabeceraTicket)ticketPrincipal.getCabecera()).getIdTransaccion(),
						codTicket, pagoTicket.getImporte(), ticketPrincipal.getCabecera().getCodTipoDocumento());
				datosPeticionTarjeta.add(datoPeticion);

				if(ticketOrigen != null){
					/* Asociamos los datos respuesta de los pagos origen */
					List<IPagoTicket> pagosTarjetaOrig = new LinkedList<>(ticketOrigen.getPagos());
					for(IPagoTicket pagoTicketOrigen : pagosTarjetaOrig){
						if(pagoTicket.getCodMedioPago().equals(pagoTicketOrigen.getCodMedioPago())){
							DatosRespuestaPagoTarjeta datosRespuestaPagoTarjeta = pagoTicketOrigen.getDatosRespuestaPagoTarjeta();
			            	if(datosRespuestaPagoTarjeta != null){
			            		datoPeticion.setCodAutorizacion(datosRespuestaPagoTarjeta.getCodAutorizacion());
			            		datoPeticion.setNumOpBanco(datosRespuestaPagoTarjeta.getNumOperacionBanco());
			            		datoPeticion.setNumOperacion(datosRespuestaPagoTarjeta.getNumOperacion());
								datoPeticion.setIdDocumentoOrigen(datosRespuestaPagoTarjeta.getNumTransaccion());
								datoPeticion.setFechaDocumentoOrigen(datosRespuestaPagoTarjeta.getFechaTransaccion());

			            		for(String clave : datosRespuestaPagoTarjeta.getAdicionales().keySet()){
			            			datoPeticion.addAdiccional(clave, datosRespuestaPagoTarjeta.getAdicionales().get(clave));
			            		}
			            	}
						}
					}
				}
			}
		}

        TarjetaCallback<List<DatosRespuestaPagoTarjeta>> dispositivoCallback = new TarjetaCallback<List<DatosRespuestaPagoTarjeta>>(){

			@Override
			public void onSuccess(List<DatosRespuestaPagoTarjeta> datosRespuesta) {
				//Asignamos los datos respuesta a los pagos
				for (DatosRespuestaPagoTarjeta datoRespuesta : datosRespuesta) {
					pagosTarjeta.get(datosRespuesta.indexOf(datoRespuesta)).setDatosRespuestaPagoTarjeta(datoRespuesta);
				}
				crearClaseRegistrarTicketTask(stage, callback, datosRespuesta).start();
			}

			@Override
			public void onFailure(List<DatosRespuestaPagoTarjeta> datosRespuesta, Throwable caught) {
	            anularPagos(datosRespuesta, stage);
	            anularMovimientoTarjetaRegalo(stage);
				callback.onFailure((Exception) caught);

				/* Para comprobar los intentos fallidos para devolver */
	            if(Dispositivos.getInstance().getTarjeta() instanceof TefConexFlow){
					if(((ConexFlowDatosPeticionPagoTarjeta) datosRespuesta) != null &&
							((ConexFlowDatosPeticionPagoTarjeta) datosRespuesta.get(0).getDatosPeticion()) != null &&
							((ConexFlowDatosPeticionPagoTarjeta) datosRespuesta.get(0).getDatosPeticion()).getEsDevolucion()){

						setIntentosDevolucionFallido(getIntentosDevolucionFallido() + 1);
						VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("No se ha podido devolver automáticamente. Debe introducir la tarjeta."), stage);

						salvarTicketSeguridad(stage, callback);
					}
	            }else if(Dispositivos.getInstance().getTarjeta() instanceof AxisManager){
	            	if(((AxisDatosPeticionPagoTarjeta) datosRespuesta) != null &&
							((AxisDatosPeticionPagoTarjeta) datosRespuesta.get(0).getDatosPeticion()) != null &&
							((AxisDatosPeticionPagoTarjeta) datosRespuesta.get(0).getDatosPeticion()).getEsDevolucion()){

						setIntentosDevolucionFallido(getIntentosDevolucionFallido() + 1);
						VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("No se ha podido devolver automáticamente. Debe introducir la tarjeta."), stage);

						salvarTicketSeguridad(stage, callback);
					}
	            }
			}
		};

        boolean venta = (!ticketPrincipal.getCabecera().getCodTipoDocumento().equals("NC")) &&
        		BigDecimalUtil.isMayorOrIgualACero(ticketPrincipal.getTotales().getTotal());

        if(venta){
        	Dispositivos.getInstance().getTarjeta().solicitarPagosTarjeta(datosPeticionTarjeta, stage, dispositivoCallback);
        }else{
        	Dispositivos.getInstance().getTarjeta().solicitarDevolucion(datosPeticionTarjeta, stage, dispositivoCallback);
        	/* Inicializamos el valor esDevolucion = true para controlar en caso de fallo y volver a enviar petición como devolución no referenciada */
        	if(datosPeticionTarjeta != null && !datosPeticionTarjeta.isEmpty()) {
	        	if(Dispositivos.getInstance().getTarjeta() instanceof TefConexFlow){
		        		((ConexFlowDatosPeticionPagoTarjeta) datosPeticionTarjeta.get(0)).setEsDevolucion(true);
	        	}else if(Dispositivos.getInstance().getTarjeta() instanceof AxisManager){
		        		((AxisDatosPeticionPagoTarjeta) datosPeticionTarjeta.get(0)).setEsDevolucion(true);
	        	}
        	}
        }
	}

	public void setTicketOrigen(TicketVenta ticketOrigen){
		this.ticketOrigen = ticketOrigen;
	}

	protected void procesarTarjetaRegalo(Stage stage) throws TarjetaRegaloException, DocumentoException{
		try{
	        log.trace("procesarTarjetaRegalo()");

	        final TarjetaRegaloTicket tarjRegalo = ticketPrincipal.getCabecera().getTarjetaRegalo();

	        String uidTransaccion = UUID.randomUUID().toString();
	        if(tarjRegalo!=null){
	            tarjRegalo.setUidTransaccion(uidTransaccion);
	            /**/
	            /* Si es una operacion de carga inicial en tarjeta mandamos a recargar la tarjeta con el importe definido */
	            if(esRecargaTarjetaRegalo){
	                log.trace("Procesando recarga de la tarjeta regalo "+ tarjRegalo.getNumTarjetaRegalo());
	                ((ByLGiftCard)Dispositivos.getInstance().getGiftCard()).recargarImporteTarjetaRegalo(
		        		//stage,
		        		//uidTransaccion,
		        		//sesion.getAplicacion().getUidActividad(),
		                //tarjRegalo.getSaldoProvisional(),
		                //tarjRegalo.getSaldo(),
		                tarjRegalo.getImporteRecarga(),
		                tarjRegalo.getNumTarjetaRegalo(),
		                //ticketPrincipal.getUidTicket(),
		                //ticketPrincipal.getCabecera().getDesTipoDocumento() + " " + 
		                ticketPrincipal.getCabecera().getCodTicket(),
		                ((ByLCabeceraTicket)ticketPrincipal.getCabecera()).getTarjeta().getEstado());
	                tarjRegalo.setSaldo(tarjRegalo.getSaldo().add(tarjRegalo.getImporteRecarga()));
	            }
	            else if(devolucionTarjetaRegalo){
	                log.trace("Procesando devolución de la tarjeta regalo "+ tarjRegalo.getNumTarjetaRegalo());

	                try {
						BigDecimal total = ticketPrincipal.getTotales().getTotal().abs();
						ByLGiftCardBean giftCard = ((ByLCabeceraTicket) ticketPrincipal.getCabecera()).getTarjeta();
						/* Estado 1 = Activo */
						if (giftCard.getEstado() == 1 && BigDecimalUtil.isIgual(tarjRegalo.getSaldo(), total)) {
			                ((ByLGiftCard)Dispositivos.getInstance().getGiftCard()).recargarImporteTarjetaRegalo(
					    		//stage,
					    		//uidTransaccion,
					            //sesion.getAplicacion().getUidActividad(),
					            //tarjRegalo.getSaldoProvisional(),
					            //tarjRegalo.getSaldo(),
					            total.negate(),
					            tarjRegalo.getNumTarjetaRegalo(),
					            //ticketPrincipal.getUidTicket(),
					            //ticketPrincipal.getCabecera().getDesTipoDocumento() + " " + 
					            ticketPrincipal.getCabecera().getCodTicket(),
					            ((ByLCabeceraTicket)ticketPrincipal.getCabecera()).getTarjeta().getEstado());
			                giftCard.setSaldo(BigDecimal.ZERO);
			                tarjRegalo.setSaldo(tarjRegalo.getSaldo().add(tarjRegalo.getImporteRecarga()));
						}
						else {
							tarjRegalo.setUidTransaccion(null);

							VentanaDialogoComponent.crearVentanaError(I18N.getTexto("Esta tarjeta regalo no se puede devolver por alguno de estos motivos: \n\n "
									+ " - Ya se han realizado operaciones sobre esta tarjeta regalo. \n\n "
									+ " - La tarjeta regalo está inactiva. \n"), stage);

							throw new TarjetaRegaloException(I18N.getTexto("Esta tarjeta regalo no se puede devolver por alguno de estos motivos: \n\n "
									+ " - Ya se han realizado operaciones sobre esta tarjeta regalo. \n\n "
									+ " - La tarjeta regalo está inactiva."));
						}
	                }
	                catch(TarjetaRegaloException e) {
	                	throw e;
	                }
	                catch(Exception e) {
	                	log.error("procesarTarjetaRegalo() - Ha habido un error al procesar la devolución de la tarjeta regalo: " + e.getMessage(), e);
	                	throw new TarjetaRegaloException(I18N.getTexto("Ha habido un error al procesar la devolución de la tarjeta regalo."), e);
	                }
	            }
	            /**/
	        }else{
	        	final List<GiftCardBean> listaPagosGiftcard = new ArrayList<>();

	        	//Si es un pago con tarjeta regalo realizamos el cargo con el importe elegido
	        	final List<PagoTicket> pagosTarjetaRegalo = obtenerPagosTarjetaRegalo();
	        	for(PagoTicket pagoTarjetaRegalo : pagosTarjetaRegalo) {
		        	for(GiftCardBean giftcardBean : pagoTarjetaRegalo.getGiftcards()) {
		        		log.trace("Procesando pago con la tarjeta regalo "+ giftcardBean.getNumTarjetaRegalo());
		        		giftcardBean.setUidTransaccion(UUID.randomUUID().toString());

		        		listaPagosGiftcard.add(giftcardBean);
		        	}
	        	}

				/**/
				String origen = null;
				String codTicket = ticketPrincipal.getCabecera().getCodTicket();

				if(ticketOrigen != null){
					origen = ticketOrigen.getCabecera().getCodTicket();
				}else if(devolucionSinTicket != null && devolucionSinTicket){
					origen = SIN_TICKET;
				}

				((ByLGiftCard) Dispositivos.getInstance().getGiftCard()).crearMovimientosProvisionales(stage,
						sesion.getAplicacion().getUidActividad(), listaPagosGiftcard,
				        ticketPrincipal.getUidTicket(), codTicket, origen);
	        	/**/
	        	
//	        	String codTicket = null;
//	        	if("RE".equals(ticketPrincipal.getCabecera().getCodTipoDocumento())) {
//	        		codTicket = ticketPrincipal.getCabecera().getDesTipoDocumento() + " " + "RE";
//	        	}else{
//	        		codTicket = ticketPrincipal.getCabecera().getDesTipoDocumento() + " " + ticketPrincipal.getCabecera().getCodTicket();
//	        	}
//	        	if(ticketOrigen != null){
//					codTicket = codTicket + " " + ticketOrigen.getCabecera().getCodTicket();
//				}
//
//	        	((ByLGiftCard)Dispositivos.getInstance().getGiftCard()).crearMovimientosProvisionales(
//	        			stage,
//	        			sesion.getAplicacion().getUidActividad(),
//	        			listaPagosGiftcard,
//	        			ticketPrincipal.getUidTicket(),
//	        			codTicket,
//	        			!esDevolucion);
			}
		}
		catch (RestTimeoutException e) {
			VentanaDialogoComponent.crearVentanaError(I18N.getTexto(e.getMessage()), stage);
			throw new TarjetaRegaloException(e);
		}
		catch (RestException e) {
			VentanaDialogoComponent.crearVentanaError(I18N.getTexto(e.getMessage()), stage);
			throw new TarjetaRegaloException(e);
		}
		catch (RestHttpException e) {
			VentanaDialogoComponent.crearVentanaError(I18N.getTexto(e.getMessage()), stage);
			throw new TarjetaRegaloException(e);
		}
	}

	 protected void anularMovimientoTarjetaRegalo(Stage stage){
        log.trace("anularMovimientoTarjetaRegalo()");

		if (!Dispositivos.getInstance().getGiftCard().getClass().equals(GiftCardNoConfig.class)) {
			final TarjetaRegaloTicket tarjRegalo = ticketPrincipal.getCabecera().getTarjetaRegalo();
			List<MovimientoRequestRest> movList = new ArrayList<MovimientoRequestRest>();
			List<GiftCardBean> listaTarjetasUsadas = new ArrayList<>();
			if (tarjRegalo != null) {
				MovimientoRequestRest mov = new MovimientoRequestRest();
				mov.setNumeroTarjeta(tarjRegalo.getNumTarjetaRegalo());
				mov.setUidTransaccion(tarjRegalo.getUidTransaccion());
				mov.setApiKey(variablesServices.getVariableAsString(VariablesServices.WEBSERVICES_APIKEY));
				mov.setUidActividad(sesion.getAplicacion().getUidActividad());
				mov.setDocumento(ticketPrincipal.getUidTicket());
				mov.setFecha(new Date());
				movList.add(mov);

				EnviarMovimientoGiftCardBean enviarMovimiento = new EnviarMovimientoGiftCardBean();
				enviarMovimiento.setDocumentNumber(ticketPrincipal.getCabecera().getCodTicket());
				enviarMovimiento.setEntrada(ticketPrincipal.getCabecera().getTarjetaRegalo().getImporteRecarga().negate().doubleValue());
				enviarMovimiento.setNumeroTarjeta(ticketPrincipal.getCabecera().getTarjetaRegalo().getNumTarjetaRegalo());
				enviarMovimiento.setTypeCode(0);

				try {
					SpringContext.getBean(ByLGiftCard.class).realizarCancelacionCompraGifcard(enviarMovimiento);
					tarjRegalo.setSaldo(new BigDecimal(0));
				}
				catch (RestException | RestHttpException e) {
					log.error("anularMovimientoTarjetaRegalo() - Ha ocurrido un error al intentar cancelar la recarga/compra de una tarjeta regalo: " + e.getMessage(), e);
					return;
				}
			}
			else {
				List<PagoTicket> pagosTarjetaRegalo = obtenerPagosTarjetaRegalo();
				if (pagosTarjetaRegalo != null && !pagosTarjetaRegalo.isEmpty()) {
					for (PagoTicket pago : pagosTarjetaRegalo) {
						listaTarjetasUsadas.addAll(pago.getGiftcards());
					}
				}
			}
			if (listaTarjetasUsadas != null && !listaTarjetasUsadas.isEmpty()) {
				try {

					String codTicket = ticketPrincipal.getCabecera().getDesTipoDocumento() + " " + ticketPrincipal.getCabecera().getCodTicket();
					if (ticketOrigen != null) {
						codTicket = codTicket + " " + ticketOrigen.getCabecera().getCodTicket();
					}

					Dispositivos.getInstance().getGiftCard().anularMovimientosTarjetaRegalo(stage, sesion.getAplicacion().getUidActividad(), listaTarjetasUsadas, ticketPrincipal.getUidTicket(),
					        codTicket, !esDevolucion, new DispositivoCallback<Void>(){

						        @Override
						        public void onFailure(Throwable caught) {
						        }

						        @Override
						        public void onSuccess(Void result) {
							        if (esRecargaTarjetaRegalo) {
								        tarjRegalo.setSaldoProvisional(tarjRegalo.getSaldoProvisional().subtract(tarjRegalo.getImporteRecarga()));
							        }
							        else if (devolucionTarjetaRegalo) {
								        tarjRegalo.setSaldoProvisional(tarjRegalo.getSaldoProvisional().add(tarjRegalo.getImporteRecarga()));
							        }
						        }
					        }).start();
				}
				catch (Exception ex) {
					log.error("anularMovimientoTarjetaRegalo() - No se han podido anular los movimientos de tarjeta de regalo: " + ex.getMessage(), ex);
				}
			}
		}
	}

	protected BackgroundTask<Void> crearClaseRegistrarTicketTask(Stage stage, SalvarTicketCallback callback, List<DatosRespuestaPagoTarjeta> datosRespuesta) {
		return new ByLRegistrarTicketTask(stage, callback, datosRespuesta);
	}

	protected class ByLRegistrarTicketTask extends BackgroundTask<Void>{

    	protected Stage stage;
    	protected SalvarTicketCallback callback;
    	protected List<DatosRespuestaPagoTarjeta> pagosAutorizados;

		public ByLRegistrarTicketTask(Stage stage, SalvarTicketCallback callback, List<DatosRespuestaPagoTarjeta> pagosAutorizados) {
			this.stage = stage;
			this.callback = callback;
			this.pagosAutorizados = pagosAutorizados;
		}

		@Override
		protected Void call() throws Exception {
			if (esOperacionTarjetaRegalo) {
				procesarTarjetaRegalo(stage);
			}

			redondearImportesTicket();

			Boolean permiteSeguir = Boolean.TRUE;
			/* Impresora fiscal RUSIA SPARK130F */
			if (Dispositivos.getInstance().getImpresora1() instanceof Spark130F) {
				if (Dispositivos.getInstance().getImpresora1().getConfiguracion().getNombreConexion().equals(Spark130F.NOMBRE_CONEXION_FISCAL_PRINTER)) {
					permiteSeguir = operacionVentaDevSpark130F();
				}
			}

			/*
			 * Impresora TM30 con el metodo de conexion TSE
			 */
			if (Dispositivos.getInstance().getImpresora1() instanceof EpsonTM30) {
				if (Dispositivos.getInstance().getImpresora1().getConfiguracion().getNombreConexion().equals(EpsonTSEService.NOMBRE_CONEXION_TSE)) {
					if (((ByLCabeceraTicket) getTicket().getCabecera()).getTse() != null) {
						tseFinishTransaction();
					}
				}
			}

			if (permiteSeguir) {
				procesoRegistrarTicket();
				confirmarPagosTarjeta(pagosAutorizados, stage);
			}

			return null;
		}

		@Override
		protected void failed() {
			super.failed();
			Exception ex = (Exception) getException();

			log.error("salvarTicket() Error salvando ticket : " + ex.getMessage(), ex);

			if(ex instanceof Spark130FException) {
				try {
					List<String> listAtributos = new ArrayList<String>();
					listAtributos.add("RC");
					HashMap<String, String> mapaCampos = spark130FService.getCamposRespuesta(spark130FService.realizarLlamada(spark130FService.cancelLastDocument()), listAtributos);
					String returnCode = mapaCampos.get("RC");
					
					if(returnCode.equals(Spark130FConstants.NO_ERROR)) {
						log.debug("salvarTicket() - Se ha cancelado correctamente el documento");
					}else {
						log.error("salvarTicket() - Ha ocurrido un error al cancelar el documento");
					}
				}
				catch (Spark130FException ignore) {
				}
			}


			if(pagosAutorizados != null && !pagosAutorizados.isEmpty()) {
				anularPagos(pagosAutorizados, stage);
			}
            anularMovimientoTarjetaRegalo(stage);
            anularPromocionesFinales(stage);
			callback.onFailure(ex);
		}

		@Override
		protected void succeeded() {
			super.succeeded();
			callback.onSucceeded();
		}

    }
	 private String crearCodTicketPagoTarjeta(String codTicket) {
		 String numeroTransaccion = codTicket.replaceAll("/", "");
		 return numeroTransaccion;
	 }

	@SuppressWarnings("unused")
	private String crearNumeroTransaccionApdo(String id) {
		String numeroTransaccion = id.replaceAll("[a-zA-Z]", "").replaceAll("/", "");
		log.debug("crearNumeroTransaccionApdo() - Se ha creado el siguiente número de transacción (EM) creado para la operación: " + numeroTransaccion);
		return numeroTransaccion;
    }

	 public void salvarTicketApartado(final ApartadosCabeceraBean cabeceraApartado, final Stage stage, final SalvarTicketCallback callback) throws TicketsServiceException {
		 new SalvarTicketApartadoTask(cabeceraApartado, stage, callback).start();
	 }

	 class SalvarTicketApartadoTask extends BackgroundTask<Void>{
	    	protected ApartadosCabeceraBean cabeceraApartado;
	    	protected Stage stage;
			protected SalvarTicketCallback callback;

			public SalvarTicketApartadoTask(ApartadosCabeceraBean cabeceraApartado, Stage stage, SalvarTicketCallback callback){
				this.cabeceraApartado = cabeceraApartado;
				this.stage = stage;
				this.callback = callback;
			}

			@Override
			protected Void call() throws Exception{
	            return null;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void succeeded(){
				super.succeeded();
		    	final List<PagoTicket> pagosTarjeta = new ArrayList<>();

		    	List<IPagoTicket> pagos = ticketPrincipal.getPagos();
		        for(IPagoTicket pago : pagos){
		            if(Dispositivos.getInstance().getTarjeta().isCodMedPagoAceptado(pago.getCodMedioPago())){
		                pagosTarjeta.add((PagoTicket)pago);
		            }

		            if(Dispositivos.getInstance().getGiftCard().isMedioPagoGiftCard(pago.getCodMedioPago())){
		            	esOperacionTarjetaRegalo = true;
		            }
		        }

				List<DatosPeticionPagoTarjeta> datosPeticionTarjeta = new LinkedList<>();
				/* Generamos los DatosPeticionTarjeta */
				for (PagoTicket pago : pagosTarjeta) {
					/* Igual que en ApartadosService.registrarMovimientoAbonoApartado() */
					String id = "Apartado nº " + cabeceraApartado.getIdApartado();
					if (Dispositivos.getInstance().getTarjeta() instanceof TefConexFlow) {
						ConexFlowDatosPeticionPagoTarjeta datoPeticion = new ConexFlowDatosPeticionPagoTarjeta(id, cabeceraApartado.getIdApartado(), pago.getImporte(),
						        ticketPrincipal.getCabecera().getCodTipoDocumento());
						datosPeticionTarjeta.add(datoPeticion);
					}
					else if (Dispositivos.getInstance().getTarjeta() instanceof AxisManager) {
						AxisDatosPeticionPagoTarjeta datoPeticion = new AxisDatosPeticionPagoTarjeta(id, cabeceraApartado.getIdApartado(), pago.getImporte(),
						        ticketPrincipal.getCabecera().getCodTipoDocumento());
						datosPeticionTarjeta.add(datoPeticion);
					}
					else if (Dispositivos.getInstance().getTarjeta() instanceof TefAdyenCloud) {
						try {
							((ByLTicketsService) ticketsService).asignarIdTransaccion((Ticket<?, ?, ?>) ticketPrincipal);
						}
						catch (TicketsServiceException e) {
							log.error("SalvarTicketApartadoTask() - Ha ocurrido un error en la asignación del contador idTransacción");
						}

						/* ----------------- INICIO ----------------------------- */
						/* ----------------- Para genera el codTicket de reserva y poderlo enviar como merchantReference a Adyen ----------------------------- */
						if ("RE".equals(ticketPrincipal.getCabecera().getCodTipoDocumento())) {
							CabeceraReservaTicket cabecera = new CabeceraReservaTicket();
							cabecera.setUidApartado(cabeceraApartado.getUidApartado());
							log.debug("crearCabeceraTicketReserva() - UidApartado generado : " + cabecera.getUidApartado());
							cabecera.setUidActividad(sesion.getAplicacion().getUidActividad());
							cabecera.setUidDiarioCaja(sesion.getSesionCaja().getUidDiarioCaja());
							/* Sacamos el estado del apartado de el objeto "ApartadoManager" */
							cabecera.setEstadoApartado(cabeceraApartado.getEstadoApartado());
							/* Recorremos el listado de documentos activos para encontrar el documento de Reserva */
							TipoDocumentoBean tipoDocumentoReserva = null;
							Map<Long, TipoDocumentoBean> mapaDocumentos = sesion.getAplicacion().getDocumentos().getDocumentos();
							for (Map.Entry<Long, TipoDocumentoBean> entry : mapaDocumentos.entrySet()) {
								TipoDocumentoBean documento = entry.getValue();
								if (ByLDetalleApartadosController.CODIGO_DOCUMENTO_RESERVA.equals(documento.getCodtipodocumento())) {
									tipoDocumentoReserva = documento;
								}
							}

							if (tipoDocumentoReserva != null) {
								cabecera.setTipoDocumento(tipoDocumentoReserva.getIdTipoDocumento().toString());
								cabecera.setCodigoTipoDocumento(tipoDocumentoReserva.getCodtipodocumento());
								cabecera.setDescripcionTipoDocumento(tipoDocumentoReserva.getDestipodocumento());
								cabecera.setFormatoImpresion(tipoDocumentoReserva.getFormatoImpresion());
							}
							else {
								String mensajeError = "Error al localizar el tipo de documento de Reserva";
								log.error("crearCabeceraTicketReserva() - " + mensajeError);
							}

							/* Calculamos la serie del Ticket a partir de los siguientes datos */
							Map<String, String> parametrosContador = new HashMap<>();
							parametrosContador.put("CODEMP", sesion.getAplicacion().getEmpresa().getCodEmpresa());
							parametrosContador.put("CODTIPODOCUMENTO", cabecera.getCodigoTipoDocumento());
							parametrosContador.put("CODSERIE", sesion.getAplicacion().getTienda().getAlmacenBean().getCodAlmacen());
							parametrosContador.put("CODCAJA", sesion.getSesionCaja().getCajaAbierta().getCodCaja());
							parametrosContador.put("PERIODO", ((new Fecha()).getAño().toString()));
							try {
								/* Generamos el ID del apartado a partir del contador */
								ContadorBean ticketContador = contadoresService.obtenerContador(tipoDocumentoReserva.getIdContador(), parametrosContador, cabecera.getUidActividad());

								cabecera.setSerieTicket(ticketContador.getConfigContador().getValorDivisor3());

								/* Rellenamos el IdReserva con el serie Ticket delante */
								id = StringUtils.leftPad(cabeceraApartado.getIdApartado().toString(), 6, "0");
								id = cabecera.getSerieTicket() + id;

							}
							catch (ContadorServiceException e) {
								String mensajeError = "Error al consultar el contador para el Ticket de Reserva";
								log.error("crearCabeceraTicketReserva() - " + mensajeError + " - " + e.getMessage());
							}
						}
						
						/* ----------------- FIN ----------------------------- */ 

						AdyenDatosPeticionPagoTarjeta datoPeticion = new AdyenDatosPeticionPagoTarjeta(((ByLCabeceraTicket) ticketPrincipal.getCabecera()).getIdTransaccion(), id, pago.getImporte(),
						        ticketPrincipal.getCabecera().getCodTipoDocumento());
						datosPeticionTarjeta.add(datoPeticion);
					}
				}

		        TarjetaCallback<List<DatosRespuestaPagoTarjeta>> tarjetaCallback = new TarjetaCallback<List<DatosRespuestaPagoTarjeta>>(){

					@Override
				public void onSuccess(List<DatosRespuestaPagoTarjeta> datosRespuesta) {
					BigDecimal saldoAntes = cabeceraApartado.getSaldoCliente();
					try {
						// Asignamos los datos respuesta a los pagos
						for (DatosRespuestaPagoTarjeta datoRespuesta : datosRespuesta) {
							pagosTarjeta.get(datosRespuesta.indexOf(datoRespuesta)).setDatosRespuestaPagoTarjeta(datoRespuesta);
						}
						if (esOperacionTarjetaRegalo) {
							// INICIO Incidencia #71
							if ("RE".equals(ticketPrincipal.getCabecera().getCodTipoDocumento())) {
								CabeceraReservaTicket cabecera = new CabeceraReservaTicket();
								cabecera.setUidApartado(cabeceraApartado.getUidApartado());
								log.debug("crearCabeceraTicketReserva() - UidApartado generado : " + cabecera.getUidApartado());
								cabecera.setUidActividad(sesion.getAplicacion().getUidActividad());
								cabecera.setUidDiarioCaja(sesion.getSesionCaja().getUidDiarioCaja());
								/* Sacamos el estado del apartado de el objeto "ApartadoManager" */
								cabecera.setEstadoApartado(cabeceraApartado.getEstadoApartado());

								/* Recorremos el listado de documentos activos para encontrar el documento de Reserva */
								TipoDocumentoBean tipoDocumentoReserva = null;
								Map<Long, TipoDocumentoBean> mapaDocumentos = sesion.getAplicacion().getDocumentos().getDocumentos();
								for (Map.Entry<Long, TipoDocumentoBean> entry : mapaDocumentos.entrySet()) {
									TipoDocumentoBean documento = entry.getValue();
									if (ByLDetalleApartadosController.CODIGO_DOCUMENTO_RESERVA.equals(documento.getCodtipodocumento())) {
										tipoDocumentoReserva = documento;
									}
								}
								
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
								parametrosContador.put("CODCAJA", sesion.getSesionCaja().getCajaAbierta().getCodCaja());
								parametrosContador.put("PERIODO", ((new Fecha()).getAño().toString()));
								try {
									/* Generamos el ID del apartado a partir del contador */
									ContadorBean ticketContador = contadoresService.obtenerContador(tipoDocumentoReserva.getIdContador(), parametrosContador, cabecera.getUidActividad());

									cabecera.setSerieTicket(ticketContador.getConfigContador().getValorDivisor3());

									/* Rellenamos el IdReserva con el serie Ticket delante */
									String idReserva = StringUtils.leftPad(cabeceraApartado.getIdApartado().toString(), 6, "0");
									idReserva = cabecera.getSerieTicket() + idReserva;

									ticketPrincipal.getCabecera().setCodTicket(idReserva);
								}
								catch (ContadorServiceException e) {
									String mensajeError = "Error al consultar el contador para el Ticket de Reserva";
									log.error("crearCabeceraTicketReserva() - " + mensajeError + " - " + e.getMessage());
									throw new TarjetaRegaloException(mensajeError, e);
								}
								// FIN Incidencia #71
							}
							procesarTarjetaRegalo(stage);

						}
						((Ticket) ticketPrincipal).getCabecera().setTienda(sesion.getAplicacion().getTienda());
						((Ticket) ticketPrincipal).getCabecera().setEmpresa(sesion.getAplicacion().getEmpresa());
						((Ticket) ticketPrincipal).setFecha(new Date());
						((Ticket) ticketPrincipal).setIdTicket(cabeceraApartado.getIdApartado());

						cabeceraApartado.setSaldoCliente(cabeceraApartado.getSaldoCliente().add(ticketPrincipal.getTotales().getTotalAPagar()));

						apartadosService.registrarMovimientosPagosApartado((Ticket) ticketPrincipal, ((TicketVenta) ticketPrincipal).getPagos(), cabeceraApartado);
						confirmarPagosTarjeta(datosRespuesta, stage);

						callback.onSucceeded();
					}
					catch (Exception e) {
						cabeceraApartado.setSaldoCliente(saldoAntes);
						anularPagos(datosRespuesta, stage);
						anularMovimientoTarjetaRegalo(stage);
						anularPromocionesFinales(stage);

						callback.onFailure(e);
					}
				}

		        	@Override
		        	public void onFailure(List<DatosRespuestaPagoTarjeta> datosRespuesta, Throwable caught){
			            anularPagos(datosRespuesta, stage);
			            anularMovimientoTarjetaRegalo(stage);
			            anularPromocionesFinales(stage);

						callback.onFailure((Exception) caught);
		        	}
		        };

		        Dispositivos.getInstance().getTarjeta().solicitarPagosTarjeta(datosPeticionTarjeta, stage, tarjetaCallback);

			}

			@Override
			protected void failed(){
				super.failed();
				Throwable ex = getException();
	            callback.onFailure((Exception) ex);
			}

	 }

	 protected void anularPagos(List<DatosRespuestaPagoTarjeta> datosRespuesta, final Stage stage){
			List<DatosPeticionPagoTarjeta> datosPeticionTarjeta = new LinkedList<>();
			for(DatosRespuestaPagoTarjeta datoRespuesta : datosRespuesta){
				if(datoRespuesta != null){
					if(Dispositivos.getInstance().getTarjeta() instanceof TefConexFlow){

						ConexFlowDatosPeticionPagoTarjeta datosPeticionOrig = (ConexFlowDatosPeticionPagoTarjeta) datoRespuesta.getDatosPeticion();
						ConexFlowDatosPeticionPagoTarjeta datoPeticion = new ConexFlowDatosPeticionPagoTarjeta(datosPeticionOrig.getIdTransaccion(),
								datosPeticionOrig.getIdDocumento(), datosPeticionOrig.getImporte(), datosPeticionOrig.getCodTipoDocumento());

						datoPeticion.setCodAutorizacion(datoRespuesta.getCodAutorizacion());
						datoPeticion.setNumOpBanco(datoRespuesta.getNumOperacionBanco());
						datoPeticion.setNumOperacion(datoRespuesta.getNumOperacion());
						datosPeticionTarjeta.add(datoPeticion);

					}
					else if (Dispositivos.getInstance().getTarjeta() instanceof AxisManager) {

						AxisDatosPeticionPagoTarjeta datosPeticionOrig = (AxisDatosPeticionPagoTarjeta) datoRespuesta.getDatosPeticion();
						AxisDatosPeticionPagoTarjeta datoPeticion = new AxisDatosPeticionPagoTarjeta(datosPeticionOrig.getIdTransaccion(), datosPeticionOrig.getIdDocumento(),
						        datosPeticionOrig.getImporte(), datosPeticionOrig.getCodTipoDocumento());

						datoPeticion.setCodAutorizacion(datoRespuesta.getCodAutorizacion());
						datoPeticion.setNumOpBanco(datoRespuesta.getNumOperacionBanco());
						datoPeticion.setNumOperacion(datoRespuesta.getNumOperacion());
						datosPeticionTarjeta.add(datoPeticion);
					}
					else {
						DatosPeticionPagoTarjeta datosPeticionOrig = datoRespuesta.getDatosPeticion();
						DatosPeticionPagoTarjeta datosPeticion = new DatosPeticionPagoTarjeta(datosPeticionOrig.getIdTransaccion(), datosPeticionOrig.getIdDocumento(), datosPeticionOrig.getImporte());
						datosPeticion.setCodAutorizacion(datoRespuesta.getCodAutorizacion());
						datosPeticion.setNumOpBanco(datoRespuesta.getNumOperacionBanco());
						datosPeticion.setNumOperacion(datoRespuesta.getNumOperacion());
						datosPeticionTarjeta.add(datosPeticion);
					}
				}
			}

			log.info("anularPagos() - Anulando pagos: " + datosPeticionTarjeta);
	        TarjetaCallback<List<DatosRespuestaPagoTarjeta>> dispositivoCallback = new TarjetaCallback<List<DatosRespuestaPagoTarjeta>>() {
				public void onSuccess(List<DatosRespuestaPagoTarjeta> result) {
					log.info("anularPagos() - Pagos anulados");
				}
				@Override
				public void onFailure(List<DatosRespuestaPagoTarjeta> result, Throwable caught) {
					log.fatal("anularPagos() - Ha ocurrido un error al anular pagos", caught);
					if (caught instanceof TarjetaException) {
						VentanaDialogoComponent.crearVentanaError(stage, caught.getMessage(), caught);
					} else {
						VentanaDialogoComponent.crearVentanaError(stage, I18N.getTexto("Ha ocurrido un error al anular los pagos"), caught);
					}
				}
			};

			boolean esVenta = (!documentos.isDocumentoAbono(ticketPrincipal.getCabecera().getCodTipoDocumento()));

	        if("RE".equals(ticketPrincipal.getCabecera().getCodTipoDocumento()) && datosPeticionTarjeta != null && !datosPeticionTarjeta.isEmpty()) {
	        	Dispositivos.getInstance().getTarjeta().solicitarAnulacionPago(datosPeticionTarjeta, stage, dispositivoCallback);
	        }
	        else if(esVenta && datosPeticionTarjeta != null && !datosPeticionTarjeta.isEmpty()) {
	        	Dispositivos.getInstance().getTarjeta().solicitarAnulacionPago(datosPeticionTarjeta, stage, dispositivoCallback);
	        }
	        else if(datosPeticionTarjeta != null && !datosPeticionTarjeta.isEmpty()){
	        	Dispositivos.getInstance().getTarjeta().solicitarAnulacionDevolucion(datosPeticionTarjeta, stage, dispositivoCallback);
	        }
	    }

	    protected void confirmarPagosTarjeta(List<DatosRespuestaPagoTarjeta> datosRespuesta, final Stage stage){
	        List<DatosPeticionPagoTarjeta> datosPeticionTarjeta = new LinkedList<>();
	        for(DatosRespuestaPagoTarjeta datoRespuesta : datosRespuesta){
	        	if(Dispositivos.getInstance().getTarjeta() instanceof TefConexFlow){

	        		ConexFlowDatosPeticionPagoTarjeta datosPeticionOrig = (ConexFlowDatosPeticionPagoTarjeta) datoRespuesta.getDatosPeticion();
					ConexFlowDatosPeticionPagoTarjeta datoPeticion = new ConexFlowDatosPeticionPagoTarjeta(datosPeticionOrig.getIdTransaccion(),
							datosPeticionOrig.getIdDocumento(), datosPeticionOrig.getImporte(), datosPeticionOrig.getCodTipoDocumento());

					datoPeticion.setCodAutorizacion(datoRespuesta.getCodAutorizacion());
					datoPeticion.setNumOpBanco(datoRespuesta.getNumOperacionBanco());
					datoPeticion.setNumOperacion(datoRespuesta.getNumOperacion());
		            datosPeticionTarjeta.add(datoPeticion);

				}else if(Dispositivos.getInstance().getTarjeta() instanceof AxisManager){

					AxisDatosPeticionPagoTarjeta datosPeticionOrig = (AxisDatosPeticionPagoTarjeta) datoRespuesta.getDatosPeticion();
					AxisDatosPeticionPagoTarjeta datoPeticion = new AxisDatosPeticionPagoTarjeta(datosPeticionOrig.getIdTransaccion(),
							datosPeticionOrig.getIdDocumento(), datosPeticionOrig.getImporte(), datosPeticionOrig.getCodTipoDocumento());

					datoPeticion.setCodAutorizacion(datoRespuesta.getCodAutorizacion());
					datoPeticion.setNumOpBanco(datoRespuesta.getNumOperacionBanco());
					datoPeticion.setNumOperacion(datoRespuesta.getNumOperacion());
		            datosPeticionTarjeta.add(datoPeticion);

				}
				else {
					DatosPeticionPagoTarjeta datosPeticionOrig = datoRespuesta.getDatosPeticion();
					DatosPeticionPagoTarjeta datosPeticion = new DatosPeticionPagoTarjeta(datosPeticionOrig.getIdTransaccion(), datosPeticionOrig.getIdDocumento(), datosPeticionOrig.getImporte());
					datosPeticion.setCodAutorizacion(datoRespuesta.getCodAutorizacion());
					datosPeticion.setNumOpBanco(datoRespuesta.getNumOperacionBanco());
					datosPeticion.setNumOperacion(datoRespuesta.getNumOperacion());
					datosPeticionTarjeta.add(datosPeticion);
				}
	        }

	        log.info("confirmarPagosTarjeta() - Confirmando pagos con tarjeta: " + datosPeticionTarjeta);
	        TarjetaCallback<List<DatosRespuestaPagoTarjeta>> dispositivoCallback = new TarjetaCallback<List<DatosRespuestaPagoTarjeta>>(){
	            public void onSuccess(List<DatosRespuestaPagoTarjeta> result){
	                log.info("confirmarPagosTarjeta() - Pagos confirmados");
	            }
	            @Override
	            public void onFailure(List<DatosRespuestaPagoTarjeta> result, Throwable caught){
	                log.fatal("confirmarPagosTarjeta() - Ha ocurrido un error al confirmar los pagos con tarjeta de crédito", caught);
	                VentanaDialogoComponent.crearVentanaError(stage, I18N.getTexto("Ha ocurrido un error al confirmar los pagos con tarjeta de crédito"), caught);
	            }
	        };

	        boolean esVenta = (!documentos.isDocumentoAbono(ticketPrincipal.getCabecera().getCodTipoDocumento()));

	        if("RE".equals(ticketPrincipal.getCabecera().getCodTipoDocumento())){
	            Dispositivos.getInstance().getTarjeta().solicitarConfirmacionPagos(datosPeticionTarjeta, stage, dispositivoCallback);
	        }else if(esVenta){
	            Dispositivos.getInstance().getTarjeta().solicitarConfirmacionPagos(datosPeticionTarjeta, stage, dispositivoCallback);
	        }else{
	            Dispositivos.getInstance().getTarjeta().solicitarConfirmacionDevolucion(datosPeticionTarjeta, stage, dispositivoCallback);
	        }
	    }

		protected void recuperarDatosPersonalizados(TicketVenta ticketRecuperado){
			ticketPrincipal.getCabecera().setIdTicket(null);
		}

		public int getIntentosDevolucionFallido() {
			return intentosDevolucionFallido;
		}

		public void setIntentosDevolucionFallido(int intentosDevolucionFallido) {
			this.intentosDevolucionFallido = intentosDevolucionFallido;
		}

		@Override
		public Boolean getEsTicketRegalo() {
			return esTicketRegalo;
		}

		public void setEsTicketRegalo(Boolean esTicketRegalo) {
		this.esTicketRegalo = esTicketRegalo;
	}
	
		
	/**
	 * Crea un nuevo Ticket para una reserva 
	 * @throws PromocionesServiceException
	 * @throws DocumentoException
	 */
	@SuppressWarnings("unchecked")
	public void nuevoTicketReserva() throws PromocionesServiceException, DocumentoException{
    	log.debug("nuevoTicketApartado() - Creando nuevo ticket de Reserva...");
    	
        documentoActivo = sesion.getAplicacion().getDocumentos().getDocumento(ByLDetalleApartadosController.CODIGO_DOCUMENTO_RESERVA);
        ticketPrincipal = SpringContext.getBean(ByLTicketPagosApartado.class);
        ticketPrincipal.getCabecera().inicializarCabecera(ticketPrincipal);
        ((ByLTicketPagosApartado)ticketPrincipal).inicializarTotales();
        ticketPrincipal.setCliente(sesion.getAplicacion().getTienda().getCliente().clone());
        ticketPrincipal.setCajero(sesion.getSesionUsuario().getUsuario());
        ticketPrincipal.getCabecera().getTotales().setCambio(SpringContext.getBean(PagoTicket.class , MediosPagosService.medioPagoDefecto));
        ticketPrincipal.getCabecera().setDocumento(documentoActivo);

        devolucionTarjetaRegalo = false;
        esRecargaTarjetaRegalo = false;
        esOperacionTarjetaRegalo = false;
        esDevolucion = false;
        ticketPrincipal.setEsDevolucion(false);
        
        log.debug("nuevoTicketApartado() - Finalizado el nuevo ticket de Reserva");
    }
	
	@SuppressWarnings("unchecked")
    protected void descontarLineasNegativasTicketOrigen() {
    	List<LineaTicket> lineasNegativas = new ArrayList<LineaTicket>();
    	Iterator<LineaTicket> it = ticketOrigen.getLineas().iterator();
    	while(it.hasNext()) {
    		LineaTicket linea = it.next();
    		if(BigDecimalUtil.isMenorACero(linea.getImporteTotalConDto())){
    			lineasNegativas.add(linea);
    			it.remove();
    		}
    	}
    	for(LineaTicket lineaNegativa : lineasNegativas){
    		BigDecimal cantidadRestante = lineaNegativa.getCantidad().abs();    	
    		if(lineaNegativa.getLineaDocumentoOrigen() != null){
    			continue;
    		}
	    	for(LineaTicket linea : (List<LineaTicket>) ticketOrigen.getLineas()){
				if (linea.getCodArticulo().equals(lineaNegativa.getCodArticulo()) && linea.getDesglose1().equals(lineaNegativa.getDesglose1())
				        && linea.getDesglose2().equals(lineaNegativa.getDesglose2()) && BigDecimalUtil.isIgual(
				        		linea.getImporteTotalConDto(), lineaNegativa.getImporteTotalConDto().abs())){
	    			BigDecimal cantidadSobrante = linea.getCantidad().subtract(cantidadRestante);
	    			
	    			if(BigDecimalUtil.isMenorACero(cantidadSobrante)){
	    				linea.setCantidadDevuelta(linea.getCantidad());
	    				cantidadRestante = cantidadRestante.add(cantidadSobrante);
	    			}
	    			else {
	    				linea.setCantidadDevuelta(lineaNegativa.getCantidad().abs());
	    				break;
	    			}
	    		}
	    	}
    	}
    }
	
	@Override
	protected void asignarLineasDevueltas(ResponseGetTicketDev res) {
		log.debug("asignarLineasDevueltas() - Actualizamos precio de lineas origen restando importe de promociones de tipo menos ingreso");
		@SuppressWarnings("unchecked")
		List<LineaTicketAbstract> lineas = ticketOrigen.getLineas();
		SesionImpuestos sesionImpuestos = sesion.getImpuestos();
		BigDecimal importeLineas = BigDecimal.ZERO; 
		BigDecimal importeLineasOriginal = BigDecimal.ZERO; 
		Boolean promocionMenosIngreso = variablesServices.getVariableAsBoolean(VariablesServices.TPV_TRATAR_PROMOCIONES_MENOS_INGRESO);
		
		//calculamos el precio origen restando el precio de las promociones de menos ingreso
		for (LineaTicketAbstract lineaOrigen : lineas) {
			
			if(promocionMenosIngreso && !BigDecimalUtil.isIgualACero(lineaOrigen.getImporteTotalPromocionesMenosIngreso())){
                //Calculamos el precioSinDto 
                BigDecimal importeSinPromocionesMenosIngreso = lineaOrigen.getImporteTotalConDto().subtract(lineaOrigen.getImporteTotalPromocionesMenosIngreso()); 
                BigDecimal precioSinPromocionesMenosIngreso =  importeSinPromocionesMenosIngreso.divide(lineaOrigen.getCantidad(), 6, RoundingMode.HALF_UP);
                
                //Actualizamos todas los demás precios e importes a partir del precioSinDto 
                lineaOrigen.setPrecioTotalSinDto(precioSinPromocionesMenosIngreso); 
                BigDecimal precioSinImpuestos = sesionImpuestos.getPrecioSinImpuestos(lineaOrigen.getCodImpuesto(), precioSinPromocionesMenosIngreso, lineaOrigen.getCabecera().getCliente().getIdTratImpuestos());
                
              //Actualizamos todas los demás precios e importes a partir del precioSinDto
    			lineaOrigen.setPrecioSinDto(precioSinImpuestos);
    			lineaOrigen.setPrecioConDto(lineaOrigen.getPrecioSinDto());
    			lineaOrigen.setPrecioTotalConDto(lineaOrigen.getPrecioTotalSinDto());
    			lineaOrigen.setImporteConDto(BigDecimalUtil.redondear(lineaOrigen.getPrecioConDto().multiply(lineaOrigen.getCantidad()))); 
                lineaOrigen.setImporteTotalConDto(BigDecimalUtil.redondear(lineaOrigen.getPrecioTotalConDto().multiply(lineaOrigen.getCantidad())));
			}
			else{
				//igualmos los precios sin DTO a los precios con DTO, en devoluciones no hay descuentos 
				lineaOrigen.setPrecioTotalConDto(lineaOrigen.getImporteTotalConDto().setScale(6, BigDecimal.ROUND_HALF_UP).divide(lineaOrigen.getCantidad().setScale(6, BigDecimal.ROUND_HALF_UP),BigDecimal.ROUND_HALF_UP)); 
				lineaOrigen.setPrecioTotalSinDto(lineaOrigen.getPrecioTotalConDto()); 
				lineaOrigen.setPrecioSinDto(lineaOrigen.getPrecioConDto()); 
				
				importeLineasOriginal = importeLineasOriginal.add(lineaOrigen.getImporteTotalConDto());
			}
			
			importeLineas = importeLineas.add(lineaOrigen.getImporteTotalConDto());
		}
		
		if(promocionMenosIngreso){
            //Añadimos a la ultima linea la diferencia entre el total de la cabecera del ticket y el total de precio de las lineas 
			if(!lineas.isEmpty()){
				BigDecimal totalTicket = ticketOrigen.getCabecera().getTotales().getTotalAPagar().subtract(ticketOrigen.getCabecera().getTotales().getTotalPromocionesCabecera()); 
				BigDecimal diferenciaImportes = totalTicket.subtract(importeLineas);
				
				int i = 1;
				LineaTicketAbstract ultimaLinea = lineas.get(lineas.size()-i);
				BigDecimal precioTotalSinDto = ultimaLinea.getPrecioTotalSinDto().add(diferenciaImportes.divide(ultimaLinea.getCantidad(), 2, RoundingMode.HALF_UP));
				while(BigDecimalUtil.isMenorACero(precioTotalSinDto)) {
					i++;
					ultimaLinea = lineas.get(lineas.size()-i);
					precioTotalSinDto = ultimaLinea.getPrecioTotalSinDto().add(diferenciaImportes.divide(ultimaLinea.getCantidad(), 2, RoundingMode.HALF_UP));
				}
				
				Long idTratamientoImpuesto = ultimaLinea.getCabecera().getCliente().getIdTratImpuestos() == null ? 1 : ultimaLinea.getCabecera().getCliente().getIdTratImpuestos();
				BigDecimal precioSinImpuestos = sesionImpuestos.getPrecioSinImpuestos(ultimaLinea.getCodImpuesto(), ultimaLinea.getPrecioTotalSinDto(), idTratamientoImpuesto);
				
				ultimaLinea.setPrecioTotalSinDto(ultimaLinea.getPrecioTotalSinDto().add(diferenciaImportes.divide(ultimaLinea.getCantidad(), 2, RoundingMode.HALF_UP)));
				ultimaLinea.setPrecioSinDto(precioSinImpuestos);
				ultimaLinea.setPrecioConDto(ultimaLinea.getPrecioSinDto());
				ultimaLinea.setPrecioTotalConDto(ultimaLinea.getPrecioTotalSinDto());
                ultimaLinea.setImporteConDto(ultimaLinea.getPrecioConDto().multiply(ultimaLinea.getCantidad())); 
                ultimaLinea.setImporteTotalConDto(ultimaLinea.getPrecioTotalConDto().multiply(ultimaLinea.getCantidad()));
				
			}
		}
		else{
			//Añadimos a la ultima linea la diferencia entre la suma de importes de las lineas del ticket original y la suma de importes de las lineas del ticket de devolución
			BigDecimal diferenciaImportes = importeLineasOriginal.subtract(importeLineas);
			if(!lineas.isEmpty() && !BigDecimalUtil.isIgualACero(diferenciaImportes)){
				LineaTicketAbstract ultimaLinea = lineas.get(lineas.size()-1);
				ultimaLinea.setPrecioTotalSinDto(ultimaLinea.getPrecioTotalSinDto().add(diferenciaImportes));
				
				Long idTratamientoImpuesto = ultimaLinea.getCabecera().getCliente().getIdTratImpuestos() == null ? 1 : ultimaLinea.getCabecera().getCliente().getIdTratImpuestos();
				BigDecimal precioSinImpuestos = sesionImpuestos.getPrecioSinImpuestos(ultimaLinea.getCodImpuesto(), ultimaLinea.getPrecioTotalSinDto(), idTratamientoImpuesto);
				
				ultimaLinea.setPrecioSinDto(precioSinImpuestos);
				ultimaLinea.setPrecioConDto(ultimaLinea.getPrecioSinDto());
				ultimaLinea.setPrecioTotalConDto(ultimaLinea.getPrecioTotalSinDto());
				ultimaLinea.setImporteConDto(BigDecimalUtil.redondear(ultimaLinea.getPrecioConDto().multiply(ultimaLinea.getCantidad())));
				ultimaLinea.setImporteTotalConDto(BigDecimalUtil.redondear(ultimaLinea.getPrecioTotalConDto().multiply(ultimaLinea.getCantidad())));
			}
		}

		
		//Si tenemos datos devolución los usamos
    	if(res != null){
    		log.debug("asignarLineasDevueltas() - Sí hemos recibido datos de devolución, actualizamos precios de líneas origen");
	        List<ArticulosDevueltosBean> lineasDevolucion = res.getLineas();
	        for(ArticulosDevueltosBean articulo: lineasDevolucion){
	            
	            LineaTicketAbstract linea = (LineaTicketAbstract) ticketOrigen.getLinea(articulo.getLinea());
	            if(linea!=null){
	            	linea.setCantidadDevuelta(new BigDecimal(articulo.getCantidadDevuelta()).setScale(2, RoundingMode.HALF_UP));
	                linea.setPrecioTotalConDto(new BigDecimal(articulo.getPrecioTotal()).setScale(2, RoundingMode.HALF_UP));
	            }
	        }
        }
    }

	public Boolean getDevolucionSinTicket() {
		return devolucionSinTicket;
	}

	public void setDevolucionSinTicket(Boolean devolucionSinTicket) {
		this.devolucionSinTicket = devolucionSinTicket;
	}
	
    public boolean comprobarConfigContador(){
    	return comprobarConfigContador(null);
    }
    
	public boolean comprobarConfigContador(String codDocumento) {
		boolean res = true;

		try {
//			TipoDocumentoBean tipoDocumento = documentos.getDocumento(ticketPrincipal.getCabecera().getTipoDocumento());
			String tipoDocumento = codDocumento != null ? codDocumento : ticketPrincipal.getCabecera().getCodTipoDocumento();
			TipoDocumentoBean documento = sesion.getAplicacion().getDocumentos().getDocumento(tipoDocumento);

			PropiedadDocumentoBean propiedadClaseProcesamiento = documento.getPropiedades().get(ByLTicketsService.CONTADOR_ADICIONAL);

			if (propiedadClaseProcesamiento != null && propiedadClaseProcesamiento.getValor() != null && !propiedadClaseProcesamiento.getValor().isEmpty()) {
				String idContadorFiscal = propiedadClaseProcesamiento.getValor();

				ByLConfigContadorBean confContador = ByLServicioConfigContadoresImpl.get().consultar(idContadorFiscal);
				if (!confContador.isRangosCargados()) {
					ConfigContadorRangoExample example = new ConfigContadorRangoExample();
					example.or().andIdContadorEqualTo(idContadorFiscal);
					example.setOrderByClause(ConfigContadorRangoExample.ORDER_BY_RANGO_INICIO + ", " + ConfigContadorRangoExample.ORDER_BY_RANGO_FIN + ", "
					        + ConfigContadorRangoExample.ORDER_BY_RANGO_FECHA_INICIO + ", " + ConfigContadorRangoExample.ORDER_BY_RANGO_FECHA_FIN);
					List<ConfigContadorRango> rangos = ServicioConfigContadoresRangos.get().consultar(example);

					confContador.setRangos(rangos);
					confContador.setRangosCargados(true);
				}
				// Si tiene rangos se aplica la lógica de rangos
				if (!confContador.getRangos().isEmpty()) {
					res = comprobarRangoActivo(documento, confContador);
				}
			}
		}
		catch (Exception e) {
			log.error("comprobarConfigContador() - Ha ocurrido un error al comprobar las vigencias del ticket", e);
			res = false;
		}

		return res;
	}
    
    protected boolean comprobarRangoActivo(TipoDocumentoBean documento, ByLConfigContadorBean confContador) throws ConfigContadoresRangosConstraintViolationException, ConfigContadoresRangosException, ConfigContadoresParametrosException {
    	boolean res = true;
    	Map<String, String> parametrosContador = new HashMap<>();
    	Map<String, String> condicionesVigencias = new HashMap<>();
        parametrosContador.put(PARAMETRO_CODEMP,sesion.getAplicacion().getEmpresa().getCodEmpresa());
        parametrosContador.put(PARAMETRO_CODALM,sesion.getAplicacion().getTienda().getAlmacenBean().getCodAlmacen());
        parametrosContador.put(PARAMETRO_CODSERIE,sesion.getAplicacion().getTienda().getAlmacenBean().getCodAlmacen());
        parametrosContador.put(PARAMETRO_CODCAJA,sesion.getAplicacion().getCodCaja());
        parametrosContador.put(PARAMETRO_CODDOC,documento.getCodtipodocumento());
        parametrosContador.put(PARAMETRO_PERIODO,((new Fecha()).getAño().toString()));
        
        condicionesVigencias.put(ConfigContadorRango.VIGENCIA_CODCAJA,sesion.getAplicacion().getCodCaja());
        condicionesVigencias.put(ConfigContadorRango.VIGENCIA_CODALM,sesion.getAplicacion().getCodAlmacen());
        condicionesVigencias.put(ConfigContadorRango.VIGENCIA_CODEMP,sesion.getAplicacion().getEmpresa().getCodEmpresa());
        
		try {
			contador = byLServicioContadores.consultarContadorActivo(confContador, parametrosContador,condicionesVigencias, sesion.getAplicacion().getUidActividad(), false);
		}catch (ContadorServiceException | ConfigContadoresParametrosException e) {
			log.warn("comprobarRangoActivo(): No se pudo obtener un contador disponible");
		}
    	if(contador==null||contador.getError()!=null){
    		res = false;
    	}
		return res;
	}

	public void notificarContadores() {
		comprobarConfigContador();
//		if (contador != null && ticketPrincipal != null && ((ByLCabeceraTicket) ticketPrincipal.getCabecera()).getConfigContadorRango() != null) {
		if (contador != null && ticketPrincipal != null) {
			Long valorUltimoContador = contador.getValor();
			Long rangoSiguienteAviso = contador.getRangoSiguienteAviso();
			if (rangoSiguienteAviso != null
			        && (rangoSiguienteAviso <= valorUltimoContador || (contador.getConfigContadorRango() != null && valorUltimoContador >= contador.getConfigContadorRango().getRangoFin()))) {
				TipoDocumentoBean documento = null;
				try {
					documento = sesion.getAplicacion().getDocumentos().getDocumento(ticketPrincipal.getCabecera().getTipoDocumento());
				}
				catch (DocumentoException e) {
					log.error("notificarContadores() - Ha occurido un error al recuperar el tipo de documento");
				}

				Long idFiscalDisponibles = contador.getConfigContadorRango().getRangoFin() - valorUltimoContador;

				String msg = I18N.getTexto("{0}: Quedan disponibles {1} identificadores fiscales disponibles. Contacte con el Administrador.", documento.getDestipodocumento(), idFiscalDisponibles);

				Notificacion notif = new Notificacion(msg, Tipo.WARN);
				Notificaciones.get().addNotification(notif);
			}
		}
	}

	public ByLContadorBean getContador() {
		return contador;
	}
	
	public void crearVentanaErrorContador(Stage stage) {
		String msg = I18N.getTexto("No es posible emitir un documento por caducidad en fecha o nº secuencia asociada.");
		if(contador!=null && ByLContadorBean.ERROR_FECHAS.equals(contador.getError())){
			msg = I18N.getTexto("No es posible emitir un documento por caducidad en fecha");
		}else if(contador!=null && ByLContadorBean.ERROR_RANGOS.equals(contador.getError())){
			msg = I18N.getTexto("No es posible emitir un documento por caducidad en nº secuencia asociada.");
		}else if(contador != null && ByLContadorBean.ERROR_CONTADOR_INVALIDO.equals(contador.getError())){
			msg = I18N.getTexto("No es posible emitir un documento por tener el contador un valor inválido.");
		}else if(contador != null && ByLContadorBean.ERROR_PARAMETRO_INVALIDO.equals(contador.getError())){
			msg = I18N.getTexto("No es posible emitir un documento por tener uno de los parámetros un valor inválido. Revise el log.");
		}else if(contador != null && ByLContadorBean.ERROR_SALTO_NUMERACION.equals(contador.getError())){
			msg = I18N.getTexto("No es posible emitir un documento porque hay un salto en la numeración del contador. Revise la configuración de la vigencia asociada.");
		}
		VentanaDialogoComponent.crearVentanaError(msg, stage);
    }

    @Override
	public synchronized LineaTicket nuevaLineaArticulo(String codArticulo, String desglose1, String desglose2, BigDecimal cantidad, boolean pesarArticulo, Integer idLineaDocOrigen,
	        boolean esLineaDevolucionPositiva) throws LineaTicketException {
		log.debug("nuevaLineaArticulo() - Creando nueva línea de artículo...");
		boolean isCupon = false;
		ByLLineaTicketProfesional linea = null;
		BigDecimal precio = null;

		String codBarras = null;
		// Comprobamos si es codigo de barras especial o normal y actualizamos codigoArticulo y otras variables
		try {
			CodigoBarrasBean codBarrasEspecial = codBarrasEspecialesServices.esCodigoBarrasEspecial(codArticulo);

			if (codBarrasEspecial != null) {

				codBarras = codArticulo;

				// Ponemos la variable a falsa ya que se cogerá el peso del código de barras
				pesarArticulo = false;

				if (codBarrasEspecial.getCodticket() != null) {
					return tratarCodigoBarraEspecialTicket(codBarrasEspecial);
				}

				codArticulo = codBarrasEspecial.getCodart();
				String cantCodBar = codBarrasEspecial.getCantidad();
				if (cantCodBar != null) {
					cantidad = FormatUtil.getInstance().desformateaBigDecimal(cantCodBar, 3);
				}
				else {
					cantidad = BigDecimal.ONE;
				}
				String precioCodBar = codBarrasEspecial.getPrecio();
				if (precioCodBar != null) {
					precio = FormatUtil.getInstance().desformateaBigDecimal(codBarrasEspecial.getPrecio(), 2);
				}
				else {
					precio = null;
				}

				if (codArticulo == null) {
					log.error(String.format("nuevaLineaArticulo() - El código de barra especial obtenido no es válido. CodArticulo: %s, cantidad: %s, precio: %s", codArticulo, cantidad, precio));
					throw new LineaTicketException(I18N.getTexto("Error procesando el código de barras. Revise configuración."));
				}
			}
		}
		catch (LineaTicketException e) {
			throw e;
		}
		catch (Exception e) {
			log.error("Error procesando el código de barras especial : " + codArticulo, e);
			throw new LineaTicketException(I18N.getTexto("Error procesando el código de barras. Revise configuración."));
		}

		try {
			linea = (ByLLineaTicketProfesional) lineasTicketServices.createLineaArticulo((TicketVenta) ticketPrincipal, codArticulo, desglose1, desglose2, cantidad, precio, createLinea());
			comprobarCantidadUnitaria(linea);

			BigDecimal cantidadLinea = linea.getCantidad();
			if(ticketPrincipal.isEsDevolucion()){
				cantidadLinea = cantidadLinea.abs().negate();
			}
//			linea.setCantidad(tratarSignoCantidad(linea.getCantidad(), linea.getCabecera().getCodTipoDocumento()));
			linea.setCantidad(cantidadLinea);
						
			
			if (esLineaDevolucionPositiva) {
				linea.setCantidad(linea.getCantidad().abs());
			}

			if (codBarras != null) {
				linea.setCodigoBarras(codBarras);
			}

			// Si el artículo tiene en su campo FORMATO en BBDD...
			if (pesarArticulo && StringUtils.isNotBlank(linea.getArticulo().getBalanzaTipoArticulo()) && linea.getArticulo().getBalanzaTipoArticulo().trim().toUpperCase().equals(PESAR_ARTICULO)) {
				IBalanza balanza = Dispositivos.getInstance().getBalanza();
				if (!(balanza instanceof BalanzaNoConfig)) {
					Double pesoDouble = balanza.getPeso(linea.getPrecioTotalConDto());

					if (pesoDouble == null || pesoDouble <= 0.0) {
						throw new LineaTicketException(I18N.getTexto("No se ha podido pesar el artículo, compruebe la configuración de la balanza."));
					}

					BigDecimal pesoNuevo = BigDecimal.valueOf(pesoDouble);
					if (BigDecimalUtil.isMayorACero(pesoNuevo)) {
						linea.setCantidad(pesoNuevo);
					}
				}
			}

			if (esDevolucion && ticketOrigen != null && !esLineaDevolucionPositiva) {
				if (idLineaDocOrigen == null) {
					idLineaDocOrigen = getIdLineaTicketOrigen(linea.getCodArticulo(), linea.getDesglose1(), linea.getDesglose2(), linea.getCantidad().abs());
				}
				LineaTicketAbstract lineaOrigen = ticketOrigen.getLinea(idLineaDocOrigen);
				
				tratarImpuestoVertex(lineaOrigen, linea);
				
				lineaOrigen.setPrecioTotalConDto(lineaOrigen.getImporteTotalConDto().setScale(6, BigDecimal.ROUND_HALF_UP)
				        .divide(lineaOrigen.getCantidad().setScale(6, BigDecimal.ROUND_HALF_UP), BigDecimal.ROUND_HALF_UP));
				linea.resetPromociones();
				linea.setPrecioSinDto(lineaOrigen.getPrecioConDto());
				linea.setPrecioTotalSinDto(lineaOrigen.getPrecioTotalConDto());
				linea.recalcularImporteFinal();

				linea.setLineaDocumentoOrigen(lineaOrigen.getIdLinea());
				
				/* Recuperamos la nota informativa en caso de que la tenga */
				if (lineaOrigen instanceof ByLLineaTicket) { /* Si es un ticket de Navision, no viene con el xsi:type=bylLineaTicket, por lo que fallaría al realizar el casting */
					if (((ByLLineaTicketProfesional) lineaOrigen).getNotaInformativa() != null) {
						((ByLLineaTicketProfesional) linea).setNotaInformativa(((ByLLineaTicketProfesional) lineaOrigen).getNotaInformativa());
					}
				}
				actualizarCantidadesOrigenADevolver(lineaOrigen, lineaOrigen.getCantidadADevolver().add(linea.getCantidad().abs()));
			}

			addLinea(linea);
			ticketPrincipal.getTotales().recalcular();
		}
		catch (ArticuloNotFoundException e) {
			linea = null;
			try { // Si no se ha encontrado artículo, intentamos aplicar cupón
				isCupon = sesionPromociones.aplicarCupon(codArticulo, (TicketVentaAbono) ticketPrincipal);
				if (!isCupon) { // Si el código no es de un cupón válido,
					            // lanzamos excepción de artículo no encontrado
					log.warn("nuevaLineaArticulo() - Artículo no encontrado " + codArticulo);
					throw new LineaTicketException(e.getMessageI18N());
				}
				ticketPrincipal.getTotales().recalcular();

			} // Si tenemos excepción durante la aplicación del cupón, lanzamos
			  // excepción indicativa
			catch (CuponAplicationException | CuponUseException | CuponesServiceException ex) {
				log.warn("nuevaLineaArticulo() - Error en la aplicación del cupón -" + ex.getMessageI18N());
				throw new LineaTicketException(ex.getMessageI18N(), e);
			}
		}
		return (LineaTicket) linea;
	}
	
	@SuppressWarnings({ "unchecked"})
	public void recuperarTicket(TicketAparcadoBean ticketAparcado) throws TicketsServiceException, PromocionesServiceException, DocumentoException, LineaTicketException {
        log.debug("recuperarTicket() - Recuperando ticket...");

        nuevoTicket();
        // Realizamos el unmarshall
        log.debug("Ticket recuperado:\n"+new String(ticketAparcado.getTicket()));
        TicketVenta ticketRecuperado = (TicketVentaAbono) MarshallUtil.leerXML(ticketAparcado.getTicket(), getTicketClasses(documentoActivo).toArray(new Class[]{}));

        ticketPrincipal.getCabecera().setIdTicket(ticketRecuperado.getIdTicket());
        ticketPrincipal.getCabecera().setCodTicket(ticketRecuperado.getCabecera().getCodTicket());
        ticketPrincipal.getCabecera().setSerieTicket(ticketRecuperado.getCabecera().getSerieTicket());
        
        //TODO-REE Parche para poder recuperar tickets de FASTPOS - Si es fastpos, no cambiamos cliente
        if(ticketAparcado.getUsuario() == null || !ticketAparcado.getUsuario().equals("FASTPOS")){
        	// Recuperamos el cliente del ticket aparcado
        	ticketPrincipal.getCabecera().setCliente(ticketRecuperado.getCabecera().getCliente());
        }
	    String uidDiarioCaja = sesion.getSesionCaja().getUidDiarioCaja();
        ticketPrincipal.getCabecera().setUidDiarioCaja(uidDiarioCaja);
        
        recuperarDatosPersonalizados(ticketRecuperado);

        List<LineaTicket> lineas = ticketRecuperado.getLineas();
        for (LineaTicket lineaRecuperada : lineas) {
			LineaTicket nuevaLineaArticulo = nuevaLineaArticulo(lineaRecuperada.getCodArticulo(), lineaRecuperada.getDesglose1(), lineaRecuperada.getDesglose2(), lineaRecuperada.getCantidad(), null);
			
			nuevaLineaArticulo.setDocumentoOrigen(lineaRecuperada.getDocumentoOrigen());
			
			nuevaLineaArticulo.setDesArticulo(lineaRecuperada.getDesArticulo());
			nuevaLineaArticulo.setDescuentoManual(lineaRecuperada.getDescuentoManual());
			BigDecimal nuevoPrecio = lineaRecuperada.getPrecioTotalSinDto();
			nuevaLineaArticulo.setPrecioTotalSinDto(nuevoPrecio);
			BigDecimal precioSinDto = lineaRecuperada.getPrecioSinDto();
			nuevaLineaArticulo.setPrecioSinDto(precioSinDto);
			nuevaLineaArticulo.setCodigoBarras(lineaRecuperada.getCodigoBarras());
			nuevaLineaArticulo.setNumerosSerie(lineaRecuperada.getNumerosSerie());
			nuevaLineaArticulo.setEditable(lineaRecuperada.isEditable());
			
			// Parte personalizada para ByL - PROBLEMAS DE CAST
			if (nuevaLineaArticulo instanceof ByLLineaTicketProfesional) { /* Si es un ticket de Navision, no viene con el xsi:type=bylLineaTicket, por lo que fallaría al realizar el casting */
				((ByLLineaTicketProfesional) nuevaLineaArticulo).setNotaInformativa(((ByLLineaTicketProfesional) lineaRecuperada).getNotaInformativa());
			}
			nuevaLineaArticulo.setVendedor(lineaRecuperada.getVendedor());	
			
			/* Trazabilidad en linea de artículo */
			((ByLLineaTicketProfesional) nuevaLineaArticulo).setTrazabilidad(((ByLLineaTicketProfesional) lineaRecuperada).getTrazabilidad());
		}
        
        FidelizacionBean datosFidelizado = ticketRecuperado.getCabecera().getDatosFidelizado();
		if(datosFidelizado!=null){
        	try {
				FidelizacionBean tarjetaFidelizado = Dispositivos.getInstance().getFidelizacion().consultarTarjetaFidelizado(datosFidelizado.getNumTarjetaFidelizado(), ticketPrincipal.getCabecera().getUidActividad());
				ticketPrincipal.getCabecera().setDatosFidelizado(tarjetaFidelizado);
			} catch (ConsultaTarjetaFidelizadoException e) {
				log.debug("recuperarTicket() - Error al consultar fidelizado", e);
				FidelizacionBean fidelizacionBean = new FidelizacionBean();
				fidelizacionBean.setNumTarjetaFidelizado(datosFidelizado.getNumTarjetaFidelizado());
				ticketPrincipal.getCabecera().setDatosFidelizado(fidelizacionBean);
			}
        }
        
        recalcularConPromociones();
        
        // Establecemos el contador
        contadorLinea = ticketPrincipal.getLineas().size()+1;
        //Eliminamos el ticket recuperado de la lista de tickets aparcados.
        ticketsAparcadosService.eliminarTicket(ticketAparcado.getUidTicket());
    }

	@SuppressWarnings({ "unchecked"})
	private void tseFinishTransaction() {
        log.debug("tseFinishTransaction() - Inicio del proceso de finishTransaction del TSE...");

		try {
			epsonTSEService.enviarPeticion(epsonTSEService.getGetStorageInfo());
			String respuestaGetStorageInfo = epsonTSEService.lecturaSocket();

			List<PagoTicket> pagos = ((TicketVenta) getTicket()).getPagos();
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

			EposOutput eposOutput = ((ByLCabeceraTicket) getTicket().getCabecera()).getTse();
			List<String> listaCampos = new ArrayList<String>();
			
			String peticionFinishTransaction = epsonTSEService.finishTransaction(EpsonTSEService.PROCESSTYPE_KASSENBELEG_V1, EpsonTSEService.TIPO_TRANSACCION_BELEG, getTicket().getTotales().getTotal(),
			        pagosEfectivo, pagosNoEfectivo, Integer.parseInt(eposOutput.getTransactionNumber()), false);
			epsonTSEService.enviarPeticion(peticionFinishTransaction);
			String respuestaFinishTransaction = epsonTSEService.lecturaSocket();
			
			listaCampos = new ArrayList<String>();
			listaCampos.add("logTime");
			HashMap<String, String> mapaCampos = epsonTSEService.tratamientoRespuesta(respuestaFinishTransaction, listaCampos);
			String logTimeFinish = mapaCampos.get("logTime");
			String result = epsonTSEService.tratamientoRespuestaResult(respuestaFinishTransaction);

			if (result.equals(EpsonTSEService.EXECUTION_OK)) {
				eposOutput.setLogTimeFinish(logTimeFinish);
				String processData = epsonTSEService.generaProcessData(EpsonTSEService.PROCESSTYPE_KASSENBELEG_V1, EpsonTSEService.TIPO_TRANSACCION_BELEG, getTicket().getTotales().getTotal(),
				        pagosEfectivo, pagosNoEfectivo);
				String QR = epsonTSEService.generaQR(respuestaGetStorageInfo, EpsonTSEService.PROCESSTYPE_KASSENBELEG_V1, processData, eposOutput.getTransactionNumber(),
				        eposOutput.getSignatureCounter(), eposOutput.getLogTimeStart(), logTimeFinish, eposOutput.getSignature());
				
		        log.debug("tseFinishTransaction() - QR generado - " + QR);

				eposOutput.setQr(QR);
			}
			else {
				log.error("tseFinishTransaction() - Error al realizar el proceso de TSE - " + result);
			}
		}
		catch (Exception e) {
			log.error("tseFinishTransaction() - Error al realizar el proceso de TSE - " + e.getMessage());
			
		}
	}
	
	@Override
	public void generarVentaDeApartados(List<ApartadosDetalleBean> articulos, ClienteBean clienteApartado, ApartadosCabeceraBean cabeceraApartado) throws LineaTicketException {
		try {
			nuevoTicket();
			ticketPrincipal.setCliente(clienteApartado);
			
			for(ApartadosDetalleBean articulo : articulos){
				if(articulo.getEstadoLineaApartado() == ApartadosCabeceraBean.ESTADO_DISPONIBLE){
					LineaTicket nuevaLineaArticulo = nuevaLineaArticulo(articulo.getCodart(), articulo.getDesglose1(), articulo.getDesglose2(), articulo.getCantidad(), null);
					nuevaLineaArticulo.setVendedor(usuariosService.consultarUsuario(articulo.getUsuario()));
					nuevaLineaArticulo.setNumerosSerie(articulo.getNumerosSerie());
					nuevaLineaArticulo.setPrecioTotalSinDto(articulo.getImporteTotal());
					nuevaLineaArticulo.recalcularImporteFinal();
				}
			}
			ticketPrincipal.getTotales().recalcular();
			String codMedioPagoApartado = sesion.getAplicacion().getTienda().getTiendaBean().getCodMedioPagoApartado();
			nuevaLineaPago(codMedioPagoApartado, ticketPrincipal.getTotales().getTotalAPagar(), false, false);
			
			
					
		} catch (PromocionesServiceException | DocumentoException e) {
			LineaTicketException ex = new LineaTicketException(I18N.getTexto("Error creando el ticket de venta."),e);
			log.error("Error creando ticket", ex);
			throw ex;
		} catch (LineaTicketException e) {
			throw e;
		}
		catch (Exception e) {
			log.error("Usuario no encontrado");
		}
	}

	
	public ByLVentaProfesionalManager getTicketAuxiliar() {
		return ticketAuxiliar;
	}

	
	public void setTicketAuxiliar(ByLVentaProfesionalManager ticketAuxiliar) {
		this.ticketAuxiliar = ticketAuxiliar;
	}

	public ByLTicketVentaAbono recuperarTicketFidelizadoByL(String codigo, String codTienda, String codCaja, Long idTipoDoc) throws ArticuloNoAptoException {

		TicketDevolucionResponse respuesta = new TicketDevolucionResponse();
		ByLTicketVentaAbono ticket = null;

		try {
			log.debug("recuperarTicketDevolucion() - Recuperando ticket...");
			byte[] xmlTicketOrigen = null;

			// Por codigo desde central
			try {
				respuesta = obtenerTicketDevolucionCentralByL(codCaja, codTienda, codigo, idTipoDoc);
				if (respuesta.getTicket() != null) {
					xmlTicketOrigen = respuesta.getTicket().getBytes("UTF-8");

				}
			}
			catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
			catch (Exception e) {
				log.warn("recuperarTicketDevolucion() - Error al obtener ticket devolución desde central - " + e.getClass().getName() + " - " + e.getLocalizedMessage(), e);
			}

			if (xmlTicketOrigen == null) {
				// Si null, obtenemos por codigo desde local
				TicketBean ticketA = ticketsService.consultarTicketAbono(codTienda, codCaja, codigo, idTipoDoc);
				if (ticketA != null) {
					xmlTicketOrigen = ticketA.getTicket();
				}
				else {
					throw new TicketsServiceException("No se ha encontrado ticket con codigo: " + codigo);
				}
			}
			ticket = tratarTicketRecuperadoFidelizado(xmlTicketOrigen, idTipoDoc);

			if (respuesta.getTaxFree() != null) {
				ticket.setTaxFree(respuesta.getTaxFree());
			}
			return ticket;

		}
		catch (TicketsServiceException e) {
			log.error("recuperarTicketFidelizadoByL() - " + e.getClass().getName() + " - " + e.getLocalizedMessage(), e);
			return null;
		}

	}

	private ByLTicketVentaAbono tratarTicketRecuperadoFidelizado(byte[] ticketRecuperado, Long idTipoDoc) throws TicketsServiceException {
		ByLTicketVentaAbono ticket = null;
		try {
			log.debug(new String(ticketRecuperado, "UTF-8"));
			// Realizamos el unmarshall
			ticket = (ByLTicketVentaAbono) MarshallUtil.leerXML(ticketRecuperado, getTicketClasses(documentos.getDocumento(idTipoDoc)).toArray(new Class[] {}));
		}
		catch (Exception e) {
			log.warn("tratarTicketRecuperado - Error al parsear el ticket");
		}
		return ticket;
	}
	
	 public void solicitarDevTarjetaApartado(final ApartadosCabeceraBean cabeceraApartado, final Stage stage, MedioPagoBean mp, final SalvarTicketCallback callback) throws TicketsServiceException {
		 new SolicitarDevTarjetaApartadoTask(cabeceraApartado, stage, mp, callback).start();
	 }

		class SolicitarDevTarjetaApartadoTask extends BackgroundTask<Void> {

			protected ApartadosCabeceraBean cabeceraApartado;
			protected Stage stage;
			protected SalvarTicketCallback callback;
			protected MedioPagoBean mp;

			public SolicitarDevTarjetaApartadoTask(ApartadosCabeceraBean cabeceraApartado, Stage stage, MedioPagoBean mp, SalvarTicketCallback callback) {
				this.cabeceraApartado = cabeceraApartado;
				this.stage = stage;
				this.callback = callback;
				this.mp = mp;
			}

			@Override
			protected Void call() throws Exception {
				return null;
			}

			@Override
			protected void succeeded() {
				super.succeeded();
				final List<PagoTicket> pagosTarjeta = new ArrayList<>();

				PagoTicket pago = new PagoTicket();
				pago.setMedioPago(mp);
				pago.setImporte(cabeceraApartado.getSaldoCliente());

				pagosTarjeta.add((PagoTicket) pago);

				List<DatosPeticionPagoTarjeta> datosPeticionTarjeta = new LinkedList<>();
				/* Generamos los DatosPeticionTarjeta */
				/* Igual que en ApartadosService.registrarMovimientoAbonoApartado() */
				String id = "Apartado nº " + cabeceraApartado.getIdApartado();
				if (Dispositivos.getInstance().getTarjeta() instanceof TefAdyenCloud) {
					try {
						id = generaCabeceraApartado(cabeceraApartado);

						nuevoTicket();
						setDocumentoActivo(documentos.getDocumento("RE"));

						try {
							((ByLTicketsService) ticketsService).asignarIdTransaccion((Ticket<?, ?, ?>) ticketPrincipal);
						}
						catch (TicketsServiceException e) {
							log.error("SalvarTicketApartadoTask() - Ha ocurrido un error en la asignación del contador idTransacción");
						}

						AdyenDatosPeticionPagoTarjeta datoPeticion = new AdyenDatosPeticionPagoTarjeta(((ByLCabeceraTicket) ticketPrincipal.getCabecera()).getIdTransaccion(), id, pago.getImporte(),
						        ticketPrincipal.getCabecera().getCodTipoDocumento());
						datosPeticionTarjeta.add(datoPeticion);
					}
					catch (Exception e) {
						log.error("grabarMovimientoDevolucion() - Ha habido un error al crear el ticket para la impresión: " + e.getMessage());
					}
				}

				TarjetaCallback<List<DatosRespuestaPagoTarjeta>> tarjetaCallback = new TarjetaCallback<List<DatosRespuestaPagoTarjeta>>(){

					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(List<DatosRespuestaPagoTarjeta> datosRespuesta) {
						BigDecimal saldoAntes = cabeceraApartado.getSaldoCliente();
						try {
							// Asignamos los datos respuesta a los pagos
							for (DatosRespuestaPagoTarjeta datoRespuesta : datosRespuesta) {
								pagosTarjeta.get(datosRespuesta.indexOf(datoRespuesta)).setDatosRespuestaPagoTarjeta(datoRespuesta);
							}

							ticketPrincipal.setPagos(pagosTarjeta);

							callback.onSucceeded();
						}
						catch (Exception e) {
							cabeceraApartado.setSaldoCliente(saldoAntes);
							anularPagos(datosRespuesta, stage);
							anularMovimientoTarjetaRegalo(stage);
							anularPromocionesFinales(stage);

							callback.onFailure(e);
						}
					}

					@Override
					public void onFailure(List<DatosRespuestaPagoTarjeta> datosRespuesta, Throwable caught) {
						anularPagos(datosRespuesta, stage);
						anularMovimientoTarjetaRegalo(stage);
						anularPromocionesFinales(stage);

						callback.onFailure((Exception) caught);
					}
				};

				Dispositivos.getInstance().getTarjeta().solicitarDevolucion(datosPeticionTarjeta, stage, tarjetaCallback);
			}

			@Override
			protected void failed() {
				super.failed();
				Throwable ex = getException();
				callback.onFailure((Exception) ex);
			}

		}

		private Boolean operacionVentaDevSpark130F() throws Spark130FException {
			List<String> listAtributos = new ArrayList<String>();
			listAtributos.add("RC");
			HashMap<String, String> mapaCampos = spark130FService.getCamposRespuesta(spark130FService.realizarLlamada(spark130FService.shiftStatus()), listAtributos);
			String returnCode = mapaCampos.get("RC");
			
			if (returnCode.equals(Spark130FConstants.NO_ERROR)) {
				Spark130FOutput spark130FOutput = spark130FService.procesoVentaDev(ticketPrincipal);
				((ByLCabeceraTicket) ticketPrincipal.getCabecera()).setSpark130F(spark130FOutput);
				
				if(ticketAuxiliar != null) {
					Spark130FOutput spark130FOutputAux = spark130FService.procesoVentaDev(ticketAuxiliar.getTicket());
					((ByLCabeceraTicket) ticketAuxiliar.getTicket().getCabecera()).setSpark130F(spark130FOutputAux);
				}
				
				return true;
			}
			else {
				Map<String, String> mapaErrores = Spark130FConstants.setErrors();

				String errorDesc = mapaErrores.get(returnCode);
				throw new Spark130FException(errorDesc);
			}
		}
		
		private void procesoRegistrarTicket() throws TicketsServiceException {
			SqlSession sqlSession = SpringContext.getBean(SpringTransactionSqlSession.class);
			sqlSession.openSession(SessionFactory.openSession());
			try {

				/* Integracion con VERTEX USA */
				llamadaVertex();

				if (ticketAuxiliar != null) {
					ticketsService.asignarIdTransaccion((Ticket<?, ?, ?>) ticketAuxiliar.getTicket());
					if (ticketAuxiliar.getTicket().getIdTicket() == null) {
						ticketsService.setContadorIdTicket((Ticket<?, ?, ?>) ticketAuxiliar.getTicket());
					}

					ticketsService.registrarTicket((Ticket) ticketAuxiliar.getTicket(), documentoActivo, esDevolucion || esFacturacionVentaCredito, sqlSession);
				}

				ticketsService.registrarTicket((Ticket) ticketPrincipal, documentoActivo, esDevolucion || esFacturacionVentaCredito, sqlSession);

				log.debug("ByLRegistrarTicketTask() - Confirmando transacción...");
				sqlSession.commit();
				log.debug("ByLRegistrarTicketTask() - Ticket salvado correctamente.");
			}
			catch (Exception e) {
				try {
					sqlSession.rollback();
				}
				catch (Exception e2) {
					log.error("ByLRegistrarTicketTask() - " + e2.getClass().getName() + " - " + e2.getLocalizedMessage(), e2);
				}
				String msg = "Se ha producido un error al registrar el ticket : " + e.getMessage();
				log.error("ByLRegistrarTicketTask() - " + msg, e);
				throw new TicketsServiceException(e);
			}
			finally {
				sqlSession.close();
			}
		}
		
		private String generaCabeceraApartado(ApartadosCabeceraBean cabeceraApartado) {
			log.debug("generaCabeceraApartado() - Generación del codTicket de reserva y poderlo enviar como merchantReference a Adyen");
			String id = "";
			CabeceraReservaTicket cabecera = new CabeceraReservaTicket();
			cabecera.setUidApartado(cabeceraApartado.getUidApartado());
			log.debug("generaCabeceraApartado() - UidApartado generado : " + cabecera.getUidApartado());
			cabecera.setUidActividad(sesion.getAplicacion().getUidActividad());
			cabecera.setUidDiarioCaja(sesion.getSesionCaja().getUidDiarioCaja());
			/* Sacamos el estado del apartado de el objeto "ApartadoManager" */
			cabecera.setEstadoApartado(cabeceraApartado.getEstadoApartado());

			/* Recorremos el listado de documentos activos para encontrar el documento de Reserva */
			TipoDocumentoBean tipoDocumentoReserva = null;
			Map<Long, TipoDocumentoBean> mapaDocumentos = sesion.getAplicacion().getDocumentos().getDocumentos();
			for (Map.Entry<Long, TipoDocumentoBean> entry : mapaDocumentos.entrySet()) {
				TipoDocumentoBean documento = entry.getValue();
				if (ByLDetalleApartadosController.CODIGO_DOCUMENTO_RESERVA.equals(documento.getCodtipodocumento())) {
					tipoDocumentoReserva = documento;
				}
			}

			if (tipoDocumentoReserva != null) {
				cabecera.setTipoDocumento(tipoDocumentoReserva.getIdTipoDocumento().toString());
				cabecera.setCodigoTipoDocumento(tipoDocumentoReserva.getCodtipodocumento());
				cabecera.setDescripcionTipoDocumento(tipoDocumentoReserva.getDestipodocumento());
				cabecera.setFormatoImpresion(tipoDocumentoReserva.getFormatoImpresion());
			}
			else {
				String mensajeError = "Error al localizar el tipo de documento de Reserva";
				log.error("crearCabeceraTicketReserva() - " + mensajeError);
			}

			/* Calculamos la serie del Ticket a partir de los siguientes datos */
			Map<String, String> parametrosContador = new HashMap<>();
			parametrosContador.put("CODEMP", sesion.getAplicacion().getEmpresa().getCodEmpresa());
			parametrosContador.put("CODTIPODOCUMENTO", cabecera.getCodigoTipoDocumento());
			parametrosContador.put("CODSERIE", sesion.getAplicacion().getTienda().getAlmacenBean().getCodAlmacen());
			parametrosContador.put("CODCAJA", sesion.getSesionCaja().getCajaAbierta().getCodCaja());
			parametrosContador.put("PERIODO", ((new Fecha()).getAño().toString()));
			try {
				/* Generamos el ID del apartado a partir del contador */
				ByLContadorBean ticketContador = byLServicioContadores.obtenerContadorByL(tipoDocumentoReserva.getIdContador(), parametrosContador, cabecera.getUidActividad());

				cabecera.setSerieTicket(ticketContador.getConfigContador().getValorDivisor3());

				/* Rellenamos el IdReserva con el serie Ticket delante */
				id = StringUtils.leftPad(cabeceraApartado.getIdApartado().toString(), 6, "0");
				id = cabecera.getSerieTicket() + id;

			}
			catch (ContadorServiceException e) {
				String mensajeError = "Error al consultar el contador para el Ticket de Reserva";
				log.error("generaCabeceraApartado() - " + mensajeError + " - " + e.getMessage());
			}
			return id;
		}
	
	@Override
	protected LineaTicketAbstract createLinea() {
    	return SpringContext.getBean(ByLLineaTicketProfesional.class);
	}
	
	@Override
	public TipoDocumentoBean getNuevoDocumentoActivo() throws DocumentoException {
		return sesion.getAplicacion().getDocumentos().getDocumento(Documentos.FACTURA_SIMPLIFICADA);
	}
	
	
	public List<Class<?>> getTicketClasses(TipoDocumentoBean tipoDocumento) {
		List<Class<?>> classes = new LinkedList<>();
		
		// Obtenemos la clase root
		Class<?> clazz = SpringContext.getBean(getTicketClass(tipoDocumento)).getClass();
		
		// Generamos lista de clases "ancestras" de la principal
		Class<?> superClass = clazz.getSuperclass();
		while (!superClass.equals(Object.class)) {
			classes.add(superClass);
			superClass = superClass.getSuperclass();
		}
		// Las ordenamos descendentemente
		Collections.reverse(classes);
		
		//Añadimos la clase principal y otras necesarias
		classes.add(clazz);
		classes.add(SpringContext.getBean(ByLLineaTicketProfesional.class).getClass());
		classes.add(SpringContext.getBean(LineaTicket.class).getClass());
		classes.add(SpringContext.getBean(CabeceraTicket.class).getClass());
		classes.add(SpringContext.getBean(TotalesTicket.class).getClass());
		classes.add(SpringContext.getBean(PagoTicket.class).getClass());

		return classes;
	}
	
	@SuppressWarnings("unchecked")
	protected Integer getPuntosConcedidosLinea(String codArticulo,
			Integer lineaOrigen, BigDecimal cantidadLineaDevolucion) {
		Integer result = 0;
	    List<LineaTicketAbstract> lineasOriginales = getTicketOrigen().getLinea(codArticulo);
	    for(LineaTicketAbstract lineaOriginal : lineasOriginales){
	    	if(lineaOriginal.getIdLinea().equals(lineaOrigen) &&
	    			lineaOriginal.getCantidadDevuelta().compareTo(lineaOriginal.getCantidad()) <= 0
	    			){
	    		Integer puntosLinea = 0;
				for(PromocionLineaTicket promo : lineaOriginal.getPromociones()){
			    	if(promo.getPuntos()>0){
			    		puntosLinea +=promo.getPuntos();
			    	}
			    }
				BigDecimal factor = cantidadLineaDevolucion.divide(lineaOriginal.getCantidad(),2,RoundingMode.HALF_UP).abs();
	    		result += BigDecimal.valueOf(puntosLinea).multiply(factor).intValue();
				break;
	    	}
	    }
	    return result;
	}

	private void tratarImpuestoVertex(LineaTicketAbstract lineaOrigen, ByLLineaTicketProfesional linea) {
		log.debug("tratarImpuestoVertex() - Comprobamos si la venta origen tiene impuestos de vertex");

		if (((ByLLineaTicketProfesional) lineaOrigen).getLineaVertex() != null) {
			log.debug("tratarImpuestoVertex() - La venta origen tiene impuestos de vertex");
			
			linea.setLineaVertex(((ByLLineaTicketProfesional) lineaOrigen).getLineaVertex());
			linea.setListaImpuestosVertex(((ByLLineaTicketProfesional) lineaOrigen).getListaImpuestosVertex());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override 
	protected Class<? extends ITicket> getTicketClass(TipoDocumentoBean tipoDocumento) {
    	String claseDocumento = tipoDocumento.getClaseDocumento();
    	if(claseDocumento != null){
    		try {
				return (Class<? extends ITicket>) Class.forName(claseDocumento);
			} catch (ClassNotFoundException e) {
				log.error(String.format("getTicketClass() - Clase %s no encontrada, devolveremos ByLTicketVentaProfesional", claseDocumento));
			}
    	}
		return ByLTicketVentaProfesional.class;
	}
	
	private void llamadaVertex() {
		if (vertexservice.integracionImpuestosVertexActiva(ticketPrincipal.getCabecera().getTipoDocumento())) {
			log.debug("llamadaVertex() - Inicio del proceso de llamada a Vertex");
			try {
				if (((ByLCabeceraTicket) ticketPrincipal.getCabecera()).getCabeceraVertex() != null
				        && ((ByLCabeceraTicket) ticketPrincipal.getCabecera()).getCabeceraVertex().getIsSentToVertex().equals("S")) {

					if (ticketOrigen != null) {
						vertexservice.peticionVertex(VertexService.VERTEX_TIPO_INVOICE, ticketPrincipal, documentoActivo.getCodtipodocumento(), (ByLTicketVentaAbono) ticketOrigen, true);
					}
					else {
						ticketsService.setContadorIdTicket((Ticket<?, ?, ?>) ticketPrincipal);
						vertexservice.peticionVertex(VertexService.VERTEX_TIPO_INVOICE, ticketPrincipal, documentoActivo.getCodtipodocumento(), null, false);
					}
				}
			}
			catch (Exception e) {
				log.warn("llamadaVertex() - Error en la peticion a vertex : " + e.getMessage(), e);
				/*
				 * Si la petición de tipo INVOICE falla, mandamos el ticket a Navision indicando que no se ha mandado a
				 * Vertex
				 */
				((ByLCabeceraTicket) ticketPrincipal.getCabecera()).getCabeceraVertex().setIsSentToVertex("N");
			}
		}
	}
}
