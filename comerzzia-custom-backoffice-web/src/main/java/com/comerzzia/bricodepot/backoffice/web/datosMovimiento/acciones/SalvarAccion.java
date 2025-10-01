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
package com.comerzzia.bricodepot.backoffice.web.datosMovimiento.acciones;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;

import com.comerzzia.bricodepot.backoffice.persistence.datosMovimiento.DatosMovimientoBean;
import com.comerzzia.bricodepot.backoffice.services.datosMovimiento.DatosMovimientoServiceImpl;
import com.comerzzia.bricodepot.backoffice.services.ventas.informetto.ServicioVentasInformeTto;
import com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.core.util.db.Database;
import com.comerzzia.core.util.numeros.Numero;
import com.comerzzia.model.general.tiendas.cajas.TiendaCajaBean;
import com.comerzzia.model.ventas.cajas.cabecera.CabeceraCaja;
import com.comerzzia.model.ventas.cajas.detalle.DetalleCaja;
import com.comerzzia.servicios.general.tiendas.cajas.ServicioCajasTiendasImpl;
import com.comerzzia.servicios.ventas.cajas.cabecera.ServicioCabeceraCajasImpl;
import com.comerzzia.servicios.ventas.cajas.detalles.ServicioDetallesCajasImpl;
import com.comerzzia.web.base.Accion;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;

public class SalvarAccion extends Accion {
	
	protected static Logger log = Logger.getLogger(SalvarAccion.class);

	
	private static final Vista NEXT_PAGE = new Vista("backoffice/datosMovimiento/mantenimiento/jsp/datosMovimiento.jsp", Vista.INTERNA);

	@Autowired
	protected DatosMovimientoServiceImpl datosMovimientoServiceImpl;
	
	@Override
    public String getNombre() {
        return "salvar";
    }
    
    
	@Override
    @SuppressWarnings("deprecation")
	public Vista ejecutar(HttpServletRequest request) {
    	HttpSession sesion = request.getSession();
		DatosSesionBean datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);
    	Connection conn = new Connection();
		try {
			conn.abrirConexion(Database.getConnection());
			
    		// Comprobamos los permisos de la acción
    		PermisosEfectivosAccionBean permisos = datosSesion.getPermisosCache(this.getAccionMenu());
    		if (!permisos.isPuedeAñadir()) {
    			return SIN_PERMISO;
    		}
    		
    		// Comprobamos que ambos importes introducidos son el mismo
    		Double importe = Numero.desformatea(request.getParameter("importe"));
    		Double importeRepetido = Numero.desformatea(request.getParameter("importeRepetido"));
    		if(0 != new BigDecimal(importe).compareTo(new BigDecimal(importeRepetido))) {
    			setMensajeError(request, datosSesion.getTranslation().getText("Los importes introducidos no coinciden"));
    			return NEXT_PAGE;
    		}
    		
    		DatosMovimientoBean datosMovimientoBean = crearDatosMovimientoBean(request, sesion, importe);
    		
    		String uidDiarioCajaGenerado = UUID.randomUUID().toString();
    		DetalleCaja detalleCaja = crearDetalleCaja(datosSesion, datosMovimientoBean, uidDiarioCajaGenerado);
    		
    		// Consultamos si existe un movimiento en nuestro almacen con código 92 del día que hemos puesto
    		String uidDiarioCaja = datosMovimientoServiceImpl.consultarUidDiarioCaja(datosMovimientoBean.getCodAlm(), datosMovimientoBean.getFecha());
			if (uidDiarioCaja == null) {
				// Si no existe en BBDD ningún registro anterior los creamos para insertarlos
	    		CabeceraCaja cabeceraCaja = crearCabeceraCaja(datosSesion, datosMovimientoBean, uidDiarioCajaGenerado);
	    		
	    		// Insertamos la cabecera y el detalle
				ServicioCabeceraCajasImpl.get().crear(datosSesion, conn, cabeceraCaja);
				ServicioDetallesCajasImpl.get().crear(datosSesion, conn, detalleCaja);
				setMensaje(request, datosSesion.getTranslation().getText("Se han salvado " +  new BigDecimal(datosMovimientoBean.getImporte()) +  "€ como importe para el almacén " + datosMovimientoBean.getCodAlm()));
			}else {
				// Si ya existe algún registro en BBDD, únicamente actualizamos el importe y los comentarios
				detalleCaja.setUidDiarioCaja(uidDiarioCaja);
				ServicioDetallesCajasImpl.get().modificar(datosSesion, detalleCaja);
				setMensaje(request, datosSesion.getTranslation().getText("Se ha modificado a " +  new BigDecimal(datosMovimientoBean.getImporte()) +  "€ el importe para el almacén " + datosMovimientoBean.getCodAlm()));
			}
    		
			String codAlm = datosMovimientoBean.getCodAlm();
			String uidActividad= datosSesion.getUidActividad();
			Date fechaMovimiento = datosMovimientoBean.getFecha();
			insertInformeTtoVenta(codAlm, uidActividad, fechaMovimiento, datosSesion, conn);
			conn.getSqlSession().commit();
			// Liberamos recursos
    		sesion.removeAttribute("datosMovimiento");
    		
			return getControlador().getAccion("ejecutar").ejecutar(request);
    	}
    	catch (Exception e) {
    		conn.getSqlSession().rollback();
    		Log.error("ejecutar() - Ha ocurrido un error al intentar salvar el dato movimiento 92 : " + e.getMessage());
            setError(request, e);
            return ERROR_PAGE;
        }finally{
        	if(conn != null) {
        		conn.getSqlSession().close();
        		conn.close();
        	}
        }
    }


	private CabeceraCaja crearCabeceraCaja(DatosSesionBean datosSesion, DatosMovimientoBean datosMovimientoBean, String uidDiarioCajaGenerado) {
		Log.debug("crearCabeceraCaja() - Creando cabecera de la caja con uidDiarioCaja '" + uidDiarioCajaGenerado + "' para el movimiento de dato 92");
		// Rellenamos los datos de la cabecera de la caja
		CabeceraCaja cabeceraCaja = new CabeceraCaja();
		cabeceraCaja.setUidActividad(datosSesion.getUidActividad());
		cabeceraCaja.setUidDiarioCaja(uidDiarioCajaGenerado);
		cabeceraCaja.setCodalm(datosMovimientoBean.getCodAlm());
		cabeceraCaja.setCodcaja("90");
		cabeceraCaja.setFechaApertura(datosMovimientoBean.getFecha());
		cabeceraCaja.setFechaCierre(datosMovimientoBean.getFecha());
		cabeceraCaja.setFechaEnvio(datosMovimientoBean.getFecha());
		cabeceraCaja.setUsuario(datosSesion.getUsuario().getDesUsuario());
		cabeceraCaja.setUsuarioCierre(datosSesion.getUsuario().getDesUsuario());
		cabeceraCaja.setFechaContable(datosMovimientoBean.getFecha());
		
		Log.debug("crearDetalleCaja() - Cabecera de caja con uidDiarioCaja '" + uidDiarioCajaGenerado + "' creado correctamente");
		return cabeceraCaja;
	}

	private DetalleCaja crearDetalleCaja(DatosSesionBean datosSesion, DatosMovimientoBean datosMovimientoBean, String uidDiarioCajaGenerado) {
		Log.debug("crearDetalleCaja() - Creando detalle de la caja con uidDiarioCaja '" + uidDiarioCajaGenerado + "' para el movimiento de dato 92");
		// Rellenamos los datos del detalle de la caja
		DetalleCaja detalleCaja = new DetalleCaja();
		detalleCaja.setUidActividad(datosSesion.getUidActividad());
		detalleCaja.setUidDiarioCaja(uidDiarioCajaGenerado);
		detalleCaja.setLinea(1);
		detalleCaja.setFecha(datosMovimientoBean.getFecha());
		detalleCaja.setCargo(new BigDecimal(datosMovimientoBean.getImporte()));
		detalleCaja.setAbono(BigDecimal.ZERO);
		detalleCaja.setConcepto("IMPORTE INGRESADO EN BANCO");
		detalleCaja.setDocumento(datosMovimientoBean.getComentarios());
		detalleCaja.setCodmedpag("0000");
		detalleCaja.setCodconceptoMov("92");
		detalleCaja.setUsuario(datosSesion.getUsuario().getDesUsuario());
		
		Log.debug("crearDetalleCaja() - Detalle de caja con uidDiarioCaja '" + uidDiarioCajaGenerado + "' creado correctamente");
		return detalleCaja;
	}

	@SuppressWarnings("deprecation")
	private DatosMovimientoBean crearDatosMovimientoBean(HttpServletRequest request, HttpSession sesion, Double importe) throws ParseException {
		Log.debug("crearDatosMovimientoBean() - Creando DatoMovimientoBean con los datos recogidos del formulario para el almacén " + request.getParameter("codAlm"));
		// Obtenemos todos los datos del formulario de la pantalla
		DatosMovimientoBean datosMovimientoBean = (DatosMovimientoBean) sesion.getAttribute("datosMovimiento");
		datosMovimientoBean.setCodAlm(request.getParameter("codAlm"));
		datosMovimientoBean.setComentarios(request.getParameter("comentarios"));

		// Formateamos la fecha
		SimpleDateFormat formatEntrada = new SimpleDateFormat("dd/MM/yyyy");
		Date fechaEntrada = formatEntrada.parse(request.getParameter("fecha"));
		SimpleDateFormat formatSalida = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date fecha = new Date(formatSalida.format(fechaEntrada));
		datosMovimientoBean.setFecha(fecha);

		datosMovimientoBean.setImporte(importe.toString());

		// Como ya hemos comprobado que el importe e importeRepetido son el mismo,
		// rellenamos ambos atributos con el mismo valor
		datosMovimientoBean.setImporteRepetido(importe.toString());
		
		Log.debug("crearDatosMovimientoBean() - DatoMovimientoBean creado satisfactoriamente");
		return datosMovimientoBean;
	}
	
	private void crearInformeTtoVentasDia(String codalm, String codcaja, String uidActividad, Date fecha, Connection conn) {
		log.debug("crearInformeTtoVentasDia() - Creando informe Tto Ventas del dia");
		try {
			LocalDate localDate = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String formattedDate = localDate.format(formatter);

			// Actualizamos el informe que se generó en la apertura de tienda
			ServicioVentasInformeTto.get().actualizarInformeTtoVenta(uidActividad, codalm, codcaja, formattedDate, conn);

		}
		catch (Exception e) {
			log.error("crearInformeTtoVentasDia() - Error creando informe Tto Ventas del dia");
		}
	}

	private void insertInformeTtoVenta(String codalm, String uidActividad, Date fecha, DatosSesionBean datosSesion, Connection conn) {
		try {
			List<TiendaCajaBean> cajasAlmacen = ServicioCajasTiendasImpl.get().consultar(codalm, datosSesion);

			for (TiendaCajaBean caja : cajasAlmacen) {
				crearInformeTtoVentasDia(caja.getCodAlm(), caja.getCodCaja(), uidActividad, fecha, conn);
			}
		}
		catch (Exception e) {
			log.error("insertInformeTtoVenta() - Eror al insertar el informe Tto venta: " + e.getMessage(), e);
		}
	}
}
