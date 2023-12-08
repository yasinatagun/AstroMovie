package com.yasinatagun.astromovie.view.loginFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yasinatagun.astromovie.R;
import com.yasinatagun.astromovie.databinding.FragmentLoginBinding;
import com.yasinatagun.astromovie.view.MainActivity;

public class LoginFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    String writtenEmail;
    String writtenPassword;
    FirebaseUser currentUser;
    FirebaseFirestore firebaseFirestore;
    private FragmentLoginBinding binding;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

        binding.textForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goForgotFragment(view);
            }
        });

        binding.bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginClicked(v);
            }
        });

        binding.bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerClicked(v);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void loginClicked(View view) {
        writtenPassword = binding.etPassword.getText().toString();
        writtenEmail = binding.etEmail.getText().toString();
        if (writtenEmail.isEmpty() || writtenPassword.isEmpty()) {
            Toast.makeText(getContext(), "Enter Email and Password", Toast.LENGTH_LONG).show();
        } else {
            firebaseAuth.signInWithEmailAndPassword(writtenEmail, writtenPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                    getContext().getSharedPreferences("default", Context.MODE_PRIVATE).edit().putString("userEmail", writtenEmail).apply();
                    getActivity().finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void registerClicked(View view) {
        writtenPassword = binding.etPassword.getText().toString();
        writtenEmail = binding.etEmail.getText().toString();
        if (writtenEmail.isEmpty() || writtenPassword.isEmpty()) {
            Toast.makeText(getContext(), "Enter Email And Password", Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.createUserWithEmailAndPassword(writtenEmail, writtenPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    getContext().getSharedPreferences("default", Context.MODE_PRIVATE).edit().putString("userEmail", writtenEmail).apply();
                    startActivity(intent);
                    getActivity().finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void goForgotFragment(View view) {
        writtenEmail = binding.etEmail.getText().toString();
        LoginFragmentDirections.ActionLoginFragmentToForgotFragment action = LoginFragmentDirections.actionLoginFragmentToForgotFragment(writtenEmail);
        Navigation.findNavController(view).navigate((NavDirections) action);
    }
}