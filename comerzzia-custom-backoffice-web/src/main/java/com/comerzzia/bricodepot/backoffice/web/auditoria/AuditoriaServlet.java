package com.comerzzia.bricodepot.backoffice.web.auditoria;

import javax.servlet.annotation.WebServlet;

import com.comerzzia.web.base.ControladorServlet;
import com.comerzzia.bricodepot.backoffice.web.auditoria.acciones.*;

@WebServlet(value = "/auditoria", description = "Servlet de Auditoria", displayName = "AuditoriaServlet", name = "AuditoriaServlet")
public class AuditoriaServlet extends ControladorServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5797279523585065913L;
	
	public static final String PARAM_BUSCAR_EVENTO_AUDITORIA = "paramBuscarEventoAuditoria";

	@Override
	protected void loadAcciones() {
		this.añadirAccionDefault(new EjecutarAccion());
		this.añadirAccion(new BuscarAccion());
		this.añadirAccion(new ImprimirAccion());
	}

}
