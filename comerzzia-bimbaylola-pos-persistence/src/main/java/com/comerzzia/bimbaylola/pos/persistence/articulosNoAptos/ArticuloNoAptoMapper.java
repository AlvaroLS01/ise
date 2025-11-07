package com.comerzzia.bimbaylola.pos.persistence.articulosNoAptos;

import com.comerzzia.bimbaylola.pos.persistence.articulosNoAptos.ArticuloNoAptoBean;
import com.comerzzia.bimbaylola.pos.persistence.articulosNoAptos.ArticuloNoAptoExample;
import com.comerzzia.bimbaylola.pos.persistence.articulosNoAptos.ArticuloNoAptoKey;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ArticuloNoAptoMapper{
	
    int countByExample(ArticuloNoAptoExample example);

    int deleteByExample(ArticuloNoAptoExample example);

    int deleteByPrimaryKey(ArticuloNoAptoKey key);

    int insert(ArticuloNoAptoBean record);

    int insertSelective(ArticuloNoAptoBean record);

    List<ArticuloNoAptoBean> selectByExampleWithRowbounds(ArticuloNoAptoExample example, RowBounds rowBounds);

    List<ArticuloNoAptoBean> selectByExample(ArticuloNoAptoExample example);

    ArticuloNoAptoBean selectByPrimaryKey(ArticuloNoAptoKey key);

    int updateByExampleSelective(@Param("record") ArticuloNoAptoBean record, @Param("example") 
    	ArticuloNoAptoExample example);

    int updateByExample(@Param("record") ArticuloNoAptoBean record, @Param("example") 
    	ArticuloNoAptoExample example);

    int updateByPrimaryKeySelective(ArticuloNoAptoBean record);

    int updateByPrimaryKey(ArticuloNoAptoBean record);
    
}