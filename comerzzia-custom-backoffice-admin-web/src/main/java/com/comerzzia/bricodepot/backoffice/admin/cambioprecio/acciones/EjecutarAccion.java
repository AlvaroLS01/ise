package com.comerzzia.bricodepot.backoffice.admin.cambioprecio.acciones;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.fechas.Fecha;
import com.comerzzia.web.base.Accion;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;

public class EjecutarAccion extends Accion{
	
	private static final Vista NEXT_PAGE = new Vista("admin/cambioprecio/buscar/jsp/buscar.jsp", Vista.INTERNA);
	
	protected static Logger log = Logger.getLogger(EjecutarAccion.class);

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
			Fecha fecha = new Fecha(new Fecha().getString(Fecha.PATRON_FECHA_CORTA), Fecha.PATRON_FECHA_CORTA);
			sesion.setAttribute("fechaHasta", fecha.getDate());
			fecha.sumaDias(-30);
			sesion.setAttribute("fechaDesde", fecha.getDate());
		

			return NEXT_PAGE;
		}
		catch (Exception e) {
			log.error("ejecutar() - Se ha producido un error " + e.getMessage());
			setError(request, e);
			return ERROR_PAGE;
		}

	}

	@Override
	public String getNombre() {
		
		return "ejecutar";
	}

}
