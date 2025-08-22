package com.comerzzia.bricodepot.inStoreEngine.rest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;

import com.comerzzia.bricodepot.inStoreEngine.persistence.cajas.movimientos.CajasMovimientosBean;
import com.comerzzia.bricodepot.inStoreEngine.services.ServicioCajasMovimientos;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.core.util.db.Database;
import com.comerzzia.core.util.numeros.BigDecimalUtil;
import com.comerzzia.instoreengine.rest.ResponseRest;
import com.comerzzia.instoreengine.rest.ValidacionDatosExceptionRest;
import com.comerzzia.instoreengine.util.AppISE;
import com.comerzzia.model.ventas.cajas.cabecera.CabeceraCaja;
import com.comerzzia.model.ventas.cajas.cabecera.CabeceraCajaExample;
import com.comerzzia.model.ventas.cajas.cabecera.CabeceraCajaExample.Criteria;
import com.comerzzia.model.ventas.cajas.conceptos.ConceptoMovimientoCajaBean;
import com.comerzzia.model.ventas.cajas.detalle.DetalleCaja;
import com.comerzzia.model.ventas.cajas.detalle.DetalleCajaExample;
import com.comerzzia.servicios.rest.response.ResponseMensajesRest;
import com.comerzzia.servicios.ventas.cajas.cabecera.ServicioCabeceraCajasImpl;
import com.comerzzia.servicios.ventas.cajas.conceptos.ServicioConceptosMovimientosCajaImpl;
import com.comerzzia.servicios.ventas.cajas.detalles.ServicioDetallesCajasImpl;

@Path("bricocajas")
public class BricodepotServicioRestCajas {

	private static final Logger log = Logger.getLogger(BricodepotServicioRestCajas.class);

	@SuppressWarnings("deprecation")
	@PUT
	@Path("/insertarApunte")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response insertarApunteCaja(@QueryParam("apiKey") String apiKey,
			@QueryParam("uidActividad") String uidActividad, @QueryParam("codcaja") String codcaja,
			@QueryParam("codConcepto") String codConcepto, @QueryParam("importe") String importe,
			@QueryParam("usuario") String usuario, @QueryParam("documento") String documento) {
		log.debug("insertarApunteCaja() - insertando apunte " + codcaja);
		log.debug("insertarApunteCaja() - Codigo caja  " + codcaja);
		log.debug("insertarApunteCaja() - Codigo concepto " + codConcepto);
		
		ResponseRest resultado = new ResponseRest();
		Response response = null;
		Connection conn = new Connection();
		try {
			BricodepotCajaRequestRest request = new BricodepotCajaRequestRest();
			request.setApiKey(apiKey);
			request.setUidActividad(uidActividad);
			request.setCodcaja(codcaja);
			request.setCodConcepto(codConcepto);
			request.setImporte(importe);
			request.setUsuario(usuario);
			request.setDocumento(documento);
			request.setIdTipoCaja(AppISE.getPosConfig().getTiendaCaja().getIdTipoCaja());

			request.validar();

			// modificacion del bean para prepararlo para la consulta
			// codigo para iniciar la session para las consultas
			DatosSesionBean datosSesion = new DatosSesionBean();
			datosSesion.setUidActividad(uidActividad);

			request.validarApiKey(datosSesion);

			conn.abrirConexion(Database.getConnection());

			DetalleCaja movimiento = new DetalleCaja();
			CabeceraCajaExample cabCaja = new CabeceraCajaExample();

			Criteria cri = cabCaja.createCriteria();
			cri.andUidActividadEqualTo(uidActividad).andCodcajaEqualTo(codcaja).andFechaCierreIsNull();

			List<CabeceraCaja> cabCajas = ServicioCabeceraCajasImpl.get().selectByExample(cabCaja);
			String uidDiarioCaja = cabCajas.get(0).getUidDiarioCaja();
			movimiento.setUidDiarioCaja(uidDiarioCaja);
			movimiento.setCodconceptoMov(codConcepto);
			movimiento.setDocumento(documento);
			movimiento.setUsuario(usuario);

			DetalleCajaExample detCaja = new DetalleCajaExample();
			com.comerzzia.model.ventas.cajas.detalle.DetalleCajaExample.Criteria criDet = detCaja.createCriteria();
			criDet.andUidActividadEqualTo(uidActividad).andUidDiarioCajaEqualTo(uidDiarioCaja);

			List<DetalleCaja> detCajas = ServicioDetallesCajasImpl.get().selectByExample(detCaja);
			Collections.sort(detCajas, (o1, o2) -> o1.getLinea().compareTo(o2.getLinea()));
			Collections.reverse(detCajas);
			if (!detCajas.isEmpty() && detCajas != null) {
				Integer linea = detCajas.get(0).getLinea();
				movimiento.setLinea(linea + 1);
			} else {
				movimiento.setLinea(1);
			}
			java.util.Date sqlDate = new java.util.Date();
			movimiento.setFecha(sqlDate);
			movimiento.setCodmedpag("0000");
			ConceptoMovimientoCajaBean concepto = ServicioConceptosMovimientosCajaImpl.get().consultar(codConcepto,
					datosSesion);
			
			movimiento.setConcepto(concepto.getDesConceptoMovimiento());
			
			BigDecimal importeDes = new BigDecimal(importe);
			log.debug("insertarApunteCaja() - IMPORTE  [" + importe + "]");
			log.debug("insertarApunteCaja() - IMPORTE DES  [" + importeDes + "]");
			
			if (BigDecimalUtil.isMayorACero(importeDes)) {
				movimiento.setCargo(importeDes);
				movimiento.setAbono(BigDecimal.ZERO);
			} else {
				movimiento.setAbono(importeDes.abs());
				movimiento.setCargo(BigDecimal.ZERO);
			}

			log.debug("insertarApunteCaja() - ABONO [" + movimiento.getAbono() + "]");
			log.debug("insertarApunteCaja() - CARGO [" + movimiento.getCargo() + "]");
			ServicioDetallesCajasImpl.get().crear(datosSesion, conn, movimiento);

			resultado.setCodError(ResponseMensajesRest.COD_EXITO);
		} catch (ValidacionDatosExceptionRest e) {
			log.error("insertarApunteCaja() - Error: " + e.getMessage(), e);
			resultado.setMensaje(e.getMessage());
			resultado.setCodError(e.getcodError());
		} catch (Exception e) {
			log.error("insertarApunteCaja() - Error creando el apunte de caja: " + e.getMessage(), e);
			resultado.setCodError(ResponseMensajesRest.COD_ERROR_GET);
			resultado.setMensaje(e.getMessage());
		} finally {
			conn.close();
		}

		if (resultado.isError()) {
			log.error("insertarApunteCaja() - error al insertar apunte de caja: " + resultado.getCodError()
					+ " mensajeError: " + resultado.getMensaje());
			log.error("/insertarApunteCaja PUT -SALIDA- " + resultado.getXMLError());
			response = Response.status(ResponseMensajesRest.STATUS_RESPONSE_ERROR).entity(resultado.getXMLError())
					.header("Content-Encoding", "UTF-8").build();
		} else {
			log.debug("insertarApunteCaja() - apunte insertado sin errores");
			response = Response.ok().header("Content-Encoding", "UTF-8").build();
		}
		return response;

	}
	
	@GET
	@Path("/consultarCajasMovimientos")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response consultarCajasMovimientos(@QueryParam("apiKey") String apiKey, @QueryParam("uidActividad") String uidActividad, @QueryParam("codcaja") String codcaja,
	        @QueryParam("codConcepto") String codConcepto) {
		log.debug("consultarCajasMovimientos() - consultando tabla de correspondencia de cajas/movimientos para la caja " + codcaja + " y el movimiento origen" + codConcepto);
		ResponseRest resultado = new ResponseRest();
		Response response = null;

		CajasMovimientosResponse cajasMovimientosResponse = null;
		List<CajasMovimientosBean> listaCajasMovimientos = null;

		try {
			BricodepotCajaRequestRest request = new BricodepotCajaRequestRest();
			request.setApiKey(apiKey);
			request.setUidActividad(uidActividad);
			request.setCodcaja(codcaja);
			request.setCodConcepto(codConcepto);
			request.validar();

			DatosSesionBean datosSesion = new DatosSesionBean();
			datosSesion.setUidActividad(uidActividad);
			request.validarApiKey(datosSesion);

			listaCajasMovimientos = ServicioCajasMovimientos.get().consultarCajasMovimientos(codcaja, codConcepto, uidActividad);
			cajasMovimientosResponse = new CajasMovimientosResponse();
			cajasMovimientosResponse.setListaCajasMovimientos(listaCajasMovimientos);
			resultado.setCodError(ResponseMensajesRest.COD_EXITO);
		}
		catch (ValidacionDatosExceptionRest e) {
			log.error("consultarCajasMovimientos() - Error: " + e.getMessage(), e);
			resultado.setMensaje(e.getMessage());
			resultado.setCodError(e.getcodError());
		}
		catch (Exception e) {
			log.error("consultarCajasMovimientos() - Error consultando la tabla de correspondencia de cajas/movimientos: " + e.getMessage(), e);
			resultado.setCodError(ResponseMensajesRest.COD_ERROR_GET);
			resultado.setMensaje(e.getMessage());
		}

		if (resultado.isError()) {
			log.error("consultarCajasMovimientos() - error al insertar apunte de caja: " + resultado.getCodError() + " mensajeError: " + resultado.getMensaje());
			log.error("/consultarCajasMovimientos PUT -SALIDA- " + resultado.getXMLError());
			response = Response.status(ResponseMensajesRest.STATUS_RESPONSE_ERROR).entity(resultado.getXMLError()).header("Content-Encoding", "UTF-8").build();
		}
		else {
			JAXBElement<CajasMovimientosResponse> jaxbelement = new JAXBElement<CajasMovimientosResponse>(new QName("cajasMovimientosResponse"), CajasMovimientosResponse.class,
			        cajasMovimientosResponse);
			log.debug("consultarCajasMovimientos() - consulta realizada sin errores");
			response = Response.ok(jaxbelement).header("Content-Encoding", "UTF-8").build();
		}
		return response;

	}
}
