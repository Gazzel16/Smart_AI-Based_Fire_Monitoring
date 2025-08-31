package com.example.smartai_basedfiremonitoring.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartai_basedfiremonitoring.Model.FireReport;
import com.example.smartai_basedfiremonitoring.R;

import java.util.List;

public class AdminFireIncidentReportAdapter extends RecyclerView.Adapter<AdminFireIncidentReportAdapter.FireReportViewHolder> {

    private Context context;
    private List<FireReport> reportList;

    public AdminFireIncidentReportAdapter(Context context, List<FireReport> reportList) {
        this.context = context;
        this.reportList = reportList;
    }

    @NonNull
    @Override
    public FireReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fire_incident_admin_item, parent, false);
        return new FireReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FireReportViewHolder holder, int position) {
        FireReport report = reportList.get(position);

        holder.name.setText(report.getReporterName());
        holder.description.setText(report.getDescription());
        holder.timeReported.setText(report.getTimeReported());

        if (report.isConfirmation()) {
            holder.confirmation.setText("Confirmed");
            holder.confirmation.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else {
            holder.confirmation.setText("Not Confirmed");
            holder.confirmation.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        }

        holder.confirmReport.setOnClickListener(v -> {

        });

        holder.falseReport.setOnClickListener(v -> {

        });
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public static class FireReportViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, timeReported, confirmation;
        Button confirmReport, falseReport;

        public FireReportViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            timeReported = itemView.findViewById(R.id.timeReported);
            confirmation = itemView.findViewById(R.id.confirmation);
            confirmReport = itemView.findViewById(R.id.confirmReport);
            falseReport = itemView.findViewById(R.id.falseReport);
        }
    }
}
