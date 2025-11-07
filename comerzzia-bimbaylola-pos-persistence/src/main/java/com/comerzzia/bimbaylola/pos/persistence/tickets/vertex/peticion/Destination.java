
package com.comerzzia.bimbaylola.pos.persistence.tickets.vertex.peticion;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "city",
    "country",
    "currencyConversion",
    "externalJurisdictionCode",
    "latitude",
    "locationCode",
    "locationCustomsStatus",
    "longitude",
    "mainDivision",
    "postalCode",
    "streetAddress1",
    "streetAddress2",
    "subDivision",
    "taxAreaId"
})
@Generated("jsonschema2pojo")
public class Destination {

    @JsonProperty("city")
    private String city;
    @JsonProperty("country")
    private String country;
    @JsonProperty("currencyConversion")
    private CurrencyConversion__1 currencyConversion;
    @JsonProperty("externalJurisdictionCode")
    private String externalJurisdictionCode;
    @JsonProperty("latitude")
    private String latitude;
    @JsonProperty("locationCode")
    private String locationCode;
    @JsonProperty("locationCustomsStatus")
    private String locationCustomsStatus;
    @JsonProperty("longitude")
    private String longitude;
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

    @JsonProperty("currencyConversion")
    public CurrencyConversion__1 getCurrencyConversion() {
        return currencyConversion;
    }

    @JsonProperty("currencyConversion")
    public void setCurrencyConversion(CurrencyConversion__1 currencyConversion) {
        this.currencyConversion = currencyConversion;
    }

    @JsonProperty("externalJurisdictionCode")
    public String getExternalJurisdictionCode() {
        return externalJurisdictionCode;
    }

    @JsonProperty("externalJurisdictionCode")
    public void setExternalJurisdictionCode(String externalJurisdictionCode) {
        this.externalJurisdictionCode = externalJurisdictionCode;
    }

    @JsonProperty("latitude")
    public String getLatitude() {
        return latitude;
    }

    @JsonProperty("latitude")
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @JsonProperty("locationCode")
    public String getLocationCode() {
        return locationCode;
    }

    @JsonProperty("locationCode")
    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    @JsonProperty("locationCustomsStatus")
    public String getLocationCustomsStatus() {
        return locationCustomsStatus;
    }

    @JsonProperty("locationCustomsStatus")
    public void setLocationCustomsStatus(String locationCustomsStatus) {
        this.locationCustomsStatus = locationCustomsStatus;
    }

    @JsonProperty("longitude")
    public String getLongitude() {
        return longitude;
    }

    @JsonProperty("longitude")
    public void setLongitude(String longitude) {
        this.longitude = longitude;
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
