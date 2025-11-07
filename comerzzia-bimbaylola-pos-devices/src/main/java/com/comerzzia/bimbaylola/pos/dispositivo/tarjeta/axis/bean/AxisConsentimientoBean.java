package com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.axis.bean;

/**
 * Para leer la respuesta JSON del consentimiento de Axis
 * {"Operation":{"Name":"InfoCheckBox"},"OperationResult":{"Status":"Success","MainCondition":true,"SecondaryCondition":true}}
 */
public class AxisConsentimientoBean{
	
	private AxisConsentimientoOperacionBean Operation;
	private AxisConsentimientoOperacionResultBean OperationResult;
	
	public AxisConsentimientoOperacionBean getOperation(){
		return Operation;
	}
	public void setOperation(AxisConsentimientoOperacionBean Operation){
		this.Operation = Operation;
	}
	public AxisConsentimientoOperacionResultBean getOperationResult(){
		return OperationResult;
	}
	public void setOperationResult(
			AxisConsentimientoOperacionResultBean OperationResult){
		this.OperationResult = OperationResult;
	}
	
}
