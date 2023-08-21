package com.despance.salesapp.modal.CartItem;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.despance.salesapp.modal.Product.Product;

import java.util.List;

public class CartItemRepository {

    private CartItemDao cartItemDao;
    LiveData<List<CartItem>> allCartItems;

    public CartItemRepository(Application application){
        CartItemDatabase db = CartItemDatabase.getDatabase(application);
        cartItemDao = db.cartItemDao();
        allCartItems = cartItemDao.getCartItems();
    }

    public LiveData<List<CartItem>> getAllCartItems() {
        return allCartItems;
    }
    public void insert(Product cartItem) {
        CartItemDatabase.databaseWriteExecutor.execute(() -> {


            cartItemDao.insert(new CartItem(cartItem, 1));


        });
    }

    public void deleteAll() {
        CartItemDatabase.databaseWriteExecutor.execute(() -> {
            cartItemDao.deleteAll();
        });
    }

    public CartItem getCartItemById(int id) {
        return cartItemDao.getCartItemById(id);
    }


    public void updateCartItem(int id, int quantity) {
        CartItemDatabase.databaseWriteExecutor.execute(() -> {
            cartItemDao.updateCartItem(id, quantity);
        });
    }

    public void deleteById(int id) {
        CartItemDatabase.databaseWriteExecutor.execute(() -> {
            cartItemDao.deleteById(id);
        });
    }
}
