package com.despance.salesapp.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.despance.salesapp.R;
import com.despance.salesapp.adapter.ProductRecyclerViewAdapter;
import com.despance.salesapp.databinding.FragmentProductBinding;
import com.despance.salesapp.viewModel.CartItemViewModel;
import com.despance.salesapp.viewModel.ProductViewModel;

public class ProductFragment extends Fragment {
    private FragmentProductBinding _binding;
    private CartItemViewModel cartItemViewModel;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        _binding = FragmentProductBinding.inflate(getLayoutInflater());
        cartItemViewModel = new ViewModelProvider(this).get(CartItemViewModel.class);


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.despance.salesapp", Context.MODE_PRIVATE);


        int loginId = getArguments() != null ? getArguments().getInt("id") : 0;
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);

        //Empty the cart if a new user login
        if (loginId != sharedPreferences.getInt("id",-1))
            cartItemViewModel.deleteAll();

        sharedPreferences.edit().putInt("id",loginId).apply();


        ProductViewModel productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        cartItemViewModel = new ViewModelProvider(this).get(CartItemViewModel.class);



        productViewModel.getAllProducts().observe(this, product->{
            ProductRecyclerViewAdapter adapter = new ProductRecyclerViewAdapter(getContext(), product, cartItemViewModel);
            adapter.setProducts(product);
            _binding.recyclerView.setAdapter(adapter);
        } );
        _binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        _binding.recyclerView.setHasFixedSize(true);

        _binding.floatingActionButton.setOnClickListener(view -> navController.navigate(R.id.action_ProductFragment_to_cartFragment));

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return _binding.getRoot();
    }
}