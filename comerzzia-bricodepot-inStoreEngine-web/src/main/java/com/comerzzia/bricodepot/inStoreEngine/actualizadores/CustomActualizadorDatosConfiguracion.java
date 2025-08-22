package com.comerzzia.bricodepot.inStoreEngine.actualizadores;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.core.util.db.Connection;
import com.comerzzia.core.util.db.PreparedStatement;
import com.comerzzia.core.util.fechas.FechaException;
import com.comerzzia.core.util.xml.XMLDocument;
import com.comerzzia.core.util.xml.XMLDocumentException;
import com.comerzzia.core.util.xml.XMLDocumentNode;
import com.comerzzia.core.util.xml.XMLDocumentNodeNotFoundException;
import com.comerzzia.sincro.actualizadores.ActualizadorDatosConfiguracion;
import com.comerzzia.sincro.actualizadores.ActualizadorDatosException;

@Component
@Primary
public class CustomActualizadorDatosConfiguracion extends ActualizadorDatosConfiguracion {

	// Tabla donde se realizará la actualización
	protected static final String TABLA_MOTIVOS = "X_MOTIVOS_TBL";

	public static final String TAG_MOTIVOS = "motivos";
	public static final String TAG_MOTIVO = "motivo";
	public static final String TAG_CODIGO = "codigo";
	public static final String TAG_DESCRIPCION = "descripcion";
	public static final String TAG_COMENTARIO = "comentario";

	/* TIPOS MOTIVOS */
	protected static final String TABLA_TIPOS_MOTIVOS = "X_TIPOS_MOTIVOS_TBL";
	public static final String TAG_TIPOS_MOTIVOS = "tipos_motivos";
	public static final String TAG_TIPOS_MOTIVO = "tipos_motivo";
	public static final String TAG_TIPOS_CODIGO = "codigo_tipo";
	public static final String TAG_TIPOS_MOTIVOS_TIPO = "tipo";

	@Override
	public void actualizar(Connection conn, InputStream is) throws ActualizadorDatosException {
		try {
			XMLDocument document = new XMLDocument(is);

			// Obtenemos el root del xml
			XMLDocumentNode root = document.getRoot();

			// Actualizamos la instancia
			sincronizarInstancia(conn, root);

			// Actualizamos la actividad
			sincronizarActividad(conn, root);

			// Actualizamos las variables
			sincronizarVariables(conn, root);

			sincronizarTiposEfectos(conn, root);

			// Actualizamos las divisas
			sincronizarDivisas(conn, root);

			// Actualizamos los países
			sincronizarPaises(conn, root);

			// Actualizamos los impuestos
			sincronizarImpuestos(conn, root);

			// Actualizamos los perfiles
			sincronizarPerfiles(conn, root);

			// Actualizamos los usuarios
			sincronizarUsuarios(conn, root);

			// Actualizamos los medios de pago
			sincronizarMediosPagoPasarelas(conn, root);

			// Actualizamos la empresa
			sincronizarEmpresa(conn, root);

			// Actualizamos la tienda
			sincronizarTienda(conn, root);

			// Actualizamos los conceptos de movimiento de caja
			sincronizarConceptosMovCaja(conn, root);

			// Actualizamos los códigos postales
			sincronizarCodigosPostales(conn, root);

			sincronizarConceptosAlmacenes(conn, root);

			// Actualizamos los tipos de documento
			sincronizarTiposDocumentos(conn, root);

			// Actualizamos los tipos de documento prop
			sincronizarTiposDocumentosProp(conn, root);

			sincronizarTiposIdentPaises(conn, root);

			sincronizarAlmacenesUsuarios(conn, root);

			// Actualizamos las categorias de las etiquetas
			sincronizaarCategoriasEtiquetas(conn, root);

			sincronizarGruposDesgloses(conn, root);

			sincronizarTiposPromociones(conn, root);

			// Método para añadir información personalizada al XML de configuración
			sincronizarDatosAdicionales(conn, root);

			// Actualizamos los motivos y sus tipos
			sincronizarMotivos(conn, root);

		}
		catch (XMLDocumentException e) {
			String msg = "Error tratando el xml de actualización de datos de configuración: " + e.getMessage();
			log.error("actualizar() - " + msg);
			throw new ActualizadorDatosException(e.getMessage(), e);
		}
		catch (SQLException e) {
			log.error("actualizar() - Error al actualizar los datos de configuración: " + e.getMessage());
			throw new ActualizadorDatosException(e.getMessage(), e);
		}
		catch (FechaException e) {
			String msg = "Error formateando fecha: " + e.getMessage();
			log.error("actualizar() - " + msg);
			throw new ActualizadorDatosException(e.getMessage(), e);
		}
	}

	protected void sincronizarMotivos(Connection conn, XMLDocumentNode root) throws SQLException, XMLDocumentNodeNotFoundException {
		List<XMLDocumentNode> lstMotivos = root.getNodo(TAG_MOTIVOS).getHijos();
		if(lstMotivos != null && !lstMotivos.isEmpty()){
			borrarMotivos(conn, lstMotivos.get(0).getNodo(TAG_UID_ACTIVIDAD).getValue());
		}

		List<XMLDocumentNode> lstTiposMotivos = root.getNodo(TAG_TIPOS_MOTIVOS).getHijos();
		if(lstTiposMotivos != null && !lstTiposMotivos.isEmpty()){
			borrarTiposMotivos(conn, lstTiposMotivos.get(0).getNodo(TAG_UID_ACTIVIDAD).getValue());
		}

		for (XMLDocumentNode tagMotivo : lstTiposMotivos) {
			insertarTiposMotivos(conn, tagMotivo);
		}

		for (XMLDocumentNode tagMotivo : lstMotivos) {
			insertarMotivos(conn, tagMotivo);
		}
	}

	protected void borrarMotivos(Connection conn, String uidActividad) throws SQLException {
		PreparedStatement pstmt = null;
		String sql = null;

		sql = "DELETE FROM " + TABLA_MOTIVOS + " WHERE UID_ACTIVIDAD = " + "'" + uidActividad + "'" + ";";

		try {
			pstmt = new PreparedStatement(conn, sql);
			log.debug("borrarMotivos() - Borrando todos los registros de la tabla: " + TABLA_MOTIVOS);
			log.debug("borrarMotivos() - " + sql);
			pstmt.execute();
		}
		finally {
			try {
				pstmt.close();
			}
			catch (Exception ignore) {
				;
			}
		}

	}

	protected void insertarMotivos(Connection conn, XMLDocumentNode tagMotivo) throws SQLException, XMLDocumentNodeNotFoundException {
		PreparedStatement pstmt = null;
		String sql = null;

		sql = "INSERT INTO " + TABLA_MOTIVOS + " " + "(UID_ACTIVIDAD, CODIGO, CODIGO_TIPO, DESCRIPCION, COMENTARIO) " + "VALUES (?, ?, ?, ?, ?)";

		try {
			pstmt = new PreparedStatement(conn, sql);
			pstmt.setString(1, tagMotivo.getNodo(TAG_UID_ACTIVIDAD).getValue());
			pstmt.setString(2, tagMotivo.getNodo(TAG_CODIGO).getValue());
			pstmt.setString(3, tagMotivo.getNodo(TAG_TIPOS_CODIGO).getValue());
			pstmt.setString(4, tagMotivo.getNodo(TAG_DESCRIPCION).getValue());
			pstmt.setString(5, tagMotivo.getNodo(TAG_COMENTARIO).getValue());

			log.debug("insertarMotivos() - " + pstmt);
			pstmt.execute();
		}
		finally {
			try {
				pstmt.close();
			}
			catch (Exception ignore) {
				;
			}
		}
	}

	protected void borrarTiposMotivos(Connection conn, String uidActividad) throws SQLException {
		PreparedStatement pstmt = null;
		String sql = null;

		sql = "DELETE FROM " + TABLA_TIPOS_MOTIVOS + " WHERE UID_ACTIVIDAD = " + "'" + uidActividad + "'" + ";";

		try {
			pstmt = new PreparedStatement(conn, sql);
			log.debug("borrarMotivos() - Borrando todos los registros de la tabla: " + TABLA_TIPOS_MOTIVOS);
			log.debug("borrarMotivos() - " + sql);
			pstmt.execute();
		}
		finally {
			try {
				pstmt.close();
			}
			catch (Exception ignore) {
				;
			}
		}

	}

	protected void insertarTiposMotivos(Connection conn, XMLDocumentNode tagMotivo) throws SQLException, XMLDocumentNodeNotFoundException {
		PreparedStatement pstmt = null;
		String sql = null;

		sql = "INSERT INTO " + TABLA_TIPOS_MOTIVOS + " " + "(UID_ACTIVIDAD, CODIGO_TIPO, TIPO, DESCRIPCION) " + "VALUES (?, ?, ?, ?)";

		try {
			pstmt = new PreparedStatement(conn, sql);
			pstmt.setString(1, tagMotivo.getNodo(TAG_UID_ACTIVIDAD).getValue());
			pstmt.setString(2, tagMotivo.getNodo(TAG_TIPOS_CODIGO).getValue());
			pstmt.setString(3, tagMotivo.getNodo(TAG_TIPOS_MOTIVOS_TIPO).getValue());
			pstmt.setString(4, tagMotivo.getNodo(TAG_DESCRIPCION).getValue());

			log.debug("insertarTiposMotivos() - " + pstmt);
			pstmt.execute();
		}
		finally {
			try {
				pstmt.close();
			}
			catch (Exception ignore) {
				;
			}
		}
	}

}
