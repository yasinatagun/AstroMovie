package com.yasinatagun.astromovie.view.loginFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.yasinatagun.astromovie.R;
import com.yasinatagun.astromovie.databinding.FragmentForgotBinding;

public class ForgotFragment extends Fragment {
    String emailAdress;
    private FragmentForgotBinding binding;
    public ForgotFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentForgotBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText emailEditText = view.findViewById(R.id.etResetEmail);
        Button button = view.findViewById(R.id.bBackLogin);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLoginFragment(view);
            }
        });

        if (getArguments() != null) {
            emailAdress = ForgotFragmentArgs.fromBundle(getArguments()).getEmail();
            emailEditText.setText(emailAdress);
        }


        binding.bResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPasswordClicked(v);
            }
        });

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void goLoginFragment(View view) {
        NavDirections action = ForgotFragmentDirections.actionForgotFragmentToLoginFragment();
        Navigation.findNavController(view).navigate(action);
    }
    // Password Resetleme Tuşuna Basıldığında Ne Olacak
    public void resetPasswordClicked(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = binding.etResetEmail.getText().toString();
        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("", "Email sent.");
                            Toast.makeText(getContext(), "RESET EMAIL SENT", Toast.LENGTH_SHORT).show();
                            goLoginFragment(view);
                        }
                    }
                });
    }
}