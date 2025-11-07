package com.comerzzia.bimbaylola.pos.persistence.idfiscal;

public class IdFiscalDTO {

	protected String documentTypeCode;
	protected String numeroIdFiscal;

	public IdFiscalDTO() {
		super();
	}

	public IdFiscalDTO(String documentTypeCode) {
		this.documentTypeCode = documentTypeCode;
	}

	public String getDocumentTypeCode() {
		return documentTypeCode;
	}

	public void setDocumentTypeCode(String documentTypeCode) {
		this.documentTypeCode = documentTypeCode;
	}

	public String getNumeroIdFiscal() {
		return numeroIdFiscal;
	}

	public void setNumeroIdFiscal(String numeroIdFiscal) {
		this.numeroIdFiscal = numeroIdFiscal;
	}
}
