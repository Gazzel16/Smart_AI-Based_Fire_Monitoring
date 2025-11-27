package com.example.smartai_basedfiremonitoring.Fragments.UserFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.smartai_basedfiremonitoring.Adapter.FireReportAdapter;
import com.example.smartai_basedfiremonitoring.Model.FireReport;
import com.example.smartai_basedfiremonitoring.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FireUpdateFragment extends Fragment {

    private FireReportAdapter adapter;
    private List<FireReport> reports;
    private DatabaseReference dbRef;
    private ValueEventListener valueEventListener;

    private TextView fireDetected, description;

    private boolean hasConfirmed = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fire_update, container, false);


        TextView emergencyBtn = view.findViewById(R.id.emergencyBtn);
        emergencyBtn.setOnClickListener(v -> {
            Fragment fragment = new EmergencyContactFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        FireReportConfirmed(view);
        FireUpdateResidenceReport(view);
        FireUpdateHotline(view);

        return  view;
    }

    public void FireReportConfirmed(View view){

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("sensors");

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
                    description.setText("Fire Alert Detected");
                    fireDetected.setText("Fire detected in barangay ilaya alabang, \n\nplease evacuate in the nearest evacuation center");
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
    public void FireUpdateResidenceReport(View view){

        dbRef = FirebaseDatabase.getInstance().getReference("users");
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        reports = new ArrayList<>();
        reports = new ArrayList<>();

        adapter = new FireReportAdapter(requireContext(), reports);
        recyclerView.setAdapter(adapter);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reports.clear();

                for (DataSnapshot dataSnapshotUsers : snapshot.getChildren()){
                    DataSnapshot reportsSnapshot = dataSnapshotUsers.child("reports");

                    for (DataSnapshot reportSnapshot :reportsSnapshot.getChildren()){
                        FireReport fireReport = reportSnapshot.getValue(FireReport.class);

                        if (fireReport != null){
                            reports.add(fireReport);
                        }
                    }
                }

                // Sort newest first
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());
                reports.sort((r1, r2) -> {
                    try {
                        java.util.Date d1 = sdf.parse(r1.getTimeReported());
                        java.util.Date d2 = sdf.parse(r2.getTimeReported());
                        return d2.compareTo(d1); // newest on top
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

        dbRef.addValueEventListener(valueEventListener);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // prevent memory leaks & callbacks after fragment is destroyed
        if (dbRef != null && valueEventListener != null) {
            dbRef.removeEventListener(valueEventListener);
            valueEventListener = null;
        }
    }

}