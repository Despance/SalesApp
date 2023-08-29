package com.despance.salesapp.view;

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

import com.despance.salesapp.MainActivity;
import com.despance.salesapp.R;
import com.despance.salesapp.adapter.ProductRecyclerViewAdapter;
import com.despance.salesapp.databinding.FragmentProductBinding;
import com.despance.salesapp.viewModal.CartItemViewModel;
import com.despance.salesapp.viewModal.ProductViewModel;

public class ProductFragment extends Fragment {
    private FragmentProductBinding _binding;
    private CartItemViewModel cartItemViewModel;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        _binding = FragmentProductBinding.inflate(getLayoutInflater());
        cartItemViewModel = new ViewModelProvider(this).get(CartItemViewModel.class);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.despance.salesapp", getActivity().MODE_PRIVATE);


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



        /*
        productViewModel.insert(new Product("Coca Cola", 30, 0.18f, "8690536000012"));
        productViewModel.insert(new Product("Fanta", 25, 0.18f, "8690536000022"));
        productViewModel.insert(new Product("Sprite", 20, 0.18f, "8690536000032"));
        productViewModel.insert(new Product("Cappy", 27, 0.18f, "8690536000042"));
        productViewModel.insert(new Product("Limonata", 15, 0.18f, "8690536000082"));
        productViewModel.insert(new Product("Soda", 10, 0.18f, "8690536000112"));
        productViewModel.insert(new Product("Ayran", 5, 0.18f, "8690536000062"));
        productViewModel.insert(new Product("Süt", 7, 0.18f, "8690536000072"));
         */

        _binding.floatingActionButton.setOnClickListener(view -> navController.navigate(R.id.action_ProductFragment_to_cartFragment));

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return _binding.getRoot();
    }
}