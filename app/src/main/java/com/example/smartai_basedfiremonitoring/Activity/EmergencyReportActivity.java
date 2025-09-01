package com.example.smartai_basedfiremonitoring.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartai_basedfiremonitoring.Model.FireReport;
import com.example.smartai_basedfiremonitoring.Model.User;
import com.example.smartai_basedfiremonitoring.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class EmergencyReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_emergency_report);

        EmergencyReport();
    }

    private void EmergencyReport() {
        EditText inputReport = findViewById(R.id.inputReport);
        Button reportBtn = findViewById(R.id.reportBtn);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        reportBtn.setOnClickListener(v -> {
            String description = inputReport.getText().toString().trim();

            if (description.isEmpty()) {
                Toast.makeText(this, "Pls fill the input", Toast.LENGTH_SHORT).show();
                return;
            }

            if (currentUser != null) {
                String userId = currentUser.getUid();
                String reportedId = dbRef.child(userId).child("reports").push().getKey();

                DatabaseReference userRef = dbRef.child(userId);

                userRef.get().addOnSuccessListener(snapshot -> {
                    User user = snapshot.getValue(User.class);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    sdf.setTimeZone(TimeZone.getTimeZone("Asia/Manila"));
                    String currentDateTime = sdf.format(new Date());

                    if (user != null) {
                        String reporterName = user.getUsername();
                        FireReport fireReport = new FireReport(
                                reportedId,
                                userId,
                                reporterName,
                                false,
                                false,
                                description,
                                currentDateTime
                        );

                        dbRef.child(userId).child("reports").child(reportedId).setValue(fireReport)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Report submitted", Toast.LENGTH_SHORT).show();
                                    inputReport.setText("");

                                    Intent intent = new Intent(this, MainActivity.class);
                                    startActivity(intent);
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                });
            }
        });
    }
}