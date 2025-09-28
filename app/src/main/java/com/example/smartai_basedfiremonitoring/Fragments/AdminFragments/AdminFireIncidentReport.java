package com.example.smartai_basedfiremonitoring.Fragments.AdminFragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminFireIncidentReport extends Fragment {

    private RecyclerView recyclerView;
    private AdminFireIncidentReportAdapter adapter;
    private List<FireReport> reportList;
    private TextView fireDetected,description,emergencyBtn ;
    private DatabaseReference dbRef;
    private ValueEventListener valueEventListener;

    private boolean hasConfirmed = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_fire_incident_report, container, false);

        emergencyBtn = view.findViewById(R.id.emergencyBtn);
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
        FireUpdateHotline(view);


        emergencyBtn.setOnClickListener(v -> {
            informResidence();
        });

        return view;
    }

    public void informResidence(){

        DatabaseReference dbFireDetected = FirebaseDatabase.getInstance().getReference("sensors");

        Map<String, Object> fireUpdate = new HashMap<>();
        fireUpdate.put("flame", true);

        dbFireDetected.updateChildren(fireUpdate);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Map<String, Object> resetUpdate = new HashMap<>();
            resetUpdate.put("flame",false);

            dbFireDetected.updateChildren(resetUpdate);
        }, 50000);

    }

    public void FireUpdateHotline(View view){

        fireDetected = view.findViewById(R.id.fireDetected);
        description = view.findViewById(R.id.description);

        dbRef = FirebaseDatabase.getInstance().getReference("sensors");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean flameSensor = snapshot.child("flame").getValue(Boolean.class);

                if (!hasConfirmed){
                    if (flameSensor != null && flameSensor){
                        fireDetected.setText("Fire Alert Detected");
                        description.setText("Fire detected in barangay ilaya alabang, \n\nplease inform all residence");
                    }else {
                        description.setText("No details until further notice");
                        fireDetected.setText("No Fire Detected");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.d("FlameSensor", "Database error" + error.getMessage());
            }
        });



    }

}
