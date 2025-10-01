package com.comerzzia.bricodepot.backoffice.web.motivos;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.comerzzia.bricodepot.backoffice.persistence.motivos.Motivos;
import com.comerzzia.core.util.base.Estado;
import com.comerzzia.web.base.Accion;
import com.comerzzia.web.base.Vista;

public class AltaAccion extends Accion {

	protected static Logger log = Logger.getLogger(AltaAccion.class);
	private static final Vista NEXT_PAGE = new Vista("backoffice/motivos/mantenimiento/jsp/motivo.jsp", Vista.INTERNA);

	@Override
	public Vista ejecutar(HttpServletRequest request) {
		try {
			HttpSession sesion = request.getSession();

			Motivos motivo = new Motivos();
			motivo.setEstadoBean(Estado.NUEVO);
			motivo.setEnEdicion(true);

			sesion.setAttribute("motivo", motivo);

			return NEXT_PAGE;
		}
		catch (Exception e) {
			log.error("ejecutar() - Ha ocurrido un error: ", e);
			setError(request, e);
			return ERROR_PAGE;
		}
	}

	@Override
	public String getNombre() {
		return "alta";
	}

}
