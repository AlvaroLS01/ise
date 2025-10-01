package com.comerzzia.bricodepot.backoffice.web.facturas.acciones;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.comerzzia.aena.util.xml.MarshallUtil;
import com.comerzzia.bricodepot.backoffice.services.ventas.facturas.CargarFacturaA4Servicio;
import com.comerzzia.core.model.actividades.ActividadBean;
import com.comerzzia.core.model.informes.TrabajoInformeBean;
import com.comerzzia.core.model.tiposdocumentos.TipoDocumentoBean;
import com.comerzzia.core.model.ventas.tickets.TicketBean;
import com.comerzzia.core.servicios.informes.InformeException;
import com.comerzzia.core.servicios.informes.InformesService;
import com.comerzzia.core.servicios.instancias.ServicioInstanciasImpl;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.servicios.tipodocumento.ServicioTiposDocumentosImpl;
import com.comerzzia.core.servicios.tipodocumento.TipoDocumentoException;
import com.comerzzia.core.servicios.tipodocumento.TipoDocumentoNotFoundException;
import com.comerzzia.core.servicios.ventas.tickets.TicketService;
import com.comerzzia.core.util.base64.Base64Coder;
import com.comerzzia.core.util.config.AppInfo;
import com.comerzzia.core.util.xml.XMLDocument;
import com.comerzzia.core.util.xml.XMLDocumentNode;
import com.comerzzia.core.util.xml.XMLDocumentNodeNotFoundException;
import com.comerzzia.model.fidelizacion.tarjetas.TarjetaBean;
import com.comerzzia.omnichannel.documentos.facturas.converters.albaran.ticket.LineaTicket;
import com.comerzzia.omnichannel.documentos.facturas.converters.albaran.ticket.TicketVentaAbono;
import com.comerzzia.servicios.fidelizacion.tarjetas.ServicioTarjetasImpl;
import com.comerzzia.servicios.fidelizacion.tarjetas.TarjetaNotFoundException;
import com.comerzzia.web.base.InformeAccion;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class ImprimirAccion extends InformeAccion {

	public static final Vista NEXT_PAGE = new Vista("", Vista.INTERNA);
	protected static final String ATCUD = "ATCUD";
	protected static final String QR ="QR";
	protected static final String PORTUGAL = "PT";
	protected static final String ESPANYA ="ES";
	protected static final String CATALUNYA ="CA";

	private Logger log = Logger.getLogger(ImprimirAccion.class);

	TicketVentaAbono ticketVenta = null;
	TicketBean ticket = null;

	@Autowired
	CargarFacturaA4Servicio cargarFacturaA4Servicio;

	@Autowired
	InformesService informesService;

	@Autowired
	TicketService ticketService;
	
	@Override
	public Vista vistaInforme(HttpServletRequest request) {
		return NEXT_PAGE;
	}

	@SuppressWarnings({ "deprecation" })
	@Override
	public void imprimirInforme(HttpServletRequest request, TrabajoInformeBean trabajoInforme) throws InformeException {
		log.debug("imprimirInforme() - Obteniendo ticket seleccionado con codigo: " + request.getParameter("referenciaClientes"));

		HttpSession sesion = request.getSession();
		DatosSesionBean datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);

		Long reportVersion = StringUtils.isNotBlank(request.getParameter("reportVersion")) ? Long.valueOf(request.getParameter("reportVersion")) : 0l;
		if (reportVersion != 0l) {
			try {
				// UID_ACTIVIDAD
				trabajoInforme.addParametro("UID_ACTIVIDAD", datosSesion.getUidActividad());
				String uidTicket = request.getParameter("uidTicket");
				ticket = ticketService.consultarTicketUid(datosSesion, uidTicket);

				log.debug("imprimirInforme() - Parseando a XML el ticket");
				ticketVenta = (TicketVentaAbono) MarshallUtil.leerXML(ticket.getTicket(), TicketVentaAbono.class);
				
		
				if (ticketVenta == null) {
					String msgError = "Error al parsear el xml del ticket en un objeto 'TicketVentaAbono.class'.";
					throw new Exception(msgError);
				}
				
				
				if (ticket.getFecha() != null) {
						trabajoInforme.addParametro("fecha", ticket.getFecha());
				}
				if (StringUtils.isNotBlank(ticket.getLocatorId())) {
						trabajoInforme.addParametro("LOCATOR_ID", ticket.getLocatorId());
				}

				trabajoInforme.addParametro("esDuplicado", true);
				
				String fechaOrigen = obtenerFechaOrigen(ticket.getXml());
				if (fechaOrigen != null) {
					trabajoInforme.addParametro("fecha_origen", fechaOrigen);
				}
				
				// UID_INSTANCIA para el logo
				ActividadBean actividad = ServicioInstanciasImpl.get().consultarActividad(ticket.getUidActividad());
				trabajoInforme.addParametro("UID_INSTANCIA", actividad.getUidInstancia());
				
				// Medios de pago
				cargarFacturaA4Servicio.getPagoGiftCard(ticketVenta, trabajoInforme);
				cargarFacturaA4Servicio.generarMediosPago(ticketVenta, datosSesion);

				cargarFacturaA4Servicio.cargarPromociones(ticket, trabajoInforme);
				cargarFacturaA4Servicio.cargarDatosPagoTarjeta(ticket, ticketVenta, trabajoInforme);
				trabajoInforme.addParametro("DEVOLUCION", false);
				TipoDocumentoBean tipoDocumentoTicket = ServicioTiposDocumentosImpl.get().consultar(datosSesion, ((TicketVentaAbono) ticketVenta).getCabecera().getTipoDocumento());
				if (ticketVenta.getCabecera().getCodTipoDocumento().equals("FT") || ticketVenta.getCabecera().getCodTipoDocumento().equals("FR")
				        || (ticketVenta.getCabecera().getCodTipoDocumento().equals("NC") && !tipoDocumentoTicket.getCodPais().equals(ESPANYA))) {
					if (ticketVenta.getCabecera().getCodTipoDocumento().equals("FR")) {
						trabajoInforme.addParametro("DEVOLUCION", true);
					}
				}
				
				trabajoInforme.addParametro("ticket", ticketVenta);
				
				calcularGiftCardsTicket ( trabajoInforme, datosSesion);
				
				// Obtener el parametro get numPedido de la etiqueta documento de pagos
				Document document = ticket.getXml();
				String documento = getNumPedido(document);
				if (documento != null) {
				    trabajoInforme.addParametro("numPedido", documento);
				}
				// Subreportes
				String ruta = AppInfo.getInformesInfo().getRutaBase() + "ventas/facturas/";
				if (StringUtils.isNotBlank(ruta)) {
					trabajoInforme.addParametro("SUBREPORT_DIR", cargarFacturaA4Servicio.getSubReportDir(ruta));
				}
				/*Para que si hay direferentes lienas con el mismo codigo, salgan en una solo*/
				List<LineaTicket> lineasAgrupadas = cargarFacturaA4Servicio.getLineasAgrupadas(ticketVenta, trabajoInforme);
				trabajoInforme.addParametro("lineasAgrupadas", lineasAgrupadas);
				tratarTicketPorPais(trabajoInforme, ticket, ticketVenta, datosSesion, reportVersion);
				
			}
			catch (Exception e) {
				log.error("imprimirInforme() - Ha ocurrido un error: ", e);
			}
		}
	}
	
	public void calcularGiftCardsTicket (TrabajoInformeBean trabajoInforme, DatosSesionBean datosSesion) throws TarjetaNotFoundException {
		if(ticketVenta.getCabecera().getTarjetaRegalo() != null) {
			List<String> tarjetasRegaloTicket = new ArrayList<String>();
			Double totalSaldoGiftCard = 0.0;
			if(StringUtils.isNotBlank(ticketVenta.getCabecera().getTarjetaRegalo().getNumTarjetaRegalo())) {
				log.debug("imprimirInforme() - Consultando tarjeta num=" + ticketVenta.getCabecera().getTarjetaRegalo().getNumTarjetaRegalo() + ", uidTicket=" + ticket.getUidTicket());
				if (ticketVenta.getCabecera().getTarjetaRegalo().getNumTarjetaRegalo().contains("/")) {
					tarjetasRegaloTicket = Arrays.asList(
						    ticketVenta.getCabecera().getTarjetaRegalo().getNumTarjetaRegalo().split("/")
						);
				} else {
					tarjetasRegaloTicket.add(ticketVenta.getCabecera().getTarjetaRegalo().getNumTarjetaRegalo());
				}
				for (String tarjetaRegalo : tarjetasRegaloTicket) {
					TarjetaBean tarjetaGiftCard = ServicioTarjetasImpl.get().consultarTarjetaPorNumero(tarjetaRegalo, datosSesion);
					totalSaldoGiftCard = totalSaldoGiftCard + tarjetaGiftCard.getSaldoProvisional()+ tarjetaGiftCard.getSaldo();
				}
				trabajoInforme.addParametro("totalSaldoGiftCard", totalSaldoGiftCard);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void tratarTicketPorPais(TrabajoInformeBean trabajoInforme, TicketBean ticketBean, TicketVentaAbono ticket,DatosSesionBean datosSesion,Long reportVersion) throws IOException, Exception {
		log.debug("tratarTicketPorPais() - Tratando el informe segun el pais del tipo de documento");
		Long idTipoDocumento = ticket.getCabecera().getTipoDocumento();
		try {
			TipoDocumentoBean tipoDocumentoTicket = ServicioTiposDocumentosImpl.get().consultar(datosSesion, idTipoDocumento);

			if (tipoDocumentoTicket.getCodPais().equals(ESPANYA)) {
				reportVersion = 1L;
			}

			if (tipoDocumentoTicket.getCodPais().equals(PORTUGAL)) {
				reportVersion = 2L;
				String atcud = obtenerValorTagXml(ticketBean.getXml(), ATCUD);
				String qrData = obtenerValorTagXml(ticketBean.getXml(), QR);
				if (StringUtils.isNotBlank(atcud)) {
					trabajoInforme.addParametro("fiscalData_ACTUD", atcud);
				}
				if (StringUtils.isNotBlank(atcud)) {
					trabajoInforme.addParametro("fiscalData_QR", obtenerValorTagXml(ticketBean.getXml(), QR));
					addQR(qrData, trabajoInforme);
				}

			}

			if (tipoDocumentoTicket.getCodPais().equals(CATALUNYA)) {
				reportVersion = 3L;
			}
			log.debug("imprimirInforme() - Obteniendo version del informe.");
			trabajoInforme.setVersion(informesService.consultarVersionInforme(trabajoInforme.getIdInforme(), reportVersion));
		}
		catch (TipoDocumentoNotFoundException | TipoDocumentoException e) {
			log.debug("tratarTicketPorPais() - Documento no encontrado :" + e.getMessage());
			throw e;
		}
	}

	@Override
	public String getNombreInforme() {

		return "ventas.facturas.facturaA4";
	}
	
	public static String obtenerValorTagXml(Document document, String nodo) {
		NodeList fiscalDataNodes = document.getElementsByTagName("fiscal_data");
		String valor = null;

		if (fiscalDataNodes.getLength() > 0) {
			Node fiscalDataNode = fiscalDataNodes.item(0);
			if (fiscalDataNode.getNodeType() == Node.ELEMENT_NODE) {
				Element fiscalDataElement = (Element) fiscalDataNode;
				NodeList propertyNodes = fiscalDataElement.getElementsByTagName("property");

				for (int i = 0; i < propertyNodes.getLength(); i++) {
					Element propertyElement = (Element) propertyNodes.item(i);
					String name = propertyElement.getElementsByTagName("name").item(0).getTextContent();
					if (nodo.equals(name)) {
						valor = propertyElement.getElementsByTagName("value").item(0).getTextContent();
					}
				}
			}
		}

		return valor;
	}
	
	public void addQR(String qrData, TrabajoInformeBean trabajoInforme) throws Exception, IOException {
		log.debug("addQR() - Añadiendo imagen QR");

		if (qrData != null) {
			Base64Coder coder = new Base64Coder(Base64Coder.UTF8);
			String qr = coder.decodeBase64(qrData);
			BufferedImage qrImage = generateQRCodeImage(qr);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(qrImage, "jpeg", os);
			InputStream is = new ByteArrayInputStream(os.toByteArray());
			trabajoInforme.addParametro("QR_PORTUGAL", is);
		}
		else {
			log.debug("addQr() - La información fiscal no viene en el ticket.");
		}
	}

	/**
	 * @param barcodeText
	 * @return BufferedImage de la imagen QR en base a su Base64
	 * @throws Exception
	 */
	private BufferedImage generateQRCodeImage(String barcodeText) throws Exception {
		log.debug("generateQRCodeImage() - Generando codigo de la imagen QR");
		QRCodeWriter barcodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200);
		return MatrixToImageWriter.toBufferedImage(bitMatrix);
	}
	

	public String obtenerFechaOrigen(Document document) {
		String fecha = null;
		if (document.getElementsByTagName("fechaTicketOrigen") != null && document.getElementsByTagName("fechaTicketOrigen").item(0) != null) {
			log.debug("obtenerFechaOrigen() - obteniendo fecha de origen de venta del xml");
			fecha = document.getElementsByTagName("fechaTicketOrigen").item(0).getTextContent();
			log.debug("obtenerFechaOrigen() - fecha de origen de venta : " + fecha);
		}
		return fecha;
	}
	
	public String getNumPedido(Document document) throws XMLDocumentNodeNotFoundException {
		XMLDocument xmlDoc = new XMLDocument(document);
		XMLDocumentNode pagosNode = xmlDoc.getNodo("pagos", true);
		List<XMLDocumentNode> pagos = pagosNode != null ? pagosNode.getHijos("pago") : Collections.emptyList();
		log.debug("getNumPedido() - total <pago>= " + pagos.size());
		if (pagosNode != null) {
			for (XMLDocumentNode pagoNode : pagosNode.getHijos("pago")) {
				int idx = 0;
				XMLDocumentNode extendedDataNode = pagoNode.getNodo("extendedData", true);
				log.debug("getNumPedido() - pago[" + idx++ + "] extendedData=" + (extendedDataNode != null ? extendedDataNode.toString() : "<null>"));
				if (extendedDataNode != null) {
					XMLDocumentNode documentoNode = extendedDataNode.getNodo("documento", true);
					if (documentoNode != null) {
						return documentoNode.getValue();
					}
					else {
						log.debug("getNumPedido() - No se encontró el nodo 'documento' dentro de 'extendedData'");
					}
				}
				else {
					log.debug("getNumPedido() - No se encontró el nodo 'extendedData' dentro del nodo 'pago'");
				}
			}
			log.debug("getNumPedido() - No se encontró ningún nodo 'documento' en los nodos 'pago'");
		}
		else {
			log.debug("getNumPedido() - No se encontró el nodo 'pagos'");
		}
		return null;
	}


}

