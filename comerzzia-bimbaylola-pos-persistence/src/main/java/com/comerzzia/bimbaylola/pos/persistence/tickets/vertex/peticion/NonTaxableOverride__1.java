
package com.comerzzia.bimbaylola.pos.persistence.tickets.vertex.peticion;

import java.math.BigDecimal;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "amount",
    "impositionType",
    "jurisdictionType",
    "reasonCode"
})
@Generated("jsonschema2pojo")
public class NonTaxableOverride__1 {

    @JsonProperty("amount")
    private BigDecimal amount;
    @JsonProperty("impositionType")
    private ImpositionType__10 impositionType;
    @JsonProperty("jurisdictionType")
    private String jurisdictionType;
    @JsonProperty("reasonCode")
    private String reasonCode;

    @JsonProperty("amount")
    public BigDecimal getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @JsonProperty("impositionType")
    public ImpositionType__10 getImpositionType() {
        return impositionType;
    }

    @JsonProperty("impositionType")
    public void setImpositionType(ImpositionType__10 impositionType) {
        this.impositionType = impositionType;
    }

    @JsonProperty("jurisdictionType")
    public String getJurisdictionType() {
        return jurisdictionType;
    }

    @JsonProperty("jurisdictionType")
    public void setJurisdictionType(String jurisdictionType) {
        this.jurisdictionType = jurisdictionType;
    }

    @JsonProperty("reasonCode")
    public String getReasonCode() {
        return reasonCode;
    }

    @JsonProperty("reasonCode")
    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

}
