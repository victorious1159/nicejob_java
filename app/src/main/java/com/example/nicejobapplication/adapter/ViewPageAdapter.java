package com.example.nicejobapplication.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.nicejobapplication.TabFragment.LoginTabFragment;
import com.example.nicejobapplication.TabFragment.SignupTabFragment;

public class ViewPageAdapter extends FragmentStateAdapter {

    public ViewPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new SignupTabFragment(); // Thay thế bằng tên class thực tế của bạn
        } else {
            return new LoginTabFragment(); // Thay thế bằng tên class thực tế của bạn
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Số lượng tab
    }
}
