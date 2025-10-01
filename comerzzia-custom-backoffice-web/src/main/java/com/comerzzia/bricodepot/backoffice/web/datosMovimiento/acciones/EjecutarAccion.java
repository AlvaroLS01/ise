package com.comerzzia.bricodepot.backoffice.web.datosMovimiento.acciones;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.comerzzia.bricodepot.backoffice.persistence.datosMovimiento.DatosMovimientoBean;
import com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.web.base.Accion;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;

public class EjecutarAccion extends Accion{
	
	protected static Logger log = Logger.getLogger(EjecutarAccion.class);
	
	private static final Vista NEXT_PAGE = new Vista("backoffice/datosMovimiento/mantenimiento/jsp/datosMovimiento.jsp", Vista.INTERNA);

	@Override
	public String getNombre() {
		return "ejecutar";
	}

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

			DatosMovimientoBean datosMovimiento = new DatosMovimientoBean();
    		sesion.setAttribute("datosMovimiento", datosMovimiento);
    		
    		return NEXT_PAGE;
    	}
    	catch (Exception e) {
            setError(request, e);
            
            return ERROR_PAGE;
        }
	}

}
