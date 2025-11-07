
package com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.vertex.recibir;

import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "customer",
    "documentDate",
    "lineItems",
    "postingDate",
    "returnAssistedParametersIndicator",
    "roundAtLineLevel",
    "saleMessageType",
    "seller",
    "subTotal",
    "total",
    "totalTax",
    "transactionType"
})
@Generated("jsonschema2pojo")
public class Data {

    @JsonProperty("customer")
    private Customer customer;
    @JsonProperty("documentDate")
    private String documentDate;
    @JsonProperty("lineItems")
    private List<LineItem> lineItems = null;
    @JsonProperty("postingDate")
    private String postingDate;
    @JsonProperty("returnAssistedParametersIndicator")
    private Boolean returnAssistedParametersIndicator;
    @JsonProperty("roundAtLineLevel")
    private Boolean roundAtLineLevel;
    @JsonProperty("saleMessageType")
    private String saleMessageType;
    @JsonProperty("seller")
    private Seller__1 seller;
    @JsonProperty("subTotal")
    private Double subTotal;
    @JsonProperty("total")
    private Double total;
    @JsonProperty("totalTax")
    private Double totalTax;
    @JsonProperty("transactionType")
    private String transactionType;

    @JsonProperty("customer")
    public Customer getCustomer() {
        return customer;
    }

    @JsonProperty("customer")
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @JsonProperty("documentDate")
    public String getDocumentDate() {
        return documentDate;
    }

    @JsonProperty("documentDate")
    public void setDocumentDate(String documentDate) {
        this.documentDate = documentDate;
    }

    @JsonProperty("lineItems")
    public List<LineItem> getLineItems() {
        return lineItems;
    }

    @JsonProperty("lineItems")
    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    @JsonProperty("postingDate")
    public String getPostingDate() {
        return postingDate;
    }

    @JsonProperty("postingDate")
    public void setPostingDate(String postingDate) {
        this.postingDate = postingDate;
    }

    @JsonProperty("returnAssistedParametersIndicator")
    public Boolean getReturnAssistedParametersIndicator() {
        return returnAssistedParametersIndicator;
    }

    @JsonProperty("returnAssistedParametersIndicator")
    public void setReturnAssistedParametersIndicator(Boolean returnAssistedParametersIndicator) {
        this.returnAssistedParametersIndicator = returnAssistedParametersIndicator;
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

    @JsonProperty("subTotal")
    public Double getSubTotal() {
        return subTotal;
    }

    @JsonProperty("subTotal")
    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
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

    @JsonProperty("transactionType")
    public String getTransactionType() {
        return transactionType;
    }

    @JsonProperty("transactionType")
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

}
