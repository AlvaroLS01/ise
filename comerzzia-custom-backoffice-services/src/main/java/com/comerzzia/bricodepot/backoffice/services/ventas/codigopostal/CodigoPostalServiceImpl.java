package com.comerzzia.bricodepot.backoffice.services.ventas.codigopostal;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.comerzzia.bricodepot.backoffice.persistence.tickets.codigopostal.CodigoPostalBeanKey;
import com.comerzzia.bricodepot.backoffice.persistence.tickets.codigopostal.CodigoPostalBeanMapper;
import com.comerzzia.core.servicios.ContextHolder;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;

@Service
public class CodigoPostalServiceImpl {

	protected static Logger log = Logger.getLogger(CodigoPostalServiceImpl.class);
	
	protected static CodigoPostalServiceImpl instance;
	
	public static CodigoPostalServiceImpl get() {
		
		if (instance != null) {
			   return instance;
		}

		instance = ContextHolder.get().getBean(CodigoPostalServiceImpl.class) ;
		
		if (instance == null) {
			throw new RuntimeException("The Spring context is not initilized");
		}
		
		return instance;
	}
	
	public static void setCustomInstance(CodigoPostalServiceImpl instance){
		CodigoPostalServiceImpl.instance = instance;
	}
	
	public void añadirVentaCodigoPostal(SqlSession sqlSession, DatosSesionBean datosSesion, Long idClieAlbaran, String codigoPostal) {
		log.debug("añadirVentaCodigoPostal() - añadiendo codigo postal");
		
		CodigoPostalBeanMapper mapper = sqlSession.getMapper(CodigoPostalBeanMapper.class);
		
		CodigoPostalBeanKey codigoPostalBean = new CodigoPostalBeanKey();
		codigoPostalBean.setUidActividad(datosSesion.getUidActividad());
		codigoPostalBean.setIdClieAlbaran(idClieAlbaran);
		codigoPostalBean.setCodigoPostal(codigoPostal);
		
		mapper.insert(codigoPostalBean);
		
		log.debug("añadirVentaCodigoPostal() - añadido codigo postal "+ codigoPostal +" con id cliente albaran : "+idClieAlbaran);
		
	}
}
