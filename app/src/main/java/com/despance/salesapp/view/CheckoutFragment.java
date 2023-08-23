package com.despance.salesapp.view;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.despance.salesapp.adapter.OrderSummaryRecyclerViewAdapter;
import com.despance.salesapp.databinding.FragmentCheckoutBinding;
import com.despance.salesapp.modal.CartItem.CartItem;
import com.despance.salesapp.viewModal.CartItemViewModel;

public class CheckoutFragment extends Fragment {

    CartItemViewModel cartItemViewModel;
    private FragmentCheckoutBinding _binding;
    private String qrCodeData = "Hello World";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        _binding = FragmentCheckoutBinding.inflate(getLayoutInflater());
        cartItemViewModel = new ViewModelProvider(this).get(CartItemViewModel.class);
        OrderSummaryRecyclerViewAdapter orderSummaryRecyclerViewAdapter = new OrderSummaryRecyclerViewAdapter();

        cartItemViewModel.getAllProducts().observe(this, cartItems -> {

            orderSummaryRecyclerViewAdapter.setCartItems(cartItems);
            _binding.checkoutRecyclerView.setAdapter(orderSummaryRecyclerViewAdapter);

            float total = 0;
            for (CartItem cartItem:cartItems) {
                total+=cartItem.getProduct().getPrice()*cartItem.getQuantity();
            }
            _binding.totalTextView.setText(String.format("Total: %s", total));

        });
        _binding.checkoutRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        _binding.checkoutRecyclerView.setHasFixedSize(true);

        _binding.qrButton.setOnClickListener(v -> {

            Bundle bundle = new Bundle();
            bundle.putString("qrCodeData",qrCodeData);
            QRFragment showPopUp = new QRFragment();
            showPopUp.setArguments(bundle);

            showPopUp.show(getChildFragmentManager(),"QR");

        });
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return _binding.getRoot();
    }
}