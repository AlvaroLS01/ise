package com.comerzzia.bricodepot.backoffice.web.tiendas.acciones;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.comerzzia.bricodepot.backoffice.web.tiendas.ui.BricodepotFormularioTiendaBean;
import com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.persistencia.general.mediospago.ParametrosBuscarMediosPagoBean;
import com.comerzzia.persistencia.general.tiendas.ParametrosBuscarTiendasBean;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;
import com.comerzzia.web.general.tiendas.acciones.EjecutarAccion;
import com.comerzzia.web.general.tiendas.ui.FormularioTiendaBean;

public class BricodepotEjecutarAccion extends EjecutarAccion{
	
	protected static final Logger log = Logger.getLogger(BricodepotEjecutarAccion.class);
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
    		
    		ParametrosBuscarTiendasBean param = (ParametrosBuscarTiendasBean) sesion.getAttribute(WebKeys.PARAM_BUSCAR_TIENDAS);
    		if (param == null) {
    			param = new ParametrosBuscarTiendasBean();
    			
    			sesion.setAttribute(WebKeys.PARAM_BUSCAR_TIENDAS, param);
    		} else {
    			param.setNumPagina(0);
    		}    		
    		
    		ParametrosBuscarMediosPagoBean paramMediosPago = (ParametrosBuscarMediosPagoBean) sesion.getAttribute(WebKeys.PARAM_BUSCAR_MEDIOS_PAGO);
    		if (paramMediosPago == null) {
    			paramMediosPago = new ParametrosBuscarMediosPagoBean();
    			
    			sesion.setAttribute(WebKeys.PARAM_BUSCAR_MEDIOS_PAGO, paramMediosPago);
    		}

    		// Inicializamos el formulario
    		FormularioTiendaBean formulario = (FormularioTiendaBean) sesion.getAttribute(WebKeys.FORMULARIO_TIENDA);
    		if (formulario == null) {
    			/*BricoD-242*/
    			formulario = new BricodepotFormularioTiendaBean();
    			
    			sesion.setAttribute(WebKeys.FORMULARIO_TIENDA, formulario);
    		}
            
    		return getControlador().getAccion("buscar").ejecutar(request);
    	}
    	catch (Exception e) {
    		log.debug("ejecutar() - Ha ocurrido un error : " + e.getMessage());
            setError(request, e);
            
            return ERROR_PAGE;
        }
	}

}
