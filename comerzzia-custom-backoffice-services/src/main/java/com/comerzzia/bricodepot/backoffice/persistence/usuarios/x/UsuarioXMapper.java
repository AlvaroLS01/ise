package com.comerzzia.bricodepot.backoffice.persistence.usuarios.x;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface UsuarioXMapper {
    long countByExample(UsuarioXExample example);

    int deleteByExample(UsuarioXExample example);

    int deleteByPrimaryKey(UsuarioXKey key);

    int insert(UsuarioX row);

    int insertSelective(UsuarioX row);

    List<UsuarioX> selectByExampleWithRowbounds(UsuarioXExample example, RowBounds rowBounds);

    List<UsuarioX> selectByExample(UsuarioXExample example);

    UsuarioX selectByPrimaryKey(UsuarioXKey key);

    int updateByExampleSelective(@Param("row") UsuarioX row, @Param("example") UsuarioXExample example);

    int updateByExample(@Param("row") UsuarioX row, @Param("example") UsuarioXExample example);

    int updateByPrimaryKeySelective(UsuarioX row);

    int updateByPrimaryKey(UsuarioX row);
}