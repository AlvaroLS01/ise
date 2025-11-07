package com.comerzzia.bimbaylola.pos.dispositivo.giftcard;

public class EnviarMovimientoGiftCardBean{

	private Double salida;
	private Double entrada;
	private String numeroTarjeta;
	private Integer typeCode;
	private String documentNumber;
	private String sourceDocumentNumber;

	public Double getSalida(){
		return salida;
	}

	public void setSalida(Double salida){
		this.salida = salida;
	}

	public Double getEntrada(){
		return entrada;
	}

	public void setEntrada(Double entrada){
		this.entrada = entrada;
	}

	public String getNumeroTarjeta(){
		return numeroTarjeta;
	}

	public void setNumeroTarjeta(String numeroTarjeta) {
		this.numeroTarjeta = numeroTarjeta;
	}

	public Integer getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(Integer typeCode) {
		this.typeCode = typeCode;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber){
		this.documentNumber = documentNumber;
	}

	public String getSourceDocumentNumber(){
		return sourceDocumentNumber;
	}

	public void setSourceDocumentNumber(String sourceDocumentNumber){
		this.sourceDocumentNumber = sourceDocumentNumber;
	}

}
