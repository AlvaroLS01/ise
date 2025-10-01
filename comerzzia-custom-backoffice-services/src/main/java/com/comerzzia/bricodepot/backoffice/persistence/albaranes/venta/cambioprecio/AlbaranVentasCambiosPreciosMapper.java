package com.comerzzia.bricodepot.backoffice.persistence.albaranes.venta.cambioprecio;

import com.comerzzia.bricodepot.backoffice.persistence.albaranes.venta.cambioprecio.AlbaranVentasCambiosPrecios;
import com.comerzzia.bricodepot.backoffice.persistence.albaranes.venta.cambioprecio.AlbaranVentasCambiosPreciosExample;
import com.comerzzia.bricodepot.backoffice.persistence.albaranes.venta.cambioprecio.AlbaranVentasCambiosPreciosKey;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AlbaranVentasCambiosPreciosMapper {
    long countByExample(AlbaranVentasCambiosPreciosExample example);
    int deleteByExample(AlbaranVentasCambiosPreciosExample example);
    int deleteByPrimaryKey(AlbaranVentasCambiosPreciosKey key);
    int insert(AlbaranVentasCambiosPrecios row);
    int insertSelective(AlbaranVentasCambiosPrecios row);
    List<AlbaranVentasCambiosPrecios> selectByExample(AlbaranVentasCambiosPreciosExample example);
    AlbaranVentasCambiosPrecios selectByPrimaryKey(AlbaranVentasCambiosPreciosKey key);
    int updateByExampleSelective(@Param("row") AlbaranVentasCambiosPrecios row, @Param("example") AlbaranVentasCambiosPreciosExample example);
    int updateByExample(@Param("row") AlbaranVentasCambiosPrecios row, @Param("example") AlbaranVentasCambiosPreciosExample example);
    int updateByPrimaryKeySelective(AlbaranVentasCambiosPrecios row);
    int updateByPrimaryKey(AlbaranVentasCambiosPrecios row);
    List<String> consultarTicketsUid(@Param("uidActividad")String uidActividad,@Param("fechaInicio")Date fechaInicio,@Param("fechaFin")Date fechaFin);
    String consultarCodCajaPorUidTicket(@Param("uidActividad")String uidActividad,@Param("uidTicket")String uidTicket);
}