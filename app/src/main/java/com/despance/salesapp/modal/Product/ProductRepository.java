package com.despance.salesapp.modal.Product;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ProductRepository {

    private ProductDao productDao;
    private LiveData<List<Product>> allProducts;

    public ProductRepository(Application application) {
        ProductDatabase db = ProductDatabase.getDatabase(application);
        productDao = db.productDao();
        allProducts = productDao.getAllProducts();
    }

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public void insert(Product product) {
        ProductDatabase.databaseWriteExecutor.execute(() -> {
            productDao.insert(product);
        });
    }

    public Product getProductByBarcode(String barcode) {
        return productDao.getProductByBarcode(barcode);
    }

    public Product getProductById(int id) {
        return productDao.getProductById(id);
    }

}
