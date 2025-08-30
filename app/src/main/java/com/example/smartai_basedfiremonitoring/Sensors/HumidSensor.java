package com.example.smartai_basedfiremonitoring.Sensors;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HumidSensor {
    private static DatabaseReference databaseReference;
    private static ValueEventListener humidListener;
    public static void humidMonitoring(TextView humidAnalogOutput, TextView humidStatus, Fragment fragment){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("sensors");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Double humid = snapshot.child("humidity").getValue(Double.class);

                if (humid != null){
                    humidAnalogOutput.setText(String.valueOf(humid) + " %RH");
                }

                if (humid < 30) {
                    humidStatus.setText("⚠️ Low Humidity!");
                    humidStatus.setTextColor(fragment.getResources().getColor(android.R.color.holo_red_dark));
                } else if (humid > 70) {
                    humidStatus.setText("⚠️ High Humidity!");
                    humidStatus.setTextColor(fragment.getResources().getColor(android.R.color.holo_red_dark));
                } else {
                    humidStatus.setText("Normal");
                    humidStatus.setTextColor(fragment.getResources().getColor(android.R.color.holo_green_dark));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static void removeListener() {
        if (databaseReference != null && humidListener != null) {
            databaseReference.removeEventListener(humidListener);
            humidListener = null;
        }
    }
}
