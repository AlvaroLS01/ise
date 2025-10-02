package com.comerzzia.bricodepot.api.omnichannel.api.web.salesdocument;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.util.JRLoader;

@Component
public class GeneradorFacturaA4 {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneradorFacturaA4.class);

    private static final String PLANTILLA_ES = "facturaA4";
    private static final String PLANTILLA_PT = "facturaA4_PT";
    private static final String PLANTILLA_CA = "facturaA4_CA";
    private static final String PLANTILLA_ORIGINAL = "facturaA4_Original";
    private static final String PLANTILLA_DEVOLUCION_PT = "facturaDevolucionA4_PT";

    private static final List<String> PLANTILLAS_VALIDAS = Arrays.asList(
            PLANTILLA_ES,
            PLANTILLA_PT,
            PLANTILLA_CA,
            PLANTILLA_ORIGINAL,
            PLANTILLA_DEVOLUCION_PT);

    private static final Map<String, String> ALIAS_PLANTILLAS;
    static {
        Map<String, String> alias = new HashMap<>();
        alias.put("facturaa4", PLANTILLA_ES);
        alias.put("facturaa4_original", PLANTILLA_ORIGINAL);
        alias.put("facturaa4_pt", PLANTILLA_PT);
        alias.put("facturaa4_ca", PLANTILLA_CA);
        alias.put("facturadevoluciona4_pt", PLANTILLA_DEVOLUCION_PT);
        alias.put("facturadevoluciona4_pt_old", PLANTILLA_DEVOLUCION_PT);
        alias.put("fs", PLANTILLA_ES);
        alias.put("ft", PLANTILLA_ES);
        alias.put("fr", PLANTILLA_ES);
        alias.put("nc", PLANTILLA_ES);
        ALIAS_PLANTILLAS = alias;
    }

    private final ApplicationContext applicationContext;
    private final ObjectMapper conversorJson;
    private final PathMatchingResourcePatternResolver buscadorRecursos;
    private final String rutaInformesConfigurada;

    private volatile Path directorioPlantillasSeleccionado;
    private volatile Path directorioSubinformesTemporal;

    GeneradorFacturaA4(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.conversorJson = new ObjectMapper();
        this.buscadorRecursos = new PathMatchingResourcePatternResolver();
        this.rutaInformesConfigurada = localizarRutaInformes();
    }

    Optional<ResultadoFacturaPdf> generarFactura(String uidDocumento,
                                                 OpcionesImpresionDocumentoVenta opciones) throws IOException {
        Optional<Object> posibleTicket = localizarTicketVenta(uidDocumento);
        if (!posibleTicket.isPresent()) {
            return Optional.empty();
        }

        Object ticketVenta = posibleTicket.get();
        PlantillaFactura plantilla = determinarPlantilla(ticketVenta, opciones.getPlantillaImpresion());
        Map<String, Object> parametros = prepararParametros(ticketVenta,
                opciones.esCopia(),
                plantilla,
                convertirParametrosPersonalizados(opciones.getParametrosPersonalizados()));

        String nombreFichero = calcularNombreFichero(opciones.getNombreDocumentoSalida(), ticketVenta, plantilla);
        byte[] pdfGenerado = ejecutarJasper(plantilla, parametros);

        return Optional.of(new ResultadoFacturaPdf(pdfGenerado, nombreFichero));
    }

    private String localizarRutaInformes() {
        try {
            Class<?> claseAppInfo = Class.forName("com.comerzzia.core.util.config.AppInfo");
            Method metodoInformes = claseAppInfo.getMethod("getInformesInfo");
            Object informacion = metodoInformes.invoke(null);
            if (informacion != null) {
                Method metodoRuta = informacion.getClass().getMethod("getRutaBase");
                Object ruta = metodoRuta.invoke(informacion);
                if (ruta != null) {
                    String textoRuta = ruta.toString().trim();
                    if (!textoRuta.isEmpty()) {
                        return textoRuta;
                    }
                }
            }
        }
        catch (Exception excepcion) {
            LOGGER.debug("No se pudo obtener la ruta de informes desde AppInfo", excepcion);
        }
        return null;
    }

    private Optional<Object> localizarTicketVenta(String uidDocumento) {
        String uidNormalizado = uidDocumento != null ? uidDocumento.trim() : null;
        if (uidNormalizado == null || uidNormalizado.isEmpty()) {
            return Optional.empty();
        }

        String[] nombresBeans = applicationContext.getBeanDefinitionNames();
        for (String nombreBean : nombresBeans) {
            Object bean;
            try {
                bean = applicationContext.getBean(nombreBean);
            }
            catch (BeansException | NoClassDefFoundError | LinkageError excepcion) {
                LOGGER.trace("No se pudo inicializar el bean {} al localizar el ticket", nombreBean, excepcion);
                continue;
            }
            Optional<Object> ticket = intentarBuscarTicket(bean, uidNormalizado);
            if (ticket.isPresent()) {
                LOGGER.debug("Ticket localizado usando el bean {}", bean.getClass().getName());
                return ticket;
            }
        }
        LOGGER.warn("No se encontró un servicio capaz de localizar el ticket {}", uidNormalizado);
        return Optional.empty();
    }

    private Optional<Object> intentarBuscarTicket(Object bean, String uidDocumento) {
        Method[] metodos = bean.getClass().getMethods();
        for (Method metodo : metodos) {
            if (metodo.getParameterCount() != 1) {
                continue;
            }
            if (!metodo.getParameterTypes()[0].equals(String.class)) {
                continue;
            }
            String nombreMetodo = metodo.getName().toLowerCase(Locale.ROOT);
            if (!(nombreMetodo.contains("ticket") || nombreMetodo.contains("document"))) {
                continue;
            }
            try {
                Object posibleTicket = metodo.invoke(bean, uidDocumento);
                if (posibleTicket != null && posibleTicket.getClass().getName().contains("TicketVentaAbono")) {
                    return Optional.of(posibleTicket);
                }
            }
            catch (IllegalAccessException | InvocationTargetException excepcion) {
                LOGGER.trace("No se pudo invocar {} en {}", metodo.getName(), bean.getClass().getName(), excepcion);
            }
        }
        return Optional.empty();
    }

    private PlantillaFactura determinarPlantilla(Object ticketVenta, String plantillaSolicitada) {
        if (plantillaSolicitada != null && !plantillaSolicitada.trim().isEmpty()) {
            String plantillaNormalizada = normalizarNombrePlantilla(plantillaSolicitada);
            if (!PLANTILLAS_VALIDAS.contains(plantillaNormalizada)) {
                LOGGER.warn("Plantilla {} no reconocida. Se aplicará la plantilla por defecto.", plantillaNormalizada);
            }
            else {
                return construirPlantilla(plantillaNormalizada);
            }
        }

        String codigoPais = convertirTexto(leerPropiedad(ticketVenta,
                "cabecera.empresa.codPais",
                "cabecera.empresa.pais",
                "cabecera.tienda.codPais",
                "cabecera.tienda.pais",
                "cabecera.pais"));

        boolean esDevolucion = esDocumentoDevolucion(ticketVenta);

        if ("PT".equalsIgnoreCase(codigoPais)) {
            if (esDevolucion) {
                return construirPlantilla(PLANTILLA_DEVOLUCION_PT);
            }
            return construirPlantilla(PLANTILLA_PT);
        }
        if ("CA".equalsIgnoreCase(codigoPais)) {
            return construirPlantilla(PLANTILLA_CA);
        }
        return construirPlantilla(PLANTILLA_ES);
    }

    private PlantillaFactura construirPlantilla(String nombrePlantilla) {
        int version = 1;
        if (PLANTILLA_PT.equals(nombrePlantilla) || PLANTILLA_DEVOLUCION_PT.equals(nombrePlantilla)) {
            version = 2;
        }
        else if (PLANTILLA_CA.equals(nombrePlantilla)) {
            version = 3;
        }
        return new PlantillaFactura(nombrePlantilla, version);
    }

    private Map<String, Object> prepararParametros(Object ticketVenta,
                                                   boolean esCopia,
                                                   PlantillaFactura plantilla,
                                                   Map<String, Object> parametrosPersonalizados) {
        Map<String, Object> parametros = new LinkedHashMap<>();

        parametros.put("ticket", ticketVenta);
        parametros.put("esDuplicado", esCopia);
        parametros.put("reportVersion", plantilla.getVersion());

        Object fechaTicket = leerPropiedad(ticketVenta,
                "cabecera.fechaAsDate",
                "cabecera.fechaTicket",
                "cabecera.fecha",
                "cabecera.fechaOperacion");
        if (fechaTicket != null) {
            parametros.put("FECHA_TICKET", convertirFecha(fechaTicket));
        }

        Object locatorId = leerPropiedad(ticketVenta, "cabecera.locatorId", "cabecera.locatorID");
        if (locatorId != null) {
            parametros.put("LOCATOR_ID", locatorId);
        }

        Object uidInstancia = leerPropiedad(ticketVenta,
                "cabecera.uidInstancia",
                "cabecera.uidinstancia",
                "cabecera.instancia.uid");
        if (uidInstancia != null) {
            parametros.put("UID_INSTANCIA", uidInstancia);
        }

        Object fechaOrigen = leerPropiedad(ticketVenta,
                "cabecera.datosDocOrigen.fecha",
                "cabecera.datosDocOrigen.fechaAsDate",
                "cabecera.datosOrigen.fecha");
        if (fechaOrigen != null) {
            parametros.put("fecha_origen", convertirFecha(fechaOrigen));
        }

        Object numeroPedido = leerPropiedad(ticketVenta,
                "cabecera.numPedido",
                "cabecera.numeroPedido");
        if (numeroPedido != null) {
            parametros.put("numPedido", numeroPedido);
        }

        Object lineasAgrupadas = leerPropiedad(ticketVenta, "lineasAgrupadas", "lineasAgrupadasFactura");
        if (lineasAgrupadas != null) {
            parametros.put("lineasAgrupadas", lineasAgrupadas);
        }

        Object listaPromociones = leerPropiedad(ticketVenta,
                "promociones",
                "listaPromociones",
                "cabecera.listaPromociones");
        if (listaPromociones != null) {
            parametros.put("listaPromociones", listaPromociones);
        }

        Object pagosTarjetaDatosPeticion = leerPropiedad(ticketVenta,
                "pagosTarjetaDatosPeticion",
                "listaPagosTarjetaDatosPeticion");
        if (pagosTarjetaDatosPeticion != null) {
            parametros.put("listaPagosTarjetaDatosPeticion", pagosTarjetaDatosPeticion);
        }

        Object pagosTarjeta = leerPropiedad(ticketVenta,
                "pagosTarjeta",
                "listaPagosTarjeta");
        if (pagosTarjeta != null) {
            parametros.put("listaPagosTarjeta", pagosTarjeta);
        }

        Object pagoGiftCard = leerPropiedad(ticketVenta, "pagoGiftcard", "pagoGiftCard");
        if (pagoGiftCard != null) {
            parametros.put("pagoGiftcard", pagoGiftCard);
        }

        Object totalSaldoGiftCard = leerPropiedad(ticketVenta,
                "totalSaldoGiftCard",
                "totalSaldoTarjetaRegalo");
        if (totalSaldoGiftCard != null) {
            parametros.put("totalSaldoGiftCard", totalSaldoGiftCard);
        }

        Object logo = leerPropiedad(ticketVenta,
                "cabecera.empresa.logo",
                "cabecera.empresa.logotipo",
                "cabecera.logo");
        InputStream logotipo = convertirImagen(logo);
        if (logotipo != null) {
            parametros.put("LOGO", logotipo);
        }

        boolean esDocumentoDevolucion = esDocumentoDevolucion(ticketVenta);
        parametros.put("DEVOLUCION", esDocumentoDevolucion);

        if (plantilla.getNombre().equals(PLANTILLA_PT)) {
            prepararDatosFiscalesPortugal(ticketVenta, parametros);
        }

        parametros.put("SUBREPORT_DIR", resolverDirectorioSubinformes());

        fusionarParametrosPersonalizados(parametros, parametrosPersonalizados);

        return parametros;
    }

    private void prepararDatosFiscalesPortugal(Object ticketVenta, Map<String, Object> parametros) {
        Object atcud = leerPropiedad(ticketVenta,
                "cabecera.datosFiscales.atcud",
                "cabecera.fiscalData.atcud");
        if (atcud != null) {
            parametros.put("fiscalData_ATCUD", atcud);
        }
        Object datosQr = leerPropiedad(ticketVenta,
                "cabecera.datosFiscales.codigoQR",
                "cabecera.datosFiscales.qr",
                "cabecera.fiscalData.qr");
        if (datosQr != null) {
            parametros.put("fiscalData_QR", datosQr);
            InputStream imagenQr = generarCodigoQr(datosQr.toString());
            if (imagenQr != null) {
                parametros.put("QR_PORTUGAL", imagenQr);
            }
        }
    }

    private Map<String, Object> convertirParametrosPersonalizados(Map<String, String> parametros) {
        if (parametros == null || parametros.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, Object> normalizados = new LinkedHashMap<>();
        parametros.forEach((clave, valor) -> {
            if (clave == null || valor == null) {
                return;
            }
            String claveNormalizada = clave.trim();
            if (claveNormalizada.isEmpty()) {
                return;
            }
            if ("customParams".equalsIgnoreCase(claveNormalizada)) {
                normalizados.putAll(parsearCustomParams(valor));
                return;
            }
            if (claveNormalizada.startsWith("customParams.")) {
                String destino = claveNormalizada.substring("customParams.".length());
                if (!destino.isEmpty()) {
                    normalizados.put(destino, valor);
                }
                return;
            }
            if (claveNormalizada.startsWith("customParams[") && claveNormalizada.endsWith("]")) {
                String destino = claveNormalizada.substring("customParams[".length(), claveNormalizada.length() - 1);
                if (!destino.isEmpty()) {
                    normalizados.put(destino, valor);
                }
                return;
            }
            normalizados.put(claveNormalizada, valor);
        });
        return normalizados;
    }

    private Map<String, Object> parsearCustomParams(String valor) {
        try {
            return conversorJson.readValue(valor, new TypeReference<Map<String, Object>>() {
            });
        }
        catch (Exception excepcion) {
            LOGGER.warn("No fue posible interpretar customParams como JSON. Se utilizará el texto plano.", excepcion);
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("customParams", valor);
            return parametros;
        }
    }

    private void fusionarParametrosPersonalizados(Map<String, Object> parametros,
                                                   Map<String, Object> personalizados) {
        if (personalizados == null || personalizados.isEmpty()) {
            return;
        }
        personalizados.forEach((clave, valor) -> {
            if (clave != null && !clave.trim().isEmpty() && valor != null) {
                parametros.put(clave, valor);
            }
        });
    }

    private String calcularNombreFichero(String nombreSolicitado,
                                         Object ticketVenta,
                                         PlantillaFactura plantilla) {
        if (nombreSolicitado != null && !nombreSolicitado.trim().isEmpty()) {
            String nombreNormalizado = nombreSolicitado.trim();
            if (!nombreNormalizado.toLowerCase(Locale.ROOT).endsWith(".pdf")) {
                nombreNormalizado = nombreNormalizado + ".pdf";
            }
            return nombreNormalizado;
        }
        String codigoTicket = convertirTexto(leerPropiedad(ticketVenta,
                "cabecera.codTicket",
                "cabecera.codigoTicket",
                "cabecera.ticket"));
        if (codigoTicket == null || codigoTicket.isEmpty()) {
            codigoTicket = "documento";
        }
        String baseNombre = Normalizer.normalize(plantilla.getNombre(), Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return baseNombre + "_" + codigoTicket + ".pdf";
    }

    private byte[] ejecutarJasper(PlantillaFactura plantilla, Map<String, Object> parametros) throws IOException {
        InputStream flujo = null;
        try {
            if (rutaInformesConfigurada != null) {
                flujo = localizarPlantillaEnSistemaArchivos(plantilla);
            }

            if (flujo == null) {
                String rutaPlantilla = "informes/ventas/facturas/" + plantilla.getNombre() + ".jasper";
                Resource recursoPlantilla = new ClassPathResource(rutaPlantilla);
                if (!recursoPlantilla.exists()) {
                    throw new IOException("No se encontró la plantilla jasper " + rutaPlantilla);
                }
                flujo = recursoPlantilla.getInputStream();
            }

            JasperReport informe = (JasperReport) JRLoader.loadObject(flujo);
            return JasperRunManager.runReportToPdf(informe, parametros, new JREmptyDataSource());
        }
        catch (Exception excepcion) {
            throw new IOException("Error generando el informe Jasper", excepcion);
        }
        finally {
            if (flujo != null) {
                try {
                    flujo.close();
                }
                catch (IOException ignorada) {
                    LOGGER.debug("Error cerrando el flujo de la plantilla", ignorada);
                }
            }
        }
    }

    private String resolverDirectorioSubinformes() {
        if (directorioPlantillasSeleccionado != null && Files.exists(directorioPlantillasSeleccionado)) {
            return directorioPlantillasSeleccionado.toAbsolutePath().toString() + File.separator;
        }
        if (rutaInformesConfigurada != null) {
            try {
                Path rutaBase = Paths.get(rutaInformesConfigurada);
                List<Path> candidatos = Arrays.asList(
                        rutaBase,
                        rutaBase.resolve("ventas").resolve("facturas"),
                        rutaBase.resolve("facturas"));
                for (Path candidato : candidatos) {
                    if (Files.exists(candidato)) {
                        directorioPlantillasSeleccionado = candidato;
                        return candidato.toAbsolutePath().toString() + File.separator;
                    }
                }
            }
            catch (Exception excepcion) {
                LOGGER.debug("No fue posible construir la ruta física de subinformes", excepcion);
            }
        }
        return obtenerDirectorioSubinformes().toString() + File.separator;
    }

    private Path obtenerDirectorioSubinformes() {
        if (directorioSubinformesTemporal != null && Files.exists(directorioSubinformesTemporal)) {
            return directorioSubinformesTemporal;
        }
        synchronized (this) {
            if (directorioSubinformesTemporal != null && Files.exists(directorioSubinformesTemporal)) {
                return directorioSubinformesTemporal;
            }
            try {
                Path directorioTemporal = Files.createTempDirectory("facturas-brico");
                copiarSubinformes(directorioTemporal);
                directorioSubinformesTemporal = directorioTemporal;
                return directorioTemporal;
            }
            catch (IOException excepcion) {
                throw new DocumentoVentaImpresionException(
                        "No se pudo preparar el directorio temporal de subinformes", excepcion);
            }
        }
    }

    private void copiarSubinformes(Path directorioDestino) throws IOException {
        Resource[] recursos = buscadorRecursos.getResources("classpath*:informes/ventas/facturas/*.jasper");
        for (Resource recurso : recursos) {
            if (!recurso.isReadable()) {
                continue;
            }
            String nombreFichero = recurso.getFilename();
            if (nombreFichero == null) {
                continue;
            }
            try (InputStream entrada = recurso.getInputStream()) {
                Files.copy(entrada, directorioDestino.resolve(nombreFichero), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    private boolean esDocumentoDevolucion(Object ticketVenta) {
        String tipoDocumento = convertirTexto(leerPropiedad(ticketVenta,
                "cabecera.codTipoDocumento",
                "cabecera.tipoDocumento",
                "cabecera.desTipoDocumento"));
        if (tipoDocumento == null) {
            return false;
        }
        String tipoNormalizado = tipoDocumento.toUpperCase(Locale.ROOT);
        return tipoNormalizado.contains("DEVOL") || tipoNormalizado.equals("FR");
    }

    private InputStream generarCodigoQr(String contenido) {
        try {
            Map<EncodeHintType, Object> pistas = new HashMap<>();
            pistas.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            pistas.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            QRCodeWriter generador = new QRCodeWriter();
            int dimension = 250;
            BitMatrix bitMatrix = generador.encode(contenido, BarcodeFormat.QR_CODE, dimension, dimension, pistas);
            BufferedImage imagen = new BufferedImage(dimension, dimension, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < dimension; x++) {
                for (int y = 0; y < dimension; y++) {
                    imagen.setRGB(x, y, bitMatrix.get(x, y) ? 0x000000 : 0xFFFFFF);
                }
            }
            ByteArrayOutputStream salida = new ByteArrayOutputStream();
            ImageIO.write(imagen, "png", salida);
            return new ByteArrayInputStream(salida.toByteArray());
        }
        catch (WriterException | IOException excepcion) {
            LOGGER.warn("No fue posible generar el código QR", excepcion);
            return null;
        }
    }

    private Date convertirFecha(Object posibleFecha) {
        if (posibleFecha == null) {
            return null;
        }
        if (posibleFecha instanceof Date) {
            return (Date) posibleFecha;
        }
        if (posibleFecha instanceof TemporalAccessor) {
            TemporalAccessor temporal = (TemporalAccessor) posibleFecha;
            return Date.from(java.time.Instant.from(temporal));
        }
        if (posibleFecha instanceof Long) {
            return new Date((Long) posibleFecha);
        }
        if (posibleFecha instanceof String) {
            try {
                long milis = Long.parseLong((String) posibleFecha);
                return new Date(milis);
            }
            catch (NumberFormatException excepcion) {
                LOGGER.debug("No se pudo convertir la fecha desde texto {}", posibleFecha);
            }
        }
        return null;
    }

    private InputStream convertirImagen(Object posibleImagen) {
        if (posibleImagen == null) {
            return null;
        }
        if (posibleImagen instanceof byte[]) {
            byte[] datos = (byte[]) posibleImagen;
            if (datos.length == 0) {
                return null;
            }
            return new ByteArrayInputStream(datos);
        }
        if (posibleImagen instanceof InputStream) {
            return (InputStream) posibleImagen;
        }
        return null;
    }

    private String convertirTexto(Object valor) {
        if (valor == null) {
            return null;
        }
        if (valor instanceof String) {
            return (String) valor;
        }
        return String.valueOf(valor);
    }

    private Object leerPropiedad(Object origen, String... rutas) {
        if (origen == null || rutas == null) {
            return null;
        }
        for (String ruta : rutas) {
            if (ruta == null) {
                continue;
            }
            Object resultado = recorrerPropiedad(origen, ruta);
            if (resultado != null) {
                return resultado;
            }
        }
        return null;
    }

    private Object recorrerPropiedad(Object origen, String ruta) {
        String[] pasos = ruta.split("\\.");
        Object actual = origen;
        for (String paso : pasos) {
            if (actual == null) {
                return null;
            }
            actual = invocarGetter(actual, paso);
        }
        return actual;
    }

    private Object invocarGetter(Object origen, String propiedad) {
        if (origen == null || propiedad == null) {
            return null;
        }
        Class<?> tipo = origen.getClass();
        List<String> nombresMetodos = generarNombresMetodos(propiedad);
        for (String nombre : nombresMetodos) {
            try {
                Method metodo = tipo.getMethod(nombre);
                return metodo.invoke(origen);
            }
            catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException excepcion) {
                // ignorar e intentar con el siguiente
            }
        }
        return null;
    }

    private List<String> generarNombresMetodos(String propiedad) {
        String capitalizado = propiedad.substring(0, 1).toUpperCase(Locale.ROOT) + propiedad.substring(1);
        return Arrays.asList(propiedad, "get" + capitalizado, "is" + capitalizado, "has" + capitalizado);
    }

    private String normalizarNombrePlantilla(String plantillaSolicitada) {
        if (plantillaSolicitada == null) {
            return null;
        }
        String texto = plantillaSolicitada.trim();
        if (texto.isEmpty()) {
            return null;
        }
        String textoNormalizado = texto.replace(' ', '_');
        String textoMinusculas = textoNormalizado.toLowerCase(Locale.ROOT);
        if (textoMinusculas.endsWith(".jasper")) {
            textoNormalizado = textoNormalizado.substring(0, textoNormalizado.length() - ".jasper".length());
            textoMinusculas = textoNormalizado.toLowerCase(Locale.ROOT);
        }
        else if (textoMinusculas.endsWith(".jrxml")) {
            textoNormalizado = textoNormalizado.substring(0, textoNormalizado.length() - ".jrxml".length());
            textoMinusculas = textoNormalizado.toLowerCase(Locale.ROOT);
        }
        textoMinusculas = textoMinusculas.replace('-', '_');
        if (ALIAS_PLANTILLAS.containsKey(textoMinusculas)) {
            return ALIAS_PLANTILLAS.get(textoMinusculas);
        }
        return textoNormalizado;
    }

    private InputStream localizarPlantillaEnSistemaArchivos(PlantillaFactura plantilla) {
        try {
            Path rutaBase = Paths.get(rutaInformesConfigurada);
            List<Path> candidatos = new ArrayList<>();
            candidatos.add(rutaBase.resolve(plantilla.getNombre() + ".jasper"));
            candidatos.add(rutaBase.resolve("ventas").resolve("facturas").resolve(plantilla.getNombre() + ".jasper"));
            candidatos.add(rutaBase.resolve("facturas").resolve(plantilla.getNombre() + ".jasper"));
            for (Path candidato : candidatos) {
                if (Files.exists(candidato)) {
                    directorioPlantillasSeleccionado = candidato.getParent();
                    return Files.newInputStream(candidato);
                }
            }
        }
        catch (Exception excepcion) {
            LOGGER.debug("No se pudo localizar la plantilla en el sistema de ficheros", excepcion);
        }
        return null;
    }

    private static final class PlantillaFactura {
        private final String nombre;
        private final int version;

        PlantillaFactura(String nombre, int version) {
            this.nombre = nombre;
            this.version = version;
        }

        String getNombre() {
            return nombre;
        }

        int getVersion() {
            return version;
        }
    }
}
