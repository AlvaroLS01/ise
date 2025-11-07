package com.comerzzia.bimbaylola.pos.persistence.ventas.tipooperacion;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface TipoOperacionVentaMapper {
    long countByExample(TipoOperacionVentaExample example);

    int deleteByExample(TipoOperacionVentaExample example);

    int deleteByPrimaryKey(TipoOperacionVentaKey key);

    int insert(TipoOperacionVenta record);

    int insertSelective(TipoOperacionVenta record);

    List<TipoOperacionVenta> selectByExampleWithRowbounds(TipoOperacionVentaExample example, RowBounds rowBounds);

    List<TipoOperacionVenta> selectByExample(TipoOperacionVentaExample example);

    TipoOperacionVenta selectByPrimaryKey(TipoOperacionVentaKey key);

    int updateByExampleSelective(@Param("record") TipoOperacionVenta record, @Param("example") TipoOperacionVentaExample example);

    int updateByExample(@Param("record") TipoOperacionVenta record, @Param("example") TipoOperacionVentaExample example);

    int updateByPrimaryKeySelective(TipoOperacionVenta record);

    int updateByPrimaryKey(TipoOperacionVenta record);
}