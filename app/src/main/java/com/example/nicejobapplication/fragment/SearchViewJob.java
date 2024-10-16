package com.example.nicejobapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.nicejobapplication.R;
import com.example.nicejobapplication.adapter.JobsSearchAdapter;
import com.example.nicejobapplication.adapter.OnItemClickListener;
import com.example.nicejobapplication.databinding.FragmentSearchViewJobBinding;
import com.example.nicejobapplication.modal.Jobs;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class SearchViewJob extends Fragment implements OnItemClickListener {

    private FragmentSearchViewJobBinding binding;
    private NavController navController;
    private ArrayList<Jobs> searchResults;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchViewJobBinding.inflate(inflater, container, false);

        binding.jobSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null && !query.isEmpty()) {
                    performSearch(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null && !newText.isEmpty()) {
                    performSearch(newText);
                }
                return true;
            }
        });

        navController = NavHostFragment.findNavController(this);
        binding.jobSearchView.setOnCloseListener(() -> {
            navController.popBackStack();
            return true;
        });

        return binding.getRoot();
    }

    private void performSearch(String query) {
        ArrayList<Jobs> jobItem = new ArrayList<>();
        searchResults = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("jobs").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) { // Thay 'var' bằng 'QueryDocumentSnapshot'
                Jobs job = new Jobs(
                        doc.getId(),
                        doc.getString("jobTitle"),
                        doc.getString("corpId")
                );
                jobItem.add(job);
            }
            for (Jobs job : jobItem) {
                if (job.getJobName().toLowerCase().contains(query.toLowerCase())) {
                    searchResults.add(job);
                }
            }
            showSearchResults(searchResults);
        });
    }

    private void showSearchResults(ArrayList<Jobs> arrayList) {
        JobsSearchAdapter adapter = new JobsSearchAdapter(arrayList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        binding.jobResultList.setLayoutManager(linearLayoutManager);
        binding.jobResultList.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position, ArrayList<Jobs> jobsArrayList) {
        Bundle bundle = new Bundle(); // Tạo một Bundle mới
        bundle.putString("documentID", jobsArrayList.get(position).getJobID());
        navController.navigate(R.id.action_searchViewJob_to_jobDetail, bundle);
    }

    @Override
    public void onItemClickUpdate(int position) {
        // TODO: Not yet implemented
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Giải phóng binding
    }
}
