package com.comerzzia.bricodepot.backoffice.services.procesamiento.notificaciones.factura;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import com.comerzzia.aena.util.xml.MarshallUtil;
import com.comerzzia.bricodepot.backoffice.services.procesamiento.notificaciones.factura.exceptions.PDFServiceException;
import com.comerzzia.bricodepot.backoffice.services.ventas.facturas.CargarFacturaA4ServicioImpl;
import com.comerzzia.core.model.actividades.ActividadBean;
import com.comerzzia.core.model.empresas.EmpresaBean;
import com.comerzzia.core.model.informes.TrabajoInformeBean;
import com.comerzzia.core.model.ventas.tickets.TicketBean;
import com.comerzzia.core.servicios.empresas.ServicioEmpresasImpl;
import com.comerzzia.core.servicios.instancias.ServicioInstanciasImpl;
import com.comerzzia.core.servicios.sesion.IDatosSesion;
import com.comerzzia.core.servicios.ventas.tickets.ServicioTicketsImpl;
import com.comerzzia.core.util.config.AppInfo;
import com.comerzzia.core.util.db.Database;
import com.comerzzia.omnichannel.documentos.facturas.converters.albaran.ticket.LineaTicket;
import com.comerzzia.omnichannel.documentos.facturas.converters.albaran.ticket.TicketVentaAbono;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 * ISK-185 Plantilla de ticket
 */

@Component
@SuppressWarnings({"deprecation" })
public class GeneradorTicketEmailPDF {

	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

	public static final String VARIABLE_URL_EMPRESA = "X_URL_EMPRESA_TICKET";
	public static final String RUTA_PDF_TICKET_EMAIL_JasperReport = "ventas/facturas/";
	public static final String ARCHIVO_PDF_TICKET_EMAIL_PT = "facturaA4_PT.jrxml";
	public static final String ARCHIVO_PDF_TICKET_EMAIL_CA = "facturaA4_CA.jrxml";
	public static final String ARCHIVO_PDF_TICKET_EMAIL_ES = "facturaA4_Original.jrxml";
	private final Logger log = LoggerFactory.getLogger(GeneradorTicketEmailPDF.class);

	@Autowired
	protected ServicioEmpresasImpl servicioEmpresasImpl;

	protected CargarFacturaA4ServicioImpl cargarFacturaServicio = new CargarFacturaA4ServicioImpl();

	private TrabajoInformeBean trabajoInforme = new TrabajoInformeBean();
	

	public GeneradorTicketEmailPDF() 
	{
	}

	public byte[] generatePdf(IDatosSesion datosSesion, String uidTicket, Boolean esPDFCupones, String idioma) throws PDFServiceException {
		try {
			log.info("generatePdf() - Inicio de generación de PDF de Ticket (" + uidTicket + ")" + " para enviar por email...");
			JasperReport jasperReport = null;
			if ("ES".equals(idioma)) {
				jasperReport = getJasperReport(AppInfo.getInformesInfo().getRutaBase() + RUTA_PDF_TICKET_EMAIL_JasperReport + ARCHIVO_PDF_TICKET_EMAIL_ES);
			}
			else if ("PT".equals(idioma)) {
				jasperReport = getJasperReport(AppInfo.getInformesInfo().getRutaBase() + RUTA_PDF_TICKET_EMAIL_JasperReport + ARCHIVO_PDF_TICKET_EMAIL_PT);
			}
			else if("CA".equals(idioma)){
				jasperReport = getJasperReport(AppInfo.getInformesInfo().getRutaBase() + RUTA_PDF_TICKET_EMAIL_JasperReport + ARCHIVO_PDF_TICKET_EMAIL_CA);
			}
			Map<String, Object> parameters = getReportParameters(datosSesion, uidTicket, idioma);
			byte[] pdf = JasperRunManager.runReportToPdf(jasperReport, parameters);
			log.info("generatePdf() - Finalizada la generación de PDF de Ticket (" + uidTicket + ")" + " para enviar por email.");
			return pdf;
		}
		catch (Exception e) {
			throw new PDFServiceException(e.getMessage());
		}
	}

	private JasperReport getJasperReport(String reportsPath) throws Exception {
		try {
			File fileJasper = new File(reportsPath);
			JasperDesign jasperDesign = JRXmlLoader.load(fileJasper);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			return jasperReport;
		}
		catch (Exception e) {
			log.error("getJasperReport() - Error al buscar la plantilla en la ruta " + RUTA_PDF_TICKET_EMAIL_JasperReport + " : " + e.getMessage(), e);
			throw new Exception(e.getMessage(), e);
		}
	}

	private Map<String, Object> getReportParameters(IDatosSesion datosSesion, String uidTicket, String idioma) throws Exception {
		SqlSession sqlSession = null;
		try {
			TicketBean ticket = null;
			try {
				ticket = ServicioTicketsImpl.get().consultarTicketUid(uidTicket, datosSesion.getUidActividad());
			}
			catch (Exception e) {
				log.error("getReportParameters() -  : " + e.getMessage(), e);
				throw new Exception("Error al buscar el ticket para enviar por email (" + uidTicket + ")", e);
			}

			if (ticket != null) {
				TicketVentaAbono ticketVenta = null;
				try {
					ticketVenta = (TicketVentaAbono) MarshallUtil.leerXML(ticket.getTicket(), TicketVentaAbono.class);
					if (ticketVenta == null) {
						String msgError = "Error al parsear el xml del ticket en un objeto 'TicketVentaAbono.class'.";
						throw new Exception(msgError);
					}
				}
				catch (Exception e) {
					String msgError = e.getMessage();
					log.error("getReportParameters() - " + msgError, e);
					throw new Exception(msgError, e);
				}

				/* Enviamos el ticket y los datos de los listados que vamos a mostrar. */
				if (ticketVenta != null) {

					if (ticket.getFecha() != null) {
						trabajoInforme.addParametro("FECHA_TICKET", ticket.getFecha());
					}
					if (StringUtils.isNotBlank(ticket.getLocatorId())) {
						trabajoInforme.addParametro("LOCATOR_ID", ticket.getLocatorId());
					}

					// UID_INSTANCIA para el logo
					ActividadBean actividad = ServicioInstanciasImpl.get().consultarActividad(ticket.getUidActividad());
					trabajoInforme.addParametro("UID_INSTANCIA", actividad.getUidInstancia());
					servicioEmpresasImpl = new ServicioEmpresasImpl();
					// Datos Brico
					cargarFacturaServicio.addFiscalData(ticket, trabajoInforme);
					EmpresaBean empresa = new EmpresaBean();
					

					sqlSession = Database.getSqlSession();

					empresa = servicioEmpresasImpl.consultar(sqlSession, ticket.getCodemp(), datosSesion.getUidActividad());
					if (empresa != null) {
						aniadirLogoParametrosImprimir(empresa, trabajoInforme);
					}

					// Medios de pago
					cargarFacturaServicio.getPagoGiftCard(ticketVenta, trabajoInforme);
					cargarFacturaServicio.generarMediosPago(ticketVenta, datosSesion);
					
					trabajoInforme.addParametro("ticket", ticketVenta);
					trabajoInforme.addParametro("ticketVentaAbono", ticketVenta);
					cargarFacturaServicio.cargarDatosPagoTarjeta(ticket, ticketVenta, trabajoInforme);
					cargarFacturaServicio.cargarPromociones(ticket, trabajoInforme);
					/*Para que si hay direferentes lienas con el mismo codigo, salgan en una solo*/
					List<LineaTicket> lineasAgrupadas = cargarFacturaServicio.getLineasAgrupadas(ticketVenta, trabajoInforme);
					trabajoInforme.addParametro("lineasAgrupadas", lineasAgrupadas);
				}
				
				//parámetro fecha_origen
				String fechaOrigen = obtenerFechaOrigen(ticket.getXml());
				if (fechaOrigen != null) {
					trabajoInforme.addParametro("fecha_origen", fechaOrigen);
				}

				// Subreportes
				String ruta = AppInfo.getInformesInfo().getRutaBase() + RUTA_PDF_TICKET_EMAIL_JasperReport;
				if (StringUtils.isNotBlank(ruta)) {
					trabajoInforme.addParametro("SUBREPORT_DIR", cargarFacturaServicio.getSubReportDir(ruta));
				}

			}
		}
		catch (Exception e) {

			log.error("getReportParameters() - Error inicializando los parámetros del envío de ticket a email : " + e.getMessage(), e);
			throw new Exception(e.getMessage(), e);
		}finally {
			if(sqlSession != null) {
				sqlSession.close();
			}
		}

		return trabajoInforme.getParametros();
	}

	private void aniadirLogoParametrosImprimir(EmpresaBean empresa, TrabajoInformeBean trabajoInforme) throws IOException {

		InputStream is = new ByteArrayInputStream(empresa.getLogotipo());
		trabajoInforme.addParametro("LOGO", is);
		is.close();

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

}
