package com.comerzzia.bricodepot.backoffice.web.fidelizacion.fidelizados;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.apache.log4j.Logger;

import com.comerzzia.core.servicios.acciones.AccionException;
import com.comerzzia.core.servicios.acciones.ServicioAccionesImpl;
import com.comerzzia.web.fidelizacion.fidelizados.ExportarFidelizadosServlet;

@WebServlet(value = "/exportarFidelizados", description = "Servlet de exportacion de los fidelizados", displayName = "ExportarFidelizadosServlet", name = "ExportarFidelizadosServlet")
public class BricodepotExportarFidelizadosServlet extends ExportarFidelizadosServlet {

	private static final long serialVersionUID = -6271061757748911225L;

	protected static Logger log = Logger.getLogger(BricodepotExportarFidelizadosServlet.class);

	@Override
	@SuppressWarnings("deprecation")
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		// Obtenemos la información de la acción de menú asociada al servlet para controlar los permisos
		try {
			accionMenu = ServicioAccionesImpl.get().obtenerAccion(BricodepotFidelizadosServlet.class.getCanonicalName());
		}
		catch (AccionException e) {
			log.error("Error al obtener la acción asociada a " + this.getClass().getCanonicalName());
		}

	}

}
