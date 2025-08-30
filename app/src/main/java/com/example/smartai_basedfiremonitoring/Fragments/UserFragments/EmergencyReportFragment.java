package com.example.smartai_basedfiremonitoring.Fragments.UserFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
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


public class EmergencyReportFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emergency_report, container, false);

        EditText inputReport = view.findViewById(R.id.inputReport);
        Button reportBtn = view.findViewById(R.id.reportBtn);

        // Firebase reference
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Handle button click
        reportBtn.setOnClickListener(v -> {
            String description = inputReport.getText().toString().trim();

            if (description.isEmpty()) {
                Toast.makeText(getContext(), "Please enter details", Toast.LENGTH_SHORT).show();
                return;
            }

            if (currentUser != null) {
                String userId = currentUser.getUid();
                String reportId = dbRef.child(userId).child("reports").push().getKey();

                DatabaseReference userRef = dbRef.child(userId);
                userRef.get().addOnSuccessListener(snapshot -> {
                   User user = snapshot.getValue(User.class);

                    // force timezone to Asia/Manila (Philippines)
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    sdf.setTimeZone(TimeZone.getTimeZone("Asia/Manila"));
                    String currentDateTimePH = sdf.format(new Date());

                   if (user != null){
                       String reporterName = user.getUsername();
                       FireReport fireReport = new FireReport(
                               reporterName,
                               false, // confirmation default
                               description,
                               currentDateTimePH
                       );

                       dbRef.child(userId).child("reports").child(reportId).setValue(fireReport)
                               .addOnSuccessListener(aVoid -> {
                                   Toast.makeText(getContext(), "Report submitted!", Toast.LENGTH_SHORT).show();
                                   inputReport.setText(""); // clear input
                               })
                               .addOnFailureListener(e ->
                                       Toast.makeText(getContext(), "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                               );
                   }
                });

            }
        });


        return  view;
    }
}