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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.techietech.haryanagkinhindienglish.activities.MainActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {


    public SignUpFragment() {
        // Required empty public constructor
    }

    private TextView tvsignin;
    private EditText etName,etEmail,etPassword,etConfirmPassword;
    private FrameLayout parentFrameLayout;
    private Button btn_Signup;
    private ProgressBar pBar_signup;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        tvsignin = view.findViewById(R.id.textView_signin_signup);
        etName = view.findViewById(R.id.editText_name_signup);
        etEmail = view.findViewById(R.id.editText_email_signup);
        etPassword = view.findViewById(R.id.editText_password_signup);
        etConfirmPassword = view.findViewById(R.id.editText_confirm_pwd_signup);
        parentFrameLayout = requireActivity().findViewById(R.id.register_frameLayout);
        btn_Signup = view.findViewById(R.id.button_signUp);
        pBar_signup = view.findViewById(R.id.progressBar_signup);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
        etConfirmPassword.addTextChangedListener(new TextWatcher() {
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


        tvsignin.setOnClickListener(view1 -> setFragment(new SignInFragment()));

        btn_Signup.setOnClickListener(view12 -> checkEmailAndPassword());
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    private void checkInputs(){
        if (!TextUtils.isEmpty(etEmail.getText())){
               if (!TextUtils.isEmpty(etName.getText())){
                   if (!TextUtils.isEmpty(etPassword.getText()) && etPassword.length() >= 8){
                        if (!TextUtils.isEmpty(etConfirmPassword.getText())){
                                btn_Signup.setEnabled(true);
                                btn_Signup.setTextColor(getResources().getColor(R.color.colorWhite));
                        } else {
                            btn_Signup.setEnabled(false);
                            btn_Signup.setTextColor(getResources().getColor(R.color.buttonBorder));
                        }
                   } else {
                       btn_Signup.setEnabled(false);
                       btn_Signup.setTextColor(getResources().getColor(R.color.buttonBorder));
                   }
               } else {
                   btn_Signup.setEnabled(false);
                   btn_Signup.setTextColor(getResources().getColor(R.color.buttonBorder));
               }
        } else {
            btn_Signup.setEnabled(false);
            btn_Signup.setTextColor(getResources().getColor(R.color.buttonBorder));

        }
    }

    private void checkEmailAndPassword(){
        if (etEmail.getText().toString().matches(emailPattern)){
            if (etPassword.getText().toString().equals(etConfirmPassword.getText().toString())){

                pBar_signup.setVisibility(View.VISIBLE);
                btn_Signup.setEnabled(false);
                btn_Signup.setTextColor(getResources().getColor(R.color.buttonBorder));

                firebaseAuth.createUserWithEmailAndPassword(etEmail.getText().toString(),etPassword.getText().toString())
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()){

                                    Map<Object,String> userdata = new HashMap<>();
                                    userdata.put("username",etName.getText().toString());

                                    firebaseFirestore.collection("USERS")
                                                     .add(userdata)
                                                     .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                         @Override
                                                         public void onComplete(@NonNull Task<DocumentReference> task) {
                                                             if (task.isSuccessful()){
                                                                saveCurrentData();
                                                                 mainIntent();

                                                             } else {
                                                                 btn_Signup.setEnabled(true);
                                                                 btn_Signup.setTextColor(getResources().getColor(R.color.colorWhite));
                                                                 pBar_signup.setVisibility(View.INVISIBLE);
                                                                 String error = Objects.requireNonNull(task.getException()).getMessage();
                                                                 Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                                             }
                                                         }
                                                     });


                                } else {
                                    btn_Signup.setEnabled(true);
                                    btn_Signup.setTextColor(getResources().getColor(R.color.colorWhite));
                                    pBar_signup.setVisibility(View.INVISIBLE);
                                    String error = Objects.requireNonNull(task.getException()).getMessage();
                                    Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                }
                            });
            } else {
                etConfirmPassword.setError(getString(R.string.does_not_match));
            }
        } else {
            etEmail.setError(getString(R.string.invalid_email));
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
