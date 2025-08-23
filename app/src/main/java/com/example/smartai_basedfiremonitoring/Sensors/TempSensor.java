package com.example.smartai_basedfiremonitoring.Sensors;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TempSensor {
    public static void tempMonitoring(TextView tempAnalogOutput, TextView tempStatus, Fragment fragment){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("sensors");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Double temp = snapshot.child("temperature").getValue(Double.class);

                if (temp != null){
                    tempAnalogOutput.setText(String.valueOf(temp) + " °C");
                }

                if (temp != null && temp > 35) {
                    tempStatus.setText("⚠️ High Temperature!");
                    tempStatus.setTextColor(fragment.getResources().getColor(android.R.color.holo_red_dark));
                } else if (temp != null) {
                    tempStatus.setText("Normal");
                    tempStatus.setTextColor(fragment.getResources().getColor(android.R.color.holo_green_dark));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
