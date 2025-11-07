package com.comerzzia.bimbaylola.pos.persistence.pais.x;

import com.comerzzia.bimbaylola.pos.persistence.pais.x.XPaisBean;
import com.comerzzia.bimbaylola.pos.persistence.pais.x.XPaisExample;
import com.comerzzia.bimbaylola.pos.persistence.pais.x.XPaisKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface XPaisMapper {
    int countByExample(XPaisExample example);

    int deleteByExample(XPaisExample example);

    int deleteByPrimaryKey(XPaisKey key);

    int insert(XPaisBean record);

    int insertSelective(XPaisBean record);

    List<XPaisBean> selectByExampleWithRowbounds(XPaisExample example, RowBounds rowBounds);

    List<XPaisBean> selectByExample(XPaisExample example);

    XPaisBean selectByPrimaryKey(XPaisKey key);

    int updateByExampleSelective(@Param("record") XPaisBean record, @Param("example") XPaisExample example);

    int updateByExample(@Param("record") XPaisBean record, @Param("example") XPaisExample example);

    int updateByPrimaryKeySelective(XPaisBean record);

    int updateByPrimaryKey(XPaisBean record);
}