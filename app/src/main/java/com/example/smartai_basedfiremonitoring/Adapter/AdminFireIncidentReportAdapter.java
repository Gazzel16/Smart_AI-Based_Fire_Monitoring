package com.example.smartai_basedfiremonitoring.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartai_basedfiremonitoring.Adapter.AdminFireIncidentReportHandler.AdminConfirmReport;
import com.example.smartai_basedfiremonitoring.Adapter.AdminFireIncidentReportHandler.AdminDeclineFalseReport;
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_fire_incident_admin, parent, false);
        return new FireReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FireReportViewHolder holder, int position) {
        FireReport report = reportList.get(position);

        holder.name.setText(report.getReporterName());
        holder.description.setText(report.getDescription());
        holder.timeReported.setText(report.getTimeReported());

        holder.confirmReport.setOnClickListener(v -> {
            AdminConfirmReport.confirm(report.getUserId(), report.getReportId());
            holder.confirmReport.setTextColor(Color.GRAY);
            holder.confirmReport.setEnabled(false);
            report.setConfirmation(true);
            notifyItemChanged(position);
        });

        holder.falseReport.setOnClickListener(v -> {
            AdminDeclineFalseReport.falseReport(report.getUserId(), report.getReportId());
            holder.falseReport.setTextColor(Color.GRAY);
            holder.falseReport.setEnabled(false);
            report.setFalseReport(true);
            holder.falseReport.setText("False Report");
            notifyItemChanged(position);
        });

        reportConfirmation(holder, report);
    }

    public void reportConfirmation(FireReportViewHolder holder, FireReport report) {
        if (report.isConfirmation()) {

            holder.cardViewFireReport.setVisibility(View.GONE);

        } else if (report.isFalseReport()) {

            holder.cardViewFireReport.setVisibility(View.GONE);

        } else {
            holder.confirmation.setText("Not Confirmed");
            holder.confirmation.setTextColor(context.getResources().getColor(android.R.color.holo_blue_dark));

            // reset buttons to active state
            holder.confirmReport.setEnabled(true);
            holder.confirmReport.setTextColor(Color.WHITE);

            holder.falseReport.setEnabled(true);
            holder.falseReport.setTextColor(Color.WHITE);
            holder.falseReport.setText("Decline"); // or original text
        }
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public static class FireReportViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, timeReported, confirmation;
        Button confirmReport, falseReport;

        CardView cardViewFireReport;

        public FireReportViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            timeReported = itemView.findViewById(R.id.timeReported);
            confirmation = itemView.findViewById(R.id.confirmation);
            confirmReport = itemView.findViewById(R.id.confirmReport);
            falseReport = itemView.findViewById(R.id.falseReport);
            cardViewFireReport = itemView.findViewById(R.id.cardViewFireReport);
        }
    }
}
