package com.example.smartai_basedfiremonitoring.Fragments.UserFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartai_basedfiremonitoring.Adapter.EmergencyActivityLogAdapter;
import com.example.smartai_basedfiremonitoring.Model.FireReport;
import com.example.smartai_basedfiremonitoring.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserEmergencyActivityLogFragment extends Fragment {

    private RecyclerView rvActivityLog;
    private EmergencyActivityLogAdapter adapter;
    private List<FireReport> reportList = new ArrayList<>();
    private DatabaseReference reportsRef;
    private EditText searchActivityLog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_emergency_activity_log, container, false);

        rvActivityLog = view.findViewById(R.id.rvActivityLog);
        searchActivityLog = view.findViewById(R.id.searchActivityLog);

        rvActivityLog.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EmergencyActivityLogAdapter(reportList);
        rvActivityLog.setAdapter(adapter);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            reportsRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("reports");
            loadReports();
        } else {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void loadReports() {
        reportsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reportList.clear();
                for (DataSnapshot reportSnap : snapshot.getChildren()) {
                    FireReport report = reportSnap.getValue(FireReport.class);
                    if (report != null) {
                        reportList.add(report);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load reports: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        View geminiAdvisory = requireActivity().findViewById(R.id.geminiAdvisory);
        View bottom_navigation = requireActivity().findViewById(R.id.bottom_navigation);
        if (bottom_navigation != null || geminiAdvisory != null) {
            geminiAdvisory.setVisibility(View.GONE);
            bottom_navigation.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        View geminiAdvisory = requireActivity().findViewById(R.id.geminiAdvisory);
        View bottom_navigation = requireActivity().findViewById(R.id.bottom_navigation);
        if (bottom_navigation != null || geminiAdvisory != null){
            geminiAdvisory.setVisibility(View.VISIBLE);
            bottom_navigation.setVisibility(View.VISIBLE);
        }
    }
}
