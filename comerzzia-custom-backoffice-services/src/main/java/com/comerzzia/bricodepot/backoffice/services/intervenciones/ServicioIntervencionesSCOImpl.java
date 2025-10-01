package com.comerzzia.bricodepot.backoffice.services.intervenciones;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.comerzzia.bricodepot.backoffice.persistence.intervenciones.Intervenciones;
import com.comerzzia.bricodepot.backoffice.persistence.intervenciones.IntervencionesExample;
import com.comerzzia.bricodepot.backoffice.persistence.intervenciones.IntervencionesMapper;
import com.comerzzia.bricodepot.backoffice.services.intervenciones.exceptions.IntervencionesConstraintViolationException;
import com.comerzzia.bricodepot.backoffice.services.intervenciones.exceptions.IntervencionesException;

@Service
public class ServicioIntervencionesSCOImpl {
	
	protected static Logger log = Logger.getLogger(ServicioIntervencionesSCOImpl.class);

	protected static ServicioIntervencionesSCOImpl instance;
	
	public static ServicioIntervencionesSCOImpl get() {
		if (instance == null) {
			instance = new ServicioIntervencionesSCOImpl();
		}
		return instance;
	}

	public static void setCustomInstance(ServicioIntervencionesSCOImpl instance) {
		ServicioIntervencionesSCOImpl.instance = instance;
	}
	
	public Integer insertarIntervencionSCO(SqlSession sqlSession, Intervenciones inter) throws IntervencionesConstraintViolationException, IntervencionesException {
		
		Integer resultado = 0;
		try {
			
			IntervencionesMapper mapper = sqlSession.getMapper(IntervencionesMapper.class);
			resultado = mapper.insert(inter);
			return resultado;
			
		}catch (Exception e) {
			String mensajeError = "";
			if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
				mensajeError = "Ya existe un registro de Auditoria con los mismos datos";
				throw new IntervencionesConstraintViolationException(mensajeError, e);
			}
			else {
				mensajeError = "Se ha producido un error al insertar la Auditoria";
				throw new IntervencionesException(mensajeError, e);
			}
		}
	}

	public List<Intervenciones> consultar(IntervencionesExample example, SqlSession sqlSession) {
		IntervencionesMapper mapper = sqlSession.getMapper(IntervencionesMapper.class);
		return mapper.selectByExample(example);
	}
}
