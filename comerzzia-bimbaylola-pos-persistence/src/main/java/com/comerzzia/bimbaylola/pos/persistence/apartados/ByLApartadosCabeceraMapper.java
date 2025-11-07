package com.comerzzia.bimbaylola.pos.persistence.apartados;

import com.comerzzia.bimbaylola.pos.persistence.apartados.ByLApartadosCabecera;
import com.comerzzia.bimbaylola.pos.persistence.apartados.ByLApartadosCabeceraExample;
import com.comerzzia.bimbaylola.pos.persistence.apartados.ByLApartadosCabeceraKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ByLApartadosCabeceraMapper {
    long countByExample(ByLApartadosCabeceraExample example);

    int deleteByExample(ByLApartadosCabeceraExample example);

    int deleteByPrimaryKey(ByLApartadosCabeceraKey key);

    int insert(ByLApartadosCabecera row);

    int insertSelective(ByLApartadosCabecera row);

    List<ByLApartadosCabecera> selectByExampleWithRowbounds(ByLApartadosCabeceraExample example, RowBounds rowBounds);

    List<ByLApartadosCabecera> selectByExample(ByLApartadosCabeceraExample example);

    ByLApartadosCabecera selectByPrimaryKey(ByLApartadosCabeceraKey key);

    int updateByExampleSelective(@Param("row") ByLApartadosCabecera row, @Param("example") ByLApartadosCabeceraExample example);

    int updateByExample(@Param("row") ByLApartadosCabecera row, @Param("example") ByLApartadosCabeceraExample example);

    int updateByPrimaryKeySelective(ByLApartadosCabecera row);

    int updateByPrimaryKey(ByLApartadosCabecera row);
}