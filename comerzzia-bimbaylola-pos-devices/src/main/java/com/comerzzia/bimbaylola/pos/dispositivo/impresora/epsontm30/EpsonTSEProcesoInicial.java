package com.comerzzia.bimbaylola.pos.dispositivo.impresora.epsontm30;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.services.epsontse.EpsonTSEService;
import com.comerzzia.pos.core.dispositivos.Dispositivos;
import com.comerzzia.pos.core.dispositivos.configuracion.ConfiguracionDispositivo;
import com.comerzzia.pos.core.gui.ApplicationListener;

@Component
public class EpsonTSEProcesoInicial implements ApplicationListener {

	@Autowired
	private EpsonTSEService epsonTSEService;

	private static final Logger log = Logger.getLogger(EpsonTSEProcesoInicial.class.getName());

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
		if (Dispositivos.getInstance().getImpresora1() instanceof EpsonTM30) {
			if (Dispositivos.getInstance().getImpresora1().getConfiguracion().getNombreConexion().equals(EpsonTSEService.NOMBRE_CONEXION_TSE)) {
				try {
					ConfiguracionDispositivo impresora = Dispositivos.getInstance().getImpresora1().getConfiguracion();
					String ip = impresora.getParametrosConfiguracion().get("IP");
					if (!StringUtils.isBlank(ip)) {
						epsonTSEService.procesoInicialTSE(ip);

						compruebaAutoTest();

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
		if (Dispositivos.getInstance().getImpresora1() instanceof EpsonTM30) {
			if (Dispositivos.getInstance().getImpresora1().getConfiguracion().getNombreConexion().equals(EpsonTSEService.NOMBRE_CONEXION_TSE)) {
				try {
					epsonTSEService.desconectaTSE();
				}
				catch (IOException e) {
					log.error("onBeforeApplicationClose() - Ha ocurrido un error al realizar el proceso final del TSE", e);
				}
			}
		}

	}

	private void compruebaAutoTest() throws Exception {
		log.debug("compruebaAutoTest() - Inicio de comprobacion del status del TSE");
		if (epsonTSEService.socketConectado()) {
			List<String> listaCampos = new ArrayList<String>();
			listaCampos.add("timeUntilNextSelfTest");
			epsonTSEService.enviarPeticion(epsonTSEService.getGetStorageInfo());

			String respuestaGetStorageInfo = epsonTSEService.lecturaSocket();
			HashMap<String, String> mapaCampos = epsonTSEService.tratamientoRespuestaGetStorageInfo(respuestaGetStorageInfo, listaCampos);
			String timeUntilNextSelfTest = mapaCampos.get("timeUntilNextSelfTest");

			if (StringUtils.isNotBlank(timeUntilNextSelfTest)) {
				log.debug("compruebaAutoTest() - Se ha recuperado la informacion de GetStorageInfo - timeUntilNextSelfTest [" + timeUntilNextSelfTest + "]");
				if (Integer.parseInt(timeUntilNextSelfTest) == 0) {
					epsonTSEService.autoTest();
				}
			}

		}
		else {
			log.warn("compruebaAutoTest() - Socket no conectado");
		}
	}
}
