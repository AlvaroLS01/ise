package com.comerzzia.bricodepot.api.omnichannel.api.web.salesdocument;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.comerzzia.aena.util.xml.MarshallUtil;
import com.comerzzia.core.model.actividades.ActividadBean;
import com.comerzzia.core.model.empresas.EmpresaBean;
import com.comerzzia.core.model.informes.TrabajoInformeBean;
import com.comerzzia.core.model.tiposdocumentos.TipoDocumentoBean;
import com.comerzzia.core.model.ventas.tickets.TicketBean;
import com.comerzzia.core.servicios.empresas.ServicioEmpresasImpl;
import com.comerzzia.core.servicios.instancias.ServicioInstanciasImpl;
import com.comerzzia.core.servicios.sesion.IDatosSesion;
import com.comerzzia.core.servicios.tipodocumento.ServicioTiposDocumentosImpl;
import com.comerzzia.core.servicios.tipodocumento.TipoDocumentoException;
import com.comerzzia.core.servicios.tipodocumento.TipoDocumentoNotFoundException;
import com.comerzzia.core.servicios.ventas.tickets.ServicioTicketsImpl;
import com.comerzzia.core.util.config.AppInfo;
import com.comerzzia.core.util.db.Database;
import com.comerzzia.core.util.xml.XMLDocument;
import com.comerzzia.core.util.xml.XMLDocumentException;
import com.comerzzia.core.util.xml.XMLDocumentNode;
import com.comerzzia.core.util.xml.XMLDocumentNodeNotFoundException;
import com.comerzzia.model.fidelizacion.tarjetas.TarjetaBean;
import com.comerzzia.omnichannel.documentos.facturas.converters.albaran.ticket.LineaTicket;
import com.comerzzia.omnichannel.documentos.facturas.converters.albaran.ticket.TicketVentaAbono;
import com.comerzzia.servicios.fidelizacion.tarjetas.ServicioTarjetasImpl;
import com.comerzzia.servicios.fidelizacion.tarjetas.TarjetaNotFoundException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.util.JRLoader;

@Service
public class SalesDocumentPrintService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SalesDocumentPrintService.class);

    private static final String PORTUGAL = "PT";
    private static final String ESPANYA = "ES";
    private static final String CATALUNYA = "CA";

    private static final String DEFAULT_MIMETYPE = "application/pdf";
    private static final String REPORT_DIRECTORY = "ventas/facturas/";

    private final ServicioTiposDocumentosImpl servicioTiposDocumentos;
    private final ServicioEmpresasImpl servicioEmpresas;
    private final com.comerzzia.bricodepot.backoffice.services.ventas.facturas.CargarFacturaA4Servicio cargarFacturaA4Servicio;

    @Autowired
    public SalesDocumentPrintService(ServicioTiposDocumentosImpl servicioTiposDocumentos,
            ServicioEmpresasImpl servicioEmpresas,
            com.comerzzia.bricodepot.backoffice.services.ventas.facturas.CargarFacturaA4Servicio cargarFacturaA4Servicio) {
        this.servicioTiposDocumentos = servicioTiposDocumentos;
        this.servicioEmpresas = servicioEmpresas;
        this.cargarFacturaA4Servicio = cargarFacturaA4Servicio;
    }

    public Optional<SalesDocumentPrintResponse> print(String documentUid, SalesDocumentPrintOptions options, IDatosSesion datosSesion) {
        if (StringUtils.isBlank(documentUid)) {
            throw new SalesDocumentPrintException("The document uid is required");
        }
        try {
            TicketBean ticket = ServicioTicketsImpl.get().consultarTicketUid(documentUid, datosSesion.getUidActividad());
            if (ticket == null) {
                LOGGER.warn("print() - Sales document with uid '{}' not found", documentUid);
                return Optional.empty();
            }

            TicketVentaAbono ticketVenta = parseTicket(ticket);
            TrabajoInformeBean trabajoInforme = buildReportParameters(ticket, ticketVenta, options, datosSesion);
            enrichWithCustomParams(trabajoInforme, options.getCustomParams());

            TipoDocumentoBean tipoDocumento = resolveTipoDocumento(ticketVenta, datosSesion);
            String jasperTemplate = resolveTemplate(ticketVenta, tipoDocumento, options.getPrintTemplate());
            byte[] pdf = runReport(trabajoInforme, jasperTemplate);
            SalesDocumentPrintResponse response = buildResponse(documentUid, options, pdf);
            return Optional.of(response);
        }
        catch (Exception e) {
            throw new SalesDocumentPrintException("Unable to generate sales document pdf", e);
        }
    }

    protected TicketVentaAbono parseTicket(TicketBean ticket) throws Exception {
        try {
            return (TicketVentaAbono) MarshallUtil.leerXML(ticket.getTicket(), TicketVentaAbono.class);
        }
        catch (Exception e) {
            LOGGER.error("parseTicket() - Error parsing ticket xml", e);
            throw e;
        }
    }

    protected TrabajoInformeBean buildReportParameters(TicketBean ticket, TicketVentaAbono ticketVenta,
            SalesDocumentPrintOptions options, IDatosSesion datosSesion) throws Exception {
        TrabajoInformeBean trabajoInforme = new TrabajoInformeBean();
        trabajoInforme.addParametro("ticket", ticketVenta);
        trabajoInforme.addParametro("ticketVentaAbono", ticketVenta);
        trabajoInforme.addParametro("esDuplicado", options.isCopy());
        trabajoInforme.addParametro("DEVOLUCION", isRefund(ticketVenta));
        trabajoInforme.addParametro("UID_ACTIVIDAD", datosSesion.getUidActividad());

        if (ticket.getFecha() != null) {
            trabajoInforme.addParametro("FECHA_TICKET", ticket.getFecha());
        }
        if (StringUtils.isNotBlank(ticket.getLocatorId())) {
            trabajoInforme.addParametro("LOCATOR_ID", ticket.getLocatorId());
        }

        addEmpresaLogo(trabajoInforme, ticket, datosSesion);
        addActividad(trabajoInforme, ticket);
        addFiscalData(ticket, trabajoInforme);
        addPagoInformation(ticket, ticketVenta, trabajoInforme, datosSesion);
        addPromotions(ticket, trabajoInforme);
        addGiftCardTotals(trabajoInforme, ticketVenta, datosSesion);
        addInvoiceMetadata(trabajoInforme, ticket);
        addReportDirectories(trabajoInforme);
        addGroupedLines(trabajoInforme, ticketVenta);
        addCopyInformation(trabajoInforme, options);
        addCustomMetadata(trabajoInforme, ticket);

        return trabajoInforme;
    }

    private void addEmpresaLogo(TrabajoInformeBean trabajoInforme, TicketBean ticket, IDatosSesion datosSesion) throws IOException {
        SqlSession sqlSession = null;
        try {
            sqlSession = Database.getSqlSession();
            EmpresaBean empresa = servicioEmpresas.consultar(sqlSession, ticket.getCodemp(), datosSesion.getUidActividad());
            if (empresa != null && empresa.getLogotipo() != null) {
                InputStream is = new ByteArrayInputStream(empresa.getLogotipo());
                trabajoInforme.addParametro("LOGO", is);
            }
        }
        finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }

    private void addActividad(TrabajoInformeBean trabajoInforme, TicketBean ticket) throws Exception {
        ActividadBean actividad = ServicioInstanciasImpl.get().consultarActividad(ticket.getUidActividad());
        if (actividad != null) {
            trabajoInforme.addParametro("UID_INSTANCIA", actividad.getUidInstancia());
        }
    }

    private void addFiscalData(TicketBean ticket, TrabajoInformeBean trabajoInforme) throws Exception {
        cargarFacturaA4Servicio.addFiscalData(ticket, trabajoInforme);
    }

    private void addPagoInformation(TicketBean ticket, TicketVentaAbono ticketVenta, TrabajoInformeBean trabajoInforme,
            IDatosSesion datosSesion) throws Exception {
        cargarFacturaA4Servicio.getPagoGiftCard(ticketVenta, trabajoInforme);
        cargarFacturaA4Servicio.generarMediosPago(ticketVenta, datosSesion);
        cargarFacturaA4Servicio.cargarDatosPagoTarjeta(ticket, ticketVenta, trabajoInforme);
    }

    private void addPromotions(TicketBean ticket, TrabajoInformeBean trabajoInforme) throws XMLDocumentException {
        cargarFacturaA4Servicio.cargarPromociones(ticket, trabajoInforme);
    }

    private void addGiftCardTotals(TrabajoInformeBean trabajoInforme, TicketVentaAbono ticketVenta, IDatosSesion datosSesion)
            throws TarjetaNotFoundException {
        if (ticketVenta.getCabecera().getTarjetaRegalo() != null
                && StringUtils.isNotBlank(ticketVenta.getCabecera().getTarjetaRegalo().getNumTarjetaRegalo())) {
            String value = ticketVenta.getCabecera().getTarjetaRegalo().getNumTarjetaRegalo();
            String[] splitted = StringUtils.split(value, '/');
            List<String> tarjetas = splitted != null ? Arrays.asList(splitted) : Collections.singletonList(value);
            double totalSaldo = 0.0d;
            for (String numeroTarjeta : tarjetas) {
                TarjetaBean tarjeta = ServicioTarjetasImpl.get().consultarTarjetaPorNumero(numeroTarjeta, datosSesion);
                if (tarjeta != null) {
                    totalSaldo += tarjeta.getSaldoProvisional() + tarjeta.getSaldo();
                }
            }
            trabajoInforme.addParametro("totalSaldoGiftCard", totalSaldo);
        }
    }

    private void addInvoiceMetadata(TrabajoInformeBean trabajoInforme, TicketBean ticket)
            throws XMLDocumentNodeNotFoundException {
        Document document = ticket.getXml();
        if (document != null) {
            String fechaOrigen = obtenerFechaOrigen(document);
            if (fechaOrigen != null) {
                trabajoInforme.addParametro("fecha_origen", fechaOrigen);
            }
            String documento = getNumPedido(document);
            if (StringUtils.isNotBlank(documento)) {
                trabajoInforme.addParametro("numPedido", documento);
            }
        }
    }

    private void addReportDirectories(TrabajoInformeBean trabajoInforme) {
        String basePath = AppInfo.getInformesInfo().getRutaBase();
        if (StringUtils.isBlank(basePath)) {
            basePath = "." + File.separator;
        }
        String subReportDir = basePath + REPORT_DIRECTORY;
        trabajoInforme.addParametro("SUBREPORT_DIR", subReportDir);
    }

    private void addGroupedLines(TrabajoInformeBean trabajoInforme, TicketVentaAbono ticketVenta) {
        List<LineaTicket> lineasAgrupadas = cargarFacturaA4Servicio.getLineasAgrupadas(ticketVenta, trabajoInforme);
        trabajoInforme.addParametro("lineasAgrupadas", lineasAgrupadas);
    }

    private void addCopyInformation(TrabajoInformeBean trabajoInforme, SalesDocumentPrintOptions options) {
        if (options.isCopy()) {
            trabajoInforme.addParametro("esDuplicado", Boolean.TRUE);
        }
    }

    private void addCustomMetadata(TrabajoInformeBean trabajoInforme, TicketBean ticket) {
        trabajoInforme.addParametro("print_timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        trabajoInforme.addParametro("ticket_uid", ticket.getUidTicket());
    }

    private void enrichWithCustomParams(TrabajoInformeBean trabajoInforme, Map<String, String> customParams) {
        if (customParams == null || customParams.isEmpty()) {
            return;
        }
        for (Map.Entry<String, String> entry : customParams.entrySet()) {
            trabajoInforme.addParametro(entry.getKey(), entry.getValue());
        }
    }

    private TipoDocumentoBean resolveTipoDocumento(TicketVentaAbono ticketVenta, IDatosSesion datosSesion)
            throws TipoDocumentoNotFoundException, TipoDocumentoException {
        return servicioTiposDocumentos.consultar(datosSesion, ticketVenta.getCabecera().getTipoDocumento());
    }

    private String resolveTemplate(TicketVentaAbono ticketVenta, TipoDocumentoBean tipoDocumento, String requestedTemplate) {
        if (StringUtils.isNotBlank(requestedTemplate)) {
            return requestedTemplate.endsWith(".jasper") ? requestedTemplate : requestedTemplate + ".jasper";
        }
        String baseTemplate = "facturaA4_Original.jasper";
        if (tipoDocumento != null) {
            if (PORTUGAL.equals(tipoDocumento.getCodPais())) {
                if (isRefund(ticketVenta)) {
                    baseTemplate = "facturaDevolucionA4_PT.jasper";
                }
                else {
                    baseTemplate = "facturaA4_PT.jasper";
                }
            }
            else if (CATALUNYA.equals(tipoDocumento.getCodPais())) {
                baseTemplate = "facturaA4_CA.jasper";
            }
            else {
                baseTemplate = "facturaA4_Original.jasper";
            }
        }
        return baseTemplate;
    }

    private boolean isRefund(TicketVentaAbono ticketVenta) {
        if (ticketVenta.getCabecera() == null) {
            return false;
        }
        String codigo = ticketVenta.getCabecera().getCodTipoDocumento();
        return "FR".equalsIgnoreCase(codigo) || "NC".equalsIgnoreCase(codigo);
    }

    private byte[] runReport(TrabajoInformeBean trabajoInforme, String template) throws IOException, JRException {
        String basePath = AppInfo.getInformesInfo().getRutaBase();
        if (StringUtils.isBlank(basePath)) {
            basePath = "." + File.separator;
        }
        File jasper = new File(basePath + REPORT_DIRECTORY + template);
        try (InputStream jasperStream = new FileInputStream(jasper)) {
            Object jasperObject = JRLoader.loadObject(jasperStream);
            return JasperRunManager.runReportToPdf((net.sf.jasperreports.engine.JasperReport) jasperObject,
                    trabajoInforme.getParametros());
        }
    }

    private SalesDocumentPrintResponse buildResponse(String documentUid, SalesDocumentPrintOptions options, byte[] pdf) {
        SalesDocumentPrintResponse response = new SalesDocumentPrintResponse();
        response.setDocumentUid(documentUid);
        response.setMimeType(StringUtils.defaultIfBlank(options.getMimeType(), DEFAULT_MIMETYPE));
        response.setCopy(options.isCopy());
        response.setInline(options.isInline());
        response.setFileName(resolveFileName(documentUid, options));
        response.setDocument(Base64.getEncoder().encodeToString(pdf));
        return response;
    }

    private String resolveFileName(String documentUid, SalesDocumentPrintOptions options) {
        String baseName = StringUtils.isNotBlank(options.getOutputDocumentName()) ? options.getOutputDocumentName() : documentUid;
        if (!StringUtils.endsWithIgnoreCase(baseName, ".pdf")) {
            baseName = baseName + ".pdf";
        }
        if (options.isCopy() && !baseName.toLowerCase(Locale.ROOT).contains("copia")) {
            baseName = baseName.replaceFirst("(?i)\\.pdf$", options.isCopy() ? "_copia.pdf" : ".pdf");
        }
        return baseName;
    }

    private String obtenerFechaOrigen(Document document) {
        if (document == null) {
            return null;
        }
        NodeList nodes = document.getElementsByTagName("fechaTicketOrigen");
        if (nodes != null && nodes.getLength() > 0 && nodes.item(0) != null) {
            return nodes.item(0).getTextContent();
        }
        return null;
    }

    private String getNumPedido(Document document) throws XMLDocumentNodeNotFoundException {
        if (document == null) {
            return null;
        }
        XMLDocument xmlDoc = new XMLDocument(document);
        XMLDocumentNode pagosNode = xmlDoc.getNodo("pagos", true);
        if (pagosNode == null) {
            return null;
        }
        List<XMLDocumentNode> pagos = pagosNode.getHijos("pago");
        int idx = 0;
        for (XMLDocumentNode pagoNode : pagos) {
            XMLDocumentNode extendedDataNode = pagoNode.getNodo("extendedData", true);
            LOGGER.debug("getNumPedido() - pago[{}] extendedData={}", idx++, extendedDataNode != null ? extendedDataNode.toString() : "<null>");
            if (extendedDataNode != null) {
                XMLDocumentNode documentoNode = extendedDataNode.getNodo("documento", true);
                if (documentoNode != null) {
                    return documentoNode.getValue();
                }
            }
        }
        return null;
    }

}
