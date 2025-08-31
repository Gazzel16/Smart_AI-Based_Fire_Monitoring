package com.example.smartai_basedfiremonitoring.Adapter;

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
        holder.username.setText(user.getUsername());
        holder.email.setText(user.getEmail());
        holder.id.setText(user.getId());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView username, email, id;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            username = itemView.findViewById(R.id.username);
            email = itemView.findViewById(R.id.textView14);
        }
    }

}
