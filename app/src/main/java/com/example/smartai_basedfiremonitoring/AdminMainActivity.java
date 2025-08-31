package com.example.smartai_basedfiremonitoring;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.smartai_basedfiremonitoring.Fragments.AdminFragments.AdminDashboardFragment;
import com.example.smartai_basedfiremonitoring.Fragments.AdminFragments.AdminSettingsFragment;
import com.example.smartai_basedfiremonitoring.Fragments.AdminFragments.ESP32_WIFI_CredentialsFragment;

public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_main);

        if (savedInstanceState == null){
            loadFragment(new AdminDashboardFragment());
        }

    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}