package com.comerzzia.bricodepot.backoffice.services.general.tiendas.atcud;
import java.sql.Connection;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.comerzzia.bricodepot.backoffice.persistence.general.tiendas.atcud.AtcudMagento;
import com.comerzzia.bricodepot.backoffice.persistence.general.tiendas.atcud.AtcudMagentoExample;
import com.comerzzia.bricodepot.backoffice.persistence.general.tiendas.atcud.AtcudMagentoMapper;
import com.comerzzia.bricodepot.backoffice.persistence.general.tiendas.atcud.Exception.BricodepotAtcudException;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.base.Estado;


@Service
public class BricodepotServicioAtcud {

	private static Logger log = Logger.getLogger(BricodepotServicioAtcud.class);
	
	protected static BricodepotServicioAtcud instance;
	
	public static BricodepotServicioAtcud get() {
		if(instance == null){
			instance = new BricodepotServicioAtcud();
		}
		return instance;
	}
	
	public AtcudMagento consultarAtcudPorAlmacenYTipoDoc(SqlSession sqlSession, String codAlm, String codEmp, String tipoDoc, String uidActividad) {
		log.debug("consultarAtcudPorAlmacenYTipoDoc()");
		AtcudMagento atcud = null;
		try {
			AtcudMagentoMapper mapper = sqlSession.getMapper(AtcudMagentoMapper.class);
			AtcudMagentoExample example = new AtcudMagentoExample();
			example.or().andUidActividadEqualTo(uidActividad).andCodalmEqualTo(codAlm).andMascaraDivisor1EqualTo(codEmp).andMascaraDivisor2EqualTo(tipoDoc);
			List<AtcudMagento> lista = mapper.selectByExample(example);
			if (lista.size() > 0) {
				atcud = lista.get(0);
			}
		}
		catch (Exception e) {
			log.error("consultarAtcudPorAlmacenYTipoDoc() - " + e.getMessage(), e);
			throw e;
		}
		return atcud;
	}
	
	@SuppressWarnings("deprecation")
	public List<AtcudMagento> consultarRangosFiscales(DatosSesionBean datosSesion, String codAlm) {
		SqlSession sqlSession = datosSesion.getSqlSessionFactory().openSession();
		List<AtcudMagento> lista = null;
		try {
			log.debug("consultarRangosFiscales() - Consultando de rangos fiscales");
			AtcudMagentoMapper mapper = sqlSession.getMapper(AtcudMagentoMapper.class);
			AtcudMagentoExample example = new AtcudMagentoExample();
			example.or().andUidActividadEqualTo(datosSesion.getUidActividad()).andCodalmEqualTo(codAlm);
			lista = mapper.selectByExample(example);
		
		}catch (Exception e) {
			log.error("consultarRangosFiscales() - " + e.getMessage(), e);
		}
		finally {
			sqlSession.close();
		}
		
		return lista;
	}
	
	@SuppressWarnings("deprecation")
	public void eliminar(Connection conn, AtcudMagento atcud,DatosSesionBean datosSesion) throws BricodepotAtcudException {
		SqlSession sqlSession = datosSesion.getSqlSessionFactory().openSession(conn);
		
		try {
			log.error("eliminar() - Eliminando el atcud para el contador " + atcud.getIdContador() + " con almacén  " +  atcud.getCodalm());
			
			AtcudMagentoMapper mapper = sqlSession.getMapper(AtcudMagentoMapper.class);
			
			
			mapper.deleteByPrimaryKey(atcud);
		}
		catch (Exception e) {
			String msg = "Se ha producido un error eliminando el contador " + atcud.getIdContador() + " con almacén " +  atcud.getCodalm() + ": " + e.getMessage();
			log.error("eliminar() - " + msg);

			throw new BricodepotAtcudException(msg, e);
		}
	}
	@SuppressWarnings("deprecation")
	public void crear(Connection conn, AtcudMagento atcud, DatosSesionBean datosSesion) throws BricodepotAtcudException {
		SqlSession sqlSession = datosSesion.getSqlSessionFactory().openSession(conn);
		
		try {
			log.debug("crear() - Añadiendo contador " + atcud.getIdContador() + " con almacén  " +  atcud.getCodalm());
			
			AtcudMagentoMapper mapper = sqlSession.getMapper(AtcudMagentoMapper.class);
			
			mapper.insert(atcud);
		}
		catch (Exception e) {
			if(e.getCause() instanceof SQLIntegrityConstraintViolationException) {
				String msg = "Ya se ha establecido la tienda "+atcud.getCodalm()+" - para el contador " + atcud.getIdContador();
				throw new BricodepotAtcudException(msg, e);
			}
			else {
				String msg = "Se ha producido un error añadiendo contador  " + atcud.getIdContador() + "  con almacén  " +  atcud.getCodalm() +": " + e.getMessage();
				log.error("crear() - " + msg);
				throw new BricodepotAtcudException(msg, e);
			}
		}	
		
	}
	public void salvar(Connection conn, AtcudMagento atcud, DatosSesionBean datosSesion) throws BricodepotAtcudException{

		switch (atcud.getEstadoBean()) {
			case Estado.NUEVO:
				crear(conn, atcud, datosSesion);
				break;
			case Estado.BORRADO:
				eliminar(conn, atcud, datosSesion);
			}
	}

	
}
