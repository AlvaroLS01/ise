package com.comerzzia.bricodepot.backoffice.admin.erroresinterfaces;

import javax.servlet.annotation.WebServlet;

import com.comerzzia.bricodepot.backoffice.admin.erroresinterfaces.acciones.BuscarAccion;
import com.comerzzia.bricodepot.backoffice.admin.erroresinterfaces.acciones.EjecutarAccion;
import com.comerzzia.bricodepot.backoffice.admin.erroresinterfaces.acciones.EliminarAccion;
import com.comerzzia.web.base.ControladorServlet;

@SuppressWarnings("serial")
@WebServlet(value = "/erroresInterfaces", description = "Servlet de Errores en las Interfaces", displayName = "ErroresInterfacesServlet", name = "ErroresInterfacesServlet")
public class ErroresInterfacesServlet extends ControladorServlet{

	@Override
	protected void loadAcciones() {
		this.añadirAccionDefault(new EjecutarAccion());
		this.añadirAccion(new BuscarAccion());
		this.añadirAccion(new EliminarAccion());
	}

}
