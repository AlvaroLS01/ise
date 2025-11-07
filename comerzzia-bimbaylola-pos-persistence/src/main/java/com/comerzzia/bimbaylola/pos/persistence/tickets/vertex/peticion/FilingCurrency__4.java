
package com.comerzzia.bimbaylola.pos.persistence.tickets.vertex.peticion;

import java.math.BigDecimal;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "isoCurrencyCodeAlpha",
    "isoCurrencyCodeNum",
    "isoCurrencyName"
})
@Generated("jsonschema2pojo")
public class FilingCurrency__4 {

    @JsonProperty("isoCurrencyCodeAlpha")
    private String isoCurrencyCodeAlpha;
    @JsonProperty("isoCurrencyCodeNum")
    private BigDecimal isoCurrencyCodeNum;
    @JsonProperty("isoCurrencyName")
    private String isoCurrencyName;

    @JsonProperty("isoCurrencyCodeAlpha")
    public String getIsoCurrencyCodeAlpha() {
        return isoCurrencyCodeAlpha;
    }

    @JsonProperty("isoCurrencyCodeAlpha")
    public void setIsoCurrencyCodeAlpha(String isoCurrencyCodeAlpha) {
        this.isoCurrencyCodeAlpha = isoCurrencyCodeAlpha;
    }

    @JsonProperty("isoCurrencyCodeNum")
    public BigDecimal getIsoCurrencyCodeNum() {
        return isoCurrencyCodeNum;
    }

    @JsonProperty("isoCurrencyCodeNum")
    public void setIsoCurrencyCodeNum(BigDecimal isoCurrencyCodeNum) {
        this.isoCurrencyCodeNum = isoCurrencyCodeNum;
    }

    @JsonProperty("isoCurrencyName")
    public String getIsoCurrencyName() {
        return isoCurrencyName;
    }

    @JsonProperty("isoCurrencyName")
    public void setIsoCurrencyName(String isoCurrencyName) {
        this.isoCurrencyName = isoCurrencyName;
    }

}
