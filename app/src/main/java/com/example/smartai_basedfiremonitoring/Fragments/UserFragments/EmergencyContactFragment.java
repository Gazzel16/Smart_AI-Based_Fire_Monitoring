package com.example.smartai_basedfiremonitoring.Fragments.UserFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.smartai_basedfiremonitoring.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
public class EmergencyContactFragment extends Fragment {

    public EmergencyContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_emergency_contact, container, false);

        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
        ImageView geminiAdvisory = requireActivity().findViewById(R.id.geminiAdvisory);
        if (bottomNav != null || geminiAdvisory != null) {
            bottomNav.setVisibility(View.GONE);
            geminiAdvisory.setVisibility(View.GONE);
        }
        return  view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Show bottom navigation again when leaving this fragment
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
        ImageView geminiAdvisory = requireActivity().findViewById(R.id.geminiAdvisory);
        if (bottomNav != null || geminiAdvisory != null) {
            bottomNav.setVisibility(View.VISIBLE);
            geminiAdvisory.setVisibility(View.VISIBLE);
        }
    }
}