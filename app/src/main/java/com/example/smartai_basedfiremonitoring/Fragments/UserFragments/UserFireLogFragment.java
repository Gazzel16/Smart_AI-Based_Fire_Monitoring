package com.example.smartai_basedfiremonitoring.Fragments.UserFragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.smartai_basedfiremonitoring.Adapter.FireLogAdapter;
import com.example.smartai_basedfiremonitoring.Model.FireLogModel;
import com.example.smartai_basedfiremonitoring.R;
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

public class UserFireLogFragment extends Fragment {

    private RecyclerView rvFireLog;
    private FireLogAdapter adapter;
    private ArrayList<FireLogModel> fireLogs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_fire_log, container, false);

        ImageView backBtn = view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            onDestroy();
            Fragment fragment = new SettingsFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment) // make sure R.id.fragment_container is your FrameLayout
                    .addToBackStack(null) // optional: adds transaction to back stack so you can navigate back
                    .commit();

        });

        // Setup RecyclerView
        rvFireLog = view.findViewById(R.id.rvFireLog);
        rvFireLog.setLayoutManager(new LinearLayoutManager(getContext()));
        fireLogs = new ArrayList<>();
        adapter = new FireLogAdapter(getContext(), fireLogs);
        rvFireLog.setAdapter(adapter);

        // Reference to fire_logs (for displaying the logs)
        DatabaseReference logRef = FirebaseDatabase.getInstance().getReference("fire_logs");
        logRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fireLogs.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String time = child.getValue(String.class);
                    if (time != null) {
                        fireLogs.add(new FireLogModel(time));
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // Reference to sensors (for detecting flame and saving new logs)
        DatabaseReference sensorRef = FirebaseDatabase.getInstance().getReference("sensors");
        sensorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean flameSensor = snapshot.child("flame").getValue(Boolean.class);

                if (flameSensor != null && flameSensor) {
                    // Get current PHT time
                    TimeZone phTimeZone = TimeZone.getTimeZone("Asia/Manila");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    sdf.setTimeZone(phTimeZone);
                    String currentTime = sdf.format(new Date());

                    // Save to fire_logs
                    logRef.push().setValue(currentTime);
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
