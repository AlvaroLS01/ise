package com.comerzzia.bricodepot.backoffice.services.erroresinterfaces;

import java.util.List;

import com.comerzzia.bricodepot.backoffice.persistence.erroresinterfaces.ErrorInterfazBean;
import com.comerzzia.bricodepot.backoffice.persistence.erroresinterfaces.ErrorInterfazExample;
import com.comerzzia.bricodepot.backoffice.persistence.erroresinterfaces.ErrorInterfazKey;
import com.comerzzia.bricodepot.backoffice.persistence.erroresinterfaces.ParametrosBuscarErroresInterfacesBean;
import com.comerzzia.core.servicios.sesion.IDatosSesion;
import com.comerzzia.core.util.paginacion.PaginaResultados;

public interface ErroresInterfacesService {

	ErrorInterfazBean selectByPrimaryKey(ErrorInterfazKey key);
	List<ErrorInterfazBean> selectByExample(ErrorInterfazExample example);
	PaginaResultados consultar(ParametrosBuscarErroresInterfacesBean param, IDatosSesion datosSesion) throws ErrorInterfazException;
	List<String> consultarIdClases(IDatosSesion datosSesion);
	void delete(IDatosSesion datosSesion, String idClase, String idObjeto, String uidError);
}
