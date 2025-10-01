package com.comerzzia.bricodepot.backoffice.web.core.login.acciones;

import javax.servlet.http.HttpServletRequest;

import com.comerzzia.bricodepot.backoffice.services.usuarios.ServicioCambiarClaveImpl;
import com.comerzzia.bricodepot.backoffice.services.usuarios.x.exception.DuracionMinimaException;
import com.comerzzia.bricodepot.backoffice.services.usuarios.x.exception.HorasMinimasException;
import com.comerzzia.core.model.usuarios.UsuarioBean;
import com.comerzzia.core.servicios.empresas.EmpresaException;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.servicios.usuarios.UsuarioException;
import com.comerzzia.core.servicios.usuarios.UsuariosService;
import com.comerzzia.core.util.fechas.Fecha;
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
import com.comerzzia.web.i18n.ui.i18nUtil;

import org.springframework.beans.factory.annotation.Autowired;

public class CambiarClaveAccion extends Accion {

	private static final Vista CHANGE_PASSWORD_PAGE = new Vista("core/login/jsp/cambiarClave.jsp", Vista.INTERNA);

	@Autowired
	protected UsuariosService usuariosService;
	@Autowired
	protected ServicioCambiarClaveImpl servicioCambiarClave;
	
	@Override
	public String getNombre() {
		return "cambiarClave";
	}

	@Override
	public Vista ejecutar(HttpServletRequest request) {

		try {
			Translation t = i18nUtil.getTranslation(request, null);

			String nombreUsuario = request.getParameter("usuario");
			String uidInstancia = request.getParameter("uidInstanciaSeleccionada");
			String claveAntiguaCifrada = request.getParameter("claveAnt");
			String claveNuevaCifrada = request.getParameter("clave");
			String claveAntigua = request.getParameter("antigua");
			String claveNueva = request.getParameter("password");
			String claveNuevaConfirma = request.getParameter("confirma");

			// CARGA DATOS SESION
			DatosSesionBean datosSesion;
			try {
				datosSesion = new DatosSesionBean();
				datosSesion.setUidInstancia(uidInstancia);
			} catch (EmpresaException e) {
				setError(request, e);
				return CHANGE_PASSWORD_PAGE;
			}

			// COMPRUEBA CLAVE ANTIGUA CORRECTA
			UsuarioBean usuario = null;
			try {
				usuario = usuariosService.consultar(datosSesion, nombreUsuario.toUpperCase());
			} catch (Exception e) {
				setMensajeError(request, t._("Se ha producido un error al cosultar usuario"));
				return getControlador().getAccion("logout").ejecutar(request);
			}

			boolean claveAntiguaCorrecta = usuario.getClave().equals(claveAntiguaCifrada);
			if (!claveAntiguaCorrecta) {
				setMensajeError(request, t._("La antigua contraseña no es correcta"));
				return CHANGE_PASSWORD_PAGE;
			}

			// VALIDACIONES DE LA NUEVA CLAVE
			try {
				PasswordChangeValidator validadorClave = PasswordChangeValidator.getInstance(claveAntigua, claveNueva,
						claveNuevaConfirma);
				validadorClave.validatePasswordChange();
			} catch (Exception e) {
				setMensajeErrorValidacion(request, e, t);
				return CHANGE_PASSWORD_PAGE;
			}

			// GUARDADO DE NUEVA CLAVE
			try {
				servicioCambiarClave.cambiarClaveUsuario(datosSesion, usuario.getIdUsuario(), claveNuevaCifrada, false);

				setMensaje(request, t._("La contraseña se ha cambiado correctamente"));
			} catch (UsuarioException e) {
				if (e instanceof HorasMinimasException) {
					setMensajeError(request, t._(ErroresCambioPassword.MSG_ERROR_HORAS_MINIMAS));
				}
				if (e instanceof DuracionMinimaException) {
					setMensajeError(request, t._(ErroresCambioPassword.MSG_ERROR_VAR_MIN_HORA_RENOVACION));
				} else {
					setMensajeError(request, t._("No se ha podido cambiar la contraseña"));
				}
				return CHANGE_PASSWORD_PAGE;
				// BRICO-326
			}

			return getControlador().getAccion("logout").ejecutar(request);
		} finally {
			request.setAttribute("anyoActual", new Fecha().getAño());
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

}
