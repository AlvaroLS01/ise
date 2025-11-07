package com.comerzzia.bimbaylola.pos.gui.disponibilidadstock;

import java.math.BigDecimal;

import javafx.beans.property.SimpleStringProperty;

import com.comerzzia.pos.core.gui.disponibilidadstock.StockGui;
import com.comerzzia.pos.util.format.FormatUtil;
import com.comerzzia.rest.client.articulos.DisponibilidadArticuloBean;

public class ByLStockGui extends StockGui {
	
	public ByLStockGui(DisponibilidadArticuloBean disponibilidad){
		super(disponibilidad);
		this.disponibilidad = disponibilidad;
		this.codAlm = new SimpleStringProperty(disponibilidad.getStock().getCodAlmacen() == null? "" : disponibilidad.getStock().getCodAlmacen());
		this.desglose1 = new SimpleStringProperty(disponibilidad.getStock().getDesglose1() == null? "" : disponibilidad.getStock().getDesglose1());
		this.desglose2 = new SimpleStringProperty(disponibilidad.getStock().getDesglose2() == null? "" : disponibilidad.getStock().getDesglose2());
		this.desAlm = new SimpleStringProperty(disponibilidad.getStock().getDesAlmacen() == null? "" : disponibilidad.getStock().getDesAlmacen()+(disponibilidad.getTienda().getDistancia() == null ? "" : " ("+procesarDistancia()+")"));
		this.provincia = new SimpleStringProperty(disponibilidad.getStock().getProvincia() == null? "" : disponibilidad.getStock().getProvincia());
		this.cp = new SimpleStringProperty(disponibilidad.getTienda().getCp() == null? "" : disponibilidad.getTienda().getCp() );
		BigDecimal stock = new BigDecimal(disponibilidad.getStock().getStock());
		this.stock = new SimpleStringProperty(FormatUtil.getInstance().formateaNumero(stock, 0));
		
		if(disponibilidad.getStock().getStockA() != null) {
			BigDecimal stockA = new BigDecimal(disponibilidad.getStock().getStockA());
			this.stockA = new SimpleStringProperty(FormatUtil.getInstance().formateaNumero(stockA, 0));
		}
		
		if(disponibilidad.getStock().getStockB() != null) {
			BigDecimal stockB = new BigDecimal(disponibilidad.getStock().getStockB());
			this.stockB = new SimpleStringProperty(FormatUtil.getInstance().formateaNumero(stockB, 0));
		}
		
		if(disponibilidad.getStock().getStockC() != null) {
			BigDecimal stockC = new BigDecimal(disponibilidad.getStock().getStockC());
			this.stockC = new SimpleStringProperty(FormatUtil.getInstance().formateaNumero(stockC, 0));
		}
		
		if(disponibilidad.getStock().getStockD() != null) {
			BigDecimal stockD = new BigDecimal(disponibilidad.getStock().getStockD());
			this.stockD = new SimpleStringProperty(FormatUtil.getInstance().formateaNumero(stockD, 0));
		}
	}

}
