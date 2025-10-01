package com.comerzzia.bricodepot.backoffice.web.general.importacionmasiva;

import javax.servlet.annotation.WebServlet;

import com.comerzzia.bricodepot.backoffice.web.general.importacionmasiva.acciones.ImportarAccion;
import com.comerzzia.web.general.importacionmasiva.ImportacionMasivaServlet;
import com.comerzzia.web.general.importacionmasiva.acciones.DescargarPlantillaAccion;
import com.comerzzia.web.general.importacionmasiva.acciones.EjecutarAccion;

@WebServlet(value = "/importacionMasiva", description = "Servlet de Importación Masiva", displayName = "ImportacionMasivaServlet", name = "ImportacionMasivaServlet")
public class CustomImportacionMasivaServlet extends ImportacionMasivaServlet {

	private static final long serialVersionUID = -4387174440226382816L;

	@Override
	protected void loadAcciones() {
		añadirAccionDefault(new EjecutarAccion());
		añadirAccion(new DescargarPlantillaAccion());

		/* Custom */
		añadirAccion(new ImportarAccion());
	}

}
