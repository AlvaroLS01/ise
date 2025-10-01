package com.comerzzia.bricodepot.backoffice.admin.erroresinterfaces.acciones;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.paginacion.PaginaResultados;
import com.comerzzia.bricodepot.backoffice.persistence.erroresinterfaces.ParametrosBuscarErroresInterfacesBean;
import com.comerzzia.bricodepot.backoffice.services.erroresinterfaces.ErroresInterfacesService;
import com.comerzzia.web.base.Accion;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;

public class BuscarAccion extends Accion{
	
	private static final Vista NEXT_PAGE = new Vista("admin/erroresinterfaces/buscar/jsp/buscar.jsp", Vista.INTERNA);
	
	protected static Logger log = Logger.getLogger(BuscarAccion.class);
	
	
	@Autowired
	ErroresInterfacesService erroresInterfacesService;
	
	@Override
	public String getNombre() {
		return "buscar";
	}

	@Override
	public Vista ejecutar(HttpServletRequest request) {
		try {
			HttpSession sesion = request.getSession();
			DatosSesionBean datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);
			
			// Comprobamos los permisos de la acción
			PermisosEfectivosAccionBean permisos = datosSesion.getPermisosCache(this.getAccionMenu());
			if (!permisos.isPuedeEjecutar()) {
				return SIN_PERMISO;
			}
			
			// Obtenemos parámetros de búsqueda
    		ParametrosBuscarErroresInterfacesBean param = (ParametrosBuscarErroresInterfacesBean) sesion.getAttribute("paramBuscarErroresInterfaces");
    		
			// Comprobamos la operación a realizar
			String operacion = request.getParameter("operacion");
			if(operacion != null) {
				// Consultar
				if(operacion.equals("consultar")) {
					setParametrosBuscar(param, request);
				}
				 // Ver pagina
                else if (operacion.equals("paginar")) {
                    setPaginaBuscar(param, request);
                }
                // Ordenar
                else if (operacion.equals("ordenar")) {
                    setOrdenBuscar(param, request);
                }
				
				 // Si tenemos página, realizamos la búsqueda
	            if (param.getNumPagina() > 0) {
	            	PaginaResultados paginaResultados = erroresInterfacesService.consultar(param, datosSesion);
	            	request.setAttribute(WebKeys.PAGINA_RESULTADOS, paginaResultados);
	            }
	            
	            
			}
			sesion.setAttribute("paramBuscarErroresInterfaces", param);
			request.setAttribute("listaClases", erroresInterfacesService.consultarIdClases(datosSesion));
			
			return NEXT_PAGE;
		} catch (Exception e) {
			log.error("Error realizando la búsqueda de errores de interfaces. ", e);
			 setError(request, e);
	            
	         return ERROR_PAGE;
		}
	}
	
	protected void setParametrosBuscar(ParametrosBuscarErroresInterfacesBean param, HttpServletRequest request) {
		
		String idClase = request.getParameter("idClase");
    	if(idClase!=null){    		
    		param.setIdClase(idClase);
    	}
        param.setNumPagina(1);
        
        // Actualizar el número de resultados por página
        try {
        	param.setTamañoPagina(Integer.parseInt(request.getParameter("tamanoPagina")));
        } catch(Exception ignore) {
        	// Si no se recibe el tamaño de página, se mantiene el que se tenía
        }
    }
    
    
	protected void setPaginaBuscar(ParametrosBuscarErroresInterfacesBean param, HttpServletRequest request) {
        try {
            // Obtenemos la pagina solicitada
            int pagina = Integer.parseInt(request.getParameter("pagina"));
            param.setNumPagina(pagina);
        }
        catch (Exception ignore) {
            // Si no tenemos pagina, se mostrará la que teníamos
        }
    }
    
    
	protected void setOrdenBuscar(ParametrosBuscarErroresInterfacesBean param, HttpServletRequest request) {
    	try {
            // Establecemos primera pagina
            param.setNumPagina(1);
            
            // Obtenemos la columna por la que ordenar
            int columna = Integer.parseInt(request.getParameter("columna"));
            
            // Establecemos la columna de orden
            switch (columna) {
	            case 1:  // ID_CLASE
	                if (param.getOrden().equals("ID_CLASE")) {
	                    param.setOrden("ID_CLASE DESC");
	                }
	                else {
	                    param.setOrden("ID_CLASE");
	                }
	                break;
                case 2:  // ID_OBJETO
                    if (param.getOrden().equals("ID_OBJETO")) {
                        param.setOrden("ID_OBJETO DESC");
                    }
                    else {
                        param.setOrden("ID_OBJETO");
                    }
                    break;
                case 3:  // FECHA_INICIO
                    if (param.getOrden().equals("FECHA_INICIO")) {
                        param.setOrden("FECHA_INICIO DESC");
                    }
                    else {
                        param.setOrden("FECHA_INICIO");
                    }
                    break;
                case 4:  // ULTIMO_ERROR
                    if (param.getOrden().equals("ULTIMO_ERROR")) {
                        param.setOrden("ULTIMO_ERROR DESC");
                    }
                    else {
                        param.setOrden("ULTIMO_ERROR");
                    }
                    break;
                case 5:  // ULTIMO_DOCUMENTO
                	 if (param.getOrden().equals("ULTIMO_DOCUMENTO")) {
                         param.setOrden("ULTIMO_DOCUMENTO DESC");
                     }
                     else {
                         param.setOrden("ULTIMO_DOCUMENTO");
                     }
                     break;
                case 6:  // TIENDAS_IMPLICADAS
                    if (param.getOrden().equals("TIENDAS_IMPLICADAS")) {
                        param.setOrden("TIENDAS_IMPLICADAS DESC");
                    }
                    else {
                        param.setOrden("TIENDAS_IMPLICADAS");
                    }
                    break;
               case 7:  // ULTIMO_MENSAJE
                	if (param.getOrden().equals("ULTIMO_MENSAJE")) {
                        param.setOrden("ULTIMO_MENSAJE DESC");
                    }
                    else {
                        param.setOrden("ULTIMO_MENSAJE");
                    }
                    break;
               case 8:  // ULTIMO_MENSAJE_TRAZA
            	   if (param.getOrden().equals("ULTIMO_MENSAJE_TRAZA")) {
            		   param.setOrden("ULTIMO_MENSAJE_TRAZA DESC");
            	   }
            	   else {
            		   param.setOrden("ULTIMO_MENSAJE_TRAZA");
            	   }
            	   break;
                default:
                    param.setOrden("FECHA_INICIO DESC");
            }
        }
        catch (Exception e) {
        	// Si no tenemos orden, se dejar el que se tenía
        }
    }

}
