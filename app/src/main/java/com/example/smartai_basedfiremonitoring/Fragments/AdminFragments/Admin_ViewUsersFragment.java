package com.example.smartai_basedfiremonitoring.Fragments.AdminFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartai_basedfiremonitoring.Adapter.ViewUserAdapter;
import com.example.smartai_basedfiremonitoring.Model.User;
import com.example.smartai_basedfiremonitoring.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Admin_ViewUsersFragment extends Fragment {

    RecyclerView recyclerView;
    ViewUserAdapter adapter;
    List<User> userList = new ArrayList<>();
    List<User> filteredList = new ArrayList<>();
    EditText searchUser;
    DatabaseReference dbRef;

    public Admin_ViewUsersFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout (make sure you create fragment_admin_view_users.xml)
        View view = inflater.inflate(R.layout.fragment_admin_view_users, container, false);

        dbRef = FirebaseDatabase.getInstance().getReference("users");
        recyclerView = view.findViewById(R.id.recyclerView);

        searchUser = view.findViewById(R.id.searchUser);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        filteredList = new ArrayList<>();
        userList = new ArrayList<>();

        adapter = new ViewUserAdapter(filteredList);
        recyclerView.setAdapter(adapter);

        userListView();
        setupSearch();

        return view;
    }

    public void userListView(){
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();

                for (DataSnapshot child : snapshot.getChildren()){
                    User user = child.getValue(User.class);

                    if (user != null && "user".equalsIgnoreCase(user.getRole())){
                        userList.add(user);
                    }
                }
                filteredList.clear();
                filteredList.addAll(userList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void setupSearch() {
        searchUser.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }


    private void filter(String text) {
        filteredList.clear();

        for (User user : userList) {
            if (user.getUsername().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(user);
            }
        }

        adapter.notifyDataSetChanged();
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
