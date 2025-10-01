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
package com.comerzzia.bricodepot.backoffice.web.auditoria.acciones;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.comerzzia.bricodepot.backoffice.persistence.tickets.auditoria.EventoAuditoriaBean;
import com.comerzzia.bricodepot.backoffice.persistence.tickets.auditoria.ParametrosBuscarEventoAuditoria;
import com.comerzzia.bricodepot.backoffice.services.tickets.auditoria.EventoAuditoriaException;
import com.comerzzia.bricodepot.backoffice.services.tickets.auditoria.ServicioEventosAuditoria;
import com.comerzzia.bricodepot.backoffice.web.auditoria.AuditoriaServlet;
import com.comerzzia.core.model.informes.TrabajoInformeBean;
import com.comerzzia.core.servicios.informes.InformeException;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.fechas.Fechas;
import com.comerzzia.web.base.InformeAccion;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;

public class ImprimirAccion extends InformeAccion {

	public String getNombreInforme() {
		return "auditoria.lstEventosAuditoria";
	}

	public Vista vistaInforme(HttpServletRequest request) {
		

		return new Vista("backoffice/auditoria/buscar/jsp/imprimir.jsp", Vista.INTERNA);
	}

	public void imprimirInforme(HttpServletRequest request, TrabajoInformeBean trabajoInforme) throws InformeException {
		// get parametros de busqueda
		HttpSession sesion = request.getSession();
		DatosSesionBean datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);
		ParametrosBuscarEventoAuditoria param = (ParametrosBuscarEventoAuditoria) sesion
				.getAttribute(AuditoriaServlet.PARAM_BUSCAR_EVENTO_AUDITORIA);

		param.setUsuario(request.getParameter("usuario"));
		param.setUsuarioSupervisor(request.getParameter("supervisor"));

		try {
			param.setFechaDesde(Fechas.getFecha(request.getParameter("fechaDesde")));
		} catch (Exception ignore) {
			param.setFechaDesde(null);
		}

		try {
			param.setFechaHasta(Fechas.getFecha(request.getParameter("fechaHasta")));
		} catch (Exception ignore) {
			param.setFechaHasta(null);
		}
		
		// buscar usando el servicio y pasar el resultado al trabajoinformebean
		try {
			List<EventoAuditoriaBean> eventos = ServicioEventosAuditoria.get().consultarSimple(param, datosSesion);
			trabajoInforme.addParametro("EVENTOS", eventos);
		} catch (EventoAuditoriaException e) {
			log.error("imprimirInforme() ERROR - Error consultando eventos:"+e.getMessage());
		}


	}
}
