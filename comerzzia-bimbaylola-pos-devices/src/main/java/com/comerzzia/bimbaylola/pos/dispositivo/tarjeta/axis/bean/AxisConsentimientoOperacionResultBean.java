package com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.axis.bean;

/**
 * Para leer la respuesta JSON del consentimiento de Axis
 * "OperationResult":{"Status":"Success","MainCondition":true,"SecondaryCondition":true}
 */
public class AxisConsentimientoOperacionResultBean{

	private String Status;
	private Boolean MainCondition;
	private Boolean SecondaryCondition;
	
	public String getStatus(){
		return Status;
	}
	public void setStatus(String Status){
		this.Status = Status;
	}
	public Boolean getMainCondition(){
		return MainCondition;
	}
	public void setMainCondition(Boolean MainCondition){
		this.MainCondition = MainCondition;
	}
	public Boolean getSecondaryCondition(){
		return SecondaryCondition;
	}
	public void setSecondaryCondition(Boolean SecondaryCondition){
		this.SecondaryCondition = SecondaryCondition;
	}
	
}
