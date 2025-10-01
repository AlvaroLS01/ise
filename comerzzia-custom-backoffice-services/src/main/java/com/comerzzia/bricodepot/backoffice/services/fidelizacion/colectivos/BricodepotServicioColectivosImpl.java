package com.comerzzia.bricodepot.backoffice.services.fidelizacion.colectivos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comerzzia.bricodepot.backoffice.persistence.fidelizacion.colectivos.CustomColectivo;
import com.comerzzia.bricodepot.backoffice.persistence.fidelizacion.colectivos.CustomColectivoKey;
import com.comerzzia.bricodepot.backoffice.persistence.fidelizacion.colectivos.CustomColectivoMapper;
import com.comerzzia.bricodepot.backoffice.services.fidelizacion.fidelizados.versioning.CustomFidelizadoVersionControlServiceImpl;
import com.comerzzia.core.servicios.i18n.ServicioI18NImpl;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.servicios.sesion.IDatosSesion;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.core.util.mybatis.exception.PersistenceExceptionFactory;
import com.comerzzia.core.util.mybatis.session.SqlSession;
import com.comerzzia.model.fidelizacion.colectivos.ColectivoBean;
import com.comerzzia.model.fidelizacion.colectivos.ColectivoKey;
import com.comerzzia.model.fidelizacion.fidelizados.FidelizadoBean;
import com.comerzzia.persistencia.fidelizacion.colectivos.ColectivoMapper;
import com.comerzzia.servicios.fidelizacion.colectivos.ColectivoNotFoundException;
import com.comerzzia.servicios.fidelizacion.colectivos.ServicioColectivosImpl;
import com.comerzzia.servicios.fidelizacion.fidelizados.colectivos.ColectivoConstraintViolationException;
import com.comerzzia.servicios.fidelizacion.fidelizados.versioning.FidelizadoVersion;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@SuppressWarnings("deprecation")
@Service
@Primary
public class BricodepotServicioColectivosImpl extends ServicioColectivosImpl {
	
	protected static Logger log = Logger.getLogger(BricodepotServicioColectivosImpl.class);

	protected static BricodepotServicioColectivosImpl instance;
	
	
	@Override
	public ColectivoBean consultar(String codColectivo, DatosSesionBean datosSesion) throws ColectivoNotFoundException {
		log.debug("consultar() - Consultando x_colectivo codColectivo:["+codColectivo+"]");
		
		ColectivoBean colectivo = super.consultar(codColectivo, datosSesion);
		SqlSession sqlSession = new SqlSession();

		try {
			sqlSession.openSession(datosSesion.getSqlSessionFactory().openSession());
			
			CustomColectivoMapper customMapper = sqlSession.getMapper(CustomColectivoMapper.class);
			CustomColectivoKey customKey = new CustomColectivoKey();
			customKey.setUidInstancia(colectivo.getUidInstancia());
			customKey.setCodColectivo(colectivo.getCodColectivo());
			CustomColectivo customColectivo = customMapper.selectByPrimaryKey(customKey);
			
			colectivo.putExtension("visualizacionAltas", customColectivo.getVisualizacionAltas());
			colectivo.putExtension("profesional", customColectivo.getProfesional());
			
			return colectivo;
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public void crear(ColectivoBean colectivo, DatosSesionBean datosSesion) {
		SqlSession sqlSession = new SqlSession();

		try {
			log.debug("crear() - Creando nuevo colectivo");
			sqlSession.openSession(datosSesion.getSqlSessionFactory().openSession());

			ColectivoMapper mapper = sqlSession.getMapper(ColectivoMapper.class);
			colectivo.setUidInstancia(datosSesion.getUidInstancia());
			mapper.insert(colectivo);
			salvarFidelizadosColectivo(sqlSession, colectivo, datosSesion);
			salvarTraducciones(sqlSession, colectivo, datosSesion);
			
			/* BRICO-146 modificación campos alta fidelizado */
			CustomColectivoMapper customMapper = sqlSession.getMapper(CustomColectivoMapper.class);
			CustomColectivo customColectivo = new CustomColectivo();
			customColectivo.setUidInstancia(datosSesion.getUidInstancia());
			customColectivo.setCodColectivo(colectivo.getCodColectivo());
			customColectivo.setVisualizacionAltas((Boolean)colectivo.getExtension("visualizacionAltas"));
			customColectivo.setProfesional((Boolean)colectivo.getExtension("profesional"));
			
			customMapper.insert(customColectivo);
			/* fin BRICO-146 modificación campos alta fidelizado */
			
			sqlSession.commit();
		} 
		catch (PersistenceException e) {
			sqlSession.rollback();
			if(PersistenceExceptionFactory.getPersistenceExpception(e).isKeyConstraintViolationException()) {
				String msg = "No se ha podido crear el colectivo. El código introducido ya está registrado para otro colectivo: " + e.getMessage();
				log.error("crear() - " + msg);
				throw new ColectivoConstraintViolationException(msg, e);
			}
			
		}
		finally{
			sqlSession.close();
		}
	}
	
	@Override
	public void modificar(ColectivoBean colectivo, DatosSesionBean datosSesion) {
		SqlSession sqlSession = new SqlSession();
		
		try {
			log.debug("modificar() - Modificando colectivo con Código " + colectivo.getCodColectivo());
			sqlSession.openSession(datosSesion.getSqlSessionFactory().openSession(true));

			ColectivoMapper mapper = sqlSession.getMapper(ColectivoMapper.class);
			mapper.updateByPrimaryKey(colectivo);
			salvarTraducciones(sqlSession, colectivo, datosSesion);
			
			/* BRICO-146 modificación campos alta fidelizado */
			CustomColectivoMapper customMapper = sqlSession.getMapper(CustomColectivoMapper.class);
			CustomColectivo customColectivo = new CustomColectivo();
			customColectivo.setUidInstancia(datosSesion.getUidInstancia());
			customColectivo.setCodColectivo(colectivo.getCodColectivo());
			customColectivo.setVisualizacionAltas((Boolean)colectivo.getExtension("visualizacionAltas"));
			customColectivo.setProfesional((Boolean)colectivo.getExtension("profesional"));
			
			if(customMapper.selectByPrimaryKey(customColectivo)==null) {
				//crear
				customMapper.insert(customColectivo);
			}
			else {
				//modificar
				customMapper.updateByPrimaryKey(customColectivo);
			}
			/* fin BRICO-146 modificación campos alta fidelizado */
		} 
		finally {
			sqlSession.close();
		}
	}
	
	@Override
	public void eliminar(String codColectivo, DatosSesionBean datosSesion) {
		org.apache.ibatis.session.SqlSession sqlSession =  datosSesion.getSqlSessionFactory().openSession();
		Connection conn = new Connection(sqlSession.getConnection());

		try {
			log.debug("eliminar() - Eliminando colectivo con Código " + codColectivo);
			
			
			ColectivoMapper mapper = sqlSession.getMapper(ColectivoMapper.class);
			
			ColectivoKey key = new ColectivoKey(datosSesion.getUidInstancia(), codColectivo);
			ColectivoBean colectivo = mapper.selectFromViewByPrimaryKey(key);
			
			/* BRICO-146 modificación campos alta fidelizado */
			CustomColectivoMapper customMapper = sqlSession.getMapper(CustomColectivoMapper.class);
			CustomColectivoKey customColectivoKey = new CustomColectivoKey();
			customColectivoKey.setUidInstancia(colectivo.getUidInstancia());
			customColectivoKey.setCodColectivo(codColectivo);
			
			customMapper.deleteByPrimaryKey(customColectivoKey);
			/* fin BRICO-146 modificación campos alta fidelizado */
			
			colectivo.setFidelizados(com.comerzzia.servicios.fidelizacion.fidelizados.colectivos.ServicioColectivosFidelizadosImpl.get().consultarPorColectivo(sqlSession, codColectivo, datosSesion));
			eliminarFidelizadosColectivo(sqlSession, colectivo, datosSesion);
			mapper.deleteByPrimaryKey(key);
			ServicioI18NImpl.get().eliminarPorObjeto(conn, datosSesion, ColectivoBean.CLASE_COLECTIVO, codColectivo);
			sqlSession.commit();
		} 
		catch(PersistenceException e) {
			sqlSession.rollback();
			String msg = "No se ha podido eliminar el colectivo: " + codColectivo ;
			log.error("eliminar() -"+ msg+" : " + e.getMessage());
			if(PersistenceExceptionFactory.getPersistenceExpception(e).isForeingKeyConstraintViolationException()) {
				throw new ColectivoConstraintViolationException(msg, e);
			}
			
		}
		
		 finally {
			sqlSession.close();
		}
	}
	
	@Override
	public List<ColectivoBean> consultarTodosPublicos(DatosSesionBean datosSesion){
		org.apache.ibatis.session.SqlSession sqlSession = datosSesion.getSqlSessionFactory().openSession();
		
		try{
			log.debug("consultarTodosPublicos() - Consultando coletivos filtrando por \"visualizacion_altas\"");
			
			CustomColectivoMapper mapper = sqlSession.getMapper(CustomColectivoMapper.class);
			Map<String, String> map = new HashMap<String, String>();
			map.put("uidInstancia", datosSesion.getUidInstancia());
			return mapper.consultarTodosPublicosVisualizacionAltas(map);
		}finally{
			sqlSession.close();
		}
		
		
	}

	@Override
	protected void versionarFidelizado(Connection conn, IDatosSesion datosSesion, FidelizadoBean fidelizado) {
		log.debug("versionarFidelizado() - Inicio de versionado del fidelizado " + fidelizado.getIdFidelizado());
		FidelizadoVersion fidelizadoVersion = new FidelizadoVersion(conn, fidelizado.getIdFidelizado());
		CustomFidelizadoVersionControlServiceImpl.get().checkFidelizadosVersion(datosSesion, fidelizadoVersion);
	}
	
	
}
