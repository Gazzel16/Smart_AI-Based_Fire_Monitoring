package com.example.smartai_basedfiremonitoring;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

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
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class); // Replace with your main activity
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
