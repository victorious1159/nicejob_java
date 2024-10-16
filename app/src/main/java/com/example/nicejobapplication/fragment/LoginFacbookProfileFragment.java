package com.example.nicejobapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.nicejobapplication.MainActivity;
import com.example.nicejobapplication.R;
import com.example.nicejobapplication.databinding.FragmentLoginFacbookProfileBinding;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class LoginFacbookProfileFragment extends Fragment {
    private FragmentLoginFacbookProfileBinding binding;
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginFacbookProfileBinding.inflate(inflater, container, false);
        auth = FirebaseAuth.getInstance();

        // Lấy access token từ Facebook
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            GraphRequest request = GraphRequest.newMeRequest(accessToken, (jsonObject, response) -> {
                // Lấy dữ liệu người dùng từ Facebook
                String email = jsonObject != null ? jsonObject.optString("email") : null;
                String name = jsonObject != null ? jsonObject.optString("name") : null;
                String profileUrl = jsonObject != null ? jsonObject.optJSONObject("picture")
                        .optJSONObject("data").optString("url") : null;

                binding.txtNameFB.setText(name);
                binding.txtEmailFB.setText(email);
                if (profileUrl != null) {
                    Picasso.get().load(profileUrl).into(binding.profileFBButton);
                }
            });

            // Thêm các trường cần lấy từ Facebook
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link,picture.type(large),email");
            request.setParameters(parameters);
            request.executeAsync();
        } else {
            Toast.makeText(getActivity(), "Facebook AccessToken is null", Toast.LENGTH_SHORT).show();
        }

        // Logout
        binding.btnFBLogout.setOnClickListener(v -> {
            LoginManager.getInstance().logOut();
            startActivity(new Intent(getActivity(), MainActivity.class));
        });

        return binding.getRoot();
    }
}
