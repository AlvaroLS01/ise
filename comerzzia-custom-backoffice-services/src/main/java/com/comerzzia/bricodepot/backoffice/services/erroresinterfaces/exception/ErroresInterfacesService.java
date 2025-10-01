package com.comerzzia.bricodepot.backoffice.services.erroresinterfaces.exception;

import java.util.List;

import com.comerzzia.core.servicios.sesion.IDatosSesion;
import com.comerzzia.core.util.paginacion.PaginaResultados;
import com.comerzzia.bricodepot.backoffice.persistence.erroresinterfaces.ErrorInterfaz;
import com.comerzzia.bricodepot.backoffice.persistence.erroresinterfaces.ErrorInterfazExample;
import com.comerzzia.bricodepot.backoffice.persistence.erroresinterfaces.ErrorInterfazKey;
import com.comerzzia.bricodepot.backoffice.persistence.erroresinterfaces.ParametrosBuscarErroresInterfacesBean;

public interface ErroresInterfacesService {

	ErrorInterfaz selectByPrimaryKey(ErrorInterfazKey key);
	List<ErrorInterfaz> selectByExample(ErrorInterfazExample example);
	PaginaResultados consultar(ParametrosBuscarErroresInterfacesBean param, IDatosSesion datosSesion) throws ErrorInterfazException;
	List<String> consultarIdClases(IDatosSesion datosSesion);
	void delete(IDatosSesion datosSesion, String idClase, String idObjeto);
}
