package com.comerzzia.bricodepot.backoffice.services.sincronizacion.trabajos;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.comerzzia.bricodepot.backoffice.services.ventas.cajas.BricodepotServicioCajasImpl;
import com.comerzzia.bricodepot.backoffice.services.ventas.cajas.CajasFicticiasException;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.mybatis.session.SqlSession;

public class InicioDiaTienda implements Job {

	protected static Logger log = Logger.getLogger(InicioDiaTienda.class);

	@SuppressWarnings("deprecation")
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap dataMap = context.getMergedJobDataMap();
		SqlSession sqlSession = null;
		try {
			String uidInstancia = dataMap.getString("uidInstancia");
			if (uidInstancia == null) {
				throw new JobExecutionException("Error al ejecutar la tarea de envío de ventas-devoluciones: " + "No se ha recibido el parámetro con el UID_INSTANCIA");
			}

			String uidActividad = dataMap.getString("uidActividad");
			if (uidActividad == null) {
				throw new JobExecutionException("Error al ejecutar la tarea de envío de ventas-devoluciones: " + "No se ha recibido el parámetro con el UID_ACTIVIDAD");
			}

			// Recibimos como parámetro el almacén a sincronizar
			String codAlm = dataMap.getString("codAlmacen");
			if (codAlm == null) {
				throw new JobExecutionException("Error al ejecutar la tarea de envío de ventas-devoluciones: " + "No se ha recibido el parámetro con el almacén de destino");
			}
			DatosSesionBean datosSesion = new DatosSesionBean();
			datosSesion.setUidActividad(uidActividad);
			datosSesion.setUidInstancia(uidInstancia);
			sqlSession = new SqlSession();
			sqlSession.openSession(datosSesion.getSqlSessionFactory().openSession());
			Date fechaApertura = new Date();
			BricodepotServicioCajasImpl.get().abrirCajaFuerteYProsegur(datosSesion, sqlSession, fechaApertura, codAlm);
			sqlSession.commit();

		}
		catch (CajasFicticiasException e) {
			sqlSession.rollback();
			log.error("execute() - Ha ocurrido un error: " + e.getMessage(), e);
			dataMap.put("exception", e);
		}
		catch (Exception e) {
			sqlSession.rollback();
			log.error("execute() - Ha ocurrido un error: " + e.getMessage(), e);
			dataMap.put("exception", e);
		}
		catch (Throwable e) {
			sqlSession.rollback();
			log.error("execute() - Ha ocurrido un error: " + e.getMessage(), e);
			dataMap.put("exception", e);
		}
		finally {
			sqlSession.close();
		}
	}

}
