package com.example.smartai_basedfiremonitoring.Sensors;

import android.graphics.Color;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SmokeSensor {

    private static DatabaseReference databaseReference;
    private static ValueEventListener humidListener;
    public static void smokeSensor(TextView smokeOutput,
                                   TextView smokeStatus,
                                   LineChart smokeLineChart, Fragment fragment){


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("sensors");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (fragment == null || !fragment.isAdded()) return;
                Double gas = snapshot.child("gas").getValue(Double.class);

                if (gas != null){
                    smokeOutput.setText("Gas Analog Output: " + String.valueOf(gas) + " %RH");
                }

                if (gas < 200) {
                    smokeStatus.setText("Gas Condition: Normal Air Quality");
                    smokeStatus.setTextColor(ContextCompat.getColor(fragment.getContext(), android.R.color.holo_green_dark));
                } else if (gas < 500) {
                    smokeStatus.setText("Gas Condition: Moderate Gas Level");
                    smokeStatus.setTextColor(ContextCompat.getColor(fragment.getContext(), android.R.color.holo_orange_light));
                } else {
                    smokeStatus.setText("Gas Condition: High Gas PPM! Dangerous!");
                    smokeStatus.setTextColor(ContextCompat.getColor(fragment.getContext(), android.R.color.holo_red_dark));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        humidChart(smokeLineChart, fragment);
    }

    public static void humidChart(LineChart smokeLineChart, Fragment fragment){
        smokeLineChart.getDescription().setEnabled(false);
        smokeLineChart.getLegend().setEnabled(false);

        XAxis xAxis = smokeLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        YAxis leftAxis = smokeLineChart.getAxisLeft();
        leftAxis.setDrawLabels(true);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.WHITE);
        leftAxis.setDrawAxisLine(false);
        smokeLineChart.getAxisRight().setEnabled(false);

        LineDataSet dataSet = new LineDataSet(new ArrayList<>(), "");
        dataSet.setColor(Color.parseColor("#555555"));//Dark Gray
        dataSet.setLineWidth(2f);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);

        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.parseColor("#D3D3D3"));//Light Gray
        dataSet.setFillAlpha(80);

        LineData lineData = new LineData(dataSet);
        smokeLineChart.setData(lineData);

        databaseReference = FirebaseDatabase.getInstance().getReference("sensors");

        databaseReference.addValueEventListener(new ValueEventListener() {

            int index = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (fragment == null || !fragment.isAdded()) return;
                Double humid = snapshot.child("gas").getValue(Double.class);

                if (humid != null){
                    dataSet.addEntry(new Entry(index++, humid.floatValue()));
                    lineData.notifyDataChanged();
                    smokeLineChart.notifyDataSetChanged();
                    smokeLineChart.invalidate();
                    smokeLineChart.moveViewToX(dataSet.getEntryCount());
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
