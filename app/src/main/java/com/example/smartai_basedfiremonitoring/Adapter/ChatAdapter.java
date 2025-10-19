package com.example.smartai_basedfiremonitoring.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartai_basedfiremonitoring.Model.ChatBotModel;
import com.example.smartai_basedfiremonitoring.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ChatBotModel> chatList;

    // View types
    private static final int VIEW_TYPE_BOT = 0;
    private static final int VIEW_TYPE_USER = 1;

    public ChatAdapter(Context context, List<ChatBotModel> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @Override
    public int getItemViewType(int position) {
        if (chatList.get(position).isBot()) {
            return VIEW_TYPE_BOT;
        } else {
            return VIEW_TYPE_USER;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_BOT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_bot_chat, parent, false);
            return new BotViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_user_chat, parent, false);
            return new UserViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatBotModel chat = chatList.get(position);

        if (holder instanceof BotViewHolder) {
            ((BotViewHolder) holder).botChat.setText(chat.getMessage());
        } else if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).userChat.setText(chat.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    // --- Bot ViewHolder ---
    public static class BotViewHolder extends RecyclerView.ViewHolder {
        TextView botChat, bot;

        public BotViewHolder(@NonNull View itemView) {
            super(itemView);
            botChat = itemView.findViewById(R.id.botChat);
            bot = itemView.findViewById(R.id.bot);
        }
    }

    // --- User ViewHolder ---
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userChat, user;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userChat = itemView.findViewById(R.id.userChat);
            user = itemView.findViewById(R.id.user);
        }
    }
}
