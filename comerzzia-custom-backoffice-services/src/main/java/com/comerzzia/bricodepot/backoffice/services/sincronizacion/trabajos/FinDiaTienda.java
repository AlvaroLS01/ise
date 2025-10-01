package com.comerzzia.bricodepot.backoffice.services.sincronizacion.trabajos;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.comerzzia.bricodepot.backoffice.services.ventas.cajas.BricodepotServicioCajasImpl;
import com.comerzzia.bricodepot.backoffice.services.ventas.informetto.ServicioVentasInformeTto;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.mybatis.session.SqlSession;
import com.comerzzia.model.general.tiendas.cajas.TiendaCajaBean;
import com.comerzzia.servicios.general.tiendas.cajas.ServicioCajasTiendasImpl;


public class FinDiaTienda implements Job {

	protected static Logger log = Logger.getLogger(FinDiaTienda.class);

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
			BricodepotServicioCajasImpl.get().cerrarCajaFuerteYProsegur(datosSesion, sqlSession, fechaApertura, codAlm);
			insertInformeTtoVenta(codAlm, uidActividad, fechaApertura, datosSesion, sqlSession);
			sqlSession.commit();
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
	
	private void crearInformeTtoVentasDia(String codalm, String codcaja, String uidActividad, Date fecha, SqlSession sqlSession) {
		log.debug("crearInformeTtoVentasDia() - Creando informe Tto Ventas del dia");
		try {
			LocalDate localDate = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String formattedDate = localDate.format(formatter);

			// Actualizamos el informe que se generó en la apertura de tienda
			ServicioVentasInformeTto.get().actualizarInformeTtoVenta(uidActividad, codalm, codcaja, formattedDate, sqlSession);

		}
		catch (Exception e) {
			log.error("crearInformeTtoVentasDia() - Error creando informe Tto Ventas del dia");
		}
	}

	private void insertInformeTtoVenta(String codalm, String uidActividad, Date fecha, DatosSesionBean datosSesion, SqlSession sqlSession) {
		try {
			List<TiendaCajaBean> cajasAlmacen = ServicioCajasTiendasImpl.get().consultar(codalm, datosSesion);

			for (TiendaCajaBean caja : cajasAlmacen) {
				crearInformeTtoVentasDia(caja.getCodAlm(), caja.getCodCaja(), uidActividad, fecha, sqlSession);
			}
		}
		catch (Exception e) {
			log.error("insertInformeTtoVenta() - Eror al insertar el informe Tto venta: " + e.getMessage(), e);
		}
	}
}
