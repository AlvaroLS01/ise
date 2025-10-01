package com.comerzzia.bricodepot.backoffice.services.anticipos;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comerzzia.bricodepot.backoffice.persistence.albaranes.venta.pagos.anticipos.Anticipos;
import com.comerzzia.bricodepot.backoffice.persistence.albaranes.venta.pagos.anticipos.AnticiposExample;
import com.comerzzia.bricodepot.backoffice.persistence.albaranes.venta.pagos.anticipos.AnticiposExample.Criteria;
import com.comerzzia.bricodepot.backoffice.persistence.albaranes.venta.pagos.anticipos.AnticiposMapper;
import com.comerzzia.bricodepot.backoffice.util.anticipos.AnticiposConstants;
import com.comerzzia.core.servicios.ContextHolder;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.db.Connection;

@Service
public class AnticiposServiceImpl {

	protected static Logger log = Logger.getLogger(AnticiposServiceImpl.class);

	protected static AnticiposServiceImpl instance;
	
	public static AnticiposServiceImpl get(){
		if (instance != null) {
			   return instance;
		}

		instance = ContextHolder.get().getBean(AnticiposServiceImpl.class) ;
		
		if (instance == null) {
			throw new RuntimeException("The Spring context is not initilized");
		}
		
		return instance;
		
	}
	
	public static void setCustomInstance(AnticiposServiceImpl instance){
		AnticiposServiceImpl.instance = instance;
	}

	public Anticipos consultarAnticipo(SqlSession sqlSession, DatosSesionBean datosSesion, String numAnticipo) {
		log.debug("consultarAnticipo() - Consultando anticipos");
		
		AnticiposMapper mapper = sqlSession.getMapper(AnticiposMapper.class);
		
		AnticiposExample example = new AnticiposExample();
		Criteria criteria = example.or();
		
		criteria.andUidActividadEqualTo(datosSesion.getUidActividad())
		.andNumAnticipoEqualTo(numAnticipo);

		List<Anticipos> anticipos = mapper.selectByExample(example);

		Anticipos resultado = null;
		if (!anticipos.isEmpty()) {

			resultado = anticipos.get(0);
		}
		return resultado;
	}
	
	public void añadirAnticipo(SqlSession sqlSession, DatosSesionBean datosSesion, Long idClieAlbaran, String importeAnticipo, String numAnticipo) {
		log.debug("añadirAnticipo() - Añadiendo anticipos");
		
		AnticiposMapper mapper = sqlSession.getMapper(AnticiposMapper.class);
		
		Anticipos anticipos = new Anticipos();
		anticipos.setUidActividad(datosSesion.getUidActividad());
		anticipos.setIdClieAlbaran(idClieAlbaran);
		anticipos.setImporte(importeAnticipo);
		anticipos.setNumAnticipo(numAnticipo);
		
		anticipos.setEstado(AnticiposConstants.PARAMETRO_ESTADO_DISPONIBLE);
		mapper.insert(anticipos);
	}
	
	public void actualizaEstadoAnticipo(SqlSession sqlSession, DatosSesionBean datosSesion, Anticipos anticipo, String estado) {
		log.debug("actualizaEstadoAnticipo() - Actualizando el estado del anticipo");

		
		AnticiposMapper mapper = sqlSession.getMapper(AnticiposMapper.class);
		
		AnticiposExample anticipoExample = new AnticiposExample();
		Criteria crit = anticipoExample.createCriteria();
		crit.andUidActividadEqualTo(anticipo.getUidActividad());
		crit.andIdClieAlbaranEqualTo(anticipo.getIdClieAlbaran());
		crit.andNumAnticipoEqualTo(anticipo.getNumAnticipo());
		
		anticipo.setEstado(estado);
		mapper.updateByExample(anticipo, anticipoExample);
	}

}
