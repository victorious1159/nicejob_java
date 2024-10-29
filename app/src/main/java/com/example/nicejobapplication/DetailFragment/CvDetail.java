package com.example.nicejobapplication.DetailFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.example.nicejobapplication.databinding.FragmentCvDetailBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.Objects;

public class CvDetail extends Fragment {

    private FragmentCvDetailBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCvDetailBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();

        // Nhận Bundle từ Fragment trước
        Bundle bundle = getArguments();
        String documentID = Objects.requireNonNull(bundle).getString("cvName");
        String userEmail = Objects.requireNonNull(auth.getCurrentUser()).getEmail();

        db = FirebaseFirestore.getInstance();
        db.collection("created_cv").document(Objects.requireNonNull(userEmail))
                .collection(userEmail).document(Objects.requireNonNull(documentID))
                .get()
                .addOnSuccessListener(document -> {
                    Map<String, Object> data = document.getData();
                    String cvName = data.get("cvName").toString();
                    String avatar = data.get("avatar").toString();
                    String employerName = data.get("employerName").toString();
                    String email = data.get("email").toString();
                    String phoneNumber = data.get("phoneNumber").toString();
                    String gentle = data.get("gender").toString();
                    String address = data.get("address").toString();
                    String dayOfBirth = data.get("dayOfBirth").toString();
                    String careerGoal = data.get("careerGoal").toString();
                    String workExperience = data.get("workExperience").toString();
                    String academicLevel = data.get("academicLevel").toString();

                    binding.txtCvNameView.setText(cvName);
                    Glide.with(requireActivity()).load(avatar).into(binding.avtViewCV);
                    binding.txtViewNameEmployee.setText(employerName);
                    binding.txtViewEmail.setText(email);
                    binding.txtViewPhone.setText(phoneNumber);
                    binding.txtViewGender.setText(gentle);
                    binding.txtViewAddress.setText(address);
                    binding.txtViewBirthday.setText(dayOfBirth);
                    binding.txtViewCareerGoal.setText(careerGoal);
                    binding.txtViewExp.setText(workExperience);
                    binding.txtViewAcademicLevel.setText(academicLevel);
                })
                .addOnFailureListener(e -> Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show());

        return binding.getRoot();
    }
}
