package com.comerzzia.bricodepot.backoffice.admin.erroresinterfaces.acciones;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.bricodepot.backoffice.persistence.erroresinterfaces.ParametrosBuscarErroresInterfacesBean;
import com.comerzzia.web.base.Accion;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;

public class EjecutarAccion extends Accion{
	

	@Override
	public Vista ejecutar(HttpServletRequest request) {
		try {
    		HttpSession sesion = request.getSession();
    		DatosSesionBean datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);
    		
    		// Comprobamos los permisos de la acción
    		PermisosEfectivosAccionBean permisos = datosSesion.getPermisos(this.getAccionMenu());
    		if (!permisos.isPuedeEjecutar()) {
    			return SIN_PERMISO;
    		}
    		
    		ParametrosBuscarErroresInterfacesBean param = (ParametrosBuscarErroresInterfacesBean) sesion.getAttribute("paramBuscarErroresInterfaces");
    		if (param == null) {
    			param = new ParametrosBuscarErroresInterfacesBean();		
    			sesion.setAttribute("paramBuscarErroresInterfaces", param);
    		}
    		
    		param.setNumPagina(0);
    		
			return getControlador().getAccion("buscar").ejecutar(request);
    	}
    	catch (Exception e) {
            setError(request, e);
            
            return ERROR_PAGE;
        }
	}

	@Override
	public String getNombre() {
		return "ejecutar";
	}

}
