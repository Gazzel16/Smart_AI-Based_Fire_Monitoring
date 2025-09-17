package com.example.smartai_basedfiremonitoring.Fragments.AdminFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartai_basedfiremonitoring.Adapter.FireReportAdapter;
import com.example.smartai_basedfiremonitoring.Adapter.ViewUserAdapter;
import com.example.smartai_basedfiremonitoring.Model.FireReport;
import com.example.smartai_basedfiremonitoring.Model.User;
import com.example.smartai_basedfiremonitoring.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FireReportListFragment extends Fragment {


    RecyclerView recyclerView;
    FireReportAdapter adapter;
    List<FireReport> fireReportList;

    DatabaseReference dbRef;

    public FireReportListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fire_report_list, container, false);

        dbRef = FirebaseDatabase.getInstance().getReference("users");
        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fireReportList = new ArrayList<>();
        adapter = new FireReportAdapter(getContext(), fireReportList);
        recyclerView.setAdapter(adapter);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fireReportList.clear();

                for (DataSnapshot dataSnapshotUsers : snapshot.getChildren()){
                    DataSnapshot reportsSnapshot = dataSnapshotUsers.child("reports");

                    for (DataSnapshot reportSnapshot :reportsSnapshot.getChildren()){
                        FireReport fireReport = reportSnapshot.getValue(FireReport.class);

                        if (fireReport != null){
                            fireReportList.add(fireReport);
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
        });




        return view;
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