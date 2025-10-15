package com.example.smartai_basedfiremonitoring.Fragments.UserFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartai_basedfiremonitoring.Adapter.GroupMemberAdapter;
import com.example.smartai_basedfiremonitoring.Model.GroupMemberModel;
import com.example.smartai_basedfiremonitoring.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentHomeDashboard extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_dashboard, container, false);

        return view;
    }
}