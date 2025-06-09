package com.comerzzia.instoreengine.herbnav.servicios.trabajos.articulosbalanza;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.comerzzia.core.model.empresas.ConfigEmpresaBean;
import com.comerzzia.core.servicios.empresas.Empresa;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.servicios.variables.ServicioVariablesImpl;
import com.comerzzia.instoreengine.herbnav.persistencia.trabajos.articulosbalanza.ArticuloBalanza;
import com.comerzzia.instoreengine.herbnav.util.HerbNavUtils;
import com.comerzzia.instoreengine.util.AppISE;

public class GeneraArticulosBalanzaJob implements Job {

	protected static Logger log = Logger.getLogger(GeneraArticulosBalanzaJob.class);

	protected DatosSesionBean datosSesion;

	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.debug("execute() - Ejecutando trabajo GeneraArticulosBalanzaJob");

		JobDataMap dataMap = context.getMergedJobDataMap();

		try {
			String archivoCSV = generarCSV(dataMap);
			subirFicheroFTP(archivoCSV);
			dataMap.put("observaciones", "Articulos balanza generados con éxito");
		}
		catch (Throwable e) {
			log.error("execute() - Error subiendo fichero al FTP: " + e.getMessage(), e);
			dataMap.put("exception", e);
		}

	}

	private String generarCSV(JobDataMap dataMap) {
	    log.debug("generarCSV() - Generando CSV con artículos balanza...");

	    String archivoCSV = "";
	    try {
	        datosSesion = new DatosSesionBean();
	        datosSesion.setUidActividad(AppISE.getPosConfig().getPosConfigDatos().getUidActividad());
	        ConfigEmpresaBean config = new ConfigEmpresaBean();
	        config.setUidActividad(datosSesion.getUidActividad());
	        Empresa empresa = new Empresa(null, config);
	        datosSesion.setEmpresa(empresa);

	        /* Fichero */
	        String fechaActual = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
	        archivoCSV = AppISE.directorioRaiz + "/" + fechaActual + ".csv";
	        File file = new File(archivoCSV);

	        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
	             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {

	            // Escribir BOM UTF-8
	            bufferedWriter.write('\uFEFF');

	            // Cabecera
	            inicializarCSV(bufferedWriter);

	    		DecimalFormat df = new DecimalFormat();
	    		df.setMaximumFractionDigits(2);
	    		df.setMinimumFractionDigits(0);
	    		df.setGroupingUsed(false);
	    		
	            // ArtículosBalanza
	            List<ArticuloBalanza> articulosBalanza = obtenerArticulosBalanza(dataMap);
	            for (ArticuloBalanza articulo : articulosBalanza) {
	                escribirArticulo(articulo, bufferedWriter, df);
	            }
	        }

	    } catch (Exception e) {
	        log.error("generarCSV() - Error al generar el CSV de artículos balanza: " + e.getMessage(), e);
	        dataMap.put("exception", e);
	    } catch (Throwable e) {
	        log.error("generarCSV() - Error al generar el CSV de artículos balanza: " + e.getMessage(), e);
	        dataMap.put("exception", e);
	    }

	    log.debug("generarCSV() - Fichero CSV generado: " + archivoCSV);
	    return archivoCSV;
	}

	private void inicializarCSV(BufferedWriter writer) throws IOException {
		log.debug("inicializarCSV() - Escribiendo la cabecera en el CSV");

		StringBuilder cabecera = new StringBuilder();
        cabecera.append("Codigo").append(";")
                .append("Nombre articulo").append(";")
                .append("Tipo").append(";")
                .append("Precio").append(";")
                .append("Texto 01").append(";")
                .append("Dias Caducidad");

        writer.write(cabecera.toString());
        writer.newLine();
	}
	
	private void escribirArticulo(ArticuloBalanza articuloBalanza, BufferedWriter writer, DecimalFormat df) throws IOException {
		log.debug("escribirArticulo() - Escribiendo en la hoja el artículo " + articuloBalanza.getCodArt());
		
		String precio = df.format(articuloBalanza.getPrecioTotal());
	    String tipo = articuloBalanza.getTipo() == null ? "U" :articuloBalanza.getTipo();
	    String observaciones = articuloBalanza.getObservaciones();
	    if(StringUtils.isNotBlank(articuloBalanza.getExtraObservaciones())){
	    	observaciones = observaciones.concat(articuloBalanza.getExtraObservaciones());
	    }
		
		StringBuilder contenido = new StringBuilder();
	    contenido.append(escape(articuloBalanza.getCodArt())).append(";");
	    contenido.append(escape(articuloBalanza.getDesArt())).append(";");
	    contenido.append(escape(tipo)).append(";");
	    contenido.append(precio).append(";");
	    contenido.append(escape(observaciones)).append(";");
	    contenido.append(articuloBalanza.getDiasCaducidad());
	    writer.write(contenido.toString());
	    writer.newLine();
	}

	private String escape(String value) {
	    if (value == null) return "";
	    return value.replace(";", ","); // Para evitar conflictos con el separador
	}

	@SuppressWarnings("deprecation")
	private void subirFicheroFTP(String archivoCSV) throws Throwable {
		log.debug("subirFicheroFTP() - Subiendo fichero CSV al FTP");
		FTPClient ftpClient = new FTPClient();
		FileInputStream fis = null;
		File file = new File(archivoCSV);
		try {

			String host = ServicioVariablesImpl.get().consultarValor(datosSesion, HerbNavUtils.VARIABLE_X_BALANZA_FTP_HOST);
			String port = ServicioVariablesImpl.get().consultarValor(datosSesion, HerbNavUtils.VARIABLE_X_BALANZA_FTP_PORT);
			String user = ServicioVariablesImpl.get().consultarValor(datosSesion, HerbNavUtils.VARIABLE_X_BALANZA_FTP_USER);
			String password = ServicioVariablesImpl.get().consultarValor(datosSesion, HerbNavUtils.VARIABLE_X_BALANZA_FTP_PASS);
			String path = ServicioVariablesImpl.get().consultarValor(datosSesion, HerbNavUtils.VARIABLE_X_BALANZA_FTP_RUTA_IMPORTACION);
			String pathCopia = ServicioVariablesImpl.get().consultarValor(datosSesion, HerbNavUtils.VARIABLE_X_BALANZA_FTP_RUTA_IMPORTACION2);

			log.debug("subirFicheroFTP() - Datos de conexion del FTP");
			log.debug("subirFicheroFTP() - Url: " + host);
			log.debug("subirFicheroFTP() - Puerto: " + port);
			log.debug("subirFicheroFTP() - Usuario: " + user);
			log.debug("subirFicheroFTP() - Contraseña: " + password);
			log.debug("subirFicheroFTP() - Ruta: " + path);

			fis = new FileInputStream(file);

			ftpClient.connect(InetAddress.getByName(host), new Integer(port));
			ftpClient.enterLocalPassiveMode();
			ftpClient.login(user, password);

			ftpClient.changeWorkingDirectory(path);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			boolean subida = ftpClient.storeFile(file.getName(), fis);
			fis.close(); 
			
			// Abrir un nuevo FileInputStream para la copia
			fis = new FileInputStream(file);

			// Cambiamos al directorio donde se guardará la copia del ticket de balanza
			ftpClient.changeWorkingDirectory(pathCopia);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

			// Cambiamos el nombre del fichero para la copia
			String nombreCopia = file.getName().replaceFirst("\\.csv$", "_copia.csv");

			// Subimos al directorio la copia del ticket de balanza con el nuevo nombre
			boolean subidaCopia = ftpClient.storeFile(nombreCopia, fis);

			if (!subida && !subidaCopia) {
				throw new Exception("No se ha podido subir el fichero al ftp ni su copia");
			}
			try {
				fis.close();
			}
			catch (Exception e) {
				log.error("subirFicheroFTP() - Error al cerrar el inputStream " + e.getMessage(), e);
			}
			boolean borrado = file.delete();
			if (!borrado) {
				log.error("subirFicheroFTP() - Se ha subido el fichero: " + file.getAbsolutePath() + " al FTP pero ha habido un error al borrarlo");
			}

		}
		catch (Exception e) {
			log.error("subirFicheroFTP() - Ha ocurrido un error al subir el fichero: " + file.getAbsolutePath() + " al FTP: " + e.getMessage(), e);
			throw e;
		}
		finally {
			try {
				ftpClient.disconnect();
			}
			catch (Exception e) {
				log.error("subirFicheroFTP() - Error al hacer desconexión de FTP: " + e.getMessage(), e);
			}

		}
	}

	private List<ArticuloBalanza> obtenerArticulosBalanza(JobDataMap dataMap) {
		log.debug("obtenerArticulosBalanza() - Obteniendo los artículos con los que rellenar el fichero CSV");

		List<ArticuloBalanza> articulosBalanza = new ArrayList<>();
		try {
			String uidActividad = AppISE.getPosConfig().getPosConfigDatos().getUidActividad();
			articulosBalanza = ArticulosBalanzaService.get().consultarArticulosBalanza(uidActividad, datosSesion);
		}
		catch (Exception e) {
			log.error("obtenerArticulosBalanza() - Error al obtener los articulos balanza: " + e.getMessage(), e);
			dataMap.put("exception", e);
		}

		return articulosBalanza;
	}

}
