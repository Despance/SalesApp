package com.despance.salesapp.data;

public class Product {
    private int id;
    private String productName;
    private float price;
    private float vatRate;
    private String barcode;

    public Product(String productName, float price, float vatRate, String barcode) {
        this.productName = productName;
        this.price = price;
        this.vatRate = vatRate;
        this.barcode = barcode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getVatRate() {
        return vatRate;
    }

    public void setVatRate(float vatRate) {
        this.vatRate = vatRate;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
