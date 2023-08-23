package com.despance.salesapp.view;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.despance.salesapp.MainActivity;
import com.despance.salesapp.modal.Product.Product;
import com.despance.salesapp.modal.ProductDBHelper;
import com.despance.salesapp.databinding.FragmentProductBinding;
import com.despance.salesapp.adapter.ProductRecyclerViewAdapter;
import com.despance.salesapp.viewModal.CartItemViewModel;
import com.despance.salesapp.viewModal.ProductViewModel;

public class ProductFragment extends Fragment {

    private FragmentProductBinding _binding;
    private ProductDBHelper dbHelper;

    private ProductViewModel productViewModel;
    private CartItemViewModel cartItemViewModel;

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

        String userName = getArguments().getString("username");
        int loginId = getArguments().getInt("id");

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        cartItemViewModel = new ViewModelProvider(this).get(CartItemViewModel.class);


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.despance.salesapp", getActivity().MODE_PRIVATE);


        ((MainActivity)getActivity()).setActionBarTitle("Welcome " + userName);



        if (loginId != sharedPreferences.getInt("id",-1)){
            cartItemViewModel.deleteAll();
            Log.d("LOGIN","Login id is not same deleted all cart items");
        }else {
            Log.d("LOGIN","Login id is same");
        }

        sharedPreferences.edit().putInt("id",loginId).apply();

        productViewModel.getAllProducts().observe(this,product->{
            ProductRecyclerViewAdapter adapter = new ProductRecyclerViewAdapter(getContext(), product, cartItemViewModel);

            adapter.setProducts(product);
            adapter.notifyDataSetChanged();
            _binding.recyclerView.setAdapter(adapter);
        } );
        /*
        dbHelper = new ProductDBHelper(getContext());
        dbHelper.addProduct(new Product("Coca Cola", 5, 0.18f, "8690536000012"));
        dbHelper.addProduct(new Product("Fanta", 5, 0.18f, "8690536000022"));
        dbHelper.addProduct(new Product("Sprite", 5, 0.18f, "8690536000032"));
        dbHelper.addProduct(new Product("Cappy", 5, 0.18f, "8690536000042"));

         */


        productViewModel.insert(new Product("Coca Cola", 30, 0.18f, "8690536000012"));
        productViewModel.insert(new Product("Fanta", 25, 0.18f, "8690536000022"));
        productViewModel.insert(new Product("Sprite", 20, 0.18f, "8690536000032"));
        productViewModel.insert(new Product("Cappy", 27, 0.18f, "8690536000042"));
        productViewModel.insert(new Product("Limonata", 15, 0.18f, "8690536000082"));
        productViewModel.insert(new Product("Soda", 10, 0.18f, "8690536000112"));
        productViewModel.insert(new Product("Ayran", 5, 0.18f, "8690536000062"));
        productViewModel.insert(new Product("Süt", 7, 0.18f, "8690536000072"));



        _binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        _binding.recyclerView.setHasFixedSize(true);

        _binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                Log.d("CART", "Cart button clicked");
                navController.navigate(com.despance.salesapp.R.id.action_ProductFragment_to_cartFragment);
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