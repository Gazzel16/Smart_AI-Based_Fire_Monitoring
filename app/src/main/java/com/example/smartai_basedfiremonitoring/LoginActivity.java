package com.example.smartai_basedfiremonitoring;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.smartai_basedfiremonitoring.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {

    EditText usernameEditText, phoneNumberEditText;
    Button logInBtn;

    TextView signUp;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login);


        usernameEditText = findViewById(R.id.username);
        phoneNumberEditText = findViewById(R.id.phoneNumber);
        logInBtn = findViewById(R.id.logInBtn);

        signUp = findViewById(R.id.signUp);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        signUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        logInBtn.setOnClickListener(v -> loginUser());


    }

    private void loginUser() {
        String username = usernameEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();

        // Input validation
        if (username.isEmpty()) {
            usernameEditText.setError("Please enter your username");
            usernameEditText.requestFocus();
            return;
        }

        if (phoneNumber.isEmpty()) {
            phoneNumberEditText.setError("Please enter your phone number");
            phoneNumberEditText.requestFocus();
            return;
        }

        if (!phoneNumber.startsWith("09") || phoneNumber.length() != 11 || !phoneNumber.matches("\\d+")) {
            phoneNumberEditText.setError("Enter a valid 11-digit phone number starting with 09");
            phoneNumberEditText.requestFocus();
            return;
        }

        // Get all users
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean found = false;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    if (user != null &&
                            user.username != null &&
                            user.phoneNumber != null &&
                            user.username.trim().equalsIgnoreCase(username.trim()) &&
                            user.phoneNumber.trim().equals(phoneNumber.trim())) {
                        found = true;
                        break;
                    }
                }

                if (found) {
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    // Navigate to main activity
                   Intent intent = new Intent(LoginActivity.this, UserDashboardActivity.class); // replace with your main activity
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid username or phone number", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}