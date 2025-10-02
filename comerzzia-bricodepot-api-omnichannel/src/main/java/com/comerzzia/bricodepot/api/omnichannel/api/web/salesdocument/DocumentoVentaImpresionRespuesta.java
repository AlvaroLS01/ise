package com.comerzzia.bricodepot.api.omnichannel.api.web.salesdocument;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DocumentoVentaImpresionRespuesta implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("documentUid")
    private String uidDocumento;

    @JsonProperty("mimeType")
    private String tipoMime;

    @JsonProperty("fileName")
    private String nombreArchivo;

    @JsonProperty("copy")
    private boolean copia;

    @JsonProperty("inline")
    private boolean enLinea;

    @JsonProperty("document")
    private String documento;

    public String getUidDocumento() {
        return uidDocumento;
    }

    public void setUidDocumento(String uidDocumento) {
        this.uidDocumento = uidDocumento;
    }

    public String getTipoMime() {
        return tipoMime;
    }

    public void setTipoMime(String tipoMime) {
        this.tipoMime = tipoMime;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public boolean isCopia() {
        return copia;
    }

    public void setCopia(boolean copia) {
        this.copia = copia;
    }

    public boolean isEnLinea() {
        return enLinea;
    }

    public void setEnLinea(boolean enLinea) {
        this.enLinea = enLinea;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }
}
