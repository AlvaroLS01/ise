package com.comerzzia.bricodepot.backoffice.admin.core.login.accciones;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.comerzzia.core.servicios.login.InvalidLoginException;
import com.comerzzia.core.servicios.login.LoginException;
import com.comerzzia.core.servicios.login.ServicioLoginImpl;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.fechas.Fecha;
import com.comerzzia.core.util.i18n.Translation;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;
import com.comerzzia.web.core.login.acciones.CargarEmpresasLoginAccion;
import com.comerzzia.web.core.login.acciones.LoginAccion;
import com.comerzzia.web.i18n.ui.i18nUtil;

public class BricodepotLoginAction extends LoginAccion {

	private static final Vista NEXT_PAGE = new Vista("inicio.screen", Vista.EXTERNA);
	private static final Vista CONFIG_PAGE = new Vista("asistenteConfig?inicio=1", Vista.EXTERNA);

	/* BRICO-128 */
	private static final Vista CHANGE_PASSWORD_PAGE = new Vista("admin/core/login/jsp/cambiarClave.jsp", Vista.INTERNA);

	@Override
	public Vista ejecutar(HttpServletRequest request) {

		Translation t = i18nUtil.getTranslation(request, null);

		try {
			// get the old session and invalidate
			HttpSession oldSession = request.getSession(false);
			if (oldSession != null) {
				oldSession.invalidate();
			}
			// generate a new session
			HttpSession sesion = request.getSession(true);

			String usuario = request.getParameter("usuario");
			String clave = request.getParameter("clave");
			String uidInstancia = request.getParameter("uidInstanciaSeleccionada");
			String uidActividad = request.getParameter("uidActividadSeleccionada");
			String codEmpresa = request.getParameter("codEmpresaSeleccionada");

			// Iniciamos la sesión
			DatosSesionBean datosSesion = (DatosSesionBean) ServicioLoginImpl.get().iniciarSesion(usuario.toUpperCase(), clave, uidInstancia, uidActividad, codEmpresa, null);

			// Guardamos los datos de sesión en la sesión de la aplicación
			sesion.setAttribute(WebKeys.DATOS_SESION, datosSesion);

			Cookie obtenerCookieIdioma = i18nUtil.obtenerCookieIdioma(request);
			if (obtenerCookieIdioma == null) {
				i18nUtil.seleccionarIdioma(request, null);
			}
			else {
				if (datosSesion != null) {
					// Creamos el translation con el nuevo locale
					datosSesion.setTranslation(new Translation(i18nUtil.getLocale(obtenerCookieIdioma.getValue())));
				}
			}

			// Si la empresa no está configurada redirigimos al asistente de
			// configuración de empresa
			if (datosSesion.getDatosEmpresa().getDesEmp() == null || datosSesion.getDatosEmpresa().getDesEmp().isEmpty()) {
				return CONFIG_PAGE;
			}

			return NEXT_PAGE;
		}
		catch (InvalidLoginException e) {
			setMensajeError(request, e.getMessage(), e);

			/* BRICO-128 Fecha de expiración contraseñas */
			if ("login.claveExpirada".equals(e.getMessageKey())) {
				request.setAttribute("anyoActual", new Fecha().getAño());
				request.setAttribute("usuario", request.getParameter("usuario"));
				request.setAttribute("uidInstanciaSeleccionada", request.getParameter("uidInstanciaSeleccionada"));
				return CHANGE_PASSWORD_PAGE;
			}
			/* fin BRICO-128 */

			return new CargarEmpresasLoginAccion().ejecutar(request);
		}
		catch (LoginException e) {
			setMensajeError(request, t._("En estos momentos no es posible acceder al sistema"), e);

			return new CargarEmpresasLoginAccion().ejecutar(request);
		}
		catch (Exception e) {
			setMensajeError(request, t._("En estos momentos no es posible acceder al sistema"), e);

			return new CargarEmpresasLoginAccion().ejecutar(request);
		}
	}

}
