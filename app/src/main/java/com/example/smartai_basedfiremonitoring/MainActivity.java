package com.example.smartai_basedfiremonitoring;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);


        // Load Home by default
        if (savedInstanceState == null) {
            loadFragment(new UserSensorDashboardFragment());
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_dashboard) {
                selectedFragment = new UserSensorDashboardFragment();
            }

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            }

            if (item.getItemId() == R.id.nav_settings) {
                selectedFragment = new SettingsFragment();
            }


            // else if (item.getItemId() == R.id.nav_home) {
            //     selectedFragment = new HomeFragment();
            // }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}