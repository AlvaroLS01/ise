package com.comerzzia.bricodepot.backoffice.admin.core.login;

import javax.servlet.annotation.WebServlet;

import com.comerzzia.bricodepot.backoffice.admin.core.login.accciones.BricodepotLoginAction;
import com.comerzzia.bricodepot.backoffice.admin.core.login.accciones.CambiarClaveAccion;
import com.comerzzia.web.core.filtros.ExcludeSessionFilter;
import com.comerzzia.web.core.login.LoginServlet;
import com.comerzzia.web.core.login.acciones.CargarEmpresasLoginAccion;
import com.comerzzia.web.core.login.acciones.LoginDiferidoAccion;
import com.comerzzia.web.core.login.acciones.LogoutAccion;

//BO-ADMIN
@ExcludeSessionFilter
@WebServlet(value = "/login", description = "Servlet de Login", displayName = "LoginServlet", name = "LoginServlet")
public class BricodepotLoginServlet extends LoginServlet {

	private static final long serialVersionUID = 3894363051958972553L;

	@Override
	protected void loadAcciones() {
		this.añadirAccionDefault(new LogoutAccion());
		this.añadirAccion(new BricodepotLoginAction()); // BRICO-326
		this.añadirAccion(new CargarEmpresasLoginAccion());
		this.añadirAccion(new LoginDiferidoAccion());
		this.añadirAccion(new CambiarClaveAccion()); // BRICO-326
	}

}
