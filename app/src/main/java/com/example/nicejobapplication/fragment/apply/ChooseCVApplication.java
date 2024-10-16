package com.example.nicejobapplication.fragment.apply;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.nicejobapplication.R;
import com.example.nicejobapplication.databinding.FragmentChooseCVApplicationBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class ChooseCVApplication extends Fragment {
    private FragmentChooseCVApplicationBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private NavController navController;
    private String selectedCV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        navController = NavHostFragment.findNavController(this);

        binding = FragmentChooseCVApplicationBinding.inflate(inflater, container, false);

        String userEmail = auth.getCurrentUser() != null ? auth.getCurrentUser().getEmail() : null;

        // Kiểm tra xem người dùng nhấn vào Radio Button nào
        binding.cvSelection.setEnabled(false);
        binding.cvOnlineRadioBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.cvSelection.setEnabled(isChecked);
        });

        binding.uploadLocalCV.setEnabled(false);
        binding.cvOnLocalRadioBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.uploadLocalCV.setEnabled(isChecked);
        });

        // Set sự kiện cho nút back
        binding.topAppBar.setNavigationOnClickListener(v -> navController.popBackStack());

        // Xử lý phần chọn CV online
        HashMap<String, String> cvArray = new HashMap<>();
        if (userEmail != null) {
            db.collection("created_cv").document(userEmail).collection(userEmail).get().addOnSuccessListener(queryDocumentSnapshots -> {
                for (com.google.firebase.firestore.DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    cvArray.put(document.getString("cvName"), document.getId());
                }
                String[] keys = cvArray.keySet().toArray(new String[0]);
                String[] values = cvArray.values().toArray(new String[0]);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.cv_dropdown, keys);
                adapter.setDropDownViewResource(R.layout.cv_dropdown);
                binding.autoCompleteTextView.setAdapter(adapter);

                binding.autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position != -1) {
                            selectedCV = values[position];
                            Log.e("CV Selected", cvArray.toString());
                        }
                    }
                });
            });
        }

        binding.applyBtn.setOnClickListener(v -> {
            // Kiểm tra xem đã chọn bất kỳ CV nào chưa
            if (binding.radioGroup.getCheckedRadioButtonId() != -1) {
                // Kiểm tra xem có nội dung ở phần giới thiệu hay không
                if (binding.introduceContent.getText().toString().isBlank()) {
                    new MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Thông báo")
                            .setMessage("Việc ứng tuyển thiếu phần giới thiệu có thể ảnh hưởng đến khả năng được nhà tuyển dụng để ý thấp hơn. Bạn có chắc muốn tiếp tục?")
                            .setNeutralButton("Hủy", (dialog, which) -> { /* Respond to neutral button press */ })
                            .setPositiveButton("Tiếp tục", (dialog, which) -> sendApplication(db, selectedCV, binding.introduceContent.getText(), getArguments() != null ? getArguments().getString("documentID") : null, userEmail))
                            .show();
                } else {
                    new MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Thông báo")
                            .setMessage("Bạn có chắc chắn muốn nộp CV cho công việc này không? Bạn không thể thay đổi tác vụ này.")
                            .setNeutralButton("Hủy", (dialog, which) -> { })
                            .setPositiveButton("Đồng ý", (dialog, which) -> sendApplication(db, selectedCV, binding.introduceContent.getText(), getArguments() != null ? getArguments().getString("documentID") : null, userEmail))
                            .show();
                }
            } else {
                Snackbar.make(binding.getRoot(), R.string.noneCVChosenError, Snackbar.LENGTH_SHORT).show();
            }
        });

        binding.goBackBtn.setOnClickListener(v -> navController.popBackStack());

        return binding.getRoot();
    }

    private void sendApplication(FirebaseFirestore db, String cvID, Editable introduce, String jobId, String employeeId) {
        cvID = cvID.replace("[", "").replace("]", "");
        HashMap<String, Object> application = new HashMap<>();
        application.put("employeeId", employeeId);
        application.put("cvId", cvID);
        application.put("jobId", jobId);
        application.put("introduction", introduce.toString());

        db.collection("applications").add(application)
                .addOnSuccessListener(documentReference -> {
                    navController.popBackStack();
                    Snackbar.make(binding.getRoot(), "Đã ứng tuyển thành công!", Snackbar.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Snackbar.make(binding.getRoot(), "Lỗi không thể nộp được, vui lòng thử lại sau.", Snackbar.LENGTH_SHORT).show());
    }
}
