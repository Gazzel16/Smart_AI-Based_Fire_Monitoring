    package com.example.smartai_basedfiremonitoring;

    import android.app.NotificationChannel;
    import android.app.NotificationManager;
    import android.os.Build;
    import android.util.Log;

    import androidx.core.app.NotificationCompat;
    import androidx.core.app.NotificationManagerCompat;

    import com.example.smartai_basedfiremonitoring.R;
    import com.google.firebase.messaging.FirebaseMessagingService;
    import com.google.firebase.messaging.RemoteMessage;

    public class MyFirebaseMessagingService extends FirebaseMessagingService {

        private static final String TAG = "MyFirebaseMsgService";

        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {
            Log.d(TAG, "From: " + remoteMessage.getFrom());


            // ✅ If it's a notification payload
            if (remoteMessage.getNotification() != null) {
                showNotification(remoteMessage.getNotification().getTitle(),
                        remoteMessage.getNotification().getBody());
            }

            // ✅ If it's a data payload
            if (remoteMessage.getData().size() > 0) {
                String title = remoteMessage.getData().get("title");
                String body = remoteMessage.getData().get("body");
                showNotification(title, body);
            }
        }

        @Override
        public void onNewToken(String token) {
            Log.d(TAG, "Refreshed token: " + token);
            // TODO: send token to your backend if needed
        }

        private void showNotification(String title, String message) {
            String channelId = "fcm_channel";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        channelId, "FCM Notifications", NotificationManager.IMPORTANCE_HIGH);
                NotificationManager manager = getSystemService(NotificationManager.class);
                if (manager != null) {
                    manager.createNotificationChannel(channel);
                }
            }

            
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.flame_logo)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            NotificationManagerCompat.from(this).notify(
                    (int) System.currentTimeMillis(), builder.build());
        }
    }
