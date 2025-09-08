package com.example.smartai_basedfiremonitoring.Adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartai_basedfiremonitoring.Model.User;
import com.example.smartai_basedfiremonitoring.R;

import java.util.List;

public class ViewUserAdapter extends RecyclerView.Adapter<ViewUserAdapter.UserViewHolder> {

    private List<User> userList;

    public ViewUserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_user, parent, false); // replace with XML filename
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.username.setText("Name: " + user.getUsername());
        holder.email.setText("Email: " + user.getEmail());
        holder.id.setText("ID: " + user.getId());

        if ("Male".equalsIgnoreCase(user.getGender())) {
            holder.gender.setText("Male");
            holder.gender.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#3F51B5")));
        } else if ("Female".equalsIgnoreCase(user.getGender())) {
            holder.gender.setText("Female");
            holder.gender.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E91E63")));
        } else {
            Log.d("gender", "gender not found");
        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView username, email, id, gender;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            username = itemView.findViewById(R.id.username);
            email = itemView.findViewById(R.id.email);
            gender = itemView.findViewById(R.id.gender);
        }
    }

}
