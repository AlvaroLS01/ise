package com.comerzzia.bricodepot.backoffice.web.tiendas.ui;


import java.util.List;

import com.comerzzia.bricodepot.backoffice.persistence.general.tiendas.atcud.AtcudMagento;
import com.comerzzia.web.core.ui.FormularioBean;
import com.comerzzia.web.general.tiendas.ui.FormularioTiendaBean;


public class BricodepotFormularioTiendaBean extends FormularioTiendaBean {
	
	public static final int PESTANIA_ATCUD = 8;
	
	protected FormularioBean pestañaAtcud = new FormularioBean("Configuración ATCUD tienda online");
	
	public BricodepotFormularioTiendaBean() {
		super();
		this.addPestaña(pestañaAtcud);
	}
	
	@SuppressWarnings("unchecked")
	public List<AtcudMagento> obtenerRegistrosDeTiendas(){
		List<AtcudMagento> lista = pestañaAtcud.getRegistros();
		return lista;
	}
}
