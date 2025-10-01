package com.comerzzia.bricodepot.backoffice.web.fidelizacion.colectivos;

import javax.servlet.annotation.WebServlet;

import com.comerzzia.bricodepot.backoffice.web.fidelizacion.colectivos.acciones.BricodepotSalvarAccion;
import com.comerzzia.web.fidelizacion.colectivos.ColectivosServlet;
import com.comerzzia.web.fidelizacion.colectivos.acciones.AltaAccion;
import com.comerzzia.web.fidelizacion.colectivos.acciones.AsignarEtiquetasAccion;
import com.comerzzia.web.fidelizacion.colectivos.acciones.BuscarAccion;
import com.comerzzia.web.fidelizacion.colectivos.acciones.DuplicarAccion;
import com.comerzzia.web.fidelizacion.colectivos.acciones.EditarAccion;
import com.comerzzia.web.fidelizacion.colectivos.acciones.EjecutarAccion;
import com.comerzzia.web.fidelizacion.colectivos.acciones.EliminarAccion;
import com.comerzzia.web.fidelizacion.colectivos.acciones.ImportarAccion;
import com.comerzzia.web.fidelizacion.colectivos.acciones.LeerFormularioAccion;
import com.comerzzia.web.fidelizacion.colectivos.acciones.VerAccion;
import com.comerzzia.web.fidelizacion.colectivos.acciones.VerFormularioAccion;

@WebServlet(value = "/colectivos", description = "Servlet de Colectivos", displayName = "ColectivosServlet", name = "ColectivosServlet")
public class CustomColectivosServlet extends ColectivosServlet {

	private static final long serialVersionUID = -8617860792419666921L;
	
	protected void loadAcciones() {
		añadirAccionDefault(new EjecutarAccion());
		añadirAccion(new BuscarAccion());
		añadirAccion(new AltaAccion());
		añadirAccion(new VerFormularioAccion());
		añadirAccion(new LeerFormularioAccion());
		añadirAccion(new BricodepotSalvarAccion());
		añadirAccion(new EditarAccion());
		añadirAccion(new EliminarAccion());
		añadirAccion(new VerAccion());
		añadirAccion(new ImportarAccion());
		añadirAccion(new AsignarEtiquetasAccion());
		añadirAccion(new DuplicarAccion());
	}

}
