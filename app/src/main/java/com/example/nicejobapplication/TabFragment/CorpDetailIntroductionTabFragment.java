package com.example.nicejobapplication.TabFragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.nicejobapplication.R;

public class CorpDetailIntroductionTabFragment extends Fragment {

    private Bundle bundle;
    private TextView corpDesTV;
    private TextView corpAddressTV;
    private TextView corpWebsiteTV;

    public CorpDetailIntroductionTabFragment(Bundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_corp_detail_introduc_tab, container, false);

        String corpDescription = bundle.getString("corpDescription");
        String corpAddress = bundle.getString("corpAddress");
        String corpWebsite = bundle.getString("corpWebsite");

        corpDesTV = view.findViewById(R.id.introduceContent);
        corpDesTV.setText(corpDescription);

        corpAddressTV = view.findViewById(R.id.address);
        corpAddressTV.setText(corpAddress);

        corpWebsiteTV = view.findViewById(R.id.website);
        corpWebsiteTV.setText(corpWebsite);

        return view;
    }
}

