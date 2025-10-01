package com.comerzzia.bricodepot.backoffice.services.albaranes.ventas.cambioprecio;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.comerzzia.api.loyalty.client.CouponsApi;
import com.comerzzia.api.loyalty.client.model.CouponDTO;
import com.comerzzia.api.loyalty.client.model.CouponUse;
import com.comerzzia.bricodepot.backoffice.persistence.albaranes.venta.cambioprecio.AlbaranVentasCambiosPrecios;
import com.comerzzia.bricodepot.backoffice.persistence.albaranes.venta.cambioprecio.AlbaranVentasCambiosPreciosMapper;
import com.comerzzia.bricodepot.backoffice.persistence.albaranes.venta.informepromocion.InformePromociones;
import com.comerzzia.bricodepot.backoffice.persistence.albaranes.venta.informepromocion.InformePromocionesMapper;
import com.comerzzia.bricodepot.backoffice.persistence.promociones.CabeceraPromociones;
import com.comerzzia.bricodepot.backoffice.persistence.promociones.detalle.DetallePromociones;
import com.comerzzia.bricodepot.backoffice.persistence.promociones.tipos.TiposPromociones;
import com.comerzzia.bricodepot.backoffice.services.promociones.BricodepotServicioPromociones;
import com.comerzzia.core.model.ventas.tickets.TicketBean;
import com.comerzzia.core.servicios.ContextHolder;
import com.comerzzia.core.servicios.api.ComerzziaApiManager;
import com.comerzzia.core.servicios.contadores.ServicioContadoresImpl;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.config.ComerzziaApp;
import com.comerzzia.core.util.numeros.BigDecimalUtil;
import com.comerzzia.core.util.xml.XMLDocumentException;
import com.comerzzia.core.util.xml.XMLDocumentUtils;
import com.comerzzia.model.general.marketing.acciones.CampAccMarketingBean;
import com.comerzzia.model.ventas.albaranes.AlbaranVentaBean;
import com.comerzzia.model.ventas.albaranes.articulos.ArticuloAlbaranVentaBean;
import com.comerzzia.model.ventas.albaranes.descuentos.DetalleDescuentoAlbaranVentaBean;
import com.comerzzia.servicios.general.marketing.acciones.CampAccMarketingNotFoundException;
import com.comerzzia.servicios.general.marketing.acciones.ServicioCampAccMarketingImpl;
import com.comerzzia.servicios.general.marketing.acciones.enl.ServicioCampAccEnlMarketingImpl;
import com.comerzzia.servicios.ventas.albaranes.AlbaranVenta;
import com.comerzzia.servicios.ventas.albaranes.ServicioAlbaranesVentasImpl;
import com.comerzzia.servicios.ventas.promociones.Promocion;
import com.comerzzia.servicios.ventas.promociones.ServicioPromocionesImpl;

@SuppressWarnings("deprecation")
public class CambioPreciosAlbaranesServiceImpl {

	protected static Logger log = Logger.getLogger(CambioPreciosAlbaranesServiceImpl.class);

	protected static CambioPreciosAlbaranesServiceImpl instance;
	
	protected ComerzziaApiManager comerzziaApiManager;
	
	public static final String CONTADOR_PRECIO = "CAMBIO_PRECIO";
	
	public static final String COD_CAJA_88 ="88";

	public static CambioPreciosAlbaranesServiceImpl get() {
		if (instance == null) {
			instance = new CambioPreciosAlbaranesServiceImpl();
		}
		return instance;
	}

	public void insertarCambiosPreciosPromocionesManualCupones(DatosSesionBean datosSesion, SqlSession sqlSession, AlbaranVenta albaran) {
		log.debug("insertarCambiosPreciosPromocionesManualCupones() - Insertando detalle de cambio de precios para el albaran : " + albaran.getIdAlbaran());
		List<ArticuloAlbaranVentaBean> articulos = albaran.getArticulos();
		String tarjetaFidelizacion = albaran.getTarjetaFidelizacion() != null ? albaran.getTarjetaFidelizacion() : "";
		for (ArticuloAlbaranVentaBean articulo : articulos) {
			if (articulo.getExtensiones() != null && !articulo.getExtensiones().isEmpty()) {
				for (Map.Entry<String, Object> entry : articulo.getExtensiones().entrySet()) {
					if (entry.getValue() instanceof AlbaranVentasCambiosPrecios) {
						AlbaranVentasCambiosPrecios cambio = (AlbaranVentasCambiosPrecios) entry.getValue();
						cambio.setUidActividad(datosSesion.getUidActividad());
						cambio.setFechaProceso(new Date());
						cambio.setIdClieAlbaran(albaran.getIdAlbaran());
						if (StringUtils.isNotBlank(tarjetaFidelizacion)) {
							cambio.setTarjetaFidelizacion(tarjetaFidelizacion);
						}
						insertarPromocionCupon(datosSesion, sqlSession, cambio);
					}

				}
			}

		}
	}
	
	public void insertarCambioPrecioPromocionCabeceraInforme(DatosSesionBean datosSesion, List<InformePromociones> informesPromocion) throws XMLDocumentException {
		SqlSession sqlSession = null;
		try {
			sqlSession = ComerzziaApp.get().getSqlSessionFactory().openSession();
			insertarPromocionInforme(datosSesion, sqlSession, informesPromocion);
		}
		catch (Exception e) {
			log.error("insertarCambioPrecioPromocionCabeceraInforme() - Error al insertar cambio de precio a nivel de cabecera en x_informe_promociones_tbl", e);
		} finally {
			sqlSession.close();
		}
	}
	
	public boolean tratarPromocionesDescuentoManualCuponesDesdeApi(DatosSesionBean datosSesion,Element linea,TicketBean ticket) throws Exception {
		String codArticulo = XMLDocumentUtils.getTagValueAsString(linea, "codart", false);
		log.debug("tratarPromocionesDescuentoManualCuponesDesdeApi() - trantando las promociones para el articulo : " + codArticulo);
		SqlSession sqlSession = null;
		Boolean procesado = false;
		AlbaranVenta albaran = ServicioAlbaranesVentasImpl.get().consultar(ticket.getUidTicket(), datosSesion.getConfigEmpresa());
		Element vendedor = XMLDocumentUtils.getElement(linea, "vendedor", true);
		String usuario = null;
		if (vendedor != null) {
			usuario = XMLDocumentUtils.getTagValueAsString(vendedor, "usuario", true);
		}
		Element promociones = XMLDocumentUtils.getElement(linea, "promociones", true);
		List<Element> lstElementos = XMLDocumentUtils.getChildElements(promociones);
		Double precioEntrada = XMLDocumentUtils.getTagValueAsDouble(linea, "precio_total_tarifa_origen", false);
		Double cantidad = XMLDocumentUtils.getTagValueAsDouble(linea, "cantidad", false);
		BigDecimal bigPrecioEntrada = new BigDecimal(precioEntrada);
		BigDecimal bigCantidad = new BigDecimal(cantidad);
		BigDecimal bigPrecioTotalEntrada = bigPrecioEntrada.multiply(bigCantidad);
		bigPrecioTotalEntrada = BigDecimalUtil.redondear(bigPrecioTotalEntrada, 2);
		String idLinea = linea.getAttribute("idlinea");
		Double precioTotalSinDto = XMLDocumentUtils.getTagValueAsDouble(linea, "precio_total_sin_dto", false);
		Double precioTotalTarifaOrigen = XMLDocumentUtils.getTagValueAsDouble(linea, "precio_total_tarifa_origen", false);
		
		Element root = ticket.getXml().getDocumentElement();
		Element cabecera = XMLDocumentUtils.getElement(root, "cabecera", false);
		Element datosFidelizacion = XMLDocumentUtils.getElement(cabecera, "datos_fidelizacion", true);
		String tarjetaFidelizacion = null;
		if (datosFidelizacion != null){
			tarjetaFidelizacion = (XMLDocumentUtils.getTagValueAsString(datosFidelizacion, "numero_tarjeta", true));
		}
		
		try {
			sqlSession = ComerzziaApp.get().getSqlSessionFactory().openSession();
			for (Element promocion : lstElementos) {
				String idPromocion = XMLDocumentUtils.getTagValueAsString(promocion, "idpromocion", false);
				String idTipoPromocion = XMLDocumentUtils.getTagValueAsString(promocion, "tipopromocion", false);
				String importeTotalDtoMenosMargen = XMLDocumentUtils.getTagValueAsString(promocion, "importeTotalDtoMenosMargen", false);
				String importeTotalDtoMenosIngreso = XMLDocumentUtils.getTagValueAsString(promocion, "importeTotalDtoMenosIngreso", false);
				String acceso = XMLDocumentUtils.getTagValueAsString(promocion, "acceso", false);
				Double descuentoManual = XMLDocumentUtils.getTagValueAsDouble(linea, "descuento_manual", false);
				AlbaranVentasCambiosPrecios cambio = new AlbaranVentasCambiosPrecios();

				
				cambio.setLinea(Integer.valueOf(idLinea));
				cambio.setOrigenModificacion(acceso);
				if (precioTotalSinDto.compareTo(precioTotalTarifaOrigen) != 0) {
					cambio.setOrigenModificacion(DetalleDescuentoAlbaranVentaBean.TIPO_MANUAL);
					cambio.setDocumentoReferencia(ticket.getUidTicket());
				}
				else if (descuentoManual > 0.0) {
					cambio.setOrigenModificacion(DetalleDescuentoAlbaranVentaBean.TIPO_DTO_MANUAL);
					cambio.setAplicadoEn(DetalleDescuentoAlbaranVentaBean.APLICADO_EN_LINEA);
					cambio.setDocumentoReferencia(ticket.getUidTicket());
				}
				else {
					cambio.setDocumentoReferencia(idPromocion);
				}
				String codCaja = consultarCodCaja(datosSesion, sqlSession, ticket.getUidTicket());
				if (codCaja != null && codCaja.equals(COD_CAJA_88)) {
					cambio.setDocumentoReferencia(idPromocion);
					cambio.setOrigenModificacion(acceso);
				}
				cambio.setUidDocumento(ticket.getUidTicket());
				cambio.setLocatorId(ticket.getLocatorId());
				String aplicadoEn;
				if (DetalleDescuentoAlbaranVentaBean.TIPO_PROMOCION_1.equals(idTipoPromocion) || DetalleDescuentoAlbaranVentaBean.TIPO_PROMOCION_2.equals(idTipoPromocion)
				        || DetalleDescuentoAlbaranVentaBean.TIPO_PROMOCION_3.equals(idTipoPromocion) || DetalleDescuentoAlbaranVentaBean.TIPO_PROMOCION_5.equals(idTipoPromocion)
				        || DetalleDescuentoAlbaranVentaBean.TIPO_PROMOCION_9.equals(idTipoPromocion)) {
					aplicadoEn = DetalleDescuentoAlbaranVentaBean.APLICADO_EN_LINEA;
				}
				else if (DetalleDescuentoAlbaranVentaBean.TIPO_PROMOCION_7.equals(idTipoPromocion)) {
					aplicadoEn = DetalleDescuentoAlbaranVentaBean.APLICADO_EN_CABECERA;
				}
				else {
					aplicadoEn = DetalleDescuentoAlbaranVentaBean.APLICADO_EN_LINEA;
				}
				cambio.setAplicadoEn(aplicadoEn);
				cambio.setPrecioEntrada(bigPrecioTotalEntrada);
				BigDecimal modificacion = new BigDecimal(importeTotalDtoMenosMargen);
				BigDecimal menosIngreso = new BigDecimal(importeTotalDtoMenosIngreso);
				modificacion = modificacion.add(menosIngreso);
				cambio.setModificacion(BigDecimalUtil.redondear(modificacion.negate(), 2));
				BigDecimal precioSalida = bigPrecioTotalEntrada.add(cambio.getModificacion());
				precioSalida = BigDecimalUtil.redondear(precioSalida, 2);
				cambio.setPrecioSalida(precioSalida);
				bigPrecioTotalEntrada = precioSalida;
				Date fechaCompleta = albaran.getFecha();
				SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
				String fechaString = formatoFecha.format(fechaCompleta);
				String horaString = "00:00:00";
				cambio.setFecha(fechaString);
				cambio.setHora(horaString);
				cambio.setIdUsuario(usuario);
				if ("PROMOCION".equals(cambio.getOrigenModificacion())) {
					Promocion promocionActiva = ServicioPromocionesImpl.get().consultar(Long.valueOf(cambio.getDocumentoReferencia()), datosSesion);
					cambio.setIdPromo(promocionActiva.getIdPromocion().toString());
					cambio.setDescripcionPromo(promocionActiva.getDescripcion());
					DetallePromociones detallePromo = BricodepotServicioPromociones.get().consultarDetallePromocion(datosSesion, sqlSession, promocionActiva.getIdPromocion(),
					       codArticulo);
					if (detallePromo != null) {
						cambio.setTextoPromo(detallePromo.getTextoPromocion());
					}

					CabeceraPromociones promocioncab = BricodepotServicioPromociones.get().consultarPromocionCabecera(datosSesion, sqlSession, promocionActiva.getIdPromocion());
					cambio.setCodtarPromo(promocioncab.getCodtar());
					cambio.setIdTipoPromo(promocioncab.getIdTipoPromocion());
					TiposPromociones tipoPromocion = BricodepotServicioPromociones.get().consultarTipoDePromocion(datosSesion, sqlSession, promocioncab.getIdTipoPromocion());
					cambio.setDesTipoPromo(tipoPromocion.getDestipopromocion() == null ? null : tipoPromocion.getDestipopromocion());
					cambio.setAplicaA(promocioncab.getAplicaATarifas() == null ? null : promocioncab.getAplicaATarifas());
					cambio.setFechaInicioPromo(promocioncab.getFechaInicio() == null ? null : promocioncab.getFechaInicio());
					cambio.setFechaFinPromo(promocioncab.getFechaFin() == null ? null : promocioncab.getFechaFin());
					cambio.setSoloFidPromo(promocioncab.getSoloFidelizacion() == null ? null : promocioncab.getSoloFidelizacion());
					cambio.setExclusiva(promocioncab.getExclusiva() == null ? null : promocioncab.getExclusiva());
					
				}
				// Cupones
				if ("CUPON".equals(acceso)) {
					String codCupon = XMLDocumentUtils.getTagValueAsString(promocion, "codAcceso", false);
					CouponDTO cupon = consultarCupon(datosSesion, codCupon);
					if (cupon != null) {
						cambio.setFechaInicioCupon(cupon.getCreationDate());
						cambio.setFechaFinCupon(cupon.getEndDate());
						cambio.setCodigoCupon(cupon.getCouponCode());
						cambio.setNombreCupon(cupon.getCouponName());
						cambio.setDescripcionCupon(cupon.getCouponDescription());
						cambio.setCodFidelizadoCupon(cupon.getLoyalCustomerId());
						CouponUse uses = cupon.getUses();
						cambio.setNumeroUsosCupon(uses.getMaxUses());
						cambio.setImporte(cupon.getBalance());
						if ("CUPON".equals(cambio.getOrigenModificacion())) {
							Promocion promocionActiva = ServicioPromocionesImpl.get().consultar(cupon.getPromotionId(), datosSesion);
							cambio.setIdPromo(promocionActiva.getIdPromocion().toString());
							cambio.setDescripcionPromo(promocionActiva.getDescripcion());
							DetallePromociones detallePromo = BricodepotServicioPromociones.get().consultarDetallePromocion(datosSesion, sqlSession, promocionActiva.getIdPromocion(), codArticulo);
							if (detallePromo != null) {
								cambio.setTextoPromo(detallePromo.getTextoPromocion());
							}

							CabeceraPromociones promocioncab = BricodepotServicioPromociones.get().consultarPromocionCabecera(datosSesion, sqlSession, promocionActiva.getIdPromocion());
							cambio.setCodtarPromo(promocioncab.getCodtar());
							cambio.setIdTipoPromo(promocioncab.getIdTipoPromocion());
							TiposPromociones tipoPromocion = BricodepotServicioPromociones.get().consultarTipoDePromocion(datosSesion, sqlSession, promocioncab.getIdTipoPromocion());
							cambio.setDesTipoPromo(tipoPromocion.getDestipopromocion() == null ? null : tipoPromocion.getDestipopromocion());
							cambio.setAplicaA(promocioncab.getAplicaATarifas() == null ? null : promocioncab.getAplicaATarifas());
							cambio.setFechaInicioPromo(promocioncab.getFechaInicio() == null ? null : promocioncab.getFechaInicio());
							cambio.setFechaFinPromo(promocioncab.getFechaFin() == null ? null : promocioncab.getFechaFin());
							cambio.setSoloFidPromo(promocioncab.getSoloFidelizacion() == null ? null : promocioncab.getSoloFidelizacion());
							cambio.setExclusiva(promocioncab.getExclusiva() == null ? null : promocioncab.getExclusiva());

						}
					}
					else {
						Long idPromocionLong = XMLDocumentUtils.getTagValueAsLong(promocion, "idpromocion", false);
						Promocion promocionCodAcceso = ServicioPromocionesImpl.get().consultar(idPromocionLong, datosSesion);
						if (promocionCodAcceso != null && promocionCodAcceso.getPromocionBean().getCodCupon() != null) {
							if (promocionCodAcceso.getPromocionBean().getCodCupon().equals(codCupon)) {
								cambio.setIdPromo(promocionCodAcceso.getIdPromocion().toString());
								cambio.setDescripcionPromo(promocionCodAcceso.getDescripcion());
								DetallePromociones detallePromo = BricodepotServicioPromociones.get().consultarDetallePromocion(datosSesion, sqlSession, promocionCodAcceso.getIdPromocion(), codArticulo);
								if (detallePromo != null) {
									cambio.setTextoPromo(detallePromo.getTextoPromocion());
								}

								CabeceraPromociones promocioncab = BricodepotServicioPromociones.get().consultarPromocionCabecera(datosSesion, sqlSession, promocionCodAcceso.getIdPromocion());
								cambio.setCodtarPromo(promocioncab.getCodtar());
								cambio.setIdTipoPromo(promocioncab.getIdTipoPromocion());
								TiposPromociones tipoPromocion = BricodepotServicioPromociones.get().consultarTipoDePromocion(datosSesion, sqlSession, promocioncab.getIdTipoPromocion());
								cambio.setDesTipoPromo(tipoPromocion.getDestipopromocion() == null ? null : tipoPromocion.getDestipopromocion());
								cambio.setAplicaA(promocioncab.getAplicaATarifas() == null ? null : promocioncab.getAplicaATarifas());
								cambio.setFechaInicioPromo(promocioncab.getFechaInicio() == null ? null : promocioncab.getFechaInicio());
								cambio.setFechaFinPromo(promocioncab.getFechaFin() == null ? null : promocioncab.getFechaFin());
								cambio.setSoloFidPromo(promocioncab.getSoloFidelizacion() == null ? null : promocioncab.getSoloFidelizacion());
								cambio.setExclusiva(promocioncab.getExclusiva() == null ? null : promocioncab.getExclusiva());
							}else {
								cambio.setCodigoCupon(codCupon);
							}
						}else {
							cambio.setCodigoCupon(codCupon);
						}
					}
				}
				
				// Acciones Marketing
				CampAccMarketingBean campaniaEnlaceMarketing = ServicioCampAccEnlMarketingImpl.get().consultarAccionPorIdObjetoEnlace(cambio.getDocumentoReferencia(), datosSesion);
				if (campaniaEnlaceMarketing != null) {
					cambio.setUidAccionMkt(campaniaEnlaceMarketing.getUidAccionMkt());
					try {
						CampAccMarketingBean accion = ServicioCampAccMarketingImpl.get().consultarAccion(cambio.getUidAccionMkt(), datosSesion);
						cambio.setCodaccionmkt(accion.getCodaccionmkt());
						cambio.setDesaccionmkt(accion.getDesaccionmkt());
					}
					catch (CampAccMarketingNotFoundException e) {
						log.warn("tratarPromocionesDescuentoManualCuponesDesdeApi() - Accion con uid " + cambio.getUidAccionMkt() + " No encontrada", e);
						throw new Exception();
					}
				}
				cambio.setUidActividad(datosSesion.getUidActividad());
				cambio.setFechaProceso(new Date());
				cambio.setIdClieAlbaran(albaran.getIdAlbaran());
				cambio.setTarjetaFidelizacion(tarjetaFidelizacion);
				BigDecimal modificacionTratada = cambio.getModificacion();
				if (BigDecimalUtil.isMayor(cambio.getPrecioEntrada(), cambio.getPrecioSalida())) {
				
					if (BigDecimalUtil.isMayorACero(modificacionTratada)) {
						modificacionTratada = modificacionTratada.negate();
						cambio.setModificacion(modificacionTratada);
					}
					
				}else if (BigDecimalUtil.isMayor(cambio.getPrecioSalida(),cambio.getPrecioEntrada())) {
					modificacionTratada = modificacionTratada.abs();
					cambio.setModificacion(modificacionTratada);
				}
				insertarPromocionCuponDesdeApi(datosSesion, sqlSession, cambio);
				procesado = true;
			}
			// PARA DESCUENTOS MANUALES
		
				if(precioTotalSinDto.compareTo(precioTotalTarifaOrigen) != 0) {
					AlbaranVentasCambiosPrecios cambio = new AlbaranVentasCambiosPrecios();

					cambio.setLinea(Integer.valueOf(idLinea));
					cambio.setOrigenModificacion(DetalleDescuentoAlbaranVentaBean.TIPO_MANUAL);
					cambio.setPrecioEntrada(bigPrecioTotalEntrada);
					
					Double precioSalidaDouble = XMLDocumentUtils.getTagValueAsDouble(linea, "importe_total", false);
					BigDecimal precioSalida = new BigDecimal(precioSalidaDouble);
					BigDecimal modificacion = bigPrecioTotalEntrada.subtract(precioSalida);
					
					cambio.setModificacion(BigDecimalUtil.redondear(modificacion, 2));

					
					precioSalida = BigDecimalUtil.redondear(precioSalida, 2);
					cambio.setPrecioSalida(precioSalida);
					
					BigDecimal modificacionTratada = cambio.getModificacion();
					if (BigDecimalUtil.isMayor(cambio.getPrecioEntrada(), cambio.getPrecioSalida())) {
						
						if (BigDecimalUtil.isMayorACero(modificacionTratada)) {
							modificacionTratada = modificacionTratada.negate();
							cambio.setModificacion(modificacionTratada);
						}
						
					}else if (BigDecimalUtil.isMayor(cambio.getPrecioSalida(),cambio.getPrecioEntrada())) {
						modificacionTratada = modificacionTratada.abs();
						cambio.setModificacion(modificacionTratada);
					}
					
					cambio.setAplicadoEn(DetalleDescuentoAlbaranVentaBean.APLICADO_EN_LINEA);
					cambio.setDocumentoReferencia(ticket.getUidTicket());
					cambio.setUidDocumento(ticket.getUidTicket());
					Date fechaCompleta = albaran.getFecha();
					SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
					String fechaString = formatoFecha.format(fechaCompleta);
					cambio.setFecha(fechaString);
					cambio.setHora("00:00:00");
					cambio.setIdUsuario(usuario);
					cambio.setLocatorId(ticket.getLocatorId());
					cambio.setUidActividad(datosSesion.getUidActividad());
					cambio.setFechaProceso(new Date());
					cambio.setIdClieAlbaran(albaran.getIdAlbaran());
					cambio.setTarjetaFidelizacion(tarjetaFidelizacion);
					insertarPromocionCuponDesdeApi(datosSesion, sqlSession, cambio);
					procesado = true;
				}
		}
		catch (Exception e) {
			log.error("tratarPromocionesDescuentoManualCuponesDesdeApi() - Ha ocurrido un error al tratar las promociones del ticket : "+ ticket.getUidTicket() +" en la linea: "+idLinea +" -codart: "+ codArticulo +e.getMessage());
			sqlSession.rollback();
			throw new Exception(e);
		}finally {
			sqlSession.close();
		}
		return procesado;
	}
	public String consultarCodCaja(DatosSesionBean datosSesion, SqlSession sqlSession, String uidTicket) {
		
		AlbaranVentasCambiosPreciosMapper mapper = sqlSession.getMapper(AlbaranVentasCambiosPreciosMapper.class);
		String codCaja = mapper.consultarCodCajaPorUidTicket(datosSesion.getUidActividad(), uidTicket);
		return codCaja;
	}
	public CouponDTO consultarCupon(DatosSesionBean datosSesion,String codCupon) {
		CouponDTO cupon = null;
		try {
			comerzziaApiManager = (ComerzziaApiManager) ContextHolder.get().getBean("comerzziaApiManagerImpl");
			CouponsApi api = comerzziaApiManager.getClient(datosSesion, "CouponsApi");
			cupon = api.getCoupon(codCupon);
		}catch(Exception e) {
			log.debug("consultarCupon() - Ha ocurrido un error consultando el cupon : "+codCupon);
		}
		return cupon;
	}
	public void tratarPromocionesDescuentoManualCupones(DatosSesionBean datosSesion, SqlSession sqlSession, Element linea, ArticuloAlbaranVentaBean articulo, AlbaranVentaBean albaran,
	        TicketBean ticket) throws Exception {
		log.debug("tratarPromocionesDescuentoManualCupones() - trantando las promociones para el articulo : " + articulo.getCodArticulo());
		Element promociones = XMLDocumentUtils.getElement(linea, "promociones", true);
		List<Element> lstElementos = XMLDocumentUtils.getChildElements(promociones);
		Double precioEntrada = XMLDocumentUtils.getTagValueAsDouble(linea, "precio_total_tarifa_origen", false);
		Double cantidad = XMLDocumentUtils.getTagValueAsDouble(linea, "cantidad", false);
		BigDecimal bigPrecioEntrada = new BigDecimal(precioEntrada);
		BigDecimal bigCantidad = new BigDecimal(cantidad);
		BigDecimal bigPrecioTotalEntrada = bigPrecioEntrada.multiply(bigCantidad);
		bigPrecioTotalEntrada = BigDecimalUtil.redondear(bigPrecioTotalEntrada, 2);
		String idLinea = linea.getAttribute("idlinea");
		articulo.setExtensiones(new HashMap<String, Object>());
		try {
			for (Element promocion : lstElementos) {
				String idPromocion = XMLDocumentUtils.getTagValueAsString(promocion, "idpromocion", false);
				String idTipoPromocion = XMLDocumentUtils.getTagValueAsString(promocion, "tipopromocion", false);
				String importeTotalDtoMenosMargen = XMLDocumentUtils.getTagValueAsString(promocion, "importeTotalDtoMenosMargen", false);
				String importeTotalDtoMenosIngreso = XMLDocumentUtils.getTagValueAsString(promocion, "importeTotalDtoMenosIngreso", false);
				String acceso = XMLDocumentUtils.getTagValueAsString(promocion, "acceso", false);
				Double descuentoManual = XMLDocumentUtils.getTagValueAsDouble(linea, "descuento_manual", false);
				Double precioTotalSinDto = XMLDocumentUtils.getTagValueAsDouble(linea, "precio_total_sin_dto", false);
				Double precioTotalTarifaOrigen = XMLDocumentUtils.getTagValueAsDouble(linea, "precio_total_tarifa_origen", false);
				AlbaranVentasCambiosPrecios cambio = new AlbaranVentasCambiosPrecios();

				Long valorContador = ServicioContadoresImpl.get().obtenerValorContador(datosSesion, CONTADOR_PRECIO);
				cambio.setIdCambio(valorContador);
				cambio.setLinea(Integer.valueOf(idLinea));
				cambio.setOrigenModificacion(acceso);
				if (precioTotalSinDto.compareTo(precioTotalTarifaOrigen) != 0) {
					cambio.setOrigenModificacion(DetalleDescuentoAlbaranVentaBean.TIPO_MANUAL);
					cambio.setDocumentoReferencia(ticket.getUidTicket());
				}
				else if (descuentoManual > 0.0) {
					cambio.setOrigenModificacion(DetalleDescuentoAlbaranVentaBean.TIPO_DTO_MANUAL);
					cambio.setAplicadoEn(DetalleDescuentoAlbaranVentaBean.APLICADO_EN_LINEA);
					cambio.setDocumentoReferencia(ticket.getUidTicket());
				}
				else {
					cambio.setDocumentoReferencia(idPromocion);
				}
				
				String codCaja = albaran.getCodCaja();
				if (codCaja != null && codCaja.equals(COD_CAJA_88)) {
					cambio.setDocumentoReferencia(idPromocion);
					cambio.setOrigenModificacion(acceso);
				}
				cambio.setUidDocumento(ticket.getUidTicket());
				cambio.setLocatorId(ticket.getLocatorId());
				String aplicadoEn;
				if (DetalleDescuentoAlbaranVentaBean.TIPO_PROMOCION_1.equals(idTipoPromocion) || DetalleDescuentoAlbaranVentaBean.TIPO_PROMOCION_2.equals(idTipoPromocion)
				        || DetalleDescuentoAlbaranVentaBean.TIPO_PROMOCION_3.equals(idTipoPromocion) || DetalleDescuentoAlbaranVentaBean.TIPO_PROMOCION_5.equals(idTipoPromocion)
				        || DetalleDescuentoAlbaranVentaBean.TIPO_PROMOCION_9.equals(idTipoPromocion)) {
					aplicadoEn = DetalleDescuentoAlbaranVentaBean.APLICADO_EN_LINEA;
				}
				else if (DetalleDescuentoAlbaranVentaBean.TIPO_PROMOCION_7.equals(idTipoPromocion)) {
					aplicadoEn = DetalleDescuentoAlbaranVentaBean.APLICADO_EN_CABECERA;
				}
				else {
					aplicadoEn = DetalleDescuentoAlbaranVentaBean.APLICADO_EN_LINEA;
				}
				cambio.setAplicadoEn(aplicadoEn);
				cambio.setPrecioEntrada(bigPrecioTotalEntrada);
				BigDecimal modificacion = new BigDecimal(importeTotalDtoMenosMargen);
				BigDecimal menosIngreso = new BigDecimal(importeTotalDtoMenosIngreso);
				modificacion = modificacion.add(menosIngreso);
				cambio.setModificacion(BigDecimalUtil.redondear(modificacion.negate(), 2));
				BigDecimal precioSalida = bigPrecioTotalEntrada.add(cambio.getModificacion());
				precioSalida = BigDecimalUtil.redondear(precioSalida, 2);
				cambio.setPrecioSalida(precioSalida);
				bigPrecioTotalEntrada = precioSalida;
				Date fechaCompleta = albaran.getFechaCompleta();
				SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
				String fechaString = formatoFecha.format(fechaCompleta);
				SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
				String horaString = formatoHora.format(fechaCompleta);
				cambio.setFecha(fechaString);
				cambio.setHora(horaString);
				cambio.setIdUsuario(albaran.getUsuario());
				if ("PROMOCION".equals(cambio.getOrigenModificacion())) {
					Promocion promocionActiva = ServicioPromocionesImpl.get().consultar(Long.valueOf(cambio.getDocumentoReferencia()), datosSesion);
					cambio.setIdPromo(promocionActiva.getIdPromocion().toString());
					cambio.setDescripcionPromo(promocionActiva.getDescripcion());
					DetallePromociones detallePromo = BricodepotServicioPromociones.get().consultarDetallePromocion(datosSesion, sqlSession, promocionActiva.getIdPromocion(),
					        articulo.getCodArticulo());
					if (detallePromo != null) {
						cambio.setTextoPromo(detallePromo.getTextoPromocion());
					}

					CabeceraPromociones promocioncab = BricodepotServicioPromociones.get().consultarPromocionCabecera(datosSesion, sqlSession, promocionActiva.getIdPromocion());
					cambio.setCodtarPromo(promocioncab.getCodtar());
					cambio.setIdTipoPromo(promocioncab.getIdTipoPromocion());
					TiposPromociones tipoPromocion = BricodepotServicioPromociones.get().consultarTipoDePromocion(datosSesion, sqlSession, promocioncab.getIdTipoPromocion());
					cambio.setDesTipoPromo(tipoPromocion.getDestipopromocion() == null ? null : tipoPromocion.getDestipopromocion());
					cambio.setAplicaA(promocioncab.getAplicaATarifas() == null ? null : promocioncab.getAplicaATarifas());
					cambio.setFechaInicioPromo(promocioncab.getFechaInicio() == null ? null : promocioncab.getFechaInicio());
					cambio.setFechaFinPromo(promocioncab.getFechaFin() == null ? null : promocioncab.getFechaFin());
					cambio.setSoloFidPromo(promocioncab.getSoloFidelizacion() == null ? null : promocioncab.getSoloFidelizacion());
					cambio.setExclusiva(promocioncab.getExclusiva() == null ? null : promocioncab.getExclusiva());
					
				}
				// Cupones
				if ("CUPON".equals(acceso)) {
					String codCupon = XMLDocumentUtils.getTagValueAsString(promocion, "codAcceso", false);
					CouponDTO cupon = consultarCupon(datosSesion, codCupon);
					if (cupon != null) {
						cambio.setFechaInicioCupon(cupon.getCreationDate());
						cambio.setFechaFinCupon(cupon.getEndDate());
						cambio.setCodigoCupon(cupon.getCouponCode());
						cambio.setNombreCupon(cupon.getCouponName());
						cambio.setDescripcionCupon(cupon.getCouponDescription());
						cambio.setCodFidelizadoCupon(cupon.getLoyalCustomerId());
						CouponUse uses = cupon.getUses();
						cambio.setNumeroUsosCupon(uses.getMaxUses());
						cambio.setImporte(cupon.getBalance());
						if ("CUPON".equals(cambio.getOrigenModificacion())) {
							Promocion promocionActiva = ServicioPromocionesImpl.get().consultar(cupon.getPromotionId(), datosSesion);
							cambio.setIdPromo(promocionActiva.getIdPromocion().toString());
							cambio.setDescripcionPromo(promocionActiva.getDescripcion());
							DetallePromociones detallePromo = BricodepotServicioPromociones.get().consultarDetallePromocion(datosSesion, sqlSession, promocionActiva.getIdPromocion(),
							        articulo.getCodArticulo());
							if (detallePromo != null) {
								cambio.setTextoPromo(detallePromo.getTextoPromocion());
							}

							CabeceraPromociones promocioncab = BricodepotServicioPromociones.get().consultarPromocionCabecera(datosSesion, sqlSession, promocionActiva.getIdPromocion());
							cambio.setCodtarPromo(promocioncab.getCodtar());
							cambio.setIdTipoPromo(promocioncab.getIdTipoPromocion());
							TiposPromociones tipoPromocion = BricodepotServicioPromociones.get().consultarTipoDePromocion(datosSesion, sqlSession, promocioncab.getIdTipoPromocion());
							cambio.setDesTipoPromo(tipoPromocion.getDestipopromocion() == null ? null : tipoPromocion.getDestipopromocion());
							cambio.setAplicaA(promocioncab.getAplicaATarifas() == null ? null : promocioncab.getAplicaATarifas());
							cambio.setFechaInicioPromo(promocioncab.getFechaInicio() == null ? null : promocioncab.getFechaInicio());
							cambio.setFechaFinPromo(promocioncab.getFechaFin() == null ? null : promocioncab.getFechaFin());
							cambio.setSoloFidPromo(promocioncab.getSoloFidelizacion() == null ? null : promocioncab.getSoloFidelizacion());
							cambio.setExclusiva(promocioncab.getExclusiva() == null ? null : promocioncab.getExclusiva());

						}
					}
					else {
						Long idPromocionLong = XMLDocumentUtils.getTagValueAsLong(promocion, "idpromocion", false);
						Promocion promocionCodAcceso = ServicioPromocionesImpl.get().consultar(idPromocionLong, datosSesion);
						if (promocionCodAcceso != null && promocionCodAcceso.getPromocionBean().getCodCupon() != null) {
							if (promocionCodAcceso.getPromocionBean().getCodCupon().equals(codCupon)) {
								cambio.setIdPromo(promocionCodAcceso.getIdPromocion().toString());
								cambio.setDescripcionPromo(promocionCodAcceso.getDescripcion());
								DetallePromociones detallePromo = BricodepotServicioPromociones.get().consultarDetallePromocion(datosSesion, sqlSession, promocionCodAcceso.getIdPromocion(), articulo.getCodArticulo());
								if (detallePromo != null) {
									cambio.setTextoPromo(detallePromo.getTextoPromocion());
								}

								CabeceraPromociones promocioncab = BricodepotServicioPromociones.get().consultarPromocionCabecera(datosSesion, sqlSession, promocionCodAcceso.getIdPromocion());
								cambio.setCodtarPromo(promocioncab.getCodtar());
								cambio.setIdTipoPromo(promocioncab.getIdTipoPromocion());
								TiposPromociones tipoPromocion = BricodepotServicioPromociones.get().consultarTipoDePromocion(datosSesion, sqlSession, promocioncab.getIdTipoPromocion());
								cambio.setDesTipoPromo(tipoPromocion.getDestipopromocion() == null ? null : tipoPromocion.getDestipopromocion());
								cambio.setAplicaA(promocioncab.getAplicaATarifas() == null ? null : promocioncab.getAplicaATarifas());
								cambio.setFechaInicioPromo(promocioncab.getFechaInicio() == null ? null : promocioncab.getFechaInicio());
								cambio.setFechaFinPromo(promocioncab.getFechaFin() == null ? null : promocioncab.getFechaFin());
								cambio.setSoloFidPromo(promocioncab.getSoloFidelizacion() == null ? null : promocioncab.getSoloFidelizacion());
								cambio.setExclusiva(promocioncab.getExclusiva() == null ? null : promocioncab.getExclusiva());
							}else {
								cambio.setCodigoCupon(codCupon);
							}
						}else {
							cambio.setCodigoCupon(codCupon);
						}
					}
				}
				// Acciones Marketing
				CampAccMarketingBean campaniaEnlaceMarketing = ServicioCampAccEnlMarketingImpl.get().consultarAccionPorIdObjetoEnlace(cambio.getDocumentoReferencia(), datosSesion);
				if (campaniaEnlaceMarketing != null) {
					cambio.setUidAccionMkt(campaniaEnlaceMarketing.getUidAccionMkt());
					try {
						CampAccMarketingBean accion = ServicioCampAccMarketingImpl.get().consultarAccion(cambio.getUidAccionMkt(), datosSesion);
						cambio.setCodaccionmkt(accion.getCodaccionmkt());
						cambio.setDesaccionmkt(accion.getDesaccionmkt());
					}
					catch (CampAccMarketingNotFoundException e) {
						log.warn("tratarPromociones() - Accion con uid " + cambio.getUidAccionMkt() + " No encontrada", e);
						throw new Exception();
					}
				}
				
				BigDecimal modificacionTratada = cambio.getModificacion();
				if (BigDecimalUtil.isMayor(cambio.getPrecioEntrada(), cambio.getPrecioSalida())) {
				
					if (BigDecimalUtil.isMayorACero(modificacionTratada)) {
						modificacionTratada = modificacionTratada.negate();
						cambio.setModificacion(modificacionTratada);
					}
					
				}else if (BigDecimalUtil.isMayor(cambio.getPrecioSalida(),cambio.getPrecioEntrada())) {
					modificacionTratada = modificacionTratada.abs();
					cambio.setModificacion(modificacionTratada);
				}

				articulo.getExtensiones().put(valorContador.toString(), cambio);
			}
			// PARA DESCUENTOS MANUALES
			List<DetalleDescuentoAlbaranVentaBean> detalleDescuentos = albaran.getDetalleDescuentos();
			for (DetalleDescuentoAlbaranVentaBean detalle : detalleDescuentos) {
				if (detalle.getLinea().toString().equals(idLinea) && "MANUAL".equals(detalle.getOrigenModificacion())) {
					AlbaranVentasCambiosPrecios cambio = new AlbaranVentasCambiosPrecios();

					Long valorContador = ServicioContadoresImpl.get().obtenerValorContador(datosSesion, CONTADOR_PRECIO);
					cambio.setIdCambio(valorContador);
					cambio.setLinea(Integer.valueOf(idLinea));
					cambio.setOrigenModificacion(detalle.getOrigenModificacion());
					cambio.setPrecioEntrada(bigPrecioTotalEntrada);
					
					Double precioSalidaDouble = XMLDocumentUtils.getTagValueAsDouble(linea, "importe_total", false);
					BigDecimal precioSalida = new BigDecimal(precioSalidaDouble);
					BigDecimal modificacion = bigPrecioTotalEntrada.subtract(precioSalida);
					
					cambio.setModificacion(BigDecimalUtil.redondear(modificacion, 2));
					
					precioSalida = BigDecimalUtil.redondear(precioSalida, 2);
					cambio.setPrecioSalida(precioSalida);

					BigDecimal modificacionTratada = cambio.getModificacion();
					if (BigDecimalUtil.isMayor(cambio.getPrecioEntrada(), cambio.getPrecioSalida())) {
						
						if (BigDecimalUtil.isMayorACero(modificacionTratada)) {
							modificacionTratada = modificacionTratada.negate();
							cambio.setModificacion(modificacionTratada);
						}
						
					}else if (BigDecimalUtil.isMayor(cambio.getPrecioSalida(),cambio.getPrecioEntrada())) {
						modificacionTratada = modificacionTratada.abs();
						cambio.setModificacion(modificacionTratada);
					}
					cambio.setLinea(detalle.getLinea());
					cambio.setOrigenModificacion(detalle.getOrigenModificacion());
					cambio.setAplicadoEn(detalle.getAplicadoEn());
					cambio.setDocumentoReferencia(detalle.getDocumentoReferencia());
					cambio.setUidDocumento(ticket.getUidTicket());
					Date fechaCompleta = albaran.getFechaCompleta();
					SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
					String fechaString = formatoFecha.format(fechaCompleta);
					SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
					String horaString = formatoHora.format(fechaCompleta);
					cambio.setFecha(fechaString);
					cambio.setHora(horaString);
					cambio.setIdUsuario(albaran.getUsuario());
					cambio.setLocatorId(ticket.getLocatorId());
					articulo.getExtensiones().put(valorContador.toString(), cambio);

				}
			}
		}
		catch (Exception e) {
			log.error("tratarPromocionesDescuentoManualCupones() - Ha ocurrido un error al tratar las promociones de: " + articulo.getCodArticulo());
			throw new Exception(e);
		}
	}

	public void insertarPromocionCupon(DatosSesionBean datosSesion, SqlSession sqlSession, AlbaranVentasCambiosPrecios cambio) {
		log.debug("insertarPromocionCupon() - insertando detalle de : " + cambio.getIdClieAlbaran());

		try {
			AlbaranVentasCambiosPreciosMapper mapper = sqlSession.getMapper(AlbaranVentasCambiosPreciosMapper.class);
			mapper.insert(cambio);
		}
		catch (Exception e) {
			log.warn("insertarPromocionCupon() - ", e);
		}
	}
	
	public void insertarPromocionCuponDesdeApi(DatosSesionBean datosSesion, SqlSession sqlSession, AlbaranVentasCambiosPrecios cambio) throws Exception {
		log.debug("insertarPromocionCupon() - insertando detalle de : " + cambio.getIdClieAlbaran());

		try {
			Long valorContador = ServicioContadoresImpl.get().obtenerValorContador(datosSesion, CONTADOR_PRECIO);
			cambio.setIdCambio(valorContador);
			AlbaranVentasCambiosPreciosMapper mapper = sqlSession.getMapper(AlbaranVentasCambiosPreciosMapper.class);
			mapper.insert(cambio);
			sqlSession.commit();
		}
		catch (Exception e) {
			log.warn("insertarPromocionCupon() - ", e);
			throw e;
		}
	}
	
	public List<String> consultarUidTicketPorFechas(DatosSesionBean datosSesion,String fechaInicio,String fechaFin) throws Exception{
		log.debug("consultarUidTicketPorFechas() - consultando uid_tickets entre : "+fechaInicio +" - "+ fechaFin);
		List<String> listaUidTickets = new ArrayList<String>();
		com.comerzzia.core.util.mybatis.session.SqlSession sqlSession = null;
		try {
			sqlSession = new com.comerzzia.core.util.mybatis.session.SqlSession();
			sqlSession.openSession(datosSesion.getSqlSessionFactory().openSession());
			AlbaranVentasCambiosPreciosMapper mapper = sqlSession.getMapper(AlbaranVentasCambiosPreciosMapper.class);
			 SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date fechaInicioDate = dateFormat.parse(fechaInicio);
			Date fechaFinDate = dateFormat.parse(fechaFin);
			listaUidTickets = mapper.consultarTicketsUid(datosSesion.getUidActividad(), fechaInicioDate, fechaFinDate);
		}catch (Exception e) {
			log.error("consultarUidTicketPorFechas() - Se ha producido un error consultando los uid_ticket ",e);
			throw e;
		}finally {
			sqlSession.close();
		}
		return listaUidTickets;
	}
	
	public void insertarPromocionInforme(DatosSesionBean datosSesion, SqlSession sqlSession, List<InformePromociones> informesPromociones) throws Exception {
		log.debug("insertarPromocionCuponInforme() - insertando registro");
		try {
			InformePromocionesMapper mapper = sqlSession.getMapper(InformePromocionesMapper.class);
			for (InformePromociones informePromo : informesPromociones) {
				mapper.insert(informePromo);
			}
			sqlSession.commit();
		}
		catch (Exception e) {
			log.warn("insertarPromocionCupon() - ", e);
			throw e;
		}
	}
}
