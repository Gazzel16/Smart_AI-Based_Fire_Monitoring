package com.example.smartai_basedfiremonitoring.Activity;


import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;



import androidx.appcompat.app.AppCompatActivity;

import com.example.smartai_basedfiremonitoring.Model.User;
import com.example.smartai_basedfiremonitoring.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class SignUpActivity extends AppCompatActivity {

    EditText usernameEditText, emailEditText, passwordEditText, ageEditText;
    Button signUpBtn;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    RadioButton female, male;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        usernameEditText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        ageEditText = findViewById(R.id.age);

        signUpBtn = findViewById(R.id.signUpBtn);

        female = findViewById(R.id.female);
        male = findViewById(R.id.male);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
         mAuth = FirebaseAuth.getInstance();

        signUpBtn.setOnClickListener(v -> {
            registerUser();
        });
    }
    private void registerUser() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String ageStr = ageEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        final String gender;

        int age = Integer.parseInt(ageStr);
        if (age <= 20){
            Toast.makeText(this, "Age must be 20 bove", Toast.LENGTH_SHORT).show();
            return;
        }

        if (male.isChecked()){
            gender = "male";
        }else if (female.isChecked()){
            gender = "female";
        }else {
            Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show();
            return;
        }

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.endsWith("@gmail.com")) {
            emailEditText.setError("Email must end with `@gmail.com`");
            emailEditText.requestFocus();
            return;
        }

        // ✅ Create account in Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Save extra info in Realtime Database
                        String uid = mAuth.getCurrentUser().getUid();
                        User user = new User(uid, username, email, ageStr, gender,"user");

                        databaseReference.child(uid).setValue(user)
                                .addOnCompleteListener(dbTask -> {
                                    if (dbTask.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(SignUpActivity.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(SignUpActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    }



