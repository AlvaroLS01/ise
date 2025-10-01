package com.comerzzia.bricodepot.backoffice.services.ventas.sco.acumuladorventas;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.comerzzia.bricodepot.backoffice.persistence.albaranes.venta.sco.acumuladorventas.AcumuladorVentas;
import com.comerzzia.bricodepot.backoffice.persistence.albaranes.venta.sco.acumuladorventas.AcumuladorVentasKey;
import com.comerzzia.bricodepot.backoffice.persistence.albaranes.venta.sco.acumuladorventas.AcumuladorVentasMapper;
import com.comerzzia.core.model.ventas.tickets.TicketBean;
import com.comerzzia.core.util.fechas.FechaException;
import com.comerzzia.core.util.fechas.Fechas;
import com.comerzzia.core.util.xml.XMLDocumentException;
import com.comerzzia.core.util.xml.XMLDocumentUtils;

public class ServicioAcumuladorVentasImpl {

	protected static Logger log = Logger.getLogger(ServicioAcumuladorVentasImpl.class);

	protected static ServicioAcumuladorVentasImpl instance;

	public static ServicioAcumuladorVentasImpl get() {
		if (instance == null) {
			instance = new ServicioAcumuladorVentasImpl();
		}
		return instance;
	}
	
	public void insertarAcumuladorVenta(AcumuladorVentas acumVentas, SqlSession sqlSession) {
		log.debug("insertarAcumuladorVenta() - Insertando registro en la tabla de acumulador de ventas: " + acumVentas);
		AcumuladorVentasMapper acumuladorVentasMapper = sqlSession.getMapper(AcumuladorVentasMapper.class);
		acumuladorVentasMapper.insert(acumVentas);
	}

	public void actualizarAcumuladorVenta(AcumuladorVentas acumVentas, SqlSession sqlSession) {
		log.debug("actualizarAcumuladorVenta() - Actualizando registro en la tabla de acumulador de ventas: " + acumVentas);
		AcumuladorVentasMapper acumuladorVentasMapper = sqlSession.getMapper(AcumuladorVentasMapper.class);
		acumuladorVentasMapper.updateByPrimaryKey(acumVentas);
	}
	
	public void cargarInformeAcumuladorVentas(TicketBean ticket, SqlSession sqlSession) throws XMLDocumentException, FechaException {
		log.debug("cargarInformeAcumuladorVentas() - Creando registro para ticket " + ticket.getUidTicket());
		Element root = ticket.getXml().getDocumentElement();
		Element cabecera = XMLDocumentUtils.getElement(root, "cabecera", false);
		Element empresa = XMLDocumentUtils.getElement(cabecera, "empresa", false);
		Element tienda = XMLDocumentUtils.getElement(cabecera, "tienda", false);
		Element lineas = XMLDocumentUtils.getElement(root, "lineas", false);
		List<Element> listaLineas = XMLDocumentUtils.getChildElements(lineas);
		
		AcumuladorVentas acumVentas = new AcumuladorVentas();
		String uidActividad = XMLDocumentUtils.getTagValueAsString(cabecera, "uid_actividad", false);
		acumVentas.setUidActividad(uidActividad);
		
		String stringFecha = XMLDocumentUtils.getTagValueAsString(cabecera, "fecha", false);
		Date fecha = Fechas.getFecha(stringFecha.substring(0, stringFecha.indexOf("T")), "yyyy-MM-dd");
		acumVentas.setFecha(fecha);
		
		String codalm = XMLDocumentUtils.getTagValueAsString(tienda, "codigo", false);
		acumVentas.setCodalm(codalm);

		String codcaja = XMLDocumentUtils.getTagValueAsString(cabecera, "codcaja", false);
		acumVentas.setCodcaja(codcaja);
		
		String codemp = XMLDocumentUtils.getTagValueAsString(empresa, "codigo", false);
		acumVentas.setCodemp(codemp);

		Element totales = XMLDocumentUtils.getElement(cabecera, "totales", false);
		Double importeTotal = XMLDocumentUtils.getTagValueAsDouble(totales, "total", false);
		Element elementPagos = XMLDocumentUtils.getElement(root, "pagos", false);
		List<Element> pagos = XMLDocumentUtils.getChildElements(elementPagos);
		
		if (importeTotal > 0D) {
			AcumuladorVentas registro = consultarRegistroExistente(sqlSession, codcaja, codalm, uidActividad, fecha, stringFecha);
			if (registro != null) {
				log.debug("cargarInformeAcumuladorVentas() - Se han encontrado registros anteriores");
				acumVentas.setNumTickets(registro.getNumTickets() + 1);
				acumVentas.setUnidadesArticulos(registro.getUnidadesArticulos() + getTotalUnidadesArticulosVenta(listaLineas));
				acumVentas.setImporteTotal(registro.getImporteTotal().add(BigDecimal.valueOf(importeTotal).setScale(2, RoundingMode.HALF_UP)));
				acumVentas.setnVentasClienteCuenta(registro.getnVentasClienteCuenta());
				acumVentas.setnVentasEcommerce(registro.getnVentasEcommerce());
				acumVentas.setnVentasEfectivo(registro.getnVentasEfectivo());
				acumVentas.setnVentasFinanciacionEs(registro.getnVentasFinanciacionEs());
				acumVentas.setnVentasFinanciacionPt(registro.getnVentasFinanciacionPt());
				acumVentas.setnVentasTarjManual(registro.getnVentasTarjManual());
				acumVentas.setnVentasVale(registro.getnVentasVale());
				acumVentas.setnVentasTarjRegalo(registro.getnVentasTarjRegalo());
				acumVentas.setnVentasTarjeta(registro.getnVentasTarjeta());
				acumVentas.setnVentasTransferencia(registro.getnVentasTransferencia());
				acumVentas.setnVentasAutonomas(registro.getnVentasAutonomas());

				for (Element pago : pagos) {
					String codMedPag = XMLDocumentUtils.getTagValueAsString(pago, "codmedpag", false);
					if (codMedPag.equals(AcumuladorVentasCodMedPagos.CLIENTE_A_CUENTA)) {
						acumVentas.setnVentasClienteCuenta(registro.getnVentasClienteCuenta() + 1);
					}
					else if (codMedPag.equals(AcumuladorVentasCodMedPagos.ECOMMERCE)) {
						acumVentas.setnVentasEcommerce(registro.getnVentasEcommerce() + 1);
					}
					else if (codMedPag.equals(AcumuladorVentasCodMedPagos.EFECTIVO)) {
						acumVentas.setnVentasEfectivo(registro.getnVentasEfectivo() + 1);
					}
					else if (codMedPag.equals(AcumuladorVentasCodMedPagos.ES_FINANCIACION)) {
						acumVentas.setnVentasFinanciacionEs(registro.getnVentasFinanciacionEs() + 1);
					}
					else if (codMedPag.equals(AcumuladorVentasCodMedPagos.PT_FINANCIACION)) {
						acumVentas.setnVentasFinanciacionPt(registro.getnVentasFinanciacionPt() + 1);
					}
					else if (codMedPag.equals(AcumuladorVentasCodMedPagos.TARJETA_MANUAL)) {
						acumVentas.setnVentasTarjManual(registro.getnVentasTarjManual() + 1);
					}
					else if (codMedPag.equals(AcumuladorVentasCodMedPagos.GIFTCARD)) {
						acumVentas.setnVentasTarjRegalo(registro.getnVentasTarjRegalo() + 1);
					}
					else if (codMedPag.equals(AcumuladorVentasCodMedPagos.VALE)) {
						acumVentas.setnVentasVale(registro.getnVentasVale() + 1);
					}
					else if (codMedPag.equals(AcumuladorVentasCodMedPagos.TARJETA)) {
						acumVentas.setnVentasTarjeta(registro.getnVentasTarjeta() + 1);
					}
					else if (codMedPag.equals(AcumuladorVentasCodMedPagos.TRANSFERENCIA)) {
						acumVentas.setnVentasTransferencia(registro.getnVentasTransferencia() + 1);
					}
				}
				acumVentas.setnVentasFidelizado(registro.getnVentasFidelizado());
				if (XMLDocumentUtils.getElement(cabecera, "datos_fidelizacion", true) != null) {
					acumVentas.setnVentasFidelizado(registro.getnVentasFidelizado() + 1);
				}
			}
			else {
				log.debug("cargarInformeAcumuladorVentas() - No hay ningún registro anterior");
				acumVentas.setNumTickets(1L);
				acumVentas.setUnidadesArticulos(getTotalUnidadesArticulosVenta(listaLineas));
				acumVentas.setImporteTotal(BigDecimal.valueOf(importeTotal));

				acumVentas.setnVentasClienteCuenta(0L);
				acumVentas.setnVentasEcommerce(0L);
				acumVentas.setnVentasEfectivo(0L);
				acumVentas.setnVentasFinanciacionEs(0L);
				acumVentas.setnVentasFinanciacionPt(0L);
				acumVentas.setnVentasTarjManual(0L);
				acumVentas.setnVentasFidelizado(0L);
				acumVentas.setnVentasVale(0L);
				acumVentas.setnVentasTarjRegalo(0L);
				acumVentas.setnVentasTarjeta(0L);
				acumVentas.setnVentasTransferencia(0L);

				acumVentas.setnVentasAutonomas(0L);

				for (Element pago : pagos) {
					String desmedpag = XMLDocumentUtils.getTagValueAsString(pago, "codmedpag", false);
					if (desmedpag.equals(AcumuladorVentasCodMedPagos.CLIENTE_A_CUENTA)) {
						acumVentas.setnVentasClienteCuenta(1L);
					}
					else if (desmedpag.equals(AcumuladorVentasCodMedPagos.ECOMMERCE)) {
						acumVentas.setnVentasEcommerce(1L);
					}
					else if (desmedpag.equals(AcumuladorVentasCodMedPagos.EFECTIVO)) {
						acumVentas.setnVentasEfectivo(1L);
					}
					else if (desmedpag.equals(AcumuladorVentasCodMedPagos.ES_FINANCIACION)) {
						acumVentas.setnVentasFinanciacionEs(1L);
					}
					else if (desmedpag.equals(AcumuladorVentasCodMedPagos.PT_FINANCIACION)) {
						acumVentas.setnVentasFinanciacionPt(1L);
					}
					else if (desmedpag.equals(AcumuladorVentasCodMedPagos.TARJETA_MANUAL)) {
						acumVentas.setnVentasTarjManual(1L);
					}
					else if (desmedpag.equals(AcumuladorVentasCodMedPagos.GIFTCARD)) {
						acumVentas.setnVentasTarjRegalo(1L);
					}
					else if (desmedpag.equals(AcumuladorVentasCodMedPagos.VALE)) {
						acumVentas.setnVentasVale(1L);
					}
					else if (desmedpag.equals(AcumuladorVentasCodMedPagos.TARJETA)) {
						acumVentas.setnVentasTarjeta(1L);
					}
					else if (desmedpag.equals(AcumuladorVentasCodMedPagos.TRANSFERENCIA)) {
						acumVentas.setnVentasTransferencia(1L);
					}
				}

				if (XMLDocumentUtils.getElement(cabecera, "datos_fidelizacion", true) != null) {
					acumVentas.setnVentasFidelizado(1L);
				}
			}

			Long numTickets = acumVentas.getNumTickets();
			BigDecimal unidadesArticulos = BigDecimal.valueOf(acumVentas.getUnidadesArticulos());
			BigDecimal cestaMediaCantidad = unidadesArticulos.divide(BigDecimal.valueOf(numTickets), 2, RoundingMode.HALF_UP);
			acumVentas.setCestaMediaCantidad(cestaMediaCantidad);

			BigDecimal valueOf = BigDecimal.valueOf(numTickets).setScale(4);
			BigDecimal cestaMediaImporte = acumVentas.getImporteTotal().divide(valueOf, 2, RoundingMode.HALF_UP);
			acumVentas.setCestaMediaImporte(cestaMediaImporte);

			String esVentaAutonoma = XMLDocumentUtils.getTagValueAsString(cabecera, "intervencion", true);
			if (StringUtils.isBlank(esVentaAutonoma)) {
				acumVentas.setnVentasAutonomas(acumVentas.getnVentasAutonomas() + 1);
			}

			if (registro != null) {
				ServicioAcumuladorVentasImpl.get().actualizarAcumuladorVenta(acumVentas, sqlSession);
			}
			else {
				ServicioAcumuladorVentasImpl.get().insertarAcumuladorVenta(acumVentas, sqlSession);
			}
		}
	}
	
	
	public AcumuladorVentas consultarRegistroExistente(SqlSession sqlSession,String codCaja,String codAlm, String uidActividad,Date fecha,String fechaAsString) {
		log.debug("consultarRegistroExistente() - Consultando registro para la fecha : "+fecha);
		AcumuladorVentas acum = null; 
		try {
			AcumuladorVentasMapper mapper = sqlSession.getMapper(AcumuladorVentasMapper.class);
			AcumuladorVentasKey key = new AcumuladorVentasKey();
			key.setUidActividad(uidActividad);
			key.setCodalm(codAlm);
			key.setCodcaja(codCaja);
			key.setFecha(fecha);
			acum = mapper.selectByPrimaryKey(key);
			
		}catch (Exception e) {
			log.warn("consultarRegistroExistente() - ",e);
		}
		
		return acum;
	}
	
	public Long getTotalUnidadesArticulosVenta(List<Element> listaLineas) throws XMLDocumentException{
		log.debug("getTotalUnidadesArticulosVenta()");
		Long cantidadVenta = 0L;
		for (Element linea : listaLineas) {
			Double cantidadLineaDouble = XMLDocumentUtils.getTagValueAsDouble(linea, "cantidad", false);
			Long cantidadLinea = cantidadLineaDouble.longValue();
			cantidadVenta += cantidadLinea;
		}
		return cantidadVenta;
	}
}
