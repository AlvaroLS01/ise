
package com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.vertex.recibir;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "country",
    "mainDivision",
    "postalCode",
    "taxAreaId"
})
@Generated("jsonschema2pojo")
public class PhysicalOrigin {

    @JsonProperty("country")
    private String country;
    @JsonProperty("mainDivision")
    private String mainDivision;
    @JsonProperty("postalCode")
    private String postalCode;
    @JsonProperty("taxAreaId")
    private String taxAreaId;

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    @JsonProperty("country")
    public void setCountry(String country) {
        this.country = country;
    }

    @JsonProperty("mainDivision")
    public String getMainDivision() {
        return mainDivision;
    }

    @JsonProperty("mainDivision")
    public void setMainDivision(String mainDivision) {
        this.mainDivision = mainDivision;
    }

    @JsonProperty("postalCode")
    public String getPostalCode() {
        return postalCode;
    }

    @JsonProperty("postalCode")
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @JsonProperty("taxAreaId")
    public String getTaxAreaId() {
        return taxAreaId;
    }

    @JsonProperty("taxAreaId")
    public void setTaxAreaId(String taxAreaId) {
        this.taxAreaId = taxAreaId;
    }

}
