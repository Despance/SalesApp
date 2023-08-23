package com.despance.salesapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.despance.salesapp.R;
import com.despance.salesapp.databinding.SummaryRecyclerViewBinding;
import com.despance.salesapp.modal.CartItem.CartItem;
import com.despance.salesapp.modal.Product.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderSummaryRecyclerViewAdapter extends RecyclerView.Adapter<OrderSummaryRecyclerViewAdapter.ViewHolder>{


    private static List<CartItem> cartItems;
    private static List<Product> products;

    private SummaryRecyclerViewBinding _binding;






    @NonNull
    @Override
    public OrderSummaryRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View w = LayoutInflater.from(parent.getContext()).inflate(R.layout.summary_recycler_view, parent, false);
        return new ViewHolder(w);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderSummaryRecyclerViewAdapter.ViewHolder holder, int position) {

        CartItem cartItem = cartItems.get(position);
        Product product = products.get(position);

        Log.d("Summary", "Summary created: "+product.getProductName()+" "+cartItem.getQuantity()+" "+product.getPrice()+" "+product.getPrice()*cartItem.getQuantity());

        _binding.productNameView.setText(product.getProductName());
        _binding.unitPriceTextView.setText(String.valueOf(product.getPrice()));
        _binding.quantityTextView.setText(String.valueOf(cartItem.getQuantity()));
        _binding.priceTextView.setText(String.valueOf(product.getPrice()*cartItem.getQuantity()));

    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            _binding = SummaryRecyclerViewBinding.bind(itemView);

        }
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
