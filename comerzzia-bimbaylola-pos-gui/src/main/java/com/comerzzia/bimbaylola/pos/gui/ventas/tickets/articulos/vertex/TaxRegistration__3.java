
package com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.vertex;

import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "hasPhysicalPresenceIndicator",
    "impositionType",
    "isoCountryCode",
    "jurisdictionId",
    "mainDivision",
    "nexusOverrides",
    "physicalLocations",
    "taxRegistrationNumber",
    "filingCurrency",
    "taxRegistrationType"
})
@Generated("jsonschema2pojo")
public class TaxRegistration__3 {

    @JsonProperty("hasPhysicalPresenceIndicator")
    private Boolean hasPhysicalPresenceIndicator;
    @JsonProperty("impositionType")
    private ImpositionType__9 impositionType;
    @JsonProperty("isoCountryCode")
    private String isoCountryCode;
    @JsonProperty("jurisdictionId")
    private String jurisdictionId;
    @JsonProperty("mainDivision")
    private String mainDivision;
    @JsonProperty("nexusOverrides")
    private List<NexusOverride__3> nexusOverrides = null;
    @JsonProperty("physicalLocations")
    private List<PhysicalLocation__3> physicalLocations = null;
    @JsonProperty("taxRegistrationNumber")
    private String taxRegistrationNumber;
    @JsonProperty("filingCurrency")
    private FilingCurrency__3 filingCurrency;
    @JsonProperty("taxRegistrationType")
    private String taxRegistrationType;

    @JsonProperty("hasPhysicalPresenceIndicator")
    public Boolean getHasPhysicalPresenceIndicator() {
        return hasPhysicalPresenceIndicator;
    }

    @JsonProperty("hasPhysicalPresenceIndicator")
    public void setHasPhysicalPresenceIndicator(Boolean hasPhysicalPresenceIndicator) {
        this.hasPhysicalPresenceIndicator = hasPhysicalPresenceIndicator;
    }

    @JsonProperty("impositionType")
    public ImpositionType__9 getImpositionType() {
        return impositionType;
    }

    @JsonProperty("impositionType")
    public void setImpositionType(ImpositionType__9 impositionType) {
        this.impositionType = impositionType;
    }

    @JsonProperty("isoCountryCode")
    public String getIsoCountryCode() {
        return isoCountryCode;
    }

    @JsonProperty("isoCountryCode")
    public void setIsoCountryCode(String isoCountryCode) {
        this.isoCountryCode = isoCountryCode;
    }

    @JsonProperty("jurisdictionId")
    public String getJurisdictionId() {
        return jurisdictionId;
    }

    @JsonProperty("jurisdictionId")
    public void setJurisdictionId(String jurisdictionId) {
        this.jurisdictionId = jurisdictionId;
    }

    @JsonProperty("mainDivision")
    public String getMainDivision() {
        return mainDivision;
    }

    @JsonProperty("mainDivision")
    public void setMainDivision(String mainDivision) {
        this.mainDivision = mainDivision;
    }

    @JsonProperty("nexusOverrides")
    public List<NexusOverride__3> getNexusOverrides() {
        return nexusOverrides;
    }

    @JsonProperty("nexusOverrides")
    public void setNexusOverrides(List<NexusOverride__3> nexusOverrides) {
        this.nexusOverrides = nexusOverrides;
    }

    @JsonProperty("physicalLocations")
    public List<PhysicalLocation__3> getPhysicalLocations() {
        return physicalLocations;
    }

    @JsonProperty("physicalLocations")
    public void setPhysicalLocations(List<PhysicalLocation__3> physicalLocations) {
        this.physicalLocations = physicalLocations;
    }

    @JsonProperty("taxRegistrationNumber")
    public String getTaxRegistrationNumber() {
        return taxRegistrationNumber;
    }

    @JsonProperty("taxRegistrationNumber")
    public void setTaxRegistrationNumber(String taxRegistrationNumber) {
        this.taxRegistrationNumber = taxRegistrationNumber;
    }

    @JsonProperty("filingCurrency")
    public FilingCurrency__3 getFilingCurrency() {
        return filingCurrency;
    }

    @JsonProperty("filingCurrency")
    public void setFilingCurrency(FilingCurrency__3 filingCurrency) {
        this.filingCurrency = filingCurrency;
    }

    @JsonProperty("taxRegistrationType")
    public String getTaxRegistrationType() {
        return taxRegistrationType;
    }

    @JsonProperty("taxRegistrationType")
    public void setTaxRegistrationType(String taxRegistrationType) {
        this.taxRegistrationType = taxRegistrationType;
    }

}
