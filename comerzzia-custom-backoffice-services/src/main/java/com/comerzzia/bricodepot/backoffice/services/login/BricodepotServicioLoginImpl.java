package com.comerzzia.bricodepot.backoffice.services.login;

import java.util.List;

import com.comerzzia.bricodepot.backoffice.services.usuarios.x.ServicioUsuarioXLoginImpl;
import com.comerzzia.bricodepot.backoffice.services.usuarios.x.exception.ClaveExpiradaException;
import com.comerzzia.bricodepot.backoffice.services.usuarios.x.exception.ServicioUsuarioXException;
import com.comerzzia.bricodepot.backoffice.services.usuarios.x.exception.UsuarioBloqueadoException;
import com.comerzzia.core.model.usuarios.UsuarioBean;
import com.comerzzia.core.model.usuarios.UsuarioExample;
import com.comerzzia.core.persistencia.usuarios.UsuarioMapper;
import com.comerzzia.core.servicios.login.InvalidLoginException;
import com.comerzzia.core.servicios.login.LoginException;
import com.comerzzia.core.servicios.login.ServicioLoginImpl;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.servicios.variables.VariableException;
import com.comerzzia.core.util.base.Exception;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.core.util.i18n.Translation;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

public class BricodepotServicioLoginImpl extends ServicioLoginImpl {
	
	protected static Logger log = Logger.getLogger(BricodepotServicioLoginImpl.class);
	
	
	protected static BricodepotServicioLoginImpl instance;

	public static BricodepotServicioLoginImpl get() {
		if (instance == null) {
			instance = new BricodepotServicioLoginImpl();
		}
		return instance;
	}
	

	@SuppressWarnings("deprecation")
	@Override
	protected UsuarioBean getUsuario(Connection conn, String login, String clave, DatosSesionBean datosSesion) throws InvalidLoginException {
		log.debug("getUsuario() - Obteniendo usuario para la sesion con nombre: " + login);

		SqlSession sqlSession = datosSesion.getSqlSessionFactory().openSession(conn);
		UsuarioMapper mapper = sqlSession.getMapper(UsuarioMapper.class);
		// Obtenemos los datos del usuario
		UsuarioExample example = new UsuarioExample();
		example.or().andUidInstanciaEqualTo(datosSesion.getUidInstancia()).andUsuarioEqualTo(login);
		UsuarioBean usuario = null;
		List<UsuarioBean> usuarios = mapper.selectByExample(example);
		if (!usuarios.isEmpty()) {
			usuario = usuarios.get(0);
		}

		try {
			// Validamos al usuario
			if (usuario != null) {
				// Comprobamos si está activo
				if (!usuario.isActivo()) {
					throw new InvalidLoginException(datosSesion.getTranslation()._("No tiene permiso para acceder a la aplicación"), "login.invalido", new UsuarioInactivoException());
				}
				// Comprobamos la clave
				if (!usuario.isClaveValida(clave)) {
					ServicioUsuarioXLoginImpl.get().sumaIntentoLoginFallido(sqlSession, datosSesion, usuario.getIdUsuario());
					throw new InvalidLoginException(datosSesion.getTranslation()._("Usuario/clave no válidos"), "login.sinPermiso", new ClaveIncorrectaException());
				}

				ServicioUsuarioXLoginImpl.get().login(datosSesion, sqlSession, usuario);
			}
			else {
				throw new InvalidLoginException(datosSesion.getTranslation()._("Usuario/clave no válidos"), "login.sinPermiso", new UsuarioNoExisteException());
			}
		}
		catch (ClaveExpiradaException e) {
			throw new InvalidLoginException(datosSesion.getTranslation()._("Su clave ha expirado"), "login.claveExpirada", e);
		}
		catch (VariableException e) {
			throw new InvalidLoginException(datosSesion.getTranslation()._("Variable no configurada consulte administrador"), e);
		}
		catch (UsuarioBloqueadoException e) {
			throw new InvalidLoginException(datosSesion.getTranslation()._("Usuario Bloqueado"), e);
		}
		finally {
			sqlSession.close();
		}

		return usuario;
	}
	
	public UsuarioBean logPOS(String login, String clave, String instancia, String actividad, String codigoEmpresa, Translation t) throws ClaveExpiradaException, ServicioUsuarioXException, InvalidLoginException, LoginException, VariableException, UsuarioBloqueadoException   { // BRICO-326
		log.debug("logPOS() Iniciando sesión del usuario: " + login + " desde POS");

		try {
			DatosSesionBean datosSesion = obtenerDatosSesion(login, clave, instancia, actividad, codigoEmpresa, t);
			UsuarioBean usuarioValido = datosSesion.getUsuario();
			return usuarioValido;
		}
		catch (InvalidLoginException | LoginException e) {
			controlaErroresPersonalizados(e);
			throw e; // Errores standard
		}
	}

	public void controlaErroresPersonalizados(Exception e) throws ClaveExpiradaException, VariableException, UsuarioBloqueadoException {
		if (e.getCause() instanceof ClaveExpiradaException) {
			throw (ClaveExpiradaException) e.getCause();
		}
		else if (e.getCause() instanceof VariableException) {
			throw (VariableException) e.getCause();
		}
		else if (e.getCause() instanceof UsuarioBloqueadoException) {
			throw (UsuarioBloqueadoException) e.getCause();
		}
	}

}
