package com.example.smartai_basedfiremonitoring;


import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartai_basedfiremonitoring.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class SignUpActivity extends AppCompatActivity {

    EditText usernameEditText, phoneNumberEditText, emailEditText;
    Button signUpBtn;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        usernameEditText = findViewById(R.id.username);
        phoneNumberEditText = findViewById(R.id.phoneNumber);
        emailEditText = findViewById(R.id.email);
        signUpBtn = findViewById(R.id.signUpBtn);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");


        signUpBtn.setOnClickListener(v -> {
            registerUser();
        });
    }
    private void registerUser() {
        String username = usernameEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();

        if (username.isEmpty() || phoneNumber.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill all of the input", Toast.LENGTH_SHORT).show();
            usernameEditText.requestFocus();
            emailEditText.requestFocus();
            phoneNumberEditText.requestFocus();
            return;
        }

        if (!email.endsWith("@gmail.com")){
            emailEditText.setError("Email must end with `@gmail.com`");
            emailEditText.requestFocus();
            return;
        }
        if (!phoneNumber.startsWith("09")){
            phoneNumberEditText.setError("Phone number must start with 09");
            phoneNumberEditText.requestFocus();
            return;
        }

        if (phoneNumber.length() != 11 || !phoneNumber.matches("\\d+")){
            phoneNumberEditText.setError("Phone number must be exactly 11 digits");
            phoneNumberEditText.requestFocus();
            return;
        }

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long userCount = snapshot.getChildrenCount();
                String userId = String.valueOf(1001 + userCount);

                User user = new User(userId, username, phoneNumber, email);

                databaseReference.child(userId).setValue(user).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(SignUpActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                });


                databaseReference.child(userId).setValue(user).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){

                        Toast.makeText(SignUpActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(SignUpActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignUpActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });

        Query phoneQuery = databaseReference.orderByChild("phoneNumber").equalTo(phoneNumber);
        phoneQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Toast.makeText(SignUpActivity.this, "Phone number already signed up", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
