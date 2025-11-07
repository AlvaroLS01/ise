package com.comerzzia.bimbaylola.pos.util.textos;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class TextUtils{

	private static TextUtils instance;

	public static TextUtils getInstance(){
		if (instance == null){
			instance = new TextUtils();
		}
		return instance;
	}

	public static void setCustomInstance(TextUtils instance){
		TextUtils.instance = instance;
	}

	/**
	 * Formatea un texto para dividirlos en lineas respecto a máximo recibido.
	 * @param texto : Texto que se desea dividir.
	 * @param maxCaracteres : Máximo de caracteres.
	 * @return
	 */
	public List<String> divideLines(String texto, int maxCaracteres, String separador){
		List<String> lineas = new ArrayList<String>();

		if(StringUtils.isBlank(separador)){
			separador = System.lineSeparator();
		}
		if(separador.equals("|")){
			separador = "\\|";
		}
		
		String[] lineasIntroducidas = texto.split(separador);
		for(int i = 0; i < lineasIntroducidas.length; i++){
			String lineaIntroducida = lineasIntroducidas[i];
			if(lineaIntroducida.length() <= maxCaracteres){
				lineas.add(lineaIntroducida);
			}else{
				String[] palabrasLinea = lineaIntroducida.split(" ");
				String linea = "";
				for (int j = 0; j < palabrasLinea.length; j++){
					String palabra = palabrasLinea[j];
					
					if(linea.length() + 1 + palabra.length() < maxCaracteres){
						if(StringUtils.isNotBlank(linea)){
							linea = linea + " " + palabra;
						}else{
							linea = palabra;
						}
					}else{
						lineas.add(linea);
						linea = palabra;
					}
					if(j==palabrasLinea.length-1){
						lineas.add(linea);
					}
				}
			}
		}
		return lineas;
	}

}

