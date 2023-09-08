package com.despance.salesapp.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.despance.salesapp.R;
import com.despance.salesapp.data.Payment;
import com.despance.salesapp.data.TLVObject;
import com.despance.salesapp.databinding.FragmentPartialPaymentBinding;
import com.despance.salesapp.utils.TLVUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Date;


public class PartialPaymentFragment extends BottomSheetDialogFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String paymentType;
    private float remainingAmount;

    FragmentPartialPaymentBinding _binding;



    public PartialPaymentFragment() {
        // Required empty public constructor
    }

    public static PartialPaymentFragment newInstance(String paymentType, float remainingAmount) {
        PartialPaymentFragment fragment = new PartialPaymentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, paymentType);
        args.putFloat(ARG_PARAM2, remainingAmount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            paymentType = getArguments().getString(ARG_PARAM1);
            remainingAmount = getArguments().getFloat(ARG_PARAM2);
        }
        _binding = FragmentPartialPaymentBinding.inflate(getLayoutInflater());
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return _binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _binding.previousTotalTextView.setText(String.format("Remaining Cart Total: %s", remainingAmount));
        _binding.paymentMethodTextView.setText(String.format("Payment Method: %s", paymentType));
        _binding.partialAmountTextView.setEndIconOnClickListener(v->{
            _binding.partialAmountTextView.getEditText().setText(String.valueOf(remainingAmount));
        });


        _binding.partialAmountTextView.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus)
                _binding.partialAmountTextView.getEditText().setHint(String.format("Remaining Amount: %s", remainingAmount));
        });


        _binding.partialAmountTextView.getEditText().addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        String partialAmountText = _binding.partialAmountTextView.getEditText().getText().toString();
                        _binding.partialAmountTextView.setError(null);
                        _binding.payButton.setEnabled(true);
                        float partialAmount = 0;
                        if(!partialAmountText.isEmpty()){
                            try {
                                partialAmount = Float.parseFloat(partialAmountText);

                            }catch (NumberFormatException e){
                                _binding.partialAmountTextView.setError("Invalid Amount");
                                _binding.payButton.setEnabled(false);
                            }
                        }

                        if (partialAmount <= remainingAmount)
                            _binding.remaningAmountTextView.setText(String.format("Remaining Amount: %s", remainingAmount - partialAmount));
                        else{
                            _binding.payButton.setEnabled(false);
                            _binding.partialAmountTextView.setError("Partial amount cannot be greater than remaining amount");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                }
        );
        _binding.payButton.setOnClickListener(v->{

            if(_binding.partialAmountTextView.getEditText().getText().toString().isEmpty()){
                _binding.partialAmountTextView.setError("Partial amount cannot be empty");
                return;
            }


            if(paymentType.equals("Cash")){
                Fragment fm =getParentFragment();
                if(fm instanceof CheckoutFragment)
                    ((CheckoutFragment) fm).setDiscount(Float.parseFloat(_binding.partialAmountTextView.getEditText().getText().toString()), paymentType);
                dismiss();
                return;
            }

            Payment payment = new Payment(paymentType,Float.parseFloat(_binding.partialAmountTextView.getEditText().getText().toString())+"TL "+paymentType+" transcation requested.",Float.parseFloat(_binding.partialAmountTextView.getEditText().getText().toString()), new Date(System.currentTimeMillis()).toString());

            byte[] tlv = TLVUtils.encode(payment);



            Thread thread = new Thread(() -> {
                sendPaymentRequest(CheckoutFragment.paymentServerIp,CheckoutFragment.paymentServerPort,tlv );
            });

            thread.start();

        });
    }


    private void sendPaymentRequest(String ip,int port, byte[] tlv){
        getActivity().runOnUiThread(()->{
        _binding.progressBar.setVisibility(View.VISIBLE);
        _binding.payButton.setClickable(false);
        _binding.payButton.setFocusable(false);
        _binding.partialAmountTextView.setClickable(false);
        _binding.partialAmountTextView.setFocusable(false);
        _binding.partialAmountTextView.getEditText().setFocusable(false);
        _binding.partialAmountTextView.getEditText().setClickable(false);
        _binding.partialAmountTextView.getEditText().clearFocus();
        _binding.partialAmountTextView.setEndIconOnClickListener(null);
        _binding.partialAmountTextView.clearFocus();
        this.setCancelable(false);
        });

        try(Socket socket = new Socket()){
            Fragment fm =getParentFragment();
            if (!(fm instanceof CheckoutFragment))
                return;

            socket.connect(new InetSocketAddress(ip,port),10000);
            socket.getOutputStream().write(tlv);
            socket.getOutputStream().flush();


            byte[] response = new byte[1];
            socket.getInputStream().read(response,0,1);
            socket.close();
            if(response[0] == '1'){
                getActivity().runOnUiThread(()-> {
                    dismiss();
                    ((CheckoutFragment) fm).setDiscount(Float.parseFloat(_binding.partialAmountTextView.getEditText().getText().toString()), paymentType);
               });

            }else if (response[0] == '0'){
                getActivity().runOnUiThread(()->{
                    Toast.makeText(getContext(),"Payment Declined",Toast.LENGTH_SHORT).show();
                    dismiss();
                });
            }else {
                getActivity().runOnUiThread(()->{
                    Toast.makeText(getContext(),"Unknown Response from Payment: "+ response[0],Toast.LENGTH_SHORT).show();
                    dismiss();
                });
            }


        }catch (Exception e){
            getActivity().runOnUiThread(()->{
                Toast.makeText(getContext(),"Request timeout",Toast.LENGTH_SHORT).show();
                dismiss();
            });
        }


    }


}