package com.comerzzia.bricodepot.backoffice.web.core.login;

import javax.servlet.annotation.WebServlet;

import com.comerzzia.bricodepot.backoffice.web.core.login.acciones.BricodepotLoginAction;
import com.comerzzia.bricodepot.backoffice.web.core.login.acciones.CambiarClaveAccion;
import com.comerzzia.web.core.filtros.ExcludeSessionFilter;
import com.comerzzia.web.core.login.LoginServlet;
import com.comerzzia.web.core.login.acciones.CargarEmpresasLoginAccion;
import com.comerzzia.web.core.login.acciones.LoginDiferidoAccion;
import com.comerzzia.web.core.login.acciones.LogoutAccion;

@ExcludeSessionFilter
@WebServlet(value = "/login", description = "Servlet de Login", displayName = "LoginServlet", name = "LoginServlet")
public class BricodepotLoginServlet extends LoginServlet {

	private static final long serialVersionUID = 6557330050643473738L;

	@Override
	protected void loadAcciones() {
		this.añadirAccionDefault(new LogoutAccion());
		this.añadirAccion(new BricodepotLoginAction());
		this.añadirAccion(new CargarEmpresasLoginAccion());
		this.añadirAccion(new LoginDiferidoAccion());
		this.añadirAccion(new CambiarClaveAccion());
	}

}
