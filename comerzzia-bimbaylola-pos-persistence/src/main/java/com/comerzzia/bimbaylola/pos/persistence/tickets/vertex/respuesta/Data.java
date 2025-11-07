
package com.comerzzia.bimbaylola.pos.persistence.tickets.vertex.respuesta;

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
    "roundAtLineLevel",
    "saleMessageType",
    "seller",
    "simplificationCode",
    "situsOverride",
    "subTotal",
    "taxOverride",
    "taxPointDate",
    "total",
    "totalTax",
    "transactionId",
    "transactionType"
})
@Generated("jsonschema2pojo")
public class Data {

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
    private List<Object> currencyConversionFactors = null;
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
    private List<Object> exemptOverrides = null;
    @JsonProperty("impositionInclusions")
    private List<Object> impositionInclusions = null;
    @JsonProperty("isTaxOnlyAdjustmentIndicator")
    private Boolean isTaxOnlyAdjustmentIndicator;
    @JsonProperty("lineItems")
    private List<LineItem> lineItems = null;
    @JsonProperty("locationCode")
    private String locationCode;
    @JsonProperty("nonTaxableOverrides")
    private List<Object> nonTaxableOverrides = null;
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
    private Double proratePercentage;
    @JsonProperty("rateOverrides")
    private List<Object> rateOverrides = null;
    @JsonProperty("returnAssistedParametersIndicator")
    private Boolean returnAssistedParametersIndicator;
    @JsonProperty("returnGeneratedLineItemsIndicator")
    private Boolean returnGeneratedLineItemsIndicator;
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
    @JsonProperty("subTotal")
    private Double subTotal;
    @JsonProperty("taxOverride")
    private TaxOverride__1 taxOverride;
    @JsonProperty("taxPointDate")
    private String taxPointDate;
    @JsonProperty("total")
    private Double total;
    @JsonProperty("totalTax")
    private Double totalTax;
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
    public List<Object> getCurrencyConversionFactors() {
        return currencyConversionFactors;
    }

    @JsonProperty("currencyConversionFactors")
    public void setCurrencyConversionFactors(List<Object> currencyConversionFactors) {
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
    public List<Object> getExemptOverrides() {
        return exemptOverrides;
    }

    @JsonProperty("exemptOverrides")
    public void setExemptOverrides(List<Object> exemptOverrides) {
        this.exemptOverrides = exemptOverrides;
    }

    @JsonProperty("impositionInclusions")
    public List<Object> getImpositionInclusions() {
        return impositionInclusions;
    }

    @JsonProperty("impositionInclusions")
    public void setImpositionInclusions(List<Object> impositionInclusions) {
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
    public List<Object> getNonTaxableOverrides() {
        return nonTaxableOverrides;
    }

    @JsonProperty("nonTaxableOverrides")
    public void setNonTaxableOverrides(List<Object> nonTaxableOverrides) {
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
    public Double getProratePercentage() {
        return proratePercentage;
    }

    @JsonProperty("proratePercentage")
    public void setProratePercentage(Double proratePercentage) {
        this.proratePercentage = proratePercentage;
    }

    @JsonProperty("rateOverrides")
    public List<Object> getRateOverrides() {
        return rateOverrides;
    }

    @JsonProperty("rateOverrides")
    public void setRateOverrides(List<Object> rateOverrides) {
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

    @JsonProperty("subTotal")
    public Double getSubTotal() {
        return subTotal;
    }

    @JsonProperty("subTotal")
    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
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

    @JsonProperty("total")
    public Double getTotal() {
        return total;
    }

    @JsonProperty("total")
    public void setTotal(Double total) {
        this.total = total;
    }

    @JsonProperty("totalTax")
    public Double getTotalTax() {
        return totalTax;
    }

    @JsonProperty("totalTax")
    public void setTotalTax(Double totalTax) {
        this.totalTax = totalTax;
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
