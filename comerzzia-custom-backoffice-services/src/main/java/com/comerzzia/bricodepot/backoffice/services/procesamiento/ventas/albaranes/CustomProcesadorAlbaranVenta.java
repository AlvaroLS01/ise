package com.comerzzia.bricodepot.backoffice.services.procesamiento.ventas.albaranes;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;

import com.comerzzia.api.loyalty.client.LoyaltyOperationsApi;
import com.comerzzia.api.loyalty.client.model.CouponRedeemData;
import com.comerzzia.api.loyalty.client.model.LoyaltySaleOperation;
import com.comerzzia.api.loyalty.client.model.NewCoupon;
import com.comerzzia.bricodepot.backoffice.persistence.albaranes.venta.pagos.anticipos.Anticipos;
import com.comerzzia.bricodepot.backoffice.services.albaranes.ventas.cambioprecio.CambioPreciosAlbaranesServiceImpl;
import com.comerzzia.bricodepot.backoffice.services.anticipos.AnticiposServiceImpl;
import com.comerzzia.bricodepot.backoffice.services.conversion.ConversionServiceImpl;
import com.comerzzia.bricodepot.backoffice.services.general.clientes.BricodepotServicioClientesImpl;
import com.comerzzia.bricodepot.backoffice.services.procesamiento.notificaciones.BricodepotProcesadorNotificaciones;
import com.comerzzia.bricodepot.backoffice.services.ventas.codigopostal.CodigoPostalServiceImpl;
import com.comerzzia.bricodepot.backoffice.services.ventas.sco.acumuladorventas.ServicioAcumuladorVentasImpl;
import com.comerzzia.bricodepot.backoffice.services.ventas.tickets.BricodepotServicioTicketsImpl;
import com.comerzzia.bricodepot.backoffice.services.ventas.tickets.CustomTicketParser;
import com.comerzzia.bricodepot.backoffice.util.anticipos.AnticiposConstants;
import com.comerzzia.core.model.acciones.PeticionOperacion;
import com.comerzzia.core.model.acciones.operaciones.OperacionBean;
import com.comerzzia.core.model.contadores.ContadorBean;
import com.comerzzia.core.model.empresas.EmpresaBean;
import com.comerzzia.core.model.variables.VariableBean;
import com.comerzzia.core.model.ventas.tickets.TicketBean;
import com.comerzzia.core.model.ventas.tickets.TicketKey;
import com.comerzzia.core.persistencia.ventas.tickets.TicketMapper;
import com.comerzzia.core.servicios.acciones.estados.ControladorEstadosService;
import com.comerzzia.core.servicios.acciones.estados.OperacionEstadoException;
import com.comerzzia.core.servicios.acciones.estados.OperacionEstadoNoPermitidaException;
import com.comerzzia.core.servicios.acciones.operaciones.ServicioOperacionesImpl;
import com.comerzzia.core.servicios.api.errorhandlers.ApiException;
import com.comerzzia.core.servicios.contadores.ContadorException;
import com.comerzzia.core.servicios.contadores.ContadorNotFoundException;
import com.comerzzia.core.servicios.contadores.ServicioContadoresImpl;
import com.comerzzia.core.servicios.empresas.EmpresaException;
import com.comerzzia.core.servicios.empresas.ServicioEmpresasImpl;
import com.comerzzia.core.servicios.procesamiento.ProcesadorDocumentoException;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.servicios.sesion.IDatosSesion;
import com.comerzzia.core.servicios.variables.ServicioVariablesImpl;
import com.comerzzia.core.servicios.variables.VariableException;
import com.comerzzia.core.servicios.variables.VariableNotFoundException;
import com.comerzzia.core.servicios.ventas.tickets.ServicioTicketsImpl;
import com.comerzzia.core.servicios.ventas.tickets.TicketException;
import com.comerzzia.core.servicios.ventas.tickets.TicketNoProcesableException;
import com.comerzzia.core.servicios.ventas.tickets.TicketNotFoundException;
import com.comerzzia.core.servicios.ventas.tickets.TicketOrigenNotFoundException;
import com.comerzzia.core.util.base.Estado;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.core.util.fechas.Fecha;
import com.comerzzia.core.util.numeros.Numero;
import com.comerzzia.core.util.xml.XMLDocument;
import com.comerzzia.core.util.xml.XMLDocumentException;
import com.comerzzia.core.util.xml.XMLDocumentNode;
import com.comerzzia.model.general.almacenes.AlmacenBean;
import com.comerzzia.model.general.clientes.ClienteBean;
import com.comerzzia.model.general.paises.PaisBean;
import com.comerzzia.model.ventas.cupones.CuponBean;
import com.comerzzia.model.ventas.promociones.uso.PromocionUsoBean;
import com.comerzzia.model.versionado.VersionadoBean;
import com.comerzzia.persistencia.general.almacenes.AlmacenesDao;
import com.comerzzia.persistencia.general.clientes.ClientesDao;
import com.comerzzia.persistencia.general.paises.PaisesDao;
import com.comerzzia.servicios.fidelizacion.movimientos.MovimientoException;
import com.comerzzia.servicios.fidelizacion.movimientos.ServicioMovimientosImpl;
import com.comerzzia.servicios.general.almacenes.AlmacenNotFoundException;
import com.comerzzia.servicios.general.almacenes.ServicioAlmacenesImpl;
import com.comerzzia.servicios.general.clientes.ClienteConstraintViolationException;
import com.comerzzia.servicios.general.clientes.ClienteException;
import com.comerzzia.servicios.general.clientes.ServicioClientesImpl;
import com.comerzzia.servicios.procesamiento.ventas.albaranes.ProcesadorAlbaranVenta;
import com.comerzzia.servicios.ventas.albaranes.AlbaranVenta;
import com.comerzzia.servicios.ventas.albaranes.AlbaranVentaConstraintViolationException;
import com.comerzzia.servicios.ventas.albaranes.AlbaranVentaException;
import com.comerzzia.servicios.ventas.albaranes.ServicioAlbaranesVentasImpl;
import com.comerzzia.servicios.ventas.promociones.uso.PromocionUsoNotFoundException;
import com.comerzzia.servicios.ventas.promociones.uso.ServicioPromocionesUsoImpl;
import com.comerzzia.servicios.ventas.tickets.TicketParser;
import com.comerzzia.servicios.versionado.ServicioVersionadoImpl;
import com.comerzzia.servicios.versionado.VersionadoException;

import feign.FeignException;
import feign.RetryableException;

@SuppressWarnings("deprecation")
public class CustomProcesadorAlbaranVenta extends ProcesadorAlbaranVenta {

	private static String ID_CLASE_VENTAS_SII = "D_CLIE_ALBARANES_CAB_TBL.ID_CLIE_ALBARAN_SII";
	private static String ID_CONTADOR_VERSION_CLIE_ALBARAN_SII = "ID_CLIE_ALBARAN_SII";

	private String emailFidelizado;
	 
	@Override
	protected TicketParser crearTicketParser(Connection conn, DatosSesionBean datosSesion, TicketBean ticket, SqlSession sqlSession) {
		return new CustomTicketParser(conn, datosSesion, ticket, sqlSession);
	}

	@Override
	public boolean procesaDocumento(Connection conn, DatosSesionBean datosSesion, TicketBean ticket, SqlSession sqlSession) throws ProcesadorDocumentoException {
		log.debug("procesaDocumento()");
		
		try {
			/*
			 * Primero realizamos el proceso de generacion de notificacion con las comprobaciones de fidelizado para
			 * poder setearle al albaran la tarjeta de fidelizacion antes de realizar el insert
			 */
			emailFidelizado = ((BricodepotServicioTicketsImpl) ServicioTicketsImpl.get()).generateEmailNotification(datosSesion, BricodepotProcesadorNotificaciones.TIPO_NOTIFICACION_ENVIO_TICKET, ticket, sqlSession);
		}
		catch (Exception e1) {
			log.warn("procesaDocumentos() - Ha ocurrido un error: " + e1.getMessage(), e1);
		}
		
		
		try {
			log.debug("Conexion a base de datos abierta: "+!conn.isClosed());
			
			// Obtenemos el albarán a partir del XML del ticket
			TicketParser ticketParser = crearTicketParser(conn, datosSesion, ticket, sqlSession);
			AlbaranVenta albaran = ticketParser.parse();			
			
			AlmacenBean almacen = AlmacenesDao.consultar(conn, datosSesion.getConfigEmpresa(), albaran.getCodAlmacen());
			EmpresaBean empresa = ServicioEmpresasImpl.get().consultar(sqlSession, almacen.getCodEmp(), datosSesion.getConfigEmpresa().getUidActividad());
			PaisBean pais = PaisesDao.consultar(conn, datosSesion.getConfigEmpresa(), empresa.getCodPais());
			albaran.setCodDivisa(pais.getCodDivisa());
			
			establecerTratamientoCliente(conn, datosSesion, albaran, almacen, sqlSession);
									
			crearAlbaran(conn, datosSesion, albaran, sqlSession);
			
			if(!"NC".equals(ticket.getCodTipoDocumento())){
				generarServicio(sqlSession, datosSesion, albaran, ticket);
			}

			// ACTUALIZACIÓN DE USOS DE PROMOCIONES
			if (albaran.getPromocionesUso() !=null){
				SimpleDateFormat sdf = new SimpleDateFormat(Fecha.PATRON_FECHA_CORTA);
				for (PromocionUsoBean promocionUso: albaran.getPromocionesUso()){
					try {
						
						boolean lock = true;
						int numIntentos = 100;
						while(lock && numIntentos > 0) {
							PromocionUsoBean promocionUsoExistente = ServicioPromocionesUsoImpl.get().consultar(conn, promocionUso.getIdPromocion(), promocionUso.getIdClase(), promocionUso.getIdObjeto(), datosSesion.getConfigEmpresa());
							promocionUsoExistente.setEstadoBean(Estado.MODIFICADO);
							Date fechaAlbaran = Fecha.getFechaHora(sdf.format(albaran.getFecha()), albaran.getHora()).getDate();
							promocionUsoExistente.setFechaUltimoUso(fechaAlbaran);
							Integer newUses = promocionUsoExistente.getNumeroUsos() + promocionUso.getNumeroUsos();
							promocionUsoExistente.setImporteDescuento(Numero.redondea((promocionUsoExistente.getImporteDescuento() + promocionUso.getImporteDescuento()), 2));
							promocionUsoExistente.setImporteVenta(Numero.redondea((promocionUsoExistente.getImporteVenta() + promocionUso.getImporteVenta()), 2));
							lock = ServicioPromocionesUsoImpl.get().updateUse(conn, promocionUsoExistente, newUses, datosSesion.getConfigEmpresa()) != 1;
							if(lock) {
								numIntentos--;
								if(numIntentos < 80) {
									log.warn("procesaDocumento() - No se puede actualizar correctamente la promoción de uso. - idPromocion: "+promocionUsoExistente.getIdPromocion());
								}
							}
						}
						if(numIntentos == 0) {
							String msg = "No se puede actualizar el uso de la promoción. - idPromocion: "+promocionUso.getIdPromocion();
							log.error("procesaDocumento() - "+msg);
							throw new Exception(msg);
						}
					}
					catch(PromocionUsoNotFoundException ignore) {
						ServicioPromocionesUsoImpl.get().salvar(conn, promocionUso, datosSesion.getConfigEmpresa());
					}
					
				}				
			}
			
			LoyaltySaleOperation loyaltySaleOperation = new LoyaltySaleOperation();
			
			loyaltySaleOperation.setDate(ticket.getFecha());
			loyaltySaleOperation.setLockByTerminalId(ticket.getCodAlmacen()+ticket.getCodCaja());
			loyaltySaleOperation.setLoyalCustomerId(albaran.getIdFidelizado() != null ? albaran.getIdFidelizado() : 0L);
			loyaltySaleOperation.setStoreId(ticket.getCodAlmacen());
			loyaltySaleOperation.setTicketUid(ticket.getUidTicket());
			loyaltySaleOperation.setTillId(albaran.getUidDiarioCaja());

			Double puntosAcumulados = obtenerPuntosCliente(albaran);
			
			// ACTUALIZACIÓN DE CUPONES EMITIDOS
			if (albaran.getCuponesEmitidos() != null) {
				List<NewCoupon> newCoupons = new ArrayList<NewCoupon>();
				for (CuponBean cuponEmitido : albaran.getCuponesEmitidos()){
					NewCoupon newCoupon = new NewCoupon();
					newCoupon.setBalance(cuponEmitido.getImporteDescuento() != null ? new BigDecimal(cuponEmitido.getImporteDescuento().toString()) : BigDecimal.ZERO);
					newCoupon.setCouponCode(cuponEmitido.getCodCupon());
					newCoupon.setCouponDescription(cuponEmitido.getDescripcion());
					newCoupon.setCouponName(cuponEmitido.getCouponTitle());
					newCoupon.setCouponTypeCode(cuponEmitido.getCodtipcupon());
					newCoupon.setCustomerMaxUses(cuponEmitido.getNumeroMaxUsos());
					newCoupon.setEndDate(cuponEmitido.getFechaFin());
					newCoupon.setImageUrl(cuponEmitido.getImageUrl());
					newCoupon.setLoyalCustomerId(loyaltySaleOperation.getLoyalCustomerId());
					newCoupon.setPromotionId(cuponEmitido.getIdPromocionAplicacion());
					newCoupon.setStartDate(cuponEmitido.getFechaInicio());
	
					newCoupons.add(newCoupon);
				}
				
				loyaltySaleOperation.setNewCoupons(newCoupons);
				
				
			}
			
			// ACTUALIZACIÓN DE CUPONES APLICADOS
			if (albaran.getCuponesRedimidos() != null) {
				List<CouponRedeemData> redeemedCoupons = new ArrayList<CouponRedeemData>();
				for (CuponBean cuponRedimido : albaran.getCuponesRedimidos()){
					CouponRedeemData redeemedCoupon = new CouponRedeemData();
					redeemedCoupon.setCouponCode(cuponRedimido.getCodCupon());
					redeemedCoupon.setDiscount(cuponRedimido.getImporteDescuento() != null ? new BigDecimal(cuponRedimido.getImporteDescuento().toString()) : BigDecimal.ZERO);
					redeemedCoupon.setSaleAmount(albaran.getTotal() != null ? new BigDecimal(albaran.getTotal().toString()) : BigDecimal.ZERO);
					redeemedCoupons.add(redeemedCoupon);

				}
				loyaltySaleOperation.setReedemCoupons(redeemedCoupons);
			}
			actualizarMovimientosPuntosDevueltos(sqlSession, datosSesion, ticket, albaran, loyaltySaleOperation);
			
			crearMovimientoTipoPuntos(sqlSession, datosSesion, ticket, albaran,puntosAcumulados, loyaltySaleOperation);
			
			
			// MOVIMIENTO DE RECARGA DE GIFTCARD
			if (!isFsToFt(ticket.getTicket())){
				ServicioMovimientosImpl.get().modificarMovimientos(sqlSession, albaran.getNumeroTarjetaRegalo(), albaran.getUidTransaccionTarjetaRegalo(), datosSesion);
				confirmarMovimientosUsoTarjetaRegalo(sqlSession, datosSesion, albaran);
			}
			
			//Tratamiento de los documentos origen de las líneas
			if(!ticketParser.getLineasDocumentoOrigen().isEmpty()) {				
				Map<String, String> documentsNature = new HashMap<>();
				for(String uidDocumentoOrigen: ticketParser.getLineasDocumentoOrigen()) {
					TicketMapper ticketVCMapper = sqlSession.getMapper(TicketMapper.class);
					TicketKey ticketVCKey = new TicketKey();
					ticketVCKey.setUidActividad(datosSesion.getUidActividad());
					ticketVCKey.setUidTicket(uidDocumentoOrigen);
					
					TicketBean ticketVC = ticketVCMapper.selectFromViewByPrimaryKey(ticketVCKey);
					if(ticketVC != null) {
						if(!documentsNature.containsKey(ticketVC.getCodTipoDocumento())) {
							String documentNature = getDocumentNature(datosSesion, ticketVC);
							documentsNature.put(ticketVC.getCodTipoDocumento(), documentNature);
						}
						String documentNature = documentsNature.get(ticketVC.getCodTipoDocumento());
						if(documentNature.equals(DOCUMENT_NATURE_VC)) {
							OperacionBean operacionParaEjecutar = ServicioOperacionesImpl.get().consultarOperacion(sqlSession, ticketVC.getIdAccionEstados(), "FACTURAR");
							
							PeticionOperacion<TicketBean> peticionOperacionFacturarVC = new PeticionOperacion<TicketBean>(ticketVC, operacionParaEjecutar);
							peticionOperacionFacturarVC.setUsuario(datosSesion.getUsuario());
							ControladorEstadosService.get().ejecutarOperacion(datosSesion, sqlSession, peticionOperacionFacturarVC);
						}
					}
				}
			}
			
			if(albaran != null && StringUtils.isNotBlank(albaran.getUidTicketOrigen())){
				insertaDocumentoEnlaceDevolucion(sqlSession, datosSesion, ticket, albaran);
				
			}
			
			if(albaran != null && albaran.getArticulos() != null && albaran.getArticulos().isEmpty()) {
				procesarTicketVacio(conn, datosSesion, ticket, albaran);
			}
			
			if(loyaltySaleOperation != null && 
					(loyaltySaleOperation.getNewCoupons() != null && !loyaltySaleOperation.getNewCoupons().isEmpty()) || 
					(loyaltySaleOperation.getReedemCoupons() != null && !loyaltySaleOperation.getReedemCoupons().isEmpty()) ||
					(loyaltySaleOperation.getAccountActivities() != null && !loyaltySaleOperation.getAccountActivities().isEmpty())) {
				LoyaltyOperationsApi api = comerzziaApiManager.getClient(datosSesion, "LoyaltyOperationsApi");
				api.newLoyaltySaleOperation(loyaltySaleOperation);
			}
			
		}
		catch (TicketOrigenNotFoundException e) {
			throw new ProcesadorDocumentoException("Error procesando el documento: " + e.getMessage(), e);
		}
		catch (TicketException e) {
			throw new ProcesadorDocumentoException("Error procesando el documento: " + e.getMessage(), e);
		}
		catch(MovimientoException e){
			throw new ProcesadorDocumentoException("Error haciendo definitivo el movimiento de la tarjeta regalo del documento: " + e.getMessage(), e);
		}
		catch (SQLException e) {
			throw new ProcesadorDocumentoException("Error procesando el documento: " + e.getMessage(), e);
		}
		catch(OperacionEstadoNoPermitidaException e) {
			throw new ProcesadorDocumentoException("Error procesando el documento; " + e.getPeticionOperacion().getOperacion().getEjecucion() + " no esta permitida");
		}
		catch(OperacionEstadoException e) {
			throw new ProcesadorDocumentoException("Error procesando el documento: " + e.getPeticionOperacion().getResultadoOperacion().getMensaje());
		}
		catch(ApiException e) {
			throw new ProcesadorDocumentoException("Error procesando el documento: " + e.getMessage());			
		}
		catch(RetryableException e) {
			throw new TicketNoProcesableException("No hay conexión con la API "+e.getMessage());
		}
		catch(FeignException e) {
			throw new TicketNoProcesableException("No hay conexión con la API "+e.getMessage());
		}
		catch (Exception e) {
			throw new ProcesadorDocumentoException("Error procesando el documento: " + e.getMessage(), e);
		}
		
		// No marcamos como procesado hasta que el documento se haya procesado completamente.
		boolean procesado = false;
		try {
			CustomTicketParser ticketParser = new CustomTicketParser(conn, datosSesion, ticket, sqlSession);
			AlbaranVenta albaran = ServicioAlbaranesVentasImpl.get().consultar(conn, ticket.getUidTicket(), datosSesion.getConfigEmpresa());
//			albaran.setArticulos(ticketParser.parse().getArticulos());
//			ticketParser.guardarInformeDevolucion(albaran);
			log.debug("Procesando albaran id" +albaran.getIdAlbaran() +" codigo: "+albaran.getCodAlbaran()+ " e referencia cliente: "+albaran.getReferenciaCliente());
			String importeAnticipo = ticketParser.getImporteAnticipo();
			String numAnticipo = ticketParser.getNumAnticipo();
			String operacionAnticipo = ticketParser.getOperacionAnticipo();
			AnticiposServiceImpl anticiposService = AnticiposServiceImpl.get();
			Anticipos anticipo = null;
			if (StringUtils.isNotBlank(numAnticipo)) {
				anticipo = anticiposService.consultarAnticipo(sqlSession, datosSesion, numAnticipo);
				if (anticipo == null && operacionAnticipo.equals(AnticiposConstants.PARAMETRO_COBRAR_ANTICIPO) && StringUtils.isNotBlank(importeAnticipo) && StringUtils.isNotBlank(numAnticipo)) {
					log.debug("procesaDocumento() - Añadimos anticipo");
					anticiposService.añadirAnticipo(sqlSession, datosSesion, albaran.getIdAlbaran(), importeAnticipo, numAnticipo);
				}
				else if (anticipo != null && anticipo.getEstado().equals(AnticiposConstants.PARAMETRO_ESTADO_DISPONIBLE) && operacionAnticipo.equals(AnticiposConstants.PARAMETRO_PAGAR_ANTICIPO)
				        && StringUtils.isNotBlank(importeAnticipo) && StringUtils.isNotBlank(numAnticipo)) {
					log.debug("procesaDocumento() - Actualizamos el estado del Anticipo a " + AnticiposConstants.PARAMETRO_ESTADO_LIQUIDADO);
					anticiposService.actualizaEstadoAnticipo(sqlSession, datosSesion, anticipo, AnticiposConstants.PARAMETRO_ESTADO_LIQUIDADO);
				}
				else if (anticipo != null && anticipo.getEstado().equals(AnticiposConstants.PARAMETRO_ESTADO_DISPONIBLE) && operacionAnticipo.equals(AnticiposConstants.PARAMETRO_ESTADO_DEVUELTO)
				        && StringUtils.isNotBlank(importeAnticipo) && StringUtils.isNotBlank(numAnticipo)) {
					log.debug("procesaDocumento() - Actualizamos el estado del Anticipo a " + AnticiposConstants.PARAMETRO_ESTADO_DEVUELTO);
					anticiposService.actualizaEstadoAnticipo(sqlSession, datosSesion, anticipo, AnticiposConstants.PARAMETRO_ESTADO_DEVUELTO);
				}
			}
			// PROCESANDO CODIGO POSTAL
			if (ticketParser.getVentaCodigoPostal() != null) {
				CodigoPostalServiceImpl codigoPostalServiceImpl = CodigoPostalServiceImpl.get();
				codigoPostalServiceImpl.añadirVentaCodigoPostal(sqlSession, datosSesion, albaran.getIdAlbaran(), ticketParser.getVentaCodigoPostal());
			}
			
			// Obtenemos el albarán a partir del uidTicket
			// Solo versionamos SII en caso de que el pais de la empresa sea ES
			EmpresaBean empresa = ServicioEmpresasImpl.get().consultar(conn, albaran.getCodEmpresa(), datosSesion);
			if (empresa.getCodPais().equals("ES")) {
				crearVersionadoSII(conn, datosSesion, albaran);
			}
			
			if(ticket.getCodTipoDocumento().equals("FS") || ticket.getCodTipoDocumento().equals("FT")) {
				try {
					ServicioAcumuladorVentasImpl.get().cargarInformeAcumuladorVentas(ticket, sqlSession);
				}
				catch (Exception e) {
					log.error("procesaDocumento() - Error al cargar la informacion del informe acumulado de ventas: " + e.getMessage(), e);
				}
			}
			procesarTicketsConvertidosAFT(datosSesion,ticket,sqlSession,albaran,ticketParser);
			procesado = true;
		}
		catch (Exception e) {
			throw new ProcesadorDocumentoException("Error procesando el documento: " + e.getMessage(), e);
		}

		return procesado;
	}

	protected void procesarTicketsConvertidosAFT(DatosSesionBean datosSesion, TicketBean ticket, SqlSession sqlSession,AlbaranVenta albaran,CustomTicketParser ticketParser) throws XMLDocumentException {
		ConversionServiceImpl.get().tratarTicketConvertido(datosSesion, ticket, sqlSession,albaran,ticketParser);
	}

	protected void crearVersionadoSII(Connection conn, DatosSesionBean datosSesion, AlbaranVenta albaran) throws VersionadoException, ContadorException {
		// Versionamos albarán para sii
		VersionadoBean version = new VersionadoBean();
		version.setIdClase(ID_CLASE_VENTAS_SII);
		version.setIdObjeto(albaran.getIdAlbaran().toString());
		version.setVersion(obtenerContador(datosSesion));
		ServicioVersionadoImpl.get().versionar(conn, datosSesion, version);
	}

	protected Long obtenerContador(IDatosSesion datosSesion) throws ContadorException {
		return ServicioContadoresImpl.get().obtenerValorContador(datosSesion, ID_CONTADOR_VERSION_CLIE_ALBARAN_SII);
	}

	
	@Override
	protected void crearAlbaran(Connection conn, DatosSesionBean datosSesion, AlbaranVenta albaran, SqlSession sqlSession)
	        throws AlbaranVentaConstraintViolationException, AlbaranVentaException, VersionadoException, ContadorException {
			log.debug("crearAlbaran()");
			if (StringUtils.isNotBlank(emailFidelizado) && albaran.getTarjetaFidelizacion() == null) {
				try {
					((BricodepotServicioTicketsImpl) ServicioTicketsImpl.get()).tratamientoEmailEnvioTicket(emailFidelizado, datosSesion, albaran, conn, sqlSession);
				}
				catch (Exception e) {
					log.warn("crearAlbaran() - Ha ocurrido un error: " + e.getMessage(), e);
				}
			}
			log.debug("Creando albaran con numero: " + albaran.getNumAlbaran() + " y referencia cliente: " + albaran.getReferenciaCliente());
			super.crearAlbaran(conn, datosSesion, albaran, sqlSession); 
			insertarCambiosPreciosPromocionesManualCupones(datosSesion, sqlSession, albaran);
	}

	protected void insertarCambiosPreciosPromocionesManualCupones(DatosSesionBean datosSesion, SqlSession sqlSession, AlbaranVenta albaran) {
		log.debug("insertarCambiosPreciosPromocionesManualCupones() - insertando cambios de precio por linea para el albaran : " + albaran.getIdAlbaran());
		CambioPreciosAlbaranesServiceImpl.get().insertarCambiosPreciosPromocionesManualCupones(datosSesion, sqlSession, albaran);
	}
	

	private boolean isFsToFt(byte[] ticket) {
		try {
			XMLDocumentNode tagFsToFt = new XMLDocument(ticket).getRoot().getNodo("cabecera").getNodo("conversion_fs_ft", true);
			if (tagFsToFt != null) {
				return true;
			}
		}
		catch (Exception e) {
			log.error("isFsToFt() - Error al identificar si es una conversión de fs " + e.getMessage(), e);
		}
		return false;
	}
	
	/*
	 * Copiado del estándar salvo que versiona el cliente
	 */
	protected void establecerTratamientoCliente(Connection conn, DatosSesionBean datosSesion, AlbaranVenta albaran, AlmacenBean almacen, SqlSession sqlSession) throws SQLException, ClienteException,
	        ClienteConstraintViolationException, VariableException, VariableNotFoundException, ContadorException, ContadorNotFoundException, TicketNotFoundException, AlmacenNotFoundException {

		/*
		 * TRATAMIENTO DE CLIENTE
		 * -----------------------
		 * 
		 *  Primero se comprobará la existencia del cliente; se realizará una consulta a través del código de cliente. En caso de no existir,
		 *  se dará de alta el cliente. Los posibles datos de facturación prevalecen sobre los datos del cliente. Una vez dado de alta, se asigna
		 *  el código de cliente generado al albarán.
		 *  
		 *  Si al realizar la consulta de cliente este ya se encuentra, se comprueba si el cliente es genérico o no. Esta comprobación se realiza comparando
		 *  el código de cliente que se está consultando con el código de cliente que está asociado al almacén.
		 *  
		 *  Si el cliente no es genérico, se actualizan los datos de dicho cliente con los datos que vienen en el documento prevaleciendo los posibles
		 *  datos de facturación.
		 *  
		 *  Si el cliente es genérico y no tiene datos de facturación, no se realiza ninguna acción.
		 *  
		 *  Si el cliente es genérico y tiene datos de facturación, buscaremos por tipo de documento, país y cif la existencia del cliente. Si no
		 *  se encuentra ningún cliente con dichos datos, se asigna un código de cliente y se da de alta. Si se encuentra el cliente, se actualizan los datos
		 *  con los que aparecen en el documento (prevaleciendo los datos de facturación) y se asigna el código de cliente al albarán.
		 * 
		 */

		// Comprobamos si existe el cliente usando el código de cliente
		ClienteBean cliente = ClientesDao.consultar(conn, datosSesion.getConfigEmpresa(), albaran.getCodCliente());

		// Si el cliente no existe, lo damos de alta. Los datos de facturación prevalecen para los datos del cliente.
		if (cliente == null) {
			albaran.getCliente().setEstadoBean(Estado.NUEVO);
			albaran.getCliente().setActivo(true);
			ServicioClientesImpl.get().crear(conn, albaran.getCliente(), datosSesion);
		}
		// El cliente existe.
		else {
			// Si tiene documento origen, necesitamos el cliente del almacén para poder hacer la comprobación del
			// cliente genérico
			String uidDocumentoOrigen = albaran.getUidTicketOrigen();
			boolean condUidDocumentoOrigen = true;
			if (uidDocumentoOrigen != null) {
				TicketMapper ticketVCMapper = sqlSession.getMapper(TicketMapper.class);
				TicketKey ticketVCKey = new TicketKey();
				ticketVCKey.setUidActividad(datosSesion.getUidActividad());
				ticketVCKey.setUidTicket(uidDocumentoOrigen);
				TicketBean ticketOrigen = ticketVCMapper.selectFromViewByPrimaryKey(ticketVCKey);
				if (ticketOrigen == null) {
					String msg = "No se ha encontrado el ticket origen con identificador: " + uidDocumentoOrigen;
					log.debug("establecerTratamientoCliente() - " + msg);
					throw new TicketNotFoundException(msg);
				}
				String codalmacenOrigen = ticketOrigen.getCodAlmacen();
				AlmacenBean almacenOrigen = ServicioAlmacenesImpl.get().consultar(conn, codalmacenOrigen, datosSesion.getConfigEmpresa());
				String codClienteAlmOrigen = almacenOrigen.getCodCliente();
				condUidDocumentoOrigen = codClienteAlmOrigen != null && !codClienteAlmOrigen.equals(albaran.getCodCliente());

			}
			// Se comprueba si el cliente es el genérico o no.
			// El cliente no es el genérico del almacén. Se actualizan los datos.
			if (!almacen.getCodCliente().equals(albaran.getCodCliente()) && condUidDocumentoOrigen) {
				completarDatosClienteTicket(albaran.getCliente(), cliente);
				ClientesDao.update(conn, datosSesion.getConfigEmpresa(), cliente);
			}
			// El cliente es el genérico del almacén.
			else {
				// Si el documento genera factura.
				if (albaran.isGenerarFactura()) {
					if (albaran.getCliente().getCodTipoIden() != null && !albaran.getCliente().getCodTipoIden().isEmpty()) {
						cliente = ClientesDao.consultarPorPaisCifTipoIden(conn, datosSesion.getConfigEmpresa(), albaran.getCliente().getCodPais(), albaran.getCliente().getCif(),
						        albaran.getCliente().getCodTipoIden());
						if (cliente == null) {
							cliente = ClientesDao.consultarPorPaisCif(conn, datosSesion.getConfigEmpresa(), albaran.getCliente().getCodPais(), albaran.getCliente().getCif());
						}
					}
					else {
						cliente = ClientesDao.consultarPorPaisCif(conn, datosSesion.getConfigEmpresa(), albaran.getCliente().getCodPais(), albaran.getCliente().getCif());
					}

					if (cliente != null) {
						// Sólo se actualizan los datos del cliente si NO es el genérico de la tienda.
						if (!almacen.getCodCliente().equals(cliente.getCodCli())) {
							if (albaran.getCliente().getCodTipoIden() != null && !albaran.getCliente().getCodTipoIden().isEmpty()) {
								cliente.setCodTipoIden(albaran.getCliente().getCodTipoIden());
							}
							albaran.getCliente().setCodCli(cliente.getCodCli());

							completarDatosClienteTicket(albaran.getCliente(), cliente);
							ClientesDao.update(conn, datosSesion.getConfigEmpresa(), cliente);
							versionarClienteFactura(conn, datosSesion, cliente);
						}
						albaran.setCodCliente(cliente.getCodCli());
						if (cliente.getCodcliFactura() != null && !cliente.getCodcliFactura().isEmpty()) {
							albaran.setCodcliFactura(cliente.getCodcliFactura());
						}
						else {
							albaran.setCodcliFactura(cliente.getCodCli());
						}
					}
					else {
						DatosSesionBean datosSesionBean = new DatosSesionBean();
						try {
							datosSesionBean.setUidActividad(datosSesion.getConfigEmpresa().getUidActividad());
						}
						catch (EmpresaException e) {
							throw new RuntimeException(e);
						}
						VariableBean vPrefijoAltaCliente = ServicioVariablesImpl.get().consultar(datosSesionBean, sqlSession, "CLIENTES.PREFIJO_ALTA");
						Map<String, String> parametrosContador = new HashMap<String, String>();
						parametrosContador.put("PREFIJO", vPrefijoAltaCliente.getValor());

						ContadorBean codCliente = ServicioContadoresImpl.get().obtenerContador(conn, datosSesion, "CODCLI", parametrosContador);
						albaran.getCliente().setCodCli(codCliente.getValorFormateado());
						albaran.setCodCliente(codCliente.getValorFormateado());
						albaran.setCodcliFactura(codCliente.getValorFormateado());

						albaran.getCliente().setEstadoBean(Estado.NUEVO);
						albaran.getCliente().setActivo(true);
						BricodepotServicioClientesImpl.get().crear(conn, albaran.getCliente(), datosSesion.getConfigEmpresa());
					}
				}
			}
		}
	}

	private void versionarClienteFactura(Connection conn, DatosSesionBean datosSesion, ClienteBean cliente) {
		try {
			BricodepotServicioClientesImpl.get().versionar(conn, datosSesion, cliente.getCodCli());
		}
		catch (Exception e) {
			log.error("versionarClienteFacturar() - Ha habido un error al versionar el cliente de la factura: " + e.getMessage(), e);
		}
	}

}