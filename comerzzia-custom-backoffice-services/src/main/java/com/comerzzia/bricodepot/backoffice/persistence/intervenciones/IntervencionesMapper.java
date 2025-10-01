package com.comerzzia.bricodepot.backoffice.persistence.intervenciones;

import com.comerzzia.bricodepot.backoffice.persistence.intervenciones.Intervenciones;
import com.comerzzia.bricodepot.backoffice.persistence.intervenciones.IntervencionesExample;
import com.comerzzia.bricodepot.backoffice.persistence.intervenciones.IntervencionesKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface IntervencionesMapper {
    long countByExample(IntervencionesExample example);

    int deleteByExample(IntervencionesExample example);

    int deleteByPrimaryKey(IntervencionesKey key);

    int insert(Intervenciones row);

    int insertSelective(Intervenciones row);

    List<Intervenciones> selectByExampleWithRowbounds(IntervencionesExample example, RowBounds rowBounds);

    List<Intervenciones> selectByExample(IntervencionesExample example);

    Intervenciones selectByPrimaryKey(IntervencionesKey key);

    int updateByExampleSelective(@Param("row") Intervenciones row, @Param("example") IntervencionesExample example);

    int updateByExample(@Param("row") Intervenciones row, @Param("example") IntervencionesExample example);

    int updateByPrimaryKeySelective(Intervenciones row);

    int updateByPrimaryKey(Intervenciones row);
}