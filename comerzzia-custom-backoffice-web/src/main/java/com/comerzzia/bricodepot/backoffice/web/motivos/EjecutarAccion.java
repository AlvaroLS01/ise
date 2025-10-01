package com.comerzzia.bricodepot.backoffice.web.motivos;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.comerzzia.bricodepot.backoffice.persistence.motivos.tipos.TiposMotivos;
import com.comerzzia.bricodepot.backoffice.persistence.motivos.tipos.TiposMotivosExample;
import com.comerzzia.bricodepot.backoffice.persistence.motivos.tipos.TiposMotivosMapper;
import com.comerzzia.bricodepot.backoffice.services.motivos.ParametrosBuscarMotivosBean;
import com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.core.util.db.Database;
import com.comerzzia.taglib.WebKeys;
import com.comerzzia.web.base.Accion;
import com.comerzzia.web.base.Vista;

public class EjecutarAccion extends Accion {

	protected static Logger log = Logger.getLogger(BuscarAccion.class);

	@Override
	public Vista ejecutar(HttpServletRequest request) {
		try {
			HttpSession sesion = request.getSession();
			DatosSesionBean datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);

			// Comprobamos los permisos de la accion
			PermisosEfectivosAccionBean permisos = datosSesion.getPermisos(this.getAccionMenu());
			if (!permisos.isPuedeEjecutar()) {
				return SIN_PERMISO;
			}
			// Inicializamos los parametros de busqueda
			ParametrosBuscarMotivosBean param = (ParametrosBuscarMotivosBean) sesion.getAttribute("paramBuscarMotivos");
			if (param == null) {
				param = new ParametrosBuscarMotivosBean();
				sesion.setAttribute("paramBuscarMotivos", param);

			}
			else {
				param.setNumPagina(0);
			}

			List<TiposMotivos> listaTiposMotivos = consultarTiposMotivos(datosSesion);
			sesion.setAttribute("tiposMotivos", listaTiposMotivos);
			
			return getControlador().getAccion("buscar").ejecutar(request);
		}
		catch (Exception e) {
			log.error("Ha ocurrido un error: " + e);
			setError(request, e);
			return ERROR_PAGE;
		}
	}

	@Override
	public String getNombre() {
		return "ejecutar";
	}

	@SuppressWarnings("deprecation")
	private List<TiposMotivos> consultarTiposMotivos(DatosSesionBean datosSesion) {
		Connection conn = new Connection();
		List<TiposMotivos> tiposMotivosParaSincronizar = null;
		try {
			conn.abrirConexion(Database.getConnection());

			SqlSession sqlSession = Database.getSqlSession(conn);
			TiposMotivosMapper mapper = sqlSession.getMapper(TiposMotivosMapper.class);
			TiposMotivosExample example = new TiposMotivosExample();
			example.or().andUidActividadEqualTo(datosSesion.getConfigEmpresa().getUidActividad());
			tiposMotivosParaSincronizar = mapper.selectByExample(example);

		}
		catch (SQLException e) {
			log.error("consultarTiposMotivos() - Ha ocurrido un error consultando los tipos de motivos : ", e);
		}

		return tiposMotivosParaSincronizar;
	}
}
