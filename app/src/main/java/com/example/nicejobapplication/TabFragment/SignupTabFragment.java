package com.example.nicejobapplication.TabFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nicejobapplication.R;
import com.example.nicejobapplication.authentication.LoginSignup;
import com.example.nicejobapplication.authentication.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Pattern;

public class SignupTabFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private FirebaseFirestore db;

    private Button btn_signup;
    private EditText signup_name;
    private EditText signup_email;
    private EditText signup_password;
    private EditText signup_confirm_password;

    private static final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_tab, container, false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        db = FirebaseFirestore.getInstance();

        signup_name = view.findViewById(R.id.signup_name);
        signup_email = view.findViewById(R.id.signup_email);
        signup_password = view.findViewById(R.id.signup_password);
        signup_confirm_password = view.findViewById(R.id.signup_confirm_password);
        btn_signup = view.findViewById(R.id.btn_signup);

        btn_signup.setOnClickListener(v -> validationInfo());

        return view;
    }

    private void validationInfo() {
        String name = signup_name.getText().toString();
        String email = signup_email.getText().toString();
        String password = signup_password.getText().toString();
        String confirm_password = signup_confirm_password.getText().toString();

        if (name.isEmpty() && email.isEmpty() && password.isEmpty() && confirm_password.isEmpty()) {
            if (name.isEmpty()) {
                signup_name.setError("Enter your name");
            }
            if (email.isEmpty()) {
                signup_email.setError("Enter your email");
            }
            if (password.isEmpty()) {
                signup_password.setError("Enter your password");
            }
            if (confirm_password.isEmpty()) {
                signup_confirm_password.setError("Re Enter your password");
            }
            Toast.makeText(getActivity(), "Please enter valid details", Toast.LENGTH_SHORT).show();
        } else if (!Pattern.matches(EMAIL_PATTERN, email)) {
            signup_email.setError("Enter valid email address");
            Toast.makeText(getActivity(), "Please enter valid email address", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6) {
            signup_password.setError("Enter password more than 6 characters");
            Toast.makeText(getActivity(), "Please enter password more than 6 characters", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirm_password)) {
            signup_confirm_password.setError("Password not matched, try again");
            Toast.makeText(getActivity(), "Password not matched, try again", Toast.LENGTH_SHORT).show();
        } else {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (auth.getCurrentUser() != null) {
                        auth.getCurrentUser().sendEmailVerification()
                                .addOnSuccessListener(aVoid -> {
                                    String userId = auth.getCurrentUser().getUid();
                                    Users users = new Users(userId, name, email, password);

                                    db.collection("users").document(userId).set(users).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Intent i = new Intent(getActivity(), LoginSignup.class);
                                            startActivity(i);
                                            Toast.makeText(getActivity(), "Signup successfully, Please verify your Email !", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), "Something went wrong, try again", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                })
                                .addOnFailureListener(e -> Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show());
                    }
                } else {
                    task.addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }
    }
}

