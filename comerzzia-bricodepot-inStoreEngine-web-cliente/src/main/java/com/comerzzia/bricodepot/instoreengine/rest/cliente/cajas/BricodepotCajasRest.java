package com.comerzzia.bricodepot.instoreengine.rest.cliente.cajas;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;

import org.apache.log4j.Logger;

import com.comerzzia.api.rest.ClientBuilder;
import com.comerzzia.api.rest.client.exceptions.RestConnectException;
import com.comerzzia.api.rest.client.exceptions.RestException;
import com.comerzzia.api.rest.client.exceptions.RestHttpException;
import com.comerzzia.api.rest.client.exceptions.RestTimeoutException;
import com.comerzzia.bricodepot.instoreengine.rest.cliente.cajas.movimientos.CajasMovimientosDTO;
import com.comerzzia.bricodepot.instoreengine.rest.cliente.cajas.movimientos.CajasMovimientosResponse;
import com.comerzzia.bricodepot.instoreengine.rest.path.BricodepotInStoreEngineWebservicesPath;

public class BricodepotCajasRest {

	private static Logger log = Logger.getLogger(BricodepotCajasRest.class);

	public static void insertarApunte(ApunteRequestRest apunteRequest) throws RestHttpException, RestException {
		log.info("insertarApunte() - Insertando apunte");
		log.info("insertarApunte() - CodCaja: " + apunteRequest.getCodcaja() + " y del usuario " + apunteRequest.getUsuario());
		log.info("insertarApunte() - Usuario: " + apunteRequest.getUsuario());
		log.info("insertarApunte() - CodConcepto: " + apunteRequest.getCodConcepto());
		log.info("insertarApunte() - Importe: " + apunteRequest.getImporte());
		log.info("insertarApunte() - Documento: " + apunteRequest.getDocumento());

		try {
			WebTarget target = ClientBuilder.getClient().target(BricodepotInStoreEngineWebservicesPath.servicio).path(BricodepotInStoreEngineWebservicesPath.servicioInsertarApunte);

			target = target.queryParam("apiKey", apunteRequest.getApiKey())
					.queryParam("uidActividad", apunteRequest.getUidActividad())
					.queryParam("codcaja", apunteRequest.getCodcaja())
					.queryParam("codConcepto", apunteRequest.getCodConcepto())
					.queryParam("importe", apunteRequest.getImporte()).queryParam("usuario", apunteRequest.getUsuario())
					.queryParam("documento", apunteRequest.getDocumento());

			log.info("insertarApunte() - URL de servicio rest en la que se realiza la petición: " + target.getUri());

			Response response = target.request().put(Entity.entity(new String(), MediaType.APPLICATION_XML));

			if (!(response.getStatus() < 300)) {
				throw new RestException(response.getStatusInfo().getStatusCode() + ": " + response.getStatusInfo().getReasonPhrase(), new Exception());
			}
		}
		catch (BadRequestException e) {
			throw RestHttpException.establecerException(e);
		}
		catch (WebApplicationException e) {
			throw new RestHttpException(e.getResponse().getStatus(),
			        "Se ha producido un error HTTP " + e.getResponse().getStatus() + ". Causa: " + e.getClass().getName() + " - " + e.getLocalizedMessage(), e);
		}
		catch (ProcessingException e) {
			if (e.getCause() instanceof ConnectException) {
				throw new RestConnectException("Se ha producido un error al conectar con el servidor", e);
			}
			else if (e.getCause() instanceof SocketTimeoutException) {
				throw new RestTimeoutException("Se ha producido timeout al conectar con el servidor", e);
			}
			throw new RestException("Se ha producido un error realizando la petición. Causa: " + e.getCause().getClass().getName() + " - " + e.getLocalizedMessage(), e);
		}
		catch (Exception e) {
			throw new RestException("Se ha producido un error realizando la petición. Causa: " + e.getCause().getClass().getName() + " - " + e.getLocalizedMessage(), e);
		}

	}

	public static List<CajasMovimientosDTO> consultarCajasMovimientos(ApunteRequestRest apunteRequest) throws RestHttpException, RestException {
		log.info("consultarCajasMovimientos() - consultando la tabla de correspondencia de caja/movimiento para la caja " + apunteRequest.getCodcaja() + " y el movimiento "
		        + apunteRequest.getCodConcepto());
		GenericType<JAXBElement<CajasMovimientosResponse>> genericType = new GenericType<JAXBElement<CajasMovimientosResponse>>(){
		};

		List<CajasMovimientosDTO> listaCajasMovimientos = new ArrayList<>();
		
		CajasMovimientosResponse response;
		try {
			WebTarget target = ClientBuilder.getClient().target(BricodepotInStoreEngineWebservicesPath.servicio).path(BricodepotInStoreEngineWebservicesPath.servicioConsultarCajasMovimientos);

			target = target.queryParam("apiKey", apunteRequest.getApiKey())
					.queryParam("uidActividad", apunteRequest.getUidActividad())
					.queryParam("codcaja", apunteRequest.getCodcaja())
					.queryParam("codConcepto", apunteRequest.getCodConcepto());

			log.info("consultarCajasMovimientos() - URL de servicio rest en la que se realiza la petición: " + target.getUri());
			response = target.request().get(genericType).getValue();
			
			listaCajasMovimientos = response.getListaCajasMovimientos();
		}
		catch (BadRequestException e) {
			throw RestHttpException.establecerException(e);
		}
		catch (WebApplicationException e) {
			throw new RestHttpException(e.getResponse().getStatus(),
			        "Se ha producido un error HTTP " + e.getResponse().getStatus() + ". Causa: " + e.getClass().getName() + " - " + e.getLocalizedMessage(), e);
		}
		catch (ProcessingException e) {
			if (e.getCause() instanceof ConnectException) {
				throw new RestConnectException("Se ha producido un error al conectar con el servidor", e);
			}
			else if (e.getCause() instanceof SocketTimeoutException) {
				throw new RestTimeoutException("Se ha producido timeout al conectar con el servidor", e);
			}
			throw new RestException("Se ha producido un error realizando la petición. Causa: " + e.getCause().getClass().getName() + " - " + e.getLocalizedMessage(), e);
		}
		catch (Exception e) {
			throw new RestException("Se ha producido un error realizando la petición. Causa: " + e.getCause().getClass().getName() + " - " + e.getLocalizedMessage(), e);
		}

		return listaCajasMovimientos;
	}
}
