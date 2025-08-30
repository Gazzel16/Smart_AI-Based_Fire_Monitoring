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

        EmergencyReport(view);

        return  view;
    }

    public void EmergencyReport(View view){
        EditText inputReport = view.findViewById(R.id.inputReport);
        Button reportBtn = view.findViewById(R.id.reportBtn);

        // Firebase reference
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        reportBtn.setOnClickListener(v -> {
            String description = inputReport.getText().toString().trim();

            if (description.isEmpty()){
                Toast.makeText(getContext(), "Pls fill the input", Toast.LENGTH_SHORT).show();
                return;
            }

            if (currentUser != null){

                String userId = currentUser.getUid();
                String reportedId = dbRef.child(userId).child("reports").push().getKey();

                DatabaseReference userRef = dbRef.child(userId);

                userRef.get().addOnSuccessListener(snapShot -> {

                    User user = snapShot.getValue(User.class);

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Manila"));
                    String currentDateTime = simpleDateFormat.format(new Date());

                    if (user != null){
                        String reporterName = user.getUsername();
                        FireReport fireReport = new FireReport(
                                reporterName,
                                false,
                                description,
                                currentDateTime
                        );

                        dbRef.child(userId).child("reports").child(reportedId).setValue(fireReport)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(getContext(), "Reported submitted", Toast.LENGTH_SHORT).show();
                                    inputReport.setText("");
                                }).addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }

                });
            }
        });
    }
}