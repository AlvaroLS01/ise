package com.comerzzia.bimbaylola.pos.dispositivo.fidelizacion.seleccion;

import javafx.beans.property.SimpleStringProperty;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.model.fidelizacion.fidelizados.FidelizadoBean;
import com.comerzzia.model.fidelizacion.fidelizados.contactos.TiposContactoFidelizadoBean;
import com.comerzzia.pos.dispositivo.fidelizacion.seleccion.SeleccionFidelizadoGui;

@Component
@Primary
public class ByLSeleccionFidelizadoGui extends SeleccionFidelizadoGui{
	
    private static final String MOVIL = "MOVIL";
	private SimpleStringProperty email;
    private SimpleStringProperty direccion;
    private SimpleStringProperty telefono;
    
    public ByLSeleccionFidelizadoGui(FidelizadoBean fidelizado){
		super(fidelizado);
    	if(fidelizado.getTipoContacto(MOVIL) != null){
    		telefono = new SimpleStringProperty(fidelizado.getTipoContacto(MOVIL).getValor());
    	}else{
    		telefono = new SimpleStringProperty();
    	}
    	if(fidelizado.getTipoContacto(TiposContactoFidelizadoBean.EMAIL) != null){
    		email = new SimpleStringProperty(fidelizado.getTipoContacto(TiposContactoFidelizadoBean.EMAIL).getValor());
    	}else{
    		email = new SimpleStringProperty();
    	}
    	if(fidelizado.getDirecciones() != null && !fidelizado.getDirecciones().isEmpty()){
    		direccion = new SimpleStringProperty(fidelizado.getDirecciones().get(0).getDomicilio());
    	}else{
    		direccion = new SimpleStringProperty();
    	}
	}

	public SimpleStringProperty getEmail(){
		return email;
	}

	public void setEmail(SimpleStringProperty email){
		this.email = email;
	}

	public SimpleStringProperty getTelefono(){
		return telefono;
	}

	public void setTelefono(SimpleStringProperty telefono){
		this.telefono = telefono;
	}

	public SimpleStringProperty getDireccion(){
		return direccion;
	}

	public void setDireccion(SimpleStringProperty direccion){
		this.direccion = direccion;
	}
	
}
