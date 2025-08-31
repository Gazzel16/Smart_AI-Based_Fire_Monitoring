package com.example.smartai_basedfiremonitoring.Fragments.UserFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.example.smartai_basedfiremonitoring.Activity.EmergencyReportActivity;
import com.example.smartai_basedfiremonitoring.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EmergencyFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_emergency, container, false);

        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            bottomNav.setVisibility(View.GONE);
        }

        LottieAnimationView lottie = view.findViewById(R.id.lottieAnimationView);

        lottie.setOnClickListener(v -> {
       Intent intent = new Intent(getContext(), EmergencyReportActivity.class);
       startActivity(intent);
        });


        return  view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Show bottom navigation again when leaving this fragment
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            bottomNav.setVisibility(View.VISIBLE);
        }
    }
}