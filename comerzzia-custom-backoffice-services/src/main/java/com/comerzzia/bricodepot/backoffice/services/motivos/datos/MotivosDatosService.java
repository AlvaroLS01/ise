package com.comerzzia.bricodepot.backoffice.services.motivos.datos;

import com.comerzzia.bricodepot.backoffice.persistence.motivos.datos.MotivosDatos;
import com.comerzzia.bricodepot.backoffice.persistence.motivos.datos.MotivosDatosMapper;
import com.comerzzia.bricodepot.backoffice.services.motivos.MotivoException;
import com.comerzzia.bricodepot.backoffice.services.motivos.MotivosServiceImpl;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

public class MotivosDatosService {

	protected static Logger log = Logger.getLogger(MotivosServiceImpl.class);

	protected static MotivosDatosService instance;

	public static MotivosDatosService get() {
		if (instance == null) {
			instance = new MotivosDatosService();
		}
		return instance;
	}

	public void insertarMotivoDato(MotivosDatos motivosDatos, SqlSession sqlSession) throws MotivoException {
		try {
			log.debug("insertarMotivoDato() - Creando motivo");
			MotivosDatosMapper mapper = sqlSession.getMapper(MotivosDatosMapper.class);
			mapper.insert(motivosDatos);
		}
		catch (Exception e) {
			String msg = "insertarMotivoDato() - No se ha podido crear el motivo";
			log.error(msg + " :" + e.getMessage());
			throw new MotivoException(msg, e);
		}

	}

}
