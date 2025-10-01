package com.comerzzia.bricodepot.backoffice.admin;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;

import com.comerzzia.bricodepot.backoffice.services.general.tiendas.BricodepotServicioTiendasImpl;
import com.comerzzia.bricodepot.backoffice.services.login.BricodepotServicioLoginImpl;
import com.comerzzia.bricodepot.backoffice.services.sincronizador.datos.CustomServicioDatosSincronizacion;
import com.comerzzia.bricodepot.backoffice.services.ventas.tickets.albaranes.BricodepotServicioAlbaranesVentasImpl;
import com.comerzzia.bricodepot.backoffice.services.ventas.tickets.albaranes.pagos.CustomServicioPagosAlbaranesVentasImpl;
import com.comerzzia.core.servicios.login.ServicioLoginImpl;
import com.comerzzia.servicios.general.tiendas.ServicioTiendasImpl;
import com.comerzzia.servicios.sincronizacion.datos.ServicioDatosSincronizacion;
import com.comerzzia.servicios.ventas.albaranes.ServicioAlbaranesVentasImpl;

@WebListener
public class AdminInitServletContextListener extends com.comerzzia.web.listeners.BackofficeInitServletContextListener {

	protected static Logger log = Logger.getLogger(AdminInitServletContextListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		log.info("Configuring custom services");

		super.contextInitialized(sce);
		ServicioDatosSincronizacion.setCustomInstance(new CustomServicioDatosSincronizacion());
		ServicioTiendasImpl.setCustomInstance(new BricodepotServicioTiendasImpl());
		CustomServicioPagosAlbaranesVentasImpl.setCustomInstance(new CustomServicioPagosAlbaranesVentasImpl());
		ServicioLoginImpl.setCustomInstance(new BricodepotServicioLoginImpl());
		ServicioAlbaranesVentasImpl.setCustomInstance(new BricodepotServicioAlbaranesVentasImpl());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
