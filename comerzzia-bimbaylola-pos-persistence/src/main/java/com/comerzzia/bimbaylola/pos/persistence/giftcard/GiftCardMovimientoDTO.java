package com.comerzzia.bimbaylola.pos.persistence.giftcard;

public class GiftCardMovimientoDTO{
	
	private String typeCode;
	private String amount;
	private String currencyCode;
	private String documentNumber;
	private String shopCode;
	private String userCode;
	private String sourceDocumentNumber;
	private String isCompensation;
	
	public String getTypeCode(){
		return typeCode;
	}
	public void setTypeCode(String typeCode){
		this.typeCode = typeCode;
	}
	public String getAmount(){
		return amount;
	}
	public void setAmount(String amount){
		this.amount = amount;
	}
	public String getCurrencyCode(){
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode){
		this.currencyCode = currencyCode;
	}
	public String getDocumentNumber(){
		return documentNumber;
	}
	public void setDocumentNumber(String documentNumber){
		this.documentNumber = documentNumber;
	}
	public String getShopCode(){
		return shopCode;
	}
	public void setShopCode(String shopCode){
		this.shopCode = shopCode;
	}
	public String getUserCode(){
		return userCode;
	}
	public void setUserCode(String userCode){
		this.userCode = userCode;
	}
	public String getSourceDocumentNumber(){
		return sourceDocumentNumber;
	}
	public void setSourceDocumentNumber(String sourceDocumentNumber){
		this.sourceDocumentNumber = sourceDocumentNumber;
	}
	public String getIsCompensation(){
		return isCompensation;
	}
	public void setIsCompensation(String isCompensation){
		this.isCompensation = isCompensation;
	}
	
}
