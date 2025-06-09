package com.comerzzia.instoreengine.herbnav.documentos.impresionetiquetas;

import javax.servlet.annotation.WebServlet;

import com.comerzzia.instoreengine.herbnav.documentos.impresionetiquetas.acciones.HNAEjecutarAccion;
import com.comerzzia.instoreengine.herbnav.documentos.impresionetiquetas.acciones.HNALeerFormularioAccion;
import com.comerzzia.instoreengine.web.documentos.impresionetiquetas.acciones.ImprimirManualAccion;
import com.comerzzia.instoreengine.web.documentos.impresionetiquetas.acciones.ImprimirPendientesAccion;
import com.comerzzia.web.base.ControladorServlet;

@SuppressWarnings("serial")
@WebServlet(value = "/impresionEtiquetas", description = "Servlet de Impresión de Etiquetas", displayName = "ImpresionEtiquetasServlet", name = "ImpresionEtiquetasServlet")
public class HNAImpresionEtiquetasServlet extends ControladorServlet{

	@Override
	protected void loadAcciones() {
		añadirAccionDefault(new HNAEjecutarAccion());
		añadirAccion(new ImprimirPendientesAccion());
		añadirAccion(new ImprimirManualAccion());
		añadirAccion(new HNALeerFormularioAccion());
		
	}

}
