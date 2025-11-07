package com.comerzzia.bimbaylola.pos.persistence.core.config.configcontadores.rangos;

import com.comerzzia.bimbaylola.pos.persistence.core.config.configcontadores.rangos.ConfigContadorRango;
import com.comerzzia.bimbaylola.pos.persistence.core.config.configcontadores.rangos.ConfigContadorRangoExample;
import com.comerzzia.bimbaylola.pos.persistence.core.config.configcontadores.rangos.ConfigContadorRangoKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ConfigContadorRangoMapper {
    long countByExample(ConfigContadorRangoExample example);

    int deleteByExample(ConfigContadorRangoExample example);

    int deleteByPrimaryKey(ConfigContadorRangoKey key);

    int insert(ConfigContadorRango row);

    int insertSelective(ConfigContadorRango row);

    List<ConfigContadorRango> selectByExampleWithRowbounds(ConfigContadorRangoExample example, RowBounds rowBounds);

    List<ConfigContadorRango> selectByExample(ConfigContadorRangoExample example);

    ConfigContadorRango selectByPrimaryKey(ConfigContadorRangoKey key);

    int updateByExampleSelective(@Param("row") ConfigContadorRango row, @Param("example") ConfigContadorRangoExample example);

    int updateByExample(@Param("row") ConfigContadorRango row, @Param("example") ConfigContadorRangoExample example);

    int updateByPrimaryKeySelective(ConfigContadorRango row);

    int updateByPrimaryKey(ConfigContadorRango row);
}