
package com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.vertex.recibir;

import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "company",
    "physicalOrigin",
    "taxRegistrations"
})
@Generated("jsonschema2pojo")
public class Seller__1 {

    @JsonProperty("company")
    private String company;
    @JsonProperty("physicalOrigin")
    private PhysicalOrigin__1 physicalOrigin;
    @JsonProperty("taxRegistrations")
    private List<Object> taxRegistrations = null;

    @JsonProperty("company")
    public String getCompany() {
        return company;
    }

    @JsonProperty("company")
    public void setCompany(String company) {
        this.company = company;
    }

    @JsonProperty("physicalOrigin")
    public PhysicalOrigin__1 getPhysicalOrigin() {
        return physicalOrigin;
    }

    @JsonProperty("physicalOrigin")
    public void setPhysicalOrigin(PhysicalOrigin__1 physicalOrigin) {
        this.physicalOrigin = physicalOrigin;
    }

    @JsonProperty("taxRegistrations")
    public List<Object> getTaxRegistrations() {
        return taxRegistrations;
    }

    @JsonProperty("taxRegistrations")
    public void setTaxRegistrations(List<Object> taxRegistrations) {
        this.taxRegistrations = taxRegistrations;
    }

}
