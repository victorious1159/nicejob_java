package com.example.nicejobapplication.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nicejobapplication.R;
import com.example.nicejobapplication.adapter.CvAdapter;
import com.example.nicejobapplication.adapter.OnItemClickCVListener;
import com.example.nicejobapplication.authentication.LoginSignup;
import com.example.nicejobapplication.databinding.FragmentCvBinding;
import com.example.nicejobapplication.modal.CV;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CVFragment extends Fragment implements OnItemClickCVListener {
    private FragmentCvBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private ArrayList<CV> cvArrayList;
    private RecyclerView rvCv;
    private NavController navController;

    @SuppressLint("NotifyDataSetChanged")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCvBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        navController = NavHostFragment.findNavController(this);
        String userEmail = auth.getCurrentUser() != null ? auth.getCurrentUser().getEmail() : null;

        if (userEmail == null) {
            binding.btnCreateCV.setText("Đăng nhập");
            binding.btnCreateCV.setOnClickListener(v -> startActivity(new Intent(getActivity(), LoginSignup.class)));
        } else {
            binding.txtTitleLogin.setText("");
            binding.btnCreateCV.setOnClickListener(v -> navController.navigate(R.id.action_CVFragment_to_createCVFragment));

            rvCv = binding.rvCv;
            rvCv.setLayoutManager(new LinearLayoutManager(getActivity()));

            // View CV
            db = FirebaseFirestore.getInstance();
            db.collection("created_cv").document(userEmail).collection(userEmail)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            cvArrayList = new ArrayList<>();
                            for (com.google.firebase.firestore.DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                CV cv = document.toObject(CV.class);
                                if (cv != null) {
                                    cv.setCvId(document.getId()); // Giả định CV có phương thức setCvId()
                                    cvArrayList.add(cv);
                                }
                            }

                            rvCv.setAdapter(new CvAdapter(getActivity(), cvArrayList, this));
                            rvCv.getAdapter().notifyDataSetChanged();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show());
        }

        return binding.getRoot();
    }

    @Override
    public void onItemClick(int position, ArrayList<CV> cvArrayList) {
        Bundle bundle = new Bundle();
        bundle.putString("cvName", cvArrayList.get(position).getCvId());

        navController.navigate(R.id.action_CVFragment_to_cvDetail2, bundle);
    }

    @Override
    public void onItemClickUpdate(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("cvName", cvArrayList.get(position).getCvId());

        navController.navigate(R.id.action_CVFragment_to_editCvFragment, bundle);
    }
}