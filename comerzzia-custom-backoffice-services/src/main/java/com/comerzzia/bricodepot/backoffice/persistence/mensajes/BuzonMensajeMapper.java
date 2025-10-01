package com.comerzzia.bricodepot.backoffice.persistence.mensajes;

import com.comerzzia.bricodepot.backoffice.persistence.mensajes.BuzonMensaje;
import com.comerzzia.bricodepot.backoffice.persistence.mensajes.BuzonMensajeExample;
import com.comerzzia.bricodepot.backoffice.persistence.mensajes.BuzonMensajeKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface BuzonMensajeMapper {
    long countByExample(BuzonMensajeExample example);

    int deleteByExample(BuzonMensajeExample example);

    int deleteByPrimaryKey(BuzonMensajeKey key);

    int insert(BuzonMensaje row);

    int insertSelective(BuzonMensaje row);

    List<BuzonMensaje> selectByExampleWithRowbounds(BuzonMensajeExample example, RowBounds rowBounds);

    List<BuzonMensaje> selectByExample(BuzonMensajeExample example);

    BuzonMensaje selectByPrimaryKey(BuzonMensajeKey key);

    int updateByExampleSelective(@Param("row") BuzonMensaje row, @Param("example") BuzonMensajeExample example);

    int updateByExample(@Param("row") BuzonMensaje row, @Param("example") BuzonMensajeExample example);

    int updateByPrimaryKeySelective(BuzonMensaje row);

    int updateByPrimaryKey(BuzonMensaje row);
}