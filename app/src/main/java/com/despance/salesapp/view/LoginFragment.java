package com.despance.salesapp.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.despance.salesapp.MainActivity;
import com.despance.salesapp.R;
import com.despance.salesapp.data.User;
import com.despance.salesapp.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding _binding;
    private String _email;
    private String _password;

    private User.DatabaseHelper dbHelper;





    public LoginFragment() {
        // Required empty public constructor
    }


    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        _binding = FragmentLoginBinding.inflate(getLayoutInflater());

        dbHelper = new User.DatabaseHelper(getContext());

        //dbHelper.addUser(new User("despance1927@gmail.com", "123456", "Mustafa Emir", "Uyar"));
        //dbHelper.addUser(new User("abc", "123456", "Mustafa Emir", "Uyar"));
        for (User user : dbHelper.getAllUsers()) {
            Log.d("USER","Id:"+user.getId()+ "Email: " + user.getEmail() + " Password: " + user.getPassword() + " First Name: " + user.getFirstName() + " Last Name: " + user.getLastName());
        }

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d( "LOGIN", "Login fragment created");

        _binding.loginButton.setOnClickListener(view -> {
            Log.println(Log.DEBUG, "LOGIN", "Login button clicked");
            _email = _binding.emailTextView.getEditText().getText().toString();
            _password = _binding.passwordTextView.getEditText().getText().toString();
            //_binding.passwordTextView.getEditText().onEditorAction(EditorInfo.IME_ACTION_DONE);
            _binding.passwordTextView.getEditText().setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    _binding.loginButton.performClick();
                    return true;
                }
                return false;
            });


            if(_email.isEmpty()){
                _binding.emailTextView.setError("Email is required");
                Log.e("LOGIN", "Email is required");


            }
            if (_password.isEmpty()){
                _binding.passwordTextView.setError("Password is required");
                Log.e("LOGIN", "Password is required");

            }
            if (_password.length() < 6){
                _binding.passwordTextView.setError("Password must be at least 6 characters");
                Log.e("LOGIN", "Password must be at least 6 characters");

            }
            else{
                _binding.emailTextView.setError(null);
                _binding.passwordTextView.setError(null);

                User loginUser =dbHelper.findUser(_email, _password);

                if (loginUser != null){
                    Log.e("LOGIN", "Login successful");
                    NavController navController = Navigation.findNavController(view);
                    Bundle bundle = new Bundle();
                    bundle.putString("username", loginUser.getFirstName());
                    bundle.putInt("id", loginUser.getId());
                    navController.navigate(R.id.login_to_product, bundle);
                }else{
                    Log.e("LOGIN", "Login failed");
                    _binding.passwordTextView.setError("Email or password is incorrect");

                }

            }




        });
        // Inflate the layout for this fragment
        return _binding.getRoot();
    }


}