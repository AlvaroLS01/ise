
package com.comerzzia.bimbaylola.pos.persistence.tickets.vertex.peticion;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "taxingLocation"
})
@Generated("jsonschema2pojo")
public class SitusOverride__1 {

    @JsonProperty("taxingLocation")
    private String taxingLocation;

    @JsonProperty("taxingLocation")
    public String getTaxingLocation() {
        return taxingLocation;
    }

    @JsonProperty("taxingLocation")
    public void setTaxingLocation(String taxingLocation) {
        this.taxingLocation = taxingLocation;
    }

}
