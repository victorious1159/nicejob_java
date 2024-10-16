package com.example.nicejobapplication.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.nicejobapplication.TabFragment.CorpDetailIntroductionTabFragment;
import com.example.nicejobapplication.TabFragment.JobsCorpListFragment;

public class CorpViewPagerAdapter extends FragmentPagerAdapter {

    private final int totalTab;
    private final Bundle bundle;

    public CorpViewPagerAdapter(@NonNull FragmentManager fragmentManager, int totalTab, Bundle bundle) {
        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.totalTab = totalTab;
        this.bundle = bundle;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CorpDetailIntroductionTabFragment(bundle);
            case 1:
                return new JobsCorpListFragment(bundle);
            default:
                return new CorpDetailIntroductionTabFragment(bundle);
        }
    }

    @Override
    public int getCount() {
        return totalTab;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Giới thiệu";
            case 1:
                return "Tuyển dụng";
            default:
                return "Giới thiệu";
        }
    }
}
