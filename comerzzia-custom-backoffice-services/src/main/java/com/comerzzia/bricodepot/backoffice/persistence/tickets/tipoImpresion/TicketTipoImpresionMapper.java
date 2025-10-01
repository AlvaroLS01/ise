package com.comerzzia.bricodepot.backoffice.persistence.tickets.tipoImpresion;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface TicketTipoImpresionMapper {
    long countByExample(TicketTipoImpresionExample example);

    int deleteByExample(TicketTipoImpresionExample example);

    int deleteByPrimaryKey(TicketTipoImpresionKey key);

	int insert(TicketTipoImpresion record);

    int insertSelective(TicketTipoImpresion record);

    List<TicketTipoImpresion> selectByExampleWithRowbounds(TicketTipoImpresionExample example, RowBounds rowBounds);

    List<TicketTipoImpresion> selectByExample(TicketTipoImpresionExample example);

    TicketTipoImpresion selectByPrimaryKey(TicketTipoImpresionKey key);

    int updateByExampleSelective(@Param("record") TicketTipoImpresion record, @Param("example") TicketTipoImpresionExample example);

    int updateByExample(@Param("record") TicketTipoImpresion record, @Param("example") TicketTipoImpresionExample example);

    int updateByPrimaryKeySelective(TicketTipoImpresion record);

    int updateByPrimaryKey(TicketTipoImpresion record);
}