package com.comerzzia.bricodepot.backoffice.services.conversion;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.comerzzia.bricodepot.backoffice.persistence.albaranes.AlbaranCabecera;
import com.comerzzia.bricodepot.backoffice.persistence.albaranes.AlbaranCabeceraExample;
import com.comerzzia.bricodepot.backoffice.persistence.albaranes.AlbaranCabeceraMapper;
import com.comerzzia.bricodepot.backoffice.persistence.conversion.Conversion;
import com.comerzzia.bricodepot.backoffice.persistence.conversion.ConversionExample;
import com.comerzzia.bricodepot.backoffice.persistence.conversion.ConversionMapper;
import com.comerzzia.bricodepot.backoffice.services.ventas.tickets.BricodepotServicioTicketsImpl;
import com.comerzzia.bricodepot.backoffice.services.ventas.tickets.CustomTicketParser;
import com.comerzzia.bricodepot.backoffice.util.marshall.MarshallUtil;
import com.comerzzia.core.model.empresas.ConfigEmpresaBean;
import com.comerzzia.core.model.ventas.tickets.TicketBean;
import com.comerzzia.core.model.ventas.tickets.TicketExample;
import com.comerzzia.core.persistencia.ventas.tickets.TicketMapper;
import com.comerzzia.core.servicios.contadores.ContadorException;
import com.comerzzia.core.servicios.contadores.ServicioContadoresImpl;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.servicios.tipodocumento.ServicioTiposDocumentosImpl;
import com.comerzzia.core.servicios.tipodocumento.TipoDocumentoException;
import com.comerzzia.core.servicios.tipodocumento.TipoDocumentoNotFoundException;
import com.comerzzia.core.servicios.ventas.tickets.ServicioTicketsImpl;
import com.comerzzia.core.servicios.ventas.tickets.TicketException;
import com.comerzzia.core.servicios.ventas.tickets.TicketNotFoundException;
import com.comerzzia.core.util.base.KeyConstraintViolationException;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.core.util.db.Database;
import com.comerzzia.core.util.xml.XMLDocumentException;
import com.comerzzia.core.util.xml.XMLDocumentUtils;
import com.comerzzia.model.ventas.albaranes.articulos.ArticuloAlbaranVentaBean;
import com.comerzzia.omnichannel.documentos.facturas.converters.albaran.ticket.CabeceraTicket;
import com.comerzzia.omnichannel.documentos.facturas.converters.albaran.ticket.DatosDocumentoOrigenTicket;
import com.comerzzia.omnichannel.documentos.facturas.converters.albaran.ticket.LineaTicket;
import com.comerzzia.omnichannel.documentos.facturas.converters.albaran.ticket.PagoTicket;
import com.comerzzia.omnichannel.documentos.facturas.converters.albaran.ticket.TicketVentaAbono;
import com.comerzzia.persistencia.ventas.albaranes.articulos.ArticulosAlbaranesVentasDao;
import com.comerzzia.persistencia.ventas.albaranes.articulos.ParCantidadLineaBean;
import com.comerzzia.servicios.ventas.albaranes.AlbaranVenta;

@SuppressWarnings("deprecation")
public class ConversionServiceImpl {
	
	private static Logger log = Logger.getLogger(ConversionServiceImpl.class);
	

	protected static ConversionServiceImpl instance;

	public static ConversionServiceImpl get() {
		if (instance == null) {
			instance = new ConversionServiceImpl();
		}
		return instance;
	}
	protected static JAXBContext jaxbContext;

	protected static Marshaller marshaller;
	
	public Conversion getConversion(String uidActividad, String uidTicket) throws TicketNotFoundException, ConversionPromocionesYDevolucionesException {
		SqlSession sqlSession = Database.getSqlSession();
		Connection conn = null;
		try {
			conn = new Connection(sqlSession.getConnection());
			AlbaranCabecera albaran = getAlbaranFromUidTicket(uidActividad, uidTicket, sqlSession);

			ConversionMapper mapper = sqlSession.getMapper(ConversionMapper.class);

			ConversionExample example = new ConversionExample();
			example.or().andUidActividadEqualTo(uidActividad).andUidTicketEqualTo(uidTicket);

			List<Conversion> lista = mapper.selectByExample(example);
			Conversion conversion = null;
			if (lista != null && !lista.isEmpty()) {
				conversion = lista.get(0);
			}
			
			if (conversion == null && ticketTienePromocionesYDevoluciones(conn, albaran)) {
				String msg = "No se permite la conversión de una factura con devoluciones y promociones al mismo tiempo";
				log.error("getConversion() - " + msg);
				conversion = new Conversion();
			}

			return conversion;

		} catch (Exception e) {
			String msg = "No se ha podido encontrar conversion con localizador " + uidTicket + " : " + e.getMessage();
			log.error("consultarUidDiarioCaja() - " + msg, e);
			throw new TicketNotFoundException(msg, e);
		}
		finally {
			if (conn != null) {
				conn.close();
			}
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
	}

	private AlbaranCabecera getAlbaranFromUidTicket(String uidActividad, String uidTicket, SqlSession sqlSession) throws TicketNotFoundException {
		AlbaranCabeceraMapper albaranMapper = sqlSession.getMapper(AlbaranCabeceraMapper.class);
		AlbaranCabeceraExample albaranExample = new AlbaranCabeceraExample();

		albaranExample.or().andUidActividadEqualTo(uidActividad).andUidTicketEqualTo(uidTicket);
		List<AlbaranCabecera> albaranes = albaranMapper.selectByExample(albaranExample);

		if (albaranes == null || albaranes.isEmpty()) {
			String msg = "No se ha encontrado albarán con UID Ticket: " + uidTicket + " y UID Actividad: " + uidActividad;
			log.error("getConversion() - " + msg);
			throw new TicketNotFoundException(msg);
		}

		return albaranes.get(0);
	}

	private boolean ticketTienePromocionesYDevoluciones(Connection conn, AlbaranCabecera albaran) throws SQLException, TicketNotFoundException {
		ConfigEmpresaBean configEmpresa = new ConfigEmpresaBean();
		configEmpresa.setUidActividad(albaran.getUidActividad());
		
		List<ParCantidadLineaBean> cantidadesDevueltas = ArticulosAlbaranesVentasDao.consultarCantidadDevuelta(conn, configEmpresa, albaran.getIdClieAlbaran());
		boolean tieneCantidadesDevueltas = cantidadesDevueltas != null && !cantidadesDevueltas.isEmpty();
		List<ArticuloAlbaranVentaBean> detalle = ArticulosAlbaranesVentasDao.consultar(conn, configEmpresa, albaran.getIdClieAlbaran());
		
		boolean tienePromociones = false;
		for (ArticuloAlbaranVentaBean articuloAlbaranVentaBean : detalle) {
			if(articuloAlbaranVentaBean.getIdPromocion() != null) {
				tienePromociones = true;
				break;
			}
		}
		
		return tienePromociones && tieneCantidadesDevueltas;
	}
	
	public void tratarTicketConvertido(DatosSesionBean datosSesion, TicketBean ticket, SqlSession sqlSession, AlbaranVenta albaran, CustomTicketParser ticketParser) throws XMLDocumentException {
		if (ticket.getCodTipoDocumento().equals("FT")) {
			Element root = ticket.getXml().getDocumentElement();
			Element cabecera = XMLDocumentUtils.getElement(root, "cabecera", false);
			Element datosDocumentoOrigen = XMLDocumentUtils.getElement(cabecera, "datos_documento_origen", true);
			if (datosDocumentoOrigen != null) {
				Conversion ticketConvertido = new Conversion();
				ticketConvertido.setUidActividad(datosSesion.getUidActividad());
				ticketConvertido.setFecha(albaran.getFecha());
				ticketConvertido.setFacturaTransformada("S");
				String uidTicket = XMLDocumentUtils.getTagValueAsString(datosDocumentoOrigen, "uidTicket", true);
				ticketConvertido.setUidTicket(uidTicket);
				ticketConvertido.setUidFacturaCompleta(ticket.getUidTicket());
				insertarTicketConvertido(sqlSession, ticketConvertido);
			}
		}
	}

	public void insertarTicketConvertido(SqlSession sqlSession, Conversion ticketConvertido) {
		log.debug("insertarTicketConvertido() - Insertando ticket : " + ticketConvertido.getUidTicket());

		ConversionMapper mapper = sqlSession.getMapper(ConversionMapper.class);
		mapper.insert(ticketConvertido);

	}

	public void convertir(DatosSesionBean datosSesion, String codigo) throws SAXException, IOException, XMLDocumentException, TicketException, TicketNotFoundException {
		Log.debug("convertir()");
		com.comerzzia.core.util.mybatis.session.SqlSession sqlSession = null;
		TicketBean ticketBean = null;
		sqlSession = new com.comerzzia.core.util.mybatis.session.SqlSession();
		sqlSession.openSession(datosSesion.getSqlSessionFactory().openSession());
		ticketBean = consultarTicket(datosSesion, sqlSession, codigo);
		MarshallUtil marshallUtil = new MarshallUtil();
		if (ticketBean == null) {
			throw new TicketException("No se ha encontrado ningun ticket con este codigo : " + codigo);
		}
		TicketVentaAbono ticket = (TicketVentaAbono) marshallUtil.unmarshal(ticketBean.getTicket(), TicketVentaAbono.class);

		try {
			generarDevolucion(datosSesion, ticket);
		}
		catch (Exception e) {
			log.error("convertir() - Ha ocurrido una error : " + e.getMessage());
		}

	}

	private void generarDevolucion(DatosSesionBean datosSesion, TicketVentaAbono ticket) throws Exception {
		log.debug("generarDevolucion() - generando devolucion del ticket :" + ticket.getCabecera().getUidTicket());
		DatosDocumentoOrigenTicket datosDocOrigen = new DatosDocumentoOrigenTicket();
		datosDocOrigen.setCodTicket(ticket.getCabecera().getCodTicket());
		datosDocOrigen.setCaja(ticket.getCabecera().getCodCaja());
		datosDocOrigen.setCodTipoDoc(ticket.getCabecera().getCodTipoDocumento());
		datosDocOrigen.setFecha(ticket.getCabecera().getFecha());
		datosDocOrigen.setIdTipoDoc(ticket.getCabecera().getTipoDocumento());
		datosDocOrigen.setUidTicket(ticket.getCabecera().getUidTicket());
		datosDocOrigen.setSerie(ticket.getCabecera().getSerieTicket());
		com.comerzzia.core.model.tiposdocumentos.TipoDocumentoBean tipoDocumentoNC = ServicioTiposDocumentosImpl.get().consultar(datosSesion, "NC",
		        datosSesion.getEmpresa().getDatosEmpresa().getCodPais());
		ticket.getCabecera().setCodTipoDocumento(tipoDocumentoNC.getCodTipoDocumento());
		ticket.getCabecera().setTipoDocumento(tipoDocumentoNC.getIdTipoDocumento());
		ticket.getCabecera().setDesTipoDocumento(tipoDocumentoNC.getDesTipoDocumento());
		ticket.getCabecera().setUidTicket(UUID.randomUUID().toString());

		List<PagoTicket> pagos = ticket.getPagos();
		for (PagoTicket pago : pagos) {
			pago.setImporte(pago.getImporte().negate());
		}
		SqlSession sqlSession = null;
		try {
			sqlSession = datosSesion.getSqlSessionFactory().openSession();
			insertarDevolucion(datosSesion, sqlSession, ticket, tipoDocumentoNC);
			insertarFT(datosSesion, sqlSession, ticket);
			sqlSession.commit();
		}
		catch (Exception e) {

		}
		finally {
			sqlSession.close();
		}

	}

	private void insertarFT(DatosSesionBean datosSesion, SqlSession sqlSession, TicketVentaAbono ticket)
	        throws TipoDocumentoNotFoundException, TipoDocumentoException, ContadorException, TicketException, KeyConstraintViolationException {
		log.debug("insertarFT() - generando FT del ticket :" + ticket.getCabecera().getUidTicket());
		com.comerzzia.core.model.tiposdocumentos.TipoDocumentoBean tipoDocumentoNC = ServicioTiposDocumentosImpl.get().consultar(datosSesion, "FT",
		        datosSesion.getEmpresa().getDatosEmpresa().getCodPais());
		ticket.getCabecera().setCodTipoDocumento(tipoDocumentoNC.getCodTipoDocumento());
		ticket.getCabecera().setTipoDocumento(tipoDocumentoNC.getIdTipoDocumento());
		ticket.getCabecera().setDesTipoDocumento(tipoDocumentoNC.getDesTipoDocumento());
		ticket.getCabecera().setUidTicket(UUID.randomUUID().toString());
		ticket.getCabecera().setIdTicket(ServicioContadoresImpl.get().obtenerValorContador(datosSesion, tipoDocumentoNC.getIdContador()));
		ticket.getCabecera().setCodTicket(ticket.getCabecera().getCodTipoDocumento() + " " + ticket.getCabecera().getFecha().substring(6, 10) + ticket.getCabecera().getCliente().getCodCliente()
		        + ticket.getCabecera().getCodCaja() + "/" + StringUtils.leftPad(ticket.getCabecera().getIdTicket().toString(), 8, "0"));
		TicketBean ticketBean = new TicketBean();
		ticketBean.setUidActividad(ticket.getCabecera().getUidActividad());
		ticketBean.setCodAlmacen(ticket.getCabecera().getTienda().getCodAlmacen());
		ticketBean.setCodCaja(ticket.getCabecera().getCodCaja());

		ticketBean.setFecha(new Date());
		ticketBean.setIdTicket(ticket.getCabecera().getIdTicket());
		ticketBean.setIdTipoDocumento(ticket.getCabecera().getTipoDocumento());
		ticketBean.setProcesado(TicketBean.PENDIENTE);
		ticketBean.setFirma("*");
		ticket.getCabecera().setUidTicket(UUID.randomUUID().toString());
		ticketBean.setUidTicket(ticket.getCabecera().getUidTicket());
		List<PagoTicket> pagos = ticket.getPagos();
		for (PagoTicket pago : pagos) {
			pago.setImporte(pago.getImporte().abs());
		}
		ServicioTicketsImpl.get().insertaTicket(sqlSession, ticketBean);

	}

	private void insertarDevolucion(DatosSesionBean datosSesion, SqlSession sqlSession, TicketVentaAbono ticket, com.comerzzia.core.model.tiposdocumentos.TipoDocumentoBean tipoDocumentoNC)
	        throws Exception {
		log.debug("insertarDevolucion() - insertando devolucion del ticket :" + ticket.getCabecera().getUidTicket());
		ticket.getCabecera().setIdTicket(ServicioContadoresImpl.get().obtenerValorContador(datosSesion, tipoDocumentoNC.getIdContador()));
		ticket.getCabecera().setCodTicket(ticket.getCabecera().getCodTipoDocumento() + " " + ticket.getCabecera().getFecha().substring(6, 10) + ticket.getCabecera().getCliente().getCodCliente()
		        + ticket.getCabecera().getCodCaja() + "/" + StringUtils.leftPad(ticket.getCabecera().getIdTicket().toString(), 8, "0"));
		TicketBean ticketBean = new TicketBean();
		ticketBean.setUidActividad(ticket.getCabecera().getUidActividad());
		ticketBean.setCodAlmacen(ticket.getCabecera().getTienda().getCodAlmacen());
		ticketBean.setCodCaja(ticket.getCabecera().getCodCaja());

		ticketBean.setFecha(new Date());
		ticketBean.setIdTicket(ticket.getCabecera().getIdTicket());
		ticketBean.setIdTipoDocumento(ticket.getCabecera().getTipoDocumento());
		ticketBean.setProcesado(TicketBean.PENDIENTE);
		ticketBean.setFirma("*");

		byte[] xmlTicket = transformarTicket(ticket);
		ticketBean.setTicket(xmlTicket);
		ticketBean.setUidTicket(ticket.getCabecera().getUidTicket());
		ServicioTicketsImpl.get().insertaTicket(sqlSession, ticketBean);

	}

	protected byte[] transformarTicket(TicketVentaAbono ticketVentaAbono) throws Exception {
		log.debug("transformarTicket() - Transformando ticket en XML");

		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			// Crear el contexto JAXB y el marshaller
			if (jaxbContext == null) {
				jaxbContext = JAXBContext.newInstance(TicketVentaAbono.class, CabeceraTicket.class, LineaTicket.class, HashMap.class);
			}
			if (marshaller == null) {
				marshaller = jaxbContext.createMarshaller();

				// Configurar el marshaller para que produzca un XML con formato
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			}

			// Convertir el objeto en XML
			marshaller.marshal(ticketVentaAbono, outputStream);

			// Obtener el arreglo de bytes resultante
			return outputStream.toByteArray();
		}

	}

	public TicketBean consultarTicket(DatosSesionBean datosSesion, com.comerzzia.core.util.mybatis.session.SqlSession sqlSession, String codigo) throws TicketException, TicketNotFoundException {
		log.debug("consultarTicket() - consultarTicket");
		TicketBean ticket = null;
		ConversionMapper conversionMapper = sqlSession.getMapper(ConversionMapper.class);
		ticket = conversionMapper.selectByLocalizador(datosSesion.getUidActividad(), codigo);
		if (ticket != null) {
			return ticket;
		}
		TicketMapper ticketMapper = sqlSession.getMapper(TicketMapper.class);
		TicketExample ticketExample = new TicketExample();
		ticketExample.or().andUidActividadEqualTo(datosSesion.getUidActividad()).andCodTicketEqualTo(codigo);
		List<TicketBean> select = ticketMapper.selectByExampleWithBLOBs(ticketExample);

		if (select != null && !select.isEmpty()) {
			ticket = select.get(0);
			return ticket;
		}

		ticket = BricodepotServicioTicketsImpl.get().consultarTicketUid(codigo, datosSesion.getConfigEmpresa().getUidActividad());
		return ticket;
	}
}
