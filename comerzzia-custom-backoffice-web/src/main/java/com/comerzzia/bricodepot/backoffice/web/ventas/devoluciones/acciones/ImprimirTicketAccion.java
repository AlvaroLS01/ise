/**
 * ComerZZia 3.0 Copyright (c) 2008-2015 Comerzzia, S.L. All Rights Reserved. THIS WORK IS SUBJECT TO SPAIN AND
 * INTERNATIONAL COPYRIGHT LAWS AND TREATIES. NO PART OF THIS WORK MAY BE USED, PRACTICED, PERFORMED COPIED,
 * DISTRIBUTED, REVISED, MODIFIED, TRANSLATED, ABRIDGED, CONDENSED, EXPANDED, COLLECTED, COMPILED, LINKED, RECAST,
 * TRANSFORMED OR ADAPTED WITHOUT THE PRIOR WRITTEN CONSENT OF COMERZZIA, S.L. ANY USE OR EXPLOITATION OF THIS WORK
 * WITHOUT AUTHORIZATION COULD SUBJECT THE PERPETRATOR TO CRIMINAL AND CIVIL LIABILITY. CONSULT THE END USER LICENSE
 * AGREEMENT FOR INFORMATION ON ADDITIONAL RESTRICTIONS.
 */
package com.comerzzia.bricodepot.backoffice.web.ventas.devoluciones.acciones;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.comerzzia.bricodepot.backoffice.services.ventas.facturas.CargarFacturaA4Servicio;
import com.comerzzia.bricodepot.backoffice.services.ventas.tickets.BricodepotServicioTicketsImpl;
import com.comerzzia.bricodepot.backoffice.util.marshall.MarshallUtil;
import com.comerzzia.core.model.informes.TrabajoInformeBean;
import com.comerzzia.core.model.tiposdocumentos.TipoDocumentoBean;
import com.comerzzia.core.model.ventas.tickets.TicketBean;
import com.comerzzia.core.servicios.informes.InformeException;
import com.comerzzia.core.servicios.informes.InformeNotFoundException;
import com.comerzzia.core.servicios.informes.InformesService;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.servicios.tipodocumento.ServicioTiposDocumentosImpl;
import com.comerzzia.core.servicios.tipodocumento.TipoDocumentoException;
import com.comerzzia.core.servicios.tipodocumento.TipoDocumentoNotFoundException;
import com.comerzzia.core.servicios.ventas.tickets.TicketException;
import com.comerzzia.core.servicios.ventas.tickets.TicketNotFoundException;
import com.comerzzia.core.util.base64.Base64Coder;
import com.comerzzia.core.util.xml.XMLDocumentException;
import com.comerzzia.omnichannel.documentos.facturas.converters.albaran.ticket.LineaTicket;
import com.comerzzia.omnichannel.documentos.facturas.converters.albaran.ticket.PagoTicket;
import com.comerzzia.omnichannel.documentos.facturas.converters.albaran.ticket.TicketVentaAbono;
import com.comerzzia.web.base.InformeAccion;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@SuppressWarnings({ "deprecation" })
public class ImprimirTicketAccion extends InformeAccion {
	
	protected static final String ATCUD = "ATCUD";
	protected static final String QR ="QR";
	protected static final String PORTUGAL = "PT";
	protected static final String ESPANYA ="ES";
	protected static final String CATALUNYA ="CA";
	
	@Autowired
	protected InformesService informesService;
	@Autowired
	protected CargarFacturaA4Servicio cargarFacturaA4Servicio;
	
	public String getNombreInforme() {
		return "ventas.facturas.facturaA4";
	}

	public Vista vistaInforme(HttpServletRequest request) {

		return new Vista("backoffice/devoluciones/buscar/jsp/buscar.jsp", Vista.INTERNA);
	}

	public void imprimirInforme(HttpServletRequest request, TrabajoInformeBean trabajoInforme) throws InformeException {
		log.debug("imprimirInforme() - Empezando trabajo de impresión de devoluciones.");
		HttpSession sesion = request.getSession();
		DatosSesionBean datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);
		String uidTicket = request.getParameter("uidTicket");
		Long reportVersion = StringUtils.isNotBlank(request.getParameter("reportVersion")) ? Long.valueOf(request.getParameter("reportVersion")) : 0l;
		try {
			TicketBean ticketBean = BricodepotServicioTicketsImpl.get().consultarTicketUid(datosSesion, uidTicket);
			MarshallUtil marshallUtil = new MarshallUtil();
			TicketVentaAbono ticket = (TicketVentaAbono) marshallUtil.unmarshal(ticketBean.getTicket(), TicketVentaAbono.class);
			String fechaOrigen = obtenerFechaOrigen(ticketBean.getXml());
			if (fechaOrigen != null) {
				trabajoInforme.addParametro("fecha_origen", fechaOrigen);
			}
			trabajoInforme.addParametro("esDuplicado", true);
			
			// Medios de pago
			cargarFacturaA4Servicio.generarMediosPago(ticket, datosSesion);

			cargarFacturaA4Servicio.cargarPromociones(ticketBean, trabajoInforme);
			cargarFacturaA4Servicio.cargarDatosPagoTarjeta(ticketBean, ticket, trabajoInforme);
			/*Para que si hay direferentes lienas con el mismo codigo, salgan en una solo*/
			List<LineaTicket> lineasAgrupadas = cargarFacturaA4Servicio.getLineasAgrupadas(ticket, trabajoInforme);
			trabajoInforme.addParametro("lineasAgrupadas", lineasAgrupadas);
			
			trabajoInforme.addParametro("ticket", ticket);
			trabajoInforme.addParametro("ticketVentaAbono", ticket);
			
			if(StringUtils.isNotBlank(getNumPedido(ticket))){
				trabajoInforme.addParametro("numPedido", getNumPedido(ticket));
			}
			
			tratarTicketPorPais(trabajoInforme, ticketBean, ticket, datosSesion, reportVersion);
		}
		catch (TicketException e) {
			log.error("imprimirInforme() - " + e.getMessage());
			String mensaje = "Error al consultar ticket: " + e.getMessage();
			throw new TicketException(mensaje, e);
		}
		catch (TicketNotFoundException e) {
			log.error("imprimirInforme() - Ticket no encontrado " + e.getMessage());
		}
		catch (XMLDocumentException e) {
			log.error("imprimirInforme() - Error en el documento xml" + e.getMessage());
		}
		catch (InformeNotFoundException e) {
			log.error("imprimirInforme() - Error informe no encontrado" + e.getMessage());
		}
		catch (SQLException e) {
			log.error("imprimirInforme() - Error al consultar la base de datos" + e.getMessage());
		}
		catch (IOException e) {
			log.error("imprimirInforme() - Ha ocurrido un error " + e.getMessage());
		}
		catch (Exception e) {
			log.error("imprimirInforme() - Ha ocurrido un error " + e.getMessage());
		}

	}
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


	public String obtenerFechaOrigen(Document document) {
		log.debug("obtenerFechaOrigen() - obteniendo fecha de origen de venta del xml");
		String fecha = null;
		
		if (document.getElementsByTagName("fechaTicketOrigen") != null && document.getElementsByTagName("fechaTicketOrigen").item(0) != null) {
			fecha = document.getElementsByTagName("fechaTicketOrigen").item(0).getTextContent();
			log.debug("obtenerFechaOrigen() - fecha de origen de venta : " + fecha);
		}
		return fecha;		
	}

	/**
	 * @param document
	 * @param nodo
	 * @return String con el valor de una etiqueta de un xml.
	 */
	public static String obtenerValorTagXml(Document document, String nodo) {
		log.debug("obtenerValorTagXml() - Obteniendo valor de una etiqueta xml");
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

	/**
	 * @param qrData
	 * @param trabajoInforme
	 * @throws Exception
	 * @throws IOException
	 *             Añadimos Imagen QR al informe como parámetro.
	 */
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
	
	public String getNumPedido(TicketVentaAbono ticket) {
		
		for (PagoTicket pago : ticket.getPagos()) {
			if(pago.getExtendedData()!=null && !pago.getExtendedData().isEmpty() && pago.getExtendedData().containsKey("num_pedido")) {
				return (String) pago.getExtendedData().get("num_pedido");
			}
		}			
		
		return null;
	}
}
