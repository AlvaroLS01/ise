package com.comerzzia.bricodepot.backoffice.services.ventas.cajas;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.comerzzia.aena.util.xml.MarshallUtil;
import com.comerzzia.aena.util.xml.MarshallUtilException;
import com.comerzzia.bricodepot.backoffice.persistence.ventas.cajas.CajaEstadoException;
import com.comerzzia.bricodepot.backoffice.persistence.ventas.cajas.CajasFicticias;
import com.comerzzia.bricodepot.backoffice.persistence.ventas.cajas.CajasFicticiasKey;
import com.comerzzia.bricodepot.backoffice.persistence.ventas.cajas.CajasFicticiasMapper;
import com.comerzzia.bricodepot.backoffice.persistence.ventas.cajas.CajasServiceException;
import com.comerzzia.bricodepot.backoffice.persistence.ventas.cajas.cierre.Caja;
import com.comerzzia.bricodepot.backoffice.persistence.ventas.cajas.fuerte.Caja90Dto;
import com.comerzzia.bricodepot.backoffice.persistence.ventas.cajas.retirada.DetMovRetiradaExample;
import com.comerzzia.bricodepot.backoffice.persistence.ventas.cajas.retirada.DetMovRetiradaKey;
import com.comerzzia.bricodepot.backoffice.persistence.ventas.cajas.retirada.DetMovRetiradaMapper;
import com.comerzzia.bricodepot.backoffice.util.movimientos.cajasficticias.CajaFicticiaDTO;
import com.comerzzia.core.model.contadores.ContadorBean;
import com.comerzzia.core.model.tiposdocumentos.TipoDocumentoBean;
import com.comerzzia.core.model.variables.VariableBean;
import com.comerzzia.core.model.ventas.tickets.TicketBean;
import com.comerzzia.core.omnichannel.engine.persistence.cajas.movimientos.CajaMovimientoBean;
import com.comerzzia.core.servicios.contadores.ContadorException;
import com.comerzzia.core.servicios.contadores.ContadorNotFoundException;
import com.comerzzia.core.servicios.contadores.ServicioContadoresImpl;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.servicios.tipodocumento.ServicioTiposDocumentosImpl;
import com.comerzzia.core.servicios.tipodocumento.TipoDocumentoException;
import com.comerzzia.core.servicios.tipodocumento.TipoDocumentoNotFoundException;
import com.comerzzia.core.servicios.variables.ServicioVariablesImpl;
import com.comerzzia.core.servicios.variables.VariableException;
import com.comerzzia.core.servicios.variables.VariableNotFoundException;
import com.comerzzia.core.servicios.ventas.tickets.ServicioTicketsImpl;
import com.comerzzia.core.servicios.ventas.tickets.TicketException;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.core.util.db.Database;
import com.comerzzia.core.util.fechas.Fecha;
import com.comerzzia.core.util.mybatis.session.SqlSession;
import com.comerzzia.core.util.numeros.BigDecimalUtil;
import com.comerzzia.model.ventas.cajas.cabecera.CabeceraCaja;
import com.comerzzia.model.ventas.cajas.cabecera.CabeceraCajaExample;
import com.comerzzia.model.ventas.cajas.conceptos.ConceptoMovimientoCajaBean;
import com.comerzzia.model.ventas.cajas.detalle.DetalleCaja;
import com.comerzzia.model.ventas.cajas.detalle.DetalleCajaExample;
import com.comerzzia.persistencia.ventas.cajas.cabecera.CabeceraCajaMapper;
import com.comerzzia.persistencia.ventas.cajas.detalle.DetalleCajaMapper;
import com.comerzzia.servicios.ventas.cajas.conceptos.ConceptosMovimientosCajaNotFoundException;
import com.comerzzia.servicios.ventas.cajas.conceptos.ConceptosMovimientosCajasException;
import com.comerzzia.servicios.ventas.cajas.conceptos.ServicioConceptosMovimientosCajaImpl;

@SuppressWarnings("deprecation")
@Service
public class BricodepotServicioCajasImpl {

	protected static Logger log = Logger.getLogger(BricodepotServicioCajasImpl.class);

	public static final String CAJA_FUERTE = "80";
	public static final String CAJA_PROSEGUR = "90";
	public static final String APERTURA_CAJA = "01";
	public static final String CIERRE_CAJA = "00";

	public static final Long COD_DOCUMENTO_CIERRE_CAJA = 500L;
	public static final String CONTADORES_CARACTER_SEPARADOR = "CONTADORES.CARACTER_SEPARADOR";

	protected static BricodepotServicioCajasImpl instance;

	public static BricodepotServicioCajasImpl get() {
		if (instance != null) {
			return instance;
		}
		else {
			return new BricodepotServicioCajasImpl();
		}
	}

	public void abrirCajaFuerteYProsegur(DatosSesionBean datosSesion, SqlSession sqlSession, Date fecha, String codalm)
	        throws CajasFicticiasException, CajasServiceException, CajaEstadoException, ConceptosMovimientosCajasException, ConceptosMovimientosCajaNotFoundException {
		log.debug("abrirCajaFuerteYProsegur()");
		abrirCajaDiaria(datosSesion, sqlSession, fecha, codalm, CAJA_FUERTE);
		abrirCajaDiaria(datosSesion, sqlSession, fecha, codalm, CAJA_PROSEGUR);
	}

	public void abrirCajaDiaria(DatosSesionBean datosSesion, SqlSession sqlSession, Date fecha, String codalm, String codCaja)
	        throws CajasServiceException, CajaEstadoException, CajasFicticiasException, ConceptosMovimientosCajasException, ConceptosMovimientosCajaNotFoundException {
		log.debug("abrirCajaDiaria() - Consultando si existe caja para almacacen : " + codalm + " y caja: " + codCaja);

		CabeceraCajaExample exampleCaja = new CabeceraCajaExample();
		exampleCaja.or().andUidActividadEqualTo(datosSesion.getUidActividad()).andCodalmEqualTo(codalm).andCodcajaEqualTo(codCaja);
		CabeceraCajaMapper cajaMapper = sqlSession.getMapper(CabeceraCajaMapper.class);
		List<CabeceraCaja> listaCajas = cajaMapper.selectByExample(exampleCaja);
		DetalleCajaMapper detalleMapper = sqlSession.getMapper(DetalleCajaMapper.class);
		if (listaCajas.isEmpty()) {
			log.debug("abrirCajaDiaria() - Nunca se ha creado una caja:" + codCaja + " para el almacen : " + codalm);
			String uid = UUID.randomUUID().toString();
			CabeceraCaja caja = new CabeceraCaja();
			caja.setUidActividad(datosSesion.getUidActividad());
			caja.setUidDiarioCaja(uid);
			caja.setCodcaja(codCaja);
			caja.setCodalm(codalm);
			caja.setFechaApertura(fecha);
			caja.setUsuario("ADMINISTRADOR");
			Calendar calendar = Calendar.getInstance();
			Date fechaActual = calendar.getTime();
			calendar.setTime(fechaActual);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			Date fechaContable = calendar.getTime();
			caja.setFechaContable(fechaContable);
			cajaMapper.insert(caja);

			ConceptoMovimientoCajaBean concepto = ServicioConceptosMovimientosCajaImpl.get().consultar(APERTURA_CAJA, datosSesion);

			DetalleCaja detalleCaja = new DetalleCaja();
			detalleCaja.setUidActividad(caja.getUidActividad());
			detalleCaja.setUidDiarioCaja(uid);
			detalleCaja.setLinea(1);
			detalleCaja.setCodconceptoMov(concepto.getCodConceptoMovimiento());
			detalleCaja.setConcepto(concepto.getDesConceptoMovimiento());
			detalleCaja.setCargo(BigDecimal.ZERO);
			detalleCaja.setAbono(BigDecimal.ZERO);
			detalleCaja.setUidTransaccionDet(UUID.randomUUID().toString());
			detalleCaja.setUsuario("ADMINISTRADOR");
			detalleCaja.setFecha(fecha);

			detalleCaja.setCodmedpag("0000");

			detalleMapper.insert(detalleCaja);
		}
		else {
			log.debug("abrirCajaDiaria() - Comprobando si existe una caja cerrada para el almacen : " + codalm);
			if (existeCajaAbiertaParaHoy(datosSesion, cajaMapper, codalm, codCaja)) {
				String mensaje = "Existe una caja " + codCaja + " para el almacen " + codalm + ". Debe cerrarse antes de abrir una nueva";
				throw new CajasFicticiasException(mensaje);
			}
			CabeceraCaja cabeceraCaja = consultarUltimaCajaCerrada(datosSesion, sqlSession, codalm, codCaja);
			log.debug("abrirCajaDiaria() - Ultima caja " + cabeceraCaja.getCodcaja() + "para almacen : " + cabeceraCaja.getCodalm() + " uid diario caja : " + cabeceraCaja.getUidDiarioCaja());
			DetalleCajaExample exampleDetalle = new DetalleCajaExample();
			exampleDetalle.or().andUidActividadEqualTo(datosSesion.getUidActividad()).andUidDiarioCajaEqualTo(cabeceraCaja.getUidDiarioCaja()).andCodconceptoMovEqualTo("04");
			DetalleCajaMapper mapperDetalle = sqlSession.getMapper(DetalleCajaMapper.class);
			List<DetalleCaja> lista = mapperDetalle.selectByExample(exampleDetalle);

			DetalleCaja detalleCierre = null;
			if (lista != null && !lista.isEmpty()) {
				detalleCierre = lista.get(0);
			}

			String uid = UUID.randomUUID().toString();
			CabeceraCaja caja = new CabeceraCaja();
			caja.setUidActividad(cabeceraCaja.getUidActividad());
			caja.setUidDiarioCaja(uid);
			caja.setCodcaja(cabeceraCaja.getCodcaja());
			caja.setCodalm(cabeceraCaja.getCodalm());
			caja.setFechaApertura(fecha);
			caja.setUsuario("ADMINISTRADOR");
			Calendar calendar = Calendar.getInstance();
			Date fechaActual = calendar.getTime();
			calendar.setTime(fechaActual);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			Date fechaContable = calendar.getTime();
			caja.setFechaContable(fechaContable);

			cajaMapper.insert(caja);
			ConceptoMovimientoCajaBean concepto = ServicioConceptosMovimientosCajaImpl.get().consultar(APERTURA_CAJA, datosSesion);
			DetalleCaja detalleCaja = new DetalleCaja();
			detalleCaja.setUidActividad(caja.getUidActividad());
			detalleCaja.setUidDiarioCaja(uid);
			detalleCaja.setLinea(1);
			detalleCaja.setCodconceptoMov(concepto.getCodConceptoMovimiento());
			detalleCaja.setConcepto(concepto.getDesConceptoMovimiento());
			detalleCaja.setCargo(detalleCierre == null ? BigDecimal.ZERO : detalleCierre.getAbono());
			detalleCaja.setAbono(BigDecimal.ZERO);
			detalleCaja.setUidTransaccionDet(uid);
			detalleCaja.setFecha(fecha);
			detalleCaja.setUsuario("ADMINISTRADOR");

			detalleCaja.setCodmedpag("0000");

			detalleMapper.insert(detalleCaja);

		}
	}

	public boolean existeCajaAbiertaParaHoy(DatosSesionBean datosSesion, CabeceraCajaMapper cajaMapper, String codAlm, String codCaja) {
		log.debug("existeCajaAbiertaParaHoy() - Comprobando si existe caja abierta hoy para la caja " + codCaja + " en la tienda: " + codAlm);
		boolean existeCajaAbierta = false;
		CabeceraCajaExample example = new CabeceraCajaExample();
		Date fechaHoy = new Date();
		fechaHoy = DateUtils.truncate(fechaHoy, Calendar.DATE);
		example.or().andUidActividadEqualTo(datosSesion.getUidActividad()).andCodalmEqualTo(codAlm).andCodcajaEqualTo(codCaja).andFechaCierreIsNull();
		example.setOrderByClause("FECHA_APERTURA DESC");
		List<CabeceraCaja> lista = cajaMapper.selectByExample(example);
		if (!lista.isEmpty()) {
			existeCajaAbierta = true;
		}
		return existeCajaAbierta;
	}

	public CabeceraCaja consultarUltimaCajaCerrada(DatosSesionBean datosSesion, SqlSession sqlSession, String codAlm, String codCaja) throws CajasServiceException, CajaEstadoException {
		try {
			CabeceraCajaExample exampleCaja = new CabeceraCajaExample();
			exampleCaja.or().andUidActividadEqualTo(datosSesion.getUidActividad()).andCodalmEqualTo(codAlm).andCodcajaEqualTo(codCaja).andFechaCierreIsNotNull();
			exampleCaja.setOrderByClause("FECHA_APERTURA DESC");
			log.debug("consultarUltimaCajaCerrada() - Consultado caja cerrada en sesion");
			CabeceraCajaMapper cajaMapper = sqlSession.getMapper(CabeceraCajaMapper.class);
			List<CabeceraCaja> cajasBean = cajaMapper.selectByExample(exampleCaja);

			if (cajasBean.isEmpty()) {
				throw new CajaEstadoException("No existen cajas cerrada en el sistema");
			}
			return cajasBean.get(0);
		}
		catch (CajaEstadoException e) {
			throw e;
		}
		catch (Exception e) {
			String msg = "Se ha producido un error consultando caja cerrada en sesion :" + e.getMessage();
			log.error("consultarCajaAbierta() - " + msg, e);
			throw new CajasServiceException("Error al consultar caja abierta en sesión del sistema", e);
		}
	}

	public CajasFicticias consultarCajaFicticia(DatosSesionBean datosSesion, SqlSession sqlSession, String codalm, String codCaja) {
		log.debug("consultarCajaFicticia() - Consultando si existe caja : " + codCaja + " para la tienda : " + codalm);
		CajasFicticiasMapper mapper = sqlSession.getMapper(CajasFicticiasMapper.class);
		CajasFicticiasKey key = new CajasFicticiasKey();
		key.setCodalm(codalm);
		key.setUidActividad(datosSesion.getUidActividad());
		key.setCodcaja(codCaja);
		CajasFicticias cajaFicticia = mapper.selectByPrimaryKey(key);
		if (cajaFicticia == null) {
			cajaFicticia = new CajasFicticias();
			cajaFicticia.setUidDiarioCaja(UUID.randomUUID().toString());
			cajaFicticia.setCodalm(codalm);
			cajaFicticia.setUidActividad(datosSesion.getUidActividad());
			cajaFicticia.setCodcaja(codCaja);
			cajaFicticia.setSaldo(BigDecimal.ZERO);
			log.debug("crearCajaFuerte() - No existe caja : " + codCaja + " para la tienda: " + codalm + ". Se procede a crearla.");
			mapper.insert(cajaFicticia);
		}
		return cajaFicticia;

	}

	public void cerrarCajaFuerteYProsegur(DatosSesionBean datosSesion, SqlSession sqlSession, Date fecha, String codalm) throws CajasFicticiasException {
		log.debug("cerrarCajaFuerteYProsegur() - Cerrando caja fuerte y caja prosegur para el almacen : " + codalm);
		cerrarCajaFicticia(datosSesion, sqlSession, fecha, codalm, CAJA_FUERTE);
		cerrarCajaFicticia(datosSesion, sqlSession, fecha, codalm, CAJA_PROSEGUR);
	}

	public void cerrarCajaFicticia(DatosSesionBean datosSesion, SqlSession sqlSession, Date fecha, String codalm, String codCaja) throws CajasFicticiasException {
		log.debug("cerrarCajaFicticia() - Cerrando caja " + codCaja + " para tienda : " + codalm);
		try {
			CabeceraCajaExample example = new CabeceraCajaExample();
			example.or().andUidActividadEqualTo(datosSesion.getUidActividad()).andCodalmEqualTo(codalm).andCodcajaEqualTo(codCaja).andFechaCierreIsNull();
			example.setOrderByClause("FECHA_APERTURA DESC");
			CabeceraCajaMapper mapper = sqlSession.getMapper(CabeceraCajaMapper.class);
			List<CabeceraCaja> lista = mapper.selectByExample(example);
			CabeceraCaja caja = null;
			if (lista.size() > 0) {
				caja = lista.get(0);
			}

			if (caja != null) {
				generarCierreDeCaja(datosSesion, sqlSession, codalm, codCaja, caja);
				caja.setFechaCierre(fecha);
				caja.setUsuarioCierre("ADMINISTRADOR");
				mapper.updateByPrimaryKey(caja);
			}
		}
		catch (Exception e) {
			log.warn("abrirCajaFuerte() - Se ha producido un error abriendo la caja fuerte para la tienda : " + codalm);
			throw new CajasFicticiasException(e.getMessage());
		}

	}

	public Caja consultarCajasMovimientosDeCaja(DatosSesionBean datosSesion, SqlSession sqlSession, String codalm, String codCaja, CabeceraCaja caja) {
		log.debug("consultarCajasMovimientosDeCaja() - Consultando listado de movimientos de la caja : " + codCaja + " codigo almacen : " + codalm + "uidDiario: " + caja.getUidDiarioCaja());
		Caja cajaXML = new Caja();
		DetalleCajaMapper mapperDetalle = sqlSession.getMapper(DetalleCajaMapper.class);

		DetalleCajaExample detalleExample = new DetalleCajaExample();
		detalleExample.or().andUidActividadEqualTo(datosSesion.getUidActividad()).andUidDiarioCajaEqualTo(caja.getUidDiarioCaja());
		List<DetalleCaja> listaDetalles = mapperDetalle.selectByExample(detalleExample);
		cajaXML.getCajaBean().setUidActividad(datosSesion.getUidActividad());
		cajaXML.getCajaBean().setUidDiarioCaja(caja.getUidDiarioCaja());
		cajaXML.getCajaBean().setCodAlmacen(codalm);
		cajaXML.getCajaBean().setCodCaja(codCaja);
		cajaXML.getCajaBean().setFechaApertura(caja.getFechaApertura());
		cajaXML.getCajaBean().setFechaCierre(new Date());
		cajaXML.getCajaBean().setFechaEnvio(new Date());
		cajaXML.getCajaBean().setUsuario(caja.getUsuario());
		cajaXML.getCajaBean().setUsuarioCierre("ADMINISTRADOR");
		cajaXML.getCajaBean().setFechaContable(caja.getFechaContable());
		if (listaDetalles != null && !listaDetalles.isEmpty()) {
			BigDecimal totalApuntesEntrada = BigDecimal.ZERO;
			BigDecimal totalApuntesSalida = BigDecimal.ZERO;
			for (DetalleCaja detalle : listaDetalles) {
				if (!BigDecimalUtil.isIgualACero(detalle.getAbono())) {
					totalApuntesSalida = totalApuntesSalida.add(detalle.getAbono());
					totalApuntesSalida = BigDecimalUtil.redondear(totalApuntesSalida, 2);
				}

				if (!BigDecimalUtil.isIgualACero(detalle.getCargo())) {
					totalApuntesEntrada = totalApuntesEntrada.add(detalle.getCargo());
					totalApuntesEntrada = BigDecimalUtil.redondear(totalApuntesEntrada, 2);
				}

				com.comerzzia.bricodepot.backoffice.persistence.ventas.cajas.cierre.CajaMovimientoBean movimiento = new com.comerzzia.bricodepot.backoffice.persistence.ventas.cajas.cierre.CajaMovimientoBean();
				movimiento.setLinea(detalle.getLinea());
				movimiento.setUidDiarioCaja(detalle.getUidDiarioCaja());
				movimiento.setFecha(detalle.getFecha());
				movimiento.setCargo(detalle.getCargo());
				movimiento.setAbono(detalle.getAbono());
				movimiento.setConcepto(detalle.getConcepto());
				movimiento.setCodMedioPago("0000");
				movimiento.setCodConceptoMovimiento(detalle.getCodconceptoMov());
				movimiento.setUsuario(detalle.getUsuario());
				cajaXML.getMovimientos().add(movimiento);
			}
			cajaXML.getTotales().setTotalApuntesEntrada(totalApuntesEntrada);
			cajaXML.getTotales().setTotalApuntesSalida(totalApuntesSalida);

		}
		return cajaXML;
	}

	public void generarCierreDeCaja(DatosSesionBean datosSesion, SqlSession sqlSession, String codalm, String codCaja, CabeceraCaja caja)
	        throws TipoDocumentoNotFoundException, TipoDocumentoException, ContadorNotFoundException, ContadorException, VariableException, VariableNotFoundException, UnsupportedEncodingException,
	        MarshallUtilException, TicketException, SQLException, CajasFicticiasException {
		log.debug("generarCierreDeCaja() - generando cierre de caja para la caja " + codCaja + " del almacen : " + codalm);
		Caja cierreCaja = consultarCajasMovimientosDeCaja(datosSesion, sqlSession, codalm, codCaja, caja);
		generarMovimientoCierreCaja(datosSesion, sqlSession, codalm, codCaja, caja, cierreCaja);
		log.debug("cerrarCaja() - Obteniendo contado para identificador...");
		Map<String, String> parametrosContador = new HashMap<>();
		parametrosContador.put("CODEMP", codalm);
		parametrosContador.put("CODALM", codalm);
		parametrosContador.put("CODSERIE", codalm);
		parametrosContador.put("CODCAJA", codalm);
		parametrosContador.put("CODTIPODOCUMENTO", codalm);
		parametrosContador.put("PERIODO", ((new Fecha(caja.getFechaApertura())).getAño().toString()));

		TipoDocumentoBean documentoActivo = ServicioTiposDocumentosImpl.get().consultar(datosSesion, COD_DOCUMENTO_CIERRE_CAJA);

		ContadorBean ticketContador = ServicioContadoresImpl.get().obtenerContador(datosSesion, documentoActivo.getIdContador(), parametrosContador);
		String codTicketString = obtenerValorTotalConSeparador(datosSesion, ticketContador.getDivisor3(), ticketContador.getValorFormateado());
		String uid = UUID.randomUUID().toString();
		TicketBean ticket = new TicketBean();
		ticket.setUidActividad(datosSesion.getUidActividad());
		ticket.setCodAlmacen(codalm);
		ticket.setCodCaja(codCaja);
		ticket.setFecha(caja.getFechaApertura());
		ticket.setIdTicket(ticketContador.getValor());
		ticket.setUidTicket(uid);
		ticket.setTicket(MarshallUtil.crearXML(cierreCaja));
		log.debug("TICKET: " + ticket.getUidTicket() + "\n" + new String(ticket.getTicket(), "UTF-8") + "\n");
		ticket.setIdTipoDocumento(documentoActivo.getIdTipoDocumento());
		ticket.setSerieTicket(ticketContador.getDivisor3());
		ticket.setCodTicket(codTicketString);
		ticket.setFirma("*");
		ticket.setLocatorId(uid);
		ticket.setProcesado("N");
		Connection conn = new Connection();
		try {
			conn.abrirConexion(Database.getConnection());
			ServicioTicketsImpl.get().insertaTicket(conn, ticket);

		}
		catch (Exception e) {
			log.error("generarCierreDeCaja() - Ha ocurrido un error : " + e.getMessage());
			throw e;
		}
		finally {
			conn.cerrarConexion();
		}

	}

	public void generarMovimientoCierreCaja(DatosSesionBean datosSesion, SqlSession sqlSession, String codalm, String codCaja, CabeceraCaja caja, Caja cierreCaja) throws CajasFicticiasException {
		log.debug("generarMovimientoCierreCaja() - generando movimiento de cierre de caja para el almacen " + codalm + " y la caja: " + codCaja);
		try {
			DetalleCajaMapper mapperDetalle = sqlSession.getMapper(DetalleCajaMapper.class);
			CajasFicticiasMapper mapperCajasFicticias = sqlSession.getMapper(CajasFicticiasMapper.class);
			DetalleCaja detalle = new DetalleCaja();
			detalle.setUidActividad(datosSesion.getUidActividad());
			detalle.setUidDiarioCaja(caja.getUidDiarioCaja());
			BigDecimal linea = mapperCajasFicticias.selectMaxLinea(datosSesion.getUidActividad(), caja.getUidDiarioCaja());
			if (linea == null) {
				detalle.setLinea(1);
			}
			else {
				detalle.setLinea(linea.intValue() + 1);
			}
			ConceptoMovimientoCajaBean concepto = ServicioConceptosMovimientosCajaImpl.get().consultar("04", datosSesion);

			detalle.setConcepto(concepto.getDesConceptoMovimiento());
			detalle.setCodconceptoMov(concepto.getCodConceptoMovimiento());
			detalle.setIdTipoDocumento(500L);
			detalle.setDocumento("CIERRE DE CAJA");
			detalle.setCodmedpag("0000");
			detalle.setUidDiarioCaja(caja.getUidDiarioCaja());
			detalle.setUsuario(caja.getUsuario());
			detalle.setUidTransaccionDet(caja.getUidDiarioCaja());
			detalle.setFecha(new Date());
			BigDecimal totalAlCierre = cierreCaja.getTotales().getTotalApuntesEntrada().subtract(cierreCaja.getTotales().getTotalApuntesSalida());
			totalAlCierre = BigDecimalUtil.redondear(totalAlCierre, 2);
			detalle.setCargo(BigDecimal.ZERO);
			detalle.setAbono(totalAlCierre);
			mapperDetalle.insert(detalle);
			com.comerzzia.bricodepot.backoffice.persistence.ventas.cajas.cierre.CajaMovimientoBean movimiento = new com.comerzzia.bricodepot.backoffice.persistence.ventas.cajas.cierre.CajaMovimientoBean();
			movimiento.setLinea(detalle.getLinea());
			movimiento.setUidDiarioCaja(detalle.getUidDiarioCaja());
			movimiento.setFecha(detalle.getFecha());
			movimiento.setCargo(detalle.getCargo());
			movimiento.setAbono(detalle.getAbono());
			movimiento.setConcepto(detalle.getConcepto());
			movimiento.setCodMedioPago("0000");
			movimiento.setCodConceptoMovimiento(detalle.getCodconceptoMov());
			movimiento.setUsuario(detalle.getUsuario());
			cierreCaja.getMovimientos().add(movimiento);

		}
		catch (Exception e) {
			log.error("generarMovimientoCierreCaja() - Se ha producido un error añadiendo detalle de cierre a la caja : " + codCaja);
			throw new CajasFicticiasException(e.getMessage());
		}
	}

	public String obtenerValorTotalConSeparador(DatosSesionBean datosSesion, String divisor, String valorContadorFormateado) throws VariableException, VariableNotFoundException {
		VariableBean variableCaracter = ServicioVariablesImpl.get().consultar(datosSesion, CONTADORES_CARACTER_SEPARADOR);
		String separador = null;
		if (variableCaracter != null) {
			separador = variableCaracter.getValor();
		}
		if (variableCaracter != null && "".equals(variableCaracter.getValor())) {
			separador = "/";
		}
		return divisor + separador + valorContadorFormateado;
	}

	public String formatearFecha(Date fecha) {
		log.debug("formatearFecha()");
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String fechaFormateada = formato.format(fecha);
		return fechaFormateada;
	}

	public void addSaldoCajaFicticia(DatosSesionBean datosSesion, String uidActividad, String codCaja, String codalm, BigDecimal importe) throws CajasFicticiasException {
		log.debug("addSaldoCajaFicticia()");
		SqlSession sqlSession = null;
		try {
			sqlSession = new SqlSession();
			sqlSession.openSession(datosSesion.getSqlSessionFactory().openSession());
			CajasFicticias cajaFicticia = consultarCajaFicticia(datosSesion, sqlSession, codalm, codCaja);
			if (cajaFicticia == null) {
				String msg = "addSaldoCajaFicticia() - No se ha encontrado la caja : " + codCaja + " - Almacen : " + codalm;
				log.warn(msg);
				throw new CajasFicticiasException(msg);
			}
			BigDecimal saldoCaja = cajaFicticia.getSaldo();
			BigDecimal saldoActualizado = saldoCaja.add(importe);
			saldoActualizado = BigDecimalUtil.redondear(saldoActualizado, 2);
			CajasFicticiasMapper mapper = sqlSession.getMapper(CajasFicticiasMapper.class);
			mapper.updateByPrimaryKey(cajaFicticia);
		}
		catch (Exception e) {
			log.warn("abrirCajaFuerte() - Se ha producido un error añadiendo saldo a la caja : " + codCaja + " - Almacen : " + codalm);
			throw new CajasFicticiasException(e.getMessage());
		}
		finally {
			sqlSession.close();
		}
	}

	public String consultarUidDiarioCajaAbierta(DatosSesionBean datosSesion, String codcaja, String codAlm) throws CajasFicticiasException {
		log.debug("consultarUidDiarioCajaAbierta()");
		SqlSession sqlSession = null;
		String uidDiarioCaja = null;
		try {
			sqlSession = new SqlSession();
			sqlSession.openSession(datosSesion.getSqlSessionFactory().openSession());
			CabeceraCajaExample example = new CabeceraCajaExample();
			example.or().andUidActividadEqualTo(datosSesion.getUidActividad()).andCodalmEqualTo(codAlm).andCodcajaEqualTo(codcaja).andFechaCierreIsNull();
			CabeceraCajaMapper mapper = sqlSession.getMapper(CabeceraCajaMapper.class);
			example.setOrderByClause("FECHA_APERTURA DESC");
			List<CabeceraCaja> lista = mapper.selectByExample(example);
			if (lista.size() == 1) {
				uidDiarioCaja = lista.get(0).getUidDiarioCaja();
			}
			return uidDiarioCaja;
		}
		catch (Exception e) {
			log.warn("abrirCajaFuerte() - Se ha producido un error consultando el uidDiario de la caja : " + codcaja + " - Almacen : " + codAlm);
			throw new CajasFicticiasException(e.getMessage());
		}
		finally {
			sqlSession.close();
		}
	}

	public void insertarMovimientoCaja(DatosSesionBean datosSesion, TicketBean ticket, org.apache.ibatis.session.SqlSession sqlSession, CajaFicticiaDTO caja) throws CajasFicticiasException {
		log.debug("insertarMovimientoCaja() - insertando movimiento de detalle para caja en central ");
		try {
			DetalleCajaMapper mapper = sqlSession.getMapper(DetalleCajaMapper.class);
			DetalleCaja detalle = getCabeceraCaja(caja);
			detalle.setUidActividad(datosSesion.getUidActividad());
			CajasFicticiasMapper mapperCajas = sqlSession.getMapper(CajasFicticiasMapper.class);

			String uid = null;
			if (detalle.getUidDiarioCaja() == null) {
				uid = consultarCajaAbierta(datosSesion, sqlSession, caja);
				detalle.setUidDiarioCaja(uid);
			}
			else {
				uid = detalle.getUidDiarioCaja();
			}

			if (StringUtils.isBlank(uid)) {
				throw new CajasFicticiasException("insertarMovimientoCaja() - No se ha encontrado un uid diario de caja para la caja : " + caja.getCodCaja() + " del almacen: " + caja.getCodAlm());
			}
			caja.setUidDiarioCaja(uid);
			log.debug("insertarMovimientoCaja() - el uid diario de la caja : " + caja.getCodCaja() + " del almacen: " + caja.getCodAlm() + "es : " + detalle.getUidDiarioCaja());
			BigDecimal idLinea = mapperCajas.selectMaxLinea(datosSesion.getUidActividad(), detalle.getUidDiarioCaja());
			if (idLinea != null) {
				detalle.setLinea(idLinea.intValue() + 1);
			}
			else {
				detalle.setLinea(1);
			}
			log.debug("insertarMovimientoCaja() - consultando linea para la caja : " + caja.getCodCaja() + " del almacen: " + caja.getCodAlm() + " -linea : " + detalle.getLinea());
			detalle.setIdTipoDocumento(ticket.getIdTipoDocumento());
			detalle.setIdDocumento(ticket.getUidTicket());
			detalle.setDocumento(caja.getMovimiento().getDocumento() != null ? caja.getMovimiento().getDocumento() : null);
			detalle.setUidTransaccionDet(caja.getMovimiento().getUidTransaccionDet());
			detalle.setCodmedpag("0000");
			mapper.insert(detalle);

		}
		catch (Exception e) {
			log.error("insertarMovimientoCaja() - Se ha producido un error añadiendo detalle a la caja : " + caja.getCodCaja() + " - Almacen : " + caja.getCodAlm());
			throw new CajasFicticiasException(e.getMessage());
		}
	}

	private String consultarCajaAbierta(DatosSesionBean datosSesion, org.apache.ibatis.session.SqlSession sqlSession, CajaFicticiaDTO caja) {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String uid = null;
		String fechaFormateada = formato.format(caja.getMovimiento().getFecha());
		log.debug("consultarCajaAbierta() - Procesando documento sin uid_diario_de_caja. Se consulta si hay una caja abierta para la fecha: " + fechaFormateada);
		CabeceraCajaMapper mapper = sqlSession.getMapper(CabeceraCajaMapper.class);
		CabeceraCajaExample example = new CabeceraCajaExample();
		example.or().andUidActividadEqualTo(datosSesion.getUidActividad()).andCodalmEqualTo(caja.getCodAlm()).andCodcajaEqualTo(caja.getCodCaja()).andFechaCierreIsNull();
		example.setOrderByClause("FECHA_APERTURA DESC");

		List<CabeceraCaja> listaCajas = mapper.selectByExample(example);

		if (!listaCajas.isEmpty()) {
			CabeceraCaja cajacab = listaCajas.get(0);
			uid = cajacab.getUidDiarioCaja();
		}
		return uid;
	}

	public DetalleCaja getCabeceraCaja(CajaFicticiaDTO cajaDTO) {
		log.debug("getCabeceraCaja()");
		DetalleCaja detalleCaja = new DetalleCaja();
		detalleCaja.setUidDiarioCaja(cajaDTO.getUidDiarioCaja() == null ? null : cajaDTO.getUidDiarioCaja());
		detalleCaja.setAbono(cajaDTO.getMovimiento().getAbono());
		detalleCaja.setCargo(cajaDTO.getMovimiento().getCargo());
		detalleCaja.setCodconceptoMov(cajaDTO.getMovimiento().getCodConceptoMovimiento());
		detalleCaja.setConcepto(cajaDTO.getMovimiento().getConcepto());
		detalleCaja.setFecha(cajaDTO.getMovimiento().getFecha());
		detalleCaja.setUsuario(cajaDTO.getMovimiento().getUsuario());
		detalleCaja.setUidTransaccionDet(UUID.randomUUID().toString());
		return detalleCaja;
	}

	public List<CajaMovimientoBean> consultarMovimientoHistorico(DatosSesionBean datosSesion, String codConcepto, String codAlmacen) {
		log.debug("consultarMovimientoHistorico() - consultando movimientos de la caja 90");
		SqlSession sqlSession = null;
		List<CajaMovimientoBean> listaMovimientos91 = null;
		try {
			sqlSession = new SqlSession();
			sqlSession.openSession(datosSesion.getSqlSessionFactory().openSession());
			CajasFicticiasMapper mapper = sqlSession.getMapper(CajasFicticiasMapper.class);
			listaMovimientos91 = mapper.selectMovCaja(datosSesion.getUidActividad(), CAJA_PROSEGUR, codConcepto, codAlmacen);
		}
		catch (Exception e) {
			log.error("consultarMovimientoHistorico() - Se ha producido un error consultando los movimientos de la caja 90 ", e);
		}
		finally {
			sqlSession.close();
		}
		return listaMovimientos91;
	}

	public Caja90Dto getImporteUidDiarioCaja90(DatosSesionBean datosSesion, String codAlmacen) {
		log.debug("getImporteCaja90() - consultando importe de la caja 90 para el almacen : " + codAlmacen);
		SqlSession sqlSession = null;
		Caja90Dto caja90Dto = null;
		try {
			sqlSession = new SqlSession();
			sqlSession.openSession(datosSesion.getSqlSessionFactory().openSession());
			CabeceraCajaMapper mapper = sqlSession.getMapper(CabeceraCajaMapper.class);
			CabeceraCajaExample example = new CabeceraCajaExample();
			example.or().andUidActividadEqualTo(datosSesion.getUidActividad()).andCodalmEqualTo(codAlmacen).andCodcajaEqualTo(CAJA_PROSEGUR).andFechaCierreIsNull();
			example.setOrderByClause("FECHA_APERTURA DESC");
			List<CabeceraCaja> lista = mapper.selectByExample(example);
			if (!lista.isEmpty()) {
				CabeceraCaja caja = lista.get(0);
				DetalleCajaMapper mapperDetalle = sqlSession.getMapper(DetalleCajaMapper.class);
				DetalleCajaExample detalleExample = new DetalleCajaExample();
				detalleExample.or().andUidActividadEqualTo(datosSesion.getUidActividad()).andUidDiarioCajaEqualTo(caja.getUidDiarioCaja());
				List<DetalleCaja> listaDetalles = mapperDetalle.selectByExample(detalleExample);
				BigDecimal cargos = BigDecimal.ZERO;
				BigDecimal abonos = BigDecimal.ZERO;
				for (DetalleCaja detalle : listaDetalles) {
					cargos = cargos.add(detalle.getCargo());
					abonos = abonos.add(detalle.getAbono());
				}
				caja90Dto = new Caja90Dto();
				caja90Dto.setImporte(cargos.subtract(abonos));
				caja90Dto.setImporte(BigDecimalUtil.redondear(caja90Dto.getImporte(), 2));
				caja90Dto.setUidDiarioCaja(caja.getUidDiarioCaja());
			}
		}
		catch (Exception e) {
			log.error("getImporteCaja90() - Se ha producido un error consultando el importe de la caja 90 ", e);
		}
		finally {
			sqlSession.close();
		}
		return caja90Dto;
	}

	public List<DetalleCaja> consultarListaMovimientos90(DatosSesionBean datosSesion, String uidDiarioCaja) {
		log.debug("consultarListaMovimientos90()");
		List<DetalleCaja> movimientos = null;
		SqlSession sqlSession = null;
		try {
			sqlSession = new SqlSession();
			sqlSession.openSession(datosSesion.getSqlSessionFactory().openSession());
			DetalleCajaMapper mapperDetalle = sqlSession.getMapper(DetalleCajaMapper.class);
			DetalleCajaExample detalleExample = new DetalleCajaExample();
			detalleExample.or().andUidActividadEqualTo(datosSesion.getUidActividad()).andUidDiarioCajaEqualTo(uidDiarioCaja);
			movimientos = mapperDetalle.selectByExample(detalleExample);
		}
		catch (Exception e) {
			log.error("consultarListaMovimientos90() - Se ha producido un error consultando la lista de mov 90 de la caja 90 ", e);
		}
		finally {
			sqlSession.close();
		}
		return movimientos;
	}

	public List<DetMovRetiradaKey> consultarListaMovimientos(DatosSesionBean datosSesion, String uidDiarioCaja) {
		log.debug("consultarListaMovimientos()");
		SqlSession sqlSession = null;
		List<DetMovRetiradaKey> lista = null;
		try {
			sqlSession = new SqlSession();
			sqlSession.openSession(datosSesion.getSqlSessionFactory().openSession());
			DetMovRetiradaMapper mapper = sqlSession.getMapper(DetMovRetiradaMapper.class);
			DetMovRetiradaExample example = new DetMovRetiradaExample();
			example.or().andUidActividadEqualTo(datosSesion.getUidActividad()).andUidDiarioCajaEqualTo(uidDiarioCaja);
			lista = mapper.selectByExample(example);
		}
		catch (Exception e) {
			log.error("consultarListaMovimientos() - Se ha producido un error consultando la lista de mov 90 de la caja 90 ", e);
		}
		finally {
			sqlSession.close();
		}
		return lista;
	}

	public void insertarMovimientos(DatosSesionBean datosSesion, List<DetMovRetiradaKey> movimientos) {
		log.debug("insertarMovimientos() ");
		SqlSession sqlSession = null;
		try {
			sqlSession = new SqlSession();
			sqlSession.openSession(datosSesion.getSqlSessionFactory().openSession());
			DetMovRetiradaMapper mapper = sqlSession.getMapper(DetMovRetiradaMapper.class);
			for (DetMovRetiradaKey mov : movimientos) {
				mapper.insert(mov);
			}
			sqlSession.commit();
		}
		catch (Exception e) {
			sqlSession.rollback();
			log.error("insertarMovimientos() - Se ha producido un error insertando movimientos ", e);
		}
		finally {
			sqlSession.close();
		}
	}

	public Integer selectMaxLinea(DatosSesionBean datosSesion, String uidDiarioCaja) {
		log.debug("insertarMovimientos() ");
		SqlSession sqlSession = null;
		Integer valorLinea = null;
		try {
			sqlSession = new SqlSession();
			sqlSession.openSession(datosSesion.getSqlSessionFactory().openSession());
			CajasFicticiasMapper mapper = sqlSession.getMapper(CajasFicticiasMapper.class);
			BigDecimal linea = mapper.selectMaxLinea(datosSesion.getUidActividad(), uidDiarioCaja);
			valorLinea = linea.intValue();
		}
		catch (Exception e) {
			sqlSession.rollback();
			log.error("selectMaxLinea() - Se ha producido un error consultando la maxima linea ", e);
		}
		finally {
			sqlSession.close();
		}
		return valorLinea;
	}
}