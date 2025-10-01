package com.comerzzia.bricodepot.backoffice.web.atcud.acciones;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.comerzzia.core.model.variables.VariableBean;
import com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.servicios.variables.ServicioVariablesImpl;
import com.comerzzia.taglib.WebKeys;
import com.comerzzia.web.base.Accion;
import com.comerzzia.web.base.Vista;

public class EjecutarAccion extends Accion {

	protected static Logger log = Logger.getLogger(EjecutarAccion.class);
	protected static String X_ATCUD_MAGENTO = "X_ATCUD_MAGENTO";
	
	@Autowired
	protected ServicioVariablesImpl servicioVariableImpl;

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
			
			VariableBean param = (VariableBean) sesion.getAttribute("variables");
			if (param == null) {
				param = new VariableBean();
				sesion.setAttribute("variables", param);
			}else {
				sesion.setAttribute("variables", param);
			}

			return getControlador().getAccion("buscar").ejecutar(request);
		}
		catch (Exception e) {
			log.error("Ha ocurrido un error: " + e);
			setError(request, e);
			return ERROR_PAGE;
		}

	}

	@Override
	public String getNombre() {
		return "ejecutar";
	}

}
