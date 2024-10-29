package com.example.nicejobapplication.TabFragment;

import android.os.Bundle;
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

import com.example.nicejobapplication.R;
import com.example.nicejobapplication.adapter.JobsAdapter;
import com.example.nicejobapplication.adapter.OnItemClickListener;
import com.example.nicejobapplication.databinding.FragmentJobsCorpListBinding;
import com.example.nicejobapplication.modal.Jobs;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

public class JobsCorpListFragment extends Fragment implements OnItemClickListener {

    private FragmentJobsCorpListBinding binding;
    private FirebaseFirestore db;
    private ArrayList<Jobs> newestJobList;
    private RecyclerView rvJobs;
    private NavController navController;
    private Bundle bundle;
    private Bundle bundleCorpId;

    public JobsCorpListFragment(Bundle bundleCorpId) {
        this.bundleCorpId = bundleCorpId;
    }
    Logger logger = Logger.getLogger(JobsCorpListFragment.class.getName());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentJobsCorpListBinding.inflate(getLayoutInflater());

        db = FirebaseFirestore.getInstance();

        Timestamp currentTimestamp = Timestamp.now();

        String corpId = bundleCorpId.getString("corpId");
        db.collection("jobs").whereEqualTo("corpId", corpId)
                .whereGreaterThan("deadline", currentTimestamp.toDate())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            binding.notification.setVisibility(View.VISIBLE);
                            binding.jobList.setVisibility(View.GONE);
                        } else {
                            binding.notification.setVisibility(View.GONE);
                            binding.jobList.setVisibility(View.VISIBLE);

                            newestJobList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                String jobId = document.getId();
                                String jobName = document.getString("jobTitle");
                                String corpId1 = document.getString("corpId");
                                int expId = data.get("expId") != null ? Integer.parseInt(data.get("expId").toString()) : 0;
                                int salaryId = data.get("salaryId") != null ? Integer.parseInt(data.get("salaryId").toString()) : 0;
                                String[] workAddress = data.get("workAddress") != null ? new String[]{data.get("workAddress").toString()} : null;
                                Timestamp deadline = (Timestamp) document.get("deadline");

                                Jobs job = new Jobs(jobId, jobName, corpId1, expId, salaryId, workAddress, deadline);
                                newestJobList.add(job);
                            }
                            rvJobs = binding.jobList;
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
                            rvJobs.setLayoutManager(linearLayoutManager);
                            rvJobs.setAdapter(new JobsAdapter(requireContext(), newestJobList, this));
                        }
                    }else{
                        Toast.makeText(requireContext(), task.getException().toString(), Toast.LENGTH_LONG).show();
                        logger.warning(task.getException().toString());
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_LONG).show());

        return binding.getRoot();
    }

    @Override
    public void onItemClick(int position, ArrayList<Jobs> jobsArrayList) {
        // Tạo một Bundle theo cách của Java
        bundle = new Bundle();
        bundle.putString("documentID", jobsArrayList.get(position).getJobID());

        navController = NavHostFragment.findNavController(this);

        // Điều hướng với bundle vừa tạo
        navController.navigate(R.id.action_corporationDetail_to_jobDetail, bundle);
    }


    @Override
    public void onItemClickUpdate(int position) {
        // TODO: Not yet implemented
    }
}

