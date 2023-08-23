package com.despance.salesapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.despance.salesapp.R;
import com.despance.salesapp.modal.CartItem.CartItem;
import com.despance.salesapp.modal.Product.Product;
import com.despance.salesapp.databinding.CartRecyclerViewBinding;
import com.despance.salesapp.viewModal.CartItemViewModel;

import java.util.HashMap;
import java.util.List;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder> {


    private final CartItemViewModel cartItemViewModel;
    private List<CartItem> _cartItems;

    private static HashMap<Integer,Product> products = new HashMap<>();

    private CartRecyclerViewBinding _binding;

    public static HashMap<Integer, Product> getProducts() {
        return products;
    }

    public CartRecyclerViewAdapter(List<CartItem> cartItems, CartItemViewModel cartItemViewModel){
        _cartItems = cartItems;
        this.cartItemViewModel=cartItemViewModel;
    }

    public void set_cartItems(List<CartItem> _cartItems) {
        this._cartItems = _cartItems;
    }

    public void setProducts(int id, Product product){
        products.put(id,product);
    }

    @NonNull
    @Override
    public CartRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View w = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_recycler_view, parent, false);

        return new ViewHolder(w);
    }

    @Override
    public void onBindViewHolder(@NonNull CartRecyclerViewAdapter.ViewHolder holder, int position) {

        _binding = CartRecyclerViewBinding.bind(holder.itemView);



        Product currentProduct = _cartItems.get(position).getProduct();
        if(currentProduct == null)
            currentProduct = new Product("DENEME",12,12,"abc");
        _binding.cartProductNameTextView.setText(currentProduct.getProductName());
        _binding.quantitiyTextView.setText(String.valueOf(_cartItems.get(position).getQuantity()));
        _binding.totalPriceTextView.setText(String.valueOf(_cartItems.get(position).getQuantity() * _cartItems.get(position).getProduct().getPrice()));

        _binding.decrementButton.setOnClickListener(v -> {
            if(_cartItems.get(position).getQuantity() > 1){
                _cartItems.get(position).setQuantity(_cartItems.get(position).getQuantity()-1);
                cartItemViewModel.updateCartItem(_cartItems.get(position).getId(),_cartItems.get(position).getQuantity());
            }else
                cartItemViewModel.deleteById(_cartItems.get(position).getId());

            v.startAnimation(android.view.animation.AnimationUtils.loadAnimation(v.getContext(), R.anim.anim_item));
        });

        _binding.incrementButton.setOnClickListener(v -> {
            _cartItems.get(position).setQuantity(_cartItems.get(position).getQuantity()+1);
            cartItemViewModel.updateCartItem(_cartItems.get(position).getId(),_cartItems.get(position).getQuantity());
            v.startAnimation(android.view.animation.AnimationUtils.loadAnimation(v.getContext(), R.anim.anim_item));
        });




        Log.d("CartRecyclerViewAdapter", "Product created: " + currentProduct.getProductName());
    }

    @Override
    public int getItemCount() {
        return _cartItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            _binding = CartRecyclerViewBinding.bind(itemView);

        }
    }
}
