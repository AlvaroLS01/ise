package com.comerzzia.bricodepot.backoffice.web.tiendas.acciones;


import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.comerzzia.bricodepot.backoffice.persistence.general.tiendas.atcud.AtcudMagento;
import com.comerzzia.bricodepot.backoffice.services.general.tiendas.atcud.BricodepotServicioAtcud;
import com.comerzzia.bricodepot.backoffice.web.tiendas.ui.BricodepotFormularioTiendaBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.servicios.general.tiendas.Tienda;
import com.comerzzia.web.general.tiendas.acciones.VerFormularioAccion;
import com.comerzzia.web.general.tiendas.ui.FormularioTiendaBean;

public class BricodepotVerFormularioAccion extends VerFormularioAccion {
	
	private Logger log = Logger.getLogger(getClass());
	@Autowired
	protected BricodepotServicioAtcud servicioAtcud;
	@Override
	protected void inicializaPestañaActiva(FormularioTiendaBean formulario, DatosSesionBean datosSesion, HttpServletRequest request) throws Exception {
		
		super.inicializaPestañaActiva(formulario, datosSesion, request);
		
		log.debug("inicializaPestañaActiva() - Inicializando pestaña Configuración ATCUD tienda online");
		
		
		try {
			if(formulario.getPestañaActiva() == BricodepotFormularioTiendaBean.PESTANIA_ATCUD) {
				
				
				Tienda tienda = (Tienda) formulario.getRegistroActivo();
				if (!isAtcudsTiendaCargados(tienda)) {
					cargaAtcudsTienda(tienda,datosSesion);
					formulario.getFormularioPestañaActiva().setRegistros(getAtcudsTienda(tienda));
				}
				List<AtcudMagento> lista = ((BricodepotFormularioTiendaBean) formulario).obtenerRegistrosDeTiendas();
				request.setAttribute("lstRangos", lista);
			}
		}catch (Exception e) {
			log.error("inicializaPestañaActiva() - Error inicializando pestaña Configuración ATCUD tienda online :" + e.getMessage(),e);
			throw e;
		}
	}
	@SuppressWarnings("unchecked")
	private List<AtcudMagento> getAtcudsTienda(Tienda tienda){
		List<AtcudMagento> extensionAtcudsienda = (List<AtcudMagento>) tienda.getExtension("AtcudsTienda");
		return extensionAtcudsienda;
	}
	private void cargaAtcudsTienda (Tienda tienda, DatosSesionBean datosSesion) {
		if(tienda.getExtensiones() == null) {
			tienda.setExtensiones(new HashMap<String,Object>());
		}
		List<AtcudMagento> lista = servicioAtcud.consultarRangosFiscales(datosSesion, tienda.getCodAlm());
		tienda.getExtensiones().put("AtcudsTienda", lista);
	}
	
	@SuppressWarnings("unchecked")
	private boolean isAtcudsTiendaCargados(Tienda tienda) {
		boolean existeExtensionGruposTienda = tienda.getExtensiones() != null && tienda.getExtension("AtcudsTienda") != null && tienda.getExtension("AtcudsTienda") != null;
		if(!existeExtensionGruposTienda) {
			return false;
		}
		List<AtcudMagento> extensionGrupoTienda = (List<AtcudMagento>) tienda.getExtension("AtcudsTienda");
		boolean extensionGruposTiendaCargada = !(extensionGrupoTienda.isEmpty());
		
		return existeExtensionGruposTienda && extensionGruposTiendaCargada;
	}
}
