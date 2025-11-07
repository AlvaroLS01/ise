package com.comerzzia.bimbaylola.pos.persistence.movimientostarjeta;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface CajaMovimientoTarjetaMapper {

	long countByExample(CajaMovimientoTarjetaExample example);

	int deleteByExample(CajaMovimientoTarjetaExample example);

	int deleteByPrimaryKey(CajaMovimientoTarjetaKey key);

	int insert(CajaMovimientoTarjeta record);

	int insertSelective(CajaMovimientoTarjeta record);

	List<CajaMovimientoTarjeta> selectByExampleWithBLOBsWithRowbounds(CajaMovimientoTarjetaExample example, RowBounds rowBounds);

	List<CajaMovimientoTarjeta> selectByExampleWithBLOBs(CajaMovimientoTarjetaExample example);

	List<CajaMovimientoTarjeta> selectByExampleWithRowbounds(CajaMovimientoTarjetaExample example, RowBounds rowBounds);

	List<CajaMovimientoTarjeta> selectByExample(CajaMovimientoTarjetaExample example);

	CajaMovimientoTarjeta selectByPrimaryKey(CajaMovimientoTarjetaKey key);

	int updateByExampleSelective(@Param("record") CajaMovimientoTarjeta record, @Param("example") CajaMovimientoTarjetaExample example);

	int updateByExampleWithBLOBs(@Param("record") CajaMovimientoTarjeta record, @Param("example") CajaMovimientoTarjetaExample example);

	int updateByExample(@Param("record") CajaMovimientoTarjeta record, @Param("example") CajaMovimientoTarjetaExample example);

	int updateByPrimaryKeySelective(CajaMovimientoTarjeta record);

	int updateByPrimaryKeyWithBLOBs(CajaMovimientoTarjeta record);

	int updateByPrimaryKey(CajaMovimientoTarjeta record);
}