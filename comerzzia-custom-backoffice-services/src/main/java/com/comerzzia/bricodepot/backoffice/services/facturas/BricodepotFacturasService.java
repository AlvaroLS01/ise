package com.comerzzia.bricodepot.backoffice.services.facturas;

import com.comerzzia.bricodepot.backoffice.persistence.facturas.ParametrosBuscarFacturasBean;
import com.comerzzia.core.servicios.sesion.IDatosSesion;
import com.comerzzia.core.util.paginacion.PaginaResultados;

public interface BricodepotFacturasService {

	PaginaResultados consultarFacturas(ParametrosBuscarFacturasBean param, IDatosSesion datosSesion) throws BricodepotFacturasException;
}
