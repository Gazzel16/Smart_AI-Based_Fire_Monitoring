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
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FireReportChart {

    public static void fireReportChart(BarChart fireReportChart, View view, Fragment fragment) {

        // === Chart styling (do this once) ===
        XAxis xAxis = fireReportChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"All", "Confirmed", "False"}));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(3, true);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(12f);

        YAxis leftAxis = fireReportChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(3f);
        leftAxis.setDrawGridLines(false);

        fireReportChart.getAxisRight().setEnabled(false);
        fireReportChart.getDescription().setEnabled(false);
        fireReportChart.getLegend().setEnabled(false);

        // === Firebase listener ===
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int allFireReports = 0;
                int confirmedFireReports = 0;
                int falseFireReports = 0;

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    DataSnapshot reportsSnapshot = userSnapshot.child("reports");

                    if (reportsSnapshot.exists()) {
                        for (DataSnapshot reportSnapshot : reportsSnapshot.getChildren()) {
                            Boolean confirmation = reportSnapshot.child("confirmation").getValue(Boolean.class);
                            Boolean falseReport = reportSnapshot.child("falseReport").getValue(Boolean.class);

                            if (confirmation != null && confirmation) {
                                confirmedFireReports++;
                                allFireReports++;
                            }
                            if (falseReport != null && falseReport) {
                                falseFireReports++;
                                allFireReports++;
                            }
                        }
                    }
                }

                // === Build chart data ===
                ArrayList<BarEntry> entries = new ArrayList<>();
                entries.add(new BarEntry(0f, (float) allFireReports));
                entries.add(new BarEntry(1f, (float) confirmedFireReports));
                entries.add(new BarEntry(2f, (float) falseFireReports));

                BarDataSet dataSet = new BarDataSet(entries, "Fire Reports");
                dataSet.setColors(new int[]{
                        Color.parseColor("#FF5722"), // All
                        Color.parseColor("#4CAF50"), // Confirmed
                        Color.parseColor("#F44336")  // False
                });
                dataSet.setValueTextSize(12f);
                dataSet.setValueTextColor(Color.BLACK);

                BarData barData = new BarData(dataSet);
                barData.setBarWidth(0.6f);

                // Update dynamic Y max
                float maxVal = Math.max(1f, Math.max(allFireReports,
                        Math.max(confirmedFireReports, falseFireReports)));
                fireReportChart.getAxisLeft().setAxisMaximum(maxVal + 1f);

                // Update chart
                fireReportChart.setData(barData);
                fireReportChart.notifyDataSetChanged();
                fireReportChart.invalidate();
                fireReportChart.animateY(800);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // no-op
            }
        });
    }
}
