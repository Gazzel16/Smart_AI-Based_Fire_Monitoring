package com.example.smartai_basedfiremonitoring.Gemini;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartai_basedfiremonitoring.R;

import java.util.List;

public class GeminiAdvisoryAdapter extends RecyclerView.Adapter<GeminiAdvisoryAdapter.ViewHolder> {

    private List<GeminiAdvisoryModel> advisoryList;

    public GeminiAdvisoryAdapter(List<GeminiAdvisoryModel> advisoryList) {
        this.advisoryList = advisoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_advisory, parent, false); // custom layout
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GeminiAdvisoryModel advisory = advisoryList.get(position);
        holder.title.setText(advisory.getTitle());
        holder.description.setText(advisory.getDescription());
    }

    @Override
    public int getItemCount() {
        return advisoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvAdvisoryTitle);
            description = itemView.findViewById(R.id.tvAdvisoryDesc);
        }
    }
}
