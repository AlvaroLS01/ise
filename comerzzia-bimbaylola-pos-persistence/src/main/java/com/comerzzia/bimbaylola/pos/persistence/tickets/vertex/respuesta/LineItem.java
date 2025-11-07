
package com.comerzzia.bimbaylola.pos.persistence.tickets.vertex.respuesta;

import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "amountBilledToDate",
    "assistedParameters",
    "chainTransactionPhase",
    "commodityCode",
    "companyCodeCurrencyTaxAmount",
    "companyCodeCurrencyTaxableAmount",
    "costCenter",
    "countryOfOriginISOCode",
    "customer",
    "deliveryTerm",
    "departmentCode",
    "discount",
    "exemptOverrides",
    "exportProcedure",
    "extendedPrice",
    "fairMarketValue",
    "flexibleFields",
    "freight",
    "generalLedgerAccount",
    "impositionInclusions",
    "inputTotalTax",
    "intrastatCommodityCode",
    "isMulticomponent",
    "lineItemId",
    "lineItemNumber",
    "lineItems",
    "lineType",
    "locationCode",
    "materialCode",
    "materialOrigin",
    "modeOfTransport",
    "natureOfTransaction",
    "netMassKilograms",
    "nonTaxableOverrides",
    "product",
    "projectNumber",
    "quantity",
    "rateOverrides",
    "returnsFields",
    "seller",
    "simplificationCode",
    "situsOverride",
    "statisticalValue",
    "supplementaryUnit",
    "taxDate",
    "taxIncludedIndicator",
    "taxOverride",
    "taxes",
    "titleTransfer",
    "totalTax",
    "transactionType",
    "unitPrice",
    "usage",
    "usageClass",
    "vendorSKU",
    "volume",
    "weight"
})
@Generated("jsonschema2pojo")
public class LineItem {

    @JsonProperty("amountBilledToDate")
    private Double amountBilledToDate;
    @JsonProperty("assistedParameters")
    private List<Object> assistedParameters = null;
    @JsonProperty("chainTransactionPhase")
    private String chainTransactionPhase;
    @JsonProperty("commodityCode")
    private CommodityCode commodityCode;
    @JsonProperty("companyCodeCurrencyTaxAmount")
    private Double companyCodeCurrencyTaxAmount;
    @JsonProperty("companyCodeCurrencyTaxableAmount")
    private Double companyCodeCurrencyTaxableAmount;
    @JsonProperty("costCenter")
    private String costCenter;
    @JsonProperty("countryOfOriginISOCode")
    private String countryOfOriginISOCode;
    @JsonProperty("customer")
    private Customer__1 customer;
    @JsonProperty("deliveryTerm")
    private String deliveryTerm;
    @JsonProperty("departmentCode")
    private String departmentCode;
    @JsonProperty("discount")
    private Discount__1 discount;
    @JsonProperty("exemptOverrides")
    private List<Object> exemptOverrides = null;
    @JsonProperty("exportProcedure")
    private String exportProcedure;
    @JsonProperty("extendedPrice")
    private Double extendedPrice;
    @JsonProperty("fairMarketValue")
    private Double fairMarketValue;
    @JsonProperty("flexibleFields")
    private FlexibleFields flexibleFields;
    @JsonProperty("freight")
    private Double freight;
    @JsonProperty("generalLedgerAccount")
    private String generalLedgerAccount;
    @JsonProperty("impositionInclusions")
    private List<Object> impositionInclusions = null;
    @JsonProperty("inputTotalTax")
    private Double inputTotalTax;
    @JsonProperty("intrastatCommodityCode")
    private String intrastatCommodityCode;
    @JsonProperty("isMulticomponent")
    private Boolean isMulticomponent;
    @JsonProperty("lineItemId")
    private String lineItemId;
    @JsonProperty("lineItemNumber")
    private Double lineItemNumber;
    @JsonProperty("lineItems")
    private List<Object> lineItems = null;
    @JsonProperty("lineType")
    private LineType lineType;
    @JsonProperty("locationCode")
    private String locationCode;
    @JsonProperty("materialCode")
    private String materialCode;
    @JsonProperty("materialOrigin")
    private String materialOrigin;
    @JsonProperty("modeOfTransport")
    private Double modeOfTransport;
    @JsonProperty("natureOfTransaction")
    private Double natureOfTransaction;
    @JsonProperty("netMassKilograms")
    private Double netMassKilograms;
    @JsonProperty("nonTaxableOverrides")
    private List<Object> nonTaxableOverrides = null;
    @JsonProperty("product")
    private Product product;
    @JsonProperty("projectNumber")
    private String projectNumber;
    @JsonProperty("quantity")
    private Quantity quantity;
    @JsonProperty("rateOverrides")
    private List<Object> rateOverrides = null;
    @JsonProperty("returnsFields")
    private ReturnsFields returnsFields;
    @JsonProperty("seller")
    private Seller seller;
    @JsonProperty("simplificationCode")
    private String simplificationCode;
    @JsonProperty("situsOverride")
    private SitusOverride situsOverride;
    @JsonProperty("statisticalValue")
    private StatisticalValue statisticalValue;
    @JsonProperty("supplementaryUnit")
    private SupplementaryUnit supplementaryUnit;
    @JsonProperty("taxDate")
    private String taxDate;
    @JsonProperty("taxIncludedIndicator")
    private Boolean taxIncludedIndicator;
    @JsonProperty("taxOverride")
    private TaxOverride taxOverride;
    @JsonProperty("taxes")
    private List<Tax> taxes = null;
    @JsonProperty("titleTransfer")
    private String titleTransfer;
    @JsonProperty("totalTax")
    private Double totalTax;
    @JsonProperty("transactionType")
    private String transactionType;
    @JsonProperty("unitPrice")
    private Double unitPrice;
    @JsonProperty("usage")
    private String usage;
    @JsonProperty("usageClass")
    private String usageClass;
    @JsonProperty("vendorSKU")
    private String vendorSKU;
    @JsonProperty("volume")
    private Volume volume;
    @JsonProperty("weight")
    private Weight weight;

    @JsonProperty("amountBilledToDate")
    public Double getAmountBilledToDate() {
        return amountBilledToDate;
    }

    @JsonProperty("amountBilledToDate")
    public void setAmountBilledToDate(Double amountBilledToDate) {
        this.amountBilledToDate = amountBilledToDate;
    }

    @JsonProperty("assistedParameters")
    public List<Object> getAssistedParameters() {
        return assistedParameters;
    }

    @JsonProperty("assistedParameters")
    public void setAssistedParameters(List<Object> assistedParameters) {
        this.assistedParameters = assistedParameters;
    }

    @JsonProperty("chainTransactionPhase")
    public String getChainTransactionPhase() {
        return chainTransactionPhase;
    }

    @JsonProperty("chainTransactionPhase")
    public void setChainTransactionPhase(String chainTransactionPhase) {
        this.chainTransactionPhase = chainTransactionPhase;
    }

    @JsonProperty("commodityCode")
    public CommodityCode getCommodityCode() {
        return commodityCode;
    }

    @JsonProperty("commodityCode")
    public void setCommodityCode(CommodityCode commodityCode) {
        this.commodityCode = commodityCode;
    }

    @JsonProperty("companyCodeCurrencyTaxAmount")
    public Double getCompanyCodeCurrencyTaxAmount() {
        return companyCodeCurrencyTaxAmount;
    }

    @JsonProperty("companyCodeCurrencyTaxAmount")
    public void setCompanyCodeCurrencyTaxAmount(Double companyCodeCurrencyTaxAmount) {
        this.companyCodeCurrencyTaxAmount = companyCodeCurrencyTaxAmount;
    }

    @JsonProperty("companyCodeCurrencyTaxableAmount")
    public Double getCompanyCodeCurrencyTaxableAmount() {
        return companyCodeCurrencyTaxableAmount;
    }

    @JsonProperty("companyCodeCurrencyTaxableAmount")
    public void setCompanyCodeCurrencyTaxableAmount(Double companyCodeCurrencyTaxableAmount) {
        this.companyCodeCurrencyTaxableAmount = companyCodeCurrencyTaxableAmount;
    }

    @JsonProperty("costCenter")
    public String getCostCenter() {
        return costCenter;
    }

    @JsonProperty("costCenter")
    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    @JsonProperty("countryOfOriginISOCode")
    public String getCountryOfOriginISOCode() {
        return countryOfOriginISOCode;
    }

    @JsonProperty("countryOfOriginISOCode")
    public void setCountryOfOriginISOCode(String countryOfOriginISOCode) {
        this.countryOfOriginISOCode = countryOfOriginISOCode;
    }

    @JsonProperty("customer")
    public Customer__1 getCustomer() {
        return customer;
    }

    @JsonProperty("customer")
    public void setCustomer(Customer__1 customer) {
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

    @JsonProperty("departmentCode")
    public String getDepartmentCode() {
        return departmentCode;
    }

    @JsonProperty("departmentCode")
    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    @JsonProperty("discount")
    public Discount__1 getDiscount() {
        return discount;
    }

    @JsonProperty("discount")
    public void setDiscount(Discount__1 discount) {
        this.discount = discount;
    }

    @JsonProperty("exemptOverrides")
    public List<Object> getExemptOverrides() {
        return exemptOverrides;
    }

    @JsonProperty("exemptOverrides")
    public void setExemptOverrides(List<Object> exemptOverrides) {
        this.exemptOverrides = exemptOverrides;
    }

    @JsonProperty("exportProcedure")
    public String getExportProcedure() {
        return exportProcedure;
    }

    @JsonProperty("exportProcedure")
    public void setExportProcedure(String exportProcedure) {
        this.exportProcedure = exportProcedure;
    }

    @JsonProperty("extendedPrice")
    public Double getExtendedPrice() {
        return extendedPrice;
    }

    @JsonProperty("extendedPrice")
    public void setExtendedPrice(Double extendedPrice) {
        this.extendedPrice = extendedPrice;
    }

    @JsonProperty("fairMarketValue")
    public Double getFairMarketValue() {
        return fairMarketValue;
    }

    @JsonProperty("fairMarketValue")
    public void setFairMarketValue(Double fairMarketValue) {
        this.fairMarketValue = fairMarketValue;
    }

    @JsonProperty("flexibleFields")
    public FlexibleFields getFlexibleFields() {
        return flexibleFields;
    }

    @JsonProperty("flexibleFields")
    public void setFlexibleFields(FlexibleFields flexibleFields) {
        this.flexibleFields = flexibleFields;
    }

    @JsonProperty("freight")
    public Double getFreight() {
        return freight;
    }

    @JsonProperty("freight")
    public void setFreight(Double freight) {
        this.freight = freight;
    }

    @JsonProperty("generalLedgerAccount")
    public String getGeneralLedgerAccount() {
        return generalLedgerAccount;
    }

    @JsonProperty("generalLedgerAccount")
    public void setGeneralLedgerAccount(String generalLedgerAccount) {
        this.generalLedgerAccount = generalLedgerAccount;
    }

    @JsonProperty("impositionInclusions")
    public List<Object> getImpositionInclusions() {
        return impositionInclusions;
    }

    @JsonProperty("impositionInclusions")
    public void setImpositionInclusions(List<Object> impositionInclusions) {
        this.impositionInclusions = impositionInclusions;
    }

    @JsonProperty("inputTotalTax")
    public Double getInputTotalTax() {
        return inputTotalTax;
    }

    @JsonProperty("inputTotalTax")
    public void setInputTotalTax(Double inputTotalTax) {
        this.inputTotalTax = inputTotalTax;
    }

    @JsonProperty("intrastatCommodityCode")
    public String getIntrastatCommodityCode() {
        return intrastatCommodityCode;
    }

    @JsonProperty("intrastatCommodityCode")
    public void setIntrastatCommodityCode(String intrastatCommodityCode) {
        this.intrastatCommodityCode = intrastatCommodityCode;
    }

    @JsonProperty("isMulticomponent")
    public Boolean getIsMulticomponent() {
        return isMulticomponent;
    }

    @JsonProperty("isMulticomponent")
    public void setIsMulticomponent(Boolean isMulticomponent) {
        this.isMulticomponent = isMulticomponent;
    }

    @JsonProperty("lineItemId")
    public String getLineItemId() {
        return lineItemId;
    }

    @JsonProperty("lineItemId")
    public void setLineItemId(String lineItemId) {
        this.lineItemId = lineItemId;
    }

    @JsonProperty("lineItemNumber")
    public Double getLineItemNumber() {
        return lineItemNumber;
    }

    @JsonProperty("lineItemNumber")
    public void setLineItemNumber(Double lineItemNumber) {
        this.lineItemNumber = lineItemNumber;
    }

    @JsonProperty("lineItems")
    public List<Object> getLineItems() {
        return lineItems;
    }

    @JsonProperty("lineItems")
    public void setLineItems(List<Object> lineItems) {
        this.lineItems = lineItems;
    }

    @JsonProperty("lineType")
    public LineType getLineType() {
        return lineType;
    }

    @JsonProperty("lineType")
    public void setLineType(LineType lineType) {
        this.lineType = lineType;
    }

    @JsonProperty("locationCode")
    public String getLocationCode() {
        return locationCode;
    }

    @JsonProperty("locationCode")
    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    @JsonProperty("materialCode")
    public String getMaterialCode() {
        return materialCode;
    }

    @JsonProperty("materialCode")
    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    @JsonProperty("materialOrigin")
    public String getMaterialOrigin() {
        return materialOrigin;
    }

    @JsonProperty("materialOrigin")
    public void setMaterialOrigin(String materialOrigin) {
        this.materialOrigin = materialOrigin;
    }

    @JsonProperty("modeOfTransport")
    public Double getModeOfTransport() {
        return modeOfTransport;
    }

    @JsonProperty("modeOfTransport")
    public void setModeOfTransport(Double modeOfTransport) {
        this.modeOfTransport = modeOfTransport;
    }

    @JsonProperty("natureOfTransaction")
    public Double getNatureOfTransaction() {
        return natureOfTransaction;
    }

    @JsonProperty("natureOfTransaction")
    public void setNatureOfTransaction(Double natureOfTransaction) {
        this.natureOfTransaction = natureOfTransaction;
    }

    @JsonProperty("netMassKilograms")
    public Double getNetMassKilograms() {
        return netMassKilograms;
    }

    @JsonProperty("netMassKilograms")
    public void setNetMassKilograms(Double netMassKilograms) {
        this.netMassKilograms = netMassKilograms;
    }

    @JsonProperty("nonTaxableOverrides")
    public List<Object> getNonTaxableOverrides() {
        return nonTaxableOverrides;
    }

    @JsonProperty("nonTaxableOverrides")
    public void setNonTaxableOverrides(List<Object> nonTaxableOverrides) {
        this.nonTaxableOverrides = nonTaxableOverrides;
    }

    @JsonProperty("product")
    public Product getProduct() {
        return product;
    }

    @JsonProperty("product")
    public void setProduct(Product product) {
        this.product = product;
    }

    @JsonProperty("projectNumber")
    public String getProjectNumber() {
        return projectNumber;
    }

    @JsonProperty("projectNumber")
    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    @JsonProperty("quantity")
    public Quantity getQuantity() {
        return quantity;
    }

    @JsonProperty("quantity")
    public void setQuantity(Quantity quantity) {
        this.quantity = quantity;
    }

    @JsonProperty("rateOverrides")
    public List<Object> getRateOverrides() {
        return rateOverrides;
    }

    @JsonProperty("rateOverrides")
    public void setRateOverrides(List<Object> rateOverrides) {
        this.rateOverrides = rateOverrides;
    }

    @JsonProperty("returnsFields")
    public ReturnsFields getReturnsFields() {
        return returnsFields;
    }

    @JsonProperty("returnsFields")
    public void setReturnsFields(ReturnsFields returnsFields) {
        this.returnsFields = returnsFields;
    }

    @JsonProperty("seller")
    public Seller getSeller() {
        return seller;
    }

    @JsonProperty("seller")
    public void setSeller(Seller seller) {
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
    public SitusOverride getSitusOverride() {
        return situsOverride;
    }

    @JsonProperty("situsOverride")
    public void setSitusOverride(SitusOverride situsOverride) {
        this.situsOverride = situsOverride;
    }

    @JsonProperty("statisticalValue")
    public StatisticalValue getStatisticalValue() {
        return statisticalValue;
    }

    @JsonProperty("statisticalValue")
    public void setStatisticalValue(StatisticalValue statisticalValue) {
        this.statisticalValue = statisticalValue;
    }

    @JsonProperty("supplementaryUnit")
    public SupplementaryUnit getSupplementaryUnit() {
        return supplementaryUnit;
    }

    @JsonProperty("supplementaryUnit")
    public void setSupplementaryUnit(SupplementaryUnit supplementaryUnit) {
        this.supplementaryUnit = supplementaryUnit;
    }

    @JsonProperty("taxDate")
    public String getTaxDate() {
        return taxDate;
    }

    @JsonProperty("taxDate")
    public void setTaxDate(String taxDate) {
        this.taxDate = taxDate;
    }

    @JsonProperty("taxIncludedIndicator")
    public Boolean getTaxIncludedIndicator() {
        return taxIncludedIndicator;
    }

    @JsonProperty("taxIncludedIndicator")
    public void setTaxIncludedIndicator(Boolean taxIncludedIndicator) {
        this.taxIncludedIndicator = taxIncludedIndicator;
    }

    @JsonProperty("taxOverride")
    public TaxOverride getTaxOverride() {
        return taxOverride;
    }

    @JsonProperty("taxOverride")
    public void setTaxOverride(TaxOverride taxOverride) {
        this.taxOverride = taxOverride;
    }

    @JsonProperty("taxes")
    public List<Tax> getTaxes() {
        return taxes;
    }

    @JsonProperty("taxes")
    public void setTaxes(List<Tax> taxes) {
        this.taxes = taxes;
    }

    @JsonProperty("titleTransfer")
    public String getTitleTransfer() {
        return titleTransfer;
    }

    @JsonProperty("titleTransfer")
    public void setTitleTransfer(String titleTransfer) {
        this.titleTransfer = titleTransfer;
    }

    @JsonProperty("totalTax")
    public Double getTotalTax() {
        return totalTax;
    }

    @JsonProperty("totalTax")
    public void setTotalTax(Double totalTax) {
        this.totalTax = totalTax;
    }

    @JsonProperty("transactionType")
    public String getTransactionType() {
        return transactionType;
    }

    @JsonProperty("transactionType")
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @JsonProperty("unitPrice")
    public Double getUnitPrice() {
        return unitPrice;
    }

    @JsonProperty("unitPrice")
    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @JsonProperty("usage")
    public String getUsage() {
        return usage;
    }

    @JsonProperty("usage")
    public void setUsage(String usage) {
        this.usage = usage;
    }

    @JsonProperty("usageClass")
    public String getUsageClass() {
        return usageClass;
    }

    @JsonProperty("usageClass")
    public void setUsageClass(String usageClass) {
        this.usageClass = usageClass;
    }

    @JsonProperty("vendorSKU")
    public String getVendorSKU() {
        return vendorSKU;
    }

    @JsonProperty("vendorSKU")
    public void setVendorSKU(String vendorSKU) {
        this.vendorSKU = vendorSKU;
    }

    @JsonProperty("volume")
    public Volume getVolume() {
        return volume;
    }

    @JsonProperty("volume")
    public void setVolume(Volume volume) {
        this.volume = volume;
    }

    @JsonProperty("weight")
    public Weight getWeight() {
        return weight;
    }

    @JsonProperty("weight")
    public void setWeight(Weight weight) {
        this.weight = weight;
    }

}
