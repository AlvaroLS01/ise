package com.comerzzia.bricodepot.backoffice.services.usuarios.x;

import java.util.Date;

import com.comerzzia.bricodepot.backoffice.persistence.usuarios.x.UsuarioX;
import com.comerzzia.bricodepot.backoffice.persistence.usuarios.x.UsuarioXKey;
import com.comerzzia.bricodepot.backoffice.persistence.usuarios.x.UsuarioXMapper;
import com.comerzzia.bricodepot.backoffice.services.usuarios.x.exception.ClaveExpiradaException;
import com.comerzzia.bricodepot.backoffice.services.usuarios.x.exception.UsuarioBloqueadoException;
import com.comerzzia.core.model.usuarios.UsuarioBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.servicios.variables.ServicioVariablesImpl;
import com.comerzzia.core.servicios.variables.VariableException;
import com.comerzzia.core.util.fechas.Fecha;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

public class ServicioUsuarioXLoginImpl {

	private static final long MAX_INTENTOS_LOGIN_FALLIDOS_DEFAULT = 5;

	protected static Logger log = Logger.getLogger(ServicioUsuarioXLoginImpl.class);

	public static final String VARIABLE_X_PASSWORD_DIAS_RENOVACION = "X_PASSWORD.DIAS_RENOVACION";
	public static final String VARIABLE_X_PASSWORD_MAX_INTENTOS_LOGIN = "X_PASSWORD.MAX_INTENTOS_LOGIN";
	

	protected static ServicioUsuarioXLoginImpl instance;

	public static ServicioUsuarioXLoginImpl get() {
		if (instance == null) {
			instance = new ServicioUsuarioXLoginImpl();
		}
		return instance;
	}

	public void login(DatosSesionBean datosSesion, SqlSession sqlSession, UsuarioBean usuario) throws  ClaveExpiradaException, VariableException, UsuarioBloqueadoException {
		log.debug("login() - Iniciando lógica de login personalizada para el usuario: " + usuario.getUsuario() + " id: " + usuario.getIdUsuario());
		
		UsuarioX usuarioX = buscaPorId(sqlSession, datosSesion, usuario.getIdUsuario());
		if(usuarioX.getBloqueado().equalsIgnoreCase("S")) {
			throw new UsuarioBloqueadoException();
		}
		validaExpiracionClave(usuarioX, datosSesion);
		resetIntentosLoginFallidos(sqlSession, datosSesion, usuarioX);
		log.debug("login() - Iniciando lógica de login personalizada para el usuario: " + usuario.getUsuario() + " id: " + usuario.getIdUsuario());
		
	}

	public UsuarioX buscaPorId(SqlSession sqlSession, DatosSesionBean datosSesion, Long idUsuario) {
		log.debug("buscaPorId() - Consultando usuarioX por idUsuario: " + idUsuario);

		UsuarioXKey key = new UsuarioXKey();
		key.setIdUsuario(idUsuario);
		key.setUidInstancia(datosSesion.getUidInstancia());
		UsuarioXMapper usuarioXMapper = sqlSession.getMapper(UsuarioXMapper.class);
		UsuarioX usuarioX = usuarioXMapper.selectByPrimaryKey(key);
		if (usuarioX == null) {
			log.debug("buscaPorId() - No existe");
			UsuarioX newUsuarioX = creaNuevoUsuarioX(datosSesion.getUidInstancia(), idUsuario);
			usuarioXMapper.insert(newUsuarioX);
			usuarioX = usuarioXMapper.selectByPrimaryKey(key);
		}
		return usuarioX;
	}

	public void validaExpiracionClave(UsuarioX usuarioX, DatosSesionBean datosSesion) throws ClaveExpiradaException, VariableException {
		log.debug("validaExpiracionClave() - Validando expiración clave para usuario con id: " + usuarioX.getIdUsuario());

		try {
			Integer diasExpiracionClave = Integer.parseInt(ServicioVariablesImpl.get().consultarValor(datosSesion, VARIABLE_X_PASSWORD_DIAS_RENOVACION));
			Fecha expiracionClave = new Fecha(usuarioX.getUltimoCambio());
			expiracionClave.sumaDias(diasExpiracionClave);
			if (expiracionClave.antes(new Fecha())) {
				throw new ClaveExpiradaException("Expiracion clave: " + expiracionClave.getTimestamp().toString());
			}
		}
		catch (ClaveExpiradaException e) {
			log.error("validaExpiracionClave() - Clave expirada - " + e.getMessage());
			throw e;
		}
		catch (VariableException e) {
			log.error("validaExpiracionClave() - Variable no encontrada - " + e.getMessage());
			throw e;
		}
		catch (Exception e) {
			log.error("validaExpiracionClave() - Error inesperado validando expiración clave - :" + e.getMessage(), e);
			throw new ClaveExpiradaException(e.getCause());
		}
	}
	
	public void resetIntentosLoginFallidos(SqlSession sqlSession, DatosSesionBean datosSesion, UsuarioX usuarioX) {
		log.debug("resetIntentosLoginFallidos() - Reseteando numero de intentos de login erróneos");
		
		UsuarioXMapper usuarioXMapper = sqlSession.getMapper(UsuarioXMapper.class);
		usuarioX.setIntentosLoginFallidos(0L);
		usuarioXMapper.updateByPrimaryKey(usuarioX);
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

	public UsuarioDTO creaUsuarioDTO(UsuarioBean usuarioValido) {
		log.debug("creaUsuarioDTO() - Creando objeto UsusarioDTO");

		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setActivo(usuarioValido.getActivoString());
		usuarioDTO.setClave(usuarioValido.getClave());
		usuarioDTO.setDesUsuario(usuarioValido.getDesUsuario());
		usuarioDTO.setIdUsuario(usuarioValido.getIdUsuario());
		usuarioDTO.setPuedeCambiarMenu(usuarioValido.getPuedeCambiarMenu());
		usuarioDTO.setUidInstancia(usuarioValido.getUidInstancia());
		usuarioDTO.setUidMenuPorDefecto(usuarioValido.getUidMenuPorDefecto());
		usuarioDTO.setUsuario(usuarioValido.getUsuario());

		return usuarioDTO;
	}

	public void sumaIntentoLoginFallido(SqlSession sqlSession, DatosSesionBean datosSesion, Long idUsuario) {
		log.debug("sumaIntentoLoginFallido() - Sumando intento de login fallido al usuario: " + idUsuario);
		UsuarioXMapper usuarioXMapper = sqlSession.getMapper(UsuarioXMapper.class);

		UsuarioX usuarioX = buscaPorId(sqlSession, datosSesion, idUsuario);
		Long intentosLoginFallidos = usuarioX.getIntentosLoginFallidos();
		if (!usuarioX.getBloqueado().equalsIgnoreCase("S")) {
			log.debug("sumaIntentoLoginFallido() - Usuario: " + usuarioX.getIdUsuario() + "NO BLOQUEADO");
		
			// Si llega al máximo de intentos bloquea al usuario
			Long maxIntentosLogin;
			try {
				maxIntentosLogin = Long.parseLong(ServicioVariablesImpl.get().consultarValor(datosSesion, VARIABLE_X_PASSWORD_MAX_INTENTOS_LOGIN));
			}
			catch (Exception e) {
				maxIntentosLogin = MAX_INTENTOS_LOGIN_FALLIDOS_DEFAULT;
			}
			
			if (intentosLoginFallidos == maxIntentosLogin) {
				log.debug("sumaIntentoLoginFallido() - Máximo número de intentos alcanzado: " + maxIntentosLogin + " bloqueando usuario");
				usuarioX.setBloqueado("S");
				usuarioX.setIntentosLoginFallidos(0L);
				log.debug("sumaIntentoLoginFallido() - Usuario: " + usuarioX.getIdUsuario() + " BLOQUEADO");
			}
			else {
				intentosLoginFallidos++;
				usuarioX.setIntentosLoginFallidos(intentosLoginFallidos);
			}
			usuarioXMapper.updateByPrimaryKey(usuarioX);
		}
		else {
			log.debug("sumaIntentoLoginFallido() - Usuario: " + usuarioX.getIdUsuario() + " BLOQUEADO");
		}
	}
}
