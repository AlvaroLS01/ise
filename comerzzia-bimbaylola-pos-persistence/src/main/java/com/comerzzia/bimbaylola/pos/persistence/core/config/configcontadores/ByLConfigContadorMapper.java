/**
 * ComerZZia 3.0 Copyright (c) 2008-2015 Comerzzia, S.L. All Rights Reserved. THIS WORK IS SUBJECT TO SPAIN AND
 * INTERNATIONAL COPYRIGHT LAWS AND TREATIES. NO PART OF THIS WORK MAY BE USED, PRACTICED, PERFORMED COPIED,
 * DISTRIBUTED, REVISED, MODIFIED, TRANSLATED, ABRIDGED, CONDENSED, EXPANDED, COLLECTED, COMPILED, LINKED, RECAST,
 * TRANSFORMED OR ADAPTED WITHOUT THE PRIOR WRITTEN CONSENT OF COMERZZIA, S.L. ANY USE OR EXPLOITATION OF THIS WORK
 * WITHOUT AUTHORIZATION COULD SUBJECT THE PERPETRATOR TO CRIMINAL AND CIVIL LIABILITY. CONSULT THE END USER LICENSE
 * AGREEMENT FOR INFORMATION ON ADDITIONAL RESTRICTIONS.
 */
package com.comerzzia.bimbaylola.pos.persistence.core.config.configcontadores;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ByLConfigContadorMapper{

	int countByExample(ByLConfigContadorExample example);

	int deleteByExample(ByLConfigContadorExample example);

	int deleteByPrimaryKey(String idContador);

	int insert(ByLConfigContadorBean record);

	int insertSelective(ByLConfigContadorBean record);

	List<ByLConfigContadorBean> selectByExampleWithRowbounds(ByLConfigContadorExample example, RowBounds rowBounds);

	List<ByLConfigContadorBean> selectByExample(ByLConfigContadorExample example);

	ByLConfigContadorBean selectByPrimaryKey(String idContador);

	int updateByExampleSelective(@Param("record") ByLConfigContadorBean record, @Param("example") ByLConfigContadorExample example);

	int updateByExample(@Param("record") ByLConfigContadorBean record, @Param("example") ByLConfigContadorExample example);

	int updateByPrimaryKeySelective(ByLConfigContadorBean record);

	int updateByPrimaryKey(ByLConfigContadorBean record);

	List<ByLConfigContadorBean> selectByExampleJoinTipoDocumento(ByLConfigContadorExample example, RowBounds rowBounds);

	int countByExampleJoinTipoDocumento(ByLConfigContadorExample example);
}