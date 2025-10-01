package com.comerzzia.bricodepot.backoffice.web.motivos;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.comerzzia.bricodepot.backoffice.services.motivos.MotivosService;
import com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.web.base.Accion;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;

public class EliminarAccion extends Accion {

	@Autowired
	MotivosService servicioMotivos;
	protected static Logger log = Logger.getLogger(AltaAccion.class);

	@Override
	public Vista ejecutar(HttpServletRequest request) {
		HttpSession sesion = request.getSession();
		DatosSesionBean datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);

		try { // Comprobamos los permisos de la accion
			PermisosEfectivosAccionBean permisos = datosSesion.getPermisos(this.getAccionMenu());
			if (!permisos.isPuedeEjecutar()) {
				return SIN_PERMISO;
			}
			String codigo = request.getParameter(WebKeys.ID_OBJETO);
			servicioMotivos.eliminar(codigo, datosSesion); 

			setMensaje(request, datosSesion.getTranslation().getText("El motivo se ha eliminado correctamente"));
			return getControlador().getAccion("buscar").ejecutar(request);

		} catch (Exception e) {
			log.error("Ha ocurrido un error: ", e);
			setError(request, e);
			return ERROR_PAGE;
		}
	}

	@Override
	public String getNombre() {
		return "eliminar";
	}

}
