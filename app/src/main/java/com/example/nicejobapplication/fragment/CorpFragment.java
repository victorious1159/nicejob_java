package com.example.nicejobapplication.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
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
import com.example.nicejobapplication.adapter.CorpAdapter;
import com.example.nicejobapplication.adapter.OnClickCorpListener;
import com.example.nicejobapplication.databinding.FragmentCorpBinding;
import com.example.nicejobapplication.modal.Corporation;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class CorpFragment extends Fragment implements OnClickCorpListener {

    private FragmentCorpBinding binding;
    private FirebaseFirestore db;
    private NavController navController;
    private ArrayList<Corporation> corpArrayList;
    private RecyclerView rvCorporation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCorpBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Khởi tạo NavController từ NavHostFragment
        navController = NavHostFragment.findNavController(this);

        // Kiểm tra kết nối Internet
        if (isNetworkConnected(requireContext())) {
            db = FirebaseFirestore.getInstance();
            db.collection("corporations")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            corpArrayList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String corpID = document.getId();
                                String corpName = document.getString("corpName");
                                String corpDescription = document.getString("corpDescription");
                                String corpLogo = "gs://nicejob2-ddaa1.appspot.com/corporation_image/" + document.getString("corpLogo");
                                Corporation corp = new Corporation(corpID, corpName, corpDescription, corpLogo);
                                corpArrayList.add(corp);
                            }

                            setupRecyclerView();
                        } else {
                            Toast.makeText(requireContext(), "Error getting documents.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

        binding.searchBar.setOnClickListener(v -> navController.navigate(R.id.action_corpFragment_to_searchViewCorp));

        return view;
    }

    private boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void setupRecyclerView() {
        rvCorporation = binding.rvCorp;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        rvCorporation.setLayoutManager(linearLayoutManager);
        rvCorporation.setAdapter(new CorpAdapter(requireContext(), corpArrayList, this));
    }

    @Override
    public void onItemClick(int position, ArrayList<Corporation> corpArrayList) {
        Bundle bundle = new Bundle();
        bundle.putString("documentID", corpArrayList.get(position).getCorpID());
        navController.navigate(R.id.action_corpFragment_to_corporationDetail, bundle);
    }

    @Override
    public void onItemClickUpdate(int position) {
        // Handle the update action here if needed
    }
}
