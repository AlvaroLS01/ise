package com.comerzzia.bricodepot.backoffice.web.atcud.acciones;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.comerzzia.bricodepot.backoffice.web.motivos.AltaAccion;
import com.comerzzia.core.model.variables.VariableBean;
import com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.servicios.variables.ServicioVariablesImpl;
import com.comerzzia.core.util.base.Estado;
import com.comerzzia.web.base.Accion;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;

public class EditarAccion extends Accion {

	protected static Logger log = Logger.getLogger(AltaAccion.class);
	private static final Vista NEXT_PAGE = new Vista("backoffice/atcud/mantenimiento/jsp/atcud.jsp", Vista.INTERNA);

	@Autowired
	protected ServicioVariablesImpl servicioVariablesImpl;

	@Override
	public Vista ejecutar(HttpServletRequest request) {
		HttpSession sesion = request.getSession();
		DatosSesionBean datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);

		try {
			PermisosEfectivosAccionBean permisos = datosSesion.getPermisosCache(this.getAccionMenu());
			if (!permisos.isPuedeEditar()) {
				return SIN_PERMISO;
			}

			String codigo = request.getParameter(WebKeys.ID_OBJETO);
			VariableBean variable = servicioVariablesImpl.consultar(datosSesion, codigo);
			variable.setEstadoBean(Estado.MODIFICADO);
			variable.setEnEdicion(true);

			sesion.setAttribute("variables", variable);

			return NEXT_PAGE;
		}
		catch (Exception e) {
			log.error("ejecutar() - Ha habido un error: ", e);
			setError(request, e);

			return ERROR_PAGE;
		}
	}

	@Override
	public String getNombre() {
		return "editar";
	}

}
