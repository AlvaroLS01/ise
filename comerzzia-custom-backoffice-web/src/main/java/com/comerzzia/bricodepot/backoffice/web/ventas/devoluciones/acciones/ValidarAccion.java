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
package com.comerzzia.bricodepot.backoffice.web.ventas.devoluciones.acciones;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.comerzzia.bricodepot.backoffice.persistence.devoluciones.validacion.ValidacionDevolucionAlbaran;
import com.comerzzia.bricodepot.backoffice.persistence.devoluciones.validacion.ValidacionDevolucionAlbaranKey;
import com.comerzzia.bricodepot.backoffice.services.devoluciones.validacion.ValidacionDevolucionException;
import com.comerzzia.bricodepot.backoffice.services.devoluciones.validacion.ValidacionDevolucionesServiceImpl;
import com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.web.base.Accion;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;

public class ValidarAccion extends Accion {

//	private static final Vista NEXT_PAGE = new Vista("backoffice/devoluciones/buscar/jsp/buscar.jsp", Vista.INTERNA);

	/**
	 * Devuelve el nombre de la acción
	 * 
	 * @return String con el nombre de la acción
	 */
	public String getNombre() {
		return "validar";
	}

	/**
	 * Ejecuta la acción
	 * 
	 * @param request Datos de la petición
	 * @return Vista con la siguiente pagina a mostrar
	 */
	public Vista ejecutar(HttpServletRequest request) {
		
		try {
			HttpSession sesion = request.getSession();
			DatosSesionBean datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);

			// Comprobamos los permisos de la acción
			PermisosEfectivosAccionBean permisos = datosSesion.getPermisosCache(this.getAccionMenu());
			if (!permisos.isPuedeEjecutar()) {
				return SIN_PERMISO;
			}
			
			String idClieAlbaran = request.getParameter("idClieAlbaran");
			String linea = request.getParameter("linea");
			
			ValidacionDevolucionAlbaranKey key = new ValidacionDevolucionAlbaranKey();
			key.setIdClieAlbaran(Long.valueOf(idClieAlbaran));
			key.setLinea(Integer.valueOf(linea));
			
			ValidacionDevolucionAlbaran validacion = ValidacionDevolucionesServiceImpl.get().consultar(key, datosSesion);
			
			validacion.setValidado(Boolean.TRUE);
			validacion.setFechaValidacion(new Date());
			validacion.setIdUsuarioValidador(datosSesion.getUserId());
			
			ValidacionDevolucionesServiceImpl.get().modificar(validacion, datosSesion);
			

			return getControlador().getAccion("buscar").ejecutar(request);
		} catch (ValidacionDevolucionException e) {
			setError(request, e);
			return ERROR_PAGE;
		} catch (Exception e) {
			setError(request, e);
			return ERROR_PAGE;
		}
	}


}
