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

        private static String safeReadEntity(Response r) {
                try {
                        if (r != null && r.hasEntity()) {
                                String s = r.readEntity(String.class);
                                return (s != null && s.length() > 2000) ? s.substring(0, 2000) + "...(truncated)" : s;
                        }
                }
                catch (Exception ignore) {
                }
                return null;
        }

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

                        try (Response response = target.request().put(Entity.entity("", MediaType.APPLICATION_XML))) {
                                int status = response.getStatus();
                                log.info("insertarApunte() - HTTP status: " + status);
                                if (status >= 300) {
                                        String reason = (response.getStatusInfo() != null) ? response.getStatusInfo().getReasonPhrase() : "";
                                        String body = safeReadEntity(response);
                                        throw new RestException(status + ": " + reason + (body != null ? " - " + body : ""), null);
                                }
                        }
                }
                catch (BadRequestException e) {
                        throw RestHttpException.establecerException(e);
                }
                catch (WebApplicationException e) {
                        int st = (e.getResponse() != null) ? e.getResponse().getStatus() : 500;
                        throw new RestHttpException(st,
                                "Se ha producido un error HTTP " + st + ". Causa: " + e.getClass().getName() + " - " + e.getLocalizedMessage(), e);
                }
                catch (ProcessingException e) {
                        Throwable c = e.getCause();
                        if (c instanceof ConnectException) {
                                throw new RestConnectException("Se ha producido un error al conectar con el servidor", e);
                        }
                        else if (c instanceof SocketTimeoutException) {
                                throw new RestTimeoutException("Se ha producido timeout al conectar con el servidor", e);
                        }
                        throw new RestException("Se ha producido un error realizando la petición. Causa: " + (c != null ? c.getClass().getName() : e.getClass().getName()) + " - " + e.getLocalizedMessage(), e);
                }
                catch (Exception e) {
                        Throwable c = e.getCause();
                        throw new RestException("Se ha producido un error realizando la petición. Causa: " + (c != null ? c.getClass().getName() : e.getClass().getName()) + " - " + e.getLocalizedMessage(), e);
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
                        try (Response r = target.request().get()) {
                                int status = r.getStatus();
                                log.info("consultarCajasMovimientos() - HTTP status: " + status);
                                if (status >= 300) {
                                        String reason = (r.getStatusInfo() != null) ? r.getStatusInfo().getReasonPhrase() : "";
                                        String body = safeReadEntity(r);
                                        log.error("consultarCajasMovimientos() - Error HTTP " + status + ": " + reason + (body != null ? " - " + body : ""));
                                        throw new WebApplicationException("HTTP " + status + ": " + reason, status);
                                }
                                response = r.readEntity(genericType).getValue();
                        }

                        listaCajasMovimientos = response.getListaCajasMovimientos();
                }
                catch (BadRequestException e) {
                        throw RestHttpException.establecerException(e);
                }
                catch (WebApplicationException e) {
                        int st = (e.getResponse() != null) ? e.getResponse().getStatus() : 500;
                        throw new RestHttpException(st,
                                "Se ha producido un error HTTP " + st + ". Causa: " + e.getClass().getName() + " - " + e.getLocalizedMessage(), e);
                }
                catch (ProcessingException e) {
                        Throwable c = e.getCause();
                        if (c instanceof ConnectException) {
                                throw new RestConnectException("Se ha producido un error al conectar con el servidor", e);
                        }
                        else if (c instanceof SocketTimeoutException) {
                                throw new RestTimeoutException("Se ha producido timeout al conectar con el servidor", e);
                        }
                        throw new RestException("Se ha producido un error realizando la petición. Causa: " + (c != null ? c.getClass().getName() : e.getClass().getName()) + " - " + e.getLocalizedMessage(), e);
                }
                catch (Exception e) {
                        Throwable c = e.getCause();
                        throw new RestException("Se ha producido un error realizando la petición. Causa: " + (c != null ? c.getClass().getName() : e.getClass().getName()) + " - " + e.getLocalizedMessage(), e);
                }

		return listaCajasMovimientos;
	}
}
