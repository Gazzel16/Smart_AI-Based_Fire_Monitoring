package com.example.smartai_basedfiremonitoring.Fragments.UserFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartai_basedfiremonitoring.Adapter.FireReportAdapter;
import com.example.smartai_basedfiremonitoring.Model.FireReport;
import com.example.smartai_basedfiremonitoring.R;

import java.util.ArrayList;
import java.util.List;

public class FireUpdateFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fire_update, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<FireReport> reports = new ArrayList<>();
        reports.add(new FireReport("John Doe", false, "Lorem ipsum dolor sit amet...", "12/12/2025 12:45"));
        reports.add(new FireReport("Jane Smith", true, "Another report description...", "13/12/2025 14:20"));
        reports.add(new FireReport("Jane Smith", true, "Another report description...", "13/12/2025 14:20"));
        reports.add(new FireReport("Jane Smith", true, "Another report description...", "13/12/2025 14:20"));
        reports.add(new FireReport("Jane Smith", true, "Another report description...", "13/12/2025 14:20"));


        FireReportAdapter adapter = new FireReportAdapter(requireContext(), reports);
        recyclerView.setAdapter(adapter);
        return  view;
    }
}