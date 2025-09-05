package com.example.smartai_basedfiremonitoring.Sensors;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.example.smartai_basedfiremonitoring.R;
import com.example.smartai_basedfiremonitoring.Utils.SoundManager;
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

public class TempSensor {

    private static DatabaseReference databaseReference;
    private static ValueEventListener tempListener;
    public static void tempMonitoring(TextView timeFireDetected, TextView tempStatus, LineChart tempLineChart, Fragment fragment){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("sensors");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (fragment == null || !fragment.isAdded()) return;
                Double temp = snapshot.child("temperature").getValue(Double.class);

                if (temp != null){
                    timeFireDetected.setText("Analog Output: " + String.valueOf(temp) + " °C");
                }

                if (temp != null && temp > 40) {
                    tempStatus.setText("Temp Condition: Critical Temperature!");
                    tempStatus.setTextColor(fragment.getResources().getColor(android.R.color.holo_red_dark));

                    Context context = fragment.getContext();
                    if (context != null) {
                        SoundManager.getInstance(context).playSound(R.raw.critical_temp_voiceline);
                    }

                    showNotification(fragment.getContext(),
                            "🔥 Critical Temperature Alert!",
                            "Barangay Ilaya Alabang: Temperature reached " + temp + "°C");

                } else if (temp != null && temp > 35) {
                    tempStatus.setText("Temp Condition: High Temperature!");
                    tempStatus.setTextColor(fragment.getResources().getColor(android.R.color.black));

                    Context context = fragment.getContext();
                    if (context != null) {
                        SoundManager.getInstance(context).playSound(R.raw.high_temp_voiceline);
                    }


                    showNotification(fragment.getContext(),
                            " High Temperature!",
                            "Barangay Ilaya Alabang: Temperature reached " + temp + "°C");

                } else if (temp != null) {
                    tempStatus.setText("Temp Condition: Normal");
                    tempStatus.setTextColor(fragment.getResources().getColor(android.R.color.holo_green_dark));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tempChart(tempLineChart, fragment);
    }

    public static void tempChart(LineChart tempLineChart, Fragment fragment) {
        // Chart base setup
        tempLineChart.getDescription().setEnabled(false);
        tempLineChart.getLegend().setEnabled(false);

        // X axis styling
        XAxis xAxis = tempLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        // Y axis styling
        YAxis leftAxis = tempLineChart.getAxisLeft();
        leftAxis.setDrawLabels(true);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.WHITE);
        leftAxis.setDrawAxisLine(false);
        tempLineChart.getAxisRight().setEnabled(false);

        // Dataset
        LineDataSet dataSet = new LineDataSet(new ArrayList<>(), "");
        dataSet.setColor(fragment.getResources().getColor(android.R.color.holo_red_dark));
        dataSet.setLineWidth(2f);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);

// Enable fill under the line
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.RED);   // or use fragment.getResources().getColor(R.color.yourColor)
        dataSet.setFillAlpha(80);


        LineData lineData = new LineData(dataSet);
        tempLineChart.setData(lineData);

        // Firebase listener (only chart update)
        databaseReference = FirebaseDatabase.getInstance().getReference("sensors");
        tempListener = new ValueEventListener() {
            int index = 0; // counter for X-axis

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (fragment == null || !fragment.isAdded()) return;
                Double temp = snapshot.child("temperature").getValue(Double.class);

                if (temp != null) {
                    // Add reading to chart
                    dataSet.addEntry(new Entry(index++, temp.floatValue()));
                    lineData.notifyDataChanged();
                    tempLineChart.notifyDataSetChanged();
                    tempLineChart.invalidate();
                    tempLineChart.moveViewToX(dataSet.getEntryCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };

        databaseReference.addValueEventListener(tempListener);
    }


    public static void removeListener() {
        if (databaseReference != null && tempListener != null) {
            databaseReference.removeEventListener(tempListener);
            tempListener = null;
        }
    }

    private static void showNotification(Context context, String title, String message) {
        String channelId = "alert_channel";

        // Create notification channel (needed for Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Critical Alerts";
            String description = "Notifications for critical sensor alerts";
            int importance = NotificationManager.IMPORTANCE_HIGH; // heads-up
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }


        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.warning_icon) // make sure you have an icon in res/drawable
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH) // heads-up
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true);

        // Show it
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }

}
