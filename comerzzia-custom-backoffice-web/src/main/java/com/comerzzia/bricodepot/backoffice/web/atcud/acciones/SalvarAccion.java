package com.comerzzia.bricodepot.backoffice.web.atcud.acciones;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.comerzzia.core.model.variables.VariableBean;
import com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.servicios.variables.ServicioVariablesImpl;
import com.comerzzia.web.base.Accion;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;

public class SalvarAccion extends Accion {

	protected static Logger log = Logger.getLogger(SalvarAccion.class);
	private static final Vista NEXT_PAGE = new Vista("backoffice/atcud/buscar/jsp/buscar.jsp", Vista.INTERNA);
	protected static String X_ATCUD_MAGENTO = "X_ATCUD_MAGENTO";

	@Autowired
	protected ServicioVariablesImpl servicioVariablesImpl;

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
			VariableBean variable = new VariableBean();
			variable.setValor(request.getParameter("atcud"));
			variable.setIdVariable(X_ATCUD_MAGENTO);
			variable.setUidActividad(datosSesion.getUidActividad());
			variable.setPeso(100L);
			servicioVariablesImpl.modificar(datosSesion, variable);

			sesion.removeAttribute("variables");

			try {
				variable = servicioVariablesImpl.consultar(datosSesion, X_ATCUD_MAGENTO);

				sesion.setAttribute("variables", variable);
				setMensaje(request, datosSesion.getTranslation().getText("El ATCUD se ha salvado correctamente"));
				return NEXT_PAGE;
			}
			catch (Exception e) {
				setError(request, e);
				return getControlador().getAccion("buscar").ejecutar(request);

			}
		}
		catch (Exception e) {
			log.error("ejecutar() - Ha ocurrido un error: ", e);
			setError(request, e);
			return ERROR_PAGE;
		}
	}

	@Override
	public String getNombre() {
		return "salvar";
	}

}
