package com.example.smartai_basedfiremonitoring.Fragments.UserFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.smartai_basedfiremonitoring.Fragments.AdminFragments.AdminSettingsFragment;
import com.example.smartai_basedfiremonitoring.Model.User;
import com.example.smartai_basedfiremonitoring.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ProfileFragment extends Fragment {

    private EditText name, email, password, age;
    private Button saveBtn;
    private RadioGroup genderGroup;
    private RadioButton female, male;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        View bottom_navigation = requireActivity().findViewById(R.id.bottom_navigation);
        if (bottom_navigation != null){
            bottom_navigation.setVisibility(View.GONE);
        }

        Profile(view);
        return view;
    }

    public void Profile(View view){
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        age = view.findViewById(R.id.age);
        password = view.findViewById(R.id.password);
        saveBtn = view.findViewById(R.id.saveBtn);
        female = view.findViewById(R.id.female);
        male = view.findViewById(R.id.male);
        genderGroup = view.findViewById(R.id.genderGroup);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        if (currentUser != null){
            String userId = currentUser.getUid();
            databaseReference.child(userId).get().addOnSuccessListener(snapShot -> {
                User user = snapShot.getValue(User.class);

                if (user != null){
                    name.setText(user.getUsername());
                    email.setText(user.getEmail());

                    String gender = user.getGender();
                    if (gender != null){
                        gender = gender.trim().toLowerCase();
                        Log.d("ProfileFragment", "Gender after trim/lowercase: " + gender);
                        if (gender.equals("male")) {
                            genderGroup.check(R.id.male);
                        } else if (gender.equals("female")) {
                            genderGroup.check(R.id.female);
                        }
                    }


                }

            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });

        }

        saveBtn.setOnClickListener(v -> {
            UpdateDetails();
        });

    }

    public void UpdateDetails(){

        String newName = name.getText().toString().trim();
        String newEmail = email.getText().toString().trim();
        String newAgeStr = age.getText().toString().trim();
        String newPassword = password.getText().toString().trim();

        String gender = "";

        int age = Integer.parseInt(newAgeStr);
        if (age <= 20){
            Toast.makeText(requireContext(), "Age must be 20 bove", Toast.LENGTH_SHORT).show();
            return;
        }

        if (male.isChecked()){
            gender = "male";
        }else  if (female.isChecked()){
            gender = "female";
        }else {
            return;
        }

        if (newName.isEmpty() || newEmail.isEmpty()) {
            Toast.makeText(getContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        String userId = currentUser.getUid();
        User updatedUser = new User(userId, newName, newEmail, newAgeStr, gender, "users");

        databaseReference.child(userId).setValue(updatedUser)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Profile updated!", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e ->{
                    Toast.makeText(getContext(), "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        currentUser.updateEmail(newEmail).addOnCompleteListener(task -> {
           if (task.isSuccessful()){
               Toast.makeText(getContext(), "Email updated", Toast.LENGTH_SHORT).show();
           }
        });

        if (!newPassword.isEmpty()) {
            currentUser.updatePassword(newPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Password updated", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        View bottom_navigation = requireActivity().findViewById(R.id.bottom_navigation);
        if (bottom_navigation != null){
            bottom_navigation.setVisibility(View.VISIBLE);
        }
    }
}