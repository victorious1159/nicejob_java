package com.example.nicejobapplication;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.nicejobapplication.databinding.ActivityLoginFacebookBinding;
import com.example.nicejobapplication.databinding.ActivityMainBinding;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Arrays;

public class LoginFacebook extends AppCompatActivity {

    private ActivityLoginFacebookBinding binding;

    private CallbackManager callbackManager = CallbackManager.Factory.create();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private AccessToken accessToken = AccessToken.getCurrentAccessToken();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginFacebookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (accessToken != null && !accessToken.isExpired()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onCancel() {
                        // TODO: Not yet implemented
                    }

                    @Override
                    public void onError(FacebookException error) {
                        // TODO: Not yet implemented
                    }

                    @Override
                    public void onSuccess(LoginResult result) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                });

        binding.loginButtonFB.setOnClickListener(v ->
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}

