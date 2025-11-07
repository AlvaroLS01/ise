
package com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.vertex;

import java.math.BigDecimal;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "accumulationCustomerNumber",
    "accumulationDocumentNumber",
    "billingType",
    "companyCodeCurrency",
    "currency",
    "currencyConversionFactors",
    "customer",
    "deliveryTerm",
    "discount",
    "documentDate",
    "documentNumber",
    "documentSequenceId",
    "documentType",
    "exemptOverrides",
    "impositionInclusions",
    "isTaxOnlyAdjustmentIndicator",
    "lineItems",
    "locationCode",
    "nonTaxableOverrides",
    "orderType",
    "originalCurrency",
    "paymentDate",
    "postToJournal",
    "postingDate",
    "proratePercentage",
    "rateOverrides",
    "returnAssistedParametersIndicator",
    "returnGeneratedLineItemsIndicator",
    "returnTimeElapsedDetailsIndicator",
    "roundAtLineLevel",
    "saleMessageType",
    "seller",
    "simplificationCode",
    "situsOverride",
    "taxOverride",
    "taxPointDate",
    "transactionId",
    "transactionType"
})
@Generated("jsonschema2pojo")
public class EnviarPeticionImpuesto {

    @JsonProperty("accumulationCustomerNumber")
    private String accumulationCustomerNumber;
    @JsonProperty("accumulationDocumentNumber")
    private String accumulationDocumentNumber;
    @JsonProperty("billingType")
    private String billingType;
    @JsonProperty("companyCodeCurrency")
    private CompanyCodeCurrency companyCodeCurrency;
    @JsonProperty("currency")
    private Currency currency;
    @JsonProperty("currencyConversionFactors")
    private List<CurrencyConversionFactor> currencyConversionFactors = null;
    @JsonProperty("customer")
    private Customer customer;
    @JsonProperty("deliveryTerm")
    private String deliveryTerm;
    @JsonProperty("discount")
    private Discount discount;
    @JsonProperty("documentDate")
    private String documentDate;
    @JsonProperty("documentNumber")
    private String documentNumber;
    @JsonProperty("documentSequenceId")
    private String documentSequenceId;
    @JsonProperty("documentType")
    private String documentType;
    @JsonProperty("exemptOverrides")
    private List<ExemptOverride> exemptOverrides = null;
    @JsonProperty("impositionInclusions")
    private List<ImpositionInclusion> impositionInclusions = null;
    @JsonProperty("isTaxOnlyAdjustmentIndicator")
    private Boolean isTaxOnlyAdjustmentIndicator;
    @JsonProperty("lineItems")
    private List<LineItem> lineItems = null;
    @JsonProperty("locationCode")
    private String locationCode;
    @JsonProperty("nonTaxableOverrides")
    private List<NonTaxableOverride__1> nonTaxableOverrides = null;
    @JsonProperty("orderType")
    private String orderType;
    @JsonProperty("originalCurrency")
    private OriginalCurrency originalCurrency;
    @JsonProperty("paymentDate")
    private String paymentDate;
    @JsonProperty("postToJournal")
    private Boolean postToJournal;
    @JsonProperty("postingDate")
    private String postingDate;
    @JsonProperty("proratePercentage")
    private BigDecimal proratePercentage;
    @JsonProperty("rateOverrides")
    private List<RateOverride__1> rateOverrides = null;
    @JsonProperty("returnAssistedParametersIndicator")
    private Boolean returnAssistedParametersIndicator;
    @JsonProperty("returnGeneratedLineItemsIndicator")
    private Boolean returnGeneratedLineItemsIndicator;
    @JsonProperty("returnTimeElapsedDetailsIndicator")
    private Boolean returnTimeElapsedDetailsIndicator;
    @JsonProperty("roundAtLineLevel")
    private Boolean roundAtLineLevel;
    @JsonProperty("saleMessageType")
    private String saleMessageType;
    @JsonProperty("seller")
    private Seller__1 seller;
    @JsonProperty("simplificationCode")
    private String simplificationCode;
    @JsonProperty("situsOverride")
    private SitusOverride__1 situsOverride;
    @JsonProperty("taxOverride")
    private TaxOverride__1 taxOverride;
    @JsonProperty("taxPointDate")
    private String taxPointDate;
    @JsonProperty("transactionId")
    private String transactionId;
    @JsonProperty("transactionType")
    private String transactionType;

    @JsonProperty("accumulationCustomerNumber")
    public String getAccumulationCustomerNumber() {
        return accumulationCustomerNumber;
    }

    @JsonProperty("accumulationCustomerNumber")
    public void setAccumulationCustomerNumber(String accumulationCustomerNumber) {
        this.accumulationCustomerNumber = accumulationCustomerNumber;
    }

    @JsonProperty("accumulationDocumentNumber")
    public String getAccumulationDocumentNumber() {
        return accumulationDocumentNumber;
    }

    @JsonProperty("accumulationDocumentNumber")
    public void setAccumulationDocumentNumber(String accumulationDocumentNumber) {
        this.accumulationDocumentNumber = accumulationDocumentNumber;
    }

    @JsonProperty("billingType")
    public String getBillingType() {
        return billingType;
    }

    @JsonProperty("billingType")
    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }

    @JsonProperty("companyCodeCurrency")
    public CompanyCodeCurrency getCompanyCodeCurrency() {
        return companyCodeCurrency;
    }

    @JsonProperty("companyCodeCurrency")
    public void setCompanyCodeCurrency(CompanyCodeCurrency companyCodeCurrency) {
        this.companyCodeCurrency = companyCodeCurrency;
    }

    @JsonProperty("currency")
    public Currency getCurrency() {
        return currency;
    }

    @JsonProperty("currency")
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @JsonProperty("currencyConversionFactors")
    public List<CurrencyConversionFactor> getCurrencyConversionFactors() {
        return currencyConversionFactors;
    }

    @JsonProperty("currencyConversionFactors")
    public void setCurrencyConversionFactors(List<CurrencyConversionFactor> currencyConversionFactors) {
        this.currencyConversionFactors = currencyConversionFactors;
    }

    @JsonProperty("customer")
    public Customer getCustomer() {
        return customer;
    }

    @JsonProperty("customer")
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @JsonProperty("deliveryTerm")
    public String getDeliveryTerm() {
        return deliveryTerm;
    }

    @JsonProperty("deliveryTerm")
    public void setDeliveryTerm(String deliveryTerm) {
        this.deliveryTerm = deliveryTerm;
    }

    @JsonProperty("discount")
    public Discount getDiscount() {
        return discount;
    }

    @JsonProperty("discount")
    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    @JsonProperty("documentDate")
    public String getDocumentDate() {
        return documentDate;
    }

    @JsonProperty("documentDate")
    public void setDocumentDate(String documentDate) {
        this.documentDate = documentDate;
    }

    @JsonProperty("documentNumber")
    public String getDocumentNumber() {
        return documentNumber;
    }

    @JsonProperty("documentNumber")
    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    @JsonProperty("documentSequenceId")
    public String getDocumentSequenceId() {
        return documentSequenceId;
    }

    @JsonProperty("documentSequenceId")
    public void setDocumentSequenceId(String documentSequenceId) {
        this.documentSequenceId = documentSequenceId;
    }

    @JsonProperty("documentType")
    public String getDocumentType() {
        return documentType;
    }

    @JsonProperty("documentType")
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    @JsonProperty("exemptOverrides")
    public List<ExemptOverride> getExemptOverrides() {
        return exemptOverrides;
    }

    @JsonProperty("exemptOverrides")
    public void setExemptOverrides(List<ExemptOverride> exemptOverrides) {
        this.exemptOverrides = exemptOverrides;
    }

    @JsonProperty("impositionInclusions")
    public List<ImpositionInclusion> getImpositionInclusions() {
        return impositionInclusions;
    }

    @JsonProperty("impositionInclusions")
    public void setImpositionInclusions(List<ImpositionInclusion> impositionInclusions) {
        this.impositionInclusions = impositionInclusions;
    }

    @JsonProperty("isTaxOnlyAdjustmentIndicator")
    public Boolean getIsTaxOnlyAdjustmentIndicator() {
        return isTaxOnlyAdjustmentIndicator;
    }

    @JsonProperty("isTaxOnlyAdjustmentIndicator")
    public void setIsTaxOnlyAdjustmentIndicator(Boolean isTaxOnlyAdjustmentIndicator) {
        this.isTaxOnlyAdjustmentIndicator = isTaxOnlyAdjustmentIndicator;
    }

    @JsonProperty("lineItems")
    public List<LineItem> getLineItems() {
        return lineItems;
    }

    @JsonProperty("lineItems")
    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    @JsonProperty("locationCode")
    public String getLocationCode() {
        return locationCode;
    }

    @JsonProperty("locationCode")
    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    @JsonProperty("nonTaxableOverrides")
    public List<NonTaxableOverride__1> getNonTaxableOverrides() {
        return nonTaxableOverrides;
    }

    @JsonProperty("nonTaxableOverrides")
    public void setNonTaxableOverrides(List<NonTaxableOverride__1> nonTaxableOverrides) {
        this.nonTaxableOverrides = nonTaxableOverrides;
    }

    @JsonProperty("orderType")
    public String getOrderType() {
        return orderType;
    }

    @JsonProperty("orderType")
    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    @JsonProperty("originalCurrency")
    public OriginalCurrency getOriginalCurrency() {
        return originalCurrency;
    }

    @JsonProperty("originalCurrency")
    public void setOriginalCurrency(OriginalCurrency originalCurrency) {
        this.originalCurrency = originalCurrency;
    }

    @JsonProperty("paymentDate")
    public String getPaymentDate() {
        return paymentDate;
    }

    @JsonProperty("paymentDate")
    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    @JsonProperty("postToJournal")
    public Boolean getPostToJournal() {
        return postToJournal;
    }

    @JsonProperty("postToJournal")
    public void setPostToJournal(Boolean postToJournal) {
        this.postToJournal = postToJournal;
    }

    @JsonProperty("postingDate")
    public String getPostingDate() {
        return postingDate;
    }

    @JsonProperty("postingDate")
    public void setPostingDate(String postingDate) {
        this.postingDate = postingDate;
    }

    @JsonProperty("proratePercentage")
    public BigDecimal getProratePercentage() {
        return proratePercentage;
    }

    @JsonProperty("proratePercentage")
    public void setProratePercentage(BigDecimal proratePercentage) {
        this.proratePercentage = proratePercentage;
    }

    @JsonProperty("rateOverrides")
    public List<RateOverride__1> getRateOverrides() {
        return rateOverrides;
    }

    @JsonProperty("rateOverrides")
    public void setRateOverrides(List<RateOverride__1> rateOverrides) {
        this.rateOverrides = rateOverrides;
    }

    @JsonProperty("returnAssistedParametersIndicator")
    public Boolean getReturnAssistedParametersIndicator() {
        return returnAssistedParametersIndicator;
    }

    @JsonProperty("returnAssistedParametersIndicator")
    public void setReturnAssistedParametersIndicator(Boolean returnAssistedParametersIndicator) {
        this.returnAssistedParametersIndicator = returnAssistedParametersIndicator;
    }

    @JsonProperty("returnGeneratedLineItemsIndicator")
    public Boolean getReturnGeneratedLineItemsIndicator() {
        return returnGeneratedLineItemsIndicator;
    }

    @JsonProperty("returnGeneratedLineItemsIndicator")
    public void setReturnGeneratedLineItemsIndicator(Boolean returnGeneratedLineItemsIndicator) {
        this.returnGeneratedLineItemsIndicator = returnGeneratedLineItemsIndicator;
    }

    @JsonProperty("returnTimeElapsedDetailsIndicator")
    public Boolean getReturnTimeElapsedDetailsIndicator() {
        return returnTimeElapsedDetailsIndicator;
    }

    @JsonProperty("returnTimeElapsedDetailsIndicator")
    public void setReturnTimeElapsedDetailsIndicator(Boolean returnTimeElapsedDetailsIndicator) {
        this.returnTimeElapsedDetailsIndicator = returnTimeElapsedDetailsIndicator;
    }

    @JsonProperty("roundAtLineLevel")
    public Boolean getRoundAtLineLevel() {
        return roundAtLineLevel;
    }

    @JsonProperty("roundAtLineLevel")
    public void setRoundAtLineLevel(Boolean roundAtLineLevel) {
        this.roundAtLineLevel = roundAtLineLevel;
    }

    @JsonProperty("saleMessageType")
    public String getSaleMessageType() {
        return saleMessageType;
    }

    @JsonProperty("saleMessageType")
    public void setSaleMessageType(String saleMessageType) {
        this.saleMessageType = saleMessageType;
    }

    @JsonProperty("seller")
    public Seller__1 getSeller() {
        return seller;
    }

    @JsonProperty("seller")
    public void setSeller(Seller__1 seller) {
        this.seller = seller;
    }

    @JsonProperty("simplificationCode")
    public String getSimplificationCode() {
        return simplificationCode;
    }

    @JsonProperty("simplificationCode")
    public void setSimplificationCode(String simplificationCode) {
        this.simplificationCode = simplificationCode;
    }

    @JsonProperty("situsOverride")
    public SitusOverride__1 getSitusOverride() {
        return situsOverride;
    }

    @JsonProperty("situsOverride")
    public void setSitusOverride(SitusOverride__1 situsOverride) {
        this.situsOverride = situsOverride;
    }

    @JsonProperty("taxOverride")
    public TaxOverride__1 getTaxOverride() {
        return taxOverride;
    }

    @JsonProperty("taxOverride")
    public void setTaxOverride(TaxOverride__1 taxOverride) {
        this.taxOverride = taxOverride;
    }

    @JsonProperty("taxPointDate")
    public String getTaxPointDate() {
        return taxPointDate;
    }

    @JsonProperty("taxPointDate")
    public void setTaxPointDate(String taxPointDate) {
        this.taxPointDate = taxPointDate;
    }

    @JsonProperty("transactionId")
    public String getTransactionId() {
        return transactionId;
    }

    @JsonProperty("transactionId")
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @JsonProperty("transactionType")
    public String getTransactionType() {
        return transactionType;
    }

    @JsonProperty("transactionType")
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

}
