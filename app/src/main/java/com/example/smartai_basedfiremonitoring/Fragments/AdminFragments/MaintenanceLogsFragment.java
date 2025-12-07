package com.example.smartai_basedfiremonitoring.Fragments.AdminFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartai_basedfiremonitoring.Adapter.MaintenanceLogsAdapter;
import com.example.smartai_basedfiremonitoring.Model.MaintenanceLogsModel;
import com.example.smartai_basedfiremonitoring.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MaintenanceLogsFragment extends Fragment {

    private TextView informBtn;
    private RecyclerView rvMaintenanceLogs;
    private MaintenanceLogsAdapter adapter;
    private ArrayList<MaintenanceLogsModel> logsList;

    private DatabaseReference dbRef;
    private boolean isMaintenance = false; // current state

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maintenance_logs, container, false);

        informBtn = view.findViewById(R.id.informBtn);
        rvMaintenanceLogs = view.findViewById(R.id.rvMaintenanceLogs);

        logsList = new ArrayList<>();
        adapter = new MaintenanceLogsAdapter(getContext(), logsList);
        rvMaintenanceLogs.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMaintenanceLogs.setAdapter(adapter);

        dbRef = FirebaseDatabase.getInstance().getReference("MaintenanceLogs");

        // ⭐ Listen only to logs
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                logsList.clear();

                for (DataSnapshot data : snapshot.getChildren()) {

                    // Skip "isMaintenance" node
                    if (data.getKey().equals("isMaintenance"))
                        continue;

                    String dateTime = data.child("lastMaintenance").getValue(String.class);

                    if (dateTime != null) {
                        logsList.add(new MaintenanceLogsModel(dateTime, false));
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load logs", Toast.LENGTH_SHORT).show();
            }
        });

        // ⭐ Listen to maintenance state
        dbRef.child("isMaintenance").child("maintenance")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Boolean state = snapshot.getValue(Boolean.class);

                        if (state != null) {
                            isMaintenance = state;
                            if (isMaintenance) {
                                informBtn.setText("revert");
                                // set red tint
                                informBtn.setBackgroundTintList(
                                        ContextCompat.getColorStateList(view.getContext(), android.R.color.holo_red_dark));
                            } else {
                                informBtn.setText("inform");
                                // set green tint
                                informBtn.setBackgroundTintList(
                                        ContextCompat.getColorStateList(view.getContext(), R.color.green)); // or #4CAF50
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });

        // Button listener
        informBtn.setOnClickListener(v -> toggleMaintenance());

        return view;
    }


    private void toggleMaintenance() {
        isMaintenance = !isMaintenance; // toggle state

        // Create timestamp (PH Time)
        String dateTime = getPHTime();

        // Update main maintenance state
        dbRef.child("isMaintenance").child("maintenance").setValue(isMaintenance);

        if (isMaintenance) {
            // Add a new maintenance log (only date)
            String key = dbRef.push().getKey();
            if (key != null) {
                dbRef.child(key).child("lastMaintenance").setValue(dateTime);
            }
            Toast.makeText(getContext(), "Maintenance informed", Toast.LENGTH_SHORT).show();
            informBtn.setText("revert");

        } else {
            Toast.makeText(getContext(), "Maintenance reverted", Toast.LENGTH_SHORT).show();
            informBtn.setText("inform");
        }
    }


    private String getPHTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Manila"));
        return sdf.format(new Date());
    }

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            bottomNav.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            bottomNav.setVisibility(View.VISIBLE);
        }
    }
}
