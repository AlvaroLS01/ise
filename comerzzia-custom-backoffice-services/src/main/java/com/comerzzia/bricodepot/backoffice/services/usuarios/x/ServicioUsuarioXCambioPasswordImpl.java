package com.comerzzia.bricodepot.backoffice.services.usuarios.x;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comerzzia.bricodepot.backoffice.persistence.usuarios.x.UsuarioX;
import com.comerzzia.bricodepot.backoffice.persistence.usuarios.x.UsuarioXKey;
import com.comerzzia.bricodepot.backoffice.persistence.usuarios.x.UsuarioXMapper;
import com.comerzzia.bricodepot.backoffice.services.usuarios.x.exception.HorasMinimasException;
import com.comerzzia.core.servicios.sesion.IDatosSesion;
import com.comerzzia.core.servicios.variables.ServicioVariablesImpl;
import com.comerzzia.core.servicios.variables.VariableException;
import com.comerzzia.core.servicios.variables.VariableNotFoundException;
import com.comerzzia.core.util.fechas.Fecha;
import com.comerzzia.validacion.passwords.constants.ErroresCambioPassword;

@Service
public class ServicioUsuarioXCambioPasswordImpl { // BRICO-326

	protected static Logger log = Logger.getLogger(ServicioUsuarioXCambioPasswordImpl.class);

	public static final String VARIABLE_X_PASSWORD_MIN_HORAS_RENOVACION = "X_PASSWORD.MIN_HORAS_RENOVACION";
	public static final int HORAS_DURACION_MINIMA_CLAVE_DEFAULT = 24;

	@Autowired
	protected UsuarioXMapper usuarioXMapper;
	
	protected static ServicioUsuarioXCambioPasswordImpl instance;

	public static ServicioUsuarioXCambioPasswordImpl get() {
		if (instance != null) {
			return instance;
		}
		else {
			return new ServicioUsuarioXCambioPasswordImpl();
		}
	}	

	public void actualizaCambiosClave(IDatosSesion datosSesion, Long idUsuario, UsuarioX usuarioX) throws HorasMinimasException {
		log.debug("actualizaCambiosClave() - Actualizando cambios de clave en tabla X_USUARIOS_TBL");
		
		usuarioXMapper.updateByPrimaryKey(usuarioX);
	}

	public UsuarioX buscaPorId(IDatosSesion datosSesion, Long idUsuario) {
		log.debug("buscaPorId() - Consultando usuarioX por idUsuario: " + idUsuario);

		UsuarioXKey key = new UsuarioXKey();
		key.setIdUsuario(idUsuario);
		key.setUidInstancia(datosSesion.getUidInstancia());
		UsuarioX usuarioX = usuarioXMapper.selectByPrimaryKey(key);
		
		return usuarioX;
	}

	public UsuarioX generaUsuario(IDatosSesion datosSesion, Long idUsuario) {
		log.debug("generaUsuario()");
	
		UsuarioX newUsuarioX = creaNuevoUsuarioX(datosSesion.getUidInstancia(), idUsuario);
		usuarioXMapper.insert(newUsuarioX);

		return newUsuarioX;
	}

	public void validaDuracionMinimaClave(IDatosSesion datosSesion, UsuarioX usuarioX) throws HorasMinimasException {
		log.debug("validaDuracionMinimaClave() - Validando restricción mínimo número de horas antes de cambiar clave");

		Fecha restriccionMinHoras = new Fecha(usuarioX.getUltimoCambio());
		int horasMinimasClave = obtenDuracionMinimaClave(datosSesion);
		restriccionMinHoras.sumaHoras(horasMinimasClave);

		Fecha fechaUltimoCambio = new Fecha(usuarioX.getUltimoCambio());

		Timestamp restringidoHasta = restriccionMinHoras.getTimestamp();
		Timestamp hoy = new Fecha(new Date()).getTimestamp();
		boolean cumpleRestriccionMinHoras = hoy.after(restringidoHasta);
		log.debug("validaDuracionMinimaClave() Último cambio clave: " + fechaUltimoCambio.getTimestamp() + "; Restringido cambio hasta: " + restringidoHasta + "; fecha Actual: " + hoy);

		if (!cumpleRestriccionMinHoras) {
			log.error("validaDuracionMinimaClave() Restringido cambio hasta: " + restringidoHasta);
			throw new HorasMinimasException(ErroresCambioPassword.MSG_ERROR_HORAS_MINIMAS);
		}
	}

	public UsuarioX creaNuevoUsuarioX(String uidInstancia, Long idUsuario) {
		log.debug("createNewUsuarioX() - Creando usuarioX uidInstancia: " + uidInstancia + " idUsuari: " + idUsuario);

		UsuarioX usuarioX = new UsuarioX();
		usuarioX.setUidInstancia(uidInstancia);
		usuarioX.setIdUsuario(idUsuario);
		usuarioX.setBloqueado("N");
		usuarioX.setCambiosClave(0L);
		usuarioX.setIntentosLoginFallidos(0L);
		usuarioX.setUltimoCambio(new Date());

		return usuarioX;
	}

	@SuppressWarnings("deprecation")
	public int obtenDuracionMinimaClave(IDatosSesion datosSesion) {
		log.debug("obtenDuracionMinimaClave() - Obteniendo horas de duración mínima de la clave:");

		int horasMinimasClave = HORAS_DURACION_MINIMA_CLAVE_DEFAULT;
		try {
			String valor = ServicioVariablesImpl.get().consultarValor(datosSesion, VARIABLE_X_PASSWORD_MIN_HORAS_RENOVACION);
			horasMinimasClave = Integer.valueOf(valor);
		}
		catch (NumberFormatException | VariableException | VariableNotFoundException e) {
			log.error("obtenDuracionMinimaClave() - Error obteniendo la duración mínima de la clave en BD: " + e.getMessage());
		}

		log.debug("obtenDuracionMinimaClave() - Horas duración mínima de la clave: " + horasMinimasClave);
		return horasMinimasClave;
	}
}
