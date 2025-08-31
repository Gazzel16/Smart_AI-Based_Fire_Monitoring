package com.example.smartai_basedfiremonitoring.Fragments.AdminFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartai_basedfiremonitoring.Adapter.AdminFireIncidentReportAdapter;
import com.example.smartai_basedfiremonitoring.Model.FireReport;
import com.example.smartai_basedfiremonitoring.R;

import java.util.ArrayList;
import java.util.List;

public class AdminFireIncidentReport extends Fragment {

    private RecyclerView recyclerView;
    private AdminFireIncidentReportAdapter adapter;
    private List<FireReport> reportList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_fire_incident_report, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView); // make sure you have a RecyclerView with this ID in your fragment layout
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Generate fake data
        reportList = generateFakeData();

        // Initialize adapter
        adapter = new AdminFireIncidentReportAdapter(getContext(), reportList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private List<FireReport> generateFakeData() {
        List<FireReport> list = new ArrayList<>();

        list.add(new FireReport("John Doe", false, "Fire detected in Barangay Hall kitchen", "12/12/2025 12:45"));
        list.add(new FireReport("Jane Smith", true, "Minor fire at the local market", "12/12/2025 13:15"));
        list.add(new FireReport("Mark Johnson", false, "Fire in a residential area", "12/12/2025 14:05"));
        list.add(new FireReport("Emily Davis", true, "Kitchen fire reported in Barangay Hall", "12/12/2025 15:20"));
        list.add(new FireReport("Michael Brown", false, "Fire in storage warehouse", "12/12/2025 16:30"));

        return list;
    }
}
