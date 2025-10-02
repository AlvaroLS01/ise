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
public class SalesDocumentPrintController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SalesDocumentPrintController.class);

    private final SalesDocumentPrintService printService;
    private final SessionService sessionService;

    @Autowired
    public SalesDocumentPrintController(SalesDocumentPrintService printService, SessionService sessionService) {
        this.printService = printService;
        this.sessionService = sessionService;
    }

    @GetMapping(value = "/{documentUid}/print")
    public ResponseEntity<SalesDocumentPrintResponse> print(@PathVariable("documentUid") String documentUid,
            @RequestParam(value = "mimeType", required = false, defaultValue = "application/pdf") String mimeType,
            @RequestParam(value = "copy", required = false, defaultValue = "false") boolean copy,
            @RequestParam(value = "inline", required = false, defaultValue = "false") boolean inline,
            @RequestParam(value = "outputDocumentName", required = false) String outputDocumentName,
            @RequestParam(value = "printTemplate", required = false) String printTemplate,
            @RequestParam Map<String, String> queryParams) {

        Map<String, String> customParams = new HashMap<>(queryParams);
        customParams.remove("mimeType");
        customParams.remove("copy");
        customParams.remove("inline");
        customParams.remove("outputDocumentName");
        customParams.remove("printTemplate");

        SalesDocumentPrintOptions options = new SalesDocumentPrintOptions(mimeType, copy, inline, outputDocumentName,
                printTemplate, customParams);

        IDatosSesion datosSesion = sessionService.getDatosSesion();
        if (datosSesion == null) {
            LOGGER.warn("print() - Unable to obtain session data for current request");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<SalesDocumentPrintResponse> response = printService.print(documentUid, options, datosSesion);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.ok().body(null));
    }
}
