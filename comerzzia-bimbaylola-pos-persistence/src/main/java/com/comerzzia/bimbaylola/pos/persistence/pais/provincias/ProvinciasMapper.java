package com.comerzzia.bimbaylola.pos.persistence.pais.provincias;

import com.comerzzia.bimbaylola.pos.persistence.pais.provincias.ProvinciasBean;
import com.comerzzia.bimbaylola.pos.persistence.pais.provincias.ProvinciasExample;
import com.comerzzia.bimbaylola.pos.persistence.pais.provincias.ProvinciasKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ProvinciasMapper{

	int countByExample(ProvinciasExample example);

	int deleteByExample(ProvinciasExample example);

	int deleteByPrimaryKey(ProvinciasKey key);

	int insert(ProvinciasBean record);

	int insertSelective(ProvinciasBean record);

	List<ProvinciasBean> selectByExampleWithRowbounds(ProvinciasExample example, RowBounds rowBounds);

	List<ProvinciasBean> selectByExample(ProvinciasExample example);

	ProvinciasBean selectByPrimaryKey(ProvinciasKey key);

	int updateByExampleSelective(@Param("record") ProvinciasBean record, @Param("example") ProvinciasExample example);

	int updateByExample(@Param("record") ProvinciasBean record, @Param("example") ProvinciasExample example);

	int updateByPrimaryKeySelective(ProvinciasBean record);

	int updateByPrimaryKey(ProvinciasBean record);
}