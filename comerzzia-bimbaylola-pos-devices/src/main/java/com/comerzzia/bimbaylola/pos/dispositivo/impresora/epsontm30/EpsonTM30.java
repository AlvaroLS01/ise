package com.comerzzia.bimbaylola.pos.dispositivo.impresora.epsontm30;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.comerzzia.bimbaylola.pos.dispositivo.impresora.epsontm30.gui.ConfiguracionTM30View;
import com.comerzzia.pos.core.gui.POSApplication;
import com.comerzzia.pos.dispositivo.impresora.ImpresoraDriver;

import javafx.stage.Stage;

public class EpsonTM30 extends ImpresoraDriver {

	public static final String PARAMETRO_SALIDA_CONFIGURACION = "salida_configuracion";
	public static final String EPSON_IP = "IP";
	public static final String NOMBRE_CONEXION_TSE = "TSE";

	private static Logger log = Logger.getLogger(EpsonTM30.class);

	@SuppressWarnings("unchecked")
	public void configurar(Stage stage) {
		log.debug("configurar() - Abrienda pantalla de configuración de EpsonTM30 ...");
		super.configurar(stage);

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(PARAMETRO_SALIDA_CONFIGURACION, getConfiguracion().getParametrosConfiguracion());

		POSApplication.getInstance().getMainView().showModalCentered(ConfiguracionTM30View.class, params, stage);

		/*
		 * Guardamos en sesión los datos de configuración, realmente no están guardado del todo hasta que no se Acepta
		 * los cambios en la pantalla de "Configuración TPV"
		 */
		if (params.containsKey(PARAMETRO_SALIDA_CONFIGURACION)) {
			Map<String, String> parametrosConfiguracion = (Map<String, String>) params.get(PARAMETRO_SALIDA_CONFIGURACION);
			getConfiguracion().setParametrosConfiguracion(parametrosConfiguracion);
			log.debug("configurar() - Parámetros de configuración de EpsonTM30 nuevos : " + parametrosConfiguracion);
		}
	}

	@Override
	public boolean isConfigurable() {
		if (StringUtils.isNotBlank(getConfiguracion().getNombreConexion()) && getConfiguracion().getNombreConexion().equals(NOMBRE_CONEXION_TSE)) {
			return true;
		}

		return false;
	}
}
