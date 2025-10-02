package com.comerzzia.bricodepot.api.omnichannel.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.comerzzia.api.omnichannel.web.rest.salesdoc.SalesDocumentResource;
import com.comerzzia.bricodepot.api.omnichannel.api.web.salesdocument.SalesDocumentPrintService;

/**
 * Registers the custom sales document resource so that Jersey always uses the
 * version implemented for Brico Depot.
 */
@Configuration
public class SalesDocumentResourceConfiguration {

    private final SalesDocumentPrintService salesDocumentPrintService;

    public SalesDocumentResourceConfiguration(SalesDocumentPrintService salesDocumentPrintService) {
        this.salesDocumentPrintService = salesDocumentPrintService;
    }

    @Bean("salesDocumentResource")
    @Primary
    public SalesDocumentResource salesDocumentResource() {
        return new SalesDocumentResource(salesDocumentPrintService);
    }
}
