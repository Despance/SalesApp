package com.despance.salesapp.adapter;

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

import java.util.List;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder> {


    private final CartItemViewModel cartItemViewModel;
    private List<CartItem> cartItems;
    private CartRecyclerViewBinding _binding;


    public CartRecyclerViewAdapter(List<CartItem> cartItems, CartItemViewModel cartItemViewModel){
        this.cartItems = cartItems;
        this.cartItemViewModel=cartItemViewModel;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }


    @NonNull
    @Override
    public CartRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View w = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_recycler_view, parent, false);

        return new ViewHolder(w);
    }

    @Override
    public void onBindViewHolder(@NonNull CartRecyclerViewAdapter.ViewHolder holder, int position) {


        CartItem currentCartItem = cartItems.get(position);
        Product currentProduct = currentCartItem.getProduct();

        _binding.cartProductNameTextView.setText(currentProduct.getProductName());
        _binding.quantitiyTextView.setText(String.valueOf(currentCartItem.getQuantity()));
        _binding.totalPriceTextView.setText(String.valueOf(currentCartItem.getQuantity() * currentProduct.getPrice()));


        _binding.decrementButton.setOnClickListener(v -> {
            if(currentCartItem.getQuantity() > 1){
                cartItemViewModel.updateCartItem(currentCartItem.getId(), currentCartItem.getQuantity()-1);
            }else
                cartItemViewModel.deleteById(currentCartItem.getId());

            v.startAnimation(android.view.animation.AnimationUtils.loadAnimation(v.getContext(), R.anim.anim_item));
        });

        _binding.incrementButton.setOnClickListener(v -> {
            cartItemViewModel.updateCartItem(currentCartItem.getId(), currentCartItem.getQuantity() + 1);
            v.startAnimation(android.view.animation.AnimationUtils.loadAnimation(v.getContext(), R.anim.anim_item));
        });

    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            _binding = CartRecyclerViewBinding.bind(itemView);

        }
    }
}
