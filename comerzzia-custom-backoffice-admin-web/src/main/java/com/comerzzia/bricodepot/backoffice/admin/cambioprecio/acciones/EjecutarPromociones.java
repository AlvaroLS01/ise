package com.comerzzia.bricodepot.backoffice.admin.cambioprecio.acciones;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.comerzzia.core.servicios.ContextHolder;
import com.comerzzia.core.servicios.api.ComerzziaApiManager;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.pricechangetracker.client.PriceChangeTrackerApi;
import com.comerzzia.pricechangetracker.client.model.ConsultaTickets;
import com.comerzzia.web.base.Accion;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;


public class EjecutarPromociones extends Accion{

	private static final Vista NEXT_PAGE = new Vista("admin/cambioprecio/buscar/jsp/buscar.jsp", Vista.INTERNA);
	protected static Logger log = Logger.getLogger(EjecutarPromociones.class);
	
	protected ComerzziaApiManager comerzziaApiManager;
	@Override
	public Vista ejecutar(HttpServletRequest request) {
		try {
			
			HttpSession sesion = request.getSession();
			DatosSesionBean datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);
			String fechaDesde = request.getParameter("fechaDesde");
			String fechaHasta = request.getParameter("fechaHasta");
			comerzziaApiManager = (ComerzziaApiManager) ContextHolder.get().getBean("comerzziaApiManagerImpl");
			PriceChangeTrackerApi api = comerzziaApiManager.getClient(datosSesion, "PriceChangeTrackerApi");
			ConsultaTickets consulta = new ConsultaTickets();
			consulta.setFechaDesde(fechaDesde);
			consulta.setFechaHasta(fechaHasta);
			api.tratarPromocionesDescuentoManualCuponesPorFechas(consulta);
			return NEXT_PAGE;
		}catch (Exception e) {
			log.error("ejecutar() - Se ha producido un error " + e.getMessage());
			setError(request, e);
			return ERROR_PAGE;
		}
	}

	@Override
	public String getNombre() {
		
		return "ejecutarPromociones";
	}
	
}
