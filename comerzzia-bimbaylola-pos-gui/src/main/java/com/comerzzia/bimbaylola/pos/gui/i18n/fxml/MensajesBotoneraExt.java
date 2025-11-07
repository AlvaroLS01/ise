package com.comerzzia.bimbaylola.pos.gui.i18n.fxml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.SystemUtils;


public class MensajesBotoneraExt {
	
	private static final String EXTENSION_XML = "_panel.xml";
	private static final String EXTENSION_ADIC = "_adic_"; //Paneles adicionales
	
	private static Map<String, String> mapGettextXml = new LinkedHashMap<String, String>();
	
	public static Map<String, String> obtenerCadenas() throws MensajesExceptionExt {
		Writer fstreamDebug = null;
		BufferedWriter outDebug = null;
		try {
			
			//=======================================================================================//
			// 								EXTRAER LAS CADENAS									 	 //
			//=======================================================================================//
			
			//Añadimos los directorios a una lista. De esta forma se podrá definir más de un directorio evitando tener que recorrer
			//el proyecto entero y así ahorrarnos el recorrido de directorios que no interesan como pueden ser las clases java o los
			//propios directorios de subvesion (.svn).
			List<String> listDirectorios = new ArrayList<String>();
			
			String path = MensajesExt.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "../../../comerzzia-bimbaylola-pos-skin/src/main/resources/skins/bimbaylola";
			if (SystemUtils.IS_OS_WINDOWS) {
				path = path.substring(1); //En Windows hay que quitar el primer carácter /
			}
			path = path.replace("%20", " ");
			listDirectorios.add(path);
			
			
			for(String sDirectorio: listDirectorios){
				listarDirectorio(sDirectorio, fstreamDebug);
			}
		}
		catch (Exception e) {
			throw new MensajesExceptionExt(e.getMessage(), e);
		}
		finally {
			try {
				if (outDebug != null) {
					outDebug.close();
				}
			}
			catch (IOException e) {
				throw new MensajesExceptionExt(e.getMessage());
			}
		}
		
		return mapGettextXml;
	}
	
	private static void listarDirectorio(String sDirectorio, Writer fstreamDebug) throws IOException {
		DirectoryStream.Filter<Path> filter = new DirectoryStream.Filter<Path>() {
	         public boolean accept(Path file) throws IOException {
	             return !".svn".equals(file.getFileName().toString());
	         }
	     };

		
		DirectoryStream<Path> newDirectoryStream = Files.newDirectoryStream(FileSystems.getDefault().getPath(sDirectorio), filter);

		String patternFXML = "<texto>(.*?)</texto>";
		Pattern pattern = Pattern.compile(patternFXML);

		Iterator<Path> iterator = newDirectoryStream.iterator();
		while(iterator.hasNext()) {
			Path next = iterator.next();
			File file = next.toFile();
			if (file.isDirectory()) {
				listarDirectorio(next.toString(), fstreamDebug);
			}
			else {
				if (file.getName().endsWith(EXTENSION_XML) || file.getName().contains(EXTENSION_ADIC)) {
					try {
						Matcher matcher = pattern.matcher(fromFile(file.getPath()));

						while (matcher.find()) {
							String match = matcher.group(1);
							if(match != null && !match.isEmpty()) {
								if(!mapGettextXml.containsKey(match)) {
									mapGettextXml.put(match, match);
								}
							}
						}
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private static CharSequence fromFile(String filename) throws IOException {
		FileInputStream input = new FileInputStream(filename);
		FileChannel channel = input.getChannel();

		ByteBuffer bbuf = channel.map(FileChannel.MapMode.READ_ONLY, 0, (int) channel.size());
		CharBuffer cbuf = Charset.forName("UTF-8").newDecoder().decode(bbuf);
		input.close();
		
		return cbuf;
	}
}
