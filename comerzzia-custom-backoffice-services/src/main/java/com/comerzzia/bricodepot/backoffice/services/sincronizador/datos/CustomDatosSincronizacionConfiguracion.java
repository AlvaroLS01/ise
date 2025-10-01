package com.comerzzia.bricodepot.backoffice.services.sincronizador.datos;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Primary;

import com.comerzzia.bricodepot.backoffice.persistence.motivos.Motivos;
import com.comerzzia.bricodepot.backoffice.persistence.motivos.MotivosExample;
import com.comerzzia.bricodepot.backoffice.persistence.motivos.MotivosMapper;
import com.comerzzia.bricodepot.backoffice.persistence.motivos.tipos.TiposMotivos;
import com.comerzzia.bricodepot.backoffice.persistence.motivos.tipos.TiposMotivosExample;
import com.comerzzia.bricodepot.backoffice.persistence.motivos.tipos.TiposMotivosMapper;
import com.comerzzia.core.model.empresas.ConfigEmpresaBean;
import com.comerzzia.core.model.i18n.I18NBean;
import com.comerzzia.core.model.i18n.I18NExample;
import com.comerzzia.core.model.i18n.I18NExample.Criteria;
import com.comerzzia.core.persistencia.i18n.I18NMapper;
import com.comerzzia.core.servicios.actividades.ActividadNotFoundException;
import com.comerzzia.core.servicios.impuestos.porcentajes.PorcentajeImpuestoException;
import com.comerzzia.core.servicios.sincronizacion.datos.DatosSincronizadorException;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.core.util.db.Database;
import com.comerzzia.core.util.xml.XMLDocument;
import com.comerzzia.core.util.xml.XMLDocumentException;
import com.comerzzia.core.util.xml.XMLDocumentNode;
import com.comerzzia.core.util.xml.XMLDocumentTransformerException;
import com.comerzzia.model.general.tiendas.TiendaBean;
import com.comerzzia.servicios.sincronizacion.datos.DatosSincronizacionConfiguracion;
import com.comerzzia.sincro.actualizadores.IActualizadorDatos;

@Primary
@SuppressWarnings("deprecation")
public class CustomDatosSincronizacionConfiguracion extends DatosSincronizacionConfiguracion {

	protected static Logger log = Logger.getLogger(CustomDatosSincronizacionConfiguracion.class);

	public static final String TAG_MOTIVOS = "motivos";
	public static final String TAG_MOTIVO = "motivo";
	public static final String TAG_CODIGO = "codigo";
	public static final String TAG_DESCRIPCION = "descripcion";
	public static final String TAG_COMENTARIO = "comentario";
	
	public static final String TAG_TIPOS_MOTIVOS = "tipos_motivos";
	public static final String TAG_TIPOS_MOTIVO = "tipos_motivo";
	public static final String TAG_TIPOS_CODIGO = "codigo_tipo";
	public static final String TAG_TIPO = "tipo";

	public CustomDatosSincronizacionConfiguracion(TiendaBean tienda, Connection conn, ConfigEmpresaBean config, String directorioScriptsTpv) {
		super(tienda, conn, config, directorioScriptsTpv);
	}

	@Override
	public XMLDocument generaXML() throws XMLDocumentException, SQLException, DatosSincronizadorException, XMLDocumentTransformerException, PorcentajeImpuestoException, ActividadNotFoundException {
		XMLDocument xml = super.generaXML();
		XMLDocumentNode root = xml.getRoot();
		
		root.añadirHijo(generarTagTipoMotivos(xml));
		root.añadirHijo(generarTagMotivos(xml));
		
		return xml;
	}

	public XMLDocumentNode generarTagMotivos(XMLDocument xml) throws DatosSincronizadorException {
		XMLDocumentNode tagMotivos = new XMLDocumentNode(xml, TAG_MOTIVOS);

		List<Motivos> motivos = obtenerMotivos();
		for (Motivos motivo : motivos) {
			XMLDocumentNode tagMotivo = new XMLDocumentNode(xml, TAG_MOTIVO);

			tagMotivo.añadirHijo(IActualizadorDatos.TAG_UID_ACTIVIDAD, motivo.getUidActividad());
			tagMotivo.añadirHijo(TAG_CODIGO, motivo.getCodigo());
			String desc = traduceDesc(motivo);
			tagMotivo.añadirHijo(TAG_DESCRIPCION, desc);
			tagMotivo.añadirHijo(TAG_COMENTARIO, motivo.getComentario());
			tagMotivo.añadirHijo(TAG_TIPOS_CODIGO, motivo.getCodigoTipo());

			tagMotivos.añadirHijo(tagMotivo);
		}
		return tagMotivos;
	}

	private String traduceDesc(Motivos motivo) {
		SqlSession sqlSession = Database.getSqlSession(conn);
		I18NMapper mapper = sqlSession.getMapper(I18NMapper.class);

		I18NExample example = new I18NExample();
		Criteria crit = example.createCriteria();
		String descTraducida = "";
		boolean existeRegistro = false;
		crit.andUidActividadEqualTo(config.getUidActividad()).andCodlenguaEqualTo(tienda.getCodPais()).andIdClaseEqualTo(Motivos.CLASE_DESCRIPCION).andIdObjetoEqualTo(motivo.getCodigo());

		List<I18NBean> lista = mapper.selectByExample(example);

		if (!lista.isEmpty()) {
			descTraducida = lista.get(0).getValor();
			existeRegistro = true;
		}
		return existeRegistro ? descTraducida : motivo.getDescripcion();
	}

	public List<Motivos> obtenerMotivos() throws DatosSincronizadorException {
		SqlSession sqlSession = Database.getSqlSession(conn);
		MotivosMapper mapper = sqlSession.getMapper(MotivosMapper.class);
		MotivosExample example = new MotivosExample();
		example.or().andUidActividadEqualTo(config.getUidActividad()).andComentarioIsNull();
		List<Motivos> motivosParaSincronizar = mapper.selectByExample(example);

		return motivosParaSincronizar;
	}

	@Override
	protected String getClaseActualizacion() {
		return "com.comerzzia.bricodepot.inStoreEngine.actualizadores.CustomActualizadorDatosConfiguracion";
	}
	
	public XMLDocumentNode generarTagTipoMotivos(XMLDocument xml) throws DatosSincronizadorException {
		XMLDocumentNode tagTiposMotivos = new XMLDocumentNode(xml, TAG_TIPOS_MOTIVOS);

		List<TiposMotivos> tipoMotivos = obtenerTiposMotivos();
		for (TiposMotivos tipoMotivo : tipoMotivos) {
			XMLDocumentNode tagTipoMotivo = new XMLDocumentNode(xml, TAG_TIPOS_MOTIVO);

			tagTipoMotivo.añadirHijo(IActualizadorDatos.TAG_UID_ACTIVIDAD, tipoMotivo.getUidActividad());
			tagTipoMotivo.añadirHijo(TAG_TIPOS_CODIGO, tipoMotivo.getCodigoTipo());
			tagTipoMotivo.añadirHijo(TAG_DESCRIPCION, tipoMotivo.getDescripcion());
			tagTipoMotivo.añadirHijo(TAG_TIPO, tipoMotivo.getTipo());

			tagTiposMotivos.añadirHijo(tagTipoMotivo);
		}
		return tagTiposMotivos;
	}

	public List<TiposMotivos> obtenerTiposMotivos() throws DatosSincronizadorException {
		SqlSession sqlSession = Database.getSqlSession(conn);
		TiposMotivosMapper mapper = sqlSession.getMapper(TiposMotivosMapper.class);
		TiposMotivosExample example = new TiposMotivosExample();
		example.or().andUidActividadEqualTo(config.getUidActividad());
		List<TiposMotivos> tiposMotivosParaSincronizar = mapper.selectByExample(example);

		return tiposMotivosParaSincronizar;
	}
}
