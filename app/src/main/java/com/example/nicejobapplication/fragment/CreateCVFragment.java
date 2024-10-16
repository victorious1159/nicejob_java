package com.example.nicejobapplication.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.nicejobapplication.MainActivity;
import com.example.nicejobapplication.R;
import com.example.nicejobapplication.databinding.FragmentCreateCvBinding;
import com.example.nicejobapplication.modal.CV;
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

public class CreateCVFragment extends Fragment {
    private FragmentCreateCvBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference dbRef;
    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private FirebaseFirestore db;

    private static final int PICK_IMAGE_REQUEST = 71;
    private Uri selectedAvt;
    private FirebaseStorage firebaseStore;
    private StorageReference storageReference;

    // Get creation date
    private final Calendar calendar = Calendar.getInstance();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
    private final String dt = dateFormat.format(calendar.getTime());

    // Email and date pattern
    private final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private final String dayOfBirthPattern = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreateCvBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("CV");
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        db = FirebaseFirestore.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();

        binding.btnContinue.setOnClickListener(v -> saveCV());

        // Select avatar
        binding.avtCreateCV.setOnClickListener(v -> launchGallery());

        // Dropdown items
        dropdownItem();

        return binding.getRoot();
    }

    private void launchGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Vui lòng lựa chọn ảnh!"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.getData() == null) {
                return;
            }

            selectedAvt = data.getData();
            try {
                // Display selected avatar
                binding.avtCreateCV.setImageBitmap(MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedAvt));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveCV() {
        // Getting values
        String cvName = binding.edtCvName.getText().toString();
        String name = binding.edtNameCreateCV.getText().toString();
        String email = binding.edtEmailCreateCV.getText().toString();
        String phone = binding.edtPhoneCreateCV.getText().toString();
        String gentle = binding.autoCompleteTextViewGentle.getText().toString();
        String address = binding.edtAddressCreateCV.getText().toString();
        String dateOfBirth = binding.edtBirthdayCreateCV.getText().toString();
        String careerGoal = binding.edtcareerGoalCreateCV.getText().toString();
        String workExp = binding.autoCompleteTextViewExperience.getText().toString();
        String academicLevel = binding.edtAcademicLevelCreateCV.getText().toString();

        if (isDataIncomplete(cvName, name, email, phone, gentle, address, dateOfBirth, careerGoal, workExp, academicLevel)) {
            Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        } else if (!email.matches(emailPattern)) {
            binding.edtEmailCreateCV.setError("Vui lòng nhập email đúng cú pháp");
        } else if (!dateOfBirth.matches(dayOfBirthPattern)) {
            binding.edtBirthdayCreateCV.setError("Vui lòng nhập ngày sinh (yyyy/MM/dd)");
        } else if (selectedAvt == null) {
            Toast.makeText(requireContext(), "Vui lòng chọn ảnh!", Toast.LENGTH_SHORT).show();
        } else {
            uploadData();
        }
    }

    private boolean isDataIncomplete(String cvName, String name, String email, String phone, String gentle, String address,
                                     String dateOfBirth, String careerGoal, String workExp, String academicLevel) {
        boolean isIncomplete = false;

        if (cvName.isEmpty()) {
            binding.edtCvName.setError("Vui lòng nhập tên CV");
            isIncomplete = true;
        }
        if (name.isEmpty()) {
            binding.edtNameCreateCV.setError("Vui lòng nhập tên");
            isIncomplete = true;
        }
        if (email.isEmpty()) {
            binding.edtEmailCreateCV.setError("Vui lòng nhập email");
            isIncomplete = true;
        }
        if (dateOfBirth.isEmpty()) {
            binding.edtBirthdayCreateCV.setError("Vui lòng nhập ngày sinh");
            isIncomplete = true;
        }
        if (phone.isEmpty()) {
            binding.edtPhoneCreateCV.setError("Vui lòng nhập số điện thoại");
            isIncomplete = true;
        }
        if (gentle.isEmpty()) {
            binding.autoCompleteTextViewGentle.setError("Vui lòng chọn giới tính");
            isIncomplete = true;
        }
        if (address.isEmpty()) {
            binding.edtAddressCreateCV.setError("Vui lòng nhập địa chỉ");
            isIncomplete = true;
        }
        if (workExp.isEmpty()) {
            binding.autoCompleteTextViewExperience.setError("Vui lòng nhập kinh nghiệm");
            isIncomplete = true;
        }
        if (careerGoal.isEmpty()) {
            binding.edtcareerGoalCreateCV.setError("Vui lòng nhập mục tiêu sự nghiệp");
            isIncomplete = true;
        }
        if (academicLevel.isEmpty()) {
            binding.edtAcademicLevelCreateCV.setError("Vui lòng nhập trình độ học vấn");
            isIncomplete = true;
        }

        return isIncomplete;
    }

    private void uploadData() {
        StorageReference reference = storageReference.child("CV_Avt").child(String.valueOf(new Date().getTime()));
        reference.putFile(selectedAvt).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                reference.getDownloadUrl().addOnSuccessListener(taskUri -> uploadInfo(taskUri.toString()));
            } else {
                Toast.makeText(requireContext(), "Đã xảy ra lỗi, Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadInfo(String avtUrl) {
        long date = dateToMilliseconds(dt, dateFormat);
        String userEmail = auth.getCurrentUser().getEmail();

        CV cv = new CV("", binding.edtCvName.getText().toString(), avtUrl, binding.edtNameCreateCV.getText().toString(),
                binding.edtEmailCreateCV.getText().toString(), binding.edtPhoneCreateCV.getText().toString(),
                binding.autoCompleteTextViewGentle.getText().toString(), binding.edtAddressCreateCV.getText().toString(),
                binding.edtBirthdayCreateCV.getText().toString(), binding.edtcareerGoalCreateCV.getText().toString(),
                binding.autoCompleteTextViewExperience.getText().toString(), binding.edtAcademicLevelCreateCV.getText().toString(),
                date);

        db.collection("created_cv").document(userEmail).collection(userEmail).document()
                .set(cv)
                .addOnCompleteListener(task -> {
                    Toast.makeText(requireContext(), "Tạo CV thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(requireContext(), MainActivity.class));
                })
                .addOnFailureListener(e -> Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void dropdownItem() {
        // Dropdown item experience
        String[] experience = getResources().getStringArray(R.array.exp);
        ArrayAdapter<String> arrAdapterExp = new ArrayAdapter<>(requireContext(), R.layout.dropdown_item, experience);
        binding.autoCompleteTextViewExperience.setAdapter(arrAdapterExp);

        // Dropdown item gender
        String[] gentle = getResources().getStringArray(R.array.gentle);
        ArrayAdapter<String> arrAdapterGen = new ArrayAdapter<>(requireContext(), R.layout.dropdown_item, gentle);
        binding.autoCompleteTextViewGentle.setAdapter(arrAdapterGen);
    }

    private long dateToMilliseconds(String date, SimpleDateFormat dateFormat) {
        try {
            Date mDate = dateFormat.parse(date);
            return mDate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
