package com.comerzzia.bricodepot.api.omnichannel.api.web.salesdocument;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.comerzzia.api.core.services.session.SessionService;
import com.comerzzia.core.servicios.sesion.IDatosSesion;

@RestController
@RequestMapping("/salesdocument")
public class DocumentoVentaImpresionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentoVentaImpresionController.class);

    private final DocumentoVentaImpresionServicio servicioImpresion;
    private final SessionService servicioSesion;

    @Autowired
    public DocumentoVentaImpresionController(DocumentoVentaImpresionServicio servicioImpresion,
                                             SessionService servicioSesion) {
        this.servicioImpresion = servicioImpresion;
        this.servicioSesion = servicioSesion;
    }

    @GetMapping(value = "/{documentUid}/print")
    public ResponseEntity<DocumentoVentaImpresionRespuesta> imprimir(
            @PathVariable("documentUid") String uidDocumento,
            @RequestParam(value = "mimeType", required = false, defaultValue = "application/pdf") String tipoMime,
            @RequestParam(value = "copy", required = false, defaultValue = "false") boolean esCopia,
            @RequestParam(value = "inline", required = false, defaultValue = "false") boolean enLinea,
            @RequestParam(value = "outputDocumentName", required = false) String nombreDocumentoSalida,
            @RequestParam(value = "printTemplate", required = false) String plantillaImpresion,
            @RequestParam Map<String, String> parametrosPeticion) {

        Map<String, String> parametrosPersonalizados = new HashMap<>(parametrosPeticion);
        parametrosPersonalizados.remove("mimeType");
        parametrosPersonalizados.remove("copy");
        parametrosPersonalizados.remove("inline");
        parametrosPersonalizados.remove("outputDocumentName");
        parametrosPersonalizados.remove("printTemplate");

        OpcionesImpresionDocumentoVenta opciones = new OpcionesImpresionDocumentoVenta(
                tipoMime,
                esCopia,
                enLinea,
                nombreDocumentoSalida,
                plantillaImpresion,
                parametrosPersonalizados);

        IDatosSesion datosSesion = servicioSesion.getDatosSesion();
        if (datosSesion == null) {
            LOGGER.warn("imprimir() - No se pudieron obtener los datos de sesión para la petición actual");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<DocumentoVentaImpresionRespuesta> respuesta =
                servicioImpresion.imprimir(uidDocumento, opciones, datosSesion);

        return respuesta.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.ok().body(null));
    }
}
