package com.example.nicejobapplication;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.example.nicejobapplication.databinding.ActivityMainBinding;
import com.facebook.AccessToken;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseAuth firebaseAuth;

    private AccessToken accessToken = AccessToken.getCurrentAccessToken();
    private NavController navController;
    private NavHostFragment navHostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);

        navController = Navigation.findNavController(this, R.id.fragment_container);
        NavigationUI.setupWithNavController(bottomNavigation, navController);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            switch (destination.getId()) {
                case R.id.corporationDetail:
                case R.id.jobDetail:
                case R.id.chooseCVApplication:
                case R.id.createCVFragment:
                case R.id.editCvFragment:
                case R.id.cvDetail2:
                case R.id.searchViewJob:
                case R.id.viewMoreJob:
                case R.id.searchViewCorp:
                    bottomNavigation.setVisibility(View.GONE);
                    break;
                default:
                    bottomNavigation.setVisibility(View.VISIBLE);
                    break;
            }
        });
    }
}

