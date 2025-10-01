package com.comerzzia.bricodepot.backoffice.web.ventas.devoluciones.acciones;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.comerzzia.bricodepot.backoffice.persistence.devoluciones.validacion.ParametrosBuscarValidacionesDevolucionAlbaran;
import com.comerzzia.bricodepot.backoffice.web.ventas.devoluciones.DevolucionesServlet;
import com.comerzzia.core.model.informes.TrabajoInformeBean;
import com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.fechas.Fecha;
import com.comerzzia.web.base.Accion;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;

public class EjecutarAccion extends Accion{
	
	
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
    		ParametrosBuscarValidacionesDevolucionAlbaran param = (ParametrosBuscarValidacionesDevolucionAlbaran) sesion.getAttribute(DevolucionesServlet.PARAM_BUSCAR_DEVOLUCIONES);
    		if (param == null) {
    			param = new ParametrosBuscarValidacionesDevolucionAlbaran();
    			
    			Fecha fecha = new Fecha(new Fecha().getString(Fecha.PATRON_FECHA_CORTA), Fecha.PATRON_FECHA_CORTA);
    			param.setFechaHasta(fecha.getDate());
    			param.setFechaHastaTime("23:59");
    			fecha.sumaDias(-30);
    			param.setFechaDesde(fecha.getDate());
    			param.setFechaDesdeTime("00:00");
    			
    			param.setValidado("N");
    			
    			sesion.setAttribute(DevolucionesServlet.PARAM_BUSCAR_DEVOLUCIONES, param);
    		}
    		
    		TrabajoInformeBean trabajoInforme = new TrabajoInformeBean();
    		trabajoInforme.setIdInforme(60001L);
    		trabajoInforme.setInforme("ventas.facturas.facturaA4");
    		sesion.setAttribute(WebKeys.TRABAJO_INFORME, trabajoInforme);
    		
    		param.setNumPagina(0);
    
            
    		return getControlador().getAccion("buscar").ejecutar(request);
    	}catch (Exception e) {
            setError(request, e);
            
            return ERROR_PAGE;
        }
    }
}
