
package com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.vertex;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "exemptionCertificateNumber",
    "value"
})
@Generated("jsonschema2pojo")
public class ExemptionCertificate {

    @JsonProperty("exemptionCertificateNumber")
    private String exemptionCertificateNumber;
    @JsonProperty("value")
    private String value;

    @JsonProperty("exemptionCertificateNumber")
    public String getExemptionCertificateNumber() {
        return exemptionCertificateNumber;
    }

    @JsonProperty("exemptionCertificateNumber")
    public void setExemptionCertificateNumber(String exemptionCertificateNumber) {
        this.exemptionCertificateNumber = exemptionCertificateNumber;
    }

    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(String value) {
        this.value = value;
    }

}
