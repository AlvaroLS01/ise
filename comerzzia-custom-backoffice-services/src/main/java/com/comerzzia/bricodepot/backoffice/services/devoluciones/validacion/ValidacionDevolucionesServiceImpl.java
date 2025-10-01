package com.comerzzia.bricodepot.backoffice.services.devoluciones.validacion;

import java.util.ArrayList;
import java.util.List;

import com.comerzzia.bricodepot.backoffice.persistence.devoluciones.validacion.ParametrosBuscarValidacionesDevolucionAlbaran;
import com.comerzzia.bricodepot.backoffice.persistence.devoluciones.validacion.ValidacionDevolucionAlbaran;
import com.comerzzia.bricodepot.backoffice.persistence.devoluciones.validacion.ValidacionDevolucionAlbaranKey;
import com.comerzzia.bricodepot.backoffice.persistence.devoluciones.validacion.ValidacionDevolucionAlbaranMapper;
import com.comerzzia.core.servicios.sesion.IDatosSesion;
import com.comerzzia.core.util.fechas.Fecha;
import com.comerzzia.core.util.paginacion.PaginaResultados;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ValidacionDevolucionesServiceImpl implements ValidacionDevolucionesService{
	
	protected static ValidacionDevolucionesServiceImpl servicio;
	
	public static ValidacionDevolucionesServiceImpl get() {
		if (servicio==null) {
			servicio=new ValidacionDevolucionesServiceImpl();
		}
		return servicio;
	}
	
	protected static Logger log = Logger.getLogger(ValidacionDevolucionesServiceImpl.class);

	@Override
	public PaginaResultados consultarPaginado(ParametrosBuscarValidacionesDevolucionAlbaran param, IDatosSesion datosSesion)
			throws ValidacionDevolucionException {
		
		SqlSession sqlSession = null;
		try {
			log.debug("consultarPaginado() - Consultando validaciones");
			sqlSession = datosSesion.getSqlSessionFactory().openSession();
			ValidacionDevolucionAlbaranMapper mapper = sqlSession.getMapper(ValidacionDevolucionAlbaranMapper.class);
			
			List<ValidacionDevolucionAlbaran> resultados = new ArrayList<ValidacionDevolucionAlbaran>(param.getTamañoPagina());
			PaginaResultados paginaResultados = new PaginaResultados(param, resultados);
			
			param.setUidActividad(datosSesion.getUidActividad() );
			
			List<ValidacionDevolucionAlbaran> validaciones = mapper.consultarPaginado(param);
			
			//Edición de valores
			
			for(ValidacionDevolucionAlbaran validacion : validaciones) {
				//Se suma hora a la fecha del albarán
				try {
					Fecha fechaAlbaran = Fecha.getFecha(validacion.getFechaAlbaran());
					String[] horasMinutosAlbaran = validacion.getHoraAlbaran().split(":");
					fechaAlbaran.sumaHoras(Integer.parseInt(horasMinutosAlbaran[0]));
					fechaAlbaran.sumaMinutos(Integer.parseInt(horasMinutosAlbaran[1]));
					validacion.setFechaAlbaran(fechaAlbaran.getDate());
				} catch (Exception ignore) {;}

				//Se hacen positivas las unidades negativas
				validacion.setCantidad(Math.abs(validacion.getCantidad()));
				validacion.setImporte(validacion.getImporte().abs());
			}
			
			
			int fromIndex = (paginaResultados.getInicio() - 1);
			int toIndex = (paginaResultados.getInicio() + paginaResultados.getTamañoPagina() - 1);
			if (toIndex > validaciones.size()) {
				toIndex = validaciones.size();
			}
			resultados.addAll(validaciones.subList(fromIndex, toIndex));
			
			paginaResultados.setTotalResultados(validaciones.size());
			sqlSession.commit();

			return paginaResultados;
			
		} catch (Exception e) {
			sqlSession.rollback();
			String msg = "No se ha podido consultar las validaciones: " + e.getMessage();
			log.error("consultarPaginado() - " + msg, e);
			throw new ValidacionDevolucionException(msg, e);
		}
		finally {
			sqlSession.close();
		}
	}

	@Override
	public ValidacionDevolucionAlbaran consultar(ValidacionDevolucionAlbaranKey key, IDatosSesion datosSesion)
			throws ValidacionDevolucionException {
		SqlSession sqlSession = null;
		try {
			log.debug("consultar() - Consultando validación con idClieAlbaran:["+key.getIdClieAlbaran()+"] y linea:["+key.getLinea()+"]");

			sqlSession = datosSesion.getSqlSessionFactory().openSession();
			ValidacionDevolucionAlbaranMapper mapper = sqlSession.getMapper(ValidacionDevolucionAlbaranMapper.class);
			
			key.setUidActividad(datosSesion.getUidActividad());
			
			return mapper.selectByPrimaryKey(key);
		} catch (Exception e) {
			String msg = "No se ha podido consultar la validación: " + e.getMessage();
			log.error("consultar() - " + msg, e);
			throw new ValidacionDevolucionException(msg, e);
		}
		finally {
			sqlSession.close();
		}
	}


	@SuppressWarnings("deprecation")
	@Override
	public void crear(ValidacionDevolucionAlbaran validacion, IDatosSesion datosSesion)
			throws ValidacionDevolucionException {
		SqlSession sqlSession = null;
		try {
			log.debug("crear() - Creando validación con idClieAlbaran:["+validacion.getIdClieAlbaran()+"] y linea:["+validacion.getLinea()+"]");
			sqlSession = datosSesion.getSqlSessionFactory().openSession();
			ValidacionDevolucionAlbaranMapper mapper = sqlSession.getMapper(ValidacionDevolucionAlbaranMapper.class);
			
			validacion.setUidActividad(datosSesion.getUidActividad());
			
			mapper.insert(validacion);
			
			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			String msg = "No se ha podido crear la validación: " + e.getMessage();
			log.error("crear() - " + msg, e);
			throw new ValidacionDevolucionException(msg, e);
		}
		finally {
			sqlSession.close();
		}
	}

	@Override
	public void modificar(ValidacionDevolucionAlbaran validacion, IDatosSesion datosSesion)
			throws ValidacionDevolucionException {
		SqlSession sqlSession = null;
		try {
			log.debug("modificar() - Modificando la validación con idClieAlbaran:["+validacion.getIdClieAlbaran()+"] y linea:["+validacion.getLinea()+"]");
			sqlSession = datosSesion.getSqlSessionFactory().openSession();
			ValidacionDevolucionAlbaranMapper mapper = sqlSession.getMapper(ValidacionDevolucionAlbaranMapper.class);
			
			validacion.setUidActividad(datosSesion.getUidActividad());
			
			mapper.updateByPrimaryKey(validacion);
			
			sqlSession.commit();
		} catch (Exception e) {
			String msg = "No se ha podido modificar la validación: " + e.getMessage();
			log.error("modificar() - " + msg, e);
			throw new ValidacionDevolucionException(msg, e);
		}
		finally {
			sqlSession.close();
		}
		
	}
	
}
