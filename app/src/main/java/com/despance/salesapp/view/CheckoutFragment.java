package com.despance.salesapp.view;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import com.despance.salesapp.data.Receipt;
import com.despance.salesapp.data.TLVObject;
import com.despance.salesapp.databinding.FragmentCheckoutBinding;
import com.despance.salesapp.modal.CartItem.CartItem;
import com.despance.salesapp.utils.TLVUtils;
import com.despance.salesapp.viewModel.CartItemViewModel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Date;
import java.util.ArrayList;

public class CheckoutFragment extends Fragment {

    public static String paymentServerIp = "192.168.50.2";
    public static int paymentServerPort = 25565;
    CartItemViewModel cartItemViewModel;
    private FragmentCheckoutBinding _binding;
    private String qrCodeData;
    private float total = 0;
    private float discountTotal,discountCash,discountCredit,discountQR = 0;

    ArrayList<CartItem> cartItemArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        _binding = FragmentCheckoutBinding.inflate(getLayoutInflater());
        NavController navController= Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
        cartItemViewModel = new ViewModelProvider(this).get(CartItemViewModel.class);
        OrderSummaryRecyclerViewAdapter orderSummaryRecyclerViewAdapter = new OrderSummaryRecyclerViewAdapter();

        cartItemViewModel.getAllProducts().observe(this, cartItems -> {
            cartItemArrayList = new ArrayList<>(cartItems);
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
        if(total - this.discountTotal == 0)
            generateReceipt();
    }

    private void generateReceipt(){
        _binding.cartProgressBar.setVisibility(View.VISIBLE);
        _binding.creditCartButton.setEnabled(false);
        _binding.cashButton.setEnabled(false);
        _binding.qrButton.setEnabled(false);

        Receipt receipt = new Receipt();
        receipt.setCashTotal(discountCash);
        receipt.setCreditTotal(discountCredit);
        receipt.setQrTotal(discountQR);
        receipt.setTimestamp(new Date(System.currentTimeMillis()).toString());

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.despance.salesapp", Context.MODE_PRIVATE);
        int loginId =  sharedPreferences.getInt("id",-1);
        receipt.setUserId(loginId);

        receipt.setCartItems(cartItemArrayList.toArray(new CartItem[cartItemArrayList.size()]));

        sendReceiptToServer(receipt);

    }

    @Deprecated
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
    private void sendReceiptToServer(Receipt receipt){
        Thread thread = new Thread(() -> {
            try(Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(paymentServerIp,paymentServerPort),10000);
                socket.getOutputStream().write(TLVUtils.encode(receipt));
                byte[] response = new byte[1];

                socket.getInputStream().read(response,0,1);
                socket.close();

                if(response[0] == '1'){
                    cartItemViewModel.deleteAll();
                    if(isAdded())
                        getActivity().runOnUiThread(() ->{
                            NavController navController= Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                            if(navController.getCurrentDestination().getId() == R.id.checkoutFragment){
                                navController.navigate(R.id.action_checkoutFragment_to_receiptRecievedFragment);
                            }
                        });

                }
                else if (response[0] == '0'){

                    requireActivity().runOnUiThread(() ->{
                        _binding.cartProgressBar.setVisibility(View.GONE);
                        _binding.creditCartButton.setEnabled(true);
                        _binding.cashButton.setEnabled(true);
                        _binding.qrButton.setEnabled(true);
                        Toast.makeText(getContext(),"Receipt denied",Toast.LENGTH_SHORT).show();
                    });
                }else{
                    requireActivity().runOnUiThread(() ->{
                        _binding.cartProgressBar.setVisibility(View.GONE);
                        _binding.creditCartButton.setEnabled(true);
                        _binding.cashButton.setEnabled(true);
                        _binding.qrButton.setEnabled(true);
                        Toast.makeText(getContext(),"Unknown response from Receipt: "+ response[0],Toast.LENGTH_SHORT).show();
                    });
                }

            } catch (IOException e) {
                requireActivity().runOnUiThread(() ->{
                    _binding.cartProgressBar.setVisibility(View.GONE);
                    _binding.creditCartButton.setEnabled(true);
                    _binding.cashButton.setEnabled(true);
                    _binding.qrButton.setEnabled(true);
                    Toast.makeText(getContext(),"Cannot connect to Payment Server",Toast.LENGTH_SHORT).show();
                });
            }

        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return _binding.getRoot();
    }
}