package com.comerzzia.instoreengine.herbnav.persistencia.trabajos.articulosbalanza;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ArticulosBalanzaMapper {

	public List<ArticuloBalanza> selectArticulosBalanza(@Param("uid_actividad") String uidActividad);
	
}
