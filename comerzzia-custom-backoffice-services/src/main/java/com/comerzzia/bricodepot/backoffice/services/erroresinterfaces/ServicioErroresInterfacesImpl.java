package com.comerzzia.bricodepot.backoffice.services.erroresinterfaces;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comerzzia.core.servicios.sesion.IDatosSesion;
import com.comerzzia.core.util.paginacion.PaginaResultados;
import com.comerzzia.bricodepot.backoffice.persistence.erroresinterfaces.ErrorInterfazBean;
import com.comerzzia.bricodepot.backoffice.persistence.erroresinterfaces.ErrorInterfazExample;
import com.comerzzia.bricodepot.backoffice.persistence.erroresinterfaces.ErrorInterfazKey;
import com.comerzzia.bricodepot.backoffice.persistence.erroresinterfaces.ErrorInterfazMapper;
import com.comerzzia.bricodepot.backoffice.persistence.erroresinterfaces.ParametrosBuscarErroresInterfacesBean;
import com.comerzzia.bricodepot.backoffice.persistence.erroresinterfaces.ErrorInterfazExample.Criteria;

/**
 * TABLA : X_INTERFACES_ERR_CAB_HIS_TBL, X_INTERFACES_ERR_CAB_TBL, X_INTERFACES_ERR_DET_HIS_TBL,
 * X_INTERFACES_ERR_DET_TBL, X_INTERFACES_ERR_TBL, X_INTERFACES_LOG_TBL 
 */
@Service
public class ServicioErroresInterfacesImpl implements ErroresInterfacesService {

	private static final Logger log = Logger.getLogger(ServicioErroresInterfacesImpl.class);

	@Autowired
	protected ErrorInterfazMapper mapper;

	@Override
	public ErrorInterfazBean selectByPrimaryKey(ErrorInterfazKey key) {
		return mapper.selectByPrimaryKey(key);
	}

	@Override
	public List<ErrorInterfazBean> selectByExample(ErrorInterfazExample example) {
		return mapper.selectByExample(example);
	}

	@Override
	public PaginaResultados consultar(ParametrosBuscarErroresInterfacesBean param, IDatosSesion datosSesion) throws ErrorInterfazException {
		log.debug("consultar() - Consultando errores interfaces");

		PaginaResultados paginaResultados = null;
		try {
			List<ErrorInterfazBean> resultados = new ArrayList<ErrorInterfazBean>(param.getTamañoPagina());
			paginaResultados = new PaginaResultados(param, resultados);

			ErrorInterfazExample filtro = obtenerFiltro(param, datosSesion);
			if (!param.getOrden().isEmpty()) {
				filtro.setOrderByClause(param.getOrden());
			}
			else {
				filtro.setOrderByClause(ErrorInterfazExample.ORDER_BY_FECHA_INICIO);
			}
			paginaResultados.setTotalResultados(mapper.countByExample(filtro));
			resultados.addAll(mapper.selectByExampleWithRowbounds(filtro, new RowBounds(paginaResultados.getInicio() - 1, paginaResultados.getTamañoPagina())));

			return paginaResultados;
		}
		catch (Exception e) {
			log.error("consultar() - " + e.getMessage());
			String mensaje = "Error al consultar las variables: " + e.getMessage();
			throw new ErrorInterfazException(mensaje, e);
		}
	}

	protected ErrorInterfazExample obtenerFiltro(ParametrosBuscarErroresInterfacesBean param, IDatosSesion datosSesion) {
		ErrorInterfazExample filtro = new ErrorInterfazExample();

		Criteria cirteria = filtro.or();

		// ID_CLASE
		if (param.getIdClase() != null && !param.getIdClase().isEmpty()) {
			cirteria.andIdClaseEqualTo(param.getIdClase());
		}
		cirteria.andUidActividadEqualTo(datosSesion.getUidActividad());
		return filtro;
	}

	@Override
	public List<String> consultarIdClases(IDatosSesion datosSesion) {
		log.debug("consultar() - Consultando idClases de errores de interfaces");

		List<String> resultados = new ArrayList<String>();
		resultados = mapper.selectIdClase(datosSesion.getUidActividad());

		return resultados;
	}

	@Override
	public void delete(IDatosSesion datosSesion, String idClase, String idObjeto, String uidError) {
		
		//Copiamos los registros que se van a eliminar(detalles) a la tabla de historicos
		mapper.copiaHistoricoDet(datosSesion.getUidActividad(), uidError);
		mapper.copiaHistoricoCab(datosSesion.getUidActividad(), uidError);

		//Eliminamos los registros copiados
		ErrorInterfazKey errIntKey = new ErrorInterfazKey();
		errIntKey.setUidActividad(datosSesion.getUidActividad());
		errIntKey.setUidError(uidError);
		
		mapper.deleteByPrimaryKeyDet(errIntKey);
		mapper.deleteByPrimaryKeyCab(errIntKey);
		
	}

}
