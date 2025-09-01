package com.example.smartai_basedfiremonitoring.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartai_basedfiremonitoring.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    Button logInBtn;
    TextView signUp;
    FirebaseAuth mAuth; // ✅ Firebase Authentication

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        logInBtn = findViewById(R.id.logInBtn);
        signUp = findViewById(R.id.signUp);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            // User is already logged in, go directly to MainActivity
            startActivity(new Intent(this, MainActivity.class));
            finish(); // close login activity
            return; // skip the rest of onCreate
        }

        // Go to sign up page
        signUp.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });

        // Login
        logInBtn.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if(email.equals("admin@gmail.com") && password.equals("123456")){
            Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class); // Replace with your main activity
            startActivity(intent);
        }
        // ✅ Input validation
        if (email.isEmpty()) {
            emailEditText.setError("Please enter your email");
            emailEditText.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Please enter your password");
            passwordEditText.requestFocus();
            return;
        }

        // ✅ Sign in using FirebaseAuth
        // ✅ Sign in using FirebaseAuth
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Login success → now fetch FCM token
                        FirebaseMessaging.getInstance().getToken()
                                .addOnCompleteListener(tokenTask -> {
                                    if (!tokenTask.isSuccessful()) {
                                        Log.w("FCM", "Fetching FCM registration token failed", tokenTask.getException());
                                        return;
                                    }

                                    // Get FCM registration token
                                    String token = tokenTask.getResult();
                                    Log.d("FCM", "User FCM Token: " + token);

                                    // ✅ Save token in Realtime Database
                                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
                                    userRef.child("fcmToken").setValue(token);

                                    // Navigate to main screen
                                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                });
                    } else {
                        // Login failed
                        Toast.makeText(LoginActivity.this,
                                "Login failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
