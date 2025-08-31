package com.example.smartai_basedfiremonitoring.Fragments.AdminFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartai_basedfiremonitoring.Adapter.AdminFireIncidentReportAdapter;
import com.example.smartai_basedfiremonitoring.Model.FireReport;
import com.example.smartai_basedfiremonitoring.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminFireIncidentReport extends Fragment {

    private RecyclerView recyclerView;
    private AdminFireIncidentReportAdapter adapter;
    private List<FireReport> reportList;

    private DatabaseReference dbRef;
    private ValueEventListener valueEventListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_fire_incident_report, container, false);

        dbRef = FirebaseDatabase.getInstance().getReference("users");
        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView); // make sure you have a RecyclerView with this ID in your fragment layout
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Generate fake data
        reportList = new ArrayList<>();
        adapter = new AdminFireIncidentReportAdapter(getContext(), reportList);
        recyclerView.setAdapter(adapter);


        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reportList.clear();

                for (DataSnapshot dataSnapshotUsers : snapshot.getChildren()){
                    DataSnapshot reportsSnapshot = dataSnapshotUsers.child("reports");

                    for (DataSnapshot reportSnapshot :reportsSnapshot.getChildren()){
                        FireReport fireReport = reportSnapshot.getValue(FireReport.class);

                        if (fireReport != null){
                            reportList.add(fireReport);
                        }
                    }
                }
                if (adapter != null){
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };


        dbRef.addValueEventListener(valueEventListener);

        return view;
    }

}
