package com.comerzzia.bricodepot.backoffice.services.tickets.auditoria;

import java.util.ArrayList;
import java.util.List;

import com.comerzzia.bricodepot.backoffice.persistence.tickets.auditoria.EventoAuditoriaBean;
import com.comerzzia.bricodepot.backoffice.persistence.tickets.auditoria.EventoAuditoriaBeanExample;
import com.comerzzia.bricodepot.backoffice.persistence.tickets.auditoria.EventoAuditoriaBeanExample.Criteria;
import com.comerzzia.bricodepot.backoffice.persistence.tickets.auditoria.EventoAuditoriaBeanKey;
import com.comerzzia.bricodepot.backoffice.persistence.tickets.auditoria.EventoAuditoriaBeanMapper;
import com.comerzzia.bricodepot.backoffice.persistence.tickets.auditoria.ParametrosBuscarEventoAuditoria;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.paginacion.PaginaResultados;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ServicioEventosAuditoria{

	protected static final Logger log = Logger.getLogger(ServicioEventosAuditoria.class);

	// usa inyeccion @Autowired

	protected static ServicioEventosAuditoria instance;

	public static ServicioEventosAuditoria get() {
		if (instance == null) {
			instance = new ServicioEventosAuditoria();
		}
		return instance;
	}

	public static void setCustomInstance(ServicioEventosAuditoria instance) {
		ServicioEventosAuditoria.instance = instance;
	}

	@SuppressWarnings("deprecation")
	public PaginaResultados consultar(ParametrosBuscarEventoAuditoria param, DatosSesionBean datosSesion)
			throws EventoAuditoriaException {
		SqlSession sqlSession = null;
		try {
			sqlSession = datosSesion.getSqlSessionFactory().openSession();
			log.debug("consultar() - Consultando eventos de auditoria");

			EventoAuditoriaBeanMapper mapper = sqlSession.getMapper(EventoAuditoriaBeanMapper.class);

			EventoAuditoriaBeanExample example = new EventoAuditoriaBeanExample();

			Criteria criteria = example.or();
			// UID_INSTANCIA
			criteria.andUidInstanciaEqualTo(datosSesion.getUidInstancia());

			// en principio el filtro es por usuario/usuario supervisor y por fecha
			// USUARIO
			if (StringUtils.isNotBlank(param.getUsuario())) {
				// si es numerico buscar por id, si no por nombre
				if (param.getUsuario().matches("\\d+")) {
					criteria.andIdUsuarioEqualTo(Long.valueOf(param.getUsuario()));
				} else {
					criteria.andNombreUsuarioLikeInsensitive("%" + param.getUsuario() + "%");
				}
			}
			// USUARIO SUPERVISOR
			if (StringUtils.isNotBlank(param.getUsuarioSupervisor())) {
				// si es numerico buscar por id, si no por nombre
				if (param.getUsuarioSupervisor().matches("\\d+")) {
					criteria.andIdUsuarioSupervisorEqualTo(Long.valueOf(param.getUsuarioSupervisor()));
				} else {
					criteria.andNombreUsuarioSupervisorLikeInsensitive("%" + param.getUsuarioSupervisor() + "%");
				}
			}
			// FECHA
			if (param.getFechaDesde() != null && param.getFechaHasta() != null) {
				criteria.andFechaBetween(param.getFechaDesde(), param.getFechaHasta());
			} else if (param.getFechaDesde() != null) {
				criteria.andFechaGreaterThanOrEqualTo(param.getFechaDesde());
			} else if (param.getFechaHasta() != null) {
				criteria.andFechaLessThanOrEqualTo(param.getFechaHasta());
			}

			// ORDEN
			if (!param.getOrden().isEmpty()) {
				example.setOrderByClause(param.getOrden());
			}
			
			// NUM PAGINA
			if (param.getNumPagina() == 0)
				param.setNumPagina(1);

			List<EventoAuditoriaBean> res = new ArrayList<EventoAuditoriaBean>(param.getTamañoPagina());
			PaginaResultados paginaRes = new PaginaResultados(param, res);

			List<EventoAuditoriaBean> eventos = mapper.selectByExample(example);

			int fromIndex = (paginaRes.getInicio() - 1);
			int toIndex = (paginaRes.getInicio() + paginaRes.getTamañoPagina() - 1);
			if (toIndex > eventos.size()) {
				toIndex = eventos.size();
			}

			res.addAll(eventos.subList(fromIndex, toIndex));
			paginaRes.setTotalResultados(eventos.size());
			return paginaRes;

		} catch (Exception e) {
			log.error("consultar() - " + e.getMessage(), e);
			String mensaje = "Error al eventos de auditoria: " + e.getMessage();

			throw new EventoAuditoriaException(mensaje, e);
		} finally {
			sqlSession.close();
		}
	}
	
	/**
	 * El mismo metodo de consulta pero devuelve una lista en vez de un objeto paginaResultados
	 * 
	 * @param param
	 * @param datosSesion
	 * @return
	 * @throws EventoAuditoriaException
	 */
	@SuppressWarnings("deprecation")
	public List<EventoAuditoriaBean> consultarSimple(ParametrosBuscarEventoAuditoria param, DatosSesionBean datosSesion)
			throws EventoAuditoriaException {
		SqlSession sqlSession = null;
		try {
			sqlSession = datosSesion.getSqlSessionFactory().openSession();
			log.debug("consultar() - Consultando eventos de auditoria");

			EventoAuditoriaBeanMapper mapper = sqlSession.getMapper(EventoAuditoriaBeanMapper.class);

			EventoAuditoriaBeanExample example = new EventoAuditoriaBeanExample();

			Criteria criteria = example.or();
			
			// UID_ACTIVIDAD
			criteria.andUidActividadEqualTo(datosSesion.getUidActividad());
			// UID_INSTANCIA
			criteria.andUidInstanciaEqualTo(datosSesion.getUidInstancia());

			// en principio el filtro es por usuario/usuario supervisor y por fecha
			// USUARIO
			if (StringUtils.isNotBlank(param.getUsuario())) {
				// si es numerico buscar por id, si no por nombre
				if (param.getUsuario().matches("\\d+")) {
					criteria.andIdUsuarioEqualTo(Long.valueOf(param.getUsuario()));
				} else {
					criteria.andNombreUsuarioLikeInsensitive("%" + param.getUsuario() + "%");
				}
			}
			// USUARIO SUPERVISOR
			if (StringUtils.isNotBlank(param.getUsuarioSupervisor())) {
				// si es numerico buscar por id, si no por nombre
				if (param.getUsuario().matches("\\d+")) {
					criteria.andIdUsuarioSupervisorEqualTo(Long.valueOf(param.getUsuarioSupervisor()));
				} else {
					criteria.andNombreUsuarioSupervisorLikeInsensitive("%" + param.getUsuarioSupervisor() + "%");
				}
			}
			// FECHA
			if (param.getFechaDesde() != null && param.getFechaHasta() != null) {
				criteria.andFechaBetween(param.getFechaDesde(), param.getFechaHasta());
			} else if (param.getFechaDesde() != null) {
				criteria.andFechaGreaterThanOrEqualTo(param.getFechaDesde());
			} else if (param.getFechaHasta() != null) {
				criteria.andFechaLessThanOrEqualTo(param.getFechaHasta());
			}

			// ORDEN
			if (!param.getOrden().isEmpty()) {
				example.setOrderByClause(param.getOrden());
			}

			List<EventoAuditoriaBean> eventos = mapper.selectByExample(example);

			return eventos;

		} catch (Exception e) {
			log.error("consultar() - " + e.getMessage(), e);
			String mensaje = "Error al eventos de auditoria: " + e.getMessage();

			throw new EventoAuditoriaException(mensaje, e);
		} finally {
			sqlSession.close();
		}
	}

	/**
	 * Busca un evento de auditoria por su clave primaria en BBDD
	 * 
	 * @param uidTicketAuditoria
	 * @param uidInstancia
	 * @param uidActividad
	 * @param datosSesion
	 * @return
	 * @throws EventoAuditoriaNotFoundException
	 */
	@SuppressWarnings("deprecation")
	public EventoAuditoriaBean consultarUno(String uidTicketAuditoria, String uidInstancia, String uidActividad,
			DatosSesionBean datosSesion) throws EventoAuditoriaNotFoundException {
		SqlSession sqlSession = null;
		EventoAuditoriaBean res = null;
		try {
			sqlSession = datosSesion.getSqlSessionFactory().openSession();
			log.debug("consultar() - Consultando evento de auditoria con UID ticket: " + uidTicketAuditoria);

			EventoAuditoriaBeanMapper mapper = sqlSession.getMapper(EventoAuditoriaBeanMapper.class);

			EventoAuditoriaBeanKey key = new EventoAuditoriaBeanKey();

			key.setUidActividad(uidActividad);
			key.setUidInstancia(uidInstancia);
			key.setUidTicketAuditoria(uidTicketAuditoria);

			res = mapper.selectByPrimaryKey(key);

			return res;

		} catch (Exception e) {
			log.error("consultar() - " + e.getMessage(), e);
			String mensaje = "Error consultado evento de auditoria: " + e.getMessage();

			throw new EventoAuditoriaNotFoundException(mensaje, e);
		} finally {
			sqlSession.close();
		}
	}

	public void crear(SqlSession sqlSession,EventoAuditoriaBean eventoAuditoria, DatosSesionBean datosSesion)
			throws EventoAuditoriaException {
		try {
			log.debug("crear() - Guardo evento auditoria con UID ticket: " + eventoAuditoria.getUidTicketAuditoria());

			EventoAuditoriaBeanMapper mapper = sqlSession.getMapper(EventoAuditoriaBeanMapper.class);

			mapper.insert(eventoAuditoria);

		} catch (Exception e) {

			log.error("consultar() - " + e.getMessage(), e);
			String mensaje = "Error consultado evento de auditoria: " + e.getMessage();

			throw new EventoAuditoriaException(mensaje, e);
		} 
	}

}
