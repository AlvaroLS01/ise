
package com.comerzzia.bimbaylola.pos.persistence.tickets.vertex.peticion;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "classCode",
    "isBusinessIndicator",
    "value"
})
@Generated("jsonschema2pojo")
public class CustomerCode {

    @JsonProperty("classCode")
    private String classCode;
    @JsonProperty("isBusinessIndicator")
    private Boolean isBusinessIndicator;
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

    @JsonProperty("isBusinessIndicator")
    public Boolean getIsBusinessIndicator() {
        return isBusinessIndicator;
    }

    @JsonProperty("isBusinessIndicator")
    public void setIsBusinessIndicator(Boolean isBusinessIndicator) {
        this.isBusinessIndicator = isBusinessIndicator;
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
