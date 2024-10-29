package com.techietech.haryanagkinhindienglish;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.techietech.haryanagkinhindienglish.activities.MainActivity;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {


    private TextView dontHaveAnAccount,tvskip, forgotpassword;
    private FrameLayout parentFrameLayout;
    private Button btnLogin;
    private EditText etEmail,etPassword;
    private FirebaseAuth firebaseAuth;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    private ProgressBar progressBar;

    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        dontHaveAnAccount = view.findViewById(R.id.textView_dont_haveAccount);
        parentFrameLayout = requireActivity().findViewById(R.id.register_frameLayout);
        btnLogin = view.findViewById(R.id.button_login);
        etEmail = view.findViewById(R.id.editText_email);
        etPassword = view.findViewById(R.id.editText_password);
        tvskip = view.findViewById(R.id.textView_skip);
        forgotpassword = view.findViewById(R.id.tv_forgot_password);
        progressBar = view.findViewById(R.id.progressBar_signin);
        firebaseAuth = FirebaseAuth.getInstance();
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dontHaveAnAccount.setOnClickListener(view1 -> setFragment(new SignUpFragment()));

        tvskip.setOnClickListener(view12 -> {
            tvskip.setEnabled(false);
            saveCurrentData();
            mainIntent();
        });

        forgotpassword.setOnClickListener(view13 -> setFragment(new ResetPasswordFragment()));

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnLogin.setOnClickListener(view14 -> checkEmailAndPassword());
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right,R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    private void checkInputs(){
        if (!TextUtils.isEmpty(etEmail.getText().toString())){
            if (!TextUtils.isEmpty(etPassword.getText().toString())){
                btnLogin.setEnabled(true);
                btnLogin.setTextColor(getResources().getColor(R.color.colorWhite));
            } else{
                btnLogin.setEnabled(false);
                btnLogin.setTextColor(getResources().getColor(R.color.buttonBorder));
            }
        } else {
            btnLogin.setEnabled(false);
            btnLogin.setTextColor(getResources().getColor(R.color.buttonBorder));
        }
    }

    private void checkEmailAndPassword(){
        if (etEmail.getText().toString().matches(emailPattern)){
            if (etPassword.length() >= 8 ){

                progressBar.setVisibility(View.VISIBLE);
                btnLogin.setEnabled(false);
                btnLogin.setTextColor(getResources().getColor(R.color.buttonBorder));


                firebaseAuth.signInWithEmailAndPassword(etEmail.getText().toString(),etPassword.getText().toString())
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()){
                                    saveCurrentData();
                                    mainIntent();
                                } else {
                                    btnLogin.setEnabled(true);
                                    btnLogin.setTextColor(getResources().getColor(R.color.colorWhite));
                                    progressBar.setVisibility(View.INVISIBLE);
                                    String error = Objects.requireNonNull(task.getException()).getMessage();
                                    Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                }
                            });

            } else {
                etPassword.setError(getString(R.string.incorrect_password));
            }
        } else {
            etEmail.setError(getString(R.string.incorrect_password));
        }
    }

    private void mainIntent(){
        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
        startActivity(mainIntent);
        requireActivity().finish();
    }

    private void saveCurrentData(){
        SharedPreferences cLSSData = requireActivity().getSharedPreferences("cLSSDataValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor cLSSDataEditor = cLSSData.edit();
        cLSSDataEditor.putString("LoginSignStatus","on");
        cLSSDataEditor.apply();
    }

}
