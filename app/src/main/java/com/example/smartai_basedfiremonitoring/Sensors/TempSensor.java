package com.example.smartai_basedfiremonitoring.Sensors;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.example.smartai_basedfiremonitoring.MainActivity;
import com.example.smartai_basedfiremonitoring.R;
import com.example.smartai_basedfiremonitoring.Utils.SoundManager;
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
                MediaPlayer mediaPlayer;
                Double temp = snapshot.child("temperature").getValue(Double.class);

                if (temp != null){
                    tempAnalogOutput.setText(String.valueOf(temp) + " °C");
                }

                if (temp != null && temp > 40) {
                    tempStatus.setText("Critical Temperature!");
                    tempStatus.setTextColor(fragment.getResources().getColor(android.R.color.holo_red_dark));

                    Context context = fragment.getContext();
                    if (context != null) {
                        SoundManager.getInstance(context).playSound(R.raw.critical_temp_voiceline);
                    }

                    showNotification(fragment.getContext(),
                            "🔥 Critical Temperature Alert!",
                            "Barangay Ilaya Alabang: Temperature reached " + temp + "°C");

                } else if (temp != null && temp > 35) {
                    tempStatus.setText("High Temperature!");
                    tempStatus.setTextColor(fragment.getResources().getColor(android.R.color.black));

                    Context context = fragment.getContext();
                    if (context != null) {
                        SoundManager.getInstance(context).playSound(R.raw.high_temp_voiceline);
                    }


                    showNotification(fragment.getContext(),
                            "⚠️ High Temperature!",
                            "Barangay Ilaya Alabang: Temperature reached " + temp + "°C");

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
