
package com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.vertex;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "classCode",
    "value"
})
@Generated("jsonschema2pojo")
public class DispatcherCode__1 {

    @JsonProperty("classCode")
    private String classCode;
    @JsonProperty("value")
    private String value;

    @JsonProperty("classCode")
    public String getClassCode() {
        return classCode;
    }

    @JsonProperty("classCode")
    public void setClassCode(String classCode) {
        this.classCode = classCode;
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
