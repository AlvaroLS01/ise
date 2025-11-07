package com.comerzzia.bimbaylola.pos.persistence.mediosPago;

import com.comerzzia.bimbaylola.pos.persistence.mediosPago.MediosPagoBIN;
import com.comerzzia.bimbaylola.pos.persistence.mediosPago.MediosPagoBINExample;
import com.comerzzia.bimbaylola.pos.persistence.mediosPago.MediosPagoBINKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface MediosPagoBINMapper {
    long countByExample(MediosPagoBINExample example);

    int deleteByExample(MediosPagoBINExample example);

    int deleteByPrimaryKey(MediosPagoBINKey key);

    int insert(MediosPagoBIN record);

    int insertSelective(MediosPagoBIN record);

    List<MediosPagoBIN> selectByExampleWithRowbounds(MediosPagoBINExample example, RowBounds rowBounds);

    List<MediosPagoBIN> selectByExample(MediosPagoBINExample example);

    MediosPagoBIN selectByPrimaryKey(MediosPagoBINKey key);

    int updateByExampleSelective(@Param("record") MediosPagoBIN record, @Param("example") MediosPagoBINExample example);

    int updateByExample(@Param("record") MediosPagoBIN record, @Param("example") MediosPagoBINExample example);

    int updateByPrimaryKeySelective(MediosPagoBIN record);

    int updateByPrimaryKey(MediosPagoBIN record);
}