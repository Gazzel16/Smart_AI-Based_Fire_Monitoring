package com.example.smartai_basedfiremonitoring.Fragments.AdminFragments.ChartHandler;

import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.smartai_basedfiremonitoring.R;
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

public class UserCountChart {

    public static void userChart(BarChart userCountChart, View view, Fragment fragment ){

        // Chart base setup
        userCountChart.getDescription().setEnabled(false);
        userCountChart.getLegend().setEnabled(false);

        // X axis styling
        XAxis xAxis = userCountChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setTextColor(Color.WHITE);
        // Y axis styling
        YAxis leftAxis = userCountChart.getAxisLeft();
        leftAxis.setDrawLabels(true);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.WHITE);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawAxisLine(false);
        userCountChart.getAxisRight().setEnabled(false);

        ArrayList<BarEntry> entries = new ArrayList<>();

        // Dataset
        BarDataSet dataSet = new BarDataSet(entries, "");;
        dataSet.setColors(
                Color.parseColor("#FFCC80"), // All Users (blue)
                Color.parseColor("#2196F3"), // Male Users (light blue)
                Color.parseColor("#E91E63")  // Female Users (pink)
        );
        dataSet.setBarBorderColor(Color.parseColor("#FFFFFFFF")); // outline color
        dataSet.setBarBorderWidth(2f); // outline thickness
        dataSet.setDrawValues(true);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.6f);
        userCountChart.setData(barData);


        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users");
        ValueEventListener userListener = new ValueEventListener() {
            int index = 0;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (fragment == null || !fragment.isAdded()) return;

                int totalUsers  = 0;
                int maleUsers = 0;
                int femaleUsers = 0;

                for (DataSnapshot child : snapshot.getChildren()) {

                    String role = child.child("role").getValue(String.class);
                    String gender = child.child("gender").getValue(String.class);

                    if (role != null && role.equalsIgnoreCase("user")) {
                        totalUsers++;

                        if (gender != null){
                            if(gender.equalsIgnoreCase("male")){
                                maleUsers++;
                            }else if(gender.equalsIgnoreCase("female")){
                                femaleUsers++;
                            }
                        }
                    }
                }

                // Add entry for this snapshot update
                entries.clear();
                entries.add(new BarEntry(0, totalUsers));
                entries.add(new BarEntry(1, maleUsers));
                entries.add(new BarEntry(2, femaleUsers));

                dataSet.notifyDataSetChanged();
                barData.notifyDataChanged();

                userCountChart.notifyDataSetChanged();
                userCountChart.invalidate();
                userCountChart.moveViewToX(dataSet.getEntryCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        dbRef.addValueEventListener(userListener);

    }
}
