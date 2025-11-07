
package com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.vertex;

import java.math.BigDecimal;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "impositionType",
    "jurisdictionType",
    "rate"
})
@Generated("jsonschema2pojo")
public class RateOverride {

    @JsonProperty("impositionType")
    private ImpositionType__7 impositionType;
    @JsonProperty("jurisdictionType")
    private String jurisdictionType;
    @JsonProperty("rate")
    private BigDecimal rate;

    @JsonProperty("impositionType")
    public ImpositionType__7 getImpositionType() {
        return impositionType;
    }

    @JsonProperty("impositionType")
    public void setImpositionType(ImpositionType__7 impositionType) {
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

    @JsonProperty("rate")
    public BigDecimal getRate() {
        return rate;
    }

    @JsonProperty("rate")
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

}
