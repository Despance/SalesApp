package com.despance.salesapp.modal.CartItem;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.despance.salesapp.data.Converters;
import com.despance.salesapp.modal.Product.Product;



@Entity(tableName = "cartItems")
public class CartItem {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @TypeConverters(Converters.class)
    @ColumnInfo(name = "product")
    private Product product;

    @ColumnInfo(name = "quantity")
    private int quantity;

    @ColumnInfo(name = "timeAdded")
    private long timeAdded;

    public CartItem(){

    };
    public CartItem(Product cartItem, int quantity) {

        this.product = cartItem;
        this.quantity = quantity;
        this.timeAdded = System.currentTimeMillis();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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
