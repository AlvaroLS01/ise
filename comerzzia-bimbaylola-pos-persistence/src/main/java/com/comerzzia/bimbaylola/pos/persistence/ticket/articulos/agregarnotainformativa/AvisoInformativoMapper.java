package com.comerzzia.bimbaylola.pos.persistence.ticket.articulos.agregarnotainformativa;

import com.comerzzia.bimbaylola.pos.persistence.ticket.articulos.agregarnotainformativa.AvisoInformativoBean;
import com.comerzzia.bimbaylola.pos.persistence.ticket.articulos.agregarnotainformativa.AvisoInformativoExample;
import com.comerzzia.bimbaylola.pos.persistence.ticket.articulos.agregarnotainformativa.AvisoInformativoKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface AvisoInformativoMapper{

	int countByExample(AvisoInformativoExample example);

	int deleteByExample(AvisoInformativoExample example);

	int deleteByPrimaryKey(AvisoInformativoKey key);

	int insert(AvisoInformativoBean record);

	int insertSelective(AvisoInformativoBean record);

	List<AvisoInformativoBean> selectByExampleWithRowbounds(AvisoInformativoExample example, RowBounds rowBounds);

	List<AvisoInformativoBean> selectByExample(AvisoInformativoExample example);

	AvisoInformativoBean selectByPrimaryKey(AvisoInformativoKey key);

	int updateByExampleSelective(@Param("record") AvisoInformativoBean record, @Param("example") AvisoInformativoExample example);

	int updateByExample(@Param("record") AvisoInformativoBean record, @Param("example") AvisoInformativoExample example);

	int updateByPrimaryKeySelective(AvisoInformativoBean record);

	int updateByPrimaryKey(AvisoInformativoBean record);

	AvisoInformativoBean selectByPrimaryKeyCodigo(AvisoInformativoExample example);
	
}