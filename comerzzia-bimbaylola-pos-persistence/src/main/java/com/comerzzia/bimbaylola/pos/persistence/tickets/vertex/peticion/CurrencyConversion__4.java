
package com.comerzzia.bimbaylola.pos.persistence.tickets.vertex.peticion;

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
public class CurrencyConversion__4 {

    @JsonProperty("currency")
    private Currency__5 currency;
    @JsonProperty("rate")
    private BigDecimal rate;

    @JsonProperty("currency")
    public Currency__5 getCurrency() {
        return currency;
    }

    @JsonProperty("currency")
    public void setCurrency(Currency__5 currency) {
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
