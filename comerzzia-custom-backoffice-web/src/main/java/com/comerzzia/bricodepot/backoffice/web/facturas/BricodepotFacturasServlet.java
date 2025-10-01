package com.comerzzia.bricodepot.backoffice.web.facturas;

import javax.servlet.annotation.WebServlet;

import com.comerzzia.bricodepot.backoffice.web.facturas.acciones.BuscarAccion;
import com.comerzzia.bricodepot.backoffice.web.facturas.acciones.EjecutarAccion;
import com.comerzzia.bricodepot.backoffice.web.facturas.acciones.ImprimirAccion;
import com.comerzzia.web.base.ControladorServlet;

@WebServlet(value = "/facturas", description = "Servlet de Facturas", displayName = "FacturasServlet", name = "FacturasServlet")
public class BricodepotFacturasServlet extends ControladorServlet {

	private static final long serialVersionUID = 492699653705974509L;

	@Override
	protected void loadAcciones() {
		añadirAccionDefault(new EjecutarAccion());
		añadirAccion(new BuscarAccion());
		añadirAccion(new ImprimirAccion());
	}

}
