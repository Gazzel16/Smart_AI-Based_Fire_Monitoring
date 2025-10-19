package com.example.smartai_basedfiremonitoring.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartai_basedfiremonitoring.Model.FireLogModel;
import com.example.smartai_basedfiremonitoring.R;

import java.util.List;

public class FireLogAdapter extends RecyclerView.Adapter<FireLogAdapter.ViewHolder> {

    private Context context;
    private List<FireLogModel> activityLogList;

    public FireLogAdapter(Context context, List<FireLogModel> activityLogList) {
        this.context = context;
        this.activityLogList = activityLogList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_fire_log, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FireLogModel log = activityLogList.get(position);
        holder.tvTime.setText(log.getDateTime());
    }

    @Override
    public int getItemCount() {
        return activityLogList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
