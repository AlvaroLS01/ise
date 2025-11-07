package com.comerzzia.bimbaylola.pos.persistence.core.contadores;

import com.comerzzia.bimbaylola.pos.persistence.core.contadores.ByLContadorBean;
import com.comerzzia.bimbaylola.pos.persistence.core.contadores.ByLContadorExample;
import com.comerzzia.bimbaylola.pos.persistence.core.contadores.ByLContadorKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ByLContadorMapper {
    int countByExample(ByLContadorExample example);

    int deleteByExample(ByLContadorExample example);

    int deleteByPrimaryKey(ByLContadorKey key);

    int insert(ByLContadorBean record);

    int insertSelective(ByLContadorBean record);

    List<ByLContadorBean> selectByExampleWithRowbounds(ByLContadorExample example, RowBounds rowBounds);

    List<ByLContadorBean> selectByExample(ByLContadorExample example);

    ByLContadorBean selectByPrimaryKey(ByLContadorKey key);

    int updateByExampleSelective(@Param("record") ByLContadorBean record, @Param("example") ByLContadorExample example);

    int updateByExample(@Param("record") ByLContadorBean record, @Param("example") ByLContadorExample example);

    int updateByPrimaryKeySelective(ByLContadorBean record);

    int updateByPrimaryKey(ByLContadorBean record);
}