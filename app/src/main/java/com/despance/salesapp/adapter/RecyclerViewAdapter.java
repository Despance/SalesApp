package com.despance.salesapp.adapter;

import static androidx.recyclerview.widget.RecyclerView.*;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.despance.salesapp.data.Product;
import com.despance.salesapp.databinding.RecyclerViewAdapterBinding;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Product> _products;
    private Product selectedProduct;
    private Context context;

    public RecyclerViewAdapter(Context context, ArrayList<Product> products){
        this.context = context;
        _products = products;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View w = LayoutInflater.from(parent.getContext()).inflate(com.despance.salesapp.R.layout.recycler_view_adapter, parent, false);
        return new ViewHolder(w);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = _products.get(position);


        holder._binding.productNameTextView.setText(product.getProductName());
        holder._binding.priceTextView.setText(String.format("Price: %s", String.valueOf(product.getPrice())));
        holder._binding.vatRateTextView.setText(String.format("VAT Rate: %s%%", String.valueOf(product.getVatRate() * 100)));
        holder._binding.barcodeTextView.setText(String.format("Barcode Number\n %s", product.getBarcode()));

    }


    @Override
    public int getItemCount() {
        return _products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener{

        RecyclerViewAdapterBinding _binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            _binding = RecyclerViewAdapterBinding.bind(itemView);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            Log.d("PRODUCT", "Product selected: "+ _products.get(getAdapterPosition()).getProductName());
        }
    }
}


