package com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.axis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javafx.stage.Stage;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.axis.exception.AxisCodeErrorException;
import com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.axis.exception.AxisResponseException;
import com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.axis.gui.ConfiguracionAxisView;
import com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.axis.utils.ObjectParseUtil;
import com.comerzzia.bimbaylola.pos.services.core.variables.ByLVariablesServices;
import com.comerzzia.bimbaylola.pos.services.dispositivofirma.ByLConfiguracionModelo;
import com.comerzzia.bimbaylola.pos.services.dispositivofirma.IFirma;
import com.comerzzia.bimbaylola.pos.services.ticket.pagos.tarjeta.ByLDatosRespuestaPagoTarjeta;
import com.comerzzia.pos.core.dispositivos.configuracion.ConfiguracionDispositivo;
import com.comerzzia.pos.core.dispositivos.dispositivo.DispositivoException;
import com.comerzzia.pos.core.dispositivos.dispositivo.tarjeta.TarjetaBase;
import com.comerzzia.pos.core.dispositivos.dispositivo.tarjeta.TarjetaException;
import com.comerzzia.pos.core.gui.POSApplication;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.services.core.variables.VariablesServices;
import com.comerzzia.pos.services.ticket.pagos.tarjeta.DatosPeticionPagoTarjeta;
import com.comerzzia.pos.services.ticket.pagos.tarjeta.DatosRespuestaPagoTarjeta;
import com.comerzzia.pos.util.config.SpringContext;
import com.comerzzia.pos.util.i18n.I18N;
import com.ingenico.fr.jc3api.JC3ApiC3Rspn;
import com.ingenico.fr.jc3api.JC3ApiConstants;
import com.ingenico.fr.jc3api.JC3ApiConstants.C3TenderTypes;
import com.ingenico.fr.jc3api.JC3ApiInterface.JC3ApiCallbacks;
import com.ingenico.fr.jc3api.JC3ApiInterface.JC3ApiCallbacksExt;
import com.ingenico.fr.jc3api.JC3ApiInterfaceNet;
import com.ingenico.fr.jc3api.JC3ApiParams;
import com.ingenico.fr.jc3api.json.JsonEnums;
import com.ingenico.fr.jc3api.json.JsonInfoCheckBoxDocument;
import com.ingenico.fr.jc3api.json.JsonInfoCheckBoxSettings;
import com.ingenico.fr.jc3api.json.JsonOperationResultInfoCheckBox;
import com.ingenico.fr.jc3api.json.JsonSignBoxSettings;

public class AxisManager extends TarjetaBase implements IFirma {

	private static final String TIPO_AXIS = "TIPO_AXIS";
	private static final String MERCHANT_TICKET = "MERCHANT_TICKET";
	private static final String CUSTOMER_TICKET = "CUSTOMER_TICKET";

	@Autowired
	protected Sesion sesion;
	@Autowired
	protected VariablesServices variableService;

	private static Logger log = Logger.getLogger(AxisManager.class);
	protected static Boolean conectado;

	private static int keyCode_;
	private static Object keyCodeLock_;
	private JC3ApiParams params;
	private static JC3ApiInterfaceNet agent;

	public static final String PARAMETRO_SALIDA_CONFIGURACION = "salida_configuracion";
	public static final String COD_MONEDA_AXIS = "codMonedaAxis";
	public static final String FORMA_PAGO_AXIS = "formaPagoAxis";

	private static final String ARCHIVO_CONFIGURACION = "jc3api-example.properties";
	private static final String PROPIEDAD_ARCHIVO_LOG = "jc3api.log4j.properties";
	private static final String ARCHIVO_LOG = "jc3api-log4j.properties";

	private static final String SOLICITUD_ACEPTADA = "0000";
	private static final String FIRMA_RECHAZADA = "0310";

	public static final String IMAGEN_FIRMA = "IMAGEN_FIRMA";
	public static final String RESPUESTA_CONSENTIMIENTO = "RESPUESTA_CONSENTIMIENTO";

	private final String VENTA = "VENTA";
	private final String DEVOLUCION = "DEVOLUCION";
	private final String ANULACION = "ANULACION";

	private String codMonedaAxis;
	private String formaPagoAxis;
	private Map<String, String> errores;

	private static byte[] imagenFirma;

	DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	protected String modelo;
	protected String modoConexion;
	protected HashMap<String, ByLConfiguracionModelo> listaConfiguracion;
	protected ByLConfiguracionModelo configuracionActual;

	@Override
	public boolean isConfigurable() {
		return true;
	}

	/**
	 * Realizamos la carga de la configuración de la forma de pago.
	 * 
	 * @param config
	 *            : Objeto que contiene la configuración del dispositivo.
	 * @throws DispositivoException
	 */
	@Override
	protected void cargaConfiguracion(ConfiguracionDispositivo config) throws DispositivoException {
		log.debug("cargaConfiguracion() - Iniciamos la cargar de la configuración de Axis...");
		/* Realizamos la carga de los datos de sesión para usarlo mas tarde */
		sesion = SpringContext.getBean(Sesion.class);

		try {
			/* Cargamos los datos de configuración de BD */
			for (String parametro : config.getParametrosConfiguracion().keySet()) {
				if (parametro.equals(COD_MONEDA_AXIS)) {
					codMonedaAxis = config.getParametrosConfiguracion().get(parametro);
				}
				else if (parametro.equals(FORMA_PAGO_AXIS)) {
					formaPagoAxis = config.getParametrosConfiguracion().get(parametro);
				}
			}

			/* Realizamos una última comprobación de los datos de configuración */
			if (StringUtils.isBlank(codMonedaAxis) || StringUtils.isBlank(formaPagoAxis)) {
				String mensajeError = "Alguno de los datos de configuración de Axis no esta cargado correctamente";
				log.error("cargaConfiguracion() - " + mensajeError);
				throw new DispositivoException(I18N.getTexto(mensajeError));
			}

			variableService = SpringContext.getBean(VariablesServices.class);
			/* Cargamos la ruta de la variable de BD */
			String rutaProperties = variableService.getVariableAsString(ByLVariablesServices.PATH_AXIS_CONFIGURACION);

			/*
			 * Realizamos la carga de los datos de configuración a partir de los archivos de configuración que están en
			 * "pos\comerzzia-bimbaylola-pos-resources\configuracion"
			 */
			loadParamsFromFile(rutaProperties);

			log.debug("cargaConfiguracion() - Configuración de Axis: ");
			log.debug("cargaConfiguracion() - Ip Address : " + params.getC3NetIpAddress());
			log.debug("cargaConfiguracion() - Port : " + params.getC3NetTcpPort());
			log.debug("cargaConfiguracion() - Timeout : " + params.getC3NetTimeout());
			log.debug("cargaConfiguracion() - TPV : " + params.getC3TpvFromParams());
			log.debug("cargaConfiguracion() - Código Moneda : " + codMonedaAxis);
			log.debug("cargaConfiguracion() - Medio de Pago : " + formaPagoAxis);
			log.debug("cargaConfiguracion() - Ruta archivos configuración : " + rutaProperties);

		}
		catch (Exception e) {
			log.error("cargaConfiguracion() - " + e.getMessage(), e);
			throw new DispositivoException(e.getMessage(), e);
		}

		/* Registramos los posibles errores */
		errores = new HashMap<String, String>();
		errores.put("0100", I18N.getTexto("EFT timeout error"));
		errores.put("0101", I18N.getTexto("No se puede abrir el puerto serie"));
		errores.put("0102", I18N.getTexto("EFT reading error"));
		errores.put("0103", I18N.getTexto("EFT protocol error"));
		errores.put("0201", I18N.getTexto("Axis connection failed"));
		errores.put("0311", I18N.getTexto("Authorization center connection failed "));
		errores.put("0310", I18N.getTexto("Cardholder or acceptor abort"));
		errores.put("0014", I18N.getTexto("Operation impossible"));
		errores.put("0033", I18N.getTexto("Card expired"));
		errores.put("0018", I18N.getTexto("Application not initialized"));
		errores.put("0021", I18N.getTexto("Offline transaction file full"));
		errores.put("0004", I18N.getTexto("Payment refused"));
		errores.put("2006", I18N.getTexto("Amount below authorized minimum"));
		errores.put("2008", I18N.getTexto("Amount over authorized maximum"));
		errores.put("0024", I18N.getTexto("Currency not supported"));
		errores.put("0016", I18N.getTexto("Unknown terminal number"));
		errores.put("2010", I18N.getTexto("Card present in the reader"));
		errores.put("2011", I18N.getTexto("Customer ticket printing failed"));
		errores.put("2012", I18N.getTexto("Retailer ticket printing failed"));
		errores.put("0110", I18N.getTexto("Card unreadable"));
		errores.put("0113", I18N.getTexto("Mute chip"));
		errores.put("0128", I18N.getTexto("Declined"));
		errores.put("3001", I18N.getTexto("Devolución de la llamada desconocida"));
		errores.put("3002", I18N.getTexto("Se ha superado el tiempo de espera de la llamada"));
		errores.put("3003", I18N.getTexto("Devolución de la llamada cancelada"));
		errores.put("0900", I18N.getTexto("Error al localizar el archivo de configuración c3Config"));

		log.debug("cargaConfiguracion() - Finalizada la cargar de la configuración de Axis.");
	}

	/**
	 * Carga un objeto de tipo JC3ApiParams con los datos de configuración del Pinpad.
	 * 
	 * @param urlConfiguracion
	 *            : Ruta donde se encuentran los archivos de configuración
	 * @throws IOException
	 */
	public void loadParamsFromFile(String urlConfiguración) throws IOException {
		Properties c3AgentProperties_ = null;
		c3AgentProperties_ = new Properties();

		/* Cargamos los parámetros de configuración */
		FileInputStream input = null;
		try {
			if (StringUtils.isNotBlank(urlConfiguración)) {
				input = new FileInputStream(urlConfiguración + "\\" + ARCHIVO_CONFIGURACION);
				/* Cargamos las propiedades */
				c3AgentProperties_.load(input);
				/* En caso de no estar modificado anteriormente, cambiamos la ruta del archivo de log */
				if (ARCHIVO_LOG.equals(c3AgentProperties_.getProperty(PROPIEDAD_ARCHIVO_LOG))) {
					c3AgentProperties_.setProperty(PROPIEDAD_ARCHIVO_LOG, urlConfiguración + "\\" + ARCHIVO_LOG);
				}
				params = new JC3ApiParams(c3AgentProperties_);
			}
			else {
				String mensajeError = "No se ha configurado la Ruta para los archivos de Configuración";
				log.error("loadParamsFromFile() - " + mensajeError);
				throw new IOException(I18N.getTexto(mensajeError));
			}
		}
		catch (Exception e) {
			String mensajeError = "Se ha producido un error al cargar los archivos de configuración de Axis - " + e.getMessage();
			log.error("loadParamsFromFile() - " + mensajeError, e);
			throw new IOException(I18N.getTexto(mensajeError), e);
		}
		finally {
			if (input != null) {
				input.close();
			}
		}
	}

	/**
	 * Realiza la apertura de la pantalla de configuración de parámetros.
	 * 
	 * @param stage
	 *            : Objeto que contiene la pantalla donde vamos a lanzar la nueva.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void configurar(Stage stage) {
		log.debug("configurar() - Abrienda pantalla de configuración de Axis...");
		super.configurar(stage);

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(PARAMETRO_SALIDA_CONFIGURACION, getConfiguracion().getParametrosConfiguracion());

		POSApplication.getInstance().getMainView().showModalCentered(ConfiguracionAxisView.class, params, stage);

		/*
		 * Guardamos en sesión los datos de configuración, realmente no están guardado del todo hasta que no se Acepta
		 * los cambios en la pantalla de "Configuración TPV"
		 */
		if (params.containsKey(PARAMETRO_SALIDA_CONFIGURACION)) {
			Map<String, String> parametrosConfiguracion = (Map<String, String>) params.get(PARAMETRO_SALIDA_CONFIGURACION);
			getConfiguracion().setParametrosConfiguracion(parametrosConfiguracion);
			log.debug("configurar() - Parámetros de configuración de Axis nuevos : " + parametrosConfiguracion);
		}
	}

	/**
	 * Realiza una comparación con el medio de pago cargado.
	 * 
	 * @param codMedioPago
	 *            : Código del medio de pago para comparar.
	 * @return Boolean
	 */
	@Override
	public boolean isCodMedPagoAceptado(String codMedioPago) {
		return codMedioPago.equals(formaPagoAxis);
	}

	/**
	 * Realiza un Test de conexión para comprobar que funciona correctamente la conexión del Pinpad.
	 * 
	 * @return String
	 * @throws DispositivoException
	 */
	public String conexionTest() throws DispositivoException {
		try {
			conecta();
			JC3ApiC3Rspn responseCone = agent.processC3Version(params.getC3TpvFromParams());

			/* En caso de contener algún error lo mostramos por pantalla */
			if (!SOLICITUD_ACEPTADA.equals(responseCone.getcC3ErrorStr())) {
				String mensajeError = "";
				/* Comprobamos errores de Axis */
				mensajeError = procesarRespuestaGenericas(responseCone);
				if (StringUtils.isNotBlank(mensajeError)) {
					return mensajeError;
				}
				else {
					if (errores.containsKey(responseCone.getcC3ErrorStr())) {
						mensajeError = comprobarErroresBase(responseCone.getcC3ErrorStr());
					}
					else {
						mensajeError = "Se ha producido un error al realizar el Test de Conexión " + "\nCompruebe las conexiones y la configuración";
					}
					log.error("conexionTest() - " + mensajeError);
					return mensajeError;
				}
			}
			else {
				log.debug("conexionTest() - " + responseCone.getcC3Version());
			}
		}
		catch (DispositivoException e) {
			String mensajeError = "Se ha producido un error al realizar el Test";
			log.error("conexionTest() - " + mensajeError + " - " + e.getMessage(), e);
			throw new DispositivoException(I18N.getTexto(mensajeError), e);
		}
		return "";
	}

	/**
	 * Realiza un Test de los componentes del Pinpad, para comprobar que está todo correcto.
	 * 
	 * @return String
	 * @throws DispositivoException
	 */
	public String autoTest() throws DispositivoException {
		try {
			conecta();
			JC3ApiC3Rspn responseAutotest = agent.processC3AutoTest(params.getC3TpvFromParams());

			/* En caso de contener algún error lo mostramos por pantalla */
			if (!SOLICITUD_ACEPTADA.equals(responseAutotest.getcC3ErrorStr())) {
				String mensajeError = "";
				/* Comprobamos errores de Axis */
				mensajeError = procesarRespuestaGenericas(responseAutotest);
				if (StringUtils.isNotBlank(mensajeError)) {
					return mensajeError;
				}
				else {
					if (errores.containsKey(responseAutotest.getcC3ErrorStr())) {
						mensajeError = comprobarErroresBase(responseAutotest.getcC3ErrorStr());
					}
					else {
						mensajeError = "Se ha producido un error al realizar el AutoTest - " + responseAutotest.getcC3ErrorStr();
					}
					log.error("autoTest() - " + mensajeError);
					return mensajeError;
				}
			}
		}
		catch (DispositivoException e) {
			String mensajeError = "Se ha producido un error al realizar el Test";
			log.error("conexionTest() - " + mensajeError + " - " + e.getMessage(), e);
			throw new DispositivoException(I18N.getTexto(mensajeError), e);
		}
		return "";
	}

	/**
	 * Realiza una petición de pago.
	 * 
	 * @param datoPeticion
	 *            : Objeto que contiene los datos de la petición.
	 * @return DatosRespuestaPagoTarjeta
	 * @throw TarjetaException
	 */
	@Override
	protected ByLDatosRespuestaPagoTarjeta solicitarPago(DatosPeticionPagoTarjeta datoPeticion) throws TarjetaException {
		log.debug("solicitarPago() - Iniciamos la acción para solicitar el pago en Axis...");
		ByLDatosRespuestaPagoTarjeta datosRespuesta = new ByLDatosRespuestaPagoTarjeta(datoPeticion);
		try {
			// conecta();
			/* Calculamos el importe, tiene que ser Long y multiplicado por 100 */
			Long importePago = datoPeticion.getImporte().multiply(new BigDecimal(100)).longValue();

			log.debug("solicitarPago() - Datos petición : \n- TPV(" + params.getC3TpvFromParams() + ") \n- Importe(" + importePago + ") " + "\n- Caja("
			        + sesion.getSesionCaja().getCajaAbierta().getCodCaja() + ") " + "\n- Usuario Caja(" + sesion.getSesionUsuario().getUsuario().getIdUsuario().toString() + ")");

			log.debug("TIEMPO RESPUESTA : PETICIÓN DE PAGO EN AXIS : " + dateFormat.format(new Date()));

			JC3ApiC3Rspn response = agent.processC3Debit(params.getC3TpvFromParams(), sesion.getSesionCaja().getCajaAbierta().getCodCaja(), importePago, codMonedaAxis, sesion.getSesionUsuario()
			        .getUsuario().getIdUsuario().toString(), null);

			log.debug("solicitarPago() - La respuesta recibida es : " + response.getCustomerTicket());

			log.debug("TIEMPO RESPUESTA : PETICIÓN DE PAGO EN AXIS FINALIZADA : " + dateFormat.format(new Date()));

			/* Rellenamos un objeto de tipo "DatosRespuestaPagoTarjeta" o un Exception, según el resultado */
			datosRespuesta = procesarRespuestaTransaccionPago(response, datoPeticion, VENTA);

		}
		catch (Exception e) {
			String mensajeError = "Error al solicitar el pago por Axis";
			if (e instanceof AxisCodeErrorException) {
				mensajeError = mensajeError + "\n" + e.getMessage();
			}
			else if (e instanceof DispositivoException) {
				mensajeError = e.getMessage();
			}
			log.error("solicitarPago() - " + mensajeError, e);
			throw new TarjetaException(I18N.getTexto(mensajeError), datosRespuesta);
		}
		log.debug("solicitarPago() - Finalizada la acción para solicitar el pago en Axis.");
		return datosRespuesta;
	}

	/**
	 * Realiza una petición de devolución.
	 * 
	 * @param datoPeticion
	 *            : Objeto que contiene los datos de la petición.
	 * @return DatosRespuestaPagoTarjeta
	 * @throws TarjetaException
	 */
	@Override
	protected ByLDatosRespuestaPagoTarjeta solicitarDevolucion(DatosPeticionPagoTarjeta datoPeticion) throws TarjetaException {
		log.debug("solicitarDevolucion() - Iniciamos la acción para solicitar una devolución en Axis...");
		ByLDatosRespuestaPagoTarjeta datosRespuesta = new ByLDatosRespuestaPagoTarjeta(datoPeticion);
		try {
			// conecta();
			/* Calculamos el importe, tiene que ser Long y multiplicado por 100 */
			Long importeDevolver = datoPeticion.getImporte().abs().multiply(new BigDecimal(100)).longValue();

			log.debug("solicitarDevolucion() - Datos petición : \n- TPV(" + params.getC3TpvFromParams() + ")" + "\n- Caja(" + sesion.getSesionCaja().getCajaAbierta().getCodCaja() + ") "
			        + "\n- Usuario Caja(" + sesion.getSesionUsuario().getUsuario().getIdUsuario().toString() + ")");

			log.debug("TIEMPO RESPUESTA : PETICIÓN DE DEVOLUCIÓN EN AXIS : " + dateFormat.format(new Date()));

			JC3ApiC3Rspn response = agent.processC3Refund(params.getC3TpvFromParams(), sesion.getSesionCaja().getCajaAbierta().getCodCaja(), importeDevolver, codMonedaAxis, sesion.getSesionUsuario()
			        .getUsuario().getIdUsuario().toString(), null);

			log.debug("TIEMPO RESPUESTA : PETICIÓN DE DEVOLUCIÓN EN AXIS FINALIZADA : " + dateFormat.format(new Date()));

			log.debug("solicitarDevolucion() - La respuesta recibida es : " + response.getCustomerTicket());

			/* Rellenamos un objeto de tipo "DatosRespuestaPagoTarjeta" o un Exception, según el resultado */
			datosRespuesta = procesarRespuestaTransaccionPago(response, datoPeticion, DEVOLUCION);

		}
		catch (Exception e) {
			String mensajeError = "Error al solicitar la devolución por Axis";
			if (e instanceof AxisCodeErrorException) {
				mensajeError = mensajeError + "\n" + e.getMessage();
			}
			else if (e instanceof DispositivoException) {
				mensajeError = e.getMessage();
			}
			log.error("solicitarDevolucion() - " + mensajeError, e);
			throw new TarjetaException(I18N.getTexto(mensajeError), datosRespuesta);
		}
		log.debug("solicitarDevolucion() - Finalizada la acción para solicitar una devolución en Axis.");
		return datosRespuesta;
	}

	/**
	 * Solicita la cancelación de un pago en caso de realizarse.
	 * 
	 * @param datoPeticion
	 *            : Objeto que contiene los datos de la petición
	 * @return DatosRespuestaPagoTarjeta
	 * @throws TarjetaException
	 */
	@Override
	protected DatosRespuestaPagoTarjeta solicitarAnulacionVenta(DatosPeticionPagoTarjeta datoPeticion) throws TarjetaException {
		return anularMovimiento(datoPeticion);
	}

	/**
	 * Solicita la cancelación de una devolución en caso de realizarse.
	 * 
	 * @param datoPeticion
	 *            : Objeto que contiene los datos de la petición
	 * @return DatosRespuestaPagoTarjeta
	 * @throws TarjetaException
	 */
	@Override
	protected DatosRespuestaPagoTarjeta solicitarAnulacionDevolucion(DatosPeticionPagoTarjeta datoPeticion) throws TarjetaException {
		return anularMovimiento(datoPeticion);
	}

	/**
	 * Realiza una petición para cancelar o un pago o una devolución.
	 * 
	 * @param datoPeticion
	 *            : Objeto que contiene los datos de la petición
	 * @return DatosRespuestaPagoTarjeta
	 * @throws TarjetaException
	 */
	protected ByLDatosRespuestaPagoTarjeta anularMovimiento(DatosPeticionPagoTarjeta datoPeticion) throws TarjetaException {
		log.debug("anularMovimiento() - Iniciando la acción para anular un movimiento de Axis...");
		ByLDatosRespuestaPagoTarjeta datosRespuesta = new ByLDatosRespuestaPagoTarjeta(datoPeticion);
		try {
			// conecta();

			log.debug("anularMovimiento() - Datos petición : \n- TPV(" + params.getC3TpvFromParams() + ")" + "\n- Caja(" + sesion.getSesionCaja().getCajaAbierta().getCodCaja() + ") "
			        + "\n- Usuario Caja(" + sesion.getSesionUsuario().getUsuario().getIdUsuario().toString() + ")");

			log.debug("TIEMPO RESPUESTA : PETICIÓN DE ANULACIÓN EN AXIS : " + dateFormat.format(new Date()));

			JC3ApiC3Rspn responseCancel = agent.processC3CancellationWithRef(C3TenderTypes.C3_TENDERTYPE_CPA.getTenderType(), params.getC3TpvFromParams(), sesion.getSesionCaja().getCajaAbierta()
			        .getCodCaja(), datoPeticion.getIdDocumentoOrigen(), sesion.getSesionUsuario().getUsuario().getIdUsuario().toString(), null);

			log.debug("TIEMPO RESPUESTA : PETICIÓN DE ANULACIÓN EN AXIS : " + dateFormat.format(new Date()));

			log.debug("anularMovimiento() - La respuesta recibida es : " + responseCancel.getCustomerTicket());

			/* Rellenamos un objeto de tipo "DatosRespuestaPagoTarjeta" o un Exception, según el resultado */
			datosRespuesta = procesarRespuestaTransaccionPago(responseCancel, datoPeticion, ANULACION);

		}
		catch (Exception e) {
			String mensajeError = "Error al solicitar la cancelación por Axis";
			if (e instanceof AxisCodeErrorException) {
				mensajeError = mensajeError + "\n" + e.getMessage();
			}
			else if (e instanceof DispositivoException) {
				mensajeError = e.getMessage();
			}
			log.error("anularMovimiento() - " + mensajeError, e);
			throw new TarjetaException(I18N.getTexto(mensajeError), datosRespuesta);
		}
		log.debug("anularMovimiento() - Finalizada la acción para anular un movimiento de Axis.");
		return datosRespuesta;
	}

	/**
	 * Realiza la comprobación para saber si se han producido errores en la transacción.
	 * 
	 * @param response
	 *            : Objeto que contiene los datos de respuesta de la transacción.
	 * @param peticion
	 *            : Objeto que contiene los datos de la petición de la transacción.
	 * @param tipo
	 *            : El tipo de acción que se va a realizar
	 * @throws AxisResponseException
	 */
	protected ByLDatosRespuestaPagoTarjeta procesarRespuestaTransaccionPago(JC3ApiC3Rspn response, DatosPeticionPagoTarjeta peticion, String tipo) throws AxisResponseException, AxisCodeErrorException {
		log.debug("procesarRespuestaTransaccionPago() - Procesando respuesta de la transacción de pago...");
		ByLDatosRespuestaPagoTarjeta respuesta = new ByLDatosRespuestaPagoTarjeta(peticion);

		/* En caso de que el código devuelto sea diferente de 0000, significa que es un error */
		if (!StringUtils.isNotBlank(response.getcResponseCodeStr())) {
			String mensajeError = comprobarErroresBase(response.getcResponseCodeStr());
			if (StringUtils.isNotBlank(mensajeError)) {
				throw new AxisCodeErrorException(mensajeError);
			}
			else {
				throw new AxisResponseException();
			}
		}
		else {
			if (SOLICITUD_ACEPTADA.equals(response.getcC3ErrorStr())) {
				respuesta = procesarRespuestaCorrecta(response, peticion, tipo);
			}
			else {
				String mensajeError = comprobarErroresBase(response.getcC3ErrorStr());
				if (StringUtils.isNotBlank(mensajeError)) {
					throw new AxisCodeErrorException(mensajeError);
				}
				else {
					throw new AxisResponseException();
				}
			}
		}
		log.debug("procesarRespuestaTransaccionPago() - Finalizado el procesamiento de la respuesta de la transacción de pago.");
		return respuesta;
	}

	protected String procesarRespuestaGenericas(JC3ApiC3Rspn response) {
		log.debug("procesarRespuestaTransaccionPago() - Procesando respuesta Axis...");
		String mensajeError = "";
		/* En caso de que el código devuelto sea diferente de 0000, significa que es un error */
		if (StringUtils.isNotBlank(response.getcResponseCodeStr())) {
			mensajeError = comprobarErroresBase(response.getcResponseCodeStr());
		}
		else {
			if (!SOLICITUD_ACEPTADA.equals(response.getcC3ErrorStr())) {
				mensajeError = comprobarErroresBase(response.getcC3ErrorStr());
			}
		}
		return mensajeError;
	}

	/**
	 * Transforma la respuesta del servicio en un objeto de tipo "DatosRespuestaPagoTarjeta".
	 * 
	 * @param response
	 *            : Objeto que contiene los datos de respuesta.
	 * @param peticion
	 *            : Objeto que contiene los datos de la petición de la transacción.
	 * @param tipo
	 *            : El tipo de acción que se va a realizar
	 * @return
	 * @throws AxisResponseException
	 */
	protected ByLDatosRespuestaPagoTarjeta procesarRespuestaCorrecta(JC3ApiC3Rspn response, DatosPeticionPagoTarjeta peticion, String tipo) throws AxisResponseException {
		log.debug("procesarRespuestaCorrecta() - Convirtiendo la respuesta : " + response.getCustomerTicket());
		ByLDatosRespuestaPagoTarjeta respuesta = new ByLDatosRespuestaPagoTarjeta(peticion);
		String localizadorFuc = "";
		try {
			/* =================================== Datos Principales =================================== */
			respuesta.setCodAutorizacion(response.getcNumAuto());
			respuesta.setNumTransaccion(response.getcNumDossier());

			Calendar calendarioFechaTrans = Calendar.getInstance();
			calendarioFechaTrans.set(Calendar.DAY_OF_MONTH, Integer.valueOf(response.getcDateTrns().substring(0, 2)));
			calendarioFechaTrans.set(Calendar.MONTH, Integer.valueOf(response.getcDateTrns().substring(2, 4)) - 1);
			calendarioFechaTrans.set(Calendar.YEAR, Integer.valueOf("20" + response.getcDateTrns().substring(4, 6)));
			calendarioFechaTrans.set(Calendar.HOUR_OF_DAY, Integer.valueOf(response.getcHeureTrns().substring(0, 2)));
			calendarioFechaTrans.set(Calendar.MINUTE, Integer.valueOf(response.getcHeureTrns().substring(2, 4)));
			calendarioFechaTrans.set(Calendar.SECOND, Integer.valueOf(response.getcHeureTrns().substring(4, 6)));
			calendarioFechaTrans.set(Calendar.MILLISECOND, new Integer(0));
			SimpleDateFormat formatFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			String fechaTratada = formatFecha.format(calendarioFechaTrans.getTime());
			respuesta.setFechaTransaccion(fechaTratada);

			respuesta.setTipoTransaccion(tipo);
			/* Actualmente para este Pinpad no se están usando */
			respuesta.setNumOperacion("");
			respuesta.setNumOperacionBanco("");
			/* Para devoluciones referenciadas, aún no están activadas */
			respuesta.setCodigoCentro("");
			respuesta.setPos("");
			String numeroTarjeta = response.getcPan();
			numeroTarjeta = numeroTarjeta.substring(numeroTarjeta.length() - 4, numeroTarjeta.length());
			numeroTarjeta = StringUtils.leftPad(numeroTarjeta, 12, "X");
			respuesta.setPAN(numeroTarjeta);
			respuesta.setTerminalId(response.getcTermNum());

			/* =================================== Datos Adicionales =================================== */
			Map<String, Object> datosTags = ObjectParseUtil.introspect(response);
			Map<String, String> adicionales = new HashMap<String, String>();
			Map<String, String> extendedTags = new HashMap<String, String>();

			for (Map.Entry<String, Object> entry : datosTags.entrySet()) {
				String key = entry.getKey();
				Object valor = entry.getValue();
				/*
				 * Comprobamos los tipos de objetos posibles que pueden ser para sacar sus valores correctamente. Según
				 * el tipo se procesará de una manera o otra
				 */
				if (valor instanceof String) {
					adicionales.put(key, ((String) valor).trim());
				}
				else if (valor instanceof Boolean) {
					if (Boolean.TRUE == (Boolean) valor) {
						adicionales.put(key, "true");
					}
					else {
						adicionales.put(key, "false");
					}
					continue;
				}
				else if (valor instanceof Integer) {
					adicionales.put(key, valor.toString());
					continue;
				}
				if ("cTermNum".equals(key)) {
					respuesta.setTipoTarjeta(((String) valor).trim());
				}
				if ("cExtensionApplicationIdentifier".equals(key)) {
					localizadorFuc = (String) valor;
				}
			}
			/* Añadimos los tags que hemos sacado de los "ExtendedTags" */
			adicionales.putAll(extendedTags);
			/* Añadimos los dos tipos de Ticket que pueden traer de respuesta */
			if (StringUtils.isNotBlank(response.getCustomerTicket())) {
				adicionales.put(CUSTOMER_TICKET, response.getCustomerTicket());
				/* Sacamos el FUC y el Terminal Lógico de Axis */
				String[] ticketRespuestaTarjeta = response.getCustomerTicket().split("\n");
				for (int i = 0; i < ticketRespuestaTarjeta.length; i++) {
					if (ticketRespuestaTarjeta[i].length() < 40 && ticketRespuestaTarjeta[i].length() > 1) {
						if (StringUtils.isNotBlank(localizadorFuc)) {
							if (localizadorFuc.equals(ticketRespuestaTarjeta[i])) {
								adicionales.put("TERMINAL_LOGICO", ticketRespuestaTarjeta[i - 1]);
								respuesta.setFuc(ticketRespuestaTarjeta[i - 2]);
							}
						}
					}
				}
			}
			else {
				log.debug("procesarRespuestaCorrecta() - No se ha recibido el CustomerTicket");
			}
			if (StringUtils.isNotBlank(response.getMerchantTicket())) {
				adicionales.put(MERCHANT_TICKET, response.getMerchantTicket());
			}
			else {
				log.debug("procesarRespuestaCorrecta() - No se ha recibido el MerchantTicket");
			}

			/* Añadimos el Tipo de Tarjeta que se va a usar */
			adicionales.put(TIPO_AXIS, TIPO_AXIS);

			/* Añadimos todos los datos adicionales que hemos podido rescatar en el objeto de respuesta */
			respuesta.setAdicionales(adicionales);

			log.debug("procesarRespuestaCorrecta() - Datos de la respuesta de la petición : ");
			log.debug("procesarRespuestaCorrecta() - Código Autorización : " + respuesta.getCodAutorizacion());
			log.debug("procesarRespuestaCorrecta() - Número de Transacción : " + respuesta.getNumTransaccion());
			log.debug("procesarRespuestaCorrecta() - Fecha Transacción : " + respuesta.getFechaTransaccion());

		}
		catch (Exception e) {
			log.error("procesarRespuestaCorrecta() - " + e.getMessage(), e);
			throw new AxisResponseException(e.getMessage(), e);
		}
		log.debug("procesarRespuestaCorrecta() - Finalizada la converción del objeto de respuesta " + respuesta);
		return respuesta;
	}

	/**
	 * Realizamos la conexión con el Pinpad de Axis.
	 * 
	 * @throws DispositivoException
	 */
	@Override
	public void conecta() throws DispositivoException {
		log.debug("AxisManager/conecta() ...");

		if (conectado != null && conectado) {
			// Desconectamos y volvemos a conectar por si lo hemos inicializamos previamente desde el dispositivo de
			// firma con parametros por defecto
			desconecta();
		}
		log.debug("AxisManager/conecta() - Iniciamos la conexión con Axis...");

		try {
			keyCode_ = JC3ApiConstants.C3KEY_NOKEY;
			keyCodeLock_ = new Object();
			JC3ApiCallbacks callbacks = new JC3ApiCallbacksExt(){

				/* Rescata la imagen de la firma */
				@Override
				public void displayImage(byte[] img, String imgType) {
					log.debug("JC3ApiCallbacksExt/displayImage() - Tipo de imagen recibida : " + imgType);
					imagenFirma = img;
				}

				@Override
				public void printTicket(String ticket) {
					log.debug("JC3ApiCallbacksExt/printTicket() - " + ticket);
					Reader reader = null;
					File file = new File(ticket);
					if (file.exists()) {
						try {
							InputStream is = new FileInputStream(file);
							reader = new InputStreamReader(is, JC3ApiConstants.CHARSET_JC3API);
						}
						catch (IOException e) {
						}
					}
					else {
						reader = new StringReader(ticket);
					}

					/* Error al iniciar el Reader */
					if (reader == null) {
						return;
					}

					/* Log para las lineas del ticket */
					BufferedReader bReader = null;
					StringBuilder sb = new StringBuilder();
					try {
						bReader = new BufferedReader(reader);
						String line;
						sb.append("========================" + JC3ApiConstants.LS);
						while ((line = bReader.readLine()) != null) {
							sb.append(line + JC3ApiConstants.LS);
						}
						sb.append("========================");
					}
					catch (IOException e) {
					}
					finally {
						if (bReader != null) {
							try {
								bReader.close();
							}
							catch (IOException e) {
							}
						}
					}
				}

				@Override
				public boolean keyAvailable() {
					synchronized (keyCodeLock_) {
						return (keyCode_ != JC3ApiConstants.C3KEY_NOKEY);
					}
				}

				@Override
				public int getString(StringBuffer arg0, int arg1, String arg2) {
					log.debug("JC3ApiCallbacksExt/getString() - " + arg1 + " - " + arg2);
					arg0.setLength(0);
					String res = "";
					if (arg2.contains("123456789")) {
						res = "123456789";
					}
					arg0.append(res);
					return JC3ApiConstants.C3KEY_VALIDATION;
				}

				@Override
				public int getSecurity() {
					log.debug("JC3ApiCallbacksExt/getSecurity() - " + JC3ApiConstants.C3KEY_VALIDATION);
					return JC3ApiConstants.C3KEY_VALIDATION;
				}

				@Override
				public int getSalesConfirmation() {
					log.debug("JC3ApiCallbacksExt/getSalesConfirmation() - " + JC3ApiConstants.C3KEY_VALIDATION);
					return JC3ApiConstants.C3KEY_VALIDATION;
				}

				@Override
				public int getKey() {
					synchronized (keyCodeLock_) {
						int keyCode = keyCode_;
						keyCode_ = JC3ApiConstants.C3KEY_NOKEY;
						log.debug("JC3ApiCallbacksExt/getKey() " + JC3ApiConstants.C3Keys.getInfo(keyCode));
						return keyCode;
					}
				}

				@Override
				public void display(String arg0, int arg1) {
					log.debug("JC3ApiCallbacksExt/display() - " + arg0);
					String display = "";
					switch (arg1) {
						case JC3ApiInterfaceNet.C3DSP_WAIT_NUM:
							display = "showListNumerics";
							String trim = arg0.trim();
							Integer num = Integer.valueOf(trim.substring(trim.length() - 1, trim.length()));
							switch (num) {
								case 0:
									keyCode_ = JC3ApiConstants.C3KEY_NUMERIC_0;
									break;
								case 1:
									keyCode_ = JC3ApiConstants.C3KEY_NUMERIC_1;
									break;
								case 2:
									keyCode_ = JC3ApiConstants.C3KEY_NUMERIC_2;
									break;
								case 3:
									keyCode_ = JC3ApiConstants.C3KEY_NUMERIC_3;
									break;
								case 4:
									keyCode_ = JC3ApiConstants.C3KEY_NUMERIC_4;
									break;
								case 5:
									keyCode_ = JC3ApiConstants.C3KEY_NUMERIC_5;
									break;
								case 6:
									keyCode_ = JC3ApiConstants.C3KEY_NUMERIC_6;
									break;
								case 7:
									keyCode_ = JC3ApiConstants.C3KEY_NUMERIC_7;
									break;
								case 8:
									keyCode_ = JC3ApiConstants.C3KEY_NUMERIC_8;
									break;
								case 9:
									keyCode_ = JC3ApiConstants.C3KEY_NUMERIC_9;
									break;
								default:
									break;
							}
							break;
						case JC3ApiInterfaceNet.C3DSP_WAIT_NONE:
							break;
						case JC3ApiInterfaceNet.C3DSP_WAIT_1SEC:
							try {
								Thread.sleep(1000);
							}
							catch (InterruptedException e) {
							}
							break;
						case JC3ApiInterfaceNet.C3DSP_WAIT_KEY:
							try {
								Thread.sleep(100);
							}
							catch (InterruptedException e) {
							}
							keyCode_ = JC3ApiConstants.C3KEY_NOKEY;
							break;
						case JC3ApiInterfaceNet.C3DSP_BREAKABLE:
							display = "transaction is breakable";
							break;
						case JC3ApiInterfaceNet.C3DSP_WAIT_ANN:
							display = "Show button cancel";
							keyCode_ = JC3ApiConstants.C3KEY_CANCELLATION;
							break;
						case JC3ApiInterfaceNet.C3DSP_WAIT_COR:
							display = "Show button correlation";
							keyCode_ = JC3ApiConstants.C3KEY_CORRECTION;
							break;
						case JC3ApiInterfaceNet.C3DSP_WAIT_VAL:
							display = "Show button valid";
							keyCode_ = JC3ApiConstants.C3KEY_VALIDATION;
							break;
						case JC3ApiInterfaceNet.C3DSP_WAIT_VAL_ANN:
							display = "Show button cancel - Show button valid";
							keyCode_ = JC3ApiConstants.C3KEY_VALIDATION;
							break;
						case JC3ApiInterfaceNet.C3DSP_WAIT_VAL_ANN_COR:
							display = "Show button cancel - Show button correction - Show button valid";
							keyCode_ = JC3ApiConstants.C3KEY_VALIDATION;
							break;
						default:
							break;
					}
					log.debug("JC3ApiCallbacksExt/Mensaje: - " + display);
				}
			};

			agent = new JC3ApiInterfaceNet(callbacks, params, log);
			JC3ApiC3Rspn responseInit = agent.processC3Init(params.getC3TpvFromParams());
			log.debug("AxisManager/conecta() - " + responseInit.getcUserData1() + " " + responseInit.getcUserData2());

			// La respuesta será correcta cuando el campo cC3ErrorStr sea 0000
			// y los campos cUserData1 y cUserData2 sean idénticos
			if (!SOLICITUD_ACEPTADA.equals(responseInit.getcC3ErrorStr()) || (!responseInit.getcUserData1().equals(responseInit.getcUserData2()))) {
				String mensajeError = "";
				/* Comprobamos errores de Axis */
				mensajeError = procesarRespuestaGenericas(responseInit);
				if (StringUtils.isNotBlank(mensajeError)) {
					throw new DispositivoException(I18N.getTexto(mensajeError));
				}
				else {
					if (errores.containsKey(responseInit.getcC3ErrorStr())) {
						mensajeError = comprobarErroresBase(responseInit.getcC3ErrorStr());
					}
					else {
						mensajeError = "Se ha producido un error al iniciar la conexión con Axis - " + responseInit.getcC3ErrorStr();
					}
					log.error("AxisManager/conecta() - " + mensajeError);
					throw new DispositivoException(I18N.getTexto(mensajeError));
				}
			}
		}
		catch (Throwable e) {
			log.error("AxisManager/conecta() " + e.getMessage(), e);
			throw new DispositivoException(I18N.getTexto(e.getMessage()), e);
		}
		conectado = Boolean.TRUE;
		log.debug("AxisManager/conecta() - Finalizada la conexión con Axis.");
	}

	/**
	 * Realiza una petición de los consentimientos y firma en el Pinpad.
	 * 
	 * @return Map<String, JC3ApiC3Rspn>
	 * @throws DispositivoException
	 * @throws AxisResponseException
	 */
	public Map<String, Object> realizarConsentimientoFirma() throws DispositivoException, AxisResponseException {
		Map<String, Object> mapaRespuestas = new HashMap<String, Object>();
		imagenFirma = null;
		// try{
		// conecta();
		int inactivityTimeout = 60;
		boolean returnToIdle = true;

		/* ============================= CONSENTIMIENTOS ============================= */
		JsonInfoCheckBoxSettings infoCheckBoxSettings = new JsonInfoCheckBoxSettings();
		infoCheckBoxSettings.setScreenType(JsonEnums.InfoCheckBoxScreenTypes.DEFAULT);
		/* Enviamos el Lenguaje */
		infoCheckBoxSettings.setLanguage(JsonEnums.InfoCheckBoxLanguages.ES);
		/* Establecemos cuantos consentimientos queremos mostrar */
		List<JsonEnums.InfoCheckBoxMandatoryConditions> mandatoryConditions = new ArrayList<JsonEnums.InfoCheckBoxMandatoryConditions>();
		// mandatoryConditions.add(JsonEnums.InfoCheckBoxMandatoryConditions.MAIN_CONDITION);
		// mandatoryConditions.add(JsonEnums.InfoCheckBoxMandatoryConditions.SECONDARY_CONDITION);
		infoCheckBoxSettings.setMandatoryCondition(mandatoryConditions);

		/* Pasamos el texto que se va a mostrar en el primer consentimiento */
		String mensajeRecibirNotificaciones = MENSAJE_CHECK_NOTIFICACIONES;
		log.debug("realizarFirma() - Consentimiento para Recibir Notificaciones : " + mensajeRecibirNotificaciones);
		infoCheckBoxSettings.setMainCondition(mensajeRecibirNotificaciones);
		/* Pasamos el texto que se va a mostrar en el segundo consentimiento */
		String mensajeUsoDeDatos = MENSAJE_CHECK_USODATOS;
		log.debug("realizarFirma() - Consentimiento para Uso de Datos : " + mensajeUsoDeDatos);
		infoCheckBoxSettings.setSecondaryCondition(mensajeUsoDeDatos);

		/* Texto del documento que se mostrará en el cuadro de texto en la fuente seleccionada por campo de idioma */
		JsonInfoCheckBoxDocument infoCheckBoxDocument = new JsonInfoCheckBoxDocument();
		String mensajeSeleccionIdioma = "Bimba y Lola es Responsable del Tratamiento de sus datos. Para más info consulte en tienda";
		log.debug("realizarFirma() - Texto de la selección de idioma : " + mensajeSeleccionIdioma);
		infoCheckBoxDocument.setText(mensajeSeleccionIdioma);
		/* Para rescatar los resultados de la operación */
		JsonOperationResultInfoCheckBox[] infoCheckBoxResult = new JsonOperationResultInfoCheckBox[1];

		JC3ApiC3Rspn respuestaConsentimiento = agent
		        .processC3InfoCheckBox(params.getC3TpvFromParams(), inactivityTimeout, returnToIdle, infoCheckBoxSettings, infoCheckBoxDocument, infoCheckBoxResult);
		/* En caso de contener algún error lo mostramos por pantalla */
		if (!SOLICITUD_ACEPTADA.equals(respuestaConsentimiento.getcC3ErrorStr())) {
			String mensajeError = "Se ha producido un error al realizar la petición de Consentimientos - " + respuestaConsentimiento.getcC3ErrorStr();
			log.error("realizarFirma() - " + mensajeError);
			throw new AxisResponseException(mensajeError);
		}
		else {
			mapaRespuestas.put(RESPUESTA_CONSENTIMIENTO, respuestaConsentimiento);
		}
		/* ============================= FIRMA ============================= */
		JsonSignBoxSettings signBoxSettings = new JsonSignBoxSettings();
		signBoxSettings.setScreenType(JsonEnums.SignBoxScreenTypes.DEFAULT);
		/* Formato de imagen por defecto */
		signBoxSettings.setFileFormat(JsonEnums.SignBoxFileFormats.BMP);
		/* Formato de compresión de la imagen */
		signBoxSettings.setEncoding(JsonEnums.SignBoxEncodings.GZIP);
		/* Enviamos el Lenguaje */
		signBoxSettings.setLanguage(JsonEnums.SignBoxLanguages.ES);
		/* Pasamos el texto que se va a mostrar en la parte de la firma */
		signBoxSettings.setText("Firmar dentro del cuadro en blanco :");
		/* Marcamos True para esperar la firma del cliente */
		signBoxSettings.setCheck(true);

		/* Tratamos la respuesta con el objeto de la firma dentro en forma de Byte[] */
		JC3ApiC3Rspn respuestaFirma = agent.processC3SignBox(params.getC3TpvFromParams(), inactivityTimeout, returnToIdle, signBoxSettings);

		/* En caso de contener algún error lo mostramos por pantalla */
		if (!SOLICITUD_ACEPTADA.equals(respuestaFirma.getcC3ErrorStr())) {
			/* Es el error que devuelve al cancelar la firma */
			if (FIRMA_RECHAZADA.equals(respuestaFirma.getcC3ErrorStr())) {
				mapaRespuestas.put(IMAGEN_FIRMA, null);
			}
			else {
				String mensajeError = "Se ha producido un error al realizar la petición de Firma - " + respuestaFirma.getcC3ErrorStr();
				log.error("realizarFirma() - " + mensajeError);
				throw new AxisResponseException(mensajeError);
			}
		}
		else {
			mapaRespuestas.put(IMAGEN_FIRMA, imagenFirma);
		}

		/* En caso de no haber rellenado nada, lanzamos una Exception */
		if (mapaRespuestas.isEmpty()) {
			String mensajeAviso = "No se ha rellenado ni el Consentimiento ni la Firma";
			log.info("realizarFirma() - " + mensajeAviso);
			throw new AxisResponseException(I18N.getTexto(mensajeAviso));
		}

		return mapaRespuestas;
		// }catch(DispositivoException e){
		// String mensajeError = "Se ha producido un error al conectar con Axis";
		// log.error("realizarFirma() - " + mensajeError + " - " + e.getMessage(), e);
		// throw new DispositivoException(I18N.getTexto(mensajeError), e);
		// }
	}

	/**
	 * Comprueba si el error está contemplado en los errores iniciales.
	 * 
	 * @param codigoError
	 *            : Código de error que se ha producido en la llamada.
	 * @return String
	 */
	public String comprobarErroresBase(String codigoError) {
		if (errores.containsKey(codigoError)) {
			return errores.get(codigoError);
		}
		return new String();
	}

	@Override
	public Map<String, Object> firmar() {
		// /* Cargamos los archivos a partir de la ruta proporcionada de las variables de BD */
		Map<String, Object> resultado = null;
		try {
			variableService = SpringContext.getBean(VariablesServices.class);
			String rutaProperties = variableService.getVariableAsString(ByLVariablesServices.PATH_AXIS_CONFIGURACION);
			if (StringUtils.isNotBlank(rutaProperties)) {
				loadParamsFromFile(rutaProperties);
			}

			resultado = realizarConsentimientoFirma();
		}
		catch (IOException | AxisResponseException | DispositivoException e) {
			log.debug("firmar() - Ha ocurrido un error en el proceso de firma con Axis: " + e.getMessage());
		}

		return resultado;
	}

	@Override
	public void iniciarDispositivoFirma() {
		try {

			Map<String, String> parametrosConfiguracion = new HashMap<String, String>();
			parametrosConfiguracion.put(AxisManager.COD_MONEDA_AXIS, "978");
			parametrosConfiguracion.put(AxisManager.FORMA_PAGO_AXIS, "0010");

			ConfiguracionDispositivo configuracion = new ConfiguracionDispositivo();
			configuracion.setParametrosConfiguracion(parametrosConfiguracion);
			setConfiguracion(configuracion);

			cargaConfiguracion(configuracion);

			log.debug("iniciarDispositivoFirma() - Parámetros de configuración de Axis por defecto para poder inicializar el dispositivo de firma : " + parametrosConfiguracion);

			conecta();
		}
		catch (DispositivoException e) {
			log.error("iniciarDispositivoFirma() - " + "Ha ocurrido un problema al iniciar el dispositivo AXIS");
		}
	}

	@Override
	public String getModelo() {
		return modelo;
	}

	@Override
	public String getManejador() {
		return listaConfiguracion.get(modelo).getManejador();
	}

	@Override
	public String getModoConexion() {
		return modoConexion;
	}

	@Override
	public HashMap<String, ByLConfiguracionModelo> getListaConfiguracion() {
		return listaConfiguracion;
	}

	public void setListaConfiguracion(HashMap<String, ByLConfiguracionModelo> listaConfiguracion) {
		this.listaConfiguracion = listaConfiguracion;
	}

	@Override
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	@Override
	public void setModoConexion(String modoConexion) {
		this.modoConexion = modoConexion;
	}

	@Override
	public ByLConfiguracionModelo getConfiguracionActual() {
		return configuracionActual;
	}

	@Override
	public void setConfiguracionActual(ByLConfiguracionModelo configuracionActual) {
		this.configuracionActual = configuracionActual;

	}

	/* ================================================================================================================ */
	/* =================================================== NO USADO =================================================== */
	/* ================================================================================================================ */

	@Override
	protected DatosRespuestaPagoTarjeta solicitarEstado(DatosPeticionPagoTarjeta datoPeticion) throws TarjetaException {
		return null;
	}

	@Override
	protected DatosRespuestaPagoTarjeta solicitarTotales(DatosPeticionPagoTarjeta datoPeticion) throws TarjetaException {
		return null;
	}

	@Override
	protected DatosRespuestaPagoTarjeta solicitarUltimaOperacion(DatosPeticionPagoTarjeta datoPeticion) throws TarjetaException {
		return null;
	}

	@Override
	protected DatosRespuestaPagoTarjeta realizarFinDia(DatosPeticionPagoTarjeta datoPeticion) throws TarjetaException {
		return null;
	}

	@Override
	public void desconecta() throws DispositivoException {
	}

	@Override
	public void getMetodosConexion() {
	}
}
