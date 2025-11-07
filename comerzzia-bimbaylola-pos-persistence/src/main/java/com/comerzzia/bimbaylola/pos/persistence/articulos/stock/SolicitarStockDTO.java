package com.comerzzia.bimbaylola.pos.persistence.articulos.stock;

public class SolicitarStockDTO{

	private String product;
	private String productDescription;
	private String variant;
	private String ean;
	private int totalStock;
	private int showcaseStock;
	private int reservationStock;
	private int onTheWayStock;
	private int storeStock;
	
	public String getProduct(){
		return product;
	}
	public void setProduct(String product){
		this.product = product;
	}
	public String getProductDescription(){
		return productDescription;
	}
	public void setProductDescription(String productDescription){
		this.productDescription = productDescription;
	}
	public String getVariant(){
		return variant;
	}
	public void setVariant(String variant){
		this.variant = variant;
	}
	public String getEan(){
		return ean;
	}
	public void setEan(String ean){
		this.ean = ean;
	}
	public int getTotalStock() {
		return totalStock;
	}
	public void setTotalStock(int totalStock) {
		this.totalStock = totalStock;
	}
	public int getShowcaseStock() {
		return showcaseStock;
	}
	public void setShowcaseStock(int showcaseStock) {
		this.showcaseStock = showcaseStock;
	}
	public int getReservationStock() {
		return reservationStock;
	}
	public void setReservationStock(int reservationStock) {
		this.reservationStock = reservationStock;
	}
	public int getOnTheWayStock() {
		return onTheWayStock;
	}
	public void setOnTheWayStock(int onTheWayStock) {
		this.onTheWayStock = onTheWayStock;
	}
	public int getStoreStock() {
		return storeStock;
	}
	public void setStoreStock(int storeStock) {
		this.storeStock = storeStock;
	}
	
}
