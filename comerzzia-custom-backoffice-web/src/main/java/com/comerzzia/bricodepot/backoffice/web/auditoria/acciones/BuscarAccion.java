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
package com.comerzzia.bricodepot.backoffice.web.auditoria.acciones;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.comerzzia.bricodepot.backoffice.persistence.tickets.auditoria.ParametrosBuscarEventoAuditoria;
import com.comerzzia.bricodepot.backoffice.services.tickets.auditoria.EventoAuditoriaException;
import com.comerzzia.bricodepot.backoffice.services.tickets.auditoria.ServicioEventosAuditoria;
import com.comerzzia.bricodepot.backoffice.web.auditoria.AuditoriaServlet;
import com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.fechas.FechaException;
import com.comerzzia.core.util.fechas.Fechas;
import com.comerzzia.core.util.paginacion.PaginaResultados;
import com.comerzzia.web.base.Accion;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;

public class BuscarAccion extends Accion {

	private static final Vista NEXT_PAGE = new Vista("backoffice/auditoria/buscar/jsp/buscar.jsp", Vista.INTERNA);

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

			// Obtenemos parámetros de búsqueda
			ParametrosBuscarEventoAuditoria param = (ParametrosBuscarEventoAuditoria) sesion
					.getAttribute(AuditoriaServlet.PARAM_BUSCAR_EVENTO_AUDITORIA);

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
				PaginaResultados paginaResultados = ServicioEventosAuditoria.get().consultar(param, datosSesion);
				request.setAttribute(WebKeys.PAGINA_RESULTADOS, paginaResultados);
			}

			return NEXT_PAGE;
		} catch (EventoAuditoriaException e) {
			setError(request, e);
			return ERROR_PAGE;
		} catch (Exception e) {
			setError(request, e);

			return ERROR_PAGE;
		}
	}

	protected void setParametrosBuscar(ParametrosBuscarEventoAuditoria param, HttpServletRequest request)
			throws FechaException {
		param.setUsuario(request.getParameter("usuario"));
		param.setUsuarioSupervisor(request.getParameter("supervisor"));

		try {
			param.setFechaDesde(Fechas.getFecha(request.getParameter("fechaDesde")));
		} catch (Exception ignore) {
			param.setFechaDesde(null);
		}

		try {
			param.setFechaHasta(Fechas.getFecha(request.getParameter("fechaHasta")));
		} catch (Exception ignore) {
			param.setFechaHasta(null);
		}

		param.setNumPagina(1);

		// Actualizar el número de resultados por página
		try {
			param.setTamañoPagina(Integer.parseInt(request.getParameter("tamanoPagina")));
		} catch (Exception ignore) {
			// Si no se recibe el tamaño de página, se mantiene el que se tenía
		}
	}

	protected void setPaginaBuscar(ParametrosBuscarEventoAuditoria param, HttpServletRequest request) {
		try {
			// Obtenemos la pagina solicitada
			int pagina = Integer.parseInt(request.getParameter("pagina"));
			param.setNumPagina(pagina);
		} catch (Exception ignore) {
			// Si no tenemos pagina, se mostrará la que teníamos
		}
	}

	protected void setOrdenBuscar(ParametrosBuscarEventoAuditoria param, HttpServletRequest request) {
		try {
			// Establecemos primera pagina
			param.setNumPagina(1);

			// Obtenemos la columna por la que ordenar
			int columna = Integer.parseInt(request.getParameter("columna"));

			// Establecemos la columna de orden
			switch (columna) {
			case 1: // CODALM
				if (param.getOrden().equals("CODALM")) {
					param.setOrden("CODALM DESC");
				} else {
					param.setOrden("CODALM");
				}
				break;
			case 2: // FECHA
				if (param.getOrden().equals("FECHA")) {
					param.setOrden("FECHA DESC");
				} else {
					param.setOrden("FECHA");
				}
				break;
			case 3: // TIPO_EVENTO
				if (param.getOrden().equals("TIPO_EVENTO")) {
					param.setOrden("TIPO_EVENTO DESC");
				} else {
					param.setOrden("TIPO_EVENTO");
				}
				break;
			case 4: // DES_USUARIO
				if (param.getOrden().equals("DES_USUARIO")) {
					param.setOrden("DES_USUARIO DESC");
				} else {
					param.setOrden("DES_USUARIO");
				}
				break;
			case 5: // DES_USUARIO
				if (param.getOrden().equals("DES_USUARIO_SUPERVISOR")) {
					param.setOrden("DES_USUARIO_SUPERVISOR DESC");
				} else {
					param.setOrden("DES_USUARIO_SUPERVISOR");
				}
				break;
			case 6: // UID_TICKET_VENTA
				if (param.getOrden().equals("UID_TICKET_VENTA")) {
					param.setOrden("UID_TICKET_VENTA DESC");
				} else {
					param.setOrden("UID_TICKET_VENTA");
				}
				break;
			case 8: // DESART
				if (param.getOrden().equals("DESART")) {
					param.setOrden("DESART DESC");
				} else {
					param.setOrden("DESART");
				}
				break;

			default:
				param.setOrden("FECHA DESC");
			}
		} catch (Exception e) {
			// Si no tenemos orden, se dejar el que se tenía
		}
	}

}
