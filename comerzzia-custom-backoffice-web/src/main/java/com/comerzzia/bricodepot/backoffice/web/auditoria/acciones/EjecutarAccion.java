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

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.comerzzia.bricodepot.backoffice.persistence.tickets.auditoria.ParametrosBuscarEventoAuditoria;
import com.comerzzia.bricodepot.backoffice.web.auditoria.AuditoriaServlet;
import com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.fechas.Fechas;
import com.comerzzia.web.base.Accion;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;


public class EjecutarAccion extends Accion {
	
	
	private DatosSesionBean datosSesion;
	
	/**
     * Devuelve el nombre de la acción
     * @return String con el nombre de la acción
     */
    public String getNombre() {
        return "ejecutar";
    }
    
    /**
     * Ejecuta la acción
     * @param request Datos de la petición
     * @return Vista con la siguiente pagina a mostrar
     */
    public Vista ejecutar(HttpServletRequest request) {
    	try {
    		HttpSession sesion = request.getSession();
    		datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);
    		
    		// Comprobamos los permisos de la acción
    		PermisosEfectivosAccionBean permisos = datosSesion.getPermisos(this.getAccionMenu());
    		if (!permisos.isPuedeEjecutar()) {
    			return SIN_PERMISO;
    		}
    		
    		// Inicializamos parámetros de búsqueda
    		ParametrosBuscarEventoAuditoria param = (ParametrosBuscarEventoAuditoria) sesion.getAttribute(AuditoriaServlet.PARAM_BUSCAR_EVENTO_AUDITORIA);
    		if (param == null) {
    			param = new ParametrosBuscarEventoAuditoria();
    			
    			Date hoy = new Date();
    			param.setFechaDesde(Fechas.sumaDias(hoy, -30));
    			param.setFechaHasta(hoy);
    			
    			
    			sesion.setAttribute(AuditoriaServlet.PARAM_BUSCAR_EVENTO_AUDITORIA, param);
    		}
    		
    		param.setNumPagina(0);
    
            
    		return getControlador().getAccion("buscar").ejecutar(request);
    	}catch (Exception e) {
            setError(request, e);
            
            return ERROR_PAGE;
        }
    }
}
