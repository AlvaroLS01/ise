package com.comerzzia.bricodepot.backoffice.admin.erroresinterfaces.acciones;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.comerzzia.api.core.client.model.I18n;
import com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.bricodepot.backoffice.services.erroresinterfaces.ErroresInterfacesService;
import com.comerzzia.web.base.Accion;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;

public class EliminarAccion extends Accion{

	protected static Logger log = Logger.getLogger(EliminarAccion.class);
	
	@Autowired
	ErroresInterfacesService erroresService;
	
	@Override
	public Vista ejecutar(HttpServletRequest request) {
		try {
		HttpSession sesion = request.getSession();
		DatosSesionBean datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);
		
		// Comprobamos los permisos de la acción
		PermisosEfectivosAccionBean permisos = datosSesion.getPermisosCache(this.getAccionMenu());
		if (!permisos.isPuedeEjecutar()) {
			return SIN_PERMISO;
		}
		
		String[] uidsErrores = request.getParameter("uidsErrores").split(",");
		String[] idsClases = request.getParameter("idsClases").split(",");
		String[] idsObjetos = request.getParameter("idsObjetos").split(",");
		if(uidsErrores == null || uidsErrores.length == 0 || uidsErrores[0].isEmpty()) {
			setMensajeError(request, datosSesion.getTranslation()._("Debe seleccionar al menos un error"));
		}else {
			for(int i=0; i<uidsErrores.length; i++) {
				erroresService.delete(datosSesion, idsClases[i], idsObjetos[i], uidsErrores[i]);
			}
			setMensaje(request, datosSesion.getTranslation()._("Se han eliminado los errores correctamente"));
		}
			return getControlador().getAccion("buscar").ejecutar(request);
		} catch (Exception e) {
			log.error("Error eliminando las interfaces. ", e);
			 setError(request, e);
	            
	         return ERROR_PAGE;
		}
	}

	@Override
	public String getNombre() {
		return "eliminar";
	}

}
