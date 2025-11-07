
package com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.vertex;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "city",
    "country",
    "district",
    "locationRole",
    "mainDivision",
    "subDivision"
})
@Generated("jsonschema2pojo")
public class NexusOverride__3 {

    @JsonProperty("city")
    private Boolean city;
    @JsonProperty("country")
    private Boolean country;
    @JsonProperty("district")
    private Boolean district;
    @JsonProperty("locationRole")
    private String locationRole;
    @JsonProperty("mainDivision")
    private Boolean mainDivision;
    @JsonProperty("subDivision")
    private Boolean subDivision;

    @JsonProperty("city")
    public Boolean getCity() {
        return city;
    }

    @JsonProperty("city")
    public void setCity(Boolean city) {
        this.city = city;
    }

    @JsonProperty("country")
    public Boolean getCountry() {
        return country;
    }

    @JsonProperty("country")
    public void setCountry(Boolean country) {
        this.country = country;
    }

    @JsonProperty("district")
    public Boolean getDistrict() {
        return district;
    }

    @JsonProperty("district")
    public void setDistrict(Boolean district) {
        this.district = district;
    }

    @JsonProperty("locationRole")
    public String getLocationRole() {
        return locationRole;
    }

    @JsonProperty("locationRole")
    public void setLocationRole(String locationRole) {
        this.locationRole = locationRole;
    }

    @JsonProperty("mainDivision")
    public Boolean getMainDivision() {
        return mainDivision;
    }

    @JsonProperty("mainDivision")
    public void setMainDivision(Boolean mainDivision) {
        this.mainDivision = mainDivision;
    }

    @JsonProperty("subDivision")
    public Boolean getSubDivision() {
        return subDivision;
    }

    @JsonProperty("subDivision")
    public void setSubDivision(Boolean subDivision) {
        this.subDivision = subDivision;
    }

}
