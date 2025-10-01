package com.comerzzia.bricodepot.backoffice.services.usuarios;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.comerzzia.bricodepot.backoffice.persistence.usuarios.x.UsuarioX;
import com.comerzzia.bricodepot.backoffice.services.usuarios.x.ServicioUsuarioXCambioPasswordImpl;
import com.comerzzia.bricodepot.backoffice.services.usuarios.x.ServicioUsuarioXLoginImpl;
import com.comerzzia.core.servicios.sesion.IDatosSesion;
import com.comerzzia.core.servicios.usuarios.UsuarioException;
import com.comerzzia.core.servicios.usuarios.UsuariosService;
import com.comerzzia.core.servicios.variables.ServicioVariablesImpl;
import com.comerzzia.core.servicios.variables.VariableException;
import com.comerzzia.core.servicios.variables.VariableNotFoundException;

@Service
public class ServicioCambiarClaveImpl {

	protected static Logger log = Logger.getLogger(ServicioCambiarClaveImpl.class);

	@Autowired
	protected UsuariosService servicioUsuariosImpl;
	
	@Autowired
	protected ServicioUsuarioXCambioPasswordImpl servicioUsuarioXCambioPasswordImpl;

	protected static ServicioCambiarClaveImpl instance;

	public static ServicioCambiarClaveImpl get() {
		if (instance != null) {
			return instance;
		}
		else {
			return new ServicioCambiarClaveImpl();
		}
	}

	@SuppressWarnings("deprecation")
	@Transactional(rollbackFor = Exception.class)
	public void cambiarClaveUsuario(IDatosSesion datosSesion, Long idUsuario, String clave, boolean esRestablecerContrasena) throws UsuarioException {
		log.debug("cambiarClaveUsuario() - Cambiando clave del usuario " + idUsuario);
		
		servicioUsuariosImpl.cambiarClaveUsuario(datosSesion, idUsuario, clave);
		
		UsuarioX usuarioX = servicioUsuarioXCambioPasswordImpl.buscaPorId(datosSesion, idUsuario);

		if (usuarioX == null) {
			usuarioX = servicioUsuarioXCambioPasswordImpl.generaUsuario(datosSesion, idUsuario);
		}
		else {
			if (esRestablecerContrasena) {
				usuarioX.setBloqueado("N");
				usuarioX.setIntentosLoginFallidos(0L);

				/* Restaremos los días de expiración de clave para que al realizar el login pida cambiar la clave */
				Integer diasExpiracionClave = 90;
				try {
					diasExpiracionClave = Integer.parseInt(ServicioVariablesImpl.get().consultarValor(datosSesion, ServicioUsuarioXLoginImpl.VARIABLE_X_PASSWORD_DIAS_RENOVACION)) + 1;
				}
				catch (NumberFormatException | VariableException | VariableNotFoundException e) {
					log.error("cambiarClaveUsuario() - No existe variable, se usará valor por defecto 90 dias");
				}
				
				Calendar c = Calendar.getInstance();
				c.add(Calendar.DATE, -diasExpiracionClave);
				Date fecha = c.getTime();

				usuarioX.setUltimoCambio(fecha);
			}
			else {
				servicioUsuarioXCambioPasswordImpl.validaDuracionMinimaClave(datosSesion, usuarioX);
				usuarioX.setUltimoCambio(new Date());
			}

			usuarioX.setCambiosClave(usuarioX.getCambiosClave() + 1);
		}

		servicioUsuarioXCambioPasswordImpl.actualizaCambiosClave(datosSesion, idUsuario, usuarioX);
	}

}
