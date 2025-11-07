
package com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.vertex;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "commodityCodeType",
    "value"
})
@Generated("jsonschema2pojo")
public class CommodityCode {

    @JsonProperty("commodityCodeType")
    private String commodityCodeType;
    @JsonProperty("value")
    private String value;

    @JsonProperty("commodityCodeType")
    public String getCommodityCodeType() {
        return commodityCodeType;
    }

    @JsonProperty("commodityCodeType")
    public void setCommodityCodeType(String commodityCodeType) {
        this.commodityCodeType = commodityCodeType;
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
