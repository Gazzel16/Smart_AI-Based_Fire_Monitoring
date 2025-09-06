package com.example.smartai_basedfiremonitoring.Fragments.AdminFragments.ChartHandler;

import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminCountChart {
    public static void adminChart(BarChart adminCountChart, View view, Fragment fragment ){

        // Chart base setup
        adminCountChart.getDescription().setEnabled(false);
        adminCountChart.getLegend().setEnabled(false);

        // X axis styling
        XAxis xAxis = adminCountChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        // Y axis styling
        YAxis leftAxis = adminCountChart.getAxisLeft();
        leftAxis.setDrawLabels(true);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.WHITE);
        leftAxis.setDrawAxisLine(false);
        adminCountChart.getAxisRight().setEnabled(false);

        // Dataset
        BarDataSet dataSet = new BarDataSet(new ArrayList<>(), "");
        int transparentPink = Color.argb(180, 255, 105, 180);
        dataSet.setColor(transparentPink);
        dataSet.setDrawValues(true);

        BarData barData = new BarData(dataSet);
        adminCountChart.setData(barData);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users");
        ValueEventListener userListener = new ValueEventListener() {
            int index = 0;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (fragment == null || !fragment.isAdded()) return;

                int adminCount = 0;
                for (DataSnapshot child : snapshot.getChildren()) {
                    String role = child.child("role").getValue(String.class);
                    if (role != null && role.equalsIgnoreCase("admin")) {
                        adminCount++;
                    }
                }

                // Add entry for this snapshot update
                dataSet.addEntry(new BarEntry(index++, adminCount));
                barData.notifyDataChanged();
                adminCountChart.notifyDataSetChanged();
                adminCountChart.invalidate();
                adminCountChart.moveViewToX(dataSet.getEntryCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        dbRef.addValueEventListener(userListener);

    }
}
