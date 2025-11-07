package com.comerzzia.bimbaylola.pos.dispositivo.impresora.spark130f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.dispositivo.impresora.spark130f.gui.ConfiguracionSpark130FView;
import com.comerzzia.bimbaylola.pos.services.spark130f.Spark130FConstants;
import com.comerzzia.bimbaylola.pos.services.spark130f.Spark130FService;
import com.comerzzia.bimbaylola.pos.services.spark130f.exception.Spark130FException;
import com.comerzzia.pos.core.gui.POSApplication;
import com.comerzzia.pos.dispositivo.impresora.ImpresoraDriver;
import com.comerzzia.pos.util.i18n.I18N;

import javafx.stage.Stage;

@Component
public class Spark130F extends ImpresoraDriver {

	public static final String PARAMETRO_SALIDA_CONFIGURACION = "salida_configuracion";
	public static final String IP = "IP";
	public static final String NOMBRE_CONEXION_FISCAL_PRINTER = "Fiscal Printer";

	private static Logger log = Logger.getLogger(Spark130F.class);
	private Spark130FService spark130FService;

	@SuppressWarnings("unchecked")
	public void configurar(Stage stage) {
		log.debug("configurar() - Abrienda pantalla de configuración de Spark130F ...");
		super.configurar(stage);

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(PARAMETRO_SALIDA_CONFIGURACION, getConfiguracion().getParametrosConfiguracion());

		POSApplication.getInstance().getMainView().showModalCentered(ConfiguracionSpark130FView.class, params, stage);

		/*
		 * Guardamos en sesión los datos de configuración, realmente no están guardado del todo hasta que no se Acepta
		 * los cambios en la pantalla de "Configuración TPV"
		 */
		if (params.containsKey(PARAMETRO_SALIDA_CONFIGURACION)) {
			Map<String, String> parametrosConfiguracion = (Map<String, String>) params.get(PARAMETRO_SALIDA_CONFIGURACION);
			getConfiguracion().setParametrosConfiguracion(parametrosConfiguracion);
			log.debug("configurar() - Parámetros de configuración de Spark130F nuevos : " + parametrosConfiguracion);
		}
	}

	@Override
	public boolean isConfigurable() {
		if (StringUtils.isNotBlank(getConfiguracion().getNombreConexion()) && getConfiguracion().getNombreConexion().equals(NOMBRE_CONEXION_FISCAL_PRINTER)) {
			return true;
		}

		return false;
	}

	@Override
	public void empezarDocumento(Map<String, Object> datos) {
		try {
			spark130FService = new Spark130FService();
			String respuesta = spark130FService.realizarLlamada("<SNonFD R='0'/>");

			List<String> listaCampos = new ArrayList<String>();
			listaCampos.add(Spark130FConstants.ATT_RC);
			HashMap<String, String> mapaCampos = spark130FService.getCamposRespuesta(respuesta, listaCampos);
			String returnCode = mapaCampos.get(Spark130FConstants.ATT_RC);

			if (!returnCode.equals(Spark130FConstants.NO_ERROR)) {
				Map<String, String> mapaErrores = Spark130FConstants.setErrors();

				String errorDesc = mapaErrores.get(returnCode);
				throw new Spark130FException(errorDesc);
			}

		}
		catch (Spark130FException e) {
			log.error("empezarDocumento() - " + e.getMessage(), e);
		}
	}

	@Override
	public void empezarLinea(int size, int lineCols) {
	}

	@Override
	public void imprimirTexto(String texto, Integer size, String align, Integer style, String fontName, int fontSize) {
		String alineacion = "0";

		if (align != null && size != null) {
			if ("center".equals(align)) {
				alineacion = "1";
			}
			else if ("right".equals(align)) {
				alineacion = "2";
			}
			else if ("left".equals(align)) {
				alineacion = "0";
			}
		}
		else {
			if (size == null) {
				log.error(I18N.getTexto("imprimirTexto() - Argumento inválido: Alineamiento [" + align + "] con tamaño nulo. Utilizamos alineación izquierda"));
			}
		}

		try {
			String cadena = "<AddTXT FN='00020" + alineacion + "'><Str> " + texto + "</Str></AddTXT>";
			String respuesta = spark130FService.realizarLlamada(cadena);

			List<String> listaCampos = new ArrayList<String>();
			listaCampos.add(Spark130FConstants.ATT_RC);
			HashMap<String, String> mapaCampos = spark130FService.getCamposRespuesta(respuesta, listaCampos);
			String returnCode = mapaCampos.get(Spark130FConstants.ATT_RC);

			if (!returnCode.equals(Spark130FConstants.NO_ERROR)) {
				Map<String, String> mapaErrores = Spark130FConstants.setErrors();

				String errorDesc = mapaErrores.get(returnCode);
				throw new Spark130FException(errorDesc);
			}
		}
		catch (Spark130FException e) {
			log.error("imprimirTexto() - " + e.getMessage(), e);
		}
	}

	@Override
	public void imprimirCodigoBarras(String codigoBarras, String tipo, String alineacion, int tipoLeyendaNumericaCodBar, int height) {
		try {
			String cadena = "<AddBC TYPE='" + (tipo == null ? "8" : tipo) + "' X='2' Y='50'><VAL>" + codigoBarras + "</VAL></AddBC>";
			String respuesta = spark130FService.realizarLlamada(cadena);

			List<String> listaCampos = new ArrayList<String>();
			listaCampos.add(Spark130FConstants.ATT_RC);
			HashMap<String, String> mapaCampos = spark130FService.getCamposRespuesta(respuesta, listaCampos);
			String returnCode = mapaCampos.get(Spark130FConstants.ATT_RC);

			if (!returnCode.equals(Spark130FConstants.NO_ERROR)) {
				Map<String, String> mapaErrores = Spark130FConstants.setErrors();

				String errorDesc = mapaErrores.get(returnCode);
				throw new Spark130FException(errorDesc);
			}
		}
		catch (Spark130FException e) {
			log.error("imprimirCodigoBarras() - " + e.getMessage(), e);
		}
	}
	
	@Override
	public void terminarLinea() {
	}

	@Override
	public void terminarDocumento() {
		try {
			String respuesta = spark130FService.realizarLlamada("<ENonFD/>");

			List<String> listaCampos = new ArrayList<String>();
			listaCampos.add(Spark130FConstants.ATT_RC);
			HashMap<String, String> mapaCampos = spark130FService.getCamposRespuesta(respuesta, listaCampos);
			String returnCode = mapaCampos.get(Spark130FConstants.ATT_RC);

			if (!returnCode.equals(Spark130FConstants.NO_ERROR)) {
				Map<String, String> mapaErrores = Spark130FConstants.setErrors();

				String errorDesc = mapaErrores.get(returnCode);
				throw new Spark130FException(errorDesc);
			}
//			else {
//				respuesta = spark130FService.realizarLlamada("<Print/>");
//				listaCampos = new ArrayList<String>();
//				listaCampos.add(Spark130FConstants.ATT_RC);
//				mapaCampos = spark130FService.getCamposRespuesta(respuesta, listaCampos);
//				returnCode = mapaCampos.get(Spark130FConstants.ATT_RC);
//
//				if (!returnCode.equals(Spark130FConstants.NO_ERROR)) {
//					Map<String, String> mapaErrores = Spark130FConstants.setErrors();
//
//					String errorDesc = mapaErrores.get(returnCode);
//					throw new Spark130FException(errorDesc);
//				}
//			}

		}
		catch (Spark130FException e) {
			log.error("terminarDocumento() - " + e.getMessage(), e);
		}
	}

}
