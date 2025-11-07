
package com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.vertex;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "accumulationLocation",
    "content",
    "direction",
    "status",
    "value"
})
@Generated("jsonschema2pojo")
public class LineType {

    @JsonProperty("accumulationLocation")
    private String accumulationLocation;
    @JsonProperty("content")
    private String content;
    @JsonProperty("direction")
    private String direction;
    @JsonProperty("status")
    private String status;
    @JsonProperty("value")
    private String value;

    @JsonProperty("accumulationLocation")
    public String getAccumulationLocation() {
        return accumulationLocation;
    }

    @JsonProperty("accumulationLocation")
    public void setAccumulationLocation(String accumulationLocation) {
        this.accumulationLocation = accumulationLocation;
    }

    @JsonProperty("content")
    public String getContent() {
        return content;
    }

    @JsonProperty("content")
    public void setContent(String content) {
        this.content = content;
    }

    @JsonProperty("direction")
    public String getDirection() {
        return direction;
    }

    @JsonProperty("direction")
    public void setDirection(String direction) {
        this.direction = direction;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
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
