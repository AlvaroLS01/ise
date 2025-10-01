package com.comerzzia.bricodepot.backoffice.services.motivos;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comerzzia.bricodepot.backoffice.persistence.motivos.Motivos;
import com.comerzzia.bricodepot.backoffice.persistence.motivos.MotivosExample;
import com.comerzzia.bricodepot.backoffice.persistence.motivos.MotivosExample.Criteria;
import com.comerzzia.bricodepot.backoffice.persistence.motivos.MotivosKey;
import com.comerzzia.bricodepot.backoffice.persistence.motivos.MotivosMapper;
import com.comerzzia.core.model.empresas.ConfigEmpresaBean;
import com.comerzzia.core.model.i18n.I18NBean;
import com.comerzzia.core.servicios.contadores.ServicioContadoresImpl;
import com.comerzzia.core.servicios.i18n.ServicioI18NImpl;
import com.comerzzia.core.servicios.sesion.IDatosSesion;
import com.comerzzia.core.util.base.Estado;
import com.comerzzia.core.util.paginacion.PaginaResultados;
import com.comerzzia.model.versionado.VersionadoBean;

@Service
public class MotivosServiceImpl implements MotivosService {

	protected static Logger log = Logger.getLogger(MotivosServiceImpl.class);

	@Autowired
	protected MotivosMapper mapper;

	@Autowired
	protected ServicioI18NImpl i18n;

	@Autowired
	protected ServicioContadoresImpl servicioContador;

	@Override
	public PaginaResultados consultar(ParametrosBuscarMotivosBean param, IDatosSesion datosSesion)
			throws MotivoException {
		try {
			log.debug("consultar() - Consultando motivos");
			MotivosExample example = new MotivosExample();
			Criteria criteria = example.or().andUidActividadEqualTo(datosSesion.getUidActividad());

			// CODIGO
			if (StringUtils.isNotBlank(param.getCodigo())) {
				criteria.andCodigoLike("%" + param.getCodigo() + "%");
			}

			// DESCRIPCION
			if (StringUtils.isNotBlank(param.getDescripcion())) {
				criteria.andDescripcionLike("%" + param.getDescripcion() + "%");
			}

			example.setOrderByClause(param.getOrden());
			if (param.getNumPagina() == 0) {
				param.setNumPagina(1);
			}

			List<Motivos> resultados = new ArrayList<Motivos>(param.getTamañoPagina());
			PaginaResultados paginaResultados = new PaginaResultados(param, resultados);

			List<Motivos> motivos = mapper.selectByExample(example);

			motivos.stream().forEach(motivo -> {
				if (!motivo.isTraduccionesCargadas() && !motivo.isEstadoNuevo()) {
					List<I18NBean> traducciones = i18n.selectByObjectId(Motivos.CLASE_DESCRIPCION,
							motivo.getCodigo(), datosSesion);
					motivo.setTraduccionesDescripcion(traducciones);
					motivo.setTraduccionesCargadas(true);
				}
			});
			//Este código es para que en la lista salga vacío, tal como se envío desde el POS
			//La conversión es debido a que no puedo guardar el campo como cadena vacía, se me guarda como NULL, y eso está "reservado"
			//Para los nuevos motivos que se añaden desde el BO
			motivos.stream()
			.filter(x -> x.getComentario() != null)
			.filter(x -> x.getComentario().equals("Ignorar")).forEach(x -> x.setComentario(""));

			int fromIndex = (paginaResultados.getInicio() - 1);
			int toIndex = (paginaResultados.getInicio() + paginaResultados.getTamañoPagina() - 1);
			if (toIndex > motivos.size()) {
				toIndex = motivos.size();
			}
			resultados.addAll(motivos.subList(fromIndex, toIndex));

			paginaResultados.setTotalResultados(motivos.size());
			return paginaResultados;

		} catch (Exception e) {
			String msg = "consultar() - Ha ocurrido un error consultando los motivos";
			log.error(msg + ": " + e.getMessage());

			throw new MotivoException(msg, e);

		}
	}

	@Override
	public Motivos consultar(String codigo, IDatosSesion datosSesion) throws MotivoException {
		try {
			log.debug("consultar() - Consultando motivo con código: " + codigo);
			MotivosKey key = new MotivosKey();
			key.setUidActividad(datosSesion.getUidActividad());
			key.setCodigo(codigo);

			Motivos motivo = mapper.selectByPrimaryKey(key);
			if (motivo == null) {
				String msg = "No se ha encontrado el motivo: " + codigo;
				log.info(msg);
				throw new MotivoException(msg);
			}
			return motivo;
		} catch (Exception e) {
			String msg = "Ha ocurrido un error consultando el motivo";
			log.error(msg + " :" + e.getMessage());
			throw new MotivoException(msg, e);
		}
	}

	@Override
	public void salvar(Motivos motivo, IDatosSesion datosSesion) throws MotivoException {
		switch (motivo.getEstadoBean()) {
		case Estado.NUEVO:
			crear(motivo, datosSesion);
			break;

		case Estado.MODIFICADO:
			modificar(motivo, datosSesion);
			break;
		}

	}

	@Override
	public void crear(Motivos motivo, IDatosSesion datosSesion) throws MotivoException {
		try {
			log.debug("crear() - Creando motivo");
			motivo.setUidActividad(datosSesion.getUidActividad());
			motivo.setCodigo(
					String.valueOf(servicioContador.obtenerValorContador(datosSesion, "COD_MOTIVO_DEV").intValue()));
			mapper.insert(motivo);
			salvarTraducciones(motivo, datosSesion);
		} catch (Exception e) {
			String msg = "crear() - No se ha podido crear el motivo";
			log.error(msg + " :" + e.getMessage());
			throw new MotivoException(msg, e);
		}

	}

	@Override
	public void modificar(Motivos motivo, IDatosSesion datosSesion) throws MotivoException {
		try {
			log.debug("modificar() - Modificando el motivo " + motivo.getCodigo());
			motivo.setUidActividad(datosSesion.getUidActividad());
			mapper.updateByPrimaryKey(motivo);
			salvarTraducciones(motivo, datosSesion);
		} catch (Exception e) {
			String msg = "modificar() - No se ha podido modificar el motivo";
			log.error(msg + " :" + e.getMessage());
			throw new MotivoException(msg, e);
		}
	}

	@Override
	public void eliminar(String codigo, IDatosSesion datosSesion) throws MotivoException {
		try {
			log.debug("eliminar() - Eliminando el motivo: " + codigo);
			MotivosKey key = new MotivosKey();
			key.setUidActividad(datosSesion.getUidActividad());
			key.setCodigo(codigo);
			mapper.deleteByPrimaryKey(key);
		} catch (Exception e) {
			String msg = "eliminar() - No se ha podido eliminar el motivo";
			log.error(msg + " :" + e.getMessage());
			throw new MotivoException(msg, e);
		}

	}

	public List<Motivos> consultarClientesActualizacion(ConfigEmpresaBean configEmpresa,
			VersionadoBean versionado) throws MotivoException {
		// Que quede constancia de que el Versionado no se usa.

		try {
			MotivosExample example = new MotivosExample();
			Criteria crit = example.createCriteria();
			crit.andUidActividadEqualTo(configEmpresa.getUidActividad());

			List<Motivos> motivos = mapper.selectByExample(example);

			return motivos;
		} catch (Exception e) {
			log.error(
					"consultarClientesActualizacion() - Ha habido un error al consultar los clientes de la actualización: "
							+ e.getMessage(),
					e);
			throw new MotivoException(
					"consultarClientesActualizacion() - Ha habido un error al consultar los clientes de la actualización: "
							+ e.getMessage(),
					e);
		}
	}

	protected void salvarTraducciones(Motivos motivo, IDatosSesion datosSesion) {
		for (String idClase : motivo.getMapaTraducciones().keySet()) {
			List<I18NBean> traducciones = motivo.getMapaTraducciones().get(idClase);
			if (traducciones != null) {
				List<I18NBean> traduccionesConMotivos = new ArrayList<I18NBean>();
				for (I18NBean traduccion : traducciones) {
					traduccion.setIdObjeto(motivo.getCodigo());
					traduccionesConMotivos.add(traduccion);
				}
				motivo.setTraduccionesDescripcion(traduccionesConMotivos);
				i18n.insertUpdateDelete(traduccionesConMotivos, datosSesion);
			}
		}
	}

}
