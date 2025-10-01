package com.comerzzia.bricodepot.backoffice.web.usuarios;

import javax.servlet.annotation.WebServlet;

import com.comerzzia.bricodepot.backoffice.web.usuarios.acciones.BricodepotRestablecerContrasenaAccion;
import com.comerzzia.web.usuarios.UsuariosBackofficeServlet;
import com.comerzzia.web.usuarios.acciones.AltaAccion;
import com.comerzzia.web.usuarios.acciones.BuscarAccion;
import com.comerzzia.web.usuarios.acciones.EditarAccion;
import com.comerzzia.web.usuarios.acciones.EjecutarAccion;
import com.comerzzia.web.usuarios.acciones.EliminarAccion;
import com.comerzzia.web.usuarios.acciones.LeerFormularioAccion;
import com.comerzzia.web.usuarios.acciones.SalvarAccion;
import com.comerzzia.web.usuarios.acciones.VerAccion;
import com.comerzzia.web.usuarios.acciones.VerFormularioAccion;

@WebServlet(value = "/usuariosBackoffice", description = "Servlet de Usuarios Backoffice", displayName = "UsuariosBackofficeServlet", name = "UsuariosBackofficeServlet")
public class BricodepotUsuariosBackofficeServlet extends UsuariosBackofficeServlet {

	private static final long serialVersionUID = 695895906413954221L;

	protected void loadAcciones() {
		this.añadirAccionDefault(new EjecutarAccion());
		this.añadirAccion(new BuscarAccion());
		this.añadirAccion(new AltaAccion());
		this.añadirAccion(new VerAccion());
		this.añadirAccion(new EditarAccion());
		this.añadirAccion(new EliminarAccion());
		this.añadirAccion(new SalvarAccion());
//		this.añadirAccion(new RestablecerContrasenaAccion());
		this.añadirAccion(new LeerFormularioAccion());
		this.añadirAccion(new VerFormularioAccion());
		
		this.añadirAccion(new BricodepotRestablecerContrasenaAccion());
	}
}
