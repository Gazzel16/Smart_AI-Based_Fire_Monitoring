package com.example.smartai_basedfiremonitoring.Fragments.AdminFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.smartai_basedfiremonitoring.Activity.LoginActivity;
import com.example.smartai_basedfiremonitoring.Activity.OptionItemAdapter;
import com.example.smartai_basedfiremonitoring.Fragments.UserFragments.ProfileFragment;
import com.example.smartai_basedfiremonitoring.Model.OptionItemModel;
import com.example.smartai_basedfiremonitoring.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class AdminSettingsFragment extends Fragment {

private RecyclerView rvSetting;
    private OptionItemAdapter adapter;
    private List<OptionItemModel> optionList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_settings, container, false);
        rvSetting = view.findViewById(R.id.rvSetting);

        // 1. Prepare data
        optionList = new ArrayList<>();
        optionList.add(new OptionItemModel("Modify ESP32 Access", R.drawable.settings));
        optionList.add(new OptionItemModel("Emergency", R.drawable.settings));
        optionList.add(new OptionItemModel("Create Admin Account", R.drawable.user_icon));
        optionList.add(new OptionItemModel("Logout", R.drawable.logout));

        // 2. Setup OptionItemAdapter
        adapter = new OptionItemAdapter(getContext(), optionList, position -> {
            OptionItemModel clickedItem = optionList.get(position);

            switch (clickedItem.getTitle()){
                case "Create Admin Account":
                    Fragment adminSignup = new AdminSignup();
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, adminSignup) // container in Activity
                            .addToBackStack(null) // allows going back
                            .commit();
                    break;

                case "Modify ESP32 Access":
                    Fragment esp32Access = new ESP32_WIFI_CredentialsFragment();
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, esp32Access)
                            .addToBackStack(null)
                            .commit();
                    break;

                case "Logout":
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    break;
            }
        });

        // 3. Setup RecyclerView
        rvSetting.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSetting.setAdapter(adapter);

        return view;
    }
}