package com.comerzzia.bimbaylola.pos.gui.i18n.fxml;

public class MensajesExceptionExt extends Exception {
	private static final long serialVersionUID = 1L;

	public MensajesExceptionExt(String message) {
		super(message);
	}

	public MensajesExceptionExt(String message, Exception e) {
		super(message, e);
	}
}