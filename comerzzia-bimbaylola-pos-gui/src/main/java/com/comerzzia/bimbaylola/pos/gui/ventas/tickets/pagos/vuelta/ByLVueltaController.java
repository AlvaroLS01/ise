package com.comerzzia.bimbaylola.pos.gui.ventas.tickets.pagos.vuelta;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.gui.ventas.apartados.ByLApartadosController;
import com.comerzzia.pos.core.dispositivos.Dispositivos;
import com.comerzzia.pos.core.gui.componentes.botonaccion.medioPago.BotonBotoneraTextoComponent;
import com.comerzzia.pos.core.gui.componentes.botonera.BotoneraComponent;
import com.comerzzia.pos.core.gui.componentes.botonera.ConfiguracionBotonBean;
import com.comerzzia.pos.core.gui.componentes.botonera.medioPago.ConfiguracionBotonMedioPagoBean;
import com.comerzzia.pos.core.gui.exception.CargarPantallaException;
import com.comerzzia.pos.dispositivo.tarjeta.adyen.cloud.TefAdyenCloud;
import com.comerzzia.pos.gui.ventas.tickets.pagos.vuelta.VueltaController;
import com.comerzzia.pos.persistence.mediosPagos.MedioPagoBean;
import com.comerzzia.pos.services.mediospagos.MediosPagosService;

@Component
@Primary
public class ByLVueltaController extends VueltaController {

	private static final Logger log = Logger.getLogger(VueltaController.class.getName());
	public static final String CODIGO_TARJETA_PINPAD = "0010";

	@Override
	public void initializeComponents() {
		log.debug("inicializarComponentes() - Iniciamos la carga de los medios de pago...");

	}

	@Override
	public void initializeForm() {
		/*
		 * Incidencia #86 Se cambia el código del método initializeComponents a initializeForm para que cada vez que se
		 * entre en la pantalla se refresque y compruebe que, en la cancelación de apartados, no aparezca el método de
		 * pago "TARJETA PINDPAD"
		 */
		codMedioPagoSeleccionado = null;
		Boolean esCancelacion = Boolean.FALSE;
		if (getDatos().get(ByLApartadosController.CANCELACION_RESERVA) != null) {
			esCancelacion = (Boolean) getDatos().get(ByLApartadosController.CANCELACION_RESERVA);
		}

		List<ConfiguracionBotonBean> listaAccionesMP = new LinkedList<ConfiguracionBotonBean>();
		log.debug("initializeForm() - Creando acciones para botonera de pago contado");

		String codTarjetaPinpad = CODIGO_TARJETA_PINPAD;
		if (Dispositivos.getInstance().getTarjeta() != null && Dispositivos.getInstance().getTarjeta().getConfiguracion() != null) {
			Map<String, String> mapaConfiguracion = Dispositivos.getInstance().getTarjeta().getConfiguracion().getParametrosConfiguracion();
			if (mapaConfiguracion.get("PAYMENTS") != null) {
				codTarjetaPinpad = mapaConfiguracion.get("PAYMENTS");
			}
		}

		for (MedioPagoBean pag : MediosPagosService.mediosPagoVisibleVenta) {
			if (pag.getCodMedioPago().equals(codTarjetaPinpad) && esCancelacion) {

				if (Dispositivos.getInstance().getTarjeta() instanceof TefAdyenCloud) {
					log.debug("initializeForm() - Creando acciones para botonera de pago contado");
				}
				else {
					continue;
				}
			}
			ConfiguracionBotonMedioPagoBean cfg = new ConfiguracionBotonMedioPagoBean(null, pag.getDesMedioPago(), null, "ACCION_SELECIONAR_MEDIO_PAGO", "", pag);
			listaAccionesMP.add(cfg);
		}
		try {
			botoneraMediosPago = new BotoneraComponent(4, 4, this, listaAccionesMP, panelMediosPagos.getPrefWidth(), panelMediosPagos.getPrefHeight(), BotonBotoneraTextoComponent.class.getName());
			botoneraMediosPago.setBotonesFocusables(true);
			panelMediosPagos.getChildren().clear();
			panelMediosPagos.getChildren().add(botoneraMediosPago);
		}
		catch (CargarPantallaException ex) {
			String mensajeError = "Error cargando pantalla de medio de pago de devolución";
			log.error("initializeForm() - " + mensajeError);
		}

		super.initializeForm();
	}

}
