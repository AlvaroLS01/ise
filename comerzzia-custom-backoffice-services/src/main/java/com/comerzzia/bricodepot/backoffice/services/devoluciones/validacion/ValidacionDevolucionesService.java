package com.comerzzia.bricodepot.backoffice.services.devoluciones.validacion;

import com.comerzzia.bricodepot.backoffice.persistence.devoluciones.validacion.ParametrosBuscarValidacionesDevolucionAlbaran;
import com.comerzzia.bricodepot.backoffice.persistence.devoluciones.validacion.ValidacionDevolucionAlbaran;
import com.comerzzia.bricodepot.backoffice.persistence.devoluciones.validacion.ValidacionDevolucionAlbaranKey;
import com.comerzzia.core.servicios.sesion.IDatosSesion;
import com.comerzzia.core.util.paginacion.PaginaResultados;

public interface ValidacionDevolucionesService {
	
	PaginaResultados consultarPaginado(ParametrosBuscarValidacionesDevolucionAlbaran param, IDatosSesion datosSesion) throws ValidacionDevolucionException;
	
	ValidacionDevolucionAlbaran consultar(ValidacionDevolucionAlbaranKey key, IDatosSesion datosSesion) throws ValidacionDevolucionException;

	void crear(ValidacionDevolucionAlbaran validacion, IDatosSesion datosSesion) throws ValidacionDevolucionException;

	void modificar(ValidacionDevolucionAlbaran validacion, IDatosSesion datosSesion) throws ValidacionDevolucionException;

}
