package com.comerzzia.bricodepot.backoffice.web.fidelizacion.colectivos.acciones;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.model.fidelizacion.colectivos.ColectivoBean;
import com.comerzzia.servicios.fidelizacion.colectivos.ColectivoConstraintViolationException;
import com.comerzzia.servicios.fidelizacion.colectivos.ColectivoException;
import com.comerzzia.servicios.fidelizacion.colectivos.ServicioColectivosImpl;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;
import com.comerzzia.web.fidelizacion.colectivos.acciones.SalvarAccion;
import com.comerzzia.web.fidelizacion.colectivos.ui.FormularioColectivoBean;

public class BricodepotSalvarAccion extends SalvarAccion {
	
	@Override
	public Vista ejecutar(HttpServletRequest request) {
		try {
			HttpSession sesion = request.getSession();
			DatosSesionBean datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);
			
			// Comprobamos los permisos de la acción
			PermisosEfectivosAccionBean permisos = datosSesion.getPermisosCache(this.getAccionMenu());
			if (!permisos.isPuedeAñadir() && !permisos.isPuedeEditar()) {
				return SIN_PERMISO;
			}
			
			// Obtenemos el cliente del formulario y lo salvamos
			FormularioColectivoBean formulario = (FormularioColectivoBean) sesion.getAttribute(WebKeys.FORMULARIO_COLECTIVO);
			ColectivoBean colectivo = (ColectivoBean) formulario.getRegistroActivo();
			
			/* BRICO-146 modificación campos alta fidelizado */
			Boolean profesional = request.getParameter("profesional")!=null;
			Boolean visualizacionAltas = request.getParameter("visualizacionAltas")!=null;
			colectivo.putExtension("profesional", profesional);
			colectivo.putExtension("visualizacionAltas", visualizacionAltas);
			/* fin BRICO-146 modificación campos alta fidelizado */
			
			ServicioColectivosImpl.get().salvar(colectivo, datosSesion);
			formulario.aceptar();
			
			setMensaje(request, datosSesion.getTranslation()._("El colectivo se ha grabado correctamente"));
			
    		// Consultamos el cliente grabado e inicializamos el formulario
    		try {
    			colectivo = ServicioColectivosImpl.get().consultar(colectivo.getCodColectivo(), datosSesion);
				formulario.inicializar(colectivo);
			}
    		catch (Exception e) {
    			setMensajeError(request, datosSesion.getTranslation()._("Se ha producido un error al acceder al registro"));
    			return getControlador().getAccion("buscar").ejecutar(request);
			}
			
			return getControlador().getAccion("verFormulario").ejecutar(request);
		}
		catch (ColectivoConstraintViolationException e) {
			setMensajeError(request, e.getMessage());
			
			return getControlador().getAccion("verFormulario").ejecutar(request);
		}
		catch (ColectivoException e) {
			setError(request, e);
			
			return ERROR_PAGE;
		}
		catch (Exception e) {
			setError(request, e);
			
			return ERROR_PAGE;
		}
	}

}
