package com.comerzzia.bricodepot.inStoreEngine.services;

import java.sql.SQLException;
import java.util.List;

import com.comerzzia.bricodepot.inStoreEngine.persistence.cajas.movimientos.CajasMovimientosBean;
import com.comerzzia.bricodepot.inStoreEngine.persistence.cajas.movimientos.CajasMovimientosMapper;
import com.comerzzia.core.util.config.ComerzziaApp;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.core.util.db.Database;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicioCajasMovimientos {

	protected static ServicioCajasMovimientos instance;

	public static ServicioCajasMovimientos get() {
		if (instance == null) {
			instance = new ServicioCajasMovimientos();
		}
		return instance;
	}

	@Autowired
	protected CajasMovimientosMapper cajasMovimientosMapper;

	@SuppressWarnings({ "deprecation" })
	public List<CajasMovimientosBean> consultarCajasMovimientos(String codCajaOrigen, String codConceptoOrigen, String uidActividad) throws SQLException {

		Connection conn = new Connection();
		SqlSession sqlSession = null;
		conn.abrirConexion(Database.getConnection());
		sqlSession = ComerzziaApp.get().getSqlSessionFactory().openSession(conn);

		CajasMovimientosMapper cajasMovimientosMapper = sqlSession.getMapper(CajasMovimientosMapper.class);
		List<CajasMovimientosBean> listTickets = cajasMovimientosMapper.selectCajasMovimientos(codCajaOrigen, codConceptoOrigen, uidActividad);

		return listTickets;
	}
}
