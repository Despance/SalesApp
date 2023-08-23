package com.despance.salesapp.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.despance.salesapp.R;
import com.despance.salesapp.adapter.CartRecyclerViewAdapter;
import com.despance.salesapp.modal.CartItem.CartItem;
import com.despance.salesapp.modal.Product.Product;
import com.despance.salesapp.databinding.FragmentCartBinding;
import com.despance.salesapp.viewModal.CartItemViewModel;
import com.despance.salesapp.viewModal.ProductViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class CartFragment extends Fragment {

    private FragmentCartBinding _binding;
    private CartRecyclerViewAdapter cartRecyclerViewAdapter;
    private CartItemViewModel cartItemViewModel;
    private ProductViewModel productViewModel;



    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        _binding = FragmentCartBinding.inflate(getLayoutInflater());

        cartItemViewModel  = new ViewModelProvider(this).get(CartItemViewModel.class);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        cartRecyclerViewAdapter= new CartRecyclerViewAdapter(new ArrayList<>(),cartItemViewModel);

        productViewModel.getAllProducts().observe(this, products -> {
                for (Product product : products) {
                    cartRecyclerViewAdapter.setProducts(product.getId(), product);

            }

        });

        cartItemViewModel.getAllProducts().observe(this, cartItems -> {
            cartRecyclerViewAdapter.set_cartItems(cartItems);

            float totalCost = 0;
            int totalQuantity = 0;

                for(CartItem cartItem: cartItems){
                    totalQuantity += cartItem.getQuantity();
                    if(CartRecyclerViewAdapter.getProducts().get(cartItem.getProduct().getId()) != null)
                        totalCost+=cartItem.getProduct().getPrice()*cartItem.getQuantity();
                }
            _binding.totalItemTextView.setText(totalQuantity+" Items in Cart");
            _binding.totalCostTextView.setText(totalCost+" TL");


            _binding.recyclerView.setAdapter(cartRecyclerViewAdapter);
        });

        _binding.emptyButton.setOnClickListener(v -> {
            cartItemViewModel.deleteAll();
        });

        _binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        _binding.recyclerView.setHasFixedSize(true);

        _binding.checkoutButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_cartFragment_to_checkoutFragment);
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