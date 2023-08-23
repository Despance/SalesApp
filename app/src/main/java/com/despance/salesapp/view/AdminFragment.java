package com.despance.salesapp.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.despance.salesapp.R;
import com.despance.salesapp.data.User;
import com.despance.salesapp.viewModal.ProductViewModel;

public class AdminFragment extends Fragment {

    private User.DatabaseHelper dbHelper;
    private ProductViewModel productViewModel;
    public AdminFragment() {
        // Required empty public constructor
    }

    public static AdminFragment newInstance(String param1, String param2) {
        AdminFragment fragment = new AdminFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        productViewModel = new ProductViewModel(getActivity().getApplication());

        dbHelper = new User.DatabaseHelper(getContext());



        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin, container, false);
    }
}