package com.comerzzia.bricodepot.backoffice.web.ventas.devoluciones;

import javax.servlet.annotation.WebServlet;

import com.comerzzia.bricodepot.backoffice.web.ventas.devoluciones.acciones.BuscarAccion;
import com.comerzzia.bricodepot.backoffice.web.ventas.devoluciones.acciones.ImprimirAccion;
import com.comerzzia.bricodepot.backoffice.web.ventas.devoluciones.acciones.EjecutarAccion;
import com.comerzzia.bricodepot.backoffice.web.ventas.devoluciones.acciones.ImprimirTicketAccion;
import com.comerzzia.bricodepot.backoffice.web.ventas.devoluciones.acciones.ValidarAccion;
import com.comerzzia.web.base.ControladorServlet;

@WebServlet(value = "/devoluciones", description = "Servlet de Devoluciones", displayName = "DevolucionesServlet", name = "DevolucionesServlet")
public class DevolucionesServlet extends ControladorServlet {

	private static final long serialVersionUID = 4218907513555113675L;
	
	public static final String PARAM_BUSCAR_DEVOLUCIONES = "paramBuscarDevoluciones";

	@Override
	protected void loadAcciones() {
		this.añadirAccionDefault(new EjecutarAccion());
		this.añadirAccion(new BuscarAccion());
		this.añadirAccion(new ValidarAccion());
		this.añadirAccion(new ImprimirTicketAccion());
		this.añadirAccion(new ImprimirAccion());
	}

}
