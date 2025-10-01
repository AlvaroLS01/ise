package com.comerzzia.bricodepot.backoffice.services.ventas.facturas;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.comerzzia.bricodepot.backoffice.persistence.facturas.promociones.PromocionTicketDto;
import com.comerzzia.bricodepot.backoffice.persistence.pagos.mediospago.DatosPeticionPagoTarjetaDto;
import com.comerzzia.bricodepot.backoffice.persistence.pagos.mediospago.DatosRespuestaPagoTarjetaDto;
import com.comerzzia.bricodepot.backoffice.persistence.pagos.mediospago.MediosPago;
import com.comerzzia.bricodepot.backoffice.persistence.pagos.mediospago.MediosPagosMapper;
import com.comerzzia.core.model.informes.TrabajoInformeBean;
import com.comerzzia.core.model.ventas.tickets.TicketBean;
import com.comerzzia.core.servicios.sesion.IDatosSesion;
import com.comerzzia.core.util.base64.Base64Coder;
import com.comerzzia.core.util.db.Database;
import com.comerzzia.core.util.numeros.BigDecimalUtil;
import com.comerzzia.core.util.xml.XMLDocumentException;
import com.comerzzia.core.util.xml.XMLDocumentUtils;
import com.comerzzia.omnichannel.documentos.facturas.converters.albaran.ticket.LineaTicket;
import com.comerzzia.omnichannel.documentos.facturas.converters.albaran.ticket.PagoTicket;
import com.comerzzia.omnichannel.documentos.facturas.converters.albaran.ticket.SubtotalIvaTicket;
import com.comerzzia.omnichannel.documentos.facturas.converters.albaran.ticket.TarjetaRegaloTicket;
import com.comerzzia.omnichannel.documentos.facturas.converters.albaran.ticket.TicketVentaAbono;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@Service
public class CargarFacturaA4ServicioImpl implements CargarFacturaA4Servicio {

	private final Logger log = LoggerFactory.getLogger(CargarFacturaA4ServicioImpl.class);

	@Override
	public String getSubReportDir(String fileName) {
		log.debug("getSubReportDir() - Devolviendo dirección del SubReport.");

		int lastIndex1 = fileName.lastIndexOf("/");
		int lastIndex2 = fileName.lastIndexOf("\\");
		int lastIndex = lastIndex1;
		if (lastIndex2 > lastIndex1) {
			lastIndex = lastIndex2;
		}
		return fileName.substring(0, lastIndex + 1);
	}

	@Override
	public void generarMediosPago(TicketVentaAbono ticketVentaAbono, IDatosSesion datosSesion) throws Exception {
		log.debug("generarMediosPago() - Generando medios de pago.");

		SqlSession sqlSession = null;

		try {
			sqlSession = Database.getSqlSession();
			MediosPagosMapper mediosPagoMapper = sqlSession.getMapper(MediosPagosMapper.class);
			List<MediosPago> mediosPagoList = mediosPagoMapper.getMediosPago(ticketVentaAbono.getCabecera().getUidActividad(), ticketVentaAbono.getCabecera().getUidTicket());
			
			String numTarjetaReg= "";
			String uidTransaccion = "";
			BigDecimal saldo = new BigDecimal(0);
			BigDecimal importePago = new BigDecimal(0);
			BigDecimal importeRecarga = new BigDecimal(0);
			TarjetaRegaloTicket tarjetaRegalo = new TarjetaRegaloTicket();
			for (PagoTicket pago : ticketVentaAbono.getPagos()) {
				if(pago.getGiftcards()!=null && !pago.getGiftcards().isEmpty()) {
					numTarjetaReg = numTarjetaReg + " " + pago.getGiftcards().get(0).getNumTarjetaRegalo();
					uidTransaccion = pago.getGiftcards().get(0).getUidTransaccion();
					saldo = pago.getGiftcards().get(0).getSaldo();
					importePago = pago.getGiftcards().get(0).getImportePago();
					importeRecarga = pago.getGiftcards().get(0).getImporteRecarga();
				}
			}
			numTarjetaReg = numTarjetaReg.trim();
			numTarjetaReg = numTarjetaReg.replace(" ", "/");
	        if (numTarjetaReg.endsWith("/")) {
	            // Eliminar el último símbolo "/"
	        	numTarjetaReg = numTarjetaReg.substring(0, numTarjetaReg.length() - 1);
	        }			
	        tarjetaRegalo.setNumTarjetaRegalo(numTarjetaReg);
	        tarjetaRegalo.setUidTransaccion(uidTransaccion);
	        tarjetaRegalo.setSaldo(saldo);
	        tarjetaRegalo.setImporteRecarga(importeRecarga);
			ticketVentaAbono.getCabecera().setTarjetaRegalo(tarjetaRegalo);
			
			ticketVentaAbono.getPagos().clear();

			for (MediosPago pago : mediosPagoList) {
				PagoTicket pagos = new PagoTicket();
				pagos.getMedioPago().setCodMedioPago(pago.getCodMedioPago());
				pagos.getMedioPago().setDesMedioPago(pago.getDesMedioPago());
				pagos.setImporte(pago.getImporteTotal());
				ticketVentaAbono.getPagos().add(pagos);
			}			

		}
		catch (Exception e) {
			String mensajeError = "Se ha producido un error al consultar los medios de pago de la factura";
			log.error("generarMediosPago() - " + mensajeError, e);
			throw new Exception(mensajeError, e);
		}finally {
			if(sqlSession != null) {
				sqlSession.close();
			}
		}
	}

	@Override
	public void addFiscalData(TicketBean ticketBean, TrabajoInformeBean trabajoInforme) throws Exception, IOException {
		log.debug("addFiscalData() - Añadiendo datos fiscales al ticket");

		Document doc = ticketBean.getXml();
		Element root = doc.getDocumentElement();
		Element cabecera = XMLDocumentUtils.getElement(root, "cabecera", false);
		Element fiscalData = XMLDocumentUtils.getElement(cabecera, "fiscal_data", true);

		if (fiscalData != null) {
			List<Element> listaFiscalData = XMLDocumentUtils.getChildElements(fiscalData);

			for (Element element : listaFiscalData) {
				String name = XMLDocumentUtils.getTagValueAsString(element, "name", true);
				String value = XMLDocumentUtils.getTagValueAsString(element, "value", true);
				
				if (name != null && name.equals("QR")) {
					log.debug("addFiscalData() - Añadiendo QR");
					addQR(value, trabajoInforme);
				}

				if (name != null && name.equals("ATCUD")) {
					log.debug("addFiscalData() - Añadiendo ATCUD");
					trabajoInforme.addParametro("fiscalData_ACTUD", value);
				}
			}

		}
		else {
			log.debug("addFiscalData() - La información fiscal no viene en el ticket.");
		}

	}

	@Override
	public BufferedImage generateQRCodeImage(String barcodeText) throws Exception {
		log.debug("generateQRCodeImage() - Generando imagen QR");

		QRCodeWriter barcodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200);

		return MatrixToImageWriter.toBufferedImage(bitMatrix);
	}

	@Override
	public void cargarDatosPagoTarjeta(TicketBean ticketVenta, TicketVentaAbono ticketVentaAbono, TrabajoInformeBean trabajoInforme) {

		// Leemos xml para sacar los datos de peticion de pago tarjeta
		log.debug("cargarDatosPagoTarjeta() - Revisando si se ha pagado con tarjeta.");

		List<DatosPeticionPagoTarjetaDto> listaPagosTarjetaDatosPeticion = new ArrayList<DatosPeticionPagoTarjetaDto>();
		List<DatosRespuestaPagoTarjetaDto> listaPagosTarjeta = new ArrayList<DatosRespuestaPagoTarjetaDto>();
		try {
			Document doc = ticketVenta.getXml();
			Element root = doc.getDocumentElement();
			Element pagos = XMLDocumentUtils.getElement(root, "pagos", false);
			List<Element> listaPagos = XMLDocumentUtils.getChildElements(pagos);
			for (Element pago : listaPagos) {
				String nodeCodMegPag = XMLDocumentUtils.getTagValue(pago, "codmedpag", false);
				if (nodeCodMegPag.equals("0010")) {
					DatosPeticionPagoTarjetaDto datosPeticionObj = new DatosPeticionPagoTarjetaDto();
					DatosRespuestaPagoTarjetaDto datosRespuestaObj = new DatosRespuestaPagoTarjetaDto();
					if (XMLDocumentUtils.getElement(pago, "datosRespuestaPagoTarjeta", true) != null) {
						Element datosRespuestaPagoTarjeta = XMLDocumentUtils.getElement(pago, "datosRespuestaPagoTarjeta", true);
						Element datosPeticion = XMLDocumentUtils.getElement(datosRespuestaPagoTarjeta, "datosPeticion", true);
						Element cabecera = XMLDocumentUtils.getElement(root, "cabecera", true);
						Long idDocumento = null;
						String idTransaccion="";
						if (cabecera != null) {
							idDocumento = XMLDocumentUtils.getTagValueAsLong(datosPeticion, "idDocumento", true);
							idTransaccion = XMLDocumentUtils.getTagValueAsString(datosPeticion, "idTransaccion", true);
						}
						String fechaDocumentoOrigen ="";
						if (cabecera != null) {
							fechaDocumentoOrigen = XMLDocumentUtils.getTagValueAsString(cabecera, "fecha", true);
						}
						String tarjeta = "";
						String AID = "";
						String tipoLectura = "";
						String terminal = "";
						String comercio = "";
						String codAutorizacion = "";
						String ARC = "";
						String applicationLabel = "";
						String marcaTarjeta = "";
						
						if (datosRespuestaPagoTarjeta != null) {
							tarjeta = XMLDocumentUtils.getTagValueAsString(datosRespuestaPagoTarjeta, "tarjeta", true);
							
							AID = XMLDocumentUtils.getTagValueAsString(datosRespuestaPagoTarjeta, "AID", true);
							tipoLectura = XMLDocumentUtils.getTagValueAsString(datosRespuestaPagoTarjeta, "tipoLectura", true);
							comercio = XMLDocumentUtils.getTagValueAsString(datosRespuestaPagoTarjeta, "comercio", true);
							terminal = XMLDocumentUtils.getTagValueAsString(datosRespuestaPagoTarjeta, "terminal", true);
							codAutorizacion = XMLDocumentUtils.getTagValueAsString(datosRespuestaPagoTarjeta, "codAutorizacion", true);
							ARC = XMLDocumentUtils.getTagValueAsString(datosRespuestaPagoTarjeta, "ARC", true);
							applicationLabel = XMLDocumentUtils.getTagValueAsString(datosRespuestaPagoTarjeta, "applicationLabel", true);
							marcaTarjeta = XMLDocumentUtils.getTagValueAsString(datosRespuestaPagoTarjeta, "descripcionMarcaTarjeta", true);
	
							datosPeticionObj.setIdDocumento(idDocumento);
							datosPeticionObj.setIdTransaccion(idTransaccion);
							datosPeticionObj.setFechaDocumentoOrigen(fechaDocumentoOrigen);
	
							datosRespuestaObj.setTarjeta(tarjeta);
							datosRespuestaObj.setAID(AID);
							datosRespuestaObj.setTipoLectura(tipoLectura);
							datosRespuestaObj.setComercio(comercio);
							datosRespuestaObj.setTerminal(terminal);
							datosRespuestaObj.setCodAutorizacion(codAutorizacion);
							datosRespuestaObj.setARC(ARC);
							datosRespuestaObj.setApplicationLabel(applicationLabel);
							datosRespuestaObj.setMarcaTarjeta(marcaTarjeta);
						}
						listaPagosTarjetaDatosPeticion.add(datosPeticionObj);

						listaPagosTarjeta.add(datosRespuestaObj);
					}
				}
			}
			
			trabajoInforme.addParametro("listaPagosTarjetaDatosPeticion", listaPagosTarjetaDatosPeticion);
			trabajoInforme.addParametro("listaPagosTarjeta", listaPagosTarjeta);
		}
		catch (XMLDocumentException e) {
			log.error("cargarDatosPagoTarjeta() - Ha habido un error: ", e);
			trabajoInforme.addParametro("listaPagosTarjetaDatosPeticion", null);
			trabajoInforme.addParametro("listaPagosTarjeta", null);
		}
	}
	@Override
	public void cargarPromociones(TicketBean ticketVenta, TrabajoInformeBean trabajoInforme) throws XMLDocumentException {
		log.debug("cargarPromociones() - Recogiendo datos de las promociones...");

		Document doc = ticketVenta.getXml();
		Element root = doc.getDocumentElement();
		List<PromocionTicketDto> listaPromociones = new ArrayList<PromocionTicketDto>();
		Element promociones = XMLDocumentUtils.getElement(root, "promociones", true);

		if (promociones != null) {

			List<Element> listaPromocion = XMLDocumentUtils.getChildElements(promociones);
			String textoPromocion;

			for (Element promo : listaPromocion) {

				PromocionTicketDto promos = new PromocionTicketDto();

				Long idPromocion = XMLDocumentUtils.getTagValueAsLong(promo, "idpromocion", false);

				try {
					textoPromocion = XMLDocumentUtils.getTagValueAsString(promo, "texto_promocion", false);

				}
				catch (Exception e) {
					textoPromocion = "";
				}

				double importeTotalAhorro = XMLDocumentUtils.getTagValueAsDouble(promo, "importe_total_ahorro", false);

				promos.setIdPromocion(idPromocion);
				promos.setTextoPromocion(textoPromocion);
				promos.setImporteTotalAhorro(new BigDecimal(importeTotalAhorro));

				listaPromociones.add(promos);

			}
			trabajoInforme.addParametro("listaPromociones", listaPromociones);
		}
		else {
			log.debug("cargarPromociones()- No hay promociones");
		}

	}
	
	private void addQR(String value, TrabajoInformeBean trabajoInforme) throws Exception {
		log.debug("addQR()");
		
		Base64Coder coder = new Base64Coder(Base64Coder.UTF8);
		String qr = coder.decodeBase64(value);
		BufferedImage qrImage = generateQRCodeImage(qr);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(qrImage, "jpeg", os);
		InputStream is = new ByteArrayInputStream(os.toByteArray());
		
		trabajoInforme.addParametro("fiscalData_QR", value);
		trabajoInforme.addParametro("QR_PORTUGAL", is);
	}
	@Override
	public List<LineaTicket> getLineasAgrupadas(TicketVentaAbono ticketVenta,TrabajoInformeBean trabajoInforme) {
		List<LineaTicket> lineas = ticketVenta.getLineas();
		List<LineaTicket> lineasOriginales = lineas;
		try {

			List<Integer> lineasYaAgrupadas = new ArrayList<Integer>();
			List<LineaTicket> nuevasLineas = new ArrayList<LineaTicket>();

			List<LineaTicket> copiaLineasAux = new ArrayList<LineaTicket>(lineas);

			for (LineaTicket linea : lineas) {
				if (!lineasYaAgrupadas.contains(linea.getIdLinea())) {
					lineasYaAgrupadas.add(linea.getIdLinea());

					if (copiaLineasAux.contains(linea)) {
						copiaLineasAux.remove(linea);
					}

					BigDecimal cantidadTotal = linea.getCantidad();
					BigDecimal importeTotalPromociones = linea.getImporteTotalPromociones();
					BigDecimal importeTotalConDto = linea.getImporteTotalConDto();
					BigDecimal importeConDto = linea.getImporteConDto();
					
					Iterator<LineaTicket> itAux = copiaLineasAux.iterator();

					while (itAux.hasNext()) {
						LineaTicket lineaAux = itAux.next();

						if (tienenMismasCondicionesVenta(linea, lineaAux)) {
							itAux.remove();
							lineasYaAgrupadas.add(lineaAux.getIdLinea());
							copiaLineasAux.remove(lineaAux);

							cantidadTotal = cantidadTotal.add(lineaAux.getCantidad());
							importeTotalPromociones = importeTotalPromociones.add(lineaAux.getImporteTotalPromociones());
							importeTotalConDto = importeTotalConDto.add(lineaAux.getImporteTotalConDto());
							importeConDto  = importeConDto.add(lineaAux.getImporteConDto());
						}
					}
					
					if (!BigDecimalUtil.isIgualACero(cantidadTotal)) {
						linea.setImporteTotalConDto(importeTotalConDto);
						linea.setCantidad(cantidadTotal);
						linea.setImporteTotalPromociones(importeTotalPromociones);
						linea.setImporteConDto(importeConDto);
						
						//Operación para extraer el porcentaje de iva de cada artículo.
						List<SubtotalIvaTicket> porcentajes = ticketVenta.getCabecera().getSubtotalesIva();
						for (SubtotalIvaTicket imp : porcentajes) {
							if(imp.getCodImp().equals(linea.getCodImp())) {
								linea.setCodImp(imp.getPorcentaje().toString().split("\\.")[0]);
								 BigDecimal lineaIva = BigDecimalUtil.redondear(BigDecimalUtil.porcentaje(linea.getPrecioConDto().multiply(cantidadTotal), imp.getPorcentaje()), 4);
								linea.setDesglose2(formateaBigDecimal(lineaIva, 4));
							}
						}
						
						nuevasLineas.add(linea);
					}
				}
			}
			lineas = nuevasLineas;
			return nuevasLineas;
		}
		catch (Exception e) {
			log.error("agruparLineas() - Ha habido un error al agrupar lineas: " + e.getMessage(), e);
			return lineasOriginales;
		}
	}
	
	private String formateaBigDecimal(BigDecimal numero, int decimales) {
		try {
			Locale currentLocale = Locale.getDefault();
			DecimalFormatSymbols symbols = new DecimalFormatSymbols(currentLocale);
			DecimalFormat formateadorNumeros = new DecimalFormat();
			formateadorNumeros.setDecimalFormatSymbols(symbols);
			formateadorNumeros.setMaximumFractionDigits(decimales);
			formateadorNumeros.setMinimumFractionDigits(decimales);
			formateadorNumeros.setGroupingUsed(false);

			return formateadorNumeros.format(numero);

		}
		catch (Exception e) {
			log.error("formateaBigDecimal() - Ha ocurrido un error formateando con " + decimales + "decimales: " + e.getMessage(), e);
		}
		return null;
	}

	private boolean tienenMismasCondicionesVenta(LineaTicket linea, LineaTicket lineaAux) {
		if (!linea.getCodArticulo().equals(lineaAux.getCodArticulo())) {
			return false;
		}
		if (!BigDecimalUtil.isIgual(linea.getPrecioTotalConDto(), lineaAux.getPrecioTotalConDto())) {
			return false;
		}
		if(linea.getCantidad().signum() != lineaAux.getCantidad().signum()) {
			return false;
		}
		return true;
	}

	@Override
	public void getPagoGiftCard(TicketVentaAbono ticketVenta, TrabajoInformeBean trabajoInforme) {
		BigDecimal pagoGiftcard = new BigDecimal(0);
		for (PagoTicket pago : ticketVenta.getPagos()) {
			if(pago.getGiftcards()!=null && !pago.getGiftcards().isEmpty()) {
				pagoGiftcard = pagoGiftcard.add(pago.getGiftcards().get(0).getImportePago());
							
			}
		}
		
		trabajoInforme.addParametro("pagoGiftcard", pagoGiftcard); 
	}

}
