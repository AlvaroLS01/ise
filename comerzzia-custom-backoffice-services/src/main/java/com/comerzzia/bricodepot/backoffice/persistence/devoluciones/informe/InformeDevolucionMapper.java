package com.comerzzia.bricodepot.backoffice.persistence.devoluciones.informe;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface InformeDevolucionMapper {

	long countByExample(InformeDevolucionExample example);

	int deleteByExample(InformeDevolucionExample example);

	int deleteByPrimaryKey(InformeDevolucionKey key);

	int insert(InformeDevolucion record);

	int insertSelective(InformeDevolucion record);

	List<InformeDevolucion> selectByExample(InformeDevolucionExample example);

	InformeDevolucion selectByPrimaryKey(InformeDevolucionKey key);

	int updateByExampleSelective(@Param("record") InformeDevolucion record,	@Param("example") InformeDevolucionExample example);

	int updateByExample(@Param("record") InformeDevolucion record, @Param("example") InformeDevolucionExample example);

	int updateByPrimaryKeySelective(InformeDevolucion record);

	int updateByPrimaryKey(InformeDevolucion record);
}