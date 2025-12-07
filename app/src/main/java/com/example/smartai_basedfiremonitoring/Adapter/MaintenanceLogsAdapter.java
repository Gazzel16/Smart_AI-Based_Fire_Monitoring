package com.example.smartai_basedfiremonitoring.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartai_basedfiremonitoring.Model.MaintenanceLogsModel;
import com.example.smartai_basedfiremonitoring.R;

import java.util.List;

public class MaintenanceLogsAdapter extends RecyclerView.Adapter<MaintenanceLogsAdapter.ViewHolder> {

    private Context context;
    private List<MaintenanceLogsModel> maintenanceList;

    public MaintenanceLogsAdapter(Context context, List<MaintenanceLogsModel> maintenanceList) {
        this.context = context;
        this.maintenanceList = maintenanceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sensor_maintenance_logs, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MaintenanceLogsModel log = maintenanceList.get(position);

        holder.timeAndDate.setText(log.getLastMaintenance());
    }

    @Override
    public int getItemCount() {
        return maintenanceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeAndDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeAndDate = itemView.findViewById(R.id.timeAndDate);
        }
    }
}
