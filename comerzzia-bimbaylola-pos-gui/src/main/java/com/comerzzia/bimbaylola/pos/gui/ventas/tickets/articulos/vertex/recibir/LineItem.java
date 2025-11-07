
package com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.vertex.recibir;

import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "customer",
    "extendedPrice",
    "fairMarketValue",
    "product",
    "quantity",
    "seller",
    "taxIncludedIndicator",
    "taxes",
    "totalTax",
    "transactionType",
    "unitPrice"
})
@Generated("jsonschema2pojo")
public class LineItem {

    @JsonProperty("customer")
    private Customer__1 customer;
    @JsonProperty("extendedPrice")
    private Double extendedPrice;
    @JsonProperty("fairMarketValue")
    private Double fairMarketValue;
    @JsonProperty("product")
    private Product product;
    @JsonProperty("quantity")
    private Quantity quantity;
    @JsonProperty("seller")
    private Seller seller;
    @JsonProperty("taxIncludedIndicator")
    private Boolean taxIncludedIndicator;
    @JsonProperty("taxes")
    private List<Tax> taxes = null;
    @JsonProperty("totalTax")
    private Double totalTax;
    @JsonProperty("transactionType")
    private String transactionType;
    @JsonProperty("unitPrice")
    private Double unitPrice;

    @JsonProperty("customer")
    public Customer__1 getCustomer() {
        return customer;
    }

    @JsonProperty("customer")
    public void setCustomer(Customer__1 customer) {
        this.customer = customer;
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

    @JsonProperty("product")
    public Product getProduct() {
        return product;
    }

    @JsonProperty("product")
    public void setProduct(Product product) {
        this.product = product;
    }

    @JsonProperty("quantity")
    public Quantity getQuantity() {
        return quantity;
    }

    @JsonProperty("quantity")
    public void setQuantity(Quantity quantity) {
        this.quantity = quantity;
    }

    @JsonProperty("seller")
    public Seller getSeller() {
        return seller;
    }

    @JsonProperty("seller")
    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    @JsonProperty("taxIncludedIndicator")
    public Boolean getTaxIncludedIndicator() {
        return taxIncludedIndicator;
    }

    @JsonProperty("taxIncludedIndicator")
    public void setTaxIncludedIndicator(Boolean taxIncludedIndicator) {
        this.taxIncludedIndicator = taxIncludedIndicator;
    }

    @JsonProperty("taxes")
    public List<Tax> getTaxes() {
        return taxes;
    }

    @JsonProperty("taxes")
    public void setTaxes(List<Tax> taxes) {
        this.taxes = taxes;
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

}
