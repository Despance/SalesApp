package com.despance.salesapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.despance.salesapp.modal.Product.Product;
import com.despance.salesapp.modal.Product.ProductRepository;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {

    private final ProductRepository productRepository;
    private final LiveData<List<Product>> allProducts;

    public ProductViewModel(@NonNull Application application) {
        super(application);
        productRepository = new ProductRepository(application);
        allProducts = productRepository.getAllProducts();
    }

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public Product getProductByBarcode(String barcode){
        return productRepository.getProductByBarcode(barcode);
    }

    public Product getProductById(int id){
        return productRepository.getProductById(id);
    }

    public void deleteAll(){
        productRepository.deleteAll();
    }
    public void insert(Product product) {
        productRepository.insert(product);
    }
}
