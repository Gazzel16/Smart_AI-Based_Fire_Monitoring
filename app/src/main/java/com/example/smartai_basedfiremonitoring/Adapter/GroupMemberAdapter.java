package com.example.smartai_basedfiremonitoring.Adapter;

import com.example.smartai_basedfiremonitoring.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartai_basedfiremonitoring.Model.GroupMemberModel;


import java.util.List;

public class GroupMemberAdapter extends RecyclerView.Adapter<GroupMemberAdapter.MessageViewHolder> {

    private Context context;
    private List<GroupMemberModel> messageList;

    public GroupMemberAdapter(Context context, List<GroupMemberModel> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.about_item, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        GroupMemberModel item = messageList.get(position);

        holder.imagePlaceHolder.setImageResource(item.getImageResId());
        holder.courseSection.setText("From: " + item.getCourseSection());
        holder.name.setText("Name: " + item.getName());
        holder.description.setText(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        ImageView imagePlaceHolder;
        TextView courseSection, name, description;
        CardView cardView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            imagePlaceHolder = itemView.findViewById(R.id.imagePlaceHolder);
            courseSection = itemView.findViewById(R.id.courseSection);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            cardView = (CardView) itemView;
        }
    }
}
