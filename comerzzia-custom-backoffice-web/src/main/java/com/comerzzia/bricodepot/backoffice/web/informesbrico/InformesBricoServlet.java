package com.comerzzia.bricodepot.backoffice.web.informesbrico;

import javax.servlet.annotation.WebServlet;

import com.comerzzia.bricodepot.backoffice.web.informesbrico.acciones.ImprimirAccion;
import com.comerzzia.web.base.ControladorServlet;

@WebServlet(value = "/informesBrico", description = "Servlet de Informes", displayName = "InformesBricoServlet", name = "InformesBricoServlet")
public class InformesBricoServlet extends ControladorServlet {

	private static final long serialVersionUID = -8841253554637269311L;

	@Override
	protected void loadAcciones() {
		this.añadirAccionDefault(new ImprimirAccion());
	}

}
