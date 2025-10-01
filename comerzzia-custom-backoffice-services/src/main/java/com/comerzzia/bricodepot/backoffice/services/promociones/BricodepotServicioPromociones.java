package com.comerzzia.bricodepot.backoffice.services.promociones;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.comerzzia.bricodepot.backoffice.persistence.promociones.CabeceraPromociones;
import com.comerzzia.bricodepot.backoffice.persistence.promociones.CabeceraPromocionesKey;
import com.comerzzia.bricodepot.backoffice.persistence.promociones.CabeceraPromocionesMapper;
import com.comerzzia.bricodepot.backoffice.persistence.promociones.detalle.DetallePromociones;
import com.comerzzia.bricodepot.backoffice.persistence.promociones.detalle.DetallePromocionesKey;
import com.comerzzia.bricodepot.backoffice.persistence.promociones.detalle.DetallePromocionesMapper;
import com.comerzzia.bricodepot.backoffice.persistence.promociones.tipos.TiposPromociones;
import com.comerzzia.bricodepot.backoffice.persistence.promociones.tipos.TiposPromocionesKey;
import com.comerzzia.bricodepot.backoffice.persistence.promociones.tipos.TiposPromocionesMapper;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;

public class BricodepotServicioPromociones {
	
	protected static Logger log = Logger.getLogger(BricodepotServicioPromociones.class);
	
	protected static BricodepotServicioPromociones instance;
	
	public static BricodepotServicioPromociones get() {
		if (instance == null) {
			instance = new BricodepotServicioPromociones();
		}
		return instance;
	}
	
	public CabeceraPromociones consultarPromocionCabecera(DatosSesionBean datosSesion, SqlSession sqlSession, Long idPromocion) {
		log.debug("consultarPromocion() - consultando promocion : "+ idPromocion);
		CabeceraPromociones promocion = null;
		try {
			CabeceraPromocionesMapper mapper = sqlSession.getMapper(CabeceraPromocionesMapper.class);
			CabeceraPromocionesKey key = new CabeceraPromocionesKey();
			key.setUidActividad(datosSesion.getUidActividad());
			key.setIdPromocion(idPromocion);
			promocion = mapper.selectByPrimaryKey(key);
		}
		catch (Exception e) {
			log.error("consultarPromocionCabecera() - Ha ocurrido un error consultando la promocion "+ idPromocion,e);
		}
		
		return promocion;
	}
	
	public DetallePromociones consultarDetallePromocion(DatosSesionBean datosSesion, SqlSession sqlSession, Long idPromocion, String codart) {
		log.debug("consultarPromocion() - consultando promocion : "+ idPromocion);
		DetallePromociones promocion = null;
		try {
			DetallePromocionesMapper mapper = sqlSession.getMapper(DetallePromocionesMapper.class);
			DetallePromocionesKey key = new DetallePromocionesKey();
			key.setUidActividad(datosSesion.getUidActividad());
			key.setIdPromocion(idPromocion);
			key.setCodart(codart);
			key.setDesglose1("*");
			key.setDesglose2("*");
			promocion = mapper.selectByPrimaryKey(key);
		}
		catch (Exception e) {
			log.error("consultarDetallePromocion() - Ha ocurrido un error consultando la promocion "+ idPromocion,e);
		}
		
		return promocion;
	}

	
	public TiposPromociones consultarTipoDePromocion(DatosSesionBean datosSesion,SqlSession sqlSession, Long idPromocion) {
		log.debug("consultarDetallePromocion() - consultando tipo de la promocion : "+ idPromocion);
		TiposPromociones tipoPromocion = null;
		try {
			TiposPromocionesMapper mapper =  sqlSession.getMapper(TiposPromocionesMapper.class);
			TiposPromocionesKey key = new TiposPromocionesKey();
			key.setUidActividad(datosSesion.getUidActividad());
			key.setIdTipoPromocion(idPromocion);
			tipoPromocion = mapper.selectByPrimaryKey(key);
		}
		catch (Exception e) {
			log.error("consultarTipoDePromocion() - Ha ocurrido un error consultando la promocion "+ idPromocion,e);
		}
		
		return tipoPromocion;
	}
	
}
