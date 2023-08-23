package com.despance.salesapp.adapter;

import static androidx.recyclerview.widget.RecyclerView.*;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import com.despance.salesapp.modal.CartItem.CartItem;
import com.despance.salesapp.modal.Product.Product;
import com.despance.salesapp.databinding.ProductRecyclerViewBinding;
import com.despance.salesapp.viewModal.CartItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder> {

    private List<Product> _products;
    private final Context context;

    List<CartItem> cartItems = new ArrayList<>();
    private final CartItemViewModel cartItemViewModel;



    public ProductRecyclerViewAdapter(Context context, List<Product> products, CartItemViewModel cartItemViewModel){
        _products = products;
        this.cartItemViewModel = cartItemViewModel;
        this.context = context;
    }

    public void setProducts(List<Product> products){
        _products = products;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View w = LayoutInflater.from(parent.getContext()).inflate(com.despance.salesapp.R.layout.product_recycler_view, parent, false);

        cartItemViewModel.getAllProducts().observe((LifecycleOwner) context, cartItems1 -> {
            cartItems.clear();
            cartItems.addAll(cartItems1);
        });


        return new ViewHolder(w);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = _products.get(position);

        holder._binding.productNameTextView.setText(product.getProductName());
        holder._binding.priceTextView.setText(String.format("Price: %s", product.getPrice()));
        holder._binding.vatRateTextView.setText(String.format("VAT Rate: %s%%", product.getVatRate() * 100));
        holder._binding.barcodeTextView.setText(String.format("Barcode Number\n %s", product.getBarcode()));

    }


    @Override
    public int getItemCount() {
        return _products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener{

        ProductRecyclerViewBinding _binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            _binding = ProductRecyclerViewBinding.bind(itemView);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            Product selectedProduct = _products.get(getAdapterPosition());

            view.startAnimation(android.view.animation.AnimationUtils.loadAnimation(context, com.despance.salesapp.R.anim.click));

            boolean itemFound =false;
            for (CartItem cartItem: cartItems) {
                if (cartItem.getProduct().getId() == selectedProduct.getId()) {
                        Log.d("PRODUCT", "Product already in cart, incremented by 1: "+ selectedProduct.getProductName());
                        cartItemViewModel.updateCartItem(cartItem.getId(), cartItem.getQuantity() + 1);
                        itemFound = true;
                        break;
                }
            }
            if(!itemFound){
                Log.d("PRODUCT", "Product not in cart, added to cart: "+ selectedProduct.getProductName());
                cartItemViewModel.insert(selectedProduct);

            }

        }
    }
}


