package com.comerzzia.bricodepot.backoffice.services.variables;

import java.util.HashMap;
import java.util.Map;

public class CustomVariables {
	public static final String GENERAL_VERSION = "GENERAL.VERSION";
	public static final String APLICACION_TITULO = "APLICACION.TITULO";
	public static final String USUARIO_EMPRESA = "USUARIO.EMPRESA";
	public static final String ARTICULOS_DESGLOSE1_TITULO = "ARTICULOS.DESGLOSE1_TITULO";
	public static final String ARTICULOS_DESGLOSE2_TITULO = "ARTICULOS.DESGLOSE2_TITULO";
	public static final String TARIFAS_REDONDEO_NIVEL1 = "TARIFAS.REDONDEO_NIVEL1";
	public static final String TARIFAS_REDONDEO_NIVEL2 = "TARIFAS.REDONDEO_NIVEL2";
	public static final String TARIFAS_REDONDEO_NIVEL3 = "TARIFAS.REDONDEO_NIVEL3";
	public static final String PROCESADOR_PROCESAMIENTO_AUTOMATICO = "PROCESADOR.PROCESAMIENTO_AUTOMATICO";
	public static final String PROCESADOR_PROCESAMIENTO_AUTOMATICO_TIEMPO = "PROCESADOR.PROCESAMIENTO_AUTOMATICO_TIEMPO";
	public static final String TICKETS_USA_DESCUENTO_EN_LINEA = "TICKETS.USA_DESCUENTO_EN_LINEA";
	public static final String TICKETS_PERMITE_CAMBIO_PRECIO = "TICKETS.PERMITE_CAMBIO_PRECIO";
	public static final String TPV_PERMITIR_VENTA_PRECIO_CERO = "TPV.PERMITIR_VENTA_PRECIO_CERO";
	public static final String TPV_COLUMNA_VENDEDOR_VISIBLE = "TPV.COLUMNA_VENDEDOR_VISIBLE";
	public static final String PROMOCIONES_TRATAR_LINEAS_DEVOLUCION = "PROMOCIONES.TRATAR_LINEAS_DEVOLUCION";
	public static final String PROMOCIONES_APLICACION_PREVIA_DE_PROMOCION_PRECIO = "PROMOCIONES.APLICACION_PREVIA_DE_PROMOCION_PRECIO";
	public static final String TPV_PERMITIR_CAMBIO_DEVOLUCION = "TPV.PERMITIR_CAMBIO_DEVOLUCION";
	public static final String CAJA_REINTENTOS_CIERRE = "CAJA.REINTENTOS_CIERRE";
	public static final String CAJA_CIERRE_CAJA_DIARIO_OBLIGATORIO = "CAJA.CIERRE_CAJA_DIARIO_OBLIGATORIO";
	public static final String CAJA_APERTURA_AUTOMATICA = "CAJA.APERTURA_AUTOMATICA";
	public static final String SINCRONIZADOR_ARRANQUE_AUTOMATICO = "SINCRONIZADOR.ARRANQUE_AUTOMATICO";
	public static final String SINCRONIZADOR_MAX_TRABAJOS_SIMULTANEOS = "SINCRONIZADOR.MAX_TRABAJOS_SIMULTANEOS";
	public static final String SINCRONIZADOR_VISTA_POR_DEFECTO = "SINCRONIZADOR.VISTA_POR_DEFECTO";
	public static final String SINCRONIZADOR_ENVIAR_CODIGOS_POSTALES = "SINCRONIZADOR.ENVIAR_CODIGOS_POSTALES";
	public static final String WEBSERVICES_WSSINCRONIZACION = "WEBSERVICES.WSSINCRONIZACION";
	public static final String TPV_VERSION = "TPV.VERSION";
	public static final String ARTICULOS_CODIGOS_BARRAS_FORMATO = "ARTICULOS.FORMATO_CODBAR_AUTOMATICO";
	public static final String EAN13 = "EAN13";
	public static final String EAN8 = "EAN8";
	public static final String WEBSERVICES_APIKEY = "WEBSERVICES.APIKEY";
	public static final String REST_URL = "REST.URL";
	public static final String REST_OMNICHANNEL_URL = "REST.OMNICHANNEL.URL";
	public static final String CLIENTES_PREFIJO_ALTA = "CLIENTES.PREFIJO_ALTA";
	public static final String CLIENTES_LONGITUD_CODIGO = "CLIENTES.LONGITUD_CODIGO";
	public static final String SINCRONIZADOR_DIAS_RETENCION_ACTUALIZACIONES = "SINCRONIZADOR.DIAS_RETENCION_ACTUALIZACIONES";
	public static final String TPV_TRATAR_PROMOCIONES_MENOS_INGRESO = "TPV.TRATAR_PROMOCIONES_MENOS_INGRESO";
	public static final String CACHE_INTERVALO_COMPRUEBA_VERSION = "CACHE.INTERVALO_COMPRUEBA_VERSION";
	public static final String CACHE_INTERVALO_LIMPIEZA = "CACHE.INTERVALO_LIMPIEZA";
	public static final String CAJA_IMPORTE_MAXIMO_DESCUADRE = "CAJA.IMPORTE_MAXIMO_DESCUADRE";
	public static final String FIDELIZACION_TIPO_TARJETA_ALTA_AUTOMATICA = "FIDELIZACION.TIPO_TARJETA_ALTA_AUTOMATICA";
	public static final String FIDELIZACION_ALTA_AUTOMATICA_USUARIO = "FIDELIZACION.ALTA_AUTOMATICA_USUARIO";
	public static final String CONTADORES_CARACTER_SEPARADOR = "CONTADORES.CARACTER_SEPARADOR";
	public static final String STOCK_TITULO_A = "STOCK.TITULO_A";
	public static final String STOCK_TITULO_B = "STOCK.TITULO_B";
	public static final String STOCK_TITULO_C = "STOCK.TITULO_C";
	public static final String STOCK_TITULO_D = "STOCK.TITULO_D";
	public static final String ARTICULOS_CONTROL_SURTIDO = "ARTICULOS.CONTROL_SURTIDO";
	public static final String FIDELIZACION_VALIDACION_CANJEO_CUPONES = "FIDELIZACION.VALIDACION_CANJEO_CUPONES";
	public static final String FIDELIZACION_PUNTOS_FACTOR_CONVERSION = "FIDELIZACION.PUNTOS.FACTOR_CONVERSION";
	public static final String FIDELIZACION_PUNTOS_PROMOCION_CANJEO = "FIDELIZACION.PUNTOS.PROMOCION_CANJEO";
	public static final String FIDELIZACION_PUNTOS_TIPO_CUPON_CANJEO = "FIDELIZACION.PUNTOS.TIPO_CUPON_CANJEO";
	public static final String REST_API_V2_URL = "REST.API-V2.URL";
	public static final String CAJA_IMPORTE_AVISO_RETIRADA = "CAJA.IMPORTE_AVISO_RETIRADA";
	public static final String CAJA_IMPORTE_BLOQUEO_RETIRADA = "CAJA.IMPORTE_BLOQUEO_RETIRADA";
	public static final String DIAS_DEVOLUCION_PERMITIDA = "TPV.MAX_DIAS_PERMITIDOS_DEVOLUCION";
	public static final String FIDELIZACION_COD_COLECTIVO_PROFESIONAL = "FIDELIZACION.COD_COLECTIVO_PROFESIONAL";
	protected Map<String, String> variables;
	protected Map<String, String> administracion;

	public CustomVariables() {
		this.variables = new HashMap<String, String>();
		variables.put(ARTICULOS_DESGLOSE1_TITULO, "");
		variables.put(ARTICULOS_DESGLOSE2_TITULO, "");
		variables.put(TARIFAS_REDONDEO_NIVEL1, "");
		variables.put(TARIFAS_REDONDEO_NIVEL2, "");
		variables.put(TARIFAS_REDONDEO_NIVEL3, "");
		variables.put(TICKETS_USA_DESCUENTO_EN_LINEA, "");
		variables.put(ARTICULOS_CODIGOS_BARRAS_FORMATO, "");
		variables.put(TPV_PERMITIR_VENTA_PRECIO_CERO, "");
		variables.put(PROMOCIONES_APLICACION_PREVIA_DE_PROMOCION_PRECIO, "");
		variables.put(CAJA_REINTENTOS_CIERRE, "");
		variables.put(WEBSERVICES_APIKEY, "");
		variables.put(CAJA_CIERRE_CAJA_DIARIO_OBLIGATORIO, "");
		variables.put(CAJA_APERTURA_AUTOMATICA, "");
		variables.put(TICKETS_PERMITE_CAMBIO_PRECIO, "");
		variables.put(TPV_COLUMNA_VENDEDOR_VISIBLE, "");
		variables.put(TPV_TRATAR_PROMOCIONES_MENOS_INGRESO, "");
		variables.put(STOCK_TITULO_A, "");
		variables.put(STOCK_TITULO_B, "");
		variables.put(STOCK_TITULO_C, "");
		variables.put(STOCK_TITULO_D, "");
		variables.put(CAJA_IMPORTE_MAXIMO_DESCUADRE, "");
		variables.put(FIDELIZACION_TIPO_TARJETA_ALTA_AUTOMATICA, "");
		variables.put(FIDELIZACION_ALTA_AUTOMATICA_USUARIO, "");
		variables.put(CONTADORES_CARACTER_SEPARADOR, "");
		variables.put(ARTICULOS_CONTROL_SURTIDO, "");
		variables.put(CAJA_IMPORTE_AVISO_RETIRADA, "");
		variables.put(CAJA_IMPORTE_BLOQUEO_RETIRADA, "");
		variables.put(DIAS_DEVOLUCION_PERMITIDA, "");

		this.administracion = new HashMap<String, String>();
		administracion.put(SINCRONIZADOR_ARRANQUE_AUTOMATICO, "");
		administracion.put(SINCRONIZADOR_MAX_TRABAJOS_SIMULTANEOS, "");
		administracion.put(PROCESADOR_PROCESAMIENTO_AUTOMATICO, "");
		administracion.put(PROCESADOR_PROCESAMIENTO_AUTOMATICO_TIEMPO, "");
		administracion.put(WEBSERVICES_WSSINCRONIZACION, "");
		administracion.put(SINCRONIZADOR_VISTA_POR_DEFECTO, "");
		administracion.put(REST_URL, "");
		administracion.put(SINCRONIZADOR_DIAS_RETENCION_ACTUALIZACIONES, "");
		administracion.put(REST_OMNICHANNEL_URL, "");
		administracion.put(REST_API_V2_URL, "");
	}

	public Map<String, String> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, String> variables) {
		this.variables = variables;
	}

	public Map<String, String> getAdministracion() {
		return administracion;
	}

	public void setAdministracion(Map<String, String> administracion) {
		this.administracion = administracion;
	}
}
