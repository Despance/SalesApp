package com.despance.salesapp.modal.Product;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProductDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Product product);

    @Query("DELETE FROM products")
    void deleteAll();

    @Query("SELECT * FROM products WHERE barcode = :barcode")
    Product getProductByBarcode(String barcode);

    @Query("SELECT * FROM products WHERE id = :id")
    Product getProductById(int id);

    @Query("DELETE FROM products WHERE id = :productId")
    void deleteProductById(int productId);

    @Query("SELECT * FROM products")
    LiveData<List<Product>> getAllProducts();

    @Query("UPDATE products SET productName = :productName, price = :price, vatRate = :vatRate, barcode = :barcode WHERE id = :id")
    void updateProduct(int id, String productName, float price, float vatRate, String barcode);

}
