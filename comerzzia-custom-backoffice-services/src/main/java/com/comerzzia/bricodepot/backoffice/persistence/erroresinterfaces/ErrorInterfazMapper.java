package com.comerzzia.bricodepot.backoffice.persistence.erroresinterfaces;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ErrorInterfazMapper {
    int countByExample(ErrorInterfazExample example);

    int deleteByExampleCab(ErrorInterfazExample example);

    int deleteByPrimaryKeyCab(ErrorInterfazKey key);
    
    int deleteByPrimaryKeyDet(ErrorInterfazKey key);

    int insert(ErrorInterfazBean record);

    int insertSelective(ErrorInterfazBean record);

    List<ErrorInterfazBean> selectByExampleWithRowbounds(ErrorInterfazExample example, RowBounds rowBounds);

    List<ErrorInterfazBean> selectByExample(ErrorInterfazExample example);

    ErrorInterfazBean selectByPrimaryKey(ErrorInterfazKey key);

    int updateByExampleSelective(@Param("record") ErrorInterfazBean record, @Param("example") ErrorInterfazExample example);

    int updateByExample(@Param("record") ErrorInterfazBean record, @Param("example") ErrorInterfazExample example);

    int updateByPrimaryKeySelective(ErrorInterfazBean record);

    int updateByPrimaryKey(ErrorInterfazBean record);
    
    List<String> selectIdClase(@Param("uidActividad") String uidActividad);
    
    void deleteByProcedure(@Param("uidActividad") String uidActividad, @Param("idClase") String idClase, @Param("idObjeto") String idObjeto);
    
    void copiaHistoricoDet(@Param("uidActividad") String uidActividad, @Param("uidError") String uidError);

    void copiaHistoricoCab(@Param("uidActividad") String uidActividad, @Param("uidError") String uidError);
    
    int deleteByExampleHis(ErrorInterfazExample example);
    
}