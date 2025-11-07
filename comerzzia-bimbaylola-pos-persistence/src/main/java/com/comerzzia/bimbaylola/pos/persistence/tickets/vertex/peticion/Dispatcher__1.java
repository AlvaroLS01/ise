
package com.comerzzia.bimbaylola.pos.persistence.tickets.vertex.peticion;

import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "dispatcherCode",
    "taxRegistrations"
})
@Generated("jsonschema2pojo")
public class Dispatcher__1 {

    @JsonProperty("dispatcherCode")
    private DispatcherCode__1 dispatcherCode;
    @JsonProperty("taxRegistrations")
    private List<TaxRegistration__4> taxRegistrations = null;

    @JsonProperty("dispatcherCode")
    public DispatcherCode__1 getDispatcherCode() {
        return dispatcherCode;
    }

    @JsonProperty("dispatcherCode")
    public void setDispatcherCode(DispatcherCode__1 dispatcherCode) {
        this.dispatcherCode = dispatcherCode;
    }

    @JsonProperty("taxRegistrations")
    public List<TaxRegistration__4> getTaxRegistrations() {
        return taxRegistrations;
    }

    @JsonProperty("taxRegistrations")
    public void setTaxRegistrations(List<TaxRegistration__4> taxRegistrations) {
        this.taxRegistrations = taxRegistrations;
    }

}
