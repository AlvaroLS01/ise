package com.comerzzia.bricodepot.backoffice.persistence.general.tiendas.atcud.Exception;
import com.comerzzia.core.util.base.Exception;

public class BricodepotAtcudException extends Exception{


	private static final long serialVersionUID = 164794798882491826L;
	
	public BricodepotAtcudException() {
		super();
	}

	public BricodepotAtcudException(String msg) {
		super(msg);
	}

	public BricodepotAtcudException(String msg, Throwable e) {
		super(msg, e);
	}

	public BricodepotAtcudException(String msg, String msgKey, Throwable e) {
		super(msg, msgKey, e);
	}

	public BricodepotAtcudException(String msg, String msgKey) {
		super(msg, msgKey);
	}
}
