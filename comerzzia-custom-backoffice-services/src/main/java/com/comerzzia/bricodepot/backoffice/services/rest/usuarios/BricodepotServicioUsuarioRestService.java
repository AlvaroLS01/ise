package com.comerzzia.bricodepot.backoffice.services.rest.usuarios;

import com.comerzzia.core.model.empresas.ConfigEmpresaBean;
import com.comerzzia.servicios.rest.response.ResponsePatchRest;

public interface BricodepotServicioUsuarioRestService {

	public ResponsePatchRest cambiarClave(String nombreUsuario, String clave, String claveNueva, ConfigEmpresaBean configEmpresa);
}
