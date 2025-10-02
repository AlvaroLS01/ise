package com.comerzzia.api.omnichannel.web.rest.salesdoc;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.InvalidMimeTypeException;

import com.comerzzia.bricodepot.api.omnichannel.api.web.salesdocument.SalesDocumentPrintResponse;
import com.comerzzia.bricodepot.api.omnichannel.api.web.salesdocument.SalesDocumentPrintService;

@Component
@Path("/salesdocument")
public class SalesDocumentResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SalesDocumentResource.class);

    private static final String PARAM_COPY = "copy";
    private static final String PARAM_INLINE = "inline";
    private static final String PARAM_TEMPLATE = "printTemplate";
    private static final String PARAM_OUTPUT_NAME = "outputDocumentName";
    private static final String PARAM_MIME = "mimeType";
    private static final String PARAM_CUSTOM = "customParams";

    private final SalesDocumentPrintService servicioImpresionDocumento;

    public SalesDocumentResource(SalesDocumentPrintService servicioImpresionDocumento) {
        this.servicioImpresionDocumento = servicioImpresionDocumento;
    }

    @GET
    @Path("/{documentUid}/print")
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public Response printSaleDocumentByUid(@PathParam("documentUid") String identificadorDocumento,
            @Context UriInfo informacionUri,
            @Context HttpServletRequest peticion,
            @Context HttpServletResponse respuesta) {

        MultivaluedMap<String, String> parametros = informacionUri.getQueryParameters(true);
        boolean esCopia = esParametroBooleanoActivo(parametros.get(PARAM_COPY));
        boolean esInline = esParametroBooleanoActivo(parametros.get(PARAM_INLINE));
        String plantillaSolicitada = extraerValor(parametros, PARAM_TEMPLATE);
        String nombreSalida = extraerValor(parametros, PARAM_OUTPUT_NAME);
        String tipoContenidoSolicitado = extraerValor(parametros, PARAM_MIME);
        Map<String, Object> parametrosPersonalizados = extraerParametrosPersonalizados(parametros);

        Optional<SalesDocumentPrintResponse> posibleDocumento = servicioImpresionDocumento.imprimirDocumento(
                identificadorDocumento,
                esCopia,
                plantillaSolicitada,
                nombreSalida,
                parametrosPersonalizados,
                tipoContenidoSolicitado);

        if (!posibleDocumento.isPresent()) {
            LOGGER.debug("No se encontró documento de venta con UID {}", identificadorDocumento);
            return Response.ok().build();
        }

        SalesDocumentPrintResponse documento = posibleDocumento.get();
        MediaType tipoCabecera = resolverMediaType(documento.getMimeType());
        ContentDisposition disposicion = ContentDisposition
                .builder(esInline ? "inline" : "attachment")
                .filename(documento.getFileName())
                .build();

        return Response.ok(documento)
                .header(HttpHeaders.CONTENT_TYPE, tipoCabecera.toString())
                .header(HttpHeaders.CONTENT_DISPOSITION, disposicion.toString())
                .build();
    }

    private boolean esParametroBooleanoActivo(List<String> valores) {
        if (valores == null || valores.isEmpty()) {
            return false;
        }
        String valor = valores.get(0);
        return valor != null && valor.trim().toLowerCase(Locale.ROOT).equals("true");
    }

    private String extraerValor(MultivaluedMap<String, String> parametros, String clave) {
        List<String> valores = parametros.get(clave);
        if (valores == null || valores.isEmpty()) {
            return null;
        }
        return valores.get(0);
    }

    private Map<String, Object> extraerParametrosPersonalizados(MultivaluedMap<String, String> parametros) {
        Map<String, Object> parametrosPersonalizados = new HashMap<>();

        for (Map.Entry<String, List<String>> entrada : parametros.entrySet()) {
            String clave = entrada.getKey();
            List<String> valores = entrada.getValue();
            if (clave == null) {
                continue;
            }

            if (PARAM_COPY.equalsIgnoreCase(clave)
                    || PARAM_TEMPLATE.equalsIgnoreCase(clave)
                    || PARAM_OUTPUT_NAME.equalsIgnoreCase(clave)
                    || PARAM_INLINE.equalsIgnoreCase(clave)
                    || PARAM_MIME.equalsIgnoreCase(clave)) {
                continue;
            }

            if (clave.startsWith(PARAM_CUSTOM + ".")) {
                String claveDestino = clave.substring((PARAM_CUSTOM + ".").length());
                if (!claveDestino.isEmpty() && valores != null && !valores.isEmpty()) {
                    parametrosPersonalizados.put(claveDestino, valores.get(0));
                }
                continue;
            }

            if (clave.startsWith(PARAM_CUSTOM + "[") && clave.endsWith("]")) {
                String claveDestino = clave.substring((PARAM_CUSTOM + "[").length(), clave.length() - 1);
                if (!claveDestino.isEmpty() && valores != null && !valores.isEmpty()) {
                    parametrosPersonalizados.put(claveDestino, valores.get(0));
                }
            }
        }

        if (parametros.containsKey(PARAM_CUSTOM)) {
            String valorPlano = extraerValor(parametros, PARAM_CUSTOM);
            if (valorPlano != null) {
                parametrosPersonalizados.put(PARAM_CUSTOM, valorPlano);
            }
        }

        return parametrosPersonalizados;
    }

    private MediaType resolverMediaType(String tipoContenidoSolicitado) {
        if (tipoContenidoSolicitado == null || tipoContenidoSolicitado.trim().isEmpty()) {
            return MediaType.APPLICATION_PDF;
        }
        try {
            return MediaType.parseMediaType(tipoContenidoSolicitado);
        } catch (InvalidMimeTypeException ex) {
            LOGGER.warn("Tipo MIME {} no válido. Se utilizará application/pdf", tipoContenidoSolicitado);
            return MediaType.APPLICATION_PDF;
        }
    }
}
