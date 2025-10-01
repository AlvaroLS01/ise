package com.comerzzia.bricodepot.api.omnichannel.api.web.salesdocument;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class SalesDocumentPrintFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SalesDocumentPrintFilter.class);

    private final SalesDocumentPrintService servicioImpresionDocumento;
    private final SalesDocumentPrintRequestMapper mapeadorPeticion;
    private final ObjectMapper objectMapper;

    SalesDocumentPrintFilter(SalesDocumentPrintService servicioImpresionDocumento,
            SalesDocumentPrintRequestMapper mapeadorPeticion,
            ObjectMapper objectMapper) {
        this.servicioImpresionDocumento = servicioImpresionDocumento;
        this.mapeadorPeticion = mapeadorPeticion;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (!debeInterceptar(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String identificadorDocumento = extraerIdentificador(request);
        if (identificadorDocumento == null) {
            filterChain.doFilter(request, response);
            return;
        }

        MultiValueMap<String, String> parametros = construirParametros(request);
        SalesDocumentPrintParameters parametrosImpresion = mapeadorPeticion.map(parametros);

        Optional<SalesDocumentPrintResponse> posibleRespuesta = servicioImpresionDocumento.imprimirDocumento(
                identificadorDocumento,
                parametrosImpresion.esCopia(),
                parametrosImpresion.getPlantillaSolicitada(),
                parametrosImpresion.getNombreSalida(),
                parametrosImpresion.getParametrosPersonalizados(),
                parametrosImpresion.getTipoContenido());

        if (!posibleRespuesta.isPresent()) {
            LOGGER.debug("No se encontró el documento {}. Respondiendo null", identificadorDocumento);
            escribirRespuesta(response, null, parametrosImpresion);
            return;
        }

        SalesDocumentPrintResponse documento = posibleRespuesta.get();
        escribirRespuesta(response, documento, parametrosImpresion);
    }

    private boolean debeInterceptar(HttpServletRequest request) {
        if (!"GET".equalsIgnoreCase(request.getMethod())) {
            return false;
        }
        String contexto = request.getContextPath() == null ? "" : request.getContextPath();
        String uri = request.getRequestURI();
        if (uri == null) {
            return false;
        }
        String ruta = uri.substring(contexto.length());
        return ruta.startsWith("/salesdocument/") && ruta.endsWith("/print");
    }

    private String extraerIdentificador(HttpServletRequest request) {
        String contexto = request.getContextPath() == null ? "" : request.getContextPath();
        String uri = request.getRequestURI();
        if (uri == null) {
            return null;
        }
        String ruta = uri.substring(contexto.length());
        String[] segmentos = ruta.split("/");
        if (segmentos.length < 4) {
            return null;
        }
        return decodificar(segmentos[2]);
    }

    private String decodificar(String valor) {
        if (valor == null) {
            return null;
        }
        return URLDecoder.decode(valor, StandardCharsets.UTF_8.name());
    }

    private MultiValueMap<String, String> construirParametros(HttpServletRequest request) {
        MultiValueMap<String, String> parametros = new LinkedMultiValueMap<>();
        request.getParameterMap().forEach((clave, valores) -> {
            if (valores != null) {
                for (String valor : valores) {
                    parametros.add(clave, valor);
                }
            }
        });
        if (parametros.isEmpty() && request.getQueryString() != null) {
            parametros.addAll(UriComponentsBuilder.fromUriString("?" + request.getQueryString()).build().getQueryParams());
        }
        return parametros;
    }

    private void escribirRespuesta(HttpServletResponse response,
            SalesDocumentPrintResponse documento,
            SalesDocumentPrintParameters parametros) throws IOException {

        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        if (documento == null) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("null");
            return;
        }

        MediaType mediaType = mapeadorPeticion.resolverMediaType(documento.getMimeType());
        response.setContentType(mediaType.toString());
        ContentDisposition disposicionContenido = ContentDisposition
                .builder(parametros.esInline() ? "inline" : "attachment")
                .filename(documento.getFileName())
                .build();
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, disposicionContenido.toString());
        objectMapper.writeValue(response.getOutputStream(), documento);
    }
}
