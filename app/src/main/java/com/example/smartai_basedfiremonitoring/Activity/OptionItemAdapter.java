package com.example.smartai_basedfiremonitoring.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartai_basedfiremonitoring.Model.OptionItemModel;
import com.example.smartai_basedfiremonitoring.R;

import java.util.List;

public class OptionItemAdapter extends RecyclerView.Adapter<OptionItemAdapter.ViewHolder> {

    private Context context;
    private List<OptionItemModel> optionList;
    private OnItemClickListener listener;

    // Interface for click events
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public OptionItemAdapter(Context context, List<OptionItemModel> optionList, OnItemClickListener listener) {
        this.context = context;
        this.optionList = optionList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.settings_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OptionItemModel item = optionList.get(position);
        holder.title.setText(item.getTitle());
        holder.icon.setImageResource(item.getIconResId());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon, arrow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            icon = itemView.findViewById(R.id.imgIcon);
            arrow = itemView.findViewById(R.id.imageView16);
        }
    }
}
