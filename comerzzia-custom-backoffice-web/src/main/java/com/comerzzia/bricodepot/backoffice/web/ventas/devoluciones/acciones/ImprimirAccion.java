package com.comerzzia.bricodepot.backoffice.web.ventas.devoluciones.acciones;
 
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.jfree.util.Log;

import com.comerzzia.bricodepot.backoffice.persistence.devoluciones.validacion.ParametrosBuscarValidacionesDevolucionAlbaran;
import com.comerzzia.bricodepot.backoffice.web.ventas.devoluciones.DevolucionesServlet;
import com.comerzzia.core.model.informes.TrabajoInformeBean;
import com.comerzzia.core.servicios.acciones.operaciones.OperacionException;
import com.comerzzia.core.servicios.informes.InformeConstraintViolationException;
import com.comerzzia.core.servicios.informes.InformeException;
import com.comerzzia.core.servicios.informes.InformeNotFoundException;
import com.comerzzia.core.servicios.permisos.PermisoException;
import com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.web.base.InformeAccion;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;

import javazoom.upload.MultipartFormDataRequest;

public class ImprimirAccion extends InformeAccion {

	@Override
	public String getNombreInforme() {
		return "devoluciones.lstValidacion";
	}

	public Vista vistaInforme(HttpServletRequest request) {
		return new Vista("backoffice/devoluciones/buscar/jsp/imprimir.jsp", Vista.INTERNA);
	}

	// BRICO-307 GAP-78
	@Override
	public void imprimirInforme(HttpServletRequest request, TrabajoInformeBean trabajoInforme) {
		Log.debug("imprimirInforme() - Añadiendo parametros al informe");
		HttpSession sesion = request.getSession();
		DatosSesionBean datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);
		String fechaDesdeS = request.getParameter("fechaDesde");
		Timestamp fechaDesde = parseaFecha(fechaDesdeS);

		String fechaHastaS = request.getParameter("fechaHasta");
		Timestamp fechaHasta = parseaFecha(fechaHastaS);

		String codAlm = request.getParameter("codAlm");
		String codArt = request.getParameter("codArt");
		String referenciaCliente = request.getParameter("referenciaCliente");
		String supervisor = request.getParameter("idUsuarioSupervisor");
		String validado = request.getParameter("validado");
		
		trabajoInforme.addParametro("FECHA_DESDE", fechaDesde);
		trabajoInforme.addParametro("FECHA_HASTA", fechaHasta);
		trabajoInforme.addParametro("COD_TIENDA", StringUtils.isBlank(codAlm)?null:codAlm);
		trabajoInforme.addParametro("COD_ARTICULO", StringUtils.isBlank(codArt)?null:codArt);
		trabajoInforme.addParametro("REFERENCIA_FACTURA", StringUtils.isBlank(referenciaCliente)?null:referenciaCliente);
		trabajoInforme.addParametro("ID_USUARIO_SUPERVISOR", StringUtils.isBlank(supervisor)?null:supervisor);
		trabajoInforme.addParametro("VALIDADO", StringUtils.isBlank(validado)?null:validado);
		trabajoInforme.addParametro("UID_ACTIVIDAD", datosSesion.getUidActividad());

	}

	public Timestamp parseaFecha(String fecha) {
		String[] fechaSplit = fecha.split("/");
		Integer dia = Integer.valueOf(fechaSplit[0].trim());
		Integer mes = Integer.valueOf(fechaSplit[1].trim());
		Integer anyo = Integer.valueOf(fechaSplit[2].trim());

		LocalDateTime ldt = LocalDateTime.of(anyo, mes, dia, 0, 0);
		return Timestamp.valueOf(ldt);
	}
	
	@Override
	public Vista ejecutar(HttpServletRequest request) {
		try {
			MultipartFormDataRequest mrequest = (MultipartFormDataRequest) request.getAttribute(WebKeys.MULTIPART_REQUEST);

			String operacionReq = (String) request.getAttribute("operacion");
			HttpSession session = request.getSession(false);
			String operacion = request.getParameter("operacion");
			
			DatosSesionBean datosSesion = (DatosSesionBean) request.getSession().getAttribute(WebKeys.DATOS_SESION);
			ParametrosBuscarValidacionesDevolucionAlbaran param = (ParametrosBuscarValidacionesDevolucionAlbaran) session.getAttribute(DevolucionesServlet.PARAM_BUSCAR_DEVOLUCIONES);
			session.setAttribute(DevolucionesServlet.PARAM_BUSCAR_DEVOLUCIONES, param);
			/*
			 * Se comprueba si se tiene permiso de ejecución para la pantalla desde la que se lanza el informe 
			 */
			PermisosEfectivosAccionBean permisos = datosSesion.getPermisos(getAccionMenu());
    		if (!permisos.isPuedeEjecutar()) {
    			return SIN_PERMISO;
    		}
			
    		/*
    		 * Obtenemos los permisos para la acción del informe.
    		 */
			permisosInforme = datosSesion.getPermisos(getAccionInforme());
			request.setAttribute(WebKeys.PERMISOS_INFORME, datosSesion.getPermisosCache(getAccionInforme()));
			
			if (mrequest != null) {
				inicia(mrequest, request);
				operacion = mrequest.getParameter("operacion");
			}

			/*
			 * Si la operación está relacionada con la administración de las versiones (añadir, editar y eliminar), sólo si se es administrador
			 * se tendrá permiso.
			 */
			if (OP_NUEVA_VERSION.equalsIgnoreCase(operacionReq) || OP_NUEVA_VERSION.equalsIgnoreCase(operacion) || 
					OP_EDITAR_VERSION.equalsIgnoreCase(operacionReq) || OP_EDITAR_VERSION.equalsIgnoreCase(operacion) ||
					OP_ELIMINAR_VERSION.equalsIgnoreCase(operacionReq) || OP_ELIMINAR_VERSION.equalsIgnoreCase(operacion) ) {
				if (!permisos.isPuedeAdministrar()) {
					return SIN_PERMISO;
				}
			}

			// Preguntamos por la operación. Por defecto, se ejecuta la operación que muestra el lanzador de informes.
			if (OP_EJECUTAR.equalsIgnoreCase(operacionReq) || OP_EJECUTAR.equalsIgnoreCase(operacion) || (operacion == null && operacionReq == null)) {
				listado_versiones(request, session);
				
				String idObjeto = null;
				try{
					idObjeto = request.getParameter(WebKeys.ID_OBJETO);
				}catch(Exception e){
				}
				session.setAttribute(WebKeys.ID_OBJETO, idObjeto);
				
				return vistaInforme(request);

			}// Envio de formulario para crear una nueva versión.
			if (OP_NUEVA_VERSION.equalsIgnoreCase(operacion) || OP_NUEVA_VERSION.equalsIgnoreCase(operacionReq)) {
				return nueva_version(session);
			}// En caso de edición, se recupera la versión solicitada y se carga en el formulario.
			if (OP_EDITAR_VERSION.equalsIgnoreCase(operacion)) {
				return editar(request, session);
			}
			// Tratar la subida/modificación de un fichero .jrxml.
			if (OP_TRATAR_FICHERO.equalsIgnoreCase(operacion)) {
				return tratarFichero(mrequest, request);
			}
			// Eliminar una versión.
			if (OP_ELIMINAR_VERSION.equalsIgnoreCase(operacion)) {
				return eliminar(request);
			}
			if (OP_IMPRIMIR.equalsIgnoreCase(operacion)) {
				return imprimir(request, datosSesion, session);
			}
			if (OP_IMPRIMIR_RAPIDO.equalsIgnoreCase(operacion)) {
				return imprimirRapido(request, datosSesion, session);
			}
			setMensaje(request,datosSesion.getTranslation()._("No se ha indicado una operación correcta para ejecutar el informe."));
			return ERROR;
		}
		catch (InformeConstraintViolationException e) {
			setMensajeError(request, e.getMessage());
			return FORM_PAGE;
		}
		catch (InformeNotFoundException e) {
			setMensajeError(request, e.getMessage());
			return FORM_PAGE;
		}
		catch (InformeException e) {
			setMensajeError(request, e.getMessage());
			return ERROR;
		}
		catch (OperacionException e) {
			setMensajeError(request, e.getMessage());
			return ERROR;
		}
		catch (PermisoException e) {
			setMensajeError(request, e.getMessage());
			return ERROR;
		}
		catch (Exception e) {
			return ERROR;
		}	
		
	}
}
