
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
public class Customer {

    @JsonProperty("administrativeDestination")
    private AdministrativeDestination administrativeDestination;
    @JsonProperty("customerCode")
    private CustomerCode customerCode;
    @JsonProperty("destination")
    private Destination destination;
    @JsonProperty("exemptionCertificate")
    private ExemptionCertificate exemptionCertificate;
    @JsonProperty("exemptionReasonCode")
    private String exemptionReasonCode;
    @JsonProperty("isTaxExempt")
    private Boolean isTaxExempt;
    @JsonProperty("taxRegistrations")
    private List<TaxRegistration> taxRegistrations = null;

    @JsonProperty("administrativeDestination")
    public AdministrativeDestination getAdministrativeDestination() {
        return administrativeDestination;
    }

    @JsonProperty("administrativeDestination")
    public void setAdministrativeDestination(AdministrativeDestination administrativeDestination) {
        this.administrativeDestination = administrativeDestination;
    }

    @JsonProperty("customerCode")
    public CustomerCode getCustomerCode() {
        return customerCode;
    }

    @JsonProperty("customerCode")
    public void setCustomerCode(CustomerCode customerCode) {
        this.customerCode = customerCode;
    }

    @JsonProperty("destination")
    public Destination getDestination() {
        return destination;
    }

    @JsonProperty("destination")
    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    @JsonProperty("exemptionCertificate")
    public ExemptionCertificate getExemptionCertificate() {
        return exemptionCertificate;
    }

    @JsonProperty("exemptionCertificate")
    public void setExemptionCertificate(ExemptionCertificate exemptionCertificate) {
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
    public List<TaxRegistration> getTaxRegistrations() {
        return taxRegistrations;
    }

    @JsonProperty("taxRegistrations")
    public void setTaxRegistrations(List<TaxRegistration> taxRegistrations) {
        this.taxRegistrations = taxRegistrations;
    }

}
