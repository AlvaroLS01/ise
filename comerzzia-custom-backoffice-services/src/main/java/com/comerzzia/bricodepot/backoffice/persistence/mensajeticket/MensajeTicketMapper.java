package com.comerzzia.bricodepot.backoffice.persistence.mensajeticket;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface MensajeTicketMapper {
    long countByExample(MensajeTicketExample example);

    int deleteByExample(MensajeTicketExample example);

    int deleteByPrimaryKey(MensajeTicketKey key);

    int insert(MensajeTicketKey row);

    int insertSelective(MensajeTicketKey row);

    List<MensajeTicketKey> selectByExampleWithRowbounds(MensajeTicketExample example, RowBounds rowBounds);

    List<MensajeTicketKey> selectByExample(MensajeTicketExample example);

    int updateByExampleSelective(@Param("row") MensajeTicketKey row, @Param("example") MensajeTicketExample example);

    int updateByExample(@Param("row") MensajeTicketKey row, @Param("example") MensajeTicketExample example);
}