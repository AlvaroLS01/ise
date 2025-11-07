
package com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.vertex;

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
public class Dispatcher {

    @JsonProperty("dispatcherCode")
    private DispatcherCode dispatcherCode;
    @JsonProperty("taxRegistrations")
    private List<TaxRegistration__2> taxRegistrations = null;

    @JsonProperty("dispatcherCode")
    public DispatcherCode getDispatcherCode() {
        return dispatcherCode;
    }

    @JsonProperty("dispatcherCode")
    public void setDispatcherCode(DispatcherCode dispatcherCode) {
        this.dispatcherCode = dispatcherCode;
    }

    @JsonProperty("taxRegistrations")
    public List<TaxRegistration__2> getTaxRegistrations() {
        return taxRegistrations;
    }

    @JsonProperty("taxRegistrations")
    public void setTaxRegistrations(List<TaxRegistration__2> taxRegistrations) {
        this.taxRegistrations = taxRegistrations;
    }

}
