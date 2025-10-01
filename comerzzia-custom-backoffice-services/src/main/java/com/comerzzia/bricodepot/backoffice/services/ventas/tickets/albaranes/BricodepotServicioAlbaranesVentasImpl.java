package com.comerzzia.bricodepot.backoffice.services.ventas.tickets.albaranes;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.comerzzia.bricodepot.backoffice.persistence.albaranes.AlbaranCabecera;
import com.comerzzia.bricodepot.backoffice.persistence.albaranes.AlbaranCabeceraMapper;
import com.comerzzia.bricodepot.backoffice.persistence.albaranes.venta.informepromocion.InformePromociones;
import com.comerzzia.bricodepot.backoffice.persistence.albaranes.venta.presupuestos.PresupuestoVenta;
import com.comerzzia.bricodepot.backoffice.persistence.albaranes.venta.presupuestos.PresupuestoVentaMapper;
import com.comerzzia.bricodepot.backoffice.persistence.devoluciones.informe.InformeDevolucion;
import com.comerzzia.bricodepot.backoffice.persistence.devoluciones.informe.InformeDevolucionMapper;
import com.comerzzia.bricodepot.backoffice.persistence.devoluciones.validacion.ValidacionDevolucionAlbaran;
import com.comerzzia.bricodepot.backoffice.persistence.motivos.datos.MotivosDatos;
import com.comerzzia.bricodepot.backoffice.persistence.motivos.datos.MotivosDatosMapper;
import com.comerzzia.bricodepot.backoffice.persistence.tickets.tipoImpresion.TicketTipoImpresion;
import com.comerzzia.bricodepot.backoffice.persistence.tickets.tipoImpresion.TicketTipoImpresionMapper;
import com.comerzzia.bricodepot.backoffice.services.albaranes.ventas.cambioprecio.CambioPreciosAlbaranesServiceImpl;
import com.comerzzia.bricodepot.backoffice.services.devoluciones.validacion.ValidacionDevolucionException;
import com.comerzzia.bricodepot.backoffice.services.devoluciones.validacion.ValidacionDevolucionesServiceImpl;
import com.comerzzia.bricodepot.backoffice.services.ventas.tickets.CustomTicketParser;
import com.comerzzia.core.model.empresas.ConfigEmpresaBean;
import com.comerzzia.core.servicios.empresas.EmpresaException;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.core.util.xml.XMLDocumentException;
import com.comerzzia.model.ventas.albaranes.articulos.ArticuloAlbaranVentaBean;
import com.comerzzia.servicios.ventas.albaranes.AlbaranVenta;
import com.comerzzia.servicios.ventas.albaranes.AlbaranVentaConstraintViolationException;
import com.comerzzia.servicios.ventas.albaranes.AlbaranVentaException;
import com.comerzzia.servicios.ventas.albaranes.ServicioAlbaranesVentasImpl;

@SuppressWarnings({"unchecked","deprecation"})
public class BricodepotServicioAlbaranesVentasImpl extends ServicioAlbaranesVentasImpl {

	protected static Logger log = Logger.getLogger(BricodepotServicioAlbaranesVentasImpl.class);

	protected static BricodepotServicioAlbaranesVentasImpl instance;

	public static BricodepotServicioAlbaranesVentasImpl get() {
		if (instance == null) {
			instance = new BricodepotServicioAlbaranesVentasImpl();
		}
		return instance;
	}

	public static void setCustomInstance(BricodepotServicioAlbaranesVentasImpl instance) {
		BricodepotServicioAlbaranesVentasImpl.instance = instance;
	}

	public void modificarTarjetaFidelizacion(Long idAlbaran, String tarjetaFidelizacion, String uidActividad, SqlSession sqlSession)
	        throws AlbaranVentaConstraintViolationException, AlbaranVentaException, SQLException {
		log.debug("modificar() - Modificando albarán de venta " + idAlbaran);

		AlbaranCabeceraMapper albaranCabeceraMapper = sqlSession.getMapper(AlbaranCabeceraMapper.class);

		AlbaranCabecera albaranCabecera = new AlbaranCabecera();
		albaranCabecera.setIdClieAlbaran(idAlbaran);
		albaranCabecera.setUidActividad(uidActividad);
		albaranCabecera.setTarjetaFidelizacion(tarjetaFidelizacion);

		albaranCabeceraMapper.updateByPrimaryKeySelective(albaranCabecera);
	}

	@Override
	public void crear(AlbaranVenta albaran, ConfigEmpresaBean configEmpresa, Connection conn)throws AlbaranVentaConstraintViolationException, AlbaranVentaException {
		log.debug("crear()");
		super.crear(albaran, configEmpresa, conn);
		log.debug("Se ha creado el abaran con id: "+albaran.getIdAlbaran()+ " y con referencia cliente: "+albaran.getReferenciaCliente());
		DatosSesionBean datosSesion = new DatosSesionBean();
		try {
			datosSesion.setUidActividad(configEmpresa.getUidActividad());
		}
		catch (EmpresaException e) {
			log.error("Ha ocurrido un error al insertar datos en las tablas personalizadas de albaranes: " + e.getMessage());
			throw new AlbaranVentaException("Ha ocurrido un error al insertar datos en las tablas personalizadas: " + e.getMessage());
		}
		
		crearXAlbaran(albaran, datosSesion, conn);
		crearInformeDevolucion(albaran, datosSesion, conn);
		insertarMotivosDatos(albaran, datosSesion, conn);
		insertarTicketTipoImpresion(albaran, datosSesion, conn);
		insertarNumPresupuesto(albaran, datosSesion, conn);
		insertarPromocionesInforme(albaran, datosSesion, conn);
	}

	private void crearXAlbaran(AlbaranVenta albaran, DatosSesionBean datosSesion, Connection conn) throws AlbaranVentaException {
		/* BRICO-307 Validación de devoluciones en BO GAP-78 */
		log.debug("crearXAlbaran()");
		try {
			if (albaran.getBean() != null && albaran.getBean().getExtension("idSupervisorDevolucion") != null && !albaran.getArticulos().isEmpty()) {
				log.debug("crearXAlbaran() - Se trata de una devolucion");
				for (ArticuloAlbaranVentaBean articulo : albaran.getArticulos()) {
					ValidacionDevolucionAlbaran validacion = new ValidacionDevolucionAlbaran();
					validacion.setUidActividad(datosSesion.getUidActividad());
					validacion.setIdClieAlbaran(albaran.getIdAlbaran());
					validacion.setLinea(articulo.getLinea());

					validacion.setIdUsuarioSupervisor((Long) albaran.getBean().getExtension("idSupervisorDevolucion"));

					validacion.setValidado(Boolean.FALSE);
					validacion.setUidTicket(albaran.getBean().getUidTicket());

					log.debug("crearXAlbaran() - uidActividad: " + validacion.getUidActividad());
					log.debug("crearXAlbaran() - idClieAlbaran: " + validacion.getIdClieAlbaran());
					log.debug("crearXAlbaran() - linea: " + validacion.getLinea());
					log.debug("crearXAlbaran() - validado: " + validacion.getValidado());
					log.debug("crearXAlbaran() - uidTicket: " + validacion.getUidTicket());
					log.debug("crearXAlbaran() - idUsuarioSupervisor: " + validacion.getIdUsuarioSupervisor());

					ValidacionDevolucionesServiceImpl.get().crear(validacion, datosSesion);
				}

			}
		}
		catch (ValidacionDevolucionException e) {
			log.error("crearXAlbaran() - Ha ocurrido un error al realizar la insercion en la tabla personalizada de albaranes: " + e.getMessage());
			throw new AlbaranVentaException("Ha ocurrido un error al realizar la insercion en la tabla personalizada de albaranes: " + e.getMessage());
		}
		/* fin BRICO-307 */

	}

	private void crearInformeDevolucion(AlbaranVenta albaran, DatosSesionBean datosSesion, Connection conn) {
		log.debug("crearInformeDevolucion()");
		SqlSession sqlSession = null;
		try {
			if (albaran.getBean() != null && albaran.getBean().getExtension("listaInformesDevolucion") != null) {
				sqlSession = datosSesion.getSqlSessionFactory().openSession();
				InformeDevolucionMapper mapper = sqlSession.getMapper(InformeDevolucionMapper.class);

				List<InformeDevolucion> listaInformes = (List<InformeDevolucion>) albaran.getBean().getExtension("listaInformesDevolucion");
				for (InformeDevolucion informeDevolucion : listaInformes) {
					informeDevolucion.setIdClieAlbaran(albaran.getIdAlbaran());
					mapper.insert(informeDevolucion);
				}

				sqlSession.commit();
			}
		}
		catch (Exception ignore) {
		}
		finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
	}
	
	private void insertarMotivosDatos(AlbaranVenta albaran, DatosSesionBean datosSesion, Connection conn) {
		log.debug("insertarMotivosDatos()");
		SqlSession sqlSession = null;
		try {
			if (albaran.getBean() != null && albaran.getBean().getExtension("listaMotivosDatos") != null) {
				sqlSession = datosSesion.getSqlSessionFactory().openSession();
				MotivosDatosMapper mapper = sqlSession.getMapper(MotivosDatosMapper.class);

				List<MotivosDatos> listaMotivosDatos = (List<MotivosDatos>) albaran.getBean().getExtension("listaMotivosDatos");
				for (MotivosDatos motivoDato : listaMotivosDatos) {
//					motivoDato.setCodAlbaran(albaran.getIdAlbaran());
					mapper.insert(motivoDato);
				}

				sqlSession.commit();
			}
		}
		catch (Exception ignore) {
		}
		finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
	}

//	private void insertarTicketTipoImpresion(AlbaranVenta albaran, DatosSesionBean datosSesion, Connection conn) {
//		if (albaran.getBean() != null && albaran.getBean().getExtension("ticketTipoImpresion") != null) {
//			log.debug("insertarTicketTipoImpresion() - Insertando tipo de impresion de ticket");
//			TicketTipoImpresion ticketTipoImpresion = (TicketTipoImpresion) albaran.getBean().getExtension("ticketTipoImpresion");
//			TicketTipoImpresionServiceImpl.get().insertarTicketTipoImpresion(ticketTipoImpresion);
//		}
//	}
	
	//LUSTRUM-123123
	private void insertarTicketTipoImpresion(AlbaranVenta albaran, DatosSesionBean datosSesion, Connection conn) {
		SqlSession sqlSession = null;
		try {
			if (albaran.getBean() != null && albaran.getBean().getExtension("ticketTipoImpresion") != null) {
				TicketTipoImpresion ticketTipoImpresion = (TicketTipoImpresion) albaran.getBean().getExtension("ticketTipoImpresion");
				log.debug("insertarTicketTipoImpresion() - Insertando tipo de impresion de ticket");
				sqlSession = datosSesion.getSqlSessionFactory().openSession();
				TicketTipoImpresionMapper mapper = sqlSession.getMapper(TicketTipoImpresionMapper.class);
				mapper.insert(ticketTipoImpresion);
				sqlSession.commit();
			} 
		} catch (Exception e) {
	    	 sqlSession.rollback();
	    	 log.error("insertarTicketTipoImpresion() - Ha ocurrido un error insertando el TicketTipoImpresion: "+e.getMessage(), e);
		}
		finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
}
	
	
	private void insertarNumPresupuesto(AlbaranVenta albaran, DatosSesionBean datosSesion, Connection conn) {
	    SqlSession sqlSession = null;
	    try {
	        if (albaran.getBean() != null && albaran.getBean().getExtension(CustomTicketParser.NUM_PRESUPUESTO) != null) {
	            PresupuestoVenta presupuestoVenta = (PresupuestoVenta) albaran.getBean().getExtension(CustomTicketParser.NUM_PRESUPUESTO);
	            log.debug("insertarNumPresupuesto() - Insertando numero de presupuesto: " + presupuestoVenta.getIdPresupuesto());
	            presupuestoVenta.setIdClieAlbaran(albaran.getIdAlbaran());
	            sqlSession = datosSesion.getSqlSessionFactory().openSession();
	            PresupuestoVentaMapper mapper = sqlSession.getMapper(PresupuestoVentaMapper.class);
	            mapper.insert(presupuestoVenta);
	            sqlSession.commit();
	        }
	    } catch (Exception e) {
	    	 sqlSession.rollback();
	    	log.error("insertarNumPresupuesto() - Ha ocurrido un error insertando el presupuesto: "+e.getMessage(), e);
		}
		finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
	}
	
	private void insertarPromocionesInforme(AlbaranVenta albaran, DatosSesionBean datosSesion, Connection conn) {
		log.debug("insertarPromocionesInforme() - Relizando llamada a servicio para insertar la lista");
		if (albaran.getBean() != null && albaran.getBean().getExtension("listaPromocionesInforme") != null) {
			List<InformePromociones> listaPromocionesInformes = (List<InformePromociones>) albaran.getBean().getExtension("listaPromocionesInforme");
			try {
				CambioPreciosAlbaranesServiceImpl.get().insertarCambioPrecioPromocionCabeceraInforme(datosSesion, listaPromocionesInformes);
			}
			catch (XMLDocumentException e) {
				log.error("insertarPromocionesInforme() - Error llamando al servicio para insertar la lista de promociones informes");
			}
		}
	}
}
