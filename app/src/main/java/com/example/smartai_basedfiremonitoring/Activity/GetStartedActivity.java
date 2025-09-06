package com.example.smartai_basedfiremonitoring.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.smartai_basedfiremonitoring.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GetStartedActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_get_started);

        Button getStarted = findViewById(R.id.getStarted);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("users");

        if (mAuth.getCurrentUser() != null) {
            checkUserRole(); // navigate based on role
            return; // skip rest of onCreate
        }

        getStarted.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
    private void checkUserRole() {
        String uid = mAuth.getCurrentUser().getUid();

        database.child(uid).child("role").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String role = task.getResult().getValue(String.class);

                if ("user".equals(role)) {
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else if ("admin".equals(role)) {
                    startActivity(new Intent(this, AdminMainActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Unknown role", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Failed to get user role", Toast.LENGTH_SHORT).show();
            }
        });
    }
}