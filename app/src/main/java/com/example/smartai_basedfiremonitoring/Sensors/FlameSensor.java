package com.example.smartai_basedfiremonitoring.Sensors;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FlameSensor {
    public static void flameMonitoring(TextView flameOutput, TextView flameDetector, Fragment fragment){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("flameSensor");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && fragment.isAdded() && fragment.getActivity() != null) {
                    Log.d("FlameSensor", "Snapshot: " + snapshot.getValue());

                    fragment.getActivity().runOnUiThread(() -> {
                        Object flameOutputObj = snapshot.child("flameOutput").getValue();
                        Object flameDetectorObj = snapshot.child("flameDetector").getValue();

                        double flameOutputVal = 0;
                        if (flameOutputObj instanceof Double) {
                            flameOutputVal = (Double) flameOutputObj;
                        } else if (flameOutputObj instanceof Long) {
                            flameOutputVal = ((Long) flameOutputObj).doubleValue(); // Firebase sometimes stores numbers as Long
                        }

                        boolean flameDetected = false;
                        if (flameDetectorObj instanceof Boolean) {
                            flameDetected = (Boolean) flameDetectorObj;
                        }

// Update UI
                        flameOutput.setText(String.valueOf("AO: "+ flameOutputVal));
                        flameDetector.setText(flameDetected ? "Fire Detected" : "No Fire");


                        Log.d("FlameSensor", "FlameOutput: " + flameOutputVal + ", FlameDetector: " );
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FlameSensor", "Error: " + error.getMessage());
            }
        });
    }

}
