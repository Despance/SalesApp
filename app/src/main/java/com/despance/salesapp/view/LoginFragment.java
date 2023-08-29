package com.despance.salesapp.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.despance.salesapp.R;
import com.despance.salesapp.data.User;
import com.despance.salesapp.databinding.FragmentLoginBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding _binding;
    private String _email;
    private String _password;
    private User.DatabaseHelper dbHelper;
    private PrintWriter out;
    private BufferedReader in;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        _binding = FragmentLoginBinding.inflate(getLayoutInflater());
        dbHelper = new User.DatabaseHelper(getContext());

        //dbHelper.addUser(new User("despance1927@gmail.com", "123456", "Mustafa Emir", "Uyar"))
        //dbHelper.addUser(new User("abc", "123456", "Mustafa Emir", "Uyar"));

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        _binding.passwordTextView.getEditText().setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                _binding.loginButton.performClick();
                return true;
            }
            return false;
        });

        _binding.connectDBButton.setOnClickListener(v -> {

            Thread thread = new Thread(() -> {
                String message = connectToDatabase("192.168.50.3",25565);
                getActivity().runOnUiThread(() -> Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show());

            });
            thread.start();
        });
        _binding.loginButton.setOnClickListener(view -> onLoginClick(view));

        
        

        return _binding.getRoot();
    }

    private void onLoginClick(View view) {
        _email = _binding.emailTextView.getEditText().getText().toString();
        _password = _binding.passwordTextView.getEditText().getText().toString();


        if(_email.isEmpty())
            _binding.emailTextView.setError("Email is required");
        if (_password.isEmpty())
            _binding.passwordTextView.setError("Password is required");
        if (_password.length() < 6)
            _binding.passwordTextView.setError("Password must be at least 6 characters");

        else{
            _binding.emailTextView.setError(null);
            _binding.passwordTextView.setError(null);
            User loginUser =dbHelper.findUser(_email, _password);

            if (loginUser != null){
                NavController navController = Navigation.findNavController(view);
                Bundle bundle = new Bundle();

                bundle.putString("username", loginUser.getFirstName());
                bundle.putInt("id", loginUser.getId());

                navController.navigate(R.id.login_to_product, bundle);
            }else
                _binding.passwordTextView.setError("Email or password is incorrect");


        }
    }

    private String connectToDatabase(String address, int port) {
        Socket socket = null;
        try {
            socket = new Socket(address, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


}