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

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FlameSensor {
    private static ValueEventListener flameListener;

    public static void flameMonitoring(TextView timeFireDetected, TextView flameDetector, Fragment fragment) {

        // Flame detected time
        DatabaseReference fireLogsRef = FirebaseDatabase.getInstance().getReference("fire_logs");

        fireLogsRef.limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!fragment.isAdded()) return;

                fragment.requireActivity().runOnUiThread(() -> {
                    if (snapshot.exists()) {
                        String latestTime = "No logs found";

                        // ✅ Get the last entry's string value directly
                        for (DataSnapshot log : snapshot.getChildren()) {
                            latestTime = log.getValue(String.class); // No timestamp field!
                        }

                        timeFireDetected.setText(latestTime);
                    } else {
                        timeFireDetected.setText("Time: No Logs Available");
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FireLogs", "Error fetching fire_logs: " + error.getMessage());
            }
        });

        //Flame Detected
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("sensors");
        flameListener = new ValueEventListener() {
            private MediaPlayer mediaPlayer;
            private boolean alertPlayed = false;

            private android.os.Handler handler = new android.os.Handler();
            private Runnable logRunnable;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean flameSensor = snapshot.child("flame").getValue(Boolean.class);

                // Make sure fragment is still active before updating UI
                if (!fragment.isAdded()) return;

                // Run on UI thread to ensure proper updates
                fragment.requireActivity().runOnUiThread(() -> {


                    if (flameSensor != null && flameSensor) {
                        flameDetector.setText("Flame Detected!");


                        if (!alertPlayed) {  // ✅ Only play once
                            Context context = fragment.getContext();
                            if (context != null) {
                                SoundManager.getInstance(context)
                                        .playSound(R.raw.fire_detected_voiceline);

                                showNotification(context,
                                        "⚠️ Fire Detected",
                                        "A fire has been reported in Barangay Ilaya Alabang. Residents in the affected and nearby areas are advised to evacuate immediately...");

                                sendAlertToBackend();
                            }
                            alertPlayed = true;
                        }

                        if (logRunnable == null) {
                            logRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                    sdf.setTimeZone(TimeZone.getTimeZone("Asia/Manila"));
                                    String currentTime = sdf.format(new Date());
                                    fireLogsRef.push().setValue(currentTime); // ----------------------- NEW: log fire

                                    // Repeat every 30 seconds
                                    handler.postDelayed(this, 30000);
                                }
                            };
                            handler.post(logRunnable);
                        }
                    } else {
                        flameDetector.setText("No Flame Detected!");

                        if (logRunnable != null) {
                            handler.removeCallbacks(logRunnable);
                            logRunnable = null;
                        }

                        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            mediaPlayer = null;
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FlameSensor", "Database error: " + error.getMessage());
            }
        };

        // ✅ Add the listener to Firebase
        databaseReference.addValueEventListener(flameListener);
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

    private static void sendAlertToBackend() {
        new Thread(() -> {
            try {
                URL url = new URL("http://192.168.1.5:5000/send-alert"); // ⬅️ change this
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                JSONObject json = new JSONObject();
                json.put("flameDetected", true);

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(json.toString().getBytes());
                    os.flush();
                }

                int responseCode = conn.getResponseCode();
                Log.d("FlameSensor", "Backend response: " + responseCode);
                conn.disconnect();
            } catch (Exception e) {
                Log.e("FlameSensor", "Error sending to backend", e);
            }
        }).start();
    }
    public static void removeListener() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("sensors");
        if (flameListener != null) {
            databaseReference.removeEventListener(flameListener);
            flameListener = null;
        }
    }
}
