package com.comerzzia.bricodepot.backoffice.web.motivos;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.comerzzia.bricodepot.backoffice.persistence.motivos.Motivos;
import com.comerzzia.bricodepot.backoffice.services.motivos.MotivosService;
import com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.web.base.Accion;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;

public class SalvarAccion extends Accion {

	@Autowired
	protected MotivosService servicioMotivo;

	protected static Logger log = Logger.getLogger(AltaAccion.class);
	private static final Vista NEXT_PAGE = new Vista("backoffice/motivos/mantenimiento/jsp/motivo.jsp", Vista.INTERNA);

	@Override
	public Vista ejecutar(HttpServletRequest request) {
		HttpSession sesion = request.getSession();
		DatosSesionBean datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);

		try {
			// Comprobamos los permisos de la acción
			PermisosEfectivosAccionBean permisos = datosSesion.getPermisosCache(this.getAccionMenu());
			if (!permisos.isPuedeAñadir() && !permisos.isPuedeEditar()) {
				return SIN_PERMISO;
			}

			Motivos motivo = (Motivos) sesion.getAttribute("motivo");
			motivo.setCodigo(request.getParameter("codigo"));
			motivo.setDescripcion(request.getParameter("descripcion"));
			motivo.setCodigoTipo(request.getParameter("tipoMotivo"));

			servicioMotivo.salvar(motivo, datosSesion);

			// Liberamos los recursos
			sesion.removeAttribute("motivo");

			// Volvemos a consultar el registro
			try {
				motivo = servicioMotivo.consultar(motivo.getCodigo(), datosSesion);

				sesion.setAttribute("motivo", motivo);
				setMensaje(request, datosSesion.getTranslation().getText("El motivo se ha salvado correctamente"));
				return NEXT_PAGE;
			}
			catch (Exception e) {
				setError(request, e);
				return getControlador().getAccion("buscar").ejecutar(request);

			}

		}
		catch (Exception e) {
			log.error("Ha ocurrido un error: ", e);
			setError(request, e);
			return ERROR_PAGE;

		}
	}

	@Override
	public String getNombre() {
		return "salvar";
	}

}
