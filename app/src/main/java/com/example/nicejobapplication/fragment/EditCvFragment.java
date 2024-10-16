package com.example.nicejobapplication.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nicejobapplication.MainActivity;
import com.example.nicejobapplication.R;
import com.example.nicejobapplication.databinding.FragmentEditCvBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditCvFragment extends Fragment {
    private FragmentEditCvBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference dbRef;
    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private FirebaseFirestore db;

    private static final int PICK_IMAGE_REQUEST = 71;
    private Uri selectedAvt = null;
    private StorageReference storageReference;

    private final Calendar calendar = Calendar.getInstance();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
    private final String dt = dateFormat.format(calendar.getTime());

    private final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private final String dayOfBirthPattern = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditCvBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("CV");
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        Bundle bundle = getArguments();
        String documentID = bundle != null ? bundle.getString("cvName") : null;
        String userEmail = auth.getCurrentUser() != null ? auth.getCurrentUser().getEmail() : null;

        if (userEmail != null && documentID != null) {
            loadCVData(userEmail, documentID);
        }

        binding.btnUpdateCv.setOnClickListener(v -> updateCV());
        binding.avtEditCV.setOnClickListener(v -> launchGallery());

        return binding.getRoot();
    }

    private void loadCVData(String userEmail, String documentID) {
        db.collection("created_cv").document(userEmail).collection(userEmail).document(documentID)
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        String cvName = document.getString("cvName");
                        String avatar = document.getString("avatar");
                        String employerName = document.getString("employerName");
                        String email = document.getString("email");
                        String phoneNumber = document.getString("phoneNumber");
                        String gentle = document.getString("gentle");
                        String address = document.getString("address");
                        String dayOfBirth = document.getString("dayOfBirth");
                        String careerGoal = document.getString("careerGoal");
                        String workExperience = document.getString("workExperience");
                        String academicLevel = document.getString("academicLevel");

                        binding.edtNameCvEditCv.setText(cvName);
                        Glide.with(requireActivity()).load(avatar).into(binding.avtEditCV);
                        binding.edtNameEditCV.setText(employerName);
                        binding.edtEmailEditCV.setText(email);
                        binding.edtPhoneEditCV.setText(phoneNumber);
                        binding.autoCompleteTextViewGentleEdit.setText(gentle);
                        binding.edtAddressEditCV.setText(address);
                        binding.edtBirthdayEditCV.setText(dayOfBirth);
                        binding.edtcareerGoalEditCV.setText(careerGoal);
                        binding.autoCompleteTextViewExperienceEdit.setText(workExperience);
                        binding.edtAcademicLevelEditCV.setText(academicLevel);

                        dropdownItem();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show());
    }

    private void launchGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Vui lòng chọn ảnh"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedAvt = data.getData();
            try {
                binding.avtEditCV.setImageBitmap(MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedAvt));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateCV() {
        String name = binding.edtNameEditCV.getText().toString();
        String email = binding.edtEmailEditCV.getText().toString();
        String phone = binding.edtPhoneEditCV.getText().toString();
        String gentle = binding.autoCompleteTextViewGentleEdit.getText().toString();
        String address = binding.edtAddressEditCV.getText().toString();
        String dateOfBirth = binding.edtBirthdayEditCV.getText().toString();
        String careerGoal = binding.edtcareerGoalEditCV.getText().toString();
        String workExp = binding.autoCompleteTextViewExperienceEdit.getText().toString();
        String academicLevel = binding.edtAcademicLevelEditCV.getText().toString();

        if (validateInputs(name, email, phone, gentle, address, dateOfBirth, careerGoal, workExp, academicLevel)) {
            if (selectedAvt == null) {
                uploadDataWithoutImage();
            } else {
                uploadData();
            }
        }
    }

    private boolean validateInputs(String name, String email, String phone, String gentle, String address, String dateOfBirth, String careerGoal, String workExp, String academicLevel) {
        if (name.isEmpty()) binding.edtNameEditCV.setError("Vui lòng nhập tên ");
        if (email.isEmpty()) binding.edtEmailEditCV.setError("Vui lòng nhập email");
        if (dateOfBirth.isEmpty()) binding.edtBirthdayEditCV.setError("Vui lòng nhập ngày sinh");
        if (phone.isEmpty()) binding.edtPhoneEditCV.setError("Vui lòng nhập số điện thoại");
        if (workExp.isEmpty()) binding.autoCompleteTextViewExperienceEdit.setError("Vui lòng nhập kinh nghiệm");
        if (gentle.isEmpty()) binding.autoCompleteTextViewGentleEdit.setError("Vui lòng chọn giới tính");
        if (address.isEmpty()) binding.edtAddressEditCV.setError("Vui lòng nhập địa chỉ");
        if (careerGoal.isEmpty()) binding.edtcareerGoalEditCV.setError("Vui lòng nhập mục tiêu sự nghiệp");
        if (academicLevel.isEmpty()) binding.edtAcademicLevelEditCV.setError("Vui lòng nhập trình độ học vấn");

        if (!email.matches(emailPattern)) {
            binding.edtEmailEditCV.setError("Vui lòng nhập email đúng cú pháp");
            Toast.makeText(requireContext(), "Vui lòng nhập email đúng cú pháp", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!dateOfBirth.matches(dayOfBirthPattern)) {
            binding.edtBirthdayEditCV.setError("Vui lòng nhập ngày sinh ( yyyy/mm/dd )");
            Toast.makeText(requireContext(), "Vui lòng nhập ngày sinh ( yyyy/mm/dd )", Toast.LENGTH_SHORT).show();
            return false;
        }

        return !name.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !gentle.isEmpty() && !address.isEmpty() &&
                !workExp.isEmpty() && !academicLevel.isEmpty() && !dateOfBirth.isEmpty() && !careerGoal.isEmpty();
    }

    private void uploadData() {
        StorageReference reference = storage.getReference().child("CV_Avt").child(String.valueOf(new Date().getTime()));
        reference.putFile(selectedAvt).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                reference.getDownloadUrl().addOnSuccessListener(taskUri -> uploadInfo(taskUri.toString()));
            } else {
                Toast.makeText(requireContext(), "Đã xảy ra lỗi, Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadDataWithoutImage() {
        uploadInfo(null);
    }

    private void uploadInfo(String avtUrl) {
        long date = dateToMilliseconds(dt, dateFormat);

        Bundle bundle = getArguments();
        String documentID = bundle != null ? bundle.getString("cvName") : null;
        String userEmail = auth.getCurrentUser() != null ? auth.getCurrentUser().getEmail() : null;

        if (userEmail != null && documentID != null) {
            Map<String, Object> updateMap = new HashMap<>();
            if (avtUrl != null) updateMap.put("avatar", avtUrl);
            updateMap.put("employerName", binding.edtNameEditCV.getText().toString());
            updateMap.put("email", binding.edtEmailEditCV.getText().toString());
            updateMap.put("phoneNumber", binding.edtPhoneEditCV.getText().toString());
            updateMap.put("gentle", binding.autoCompleteTextViewGentleEdit.getText().toString());
            updateMap.put("address", binding.edtAddressEditCV.getText().toString());
            updateMap.put("dayOfBirth", binding.edtBirthdayEditCV.getText().toString());
            updateMap.put("careerGoal", binding.edtcareerGoalEditCV.getText().toString());
            updateMap.put("workExperience", binding.autoCompleteTextViewExperienceEdit.getText().toString());
            updateMap.put("academicLevel", binding.edtAcademicLevelEditCV.getText().toString());
            updateMap.put("createAt", date);

            db.collection("created_cv").document(userEmail).collection(userEmail).document(documentID)
                    .update(updateMap)
                    .addOnCompleteListener(task -> {
                        Toast.makeText(requireContext(), "Cập nhật CV thành công!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(requireContext(), MainActivity.class));
                    })
                    .addOnFailureListener(e -> Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void dropdownItem() {
        String[] experience = getResources().getStringArray(R.array.exp);
        ArrayAdapter<String> arrAdapterExp = new ArrayAdapter<>(requireContext(), R.layout.dropdown_item, experience);
        binding.autoCompleteTextViewExperienceEdit.setAdapter(arrAdapterExp);

        String[] gentle = getResources().getStringArray(R.array.gentle);
        ArrayAdapter<String> arrAdapterGen = new ArrayAdapter<>(requireContext(), R.layout.dropdown_item, gentle);
        binding.autoCompleteTextViewGentleEdit.setAdapter(arrAdapterGen);
    }

    private long dateToMilliseconds(String date, SimpleDateFormat dateFormat) {
        try {
            Date mDate = dateFormat.parse(date);
            return mDate != null ? mDate.getTime() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
