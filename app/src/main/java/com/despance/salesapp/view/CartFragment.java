package com.despance.salesapp.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.despance.salesapp.R;
import com.despance.salesapp.adapter.CartRecyclerViewAdapter;
import com.despance.salesapp.modal.CartItem.CartItem;
import com.despance.salesapp.databinding.FragmentCartBinding;
import com.despance.salesapp.viewModel.CartItemViewModel;


import java.util.ArrayList;

public class CartFragment extends Fragment {

    private FragmentCartBinding _binding;
    private CartRecyclerViewAdapter cartRecyclerViewAdapter;
    private CartItemViewModel cartItemViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        _binding = FragmentCartBinding.inflate(getLayoutInflater());

        cartItemViewModel  = new ViewModelProvider(this).get(CartItemViewModel.class);
        cartRecyclerViewAdapter= new CartRecyclerViewAdapter(new ArrayList<>(),cartItemViewModel);


        cartItemViewModel.getAllProducts().observe(this, cartItems -> {
            cartRecyclerViewAdapter.setCartItems(cartItems);
            float totalCost = 0;
            int totalQuantity = 0;
                for(CartItem cartItem: cartItems){
                    totalQuantity += cartItem.getQuantity();
                    totalCost += cartItem.getQuantity()*cartItem.getProduct().getPrice();
                }
            _binding.totalItemTextView.setText(String.format("%s Items in Cart", totalQuantity));
            _binding.totalCostTextView.setText(String.format("%s TL", totalCost));

            _binding.recyclerView.setAdapter(cartRecyclerViewAdapter);
        });

        _binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        _binding.recyclerView.setHasFixedSize(true);


        _binding.emptyButton.setOnClickListener(v -> cartItemViewModel.deleteAll());


        _binding.checkoutButton.setOnClickListener(v -> {
            if(cartItemViewModel.getAllProducts().getValue().size() == 0) {
                Toast.makeText(getContext(), "Cart is Empty", Toast.LENGTH_SHORT).show();
                return;
            }
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_cartFragment_to_checkoutFragment);
        });


        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return _binding.getRoot();
    }
}