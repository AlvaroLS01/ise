package com.comerzzia.bricodepot.backoffice.services.ventas.facturas;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import com.comerzzia.core.model.informes.TrabajoInformeBean;
import com.comerzzia.core.model.ventas.tickets.TicketBean;
import com.comerzzia.core.servicios.sesion.IDatosSesion;
import com.comerzzia.core.util.xml.XMLDocumentException;
import com.comerzzia.omnichannel.documentos.facturas.converters.albaran.ticket.LineaTicket;
import com.comerzzia.omnichannel.documentos.facturas.converters.albaran.ticket.TicketVentaAbono;

public interface CargarFacturaA4Servicio {
	
	public String getSubReportDir(final String fileName);
	
	public void generarMediosPago(TicketVentaAbono ticketVentaAbono,IDatosSesion datosSesion) throws Exception;
	
	public void addFiscalData(TicketBean ticketBean, TrabajoInformeBean trabajoInforme) throws Exception, IOException;

	public BufferedImage generateQRCodeImage(String barcodeText) throws Exception ;
	
	public void cargarDatosPagoTarjeta(TicketBean ticket, TicketVentaAbono ticketVentaAbono, TrabajoInformeBean trabajoInforme);
	
	public void cargarPromociones(TicketBean ticket, TrabajoInformeBean trabajoInforme) throws XMLDocumentException;
	
	public List<LineaTicket> getLineasAgrupadas(TicketVentaAbono ticketVenta,TrabajoInformeBean trabajoInforme);
	
	public void getPagoGiftCard(TicketVentaAbono ticketVenta, TrabajoInformeBean trabajoInforme);
}
