package com.comerzzia.bricodepot.api.omnichannel.api.web.salesdocument;

import java.io.Serializable;

public class SalesDocumentPrintResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String documentUid;
    private String mimeType;
    private String fileName;
    private boolean copy;
    private boolean inline;
    private String document;

    public String getDocumentUid() {
        return documentUid;
    }

    public void setDocumentUid(String documentUid) {
        this.documentUid = documentUid;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isCopy() {
        return copy;
    }

    public void setCopy(boolean copy) {
        this.copy = copy;
    }

    public boolean isInline() {
        return inline;
    }

    public void setInline(boolean inline) {
        this.inline = inline;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }
}
