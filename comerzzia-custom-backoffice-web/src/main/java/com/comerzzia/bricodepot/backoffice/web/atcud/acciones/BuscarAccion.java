package com.comerzzia.bricodepot.backoffice.web.atcud.acciones;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.comerzzia.core.model.variables.VariableBean;
import com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.servicios.variables.ServicioVariablesImpl;
import com.comerzzia.web.base.Accion;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;

public class BuscarAccion extends Accion {

	protected static Logger log = Logger.getLogger(BuscarAccion.class);
	private static final Vista NEXT_PAGE = new Vista("backoffice/atcud/buscar/jsp/buscar.jsp", Vista.INTERNA);
	protected static String X_ATCUD_MAGENTO = "X_ATCUD_MAGENTO";

	@Autowired
	protected ServicioVariablesImpl servicioVariablesImpl;

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

			VariableBean variable = servicioVariablesImpl.consultar(datosSesion, X_ATCUD_MAGENTO);

			if (variable != null) {
				request.setAttribute("variables", variable);
			}

			return NEXT_PAGE;
		}
		catch (Exception e) {
			log.error("ejecutar() - Ha ocurrido un error: " + e, e);
			setError(request, e);
			return ERROR_PAGE;
		}
	}

	@Override
	public String getNombre() {
		return "buscar";
	}

}
