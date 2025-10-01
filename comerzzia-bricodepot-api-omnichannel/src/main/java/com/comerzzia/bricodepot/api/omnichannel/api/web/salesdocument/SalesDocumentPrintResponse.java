package com.comerzzia.bricodepot.api.omnichannel.api.web.salesdocument;

import java.util.Objects;

public class SalesDocumentPrintResponse {

    private final String mimeType;
    private final String fileName;
    private final String content;

    public SalesDocumentPrintResponse(String mimeType, String fileName, String content) {
        this.mimeType = Objects.requireNonNull(mimeType, "mimeType");
        this.fileName = Objects.requireNonNull(fileName, "fileName");
        this.content = Objects.requireNonNull(content, "content");
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContent() {
        return content;
    }
}
