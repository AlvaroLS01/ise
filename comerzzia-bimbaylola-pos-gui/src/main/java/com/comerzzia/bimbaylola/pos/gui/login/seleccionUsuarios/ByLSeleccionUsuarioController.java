package com.comerzzia.bimbaylola.pos.gui.login.seleccionUsuarios;

import java.util.HashMap;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Controller;

import com.comerzzia.bimbaylola.pos.services.core.sesion.ByLSesionUsuario;
import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.componentes.botonaccion.BotonBotoneraComponent;
import com.comerzzia.pos.core.gui.componentes.botonaccion.usuario.BotonBotoneraUsuarioComponent;
import com.comerzzia.pos.core.gui.login.LoginController;
import com.comerzzia.pos.core.gui.login.LoginView;
import com.comerzzia.pos.core.gui.login.seleccionUsuarios.SeleccionUsuarioController;
import com.comerzzia.pos.core.gui.main.MainView;
import com.comerzzia.pos.core.gui.main.MainViewController;
import com.comerzzia.pos.persistence.core.usuarios.UsuarioBean;
import com.comerzzia.pos.services.core.perfiles.ServicioPerfiles;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.util.config.SpringContext;
import com.comerzzia.pos.util.i18n.I18N;

@Controller
@Primary
public class ByLSeleccionUsuarioController extends SeleccionUsuarioController {
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ByLSeleccionUsuarioController.class.getName());
	
	@FXML
	protected TextField usuarioTF;
	@FXML
	protected Button botonAceptar;
	
	@Autowired
    private ServicioPerfiles servicioPerfiles;
	
	@Override
    public void initializeForm() throws InitializeGuiException {
        
        if(this.getDatos().containsKey(PARAMETRO_MODO_PANTALLA_BLOQUEO)){
            modoCambioCajero = true;
            botonCancelar.setVisible(false);
            numFilas = NUM_FILAS_FLOTANTE;
            modoBloqueoTPV = true;
        }
        else if(this.getDatos().containsKey(PARAMETRO_MODO_PANTALLA_CAJERO)){
            modoCambioCajero = true;
            botonCancelar.setVisible(true);
            botonCancelar.setText(I18N.getTexto("Cancelar"));
            numFilas = NUM_FILAS_FLOTANTE;
            modoBloqueoTPV = false;
        }
        else{
            modoBloqueoTPV = false;
            modoCambioCajero = false;
            botonCancelar.setVisible(true);
            botonCancelar.setText(I18N.getTexto("Salir"));
            numFilas = NUM_FILAS;
        }
        crearBotoneraUsuarios();
        rellenarDatosTienda();
        botonAceptar.setText(I18N.getTexto("Aceptar"));
        usuarioTF.setText("");
        usuarioTF.setVisible(true);
        botonAceptar.setVisible(true);
    }
	
	@Override
    public void initializeFocus() {
		usuarioTF.requestFocus();
    }
	
	@Override
    public void realizarAccion(BotonBotoneraComponent botonAccionado) {
        
        log.trace("realizarAccion() - Realizando la acción : " + botonAccionado.getClave() + " de tipo : " + botonAccionado.getTipo());
        
        switch(botonAccionado.getClave()){
            // BOTONERA TRANSICION USUARIOS
            case "ACCION_USUARIOS_SIGUIENTES":
                if(indice*NUM_COLUMNAS+NUM_COLUMNAS*numFilas<usuarios.size()){
                    indice++;
                    crearBotoneraUsuarios();
                }
                break;
            case "ACCION_USUARIOS_ANTERIORES":
                if(indice>0){
                    indice--;
                    crearBotoneraUsuarios();
                }
                break;
            case "ACCION USUARIO":
                BotonBotoneraUsuarioComponent boton = (BotonBotoneraUsuarioComponent) botonAccionado;
                UsuarioBean usuarioSeleccionado = boton.getUsuario();
                
                HashMap<String, Object> parametrosLogin = new HashMap<>();
                
                parametrosLogin.put(LoginController.PARAMETRO_USUARIO_SELECCIONADO, usuarioSeleccionado.getUsuario());
                
                if(modoCambioCajero){
                    parametrosLogin.put(LoginController.PARAMETRO_ENTRADA_ES_MODO_SELECCION_CAJERO, "S");
    				try {
						ByLSesionUsuario sesionUsuario = (ByLSesionUsuario) SpringContext.getBean(Sesion.class).getSesionUsuario();
						sesionUsuario.init(usuarioSeleccionado.getUsuario(), usuarioSeleccionado.getUsuario());
						sesionUsuario.clearPermisos();
						sesionUsuario.setPassword(usuarioSeleccionado.getUsuario());
		            	getApplication().comprobarPermisosUI();
		            	parametrosLogin.put(LoginController.PARAMETRO_SALIDA_CAMBIO_USUARIO, "S");
		            	tratarDatosSalidaVentana(parametrosLogin);
		            	((MainViewController)getApplication().getMainView().getController()).actualizarUsuario();
    				} catch (Exception e) {
    					log.warn("El usuario " + usuarioSeleccionado.getUsuario() + " no se ha podido logar: " + e.getMessage());
    					getApplication().getMainView().showModal(LoginView.class, parametrosLogin);
    					tratarDatosSalidaVentana(parametrosLogin);
    				}
                }
                else{
                	try {
    					ByLSesionUsuario sesionUsuario = (ByLSesionUsuario) SpringContext.getBean(Sesion.class).getSesionUsuario();
    					sesionUsuario.init(usuarioSeleccionado.getUsuario(), usuarioSeleccionado.getUsuario());
    					sesionUsuario.clearPermisos();
    					sesionUsuario.setPassword(usuarioSeleccionado.getUsuario());
    	            	getApplication().comprobarPermisosUI();
    	            	getApplication().showFullScreenView(SpringContext.getBean(MainView.class));
    	            	getApplication().activarTimer();
    	            	((MainViewController)getApplication().getMainView().getController()).actualizarUsuario();
    				} catch (Exception e) {
    					log.warn("El usuario " + usuarioSeleccionado.getUsuario() + " no se ha podido logar: " + e.getMessage());
    					getApplication().showFullScreenView(LoginView.class, parametrosLogin);
    				}
                }
                break;
        }
    }
	
	@FXML
	protected void accionAceptar() {
		String usuario = usuarioTF.getText();
		HashMap<String, Object> parametrosLogin = new HashMap<>();
		
		if(modoCambioCajero){
            parametrosLogin.put(LoginController.PARAMETRO_ENTRADA_ES_MODO_SELECCION_CAJERO, "S");
            if(usuario != null && !usuario.isEmpty()) {
				UsuarioBean usuarioBean = null;
				try {
					usuarioBean = usuariosService.consultarUsuario(usuario);
					parametrosLogin.put(LoginController.PARAMETRO_USUARIO_SELECCIONADO, usuarioBean.getUsuario());
					
					ByLSesionUsuario sesionUsuario = (ByLSesionUsuario) SpringContext.getBean(Sesion.class).getSesionUsuario();
					sesionUsuario.init(usuario, usuario);
					sesionUsuario.clearPermisos();
					sesionUsuario.setPassword(usuario);
	            	getApplication().comprobarPermisosUI();
	            	parametrosLogin.put(LoginController.PARAMETRO_SALIDA_CAMBIO_USUARIO, "S");
	            	tratarDatosSalidaVentana(parametrosLogin);
	            	((MainViewController)getApplication().getMainView().getController()).actualizarUsuario();
				} catch (Exception e) {
					log.warn("El usuario " + usuario + " no se ha podido logar: " + e.getMessage());
					getApplication().getMainView().showModal(LoginView.class, parametrosLogin);
					tratarDatosSalidaVentana(parametrosLogin);
				}
			}
        }else{
			if(usuario != null && !usuario.isEmpty()) {
				UsuarioBean usuarioBean = null;
				try {
					usuarioBean = usuariosService.consultarUsuario(usuario);
					parametrosLogin.put(LoginController.PARAMETRO_USUARIO_SELECCIONADO, usuarioBean.getUsuario());
					
					ByLSesionUsuario sesionUsuario = (ByLSesionUsuario) SpringContext.getBean(Sesion.class).getSesionUsuario();
					sesionUsuario.init(usuario, usuario);
					sesionUsuario.clearPermisos();
					sesionUsuario.setPassword(usuario);
	            	getApplication().comprobarPermisosUI();
	            	getApplication().showFullScreenView(SpringContext.getBean(MainView.class));
	            	getApplication().activarTimer();
	            	((MainViewController)getApplication().getMainView().getController()).actualizarUsuario();
				} catch (Exception e) {
					log.warn("El usuario " + usuario + " no se ha podido logar: " + e.getMessage());
					getApplication().showFullScreenView(LoginView.class, parametrosLogin);
				}
			}
        }
	}
	
	@FXML
	protected void accionAceptarIntro(KeyEvent e) {
		if(e.getCode()==KeyCode.ENTER){
	          accionAceptar();
	    }
	}

}
