package com.comerzzia.bimbaylola.pos.persistence.idfiscal;


public class SolicitudTokenDTOIdFiscal {
	private String grant_type;
	private String client_id;
	private String client_secret;
	private String scope;

	public SolicitudTokenDTOIdFiscal(String grantType, String clientId, String clientSecret, String scope) {
		super();
		this.grant_type = grantType;
		this.client_id = clientId;
		this.client_secret = clientSecret;
		this.scope = scope;
	}

	public String getGrant_type() {
		return grant_type;
	}

	public String getClient_id() {
		return client_id;
	}

	public String getClient_secret() {
		return client_secret;
	}

	public String getScope() {
		return scope;
	}
}
