package com.example.nicejobapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.nicejobapplication.adapter.CorpAdapter;
import com.example.nicejobapplication.adapter.OnClickCorpListener;
import com.example.nicejobapplication.databinding.FragmentSearchViewCorpBinding;
import com.example.nicejobapplication.modal.Corporation;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class SearchViewCorp extends Fragment implements OnClickCorpListener {

    private FragmentSearchViewCorpBinding binding;
    private ArrayList<Corporation> searchResults;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchViewCorpBinding.inflate(inflater, container, false);

        binding.corpSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        return binding.getRoot();
    }

    private void performSearch(String query) {
        ArrayList<Corporation> corpItem = new ArrayList<>();
        searchResults = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("corporations").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) { // Thay 'var' bằng 'QueryDocumentSnapshot'
                Corporation corp = new Corporation(
                        document.getId(),
                        document.getString("corpName"),
                        document.getString("corpDescription"),
                        "gs://nicejob-367709.appspot.com/corporation_image/" + document.getString("corpLogo")
                );
                corpItem.add(corp);
            }
            for (Corporation corp : corpItem) {
                if (corp.getCorpName().toLowerCase().contains(query.toLowerCase())) {
                    searchResults.add(corp);
                }
            }
            showSearchResults(searchResults);
        });
    }

    private void showSearchResults(ArrayList<Corporation> arrayList) {
        CorpAdapter adapter = new CorpAdapter(requireContext(), arrayList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        binding.corpSearchResult.setLayoutManager(linearLayoutManager);
        binding.corpSearchResult.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position, ArrayList<Corporation> jobsArrayList) {
        // TODO: Implement this method
    }

    @Override
    public void onItemClickUpdate(int position) {
        // TODO: Implement this method
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Java không cho phép sử dụng null với binding, nên không cần làm điều này
    }
}
