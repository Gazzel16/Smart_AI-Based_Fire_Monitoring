package com.example.smartai_basedfiremonitoring.Activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartai_basedfiremonitoring.Adapter.ViewUserAdapter;
import com.example.smartai_basedfiremonitoring.Model.User;
import com.example.smartai_basedfiremonitoring.R;

import java.util.ArrayList;
import java.util.List;

public class Admin_ViewUsers extends AppCompatActivity {

    RecyclerView recyclerView;
    ViewUserAdapter adapter;
    List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_view_users);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Dummy data (later you can fetch from Firebase/DB/API)
        userList = new ArrayList<>();
        userList.add(new User("1001", "Andrew E", "andrew@gmail.com", "male", "user"));
        userList.add(new User("1001", "Andrew E", "andrew@gmail.com", "male", "user"));
        userList.add(new User("1001", "Andrew E", "andrew@gmail.com", "male", "user"));
        userList.add(new User("1001", "Andrew E", "andrew@gmail.com", "male", "user"));

        adapter = new ViewUserAdapter(userList);
        recyclerView.setAdapter(adapter);
    }
}
