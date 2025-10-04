package com.example.smartai_basedfiremonitoring.Fragments.UserFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.smartai_basedfiremonitoring.Activity.LoginActivity;
import com.example.smartai_basedfiremonitoring.Adapter.OptionItemAdapter;
import com.example.smartai_basedfiremonitoring.Model.OptionItemModel;
import com.example.smartai_basedfiremonitoring.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {

    private RecyclerView rvSetting;
    private OptionItemAdapter adapter;
    private List<OptionItemModel> optionList;

    private TextView name, email;
    DatabaseReference userRef;
    FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        rvSetting = view.findViewById(R.id.rvSetting);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);

        // 1. Prepare data
        optionList = new ArrayList<>();
        optionList.add(new OptionItemModel("Emergency", R.drawable.aid));
        optionList.add(new OptionItemModel("Logout", R.drawable.logout));

        // 2. Setup OptionItemAdapter
        adapter = new OptionItemAdapter(getContext(), optionList, position -> {
            OptionItemModel clickedItem = optionList.get(position);

            switch (clickedItem.getTitle()){

                case "Emergency":
                    Fragment fragment = new EmergencyFragment();
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, fragment)
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

        ConstraintLayout constraint = view.findViewById(R.id.constraint);
        constraint.setOnClickListener(v -> {
            Fragment fragment = new ProfileFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        // 3. Setup RecyclerView
        rvSetting.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSetting.setAdapter(adapter);

        userInfo();
        return view;
    }

    public void userInfo(){

        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();

        userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

        userRef.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                String userName = snapshot.child("username").getValue(String.class);
                String userEmail = snapshot.child("email").getValue(String.class);

                name.setText(userName);
                email.setText(userEmail);
            } else {
                Log.d("User", "User data not found");
            }
        }).addOnFailureListener(e -> {
            Log.d("Fetch user info", "Failed to fetch user info");
        });
    }
}