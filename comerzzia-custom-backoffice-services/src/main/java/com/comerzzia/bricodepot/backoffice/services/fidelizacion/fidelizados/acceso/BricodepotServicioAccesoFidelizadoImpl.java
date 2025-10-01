package com.comerzzia.bricodepot.backoffice.services.fidelizacion.fidelizados.acceso;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.model.fidelizacion.fidelizados.acceso.AccesoFidelizadoBean;
import com.comerzzia.servicios.fidelizacion.fidelizados.FidelizadoNotFoundException;
import com.comerzzia.servicios.fidelizacion.fidelizados.acceso.AccesoFidelizadoException;
import com.comerzzia.servicios.fidelizacion.fidelizados.acceso.ServicioAccesoFidelizadoImpl;

@Primary
@Service
public class BricodepotServicioAccesoFidelizadoImpl extends ServicioAccesoFidelizadoImpl {

	@Override
	public void crear(AccesoFidelizadoBean fidelizado, DatosSesionBean datosSesion) throws FidelizadoNotFoundException {
		org.apache.ibatis.session.SqlSession sqlSession = datosSesion.getSqlSessionFactory().openSession();
		Connection conn = new Connection(sqlSession.getConnection());

		try {
			log.debug("crear() - Creando nuevo Acceso para un Fidelizado");

			crear(fidelizado, datosSesion, conn);
			// LUSTRUM-137086: Se evita la generación del correo NUEVA_ALTA_FIDELIZADO que
			// no se debe enviar, para evitar que bloquee la cola de correo
			// sendNotificacionNuevoUsuario(fidelizado, datosSesion, sqlSession, true);

			sqlSession.commit();
		} catch (Exception e) {
			log.debug("crear() - deshaciendo transacción");
			conn.deshacerTransaccion();

			String msg = "Error creando el acceso para el fidelizado: " + e.getMessage();
			log.error("crear() - " + msg);

			throw new AccesoFidelizadoException(msg, e);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public void modificar(AccesoFidelizadoBean fidelizado, DatosSesionBean datosSesion) {
		org.apache.ibatis.session.SqlSession sqlSession = datosSesion.getSqlSessionFactory().openSession();

		try {
			log.debug("modificar() - Modificando el acceso para un fidelizado");

			actualizarDatosAccesoFidelizado(sqlSession, datosSesion, fidelizado);
			// LUSTRUM-137086: Se evita la generación del correo NUEVA_ALTA_FIDELIZADO que
			// no se debe enviar, para evitar que bloquee la cola de correo
			// sendNotificacionNuevoUsuario(fidelizado, datosSesion, sqlSession, false);

			sqlSession.commit();
		} catch (Exception e) {

			String msg = "Error creando el acceso para el fidelizado: " + e.getMessage();
			log.error("modificar() - " + msg);

			throw new AccesoFidelizadoException(msg, e);
		} finally {
			sqlSession.close();
		}
	}
}