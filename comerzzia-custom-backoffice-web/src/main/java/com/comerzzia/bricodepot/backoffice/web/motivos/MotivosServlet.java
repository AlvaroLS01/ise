package com.comerzzia.bricodepot.backoffice.web.motivos;

import javax.servlet.annotation.WebServlet;

import com.comerzzia.web.base.ControladorServlet;

@WebServlet(value = "/motivos", description = "Servlet de Motivos", displayName = "MotivosServlet", name = "MotivosServlet")
public class MotivosServlet extends ControladorServlet {

	private static final long serialVersionUID = 1716420538534171871L;

	@Override
	protected void loadAcciones() {
		this.añadirAccionDefault(new EjecutarAccion());
		this.añadirAccion(new BuscarAccion());
		this.añadirAccion(new AltaAccion());
		this.añadirAccion(new EdicionAccion());
		this.añadirAccion(new SalvarAccion());
		this.añadirAccion(new VerAccion());
		this.añadirAccion(new EliminarAccion());
	}

}
