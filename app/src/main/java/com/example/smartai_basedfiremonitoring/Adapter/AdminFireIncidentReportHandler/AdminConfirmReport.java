    package com.example.smartai_basedfiremonitoring.Adapter.AdminFireIncidentReportHandler;


    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;

    import java.util.HashMap;
    import java.util.Map;

    public class AdminConfirmReport {

        public static void confirm(String userId, String reportId) {
            DatabaseReference dbRef = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(userId)
                    .child("reports")
                    .child(reportId);

            DatabaseReference dbFireDetected = FirebaseDatabase.getInstance().getReference("sensors");

            Map<String, Object> fireUpdate = new HashMap<>();
            fireUpdate.put("flame", true);

            // Use updateChildren instead of setValue0
            Map<String, Object> updates = new HashMap<>();
            updates.put("confirmation", true);

            dbFireDetected.updateChildren(fireUpdate);
            dbRef.updateChildren(updates);
        }

    }
