package com.techietech.haryanagkinhindienglish;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPasswordFragment extends Fragment {


    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    private EditText registeredEmail;
    private Button resetPassword_btn;
    private TextView goBack,emailIconText;
    private FrameLayout parentFrameLayout;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private ViewGroup emailIconContainer;
    private ImageView emailIcon;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        registeredEmail = view.findViewById(R.id.et_email_forgotpwd);
        resetPassword_btn = view.findViewById(R.id.btn_resetPassword);
        goBack = view.findViewById(R.id.tv_goback);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = view.findViewById(R.id.progressBar_forgotPwd);
        emailIconContainer= view.findViewById(R.id.forgot_pwd_icon_container);
        emailIconText = view.findViewById(R.id.tv_emailSuccesfullySent);
        emailIcon = view.findViewById(R.id.imageView_emailIcon);
        parentFrameLayout = requireActivity().findViewById(R.id.register_frameLayout);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registeredEmail.addTextChangedListener(new TextWatcher() {
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

        goBack.setOnClickListener(view1 -> setFragment(new SignInFragment()));

        resetPassword_btn.setOnClickListener(view12 -> {

            TransitionManager.beginDelayedTransition(emailIconContainer);
            emailIconText.setVisibility(View.GONE);

            TransitionManager.beginDelayedTransition(emailIconContainer);
            emailIcon.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            resetPassword_btn.setEnabled(false);
            resetPassword_btn.setTextColor(getResources().getColor(R.color.buttonBorder));
            firebaseAuth.sendPasswordResetEmail(registeredEmail.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                resetPassword_btn.setEnabled(false);
                                emailIconText.setText(getString(R.string.check_your_inbox_after));
                                emailIconText.setTextColor(getResources().getColor(R.color.colorAccent));
                                emailIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_email_black));
                                TransitionManager.beginDelayedTransition(parentFrameLayout);
                                emailIcon.setVisibility(View.VISIBLE);
                                emailIconText.setVisibility(View.VISIBLE);

                            } else {
                                resetPassword_btn.setEnabled(true);
                                resetPassword_btn.setTextColor(getResources().getColor(R.color.colorWhite));
                                String error = Objects.requireNonNull(task.getException()).getMessage();
                                emailIconText.setText(error);
                                emailIconText.setTextColor(getResources().getColor(R.color.colorRed));
                                TransitionManager.beginDelayedTransition(emailIconContainer);
                                emailIconText.setVisibility(View.VISIBLE);

                            }
                            progressBar.setVisibility(View.GONE);

                        }
                    });

        });

    }

    private void checkInputs(){
        if (TextUtils.isEmpty(registeredEmail.getText().toString())){
              resetPassword_btn.setEnabled(false);
              resetPassword_btn.setTextColor(getResources().getColor(R.color.buttonBorder));
        } else {
            resetPassword_btn.setEnabled(true);
            resetPassword_btn.setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

}
