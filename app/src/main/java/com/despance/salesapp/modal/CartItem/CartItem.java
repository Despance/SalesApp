package com.despance.salesapp.modal.CartItem;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.despance.salesapp.modal.Product.Product;

@Entity(tableName = "cartItems")
public class CartItem {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "productId")
    private int productId;

    @ColumnInfo(name = "quantity")
    private int quantity;

    @ColumnInfo(name = "timeAdded")
    private long timeAdded;

    public CartItem(){

    };
    public CartItem(Product cartItem, int quantity) {

        this.productId = cartItem.getId();
        this.quantity = quantity;
        this.timeAdded = System.currentTimeMillis();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(long timeAdded) {
        this.timeAdded = timeAdded;
    }
}
