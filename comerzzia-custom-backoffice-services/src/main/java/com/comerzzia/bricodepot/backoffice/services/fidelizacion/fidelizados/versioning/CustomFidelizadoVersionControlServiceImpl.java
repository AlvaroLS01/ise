package com.comerzzia.bricodepot.backoffice.services.fidelizacion.fidelizados.versioning;

import com.comerzzia.core.servicios.contadores.ContadorException;
import com.comerzzia.core.servicios.contadores.ServicioContadoresImpl;
import com.comerzzia.core.servicios.sesion.IDatosSesion;
import com.comerzzia.core.util.db.Database;
import com.comerzzia.model.versionado.VersionadoBean;
import com.comerzzia.servicios.fidelizacion.fidelizados.versioning.FidelizadoVersion;
import com.comerzzia.servicios.fidelizacion.fidelizados.versioning.FidelizadoVersionControlServiceImpl;
import com.comerzzia.servicios.versionado.VersionadoException;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

public class CustomFidelizadoVersionControlServiceImpl extends FidelizadoVersionControlServiceImpl{
	
	protected static Logger log = Logger.getLogger(CustomFidelizadoVersionControlServiceImpl.class);
	
	protected static CustomFidelizadoVersionControlServiceImpl servicio;
	
	public static CustomFidelizadoVersionControlServiceImpl get() {
		if (servicio==null) {
			servicio=new CustomFidelizadoVersionControlServiceImpl();
		}
		return servicio;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void checkFidelizadosVersion(IDatosSesion datosSesion, FidelizadoVersion fidelizadoVersion) {
		log.debug("checkFidelizadosVersion");
		if (fidelizadoVersion.getFidelizadosChanedList().size() == 0) {
			return;
		}

		// get version from counter in local transaction
		Long version;
		try {
			version = ServicioContadoresImpl.get().obtenerValorContador(datosSesion, CONTADOR_ID);
		}
		catch (ContadorException e) {
			throw new RuntimeException(e);
		}

		// generate sqlSession
		SqlSession sqlSession = null;
		try {
			if (fidelizadoVersion.getConnection() != null) {
				sqlSession = Database.getSqlSession(fidelizadoVersion.getConnection());
			}
			else {
				sqlSession = Database.getSqlSession();
			}

			// generate version for every loyal customer
			for (Long idFidelizado : fidelizadoVersion.getFidelizadosChanedList()) {
				VersionadoBean versionadoBean = new VersionadoBean();
				versionadoBean.setIdClase(ID_CLASE);
				versionadoBean.setIdObjeto(idFidelizado.toString());
				versionadoBean.setVersion(version);
				try {
					getServicioVersionado().versionar(sqlSession, datosSesion, versionadoBean);
				}
				catch (VersionadoException e) {
					throw new RuntimeException(e);
				}
			}
		}
		catch (Exception e) {
			log.error("checkFidelizadosVersion() - Ha ocurrido un error : " + e.getMessage(), e);
		}
		finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
	}

	
	
}
