package com.comerzzia.bricodepot.backoffice.web.fidelizacion.colectivos;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.apache.log4j.Logger;

import com.comerzzia.core.servicios.acciones.AccionException;
import com.comerzzia.core.servicios.acciones.ServicioAccionesImpl;
import com.comerzzia.web.fidelizacion.colectivos.ExportarColectivoServlet;

@WebServlet(value = "/exportarColectivo", description = "Servlet de exportacion de los fidelizados de un colectivo", displayName = "ExportarColectivoServlet", name = "ExportarColectivoServlet")
public class BricodepotExportarColectivoServlet extends ExportarColectivoServlet {

	private static final long serialVersionUID = 7620247016165776014L;

	protected static Logger log = Logger.getLogger(BricodepotExportarColectivoServlet.class);

	@Override
	@SuppressWarnings("deprecation")
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		// Obtenemos la información de la acción de menú asociada al servlet para controlar los permisos
		try {
			accionMenu = ServicioAccionesImpl.get().obtenerAccion(BricodepotExportarColectivoServlet.class.getCanonicalName());
		}
		catch (AccionException e) {
			log.error("Error al obtener la acción asociada a " + this.getClass().getCanonicalName());
		}

	}
}
