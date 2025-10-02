package com.comerzzia.bricodepot.api.omnichannel.api.web.salesdocument;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SalesDocumentPrintOptions {

    private final String mimeType;
    private final boolean copy;
    private final boolean inline;
    private final String outputDocumentName;
    private final String printTemplate;
    private final Map<String, String> customParams;

    public SalesDocumentPrintOptions(String mimeType,
                                     boolean copy,
                                     boolean inline,
                                     String outputDocumentName,
                                     String printTemplate,
                                     Map<String, String> customParams) {
        this.mimeType = mimeType;
        this.copy = copy;
        this.inline = inline;
        this.outputDocumentName = outputDocumentName;
        this.printTemplate = printTemplate;
        this.customParams = customParams == null ? Collections.emptyMap() : Collections.unmodifiableMap(new HashMap<>(customParams));
    }

    public String getMimeType() {
        return mimeType;
    }

    public boolean isCopy() {
        return copy;
    }

    public boolean isInline() {
        return inline;
    }

    public String getOutputDocumentName() {
        return outputDocumentName;
    }

    public String getPrintTemplate() {
        return printTemplate;
    }

    public Map<String, String> getCustomParams() {
        return customParams;
    }
}
