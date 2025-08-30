package com.example.smartai_basedfiremonitoring.Fragments.UserFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.smartai_basedfiremonitoring.R;

public class SettingsFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        LinearLayout profile = view.findViewById(R.id.profile);
        LinearLayout emergency = view.findViewById(R.id.emergency);

        profile.setOnClickListener(v -> {
            Fragment profileFragment = new ProfileFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, profileFragment) // container in Activity
                    .addToBackStack(null) // allows going back
                    .commit();
        });

        emergency.setOnClickListener(v -> {
            Fragment emergencyFragment = new EmergencyFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, emergencyFragment) // container in Activity
                    .addToBackStack(null) // allows going back
                    .commit();
        });

        return  view;
    }
}