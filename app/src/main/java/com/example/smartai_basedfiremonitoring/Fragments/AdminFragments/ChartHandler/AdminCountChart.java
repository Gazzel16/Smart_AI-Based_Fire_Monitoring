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
        xAxis.setTextColor(Color.WHITE);

        // Y axis styling
        YAxis leftAxis = adminCountChart.getAxisLeft();
        leftAxis.setDrawLabels(true);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.WHITE);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawAxisLine(false);
        adminCountChart.getAxisRight().setEnabled(false);

        ArrayList<BarEntry> entries = new ArrayList<>();
        // Dataset
        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColors(
                Color.parseColor("#FFCC80"), // All Admins
                Color.parseColor("#2196F3"), // Male Admins
                Color.parseColor("#EF9A9A")  // Female Admins
        );
        dataSet.setBarBorderColor(Color.parseColor("#FFFFFFFF"));
        dataSet.setBarBorderWidth(2f);
        dataSet.setDrawValues(true);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.6f);
        adminCountChart.setData(barData);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users");
        ValueEventListener userListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (fragment == null || !fragment.isAdded()) return;

                int totalAdmins = 0;
                int maleAdmins = 0;
                int femaleAdmins = 0;

                for (DataSnapshot child : snapshot.getChildren()) {

                    String role = child.child("role").getValue(String.class);
                    String gender = child.child("gender").getValue(String.class);
                    if (role != null && role.equalsIgnoreCase("admin")) {
                        totalAdmins++;

                        if (gender != null) {
                            if (gender.equalsIgnoreCase("male")) {
                                maleAdmins++;
                            } else if (gender.equalsIgnoreCase("female")) {
                                femaleAdmins++;
                            }
                        }
                    }
                }

                // Update chart data
                entries.clear();
                entries.add(new BarEntry(0, totalAdmins));
                entries.add(new BarEntry(1, maleAdmins));
                entries.add(new BarEntry(2, femaleAdmins));

                dataSet.notifyDataSetChanged();
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
