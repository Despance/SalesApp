package com.despance.salesapp.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.despance.salesapp.adapter.CartRecyclerViewAdapter;
import com.despance.salesapp.modal.Product.Product;
import com.despance.salesapp.databinding.FragmentCartBinding;
import com.despance.salesapp.viewModal.CartItemViewModel;
import com.despance.salesapp.viewModal.ProductViewModel;

public class CartFragment extends Fragment {

    FragmentCartBinding _binding;
    CartRecyclerViewAdapter cartRecyclerViewAdapter;
    CartItemViewModel cartItemViewModel;
    ProductViewModel productViewModel;
    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        _binding = FragmentCartBinding.inflate(getLayoutInflater());

        cartItemViewModel  = new ViewModelProvider(this).get(CartItemViewModel.class);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        cartItemViewModel.getAllProducts().observe(this, cartItems -> {

            cartRecyclerViewAdapter= new CartRecyclerViewAdapter(cartItems,cartItemViewModel);
            cartRecyclerViewAdapter.set_cartItems(cartItems);



            _binding.recyclerView.setAdapter(cartRecyclerViewAdapter);
        });




        productViewModel.getAllProducts().observe(this, products -> {
            if(products != null)
                for (Product product : products) {
                       cartRecyclerViewAdapter.setProducts(product.getId(),product);
                }
        });

        _binding.emptyButton.setOnClickListener(v -> {
            cartItemViewModel.deleteAll();
        });

        _binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        _binding.recyclerView.setHasFixedSize(true);

        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return _binding.getRoot();
    }
}