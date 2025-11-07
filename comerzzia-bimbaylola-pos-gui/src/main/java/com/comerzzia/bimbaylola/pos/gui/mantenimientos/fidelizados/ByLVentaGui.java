package com.comerzzia.bimbaylola.pos.gui.mantenimientos.fidelizados;

import javafx.beans.property.SimpleStringProperty;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.model.ventas.albaranes.articulos.ArticuloAlbaranVentaBean;
import com.comerzzia.pos.gui.mantenimientos.fidelizados.VentaGui;

@Component
@Primary
public class ByLVentaGui extends VentaGui{

	private SimpleStringProperty codTicket; 
	
	public ByLVentaGui(ArticuloAlbaranVentaBean articulo){
		super(articulo);
		codTicket = new SimpleStringProperty(articulo.getCodTicket()== null ? "" : articulo.getCodTicket());
	}

	public String getCodTicket(){
		return codTicket.getValue();
	}
	
}
