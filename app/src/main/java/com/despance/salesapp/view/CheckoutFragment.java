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
import com.despance.salesapp.adapter.OrderSummaryRecyclerViewAdapter;
import com.despance.salesapp.databinding.FragmentCheckoutBinding;
import com.despance.salesapp.modal.CartItem.CartItem;
import com.despance.salesapp.viewModel.CartItemViewModel;

public class CheckoutFragment extends Fragment {

    CartItemViewModel cartItemViewModel;
    private FragmentCheckoutBinding _binding;
    private String qrCodeData;
    private float total = 0;
    private float discountTotal,discountCash,discountCredit,discountQR = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        _binding = FragmentCheckoutBinding.inflate(getLayoutInflater());
        NavController navController= Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
        cartItemViewModel = new ViewModelProvider(this).get(CartItemViewModel.class);
        OrderSummaryRecyclerViewAdapter orderSummaryRecyclerViewAdapter = new OrderSummaryRecyclerViewAdapter();

        cartItemViewModel.getAllProducts().observe(this, cartItems -> {

            orderSummaryRecyclerViewAdapter.setCartItems(cartItems);
            _binding.checkoutRecyclerView.setAdapter(orderSummaryRecyclerViewAdapter);


            for (CartItem cartItem:cartItems) {
                total+=cartItem.getProduct().getPrice()*cartItem.getQuantity();
            }
            _binding.totalTextView.setText(String.format("Total: %s", total));

        });

        _binding.checkoutRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        _binding.checkoutRecyclerView.setHasFixedSize(true);


        _binding.creditCartButton.setOnClickListener(v -> {
            //cartItemViewModel.deleteAll();
            //navController.navigate(R.id.action_checkoutFragment_to_ProductFragment);
            PartialPaymentFragment showPopUp = PartialPaymentFragment.newInstance("Credit Card",total- discountTotal);
            showPopUp.show(getChildFragmentManager(),"PartialPayment");

        });

        _binding.cashButton.setOnClickListener(v -> {
            // cartItemViewModel.deleteAll();

            //navController.navigate(R.id.action_checkoutFragment_to_ProductFragment);
            PartialPaymentFragment showPopUp = PartialPaymentFragment.newInstance("Cash",total- discountTotal);
            showPopUp.show(getChildFragmentManager(),"PartialPayment");
        });

        _binding.qrButton.setOnClickListener(v -> {

            /*
            Bundle bundle = new Bundle();
            qrCodeData = generateQRData();
            bundle.putString("qrCodeData",qrCodeData);
            QRFragment showPopUp = new QRFragment();
            showPopUp.setArguments(bundle);

            showPopUp.show(getChildFragmentManager(),"QR");
             */
            PartialPaymentFragment showPopUp = PartialPaymentFragment.newInstance("QR",total- discountTotal);
            showPopUp.show(getChildFragmentManager(),"PartialPayment");

        });
        super.onCreate(savedInstanceState);

    }

    public void setDiscount(float discount, String discountType){
        switch (discountType){
            case "Cash":
                this.discountCash += discount;
                break;
            case "Credit Card":
                this.discountCredit += discount;
                break;
            case "QR":
                this.discountQR += discount;
                break;
        }
        this.discountTotal = this.discountCash + this.discountCredit + this.discountQR;
        _binding.discountTextView.setText(String.format("-%s", this.discountTotal));
    }

    private String generateQRData(){
        StringBuilder qrCodeDataBuilder = new StringBuilder();
        cartItemViewModel.getAllProducts().observe(this, cartItems -> {
            for (CartItem cartItem:cartItems) {
                qrCodeDataBuilder.append(cartItem.getProduct().getProductName()).append("(").append(cartItem.getProduct().getPrice()).append(" TL) ").append(cartItem.getQuantity()).append("tane. Toplam: ").append(cartItem.getProduct().getPrice()*cartItem.getQuantity()).append(" TL\n");
            }
            qrCodeDataBuilder.append(_binding.totalTextView.getText().toString());

        });
        return qrCodeDataBuilder.toString();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return _binding.getRoot();
    }
}