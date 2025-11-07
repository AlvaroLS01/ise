
package com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.vertex;

import java.math.BigDecimal;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "currency",
    "rate"
})
@Generated("jsonschema2pojo")
public class CurrencyConversion__7 {

    @JsonProperty("currency")
    private Currency__9 currency;
    @JsonProperty("rate")
    private BigDecimal rate;

    @JsonProperty("currency")
    public Currency__9 getCurrency() {
        return currency;
    }

    @JsonProperty("currency")
    public void setCurrency(Currency__9 currency) {
        this.currency = currency;
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
