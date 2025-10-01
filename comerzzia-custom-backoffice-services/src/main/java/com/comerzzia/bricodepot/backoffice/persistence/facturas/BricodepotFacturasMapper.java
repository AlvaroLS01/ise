package com.comerzzia.bricodepot.backoffice.persistence.facturas;

import java.util.List;

public interface BricodepotFacturasMapper {

	List<BricodepotFacturasBean> selectByExample(BricodepotFacturasExample example);
}
