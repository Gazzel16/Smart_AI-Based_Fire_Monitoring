package com.example.smartai_basedfiremonitoring.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.smartai_basedfiremonitoring.Fragments.AdminFragments.AdminDashboardFragment;
import com.example.smartai_basedfiremonitoring.Fragments.AdminFragments.AdminFireIncidentReport;
import com.example.smartai_basedfiremonitoring.Fragments.AdminFragments.AdminSettingsFragment;
import com.example.smartai_basedfiremonitoring.Fragments.AdminFragments.ESP32_WIFI_CredentialsFragment;
import com.example.smartai_basedfiremonitoring.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable Edge-to-Edge
        androidx.activity.EdgeToEdge.enable(this);

        setContentView(R.layout.activity_admin_main);

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(new AdminDashboardFragment());
        }

        // Initialize BottomNavigationView
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_dashboard) {
                selectedFragment = new AdminDashboardFragment();
            } else if (item.getItemId() == R.id.fireUpdate) {
                selectedFragment = new AdminFireIncidentReport();
            } else if (item.getItemId() == R.id.nav_settings) {
                selectedFragment = new AdminSettingsFragment();
            }

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
