package com.comerzzia.bricodepot.backoffice.web.ventas.tratamiento;

import javax.servlet.annotation.WebServlet;

import com.comerzzia.bricodepot.backoffice.web.ventas.tratamiento.acciones.ImprimirAccion;
import com.comerzzia.web.base.ControladorServlet;

@WebServlet(value = "/tratamientoVentas", description = "Servlet de tratamiento de ventas", displayName = "TratamientoVentasServlet", name = "TratamientoVentasServlet")
public class TratamientoVentasServlet extends ControladorServlet{

	private static final long serialVersionUID = 3990345124695292570L;

	@Override
	protected void loadAcciones() {
		this.añadirAccionDefault(new ImprimirAccion());
		
	}

}
