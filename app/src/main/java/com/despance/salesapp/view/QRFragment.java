package com.despance.salesapp.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        String qrCodeData = null;
        if (bundle != null) {
            qrCodeData = bundle.getString("qrCodeData");
        }
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

    private Bitmap generateQR(String source){

        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(source, BarcodeFormat.QR_CODE, 1024, 1024);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();

            return barcodeEncoder.createBitmap(matrix);

        } catch (WriterException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return _binding.getRoot();
    }
}