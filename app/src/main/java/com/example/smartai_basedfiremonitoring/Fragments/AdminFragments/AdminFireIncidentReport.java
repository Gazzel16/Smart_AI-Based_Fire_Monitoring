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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdminFireIncidentReport extends Fragment {

    private RecyclerView recyclerView;
    private AdminFireIncidentReportAdapter adapter;
    private List<FireReport> reportList;
    private TextView fireDetected,description,emergencyBtn ;
    private DatabaseReference userRef;
    private DatabaseReference sensorRef;
    private ValueEventListener valueEventListener;

    private boolean hasConfirmed = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_fire_incident_report, container, false);

        emergencyBtn = view.findViewById(R.id.emergencyBtn);
        userRef = FirebaseDatabase.getInstance().getReference("users");
        sensorRef = FirebaseDatabase.getInstance().getReference("sensors");

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

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

                reportList.clear();

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    DataSnapshot reportsSnapshot = userSnapshot.child("reports");

                    for (DataSnapshot reportSnapshot : reportsSnapshot.getChildren()) {
                        FireReport fireReport = reportSnapshot.getValue(FireReport.class);

                        if (fireReport != null && fireReport.getTimeReported() != null) {
                            reportList.add(fireReport);
                        }
                    }
                }

                reportList.sort((r1, r2) -> {
                    try {
                        // First, check pending status
                        boolean r1Pending = !r1.isConfirmation() && !r1.isFalseReport();
                        boolean r2Pending = !r2.isConfirmation() && !r2.isFalseReport();

                        if (r1Pending && !r2Pending) {
                            return -1; // r1 comes first
                        } else if (!r1Pending && r2Pending) {
                            return 1; // r2 comes first
                        } else {
                            // Both are same status, sort by time
                            Date d1 = sdf.parse(r1.getTimeReported());
                            Date d2 = sdf.parse(r2.getTimeReported());
                            return d2.compareTo(d1); // newest first
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return 0;
                    }
                });


                if (adapter != null){
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };


        userRef.addValueEventListener(valueEventListener);
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

        sensorRef.addValueEventListener(new ValueEventListener() {
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
