package com.comerzzia.bricodepot.backoffice.persistence.tickets.codigopostal;

import com.comerzzia.bricodepot.backoffice.persistence.tickets.codigopostal.CodigoPostalBeanExample;
import com.comerzzia.bricodepot.backoffice.persistence.tickets.codigopostal.CodigoPostalBeanKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface CodigoPostalBeanMapper {
    long countByExample(CodigoPostalBeanExample example);

    int deleteByExample(CodigoPostalBeanExample example);

    int deleteByPrimaryKey(CodigoPostalBeanKey key);

    int insert(CodigoPostalBeanKey record);

    int insertSelective(CodigoPostalBeanKey record);

    List<CodigoPostalBeanKey> selectByExampleWithRowbounds(CodigoPostalBeanExample example, RowBounds rowBounds);

    List<CodigoPostalBeanKey> selectByExample(CodigoPostalBeanExample example);

    int updateByExampleSelective(@Param("record") CodigoPostalBeanKey record, @Param("example") CodigoPostalBeanExample example);

    int updateByExample(@Param("record") CodigoPostalBeanKey record, @Param("example") CodigoPostalBeanExample example);
}