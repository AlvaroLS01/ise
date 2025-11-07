
package com.comerzzia.bimbaylola.pos.persistence.tickets.vertex.peticion;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "overrideReasonCode",
    "overrideType"
})
@Generated("jsonschema2pojo")
public class TaxOverride__1 {

    @JsonProperty("overrideReasonCode")
    private String overrideReasonCode;
    @JsonProperty("overrideType")
    private String overrideType;

    @JsonProperty("overrideReasonCode")
    public String getOverrideReasonCode() {
        return overrideReasonCode;
    }

    @JsonProperty("overrideReasonCode")
    public void setOverrideReasonCode(String overrideReasonCode) {
        this.overrideReasonCode = overrideReasonCode;
    }

    @JsonProperty("overrideType")
    public String getOverrideType() {
        return overrideType;
    }

    @JsonProperty("overrideType")
    public void setOverrideType(String overrideType) {
        this.overrideType = overrideType;
    }

}
