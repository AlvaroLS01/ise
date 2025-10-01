package com.comerzzia.bricodepot.backoffice.services.datosMovimiento;

import java.util.Date;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.comerzzia.bricodepot.backoffice.persistence.datosMovimiento.DatosMovimientoMapper;
import com.comerzzia.core.util.db.Database;

@Service
public class DatosMovimientoServiceImpl {

	private static Logger log = Logger.getLogger(DatosMovimientoServiceImpl.class);

	protected static DatosMovimientoServiceImpl instance;

	public static DatosMovimientoServiceImpl get() {
		if (instance == null) {
			instance = new DatosMovimientoServiceImpl();
		}
		return instance;
	}

	public String consultarUidDiarioCaja(String codAlm, Date fecha) throws DatosMovimientoException {
		SqlSession sqlSession = Database.getSqlSession();
		try {
			log.debug("consultarUidDiarioCaja() - Consultando UidDiarioCaja del movimiento 92 del almacén " + codAlm + " y fecha " + fecha.toString());

			DatosMovimientoMapper mapper = sqlSession.getMapper(DatosMovimientoMapper.class);

			return mapper.consultarUidDiarioCaja(codAlm, fecha);
		}
		catch (Exception e) {
			String msg = "No se ha podido consultar el UidDiarioCaja del movimiento 92 del almacén " + codAlm + " : " + e.getMessage();
			log.error("consultarUidDiarioCaja() - " + msg, e);
			throw new DatosMovimientoException(msg, e);
		}
		finally {
			if(sqlSession != null) {
				sqlSession.close();
			}
		}
	}
}
