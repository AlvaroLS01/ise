package com.comerzzia.bricodepot.backoffice.web.usuarios.acciones;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.comerzzia.bricodepot.backoffice.services.usuarios.ServicioCambiarClaveImpl;
import com.comerzzia.core.model.usuarios.UsuarioBean;
import com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.servicios.usuarios.UsuarioException;
import com.comerzzia.core.servicios.usuarios.UsuarioNotFoundException;
import com.comerzzia.core.servicios.usuarios.UsuariosService;
import com.comerzzia.core.util.i18n.Translation;
import com.comerzzia.validacion.passwords.PasswordChangeValidator;
import com.comerzzia.validacion.passwords.constants.ErroresCambioPassword;
import com.comerzzia.validacion.passwords.exceptions.LowerCaseException;
import com.comerzzia.validacion.passwords.exceptions.MinNumCharException;
import com.comerzzia.validacion.passwords.exceptions.NewPasswordConfirmEmptyException;
import com.comerzzia.validacion.passwords.exceptions.NewPasswordEmptyException;
import com.comerzzia.validacion.passwords.exceptions.OldPasswordEmptyException;
import com.comerzzia.validacion.passwords.exceptions.SamePasswordException;
import com.comerzzia.validacion.passwords.exceptions.SpecialCharException;
import com.comerzzia.validacion.passwords.exceptions.UpperCaseException;
import com.comerzzia.web.base.Accion;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;
import com.comerzzia.web.i18n.ui.i18nUtil;

public class BricodepotRestablecerContrasenaAccion extends Accion{

	private static final Vista NEXT_PAGE = new Vista("backoffice/usuarios/mantenimiento/jsp/usuario.jsp", Vista.INTERNA);
	
	@Autowired
	protected ServicioCambiarClaveImpl servicioCambiarClave;
	
	@Autowired
	protected UsuariosService servicioUsuariosImpl;
	
	public String getNombre() {
		return "restablecerContrasena";
	}

	public Vista ejecutar(HttpServletRequest request) {
		HttpSession sesion = request.getSession();
		DatosSesionBean datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);

		Translation t = i18nUtil.getTranslation(request, null);

		try{
    		// Comprobamos los permisos de la acción
    		PermisosEfectivosAccionBean permisos = datosSesion.getPermisosCache(this.getAccionMenu());
    		if (!permisos.isPuedeEditar()) {
    			return SIN_PERMISO;
    		}

    		//Recogemos el id del usuario
    		Long idUsuario = Long.parseLong(request.getParameter(WebKeys.ID_OBJETO));
    		
    		//Recogemos la nueva clave
    		String clave = request.getParameter("clave");
    		String password = request.getParameter("password");
    		String claveConfirma = request.getParameter("confirma");

			// VALIDACIONES DE LA NUEVA CLAVE
			try {
				
				compararClaves(datosSesion, idUsuario, clave);
				
				PasswordChangeValidator validadorClave = PasswordChangeValidator.getInstance(clave, password, claveConfirma);
				validadorClave.validatePasswordChange();
			}
			catch (Exception e) {
				setMensajeErrorValidacion(request, e, t);
				return NEXT_PAGE;
			}
    		
    		//Guardamos los cambios
			servicioCambiarClave.cambiarClaveUsuario(datosSesion, idUsuario, clave, true);

			//Mensaje para la ventana de información
			setMensaje(request, datosSesion.getTranslation()._("La contraseña se ha restablecido correctamente"));
			
			return NEXT_PAGE;
		}    
		catch (UsuarioException e) {
			setMensajeError(request, datosSesion.getTranslation()._("No se ha podido restablecer la contraseña"));
            return NEXT_PAGE;
    	}
    	catch (Exception e) {
            setError(request, e);
            
            return NEXT_PAGE;
        }
	}

	private void setMensajeErrorValidacion(HttpServletRequest request, Exception e, Translation t) { // BRICO-326
		if (e instanceof OldPasswordEmptyException) {
			setMensajeError(request, t._(ErroresCambioPassword.MSG_ERROR_OLD_PASS_EMPTY));
		}
		else if (e instanceof NewPasswordEmptyException) {
			setMensajeError(request, t._(ErroresCambioPassword.MSG_ERROR_NEW_PASS_EMPTY));
		}
		else if (e instanceof NewPasswordConfirmEmptyException) {
			setMensajeError(request, t._(ErroresCambioPassword.MSG_ERROR_NEW_PASS_CONFIRM));
		}
		else if (e instanceof SamePasswordException) {
			setMensajeError(request, t._(ErroresCambioPassword.MSG_ERROR_MISMA_CONTRASENA_ANTERIOR));
		}
		else if (e instanceof MinNumCharException) {
			setMensajeError(request, t._(ErroresCambioPassword.MSG_ERROR_MIN_NUM_CHARS));
		}
		else if (e instanceof LowerCaseException) {
			setMensajeError(request, t._(ErroresCambioPassword.MSG_ERROR_LOWER_CLASE));
		}
		else if (e instanceof UpperCaseException) {
			setMensajeError(request, t._(ErroresCambioPassword.MSG_ERROR_UPPER_CLASE));
		}
		else if (e instanceof SpecialCharException) {
			setMensajeError(request, t._(ErroresCambioPassword.MSG_ERROR_SPECIAL_CHAR));
		}
	}
	
	private void compararClaves(DatosSesionBean datosSesion, Long idUsuario, String clave) throws UsuarioException, UsuarioNotFoundException, SamePasswordException {
		UsuarioBean usuarioRecuperado = servicioUsuariosImpl.consultar(datosSesion, idUsuario);
		
		if(usuarioRecuperado.getClave().equals(clave)) {
			throw new SamePasswordException();
		}
	}
}
