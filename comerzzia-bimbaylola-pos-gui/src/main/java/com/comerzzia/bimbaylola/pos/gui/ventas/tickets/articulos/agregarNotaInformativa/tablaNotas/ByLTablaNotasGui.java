package com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.agregarNotaInformativa.tablaNotas;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

import com.comerzzia.bimbaylola.pos.persistence.ticket.articulos.agregarnotainformativa.AvisoInformativoBean;

public class ByLTablaNotasGui {
    
    private SimpleStringProperty desDoc;
    
    private SimpleStringProperty codDoc;
		
	private AvisoInformativoBean concepto;
	
	public ByLTablaNotasGui(AvisoInformativoBean concepto){
		desDoc = new SimpleStringProperty();
		codDoc = new SimpleStringProperty();
		desDoc.setValue(concepto.getDescripcion());
		codDoc.setValue(concepto.getCodigo());
		this.concepto = concepto;
	}

	public String getDescripcion() {
		return desDoc.getValue();
	}

	public void setDescripcion(String descripcion) {
		this.desDoc.setValue(descripcion);
	}

	public String getCodigo() {
		return codDoc.getValue();
	}

	public void setCodigo(String codigo) {
		this.codDoc.setValue(codigo);
	}

	public SimpleStringProperty getDescripcionProperty(){
		return desDoc;
	}
	
	public SimpleStringProperty getCodigoProperty(){
		return codDoc;
	}
	
	public AvisoInformativoBean getConcepto(){
		return concepto;
	}

	public ObservableValue<String> getCodDoc() {
		return codDoc;
		
	}

	public ObservableValue<String> getDesDoc() {
		return desDoc;
		
	}
}
