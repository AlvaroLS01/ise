package com.comerzzia.bimbaylola.pos.gui.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.EventHandler;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.util.Callback;
import netscape.javascript.JSException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.services.core.sesion.ByLSesionUsuario;
import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.view.ActionView;
import com.comerzzia.pos.gui.backoffice.web.BackofficeController;
import com.comerzzia.pos.persistence.core.acciones.AccionBean;
import com.comerzzia.pos.services.core.sesion.Sesion;

@Component
@Primary
public class ByLBackofficeController extends BackofficeController{
	
	private static final Logger log = Logger.getLogger(ByLBackofficeController.class.getName());
	
	@Autowired
	private Sesion sesion;
	
	@Override
    public void initializeForm() throws InitializeGuiException {
    	ActionView view = (ActionView) getView();
        AccionBean accion = view.getAccion();
        
        lbTitulo.setText(accion.getTitulo());
        if(accion.getParametros() != null){
        	if (url == null || !url.equals(accion.getParametros())) {
	        	this.url = accion.getParametros();
		        final WebEngine webEngine = navegador.getEngine();
		        
		    	webEngine.setConfirmHandler(new Callback<String, Boolean>() {

					@Override
					public Boolean call(String mensaje) {
						return VentanaDialogoComponent.crearVentanaConfirmacion(mensaje, getStage());
					}
				});
				
				webEngine.setOnAlert(new EventHandler<WebEvent<String>>() {
					
					@Override
					public void handle(WebEvent<String> event) {
						VentanaDialogoComponent.crearVentanaAviso(event.getData(), getStage());
					}
				});
				
				 webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>(){
					 
				     @Override
				     public void changed(@SuppressWarnings("rawtypes") ObservableValue value, State oldState, State newState) {
				      if (newState == State.SUCCEEDED) {
				       try {
				        webEngine.executeScript("document.getElementById('usuario').value = '" + sesion.getSesionUsuario().getUsuario().getUsuario() 
				        		+ "';document.getElementById('password').value = '" + ((ByLSesionUsuario)sesion.getSesionUsuario()).getPassword() + "';aceptar();");
				       }
				       catch (JSException e) {
				       }
				      }
				     }
				    });
				
		        progressBar.setVisible(true);
		        progressBar.setPrefHeight(progressBar.getMaxHeight());
		        progressBar.progressProperty().bind(webEngine.getLoadWorker().progressProperty());
		        
		        log.debug("initializeForm() - Cargando URL: " + url.toString());
		        webEngine.load(url);
        	}
        }else{
        	log.error("initializeForm() - La acción no tiene URL en la columna parámetros. No hacemos nada.");
        }
    }

}
