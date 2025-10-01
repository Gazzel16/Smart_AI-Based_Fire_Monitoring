package com.example.smartai_basedfiremonitoring.Fragments.AdminFragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.smartai_basedfiremonitoring.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ESP32_WIFI_CredentialsFragment extends Fragment {

    private EditText wifiSSID, wifiPass;
    private Button saveBtn;
    ImageView backBtn;
    private DatabaseReference dbRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_esp32_wifi_credentials, container, false);

        wifiSSID = view.findViewById(R.id.wifiSSID);
        wifiPass = view.findViewById(R.id.wifiPass);
        saveBtn = view.findViewById(R.id.saveBtn);
        backBtn = view.findViewById(R.id.backBtn);
        // 🔥 Firebase reference to your "wifi" node
        dbRef = FirebaseDatabase.getInstance().getReference("wifi");

        saveBtn.setOnClickListener(v -> saveCredentials());

        backBtn.setOnClickListener(v -> {
            onDestroy();
            Fragment setting = new AdminSettingsFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, setting)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void saveCredentials() {
        String ssid = wifiSSID.getText().toString().trim();
        String pass = wifiPass.getText().toString().trim();

        if (TextUtils.isEmpty(ssid) || TextUtils.isEmpty(pass)) {
            Toast.makeText(getContext(), "SSID and Password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save to Firebase under "wifi/ssid" and "wifi/password"
        dbRef.child("ssid").setValue(ssid);
        dbRef.child("password").setValue(pass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "WiFi Credentials Updated!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to update!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            bottomNav.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            bottomNav.setVisibility(View.VISIBLE);
        }
    }
}
