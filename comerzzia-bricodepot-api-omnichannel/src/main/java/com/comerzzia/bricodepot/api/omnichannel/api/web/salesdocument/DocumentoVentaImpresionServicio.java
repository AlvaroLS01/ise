package com.comerzzia.bricodepot.api.omnichannel.api.web.salesdocument;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
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
public class DocumentoVentaImpresionServicio {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentoVentaImpresionServicio.class);

    private static final DateTimeFormatter FORMATO_FECHA_IMPRESION = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String PAIS_PORTUGAL = "PT";
    private static final String PAIS_CATALUNA = "CA";
    private static final String MIMETYPE_POR_DEFECTO = "application/pdf";
    private static final String DIRECTORIO_INFORMES = "ventas/facturas/";

    private final ServicioTiposDocumentosImpl servicioTiposDocumentos;
    private final ServicioEmpresasImpl servicioEmpresas;
    private final com.comerzzia.bricodepot.backoffice.services.ventas.facturas.CargarFacturaA4Servicio servicioCargaFactura;

    @Autowired
    public DocumentoVentaImpresionServicio(ServicioTiposDocumentosImpl servicioTiposDocumentos,
                                           ServicioEmpresasImpl servicioEmpresas,
                                           com.comerzzia.bricodepot.backoffice.services.ventas.facturas.CargarFacturaA4Servicio servicioCargaFactura) {
        this.servicioTiposDocumentos = servicioTiposDocumentos;
        this.servicioEmpresas = servicioEmpresas;
        this.servicioCargaFactura = servicioCargaFactura;
    }

    public Optional<DocumentoVentaImpresionRespuesta> imprimir(String uidDocumento,
                                                               OpcionesImpresionDocumentoVenta opciones,
                                                               IDatosSesion datosSesion) {
        if (StringUtils.isBlank(uidDocumento)) {
            throw new DocumentoVentaImpresionException("El identificador del documento es obligatorio");
        }
        try {
            TicketBean ticket = ServicioTicketsImpl.get().consultarTicketUid(uidDocumento, datosSesion.getUidActividad());
            if (ticket == null) {
                LOGGER.warn("imprimir() - No se encontró el documento de venta con uid '{}'", uidDocumento);
                return Optional.empty();
            }

            TicketVentaAbono ticketVenta = convertirTicket(ticket);
            TrabajoInformeBean trabajoInforme = prepararTrabajoInforme(ticket, ticketVenta, opciones, datosSesion);
            anadirParametrosPersonalizados(trabajoInforme, opciones.getParametrosPersonalizados());

            TipoDocumentoBean tipoDocumento = obtenerTipoDocumento(ticketVenta, datosSesion);
            String plantillaJasper = resolverPlantilla(ticketVenta, tipoDocumento, opciones.getPlantillaImpresion());
            byte[] pdf = generarPdf(trabajoInforme, plantillaJasper);

            DocumentoVentaImpresionRespuesta respuesta = construirRespuesta(uidDocumento, opciones, pdf);
            return Optional.of(respuesta);
        }
        catch (DocumentoVentaImpresionException excepcion) {
            throw excepcion;
        }
        catch (Exception excepcion) {
            throw new DocumentoVentaImpresionException("No fue posible generar el PDF del documento de venta", excepcion);
        }
    }

    private TicketVentaAbono convertirTicket(TicketBean ticket) {
        try {
            return (TicketVentaAbono) MarshallUtil.leerXML(ticket.getTicket(), TicketVentaAbono.class);
        }
        catch (Exception excepcion) {
            LOGGER.error("convertirTicket() - Error al interpretar el XML del ticket", excepcion);
            throw new DocumentoVentaImpresionException("No fue posible interpretar el ticket recibido", excepcion);
        }
    }

    private TrabajoInformeBean prepararTrabajoInforme(TicketBean ticket,
                                                      TicketVentaAbono ticketVenta,
                                                      OpcionesImpresionDocumentoVenta opciones,
                                                      IDatosSesion datosSesion) throws Exception {
        TrabajoInformeBean trabajoInforme = new TrabajoInformeBean();
        trabajoInforme.addParametro("ticket", ticketVenta);
        trabajoInforme.addParametro("ticketVentaAbono", ticketVenta);
        trabajoInforme.addParametro("esDuplicado", opciones.esCopia());
        trabajoInforme.addParametro("DEVOLUCION", esDevolucion(ticketVenta));
        trabajoInforme.addParametro("UID_ACTIVIDAD", datosSesion.getUidActividad());

        if (ticket.getFecha() != null) {
            trabajoInforme.addParametro("FECHA_TICKET", ticket.getFecha());
        }
        if (StringUtils.isNotBlank(ticket.getLocatorId())) {
            trabajoInforme.addParametro("LOCATOR_ID", ticket.getLocatorId());
        }

        agregarLogoEmpresa(trabajoInforme, ticket, datosSesion);
        agregarDatosActividad(trabajoInforme, ticket);
        servicioCargaFactura.addFiscalData(ticket, trabajoInforme);
        cargarInformacionPagos(ticket, ticketVenta, trabajoInforme, datosSesion);
        servicioCargaFactura.cargarPromociones(ticket, trabajoInforme);
        agregarTotalesTarjetaRegalo(trabajoInforme, ticketVenta, datosSesion);
        agregarMetadatosFactura(trabajoInforme, ticket);
        agregarDirectoriosInformes(trabajoInforme);
        agregarLineasAgrupadas(trabajoInforme, ticketVenta);
        marcarCopia(trabajoInforme, opciones);
        agregarMetadatosPersonalizados(trabajoInforme, ticket);

        return trabajoInforme;
    }

    private void agregarLogoEmpresa(TrabajoInformeBean trabajoInforme,
                                     TicketBean ticket,
                                     IDatosSesion datosSesion) throws IOException {
        try (SqlSession sesionSql = Database.getSqlSession()) {
            EmpresaBean empresa = servicioEmpresas.consultar(sesionSql, ticket.getCodemp(), datosSesion.getUidActividad());
            if (empresa != null && empresa.getLogotipo() != null) {
                InputStream logotipo = new ByteArrayInputStream(empresa.getLogotipo());
                trabajoInforme.addParametro("LOGO", logotipo);
            }
        }
    }

    private void agregarDatosActividad(TrabajoInformeBean trabajoInforme, TicketBean ticket) throws Exception {
        ActividadBean actividad = ServicioInstanciasImpl.get().consultarActividad(ticket.getUidActividad());
        if (actividad != null) {
            trabajoInforme.addParametro("UID_INSTANCIA", actividad.getUidInstancia());
        }
    }

    private void cargarInformacionPagos(TicketBean ticket,
                                        TicketVentaAbono ticketVenta,
                                        TrabajoInformeBean trabajoInforme,
                                        IDatosSesion datosSesion) throws Exception {
        servicioCargaFactura.getPagoGiftCard(ticketVenta, trabajoInforme);
        servicioCargaFactura.generarMediosPago(ticketVenta, datosSesion);
        servicioCargaFactura.cargarDatosPagoTarjeta(ticket, ticketVenta, trabajoInforme);
    }

    private void agregarTotalesTarjetaRegalo(TrabajoInformeBean trabajoInforme,
                                             TicketVentaAbono ticketVenta,
                                             IDatosSesion datosSesion) throws TarjetaNotFoundException {
        if (ticketVenta.getCabecera().getTarjetaRegalo() == null) {
            return;
        }
        String numerosTarjeta = ticketVenta.getCabecera().getTarjetaRegalo().getNumTarjetaRegalo();
        if (StringUtils.isBlank(numerosTarjeta)) {
            return;
        }
        String[] tarjetasSeparadas = StringUtils.split(numerosTarjeta, '/');
        List<String> tarjetas = tarjetasSeparadas != null
                ? Arrays.asList(tarjetasSeparadas)
                : Collections.singletonList(numerosTarjeta);

        double saldoTotal = 0.0d;
        for (String numeroTarjeta : tarjetas) {
            TarjetaBean tarjeta = ServicioTarjetasImpl.get().consultarTarjetaPorNumero(numeroTarjeta, datosSesion);
            if (tarjeta != null) {
                saldoTotal += tarjeta.getSaldoProvisional() + tarjeta.getSaldo();
            }
        }
        trabajoInforme.addParametro("totalSaldoGiftCard", saldoTotal);
    }

    private void agregarMetadatosFactura(TrabajoInformeBean trabajoInforme, TicketBean ticket)
            throws XMLDocumentNodeNotFoundException {
        Document documento = ticket.getXml();
        if (documento == null) {
            return;
        }
        String fechaOrigen = obtenerFechaOrigen(documento);
        if (fechaOrigen != null) {
            trabajoInforme.addParametro("fecha_origen", fechaOrigen);
        }
        String numeroPedido = obtenerNumeroPedido(documento);
        if (StringUtils.isNotBlank(numeroPedido)) {
            trabajoInforme.addParametro("numPedido", numeroPedido);
        }
    }

    private void agregarDirectoriosInformes(TrabajoInformeBean trabajoInforme) {
        String rutaBase = AppInfo.getInformesInfo().getRutaBase();
        if (StringUtils.isBlank(rutaBase)) {
            rutaBase = "." + File.separator;
        }
        trabajoInforme.addParametro("SUBREPORT_DIR", rutaBase + DIRECTORIO_INFORMES);
    }

    private void agregarLineasAgrupadas(TrabajoInformeBean trabajoInforme, TicketVentaAbono ticketVenta) {
        List<LineaTicket> lineasAgrupadas = servicioCargaFactura.getLineasAgrupadas(ticketVenta, trabajoInforme);
        trabajoInforme.addParametro("lineasAgrupadas", lineasAgrupadas);
    }

    private void marcarCopia(TrabajoInformeBean trabajoInforme, OpcionesImpresionDocumentoVenta opciones) {
        if (opciones.esCopia()) {
            trabajoInforme.addParametro("esDuplicado", Boolean.TRUE);
        }
    }

    private void agregarMetadatosPersonalizados(TrabajoInformeBean trabajoInforme, TicketBean ticket) {
        trabajoInforme.addParametro("print_timestamp", FORMATO_FECHA_IMPRESION.format(LocalDateTime.now()));
        trabajoInforme.addParametro("ticket_uid", ticket.getUidTicket());
    }

    private void anadirParametrosPersonalizados(TrabajoInformeBean trabajoInforme,
                                                Map<String, String> parametrosPersonalizados) {
        if (parametrosPersonalizados == null || parametrosPersonalizados.isEmpty()) {
            return;
        }
        parametrosPersonalizados.forEach(trabajoInforme::addParametro);
    }

    private TipoDocumentoBean obtenerTipoDocumento(TicketVentaAbono ticketVenta,
                                                   IDatosSesion datosSesion)
            throws TipoDocumentoNotFoundException, TipoDocumentoException {
        return servicioTiposDocumentos.consultar(datosSesion, ticketVenta.getCabecera().getTipoDocumento());
    }

    private String resolverPlantilla(TicketVentaAbono ticketVenta,
                                     TipoDocumentoBean tipoDocumento,
                                     String plantillaSolicitada) {
        if (StringUtils.isNotBlank(plantillaSolicitada)) {
            return plantillaSolicitada.endsWith(".jasper") ? plantillaSolicitada : plantillaSolicitada + ".jasper";
        }

        if (tipoDocumento == null) {
            return "facturaA4_Original.jasper";
        }

        if (PAIS_PORTUGAL.equals(tipoDocumento.getCodPais())) {
            return esDevolucion(ticketVenta) ? "facturaDevolucionA4_PT.jasper" : "facturaA4_PT.jasper";
        }
        if (PAIS_CATALUNA.equals(tipoDocumento.getCodPais())) {
            return "facturaA4_CA.jasper";
        }
        return "facturaA4_Original.jasper";
    }

    private boolean esDevolucion(TicketVentaAbono ticketVenta) {
        if (ticketVenta.getCabecera() == null) {
            return false;
        }
        String codigo = ticketVenta.getCabecera().getCodTipoDocumento();
        return "FR".equalsIgnoreCase(codigo) || "NC".equalsIgnoreCase(codigo);
    }

    private byte[] generarPdf(TrabajoInformeBean trabajoInforme, String plantilla)
            throws IOException, JRException {
        String rutaBase = AppInfo.getInformesInfo().getRutaBase();
        if (StringUtils.isBlank(rutaBase)) {
            rutaBase = "." + File.separator;
        }
        File ficheroJasper = new File(rutaBase + DIRECTORIO_INFORMES + plantilla);
        try (InputStream flujoJasper = new FileInputStream(ficheroJasper)) {
            Object objetoJasper = JRLoader.loadObject(flujoJasper);
            return JasperRunManager.runReportToPdf((net.sf.jasperreports.engine.JasperReport) objetoJasper,
                    trabajoInforme.getParametros());
        }
    }

    private DocumentoVentaImpresionRespuesta construirRespuesta(String uidDocumento,
                                                                 OpcionesImpresionDocumentoVenta opciones,
                                                                 byte[] pdf) {
        DocumentoVentaImpresionRespuesta respuesta = new DocumentoVentaImpresionRespuesta();
        respuesta.setUidDocumento(uidDocumento);
        respuesta.setTipoMime(StringUtils.defaultIfBlank(opciones.getTipoMime(), MIMETYPE_POR_DEFECTO));
        respuesta.setCopia(opciones.esCopia());
        respuesta.setEnLinea(opciones.esEnLinea());
        respuesta.setNombreArchivo(resolverNombreArchivo(uidDocumento, opciones));
        respuesta.setDocumento(Base64.getEncoder().encodeToString(pdf));
        return respuesta;
    }

    private String resolverNombreArchivo(String uidDocumento, OpcionesImpresionDocumentoVenta opciones) {
        String nombreBase = StringUtils.isNotBlank(opciones.getNombreDocumentoSalida())
                ? opciones.getNombreDocumentoSalida()
                : uidDocumento;
        if (!StringUtils.endsWithIgnoreCase(nombreBase, ".pdf")) {
            nombreBase = nombreBase + ".pdf";
        }
        if (opciones.esCopia() && !nombreBase.toLowerCase(Locale.ROOT).contains("copia")) {
            nombreBase = nombreBase.replaceFirst("(?i)\\.pdf$", "_copia.pdf");
        }
        return nombreBase;
    }

    private String obtenerFechaOrigen(Document documento) {
        NodeList nodos = documento.getElementsByTagName("fechaTicketOrigen");
        if (nodos == null || nodos.getLength() == 0 || nodos.item(0) == null) {
            return null;
        }
        return nodos.item(0).getTextContent();
    }

    private String obtenerNumeroPedido(Document documento) throws XMLDocumentNodeNotFoundException {
        XMLDocument documentoXml = new XMLDocument(documento);
        XMLDocumentNode nodoPagos = documentoXml.getNodo("pagos", true);
        if (nodoPagos == null) {
            return null;
        }
        List<XMLDocumentNode> pagos = nodoPagos.getHijos("pago");
        int indicePago = 0;
        for (XMLDocumentNode nodoPago : pagos) {
            XMLDocumentNode nodoDatosExtendidos = nodoPago.getNodo("extendedData", true);
            LOGGER.debug("obtenerNumeroPedido() - pago[{}] datosExtendidos={}",
                    indicePago++, nodoDatosExtendidos != null ? nodoDatosExtendidos.toString() : "<null>");
            if (nodoDatosExtendidos == null) {
                continue;
            }
            XMLDocumentNode nodoDocumento = nodoDatosExtendidos.getNodo("documento", true);
            if (nodoDocumento != null) {
                return nodoDocumento.getValue();
            }
        }
        return null;
    }
}
