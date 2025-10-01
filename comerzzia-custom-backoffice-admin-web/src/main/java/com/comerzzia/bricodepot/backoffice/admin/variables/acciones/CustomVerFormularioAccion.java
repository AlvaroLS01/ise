package com.comerzzia.bricodepot.backoffice.admin.variables.acciones;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;

import com.comerzzia.bricodepot.backoffice.services.variables.CustomVariables;
import com.comerzzia.core.model.variables.VariableBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.servicios.variables.VariableNotFoundException;
import com.comerzzia.core.servicios.variables.VariablesService;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;
import com.comerzzia.web.core.variables.acciones.VerFormularioAccion;
import com.comerzzia.web.core.variables.ui.FormularioVariablesBean;

@Primary
public class CustomVerFormularioAccion extends VerFormularioAccion {
	protected static final Logger log = Logger.getLogger(CustomVerFormularioAccion.class);
	private static final Vista NEXT_PAGE = new Vista("backoffice/variables/mantenimiento/jsp/variables.jsp", Vista.INTERNA);

	@Autowired
	VariablesService variablesService;

	@Override
	public Vista ejecutar(HttpServletRequest request) {
		try {
			HttpSession sesion = request.getSession();
			DatosSesionBean datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);

			// Actualizamos el formulario
			FormularioVariablesBean formulario = (FormularioVariablesBean) sesion
					.getAttribute(WebKeys.FORMULARIO_VARIABLES);
			inicializaFormulario(formulario, datosSesion, request);

			return NEXT_PAGE;
		} catch (VariableNotFoundException e) {
			log.error("ejecutar() - " + e.getClass().getName() + " - " + e.getLocalizedMessage(), e);
			setError(request, e);
			request.setAttribute("mensaje", e.getLocalizedMessage());
			return ERROR_PAGE;
		} catch (Exception e) {
			setError(request, e);

			return ERROR_PAGE;
		}
	}
	
	@Override
	protected void inicializaFormulario(FormularioVariablesBean formulario,	
			DatosSesionBean datosSesion, HttpServletRequest request) throws Exception {
		CustomVariables variables = new CustomVariables();
		
		if(formulario.getRegistros().isEmpty()){
			List<VariableBean> lstVariables = new ArrayList<VariableBean>(variablesService.obtenerVariables(datosSesion, variables.getVariables()).values());
			formulario.setRegistros(lstVariables);
		}
		
		if(formulario.getVariables().isEmpty()){
			formulario.setVariables(variablesService.obtenerVariables(datosSesion, variables.getVariables()));
		}
	}

}
