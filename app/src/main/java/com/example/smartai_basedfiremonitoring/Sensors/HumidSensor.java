package com.example.smartai_basedfiremonitoring.Sensors;

import android.graphics.Color;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

public class HumidSensor {
    private static DatabaseReference databaseReference;
    private static ValueEventListener humidListener;


    public static void humidMonitoring(TextView humidAnalogOutput, TextView humidStatus, LineChart humidLineChart, Fragment fragment){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("sensors");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (fragment == null || !fragment.isAdded()) return;
                Double humid = snapshot.child("humidity").getValue(Double.class);

                if (humid != null){
                    humidAnalogOutput.setText("Humidity Analog Output: " + String.valueOf(humid) + " %RH");
                }

                if (humid < 30) {
                    humidStatus.setText("Humidity Condition: Low Humidity!");
                    humidStatus.setTextColor(fragment.getResources().getColor(android.R.color.holo_red_dark));
                } else if (humid > 70) {
                    humidStatus.setText("Humidity Condition: High Humidity!");
                    humidStatus.setTextColor(fragment.getResources().getColor(android.R.color.holo_red_dark));
                } else {
                    humidStatus.setText("Humidity Condition: Normal");
                    humidStatus.setTextColor(fragment.getResources().getColor(android.R.color.holo_green_dark));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        humidChart(humidLineChart, fragment);
    }

    public static void humidChart(LineChart humidLineChart, Fragment fragment){
        humidLineChart.getDescription().setEnabled(false);
        humidLineChart.getLegend().setEnabled(false);

        XAxis xAxis = humidLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        YAxis leftAxis = humidLineChart.getAxisLeft();
        leftAxis.setDrawLabels(true);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.WHITE);
        leftAxis.setDrawAxisLine(false);
        humidLineChart.getAxisRight().setEnabled(false);

        LineDataSet dataSet = new LineDataSet(new ArrayList<>(), "");
        dataSet.setColor(fragment.getResources().getColor(android.R.color.holo_blue_dark));
        dataSet.setLineWidth(2f);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);

        dataSet.setDrawFilled(true);
        dataSet.setFillColor(android.R.color.holo_blue_light);
        dataSet.setFillAlpha(80);

        LineData lineData = new LineData(dataSet);
        humidLineChart.setData(lineData);

        databaseReference = FirebaseDatabase.getInstance().getReference("sensors");

        databaseReference.addValueEventListener(new ValueEventListener() {

            int index = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (fragment == null || !fragment.isAdded()) return;
                    Double humid = snapshot.child("humidity").getValue(Double.class);

                    if (humid != null){
                        dataSet.addEntry(new Entry(index++, humid.floatValue()));
                        lineData.notifyDataChanged();
                        humidLineChart.notifyDataSetChanged();
                        humidLineChart.invalidate();
                        humidLineChart.moveViewToX(dataSet.getEntryCount());
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
