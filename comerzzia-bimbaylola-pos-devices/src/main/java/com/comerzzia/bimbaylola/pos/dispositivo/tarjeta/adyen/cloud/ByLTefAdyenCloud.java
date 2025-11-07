package com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.adyen.cloud;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.adyen.enums.Environment;
import com.adyen.model.applicationinfo.ApplicationInfo;
import com.adyen.model.nexo.ErrorConditionType;
import com.adyen.model.nexo.MessageCategoryType;
import com.adyen.model.nexo.Response;
import com.adyen.model.nexo.ResultType;
import com.adyen.model.nexo.SaleToPOIResponse;
import com.adyen.model.terminal.TerminalAPIRequest;
import com.adyen.model.terminal.TerminalAPIResponse;
import com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.adyen.AdyenDatosPeticionPagoTarjeta;
import com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.adyen.cloud.service.ByLAdyenCloudService;
import com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.adyen.service.ByLAdyenService;
import com.comerzzia.bimbaylola.pos.services.dispositivofirma.ByLConfiguracionModelo;
import com.comerzzia.bimbaylola.pos.services.dispositivofirma.DispositivoFirmaException;
import com.comerzzia.bimbaylola.pos.services.dispositivofirma.IFirma;
import com.comerzzia.bimbaylola.pos.services.ticket.ByLTicketsService;
import com.comerzzia.pos.core.dispositivos.Dispositivos;
import com.comerzzia.pos.core.dispositivos.configuracion.ConfiguracionDispositivo;
import com.comerzzia.pos.core.dispositivos.dispositivo.DispositivoException;
import com.comerzzia.pos.core.dispositivos.dispositivo.tarjeta.TarjetaCallback;
import com.comerzzia.pos.core.dispositivos.dispositivo.tarjeta.TarjetaException;
import com.comerzzia.pos.dispositivo.tarjeta.adyen.cloud.TefAdyenCloud;
import com.comerzzia.pos.dispositivo.tarjeta.adyen.constans.AdyenConstans;
import com.comerzzia.pos.dispositivo.tarjeta.adyen.exception.AdyenException;
import com.comerzzia.pos.persistence.core.contadores.ContadorBean;
import com.comerzzia.pos.services.core.contadores.ServicioContadores;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.services.ticket.pagos.tarjeta.DatosPeticionPagoTarjeta;
import com.comerzzia.pos.services.ticket.pagos.tarjeta.DatosRespuestaPagoTarjeta;
import com.comerzzia.pos.util.config.SpringContext;
import com.comerzzia.pos.util.i18n.I18N;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javafx.stage.Stage;

@Component
@Primary
public class ByLTefAdyenCloud extends TefAdyenCloud  implements IFirma{

	protected ServicioContadores servicioContadores;
	protected Sesion sesion;

	private Logger log = Logger.getLogger(ByLTefAdyenCloud.class);

	protected ByLAdyenService adyenServices = new ByLAdyenService();
	protected ByLAdyenCloudService adyenCloudServices = new ByLAdyenCloudService();

	protected String modelo;
	protected String modoConexion;
	protected HashMap<String, ByLConfiguracionModelo> listaConfiguracion;
	protected ByLConfiguracionModelo configuracionActual;
	
	protected Map<String, Object> mapaRespuestas = new HashMap<String, Object>();
	
	protected static int contServiceID = 0;
	protected static String PAYMENTS2 = "PAYMENTS2";
        protected static final String TERMINAL_API_URL = "TERMINAL_API_URL";


        protected String terminalApiUrl;
	
	protected String payments2;
	
	@Override
	protected DatosRespuestaPagoTarjeta solicitarDevolucion(DatosPeticionPagoTarjeta request) throws TarjetaException {
		log.debug("solicitarDevolucion() - " + I18N.getTexto("Iniciamos la acción para solicitar una devoluciónen Adyen Cloud..."));
		DatosRespuestaPagoTarjeta response = new DatosRespuestaPagoTarjeta(request);
		TerminalAPIResponse returnResponse = null;
		try {
			checkConfigurationData();
			ApplicationInfo applicationInfo = adyenServices.generateValidationMessage(merchantApplicationName, merchantApplicationVersion, merchantDeviceSystem, merchantDeviceVersion,
			        merchantDeviceReference);
			TerminalAPIRequest returnRequest = adyenServices.generateMessage(request, pOIID, currency, AdyenConstans.RETURN, null, null, protocol, applicationInfo);
					
			returnResponse = adyenCloudServices.sendRequest(client, AdyenConstans.RETURN, returnRequest);
		}
		catch (Exception e) {
			/* If the error occurs when making the request, it is not canceled because it has not been completed. */
			throw new TarjetaException(e.getMessage(), response);
		}

		/* We check for errors in the request. */
		String errorMessageAdditional = getProcessResponse(returnResponse, response);
		if (StringUtils.isNotBlank(errorMessageAdditional)) {
			if (request.getIdDocumentoOrigen() != null && !request.getIdDocumentoOrigen().isEmpty()) {
				// Significa que ha fallado desde una devolución referenciada, por lo que se intentará realizar una
				// devolución no referenciada (pedirá tarjeta)
				request.setIdDocumentoOrigen(null);

				String idTransaccion = consultarContadorTransaccion();
				request.setIdTransaccion(idTransaccion);

				response = solicitarDevolucion(request);
			}
			else {
				String errorMessage = I18N.getTexto("Error al realizar la DEVOLUCIÓN en el dispositivo Adyen en modo Cloud: \n" + "- " + errorMessageAdditional);
				log.error("solicitarDevolucion() - " + errorMessage);
				throw new TarjetaException(errorMessage, response);
			}
		}

		log.debug("solicitarDevolucion() - " + I18N.getTexto("Finalizada la acción para solicitar una devolución en Adyen Cloud"));
		return response;
	}
	
	@Override
	protected DatosRespuestaPagoTarjeta solicitarPago(DatosPeticionPagoTarjeta request) throws TarjetaException {
		log.debug("solicitarPago() - " + I18N.getTexto("Iniciamos la acción para solicitar una venta en Adyen Cloud..."));
		DatosRespuestaPagoTarjeta response = new DatosRespuestaPagoTarjeta(request);
		TerminalAPIResponse saleResponse = null;
		TerminalAPIRequest saleRequest = null;
		try {
			checkConfigurationData();
			ApplicationInfo applicationInfo = adyenServices.generateValidationMessage(merchantApplicationName, merchantApplicationVersion, merchantDeviceSystem, merchantDeviceVersion,
			        merchantDeviceReference);
			saleRequest = adyenServices.generateMessage(request, pOIID, currency, AdyenConstans.SALE, null, null, protocol, applicationInfo);
			saleResponse = adyenCloudServices.sendRequest(client, AdyenConstans.SALE, saleRequest);
		}
		catch (Exception e) {
			/* If the error occurs when making the request, it is not canceled because it has not been completed. */
//			throw new TarjetaException(e.getMessage(), response);
			
			saleResponse = controlErrores(saleResponse, saleRequest, request, response);
		}
		
		/* We check for errors in the request. */
		String errorMessageAdditional = getProcessResponse(saleResponse, response);
		if (StringUtils.isNotBlank(errorMessageAdditional)) {
			String errorMessage = I18N.getTexto("Error al realizar la VENTA en el dispositivo Adyen en modo Cloud: \n" + "- " +  errorMessageAdditional);
			log.error("solicitarPago() - " + errorMessage);
			throw new TarjetaException(errorMessage, response);
		}

		log.debug("solicitarPago() - " + I18N.getTexto("Finalizada la acción para solicitar una venta en Adyen Cloud"));
		return response;
	}
	
	@Override
	protected String getProcessResponse(TerminalAPIResponse requestResponse, DatosRespuestaPagoTarjeta response) throws TarjetaException {
	    String error = super.getProcessResponse(requestResponse, response);
	    if (StringUtils.isBlank(error)) {
	        response.setApplicationLabel(response.getAID());
	    }
	    return error;
	}

	
	// Incidencia 106269
	@Override
	public void solicitarAnulacionPago(List<DatosPeticionPagoTarjeta> datosPago, Stage stage, TarjetaCallback<List<DatosRespuestaPagoTarjeta>> callback) {

		log.debug("solicitarAnulacionPago() - " + I18N.getTexto("Iniciamos la acción para solicitar una anulación del pago en Adyen Cloud..."));
		try {
			checkConfigurationData();
			
			String numTransaccion = null;
			if(!datosPago.isEmpty()) {
				DatosPeticionPagoTarjeta datosPeticionPagoTarjeta = datosPago.get(0);
				if(datosPeticionPagoTarjeta instanceof AdyenDatosPeticionPagoTarjeta) {
					numTransaccion = ((AdyenDatosPeticionPagoTarjeta) datosPeticionPagoTarjeta).getNumTransaccion();
				}
			}
			
			if(StringUtils.isNotBlank(numTransaccion)) {
				adyenServices.cancel(client, merchantAccount, numTransaccion);
			}
		}
		catch (AdyenException e) {
			log.debug("solicitarAnulacionPago() - " + I18N.getTexto("Error al solicitar anulación del pago en Adyen Cloud"));
		}
	}


	public String consultarContadorTransaccion() {
		String idTransaccion = null;
		try {
			log.debug("consultarContadorTransaccion() - Obteniendo contador para transacción...");

			sesion = SpringContext.getBean(Sesion.class);
			servicioContadores = SpringContext.getBean(ServicioContadores.class);
			ContadorBean idTransaccionContador = servicioContadores.obtenerContador(ByLTicketsService.CONTADOR_TRANSACCION, sesion.getAplicacion().getUidActividad());

			idTransaccion = String.valueOf(idTransaccionContador.getValor());
		}
		catch (Exception e) {
			String msg = "Se ha producido un error consultando el contador :" + e.getMessage();
			log.error("consultarContadorTransaccion() - " + msg, e);
		}

		return idTransaccion;
	}

	/* ================================================================================================================ */
	/* =================================================== FIRMA ====================================================== */
	/* ================================================================================================================ */


	@Override
	public Map<String, Object> firmar() throws DispositivoFirmaException {
		log.debug("firmar() - " + I18N.getTexto("Iniciamos la acción para solicitar firma en Adyen Cloud..."));

		/* Se cargará la configuración del dispositivo de tarjeta Adyen */
		if (Dispositivos.getInstance().getTarjeta() instanceof ByLTefAdyenCloud) {
			ConfiguracionDispositivo config = Dispositivos.getInstance().getTarjeta().getConfiguracion();
			try {
				cargaConfiguracion(config);
			}
			catch (DispositivoException e) {
				log.error("iniciarDispositivoFirma() - No se ha podido inicializar el dispositivo: " + e.getMessage());
			}
		}
		else {
			throw new DispositivoFirmaException(I18N.getTexto("No se ha podido cargar la configuración del dispositivo de tarjeta Adyen"));
		}

		byte[] firma = null;
		boolean consentimientoNotif = false;
		boolean consentimientoUsoDatos = false;
		try {
			consentimientoNotif = pedirConsentimiento(MENSAJE_CHECK_NOTIFICACIONES);
			log.debug("firmar() - " + I18N.getTexto("Consentimiento Notificaciones realizado"));

			Thread.sleep(3000);
			
			consentimientoUsoDatos = pedirConsentimiento(MENSAJE_CHECK_USODATOS);
			log.debug("firmar() - " + I18N.getTexto("Consentimiento Uso de Datos realizado"));

			Thread.sleep(3000);
			
			firma = pedirFirma();
			log.debug("firmar() - " + I18N.getTexto("Firma realizada"));

		}
		catch (Exception e) {
			log.error("iniciarDispositivoFirma() - Error al solicitar los consentimientos/firma: " + e.getMessage());
			throw new DispositivoFirmaException(I18N.getTexto("Error al solicitar los consentimientos/firma."));
		}

		log.debug("firmar() - " + I18N.getTexto("Finalizada la acción para solicitar firma en Adyen Cloud"));

		mapaRespuestas.put(IMAGEN_FIRMA, firma);
		mapaRespuestas.put(RESPUESTA_CONSENTIMIENTO_NOTIFICACIONES, consentimientoNotif);
		mapaRespuestas.put(RESPUESTA_CONSENTIMIENTO_USO_DATOS, consentimientoUsoDatos);

		return mapaRespuestas;
	}
	
	@SuppressWarnings("deprecation")
	private byte[] pedirFirma() throws Exception {
		byte[] firma = null;
		TerminalAPIResponse saleResponse = null;

		checkConfigurationData();

		String idTransaccion = consultarContadorFirmaAdyen();
		TerminalAPIRequest saleRequest = adyenServices.generateMessageFirma(idTransaccion, pOIID, protocol);
		saleResponse = adyenCloudServices.sendRequest(client, MessageCategoryType.INPUT.value(), saleRequest);

		/* We check for errors in the request. */
		String errorMessageAdditional = getProcessResponseFirma(saleResponse);
		if (StringUtils.isNotBlank(errorMessageAdditional)) {
			String errorMessage = I18N.getTexto("Error al realizar la firma en el dispositivo Adyen en modo Cloud: \n" + "- " + errorMessageAdditional);
			log.error("pedirFirma() - " + errorMessage);
			throw new DispositivoFirmaException(errorMessage);
		}

		String additionalResponse = null;
		if (saleResponse.getSaleToPOIResponse().getInputResponse().getInputResult().getInput().isConfirmedFlag()) {
			additionalResponse = saleResponse.getSaleToPOIResponse().getInputResponse().getInputResult().getResponse().getAdditionalResponse();
			additionalResponse = URLDecoder.decode(additionalResponse, StandardCharsets.UTF_8.name());

			String responseData = additionalResponse.split("=")[1].split("&")[0];
			JsonElement root = new JsonParser().parse(responseData);
			JsonArray data = root.getAsJsonObject().get("signature").getAsJsonObject().get("data").getAsJsonArray();

			firma = tratarFirma(data);
		}

		return firma;
	}
	
	private boolean pedirConsentimiento(String texto) throws Exception {
		log.debug("pedirConsentimiento() - " + I18N.getTexto("Inicio del proceso de pedir consentimiento : ") + texto);
		
		boolean consentimiento = false;
		TerminalAPIResponse saleResponse = null;

		checkConfigurationData();

		String idTransaccion = consultarContadorFirmaAdyen();
		TerminalAPIRequest saleRequest = adyenServices.generateMessageConsentimiento(idTransaccion, pOIID, protocol, texto);
		saleResponse = adyenCloudServices.sendRequest(client, MessageCategoryType.INPUT.value(), saleRequest);

		/* We check for errors in the request. */
		String errorMessageAdditional = getProcessResponseFirma(saleResponse);
		if (StringUtils.isNotBlank(errorMessageAdditional)) {
			String errorMessage = I18N.getTexto("Error al realizar el consentimiento en el dispositivo Adyen en modo Cloud: \n" + "- " + errorMessageAdditional);
			log.error("pedirConsentimiento() - " + errorMessage);
			throw new DispositivoFirmaException(errorMessage);
		}

		consentimiento = saleResponse.getSaleToPOIResponse().getInputResponse().getInputResult().getInput().isConfirmedFlag();

		return consentimiento;
	}

	protected String getProcessResponseFirma(TerminalAPIResponse requestResponse) throws TarjetaException {
		String errorMessageAdditional = "";
		try {
			if (requestResponse != null && requestResponse.getSaleToPOIResponse() != null && (requestResponse.getSaleToPOIResponse().getInputResponse() != null)) {
				ResultType result = requestResponse.getSaleToPOIResponse().getInputResponse().getInputResult().getResponse().getResult();

				if (result != null) {
					if (ResultType.FAILURE.equals(result)) {
						/* In case an error has occurred, the originalReference should be taken */
						ErrorConditionType error = null;
						error = requestResponse.getSaleToPOIResponse().getInputResponse().getInputResult().getResponse().getErrorCondition();

						if (error != null) {
							/* We check the error with the errors already controlled */
							for (Map.Entry<ErrorConditionType, String> entry : errorMap.entrySet()) {
								if (entry.getKey().equals(error)) {
									errorMessageAdditional = entry.getValue();
								}
							}
							if (StringUtils.isBlank(errorMessageAdditional)) {
								errorMessageAdditional = I18N.getTexto("Error no controlado en la respuesta del dispositivo Adyen.");
							}
						}
					}
//					else if (ResultType.SUCCESS.equals(result)) {
//						if(requestResponse.getSaleToPOIResponse().getInputResponse().getInputResult().getInput().isConfirmedFlag()) {
//						}
//					}
				}
			}
			else {
				errorMessageAdditional = I18N.getTexto("No se ha recibido respuesta desde el dispositivo Adyen.");
			}
		}
		catch (Exception e) {
			return I18N.getTexto("Error al leer los datos de respuesta del dispositivo Adyen.");
		}
		return errorMessageAdditional;
	}
	
	private byte[] tratarFirma(JsonArray signaturePoint) {
		byte[] firma = null;
		try {
			// Separate each object in a list
			List<JsonObject> signaturePoints = new ArrayList<JsonObject>();
			for (int i = 0; i < signaturePoint.size(); i++) {
				signaturePoints.add(signaturePoint.get(i).getAsJsonObject());
			}

			List<Integer> x = new ArrayList<Integer>();
			List<Integer> y = new ArrayList<Integer>();

			// For each objectm retrieve the x and the y coordinates and convert them to decimal Integers and add the to
			// a list 
			for (JsonObject point : signaturePoints) {
				x.add(Integer.parseInt(point.get("x").getAsString(), 16));
				y.add(Integer.parseInt(point.get("y").getAsString(), 16));
			}

			// Retrieve the smallest coordinate
			Integer minX = Collections.min(x);
			Integer minY = Collections.min(y);

			// 0xFFFF = 65535. 0xFFFF marks the end of the stroke. Replace 65535 by -1
			Collections.replaceAll(x, 65535, -1);
			Collections.replaceAll(y, 65535, -1);

			// Get the largest coordinate
			Integer maxX = Collections.max(x);
			Integer maxY = Collections.max(y);

			// Get the image width and height. Add 60 for margins
			Integer width = maxX - minX + 60;
			Integer height = maxY - minY + 60;

			// Modify the coordinates subtracting the smallest coordinate. The 20 is just for margin
			for (int i = 0; i < x.size(); i++) {
				if (!x.get(i).equals(-1)) {
					x.set(i, x.get(i) - minX + 20);
				}
			}

			for (int i = 0; i < y.size(); i++) {
				if (!y.get(i).equals(-1)) {
					y.set(i, y.get(i) - minY + 20);
				}
			}

			// Draw frame and save image
			firma = guardarFirmaImagen(width, height, x, y);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return firma;
	}
	
	private byte[] guardarFirmaImagen(Integer width, Integer height, List<Integer> x, List<Integer> y) throws IOException {
		byte[] imageBytes = null;

		BasicStroke bs = new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);

		/*
		 * Save signature as an image
		 */
		BufferedImage signatureImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D signature = signatureImage.createGraphics();

		signature.setBackground(Color.WHITE); // Background color
		signature.clearRect(0, 0, width, height);
		signature.setColor(Color.BLACK); // Stroke color
		signature.setStroke(bs);

		for (int i = 0; i < x.size(); i++) {
			if (!x.get(i).equals(-1) && !x.get(i + 1).equals(-1)) {
				signature.drawLine(x.get(i), y.get(i), x.get(i + 1), y.get(i + 1));
			}
		}

		signature.dispose();
		RenderedImage rendImage = signatureImage;

		// Save as PNG
		File signatureFile = new File("./signature.png");
		ImageIO.write(rendImage, "png", signatureFile);

		signatureImage = ImageIO.read(signatureFile);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(signatureImage, "bmp", baos);
		baos.flush();
		baos.close();

		signatureFile.delete();

		imageBytes = baos.toByteArray();

		return imageBytes;
	}
	
	public String consultarContadorFirmaAdyen() {
		String idTransaccion = null;
		try {
			log.debug("consultarContadorTransaccion() - Obteniendo contador para transacción...");

			sesion = SpringContext.getBean(Sesion.class);
			servicioContadores = SpringContext.getBean(ServicioContadores.class);
			ContadorBean idTransaccionContador = servicioContadores.obtenerContador(ByLTicketsService.CONTADOR_FIRMA_ADYEN, sesion.getAplicacion().getUidActividad());

			idTransaccion = String.valueOf(idTransaccionContador.getValor());
		}
		catch (Exception e) {
			String msg = "Se ha producido un error consultando el contador :" + e.getMessage();
			log.error("consultarContadorTransaccion() - " + msg, e);
		}

		return idTransaccion;
	}

	private TerminalAPIResponse controlErrores(TerminalAPIResponse saleResponse, TerminalAPIRequest saleRequest, DatosPeticionPagoTarjeta request, DatosRespuestaPagoTarjeta response) throws TarjetaException {
		log.debug("controlErrores() - Iniciamos el proceso de comprobar si se ha producido un time out");

		try {
			TerminalAPIResponse saleResponseStatus = comprobarStatus(saleRequest, request);
			Response statusResponse = saleResponseStatus.getSaleToPOIResponse().getTransactionStatusResponse().getResponse();

			if (statusResponse != null) {
				log.debug("controlErrores() - Result obtenido de la peticion de status: " + statusResponse.getResult());

				/* SUCESS de la peticion de status */
				if (ResultType.SUCCESS.equals(statusResponse.getResult())) {
					Response statusRepeatResponse = saleResponseStatus.getSaleToPOIResponse().getTransactionStatusResponse().getRepeatedMessageResponse().getRepeatedResponseMessageBody()
					        .getPaymentResponse().getResponse();
					log.debug("controlErrores() - Result obtenido del repeat response: " + statusRepeatResponse.getResult());

					saleResponse = new TerminalAPIResponse();
					SaleToPOIResponse salePoiResponse = new SaleToPOIResponse();
					saleResponse.setSaleToPOIResponse(salePoiResponse);
					/* SUCCESS (approved, cancelled, or declined) */
					if (ResultType.SUCCESS.equals(statusRepeatResponse.getResult())) {
						log.debug("controlErrores() - Se ha obtenido " + statusRepeatResponse.getResult() + ". Se añadiran los datos del repeatMessage al response principal");

						salePoiResponse.setPaymentResponse(
						        saleResponseStatus.getSaleToPOIResponse().getTransactionStatusResponse().getRepeatedMessageResponse().getRepeatedResponseMessageBody().getPaymentResponse());
						salePoiResponse.setMessageHeader(saleResponseStatus.getSaleToPOIResponse().getTransactionStatusResponse().getRepeatedMessageResponse().getMessageHeader());
					}
					/* FAILURE del pago origen */
					else if (ResultType.FAILURE.equals(statusRepeatResponse.getResult())) {
						log.debug("controlErrores() - Se ha obtenido " + statusRepeatResponse.getResult() + ". Se añadiran los datos del repeatMessage al response principal");
						salePoiResponse.setPaymentResponse(
						        saleResponseStatus.getSaleToPOIResponse().getTransactionStatusResponse().getRepeatedMessageResponse().getRepeatedResponseMessageBody().getPaymentResponse());
						salePoiResponse.setMessageHeader(saleResponseStatus.getSaleToPOIResponse().getTransactionStatusResponse().getRepeatedMessageResponse().getMessageHeader());
						
						// Aqui daría el UNRECHEABLE_HOST
					}
				}
				/* FAILURE de la peticion de status */
				else if (ResultType.FAILURE.equals(statusResponse.getResult())) {
					ErrorConditionType errorCondition = saleResponseStatus.getSaleToPOIResponse().getTransactionStatusResponse().getResponse().getErrorCondition();
					if (errorCondition.equals(ErrorConditionType.IN_PROGRESS)) { /* Dispositivo ocupado */
						/* SE HACEN PETICIONES DE STATUS CADA 5 SEGUNDOS HASTA OBTENER UN SUCCESS */
						try {
							Thread.sleep(5000);
						}
						catch (InterruptedException e) {
						}
						saleResponse = controlErrores(saleResponse, saleRequest, request, response);
					}
					else if (errorCondition.equals(ErrorConditionType.NOT_FOUND)) { /* Transaccion no encontrada */
						throw new TarjetaException(I18N.getTexto("Ha ocurrido un error en el pago y no se ha encontrado la transacción en el disposito TEF. No se ha realizado el pago"), response);
					}
					else {
						log.debug("Ha ocurrido un error en el pago TEF: " + errorCondition);
						log.debug("SaleID: " + saleRequest.getSaleToPOIRequest().getMessageHeader().getSaleID());
						log.debug("ServiceID: " + saleRequest.getSaleToPOIRequest().getMessageHeader().getServiceID());
						log.debug("POOID: " + saleRequest.getSaleToPOIRequest().getMessageHeader().getPOIID());
						
						throw new TarjetaException(I18N.getTexto("Ha ocurrido un error en el pago TEF: " + errorCondition
								+ "\n SaleID: " + saleRequest.getSaleToPOIRequest().getMessageHeader().getSaleID() 
								+ "\n ServiceID: " + saleRequest.getSaleToPOIRequest().getMessageHeader().getServiceID()
								+ "\n POOID: " + saleRequest.getSaleToPOIRequest().getMessageHeader().getPOIID()), response);
					}
				}
			}

		}
		catch (AdyenException e) {
			log.error("Ha ocurrido un error en el pago TEF:" + e.getMessage());
			log.error("SaleID: " + saleRequest.getSaleToPOIRequest().getMessageHeader().getSaleID());
			log.error("ServiceID: " + saleRequest.getSaleToPOIRequest().getMessageHeader().getServiceID());
			log.error("POOID: " + saleRequest.getSaleToPOIRequest().getMessageHeader().getPOIID());
			
			log.error("Ha ocurrido un error en el pago TEF ", e);
			throw new TarjetaException(I18N.getTexto("Ha ocurrido un error en el pago TEF: " + e.getMessage()
					+ "\n SaleID: " + saleRequest.getSaleToPOIRequest().getMessageHeader().getSaleID() 
					+ "\n ServiceID: " + saleRequest.getSaleToPOIRequest().getMessageHeader().getServiceID()
					+ "\n POOID: " + saleRequest.getSaleToPOIRequest().getMessageHeader().getPOIID()), response);
		}

		return saleResponse;
	}

	private TerminalAPIResponse comprobarStatus(TerminalAPIRequest saleRequest, DatosPeticionPagoTarjeta request) throws AdyenException {
		log.debug("comprobarStatus() - Inicio del envio de comprobacion de status al dispositivo de Adyen");
		TerminalAPIRequest saleRequestStatus = adyenServices.generateMessageStatus(saleRequest, getContServiceID(request));
		TerminalAPIResponse saleResponseStatus = adyenCloudServices.sendRequest(client, "STATUS", saleRequestStatus);
		log.debug("comprobarStatus() - Fin del envio de comprobacion de status al dispositivo de Adyen");

		return saleResponseStatus;
	}
	
	public static String getContServiceID(DatosPeticionPagoTarjeta request) {
		String serviceID = "";
		serviceID = contServiceID + request.getIdTransaccion().toString();
		contServiceID++;
		return serviceID;
	}
	
	@Override
	public void iniciarDispositivoFirma() {
	}
	
	@Override
	public String getManejador() {
		return listaConfiguracion.get(modelo).getManejador();
	}
	
	@Override
	public void getMetodosConexion() {
	}
	
	@Override
	public HashMap<String, ByLConfiguracionModelo> getListaConfiguracion() {
		return listaConfiguracion;
	}
	
	@Override
	public String getModelo() {
		return modelo;
	}
	
	@Override
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	
	@Override
	public String getModoConexion() {
		return modoConexion;
	}
	
	@Override
	public void setModoConexion(String modoConexion) {
		this.modoConexion = modoConexion;
	}
	
	@Override
	public ByLConfiguracionModelo getConfiguracionActual() {
		return configuracionActual;
	}
	
	@Override
	public void setConfiguracionActual(ByLConfiguracionModelo configuracionActual) {
		this.configuracionActual = configuracionActual;
	}
	
	@Override
	protected void cargaConfiguracion(ConfiguracionDispositivo config) throws DispositivoException {
		/* Se añade esta línea para que se pueda personalizar la clase de AdyenService*/
//		adyenServices = (ByLAdyenService) SpringContext.getBean(AdyenService.class);
				
		for (String param : config.getParametrosConfiguracion().keySet()) {
			if (param.equals(AdyenConstans.CLIENT)) {
				environment = config.getParametrosConfiguracion().get(param).equals(AdyenConstans.CLIENT_LIVE) ? Environment.LIVE : Environment.TEST;
			}
			else if (param.equals(AdyenConstans.POIID)) {
				pOIID = config.getParametrosConfiguracion().get(param);
			}
			else if (param.equals(AdyenConstans.PROTOCOLO)) {
				protocol = config.getParametrosConfiguracion().get(param);
			}
			else if (param.equals(AdyenConstans.MERCHANT_ACCOUNT)) {
				merchantAccount = config.getParametrosConfiguracion().get(param);
			}
			else if (param.equals(AdyenConstans.CURRENCY)) {
				currency = config.getParametrosConfiguracion().get(param);
			}
			else if (param.equals(AdyenConstans.API_KEY)) {
				apiKey = config.getParametrosConfiguracion().get(param);
			}
			else if (param.equals(AdyenConstans.PAYMENTS)) {
				payments = config.getParametrosConfiguracion().get(param);
			}
			else if (param.equals(PAYMENTS2)) {
				payments2 = config.getParametrosConfiguracion().get(param);
			}
                        else if (param.equals(TERMINAL_API_URL)) {
                                terminalApiUrl = StringUtils.trimToNull(config.getParametrosConfiguracion().get(param));
                        }
			else if (param.equals(AdyenConstans.MERCHANT_APPLICATION_NAME)) {
				merchantApplicationName = config.getParametrosConfiguracion().get(param);
			}
			else if (param.equals(AdyenConstans.MERCHANT_APPLICATION_VERSION)) {
				merchantApplicationVersion = config.getParametrosConfiguracion().get(param);
			}
			else if (param.equals(AdyenConstans.MERCHANT_DEVICE_SYSTEM)) {
				merchantDeviceSystem = config.getParametrosConfiguracion().get(param);
			}
			else if (param.equals(AdyenConstans.MERCHANT_DEVICE_VERSION)) {
				merchantDeviceVersion = config.getParametrosConfiguracion().get(param);
			}
			else if (param.equals(AdyenConstans.MERCHANT_DEVICE_REFERENCE)) {
				merchantDeviceReference = config.getParametrosConfiguracion().get(param);
			}
		}

		errorMap = new HashMap<ErrorConditionType, String>();
		errorMap.put(ErrorConditionType.ABORTED, I18N.getTexto("El dispositivo Adyen indica: La operación ha sido abortada."));
		errorMap.put(ErrorConditionType.BUSY, I18N.getTexto("El dispositivo está ocupado actualmente."));
		errorMap.put(ErrorConditionType.CANCEL, I18N.getTexto("La operación ha sido cancelada manualmente desde el dispositivo Adyen."));
		errorMap.put(ErrorConditionType.DEVICE_OUT, I18N.getTexto("No se ha podido establecer conexión con el dispositivo Adyen."));
		errorMap.put(ErrorConditionType.IN_PROGRESS, I18N.getTexto("El dispositivo Adyen tiene una operación en proceso."));
		errorMap.put(ErrorConditionType.INSERTED_CARD, I18N.getTexto("Error al insertar la tarjeta en el dispositivo Adyen."));
		errorMap.put(ErrorConditionType.INVALID_CARD, I18N.getTexto("La tarjeta usada para la operación no es válida."));
		errorMap.put(ErrorConditionType.LOGGED_OUT, I18N.getTexto("No se ha podido establecer conexión con el dispositivo Adyen."));
		errorMap.put(ErrorConditionType.MESSAGE_FORMAT, I18N.getTexto("Error en el formato del mensaje enviado al dispositivo Adyen."));
		errorMap.put(ErrorConditionType.NOT_ALLOWED, I18N.getTexto("El dispositivo Adyen indica: Operación no permitida"));
		errorMap.put(ErrorConditionType.NOT_FOUND, I18N.getTexto("No se ha podido establecer conexión con el dispositivo Adyen."));
		errorMap.put(ErrorConditionType.PAYMENT_RESTRICTION, I18N.getTexto("El dispositivo Adyen indica: Operación no permitida por restricción de pagos."));
		errorMap.put(ErrorConditionType.REFUSAL, I18N.getTexto("El dispositivo Adyen indica: Operación rechazada."));
		errorMap.put(ErrorConditionType.UNAVAILABLE_DEVICE, I18N.getTexto("El dispositivo Adyen no se encuentra disponible."));
		errorMap.put(ErrorConditionType.UNAVAILABLE_SERVICE, I18N.getTexto("El servicio del dispositivo Adyen no se encuentra disponible."));
		errorMap.put(ErrorConditionType.UNREACHABLE_HOST, I18N.getTexto("El dispositivo Adyen no puede establecer conexión con el servidor."));
		errorMap.put(ErrorConditionType.WRONG_PIN, I18N.getTexto("El PIN introducido en el dispositivo Adyen no es correcto."));

                adyenCloudServices.setTerminalApiEndpoint(terminalApiUrl);

                log.debug("cargaConfiguracion() - " + I18N.getTexto(
                        "Configuración del TEF de Adyen Cloud: " + "\r\n Cliente: " + environment + "\r\n POIID: " + pOIID + " - Protocolo: " + protocol + "\r\n Usuario: " + merchantAccount + " - Moneda: "
                                + currency + "\r\n Forma de Pago: " + payments + "\r\n Forma de pago 2:" + payments2 + "\r\n URL API: " + terminalApiUrl + "\r\n Merchant Application: " + "\r\n Nombre: " + merchantApplicationName + " - Versión: " + merchantApplicationVersion
                                + "\r\n Merchant Device: " + "\r\n Sistema: " + merchantDeviceSystem + " - Referencia: " + merchantDeviceReference + " - Versión: " + merchantDeviceVersion));
        }

        @Override
        protected void initializeClientAdyenCLoud() {
                super.initializeClientAdyenCLoud();
                if (StringUtils.isNotBlank(terminalApiUrl) && client != null && client.getConfig() != null) {
                        client.getConfig().setTerminalApiCloudEndpoint(terminalApiUrl);
                        log.info("initializeClientAdyenCLoud() - URL Request override : " + terminalApiUrl);
                }
        }
	
	@Override
	public boolean isCodMedPagoAceptado(String medPago) {
		if (medPago.equals(payments)) {
			return true;
		}else if (medPago.equals(payments2)) {
			return true;
		}
		
		return false;
	}
}
