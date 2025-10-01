package com.comerzzia.bricodepot.backoffice.web.fidelizacion.fidelizados;

import javax.servlet.annotation.WebServlet;

import com.comerzzia.bricodepot.backoffice.web.fidelizacion.fidelizados.acciones.BricodepotLeerFormularioAccion;
import com.comerzzia.web.base.ControladorServlet;
import com.comerzzia.web.fidelizacion.fidelizados.acciones.AltaAccion;
import com.comerzzia.web.fidelizacion.fidelizados.acciones.AltaRapidaAccion;
import com.comerzzia.web.fidelizacion.fidelizados.acciones.BuscarAccion;
import com.comerzzia.web.fidelizacion.fidelizados.acciones.ConsultarExistenciaTarjetasAccion;
import com.comerzzia.web.fidelizacion.fidelizados.acciones.EditarAccion;
import com.comerzzia.web.fidelizacion.fidelizados.acciones.EjecutarAccion;
import com.comerzzia.web.fidelizacion.fidelizados.acciones.EliminarAccion;
import com.comerzzia.web.fidelizacion.fidelizados.acciones.ExportarAccion;
import com.comerzzia.web.fidelizacion.fidelizados.acciones.GestionUsuarioAccion;
import com.comerzzia.web.fidelizacion.fidelizados.acciones.ImportarAccion;
import com.comerzzia.web.fidelizacion.fidelizados.acciones.SalvarAccion;
import com.comerzzia.web.fidelizacion.fidelizados.acciones.SalvarAltaRapidaAccion;
import com.comerzzia.web.fidelizacion.fidelizados.acciones.VerAccion;
import com.comerzzia.web.fidelizacion.fidelizados.acciones.VerFormularioAccion;

@WebServlet(value = "/fidelizados", description = "Servlet de Fidelizados", displayName = "FidelizadosServlet", name = "FidelizadosServlet")
public class BricodepotFidelizadosServlet extends ControladorServlet {

	private static final long serialVersionUID = 4868929210856252635L;

	protected void loadAcciones() {
		this.añadirAccionDefault(new EjecutarAccion());
		this.añadirAccion(new BuscarAccion());
		this.añadirAccion(new VerAccion());
		this.añadirAccion(new VerFormularioAccion());
		this.añadirAccion(new BricodepotLeerFormularioAccion()); // BRICO-459
		this.añadirAccion(new EditarAccion());
		this.añadirAccion(new AltaAccion());
		this.añadirAccion(new AltaRapidaAccion());
		this.añadirAccion(new EliminarAccion());
		this.añadirAccion(new SalvarAccion());
		this.añadirAccion(new SalvarAltaRapidaAccion());
		this.añadirAccion(new ConsultarExistenciaTarjetasAccion());
		this.añadirAccion(new ImportarAccion());
		this.añadirAccion(new GestionUsuarioAccion());
		this.añadirAccion(new ExportarAccion());
	}

}
