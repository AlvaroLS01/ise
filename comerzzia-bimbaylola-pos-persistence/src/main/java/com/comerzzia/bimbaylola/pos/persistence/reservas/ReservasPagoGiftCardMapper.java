package com.comerzzia.bimbaylola.pos.persistence.reservas;

import com.comerzzia.bimbaylola.pos.persistence.reservas.ReservasPagoGiftCardBean;
import com.comerzzia.bimbaylola.pos.persistence.reservas.ReservasPagoGiftCardExample;
import com.comerzzia.bimbaylola.pos.persistence.reservas.ReservasPagoGiftCardKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ReservasPagoGiftCardMapper{

	int countByExample(ReservasPagoGiftCardExample example);

	int deleteByExample(ReservasPagoGiftCardExample example);

	int deleteByPrimaryKey(ReservasPagoGiftCardKey key);

	int insert(ReservasPagoGiftCardBean record);

	int insertSelective(ReservasPagoGiftCardBean record);

	List<ReservasPagoGiftCardBean> selectByExampleWithRowbounds(ReservasPagoGiftCardExample example, RowBounds rowBounds);

	List<ReservasPagoGiftCardBean> selectByExample(ReservasPagoGiftCardExample example);

	ReservasPagoGiftCardBean selectByPrimaryKey(ReservasPagoGiftCardKey key);

	int updateByExampleSelective(@Param("record") ReservasPagoGiftCardBean record, @Param("example") ReservasPagoGiftCardExample example);

	int updateByExample(@Param("record") ReservasPagoGiftCardBean record, @Param("example") ReservasPagoGiftCardExample example);

	int updateByPrimaryKeySelective(ReservasPagoGiftCardBean record);

	int updateByPrimaryKey(ReservasPagoGiftCardBean record);
	
}