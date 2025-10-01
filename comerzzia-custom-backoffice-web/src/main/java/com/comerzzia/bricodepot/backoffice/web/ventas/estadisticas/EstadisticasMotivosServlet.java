package com.comerzzia.bricodepot.backoffice.web.ventas.estadisticas;

import javax.servlet.annotation.WebServlet;

import com.comerzzia.web.base.ControladorServlet;

@WebServlet(value = "/estadisticasMotivos", description = "Estadísticas de motivos", displayName = "EstadisticasMotivosServlet", name = "EstadisticasMotivosServlet")
public class EstadisticasMotivosServlet extends ControladorServlet {
	private static final long serialVersionUID = -29561542707872277L;

	@Override
	protected void loadAcciones() {
		this.añadirAccionDefault(new ImprimirAccion());
	}
}
