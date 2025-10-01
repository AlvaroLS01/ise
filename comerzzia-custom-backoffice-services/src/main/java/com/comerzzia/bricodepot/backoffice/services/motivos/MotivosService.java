package com.comerzzia.bricodepot.backoffice.services.motivos;

import com.comerzzia.bricodepot.backoffice.persistence.motivos.Motivos;
import com.comerzzia.core.servicios.sesion.IDatosSesion;
import com.comerzzia.core.util.paginacion.PaginaResultados;

public interface MotivosService {
	PaginaResultados consultar(ParametrosBuscarMotivosBean param, IDatosSesion datosSesion) throws MotivoException;

	Motivos consultar(String codMotivo, IDatosSesion datosSesion) throws MotivoException;

	void salvar(Motivos motivo, IDatosSesion datosSesion) throws MotivoException;

	void crear(Motivos motivo, IDatosSesion datosSesion) throws MotivoException;

	void modificar(Motivos motivo, IDatosSesion datosSesion) throws MotivoException;

	void eliminar(String codMotivo, IDatosSesion datosSesion) throws MotivoException;

}
