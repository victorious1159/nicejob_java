package com.example.nicejobapplication.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.nicejobapplication.MainActivity;
import com.example.nicejobapplication.R;
import com.example.nicejobapplication.databinding.FragmentLoginProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginProfileFragment extends Fragment {
    private FragmentLoginProfileBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseDatabase database;
    private FirebaseStorage storage;

    private ImageButton imgUpdate;
    private EditText edtNameUpdate;
    private static final int PICK_IMAGE_REQUEST = 71;
    private Uri selectedImg = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginProfileBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        db = FirebaseFirestore.getInstance();

        String userId = auth.getCurrentUser().getUid();

        // Load user data
        db.collection("users").document(userId).get().addOnSuccessListener(document -> {
            String name = document.getString("name");
            String email = document.getString("email");
            String img = document.getString("img");

            binding.txtName.setText(name);
            binding.txtEmail.setText("Email: " + email);
            Glide.with(requireActivity()).load(img).into(binding.profileButton);
        }).addOnFailureListener(e -> Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show());

        // Update button click listener
        binding.btnUpdate.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater1 = getLayoutInflater();
            View dialogView = inflater1.inflate(R.layout.dialog_updating_profile, null);
            builder.setView(dialogView);

            Button btnUpdate = dialogView.findViewById(R.id.btnUpdate);
            Button btnCancelUpdating = dialogView.findViewById(R.id.btnCancelUpdating);
            imgUpdate = dialogView.findViewById(R.id.imgUpdate);
            edtNameUpdate = dialogView.findViewById(R.id.edtNameUpdate);

            // Set data for update
            setData(userId);

            // Select image
            imgUpdate.setOnClickListener(v1 -> launchGallery());

            // Click update
            btnUpdate.setOnClickListener(v1 -> {
                if (edtNameUpdate.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Vui lòng nhập tên của mình!", Toast.LENGTH_SHORT).show();
                } else if (selectedImg == null) {
                    uploadDataWithoutImage(userId);
                } else {
                    uploadData(userId);
                }
            });

            // Cancel
            btnCancelUpdating.setOnClickListener(v1 -> builder.create().dismiss());
            builder.create().show();
        });

        // Logout
        binding.btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getActivity(), MainActivity.class));
        });

        return binding.getRoot();
    }

    private void uploadData(String userId) {
        if (selectedImg != null) {
            // Update in Storage
            String imagePath = "Profile/" + new Date().getTime();
            storage.getReference(imagePath).putFile(selectedImg).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    storage.getReference(imagePath).getDownloadUrl().addOnSuccessListener(taskUri -> {
                        // Update in Firestore
                        uploadInfo(taskUri.toString(), userId);
                    });
                } else {
                    Toast.makeText(getActivity(), "Đã xảy ra lỗi, Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadDataWithoutImage(String userId) {
        uploadInfoWithoutImage(userId);
    }

    private void uploadInfo(String imgUrl, String userId) {
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("name", edtNameUpdate.getText().toString());
        updateMap.put("img", imgUrl);

        db.collection("users").document(userId).update(updateMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getActivity(), "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                });
    }

    private void uploadInfoWithoutImage(String userId) {
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("name", edtNameUpdate.getText().toString());

        db.collection("users").document(userId).update(updateMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getActivity(), "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                });
    }

    private void launchGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Vui lòng lựa chọn ảnh"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.getData() == null) {
                return;
            }

            selectedImg = data.getData();
            try {
                Glide.with(requireActivity()).load(selectedImg).into(imgUpdate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setData(String userId) {
        db.collection("users").document(userId).get().addOnSuccessListener(document -> {
            String name = document.getString("name");
            String img = document.getString("img");

            edtNameUpdate.setText(name);
            Glide.with(requireActivity()).load(img).into(imgUpdate);
        });
    }
}
