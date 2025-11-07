package com.comerzzia.bimbaylola.pos.gui.ventas.articulos.cajas.apertura;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.dispositivo.impresora.epsontm30.EpsonTM30;
import com.comerzzia.bimbaylola.pos.dispositivo.impresora.spark130f.Spark130F;
import com.comerzzia.bimbaylola.pos.services.cajas.ByLCajasService;
import com.comerzzia.bimbaylola.pos.services.epsontse.EpsonTSEService;
import com.comerzzia.bimbaylola.pos.services.spark130f.Spark130FConstants;
import com.comerzzia.bimbaylola.pos.services.spark130f.Spark130FService;
import com.comerzzia.bimbaylola.pos.services.spark130f.exception.Spark130FException;
import com.comerzzia.pos.core.dispositivos.Dispositivos;
import com.comerzzia.pos.core.dispositivos.configuracion.ConfiguracionDispositivo;
import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.gui.ventas.cajas.apertura.AperturaCajaController;
import com.comerzzia.pos.gui.ventas.cajas.apertura.AperturaCajaFormularioBean;
import com.comerzzia.pos.persistence.cajas.recuentos.CajaLineaRecuentoBean;
import com.comerzzia.pos.services.cajas.Caja;
import com.comerzzia.pos.services.cajas.CajaEstadoException;
import com.comerzzia.pos.services.cajas.CajasServiceException;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.services.core.sesion.SesionCaja;
import com.comerzzia.pos.util.format.FormatUtil;
import com.comerzzia.pos.util.i18n.I18N;

import javafx.fxml.FXML;

@Component
@Primary
public class ByLAperturaCajaController extends AperturaCajaController {

	private static final Logger log = Logger.getLogger(AperturaCajaController.class.getName());

	public static final String COD_MP_EFECTIVO = "0000";
	public static final String CODIGO_PAIS_CO = "CO";

	@Autowired
	private SesionCaja sesionCaja;
	
	@Autowired
	private EpsonTSEService epsonTSEService;
	
	@Autowired
	private Spark130FService spark130FService;
	
	@Autowired
	private ByLCajasService cajasService;
	
	@Autowired
	private Sesion sesion;

	protected boolean estamosEnColombia;

	@Override
	public void initializeForm() throws InitializeGuiException {

		super.initializeForm();

		/* Insertamos el ultimo recuento de la caja anterior. */
		BigDecimal saldo = BigDecimal.valueOf(getRecuentoUltimoCierre());
		saldo = saldo.setScale(2, BigDecimal.ROUND_HALF_UP);

		String saldoFormateado = FormatUtil.getInstance().formateaImporte(saldo);

		tfSaldo.setText(saldoFormateado);
		/* Bloqueamos para que no se pueda cambiar el valor del recuento. */
		tfSaldo.setDisable(true);
		btContarSaldo.setDisable(true);

	}

	/**
	 * Devuelve el valor total del recuento de la última caja que se cerró.
	 * 
	 * @return total : Total de la suma de los recuentos de la caja.
	 */
	public double getRecuentoUltimoCierre() {

		double total = 0.0;
		Caja ultimaCaja = null;

		try {

			ultimaCaja = cajasService.consultarUltimaCajaCerrada();
			cajasService.consultarRecuento(ultimaCaja);

			/* Recorremos las lineas de recuento y las sumamos. */
			for (CajaLineaRecuentoBean recuento : ultimaCaja.getLineasRecuento()) {
				/* Solo se tendrán en cuenta los pagos efectivos. */
				if (recuento.getCodMedioPago().equals(COD_MP_EFECTIVO)) {
					/* La cantidad indica el número de billetes, y el value muestra el billete que es. */
					total = total + (recuento.getCantidad().doubleValue() * recuento.getValor().doubleValue());
				}
			}

		}
		catch (CajasServiceException e) {
			log.error("Error recuperando el último cierre", e);
			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Error recuperando el recuento del último cierre de caja."), this.getStage());
		}
		catch (CajaEstadoException e) {
			VentanaDialogoComponent.crearVentanaAviso(e.getMessage(), this.getStage());
		}

		return total;

	}

	/**
	 * Acción aceptar que realiza algunas operaciones para abrir la caja con el saldo inicial.
	 */
	@FXML
	public void accionAceptar() {

		log.debug("accionAceptar()");
		estamosEnColombia = false;

		try {
			/* Validar formulario */
			if (sesion.getAplicacion().getTienda().getCliente().getCodpais().equals(CODIGO_PAIS_CO)) {
				estamosEnColombia = true;
			}
			else {
				formularioAperturaGui = new AperturaCajaFormularioBean(tfFecha.getTexto(), tfSaldo.getText());
			}
			if (!accionValidarForm()) {
				return;
			}

			Date fechaApertura = FormatUtil.getInstance().desformateaFechaHora(tfFecha.getTexto(), true);

			Calendar calendarHoy = Calendar.getInstance();
			calendarHoy.set(Calendar.HOUR_OF_DAY, 0);
			calendarHoy.set(Calendar.MINUTE, 0);
			calendarHoy.set(Calendar.SECOND, 0);
			calendarHoy.set(Calendar.MILLISECOND, 0);

			/* Si la fecha de apertura no es hoy, la ponemos sin hora. */
			Calendar calendarAperturaSinHora = Calendar.getInstance();
			calendarAperturaSinHora.setTime(fechaApertura);
			calendarAperturaSinHora.set(Calendar.HOUR_OF_DAY, 0);
			calendarAperturaSinHora.set(Calendar.MINUTE, 0);
			calendarAperturaSinHora.set(Calendar.SECOND, 0);
			calendarAperturaSinHora.set(Calendar.MILLISECOND, 0);
			if (!calendarAperturaSinHora.equals(calendarHoy)) {
				fechaApertura = calendarAperturaSinHora.getTime();
			}

			/*
			 * Tratamos el número para que la conversión a BigDecimal no trate mal los puntos y pongan el número más
			 * grande.
			 */
			String saldo = estamosEnColombia ? tfSaldo.getText() : formularioAperturaGui.getSaldo();

			
			BigDecimal saldoBigDecimal = FormatUtil.getInstance().desformateaImporte(saldo);
			saldoBigDecimal = saldoBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);

			if(!aperturaImpresorasFiscal(saldoBigDecimal)) {
				return;
			}
			
			// cajaSesion.abrirCajaManual(fechaApertura, formularioAperturaGui.getSaldoAsBigDecimal());
			sesionCaja.abrirCajaManual(fechaApertura, saldoBigDecimal);

			// [BYL-295] - MENSAJE DE APERTURA DE CAJA
			cajasService.crearDocumentoAperturaCaja();

			getStage().close();
		}
		catch (CajasServiceException | CajaEstadoException e) {
			log.error("accionAceptar() - Error al tratar de realizar apertura de caja: " + e.getCause(), e);
			VentanaDialogoComponent.crearVentanaError(getStage(), e.getMessageI18N(), e);
		}
		catch (Spark130FException e) {
			log.error("accionAceptar() - Error al tratar de realizar apertura de caja: " + e.getCause(), e);
			VentanaDialogoComponent.crearVentanaError(getStage(), e.getMessage(), e);
		}
		catch (Exception e) {
			log.error("accionAceptar() - Error al tratar de realizar apertura de caja: " + e.getCause(), e);
			VentanaDialogoComponent.crearVentanaError(getStage(), e);
		}
	}

	private String tseAperturaCaja(BigDecimal saldo) {
		String respuesta = null;
		try {
			if (Dispositivos.getInstance().getImpresora1().getConfiguracion().getNombreConexion().equals(EpsonTSEService.NOMBRE_CONEXION_TSE)) {
				if (!epsonTSEService.socketConectado()) {
					try {
						ConfiguracionDispositivo impresora = Dispositivos.getInstance().getImpresora1().getConfiguracion();
						String ip = impresora.getParametrosConfiguracion().get("IP");
						if (!StringUtils.isBlank(ip)) {
							epsonTSEService.procesoInicialTSE(ip);
						}
					}
					catch (Exception e) {
						log.error("accionAceptar() - Ha ocurrido un error al realizar el proceso inicial del TSE", e);
					}
				}

				String peticionStartTransaction = epsonTSEService.startTransaction(EpsonTSEService.PROCESSTYPE_SONSTINGERVORGANG, EpsonTSEService.TIPO_TRANSACCION_TAGESSTART, saldo, null, null, true);
				epsonTSEService.enviarPeticion(peticionStartTransaction);
				String respuestaStartTransaction = epsonTSEService.lecturaSocket();

				respuesta = epsonTSEService.tratamientoRespuestaResult(respuestaStartTransaction);
				if (respuesta.equals(EpsonTSEService.EXECUTION_OK)) {
					List<String> listaCampos = new ArrayList<String>();
					listaCampos.add("transactionNumber");
					HashMap<String, String> mapaCampos = epsonTSEService.tratamientoRespuesta(respuestaStartTransaction, listaCampos);
					String transactionNumber = mapaCampos.get("transactionNumber");

					String peticionFinishTransaction = epsonTSEService.finishTransaction(EpsonTSEService.PROCESSTYPE_SONSTINGERVORGANG, EpsonTSEService.TIPO_TRANSACCION_TAGESSTART, saldo.abs(), null,
					        null, Integer.parseInt(transactionNumber), false);
					epsonTSEService.enviarPeticion(peticionFinishTransaction);
					String respuestaFinishTransaction = epsonTSEService.lecturaSocket();
					respuesta = epsonTSEService.tratamientoRespuestaResult(respuestaFinishTransaction);
				}
			}

		}
		catch (Exception e) {
			log.error("tseAperturaCaja() - Error al realizar el proceso de apertura de caja -" + e.getMessage());
		}

		return respuesta;
	}

	private Boolean aperturaImpresorasFiscal(BigDecimal saldoBigDecimal) throws Exception {
		/* Impresora TM-M30 TSE */
		if (Dispositivos.getInstance().getImpresora1() instanceof EpsonTM30) {
			String respuesta = tseAperturaCaja(saldoBigDecimal);

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

			HashMap<String, String> mapaCampos = spark130FService.getCamposRespuesta(spark130FService.realizarLlamada(spark130FService.openShift()), listaCampos);
			String returnCode = mapaCampos.get("RC");

			/* El returnCode 20 indica que ya está abierta la caja */
			if (!returnCode.equals(Spark130FConstants.NO_ERROR) && !returnCode.equals("20")) {
				Map<String, String> mapaErrores = Spark130FConstants.setErrors();
				throw new Spark130FException(mapaErrores.get(returnCode));
			}
		}

		return true;
	}

	@Override
	protected boolean accionValidarForm() {
		if (estamosEnColombia) {
			String msgError = "";
			
			if (StringUtils.isBlank(tfFecha.getTexto())) {
				msgError = I18N.getTexto("Ha de indicar una fecha de apertura");
			}
			if(StringUtils.isBlank(msgError) && StringUtils.isBlank(tfSaldo.getText())) {
				msgError = I18N.getTexto("El saldo ha de tener un valor positivo válido");
			}
			
			if(StringUtils.isBlank(msgError) && (tfSaldo.getText().length() < 1 || tfSaldo.getText().length() > 18)) {
				msgError = I18N.getTexto("El saldo ha de tener un valor positivo válido");
			}
			
			if(StringUtils.isNotBlank(msgError)) {
				VentanaDialogoComponent.crearVentanaError(msgError, getStage());
				return false;
			}

			return true;
		}
		else {
			return super.accionValidarForm();
		}

	}
}
