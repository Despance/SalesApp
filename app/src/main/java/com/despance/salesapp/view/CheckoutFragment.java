package com.despance.salesapp.view;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.despance.salesapp.R;
import com.despance.salesapp.adapter.OrderSummaryRecyclerViewAdapter;
import com.despance.salesapp.databinding.FragmentCheckoutBinding;
import com.despance.salesapp.modal.CartItem.CartItem;
import com.despance.salesapp.modal.Product.Product;
import com.despance.salesapp.viewModal.CartItemViewModel;
import com.despance.salesapp.viewModal.ProductViewModel;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;

public class CheckoutFragment extends Fragment {

    CartItemViewModel cartItemViewModel;
    ProductViewModel productViewModel;
    private FragmentCheckoutBinding _binding;

    private String qrCodeData = "Hello World";

    public CheckoutFragment() {
        // Required empty public constructor
    }

    public static CheckoutFragment newInstance(String param1, String param2) {
        CheckoutFragment fragment = new CheckoutFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        _binding = FragmentCheckoutBinding.inflate(getLayoutInflater());
        cartItemViewModel = new ViewModelProvider(this).get(CartItemViewModel.class);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        OrderSummaryRecyclerViewAdapter orderSummaryRecyclerViewAdapter = new OrderSummaryRecyclerViewAdapter();

        productViewModel.getAllProducts().observe(this, products -> {

            orderSummaryRecyclerViewAdapter.setProducts(products);
            for (Product product:products) {
                Log.d("Summary", "onCreate: "+ product.getProductName());
            }
            orderSummaryRecyclerViewAdapter.notifyDataSetChanged();

        });

        cartItemViewModel.getAllProducts().observe(this, cartItems -> {

            orderSummaryRecyclerViewAdapter.setCartItems(cartItems);
            orderSummaryRecyclerViewAdapter.notifyDataSetChanged();

            _binding.checkoutRecyclerView.setAdapter(orderSummaryRecyclerViewAdapter);

            float total = 0;
            for (CartItem cartItem:cartItems) {
                if (orderSummaryRecyclerViewAdapter.getProducts()!= null)
                    for (Product product:orderSummaryRecyclerViewAdapter.getProducts()) {
                        if(cartItem.getProduct().getId()==product.getId()){
                            total+=product.getPrice()*cartItem.getQuantity();
                        }
                    }
            }
            _binding.totalTextView.setText("Total: "+total);

        });

        _binding.checkoutRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        _binding.checkoutRecyclerView.setHasFixedSize(true);

        _binding.qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("qrCodeData",qrCodeData);
                QRFragment showPopUp = new QRFragment();
                showPopUp.setArguments(bundle);

                showPopUp.show(getChildFragmentManager(),"QR");

            }
        });

        super.onCreate(savedInstanceState);

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {






        // Inflate the layout for this fragment
        return _binding.getRoot();
    }
}