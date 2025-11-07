
package com.comerzzia.bimbaylola.pos.persistence.tickets.vertex.peticion;

import java.math.BigDecimal;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "amount",
    "currency"
})
@Generated("jsonschema2pojo")
public class StatisticalValue {

    @JsonProperty("amount")
    private BigDecimal amount;
    @JsonProperty("currency")
    private Currency__7 currency;

    @JsonProperty("amount")
    public BigDecimal getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @JsonProperty("currency")
    public Currency__7 getCurrency() {
        return currency;
    }

    @JsonProperty("currency")
    public void setCurrency(Currency__7 currency) {
        this.currency = currency;
    }

}
