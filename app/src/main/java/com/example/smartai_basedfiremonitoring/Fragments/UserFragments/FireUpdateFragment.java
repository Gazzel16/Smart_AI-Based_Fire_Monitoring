package com.example.smartai_basedfiremonitoring.Fragments.UserFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fire_update, container, false);

        dbRef = FirebaseDatabase.getInstance().getReference("users");

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

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
                if (adapter != null){
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };


        dbRef.addValueEventListener(valueEventListener);
        return  view;
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