package com.comerzzia.bricodepot.backoffice.services.movimientos.retirada;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.comerzzia.bricodepot.backoffice.persistence.movimientos.retirada.MovimientosRetirada;
import com.comerzzia.bricodepot.backoffice.persistence.movimientos.retirada.MovimientosRetiradaMapper;
import com.comerzzia.core.servicios.ContextHolder;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;

@Service
public class MovimientosRetiradaService {
	
	protected static Logger log = Logger.getLogger(MovimientosRetiradaService.class);

	protected static MovimientosRetiradaService instance;
	
	public static MovimientosRetiradaService get(){
		if (instance != null) {
			   return instance;
		}

		instance = ContextHolder.get().getBean(MovimientosRetiradaService.class) ;
		
		if (instance == null) {
			throw new RuntimeException("The Spring context is not initilized");
		}
		
		return instance;
	}

	public static void setCustomInstance(MovimientosRetiradaService instance){
		MovimientosRetiradaService.instance = instance;
	}
	
	public void añadirMovimientoRetirada(SqlSession sqlSession, DatosSesionBean datosSesion, MovimientosRetirada movRetirada) {
		log.debug("añadirAnticipo() - Añadiendo anticipos");
		
		MovimientosRetiradaMapper mapper = sqlSession.getMapper(MovimientosRetiradaMapper.class);
		
		mapper.insert(movRetirada);
	}

	 public boolean existeMovimientoRetirada(SqlSession sqlSession, DatosSesionBean datosSesion, MovimientosRetirada movRetirada) {
	        log.debug("existeMovimientoRetirada() - Consultando existencia del movimiento de retirada");
	        MovimientosRetiradaMapper mapper = sqlSession.getMapper(MovimientosRetiradaMapper.class);
	        Integer count = mapper.existeMovimientoRetirada(movRetirada);
	        return count != null && count > 0;
	 }

}
