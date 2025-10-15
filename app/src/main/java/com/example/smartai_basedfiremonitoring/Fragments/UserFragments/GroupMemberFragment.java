package com.example.smartai_basedfiremonitoring.Fragments.UserFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.smartai_basedfiremonitoring.Adapter.GroupMemberAdapter;
import com.example.smartai_basedfiremonitoring.Model.GroupMemberModel;
import com.example.smartai_basedfiremonitoring.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class GroupMemberFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_member, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<GroupMemberModel> messageList = new ArrayList<>();

        messageList.add(new GroupMemberModel(R.drawable.fire_fighter, "BSIT4J", "Kurt Agus", "Hi I'm Kurt Agus, I'm a firefighter"));
        messageList.add(new GroupMemberModel(R.drawable.fire_fighter, "BSIT4J", "Kurt Agus", "Hi I'm Kurt Agus, I'm a firefighter"));
        messageList.add(new GroupMemberModel(R.drawable.fire_fighter, "BSIT4J", "Kurt Agus", "Hi I'm Kurt Agus, I'm a firefighter"));

        GroupMemberAdapter adapter = new GroupMemberAdapter(requireContext(), messageList);
        recyclerView.setAdapter(adapter);

        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
        ImageView geminiAdvisory = requireActivity().findViewById(R.id.geminiAdvisory);
        if (bottomNav != null || geminiAdvisory != null) {
            bottomNav.setVisibility(View.GONE);
            geminiAdvisory.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Show bottom navigation again when leaving this fragment
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
        ImageView geminiAdvisory = requireActivity().findViewById(R.id.geminiAdvisory);
        if (bottomNav != null || geminiAdvisory != null) {
            bottomNav.setVisibility(View.VISIBLE);
            geminiAdvisory.setVisibility(View.VISIBLE);
        }
    }
}