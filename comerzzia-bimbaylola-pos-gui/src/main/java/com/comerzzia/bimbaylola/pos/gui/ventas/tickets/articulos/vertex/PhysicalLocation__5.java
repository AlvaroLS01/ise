
package com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.vertex;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "city",
    "country",
    "mainDivision",
    "postalCode",
    "streetAddress1",
    "streetAddress2",
    "subDivision",
    "taxAreaId"
})
@Generated("jsonschema2pojo")
public class PhysicalLocation__5 {

    @JsonProperty("city")
    private String city;
    @JsonProperty("country")
    private String country;
    @JsonProperty("mainDivision")
    private String mainDivision;
    @JsonProperty("postalCode")
    private String postalCode;
    @JsonProperty("streetAddress1")
    private String streetAddress1;
    @JsonProperty("streetAddress2")
    private String streetAddress2;
    @JsonProperty("subDivision")
    private String subDivision;
    @JsonProperty("taxAreaId")
    private String taxAreaId;

    @JsonProperty("city")
    public String getCity() {
        return city;
    }

    @JsonProperty("city")
    public void setCity(String city) {
        this.city = city;
    }

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

    @JsonProperty("streetAddress1")
    public String getStreetAddress1() {
        return streetAddress1;
    }

    @JsonProperty("streetAddress1")
    public void setStreetAddress1(String streetAddress1) {
        this.streetAddress1 = streetAddress1;
    }

    @JsonProperty("streetAddress2")
    public String getStreetAddress2() {
        return streetAddress2;
    }

    @JsonProperty("streetAddress2")
    public void setStreetAddress2(String streetAddress2) {
        this.streetAddress2 = streetAddress2;
    }

    @JsonProperty("subDivision")
    public String getSubDivision() {
        return subDivision;
    }

    @JsonProperty("subDivision")
    public void setSubDivision(String subDivision) {
        this.subDivision = subDivision;
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
