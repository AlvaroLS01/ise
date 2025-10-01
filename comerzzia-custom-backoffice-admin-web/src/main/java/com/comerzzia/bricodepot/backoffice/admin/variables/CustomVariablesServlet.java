package com.comerzzia.bricodepot.backoffice.admin.variables;

import javax.servlet.annotation.WebServlet;

import org.springframework.context.annotation.Primary;

import com.comerzzia.bricodepot.backoffice.admin.variables.acciones.CustomLeerFormularioAccion;
import com.comerzzia.bricodepot.backoffice.admin.variables.acciones.CustomVerFormularioAccion;
import com.comerzzia.web.core.variables.VariablesServlet;
import com.comerzzia.web.core.variables.acciones.EditarAccion;
import com.comerzzia.web.core.variables.acciones.EjecutarAccion;
import com.comerzzia.web.core.variables.acciones.SalvarAccion;

@WebServlet(value = "/variables", description = "Servlet de Variables", displayName = "CustomVariablesServlet", name = "CustomVariablesServlet")
@Primary
public class CustomVariablesServlet extends VariablesServlet {

	private static final long serialVersionUID = 1549235567351456957L;

	@Override
	protected void loadAcciones() {
		this.añadirAccionDefault(new EjecutarAccion());
		this.añadirAccion(new CustomVerFormularioAccion());
		this.añadirAccion(new EditarAccion());
		this.añadirAccion(new CustomLeerFormularioAccion());
		this.añadirAccion(new SalvarAccion());
	}

}
