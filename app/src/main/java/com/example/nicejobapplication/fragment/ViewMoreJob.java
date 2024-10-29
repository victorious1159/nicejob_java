package com.example.nicejobapplication.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.nicejobapplication.R;
import com.example.nicejobapplication.adapter.JobsAdapter;
import com.example.nicejobapplication.adapter.OnItemClickListener;
import com.example.nicejobapplication.databinding.FragmentViewMoreJobBinding;
import com.example.nicejobapplication.modal.Jobs;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Map;

public class ViewMoreJob extends Fragment implements OnItemClickListener {
    private FragmentViewMoreJobBinding binding;
    private FirebaseFirestore db;
    private ArrayList<Jobs> newestJobList;
    private ArrayList<Jobs> internshipJobList;
    private NavController navController;

    private final Timestamp currentTimestamp = Timestamp.now();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        navController = NavHostFragment.findNavController(this);
        binding = FragmentViewMoreJobBinding.inflate(inflater, container, false);

        // Lấy dữ liệu từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            int idGroup = bundle.getInt("idGroup");
            if (idGroup == 0) {
                showNewestJob();
            } else if (idGroup == 1) {
                showInternshipJob();
            }
        }

        binding.topAppBar.setNavigationOnClickListener(v -> navController.popBackStack());

        return binding.getRoot();
    }

    private void showInternshipJob() {
        db = FirebaseFirestore.getInstance();
        db.collection("jobs")
                .whereEqualTo("levelId", 8)
                .whereEqualTo("wayToWorkId", 3)
                .limit(10)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        internshipJobList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {  // T
                            Map<String, Object> data = document.getData();// hay 'var' bằng 'QueryDocumentSnapshot'
                            String jobId = document.getId();
                            String jobName = document.getString("jobTitle");
                            String corpId = document.getString("corpId");
                            int expId = data.get("expId") != null ? Integer.parseInt(data.get("expId").toString()) : 0;
                            int salaryId = data.get("salaryId") != null ? Integer.parseInt(data.get("salaryId").toString()) : 0;

                            String[] workAddress = data.get("workAddress") != null ? new String[]{data.get("workAddress").toString()} : null;

                            Timestamp deadline = (Timestamp) document.get("deadline");

                            Jobs job = new Jobs(jobId, jobName, corpId, expId, salaryId, workAddress, deadline);
                            internshipJobList.add(job);
                        }
                        Log.e("internshipJobList", internshipJobList.toString());
                        binding.moreJobRv.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
                        binding.moreJobRv.setAdapter(new JobsAdapter(requireContext(), internshipJobList, this));
                    }
                });
    }

    private void showNewestJob() {
        db = FirebaseFirestore.getInstance();
        db.collection("jobs")
                .whereGreaterThan("deadline", currentTimestamp)
                .orderBy("deadline", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        newestJobList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> data = document.getData();
                            String jobId = document.getId();
                            String jobName = document.getString("jobTitle");
                            String corpId = document.getString("corpId");
                            int expId = data.get("expId") != null ? Integer.parseInt(data.get("expId").toString()) : 0;
                            int salaryId = data.get("salaryId") != null ? Integer.parseInt(data.get("salaryId").toString()) : 0;
                            String[] workAddress = data.get("workAddress") != null ? new String[]{data.get("workAddress").toString()} : null;
                            Timestamp deadline = (Timestamp) document.get("deadline");

                            Jobs job = new Jobs(jobId, jobName, corpId, expId, salaryId, workAddress, deadline);
                            newestJobList.add(job);
                        }
                        binding.moreJobRv.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
                        binding.moreJobRv.setAdapter(new JobsAdapter(requireContext(), newestJobList, this));
                    }
                });
    }

    @Override
    public void onItemClick(int position, ArrayList<Jobs> jobsArrayList) {
        Bundle bundle = new Bundle();
        bundle.putString("documentID", jobsArrayList.get(position).getJobID());
        navController.navigate(R.id.action_viewMoreJob_to_jobDetail, bundle);
    }

    @Override
    public void onItemClickUpdate(int position) {
        // TODO: Not yet implemented
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Java không cho phép sử dụng null với binding, nên không cần làm điều này
    }
}
