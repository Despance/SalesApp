package com.despance.salesapp.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.despance.salesapp.R;
import com.despance.salesapp.data.User;
import com.despance.salesapp.databinding.FragmentServerBinding;
import com.despance.salesapp.modal.Product.Product;
import com.despance.salesapp.viewModel.ProductViewModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;


public class ServerFragment extends DialogFragment {

    public enum TLVTag{
        ID, NAME, PRICE, VATRATE, BARCODE,FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, USER, PRODUCT,ARRAY
    }

    FragmentServerBinding _binding;
    AlertDialog alertDialog;

    ProductViewModel productViewModel;
    User.DatabaseHelper dbHelper;
    private PrintWriter out;
    private BufferedReader in;
    int type;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        _binding = FragmentServerBinding.inflate(getLayoutInflater());
        dbHelper = new User.DatabaseHelper(getContext());
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Server Connection")
                .setView(_binding.getRoot())
                .create();

        _binding.ipEditText.setText("192.168.50.3");
        _binding.portEditText.setText("25565");
        _binding.button.setOnClickListener(v -> {
            String ip = _binding.ipEditText.getText().toString();
            String port = _binding.portEditText.getText().toString();
            type = _binding.group.getCheckedRadioButtonId();
            if(type == -1){
                ((RadioButton)(_binding.group.getChildAt(_binding.group.getChildCount()-1))).setError("Please select a connection type");
                return;
            }else if(type == R.id.userRadioButton){
                type = 0;
            }else{
                type = 1;
            }
            if(ip.isEmpty()){
                _binding.ipEditText.setError("Please enter an IP address");
                return;
            }
            if(port.isEmpty()){
                _binding.portEditText.setError("Please enter a port number");
                return;
            }




            Thread thread = new Thread(() -> {
                try {
                    connectToServer(ip,Integer.parseInt(port),type);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            thread.start();



        });



        return alertDialog;
    }


    public void connectToServer(String ip,int port, int type){

        try (Socket socket = new Socket()){
            getActivity().runOnUiThread(() -> {
                _binding.progressBarServer.setVisibility(android.view.View.VISIBLE);
                _binding.button.setClickable(false);
                _binding.button.setFocusable(false);
                _binding.ipEditText.setClickable(false);
                _binding.ipEditText.setFocusable(false);
                _binding.ipEditText.clearFocus();
                _binding.portEditText.clearFocus();
                _binding.portEditText.setClickable(false);
                _binding.portEditText.setFocusable(false);
                _binding.group.setClickable(false);
                _binding.group.setFocusable(false);
                this.setCancelable(false);
                for(int i = 0; i<_binding.group.getChildCount(); i++){
                    _binding.group.getChildAt(i).setClickable(false);
                    _binding.group.getChildAt(i).setFocusable(false);
                }
            });

            SocketAddress socketAddress = new InetSocketAddress(ip,port);
            socket.connect(socketAddress, 5000);
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(type);
            out.flush();

            byte[] tagArray = new byte[2];
            socket.getInputStream().read(tagArray,0,2);
            byte[] lengthArray = new byte[2];
            int length = 4;
            socket.getInputStream().read(lengthArray,0,2);

            for (int i = 0; i<lengthArray.length; i++) {
                length += (lengthArray[i] & 0xFF)<< ((1-i) * 8);
            }

            byte[] bytes = new byte[length];
            bytes[0] = tagArray[0];
            bytes[1] = tagArray[1];
            bytes[2] = lengthArray[0];
            bytes[3] = lengthArray[1];
            socket.getInputStream().read(bytes,4,length-4);


            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02X ", b));
            }
            Log.d("SERVER", "connectToServer: "+sb.toString());

            if(type == 0)
                dbHelper.deleteAll();
            else if(type == 1)
                productViewModel.deleteAll();
            ArrayList<Object> objects = (ArrayList<Object>) decode(bytes);
            for (Object object: objects) {
                if(object instanceof User){
                    User user = (User) object;
                    dbHelper.addUser(user);
                    Log.d("SERVER", "connectToServer: "+user.toString());
                }else if(object instanceof Product){
                    Product product = (Product) object;
                    productViewModel.insert(product);
                    Log.d("SERVER", "connectToServer: "+product.toString());
                }
            }
            dbHelper.close();
            alertDialog.dismiss();
            if(this.getActivity()!=null)
                this.getActivity().runOnUiThread(() -> Toast.makeText(getContext(),"Request successful", Toast.LENGTH_SHORT).show());

        } catch (SocketTimeoutException e) {
            if( this.getActivity()!=null)
                this.getActivity().runOnUiThread(() -> Toast.makeText(getContext(),"Connection timed out", Toast.LENGTH_SHORT).show());
            this.dismiss();

        }catch (IOException e){
            e.printStackTrace();
        }

    }


    public static Object decode(byte[] bytes) throws UnsupportedEncodingException {
        int byte1 = bytes[0];
        int byte2 = bytes[1];
        int tagVal = (byte1<<8)+byte2;
        TLVTag tag = TLVTag.values()[tagVal];
        short length = (short)(bytes[2]<<8+bytes[3]);
        AtomicInteger currentIndex = new AtomicInteger(4);

        switch(tag){
            case ID:
                return Integer.parseInt(new String(bytes,4,length,"windows-1252"));
            case PRICE:
            case VATRATE:
                return Float.parseFloat(new String(bytes,4,length, "windows-1252"));
            case BARCODE:
            case NAME:
            case FIRST_NAME:
            case LAST_NAME:
            case EMAIL:
            case PASSWORD:
                return new String(bytes,4,length,"windows-1252");
            case USER:

                int userId = (int) decode(bytes, currentIndex);
                String userFirstName = (String) decode(bytes, currentIndex);
                String userLastName = (String) decode(bytes, currentIndex);
                String userEmail = (String) decode(bytes, currentIndex);
                String userPassword = (String) decode(bytes, currentIndex);
                return new User(userId, userFirstName, userLastName, userEmail, userPassword);
            case PRODUCT:
                int productId = (int) decode(bytes, currentIndex);
                String productName = (String) decode(bytes, currentIndex);
                float productPrice = (float) decode(bytes, currentIndex);
                float productVatRate = (float) decode(bytes, currentIndex);
                String productBarcode = (String) decode(bytes, currentIndex);
                return new Product(productId, productName, productPrice, productVatRate, productBarcode);
            case ARRAY:
                ArrayList<Object> childs = new ArrayList<>();
                while(currentIndex.get()<bytes.length)
                    childs.add(decode(bytes,currentIndex));

                return childs;
            default:
                return null;

        }


    }


    private static Object decode(byte[] bytes, AtomicInteger indexObj) throws UnsupportedEncodingException {
        int index = indexObj.get();
        TLVTag tag = TLVTag.values()[(bytes[index]<<8) + bytes[index+1]];
        short length = (short)((bytes[index+2]<<8) + bytes[index+3]);
        index+=4;
        indexObj.set(index+length);
        switch(tag){
            case ID:
                return Integer.parseInt(new String(bytes,index,length,"windows-1252"));
            case PRICE:
            case VATRATE:
                return Float.parseFloat(new String(bytes,index,length,"windows-1252"));
            case BARCODE:
            case NAME:
            case FIRST_NAME:
            case LAST_NAME:
            case EMAIL:
            case PASSWORD:
                return new String(bytes,index,length,"windows-1252");
            case USER:
                indexObj.set(index);
                int userId = (int) decode(bytes, indexObj);
                String userFirstName = (String) decode(bytes, indexObj);
                String userLastName = (String) decode(bytes, indexObj);
                String userEmail = (String) decode(bytes, indexObj);
                String userPassword = (String) decode(bytes, indexObj);
                return new User(userId, userEmail, userPassword, userFirstName,userLastName);
            case PRODUCT:
                indexObj.set(index);
                int productId = (int) decode(bytes, indexObj);
                String productName = (String) decode(bytes, indexObj);
                float productPrice = (float) decode(bytes, indexObj);
                float productVatRate = (float) decode(bytes, indexObj);
                String productBarcode = (String) decode(bytes, indexObj);
                return new Product(productId, productName, productPrice, productVatRate, productBarcode);
            default:
                return null;



        }

    }
}