
package com.comerzzia.bimbaylola.pos.persistence.tickets.vertex.respuesta;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "calculatedTax",
    "calculationRuleId",
    "effectiveRate",
    "exempt",
    "imposition",
    "impositionType",
    "inclusionRuleId",
    "isService",
    "jurisdiction",
    "maxTaxIndicator",
    "nominalRate",
    "nonTaxable",
    "notRegisteredIndicator",
    "situs",
    "taxCollectedFromParty",
    "taxResult",
    "taxStructure",
    "taxType",
    "taxable"
})
@Generated("jsonschema2pojo")
public class Tax {

    @JsonProperty("calculatedTax")
    private Double calculatedTax;
    @JsonProperty("calculationRuleId")
    private CalculationRuleId calculationRuleId;
    @JsonProperty("effectiveRate")
    private Double effectiveRate;
    @JsonProperty("exempt")
    private Double exempt;
    @JsonProperty("imposition")
    private Imposition imposition;
    @JsonProperty("impositionType")
    private ImpositionType impositionType;
    @JsonProperty("inclusionRuleId")
    private InclusionRuleId inclusionRuleId;
    @JsonProperty("isService")
    private Boolean isService;
    @JsonProperty("jurisdiction")
    private Jurisdiction jurisdiction;
    @JsonProperty("maxTaxIndicator")
    private Boolean maxTaxIndicator;
    @JsonProperty("nominalRate")
    private Double nominalRate;
    @JsonProperty("nonTaxable")
    private Double nonTaxable;
    @JsonProperty("notRegisteredIndicator")
    private Boolean notRegisteredIndicator;
    @JsonProperty("situs")
    private String situs;
    @JsonProperty("taxCollectedFromParty")
    private String taxCollectedFromParty;
    @JsonProperty("taxResult")
    private String taxResult;
    @JsonProperty("taxStructure")
    private String taxStructure;
    @JsonProperty("taxType")
    private String taxType;
    @JsonProperty("taxable")
    private Double taxable;

    @JsonProperty("calculatedTax")
    public Double getCalculatedTax() {
        return calculatedTax;
    }

    @JsonProperty("calculatedTax")
    public void setCalculatedTax(Double calculatedTax) {
        this.calculatedTax = calculatedTax;
    }

    @JsonProperty("calculationRuleId")
    public CalculationRuleId getCalculationRuleId() {
        return calculationRuleId;
    }

    @JsonProperty("calculationRuleId")
    public void setCalculationRuleId(CalculationRuleId calculationRuleId) {
        this.calculationRuleId = calculationRuleId;
    }

    @JsonProperty("effectiveRate")
    public Double getEffectiveRate() {
        return effectiveRate;
    }

    @JsonProperty("effectiveRate")
    public void setEffectiveRate(Double effectiveRate) {
        this.effectiveRate = effectiveRate;
    }

    @JsonProperty("exempt")
    public Double getExempt() {
        return exempt;
    }

    @JsonProperty("exempt")
    public void setExempt(Double exempt) {
        this.exempt = exempt;
    }

    @JsonProperty("imposition")
    public Imposition getImposition() {
        return imposition;
    }

    @JsonProperty("imposition")
    public void setImposition(Imposition imposition) {
        this.imposition = imposition;
    }

    @JsonProperty("impositionType")
    public ImpositionType getImpositionType() {
        return impositionType;
    }

    @JsonProperty("impositionType")
    public void setImpositionType(ImpositionType impositionType) {
        this.impositionType = impositionType;
    }

    @JsonProperty("inclusionRuleId")
    public InclusionRuleId getInclusionRuleId() {
        return inclusionRuleId;
    }

    @JsonProperty("inclusionRuleId")
    public void setInclusionRuleId(InclusionRuleId inclusionRuleId) {
        this.inclusionRuleId = inclusionRuleId;
    }

    @JsonProperty("isService")
    public Boolean getIsService() {
        return isService;
    }

    @JsonProperty("isService")
    public void setIsService(Boolean isService) {
        this.isService = isService;
    }

    @JsonProperty("jurisdiction")
    public Jurisdiction getJurisdiction() {
        return jurisdiction;
    }

    @JsonProperty("jurisdiction")
    public void setJurisdiction(Jurisdiction jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    @JsonProperty("maxTaxIndicator")
    public Boolean getMaxTaxIndicator() {
        return maxTaxIndicator;
    }

    @JsonProperty("maxTaxIndicator")
    public void setMaxTaxIndicator(Boolean maxTaxIndicator) {
        this.maxTaxIndicator = maxTaxIndicator;
    }

    @JsonProperty("nominalRate")
    public Double getNominalRate() {
        return nominalRate;
    }

    @JsonProperty("nominalRate")
    public void setNominalRate(Double nominalRate) {
        this.nominalRate = nominalRate;
    }

    @JsonProperty("nonTaxable")
    public Double getNonTaxable() {
        return nonTaxable;
    }

    @JsonProperty("nonTaxable")
    public void setNonTaxable(Double nonTaxable) {
        this.nonTaxable = nonTaxable;
    }

    @JsonProperty("notRegisteredIndicator")
    public Boolean getNotRegisteredIndicator() {
        return notRegisteredIndicator;
    }

    @JsonProperty("notRegisteredIndicator")
    public void setNotRegisteredIndicator(Boolean notRegisteredIndicator) {
        this.notRegisteredIndicator = notRegisteredIndicator;
    }

    @JsonProperty("situs")
    public String getSitus() {
        return situs;
    }

    @JsonProperty("situs")
    public void setSitus(String situs) {
        this.situs = situs;
    }

    @JsonProperty("taxCollectedFromParty")
    public String getTaxCollectedFromParty() {
        return taxCollectedFromParty;
    }

    @JsonProperty("taxCollectedFromParty")
    public void setTaxCollectedFromParty(String taxCollectedFromParty) {
        this.taxCollectedFromParty = taxCollectedFromParty;
    }

    @JsonProperty("taxResult")
    public String getTaxResult() {
        return taxResult;
    }

    @JsonProperty("taxResult")
    public void setTaxResult(String taxResult) {
        this.taxResult = taxResult;
    }

    @JsonProperty("taxStructure")
    public String getTaxStructure() {
        return taxStructure;
    }

    @JsonProperty("taxStructure")
    public void setTaxStructure(String taxStructure) {
        this.taxStructure = taxStructure;
    }

    @JsonProperty("taxType")
    public String getTaxType() {
        return taxType;
    }

    @JsonProperty("taxType")
    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    @JsonProperty("taxable")
    public Double getTaxable() {
        return taxable;
    }

    @JsonProperty("taxable")
    public void setTaxable(Double taxable) {
        this.taxable = taxable;
    }

}
