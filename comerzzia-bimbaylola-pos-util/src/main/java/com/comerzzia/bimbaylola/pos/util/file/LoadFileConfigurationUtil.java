package com.comerzzia.bimbaylola.pos.util.file;

import java.net.URL;

import org.apache.log4j.Logger;

public class LoadFileConfigurationUtil {

	private static Logger log = Logger.getLogger(LoadFileConfigurationUtil.class);
	
	/**
	 * Obtiene la dirección de un archivo a partir del nombre recibido de ese nombre.
	 * @param nombreFicheroConfiguracion : Nombre del archivo para buscar.
	 * @return URL
	 */
	public URL obtenerUrlFicheroConfiguracion(String nombreFicheroConfiguracion)  {
		URL resource = Thread.currentThread().getContextClassLoader().getResource(nombreFicheroConfiguracion);
		log.debug("obtenerUrlFicheroConfiguracion() - Nombre del fichero : " + nombreFicheroConfiguracion);
		log.debug("obtenerUrlFicheroConfiguracion() - Ruta del fichero : " + resource.getPath());
		return resource;
	}
		
}
