package com.despance.salesapp.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.despance.salesapp.R;
import com.despance.salesapp.databinding.FragmentPartialPaymentBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


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
                        float partialAmount = 0;
                        if(!partialAmountText.isEmpty()){
                            try {
                                partialAmount = Float.parseFloat(partialAmountText);

                            }catch (NumberFormatException e){
                                _binding.partialAmountTextView.setError("Invalid Amount");
                            }
                        }

                        if (partialAmount <= remainingAmount)
                            _binding.remaningAmountTextView.setText(String.format("Remaining Amount: %s", remainingAmount - partialAmount));
                        else
                            _binding.partialAmountTextView.setError("Partial amount cannot be greater than remaining amount");
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                }
        );
        _binding.payButton.setOnClickListener(v->{
            Fragment fm =getParentFragment();
            if(fm instanceof CheckoutFragment){
                ((CheckoutFragment) fm).setDiscount(Float.parseFloat(_binding.partialAmountTextView.getEditText().getText().toString()), paymentType);
                dismiss();
            }
            else
                dismiss();

        });
    }
}