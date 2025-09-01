package com.example.smartai_basedfiremonitoring.Adapter.AdminFireIncidentReportHandler;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AdminDeclineFalseReport {

    public static void falseReport(String userId, String reportId){

        DatabaseReference dbRef = FirebaseDatabase.
                getInstance()
                .getReference("users")
                .child(userId)
                .child("reports")
                .child(reportId);

        Map<String, Object> updates = new HashMap<>();
        updates.put("falseReport", true);
        updates.put("confirmation", false);

        dbRef.updateChildren(updates);
    }

}
