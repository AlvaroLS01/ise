package com.comerzzia.bimbaylola.pos.gui.i18n.fxml;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class MensajesExt {

	/**Llamado desde el gettext-maven-plugin*/
	public static List<String> getTextos() throws MensajesExceptionExt {
		
		List<String> textos = new LinkedList<String>();
		
		try {
			Map<String, String> mapGettextFxml = MensajesFXMLExt.obtenerCadenas();
			
			Map<String, String> mapGettextxml = MensajesBotoneraExt.obtenerCadenas();
			
			Map<String, String> mapGettextValidacion = MensajesValidacionExt.obtenerCadenas();
			
			List<String> cadenasDefecto = MensajesDefectoExt.obtenerCadenas();

			for (String cadena : cadenasDefecto) {
				textos.add(cadena);
			}
			
			for (Entry<String, String> entrada : mapGettextFxml.entrySet()) {
				String value = entrada.getValue();
				if(value.startsWith("%")){
					value = value.substring(1, value.length());
				}
				textos.add(value);
			}
			
			for (Entry<String, String> entrada : mapGettextxml.entrySet()) {
				String value = entrada.getValue();
				if(value.startsWith("<texto>")){
					value = value.substring(7, value.length()-7);
				}
				textos.add(value);
			}
			
			for (Entry<String, String> entrada : mapGettextValidacion.entrySet()) {
				String value = entrada.getValue();
				textos.add(value);
			}
			
			try {
				//Es necesario configurar la conexión a base de datos en la clase MensajesBaseDatosExt
				Map<String, String> cadenasBD = MensajesBaseDatosExt.obtenerCadenas();
				
				for (Entry<String, String> entrada : cadenasBD.entrySet()) {
					String value = entrada.getValue();
					textos.add(value);
				}
			}catch(Exception e1){
				System.err.println("getTextos() - Error: " + e1.getMessage());
			}
		}
		catch (Exception e) {
			showError(e);
		}
		finally{
		}
		
		for(String texto : textos) {
			System.out.println(texto);
		}
		
		return textos;
	}
	
	private static void showError(Exception e){
		System.err.println("\nSe ha producido un error: "+e.getMessage());
		System.err.println("\nDetalles:\n");
		e.printStackTrace();
		System.err.println("===================================================================");
	}
}