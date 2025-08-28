package com.example.smartai_basedfiremonitoring.Sensors;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.example.smartai_basedfiremonitoring.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.smartai_basedfiremonitoring.Utils.SoundManager;

public class FlameSensor {
    public static void flameMonitoring(TextView flameOutput, TextView flameDetector, Fragment fragment){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("sensors");

        databaseReference.addValueEventListener(new ValueEventListener() {
            private MediaPlayer mediaPlayer;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean flameSensor = snapshot.child("flame").getValue(Boolean.class);

                if (flameSensor != null && flameSensor){

                    Context context = fragment.getContext();
                    if (context != null) {
                        SoundManager.getInstance(context).playSound(R.raw.fire_detected_voiceline);
                    }

                    showNotification(fragment.getContext(),
                            "⚠️ Fire Detected",
                            "A fire has been reported in Barangay Ilaya Alabang. Residents in the affected and nearby areas are advised to evacuate immediately to the nearest safe evacuation center. Please bring only essential belongings, assist children, elderly, and persons with disabilities, and follow instructions from local authorities and emergency responders.\n" +
                                    "⚠\uFE0F Stay away from the fire-affected zone for your safety. ");

                }else {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
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
                .setSmallIcon(R.drawable.flame_logo)
                .setContentTitle(title)
                .setContentText(message) // short preview (collapsed state)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message)               // full text when expanded
                        .setBigContentTitle(title)      // optional: larger title when expanded
                        .setSummaryText("Emergency Alert")) // optional: summary line
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setOngoing(true)
                .setAutoCancel(true);

        // Show it
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
