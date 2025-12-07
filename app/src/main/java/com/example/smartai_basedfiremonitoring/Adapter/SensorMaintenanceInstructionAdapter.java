package com.example.smartai_basedfiremonitoring.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartai_basedfiremonitoring.Model.SensorMaintenanceInstructionModel;
import com.example.smartai_basedfiremonitoring.R;

import java.util.List;

public class SensorMaintenanceInstructionAdapter
        extends RecyclerView.Adapter<SensorMaintenanceInstructionAdapter.ItemViewHolder> {

    private final List<SensorMaintenanceInstructionModel> items;
    private boolean isTagalog = false; // Flag to switch language

    public SensorMaintenanceInstructionAdapter(List<SensorMaintenanceInstructionModel> items) {
        this.items = items;
    }

    // Toggle between English and Tagalog
    public void toggleLanguage() {
        isTagalog = !isTagalog;
        notifyDataSetChanged();
    }

    public boolean isTagalog() {
        return isTagalog;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView instructions;
        ImageView imagePlaceHolder;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            instructions = itemView.findViewById(R.id.instructions);
            imagePlaceHolder = itemView.findViewById(R.id.imagePlaceHolder);
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sensor_instruction, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        SensorMaintenanceInstructionModel item = items.get(position);

        holder.title.setText(item.getTitle());

        // Show English or Tagalog instructions depending on the flag
        List<String> instructionsList = isTagalog ? item.getInstructionsTAG() : item.getInstructionsEN();
        String joinedInstructions = android.text.TextUtils.join("\n", instructionsList);
        holder.instructions.setText(joinedInstructions);

        holder.imagePlaceHolder.setImageResource(item.getImageResId());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
