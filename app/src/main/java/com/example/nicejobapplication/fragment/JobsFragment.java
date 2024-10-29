package com.example.nicejobapplication.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nicejobapplication.R;
import com.example.nicejobapplication.adapter.JobsAdapter;
import com.example.nicejobapplication.adapter.OnItemClickListener;
import com.example.nicejobapplication.databinding.FragmentJobsBinding;
import com.example.nicejobapplication.modal.Jobs;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JobsFragment extends Fragment implements OnItemClickListener {
    private FragmentJobsBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference dbRef;
    private FirebaseDatabase database;
    private ArrayList<Jobs> newestJobList;
    private ArrayList<Jobs> internshipJobList;
    private NavController navController;
    private FirebaseFirestore db;
    private RecyclerView rvJobs;
    private Bundle bundle;
    private static final Logger logger = Logger.getLogger(JobsFragment.class.getName());

    //    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentJobsBinding.inflate(inflater, container, false);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
        db = FirebaseFirestore.getInstance();
        navController = NavHostFragment.findNavController(this);

        // Get the current user's email
        String userEmail = auth.getCurrentUser() != null ? auth.getCurrentUser().getEmail() : null;

        // Load user data from Facebook or Firebase
        loadUserProfile();

        // Get the current timestamp
        Timestamp currentTimestamp = Timestamp.now();

        // Load newest job listings
        loadNewestJobs(currentTimestamp);

        // Load internship job listings
        loadInternshipJobs();

        // Set click listeners for navigation
        setNavigationListeners();

        return binding.getRoot();
    }

    private void loadUserProfile() {
        if (auth.getCurrentUser() != null) {
            String userId = auth.getCurrentUser().getUid();
            db.collection("users").document(userId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    String userName = task.getResult().getString("name");
                    String avatarUrl = task.getResult().getString("avatarUrl");

                    if (userName != null) {
                        binding.txtNameHome.setText("Xin Chào " + userName);
                    }

                    if (avatarUrl != null) {
                        Glide.with(this)
                                .load(avatarUrl)
                                .into(binding.profileButtonHome);
                    }
                } else {
                    Log.e("FirestoreError", "Error getting user profile", task.getException());
                }
            });
        }
    }

    private void loadNewestJobs(Timestamp currentTimestamp) {
        db.collection("jobs")
                .whereGreaterThan("deadline", currentTimestamp)
                .orderBy("deadline", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        newestJobList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String jobId = document.getId();
                            String jobName = document.getString("jobTitle");
                            String corpId = document.getString("corpId");
                            int expId = document.getLong("expId").intValue();
                            int salaryId = document.getLong("salaryId").intValue();
                            List<String> workAddressList = (List<String>) document.get("workAddress");
                            String[] workAddress = workAddressList.toArray(new String[0]);
                            Timestamp deadline = document.getTimestamp("deadline");

                            Jobs job = new Jobs(jobId, jobName, corpId, expId, salaryId, workAddress, deadline);
                            newestJobList.add(job);
                            logger.log(Level.INFO, job.toString());
                        }

                        rvJobs = binding.rvNewestJob;
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
                        rvJobs.setLayoutManager(linearLayoutManager);
                        rvJobs.setAdapter(new JobsAdapter(requireContext(), newestJobList, this));
                    } else {
                        Log.e("FirestoreError", task.getException().toString());
                        Toast.makeText(requireContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadInternshipJobs() {
        db.collection("jobs")
                .whereEqualTo("levelId", 8) // replace with appropriate condition
                .whereEqualTo("wayToWorkId", 3) // replace with appropriate condition
                .limit(10)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        internshipJobList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String jobId = document.getId();
                            String jobName = document.getString("jobTitle");
                            String corpId = document.getString("corpId");
                            int expId = document.getLong("expId").intValue();
                            int salaryId = document.getLong("salaryId").intValue();
                            List<String> workAddressList = (List<String>) document.get("workAddress");
                            String[] workAddress = workAddressList.toArray(new String[0]);
                            Timestamp deadline = document.getTimestamp("deadline");

                            Jobs job = new Jobs(jobId, jobName, corpId, expId, salaryId, workAddress, deadline);
                            internshipJobList.add(job);
                        }

                        Log.e("internshipJobList", internshipJobList.toString());
                        binding.internshipRv.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
                        binding.internshipRv.setAdapter(new JobsAdapter(requireContext(), internshipJobList, this));
                    }
                });
    }

    private void setNavigationListeners() {
        binding.searchBar.setOnClickListener(v -> navController.navigate(R.id.action_jobsFragment_to_searchViewJob));

        binding.newestJobMore.setOnClickListener(v -> {
            bundle = new Bundle();
            bundle.putInt("idGroup", 0);
            navController.navigate(R.id.action_jobsFragment_to_viewMoreJob, bundle);
        });

        binding.viewMore2.setOnClickListener(v -> {
            bundle = new Bundle();
            bundle.putInt("idGroup", 1);
            navController.navigate(R.id.action_jobsFragment_to_viewMoreJob, bundle);
        });
    }

    @Override
    public void onItemClick(int position, ArrayList<Jobs> jobsArrayList) {
        bundle = new Bundle();
        bundle.putString("documentID", jobsArrayList.get(position).getJobID());
        navController.navigate(R.id.action_jobsFragment_to_jobDetail, bundle);
    }

    @Override
    public void onItemClickUpdate(int position) {
        // Handle update action if needed
    }
}
