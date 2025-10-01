package com.comerzzia.bricodepot.backoffice.web.general.importacionmasiva;

import java.io.File;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.config.AppInfo;
import com.comerzzia.web.base.WebKeys;
import com.comerzzia.web.general.importacionmasiva.DescargarPlantillaServlet;


@WebServlet(value = "/descargarPlantilla", description = "Servlet de descarga de plantilla", displayName = "DescargarPlantillaServlet", name = "DescargarPlantillaServlet")
public class CustomDescargarPlantillaServlet extends DescargarPlantillaServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1429199088619147636L;
	
	@Override
	protected void perfomTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
        	HttpSession sesion = request.getSession();
        	DatosSesionBean datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);
        	
    		// Comprobamos los permisos de la acción
    		PermisosEfectivosAccionBean permisos = datosSesion.getPermisosCache(this.accionMenu);
    		if (!permisos.isPuedeEjecutar()) {
                RequestDispatcher dispatch = request.getRequestDispatcher(SIN_PERMISO);
                dispatch.forward(request, response);
    		}
            //Creamos el fichero

   
    		String nombreFichero = "/" + sesion.getAttribute("nombreFichero")+".xls";
    		String rutaCompleta = AppInfo.getPlantillasImportacionInfo().getRutaBase()+nombreFichero;
    		
    		File file = new File(rutaCompleta);
                      
            //Enviamos el fichero por la respuesta
            enviarFichero(file, response);
        }
        catch(Exception e){
        	log.error("Error al exportar los fidelizados: "+e.getMessage(), e);
            request.setAttribute(WebKeys.EXCEPTION, e);
  
            RequestDispatcher dispatch = request.getRequestDispatcher(ERROR_PAGE);
            dispatch.forward(request, response);
        }
    }
}
