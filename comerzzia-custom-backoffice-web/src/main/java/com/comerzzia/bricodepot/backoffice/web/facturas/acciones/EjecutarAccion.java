package com.comerzzia.bricodepot.backoffice.web.facturas.acciones;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.comerzzia.bricodepot.backoffice.persistence.facturas.ParametrosBuscarFacturasBean;
import com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.fechas.Fecha;
import com.comerzzia.web.base.Accion;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;

public class EjecutarAccion extends Accion {

	private DatosSesionBean datosSesion;

	public Vista ejecutar(HttpServletRequest request) {
		try {
			HttpSession sesion = request.getSession();
			datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);

			// Comprobamos los permisos de la acción
			PermisosEfectivosAccionBean permisos = datosSesion.getPermisos(this.getAccionMenu());
			if (!permisos.isPuedeEjecutar()) {
				return SIN_PERMISO;
			}

			// Inicializamos los parametros de busqueda
			ParametrosBuscarFacturasBean param = (ParametrosBuscarFacturasBean) sesion.getAttribute("paramBuscarFacturas");
			if (param == null) {
				param = new ParametrosBuscarFacturasBean();
				Fecha fecha = new Fecha(new Fecha().getString(Fecha.PATRON_FECHA_CORTA), Fecha.PATRON_FECHA_CORTA);
    			param.setFechaHasta(fecha.getDate());
    			fecha.sumaDias(-30);
    			param.setFechaDesde(fecha.getDate());

				sesion.setAttribute("paramBuscarFacturas", param);
			}

			else {
				param.setNumPagina(0);
			}

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
