package com.comerzzia.bricodepot.backoffice.persistence.datosMovimiento;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

public interface DatosMovimientoMapper {

	String consultarUidDiarioCaja(@Param("codAlm") String codAlm, @Param("fecha") Date fecha);

}