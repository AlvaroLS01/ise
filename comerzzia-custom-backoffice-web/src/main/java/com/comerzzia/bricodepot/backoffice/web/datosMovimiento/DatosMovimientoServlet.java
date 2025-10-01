package com.comerzzia.bricodepot.backoffice.web.datosMovimiento;

import javax.servlet.annotation.WebServlet;

import com.comerzzia.bricodepot.backoffice.web.datosMovimiento.acciones.EjecutarAccion;
import com.comerzzia.bricodepot.backoffice.web.datosMovimiento.acciones.SalvarAccion;
import com.comerzzia.web.base.ControladorServlet;

@WebServlet(value = "/datosMovimiento", description = "Servlet de Datos Movimiento 92", displayName = "DatosMovimientoServlet", name = "DatosMovimientoServlet")
public class DatosMovimientoServlet extends ControladorServlet {

	private static final long serialVersionUID = 9164314395358065557L;

	@Override
	protected void loadAcciones() {
		añadirAccionDefault(new EjecutarAccion());
		añadirAccion(new SalvarAccion());
	}

}
