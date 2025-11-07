package com.comerzzia.bimbaylola.pos.persistence.articulos.stock;

public class SolicitarTokenDTO{
	
	private String clientId;
	private String clientSecret;
	private String scope;
	private String grantType;
	/* Para la parte de tarjetas regalo */
	private String scopeCards;
	
	/* Estos datos son fijos y no se deben modificar. */
	public SolicitarTokenDTO(String grantType, String clientID, String clientSecret, String scopeStocks, String scopeCards){
		super();
		this.clientId = clientID;
		this.clientSecret = clientSecret;
		this.scope = scopeStocks;
		this.grantType = grantType;
		this.scopeCards = scopeCards;
	}
	
	public String getClientId() {
		return clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public String getScope() {
		return scope;
	}
	public String getGrantType() {
		return grantType;
	}
	public String getScopeCards() {
		return scopeCards;
	}
	
}
