package com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.adyen.service;

import java.math.BigInteger;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;

import com.adyen.Client;
import com.adyen.model.ApiError;
import com.adyen.model.applicationinfo.ApplicationInfo;
import com.adyen.model.modification.CancelRequest;
import com.adyen.model.modification.ModificationResult;
import com.adyen.model.nexo.DeviceType;
import com.adyen.model.nexo.DisplayOutput;
import com.adyen.model.nexo.DocumentQualifierType;
import com.adyen.model.nexo.InfoQualifyType;
import com.adyen.model.nexo.InputCommandType;
import com.adyen.model.nexo.InputData;
import com.adyen.model.nexo.InputRequest;
import com.adyen.model.nexo.Instalment;
import com.adyen.model.nexo.MessageCategoryType;
import com.adyen.model.nexo.MessageClassType;
import com.adyen.model.nexo.MessageHeader;
import com.adyen.model.nexo.MessageReference;
import com.adyen.model.nexo.MessageType;
import com.adyen.model.nexo.OriginalPOITransaction;
import com.adyen.model.nexo.OutputContent;
import com.adyen.model.nexo.OutputFormatType;
import com.adyen.model.nexo.OutputText;
import com.adyen.model.nexo.PaymentData;
import com.adyen.model.nexo.PaymentReceipt;
import com.adyen.model.nexo.PaymentRequest;
import com.adyen.model.nexo.PaymentTransaction;
import com.adyen.model.nexo.PaymentType;
import com.adyen.model.nexo.PredefinedContent;
import com.adyen.model.nexo.ReversalReasonType;
import com.adyen.model.nexo.ReversalRequest;
import com.adyen.model.nexo.SaleData;
import com.adyen.model.nexo.SaleToPOIRequest;
import com.adyen.model.nexo.SaleToPOIResponse;
import com.adyen.model.nexo.TransactionIdentification;
import com.adyen.model.nexo.TransactionStatusRequest;
import com.adyen.model.terminal.SaleToAcquirerData;
import com.adyen.model.terminal.TerminalAPIRequest;
import com.adyen.service.Modification;
import com.adyen.service.exception.ApiException;
import com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.adyen.AdyenDatosPeticionPagoTarjeta;
import com.comerzzia.pos.dispositivo.tarjeta.adyen.constans.AdyenConstans;
import com.comerzzia.pos.dispositivo.tarjeta.adyen.exception.AdyenException;
import com.comerzzia.pos.dispositivo.tarjeta.adyen.service.AdyenService;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.services.ticket.pagos.tarjeta.DatosPeticionPagoTarjeta;
import com.comerzzia.pos.services.ticket.pagos.tarjeta.DatosRespuestaPagoTarjeta;
import com.comerzzia.pos.util.i18n.I18N;
import com.google.gson.Gson;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class ByLAdyenService extends AdyenService {

	private Logger log = Logger.getLogger(ByLAdyenService.class);
	
	public static final String TENDER_OPTION_RECEIPT_HANDLER = "ReceiptHandler";
	
	@Override
	public TerminalAPIRequest generateMessage(DatosPeticionPagoTarjeta request, String pOIID, String currency, String messageType, String saleIdActual, String serviceIDActual, String protocol,
	        ApplicationInfo applicationInfo) throws AdyenException {

		TerminalAPIRequest requestMessage = new TerminalAPIRequest();
		SaleToPOIRequest message = new SaleToPOIRequest();

		String serviceId = request.getIdTransaccion();

		MessageHeader header = null;
		if (AdyenConstans.SALE.equals(messageType)) {
			header = generateHeaderMessage(((AdyenDatosPeticionPagoTarjeta) request).getIdDocumentoString(), serviceId, pOIID, AdyenConstans.SALE, protocol);
			generatePaymentRequest(request, currency, message, false, applicationInfo);
		}
		else if (AdyenConstans.RETURN.equals(messageType)) {
			log.debug("Devolucion adyen con datosPeticion idDocumentoOrigen: "+request.getIdDocumentoOrigen());
			if (request.getIdDocumentoOrigen() == null) {
				log.debug("IdcoumentoOrigen nulo, se genera cabecera con idDocumentoOrigen: "+((AdyenDatosPeticionPagoTarjeta) request).getIdDocumentoString());
				header = generateHeaderMessage(((AdyenDatosPeticionPagoTarjeta) request).getIdDocumentoString(), serviceId, pOIID, AdyenConstans.SALE, protocol);
				log.debug("cabecera con idDocumentoOrigen/idVenta: "+header.getSaleID()+", se procede a generar la peticion rest");
				generatePaymentRequest(request, currency, message, true, applicationInfo);
			}
			else {
				log.debug("IdcoumentoOrigen no nulo, se genera cabecera con idDocumentoOrigen: "+((AdyenDatosPeticionPagoTarjeta) request).getIdDocumentoString());
				header = generateHeaderMessage(((AdyenDatosPeticionPagoTarjeta) request).getIdDocumentoString(), serviceId, pOIID, AdyenConstans.RETURN, protocol);
				log.debug("cabecera con idDocumentoOrigen/idVenta: "+header.getSaleID()+", se procede a generar la peticion rest");
				generateReversalRequest(request, currency, message);
			}
		}
		message.setMessageHeader(header);

		requestMessage.setSaleToPOIRequest(message);
		log.debug("Mensaje request con idDocumentoOrigen/idVenta: "+message.getMessageHeader().getSaleID());
		return requestMessage;
	}
	
	public TerminalAPIRequest generateMessageFirma(String idTransaccion, String pOIID, String protocol) throws AdyenException {
		TerminalAPIRequest requestMessage = new TerminalAPIRequest();
		SaleToPOIRequest message = new SaleToPOIRequest();

		String serviceId = idTransaccion;

		MessageHeader header = null;
		header = generateHeaderMessageFirma(idTransaccion, serviceId, pOIID, protocol);
		
		DisplayOutput displayOutput = new DisplayOutput();
		displayOutput.setDevice(DeviceType.CUSTOMER_DISPLAY);
		displayOutput.setInfoQualify(InfoQualifyType.DISPLAY);
		OutputContent outputContent = new OutputContent();
		outputContent.setOutputFormat(OutputFormatType.TEXT);
		PredefinedContent predefinedContent = new PredefinedContent();
		predefinedContent.setReferenceID("GetSignature");
		outputContent.setPredefinedContent(predefinedContent);

		OutputText title = new OutputText();
		title.setText(I18N.getTexto("Por favor firme"));
		OutputText additionalText = new OutputText();
		additionalText.setText("");
		outputContent.getOutputText().add(title);
		outputContent.getOutputText().add(additionalText);
//		outputContent.getOutputText().add(declineAnswer);
//		outputContent.getOutputText().add(agreeAnswer);
		displayOutput.setOutputContent(outputContent);

		InputData inputData = new InputData();
		inputData.setDevice(DeviceType.CUSTOMER_INPUT);
		inputData.setInfoQualify(InfoQualifyType.INPUT);
		inputData.setInputCommand(InputCommandType.GET_CONFIRMATION);
		inputData.setMaxInputTime(BigInteger.valueOf(30));
		
		InputRequest inputRequest = new InputRequest();
		inputRequest.setDisplayOutput(displayOutput);
		inputRequest.setInputData(inputData);
		
		message.setMessageHeader(header);
		message.setInputRequest(inputRequest);		
		requestMessage.setSaleToPOIRequest(message);
		
		return requestMessage;
	}
	
	public TerminalAPIRequest generateMessageConsentimiento(String idTransaccion, String pOIID, String protocol, String textoConsentimiento) throws AdyenException {
		TerminalAPIRequest requestMessage = new TerminalAPIRequest();
		SaleToPOIRequest message = new SaleToPOIRequest();

		String serviceId = idTransaccion;

		MessageHeader header = null;
		header = generateHeaderMessageFirma(idTransaccion, serviceId, pOIID, protocol);
		
		DisplayOutput displayOutput = new DisplayOutput();
		displayOutput.setDevice(DeviceType.CUSTOMER_DISPLAY);
		displayOutput.setInfoQualify(InfoQualifyType.DISPLAY);
		OutputContent outputContent = new OutputContent();
		outputContent.setOutputFormat(OutputFormatType.TEXT);
		PredefinedContent predefinedContent = new PredefinedContent();
		predefinedContent.setReferenceID("GetConfirmation");
		outputContent.setPredefinedContent(predefinedContent);

		OutputText title = new OutputText();
		title.setText(I18N.getTexto("Términos y Condiciones"));
		OutputText additionalText = new OutputText();
		additionalText.setText(textoConsentimiento);
		OutputText declineAnswer = new OutputText();
		declineAnswer.setText(I18N.getTexto("Denegar"));
		OutputText agreeAnswer = new OutputText();
		agreeAnswer.setText(I18N.getTexto("Aceptar"));
		outputContent.getOutputText().add(title);
		outputContent.getOutputText().add(additionalText);
		outputContent.getOutputText().add(declineAnswer);
		outputContent.getOutputText().add(agreeAnswer);
		displayOutput.setOutputContent(outputContent);

		InputData inputData = new InputData();
		inputData.setDevice(DeviceType.CUSTOMER_INPUT);
		inputData.setInfoQualify(InfoQualifyType.INPUT);
		inputData.setInputCommand(InputCommandType.GET_CONFIRMATION);
		inputData.setMaxInputTime(BigInteger.valueOf(30));
		
		InputRequest inputRequest = new InputRequest();
		inputRequest.setDisplayOutput(displayOutput);
		inputRequest.setInputData(inputData);
		
		message.setMessageHeader(header);
		message.setInputRequest(inputRequest);		
		requestMessage.setSaleToPOIRequest(message);
		
		return requestMessage;
	}

	public MessageHeader generateHeaderMessageFirma(String saleId, String serviceId, String POIID, String protocol) {
		MessageHeader header = new MessageHeader();
		header.setPOIID(POIID);
		header.setSaleID(saleId);
		header.setServiceID(serviceId);
		header.setMessageType(MessageType.REQUEST);
		header.setMessageClass(MessageClassType.DEVICE);
		header.setProtocolVersion(protocol);
		header.setMessageCategory(MessageCategoryType.INPUT);
		return header;
	}

	@Override
	public PaymentRequest generateSaleDetails(String transactionId, ApplicationInfo applicationInfo, Boolean isNotReference) throws AdyenException {
		PaymentRequest paymentRequest = super.generateSaleDetails(transactionId, applicationInfo, isNotReference);
		
		// Se añade esta propiedad para que no imprima por el terminal de adyen
		paymentRequest.getSaleData().getSaleToAcquirerData().setTenderOption(TENDER_OPTION_RECEIPT_HANDLER);

		return paymentRequest;
	}

	private PaymentData crearPaymentData(DatosPeticionPagoTarjeta request) {
		log.debug("crearPaymentData()");

		PaymentData paymentData = new PaymentData();
		Instalment instalment = new Instalment();

		instalment.setTotalNbOfPayments(new BigInteger(((AdyenDatosPeticionPagoTarjeta) request).getTotalMeses().toString()));

		log.debug("crearPaymentData() - Seteando PaymentType e Instalment al paymentData");

		paymentData.setPaymentType(PaymentType.INSTALMENT);
		paymentData.setInstalment(instalment);
		return paymentData;
	}

	@Override
	public ReversalRequest generateReturnDetails(String currency, String idDocument, String dateDocument, DatosPeticionPagoTarjeta request) throws AdyenException {
		try {
			ReversalRequest returnMessage = new ReversalRequest();
			OriginalPOITransaction originalPOITransaction = new OriginalPOITransaction();
			TransactionIdentification pOITransactionID = new TransactionIdentification();

			pOITransactionID.setTransactionID(idDocument);
			pOITransactionID.setTimeStamp(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
			originalPOITransaction.setPOITransactionID(pOITransactionID);

			originalPOITransaction.setPOIID(((AdyenDatosPeticionPagoTarjeta) request).getTerminalOrigen());

			returnMessage.setOriginalPOITransaction(originalPOITransaction);
			returnMessage.setReversalReason(ReversalReasonType.MERCHANT_CANCEL);
			returnMessage.setReversedAmount(request.getImporte().abs());

			TransactionIdentification saleTransactionID = new TransactionIdentification();
			saleTransactionID.setTransactionID(((AdyenDatosPeticionPagoTarjeta) request).getIdDocumentoString());
			saleTransactionID.setTimeStamp(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));

			SaleData saleData = new SaleData();
			SaleToAcquirerData saleToAcquirerData = new SaleToAcquirerData();
			saleToAcquirerData.setCurrency(currency);
			saleData.setSaleToAcquirerData(saleToAcquirerData);
			saleData.setSaleTransactionID(saleTransactionID);
			returnMessage.setSaleData(saleData);

			// Se añade esta propiedad para que no imprima por el terminal de adyen
			returnMessage.getSaleData().getSaleToAcquirerData().setTenderOption(TENDER_OPTION_RECEIPT_HANDLER);

			return returnMessage;
		}
		catch (Exception e) {
			String errorMessage = I18N.getTexto("Error al generar los detalles de la petición de devolución para Adyen.");
			log.error("generateReturnDetails() - " + errorMessage + " - " + e.getMessage(), e);
			throw new AdyenException(errorMessage, e);
		}
	}
	
	@Override
	public DatosRespuestaPagoTarjeta getResponseSale(DatosRespuestaPagoTarjeta response, SaleToPOIResponse saleToPoiResponse, Sesion sesion) {

		DatosRespuestaPagoTarjeta datosRespuesta = super.getResponseSale(response, saleToPoiResponse, sesion);
        datosRespuesta.setApplicationLabel(datosRespuesta.getAID());
		List<PaymentReceipt> paymentReceiptList = saleToPoiResponse.getPaymentResponse().getPaymentReceipt();

		if (!paymentReceiptList.isEmpty()) {
			datosRespuesta.setPedirFirma(paymentReceiptList.get(0).isRequiredSignatureFlag());
		}

		return datosRespuesta;
	}
	

    @Override
    public DatosRespuestaPagoTarjeta getResponseReturn(DatosRespuestaPagoTarjeta response, SaleToPOIResponse saleToPoiResponse, Sesion sesion) {

            DatosRespuestaPagoTarjeta datosRespuesta = super.getResponseReturn(response, saleToPoiResponse, sesion);
            datosRespuesta.setApplicationLabel(datosRespuesta.getAID());
            return datosRespuesta;
    }
	
	@Override
	public void generatePaymentRequest(DatosPeticionPagoTarjeta request, String currency, SaleToPOIRequest message, Boolean isNotReference, ApplicationInfo applicationInfo)
	        throws AdyenException {
		PaymentRequest details = generateSaleDetails(((AdyenDatosPeticionPagoTarjeta) request).getIdDocumentoString(), applicationInfo, isNotReference);// applicationInfo);
		if (((AdyenDatosPeticionPagoTarjeta) request).getTotalMeses() != null) {
			details.setPaymentData(crearPaymentData(request));
		}
		PaymentTransaction payments = generateSalesPayments(currency, request.getImporte(), isNotReference);
		details.setPaymentTransaction(payments);
		message.setPaymentRequest(details);
	}
	
	public TerminalAPIRequest generateMessageStatus(TerminalAPIRequest request, String serviceID) {

		TerminalAPIRequest terminalAPIRequestStatus = new TerminalAPIRequest();
		SaleToPOIRequest saleToPOIRequestStatus = new SaleToPOIRequest();
		MessageHeader messageHeaderStatus = new MessageHeader();
		MessageReference messageReferenceStatus = new MessageReference();
		TransactionStatusRequest transactionStatusRequest = new TransactionStatusRequest();
		
		//MESSAGE HEADER
		messageHeaderStatus.setProtocolVersion(request.getSaleToPOIRequest().getMessageHeader().getProtocolVersion());
		messageHeaderStatus.setMessageClass(MessageClassType.SERVICE);
		messageHeaderStatus.setMessageCategory(MessageCategoryType.TRANSACTION_STATUS);
		messageHeaderStatus.setMessageType(MessageType.REQUEST);
		messageHeaderStatus.setSaleID(request.getSaleToPOIRequest().getMessageHeader().getSaleID());
		messageHeaderStatus.setServiceID(serviceID);
		messageHeaderStatus.setPOIID(request.getSaleToPOIRequest().getMessageHeader().getPOIID());
		saleToPOIRequestStatus.setMessageHeader(messageHeaderStatus);
		
		//TRANSACTION STATUS REQUEST
		transactionStatusRequest.setReceiptReprintFlag(true);
		transactionStatusRequest.getDocumentQualifier().add(DocumentQualifierType.CASHIER_RECEIPT);
		transactionStatusRequest.getDocumentQualifier().add(DocumentQualifierType.CUSTOMER_RECEIPT);
		messageReferenceStatus.setSaleID(request.getSaleToPOIRequest().getMessageHeader().getSaleID());
		messageReferenceStatus.setServiceID(request.getSaleToPOIRequest().getMessageHeader().getServiceID());
		messageReferenceStatus.setMessageCategory(MessageCategoryType.PAYMENT);
		transactionStatusRequest.setMessageReference(messageReferenceStatus);
		saleToPOIRequestStatus.setTransactionStatusRequest(transactionStatusRequest);
		
		terminalAPIRequestStatus.setSaleToPOIRequest(saleToPOIRequestStatus);
		return terminalAPIRequestStatus;
	}
	
	// Incidencia 106269
	@Override
	public String cancel(Client client, String merchantAccount, String originalReference) {
		String errorMessage = "";
		Modification modification = new Modification(client);
		CancelRequest cancelRequest = new CancelRequest();
		if (originalReference.contains(".")) {
			String originalReferenceDiv[] = originalReference.split("\\.");
			originalReference = originalReferenceDiv[1];
		}
		cancelRequest.setOriginalReference(originalReference);
		cancelRequest.setMerchantAccount(merchantAccount);

		ApiError error = null;
		ModificationResult modificationResponse = null;
		try {
			
			// init Incidencia 106269
			Gson gson = new Gson();
			String cancelRequestJson = gson.toJson(cancelRequest);
			log.info("cancel() - Enviando petición de ANULACION: " + cancelRequestJson);
			// fin Incidencia 106269
			
			modificationResponse = modification.cancel(cancelRequest);
			log.info("cancel() - Respuesta ANULACIÓN: " + modificationResponse.toString());
		}
		catch (Exception e) {
			if (e instanceof ApiException) {
				error = ((ApiException) e).getError();
				if (error != null) {
					errorMessage = I18N.getTexto("Error en la operación de ANULACIÓN: ") + error.getMessage();
					log.error("cancel() - " + errorMessage);
					/* This error only occurs when the operation has not been carried out, which is why we do not show it on the screen */
					if (error.getErrorCode().equals("167")) {
						errorMessage = "";
					}
				}
				else {
					log.info("cancel() - " + I18N.getTexto("Completada la operación de ANULACIÓN correctamente"));
				}
			}
			else {
				errorMessage = I18N.getTexto("Error al realizar la operación de ANULACIÓN: ") + e.getMessage();
				log.error("cancel() - " + errorMessage);
			}
		}
		return errorMessage;
	}
	
}
