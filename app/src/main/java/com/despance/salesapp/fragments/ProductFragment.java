package com.despance.salesapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.despance.salesapp.R;
import com.despance.salesapp.data.Product;
import com.despance.salesapp.data.ProductDBHelper;
import com.despance.salesapp.databinding.FragmentProductBinding;
import com.despance.salesapp.databinding.RecyclerViewAdapterBinding;
import com.despance.salesapp.view.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment {

    private FragmentProductBinding _binding;
    private ProductDBHelper dbHelper;
    public ProductFragment() {
        // Required empty public constructor
    }

    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        _binding = FragmentProductBinding.inflate(getLayoutInflater());
        dbHelper = new ProductDBHelper(getContext());

        /*
        dbHelper.addProduct(new Product("Coca Cola", 5, 0.18f, "8690536000012"));
        dbHelper.addProduct(new Product("Fanta", 5, 0.18f, "8690536000022"));
        dbHelper.addProduct(new Product("Sprite", 5, 0.18f, "8690536000032"));
        dbHelper.addProduct(new Product("Cappy", 5, 0.18f, "8690536000042"));


         */
        _binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        _binding.recyclerView.setHasFixedSize(true);
        List<Product> products = dbHelper.getAllProducts();

        for (Product product : products){
            Log.d("PRODUCT","Id:"+product.getId()+ " Product Name: " + product.getProductName() + " Price: " + product.getPrice() + " Vat Rate: " + product.getVatRate() + " Barcode: " + product.getBarcode());
        }
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), new ArrayList<Product>(products));

        Log.d("PRODUCT","RecyclerView Created");
        _binding.recyclerView.setAdapter(adapter);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return _binding.getRoot();
    }
}