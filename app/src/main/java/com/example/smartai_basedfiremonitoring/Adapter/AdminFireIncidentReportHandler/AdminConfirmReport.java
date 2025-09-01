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

            // Use updateChildren instead of setValue
            Map<String, Object> updates = new HashMap<>();
            updates.put("confirmation", true);

            dbRef.updateChildren(updates);
        }

    }
