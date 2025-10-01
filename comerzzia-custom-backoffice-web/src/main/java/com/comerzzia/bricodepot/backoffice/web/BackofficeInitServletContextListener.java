package com.comerzzia.bricodepot.backoffice.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;

import com.comerzzia.bricodepot.backoffice.services.fidelizacion.colectivos.BricodepotServicioColectivosImpl;
import com.comerzzia.bricodepot.backoffice.services.fidelizacion.fidelizados.BricodepotServicioFidelizadosImpl;
import com.comerzzia.bricodepot.backoffice.services.general.clientes.BricodepotServicioClientesImpl;
import com.comerzzia.bricodepot.backoffice.services.general.importacionmasiva.CustomServicioImportacionMasivaImpl;
import com.comerzzia.bricodepot.backoffice.services.login.BricodepotServicioLoginImpl;
import com.comerzzia.bricodepot.backoffice.services.rest.usuarios.BricodepotServicioUsuarioRestImpl;
import com.comerzzia.bricodepot.backoffice.services.ventas.tickets.albaranes.BricodepotServicioAlbaranesVentasImpl;
import com.comerzzia.core.servicios.login.ServicioLoginImpl;
import com.comerzzia.servicios.fidelizacion.colectivos.ServicioColectivosImpl;
import com.comerzzia.servicios.fidelizacion.fidelizados.ServicioFidelizadosImpl;
import com.comerzzia.servicios.general.clientes.ServicioClientesImpl;
import com.comerzzia.servicios.general.importacionmasiva.ServicioImportacionMasivaImpl;
import com.comerzzia.servicios.rest.usuarios.ServicioUsuarioRestImpl;
import com.comerzzia.servicios.ventas.albaranes.ServicioAlbaranesVentasImpl;

@WebListener
public class BackofficeInitServletContextListener extends com.comerzzia.web.listeners.BackofficeInitServletContextListener {

	protected static Logger log = Logger.getLogger(BackofficeInitServletContextListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		log.info("Configuring custom services");

		super.contextInitialized(sce);

		// set custom services instances for static classes
		// ComerzziaStandarServiceImpl.setCustomInstance(new MyCustomServiceImpl());

		ServicioImportacionMasivaImpl.setCustomInstance(new CustomServicioImportacionMasivaImpl());
		ServicioColectivosImpl.setCustomInstance(new BricodepotServicioColectivosImpl());
		ServicioFidelizadosImpl.setCustomInstance(new BricodepotServicioFidelizadosImpl());
		ServicioUsuarioRestImpl.setCustomInstance(new BricodepotServicioUsuarioRestImpl());
		ServicioLoginImpl.setCustomInstance(new BricodepotServicioLoginImpl());
		ServicioAlbaranesVentasImpl.setCustomInstance(new BricodepotServicioAlbaranesVentasImpl());
		ServicioClientesImpl.setCustomInstance(new BricodepotServicioClientesImpl());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
