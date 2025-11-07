package com.comerzzia.bimbaylola.pos.gui.login;

import javafx.event.ActionEvent;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Controller;

import com.comerzzia.bimbaylola.pos.services.core.sesion.ByLSesionUsuario;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.login.LoginController;
import com.comerzzia.pos.core.gui.main.MainViewController;
import com.comerzzia.pos.services.core.sesion.SesionInitException;
import com.comerzzia.pos.services.core.usuarios.UsuarioInvalidLoginException;
import com.comerzzia.pos.util.i18n.I18N;

@Controller
@Primary
public class ByLLoginController extends LoginController {
	
	private static final Logger log = Logger.getLogger(ByLLoginController.class.getName());
	
	/**
     * Acción que realiza el login
     * @param event 
     */
    public void actionBtAceptar(ActionEvent event) {
        log.debug("actionBtAceptar()");
        if (accionFrLoginSubmit()){        
            try {                
            	//Comprobamos que el usuario tiene acceso a la pantalla activa
            	if(!hasPermissionsCurrentScreen(tfUsuario.getText())){
            		VentanaDialogoComponent.crearVentanaError(I18N.getTexto("El usuario no tiene permisos para ejecutar la pantalla activa."), getStage());
            		return;
            	}
            	sesion.initUsuarioSesion(tfUsuario.getText(), tfPassword.getText());
            	sesion.getSesionUsuario().clearPermisos();
            	((ByLSesionUsuario)sesion.getSesionUsuario()).setPassword(tfPassword.getText());
            	getApplication().comprobarPermisosUI();
            	
                ((MainViewController)getApplication().getMainView().getController()).actualizarUsuario();
                if (isModoSeleccionDeCajero()){
                    getDatos().put(PARAMETRO_SALIDA_CAMBIO_USUARIO, "S");
                    getStage().close();
                }
                else if(isModoBloqueo()){
                    getApplication().activarTimer();
                	this.getStage().close();
                }
                else{
                    getApplication().showFullScreenView(mainView);
                    getApplication().activarTimer();
                }
            }
            catch (SesionInitException ex) {
                VentanaDialogoComponent.crearVentanaError(getStage(), ex.getMessageDefault(), ex);
                accionLimpiarFormulario();
                initializeFocus();
            } 
            catch (UsuarioInvalidLoginException ex) {
                lbError.setText(I18N.getTexto(ex.getMessageI18N()));
                initializeFocus();
            }
            catch (Exception ex){
                log.debug("actionBtAceptar() - Error no controlado -"+ex.getMessage(),ex);
                VentanaDialogoComponent.crearVentanaError(getStage(), ex);
            }
        }
    }

}
