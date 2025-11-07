package com.comerzzia.bimbaylola.pos.gui.ventas.cajas.cierre;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.devices.impresoras.fiscal.IFiscalPrinter;
import com.comerzzia.bimbaylola.pos.dispositivo.impresora.epsontm30.EpsonTM30;
import com.comerzzia.bimbaylola.pos.dispositivo.impresora.fiscal.polonia.PoloniaFiscalPrinter;
import com.comerzzia.bimbaylola.pos.dispositivo.impresora.spark130f.Spark130F;
import com.comerzzia.bimbaylola.pos.services.epsontse.EpsonTSEService;
import com.comerzzia.bimbaylola.pos.services.spark130f.Spark130FConstants;
import com.comerzzia.bimbaylola.pos.services.spark130f.Spark130FService;
import com.comerzzia.bimbaylola.pos.services.spark130f.exception.Spark130FException;
import com.comerzzia.bimbaylola.pos.services.ticket.aparcados.ByLTicketsAparcadosService;
import com.comerzzia.pos.core.dispositivos.Dispositivos;
import com.comerzzia.pos.core.dispositivos.dispositivo.impresora.IPrinter;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.exception.ValidationException;
import com.comerzzia.pos.gui.inicio.InicioView;
import com.comerzzia.pos.gui.ventas.cajas.CajasView;
import com.comerzzia.pos.gui.ventas.cajas.cierre.CierreCajaController;
import com.comerzzia.pos.gui.ventas.cajas.cierre.CierreCajaView;
import com.comerzzia.pos.services.cajas.CajasServiceException;
import com.comerzzia.pos.services.core.variables.VariablesServices;
import com.comerzzia.pos.util.format.FormatUtil;
import com.comerzzia.pos.util.i18n.I18N;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

@Component
@Primary
public class ByLCierreCajaController extends CierreCajaController {

	private static final Logger log = Logger.getLogger(ByLCierreCajaController.class.getName());

	@Autowired
	protected ByLTicketsAparcadosService tickerAparcadosService;
	@Autowired
	protected VariablesServices variablesServices;
	@Autowired
	private EpsonTSEService epsonTSEService;
	@Autowired
	private Spark130FService spark130FService;

	@FXML
	protected Label lbVentaNeta;

	@FXML
	protected TextField tfVentaNeta;

	@Override
	protected void refrescarDatosPantalla() {
		super.refrescarDatosPantalla();

		BigDecimal ventasEntradas = cajaSesion.getCajaAbierta().getTotalVentasEntrada();
		BigDecimal salidasDevolucion = cajaSesion.getCajaAbierta().getTotalVentasSalida();
		BigDecimal ventaNeta = ventasEntradas.subtract(salidasDevolucion);

		tfVentaNeta.setText(FormatUtil.getInstance().formateaNumero(ventaNeta, 2));
	}

	/**
	 * Inicia la pantalla para abrir un recuento, previamente comprobamos que no tiene ningún ticket aparcado, en caso
	 * de que lo tenga no permitimos que continúe.
	 */
	protected void abrirRecuentoCaja() throws CajasServiceException {
		/* Comprobamos que no tiene tickets aparcados. */
		if (tickerAparcadosService.countTicketsAparcados(null) == 0) {
			super.abrirRecuentoCaja();
		}
		else {
			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Existen tickets pendientes de confirmar. " + "Antes debería finalizar la operación."), getStage());
			return;
		}

	}

	/**
	 * Produce el cierre de la caja, antes de cerrar comprobamos que no tiene tickets aparcados, en ese caso no
	 * permitimos continuar.
	 */
	@SuppressWarnings("unchecked")
	protected void accionCierreCaja() {
		/* Comprobamos que no tiene tickets aparcados. */
		if (tickerAparcadosService.countTicketsAparcados(null) == 0) {
			log.debug("accionCierreCaja()");
			try {
				if (getApplication().getMainView().getSubViews().size() > 2) {
					if (!VentanaDialogoComponent.crearVentanaConfirmacion(I18N.getTexto("Para cerrar la caja se deben cerrar todas las pantallas abiertas. ¿Desea continuar?"), getStage())) {
						return;
					}
					boolean couldClose = getApplication().getMainView().closeAllViewsExcept(InicioView.class, CajasView.class, CierreCajaView.class);
					if (!couldClose) {
						return;
					}
				}

				boolean tieneDescuadres = cajaSesion.tieneDescuadres();
				Integer reintentosMax = variablesServices.getVariableAsInteger(VariablesServices.CAJA_REINTENTOS_CIERRE);

				if (tieneDescuadres) {
					if (reintentosCierre == null) {
						reintentosCierre = 0;
					}
					Integer reintentosRestantes = reintentosMax - reintentosCierre;
					if (reintentosRestantes > 0) {
						VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Caja descuadrada con un importe mayor que el permitido. Revise recuento."), getStage());
						reintentosCierre++;
						return;
					}
					else {
						if (!VentanaDialogoComponent.crearVentanaConfirmacion(I18N.getTexto("Se va a cerrar la caja con descuadres mayores al valor permitido, ¿Desea continuar?"), getStage())) {
							return;
						}
					}
				}
				if (!VentanaDialogoComponent.crearVentanaConfirmacion(I18N.getTexto("¿Seguro de realizar el Cierre?"), getStage())) {
					return;
				}

				
				if(!cierreImpresorasFiscal()) {
					return;
				}
				
				// Procedemos al cierre de la caja
				// Actualizamos el formulario
				formularioCierreCaja.setFechaCierre(tfFechaCierre.getTexto());
				// validamos el formulario
				accionValidarForm();

				cajaSesion.guardarCierreCaja(formularioCierreCaja.getDateCierre());

				try {
					imprimirCierre(cajaSesion.getCajaAbierta());
				}
				catch (CajasServiceException ex) {
					log.error("accionCierreCaja() - No se pudo realizar la impresión del cierre de caja. ");
					VentanaDialogoComponent.crearVentanaError(getStage(), ex.getMessageI18N(), ex);
				}
				cajaSesion.cerrarCaja();
				reintentosCierre = 0;
				if (tieneDescuadres) {
					VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Caja cerrada con descuadres"), getStage());
				}

				IPrinter printer = Dispositivos.getInstance().getImpresora1();
				if (printer instanceof IFiscalPrinter) {
					log.debug("accionCierreCaja() - Impresión con Impresora Fiscal de Polonia");

					((PoloniaFiscalPrinter) printer).informeDiarioFiscal();
				}
				
				getStage().close();

			}
			catch (ValidationException ex) {
				log.debug("accionCierreCaja() - La validación no fué exitosa"); // La validación ya se encarga de
				                                                                // mostrar el error
			}
			catch (CajasServiceException e) {
				log.error("accionCierreCaja() - Error al tratar de realizar cierre de caja: " + e.getCause(), e);
				VentanaDialogoComponent.crearVentanaError(getStage(), e.getMessageI18N(), e);
			}
			catch (Spark130FException e) {
				log.error("accionCierreCaja() - Error al tratar de realizar cierre de caja: " + e.getCause(), e);
				VentanaDialogoComponent.crearVentanaError(getStage(), e.getMessage(), e);
			}
			catch (Exception e) {
				log.error("accionCierreCaja() - Error al tratar de realizar cierre de caja: " + e.getCause(), e);
				VentanaDialogoComponent.crearVentanaError(getStage(), e);
			}
		}
		else {
			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Existen tickets pendientes de confirmar. " + "Antes debería finalizar la operación."), getStage());
			return;
		}

	}

	private String tseCierreCaja(BigDecimal saldo) {
		String respuesta = null;
		saldo = saldo.setScale(2, BigDecimal.ROUND_HALF_UP);
		try {
			if (Dispositivos.getInstance().getImpresora1().getConfiguracion().getNombreConexion().equals(EpsonTSEService.NOMBRE_CONEXION_TSE)) {
				String peticionStartTransaction = epsonTSEService.startTransaction(EpsonTSEService.PROCESSTYPE_SONSTINGERVORGANG, EpsonTSEService.TIPO_TRANSACCION_TAGESENDE, saldo, null, null, true);
				epsonTSEService.enviarPeticion(peticionStartTransaction);
				String respuestaStartTransaction = epsonTSEService.lecturaSocket();

				respuesta = epsonTSEService.tratamientoRespuestaResult(respuestaStartTransaction);
				if (respuesta.equals(EpsonTSEService.EXECUTION_OK)) {
					List<String> listaCampos = new ArrayList<String>();
					listaCampos.add("transactionNumber");
					HashMap<String, String> mapaCampos = epsonTSEService.tratamientoRespuesta(respuestaStartTransaction, listaCampos);
					String transactionNumber = mapaCampos.get("transactionNumber");

					String peticionFinishTransaction = epsonTSEService.finishTransaction(EpsonTSEService.PROCESSTYPE_SONSTINGERVORGANG, EpsonTSEService.TIPO_TRANSACCION_TAGESENDE, saldo.abs(), null,
					        null, Integer.parseInt(transactionNumber), false);
					epsonTSEService.enviarPeticion(peticionFinishTransaction);
					String respuestaFinishTransaction = epsonTSEService.lecturaSocket();
					respuesta = epsonTSEService.tratamientoRespuestaResult(respuestaFinishTransaction);
				}

				try {
					epsonTSEService.desconectaTSE();
				}
				catch (IOException e) {
					log.error("accionCierreCaja() - Error al cerrar el socket del TSE: " + e.getCause(), e);
				}

			}
		}
		catch (Exception e) {
			log.error("tseCierreCaja() - Error al realizar el proceso de cierre de caja -" + e.getMessage());
		}

		return respuesta;
	}
	
	private Boolean cierreImpresorasFiscal() throws Exception{
		/* Impresora TM-M30 TSE */
		if (Dispositivos.getInstance().getImpresora1() instanceof EpsonTM30) {
			String respuesta = tseCierreCaja(cajaSesion.getCajaAbierta().getTotalRecuento());
			if (respuesta != null && !respuesta.equals(EpsonTSEService.EXECUTION_OK)) {
				if (respuesta.equals(EpsonTSEService.TSE1_ERROR_WRONG_STATE_NEEDS_SELF_TEST)) {
					VentanaDialogoComponent.crearVentanaError(I18N.getTexto("El estado del TSE es erróneo, por favor, reinicie la Impresora y Comerzzia para poder operar con TSE."), getStage());
					return false;
				}
				else {
					if (!VentanaDialogoComponent.crearVentanaConfirmacion(I18N.getTexto("Ha ocurrido un error con el TSE y no se ha podido enviar la operación, ¿Desea continuar?."), getStage())) {
						return false;
					}
				}
			}
		}

		/* Impresora Spark130F */
		if (Dispositivos.getInstance().getImpresora1() instanceof Spark130F) {
			List<String> listaCampos = new ArrayList<String>();
			listaCampos.add("RC");
			listaCampos.add("FD");
			listaCampos.add("FP");

			HashMap<String, String> mapaCampos = spark130FService.getCamposRespuesta(spark130FService.realizarLlamada(spark130FService.closeShift()), listaCampos);
			String returnCode = mapaCampos.get("RC");

			/* El returnCode 21 indica que ya está cerrada la caja */
			if (!returnCode.equals(Spark130FConstants.NO_ERROR) && !returnCode.equals("21")) {
				Map<String, String> mapaErrores = Spark130FConstants.setErrors();
				throw new Spark130FException(mapaErrores.get(returnCode));
			}
		}

		return true;
	}

}
