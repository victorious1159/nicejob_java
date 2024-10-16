package com.example.nicejobapplication.TabFragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import com.example.nicejobapplication.LoginFacebook;
import com.example.nicejobapplication.MainActivity;
import com.example.nicejobapplication.R;
import com.example.nicejobapplication.databinding.FragmentLoginTabBinding;
import com.google.firebase.auth.FirebaseAuth;

public class LoginTabFragment extends Fragment {

    private FirebaseAuth auth;
    private FragmentLoginTabBinding binding;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginTabBinding.inflate(getLayoutInflater());

        auth = FirebaseAuth.getInstance();

        binding.imgBtnLoginGoogle.setOnClickListener(v -> {
            // Google login implementation
        });

        binding.imgBtnLoginFB.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), LoginFacebook.class));
        });

        binding.loginPassword.setAutofillHints(View.AUTOFILL_HINT_PASSWORD);

        binding.btnLogin.setOnClickListener(v -> {
            validationInfo();
        });

        binding.txtForgotPass.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View view = getLayoutInflater().inflate(R.layout.dialog_forgot, null);
            EditText userEmail = view.findViewById(R.id.edtBox);

            builder.setView(view);
            AlertDialog dialog = builder.create();

            view.findViewById(R.id.btnReset).setOnClickListener(v1 -> {
                compareEmail(userEmail);
                dialog.dismiss();
            });
            view.findViewById(R.id.btnCancel).setOnClickListener(v1 -> {
                dialog.dismiss();
            });

            dialog.show();
        });

        return binding.getRoot();
    }

    private void compareEmail(EditText email) {
        if (email.getText().toString().isEmpty()) {
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            return;
        }
        auth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getActivity(), "Check your email", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validationInfo() {
        String email = binding.loginEmail.getText().toString().trim();
        String password = binding.loginPassword.getText().toString().trim();

        if (email.isEmpty() && password.isEmpty()) {
            if (email.isEmpty()) {
                binding.loginEmail.setError("Enter your email address");
            }
            if (password.isEmpty()) {
                binding.loginPassword.setError("Enter your password");
            }
            Toast.makeText(getActivity(), "Please enter valid details", Toast.LENGTH_SHORT).show();
        } else if (!email.matches(emailPattern)) {
            Toast.makeText(getActivity(), "Please enter valid email address", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6) {
            Toast.makeText(getActivity(), "Please enter password more than 6 characters", Toast.LENGTH_SHORT).show();
        } else {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Boolean verification = auth.getCurrentUser().isEmailVerified();
                    if (verification) {
                        Intent i = new Intent(getActivity(), MainActivity.class);
                        startActivity(i);
                        Toast.makeText(getActivity(), "Login successfully !", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Please verify your Email !", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Something went wrong, try again", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

