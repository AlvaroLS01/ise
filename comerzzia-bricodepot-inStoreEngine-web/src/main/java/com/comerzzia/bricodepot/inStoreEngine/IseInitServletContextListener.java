package com.comerzzia.bricodepot.inStoreEngine;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.comerzzia.bricodepot.inStoreEngine.services.ventas.cajas.cabecera.BricodepotServicioCabeceraCajasImpl;
import com.comerzzia.servicios.ventas.cajas.cabecera.ServicioCabeceraCajasImpl;

public class IseInitServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServicioCabeceraCajasImpl.setCustomInstance(new BricodepotServicioCabeceraCajasImpl());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
