
package com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.vertex;

import java.math.BigDecimal;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "conversionFactor",
    "sourceCurrency",
    "targetCurrency"
})
@Generated("jsonschema2pojo")
public class CurrencyConversionFactor {

    @JsonProperty("conversionFactor")
    private BigDecimal conversionFactor;
    @JsonProperty("sourceCurrency")
    private SourceCurrency sourceCurrency;
    @JsonProperty("targetCurrency")
    private TargetCurrency targetCurrency;

    @JsonProperty("conversionFactor")
    public BigDecimal getConversionFactor() {
        return conversionFactor;
    }

    @JsonProperty("conversionFactor")
    public void setConversionFactor(BigDecimal conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    @JsonProperty("sourceCurrency")
    public SourceCurrency getSourceCurrency() {
        return sourceCurrency;
    }

    @JsonProperty("sourceCurrency")
    public void setSourceCurrency(SourceCurrency sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
    }

    @JsonProperty("targetCurrency")
    public TargetCurrency getTargetCurrency() {
        return targetCurrency;
    }

    @JsonProperty("targetCurrency")
    public void setTargetCurrency(TargetCurrency targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

}
