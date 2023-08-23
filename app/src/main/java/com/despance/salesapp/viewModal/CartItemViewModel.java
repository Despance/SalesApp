package com.despance.salesapp.viewModal;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.despance.salesapp.modal.CartItem.CartItem;
import com.despance.salesapp.modal.CartItem.CartItemRepository;
import com.despance.salesapp.modal.Product.Product;

import java.util.List;


public class CartItemViewModel extends AndroidViewModel {
    private final CartItemRepository cartItemRepository;

    private final LiveData<List<CartItem>> allProducts;
    public CartItemViewModel(@NonNull Application application){
        super(application);
        cartItemRepository = new CartItemRepository(application);
        allProducts = cartItemRepository.getAllCartItems();
    }

    public LiveData<List<CartItem>> getAllProducts() {
        return allProducts;
    }

    public void updateCartItem(int id, int quantity) {
        cartItemRepository.updateCartItem(id, quantity);
    }
    public void deleteAll() {
        cartItemRepository.deleteAll();
    }
    public void insert(Product cartItem) {
        cartItemRepository.insert(cartItem);
    }

    public void deleteById(int id) {
        cartItemRepository.deleteById(id);
    }
}
