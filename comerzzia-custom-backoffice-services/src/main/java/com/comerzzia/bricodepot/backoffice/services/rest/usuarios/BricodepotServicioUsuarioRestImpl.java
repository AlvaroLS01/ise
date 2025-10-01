package com.comerzzia.bricodepot.backoffice.services.rest.usuarios;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comerzzia.bricodepot.backoffice.services.usuarios.ServicioCambiarClaveImpl;
import com.comerzzia.bricodepot.backoffice.services.usuarios.x.exception.DuracionMinimaException;
import com.comerzzia.bricodepot.backoffice.services.usuarios.x.exception.HorasMinimasException;
import com.comerzzia.core.model.empresas.ConfigEmpresaBean;
import com.comerzzia.core.model.usuarios.UsuarioBean;
import com.comerzzia.core.servicios.ContextHolder;
import com.comerzzia.core.servicios.empresas.EmpresaException;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.servicios.usuarios.ServicioUsuariosImpl;
import com.comerzzia.core.servicios.usuarios.UsuarioException;
import com.comerzzia.core.servicios.usuarios.UsuarioNotFoundException;
import com.comerzzia.servicios.rest.response.ResponseMensajesRest;
import com.comerzzia.servicios.rest.response.ResponsePatchRest;
import com.comerzzia.servicios.rest.usuarios.ServicioUsuarioRestImpl;
import com.comerzzia.validacion.passwords.constants.ErroresCambioPassword;

@Service
public class BricodepotServicioUsuarioRestImpl extends ServicioUsuarioRestImpl implements BricodepotServicioUsuarioRestService {

	protected static Logger log = Logger.getLogger(BricodepotServicioUsuarioRestImpl.class);

	protected static BricodepotServicioUsuarioRestImpl instance;
	
	@Autowired
	protected ServicioCambiarClaveImpl servicioCambiarClaveImpl;

	public static BricodepotServicioUsuarioRestImpl get() {
		if (instance != null) {
			return instance;
		} else {
			return (BricodepotServicioUsuarioRestImpl) ContextHolder.get().getBean(BricodepotServicioUsuarioRestImpl.class);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public ResponsePatchRest cambiarClave(String nombreUsuario, String clave, String claveNueva, ConfigEmpresaBean configEmpresa) {
		log.debug("cambiarClave() - Cambiando clave del usuario: " + nombreUsuario + " en POS");

		ResponsePatchRest response = new ResponsePatchRest();

		try {
			DatosSesionBean datosSesion = new DatosSesionBean();
			datosSesion.setUidActividad(configEmpresa.getUidActividad());
			datosSesion.setUidInstancia(configEmpresa.getUidInstancia());

			UsuarioBean usuarioConsultado = ServicioUsuariosImpl.get().consultar(datosSesion, nombreUsuario);

			if (!usuarioConsultado.isClaveValida(clave)) {
				response.setMensaje(ResponseMensajesRest.CLAVE_NO_VALIDA);
				response.setCodError(ResponseMensajesRest.COD_ERROR_VALIDACION_DATOSBD);
				return response;
			}

			servicioCambiarClaveImpl.cambiarClaveUsuario(datosSesion, usuarioConsultado.getIdUsuario(), claveNueva, false);

			response.setMensaje(ResponseMensajesRest.CLAVE_CAMBIADA);
			response.setNumeroUpdates(response.getNumeroUpdates() + 1);
			response.setCodError(ResponseMensajesRest.COD_EXITO);
		}
		catch (EmpresaException e) {
			log.error("cambiarClave() - Error iniciando sesión con uidActividad: " + configEmpresa.getUidActividad() + ", uidInstancia: " + configEmpresa.getUidInstancia());
			throw new RuntimeException(e);
		}
		catch (HorasMinimasException e) {
			log.error("cambiarClave() - Horas mínimas de clave no cumplicads. No se puede actualizar clave.");
			response.setMensaje("Horas mínimas de clave no cumplicads. No se puede actualizar clave.");
			response.setCodError(ErroresCambioPassword.COD_ERROR_HORAS_MINIMAS);
		}
		catch (DuracionMinimaException e) {
			log.error("cambiarClave() - " + ErroresCambioPassword.MSG_ERROR_VAR_MIN_HORA_RENOVACION);
			response.setMensaje(ErroresCambioPassword.MSG_ERROR_VAR_MIN_HORA_RENOVACION);
			response.setCodError(ErroresCambioPassword.COD_ERROR_VAR_MIN_HORA_RENOVACION);
		}
		catch (UsuarioException e) {
			log.error("cambiarClave() - Clave no válida");
			response.setMensaje(ResponseMensajesRest.CLAVE_NO_VALIDA);
			response.setCodError(ResponseMensajesRest.COD_ERROR_PATCH);
		}
		catch (UsuarioNotFoundException e) {
			log.error("cambiarClave() - Usuario no válido");
			response.setMensaje(ResponseMensajesRest.USUARIO_NO_VALIDO);
			response.setCodError(ResponseMensajesRest.COD_ERROR_PATCH);
		}

		return response;

	}

}
