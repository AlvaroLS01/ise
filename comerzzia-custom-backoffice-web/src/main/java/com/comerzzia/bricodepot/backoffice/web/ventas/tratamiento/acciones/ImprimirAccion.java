package com.comerzzia.bricodepot.backoffice.web.ventas.tratamiento.acciones;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.comerzzia.core.model.informes.TrabajoInformeBean;
import com.comerzzia.core.servicios.informes.InformeException;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.fechas.FechaException;
import com.comerzzia.core.util.fechas.Fechas;
import com.comerzzia.web.base.InformeAccion;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;

public class ImprimirAccion extends InformeAccion{

	@Override
	public String getNombreInforme() {
		return "ventas.tratamiento.lstTratamientoVentas";
	}
	
	@Override
	public Vista vistaInforme(HttpServletRequest request) {
		Date hoy = new Date();
		Date semanaPasada = Fechas.sumaDias(hoy, -7);
		
		request.setAttribute("fechaDesde", semanaPasada);
		request.setAttribute("fechaHasta", hoy);
		
		return new Vista("backoffice/ventas/tratamiento/imprimir/jsp/tratamientoventas.jsp", Vista.INTERNA);
	}

	@Override
	public void imprimirInforme(HttpServletRequest request,
	        TrabajoInformeBean trabajoInforme) throws InformeException {
		HttpSession sesion = request.getSession();
		DatosSesionBean datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);
		
		// UID_ACTIVIDAD
		trabajoInforme.addParametro("UID_ACTIVIDAD", datosSesion.getUidActividad());
		// Fechas
		Date hoy = new Date();
		try {

			if (request.getParameter("fechaDesde").isEmpty()) {
				trabajoInforme.addParametro("FECHA_DESDE", Fechas.sumaDias(hoy, -7));
			}
			else {
				Date fechaDesde = Fechas.getFecha(request.getParameter("fechaDesde"));
				trabajoInforme.addParametro("FECHA_DESDE", fechaDesde);
			}
			if (request.getParameter("fechaHasta").isEmpty()) {
				trabajoInforme.addParametro("FECHA_HASTA", hoy);
			}
			else {
				Date fechaHasta = Fechas.getFecha(request.getParameter("fechaHasta"));
				trabajoInforme.addParametro("FECHA_HASTA", fechaHasta);
			}
		}
		catch (FechaException e) {
		}

		// Almacenes
		if (request.getParameter("codAlm").isEmpty()) {
			trabajoInforme.addParametro("CODALM_DESDE", "0");
			trabajoInforme.addParametro("CODALM_HASTA", "0");
		}
		else {
			trabajoInforme.addParametro("CODALM_DESDE", request.getParameter("codAlm"));
			trabajoInforme.addParametro("CODALM_HASTA", request.getParameter("codAlm"));
		}
		
	}
}
