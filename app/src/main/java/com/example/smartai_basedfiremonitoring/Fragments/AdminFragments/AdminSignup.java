package com.example.smartai_basedfiremonitoring.Fragments.AdminFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.smartai_basedfiremonitoring.Activity.LoginActivity;
import com.example.smartai_basedfiremonitoring.Activity.SignUpActivity;
import com.example.smartai_basedfiremonitoring.Model.User;
import com.example.smartai_basedfiremonitoring.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminSignup extends Fragment {

    EditText usernameEditText, emailEditText, passwordEditText, ageEditText;
    Button registerBtn;
    ImageView backBtn;
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
        ageEditText = view.findViewById(R.id.age);
        passwordEditText = view.findViewById(R.id.password);
        registerBtn = view.findViewById(R.id.register);
        backBtn = view.findViewById(R.id.backBtn);

        female = view.findViewById(R.id.female);
        male = view.findViewById(R.id.male);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();

       registerBtn.setOnClickListener(v -> {
           registerAdmin();
       });

        backBtn.setOnClickListener(v -> {
            onDestroy();
            Fragment setting = new AdminSettingsFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, setting)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    public void registerAdmin(){
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String ageStr = ageEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        final String gender;

        int age = Integer.parseInt(ageStr);
        if (age <= 20){
            Toast.makeText(requireContext(), "Age must be 20 bove", Toast.LENGTH_SHORT).show();
            return;
        }

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
                        User user = new User(uId, username, email, ageStr, gender, "admin");

                        databaseReference.child(uId).setValue(user)
                                .addOnCompleteListener(dbTask ->{

                                    if (dbTask.isSuccessful()){
                                        Toast.makeText(getContext(), "Admin registered successfully", Toast.LENGTH_SHORT).show();

                                        usernameEditText.setText("");
                                        emailEditText.setText("");
                                        passwordEditText.setText("");

                                    } else {
                                        Toast.makeText(getContext(), "Failed to save user data", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        } else {
                        // Email already in use or other auth error
                        if (task.getException() != null) {
                            String error = task.getException().getMessage();
                            if (error.contains("email address is already in use")) {
                                Toast.makeText(getContext(), "Email is already taken", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                });


    }
    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            bottomNav.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            bottomNav.setVisibility(View.VISIBLE);
        }
    }

}