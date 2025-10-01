package com.comerzzia.bricodepot.backoffice.web.atcud;

import javax.servlet.annotation.WebServlet;

import com.comerzzia.bricodepot.backoffice.web.atcud.acciones.BuscarAccion;
import com.comerzzia.bricodepot.backoffice.web.atcud.acciones.EditarAccion;
import com.comerzzia.bricodepot.backoffice.web.atcud.acciones.EjecutarAccion;
import com.comerzzia.bricodepot.backoffice.web.atcud.acciones.SalvarAccion;
import com.comerzzia.web.base.ControladorServlet;

@SuppressWarnings("serial")
@WebServlet(value = "/atcud", description = "Servlet de ATCUD", displayName = "AtcudServlet", name = "AtcudServlet")
public class BricodepotAtcudServlet extends ControladorServlet {

	@Override
	protected void loadAcciones() {
		añadirAccionDefault(new EjecutarAccion());
		añadirAccion(new BuscarAccion());
		añadirAccion(new EditarAccion());
		añadirAccion(new SalvarAccion());
	}
}
