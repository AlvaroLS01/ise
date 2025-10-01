package com.comerzzia.bricodepot.backoffice.services.fidelizacion.fidelizados.enlaces;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.bricodepot.backoffice.persistence.fidelizacion.colectivos.CustomColectivo;
import com.comerzzia.bricodepot.backoffice.persistence.fidelizacion.colectivos.CustomColectivoKey;
import com.comerzzia.bricodepot.backoffice.persistence.fidelizacion.colectivos.CustomColectivoMapper;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.fechas.Fecha;
import com.comerzzia.model.fidelizacion.fidelizados.FidelizadoBean;
import com.comerzzia.model.fidelizacion.fidelizados.FidelizadoKey;
import com.comerzzia.model.fidelizacion.fidelizados.enlace.EnlaceFidelizadoBean;
import com.comerzzia.model.fidelizacion.fidelizados.enlace.EnlaceFidelizadoExample;
import com.comerzzia.model.fidelizacion.fidelizados.enlace.EnlaceFidelizadoExample.Criteria;
import com.comerzzia.persistencia.fidelizacion.fidelizados.FidelizadoMapper;
import com.comerzzia.persistencia.fidelizacion.fidelizados.enlace.EnlaceFidelizadoMapper;
import com.comerzzia.servicios.fidelizacion.fidelizados.enlaces.EnlaceNotFoundException;
import com.comerzzia.servicios.fidelizacion.fidelizados.enlaces.ServicioEnlacesImpl;

@Component
@Primary
public class BricodepotServicioEnlacesImpl extends ServicioEnlacesImpl {

	protected static Logger log = Logger.getLogger(BricodepotServicioEnlacesImpl.class);

	protected static BricodepotServicioEnlacesImpl instance;
	
	private static final String CODIGO_COLECTIVO_CLUB = "1011";
	
	private static final String CUSTOMER_LINK_TIENDA_ALTA_CLUB = "TIENDA_ALTA_CLUB";
	private static final String CUSTOMER_LINK_FECHA_ALTA_CLUB = "FECHA_ALTA_CLUB";
	private static final String CUSTOMER_LINK_USUARIO_ALTA_CLUB = "USUARIO_ALTA_CLUB";

	public static BricodepotServicioEnlacesImpl get() {
		if (instance == null) {
			instance = new BricodepotServicioEnlacesImpl();
		}
		return instance;
	}

	@SuppressWarnings("deprecation")
	public List<EnlaceFidelizadoBean> consultarEnlacesDeFidelizado(Long idFidelizado, DatosSesionBean datosSesion) throws EnlaceNotFoundException {
		log.debug("consultarEnlacesDeFidelizado()");
		SqlSession sqlSession = datosSesion.getSqlSessionFactory().openSession();
		try {
			return consultarEnlacesDeFidelizado(idFidelizado, datosSesion, sqlSession);
		}
		finally {
			sqlSession.close();
		}
	}

	public List<EnlaceFidelizadoBean> consultarEnlacesDeFidelizado(Long idFidelizado, DatosSesionBean datosSesion, SqlSession sqlSession) throws EnlaceNotFoundException {
		log.debug("consultarEnlacesDeFidelizado() - consultando enlaces para el idFidelizado: " + idFidelizado);

		List<EnlaceFidelizadoBean> res = new ArrayList<EnlaceFidelizadoBean>();

		EnlaceFidelizadoMapper mapper = sqlSession.getMapper(EnlaceFidelizadoMapper.class);
		EnlaceFidelizadoExample example = new EnlaceFidelizadoExample();

		Criteria criteria = example.or().andUidActividadEqualTo(datosSesion.getUidActividad());
		criteria.andIdFidelizadoEqualTo(idFidelizado);

		res.addAll(mapper.selectByExample(example));

		return res;

	}
	public void insertarEnlacesColectivoDeFidelizado(DatosSesionBean datosSesion, SqlSession sqlSession, Long idFidelizado, Date fechaAlta, Long idUsuario, String codAlmacen) {
		log.debug("insertarEnlacesColectivoDeFidelizado() - Insertando Customer Links para el fidelizado con id: " + idFidelizado);
		EnlaceFidelizadoMapper enlaceFidelizadoMapper = sqlSession.getMapper(EnlaceFidelizadoMapper.class);
		CustomColectivoMapper customColectivoMapper = sqlSession.getMapper(CustomColectivoMapper.class);
		FidelizadoMapper fidelizadoMapper = sqlSession.getMapper(FidelizadoMapper.class);
		
		FidelizadoKey fidelizadoKey = new FidelizadoKey();
		fidelizadoKey.setIdFidelizado(idFidelizado);
		fidelizadoKey.setUidInstancia(datosSesion.getUidInstancia());
		FidelizadoBean fidelizado = fidelizadoMapper.selectByPrimaryKey(fidelizadoKey);
		
		boolean puedeInsertarCustomerLinks = fidelizadoPuedeInsertarCustomerLinks(fidelizado, datosSesion, customColectivoMapper, enlaceFidelizadoMapper);
		
		if (puedeInsertarCustomerLinks) {
			insertarCustomerLinks(datosSesion, idFidelizado, codAlmacen, fechaAlta, idUsuario, enlaceFidelizadoMapper);
		}

	}

	private void insertarCustomerLinks(DatosSesionBean datosSesion, Long idFidelizado, String codAlmacen, Date fechaAlta, Long idUsuario, EnlaceFidelizadoMapper enlaceFidelizadoMapper) {
		log.debug("insertarEnlacesColectivoDeFidelizado() - Fidelizado cumple con las condiciones para la inserción de Customer Links.");
		EnlaceFidelizadoBean enlace = new EnlaceFidelizadoBean();
		enlace.setUidActividad(datosSesion.getUidActividad());
		enlace.setIdFidelizado(idFidelizado);
		
		enlace.setIdClase(CUSTOMER_LINK_TIENDA_ALTA_CLUB);
		enlace.setIdObjeto(codAlmacen);
		enlaceFidelizadoMapper.insert(enlace);
		
		enlace.setIdClase(CUSTOMER_LINK_FECHA_ALTA_CLUB);
		enlace.setIdObjeto(Fecha.getFecha(fechaAlta).getString(Fecha.PATRON_FECHA_HORA_SEG));
		enlaceFidelizadoMapper.insert(enlace);

		enlace.setIdClase(CUSTOMER_LINK_USUARIO_ALTA_CLUB);
		enlace.setIdObjeto(idUsuario.toString());
		enlaceFidelizadoMapper.insert(enlace);
	}

	private boolean fidelizadoPuedeInsertarCustomerLinks(FidelizadoBean fidelizado, DatosSesionBean datosSesion, CustomColectivoMapper customColectivoMapper, EnlaceFidelizadoMapper enlaceFidelizadoMapper) {
		boolean tieneAlgunColectivosConVisualizacionAltas = fidelizado.getColectivos().stream().anyMatch(colectivo -> {
			CustomColectivoKey key = new CustomColectivoKey();
			key.setCodColectivo(colectivo.getCodColectivo());
			key.setUidInstancia(datosSesion.getUidInstancia());
			CustomColectivo custom = customColectivoMapper.selectByPrimaryKey(key);
			return custom != null && Boolean.TRUE.equals(custom.getVisualizacionAltas());
		});
		
		boolean tieneColectivoPRO = fidelizado.getColectivos().stream()
				.anyMatch(colectivo -> CODIGO_COLECTIVO_CLUB.equals(colectivo.getCodColectivo()));
		
		List<EnlaceFidelizadoBean> customerLinksFidelizado = new ArrayList<>();
		EnlaceFidelizadoExample enlaceFidelizadoExample = new EnlaceFidelizadoExample();
		enlaceFidelizadoExample.createCriteria().andUidActividadEqualTo(datosSesion.getUidActividad())
				.andIdFidelizadoEqualTo(fidelizado.getIdFidelizado());
		customerLinksFidelizado.addAll(enlaceFidelizadoMapper.selectByExample(enlaceFidelizadoExample));
		
		boolean tieneCustomerLinks = !customerLinksFidelizado.isEmpty();
		return !tieneAlgunColectivosConVisualizacionAltas && !tieneColectivoPRO && !tieneCustomerLinks;
	}
}
