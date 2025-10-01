package com.comerzzia.bricodepot.backoffice.services.albaranes.ventas.presupuestos;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.comerzzia.bricodepot.backoffice.persistence.albaranes.venta.presupuestos.PresupuestoVenta;
import com.comerzzia.bricodepot.backoffice.persistence.albaranes.venta.presupuestos.PresupuestoVentaExample;
import com.comerzzia.bricodepot.backoffice.persistence.albaranes.venta.presupuestos.PresupuestoVentaMapper;
import com.comerzzia.core.util.config.ComerzziaApp;

public class PresupuestosVentasServiceImpl {

	protected Logger log = Logger.getLogger(PresupuestosVentasServiceImpl.class);
	protected static PresupuestosVentasServiceImpl instance;
	
	public static PresupuestosVentasServiceImpl get() {
		if (instance == null) {
			instance = new PresupuestosVentasServiceImpl();
		}
		return instance;
	}

	/**
	 * Consulta si el idPresupuesto ya ha sido utilizado en una venta
	 * 
	 * @param idPresupuesto
	 * @param uidActividad
	 * @return presupuesto
	 */
	@SuppressWarnings("deprecation")
	public PresupuestoVenta consultarPresupuestoExistente(Long idPresupuesto, String uidActividad) {
		SqlSession sqlSession = null;
		log.debug("consultarPresupuestoExistente() - Comprobando si se ha utilizado el presupuesto: " + idPresupuesto);
		try {
			sqlSession = ComerzziaApp.get().getSqlSessionFactory().openSession();
			
			PresupuestoVentaMapper mapper = sqlSession.getMapper(PresupuestoVentaMapper.class);
 			PresupuestoVentaExample example = new PresupuestoVentaExample();
 			example.or().andIdPresupuestoEqualTo(idPresupuesto).andUidActividadEqualTo(uidActividad);
			List<PresupuestoVenta> presupuestoExistentes = mapper.selectByExample(example);

			if (presupuestoExistentes != null && !presupuestoExistentes.isEmpty()) {
				return presupuestoExistentes.get(0);
			}

		}
		catch (Exception e) {
			log.error("consultarPresupuestoExistente() - Ha ocurrido un error consultando el presupuesto " + e.getMessage(), e);
		}
		finally {
			sqlSession.close();
		}

		return null;
	}
	

}
