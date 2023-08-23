package com.despance.salesapp.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.despance.salesapp.R;
import com.despance.salesapp.databinding.FragmentQRBinding;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRFragment extends DialogFragment {


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        _binding = FragmentQRBinding.inflate(getLayoutInflater());
        Bundle bundle = getArguments();
        String qrCodeData = bundle.getString("qrCodeData");

        if(qrCodeData != null)
            qrBitmap = generateQR(qrCodeData);

        _binding.qrCodeView.setImageBitmap(qrBitmap);

        return new AlertDialog.Builder(getActivity())
                .setTitle("QR Code")
                .setView(_binding.getRoot())
                .setPositiveButton("OK", null)
                .create();
    }

    private Bitmap qrBitmap;
    private FragmentQRBinding _binding;
    public QRFragment() {
        // Required empty public constructor

    }

    public static QRFragment newInstance(String param1, String param2) {
        QRFragment fragment = new QRFragment();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    private Bitmap generateQR(String source){

        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(source, BarcodeFormat.QR_CODE, 1000, 1000);

            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(matrix);

            return bitmap;

        } catch (WriterException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return _binding.getRoot();
    }
}