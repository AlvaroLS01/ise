package com.comerzzia.bimbaylola.pos.persistence.tickets;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ByLTicketMapper {
    long countByExample(ByLTicketExample example);

    int deleteByExample(ByLTicketExample example);

    int deleteByPrimaryKey(ByLTicketKey key);

    int insert(ByLTicket record);

    int insertSelective(ByLTicket record);

    List<ByLTicket> selectByExampleWithBLOBsWithRowbounds(ByLTicketExample example, RowBounds rowBounds);

    List<ByLTicket> selectByExampleWithBLOBs(ByLTicketExample example);

    List<ByLTicket> selectByExampleWithRowbounds(ByLTicketExample example, RowBounds rowBounds);

    List<ByLTicket> selectByExample(ByLTicketExample example); 

    ByLTicket selectByPrimaryKey(ByLTicketKey key);

    int updateByExampleSelective(@Param("record") ByLTicket record, @Param("example") ByLTicketExample example);

    int updateByExampleWithBLOBs(@Param("record") ByLTicket record, @Param("example") ByLTicketExample example);

    int updateByExample(@Param("record") ByLTicket record, @Param("example") ByLTicketExample example);

    int updateByPrimaryKeySelective(ByLTicket record);

    int updateByPrimaryKeyWithBLOBs(ByLTicket record);

    int updateByPrimaryKey(ByLTicket record);
    
    List<ByLTicketBean> selectForHistoricalSearch(ByLTicketExample example);
}