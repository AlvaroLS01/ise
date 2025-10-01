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
package com.comerzzia.bricodepot.backoffice.web.ventas.devoluciones.acciones;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.comerzzia.bricodepot.backoffice.persistence.devoluciones.validacion.ParametrosBuscarValidacionesDevolucionAlbaran;
import com.comerzzia.bricodepot.backoffice.services.devoluciones.validacion.ValidacionDevolucionException;
import com.comerzzia.bricodepot.backoffice.services.devoluciones.validacion.ValidacionDevolucionesServiceImpl;
import com.comerzzia.bricodepot.backoffice.web.ventas.devoluciones.DevolucionesServlet;
import com.comerzzia.core.model.informes.TrabajoInformeBean;
import com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.fechas.Fecha;
import com.comerzzia.core.util.fechas.FechaException;
import com.comerzzia.core.util.paginacion.PaginaResultados;
import com.comerzzia.web.base.Accion;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;

public class BuscarAccion extends Accion {

	private static final Vista NEXT_PAGE = new Vista("backoffice/devoluciones/buscar/jsp/buscar.jsp", Vista.INTERNA);

	/**
	 * Devuelve el nombre de la acción
	 * 
	 * @return String con el nombre de la acción
	 */
	public String getNombre() {
		return "buscar";
	}

	/**
	 * Ejecuta la acción
	 * 
	 * @param request Datos de la petición
	 * @return Vista con la siguiente pagina a mostrar
	 */
	public Vista ejecutar(HttpServletRequest request) {
		try {
			HttpSession sesion = request.getSession();
			DatosSesionBean datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);

			// Comprobamos los permisos de la acción
			PermisosEfectivosAccionBean permisos = datosSesion.getPermisosCache(this.getAccionMenu());
			if (!permisos.isPuedeEjecutar()) {
				return SIN_PERMISO;
			}
			
			TrabajoInformeBean trabajoInforme = new TrabajoInformeBean();
    		trabajoInforme.setIdInforme(60001L);
    		trabajoInforme.setInforme("ventas.facturas.facturaA4");
    		sesion.setAttribute(WebKeys.TRABAJO_INFORME, trabajoInforme);

			// Obtenemos parámetros de búsqueda
			ParametrosBuscarValidacionesDevolucionAlbaran param = (ParametrosBuscarValidacionesDevolucionAlbaran) sesion
					.getAttribute(DevolucionesServlet.PARAM_BUSCAR_DEVOLUCIONES);

			// Comprobamos la operación a realizar
			String operacion = request.getParameter("operacion");
			if (operacion != null) {
				// Consultar
				if (operacion.equals("consultar")) {
					// Establecemos parámetros de busqueda
					setParametrosBuscar(param, request);
				}
				// Ver pagina
				else if (operacion.equals("paginar")) {
					setPaginaBuscar(param, request);
				}
				// Ordenar
				else if (operacion.equals("ordenar")) {
					setOrdenBuscar(param, request);
				}
			}

			// Si tenemos página, realizamos la búsqueda
			if (param.getNumPagina() > 0) {
				PaginaResultados paginaResultados = ValidacionDevolucionesServiceImpl.get().consultarPaginado(param, datosSesion);
				request.setAttribute(WebKeys.PAGINA_RESULTADOS, paginaResultados);
			}

			return NEXT_PAGE;
		} catch (ValidacionDevolucionException e) {
			setError(request, e);
			return ERROR_PAGE;
		} catch (Exception e) {
			setError(request, e);

			return ERROR_PAGE;
		}
	}

	protected void setParametrosBuscar(ParametrosBuscarValidacionesDevolucionAlbaran param, HttpServletRequest request)
			throws FechaException {
		
		try {
			Fecha fechaDesdeParam = new Fecha(request.getParameter("fechaDesde"), Fecha.PATRON_FECHA_CORTA);
			param.setFechaDesde(fechaDesdeParam.getDate());
			param.setFechaDesdeTime("00:00");
		} catch (Exception ignore) {
			param.setFechaDesde(null);
		}
		
		try {
			Fecha fechaHastaParam = new Fecha(request.getParameter("fechaHasta"), Fecha.PATRON_FECHA_CORTA);
			param.setFechaHasta(fechaHastaParam.getDate());
			param.setFechaHastaTime("23:59");
		} catch (Exception ignore) {
			param.setFechaHasta(null);
		}
		
		param.setCodAlm(request.getParameter("codAlm"));
		param.setDesAlm(request.getParameter("desAlm"));
		
		param.setIdUsuarioSupervisor(request.getParameter("idUsuarioSupervisor"));
		param.setDesUsuarioSupervisor(request.getParameter("desUsuarioSupervisor"));
		
		param.setCodArt(request.getParameter("codArt"));
		param.setDesArt(request.getParameter("desArt"));
		
		param.setReferenciaCliente(request.getParameter("referenciaCliente"));
		
		param.setValidado(request.getParameter("validado"));
		
		
		param.setNumPagina(1);

		// Actualizar el número de resultados por página
		try {
			param.setTamañoPagina(Integer.parseInt(request.getParameter("tamanoPagina")));
		} catch (Exception ignore) {
			// Si no se recibe el tamaño de página, se mantiene el que se tenía
		}
	}

	protected void setPaginaBuscar(ParametrosBuscarValidacionesDevolucionAlbaran param, HttpServletRequest request) {
		try {
			// Obtenemos la pagina solicitada
			int pagina = Integer.parseInt(request.getParameter("pagina"));
			param.setNumPagina(pagina);
		} catch (Exception ignore) {
			// Si no tenemos pagina, se mostrará la que teníamos
		}
	}

	protected void setOrdenBuscar(ParametrosBuscarValidacionesDevolucionAlbaran param, HttpServletRequest request) {
		//TODO BCR por alguna razón no funciona el orden en la pantalla
		try {
			// Establecemos primera pagina
			param.setNumPagina(1);

			// Obtenemos la columna por la que ordenar
			int columna = Integer.parseInt(request.getParameter("columna"));

			// Establecemos la columna de orden
			String columnaOrden = "";
			switch (columna) {
			case 1:
				columnaOrden = "alb_cab.fecha";
				break;
			case 2:
				columnaOrden = "usu.desusuario";
				break;
			case 3:
				columnaOrden = "art.desart";
				break;
			case 4:
				columnaOrden = "alb_det.cantidad";
				break;
			case 5:
				columnaOrden = "motivos.descripcion";
				break;
			case 6:
				columnaOrden = "alb_det.importe_total";
				break;
			case 7:
				columnaOrden = "almacenes.desalm";
				break;
			case 8:
				columnaOrden = "alb_cab.referencia_cliente";
				break;
			case 9:
				columnaOrden = "alb_cab.cod_albaran_origen";
				break;
			case 10:
				columnaOrden = "usu2.desusuario";
				break;
			case 11:
				columnaOrden = "dev.fecha_validacion";
				break;
				
			default:
				param.setOrden("alb_cab.fecha DESC, alb_cab.hora DESC, alb_cab.referencia_cliente DESC, art.desart DESC");
			}
			
			if (param.getOrden().equals(columnaOrden)) {
				param.setOrden(columnaOrden + " DESC");
			} else {
				param.setOrden(columnaOrden);
			}
			
		} catch (Exception e) {
			// Si no tenemos orden, se dejar el que se tenía
		}
	}

}
