/**
 * ComerZZia 3.0
 *
 * Copyright (c) 2008-2015 Comerzzia, S.L.  All Rights Reserved.
 *
 * THIS WORK IS  SUBJECT  TO  SPAIN  AND  INTERNATIONAL  COPYRIGHT  LAWS  AND
 * TREATIES.   NO  PART  OF  THIS  WORK MAY BE  USED,  PRACTICED,  PERFORMED
 * COPIED, DISTRIBUTED, REVISED, MODIFIED, TRANSLATED,  ABRIDGED, CONDENSED,
 * EXPANDED,  COLLECTED,  COMPILED,  LINKED,  RECAST, TRANSFORMED OR ADAPTED
 * WITHOUT THE PRIOR WRITTEN CONSENT OF COMERZZIA, S.L. ANY USE OR EXPLOITATION
 * OF THIS WORK WITHOUT AUTHORIZATION COULD SUBJECT THE PERPETRATOR TO
 * CRIMINAL AND CIVIL LIABILITY.
 *
 * CONSULT THE END USER LICENSE AGREEMENT FOR INFORMATION ON ADDITIONAL
 * RESTRICTIONS.
 */
package com.comerzzia.bricodepot.backoffice.services.tickets.auditoria;

import com.comerzzia.core.util.base.Exception;

public class EventoAuditoriaNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3032598740214806496L;

	public EventoAuditoriaNotFoundException() {
	}

	public EventoAuditoriaNotFoundException(String msg) {
		super(msg);
	}

	public EventoAuditoriaNotFoundException(String msg, Throwable e) {
		super(msg, e);
	}

	public EventoAuditoriaNotFoundException(String msg, String msgKey) {
		super(msg, msgKey);
	}

	public EventoAuditoriaNotFoundException(String msg, String msgKey, Throwable e) {
		super(msg, msgKey, e);
	}
}
