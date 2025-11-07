package com.comerzzia.bimbaylola.pos.persistence.ventas.tipooperacion;

public class TipoOperacionVenta extends TipoOperacionVentaKey {

	private static final long serialVersionUID = -6745851810526383775L;

	private String tipoDocumento;

	private String signoDocumento;

	private Long idTipoDocumento;

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento == null ? null : tipoDocumento.trim();
	}

	public String getSignoDocumento() {
		return signoDocumento;
	}

	public void setSignoDocumento(String signoDocumento) {
		this.signoDocumento = signoDocumento == null ? null : signoDocumento.trim();
	}

	public Long getIdTipoDocumento() {
		return idTipoDocumento;
	}

	public void setIdTipoDocumento(Long idTipoDocumento) {
		this.idTipoDocumento = idTipoDocumento;
	}
}