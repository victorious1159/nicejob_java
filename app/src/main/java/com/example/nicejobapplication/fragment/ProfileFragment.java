package com.example.nicejobapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.nicejobapplication.R;
import com.example.nicejobapplication.authentication.LoginSignup;
import com.example.nicejobapplication.databinding.FragmentProfileBinding;
import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private FirebaseAuth firebaseAuth;
    private AccessToken accessToken;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        accessToken = AccessToken.getCurrentAccessToken();

        if (accessToken != null && !accessToken.isExpired()) {
            FragmentTransaction transaction = requireFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new LoginFacbookProfileFragment()).commit();
        } else {
            if (firebaseUser == null) {
                binding.btnAccountLogin.setOnClickListener(v -> {
                    Intent i = new Intent(getActivity(), LoginSignup.class);
                    startActivity(i);
                });
            } else {
                FragmentTransaction transaction = requireFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new LoginProfileFragment()).commit();
            }
        }

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}