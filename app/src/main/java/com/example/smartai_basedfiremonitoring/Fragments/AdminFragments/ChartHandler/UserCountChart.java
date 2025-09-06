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

        // Y axis styling
        YAxis leftAxis = userCountChart.getAxisLeft();
        leftAxis.setDrawLabels(true);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.WHITE);
        leftAxis.setDrawAxisLine(false);
        userCountChart.getAxisRight().setEnabled(false);

        // Dataset
        BarDataSet dataSet = new BarDataSet(new ArrayList<>(), "");
        int transparentBlue = Color.argb(180, 0, 0, 255);
        dataSet.setColor(transparentBlue);
        dataSet.setDrawValues(true);

        BarData barData = new BarData(dataSet);
        userCountChart.setData(barData);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users");
        ValueEventListener userListener = new ValueEventListener() {
            int index = 0;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (fragment == null || !fragment.isAdded()) return;

                int userCount = 0;
                for (DataSnapshot child : snapshot.getChildren()) {
                    String role = child.child("role").getValue(String.class);
                    if (role != null && role.equalsIgnoreCase("user")) {
                        userCount++;
                    }
                }

                // Add entry for this snapshot update
                dataSet.addEntry(new BarEntry(index++, userCount));
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
