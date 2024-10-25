package com.example.nicejobapplication.DetailFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.nicejobapplication.R;
import com.example.nicejobapplication.databinding.FragmentJobDetailBinding;
import com.example.nicejobapplication.modal.Jobs;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class JobDetail extends Fragment {

    private FirebaseFirestore db;
    private FragmentJobDetailBinding binding;
    private NavController navController;
    private FirebaseAuth auth;
    private Bundle bundleCorp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        navController = NavHostFragment.findNavController(this);
        binding = FragmentJobDetailBinding.inflate(inflater, container, false);
        auth = FirebaseAuth.getInstance();

        String userEmail = auth.getCurrentUser() != null ? auth.getCurrentUser().getEmail() : null;
        Bundle bundle = getArguments();
        String documentID = bundle != null ? bundle.getString("documentID") : null;

        if (documentID != null) {
            if (userEmail != null) {
                isApplied(userEmail, documentID);
            } else {
                binding.extendedFab.setVisibility(View.INVISIBLE);
            }

            getDataFromFireStore(documentID);
            binding.extendedFab.setOnClickListener(v -> navController.navigate(R.id.action_jobDetail_to_chooseCVApplication, bundle));

            binding.corpLogo.setOnClickListener(v -> navController.navigate(R.id.action_jobDetail_to_corporationDetail, bundleCorp));
            binding.corpName.setClickable(true);
            binding.corpName.setOnClickListener(v -> navController.navigate(R.id.action_jobDetail_to_corporationDetail, bundleCorp));

        } else {
            Snackbar.make(binding.getRoot(), "Không tìm thấy thông tin người dùng", Snackbar.LENGTH_LONG).show();
        }

        binding.topAppBar.setNavigationOnClickListener(v -> navController.popBackStack());

        return binding.getRoot();
    }

    private void isApplied(String employeeEmail, String jobId) {
        db = FirebaseFirestore.getInstance();
        db.collection("applications").whereEqualTo("employeeId", employeeEmail).whereEqualTo("jobId", jobId).get()
                .addOnSuccessListener(queryDocumentSnapshots -> binding.extendedFab.setEnabled(queryDocumentSnapshots.isEmpty()));
    }

    private void getDataFromFireStore(String documentID) {
        db = FirebaseFirestore.getInstance();
        db.collection("jobs").document(documentID).get().addOnSuccessListener(document -> {
            Jobs job = new Jobs(
                    document.getId(),
                    document.getString("jobTitle"),
                    document.getString("corpId"),
                    Integer.parseInt(document.get("numOfRecruit").toString()),
                    Integer.parseInt(document.get("genderJob").toString()),
                    new String[]{document.getString("workAddress")},
                    0,
                    Integer.parseInt(document.get("expId").toString()),
                    Integer.parseInt(document.get("wayToWorkId").toString()),
                    Integer.parseInt(document.get("salaryId").toString()),
                    Integer.parseInt(document.get("levelId").toString()),
                    0,
                    Timestamp.now(),
                    document.getTimestamp("deadline"),
                    new String[]{document.getString("jobDescription")},
                    new String[]{document.getString("recruitRequire")},
                    new String[]{document.getString("benefit")}
            );

            binding.jobName.setText(job.getJobName());
            binding.salary.setText(job.getSalary(job.getSalaryId()));
            binding.wayToWork.setText(job.getWayToWork(job.getWayToWorkId()));
            binding.numOfRecruit.setText(String.valueOf(job.getNumOfRecruit()));
            binding.genderRequire.setText(job.getGenderRequire(job.getGenderJob()));
            binding.exp.setText(job.getExp(job.getExpId()));
            binding.level.setText(job.getLevel(job.getLevelId()));
            binding.address.setText(job.displayWorkAddress(job.getWorkAddress()));
            binding.jobDescription.setText(job.displayJobDescription(job.getJobDescription()));
            binding.recruitRequire.setText(job.displayRecruitRequire(job.getRecruitRequire()));
            binding.benefit.setText(job.displayBenefit(job.getBenefit()));

            String corpId = document.getString("corpId");
            db.collection("corporations").document(corpId).get().addOnSuccessListener(corp -> {
                String corpLogoUrl = corp.getString("corpLogo");
                binding.corpName.setText(corp.getString("corpName"));
                String urlImage = "gs://nicejob2-ddaa1.appspot.com/corporation_image/" + corpLogoUrl;
                StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(urlImage);
                storageRef.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(requireContext()).load(uri).into(binding.corpLogo));
            });

            bundleCorp = new Bundle();
            bundleCorp.putString("documentID", corpId);
        });
    }
}
