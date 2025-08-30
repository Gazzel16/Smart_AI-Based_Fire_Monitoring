package com.example.smartai_basedfiremonitoring.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartai_basedfiremonitoring.Model.FireReport;
import com.example.smartai_basedfiremonitoring.R;

import java.util.List;

public class FireReportAdapter extends RecyclerView.Adapter<FireReportAdapter.ReportViewHolder> {

    private Context context;
    private List<FireReport> reportList;

    public FireReportAdapter(Context context, List<FireReport> reportList) {
        this.context = context;
        this.reportList = reportList;
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView name, confirmation, description, timeReported;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            confirmation = itemView.findViewById(R.id.confirmation);
            description = itemView.findViewById(R.id.description);
            timeReported = itemView.findViewById(R.id.timeReported);
        }
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fire_update_item, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        FireReport report = reportList.get(position);

        holder.name.setText(report.getReporterName());
        holder.description.setText(report.getDescription());
        holder.timeReported.setText(report.getTimeReported());

        if (report.isConfirmation()) {
            holder.confirmation.setText("Confirmed");
            holder.confirmation.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else {
            holder.confirmation.setText("Not Confirm");
            holder.confirmation.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        }
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}
