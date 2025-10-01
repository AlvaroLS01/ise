package com.comerzzia.bricodepot.backoffice.web.motivos;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.comerzzia.bricodepot.backoffice.persistence.motivos.Motivos;
import com.comerzzia.bricodepot.backoffice.services.motivos.MotivosService;
import com.comerzzia.core.model.i18n.I18NBean;
import com.comerzzia.core.servicios.i18n.ServicioI18NImpl;
import com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.base.Estado;
import com.comerzzia.web.base.Accion;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;

public class EdicionAccion extends Accion {

	@Autowired
	MotivosService servicioMotivos;
	@Autowired
	ServicioI18NImpl i18n;

	protected static Logger log = Logger.getLogger(AltaAccion.class);
	private static final Vista NEXT_PAGE = new Vista("backoffice/motivos/mantenimiento/jsp/motivo.jsp", Vista.INTERNA);

	@Override
	public Vista ejecutar(HttpServletRequest request) {
		HttpSession sesion = request.getSession();
		DatosSesionBean datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);

		try {
			// Comprobamos los permisos de la acción
			PermisosEfectivosAccionBean permisos = datosSesion.getPermisosCache(this.getAccionMenu());
			if (!permisos.isPuedeEditar()) {
				return SIN_PERMISO;
			}

			String codigo = request.getParameter(WebKeys.ID_OBJETO);
			Motivos motivo = servicioMotivos.consultar(codigo, datosSesion);
			motivo.setEstadoBean(Estado.MODIFICADO);
			motivo.setEnEdicion(true);

			if (!motivo.isTraduccionesCargadas() && !motivo.isEstadoNuevo()) {
				List<I18NBean> traducciones = i18n.selectByObjectId(Motivos.CLASE_DESCRIPCION, motivo.getCodigo(), datosSesion);
				motivo.setTraduccionesDescripcion(traducciones);
				motivo.setTraduccionesCargadas(true);
			}

			sesion.setAttribute("motivo", motivo);

			return NEXT_PAGE;
		}

		catch (Exception e) {
			log.error("Ha ocurrido un error: ", e);
			setError(request, e);

			return ERROR_PAGE;
		}
	}

	@Override
	public String getNombre() {
		return "editar";
	}

}
