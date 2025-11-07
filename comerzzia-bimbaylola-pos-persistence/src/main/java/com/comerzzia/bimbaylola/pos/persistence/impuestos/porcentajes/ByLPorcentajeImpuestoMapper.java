package com.comerzzia.bimbaylola.pos.persistence.impuestos.porcentajes;

import com.comerzzia.bimbaylola.pos.persistence.impuestos.porcentajes.ByLPorcentajeImpuesto;
import com.comerzzia.bimbaylola.pos.persistence.impuestos.porcentajes.ByLPorcentajeImpuestoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ByLPorcentajeImpuestoMapper {
    long countByExample(ByLPorcentajeImpuestoExample example);

    int deleteByExample(ByLPorcentajeImpuestoExample example);

    int insert(ByLPorcentajeImpuesto record);

    int insertSelective(ByLPorcentajeImpuesto record);

    List<ByLPorcentajeImpuesto> selectByExampleWithRowbounds(ByLPorcentajeImpuestoExample example, RowBounds rowBounds);

    List<ByLPorcentajeImpuesto> selectByExample(ByLPorcentajeImpuestoExample example);

    int updateByExampleSelective(@Param("record") ByLPorcentajeImpuesto record, @Param("example") ByLPorcentajeImpuestoExample example);

    int updateByExample(@Param("record") ByLPorcentajeImpuesto record, @Param("example") ByLPorcentajeImpuestoExample example);
}