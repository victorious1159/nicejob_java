package com.example.nicejobapplication.DetailFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.nicejobapplication.R;
import com.example.nicejobapplication.adapter.CorpViewPagerAdapter;
import com.example.nicejobapplication.databinding.FragmentCorporationDetailBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class CorporationDetail extends Fragment {

    private FragmentCorporationDetailBinding binding;
    private FirebaseFirestore db;
    private NavController navController;
    private Bundle bundleToTabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_corporation_detail, container, false);
        binding = FragmentCorporationDetailBinding.inflate(inflater, container, false);

        navController = NavHostFragment.findNavController(this);

        // Set sự kiện cho nút back
        binding.topAppBar.setNavigationOnClickListener(v -> navController.popBackStack());

        // Nhận Bundle từ Fragment trước đó
        Bundle bundle = getArguments();
        String documentID = Objects.requireNonNull(bundle).getString("documentID");

        db = FirebaseFirestore.getInstance();
        db.collection("corporations").document(Objects.requireNonNull(documentID))
                .get()
                .addOnSuccessListener(document -> {
                    String corpName = Objects.requireNonNull(document.getData()).get("corpName").toString();
                    String corpLogo = "gs://nicejob2-ddaa1.appspot.com/corporation_image/" + Objects.requireNonNull(document.getData().get("corpLogo"));

                    String corpDescription = Objects.requireNonNull(document.getData().get("corpDescription")).toString();
                    String corpWebsite = document.getData().get("website").toString().isEmpty() ?
                            "Chưa cập nhật!" : document.getData().get("website").toString();
                    String corpAddress = Objects.requireNonNull(document.getData().get("corpAddress")).toString();

                    binding.corpName.setText(corpName);
                    StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(corpLogo);
                    storageRef.getDownloadUrl().addOnSuccessListener(uri ->
                            Glide.with(requireContext())
                                    .load(uri)
                                    .into(binding.corpLogo));

                    // Tạo Bundle để truyền qua TabLayout
                    bundleToTabLayout = new Bundle();
                    bundleToTabLayout.putString("corpId", documentID);
                    bundleToTabLayout.putString("corpDescription", corpDescription);
                    bundleToTabLayout.putString("corpAddress", corpAddress);
                    bundleToTabLayout.putString("corpWebsite", corpWebsite);

                    binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Giới thiệu"));
                    binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Tuyển dụng"));

                    // Set adapter cho ViewPager
                    CorpViewPagerAdapter adapter = new CorpViewPagerAdapter(getChildFragmentManager(), binding.tabLayout.getTabCount(), bundleToTabLayout);
                    binding.viewPager.setAdapter(adapter);

                    binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));

                    binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            binding.viewPager.setCurrentItem(tab.getPosition());
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {
                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {
                        }
                    });
                });

        return binding.getRoot();
    }
}
