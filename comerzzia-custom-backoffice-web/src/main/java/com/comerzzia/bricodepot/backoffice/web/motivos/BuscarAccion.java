package com.comerzzia.bricodepot.backoffice.web.motivos;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.comerzzia.bricodepot.backoffice.services.motivos.MotivosService;
import com.comerzzia.bricodepot.backoffice.services.motivos.ParametrosBuscarMotivosBean;
import com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.paginacion.PaginaResultados;
import com.comerzzia.taglib.WebKeys;
import com.comerzzia.web.base.Accion;
import com.comerzzia.web.base.Vista;

public class BuscarAccion extends Accion {

	protected static Logger log = Logger.getLogger(BuscarAccion.class);
	private static final Vista NEXT_PAGE = new Vista("backoffice/motivos/buscar/jsp/buscar.jsp", Vista.INTERNA);

	@Autowired
	protected MotivosService servicioMotivos;

	@Override
	public Vista ejecutar(HttpServletRequest request) {
		try {
			HttpSession sesion = request.getSession();

			DatosSesionBean datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);

			// Comprobamos los permisos de la accion
			PermisosEfectivosAccionBean permisos = datosSesion.getPermisos(this.getAccionMenu());
			if (!permisos.isPuedeEjecutar()) {
				return SIN_PERMISO;
			}
			// Obtenemos los parametros de busqueda
			ParametrosBuscarMotivosBean param = (ParametrosBuscarMotivosBean) sesion.getAttribute("paramBuscarMotivos");

			String operacion = request.getParameter("operacion");

			if (operacion != null) {
				// Consultar
				if (operacion.equals("consultar")) {
					setParametrosBuscar(param, request);
				}
				// Ver página
				else if (operacion.equals("paginar")) {
					setPaginaBuscar(param, request);
				}
				// Ordenar
				else if (operacion.equals("ordenar")) {
					setOrdenBuscar(param, request);

				}
			}

			// Si tenemos pagina, realizamos la busqueda
			if (param.getNumPagina() > 0) {
				PaginaResultados paginaResultados = servicioMotivos.consultar(param, datosSesion);
				request.setAttribute("paginaResultados", paginaResultados);
			}
			return NEXT_PAGE;
		}
		catch (Exception e) {
			log.error("Ha ocurrido un error: " + e);
			setError(request, e);
			return ERROR_PAGE;
		}
	}

	protected void setParametrosBuscar(ParametrosBuscarMotivosBean param, HttpServletRequest request) {
		param.setCodigo(request.getParameter("codigo"));
		param.setDescripcion(request.getParameter("descripcion"));
		param.setNumPagina(1);

		try {
			param.setTamañoPagina(Integer.parseInt(request.getParameter("tamanoPagina")));
		}
		catch (Exception ignore) {
		}
	}

	protected void setPaginaBuscar(ParametrosBuscarMotivosBean param, HttpServletRequest request) {
		// SIEMPRE IGUAL EN TODOS LADOS
		try {
			// Obtenemos la pagina solicitada
			int pagina = Integer.parseInt(request.getParameter("pagina"));
			param.setNumPagina(pagina);
		}
		catch (Exception ignore) {

		}
	}

	protected void setOrdenBuscar(ParametrosBuscarMotivosBean param, HttpServletRequest request) {
		// SIEMPRE IGUAL: SOLO CAMBIAN LAS COLUMNAS
		// Establecamos primera pagina
		try {
			param.setNumPagina(1);

			// Obtenemos la columna por la que ordenar
			int columna = Integer.parseInt(request.getParameter("columna"));

			// Establecemos la columna de orden
			switch (columna) {
				case 1:
					if (param.getOrden().equals("CODIGO")) {
						param.setOrden("CODIGO DESC");
					}
					else {
						param.setOrden("CODIGO");
					}
					break;
				case 2:
					if (param.getOrden().equals("DESCRIPCION")) {
						param.setOrden("DESCRIPCION DESC");
					}
					else {
						param.setOrden("DESCRIPCION");
					}
					break;
				default:
					param.setOrden("CODIGO DESC");

			}
		}
		catch (Exception e) {

		}
	}

	@Override
	public String getNombre() {
		return "buscar";
	}

}
