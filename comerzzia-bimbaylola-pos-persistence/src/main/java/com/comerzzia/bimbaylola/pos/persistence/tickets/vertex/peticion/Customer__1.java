
package com.comerzzia.bimbaylola.pos.persistence.tickets.vertex.peticion;

import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "administrativeDestination",
    "customerCode",
    "destination",
    "exemptionCertificate",
    "exemptionReasonCode",
    "isTaxExempt",
    "taxRegistrations"
})
@Generated("jsonschema2pojo")
public class Customer__1 {

    @JsonProperty("administrativeDestination")
    private AdministrativeDestination__1 administrativeDestination;
    @JsonProperty("customerCode")
    private CustomerCode__1 customerCode;
    @JsonProperty("destination")
    private Destination__1 destination;
    @JsonProperty("exemptionCertificate")
    private ExemptionCertificate__1 exemptionCertificate;
    @JsonProperty("exemptionReasonCode")
    private String exemptionReasonCode;
    @JsonProperty("isTaxExempt")
    private Boolean isTaxExempt;
    @JsonProperty("taxRegistrations")
    private List<TaxRegistration__1> taxRegistrations = null;

    @JsonProperty("administrativeDestination")
    public AdministrativeDestination__1 getAdministrativeDestination() {
        return administrativeDestination;
    }

    @JsonProperty("administrativeDestination")
    public void setAdministrativeDestination(AdministrativeDestination__1 administrativeDestination) {
        this.administrativeDestination = administrativeDestination;
    }

    @JsonProperty("customerCode")
    public CustomerCode__1 getCustomerCode() {
        return customerCode;
    }

    @JsonProperty("customerCode")
    public void setCustomerCode(CustomerCode__1 customerCode) {
        this.customerCode = customerCode;
    }

    @JsonProperty("destination")
    public Destination__1 getDestination() {
        return destination;
    }

    @JsonProperty("destination")
    public void setDestination(Destination__1 destination) {
        this.destination = destination;
    }

    @JsonProperty("exemptionCertificate")
    public ExemptionCertificate__1 getExemptionCertificate() {
        return exemptionCertificate;
    }

    @JsonProperty("exemptionCertificate")
    public void setExemptionCertificate(ExemptionCertificate__1 exemptionCertificate) {
        this.exemptionCertificate = exemptionCertificate;
    }

    @JsonProperty("exemptionReasonCode")
    public String getExemptionReasonCode() {
        return exemptionReasonCode;
    }

    @JsonProperty("exemptionReasonCode")
    public void setExemptionReasonCode(String exemptionReasonCode) {
        this.exemptionReasonCode = exemptionReasonCode;
    }

    @JsonProperty("isTaxExempt")
    public Boolean getIsTaxExempt() {
        return isTaxExempt;
    }

    @JsonProperty("isTaxExempt")
    public void setIsTaxExempt(Boolean isTaxExempt) {
        this.isTaxExempt = isTaxExempt;
    }

    @JsonProperty("taxRegistrations")
    public List<TaxRegistration__1> getTaxRegistrations() {
        return taxRegistrations;
    }

    @JsonProperty("taxRegistrations")
    public void setTaxRegistrations(List<TaxRegistration__1> taxRegistrations) {
        this.taxRegistrations = taxRegistrations;
    }

}
