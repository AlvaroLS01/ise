package com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.conexflow;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javafx.stage.Stage;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.conexflow.gui.ConfiguracionConexFlowView;
import com.comerzzia.bimbaylola.pos.services.ticket.pagos.tarjeta.ByLDatosRespuestaPagoTarjeta;
import com.comerzzia.pos.core.dispositivos.configuracion.ConfiguracionDispositivo;
import com.comerzzia.pos.core.dispositivos.dispositivo.DispositivoException;
import com.comerzzia.pos.core.dispositivos.dispositivo.tarjeta.TarjetaBase;
import com.comerzzia.pos.core.dispositivos.dispositivo.tarjeta.TarjetaCallback;
import com.comerzzia.pos.core.dispositivos.dispositivo.tarjeta.TarjetaException;
import com.comerzzia.pos.core.gui.BackgroundTask;
import com.comerzzia.pos.core.gui.POSApplication;
import com.comerzzia.pos.services.ticket.pagos.tarjeta.DatosPeticionPagoTarjeta;
import com.comerzzia.pos.services.ticket.pagos.tarjeta.DatosRespuestaPagoTarjeta;
import com.comerzzia.pos.util.i18n.I18N;

public class TefConexFlow extends TarjetaBase{

	private Logger log = Logger.getLogger(TefConexFlow.class);

	private static final String TIPO_CONEXFLOW = "TIPO_CONEXFLOW";
	public static final String PARAMETRO_SALIDA_CONFIGURACION = "salida_configuracion";

	public static final int BUFFER_LENGTH = 50000;	
	
	/* Datos de configuración del TEF */
	private String comercio;
	private String tienda;
	private String numTPV;
	private String servidorTCP;
	private String puertoTCP;
	private String timeout;
	private String formaPago;

	/* Lista de errores de ConexFlow */
	private Map<String, String> errores;

	/* Constantes de guardado en el XML */
	public static final String COMERCIO = "comercio";
	public static final String TIENDA = "tienda";
	public static final String NUM_TPV = "numTPV";
	public static final String SERVIDOR_TCP = "servidorTCP";
	public static final String PUERTO_TCP = "puertoTCP";
	public static final String TIMEOUT = "timeout";
	public static final String FORMA_PAGO = "formaPago";

	public static final String NUM_VENTAS = "numsVenta";
	public static final String IMPORTE_VENTAS = "importeVenta";
	public static final String NUM_ANULACIONES = "numAnulaciones";
	public static final String IMPORTE_ANULACIONES = "importeAnulaciones";
	public static final String NUM_DEVOLUCIONES = "numDevoluciones";
	public static final String IMPORTE_DEVOLUCIONES = "importeDevoluciones";

	public static final String TECHNOLOGY_TYPE = "technologyType";
	public static final String EXPIRATION_DATE = "expirationDate";
	public static final String DOCUMENT_KEY = "documentKey";
	public static final String DOCUMENT_DESCRIPTION = "documentDescription";
	public static final String SESSION_KEY = "sessionKey";
	public static final String SESSION_ID = "sessionId";
	public static final String REFERENCE_NUMBER = "rerenceNumber";
	public static final String CHARGE_TYPE = "chargeType";
	public static final String PROCESSOR_KEY = "processorKey";
	public static final String PROCESSOR_DESCRIPTION = "processorDescription";
	public static final String BANK_KEY = "bankKey";
	public static final String CARD_HOLDER_NAME = "cardHolderName";
	public static final String DCC_CONVERSION_RATE = "DCCConversionRate";
	public static final String DCC_AMOUNT = "DCCAmount";
	public static final String DCC_CARD_HOLDER_CURRENCY_ALFA = "DCCCardHolderCurrencyALFA";
	public static final String DCC_CARD_HOLDER_CURRENCY_SYMBOL = "DCCCardHolderCurrencySYMBOL";
	public static final String DCC_DECIMAL_PLACES = "DCCDecimalPlaces";

	/* Punto de conexión */
	private static Socket socket;

	/* Constantes axuliares */
	private final String VENTA = "VENTA";
	private final String DEVOLUCION = "DEVOLUCION";
	private final String ANULACION = "ANULACION";
	private final String TOTALES = "TOTALES";

	/* Identificador único de la transacción. Se utiliza para las anulaciones */
	private String intTransactionID;
	private String importeTransaccion;
	private String idTicket;

	private boolean transaccionBienTerminada;
	private String codResultado;

	DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	/* ################################################################################################ */
	/* ################################# CONEXIONES CON DISPOSITIVO ################################### */
	/* ################################################################################################ */
	
	@Override
	public boolean isConfigurable(){
		return true;
	}

	@Override
	protected void cargaConfiguracion(ConfiguracionDispositivo config) throws DispositivoException{
		log.debug("cargaConfiguracion() - Cargando la configuración del TEF de ConexFlow...");
		for(String parametro : config.getParametrosConfiguracion().keySet()){
			if(parametro.equals(COMERCIO)){
				comercio = config.getParametrosConfiguracion().get(parametro);
			}
			else if(parametro.equals(TIENDA)){
				tienda = config.getParametrosConfiguracion().get(parametro);
			}
			else if(parametro.equals(NUM_TPV)){
				numTPV = config.getParametrosConfiguracion().get(parametro);
			}
			else if(parametro.equals(SERVIDOR_TCP)){
				servidorTCP = config.getParametrosConfiguracion().get(parametro);
			}
			else if(parametro.equals(PUERTO_TCP)){
				puertoTCP = config.getParametrosConfiguracion().get(parametro);
			}
			else if(parametro.equals(TIMEOUT)){
				timeout = config.getParametrosConfiguracion().get(parametro);
			}
			else if(parametro.equals(FORMA_PAGO)){
				formaPago = config.getParametrosConfiguracion().get(parametro);
			}
		}

		log.debug("cargaConfiguracion() - Configuración de ConexFlow : ");
		log.debug("cargaConfiguracion() - Comercio : " + comercio);
		log.debug("cargaConfiguracion() - Tienda : " + tienda);
		log.debug("cargaConfiguracion() - TPV : " + numTPV);
		log.debug("cargaConfiguracion() - IP Servidor : " + servidorTCP);
		log.debug("cargaConfiguracion() - Puerto servidor : " + puertoTCP);
		log.debug("cargaConfiguracion() - Timeout : " + timeout);
		log.debug("cargaConfiguracion() - Medio de pago : " + formaPago);

		errores = new HashMap<String, String>();
		errores.put("501", "LA TRANSACCIÓN HA SIDO DENEGADA.");
		errores.put("502", "LA TRANSACCIÓN HA SIDO CANCELADA.");
		errores.put("505", "EL CLIENTE HA CANCELADO LA TRANSACCIÓN EN EL PINPAD MEDIANTE EL BOTÓN DE CANCELAR O HA SACADO LA TARJETA ANTES DE TIEMPO.");
		errores.put("506", "LA TRANSACCIÓN HA SIDO DENEGADA POR EL CHIP.");
		errores.put("507", "LA TRANSACCIÓN HA SIDO DENEGADA POR EL CHIP.");
		errores.put("522", "LA TRANSACCIÓN HA SIDO DENEGADA. LA TARJETA ESTÁ EN LA LISTA NEGRA.");
		errores.put("523", "LA TRANSACCIÓN HA SIDO DENEGADA. SE HA PRODUCIDO UN ERROR DE CONEXIÓN CONTRA EL SERVIDOR DE CONEXFLOW.");
		errores.put("524", "LA TRANSACCIÓN HA SIDO DENEGADA POR SUPERAR EL IMPORTE ACUMULADO AUTORIZADO.");
		errores.put("527", "LA TRANSACCIÓN HA SIDO DENEGADA PORQUE NO HAY CONEXIÓN CONTRA EL SERVIDOR DE CONEXFLOW.");
		errores.put("541", "LA TRANSACCIÓN HA SIDO DENEGADA PORQUE LA TARJETA ESTÁ CADUCADA.");
		errores.put("542", "LA TRANSACCIÓN HA SIDO DENEGADA PORQUE EL NÚMERO DE TARJETA NO ES VÁLIDO.");
		errores.put("313", "LA TRANSACCIÓN ESTÁ DUPLICADA, CONTACTE CON SU ADMINISTRADOR.");
		errores.put("322", "LA TRANSACCIÓN HA SIDO DENEGADA. EL IMPORTE QUE SE ESTÁ INTENTANDO DEVOLVER SUPERA EL IMPORTE ORIGINALMENTE PAGADO CON TARJETA.");
		errores.put("209", "LA TARJETA UTILIZADA NO ESTÁ SOPORTADA.");
		errores.put("302", "LA TRANSACCIÓN HA SIDO DENEGADA. EL ESTADO DE LA OPERACIÓN INICIAL NO PERMITE ESTA OPERACIÓN.");
		errores.put("303", "EL PINPAD ESTÁ MAL INICIALIZADO. POR FAVOR, REINICIE EL SERVICIO DE VISUAL PLUGIN.");
		errores.put("552", "NO SE HA PODIDO COMUNICAR CON EL PINPAD, REVISE QUE ESTÁ BIEN CONECTADO.");
		errores.put("701", "ERROR DE FORMATO DE LA PETICION.");
		

		log.debug("cargaConfiguracion() - Finalizada la carga de configuración del TEF de ConexFlow");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void configurar(Stage stage){
		log.debug("configurar() - Iniciamos la pantalla de configuración de ConexFlow...");

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(PARAMETRO_SALIDA_CONFIGURACION, getConfiguracion().getParametrosConfiguracion());

		POSApplication.getInstance().getMainView().showModalCentered(ConfiguracionConexFlowView.class, params, stage);

		if(params.containsKey(PARAMETRO_SALIDA_CONFIGURACION)){
			Map<String, String> parametrosConfiguracion = (Map<String, String>) params.get(PARAMETRO_SALIDA_CONFIGURACION);
			getConfiguracion().setParametrosConfiguracion(parametrosConfiguracion);
			log.debug("configurar() - Parámetros de configuración de ConexFlow nuevos: " + parametrosConfiguracion);
		}
		log.debug("configurar() - Finalizamos la pantalla de configuración de ConexFlow...");
	}

	@Override
	public boolean isCodMedPagoAceptado(String codMedioPago){
		return codMedioPago.equals(formaPago);
	}

	@Override
	public void conecta() throws DispositivoException{
		log.debug("conecta() - Iniciamos la conexión de ConexFlow...");
		try{
			/* Destruimos los objetos en caso de que existan */
			desconecta();
			/* Creamos el cliente para la comunicación TCP/IP */
			socket = new Socket(servidorTCP, new Integer(puertoTCP));
			socket.setSoTimeout(new Integer(timeout) * 1000);
			log.debug("conecta() - Finalizada la conexión de ConexFlow");
		}
		catch(IOException e){
			String mensajeError = "Error al iniciar la conexión con ConexFlow";
			log.error("conecta() - " + mensajeError + " - " + e.getMessage(), e);
			throw new DispositivoException(I18N.getTexto("Error al iniciar la conexión con ConexFlow"), e);
		}
		catch(Exception e){
			String mensajeError = "Error no controlado al iniciar la conexión con ConexFlow";
			log.error("conecta() - " + mensajeError + " - " + e.getMessage(), e);
		throw new DispositivoException(e.getClass().toString() + " - " + I18N.getTexto("Error no controlado al iniciar la conexión con ConexFlow"), e);
		}
	}

	@Override
	public void desconecta() throws DispositivoException{
		log.debug("desconecta() - Iniciamos la desconexión de ConexFlow...");
		try{
			if(socket != null){
				socket.close();
				socket = null;
			}
			log.debug("desconecta() - Finalizada la desconexión de ConexFlow");
		}
		catch(IOException e){
			String mensajeError = "Error al iniciar la desconexión con ConexFlow";
			log.error("desconecta() - " + mensajeError + " - " + e.getMessage(), e);
			throw new DispositivoException(I18N.getTexto("Error al iniciar la desconexión con ConexFlow"), e);
		}
		catch(Exception e){
			String mensajeError = "Error no controlado al iniciar la desconexión con ConexFlow";
			log.error("desconecta() - " + mensajeError + " - " + e.getMessage(), e);
			throw new DispositivoException(e.getClass().toString() + " - " + I18N.getTexto("Error no controlado al iniciar la desconexión con ConexFlow"), e);
		}
	}

	private String crearNumeroTransaccion(DatosPeticionPagoTarjeta datosPeticion){
		String numeroTransaccion = datosPeticion.getIdTransaccion().replaceAll("[a-zA-Z]", "").replaceAll("/", "");
		String mensajeInfo = "Se ha creado el número de transacción (EM) de la operación";
		log.debug("crearNumeroTransaccion() - " + mensajeInfo + "(" + numeroTransaccion + ")");
		return numeroTransaccion;
	}

	/* ################################################################################################ */
	/* #################################### PROCESAR PETICIONES ####################################### */
	/* ################################################################################################ */
	
	private String generarPeticion(String tipo, DatosPeticionPagoTarjeta datosPeticion){
		log.debug("generarPeticion() - Generando la trama de la petición de ConexFlow...");
		Date fechaActual = new Date();
		SimpleDateFormat formatFecha = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat formatHora = new SimpleDateFormat("HHmmss");
		String fechaFormateada = formatFecha.format(fechaActual);
		String horaFormateada = formatHora.format(fechaActual);

		String peticion = "PE01FN0002";
		switch (tipo){
			case VENTA:
				/* Añadimos un identificador por delante para diferenciar ventas de devoluciones */
				intTransactionID = "0" + intTransactionID;
				importeTransaccion = datosPeticion.getImporte().multiply(new BigDecimal(100)).toBigInteger().toString();
				if("RE".equals(((ConexFlowDatosPeticionPagoTarjeta) datosPeticion).getCodTipoDocumento())){
					idTicket = ((ConexFlowDatosPeticionPagoTarjeta) datosPeticion).getIdDocumento().toString();
				}
				else{
					idTicket = ((ConexFlowDatosPeticionPagoTarjeta) datosPeticion).getIdDocumentoString();
				}
				peticion = peticion + "03" + generarSegmentoPeticion("E0", comercio + tienda + numTPV) 
						+ generarSegmentoPeticion("E1", importeTransaccion) 
						+ generarSegmentoPeticion("E3", idTicket)
				        + generarSegmentoPeticion("F3", fechaFormateada) 
				        + generarSegmentoPeticion("F4", horaFormateada) 
				        + generarSegmentoPeticion("EM", intTransactionID);
				break;
			case DEVOLUCION:
				/* Añadimos un identificador por delante para diferenciar ventas de devoluciones */
				intTransactionID = "1" + intTransactionID;
				importeTransaccion = datosPeticion.getImporte().abs().multiply(new BigDecimal(100)).toBigInteger().toString();
				idTicket = ((ConexFlowDatosPeticionPagoTarjeta) datosPeticion).getIdDocumentoString();
				String tiendaOrigen = "";
				String tpvOrigen = "";

				if(datosPeticion instanceof ConexFlowDatosPeticionPagoTarjeta && ((ConexFlowDatosPeticionPagoTarjeta) datosPeticion).getAdicionales() != null){
					tiendaOrigen = ((ConexFlowDatosPeticionPagoTarjeta) datosPeticion).getAdicionales().get("CentroOrigen");
					tpvOrigen = ((ConexFlowDatosPeticionPagoTarjeta) datosPeticion).getAdicionales().get("TPVOrigen");
				}
				/* Para devoluciones en distinta tarjeta (Ej:Navision) o devoluciones fallidas */
				if(StringUtils.isBlank(tiendaOrigen) && StringUtils.isBlank(tpvOrigen)
				        || (((ConexFlowDatosPeticionPagoTarjeta) datosPeticion).getEsDevolucion() && ((ConexFlowDatosPeticionPagoTarjeta) datosPeticion).getEsDevolucionFallida())){
					peticion = peticion + "04" + generarSegmentoPeticion("E0", comercio + tienda + numTPV) 
							+ generarSegmentoPeticion("E1", importeTransaccion)
					        + generarSegmentoPeticion("E3", idTicket) 
					        + generarSegmentoPeticion("EL", "0") 
					        + generarSegmentoPeticion("F3", fechaFormateada)
					        + generarSegmentoPeticion("F4", horaFormateada) 
					        + generarSegmentoPeticion("EM", intTransactionID);
				}
				else if(datosPeticion instanceof ConexFlowDatosPeticionPagoTarjeta || ((ConexFlowDatosPeticionPagoTarjeta) datosPeticion).getAdicionales() != null){
					peticion = peticion + "04" + generarSegmentoPeticion("E0", comercio + tienda + numTPV) 
							+ generarSegmentoPeticion("E1", importeTransaccion)
					        + generarSegmentoPeticion("E3", idTicket) 
					        + generarSegmentoPeticion("EL", "1") 
					        + generarSegmentoPeticion("F3", fechaFormateada)
					        + generarSegmentoPeticion("F4", horaFormateada) 
					        + generarSegmentoPeticion("EM", intTransactionID);
				}

				if(StringUtils.isNotBlank(tiendaOrigen) && StringUtils.isNotBlank(tpvOrigen) && StringUtils.isNotBlank(datosPeticion.getIdDocumentoOrigen())
				        && StringUtils.isNotBlank(datosPeticion.getFechaDocumentoOrigen())){
					peticion = peticion + generarSegmentoPeticion("E6", tiendaOrigen) 
							+ generarSegmentoPeticion("E7", tpvOrigen) 
							+ generarSegmentoPeticion("E8", datosPeticion.getIdDocumentoOrigen());
					try{
						String fechaHoraDocumentoOrigen = datosPeticion.getFechaDocumentoOrigen();
						String fechaDocumentoOrigen = fechaHoraDocumentoOrigen.split(" ")[0];
						fechaDocumentoOrigen = fechaDocumentoOrigen.substring(6, 10) + fechaDocumentoOrigen.substring(3, 5) + fechaDocumentoOrigen.substring(0, 2);
						peticion = peticion + generarSegmentoPeticion("E9", fechaDocumentoOrigen);
					}
					catch(Exception e){
						String mensajeError = "Error al procesar la fecha de la transacción original";
						log.error("generarPeticion() - " + mensajeError + " - " + e.getMessage(), e);
					}
				}
				break;
			case ANULACION:
				peticion = peticion + "05" + generarSegmentoPeticion("E0", comercio + tienda + numTPV) + generarSegmentoPeticion("E1", importeTransaccion)
				        + generarSegmentoPeticion("F3", fechaFormateada) + generarSegmentoPeticion("F4", horaFormateada) + generarSegmentoPeticion("EM", intTransactionID);
				break;
			case TOTALES:
				peticion = "PE04FN000225" + generarSegmentoPeticion("E9", fechaFormateada);
			default:
				break;
		}

		peticion = "P" + StringUtils.leftPad(Integer.toString(peticion.length()), 5, '0') + peticion;
		log.debug("generarPeticion() - Trama generada: " + peticion);

		return peticion;
	}
	
	private String enviarPeticion(String peticion) {
        log.debug("enviarPeticion() - Enviando el siguiente mensaje al servicio ConexFlow: " + peticion);

        DataInputStream in;
        DataOutputStream salida;

        try {
            in = new DataInputStream(socket.getInputStream());
            salida = new DataOutputStream(socket.getOutputStream());
    
            salida.write(peticion.getBytes());
    
            byte[] messageByte = new byte[BUFFER_LENGTH];
            boolean end = false;
            StringBuilder dataString = new StringBuilder(BUFFER_LENGTH);
            int totalBytesRead = 0;
            int totalBytesResponse = 0;
            while (!end) {
                int currentBytesRead = in.read(messageByte);
                if (currentBytesRead != -1) {
                    totalBytesRead = currentBytesRead + totalBytesRead;
                    if (totalBytesRead <= BUFFER_LENGTH) {
                        dataString.append(new String(messageByte, 0, currentBytesRead, StandardCharsets.UTF_8));
                    }
                    else {
                        dataString.append(new String(messageByte, 0, BUFFER_LENGTH - totalBytesRead + currentBytesRead, StandardCharsets.UTF_8));
                    }
                    
                    if (dataString.length() >= BUFFER_LENGTH || (totalBytesRead >= totalBytesResponse && totalBytesResponse > 0)) {
                        end = true;
                    }
                    
                    // Comprobamos el fin de la trama
                    // Del cracter al 2 al 6 de la respuesta viene la longitud de la trama, obviando
                    // la cabecera que son 6 caracteres.
                    if(StringUtils.isNotBlank(dataString.toString().trim())) {
                    	if(dataString.length()>6){
                    		totalBytesResponse = new Integer(dataString.toString().substring(2, 6));
	                        if(totalBytesRead == totalBytesResponse + 6) {
	                            end = true;
	                        }
                    	}else{
                    		end = true;
                    	}
                    }
                }
                else {
                    end = true;
                }
            }
            
            log.debug("enviarPeticion() - Se ha recibido la siguiente respuesta del servicio VisualPlugin : " + dataString.toString());
            return dataString.toString();
        } catch (SocketTimeoutException e) {
           throw new RuntimeException(I18N.getTexto("Tiempo máximo de espera alcanzado realizando la petición. Por favor, revise su conexión y vuelva a intentarlo."));    
        } catch (IOException e) {
            throw new RuntimeException(I18N.getTexto("Error de Entrada/Salida al realizar la petición"));    
        }
    }

	private String generarSegmentoPeticion(String codigo, String valor){
		String longitud = StringUtils.leftPad(Integer.toString(valor.length()), 4, '0');
		String resultado = codigo + longitud + valor;
		return resultado;
	}
	
	/**
     * Extrae los datos de la respuesta que hemos recibido.
     *
     * @param resultado
     *            : Respuesta después de realizar la petici
     * @return
     */
    private Map<String, String> extraeSegmentosRespuesta(String resultado) {
        Map<String, String> segmentos = new HashMap<String, String>();
        String respuestaOperativa = resultado.substring(18, resultado.length());
        int index = 0;
        while (index <= respuestaOperativa.length() - 1) {
            String segmento = respuestaOperativa.substring(index, index + 2);
            Integer longitud = new Integer(respuestaOperativa.substring(index + 2, index + 6));
            try {
                int indexFinal = index + 6 + longitud > respuestaOperativa.length() ? respuestaOperativa.length() : index + 6 + longitud;
                String segmentoValor = respuestaOperativa.substring(index + 6, indexFinal);
                segmentos.put(segmento, segmentoValor);
            }
            catch(Exception e) {
                log.error("extraeSegmentosRespuesta() - No se ha podido extraer el segmento: " + segmento + ": " + e.getMessage());
            }
            index = index + 6 + longitud;
        }
        return segmentos;
    }

	protected void procesarRespuestaTransaccionPago(DatosRespuestaPagoTarjeta datosRespuesta) throws TarjetaException, IOException, DispositivoException{
		log.debug("procesarRespuestaTransaccionPago() - Procesando respuesta de la transacción de pago");
		if(!datosRespuesta.getCodResult().equals("500") && !datosRespuesta.getCodResult().equals("000")){
			procesarRespuestaErronea(datosRespuesta);
		}
	}

	private void procesarRespuestaErronea(DatosRespuestaPagoTarjeta datosRespuesta) throws TarjetaException{
		log.debug("procesarRespuestaErronea() - Procesando respuesta errónea con el CompletionCode " + datosRespuesta.getCodResult());
		String msgError = I18N.getTexto("Cod. resultado: ") + datosRespuesta.getCodResult();
		if(errores.containsKey(datosRespuesta.getCodResult())){
			msgError = msgError + System.lineSeparator() + System.lineSeparator() + I18N.getTexto(errores.get(datosRespuesta.getCodResult())) + System.lineSeparator() + System.lineSeparator();
		}
		String mensajeError = "Se ha producido un error al realizar la transacción";
		log.error("procesarRespuestaErronea() - " + mensajeError);
		throw new TarjetaException(I18N.getTexto(mensajeError + System.lineSeparator() + System.lineSeparator() + msgError), datosRespuesta);
	}

	protected void guardarDatosRespuesta(ByLDatosRespuestaPagoTarjeta datosRespuesta, String resultado){
		log.debug("guardarDatosRespuesta() - Guardando la respuesta: " + resultado);

		datosRespuesta.setMsgRespuesta(resultado);

		Map<String, String> segmentos = extraeSegmentosRespuesta(resultado);
		/* CompletionCode */
		datosRespuesta.setCodResult(segmentos.get("S0"));
		codResultado = datosRespuesta.getCodResult();
		/* EnterpriseId */
		datosRespuesta.setComercio(segmentos.get("S1"));
		/* StoreId */
		datosRespuesta.setCodigoCentro(segmentos.get("S2"));
		/* BIDPos */
		datosRespuesta.setTerminalId(segmentos.get("SQ"));
		/* PosId */
		datosRespuesta.setPos(segmentos.get("S3"));
		/* CompletionDescription */
		datosRespuesta.setVerificacion(segmentos.get("S4"));
		/* AuthenticationType */
		datosRespuesta.setAuthMode(segmentos.get("S5"));

		/* Time */
		String segmentoSP = segmentos.get("SP");
		/* Date */
		String segmentoS8 = segmentos.get("S8");
		if(StringUtils.isNotBlank(segmentoSP) && StringUtils.isNotBlank(segmentoS8)){
			String hora = segmentoSP.substring(0, 2) + ":" + segmentoSP.substring(2, 4) + ":" + segmentoSP.substring(4, 6);
			String fecha = segmentoS8.substring(6, 8) + "/" + segmentoS8.substring(4, 6) + "/" + segmentoS8.substring(0, 4);
			datosRespuesta.setFechaTransaccion(fecha + " " + hora);
		}
		/* TransactionId */
		datosRespuesta.setNumTransaccion(segmentos.get("S9"));
		/* Pan */
		String pan = segmentos.get("SC");
		if(StringUtils.isNotBlank(pan)){
			datosRespuesta.setTarjeta(pan);
			datosRespuesta.setPAN(pan);
		}

		/* EntryMethod */
		datosRespuesta.setTipoLectura(segmentos.get("SB"));
		/* AuthCode */
		datosRespuesta.setCodAutorizacion(segmentos.get("SI"));
		/* BankDescription */
		datosRespuesta.setDescBanco(segmentos.get("SO"));
		/* Emv_AID */
		datosRespuesta.setAID(segmentos.get("R0"));
		/* Emv_AID_Label */
		datosRespuesta.setApplicationLabel(segmentos.get("R1"));
		/* ARC */
		datosRespuesta.setARC(segmentos.get("R2"));
		/* BIDStore */
		datosRespuesta.setFuc(segmentos.get("SR"));
		datosRespuesta.setNombredf(segmentos.get("R0"));
		/* DCC Amount */
		datosRespuesta.setDccAmount(segmentos.get("R7"));
		/* DCC Card Holder Currency ALFA */
		datosRespuesta.setDccCardHolderCurrencyAlfa(segmentos.get("R8"));
		/* DCC Card Holder Currency SYMBOL */
		datosRespuesta.setDccCardHolderCurrencySymbol(segmentos.get("R9"));
		/* DCC Decimal Places */
		datosRespuesta.setDccDecimalPlaces(segmentos.get("T0"));

		Map<String, String> adicionales = new HashMap<String, String>();
		adicionales.put("EM", intTransactionID);
		adicionales.put(COMERCIO, comercio);
		adicionales.put(TIENDA, tienda);
		adicionales.put("CentroOrigen", tienda);
		adicionales.put("TPVOrigen", numTPV);

		adicionales.put(NUM_VENTAS, segmentos.get("TG"));
		adicionales.put(IMPORTE_VENTAS, segmentos.get("TH"));
		adicionales.put(NUM_DEVOLUCIONES, segmentos.get("TI"));
		adicionales.put(IMPORTE_DEVOLUCIONES, segmentos.get("TJ"));
		Integer numAnulaciones = (segmentos.get("TK") != null ? Integer.valueOf(segmentos.get("TK")) : 0) + (segmentos.get("TM") != null ? Integer.valueOf(segmentos.get("TM")) : 0);
		Double impAnulaciones = (segmentos.get("TL") != null ? Double.valueOf(segmentos.get("TL")) : 0) + (segmentos.get("TN") != null ? Double.valueOf(segmentos.get("TN")) : 0);
		adicionales.put(NUM_ANULACIONES, numAnulaciones.toString());
		adicionales.put(IMPORTE_ANULACIONES, impAnulaciones.toString());

		adicionales.put(TECHNOLOGY_TYPE, segmentos.get("SA"));
		adicionales.put(EXPIRATION_DATE, segmentos.get("SD"));
		adicionales.put(DOCUMENT_KEY, segmentos.get("SE"));
		adicionales.put(DOCUMENT_DESCRIPTION, segmentos.get("SF"));
		adicionales.put(SESSION_KEY, segmentos.get("SG"));
		adicionales.put(SESSION_ID, segmentos.get("SH"));
		adicionales.put(REFERENCE_NUMBER, segmentos.get("SJ"));
		adicionales.put(CHARGE_TYPE, segmentos.get("SK"));
		adicionales.put(PROCESSOR_KEY, segmentos.get("SL"));
		adicionales.put(PROCESSOR_DESCRIPTION, segmentos.get("SM"));
		adicionales.put(BANK_KEY, segmentos.get("SN"));
		adicionales.put(CARD_HOLDER_NAME, segmentos.get("SS"));
		adicionales.put(DCC_CONVERSION_RATE, segmentos.get("SY"));
		adicionales.put(DCC_AMOUNT, segmentos.get("R7"));
		adicionales.put(DCC_CARD_HOLDER_CURRENCY_ALFA, segmentos.get("R8"));
		adicionales.put(DCC_CARD_HOLDER_CURRENCY_SYMBOL, segmentos.get("R9"));
		/* DccDecimalPlaces */
		adicionales.put(DCC_DECIMAL_PLACES, segmentos.get("T0"));

		/* Añadimos el Tipo de Tarjeta que se va a usar */
		adicionales.put(TIPO_CONEXFLOW, TIPO_CONEXFLOW);

		datosRespuesta.setAdicionales(adicionales);
	}

	/* ###################################################################################################### */
	/* #################################### REALIZAR PETICIONES PAGOS ####################################### */
	/* ###################################################################################################### */
	
	@Override
	protected DatosRespuestaPagoTarjeta solicitarPago(DatosPeticionPagoTarjeta datosPeticion) throws TarjetaException{
		log.debug("solicitarPago() - Iniciamos la operación de venta en ConexFlow...");
		restaurarVariables();
		/* Creamos el número de transacción por si hay que hacer una anulación */
		intTransactionID = crearNumeroTransaccion(datosPeticion);

		ByLDatosRespuestaPagoTarjeta datosRespuesta = new ByLDatosRespuestaPagoTarjeta(datosPeticion);
		datosRespuesta.setTipoTransaccion(VENTA);
		try{
			String venta = VENTA;
			conecta();

			String peticion = generarPeticion(venta, datosPeticion);
			log.debug("solicitarPago() - Enviando trama: " + peticion);

			log.debug("TIEMPO RESPUESTA : PETICIÓN DE PAGO EN CONEXFLOW : " + dateFormat.format(new Date()));

			String resultado = enviarPeticion(peticion);

			log.debug("TIEMPO RESPUESTA : PETICIÓN DE PAGO EN CONEXFLOW FINALIZADA : " + dateFormat.format(new Date()));

			log.debug("solicitarPago() - Trama recibida: " + resultado);
			guardarDatosRespuesta(datosRespuesta, resultado);
			procesarRespuestaTransaccionPago(datosRespuesta);
			transaccionBienTerminada = true;
		}
		catch(Exception e){
			try {
				enviarConfirmacion();
			} catch (IOException | DispositivoException e1) {
				log.error("Error enviando petición de confirmación", e1);
			}
			String mensajeError = "Error al realizar la operación de venta con ConexFlow";
			if(e instanceof SocketTimeoutException){
				mensajeError = "Ha expirado el tiempo de espera de ConexFlow";
			}
			else if(e.getMessage().equals("Connection refused: connect")){
				mensajeError = "No ha sido posible establecer la comunicación en el PinPad. \n\n Compruebe que el Dispositivo esta conectado y el Servicio Visual Plugin encendido";
			}
			else if(e instanceof TarjetaException){
				mensajeError = e.getMessage();
			}
			log.error("solicitarPago() - " + mensajeError + " - " + e.getMessage(), e);
			throw new TarjetaException(I18N.getTexto(mensajeError), datosRespuesta);
		}
		log.debug("solicitarPago() - Finalizada la operación de venta en ConexFlow");
		return datosRespuesta;
	}

	@Override
	protected DatosRespuestaPagoTarjeta solicitarConfirmacionPago(DatosPeticionPagoTarjeta datoPeticion) throws TarjetaException{
		log.debug("solicitarConfirmacionPago() - Solicitando confirmación de una operación de pago...");
		solicitarConfirmacion(datoPeticion);
		return null;
	}

	@Override
	protected DatosRespuestaPagoTarjeta solicitarDevolucion(DatosPeticionPagoTarjeta datosPeticion) throws TarjetaException{
		log.debug("solicitarDevolucion() - Iniciamos la solicitud para la devolución del pago...");
		restaurarVariables();
		/* Creamos el número de transacción por si hay que hacer una anulación */
		intTransactionID = crearNumeroTransaccion(datosPeticion);

		ByLDatosRespuestaPagoTarjeta datosRespuesta = new ByLDatosRespuestaPagoTarjeta(datosPeticion);
		datosRespuesta.setTipoTransaccion(DEVOLUCION);
		try{
			String devolucion = DEVOLUCION;
			conecta();

			String peticion = generarPeticion(devolucion, datosPeticion);
			log.debug("solicitarDevolucion() - Enviando trama: " + peticion);

			log.debug("TIEMPO RESPUESTA : PETICIÓN DE DEVOLUCIÓN EN CONEXFLOW : " + dateFormat.format(new Date()));

			String resultado = enviarPeticion(peticion);

			log.debug("TIEMPO RESPUESTA : PETICIÓN DE DEVOLUCIÓN EN CONEXFLOW FINALIZADA : " + dateFormat.format(new Date()));

			log.debug("solicitarDevolucion() - Trama recibida: " + resultado);
			guardarDatosRespuesta(datosRespuesta, resultado);
			procesarRespuestaTransaccionPago(datosRespuesta);
			transaccionBienTerminada = true;
		}
		catch(Exception e){
			
			try {
				enviarConfirmacion();
			} catch (IOException | DispositivoException e1) {
				log.error("Error enviando petición de confirmación", e1);
			}
			
			// Esta funcionalidad es para que si se cancela/falla el intento de devolucion automatico (EL = 1)
			// vuelva a pedir la tarjeta de manera manual.
			// Si tiene adicionales significa que acabamos de hacer un intento automatico
			if (((ConexFlowDatosPeticionPagoTarjeta) datosPeticion).getAdicionales() != null) {
				log.debug("solicitarDevolucion() - Ha fallado el proceso automatico y se pasa al manual");
				log.debug("solicitarDevolucion() - Mensaje de respuesta del proceso automatico: " + datosRespuesta.getMsgRespuesta());
				log.debug("solicitarDevolucion() - Datos adicionales del proceso automatico : " + ((ConexFlowDatosPeticionPagoTarjeta) datosPeticion).getAdicionales());
				((ConexFlowDatosPeticionPagoTarjeta) datosPeticion).setAdicionales(null);
				datosRespuesta = (ByLDatosRespuestaPagoTarjeta) solicitarDevolucion(datosPeticion);
				log.debug("solicitarDevolucion() - Mensaje de respuesta del proceso manual: " + datosRespuesta.getMsgRespuesta());
				log.debug("solicitarDevolucion() - Datos adicionales del proceso manual : " + ((ConexFlowDatosPeticionPagoTarjeta) datosPeticion).getAdicionales());
			}
			else {
				String mensajeError = "Error al realizar la operación de venta con ConexFlow";
				if (e instanceof SocketTimeoutException){
					mensajeError = "Ha expirado el tiempo de espera de ConexFlow";
				}
				else if(e.getMessage().equals("Connection refused: connect")){
					mensajeError = "No ha sido posible establecer la comunicación en el PinPad. \n\n Compruebe que el Dispositivo esta conectado y el Servicio Visual Plugin encendido";
				}
				else if(e instanceof TarjetaException){
					mensajeError = e.getMessage();
				}
				log.error("solicitarDevolucion() - " + mensajeError + " - " + e.getMessage(), e);
				throw new TarjetaException(I18N.getTexto(mensajeError), datosRespuesta);
			}
		}
		log.debug("solicitarDevolucion() - Finalziada la solicitud para la devolución del pago");
		return datosRespuesta;
	}

	@Override
	protected DatosRespuestaPagoTarjeta solicitarConfirmacionDevolucion(DatosPeticionPagoTarjeta datoPeticion) throws TarjetaException{
		log.debug("solicitarConfirmacionDevolucion() - Solicitando confirmación de una operación de devolución...");
		solicitarConfirmacion(datoPeticion);
		return null;
	}

	@Override
	public void solicitarAnulacionPago(List<DatosPeticionPagoTarjeta> datosPago, Stage stage, TarjetaCallback<List<DatosRespuestaPagoTarjeta>> callback){
		log.debug("solicitarAnulacionPago() - Iniciamos la solicitud para la anulación de pago...");
		try{
			log.debug("TIEMPO RESPUESTA : PETICIÓN DE ANULACIÓN EN CONEXFLOW : " + dateFormat.format(new Date()));
			
			if(StringUtils.isBlank(codResultado) || transaccionBienTerminada){
				enviarAnulacion(null);
			}
			else{
				log.debug("solicitarAnulacionPago() - Enviando confirmación");
				enviarConfirmacion();
			}

			log.debug("TIEMPO RESPUESTA : PETICIÓN DE ANULACIÓN EN CONEXFLOW : " + dateFormat.format(new Date()));
			log.debug("solicitarAnulacionPago() - Finalizada la solicitud para la anulación de pago");
		}
		catch(Exception e){
			String mensajeError = "Error al realizar la operación de anulación con ConexFlow";
			if(e instanceof SocketTimeoutException){
				mensajeError = "Ha expirado el tiempo de espera de ConexFlow";
			}
			else if(e.getMessage().equals("Connection refused: connect")){
				mensajeError = "No ha sido posible establecer la comunicación en el PinPad. \n\n Compruebe que el Dispositivo esta conectado y el Servicio Visual Plugin encendido";
			}
			else if(e instanceof TarjetaException){
				mensajeError = e.getMessage();
			}
			log.error("solicitarDevolucion() - " + mensajeError + " - " + e.getMessage(), e);
		}
	}

	@Override
	protected DatosRespuestaPagoTarjeta solicitarAnulacionVenta(DatosPeticionPagoTarjeta datosPeticion) throws TarjetaException{
		log.debug("solicitarAnulacionVenta() - Se va a solicitar la anulación de una venta...");
		try{
			if(datosPeticion != null){
				enviarAnulacion(datosPeticion);
			}
			else{
				enviarConfirmacion();
			}
		}
		catch(Exception e){
			String mensajeError = "Error al realizar la operación";
			log.error("solicitarAnulacionVenta() - " + mensajeError + e.getMessage(), e);
		}
		return null;
	}

	@Override
	protected DatosRespuestaPagoTarjeta solicitarAnulacionDevolucion(DatosPeticionPagoTarjeta datosPeticion) throws TarjetaException{
		log.debug("solicitarAnulacionDevolucion() - Se va a solicitar la anulación de una devolución...");
		try{
			if(datosPeticion != null){
				enviarAnulacion(datosPeticion);
			}
			else{
				enviarConfirmacion();
			}
		}
		catch(Exception e){
			String mensajeError = "Error al realizar la operación";
			log.error("solicitarAnulacionDevolucion() - " + mensajeError + e.getMessage(), e);
		}
		return null;
	}

	@Override
	public void solicitarAnulacionDevolucion(List<DatosPeticionPagoTarjeta> datosPago, Stage stage, TarjetaCallback<List<DatosRespuestaPagoTarjeta>> callback){
		new ByLSolicitarAnulacionDevolucionTask(datosPago, callback).start();
	}

	protected void enviarAnulacion(DatosPeticionPagoTarjeta datosPeticion) throws TarjetaException{
		log.debug("enviarAnulacion() - Enviando anulación con EM: " + intTransactionID);
		try{
			conecta();
			String peticion = generarPeticion(ANULACION, datosPeticion);
			log.debug("enviarAnulacion() - Enviando trama: " + peticion);
			String resultado = enviarPeticion(peticion);
			log.debug("enviarAnulacion() - Trama recibida: " + resultado);
			log.debug("enviarAnulacion() - Enviando Confirmación");
			enviarConfirmacion();
		}
		catch(Exception e){
			try {
				enviarConfirmacion();
			} catch (IOException | DispositivoException e1) {
				log.error("Error enviando petición de confirmación", e1);
			}
			String mensajeError = "Se ha producido un error al anular la transacción";
			log.error("enviarAnulacion() - " + mensajeError + e.getMessage(), e);
			throw new TarjetaException(I18N.getTexto(mensajeError), null);
		}
	}

	public class ByLSolicitarAnulacionDevolucionTask extends BackgroundTask<List<DatosRespuestaPagoTarjeta>>{

		protected List<DatosPeticionPagoTarjeta> datosPeticion;
		protected List<DatosRespuestaPagoTarjeta> datosRespuesta = new LinkedList<>();

		private TarjetaCallback<List<DatosRespuestaPagoTarjeta>> callback;

		public ByLSolicitarAnulacionDevolucionTask(List<DatosPeticionPagoTarjeta> datosPeticion, TarjetaCallback<List<DatosRespuestaPagoTarjeta>> callback){
			super(true);
			this.datosPeticion = datosPeticion;
			this.callback = callback;
		}

		@Override
		protected List<DatosRespuestaPagoTarjeta> call() throws Exception{
			datosRespuesta.add(solicitarAnulacionDevolucion(null));
			return datosRespuesta;
		}

		@Override
		protected void failed(){
			super.failed();
			if(getException() instanceof TarjetaException){
				TarjetaException e = (TarjetaException) getException();
				log.error("ByLSolicitarAnulacionDevolucionTask() - El dispositivo devolvió una respuesta con código de error: " + e.getDatoRespuestaPagoTarjeta(), e);
			}
			callback.onFailure(datosRespuesta, getException());
		}

		@Override
		protected void succeeded(){
			super.succeeded();
			callback.onSuccess(getValue());
		}
	}

	/* ################################################################################################### */
	/* #################################### PETICIONES ADICIONALES ####################################### */
	/* ################################################################################################### */

	protected void enviarConfirmacion() throws IOException, DispositivoException{
		log.debug("enviarConfirmacion() - Enviando confirmación...");

		if(socket == null){
			conecta();
		}
		
		enviarPeticion("O");
		desconecta();
	}

	private void solicitarConfirmacion(DatosPeticionPagoTarjeta datoPeticion) throws TarjetaException{
		log.debug("solicitarConfirmacion() - Solicitando confirmación...");
		try{
			enviarConfirmacion();
		}
		catch(Exception e){
			String mensajeError = "Ha habido un error al confirmar la transacción, se anularará por parte de ConexFlow cuando expire el tiempo de espera";
			log.error("solicitarConfirmacion() - " + mensajeError + " - " + e.getMessage(), e);
			throw new TarjetaException(I18N.getTexto(mensajeError), null);
		}
	}

	@Override
	protected DatosRespuestaPagoTarjeta solicitarTotales(DatosPeticionPagoTarjeta datosPeticion) throws TarjetaException{
		log.debug("solicitarTotales() - Realizando operación de totales del día");
		/* Creamos el número de transacción por si hay que hacer una anulación */
		intTransactionID = crearNumeroTransaccion(datosPeticion);

		DatosRespuestaPagoTarjeta datosRespuesta = new DatosRespuestaPagoTarjeta(datosPeticion);
		datosRespuesta.setTipoTransaccion(TOTALES);
		try{
			conecta();
			String peticion = generarPeticion(TOTALES, datosPeticion);
			log.debug("solicitarTotales() - Enviando trama: " + peticion);
			String resultado = enviarPeticion(peticion);
			log.debug("solicitarTotales() - Trama recibida: " + resultado);
			guardarDatosRespuesta((ByLDatosRespuestaPagoTarjeta) datosRespuesta, resultado);
			procesarRespuestaTransaccionPago(datosRespuesta);
		}
		catch(Exception e){
			String mensajeError = "Error al realizar la operación de venta con ConexFlow";
			if(e instanceof SocketTimeoutException){
				mensajeError = "Ha expirado el tiempo de espera de ConexFlow";
			}
			else if(e.getMessage().equals("Connection refused: connect")){
				mensajeError = "No ha sido posible establecer la comunicación en el PinPad. \n\n Compruebe que el Dispositivo esta conectado y el Servicio Visual Plugin encendido";
			}
			else if(e instanceof TarjetaException){
				mensajeError = e.getMessage();
			}
			log.error("solicitarTotales() - " + mensajeError + " - " + e.getMessage(), e);
			throw new TarjetaException(I18N.getTexto(mensajeError), datosRespuesta);
		}
		return datosRespuesta;
	}

	@Override
	protected DatosRespuestaPagoTarjeta solicitarEstado(DatosPeticionPagoTarjeta datoPeticion) throws TarjetaException{
		DatosRespuestaPagoTarjeta respuesta = new DatosRespuestaPagoTarjeta(datoPeticion);
		String mensajeError = "Operación no implementada para su uso";
		log.error("solicitarEstado() - " + mensajeError);
		throw new TarjetaException(I18N.getTexto(mensajeError), respuesta);
	}

	@Override
	protected DatosRespuestaPagoTarjeta solicitarUltimaOperacion(DatosPeticionPagoTarjeta datoPeticion) throws TarjetaException{
		DatosRespuestaPagoTarjeta respuesta = new DatosRespuestaPagoTarjeta(datoPeticion);
		String mensajeError = "Operación no implementada para su uso";
		log.error("solicitarUltimaOperacion() - " + mensajeError);
		throw new TarjetaException(I18N.getTexto(mensajeError), respuesta);
	}

	private void restaurarVariables(){
		transaccionBienTerminada = false;
		codResultado = null;
	}

	@Override
	protected DatosRespuestaPagoTarjeta realizarFinDia(DatosPeticionPagoTarjeta datoPeticion) throws TarjetaException{
		DatosRespuestaPagoTarjeta respuesta = new DatosRespuestaPagoTarjeta(datoPeticion);
		String mensajeError = "Operación no implementada para su uso";
		log.error("realizarFinDia() - " + mensajeError);
		throw new TarjetaException(I18N.getTexto(mensajeError), respuesta);
	}

}
