package com.comerzzia.bricodepot.inStoreEngine.persistence.cajas.movimientos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface CajasMovimientosMapper {

	List<CajasMovimientosBean> selectCajasMovimientos(@Param("codCajaOrigen") String codCajaOrigen, @Param("codConceptoMovOrigen") String codConceptoMovOrigen,
	        @Param("uidActividad") String uidActividad);
}
