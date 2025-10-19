package com.example.smartai_basedfiremonitoring.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartai_basedfiremonitoring.Model.AdminDashBoardModel;
import com.example.smartai_basedfiremonitoring.R;

import java.util.List;

public class AdminDashBoardAdapter extends RecyclerView.Adapter<AdminDashBoardAdapter.FireViewHolder> {

    private Context context;
    private List<AdminDashBoardModel> fireList;

    public AdminDashBoardAdapter(Context context, List<AdminDashBoardModel> fireList) {
        this.context = context;
        this.fireList = fireList;
    }

    @NonNull
    @Override
    public FireViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_dashboard, parent, false); // Replace with your layout name
        return new FireViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FireViewHolder holder, int position) {
        AdminDashBoardModel item = fireList.get(position);
        holder.imageView.setImageResource(item.getImageResId());
        holder.titleText.setText(item.getTitle());
        holder.progressBar.setProgress(item.getProgress());
        holder.countText.setText(item.getCount());
    }

    @Override
    public int getItemCount() {
        return fireList.size();
    }

    public static class FireViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleText;
        ProgressBar progressBar;
        TextView countText;

        public FireViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imagePlaceHolder);
            titleText = itemView.findViewById(R.id.title);
            progressBar = itemView.findViewById(R.id.progressBar);
            countText = itemView.findViewById(R.id.count);
        }
    }
}

