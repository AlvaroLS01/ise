
package com.comerzzia.bimbaylola.pos.persistence.tickets.vertex.peticion;

import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "administrativeOrigin",
    "company",
    "department",
    "dispatcher",
    "division",
    "nexusIndicator",
    "nexusReasonCode",
    "physicalOrigin",
    "taxRegistrations",
    "utilityProvider"
})
@Generated("jsonschema2pojo")
public class Seller__1 {

    @JsonProperty("administrativeOrigin")
    private AdministrativeOrigin__1 administrativeOrigin;
    @JsonProperty("company")
    private String company;
    @JsonProperty("department")
    private String department;
    @JsonProperty("dispatcher")
    private Dispatcher__1 dispatcher;
    @JsonProperty("division")
    private String division;
    @JsonProperty("nexusIndicator")
    private Boolean nexusIndicator;
    @JsonProperty("nexusReasonCode")
    private String nexusReasonCode;
    @JsonProperty("physicalOrigin")
    private PhysicalOrigin__1 physicalOrigin;
    @JsonProperty("taxRegistrations")
    private List<TaxRegistration__5> taxRegistrations = null;
    @JsonProperty("utilityProvider")
    private String utilityProvider;

    @JsonProperty("administrativeOrigin")
    public AdministrativeOrigin__1 getAdministrativeOrigin() {
        return administrativeOrigin;
    }

    @JsonProperty("administrativeOrigin")
    public void setAdministrativeOrigin(AdministrativeOrigin__1 administrativeOrigin) {
        this.administrativeOrigin = administrativeOrigin;
    }

    @JsonProperty("company")
    public String getCompany() {
        return company;
    }

    @JsonProperty("company")
    public void setCompany(String company) {
        this.company = company;
    }

    @JsonProperty("department")
    public String getDepartment() {
        return department;
    }

    @JsonProperty("department")
    public void setDepartment(String department) {
        this.department = department;
    }

    @JsonProperty("dispatcher")
    public Dispatcher__1 getDispatcher() {
        return dispatcher;
    }

    @JsonProperty("dispatcher")
    public void setDispatcher(Dispatcher__1 dispatcher) {
        this.dispatcher = dispatcher;
    }

    @JsonProperty("division")
    public String getDivision() {
        return division;
    }

    @JsonProperty("division")
    public void setDivision(String division) {
        this.division = division;
    }

    @JsonProperty("nexusIndicator")
    public Boolean getNexusIndicator() {
        return nexusIndicator;
    }

    @JsonProperty("nexusIndicator")
    public void setNexusIndicator(Boolean nexusIndicator) {
        this.nexusIndicator = nexusIndicator;
    }

    @JsonProperty("nexusReasonCode")
    public String getNexusReasonCode() {
        return nexusReasonCode;
    }

    @JsonProperty("nexusReasonCode")
    public void setNexusReasonCode(String nexusReasonCode) {
        this.nexusReasonCode = nexusReasonCode;
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
    public List<TaxRegistration__5> getTaxRegistrations() {
        return taxRegistrations;
    }

    @JsonProperty("taxRegistrations")
    public void setTaxRegistrations(List<TaxRegistration__5> taxRegistrations) {
        this.taxRegistrations = taxRegistrations;
    }

    @JsonProperty("utilityProvider")
    public String getUtilityProvider() {
        return utilityProvider;
    }

    @JsonProperty("utilityProvider")
    public void setUtilityProvider(String utilityProvider) {
        this.utilityProvider = utilityProvider;
    }

}
