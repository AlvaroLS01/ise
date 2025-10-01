package com.comerzzia.bricodepot.backoffice.services.facturas;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comerzzia.bricodepot.backoffice.persistence.facturas.BricodepotFacturasBean;
import com.comerzzia.bricodepot.backoffice.persistence.facturas.BricodepotFacturasExample;
import com.comerzzia.bricodepot.backoffice.persistence.facturas.BricodepotFacturasExample.Criteria;
import com.comerzzia.bricodepot.backoffice.persistence.facturas.BricodepotFacturasMapper;
import com.comerzzia.bricodepot.backoffice.persistence.facturas.ParametrosBuscarFacturasBean;
import com.comerzzia.core.servicios.sesion.IDatosSesion;
import com.comerzzia.core.util.paginacion.PaginaResultados;

@Service
public class BricodepotServicioFacturasImpl implements BricodepotFacturasService {

	private static final Logger log = Logger.getLogger(BricodepotServicioFacturasImpl.class);

	@Autowired
	protected BricodepotFacturasMapper mapper;

	@Override
	public PaginaResultados consultarFacturas(ParametrosBuscarFacturasBean param, IDatosSesion datosSesion) throws BricodepotFacturasException {
		log.debug("consultarFacturas() - Consultando facturas");

		try {
			
			BricodepotFacturasExample example = obtenerFiltro(param, datosSesion);
			if (!param.getOrden().isEmpty()) {
				example.setOrderByClause(param.getOrden());
			}
			else {
				example.setOrderByClause("FECHA");
			}

			if (param.getNumPagina() == 0) {
				param.setNumPagina(1);
			}
			
			List<BricodepotFacturasBean> resultados = new ArrayList<BricodepotFacturasBean>(param.getTamañoPagina());
			PaginaResultados paginaRes = new PaginaResultados(param, resultados);
			
			List<BricodepotFacturasBean> facturas = mapper.selectByExample(example);

			int fromIndex = (paginaRes.getInicio() - 1);
			int toIndex = (paginaRes.getInicio() + paginaRes.getTamañoPagina() - 1);
			if (toIndex > facturas.size()) {
				toIndex = facturas.size();
			}

			resultados.addAll(facturas.subList(fromIndex, toIndex));
			paginaRes.setTotalResultados(facturas.size());

			return paginaRes;
		}
		catch (Exception e) {
			log.error("consultarFacturas() - " + e.getMessage());
			String mensaje = "Error al consultar las facturas: " + e.getMessage();
			throw new BricodepotFacturasException(mensaje, e);
		}
	}

	protected BricodepotFacturasExample obtenerFiltro(ParametrosBuscarFacturasBean param, IDatosSesion datosSesion) {
		BricodepotFacturasExample example = new BricodepotFacturasExample();

		Criteria criteria = example.or();
		
		criteria.andUidActividadEqualTo(datosSesion.getUidActividad());
		
		if (StringUtils.isNotBlank(param.getCif())) {
			criteria.andCifLikeInsensitive(param.getCif());
		}

		if (StringUtils.isNotBlank(param.getCodalm())) {
			criteria.andCodalmLikeInsensitive(param.getCodalm());
		}

		if (StringUtils.isNotBlank(param.getCodcaja())) {
			criteria.andCodcajaLikeInsensitive(param.getCodcaja());
		}

		if (StringUtils.isNotBlank(param.getCodart())) {
			example.getFiltroByCodart("SI");
			criteria.andCodartLikeInsensitive(param.getCodart());
		}

		if (StringUtils.isNotBlank(param.getDescli())) {
			criteria.andDescliLikeInsensitive(param.getDescli());
		}

		if (param.getIdTipoDoc() != null) {
			criteria.andIdTipoDocEqualTo(param.getIdTipoDoc());
		}

		if (param.getFechaDesde() != null) {
			criteria.andFechaGreaterThanOrEqualTo(param.getFechaDesde());
		}

		if (param.getFechaHasta() != null) {
			criteria.andFechaLessThanOrEqualTo(param.getFechaHasta());
		}

		if (param.getImporteTotal() != null) {
			criteria.andTotalEqualTo(param.getImporteTotal());
		}

		if (param.getReferenciaCliente() != null && !param.getReferenciaCliente().isEmpty()) {
			criteria.andReferenciaClienteLikeInsensitive(param.getReferenciaCliente());
		}

		if (param.getUsuario() != null && !param.getUsuario().isEmpty()) {
			criteria.andUsuarioLikeInsensitive(param.getUsuario());
		}

		return example;
	}

}
