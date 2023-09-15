package com.despance.salesapp.data;

import com.despance.salesapp.modal.CartItem.CartItem;

public class Receipt {

    private int id;
    private int userId;
    private String timestamp;
    private CartItem[] cartItems;
    private float creditTotal;
    private float cashTotal;
    private float qrTotal;

    public Receipt() {
    }

    public Receipt(int userId, String timestamp, float creditTotal, float cashTotal, float qrTotal) {
        this.userId = userId;
        this.timestamp = timestamp;
        this.creditTotal = creditTotal;
        this.cashTotal = cashTotal;
        this.qrTotal = qrTotal;
    }

    public Receipt(int id, int userId, String timestamp, float creditTotal, float cashTotal, float qrTotal) {
        this(userId, timestamp, creditTotal, cashTotal, qrTotal);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public float getCreditTotal() {
        return creditTotal;
    }

    public void setCreditTotal(float creditTotal) {
        this.creditTotal = creditTotal;
    }

    public float getCashTotal() {
        return cashTotal;
    }

    public void setCashTotal(float cashTotal) {
        this.cashTotal = cashTotal;
    }

    public float getQrTotal() {
        return qrTotal;
    }

    public void setQrTotal(float qrTotal) {
        this.qrTotal = qrTotal;
    }

    public CartItem[] getCartItems() {
        return cartItems;
    }

    public void setCartItems(CartItem[] cartItems) {
        this.cartItems = cartItems;
    }
}
