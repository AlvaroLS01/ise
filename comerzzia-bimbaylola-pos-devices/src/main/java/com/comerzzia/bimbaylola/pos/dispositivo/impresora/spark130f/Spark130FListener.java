package com.comerzzia.bimbaylola.pos.dispositivo.impresora.spark130f;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.services.spark130f.Spark130FService;
import com.comerzzia.pos.core.dispositivos.Dispositivos;
import com.comerzzia.pos.core.dispositivos.configuracion.ConfiguracionDispositivo;
import com.comerzzia.pos.core.gui.ApplicationListener;

@Component
public class Spark130FListener implements ApplicationListener {

	@Autowired
	private Spark130FService spark130FService;

	private static final Logger log = Logger.getLogger(Spark130FListener.class.getName());

	@Override
	public void onBeforeApplicationInit() {

	}

	@Override
	public void onAfterApplicationInit() {

	}

	@Override
	public void onBeforeApplicationStart() {

	}

	@Override
	public void onAfterApplicationStart() {
		if (Dispositivos.getInstance().getImpresora1() instanceof Spark130F) {
			if (Dispositivos.getInstance().getImpresora1().getConfiguracion().getNombreConexion().equals(Spark130F.NOMBRE_CONEXION_FISCAL_PRINTER)) {
				try {
					ConfiguracionDispositivo impresora = Dispositivos.getInstance().getImpresora1().getConfiguracion();
					String ip = impresora.getParametrosConfiguracion().get("IP");
					if (!StringUtils.isBlank(ip)) {
						spark130FService.realizarLlamada("!spark_init");
					}
				}
				catch (Exception e) {
					log.error("onBeforeApplicationInit() - Ha ocurrido un error al realizar el proceso inicial del TSE", e);
				}
			}
		}
	}

	@Override
	public void onBeforeApplicationClose() {
	}

}
