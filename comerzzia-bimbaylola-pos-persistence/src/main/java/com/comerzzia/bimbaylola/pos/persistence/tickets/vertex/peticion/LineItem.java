
package com.comerzzia.bimbaylola.pos.persistence.tickets.vertex.peticion;

import java.math.BigDecimal;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "amountBilledToDate",
    "chainTransactionPhase",
    "commodityCode",
    "companyCodeCurrencyTaxAmount",
    "companyCodeCurrencyTaxableAmount",
    "cost",
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
    "landedCost",
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
    "previousTaxPaid",
    "taxDate",
    "taxIncludedIndicator",
    "taxOverride",
    "titleTransfer",
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
    private BigDecimal amountBilledToDate;
    @JsonProperty("chainTransactionPhase")
    private String chainTransactionPhase;
    @JsonProperty("commodityCode")
    private CommodityCode commodityCode;
    @JsonProperty("companyCodeCurrencyTaxAmount")
    private BigDecimal companyCodeCurrencyTaxAmount;
    @JsonProperty("companyCodeCurrencyTaxableAmount")
    private BigDecimal companyCodeCurrencyTaxableAmount;
    @JsonProperty("cost")
    private BigDecimal cost;
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
    private List<ExemptOverride__1> exemptOverrides = null;
    @JsonProperty("exportProcedure")
    private String exportProcedure;
    @JsonProperty("extendedPrice")
    private BigDecimal extendedPrice;
    @JsonProperty("fairMarketValue")
    private BigDecimal fairMarketValue;
    @JsonProperty("flexibleFields")
    private FlexibleFields flexibleFields;
    @JsonProperty("freight")
    private BigDecimal freight;
    @JsonProperty("generalLedgerAccount")
    private String generalLedgerAccount;
    @JsonProperty("impositionInclusions")
    private List<ImpositionInclusion__1> impositionInclusions = null;
    @JsonProperty("inputTotalTax")
    private BigDecimal inputTotalTax;
    @JsonProperty("intrastatCommodityCode")
    private String intrastatCommodityCode;
    @JsonProperty("isMulticomponent")
    private Boolean isMulticomponent;
    @JsonProperty("landedCost")
    private BigDecimal landedCost;
    @JsonProperty("lineItemId")
    private String lineItemId;
    @JsonProperty("lineItemNumber")
    private BigDecimal lineItemNumber;
    @JsonProperty("lineItems")
    private List<LineItem__1> lineItems = null;
    @JsonProperty("lineType")
    private LineType lineType;
    @JsonProperty("locationCode")
    private String locationCode;
    @JsonProperty("materialCode")
    private String materialCode;
    @JsonProperty("materialOrigin")
    private String materialOrigin;
    @JsonProperty("modeOfTransport")
    private BigDecimal modeOfTransport;
    @JsonProperty("natureOfTransaction")
    private BigDecimal natureOfTransaction;
    @JsonProperty("netMassKilograms")
    private BigDecimal netMassKilograms;
    @JsonProperty("nonTaxableOverrides")
    private List<NonTaxableOverride> nonTaxableOverrides = null;
    @JsonProperty("product")
    private Product product;
    @JsonProperty("projectNumber")
    private String projectNumber;
    @JsonProperty("quantity")
    private Quantity quantity;
    @JsonProperty("rateOverrides")
    private List<RateOverride> rateOverrides = null;
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
    @JsonProperty("previousTaxPaid")
    private BigDecimal previousTaxPaid;
    @JsonProperty("taxDate")
    private String taxDate;
    @JsonProperty("taxIncludedIndicator")
    private Boolean taxIncludedIndicator;
    @JsonProperty("taxOverride")
    private TaxOverride taxOverride;
    @JsonProperty("titleTransfer")
    private String titleTransfer;
    @JsonProperty("transactionType")
    private String transactionType;
    @JsonProperty("unitPrice")
    private BigDecimal unitPrice;
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
    public BigDecimal getAmountBilledToDate() {
        return amountBilledToDate;
    }

    @JsonProperty("amountBilledToDate")
    public void setAmountBilledToDate(BigDecimal amountBilledToDate) {
        this.amountBilledToDate = amountBilledToDate;
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
    public BigDecimal getCompanyCodeCurrencyTaxAmount() {
        return companyCodeCurrencyTaxAmount;
    }

    @JsonProperty("companyCodeCurrencyTaxAmount")
    public void setCompanyCodeCurrencyTaxAmount(BigDecimal companyCodeCurrencyTaxAmount) {
        this.companyCodeCurrencyTaxAmount = companyCodeCurrencyTaxAmount;
    }

    @JsonProperty("companyCodeCurrencyTaxableAmount")
    public BigDecimal getCompanyCodeCurrencyTaxableAmount() {
        return companyCodeCurrencyTaxableAmount;
    }

    @JsonProperty("companyCodeCurrencyTaxableAmount")
    public void setCompanyCodeCurrencyTaxableAmount(BigDecimal companyCodeCurrencyTaxableAmount) {
        this.companyCodeCurrencyTaxableAmount = companyCodeCurrencyTaxableAmount;
    }

    @JsonProperty("cost")
    public BigDecimal getCost() {
        return cost;
    }

    @JsonProperty("cost")
    public void setCost(BigDecimal cost) {
        this.cost = cost;
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
    public List<ExemptOverride__1> getExemptOverrides() {
        return exemptOverrides;
    }

    @JsonProperty("exemptOverrides")
    public void setExemptOverrides(List<ExemptOverride__1> exemptOverrides) {
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
    public BigDecimal getExtendedPrice() {
        return extendedPrice;
    }

    @JsonProperty("extendedPrice")
    public void setExtendedPrice(BigDecimal extendedPrice) {
        this.extendedPrice = extendedPrice;
    }

    @JsonProperty("fairMarketValue")
    public BigDecimal getFairMarketValue() {
        return fairMarketValue;
    }

    @JsonProperty("fairMarketValue")
    public void setFairMarketValue(BigDecimal fairMarketValue) {
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
    public BigDecimal getFreight() {
        return freight;
    }

    @JsonProperty("freight")
    public void setFreight(BigDecimal freight) {
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
    public List<ImpositionInclusion__1> getImpositionInclusions() {
        return impositionInclusions;
    }

    @JsonProperty("impositionInclusions")
    public void setImpositionInclusions(List<ImpositionInclusion__1> impositionInclusions) {
        this.impositionInclusions = impositionInclusions;
    }

    @JsonProperty("inputTotalTax")
    public BigDecimal getInputTotalTax() {
        return inputTotalTax;
    }

    @JsonProperty("inputTotalTax")
    public void setInputTotalTax(BigDecimal inputTotalTax) {
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

    @JsonProperty("landedCost")
    public BigDecimal getLandedCost() {
        return landedCost;
    }

    @JsonProperty("landedCost")
    public void setLandedCost(BigDecimal landedCost) {
        this.landedCost = landedCost;
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
    public BigDecimal getLineItemNumber() {
        return lineItemNumber;
    }

    @JsonProperty("lineItemNumber")
    public void setLineItemNumber(BigDecimal lineItemNumber) {
        this.lineItemNumber = lineItemNumber;
    }

    @JsonProperty("lineItems")
    public List<LineItem__1> getLineItems() {
        return lineItems;
    }

    @JsonProperty("lineItems")
    public void setLineItems(List<LineItem__1> lineItems) {
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
    public BigDecimal getModeOfTransport() {
        return modeOfTransport;
    }

    @JsonProperty("modeOfTransport")
    public void setModeOfTransport(BigDecimal modeOfTransport) {
        this.modeOfTransport = modeOfTransport;
    }

    @JsonProperty("natureOfTransaction")
    public BigDecimal getNatureOfTransaction() {
        return natureOfTransaction;
    }

    @JsonProperty("natureOfTransaction")
    public void setNatureOfTransaction(BigDecimal natureOfTransaction) {
        this.natureOfTransaction = natureOfTransaction;
    }

    @JsonProperty("netMassKilograms")
    public BigDecimal getNetMassKilograms() {
        return netMassKilograms;
    }

    @JsonProperty("netMassKilograms")
    public void setNetMassKilograms(BigDecimal netMassKilograms) {
        this.netMassKilograms = netMassKilograms;
    }

    @JsonProperty("nonTaxableOverrides")
    public List<NonTaxableOverride> getNonTaxableOverrides() {
        return nonTaxableOverrides;
    }

    @JsonProperty("nonTaxableOverrides")
    public void setNonTaxableOverrides(List<NonTaxableOverride> nonTaxableOverrides) {
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
    public List<RateOverride> getRateOverrides() {
        return rateOverrides;
    }

    @JsonProperty("rateOverrides")
    public void setRateOverrides(List<RateOverride> rateOverrides) {
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

    @JsonProperty("previousTaxPaid")
    public BigDecimal getPreviousTaxPaid() {
        return previousTaxPaid;
    }

    @JsonProperty("previousTaxPaid")
    public void setPreviousTaxPaid(BigDecimal previousTaxPaid) {
        this.previousTaxPaid = previousTaxPaid;
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

    @JsonProperty("titleTransfer")
    public String getTitleTransfer() {
        return titleTransfer;
    }

    @JsonProperty("titleTransfer")
    public void setTitleTransfer(String titleTransfer) {
        this.titleTransfer = titleTransfer;
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
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    @JsonProperty("unitPrice")
    public void setUnitPrice(BigDecimal unitPrice) {
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
