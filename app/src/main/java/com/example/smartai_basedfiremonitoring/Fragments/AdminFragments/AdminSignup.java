package com.example.smartai_basedfiremonitoring.Fragments.AdminFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.smartai_basedfiremonitoring.Activity.LoginActivity;
import com.example.smartai_basedfiremonitoring.Activity.SignUpActivity;
import com.example.smartai_basedfiremonitoring.Model.User;
import com.example.smartai_basedfiremonitoring.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminSignup extends Fragment {

    EditText usernameEditText, emailEditText, passwordEditText;
    Button registerBtn;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    RadioButton female, male;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_signup, container, false);

        usernameEditText = view.findViewById(R.id.name);
        emailEditText = view.findViewById(R.id.email);
        passwordEditText = view.findViewById(R.id.password);
        registerBtn = view.findViewById(R.id.register);

        female = view.findViewById(R.id.female);
        male = view.findViewById(R.id.male);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();

       registerBtn.setOnClickListener(v -> {
           registerAdmin(view);
       });

        return view;
    }

    public void registerAdmin(View view){
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        final String gender;

        if (male.isChecked()){
            gender = "male";
        }else if (female.isChecked()){
            gender = "female";
        }else {
            Toast.makeText(getContext(), "Please select a gender", Toast.LENGTH_SHORT).show();
            return;
        }

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.endsWith("@gmail.com")) {
            emailEditText.setError("Email must end with `@gmail.com`");
            emailEditText.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()){

                        String uId = mAuth.getCurrentUser().getUid();
                        User user = new User(uId, email, password, gender, "admin");

                        databaseReference.child(uId).setValue(user)
                                .addOnCompleteListener(dbTask ->{

                                    if (dbTask.isSuccessful()){
                                        Toast.makeText(getContext(), "Admin registered successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "Failed to save user data", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        }

                });


    }


}