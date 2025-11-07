
package com.comerzzia.bimbaylola.pos.persistence.tickets.vertex.peticion;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "productClass",
    "value"
})
@Generated("jsonschema2pojo")
public class Product {

    @JsonProperty("productClass")
    private String productClass;
    @JsonProperty("value")
    private String value;

    @JsonProperty("productClass")
    public String getProductClass() {
        return productClass;
    }

    @JsonProperty("productClass")
    public void setProductClass(String productClass) {
        this.productClass = productClass;
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
