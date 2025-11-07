
package com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.vertex;

import java.math.BigDecimal;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "discountType",
    "discountValue",
    "userDefinedDiscountCode"
})
@Generated("jsonschema2pojo")
public class Discount__1 {

    @JsonProperty("discountType")
    private String discountType;
    @JsonProperty("discountValue")
    private BigDecimal discountValue;
    @JsonProperty("userDefinedDiscountCode")
    private String userDefinedDiscountCode;

    @JsonProperty("discountType")
    public String getDiscountType() {
        return discountType;
    }

    @JsonProperty("discountType")
    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    @JsonProperty("discountValue")
    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    @JsonProperty("discountValue")
    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    @JsonProperty("userDefinedDiscountCode")
    public String getUserDefinedDiscountCode() {
        return userDefinedDiscountCode;
    }

    @JsonProperty("userDefinedDiscountCode")
    public void setUserDefinedDiscountCode(String userDefinedDiscountCode) {
        this.userDefinedDiscountCode = userDefinedDiscountCode;
    }

}
