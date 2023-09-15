package com.despance.salesapp.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.despance.salesapp.R;
import com.despance.salesapp.databinding.FragmentReceiptRecievedBinding;

public class ReceiptRecievedFragment extends Fragment {

    private FragmentReceiptRecievedBinding _binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _binding = FragmentReceiptRecievedBinding.inflate(getLayoutInflater());
        _binding.goBackToMenuButton.setOnClickListener(v -> {
            Navigation.findNavController(requireActivity(),R.id.nav_host_fragment_content_main).navigate(R.id.action_receiptRecievedFragment_to_ProductFragment);

        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return _binding.getRoot();
    }
}