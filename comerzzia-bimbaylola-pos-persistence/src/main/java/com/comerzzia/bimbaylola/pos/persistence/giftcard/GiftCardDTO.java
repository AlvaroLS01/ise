package com.comerzzia.bimbaylola.pos.persistence.giftcard;

public class GiftCardDTO{
	
	private String number;
	private Integer stateCode;
	private Integer typeCode;
	private Double balanceAmount;
	private String balanceCurrencyCode;
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public Integer getStateCode() {
		return stateCode;
	}
	public void setStateCode(Integer stateCode) {
		this.stateCode = stateCode;
	}
	public Integer getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(Integer typeCode) {
		this.typeCode = typeCode;
	}
	public Double getBalanceAmount() {
		return balanceAmount;
	}
	public void setBalanceAmount(Double balanceAmount) {
		this.balanceAmount = balanceAmount;
	}
	public String getBalanceCurrencyCode() {
		return balanceCurrencyCode;
	}
	public void setBalanceCurrencyCode(String balanceCurrencyCode) {
		this.balanceCurrencyCode = balanceCurrencyCode;
	}
	
}
