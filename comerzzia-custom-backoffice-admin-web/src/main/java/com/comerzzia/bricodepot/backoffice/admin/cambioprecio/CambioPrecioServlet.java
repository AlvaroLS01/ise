package com.comerzzia.bricodepot.backoffice.admin.cambioprecio;

import javax.servlet.annotation.WebServlet;

import com.comerzzia.bricodepot.backoffice.admin.cambioprecio.acciones.EjecutarAccion;
import com.comerzzia.bricodepot.backoffice.admin.cambioprecio.acciones.EjecutarPromociones;
import com.comerzzia.web.base.ControladorServlet;
@WebServlet(value = "/cambioPrecio", description = "Cambios de precio", displayName = "CambioPrecioServlet", name = "CambioPrecioServlet")
public class CambioPrecioServlet extends ControladorServlet {


	private static final long serialVersionUID = -3324767665093701295L;

	@Override
	protected void loadAcciones() {
		this.añadirAccionDefault(new EjecutarAccion());
		this.añadirAccion(new EjecutarPromociones());
	}

}
