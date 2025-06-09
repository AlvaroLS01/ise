package com.comerzzia.instoreengine.herbnav.servicios.trabajos.articulosbalanza;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.core.util.db.Database;
import com.comerzzia.instoreengine.herbnav.persistencia.trabajos.articulosbalanza.ArticuloBalanza;
import com.comerzzia.instoreengine.herbnav.persistencia.trabajos.articulosbalanza.ArticulosBalanzaMapper;

public class ArticulosBalanzaService {

	protected static Logger log = Logger.getLogger(ArticulosBalanzaService.class);

	protected ArticulosBalanzaMapper mapper;

	protected static ArticulosBalanzaService instance;

	public static ArticulosBalanzaService get() {
		if (instance == null) {
			instance = new ArticulosBalanzaService();
		}
		return instance;
	}

	@SuppressWarnings("deprecation")
	public List<ArticuloBalanza> consultarArticulosBalanza(String uidActividad, DatosSesionBean datosSesion) throws Exception {
		log.debug("consultarArticulosBalanza() - Consultando artículos balanza");
		
		List<ArticuloBalanza> articulosBalanza = new ArrayList<>();
		try {
			Connection conn = new Connection();
			conn.abrirConexion(Database.getConnection());
			SqlSession sqlSession = datosSesion.getSqlSessionFactory().openSession(conn);
			ArticulosBalanzaMapper mapper = sqlSession.getMapper(ArticulosBalanzaMapper.class);
			articulosBalanza = mapper.selectArticulosBalanza(uidActividad);
		}
		catch (SQLException e) {
			String mensajeError = "Se ha producido un error al consultar los artículos balanza";
			log.error("consultarArticulosBalanza() - " + mensajeError + " - " + e.getMessage(), e);
			throw new Exception(mensajeError, e);
		}
		
		return articulosBalanza;
	}

}
