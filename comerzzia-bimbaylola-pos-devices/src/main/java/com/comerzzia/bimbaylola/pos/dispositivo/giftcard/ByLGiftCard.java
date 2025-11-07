/**
 * ComerZZia 3.0
 *
 * Copyright (c) 2008-2015 Comerzzia, S.L.  All Rights Reserved.
 *
 * THIS WORK IS  SUBJECT  TO  SPAIN  AND  INTERNATIONAL  COPYRIGHT  LAWS  AND
 * TREATIES.   NO  PART  OF  THIS  WORK MAY BE  USED,  PRACTICED,  PERFORMED
 * COPIED, DISTRIBUTED, REVISED, MODIFIED, TRANSLATED,  ABRIDGED, CONDENSED,
 * EXPANDED,  COLLECTED,  COMPILED,  LINKED,  RECAST, TRANSFORMED OR ADAPTED
 * WITHOUT THE PRIOR WRITTEN CONSENT OF COMERZZIA, S.L. ANY USE OR EXPLOITATION
 * OF THIS WORK WITHOUT AUTHORIZATION COULD SUBJECT THE PERPETRATOR TO
 * CRIMINAL AND CIVIL LIABILITY.
 *
 * CONSULT THE END USER LICENSE AGREEMENT FOR INFORMATION ON ADDITIONAL
 * RESTRICTIONS.
 */

package com.comerzzia.bimbaylola.pos.dispositivo.giftcard;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javafx.stage.Stage;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.persistence.articulos.stock.RespuestaTokenDTO;
import com.comerzzia.bimbaylola.pos.persistence.articulos.stock.SolicitarTokenDTO;
import com.comerzzia.bimbaylola.pos.persistence.giftcard.ByLGiftCardBean;
import com.comerzzia.bimbaylola.pos.persistence.giftcard.GiftCardDTO;
import com.comerzzia.bimbaylola.pos.persistence.giftcard.GiftCardMovimientoDTO;
import com.comerzzia.bimbaylola.pos.services.ticket.pagos.tarjetaregalo.GiftCardService;
import com.comerzzia.core.util.xml.XMLDocumentNode;
import com.comerzzia.core.util.xml.XMLDocumentNodeNotFoundException;
import com.comerzzia.pos.core.dispositivos.configuracion.ConfiguracionDispositivo;
import com.comerzzia.pos.core.dispositivos.dispositivo.DispositivoCallback;
import com.comerzzia.pos.core.dispositivos.dispositivo.giftcard.IGiftCard;
import com.comerzzia.pos.core.gui.BackgroundTask;
import com.comerzzia.pos.core.gui.POSApplication;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.dispositivo.comun.tarjeta.CodigoTarjetaController;
import com.comerzzia.pos.dispositivo.comun.tarjeta.CodigoTarjetaView;
import com.comerzzia.pos.dispositivo.giftcard.GiftCard;
import com.comerzzia.pos.persistence.giftcard.GiftCardBean;
import com.comerzzia.pos.persistence.paises.PaisBean;
import com.comerzzia.pos.services.core.documentos.Documentos;
import com.comerzzia.pos.services.core.paises.PaisNotFoundException;
import com.comerzzia.pos.services.core.paises.PaisService;
import com.comerzzia.pos.services.core.paises.PaisServiceException;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.services.core.variables.VariablesServices;
import com.comerzzia.pos.services.ticket.tarjetaRegalo.TarjetaRegaloException;
import com.comerzzia.pos.util.config.SpringContext;
import com.comerzzia.pos.util.i18n.I18N;
import com.comerzzia.rest.client.exceptions.HttpServiceRestException;
import com.comerzzia.rest.client.exceptions.RestConnectException;
import com.comerzzia.rest.client.exceptions.RestException;
import com.comerzzia.rest.client.exceptions.RestHttpException;
import com.comerzzia.rest.client.exceptions.RestTimeoutException;
import com.comerzzia.rest.client.exceptions.ValidationDataRestException;
import com.comerzzia.rest.client.exceptions.ValidationRequestRestException;
import com.comerzzia.rest.client.movimientos.MovimientoRequestRest;

@Component
@Primary
public class ByLGiftCard extends GiftCard{

	private static final Logger log = Logger.getLogger(ByLGiftCard.class.getName());
	
	private String nombreTarjeta;
	
	@Autowired
	private VariablesServices variablesServices;
	
	protected Documentos documentos;
	
	private static final String GRANT_TYPE = "NAVISION.GRANT_TYPE";
	private static final String CLIENT_ID = "NAVISION.CLIENT_ID";
	private static final String CLIENT_SECRET = "NAVISION.CLIENT_SECRET";
	private static final String SCOPE_CARDS = "NAVISION.SCOPE_CARDS";
	
	private static List<String> tarjetasRegaloAnulables = new ArrayList<String>();
	
	/* ################################################################################################ */
	/* ####################################### CONFIGURACIÓN ########################################## */
	/* ################################################################################################ */
	
	/**
	 * Realiza la carga de la configuración para utilizar las GiftCards.
	 */
	@Override
	public void cargaConfiguracion(ConfiguracionDispositivo config){
		codArticulosTarj = new ArrayList<>();
		try{
			XMLDocumentNode nodeConfTarjRegalo = config.getConfiguracionModelo().getConfigConexion().getNodo(IGiftCard.TAG_CONFIG_TARJETA_REGALO);
			XMLDocumentNode articulosXML = nodeConfTarjRegalo.getNodo(IGiftCard.TAG_ARTICULOS);
			List<XMLDocumentNode> articulos = articulosXML.getHijos("CodArt");

			for(XMLDocumentNode articulo : articulos){
				codArticulosTarj.add(articulo.getValue());
			}
			codMedPago = nodeConfTarjRegalo.getNodo("codMedPago").getValue();
			nombreTarjeta = nodeConfTarjRegalo.getNodo("nombreTarjeta").getValue();
			if(nombreTarjeta == null || nombreTarjeta.isEmpty()){
				throw new XMLDocumentNodeNotFoundException("No se ha especificado el nombre del lector de tarjetas.");
			}

			leerTiposTarjeta(nodeConfTarjRegalo);
		}
		catch(XMLDocumentNodeNotFoundException ex){
			log.error("Error recuperando la información de la configuración de las tarjetas regalo", ex);
		}
	}
	
	/* ###################################################################################################### */
	/* ####################################### PETICIONES SERVIDOR ########################################## */
	/* ###################################################################################################### */
	
	/**
	 * Consulta el saldo de una tarjeta de regalo para mostrarlo por pantalla o para usarlo en otras acciones.
	 * @param stage
	 * @param callback
	 */
	public void pedirTarjetaRegalo(Stage stage, DispositivoCallback<GiftCardBean> callback){
		String numTarjeta = null;
		numTarjeta = leerNumeroTarjeta(stage);
		log.debug("ByLGiftCard/pedirTarjetaRegalo() - Iniciamos la petición para cargar la tarjeta : " + numTarjeta);

		if(numTarjeta != null){
			new ByLConsultarGiftCardTask(numTarjeta, callback, stage).start();
		}
		else{
			String mensajeError = "El usuario canceló la operación";
			log.debug("ByLGiftCard/pedirTarjetaRegalo() - " + mensajeError);
			callback.onFailure(new Exception(I18N.getTexto("El usuario canceló la operación")));
		}
	}

	/**
	 * Este método consulta el saldo de una tarjeta de regalo para mostrarlo por pantalla o para usarlo
	 * en otras acciones.
	 * @param stage
	 * @param callback
	 * @throws TarjetaRegaloException 
	 */
	public void pedirTarjetaRegaloReservas(Stage stage, DispositivoCallback<GiftCardBean> callback) throws TarjetaRegaloException{
		String numTarjeta = null;
		numTarjeta = leerNumeroTarjeta(stage);
		log.debug("ByLGiftCard/pedirTarjetaRegalo() - Iniciamos la petición para cargar la tarjeta : " + numTarjeta);

		if(numTarjeta != null){
			new ByLConsultarGiftCardTask(numTarjeta, callback, stage).start();
		}
		else{
			String mensajeError = "El usuario canceló la operación";
			log.debug("ByLGiftCard/pedirTarjetaRegalo() - " + mensajeError);
			callback.onFailure(new TarjetaRegaloException(I18N.getTexto("El usuario canceló la operación")));
		}
	}

	public class ByLConsultarGiftCardTask extends BackgroundTask<GiftCardBean>{

		private final String numTarjRegalo;
		private DispositivoCallback<GiftCardBean> callback;
		private Stage stage;

		public ByLConsultarGiftCardTask(String numTarjRegalo, DispositivoCallback<GiftCardBean> callback, Stage stage){
			super(true);
			this.numTarjRegalo = numTarjRegalo;
			this.callback = callback;
			this.stage = stage;
		}

		@Override
		protected GiftCardBean call() throws Exception{
			ByLGiftCardBean tarjeta = new ByLGiftCardBean();
			tarjeta.setNumTarjetaRegalo(numTarjRegalo);
			tarjeta.setUidTransaccion(UUID.randomUUID().toString());
			tarjeta.setCodTipoTarjeta("R");
			tarjeta.setSaldoProvisional(BigDecimal.ZERO);
			GiftCardDTO estadoTarjeta = null;
			try{
				estadoTarjeta = consultarSaldoGiftCard(numTarjRegalo);
				if(estadoTarjeta != null){
					tarjeta.setTarjetaAbono(estadoTarjeta.getTypeCode() == 1 ? true : false);
					if(estadoTarjeta.getBalanceAmount() != null){
						tarjeta.setSaldo(new BigDecimal(estadoTarjeta.getBalanceAmount()));
					}
					else{
						tarjeta.setSaldo(BigDecimal.ZERO);
					}
					if(estadoTarjeta.getStateCode() == 2 || estadoTarjeta.getStateCode() == 4){
						tarjeta.setBaja(true);
						tarjeta.setActiva(false);
					}
					else if(estadoTarjeta.getStateCode() == 0 || estadoTarjeta.getStateCode() == 3){
						tarjeta.setBaja(false);
						tarjeta.setActiva(false);
					}
					else{
						tarjeta.setBaja(false);
						tarjeta.setActiva(true);
					}
					tarjeta.setEstado(estadoTarjeta.getStateCode());
				}
				else{
					tarjeta.setError(I18N.getTexto("La tarjeta indicada no existe."));
				}
			}
			catch(RestTimeoutException e){
				callback.onFailure(new Exception("Ha ocurrido un error en la petición"));
				tarjeta.setError(I18N.getTexto("No se puede establecer conexión con el servidor."));
			}
			catch(Exception e){
				callback.onFailure(new Exception(e.getMessage(), e));
				tarjeta.setError(e.getMessage());
			}
			return tarjeta;
		}

		@Override
		protected void succeeded(){
			super.succeeded();
			GiftCardBean tarjeta = (GiftCardBean) getValue();
			callback.onSuccess(tarjeta);
		}

		@Override
		protected void failed(){
			super.failed();
			Throwable e = getException();
			log.error("Ha ocurrido un error en la petición rest: " + e.getMessage(), e);
			if(e instanceof ValidationRequestRestException){
				VentanaDialogoComponent.crearVentanaError(stage, e.getMessage(), e);
			}
			else if(e instanceof ValidationDataRestException){
				VentanaDialogoComponent.crearVentanaError(stage, e.getMessage(), e);
			}
			else if(e instanceof HttpServiceRestException){
				VentanaDialogoComponent.crearVentanaError(stage, e.getMessage(), e);
			}
			else if(e instanceof RestHttpException){
				VentanaDialogoComponent.crearVentanaError(stage, I18N.getTexto("Lo sentimos, ha ocurrido un error en la petición"), e);
			}
			else if(e instanceof RestConnectException){
				VentanaDialogoComponent.crearVentanaError(stage, I18N.getTexto("No se ha podido conectar con el servidor"), e);
			}
			else if(e instanceof RestTimeoutException){
				VentanaDialogoComponent.crearVentanaError(stage, I18N.getTexto("El servidor ha tardado demasiado tiempo en responder"), e);
			}
			else if(e instanceof RestException){
				VentanaDialogoComponent.crearVentanaError(stage, I18N.getTexto("Lo sentimos, ha ocurrido un error en la petición"), e);
			}
			else{
				VentanaDialogoComponent.crearVentanaError(stage, I18N.getTexto("Lo sentimos, ha ocurrido un error."), e);
			}
			callback.onFailure(e);
		}
	}

	/**
	 * Realiza una petición para obtener el saldo de una tarjeta.
	 * @param numTarjeta : Contiene el número de la tarjeta a consultar.
	 * @return Devuelve como resultado el saldo de la tarjeta en tipo Double.
	 */
	private GiftCardDTO consultarSaldoGiftCard(String numTarjeta) throws RestTimeoutException{
		RespuestaTokenDTO token = null;
		GiftCardDTO resultado = null;
		try{
			variablesServices = SpringContext.getBean(VariablesServices.class);
			String grantType = variablesServices.getVariableAsString(GRANT_TYPE);
			String clientID = variablesServices.getVariableAsString(CLIENT_ID);
			String clientSecret = variablesServices.getVariableAsString(CLIENT_SECRET);
			String scopeCards = variablesServices.getVariableAsString(SCOPE_CARDS);
			/* Primero realizamos la llamada para recibir el token. */
			token = SpringContext.getBean(GiftCardService.class).solicitarTokenGiftCard(new SolicitarTokenDTO(grantType, clientID, clientSecret, null, scopeCards));
			/* Ahora realizamos la peticion para consultar el saldo a partir del código tarjeta y del token que hemos
			 * pedido anteriormente. */
			resultado = SpringContext.getBean(GiftCardService.class).solicitarEstadoGiftCard(numTarjeta, token.getAccess_token());
		}
		catch(RestTimeoutException e){
			throw new RestTimeoutException(e);
		}
		catch(RestException e){
			log.error("consultarSalgoGiftCard() - Ha ocurrido un error al consultar saldo de la tarjeta : " + numTarjeta, e);
		}
		return resultado;
	}

	public GiftCardBean consultarTarjetaRegalo(String numTarjRegalo, String uidActividad) throws RestException, RestHttpException{
		ByLGiftCardBean tarjeta = new ByLGiftCardBean();
		tarjeta.setNumTarjetaRegalo(numTarjRegalo);
		tarjeta.setUidTransaccion(UUID.randomUUID().toString());
		/* Este código es el asignado a las tarjetas de tipo regalo. */
		tarjeta.setCodTipoTarjeta("R");
		tarjeta.setSaldoProvisional(BigDecimal.ZERO);
		GiftCardDTO estadoTarjeta = null;
		try{
			estadoTarjeta = consultarSaldoGiftCard(numTarjRegalo);
			if(estadoTarjeta != null){
				tarjeta.setTarjetaAbono(estadoTarjeta.getTypeCode() == 1 ? true : false);
				if(estadoTarjeta.getBalanceAmount() != null){
					tarjeta.setSaldo(new BigDecimal(estadoTarjeta.getBalanceAmount()).setScale(2, RoundingMode.HALF_UP));
					/* Para cuando se va a realizar una activacion */
				}
				else{
					tarjeta.setSaldo(BigDecimal.ZERO);
				}
				if(estadoTarjeta.getStateCode() == 2 || estadoTarjeta.getStateCode() == 4){
					tarjeta.setBaja(true);
					tarjeta.setActiva(false);
				}
				else if(estadoTarjeta.getStateCode() == 0 || estadoTarjeta.getStateCode() == 3){
					tarjeta.setBaja(false);
					tarjeta.setActiva(false);
				}
				else{
					tarjeta.setBaja(false);
					tarjeta.setActiva(true);
				}

				tarjeta.setEstado(estadoTarjeta.getStateCode());

			}
			else{
				tarjeta.setError(I18N.getTexto("La tarjeta indicada no existe."));
			}
		}
		catch(RestTimeoutException e){
			tarjeta.setError("No se puede establecer conexión con el servidor.");
			throw new RestException("No se puede establecer conexión con el servidor.", e);
		}
		catch(Exception e){
			tarjeta.setError(e.getMessage());
			throw new RestException(e.getMessage(), e);
		}

		return tarjeta;
	}

	public void recargarImporteTarjetaRegalo(BigDecimal importe, String numTarjeta, String codTicket, Integer estadoTarjeta) 
			throws RestException, RestHttpException{
		
		EnviarMovimientoGiftCardBean enviarMovimiento = new EnviarMovimientoGiftCardBean();
		enviarMovimiento.setEntrada(importe.doubleValue());
		enviarMovimiento.setNumeroTarjeta(numTarjeta);
		enviarMovimiento.setTypeCode(estadoTarjeta);
		enviarMovimiento.setDocumentNumber(codTicket);
		
//		MovimientoRequestRest mov = new MovimientoRequestRest();
//		mov.setNumeroTarjeta(numTarjeta);
//		mov.setUidActividad(uidActividad);
//		mov.setSalida(0D);
//		mov.setConcepto(concepto + " " + estadoTarjeta);
//		mov.setEntrada(importe.doubleValue());
//		mov.setUidTransaccion(uidTransaccion);
//		mov.setFecha(new Date());
//		mov.setDocumento(String.valueOf(idTicket));
//		mov.setApiKey(wsApiKey);
//		mov.setSaldo(saldoTarjeta.doubleValue());
//		mov.setSaldoProvisional(saldoProvisional.doubleValue());
		realizarCompraSaldoTarjetaRest(enviarMovimiento);
	}
	
	/**
	 * Realiza la compra de saldo para una tarjeta regalo.
	 * @param mov : Movimiento de inserción de saldo.
	 * @throws RestException
	 * @throws RestHttpException
	 */
	public void realizarCompraSaldoTarjetaRest(EnviarMovimientoGiftCardBean enviarMovimiento) throws RestException, RestHttpException{
		Sesion sesion = SpringContext.getBean(Sesion.class);
		/* Realizar movimiento de suma de cantidad a la tarjeta. */
		GiftCardMovimientoDTO nuevoMovimiento = new GiftCardMovimientoDTO();
		if(enviarMovimiento.getTypeCode() == 0){ /*  0 = Inactiva*/
			nuevoMovimiento.setTypeCode("0");
		}
		else{
			nuevoMovimiento.setTypeCode("1");
		}
		/* Esta cantidad siempre es en positivo. */
		nuevoMovimiento.setAmount(enviarMovimiento.getEntrada().toString());
		nuevoMovimiento.setCurrencyCode(getCodMoneda(sesion.getAplicacion().getTienda().getCliente().getCodpais()));
		nuevoMovimiento.setDocumentNumber(enviarMovimiento.getDocumentNumber());
		nuevoMovimiento.setShopCode(sesion.getSesionCaja().getCajaAbierta().getCodAlmAsString());
		nuevoMovimiento.setUserCode(sesion.getSesionUsuario().getUsuario().getIdUsuario().toString());
		nuevoMovimiento.setIsCompensation("false");
		try{
			variablesServices = SpringContext.getBean(VariablesServices.class);
			String grantType = variablesServices.getVariableAsString(GRANT_TYPE);
			String clientID = variablesServices.getVariableAsString(CLIENT_ID);
			String clientSecret = variablesServices.getVariableAsString(CLIENT_SECRET);
			String scopeCards = variablesServices.getVariableAsString(SCOPE_CARDS);
			/* Realizamos la inserción del movimiento. */
			RespuestaTokenDTO respuestaToken = SpringContext.getBean(GiftCardService.class).solicitarTokenGiftCard(new SolicitarTokenDTO(grantType, clientID, clientSecret, null, scopeCards));
			SpringContext.getBean(GiftCardService.class).insertarMovimientoGiftCard(nuevoMovimiento, respuestaToken.getAccess_token(), enviarMovimiento.getNumeroTarjeta());
		}
		catch(RestTimeoutException e){
			log.error("realizarCompraSaldoTarjetaRest() - No se puede establecer conexión con el servidor.", e);
			throw new RestException("No se puede establecer conexión con el servidor.", e);
		}
		catch(Exception e){
			log.error("realizarCompraSaldoTarjetaRest() - Ha ocurrido un error al realizar el movimiento " + "de la tarjeta : " + enviarMovimiento.getNumeroTarjeta(), e);
			throw new RestException(e.getMessage(), e);
		}
	}

	/**
	 * Realiza la compra de saldo para una tarjeta regalo.
	 * @param mov : Movimiento de inserción de saldo.
	 * @throws RestException
	 * @throws RestHttpException
	 */
	public void realizarDevolucionTarjetaRestReservas(EnviarMovimientoGiftCardBean mov) throws RestException, RestHttpException{
		Sesion sesion = SpringContext.getBean(Sesion.class);
		/* Realizar movimiento de suma de cantidad a la tarjeta. */
		GiftCardMovimientoDTO nuevoMovimiento = new GiftCardMovimientoDTO();
		if(mov.getTypeCode() == 0){ /* 0 = Inactiva*/
			nuevoMovimiento.setTypeCode("0");
		}
		else{
			nuevoMovimiento.setTypeCode("1");
		}
		/* Esta cantidad siempre es en positivo. */
		nuevoMovimiento.setAmount(mov.getEntrada().toString());
		nuevoMovimiento.setCurrencyCode(getCodMoneda(sesion.getAplicacion().getTienda().getCliente().getCodpais()));
		nuevoMovimiento.setDocumentNumber(mov.getDocumentNumber());
		nuevoMovimiento.setSourceDocumentNumber(mov.getSourceDocumentNumber());
		nuevoMovimiento.setShopCode(sesion.getSesionCaja().getCajaAbierta().getCodAlmAsString());
		nuevoMovimiento.setUserCode(sesion.getSesionUsuario().getUsuario().getIdUsuario().toString());
		nuevoMovimiento.setIsCompensation("false");
		try{
			variablesServices = SpringContext.getBean(VariablesServices.class);
			String grantType = variablesServices.getVariableAsString(GRANT_TYPE);
			String clientID = variablesServices.getVariableAsString(CLIENT_ID);
			String clientSecret = variablesServices.getVariableAsString(CLIENT_SECRET);
			String scopeCards = variablesServices.getVariableAsString(SCOPE_CARDS);
			/* Realizamos la inserción del movimiento. */
			RespuestaTokenDTO respuestaToken = SpringContext.getBean(GiftCardService.class).solicitarTokenGiftCard(new SolicitarTokenDTO(grantType, clientID, clientSecret, null, scopeCards));
			SpringContext.getBean(GiftCardService.class).insertarMovimientoGiftCard(nuevoMovimiento, respuestaToken.getAccess_token(), mov.getNumeroTarjeta());
		}
		catch(RestTimeoutException e){
			log.error("realizarCompraSaldoTarjetaRest() - No se puede establecer conexión con el servidor.", e);
			throw new RestException("No se puede establecer conexión con el servidor.", e);
		}
		catch(Exception e){
			log.error("realizarCompraSaldoTarjetaRest() - Ha ocurrido un error al realizar el movimiento " + "de la tarjeta : " + mov.getNumeroTarjeta(), e);
			throw new RestException(e.getMessage(), e);
		}
	}

	/**
	 * Se calculan los movimientos realizados con la tarjeta y se ejecutan los movimientos.
	 * @param stage
	 * @param uidActividad
	 * @param giftcards
	 * @param uidTicket
	 * @param concepto
	 * @param esVenta
	 */
	public void crearMovimientosProvisionales(Stage stage, String uidActividad, List<GiftCardBean> giftcards, String uidTicket, String codTicket,
			String origen) throws RestException, RestHttpException, RestTimeoutException{
		List<EnviarMovimientoGiftCardBean> movimientos = new ArrayList<EnviarMovimientoGiftCardBean>();
		
		for(GiftCardBean giftCardBean : giftcards){
			EnviarMovimientoGiftCardBean enviarMovimiento = new EnviarMovimientoGiftCardBean();
			enviarMovimiento.setSalida(giftCardBean.getImportePago().doubleValue());
			enviarMovimiento.setNumeroTarjeta(giftCardBean.getNumTarjetaRegalo());
			enviarMovimiento.setTypeCode(((ByLGiftCardBean) giftCardBean).getEstado());
			enviarMovimiento.setDocumentNumber(codTicket);
			enviarMovimiento.setSourceDocumentNumber(origen);
			movimientos.add(enviarMovimiento);
			
//			MovimientoRequestRest mov = new MovimientoRequestRest();
//			mov.setNumeroTarjeta(giftCardBean.getNumTarjetaRegalo());
//			mov.setUidActividad(uidActividad);
//			mov.setConcepto(concepto + " " + ((ByLGiftCardBean) giftCardBean).getEstado());
//			mov.setUidTransaccion(giftCardBean.getUidTransaccion());
//			mov.setFecha(new Date());
//			mov.setDocumento(String.valueOf(uidTicket));
//			mov.setApiKey(wsApiKey);
//			mov.setSaldo(giftCardBean.getSaldo().doubleValue());
//			mov.setSaldoProvisional(giftCardBean.getSaldoProvisional().doubleValue());
//			mov.setSalida(giftCardBean.getImportePago().doubleValue());
//			mov.setEntrada(0.0);
		}
		realizarPeticionListaMovimientos(movimientos);
	}
	
	/**
	 * Crea un movimiento provicional de la tarjeta de regalo.
	 */
	public void realizarPeticionListaMovimientos(List<EnviarMovimientoGiftCardBean> movimientos) throws RestException, RestHttpException, RestTimeoutException{
		Sesion sesion = SpringContext.getBean(Sesion.class);
		for(EnviarMovimientoGiftCardBean movimiento : movimientos){
			GiftCardMovimientoDTO nuevoMovimiento = new GiftCardMovimientoDTO();
			Double amount = Math.abs(movimiento.getSalida());
	
			if (movimiento.getSalida() > 0) {
				amount = amount * -1;
			}

			if(StringUtils.isNotBlank(movimiento.getSourceDocumentNumber())){
				nuevoMovimiento.setSourceDocumentNumber(movimiento.getSourceDocumentNumber());
			}
			if (movimiento.getTypeCode() == 0) {/* 0 = Inactiva */
				nuevoMovimiento.setTypeCode("0");
			}
			else {
				nuevoMovimiento.setTypeCode("1");
			}
			nuevoMovimiento.setIsCompensation("false");
			nuevoMovimiento.setAmount(amount.toString());
			nuevoMovimiento.setCurrencyCode(getCodMoneda(sesion.getAplicacion().getTienda().getCliente().getCodpais()));
			nuevoMovimiento.setShopCode(sesion.getSesionCaja().getCajaAbierta().getCodAlmAsString());
			nuevoMovimiento.setUserCode(sesion.getSesionUsuario().getUsuario().getIdUsuario().toString());
			nuevoMovimiento.setDocumentNumber(movimiento.getDocumentNumber());

			/* En caso de tener el "sourceDocumentNumber" significa que viene de una devolución, y el "amount" en
			 * negativo significa que viene de una venta en una devolución con lo cual debemos quitar el
			 * "sourceDocumentNumber" sino da error */
			if(StringUtils.isNotBlank(nuevoMovimiento.getSourceDocumentNumber()) && amount < 0){
				nuevoMovimiento.setSourceDocumentNumber(null);
			}

			try{
				variablesServices = SpringContext.getBean(VariablesServices.class);
				String grantType = variablesServices.getVariableAsString(GRANT_TYPE);
				String clientID = variablesServices.getVariableAsString(CLIENT_ID);
				String clientSecret = variablesServices.getVariableAsString(CLIENT_SECRET);
				String scopeCards = variablesServices.getVariableAsString(SCOPE_CARDS);
				/* Realizamos la inserción del movimiento. */
				RespuestaTokenDTO respuestaToken = SpringContext.getBean(GiftCardService.class).solicitarTokenGiftCard(new SolicitarTokenDTO(grantType, clientID, clientSecret, null, scopeCards));
				SpringContext.getBean(GiftCardService.class).insertarMovimientoGiftCard(nuevoMovimiento, respuestaToken.getAccess_token(), movimiento.getNumeroTarjeta());
				tarjetasRegaloAnulables.add(movimiento.getNumeroTarjeta());
			}
			catch(RestTimeoutException e){
				log.error("realizarPeticionListaMovimientosRest() - No se puede establecer conexión con el servidor.", e);
				throw new RestException("No se puede establecer conexión con el servidor.", e);
			}
			catch(Exception e){
				log.error("realizarPeticionListaMovimientosRest() - Ha ocurrido un error al realizar el movimiento " + "de la tarjeta : " + movimiento.getNumeroTarjeta(), e);
				throw new RestException(e.getMessage(), e);
			}
		}
	}
    
    public List<String> obtenerTarjetasRegaloAnulables() {
    	log.debug("obtenerTarjetasRegaloAnulables() - Obtenemos las tarjetas que debemos de anular");
        return new ArrayList<String>(tarjetasRegaloAnulables);
    }
    
    public void limpiarListaTarjetasRegaloAnulables() {
    	log.debug("limpiarListaTarjetasRegaloAnulables()");
        tarjetasRegaloAnulables.clear();
    }

	/**
	 * Anula el movimiento de una tarjeta de regalo, se suele realizar si se produce un error en el
	 * procesamiento del ticket.
	 */
	protected void realizarPeticionAnulacionListaMovimientosRest(List<MovimientoRequestRest> movimientos) throws RestException, RestHttpException{
		Sesion sesion = SpringContext.getBean(Sesion.class);
		for(MovimientoRequestRest movimiento : movimientos){
			GiftCardMovimientoDTO nuevoMovimiento = new GiftCardMovimientoDTO();
			nuevoMovimiento.setTypeCode("1");
			String codigoTicket[] = movimiento.getConcepto().split(" ");
			nuevoMovimiento.setDocumentNumber(codigoTicket[2]);
			Double amount = Math.abs(movimiento.getSalida());
			if(movimiento.getSalida() < 0){
				amount = amount * -1;
				nuevoMovimiento.setSourceDocumentNumber(codigoTicket[3]);
			}
			nuevoMovimiento.setAmount(amount.toString());
			nuevoMovimiento.setCurrencyCode(getCodMoneda(sesion.getAplicacion().getTienda().getCliente().getCodpais()));
			nuevoMovimiento.setShopCode(sesion.getSesionCaja().getCajaAbierta().getCodAlmAsString());
			nuevoMovimiento.setUserCode(sesion.getSesionUsuario().getUsuario().getIdUsuario().toString());
			nuevoMovimiento.setIsCompensation("true");
			try{
				variablesServices = SpringContext.getBean(VariablesServices.class);
				String grantType = variablesServices.getVariableAsString(GRANT_TYPE);
				String clientID = variablesServices.getVariableAsString(CLIENT_ID);
				String clientSecret = variablesServices.getVariableAsString(CLIENT_SECRET);
				String scopeCards = variablesServices.getVariableAsString(SCOPE_CARDS);
				/* Realizamos la anulación del movimiento. */
				RespuestaTokenDTO respuestaToken = SpringContext.getBean(GiftCardService.class).solicitarTokenGiftCard(new SolicitarTokenDTO(grantType, clientID, clientSecret, null, scopeCards));
				SpringContext.getBean(GiftCardService.class).insertarMovimientoGiftCard(nuevoMovimiento, respuestaToken.getAccess_token(), movimiento.getNumeroTarjeta());
			}
			catch(Exception e){
				log.error("realizarPeticionAnulacionListaMovimientosRest() - Ha ocurrido un error al realizar el movimiento " + "de anulación de la tarjeta : " + movimiento.getNumeroTarjeta(), e);
			}
		}
	}
	
	/**
	 * Realiza la cancelación de una compra (activacion + recarga) de una tarjeta regalo
	 * 
	 * @param mov
	 *            : Movimiento de inserción de saldo.
	 * @throws RestException
	 * @throws RestHttpException
	 */
	public void realizarCancelacionCompraGifcard(EnviarMovimientoGiftCardBean mov) throws RestException, RestHttpException{
		Sesion sesion = SpringContext.getBean(Sesion.class);
		GiftCardMovimientoDTO nuevoMovimiento = new GiftCardMovimientoDTO();
	
		nuevoMovimiento.setTypeCode("0");
		
		nuevoMovimiento.setAmount(mov.getEntrada().toString());
		nuevoMovimiento.setCurrencyCode(getCodMoneda(sesion.getAplicacion().getTienda().getCliente().getCodpais()));
		nuevoMovimiento.setDocumentNumber(mov.getDocumentNumber());
		nuevoMovimiento.setSourceDocumentNumber(mov.getSourceDocumentNumber());
		nuevoMovimiento.setShopCode(sesion.getSesionCaja().getCajaAbierta().getCodAlmAsString());
		nuevoMovimiento.setUserCode(sesion.getSesionUsuario().getUsuario().getIdUsuario().toString());
		nuevoMovimiento.setIsCompensation("true");
		try{
			variablesServices = SpringContext.getBean(VariablesServices.class);
			String grantType = variablesServices.getVariableAsString(GRANT_TYPE);
			String clientID = variablesServices.getVariableAsString(CLIENT_ID);
			String clientSecret = variablesServices.getVariableAsString(CLIENT_SECRET);
			String scopeCards = variablesServices.getVariableAsString(SCOPE_CARDS);
			
			RespuestaTokenDTO respuestaToken = SpringContext.getBean(GiftCardService.class).solicitarTokenGiftCard(new SolicitarTokenDTO(grantType, clientID, clientSecret, null, scopeCards));
			SpringContext.getBean(GiftCardService.class).insertarMovimientoCancelacionGiftCard(nuevoMovimiento, respuestaToken.getAccess_token(), mov.getNumeroTarjeta());
		}
		catch(RestTimeoutException e){
			log.error("realizarCompraSaldoTarjetaRest() - No se puede establecer conexión con el servidor.", e);
			throw new RestException("No se puede establecer conexión con el servidor.", e);
		}
		catch(Exception e){
			log.error("realizarCompraSaldoTarjetaRest() - Ha ocurrido un error al realizar el movimiento " + "de la tarjeta : " + mov.getNumeroTarjeta(), e);
			throw new RestException(e.getMessage(), e);
		}
	}
	
    /* ###################################################################################################### */
	/* ####################################### MÉTODOS DE UTILIDAD ########################################## */
	/* ###################################################################################################### */
	
	/**
	 * Devuelve el código de divisa del pais proporcionado.
	 * @param codPais : Código del país ha consultar.
	 * @return
	 */
	public String getCodMoneda(String codPais){
		PaisBean pais = null;
		try{
			pais = SpringContext.getBean(PaisService.class).consultarCodPais(codPais);
		}
		catch(PaisNotFoundException | PaisServiceException e){
			String mensajeError = "Se ha producido un error al consultar el País";
			log.error("getCodMoneda() - " + mensajeError, e);
		}
		return pais.getCodDivisa();
	}

	/**
	 * Devuelve el estado de la tarjeta Regalo consultada.
	 * @param stateCode
	 * @return
	 */
//	public String obtenerEstadoTarjeta(Integer stateCode){
//		switch (stateCode){
//			case 0:
//				return I18N.getTexto("Inactiva");
//			case 1:
//				return I18N.getTexto("Activa");
//			case 2:
//				return I18N.getTexto("Desactivada");
//			case 3:
//				return I18N.getTexto("Consumida");
//			case 4:
//				return I18N.getTexto("Cancelada");
//
//			default:
//				return null;
//		}
//	}

	/**
	 * Permite la lectura de la tarjeta a través de una ventana.
	 */
	protected String leerNumeroTarjeta(Stage stage){
		/* Leemos la tarjeta */
		HashMap<String, Object> parametros = new HashMap<>();
		parametros.put(CodigoTarjetaController.PARAMETRO_IN_TEXTOCABECERA, I18N.getTexto("Lea o escriba el código de barras de la tarjeta de regalo"));
		parametros.put(CodigoTarjetaController.PARAMETRO_TIPO_TARJETA, "GIFTCARD");

		POSApplication posApplication = POSApplication.getInstance();
		posApplication.getMainView().showModalCentered(CodigoTarjetaView.class, parametros, stage);
		/* Leemos el código leido de la tarjeta */
		String numTarjeta = (String) parametros.get(CodigoTarjetaController.PARAMETRO_NUM_TARJETA);
		return numTarjeta;
	}

	public String getNombreTarjeta(){
		return nombreTarjeta;
	}

	public void setNombreTarjeta(String nombreTarjeta){
		this.nombreTarjeta = nombreTarjeta;
	}

	public String getApikey(){
		return wsApiKey;
	}
    
}
