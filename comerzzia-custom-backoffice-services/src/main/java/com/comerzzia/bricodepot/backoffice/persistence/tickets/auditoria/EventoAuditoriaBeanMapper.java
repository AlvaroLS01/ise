package com.comerzzia.bricodepot.backoffice.persistence.tickets.auditoria;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface EventoAuditoriaBeanMapper {
    long countByExample(EventoAuditoriaBeanExample example);

    int deleteByExample(EventoAuditoriaBeanExample example);

    int deleteByPrimaryKey(EventoAuditoriaBeanKey key);

    int insert(EventoAuditoriaBean row);

    int insertSelective(EventoAuditoriaBean row);

    List<EventoAuditoriaBean> selectByExampleWithRowbounds(EventoAuditoriaBeanExample example, RowBounds rowBounds);

    List<EventoAuditoriaBean> selectByExample(EventoAuditoriaBeanExample example);

    EventoAuditoriaBean selectByPrimaryKey(EventoAuditoriaBeanKey key);

    int updateByExampleSelective(@Param("row") EventoAuditoriaBean row, @Param("example") EventoAuditoriaBeanExample example);

    int updateByExample(@Param("row") EventoAuditoriaBean row, @Param("example") EventoAuditoriaBeanExample example);

    int updateByPrimaryKeySelective(EventoAuditoriaBean row);

    int updateByPrimaryKey(EventoAuditoriaBean row);
}