package com.example.smartai_basedfiremonitoring.Fragments.AdminFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartai_basedfiremonitoring.Adapter.AdminDashBoardAdapter;
import com.example.smartai_basedfiremonitoring.Model.AdminDashBoardModel;
import com.example.smartai_basedfiremonitoring.R;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardFragment extends Fragment {

    RecyclerView recyclerView;
    AdminDashBoardAdapter adapter;
    List<AdminDashBoardModel> adminDashboardList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);

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

        return view;
    }
}