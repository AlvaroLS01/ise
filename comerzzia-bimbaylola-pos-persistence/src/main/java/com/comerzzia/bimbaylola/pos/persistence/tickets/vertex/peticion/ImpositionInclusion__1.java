
package com.comerzzia.bimbaylola.pos.persistence.tickets.vertex.peticion;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "impositionType",
    "jurisdictionType"
})
@Generated("jsonschema2pojo")
public class ImpositionInclusion__1 {

    @JsonProperty("impositionType")
    private ImpositionType__5 impositionType;
    @JsonProperty("jurisdictionType")
    private String jurisdictionType;

    @JsonProperty("impositionType")
    public ImpositionType__5 getImpositionType() {
        return impositionType;
    }

    @JsonProperty("impositionType")
    public void setImpositionType(ImpositionType__5 impositionType) {
        this.impositionType = impositionType;
    }

    @JsonProperty("jurisdictionType")
    public String getJurisdictionType() {
        return jurisdictionType;
    }

    @JsonProperty("jurisdictionType")
    public void setJurisdictionType(String jurisdictionType) {
        this.jurisdictionType = jurisdictionType;
    }

}
