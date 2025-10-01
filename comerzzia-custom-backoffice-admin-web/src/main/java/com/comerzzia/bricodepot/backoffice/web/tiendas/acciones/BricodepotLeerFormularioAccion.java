package com.comerzzia.bricodepot.backoffice.web.tiendas.acciones;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.comerzzia.bricodepot.backoffice.persistence.general.tiendas.atcud.AtcudMagento;
import com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.base.Estado;
import com.comerzzia.model.general.tiendas.cajas.TiendaCajaBean;
import com.comerzzia.model.general.tiendas.mediospago.MedioPagoTiendaBean;
import com.comerzzia.model.general.tiendas.servicios.TiendaServicioBean;
import com.comerzzia.model.general.tiendas.servicios.cps.TiendaServicioCPBean;
import com.comerzzia.model.general.tiendas.tarifas.TarifaTiendaBean;
import com.comerzzia.servicios.general.tiendas.ServicioTiendasImpl;
import com.comerzzia.servicios.general.tiendas.Tienda;
import com.comerzzia.servicios.sincronizacion.datos.ServicioDatosSincronizacion;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;
import com.comerzzia.web.general.tiendas.acciones.LeerFormularioAccion;
import com.comerzzia.web.general.tiendas.ui.FormularioTiendaBean;


public class BricodepotLeerFormularioAccion extends LeerFormularioAccion {

	protected static Logger log = Logger.getLogger(BricodepotLeerFormularioAccion.class);
	
	public Vista ejecutar(HttpServletRequest request) {
		try {
			HttpSession sesion = request.getSession();
			DatosSesionBean datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);
			
			PermisosEfectivosAccionBean permisos = datosSesion.getPermisosCache(this.getAccionMenu());
			
			// Obtenemos la operación solicitada
			String operacion = request.getParameter("operacion");
			String indice = request.getParameter("indice");
			log.debug("ejecutar() - operacion=" + operacion + " indice=" + indice);
			
			// Actualizamos el formulario
			FormularioTiendaBean formulario = (FormularioTiendaBean) sesion.getAttribute(WebKeys.FORMULARIO_TIENDA);
			if (operacion == null || !operacion.equals("cancelar")) {
				leerFormulario(formulario, request, datosSesion);
			}			

            if (operacion != null) {
            	// Cambiar pestaña activa
                if (operacion.equals("pestaña")) {
                	formulario.setPestañaActiva(Integer.parseInt(indice));
                }
                // Cambiar modo visualización de la pestaña activa 
                else if (operacion.equals("vistaTablaPestaña")) {
                	formulario.getFormularioPestañaActiva().setModoVisualizacionTabla();
                }
                // Ver un registro de la pestaña activa
                else if (operacion.equals("verRegistroPestaña")) {
                	formulario.getFormularioPestañaActiva().seleccionaRegistroActivo(Integer.parseInt(indice));               	
                	formulario.getFormularioPestañaActiva().setModoVisualizacionFicha();
                }
                // Añadir un nuevo registro a la pestaña activa
                else if (operacion.equals("nuevoRegistroPestaña")) {
                	switch (formulario.getPestañaActiva()) {
	    				case 1:  // Tarifas
	    					request.setAttribute("modoFicha", true);
	    					/*formulario.getFormularioPestañaActiva().setModoVisualizacionFicha();*/
	    				case 2:  // Configuracion
	    					request.setAttribute("modoFicha", true);
	    					formulario.getFormularioPestañaActiva().setModoVisualizacionFicha();
	    				case 3:  // Usuarios
	    					request.setAttribute("modoFicha", true);
	    				case 4:  // Cajas
	    					// Comprobar que el usuario tiene permisos para gestionar cajas
	    					if (!permisos.isPuede(FormularioTiendaBean.OPERACION_GESTIONAR_CAJAS)) {
	    						return SIN_PERMISO;
	    					}
	    					request.setAttribute("modoFicha", true);
	    					break;
	    				case 5:  // Servicios
	    					request.setAttribute("modoFicha", true);
	    					TiendaServicioBean servicio = new TiendaServicioBean();
	                    	formulario.setModoVisualizacionFicha();
	    					formulario.getFormularioPestañaActiva().setModoInsercion(servicio);
	    					break;
	    				case 6:  // Medios de pago
	    					Tienda tienda = (Tienda) formulario.getRegistroActivo();
	    					for(MedioPagoTiendaBean medioPago : tienda.getMediosPago()){
		    					if(medioPago.getConfiguracionMedioPago().getEstadoBean() == Estado.SIN_MODIFICAR) {
	        						medioPago.getConfiguracionMedioPago().setIdConfPasarela(request.getParameter("idConfPasarela"+medioPago.getCodmedpag()));
	        						medioPago.getConfiguracionMedioPago().setDesconfpasarela(request.getParameter("desconfpasarela"+medioPago.getCodmedpag()));
	        					}
	    					}
	    					
	    					request.setAttribute("modoFicha", true);
	    					MedioPagoTiendaBean medioPago = new MedioPagoTiendaBean();
	                    	formulario.setModoVisualizacionFicha();
	    					formulario.getFormularioPestañaActiva().setModoInsercion(medioPago);
	    					break;
	    				case 8:
	    					AtcudMagento atcud = new AtcudMagento() ;
	    					request.setAttribute("modoFicha", true);
	    					formulario.getFormularioPestañaActiva().setModoInsercion(atcud);
	    					
	    			}
                }
                else if (operacion.equals("altaDeCaja")) {
        			request.setAttribute("modoAltaCaja", true);
                }
                // Editar un registro de la pestaña activa
                else if (operacion.equals("editarRegistroPestaña")) {
                	if (formulario.getPestañaActiva() == 4) {
    					if (!permisos.isPuede(FormularioTiendaBean.OPERACION_GESTIONAR_CAJAS)) {
    						return SIN_PERMISO;
    					}
    					
    					TiendaCajaBean caja = (TiendaCajaBean) formulario.getFormularioPestañaActiva().getRegistros().get(Integer.parseInt(indice));
    					request.getSession().setAttribute("caja", caja);
    					request.setAttribute("modoEdicionCaja", true);
                	}else if(formulario.getPestañaActiva() == 5){
                		TiendaServicioBean servicioTienda = (TiendaServicioBean) formulario.getFormularioPestañaActiva().getRegistros().get(Integer.parseInt(indice));
                		if(servicioTienda != null){
	                		formulario.getFormularioPestañaActiva().setModoEdicion(Integer.parseInt(indice));
                		}
                	}else if(formulario.getPestañaActiva() == 7) {
                		formulario.getFormularioPestañaActiva().setModoVisualizacionFicha();
                		
                	}
                	else{
                		formulario.getFormularioPestañaActiva().setModoEdicion(Integer.parseInt(indice));
                	}
                }
                // Eliminar un registro de la pesaña activa
                else if (operacion.equals("eliminarRegistroPestaña")) {
                	// Comprobar que el usuario tiene permisos para gestionar cajas si está en la pestaña de cajas
                	if (formulario.getPestañaActiva() == 4) {
    					if (!permisos.isPuede(FormularioTiendaBean.OPERACION_GESTIONAR_CAJAS)) {
    						return SIN_PERMISO;
    					}
                	}
                	if(formulario.getPestañaActiva() == 5) {
                		formulario.getFormularioPestañaActiva().seleccionaRegistroActivo(Integer.parseInt(indice));
                		TiendaServicioBean tiendaServicio = (TiendaServicioBean) formulario.getFormularioPestañaActiva().getRegistroActivo();
                		for(TiendaServicioCPBean cp : tiendaServicio.getCps()){
                			cp.setEstadoBean(Estado.BORRADO);
                		}
                	}
                  	if(formulario.getPestañaActiva() == 6) {
                  		//Borramos la configuración de la pasarela.
                  		formulario.getFormularioPestañaActiva().seleccionaRegistroActivo(Integer.parseInt(indice));
                		MedioPagoTiendaBean medioPagoTienda = (MedioPagoTiendaBean) formulario.getFormularioPestañaActiva().getRegistroActivo();
                		medioPagoTienda.getConfiguracionMedioPago().setEstadoBean(Estado.BORRADO);
                		medioPagoTienda.getConfiguracionMedioPago().setIdConfPasarela("");
						medioPagoTienda.getConfiguracionMedioPago().setDesconfpasarela("");
                		
                	}
                  	//Pestaña de tarifas
                  	if (formulario.getPestañaActiva() == 1) {
                  		formulario.getFormularioPestañaActiva().seleccionaRegistroActivo(Integer.parseInt(indice));
                  		@SuppressWarnings("unchecked")
						List<TarifaTiendaBean> listTarifas = (List<TarifaTiendaBean>)formulario.getFormularioPestañaActiva().getRegistros();
                  		TarifaTiendaBean tarifaSeleccionada =  (TarifaTiendaBean)formulario.getFormularioPestañaActiva().getRegistroActivo();
                  		
                  		for(TarifaTiendaBean tarifaAsociada : listTarifas) {
                  			if(!tarifaAsociada.isEstadoBorrado() && 
                  					!TarifaTiendaBean.GENERAL.equals(tarifaAsociada.getCodTarifa()) &&
                  					(tarifaAsociada.getCodTarifaPadre().equals(tarifaSeleccionada.getCodTarifa()))) {
                  				setMensajeError(request, datosSesion.getTranslation()._("No puede eliminar la tarifa {0} ya que es padre de la tarifa {1}", tarifaSeleccionada.getCodTarifa(), tarifaAsociada.getCodTarifa()));
                  				return getControlador().getAccion("verFormulario").ejecutar(request);
                  			}
                  		}
                  	}
                	formulario.getFormularioPestañaActiva().eliminar(Integer.parseInt(indice));
                }
                else if(operacion.equals("editarCP")){
                	TiendaServicioCPBean cp = ((TiendaServicioBean)formulario.getFormularioPestañaActiva().getRegistroActivo()).getCps().get(Integer.parseInt(indice));
                	cp.setEstadoBean(Estado.MODIFICADO);
                	request.setAttribute("editarPorte", "ok");
                	request.setAttribute("codPostal", cp);
                }
                else if(operacion.equals("eliminarCP")){
                	((TiendaServicioBean)formulario.getFormularioPestañaActiva().getRegistroActivo()).getCps().get(Integer.parseInt(indice)).setEstadoBean(Estado.BORRADO);
                	formulario.getFormularioPestañaActiva().getRegistroActivo().setEstadoBean(Estado.MODIFICADO);
                }
                else if(operacion.equals("aceptarCP")){
                	leerFormularioRegistro(formulario, request);
                }
                
                // Aceptar cambios del formulario de la pestaña activa
                else if (operacion.equals("aceptarRegistroPestaña")) {
                	leerFormularioRegistro(formulario, request);
                	if(formulario.getPestañaActiva() == 7) {
        				formulario.getFormularioPestañaActiva().setModoVisualizacionTabla();
                	}
                	formulario.getFormularioPestañaActiva().aceptar();
                	
                }
                // Cancelar cambios del formulario de la pestaña activa
                else if (operacion.equals("cancelarRegistroPestaña")) {
                	if(formulario.getPestañaActiva() == 7) {
                		formulario.getFormularioPestañaActiva().setModoVisualizacionTabla();
                	}
                	formulario.getFormularioPestañaActiva().cancelar();
                }
                // Aceptar cambios del formulario
                else if (operacion.equals("aceptar")) {
                	// Si tenemos algún formulario en edición forzamos a que se acepten o cancelen los cambios
                	int pestaña = formulario.getIndicePrimeraPestañaEditable();
                	if (pestaña != -1) {
                		formulario.setPestañaActiva(pestaña);
                		setMensajeError(request, datosSesion.getTranslation()._("Debe aceptar o cancelar los cambios de la línea antes de salvar la tienda"));
                	}
                	else {
                		Tienda tienda = (Tienda) formulario.getRegistroActivo();
                		// Comprobar que existen tarifas 
                		if(!tienda.isExistenTarifas()) {
                			formulario.setPestañaActiva(1); // Pestaña de tarifas
                			setMensajeError(request, datosSesion.getTranslation()._("La tienda debe tener asignada al menos una tarifa"));
                			
                			return getControlador().getAccion("verFormulario").ejecutar(request);
                		}
                		
                		// Comprobar que existen usuarios
                		if(!tienda.isExistenUsuarios()) {
                			formulario.setPestañaActiva(3); // Pestaña de usuarios
                			setMensajeError(request, datosSesion.getTranslation()._("La tienda debe tener asignada al menos un usuario"));
                			
                			return getControlador().getAccion("verFormulario").ejecutar(request);
                		}
                		
                		//Comprobar que en los medios de pago se encuentren los que estén asignados
                		
                		if(tienda.isExistenMediosPago()){
                			String codmedpag = tienda.getCodMedioPago();
                			String codmedpagApart = tienda.getCodMedioPagoApartados();
                			String codmedpagPromo = tienda.getCodMedioPagoPromociones();
                			List<String> cods = new ArrayList<String>();
                			for(MedioPagoTiendaBean medioPago : tienda.getMediosPago()){
                				if(!medioPago.isEstadoBorrado()){
                					cods.add(medioPago.getCodmedpag());
                				}
                			}
                			if((codmedpag != null && !cods.contains(codmedpag))){
                				formulario.setPestañaActiva(6);
                				setMensajeError(request, datosSesion.getTranslation()._("Debe añadirse a la lista de medios de pago el medio de pago asignado a la tienda, con código ")+codmedpag);
                    			
                    			return getControlador().getAccion("verFormulario").ejecutar(request);
                			}
                			if((codmedpagApart != null && !cods.contains(codmedpagApart))){
                				formulario.setPestañaActiva(6);
                				setMensajeError(request, datosSesion.getTranslation()._("Debe añadirse a la lista de medios de pago el medio de pago de apartados asignado a la tienda, con código ")+codmedpagApart);
                    			
                    			return getControlador().getAccion("verFormulario").ejecutar(request);
                			}
                			if((codmedpagPromo != null && !cods.contains(codmedpagPromo))){
                				formulario.setPestañaActiva(6);
                				setMensajeError(request, datosSesion.getTranslation()._("Debe añadirse a la lista de medios de pago el medio de pago de promociones asignado a la tienda, con código ")+codmedpagPromo);
                    			
                    			return getControlador().getAccion("verFormulario").ejecutar(request);
                			}
                		}
                		request.getSession().removeAttribute("caja");
                		formulario.getFormularioPestañaActiva().setModoVisualizacionTabla();
                		return getControlador().getAccion("salvar").ejecutar(request);
                	}
                }
                // Cancelar cambios en el formulario
                else if (operacion.equals("cancelar")) {
                	request.getSession().removeAttribute("caja");
                	
                	formulario.cancelar();
                	
                	// Si no tenemos registro activo volvemos al buscador
                	if (formulario.isRegistroActivoVacio()) {
                		return getControlador().getAccion("buscar").ejecutar(request);
                	}
                	else {
                		// Obtenemos el artículo y lo actualizamos en el formulario
                		Tienda tienda = (Tienda) formulario.getRegistroActivo();
                		tienda = ServicioTiendasImpl.get().consultar(tienda.getCodAlm(), datosSesion);
                		formulario.inicializar(tienda);
                		formulario.getFormularioPestañaActiva().setModoVisualizacionTabla();
//                		request.setAttribute("modoFicha", false);
                	}
                }
                // Reiniciar la version de la tienda
                else if(operacion.equals("forzarEnvioArticulos")){
                	forzarEnvioArticulosTienda(sesion, (Tienda) formulario.getRegistroActivo());
                }
                // Reiniciar la version de la tarida
                else if(operacion.equals("forzarEnvioTarifa")){
        			forzarEnvioTarifa((TarifaTiendaBean) formulario.getFormularioPestañaActiva().getRegistroActivo());
                }
                // Forzar envío de artículos a caja
                else if(operacion.equals("forzarEnvioCaja")){
                	Tienda tiendaFormulario = (Tienda) formulario.getRegistroActivo();
                	final TiendaCajaBean caja = tiendaFormulario.getCajas().get(Integer.parseInt(indice));
        			final DatosSesionBean datosSesionAux = datosSesion;
        			
        			// hacer una copia del bean para no interferir con los datos del formulario actual
        			final Tienda tienda = ServicioTiendasImpl.get().consultar(tiendaFormulario.getCodAlm(), datosSesion.getConfigEmpresa());
        			
                	Runnable r = new Runnable() {
       		        public void run() {
       	            	   ServicioDatosSincronizacion.get().generarDatosSincronizacionArticulosCajaForzado(datosSesionAux.getConfigEmpresa(), tienda.getBean(), caja.getUidCaja());
       		        	}
       		        };
       		        ExecutorService executor = Executors.newCachedThreadPool();
       		        executor.submit(r);
       		        
       		        setMensaje(request, datosSesion.getTranslation()._("Se están regenerando por completo los datos de artículos para tienda y caja seleccionada. Estarán disponibles en unos momentos."));
                }
                // Activa o desactiva una caja
                else if(operacion.equals("cambiarEstadoCaja")) {
					// Comprobar que el usuario tiene permisos para gestionar cajas
					if (!permisos.isPuede(FormularioTiendaBean.OPERACION_GESTIONAR_CAJAS)) {
						return SIN_PERMISO;
					}
                	
                	Tienda tienda = (Tienda) formulario.getRegistroActivo();
                	TiendaCajaBean caja = tienda.getCajas().get(Integer.parseInt(indice));
        			if (caja.getEstadoBean() == Estado.SIN_MODIFICAR) {
        				caja.setEstadoBean(Estado.MODIFICADO);
        			}
                	caja.setActivo((caja.getActivoString().equalsIgnoreCase("S")) ? "N" : "S");
                }
                else if (operacion.equals("aceptarServicio")){
                	switch (formulario.getPestañaActiva()) {
        				case 5:  // Servicios
	        				if (formulario.getFormularioPestañaActiva().isEnInsercion()) {
	        					leerFormularioServiciosTienda(formulario, request);
		                    	// Ponemos en formulario en modoInsercion=false
		                    	formulario.getFormularioPestañaActiva().cancelar();
	        				}
	        				if (formulario.getFormularioPestañaActiva().isEditable()) {
	        					leerFormularioRegistro(formulario, request);
		                    	// Ponemos en formulario en modoInsercion=false
		                    	formulario.getFormularioPestañaActiva().cancelar();
	        				}
	                    	break;
                	}
                	
                }
           
                else if (operacion.equals("aceptarMedioPago")){
                	switch (formulario.getPestañaActiva()) {
        				case 6:  // Medios de pago
	        				if (formulario.getFormularioPestañaActiva().isEnInsercion()) {
	        					leerFormularioMediosPagoTienda(formulario, request);
		                    	// Ponemos en formulario en modoInsercion=false
		                    	formulario.getFormularioPestañaActiva().cancelar();
	        				}
	        				if (formulario.getFormularioPestañaActiva().isEditable()) {
	        					leerFormularioRegistro(formulario, request);
		                    	// Ponemos en formulario en modoInsercion=false
		                    	formulario.getFormularioPestañaActiva().cancelar();
	        				}
	                    	break;
                	}
                	
                }else if (operacion.equals("aceptarAtcud")){
                	switch (formulario.getPestañaActiva()) {
        				case 8:  // Atcud
	        				if (formulario.getFormularioPestañaActiva().isEnInsercion()) {
	        					leerFormularioAtcud(formulario, request);
		                    	// Ponemos en formulario en modoInsercion=false
		                    	formulario.getFormularioPestañaActiva().cancelar();
	        				}
	                    	break;
                	}
                }
            }
            
            // Mostramos el formulario
            return getControlador().getAccion("verFormulario").ejecutar(request);
		}
		catch (Exception e) {
			log.debug("ejecutar() - Ha ocurrido un error : " + e.getMessage());
            setError(request, e);
            
            return ERROR_PAGE;
		}
	}
	@SuppressWarnings("unchecked")
	protected void leerFormularioAtcud(FormularioTiendaBean formulario, HttpServletRequest request) {
		String idContador = request.getParameter("idContadorHidden");
		String divisor1 = request.getParameter("divisor1Hidden");
		String divisor2 = request.getParameter("divisor2Hidden");
		String divisor3 = request.getParameter("divisor3Hidden");
		String rango = request.getParameter("rangoHidden");
		
		AtcudMagento atcud = new AtcudMagento();
		atcud.setIdContador(idContador);
		atcud.setMascaraDivisor1(divisor1);
		atcud.setMascaraDivisor2(divisor2);
		atcud.setMascaraDivisor3(divisor3);
		atcud.setRango(rango);
		atcud.setEstadoBean(Estado.NUEVO);
		
		formulario.getFormularioPestañaActiva().getRegistros().add(atcud);
	}
}
