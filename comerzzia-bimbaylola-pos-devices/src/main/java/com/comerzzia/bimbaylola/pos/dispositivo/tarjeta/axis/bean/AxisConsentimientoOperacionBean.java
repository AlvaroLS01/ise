package com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.axis.bean;

/**
 * Para leer la respuesta JSON del consentimiento de Axis
 * "Operation":{"Name":"InfoCheckBox"}
 */
public class AxisConsentimientoOperacionBean{

	private String Name;

	public String getName(){
		return Name;
	}
	public void setName(String Name){
		this.Name = Name;
	}
	
}
