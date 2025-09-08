package com.example.smartai_basedfiremonitoring.Fragments.AdminFragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.smartai_basedfiremonitoring.Adapter.AdminDashBoardAdapter;
import com.example.smartai_basedfiremonitoring.Fragments.AdminFragments.ChartHandler.AdminCountChart;
import com.example.smartai_basedfiremonitoring.Fragments.AdminFragments.ChartHandler.UserCountChart;
import com.example.smartai_basedfiremonitoring.Model.AdminDashBoardModel;
import com.example.smartai_basedfiremonitoring.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardFragment extends Fragment {

    RecyclerView recyclerView;
    AdminDashBoardAdapter adapter;
    List<AdminDashBoardModel> adminDashboardList;

    BarChart userCountChart, adminCountChart;

    ValueEventListener userListener, adminListener;

    TextView userCount, adminCount;

    DatabaseReference dbRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);

        ConstraintLayout constraintViewUser= view.findViewById(R.id.constraintViewUser);
        ConstraintLayout constraintViewAdmin= view.findViewById(R.id.constraintViewAdmin);
        userCountChart = view.findViewById(R.id.userCountChart);
        adminCountChart = view.findViewById(R.id.adminCountChart);

        constraintViewUser.setOnClickListener(v -> {
           Fragment fragment = new Admin_ViewUsersFragment();

            getParentFragmentManager().beginTransaction() // if inside a fragment
                    .replace(R.id.fragment_container, fragment) // your container id
                    .addToBackStack(null) // so you can go back
                    .commit();
        });

        constraintViewAdmin.setOnClickListener(v -> {
            Fragment fragment = new Admin_ViewAdminFragment();

            getParentFragmentManager().beginTransaction() // if inside a fragment
                    .replace(R.id.fragment_container, fragment) // your container id
                    .addToBackStack(null) // so you can go back
                    .commit();
        });


        adminCount(view);
        userCount(view);
        UserCountChart.userChart(userCountChart, view, this);
        AdminCountChart.adminChart(adminCountChart, view, this);
        fireIncidentOverview(view);
        return view;
    }

    public void adminCount(View view){
        adminCount = view.findViewById(R.id.adminCount);
        dbRef = FirebaseDatabase.getInstance().getReference("users");
        dbRef.addValueEventListener(new ValueEventListener() {
            int countAdmin = 0;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot child : snapshot.getChildren()){
                    String role = child.child("role").getValue(String.class);

                    if (role != null && role.equalsIgnoreCase("admin")){
                        countAdmin++;
                    }
                }

                adminCount.setText("Registered: +" + String.valueOf(countAdmin));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void userCount(View view){
        userCount = view.findViewById(R.id.userCount);
        dbRef = FirebaseDatabase.getInstance().getReference("users");
        dbRef.addValueEventListener(new ValueEventListener() {
            int countUsers = 0;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot child : snapshot.getChildren()){
                    String role = child.child("role").getValue(String.class);

                    if (role != null && role.equalsIgnoreCase("user")){
                        countUsers++;
                    }
                }

                userCount.setText("Registered: +" + String.valueOf(countUsers));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void fireIncidentOverview(View view){
        // 1. Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 2. Create fake data
        adminDashboardList = new ArrayList<>();
        adminDashboardList.add(new AdminDashBoardModel(R.drawable.flame_incident_report_icon, "Fire Incident Report", 70, "100+"));
        adminDashboardList.add(new AdminDashBoardModel(R.drawable.flame_incident_confirmed_report_icon, "Confirm Incident", 70, "100+"));
        adminDashboardList.add(new AdminDashBoardModel(R.drawable.flame_incident_false_report_icon, "False Incident", 70, "100+"));

        // 3. Set adapter
        adapter = new AdminDashBoardAdapter(getContext(), adminDashboardList);
        recyclerView.setAdapter(adapter);
    }
}