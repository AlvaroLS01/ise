package com.comerzzia.bricodepot.backoffice.web.tiendas.acciones;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.comerzzia.bricodepot.backoffice.services.general.tiendas.BricodepotServicioTiendasImpl;
import com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.base.Estado;
import com.comerzzia.model.general.tiendas.mediospago.MedioPagoTiendaBean;
import com.comerzzia.model.general.tiendas.servicios.TiendaServicioBean;
import com.comerzzia.servicios.general.tiendas.ServicioTiendasImpl;
import com.comerzzia.servicios.general.tiendas.Tienda;
import com.comerzzia.servicios.general.tiendas.TiendaConstraintViolationException;
import com.comerzzia.servicios.general.tiendas.TiendaException;
import com.comerzzia.servicios.general.tiendas.mediospago.MedioPagoTiendaConstraintViolationException;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;
import com.comerzzia.web.general.tiendas.acciones.SalvarAccion;
import com.comerzzia.web.general.tiendas.ui.FormularioTiendaBean;
public class BricodepotSalvarAccion extends SalvarAccion{
	protected static Logger log = Logger.getLogger(BricodepotSalvarAccion.class);
	@SuppressWarnings("deprecation")
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
    		
    		// Obtenemos la tienda del formulario y la salvamos
    		FormularioTiendaBean formulario = (FormularioTiendaBean) sesion.getAttribute(WebKeys.FORMULARIO_TIENDA);
    		Tienda tienda = (Tienda) formulario.getRegistroActivo();
    		
    		//Comprobamos que los servicios asociados a la tienda que requieren preparación tengan el almacén de preparación informado.
    		for(TiendaServicioBean tiendaServicio: tienda.getServicios()) {
    			if(tiendaServicio.getEstadoBean() != Estado.BORRADO) {
	    			if("S".equals(tiendaServicio.getServicioTipo().getRequierePreparacion()) && (tiendaServicio.getCodalmPreparacion() == null || tiendaServicio.getCodalmPreparacion().isEmpty())) {
	    				setMensajeError(request, datosSesion.getTranslation()._("El tipo de servicio {0} - {1}, requiere preparación pero no se ha indicado el almacén de preparación", tiendaServicio.getCodtipserv(), tiendaServicio.getDestipserv()));
	    	    		return getControlador().getAccion("verFormulario").ejecutar(request);
	    			}
    			}
    		}
    		
    		//Comprobamos que no se repiten para distintos medios de pago la misma configuración.
    		if(tienda.getMediosPago()!=null && !tienda.getMediosPago().isEmpty()){
    			List<String> lstConfMedPag = new ArrayList<String>();
    			Boolean existeConfMedPagoIgual = false;
    			for(MedioPagoTiendaBean medioPagoTienda : tienda.getMediosPago()){
    				String idConfPasarela = medioPagoTienda.getConfiguracionMedioPago().getIdConfPasarela();
    				if(!existeConfMedPagoIgual && StringUtils.isNotBlank(idConfPasarela)){
    					if(!lstConfMedPag.contains(idConfPasarela)){
    						lstConfMedPag.add(idConfPasarela);
    					}else{
    						existeConfMedPagoIgual = true;
    						break;
    					}
    				}
    			}
				//Lanzamos un mensaje de aviso cuando varios medios de pago tiene la misma configuración.
				if(existeConfMedPagoIgual){
					setMensajeInfo(request,datosSesion.getTranslation()._("Varios medios de pago están compartiendo la misma configuración"));
				}
    		}
    		
    		
    		BricodepotServicioTiendasImpl.get().salvar(tienda, datosSesion);
    		formulario.aceptar();
    		
    		setMensaje(request,datosSesion.getTranslation()._("La tienda se ha grabado correctamente"));
    		
    		// Consultamos la tienda e inicializamos el formulario
    		try {
				tienda = ServicioTiendasImpl.get().consultar(tienda.getCodAlm(), datosSesion);
				formulario.inicializar(tienda);
			}
    		catch (Exception e) {
    			setMensajeError(request,datosSesion.getTranslation()._("Se ha producido un error al acceder al registro"));
    			return getControlador().getAccion("buscar").ejecutar(request);
			}
    		
    		return getControlador().getAccion("verFormulario").ejecutar(request);
    	}
    	catch (MedioPagoTiendaConstraintViolationException e){
    		setMensajeError(request, e.getMessage());
    		return getControlador().getAccion("verFormulario").ejecutar(request);
    	}
    	catch (TiendaConstraintViolationException e) {
    		setMensajeError(request, e.getMessage());
    		return getControlador().getAccion("verFormulario").ejecutar(request);
    	}
    	catch (TiendaException e) {
    		setError(request, e);
            
            return ERROR_PAGE;
    	}
    	catch (Exception e) {
    		log.debug("ejecutar() - Ha ocurrido un error : " + e.getMessage());
            setError(request, e);
            
            return ERROR_PAGE;
        }
	}

}
