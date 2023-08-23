package com.despance.salesapp.modal.CartItem;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;


@Dao
public interface CartItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(CartItem cartItem);

    @Query("DELETE FROM cartItems")
    void deleteAll();

    @Query("SELECT * FROM cartItems")
    LiveData<List<CartItem>> getCartItems();

    @Query("SELECT * FROM cartItems WHERE id = :id")
    CartItem getCartItemById(int id);

    @Query("UPDATE cartItems SET quantity = :quantity WHERE id = :id")
    void updateCartItem(int id, int quantity);

    @Query("DELETE FROM cartItems WHERE id = :id")
    void deleteById(int id);
}
